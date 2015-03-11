/**
 * 
 */
package com.sail.cot.service.price.impl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.CotExportRptDao;
import com.sail.cot.dao.sample.CotReportDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotClause;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotContainerType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderEleprice;
import com.sail.cot.domain.CotOrderFittings;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotPrice;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotPriceEleprice;
import com.sail.cot.domain.CotPriceFittings;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotPriceSituation;
import com.sail.cot.domain.CotRptFile;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.CotTypeLv2;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.domain.vo.CotMsgVO;
import com.sail.cot.domain.vo.CotNewPriceVO;
import com.sail.cot.domain.vo.CotPriceVO;
import com.sail.cot.mail.service.MailService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.impl.CotOrderServiceImpl;
import com.sail.cot.service.price.CotPriceService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.sample.CotReportService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ListSort;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;

/**
 * 报价管理模块
 * 
 * @author qh-chzy
 * 
 */
public class CotPriceServiceImpl implements CotPriceService {

	private CotBaseDao priceDao;
	private CotExportRptDao exportRptDao;
	private CotReportDao reportDao;
	private MailService mailService;
	private CotSysLogService sysLogService;
	private CotElementsService cotElementsService;
	private CotCustomerService custService;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	private Logger logger = Log4WebUtil.getLogger(CotPriceServiceImpl.class);
//	private GenAllSeq seq = new GenAllSeq();

	// 得到总记录数
	public Integer getRecordCount(String tableName, String whereStr) {
		String hql = "select count(*) from ";
		if (tableName != null && !"".equals(tableName)) {
			hql = hql + tableName;
			if (whereStr != null && !"".equals(whereStr)) {
				hql += whereStr;
			}
			List<?> list = this.getPriceDao().find(hql);
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}

	// 保存报价单的产品明细
	// 根据type和srcId保存报价配件表和成本表
	public Boolean addPriceDetails(List<?> details) {
		try {
			this.getPriceDao().saveRecords(details);
			List listFitting = new ArrayList();
			List listPrice = new ArrayList();
			String fitPriceNum = ContextUtil.getProperty("remoteaddr.properties","fitPrecision");
			if(fitPriceNum==null){
				fitPriceNum="3";
			}
			for (int i = 0; i < details.size(); i++) {
				CotPriceDetail detail = (CotPriceDetail) details.get(i);
				String type = detail.getType();
				Integer srcId = detail.getSrcId();
				// 从样品来
				if (type.equals("ele") || type.equals("given")) {
					if(type.equals("given")){
						Integer eId = this.getEleIdByEleName(detail.getEleId());
						if(eId!=0){
							srcId=eId;
						}else{
							srcId=0;
						}
					}
					// 获得原配件信息
					String hql = "from CotEleFittings obj where obj.eleId="
							+ srcId;
					List list = this.getPriceDao().find(hql);
					for (int j = 0; j < list.size(); j++) {
						CotEleFittings eleFittings = (CotEleFittings) list
								.get(j);
						CotPriceFittings priceFittings = new CotPriceFittings();
						priceFittings.setPriceDetailId(detail.getId());
						priceFittings.setPriceId(detail.getPriceId());
						priceFittings.setEleId(detail.getEleId());
						priceFittings.setEleName(detail.getEleName());
						// priceFittings.setBoxCount(boxCount);
						priceFittings.setFitNo(eleFittings.getFitNo());
						priceFittings.setFitName(eleFittings.getFitName());
						priceFittings.setFitDesc(eleFittings.getFitDesc());
						priceFittings.setFacId(eleFittings.getFacId());
						priceFittings
								.setFitUseUnit(eleFittings.getFitUseUnit());
						priceFittings.setFitUsedCount(eleFittings
								.getFitUsedCount());
						priceFittings.setFitCount(eleFittings.getFitCount());
						priceFittings.setFitPrice(eleFittings.getFitPrice());
						// priceFittings.setOrderFitCount()
						priceFittings.setFitTotalPrice(eleFittings
								.getFitTotalPrice());
						priceFittings.setFitRemark(eleFittings.getFitRemark());
						priceFittings.setFittingId(eleFittings.getFittingId());
						listFitting.add(priceFittings);
					}
					// 获得原成本信息
					String hqlPrice = "from CotElePrice obj where obj.eleId="
							+ srcId;
					List listTemp = this.getPriceDao().find(hqlPrice);
					for (int j = 0; j < listTemp.size(); j++) {
						CotElePrice elePrice = (CotElePrice) listTemp.get(j);
						CotPriceEleprice priceEleprice = new CotPriceEleprice();

						priceEleprice.setPriceDetailId(detail.getId());
						priceEleprice.setPriceId(detail.getPriceId());
						priceEleprice.setPriceName(elePrice.getPriceName());
						priceEleprice.setPriceUnit(elePrice.getPriceUnit());
						priceEleprice.setPriceAmount(elePrice.getPriceAmount());
						priceEleprice.setRemark(elePrice.getRemark());

						listPrice.add(priceEleprice);
					}
				}
				// 从报价来
				if (type.equals("price")) {
					// 获得原配件信息
					String hql = "from CotPriceFittings obj where obj.priceDetailId="
							+ srcId;
					List list = this.getPriceDao().find(hql);
					for (int j = 0; j < list.size(); j++) {
						CotPriceFittings priceFittings = (CotPriceFittings) list
								.get(j);
						CotPriceFittings clone = (CotPriceFittings) SystemUtil
								.deepClone(priceFittings);
						String temp = "select obj from CotFittings obj,CotPriceFittings p "
							+ "where p.fittingId=obj.id and p.id="
							+ clone.getId()
							+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,"+fitPriceNum+")!=p.fitPrice)";
						List disFits = this.getPriceDao().find(temp);
						if (disFits.size() > 0) {
							CotFittings fittings = (CotFittings) disFits.get(0);
							clone.setFacId(fittings.getFacId());
							BigDecimal b = new BigDecimal(fittings.getFitPrice()
									/ fittings.getFitTrans());
							double f1 = b.setScale(Integer.parseInt(fitPriceNum), BigDecimal.ROUND_HALF_UP)
									.doubleValue();
							clone.setFitPrice(f1);
						}
						clone.setId(null);
						clone.setPriceDetailId(detail.getId());
						clone.setPriceId(detail.getPriceId());
						listFitting.add(clone);
					}

					// 获得原成本信息
					String hqlPrice = "from CotPriceEleprice obj where obj.priceDetailId="
							+ srcId;
					List listTemp = this.getPriceDao().find(hqlPrice);
					for (int j = 0; j < listTemp.size(); j++) {
						CotPriceEleprice eleprice = (CotPriceEleprice) listTemp
								.get(j);
						CotPriceEleprice clone = (CotPriceEleprice) SystemUtil
								.deepClone(eleprice);
						clone.setId(null);
						clone.setPriceDetailId(detail.getId());
						clone.setPriceId(detail.getPriceId());
						listPrice.add(clone);
					}
				}
				// 从订单来
				if (type.equals("order")) {
					// 获得原配件信息
					String hql = "from CotOrderFittings obj where obj.orderDetailId="
							+ srcId;
					List list = this.getPriceDao().find(hql);
					for (int j = 0; j < list.size(); j++) {
						CotOrderFittings orderFittings = (CotOrderFittings) list
								.get(j);
						CotPriceFittings priceFittings = new CotPriceFittings();
						
						String temp = "select obj from CotFittings obj,CotOrderFittings p "
							+ "where p.fittingId=obj.id and p.id="
							+ orderFittings.getId()
							+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,2)!=p.fitPrice)";
						List disFits = this.getPriceDao().find(temp);
						Integer facId=orderFittings.getFacId();
						Double pr=orderFittings.getFitPrice();
						if (disFits.size() > 0) {
							CotFittings fittings = (CotFittings) disFits.get(0);
							facId=fittings.getFacId();
							BigDecimal b = new BigDecimal(fittings.getFitPrice()
									/ fittings.getFitTrans());
							pr = b.setScale(2, BigDecimal.ROUND_HALF_UP)
									.doubleValue();
						}
						priceFittings.setPriceDetailId(detail.getId());
						priceFittings.setPriceId(detail.getPriceId());
						priceFittings.setEleId(detail.getEleId());
						priceFittings.setEleName(detail.getEleName());
						// priceFittings.setBoxCount(boxCount);
						priceFittings.setFitNo(orderFittings.getFitNo());
						priceFittings.setFitName(orderFittings.getFitName());
						priceFittings.setFitDesc(orderFittings.getFitDesc());
						priceFittings.setFacId(facId);
						priceFittings.setFitUseUnit(orderFittings
								.getFitUseUnit());
						priceFittings.setFitUsedCount(orderFittings
								.getFitUsedCount());
						priceFittings.setFitCount(orderFittings.getFitCount());
						priceFittings.setFitPrice(pr);
						// priceFittings.setOrderFitCount()
						priceFittings.setFitTotalPrice(orderFittings
								.getFitTotalPrice());
						priceFittings
								.setFitRemark(orderFittings.getFitRemark());
						priceFittings
								.setFittingId(orderFittings.getFittingId());

						listFitting.add(priceFittings);
					}

					// 获得原成本信息
					String hqlPrice = "from CotOrderEleprice obj where obj.orderDetailId="
							+ srcId;
					List listTemp = this.getPriceDao().find(hqlPrice);
					for (int j = 0; j < listTemp.size(); j++) {
						CotOrderEleprice orderEleprice = (CotOrderEleprice) listTemp
								.get(j);
						CotPriceEleprice priceEleprice = new CotPriceEleprice();
						priceEleprice.setPriceDetailId(detail.getId());
						priceEleprice.setPriceId(detail.getPriceId());
						priceEleprice
								.setPriceName(orderEleprice.getPriceName());
						priceEleprice
								.setPriceUnit(orderEleprice.getPriceUnit());
						priceEleprice.setPriceAmount(orderEleprice
								.getPriceAmount());
						priceEleprice.setRemark(orderEleprice.getRemark());
						listPrice.add(priceEleprice);
					}
				}

			}
			this.getPriceDao().saveRecords(listFitting);
			this.getPriceDao().saveRecords(listPrice);
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("保存报价单的产品明细出错!");
			return false;
		}
	}

	// 更新报价明细的产品
	public Boolean modifyPriceDetails(List<?> detailList) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject) ThreadLocalManager.getCurrentThread()
				.get();
		Integer empId = 0;
		if (ctx != null) {
			cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			empId = cotEmps.getId();
		}
		if (obj != null)
			empId = obj.getEmpId();

		try {
			this.getPriceDao().updateRecords(detailList);
			// // 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改报价单明细成功");
				cotSyslog.setOpModule("price");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return true;
		} catch (Exception e) {
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改报价单明细失败,失败原因:" + e.getMessage());
				cotSyslog.setOpModule("price");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return false;
		}

	}

	// 更新报价单的产品明细
	public Boolean modifyPriceDetail(CotPriceDetail e, String eleProTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 样品编辑时间
		e.setEleAddTime(new Date(System.currentTimeMillis()));
		try {
			if (!"".equals(eleProTime)) {
				// 样品开发时间
				e.setEleProTime(new Date(sdf.parse(eleProTime).getTime()));
			}
		} catch (Exception ex) {
			logger.error("保存开发时间错误!");
			return false;
		}

		HttpSession session = WebContextFactory.get().getSession();
		CotEmps cotEmps = null;
		if (session != null)
			cotEmps = (CotEmps) session.getAttribute("emp");
		if (cotEmps != null) {
			try {
				this.getPriceDao().update(e);
				// 保存到系统日记表
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(cotEmps.getId());
				cotSyslog.setOpMessage("修改报价明细成功");
				cotSyslog.setOpModule("price");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				cotSyslog.setItemNo(e.getEleId());
				sysLogService.addSysLogByObj(cotSyslog);
			} catch (Exception ex) {
				logger.error("修改报价明细错误!");
				// 保存到系统日记表
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(cotEmps.getId());
				cotSyslog.setOpMessage("修改报价明细失败,失败原因:" + ex.getMessage());
				cotSyslog.setOpModule("price");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				cotSyslog.setItemNo(e.getEleId());
				sysLogService.addSysLogByObj(cotSyslog);
				return false;
			}
		}
		return true;
	}

	// 根据报价编号查找报价单信息
	public CotPrice getPriceById(Integer id) {
		CotPrice cotPrice = (CotPrice) this.getPriceDao().getById(
				CotPrice.class, id);
		if (cotPrice != null) {
			cotPrice.setCotPriceDetails(null);
			return cotPrice;
		}
		return null;
	}

	// 根据报价明细编号查找报价明细单信息
	public CotPriceDetail getPriceDetailById(Integer id) {
		CotPriceDetail cotPriceDetail = (CotPriceDetail) this.getPriceDao()
				.getById(CotPriceDetail.class, id);
		cotPriceDetail.setPicImg(null);
		return cotPriceDetail;
	}

	// 根据报价明细货号查找报价明细单信息
	public CotPriceDetail getPriceDetailByEleId(String eleId) {
		String hql = "from CotPriceDetail obj where obj.eleId='" + eleId + "'";
		List<?> list = this.getPriceDao().find(hql);
		if (list.size() != 0) {
			return (CotPriceDetail) list.get(0);
		} else {
			return null;
		}
	}

	// 根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id) {
		CotCustomer cotCustomer = (CotCustomer) this.getPriceDao().getById(
				CotCustomer.class, id);
		if (cotCustomer != null) {
			CotCustomer custClone = (CotCustomer) SystemUtil
					.deepClone(cotCustomer);
			custClone.setPicImg(null);
			custClone.setCustImg(null);
			custClone.setCustomerClaim(null);
			custClone.setCotCustContacts(null);
			custClone.setCustomerVisitedLogs(null);
			return custClone;
		}
		return null;
	}

	// 删除主报价单
	@SuppressWarnings("deprecation")
	public Boolean deletePrices(List<?> ids) {
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		try {
			this.getPriceDao().deleteRecordByIds(ids, "CotPrice");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除主报价单成功");
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (DAOException e) {
			logger.error("删除报价单出错");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除主报价单失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
		return true;
	}

	// 更新主报价单
	public Integer saveOrUpdatePrice(CotPrice cotPrice, String priceTime,
			boolean flag, Map<String, String> map, String liRunCau) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			cotPrice.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
			if (priceTime != null && !"".equals(priceTime)) {
				cotPrice.setPriceTime(new Timestamp(sdf.parse(priceTime)
						.getTime()));// 报价时间
			}
			cotPrice.setAddPerson(cotEmps.getEmpsName());// 操作人

			// 如果状态为通过或不通过时,保存审核人姓名
			if (cotPrice.getPriceStatus() == 1
					|| cotPrice.getPriceStatus() == 2) {
				cotPrice.setCheckPerson(cotEmps.getEmpsName());
			}

			cotPrice.setEmpId(cotEmps.getId());// 操作人编号
			if (cotPrice.getPriceRate() != null && cotPrice.getPriceRate() != 0) {
			} else {
				CotCurrency cotCurrency = (CotCurrency) this.getPriceDao()
						.getById(CotCurrency.class, cotPrice.getCurrencyId());
				cotPrice.setPriceRate(cotCurrency.getCurRate());// 汇率
			}
			List<CotPrice> list = new ArrayList<CotPrice>();
			list.add(cotPrice);
			if (cotPrice.getId() == null) // 新增时更新客户序列号
			{
				// 更新客户序列号
				this.getCustService().updateCustSeqByType(cotPrice.getCustId(),
						"price", cotPrice.getPriceTime().toString());
				// 更新全局序列号
//				SetNoServiceImpl setNoService = new SetNoServiceImpl();
//				setNoService.saveSeq("price", cotPrice.getPriceTime()
//						.toString());
				CotSeqService cotSeqService=new CotSeqServiceImpl();
				System.out.println("==============执行开始cotSeqService.saveSeq");
				cotSeqService.saveCustSeq(cotPrice.getCustId(), "price",cotPrice.getPriceTime().toString());
				
				cotSeqService.saveSeq("price");
				System.out.println("===========================执行结束cotSeqService.saveSeq");
			}
			this.getPriceDao().saveOrUpdateRecords(list);

			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主报价单成功");
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotPrice.getPriceNo());
			sysLogService.addSysLogByObj(cotSyslog);
			// // 如果价格参数改变时,更改所有明细价格
			// if (flag == true) {
			// String hql = "from CotPriceDetail obj where obj.priceId="
			// + cotPrice.getId();
			// List<?> listDetail = this.getPriceDao().find(hql);
			// String eleIds = "";
			// for (int i = 0; i < listDetail.size(); i++) {
			// CotPriceDetail cotPriceDetail = (CotPriceDetail) listDetail
			// .get(i);
			// eleIds += cotPriceDetail.getEleId() + ",";
			// }
			// Map<String, Float[]> mapPrice = this.getNewPrice(eleIds, map,
			// liRunCau);
			// if (mapPrice == null) {
			// return cotPrice.getId();
			// }
			// List<CotPriceDetail> temp = new ArrayList<CotPriceDetail>();
			// for (int i = 0; i < listDetail.size(); i++) {
			// CotPriceDetail cotPriceDetail = (CotPriceDetail) listDetail
			// .get(i);
			// Float[] newPrice = mapPrice.get(cotPriceDetail.getEleId());
			// cotPriceDetail.setPricePrice(newPrice[0]);
			// cotPriceDetail.setLiRun(newPrice[1]);
			// cotPriceDetail.setCurrencyId(cotPrice.getCurrencyId());
			// temp.add(cotPriceDetail);
			// }
			// this.getPriceDao().updateRecords(temp);
			// // 保存到系统日记表
			// CotSyslog obj = new CotSyslog();
			// obj.setEmpId(cotEmps.getId());
			// obj.setOpMessage("修改报价明细单成功");
			// obj.setOpModule("price");
			// obj.setOpTime(new Date(System.currentTimeMillis()));
			// obj.setOpType(2);
			// obj.setItemNo(cotPrice.getPriceNo());
			// sysLogService.addSysLogByObj(cotSyslog);
			// }

			return cotPrice.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存报价单出错！");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主报价单失败:失败原因:" + e.getMessage());
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotPrice.getPriceNo());
			sysLogService.addSysLogByObj(cotSyslog);
			return null;
		}
	}

	// 删除报价单的产品明细
	@SuppressWarnings("deprecation")
	public Boolean deletePriceDetails(HttpServletRequest request,
			List<?> details) {
		CotEmps cotEmps = (CotEmps) request.getSession().getAttribute("emp");
		try {
			//b货号调用历史数据时,如果刚好是同个主单的明细a,如果刚好这条明细需要被删除,
			//则后台action会先调用删除方法,把a删掉,数据库的外键级联删除明细图片,那么b明细保存时,无法找到引用的a图片
			//解决方法是,先判断要删除的订单明细是否被其他订单引用,如果有,则暂时不删除该明细,把该明细的type字段设置为'delete',在保存的action中的最后再删除
			
			// 清除内存数据
			Object temp = SystemUtil.getObjBySession(request.getSession(),
					"price");
			Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) temp;
			//可删除
			List newDetail=new ArrayList();
			if (priceMap != null) {
				//1.先查找要删除的ids在内存中的对应对象
				Map<Integer,String> idRdm = new HashMap<Integer,String>();
				//滞后删除
				List noDetail=new ArrayList();
				for (int i = 0; i < details.size(); i++) {
					Integer id = (Integer) details.get(i);
					boolean flg=false;
					Iterator<?> it = priceMap.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						CotPriceDetail detail = priceMap.get(key);
						if (detail.getId() != null
								&& detail.getId().intValue() == id.intValue()) {
							idRdm.put(detail.getId(), detail.getRdm());
						}
						if(detail.getId()==null && detail.getSrcId().intValue()==id.intValue()){
							flg=true;
						}
					}
					if(flg==false){
						newDetail.add(id);
					}else{
						noDetail.add(id);
					}
				}
				//把该明细的type字段设置为'delete'
				for (int i = 0; i < noDetail.size(); i++) {
					Integer id = (Integer) noDetail.get(i);
					String key = idRdm.get(id);
					CotPriceDetail detail = priceMap.get(key);
					detail.setType("delete");
					this.setMapAction(request.getSession(), key, detail);
				}
				
				for (int i = 0; i < newDetail.size(); i++) {
					Integer id = (Integer) newDetail.get(i);
					Iterator<?> it = priceMap.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						CotPriceDetail detail = priceMap.get(key);
						if (detail.getId() != null
								&& detail.getId().intValue() == id.intValue()) {
							it.remove();
						}
					}
				}
			}
			this.getPriceDao().deleteRecordByIds(newDetail, "CotPriceDetail");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("批量删除报价明细成功");
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (DAOException e) {
			logger.error("删除报价的产品明细异常");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("批量删除报价明细失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
		return true;
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getPriceDao().getRecords(objName);
	}

	// 根据主报价单号找报价明细
	public List<?> getDetailByPriceId(Integer priceId) {
		String hql = "from CotPriceDetail obj where obj.priceId=" + priceId;
		return this.getPriceDao().find(hql);
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getPriceDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询配件厂家
	public Map<?, ?> getFitFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getPriceDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			if (cotFactory.getFactroyTypeidLv1() != null
					&& cotFactory.getFactroyTypeidLv1() == 2) {
				map.put(cotFactory.getId().toString(), cotFactory
						.getShortName());
			}
		}
		return map;
	}

	// 查询所有厂家按简称名排序
	public Map<?, ?> getFactoryNameTreeMap(HttpServletRequest request) {
		// Map<String, String> map = new HashMap<String, String>();
		TreeMap<String, String> map = new TreeMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getPriceDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有客户
	public Map<?, ?> getCusNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getPriceDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map
					.put(cotCustomer.getId().toString(), cotCustomer
							.getFullNameCn());
		}
		return map;
	}

	// 查询所有客户简称
	public Map<?, ?> getCusShortNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getPriceDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer
					.getCustomerShortName());
		}
		return map;
	}

	// 查询所有客户编号
	public Map<?, ?> getCustNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getPriceDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map
					.put(cotCustomer.getId().toString(), cotCustomer
							.getCustomerNo());
		}
		return map;
	}

	// 查询所有客户邮箱
	public Map<?, ?> getCustMailMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getPriceDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer
					.getCustomerEmail());
		}
		return map;
	}

	// 查询所有价格条款
	public Map<?, ?> getClauseMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("clause");
		for (int i = 0; i < list.size(); i++) {
			CotClause cotClause = (CotClause) list.get(i);
			map.put(cotClause.getId().toString(), cotClause.getClauseName());
		}
		return map;
	}

	// 查询所有报价场合
	public Map<?, ?> getSituationMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("situation");
		for (int i = 0; i < list.size(); i++) {
			CotPriceSituation priceSituation = (CotPriceSituation) list.get(i);
			map.put(priceSituation.getId().toString(), priceSituation
					.getSituationName());
		}
		return map;
	}

	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId().toString(), cotCurrency.getCurNameEn());
		}
		return map;
	}

	// 查询所有币种
	public Map<?, ?> getTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getPriceDao().getRecords("CotTypeLv1");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) list.get(i);
			map.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		return map;
	}

	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getPriceDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 查询报价单单号是否存在
	public Integer findIsExistPriceNo(String priceNo, String id) {
		String hql = "select obj.id from CotPrice obj where obj.priceNo='"
				+ priceNo + "'";
		List<?> res = this.getPriceDao().find(hql);
		if (res.size() == 0) {
			return null;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				return null;
			} else {
				return 1;
			}
		}
		return 2;
	}

	// 根据条件查询样品记录
	public List<?> getElementList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getPriceDao().findRecords(queryInfo);
			List<CotElementsVO> list = new ArrayList<CotElementsVO>();
			for (int i = 0; i < records.size(); i++) {
				Object[] obj = (Object[]) records.get(i);
				CotElementsVO cotElementsVO = new CotElementsVO();
				if (obj[0] != null) {
					cotElementsVO.setId((Integer) obj[0]);
				}
				if (obj[1] != null) {
					cotElementsVO.setEleId(obj[1].toString());
				}
				if (obj[2] != null) {
					cotElementsVO.setEleName(obj[2].toString());
				}
				if (obj[3] != null) {
					cotElementsVO.setPicPath(obj[3].toString());
				}
				list.add(cotElementsVO);
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getPriceDao().findRecords(queryInfo,
					CotPrice.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getPriceDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}
	
	// 如果是报价或订单的配件.需要判断该配件厂家或者采购价格是否和配件库一致,如果不同,用配件库的最新数据代替
	public List<?> getNewList(String tableName, List list) {
		String fitPriceNum = ContextUtil.getProperty("remoteaddr.properties","fitPrecision");
		if (tableName.equals("CotPriceFittings")) {
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				CotPriceFittings priceFittings = (CotPriceFittings) it.next();
				String hql = "select obj from CotFittings obj,CotPriceFittings p "
						+ "where p.fittingId=obj.id and p.id="
						+ priceFittings.getId()
						+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,"+fitPriceNum+")!=p.fitPrice)";
				List disFits = this.getPriceDao().find(hql);
				if (disFits.size() > 0) {
					CotFittings fittings = (CotFittings) disFits.get(0);
					priceFittings.setFacId(fittings.getFacId());
					BigDecimal b = new BigDecimal(fittings.getFitPrice()
							/ fittings.getFitTrans());
					double f1 = b.setScale(Integer.parseInt(fitPriceNum), BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					priceFittings.setFitPrice(f1);
				}
			}
		}
		if (tableName.equals("CotOrderFittings")) {
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				CotOrderFittings orderFittings = (CotOrderFittings) it.next();
				String hql = "select obj from CotFittings obj,CotOrderFittings p "
						+ "where p.fittingId=obj.id and p.id="
						+ orderFittings.getId()
						+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,"+fitPriceNum+")!=p.fitPrice)";
				List disFits = this.getPriceDao().find(hql);
				if (disFits.size() > 0) {
					CotFittings fittings = (CotFittings) disFits.get(0);
					orderFittings.setFacId(fittings.getFacId());
					BigDecimal b = new BigDecimal(fittings.getFitPrice()
							/ fittings.getFitTrans());
					double f1 = b.setScale(Integer.parseInt(fitPriceNum), BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					orderFittings.setFitPrice(f1);
				}
			}
		}
		return list;
	}

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getPriceDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getPriceDao().getRecordsCountJDBC(queryInfo);
	}

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId) {
		List<?> list = this.getPriceDao().find(
				"from CotRptFile f where f.rptType=" + rptTypeId);
		return list;
	}

	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价
	@SuppressWarnings("unchecked")
	public CotPriceDetail findDetailByEleId(CotPriceDetail cotPriceDetail,
			Map<?, ?> map, String liRunCau) {

		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 币种
		Object currencyIdObj = map.get("currencyId");
		Integer currencyId = 0;
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}

		// 取得厂家报价的人民币值
		List<?> listCur = this.getDicList("currency");
		Float rmb = 0f;
		if (cotPriceDetail.getPriceFac() != null
				&& cotPriceDetail.getPriceFacUint() != null) {
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == cotPriceDetail.getPriceFacUint()
						.intValue()) {
					Float rate = cur.getCurRate();
					Float fac = cotPriceDetail.getPriceFac();
					rmb = rate * fac;
					break;
				}
			}
		}

		// 计算报价
		// 价格条款
		Object clauseObj = map.get("clauseId");
		Integer clauseId = 0;
		if (clauseObj != null && !"".equals(clauseObj.toString())) {
			clauseId = Integer.parseInt(clauseObj.toString());
		}
		// 利润率
		Float priceProfit = 0f;
		if (cotPriceDetail.getLiRun() != null) {
			priceProfit = cotPriceDetail.getLiRun();
		}
		// FOB费用
		Object priceFobObj = map.get("priceFob");
		Float priceFob = 0f;
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			CotCurrency cotCurrency = (CotCurrency) this.getPriceDao().getById(
					CotCurrency.class, currencyId);
			priceRate = cotCurrency.getCurRate();
		}
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", priceFob.toString());
		evaluator.putVariable("priceProfit", priceProfit.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		// 整柜运费
		Object priceChargeObj = map.get("priceCharge");
		Float priceCharge = 0f;
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Object containerTypeIdObj = map.get("containerTypeId");
		Integer containerTypeId = 0;
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 样品CBM
		Float cbm = cotPriceDetail.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getPriceDao().getRecords(
				"CotContainerType");
		for (int j = 0; j < containerList.size(); j++) {
			CotContainerType cot = (CotContainerType) containerList.get(j);
			if (cot.getId().intValue() == containerTypeId) {
				if (cot.getContainerCube() != null) {
					cube = cot.getContainerCube();
				}
				break;
			}
		}
		// 装箱率
		Long boxObCount = 0l;
		if (cotPriceDetail.getBoxObCount() != null) {
			boxObCount = cotPriceDetail.getBoxObCount();
		}
		evaluator.putVariable("priceCharge", priceCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		if (cotPriceDetail.getTuiLv() != null) {
			evaluator
					.putVariable("tuiLv", cotPriceDetail.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}
		evaluator.putVariable("boxObCount", boxObCount.toString());
		// 保险费率
		Object insureRateObj = map.get("insureRate");
		Float insureRate = 0f;
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Object insureAddRateObj = map.get("insureAddRate");
		Float insureAddRate = 0f;
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());
		float res = 0;
		try {
			// 根据选择的价格条款获得价格公式
			String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
					+ clauseId;
			List<String> cauList = this.getPriceDao().find(cauHql);
			if (cauList.size() == 0) {
				return null;
			}
			String cau = cauList.get(0);
			String result = evaluator.evaluate(cau);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
		} catch (Exception e) {
			res = 0f;
		}
		cotPriceDetail.setPricePrice(res);
		// 最新RMB价
		Float rmbOut = res;
		// 计算利润率
		Evaluator lirun = new Evaluator();
		lirun.putVariable("price", rmbOut.toString());
		lirun.putVariable("rmb", rmb.toString());
		lirun.putVariable("priceFob", priceFob.toString());
		lirun.putVariable("priceProfit", priceProfit.toString());
		lirun.putVariable("priceRate", priceRate.toString());
		lirun.putVariable("priceCharge", priceCharge.toString());
		lirun.putVariable("cbm", cbm.toString());
		lirun.putVariable("cube", cube.toString());
		lirun.putVariable("boxObCount", boxObCount.toString());
		lirun.putVariable("insureRate", insureRate.toString());
		lirun.putVariable("insureAddRate", insureAddRate.toString());
		if (cotPriceDetail.getTuiLv() != null) {
			lirun.putVariable("tuiLv", cotPriceDetail.getTuiLv().toString());
		} else {
			lirun.putVariable("tuiLv", "0");
		}
		float liRes = 0;
		try {
			String li = lirun.evaluate(liRunCau);
			liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
			if (liRes <= -1000) {
				liRes = -999;
			}
			if (liRes >= 1000) {
				liRes = 999;
			}
		} catch (Exception e) {
			liRes = 0;
		}
		cotPriceDetail.setLiRun(liRes);
		return cotPriceDetail;

	}

	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价(excel导入)
	public CotPriceDetail findDetailByEleIdExcel(String rdm,
			boolean isUsePriceOut, Map<?, ?> map, String liRunCau) {
		WebContext ctx = WebContextFactory.get();
		Object temp = ctx.getSession().getAttribute("priceReport");
		CotPriceDetail cotPriceDetail = null;
		if (temp != null) {
			TreeMap<String, CotPriceDetail> mapExcel = (TreeMap<String, CotPriceDetail>) temp;
			cotPriceDetail = mapExcel.get(rdm);
		} else {
			return null;
		}

		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 币种
		Object currencyIdObj = map.get("currencyId");
		Integer currencyId = 0;
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 取得厂家报价的人民币值
		List<?> listCur = this.getDicList("currency");
		Float rmb = 0f;
		if (cotPriceDetail.getPriceFac() != null
				&& cotPriceDetail.getPriceFacUint() != null) {
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == cotPriceDetail.getPriceFacUint()
						.intValue()) {
					Float rate = cur.getCurRate();
					Float fac = cotPriceDetail.getPriceFac();
					rmb = rate * fac;
					break;
				}
			}
		}

		// 计算报价
		// 价格条款
		Object clauseObj = map.get("clauseId");
		Integer clauseId = 0;
		if (clauseObj != null && !"".equals(clauseObj.toString())) {
			clauseId = Integer.parseInt(clauseObj.toString());
		}
		// 利润率
		Float priceProfit = 0f;
		if (cotPriceDetail.getLiRun() != null) {
			priceProfit = cotPriceDetail.getLiRun();
		}
		// FOB费用
		Object priceFobObj = map.get("priceFob");
		Float priceFob = 0f;
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("priceRate");
		Float priceRate = 0f;
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			CotCurrency cotCurrency = (CotCurrency) this.getPriceDao().getById(
					CotCurrency.class, currencyId);
			priceRate = cotCurrency.getCurRate();
		}
		// 整柜运费
		Object priceChargeObj = map.get("priceCharge");
		Float priceCharge = 0f;
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Object containerTypeIdObj = map.get("containerTypeId");
		Integer containerTypeId = 0;
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 样品CBM
		Float cbm = cotPriceDetail.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getPriceDao().getRecords(
				"CotContainerType");
		for (int j = 0; j < containerList.size(); j++) {
			CotContainerType cot = (CotContainerType) containerList.get(j);
			if (cot.getId().intValue() == containerTypeId) {
				if (cot.getContainerCube() != null) {
					cube = cot.getContainerCube();
				}
				break;
			}
		}
		// 装箱率
		Long boxObCount = 0l;
		if (cotPriceDetail.getBoxObCount() != null) {
			boxObCount = cotPriceDetail.getBoxObCount();
		}
		// 保险费率
		Object insureRateObj = map.get("insureRate");
		Float insureRate = 0f;
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Object insureAddRateObj = map.get("insureAddRate");
		Float insureAddRate = 0f;
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}
		// 最新RMB价
		Float rmbOut = 0f;
		if (!isUsePriceOut) {
			Float obj = 0f;
			if (cotPriceDetail.getPriceOutUint() != null
					&& cotPriceDetail.getPriceOut() != null) {
				// 取得厂家报价的人民币值
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == cotPriceDetail
							.getPriceOutUint().intValue()) {
						obj = cotPriceDetail.getPriceOut() * cur.getCurRate();
						break;
					}
				}
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == currencyId.intValue()) {
						rmbOut = obj / cur.getCurRate();
						break;
					}
				}
				cotPriceDetail.setPricePrice(rmbOut);
			} else {
				cotPriceDetail.setPricePrice(0f);
			}
		} else {
			// 根据选择的价格条款获得价格公式
			String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
					+ clauseId;
			List<String> cauList = this.getPriceDao().find(cauHql);
			if (cauList.size() == 0) {
				return null;
			}
			String cau = cauList.get(0);
			// 定义jeavl对象,用于计算字符串公式
			Evaluator evaluator = new Evaluator();
			// 定义FOB公式的变量
			evaluator.putVariable("rmb", rmb.toString());
			evaluator.putVariable("priceFob", priceFob.toString());
			evaluator.putVariable("priceProfit", priceProfit.toString());
			evaluator.putVariable("priceRate", priceRate.toString());
			evaluator.putVariable("priceCharge", priceCharge.toString());
			evaluator.putVariable("cbm", cbm.toString());
			evaluator.putVariable("cube", cube.toString());
			evaluator.putVariable("boxObCount", boxObCount.toString());
			if (cotPriceDetail.getTuiLv() != null) {
				evaluator.putVariable("tuiLv", cotPriceDetail.getTuiLv()
						.toString());
			} else {
				evaluator.putVariable("tuiLv", "0");
			}
			evaluator.putVariable("insureRate", insureRate.toString());
			evaluator.putVariable("insureAddRate", insureAddRate.toString());
			float res = 0;
			try {
				String result = evaluator.evaluate(cau);
				res = Float.parseFloat(result);
			} catch (Exception e) {
				res = 0;
			}
			cotPriceDetail.setPricePrice(res);
			rmbOut = res;
		}

		// 计算利润率
		Evaluator lirun = new Evaluator();
		lirun.putVariable("price", rmbOut.toString());
		lirun.putVariable("rmb", rmb.toString());
		lirun.putVariable("priceFob", priceFob.toString());
		lirun.putVariable("priceProfit", priceProfit.toString());
		lirun.putVariable("priceRate", priceRate.toString());
		lirun.putVariable("priceCharge", priceCharge.toString());
		lirun.putVariable("cbm", cbm.toString());
		lirun.putVariable("cube", cube.toString());
		lirun.putVariable("boxObCount", boxObCount.toString());
		lirun.putVariable("insureRate", insureRate.toString());
		lirun.putVariable("insureAddRate", insureAddRate.toString());
		if (cotPriceDetail.getTuiLv() != null) {
			lirun.putVariable("tuiLv", cotPriceDetail.getTuiLv().toString());
		} else {
			lirun.putVariable("tuiLv", "0");
		}
		float liRes = 0;
		try {
			String li = lirun.evaluate(liRunCau);
			liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
			if (liRes <= -1000) {
				liRes = -999;
			}
			if (liRes >= 1000) {
				liRes = 999;
			}
		} catch (Exception e) {
			liRes = 0;
		}
		cotPriceDetail.setLiRun(liRes);
		return cotPriceDetail;
	}

	// 根据报价明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids) {
		try {
			this.getPriceDao().deleteRecordByIds(ids, "CotPriceDetail");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 查询该单的所有报价明细的产品货号
	public String findEleByPriceId(Integer priceId) {
		String hql = "select obj.eleId from CotPriceDetail obj where obj.priceId="
				+ priceId;
		List<?> list = this.getPriceDao().find(hql);
		String eleIds = "";
		for (int i = 0; i < list.size(); i++) {
			String eleId = (String) list.get(i);
			eleIds += eleId + ",";
		}
		return eleIds;
	}

	// 根据要更改的产品货号字符串 和 价格条件更改单价,再返回这组单价
	@SuppressWarnings("unchecked")
	public Map<String, Float[]> getNewPrice(String rdmIds, Map<?, ?> map,
			String liRunCau) {
		// 修改内存报价对象的值
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotPriceDetail> priceMap = this.getMapAction(session);
		String[] rdmAry = rdmIds.split(",");
		if (rdmAry.length == 0) {
			return null;
		}
		List list = new ArrayList();
		Map<String, Float[]> newMap = new HashMap<String, Float[]>();
		for (int i = 0; i < rdmAry.length; i++) {
			CotPriceDetail cotPriceDetail = priceMap.get(rdmAry[i]
					.toLowerCase());
			if (cotPriceDetail != null) {
				list.add(cotPriceDetail);
			}
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			List<?> listCur = this.getDicList("currency");
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("priceFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("priceCharge");
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Integer containerTypeId = 0;
		Object containerTypeIdObj = map.get("containerTypeId");
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 保险费率
		Float insureRate = 0f;
		Object insureRateObj = map.get("insureRate");
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Float insureAddRate = 0f;
		Object insureAddRateObj = map.get("insureAddRate");
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getPriceDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = null;
		for (int i = 0; i < list.size(); i++) {
			CotPriceDetail priceDetail = (CotPriceDetail) list.get(i);
			// 利润率
			Float priceProfit = 0f;
			if (priceDetail.getLiRun() != null) {
				priceProfit = priceDetail.getLiRun();
			}
			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			Float rmb = 0f;
			if (priceDetail.getPriceFac() != null
					&& priceDetail.getPriceFacUint() != null) {
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == priceDetail.getPriceFacUint()
							.intValue()) {
						Float rate = cur.getCurRate();
						Float fac = priceDetail.getPriceFac();
						rmb = rate * fac;
						break;
					}
				}
			}
			evaluator = new Evaluator();
			// 定义FOB公式的变量
			evaluator.putVariable("rmb", rmb.toString());
			evaluator.putVariable("priceFob", priceFob.toString());
			if (priceDetail.getTuiLv() != null) {
				evaluator.putVariable("tuiLv", priceDetail.getTuiLv()
						.toString());
			} else {
				evaluator.putVariable("tuiLv", "0");
			}
			evaluator.putVariable("priceProfit", priceProfit.toString());
			evaluator.putVariable("priceRate", priceRate.toString());
			// 样品CBM
			Float cbm = priceDetail.getBoxCbm();
			if (cbm == null) {
				cbm = 0f;
			}
			// 柜体积
			Float cube = 0f;
			// List<?> containerList = this.getDicList("container");
			List<?> containerList = this.getPriceDao().getRecords(
					"CotContainerType");
			for (int j = 0; j < containerList.size(); j++) {
				CotContainerType cot = (CotContainerType) containerList.get(j);
				if (cot.getId().intValue() == containerTypeId) {
					if (cot.getContainerCube() != null) {
						cube = cot.getContainerCube();
					}
					break;
				}
			}
			// 装箱率
			Long boxObCount = priceDetail.getBoxObCount();
			if (boxObCount == null) {
				boxObCount = 0l;
			}
			evaluator.putVariable("priceCharge", priceCharge.toString());
			evaluator.putVariable("cbm", cbm.toString());
			evaluator.putVariable("cube", cube.toString());
			evaluator.putVariable("boxObCount", boxObCount.toString());
			evaluator.putVariable("insureRate", insureRate.toString());
			evaluator.putVariable("insureAddRate", insureAddRate.toString());
			Float[] op = new Float[2];
			float res = 0;
			try {
				String result = evaluator.evaluate(cau);
				res = Float.parseFloat(df.format(Float.parseFloat(result)));
			} catch (Exception e) {
				e.printStackTrace();
				res = 0;
			}
			op[0] = res;

			priceDetail.setPricePrice(Float.parseFloat(df.format(res)));
			// 计算利润率
			Evaluator lirun = new Evaluator();
			// 将新价格转成RMB
			Float rmbNow = res;
			lirun.putVariable("price", rmbNow.toString());
			lirun.putVariable("rmb", rmb.toString());
			lirun.putVariable("priceFob", priceFob.toString());
			lirun.putVariable("priceProfit", priceProfit.toString());
			lirun.putVariable("priceRate", priceRate.toString());
			lirun.putVariable("priceCharge", priceCharge.toString());
			lirun.putVariable("cbm", cbm.toString());
			lirun.putVariable("cube", cube.toString());
			lirun.putVariable("boxObCount", boxObCount.toString());
			lirun.putVariable("insureRate", insureRate.toString());
			lirun.putVariable("insureAddRate", insureAddRate.toString());
			if (priceDetail.getTuiLv() != null) {
				lirun.putVariable("tuiLv", priceDetail.getTuiLv().toString());
			} else {
				lirun.putVariable("tuiLv", "0");
			}
			float liRes = 0;
			try {
				String li = lirun.evaluate(liRunCau);
				liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
				if (liRes <= -1000) {
					liRes = -999;
				}
				if (liRes >= 1000) {
					liRes = 999;
				}
				priceDetail.setLiRun(Float.parseFloat(df.format(liRes)));
			} catch (Exception e) {
				e.printStackTrace();
				liRes = 0;
			}
			op[1] = liRes;
			newMap.put(priceDetail.getRdm(), op);
			priceMap.put(priceDetail.getRdm(), priceDetail);
			SystemUtil.setObjBySession(session, priceMap, "price");
		}
		return newMap;
	}

	// 币种换算(修改当前页的价格)
	public Map<String, Float[]> changePrice(Map<String, CotPriceDetail> map,
			String eleIds, Integer currencyId, Integer oldCur) {
		String[] eleAry = eleIds.split(",");
		Map<String, Float[]> newMap = new HashMap<String, Float[]>();
		for (int i = 0; i < eleAry.length; i++) {
			Float[] op = new Float[2];
			// 修改内存map
			CotPriceDetail cotPriceDetail = map.get(eleAry[i].toLowerCase());
			op[0] = this.updatePrice(cotPriceDetail.getPricePrice(), oldCur,
					currencyId);
			cotPriceDetail.setPricePrice(op[0]);
			map.put(eleAry[i].toLowerCase(), cotPriceDetail);
			op[1] = null;
			newMap.put(cotPriceDetail.getEleId(), op);
		}
		return newMap;
	}

	// 根据要更改的产品货号字符串 和 价格条件更改单价,再返回这组单价
	@SuppressWarnings("unchecked")
	public Map<String, Float[]> getNewPriceByCurreny(String eleIds,
			Map<?, ?> map, String liRunCau, Integer oldCur) {

		// 修改内存报价对象的值
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotPriceDetail> priceMap = this.getMapAction(session);
		String[] eleAry = eleIds.split(",");
		if (eleAry.length == 0) {
			return null;
		}
		List list = new ArrayList();
		Map<String, Float[]> newMap = new HashMap<String, Float[]>();
		for (int i = 0; i < eleAry.length; i++) {
			CotPriceDetail cotPriceDetail = priceMap.get(eleAry[i]
					.toLowerCase());
			if (cotPriceDetail != null) {
				list.add(cotPriceDetail);
			}
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		} else {
			// 币种换算(修改当前页的价格)
			return changePrice(priceMap, eleIds, currencyId, oldCur);
		}
		// 利润率
		Float priceProfit = 0f;
		Object priceProfitObj = map.get("priceProfit");
		if (priceProfitObj != null && !"".equals(priceProfitObj.toString())) {
			priceProfit = Float.parseFloat(priceProfitObj.toString());
		}
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			List<?> listCur = this.getDicList("currency");
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("priceFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("priceCharge");
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Integer containerTypeId = 0;
		Object containerTypeIdObj = map.get("containerTypeId");
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 保险费率
		Float insureRate = 0f;
		Object insureRateObj = map.get("insureRate");
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Float insureAddRate = 0f;
		Object insureAddRateObj = map.get("insureAddRate");
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}

		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getPriceDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = null;
		for (int i = 0; i < list.size(); i++) {
			CotPriceDetail priceDetail = (CotPriceDetail) list.get(i);
			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			Float rmb = 0f;
			if (priceDetail.getPriceFac() != null
					&& priceDetail.getPriceFacUint() != null) {
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == priceDetail.getPriceFacUint()
							.intValue()) {
						Float rate = cur.getCurRate();
						Float fac = priceDetail.getPriceFac();
						rmb = rate * fac;
						break;
					}
				}
			}
			evaluator = new Evaluator();
			// 定义FOB公式的变量
			evaluator.putVariable("rmb", rmb.toString());
			evaluator.putVariable("priceFob", priceFob.toString());
			evaluator.putVariable("priceProfit", priceProfit.toString());
			evaluator.putVariable("priceRate", priceRate.toString());
			if (priceDetail.getTuiLv() != null) {
				evaluator.putVariable("tuiLv", priceDetail.getTuiLv()
						.toString());
			} else {
				evaluator.putVariable("tuiLv", "0");
			}
			// 柜体积
			Float cube = 0f;
			// List<?> containerList = this.getDicList("container");
			List<?> containerList = this.getPriceDao().getRecords(
					"CotContainerType");
			for (int j = 0; j < containerList.size(); j++) {
				CotContainerType cot = (CotContainerType) containerList.get(j);
				if (cot.getId().intValue() == containerTypeId) {
					if (cot.getContainerCube() != null) {
						cube = cot.getContainerCube();
					}
					break;
				}
			}

			// 样品CBM
			Float cbm = priceDetail.getBoxCbm();
			if (cbm == null) {
				cbm = 0f;
			}

			// 装箱率
			Long boxObCount = priceDetail.getBoxObCount();
			if (boxObCount == null) {
				boxObCount = 0l;
			}
			evaluator.putVariable("priceCharge", priceCharge.toString());
			evaluator.putVariable("cbm", cbm.toString());
			evaluator.putVariable("cube", cube.toString());
			evaluator.putVariable("boxObCount", boxObCount.toString());
			evaluator.putVariable("insureRate", insureRate.toString());
			evaluator.putVariable("insureAddRate", insureAddRate.toString());
			Float[] op = new Float[2];
			float res = 0;
			try {
				String result = evaluator.evaluate(cau);
				res = Float.parseFloat(df.format(Float.parseFloat(result)));
			} catch (Exception e) {
				res = 0;
			}
			op[0] = res;

			priceDetail.setPricePrice(Float.parseFloat(df.format(res)));
			// 计算利润率
			Evaluator lirun = new Evaluator();
			// 将新价格转成RMB
			Float rmbNow = res;
			lirun.putVariable("price", rmbNow.toString());
			lirun.putVariable("rmb", rmb.toString());
			lirun.putVariable("priceFob", priceFob.toString());
			lirun.putVariable("priceProfit", priceProfit.toString());
			lirun.putVariable("priceRate", priceRate.toString());
			lirun.putVariable("priceCharge", priceCharge.toString());
			lirun.putVariable("cbm", cbm.toString());
			lirun.putVariable("cube", cube.toString());
			lirun.putVariable("boxObCount", boxObCount.toString());
			lirun.putVariable("insureRate", insureRate.toString());
			lirun.putVariable("insureAddRate", insureAddRate.toString());
			if (priceDetail.getTuiLv() != null) {
				lirun.putVariable("tuiLv", priceDetail.getTuiLv().toString());
			} else {
				lirun.putVariable("tuiLv", "0");
			}
			float liRes = 0;
			try {
				String li = lirun.evaluate(liRunCau);
				liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
				if (liRes <= -1000) {
					liRes = -999;
				}
				if (liRes >= 1000) {
					liRes = 999;
				}
				priceDetail.setLiRun(Float.parseFloat(df.format(liRes)));
			} catch (Exception e) {
				liRes = 0;
			}
			op[1] = liRes;
			newMap.put(priceDetail.getEleId(), op);
			priceMap.put(priceDetail.getEleId().toLowerCase(), priceDetail);
			SystemUtil.setObjBySession(session, priceMap, "price");
		}
		return newMap;
	}

	// 根据明细中的利润率代入主单利润率算出最新价格
	@SuppressWarnings("unchecked")
	public Float getNewPriceByLiRun(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotPriceDetail obj = this.getPriceMapValue(rdm);
		if (obj == null) {
			return null;
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		} else {
			return null;
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 利润率
		Float priceProfit = 0f;
		if (obj.getLiRun() != null) {
			priceProfit = obj.getLiRun();
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("priceFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			List<?> listCur = this.getDicList("currency");
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("priceCharge");
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Integer containerTypeId = 0;
		Object containerTypeIdObj = map.get("containerTypeId");
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 保险费率
		Float insureRate = 0f;
		Object insureRateObj = map.get("insureRate");
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Float insureAddRate = 0f;
		Object insureAddRateObj = map.get("insureAddRate");
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getPriceDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 取得厂家报价的人民币值
		List<?> listCur = this.getDicList("currency");
		Float rmb = 0f;
		if (obj.getPriceFac() != null && obj.getPriceFacUint() != null) {
			for (int i = 0; i < listCur.size(); i++) {
				CotCurrency cur = (CotCurrency) listCur.get(i);
				if (cur.getId().intValue() == obj.getPriceFacUint().intValue()) {
					Float rate = cur.getCurRate();
					Float fac = obj.getPriceFac();
					rmb = rate * fac;
					break;
				}
			}
		}

		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", priceFob.toString());
		if (obj.getTuiLv() != null) {
			evaluator.putVariable("tuiLv", obj.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}
		evaluator.putVariable("priceProfit", priceProfit.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getPriceDao().getRecords(
				"CotContainerType");
		for (int j = 0; j < containerList.size(); j++) {
			CotContainerType cot = (CotContainerType) containerList.get(j);
			if (cot.getId().intValue() == containerTypeId) {
				if (cot.getContainerCube() != null) {
					cube = cot.getContainerCube();
				}
				break;
			}
		}
		// 装箱率
		Long boxObCount = obj.getBoxObCount();
		if (boxObCount == null) {
			boxObCount = 0l;
		}
		evaluator.putVariable("priceCharge", priceCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());
		float res = 0;
		try {
			String result = evaluator.evaluate(cau);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
		} catch (Exception e) {
			e.printStackTrace();
			res = 0;
		}
		return res;
	}

	// 根据明细中的退税率算出最新价格
	@SuppressWarnings("unchecked")
	public Float getNewPriceByTuiLv(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotPriceDetail obj = this.getPriceMapValue(rdm);
		if (obj == null) {
			return null;
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		} else {
			return null;
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 利润率
		Float priceProfit = 0f;
		if (obj.getLiRun() != null) {
			priceProfit = obj.getLiRun();
		}
		// 退税率
		Float tuiLv = 0f;
		Object tuiLvObj = map.get("tuiLv");
		if (tuiLvObj != null && !"".equals(tuiLvObj.toString())) {
			tuiLv = Float.parseFloat(tuiLvObj.toString());
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("priceFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			List<?> listCur = this.getDicList("currency");
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("priceCharge");
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Integer containerTypeId = 0;
		Object containerTypeIdObj = map.get("containerTypeId");
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 保险费率
		Float insureRate = 0f;
		Object insureRateObj = map.get("insureRate");
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Float insureAddRate = 0f;
		Object insureAddRateObj = map.get("insureAddRate");
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}

		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getPriceDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return 0f;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		Float rmb = 0f;
		if (obj.getPriceFac() != null && obj.getPriceFacUint() != null) {
			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			for (int i = 0; i < listCur.size(); i++) {
				CotCurrency cur = (CotCurrency) listCur.get(i);
				if (cur.getId().intValue() == obj.getPriceFacUint().intValue()) {
					Float rate = cur.getCurRate();
					Float fac = 0f;
					if (obj.getPriceFac() != null) {
						fac = obj.getPriceFac();
					}
					rmb = rate * fac;
					break;
				}
			}
		}

		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", priceFob.toString());
		evaluator.putVariable("priceProfit", priceProfit.toString());
		evaluator.putVariable("tuiLv", tuiLv.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getPriceDao().getRecords(
				"CotContainerType");
		for (int j = 0; j < containerList.size(); j++) {
			CotContainerType cot = (CotContainerType) containerList.get(j);
			if (cot.getId().intValue() == containerTypeId) {
				if (cot.getContainerCube() != null) {
					cube = cot.getContainerCube();
				}
				break;
			}
		}
		// 装箱率
		Long boxObCount = obj.getBoxObCount();
		if (boxObCount == null) {
			boxObCount = 0l;
		}
		evaluator.putVariable("priceCharge", priceCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());
		float res = 0;
		try {
			String result = evaluator.evaluate(cau);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
		} catch (Exception e) {
			res = 0;
		}
		return res;
	}

	// 根据明细中的生产价算出最新价格
	@SuppressWarnings("unchecked")
	public Float getNewPriceByPriceFac(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotPriceDetail obj = this.getPriceMapValue(rdm);
		if (obj == null) {
			return null;
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		} else {
			return null;
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 利润率
		Float priceProfit = 0f;
		if (obj.getLiRun() != null) {
			priceProfit = obj.getLiRun();
		}
		// 生产价
		Float priceFac = 0f;
		Object priceFacObj = map.get("priceFac");
		if (priceFacObj != null && !"".equals(priceFacObj.toString())) {
			priceFac = Float.parseFloat(priceFacObj.toString());
		}
		// 取得厂家报价的人民币值
		Float rmb = 0f;
		if (obj.getPriceFacUint() != null) {
			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			for (int i = 0; i < listCur.size(); i++) {
				CotCurrency cur = (CotCurrency) listCur.get(i);
				if (cur.getId().intValue() == obj.getPriceFacUint().intValue()) {
					Float rate = cur.getCurRate();
					rmb = rate * priceFac;
					break;
				}
			}
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("priceFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			List<?> listCur = this.getDicList("currency");
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("priceCharge");
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Integer containerTypeId = 0;
		Object containerTypeIdObj = map.get("containerTypeId");
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 保险费率
		Float insureRate = 0f;
		Object insureRateObj = map.get("insureRate");
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Float insureAddRate = 0f;
		Object insureAddRateObj = map.get("insureAddRate");
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getPriceDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();

		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", priceFob.toString());
		evaluator.putVariable("priceProfit", priceProfit.toString());
		if (obj.getTuiLv() != null) {
			evaluator.putVariable("tuiLv", obj.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}
		evaluator.putVariable("priceRate", priceRate.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getPriceDao().getRecords(
				"CotContainerType");
		for (int j = 0; j < containerList.size(); j++) {
			CotContainerType cot = (CotContainerType) containerList.get(j);
			if (cot.getId().intValue() == containerTypeId) {
				if (cot.getContainerCube() != null) {
					cube = cot.getContainerCube();
				}
				break;
			}
		}
		// 装箱率
		Long boxObCount = obj.getBoxObCount();
		if (boxObCount == null) {
			boxObCount = 0l;
		}
		evaluator.putVariable("priceCharge", priceCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());
		Float res = 0f;
		try {
			String result = evaluator.evaluate(cau);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
		} catch (Exception e) {
			e.printStackTrace();
			res = null;
		}
		return res;
	}

	// 根据明细中的生产价币种算出最新价格
	@SuppressWarnings("unchecked")
	public Float getNewPriceByPriceFacUint(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotPriceDetail obj = this.getPriceMapValue(rdm);
		if (obj == null) {
			return null;
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		} else {
			return null;
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 利润率
		Float priceProfit = 0f;
		if (obj.getLiRun() != null) {
			priceProfit = obj.getLiRun();
		}
		// 生产价币种
		Integer priceFacUint = 0;
		Object priceFacUintObj = map.get("priceFacUint");
		if (priceFacUintObj != null && !"".equals(priceFacUintObj.toString())) {
			priceFacUint = Integer.parseInt(priceFacUintObj.toString());
		}
		// 取得厂家报价的人民币值
		Float rmb = 0f;
		if (obj.getPriceFac() != null && obj.getPriceFacUint() != null) {
			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			for (int i = 0; i < listCur.size(); i++) {
				CotCurrency cur = (CotCurrency) listCur.get(i);
				if (cur.getId().intValue() == priceFacUint.intValue()) {
					Float rate = cur.getCurRate();
					Float fac = 0f;
					if (obj.getPriceFac() != null) {
						fac = obj.getPriceFac();
					}
					rmb = rate * fac;
					break;
				}
			}
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("priceFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			List<?> listCur = this.getDicList("currency");
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("priceCharge");
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Integer containerTypeId = 0;
		Object containerTypeIdObj = map.get("containerTypeId");
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 保险费率
		Float insureRate = 0f;
		Object insureRateObj = map.get("insureRate");
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Float insureAddRate = 0f;
		Object insureAddRateObj = map.get("insureAddRate");
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getPriceDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();

		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", priceFob.toString());
		evaluator.putVariable("priceProfit", priceProfit.toString());
		if (obj.getTuiLv() != null) {
			evaluator.putVariable("tuiLv", obj.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}
		evaluator.putVariable("priceRate", priceRate.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getPriceDao().getRecords(
				"CotContainerType");
		for (int j = 0; j < containerList.size(); j++) {
			CotContainerType cot = (CotContainerType) containerList.get(j);
			if (cot.getId().intValue() == containerTypeId) {
				if (cot.getContainerCube() != null) {
					cube = cot.getContainerCube();
				}
				break;
			}
		}
		// 装箱率
		Long boxObCount = obj.getBoxObCount();
		if (boxObCount == null) {
			boxObCount = 0l;
		}
		evaluator.putVariable("priceCharge", priceCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());
		float res = 0;
		try {
			String result = evaluator.evaluate(cau);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
		} catch (Exception e) {
			res = 0;
		}
		return res;
	}

	// 根据产品货号和客户编号查找该产品对该客户的历史报价
	@SuppressWarnings("unchecked")
	public CotNewPriceVO findNewPriceVO(String eleId, Integer cusId) {
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.00");
		CotNewPriceVO cotNewPriceVO = new CotNewPriceVO();
		cotNewPriceVO.setEleId(eleId);
		// 查找最近报价
		String newHql = "select d.pricePrice from CotPrice obj,CotPriceDetail "
				+ "d where obj.id=d.priceId and " + "obj.custId=" + cusId
				+ " and d.eleId='" + eleId
				+ "' order by obj.priceTime desc limit 0,1";
		List<Float> newList = this.getPriceDao().find(newHql);
		if (newList.size() != 0) {
			Float newPrice = newList.get(0);
			cotNewPriceVO.setNewPrice(newPrice);
		} else {
			cotNewPriceVO.setNewPrice(0.0f);
		}

		// 查找平均报价
		String avgHql = "select avg(d.pricePrice) from CotPrice obj,CotPriceDetail "
				+ "d where obj.id=d.priceId and "
				+ "obj.custId="
				+ cusId
				+ " and d.eleId='" + eleId + "'";
		List<Float> avgList = this.getPriceDao().find(avgHql);
		Float avgPrice = avgList.get(0);
		if (avgPrice == null) {
			cotNewPriceVO.setAvgPrice(0.0f);
		} else {
			cotNewPriceVO.setAvgPrice(Float.parseFloat(df.format(avgPrice)));
		}

		// 查找最低和最高报价
		String hql = "select min(d.pricePrice),max(d.pricePrice) from CotPrice obj,CotPriceDetail "
				+ "d where obj.id=d.priceId and "
				+ "obj.custId="
				+ cusId
				+ " and d.eleId='" + eleId + "'";
		List<?> list = this.getPriceDao().find(hql);
		Object[] price = (Object[]) list.get(0);
		if (price[0] == null) {
			cotNewPriceVO.setMinPrice(0.0f);
		} else {
			cotNewPriceVO.setMinPrice((Float) price[0]);
		}

		if (price[1] == null) {
			cotNewPriceVO.setMaxPrice(0.0f);
		} else {
			cotNewPriceVO.setMaxPrice((Float) price[1]);
		}
		return cotNewPriceVO;

	}

	// 判断该产品是否对该用户报价过
	@SuppressWarnings("unchecked")
	public Object[] findIsExistDetail(String eleId, String cusId,
			String clauseId, String pTime) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		java.util.Date ptimeDate = null;
		try {
			 ptimeDate = format1.parse(pTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Object[] rtn = new Object[2];
		Object[] obj = new Object[3];
		obj[0] = new Integer(cusId);
		obj[1] = eleId;
		obj[2] = ptimeDate;
		// 送样
		String hqlGiven = "select obj,p.givenTime from CotGiven p,CotGivenDetail obj,CotCustomer c "
				+ "where obj.givenId=p.id and p.custId=c.id and c.id=?"
				+ " and obj.eleId=?"
				+ " and p.givenTime<=?"
				+ " order by p.givenTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listGiven = this.getPriceDao().queryForLists(hqlGiven, obj);

		// 报价和订单多一个价格条款条件
		Object[] obj2 = new Object[4];
		obj2[0] = new Integer(cusId);;
		obj2[1] = eleId;
		String str = " and p.clauseId=?";
		if ("".equals(clauseId)) {
			obj2[2] = new Integer(0);;
			str = " and p.clauseId is null and 0=?";
		} else {
			obj2[2] = new Integer(clauseId);
		}
		obj2[3] = ptimeDate;
		// 报价
		String hql = "select obj,p.priceTime from CotPrice p,CotPriceDetail obj,CotCustomer c "
				+ "where obj.priceId=p.id and p.custId=c.id and c.id=?"
				+ " and obj.eleId=?"
				+ str
				+ " and p.priceTime<=?"
				+ " order by p.priceTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> list = this.getPriceDao().queryForLists(hql, obj2);
		// 订单
		Object[] obj3 = new Object[4];
		obj3[0] = new Integer(cusId);
		obj3[1] = eleId;
		String str2 = " and p.clauseTypeId=?";
		if ("".equals(clauseId)) {
			obj3[2] = new Integer(0);
			str = " and p.clauseTypeId is null and 0=?";
		} else {
			obj3[2] = new Integer(clauseId);;
		}
		obj3[3] = ptimeDate;
		String hqlOrder = "select obj,p.orderTime from CotOrder p,CotOrderDetail obj,CotCustomer c "
				+ "where obj.orderId=p.id and p.custId=c.id and c.id=?"
				+ " and obj.eleId=?"
				+ str2
				+ " and p.orderTime<=?"
				+ " order by p.orderTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listOrder = this.getPriceDao().queryForLists(hqlOrder, obj3);

		Timestamp priceTime = null;
		Timestamp orderTime = null;
		Timestamp givenTime = null;
		TreeMap sort = new TreeMap();
		if (list.size() > 0) {
			Object[] detail = (Object[]) list.get(0);
			if (detail[1] != null) {
				priceTime = (Timestamp) detail[1];
				sort.put(priceTime.getTime(), 1);
			}
		}
		if (listOrder.size() > 0) {
			Object[] detailOrder = (Object[]) listOrder.get(0);
			if (detailOrder[1] != null) {
				orderTime = (Timestamp) detailOrder[1];
				sort.put(orderTime.getTime(), 2);
			}
		}
		if (listGiven.size() > 0) {
			Object[] detailGiven = (Object[]) listGiven.get(0);
			if (detailGiven[1] != null) {
				givenTime = (Timestamp) detailGiven[1];
				sort.put(givenTime.getTime(), 3);
			}
		}
		if (sort.keySet().size() == 0) {
			return null;
		}
		Integer flag = (Integer) sort.get(sort.lastKey());
		if (flag == 1) {
			Object[] objTemp = (Object[]) list.get(0);
			CotPriceDetail detail = (CotPriceDetail) objTemp[0];
			detail.setPicImg(null);
			// 填充厂家简称
			List<?> facList = this.getPriceDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (detail.getFactoryId() != null
						&& cotFactory.getId().intValue() == detail
								.getFactoryId().intValue()) {
					detail.setFactoryShortName(cotFactory.getShortName());
				}
			}
			rtn[0] = 1;
			rtn[1] = detail;
			return rtn;
		} else if (flag == 2) {
			Object[] objTemp = (Object[]) listOrder.get(0);
			CotOrderDetail detail = (CotOrderDetail) objTemp[0];
			detail.setPicImg(null);
			// 填充厂家简称
			// List<?> facList = getDicList("factory");
			List<?> facList = this.getPriceDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (detail.getFactoryId() != null
						&& cotFactory.getId().intValue() == detail
								.getFactoryId().intValue()) {
					detail.setFactoryShortName(cotFactory.getShortName());
				}
			}
			rtn[0] = 2;
			rtn[1] = detail;
			return rtn;
		} else {
			Object[] objTemp = (Object[]) listGiven.get(0);
			CotGivenDetail detail = (CotGivenDetail) objTemp[0];
			// 填充厂家简称
			// List<?> facList = getDicList("factory");
			List<?> facList = this.getPriceDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (detail.getFactoryId() != null
						&& cotFactory.getId().intValue() == detail
								.getFactoryId().intValue()) {
					detail.setFactoryShortName(cotFactory.getShortName());
				}
			}
			rtn[0] = 3;
			rtn[1] = detail;
			return rtn;
		}

	}

	// 判断该产品货号是否存在
	public Integer findIsExistEleByEleId(String eleId) {
		String hql = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List<?> list = this.getPriceDao().find(hql);
		if (list.size() != 0) {
			return (Integer) list.get(0);
		} else {
			return null;
		}
	}

	// 发邮件
	@SuppressWarnings("deprecation")
	public String saveMail(Map<?, ?> map) {

		String custEmail = map.get("custEmail").toString();
		Integer custId = Integer.parseInt(map.get("custId").toString());
		String custName = map.get("custName").toString();
		Integer priceId = Integer.parseInt(map.get("priceId").toString());
		String priceNo = map.get("priceNo").toString();
		String reportTemple = map.get("reportTemple").toString();
		String printType = map.get("printType").toString();

		WebContext ctx = WebContextFactory.get();
		String rptXMLpath = ctx.getHttpServletRequest().getRealPath("/")
				+ File.separator + reportTemple;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// 设置报价单编号
		paramMap.put("STR_SQL", "obj.PRICE_ID=" + priceId);
		paramMap.put("IMG_PATH", ctx.getHttpServletRequest().getRealPath("/"));
		Object empNo = ctx.getSession().getAttribute("empNo");
		if (empNo == null) {
			return null;

		}
		CotEmps emp = (CotEmps) ctx.getSession().getAttribute("emp");
		String servicePath = ctx.getHttpServletRequest().getRealPath("/")
				+ File.separator + "mailfolder";
		File file = new File(servicePath + File.separator + empNo.toString());
		if (!file.exists()) {
			file.mkdirs();
		}
		// 新建邮件对象
		CotMail cotMail = new CotMail();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String mailPath = "";
		// 导出报价单
		if ("XLS".equals(printType)) {
			// 设置导出路径
			mailPath = empNo.toString() + File.separator
					+ sdf.format(new java.util.Date()) + "_" + priceNo + ".xls";// 附件路径
			String exportPath = servicePath + File.separator + mailPath;// 实际路径
			this.getExportRptDao().exportRptToXLS(rptXMLpath, exportPath,
					paramMap);
		}
		if ("PDF".equals(printType)) {
			// 设置导出路径
			mailPath = empNo.toString() + File.separator
					+ sdf.format(new java.util.Date()) + "_" + priceNo + ".pdf";// 附件路径
			String exportPath = servicePath + File.separator + mailPath;// 实际路径
			this.getExportRptDao().exportRptToPDF(rptXMLpath, exportPath,
					paramMap);
		}
		cotMail.setAddTime(new Timestamp(System.currentTimeMillis()));// 添加时间
		// 客户信息
		cotMail.setToUrl(custEmail);
		cotMail.setCustId(custId);
		cotMail.setToName(custName);
		// 操作人信息
		cotMail.setSendUrl(emp.getEmpsMail());
		cotMail.setEmpId(emp.getId());
		cotMail.setSendName(emp.getEmpsName());
		// 详细配置
		//cotMail.setMailPath(mailPath);// 附件路径
		cotMail.setBody(sdf.format(new java.util.Date()) + "_"
						+ priceNo);// 邮件内容(日期加报价单)
		cotMail.setSubject(sdf.format(new java.util.Date()) + "_"
						+ priceNo);// 主题
		// 保存邮件对象
		this.getPriceDao().create(cotMail);
		// 发送单封带附件的邮件
		// return this.getMailService().sendMimeMail(cotMail);
		return cotMail.getId();
	}

	// 获取邮件对象Id
	@SuppressWarnings("deprecation")
	public String getCotMailId(Map<?, ?> map) {

		String custEmail = map.get("custEmail").toString();
		Integer custId = Integer.parseInt(map.get("custId").toString());
		String custName = map.get("custName").toString();
		Integer priceId = Integer.parseInt(map.get("priceId").toString());
		String priceNo = map.get("priceNo").toString();
		String reportTemple = map.get("reportTemple").toString();
		String printType = map.get("printType").toString();

		WebContext ctx = WebContextFactory.get();
		String rptXMLpath = ctx.getHttpServletRequest().getRealPath("/")
				+ File.separator + reportTemple;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// 设置报价单编号
		paramMap.put("STR_SQL", "ele.PRICE_ID=" + priceId);
		paramMap.put("IMG_PATH", ctx.getHttpServletRequest().getRealPath("/"));
		Object empNo = ctx.getSession().getAttribute("empNo");
		if (empNo == null) {
			return "0";

		}
		CotEmps emp = (CotEmps) ctx.getSession().getAttribute("emp");
		String servicePath = ctx.getHttpServletRequest().getRealPath("/")
				+ File.separator + "mailfolder";
		File file = new File(servicePath + File.separator + empNo.toString());
		if (!file.exists()) {
			file.mkdirs();
		}
		// 新建邮件对象
		CotMail cotMail = new CotMail();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String mailPath = "";
		// 导出报价单
		if ("XLS".equals(printType)) {
			// 设置导出路径
			mailPath = empNo.toString() + File.separator
					+ sdf.format(new java.util.Date()) + "_" + priceNo + ".xls";// 附件路径
			String exportPath = servicePath + File.separator + mailPath;// 实际路径
			this.getExportRptDao().exportRptToXLS(rptXMLpath, exportPath,
					paramMap);
		}
		if ("PDF".equals(printType)) {
			// 设置导出路径
			mailPath = empNo.toString() + File.separator
					+ sdf.format(new java.util.Date()) + "_" + priceNo + ".pdf";// 附件路径
			String exportPath = servicePath + File.separator + mailPath;// 实际路径
			this.getExportRptDao().exportRptToPDF(rptXMLpath, exportPath,
					paramMap);
		}
		cotMail.setAddTime(new Timestamp(System.currentTimeMillis()));// 添加时间
		// 客户信息
		cotMail.setToUrl(custEmail);
		cotMail.setCustId(custId);
		cotMail.setToName(custName);
		// 操作人信息
		cotMail.setSendUrl(emp.getEmpsMail());
		cotMail.setEmpId(emp.getId());
		cotMail.setSendName(emp.getEmpsName());
		cotMail.setBody(sdf.format(new java.util.Date()) + "_"
						+ priceNo);// 邮件内容(日期加报价单)
		cotMail.setSubject(sdf.format(new java.util.Date()) + "_"
						+ priceNo);// 主题
		// 保存邮件对象
		this.getPriceDao().create(cotMail);
		// 发送单封带附件的邮件
		// return this.getMailService().sendMimeMail(cotMail);
		return cotMail.getId();
	}

	// 根据产品货号查找对外报价
	public Float findPriceOutByEleId(String eleId) {
		String hql = "select e.priceOut from CotElementsNew e where lower(e.eleId)='"
				+ eleId.toLowerCase() + "'";
		List<?> list = this.getPriceDao().find(hql);
		if (list.size() != 0) {
			return (Float) list.get(0);
		} else {
			return null;
		}
	}

	// 判断要更新到样品表的明细货号哪些重复
	public Map<String, List<String>> findIsExistInEle(String[] key) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String eles = "";
		List<String> allEleList = new ArrayList<String>();
		List<String> sameList = new ArrayList<String>();
		List<String> disList = new ArrayList<String>();

		for (int i = 0; i < key.length; i++) {
			eles += "'" + key[i] + "',";
			allEleList.add(key[i]);
		}

		String hql = "select obj.eleId from CotElementsNew obj where obj.eleId in ("
				+ eles.substring(0, eles.length() - 1) + ")";
		List<?> list = this.getPriceDao().find(hql);
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			String str = (String) it.next();
			sameList.add(str);
			// 截取掉eles中eleID
			allEleList.remove(str);
		}
		for (int i = 0; i < allEleList.size(); i++) {
			String eleId = (String) allEleList.get(i);
			disList.add(eleId);
		}
		map.put("same", sameList);
		map.put("dis", disList);
		return map;
	}

	// 判断要更新到样品表的明细货号哪些重复
	public List<?> updateEleToDetail(String[] eleAry, String[] rdmAry,
			String eleStr, String boxStr, String otherStr, boolean isPic) {
		String eles = "";
		for (int i = 0; i < eleAry.length; i++) {
			eles += "'" + eleAry[i] + "',";
		}
		String hql = "from CotElementsNew obj where obj.eleId in ("
				+ eles.substring(0, eles.length() - 1) + ")";
		List<?> list = this.getPriceDao().find(hql);

		String eleSql = "from CotElePic obj where obj.fkId=";
		String picSql = "from CotPricePic obj where obj.fkId=";
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			CotElementsNew cotElements = (CotElementsNew) list.get(i);
			map.put(cotElements.getEleId(), cotElements);
		}
		List listNew = new ArrayList();
		List detailPicList = new ArrayList();
		// 去掉不存在样品档案的货号,保留重复的货号
		for (int i = 0; i < eleAry.length; i++) {
			CotElementsNew cotElements = (CotElementsNew) map.get(eleAry[i]);
			if (cotElements != null) {
				CotPriceDetail priceDetail = this.getPriceMapValue(rdmAry[i]);
				CotPriceDetail newDetail = setPriceByTong(priceDetail,
						cotElements, eleStr, boxStr, otherStr);

				if (newDetail.getId() != null) {
					if (isPic) {
						List eleList = this.getPriceDao().find(
								eleSql + cotElements.getId());
						List picList = this.getPriceDao().find(
								picSql + newDetail.getId());
						CotElePic cotElePic = (CotElePic) eleList.get(0);
						CotPricePic cotPricePic = (CotPricePic) picList.get(0);
						cotPricePic.setEleId(newDetail.getEleId());
						cotPricePic.setPicImg(cotElePic.getPicImg());
						cotPricePic.setPicName(cotElePic.getPicName());
						cotPricePic.setPicSize(cotElePic.getPicSize());
						detailPicList.add(cotPricePic);
					}
				} else {
					newDetail.setType("ele");
					newDetail.setSrcId(cotElements.getId());
				}
				this.setPriceMap(rdmAry[i], newDetail);
				listNew.add(newDetail);
			}
		}
		// 同步样品图片到报价(已存在的报价明细直接覆盖图片)
		if (detailPicList.size() > 0) {
			this.getPriceDao().updateRecords(detailPicList);
		}
		return listNew;
	}

	// 根据同步选择项同步更新样品
	public CotElementsNew setEleByTong(CotElementsNew old,
			CotPriceDetail newEle, String eleStr, String boxStr, String otherStr) {
		old.setEleId(newEle.getEleId());

		if (eleStr != null && !"".equals(eleStr)) {
			String[] eleAry = eleStr.split(",");
			for (int i = 0; i < eleAry.length; i++) {
				if (eleAry[i].equals("eleTypeidLv1")) {
					old.setEleTypeidLv1(newEle.getEleTypeidLv1());
				} else if (eleAry[i].equals("eleUnit")) {
					old.setEleUnit(newEle.getEleUnit());
				} else if (eleAry[i].equals("eleFlag")) {
					old.setEleFlag(newEle.getEleFlag());
				} else if (eleAry[i].equals("eleUnitNum")) {
					old.setEleUnitNum(newEle.getEleUnitNum());
				} else if (eleAry[i].equals("eleNameEn")) {
					old.setEleNameEn(newEle.getEleNameEn());
				} else if (eleAry[i].equals("factoryId")) {
					old.setFactoryId(newEle.getFactoryId());
				} else if (eleAry[i].equals("factoryNo")) {
					old.setFactoryNo(newEle.getFactoryNo());
				} else if (eleAry[i].equals("eleName")) {
					old.setEleName(newEle.getEleName());
				} else if (eleAry[i].equals("eleFrom")) {
					old.setEleFrom(newEle.getEleFrom());
				} else if (eleAry[i].equals("eleCol")) {
					old.setEleCol(newEle.getEleCol());
				} else if (eleAry[i].equals("eleProTime")) {
					old.setEleProTime(newEle.getEleProTime());
				} else if (eleAry[i].equals("eleForPerson")) {
					old.setEleForPerson(newEle.getEleForPerson());
				} else if (eleAry[i].equals("eleGrade")) {
					old.setEleGrade(newEle.getEleGrade());
				} else if (eleAry[i].equals("eleMod")) {
					old.setEleMod(newEle.getEleMod());
				} else if (eleAry[i].equals("eleTypeidLv2")) {
					old.setEleTypeidLv2(newEle.getEleTypeidLv2());
				} else if (eleAry[i].equals("eleTypenameLv2")) {
					old.setEleTypenameLv2(newEle.getEleTypenameLv2());
				} else if (eleAry[i].equals("eleHsid")) {
					old.setEleHsid(newEle.getEleHsid());
				} else if (eleAry[i].equals("boxWeigth")) {
					old.setBoxWeigth(newEle.getBoxWeigth());
				} else if (eleAry[i].equals("cube")) {
					old.setCube(newEle.getCube());
				} else if (eleAry[i].equals("priceFac")) {
					old.setPriceFac(newEle.getPriceFac());
					old.setPriceFacUint(newEle.getPriceFacUint());
				} else if (eleAry[i].equals("priceOut")) {
					old.setPriceOut(newEle.getPricePrice());
					old.setPriceOutUint(newEle.getCurrencyId());
				} else if (eleAry[i].equals("tuiLv")) {
					old.setTuiLv(newEle.getTuiLv());
				} else if (eleAry[i].equals("liRun")) {
					old.setLiRun(newEle.getLiRun());
				} else if (eleAry[i].equals("barcode")) {
					old.setBarcode(newEle.getBarcode());
				} else if (eleAry[i].equals("eleDesc")) {
					old.setEleDesc(newEle.getEleDesc());
				} else if (eleAry[i].equals("eleRemark")) {
					old.setEleRemark(newEle.getEleRemark());
				}
			}
		}

		if (boxStr != null && !"".equals(boxStr)) {
			String[] boxAry = boxStr.split(",");
			for (int i = 0; i < boxAry.length; i++) {
				if (boxAry[i].equals("boxL")) {
					old.setBoxL(newEle.getBoxL());
					old.setBoxLInch(newEle.getBoxLInch());
				} else if (boxAry[i].equals("boxW")) {
					old.setBoxW(newEle.getBoxW());
					old.setBoxWInch(newEle.getBoxWInch());
				} else if (boxAry[i].equals("boxH")) {
					old.setBoxH(newEle.getBoxH());
					old.setBoxHInch(newEle.getBoxHInch());
				} else if (boxAry[i].equals("boxTypeId")) {
					old.setBoxTypeId(newEle.getBoxTypeId());
				} else if (boxAry[i].equals("boxPbL")) {
					old.setBoxPbL(newEle.getBoxPbL());
					old.setBoxPbLInch(newEle.getBoxPbLInch());
				} else if (boxAry[i].equals("boxPbW")) {
					old.setBoxPbW(newEle.getBoxPbW());
					old.setBoxPbWInch(newEle.getBoxPbWInch());
				} else if (boxAry[i].equals("boxPbH")) {
					old.setBoxPbH(newEle.getBoxPbH());
					old.setBoxPbHInch(newEle.getBoxPbHInch());
				} else if (boxAry[i].equals("boxPbCount")) {
					old.setBoxPbCount(newEle.getBoxPbCount());
				} else if (boxAry[i].equals("boxPbTypeId")) {
					old.setBoxPbTypeId(newEle.getBoxPbTypeId());
				} else if (boxAry[i].equals("boxPbPrice")) {
					old.setBoxPbPrice(newEle.getBoxPbPrice());
				} else if (boxAry[i].equals("boxIbL")) {
					old.setBoxIbL(newEle.getBoxIbL());
					old.setBoxIbLInch(newEle.getBoxIbLInch());
				} else if (boxAry[i].equals("boxIbW")) {
					old.setBoxIbW(newEle.getBoxIbW());
					old.setBoxIbWInch(newEle.getBoxIbWInch());
				} else if (boxAry[i].equals("boxIbH")) {
					old.setBoxIbH(newEle.getBoxIbH());
					old.setBoxIbHInch(newEle.getBoxIbHInch());
				} else if (boxAry[i].equals("boxIbCount")) {
					old.setBoxIbCount(newEle.getBoxIbCount());
				} else if (boxAry[i].equals("boxIbTypeId")) {
					old.setBoxIbTypeId(newEle.getBoxIbTypeId());
				} else if (boxAry[i].equals("boxIbPrice")) {
					old.setBoxIbPrice(newEle.getBoxIbPrice());
				} else if (boxAry[i].equals("boxMbL")) {
					old.setBoxMbL(newEle.getBoxMbL());
					old.setBoxMbLInch(newEle.getBoxMbLInch());
				} else if (boxAry[i].equals("boxMbW")) {
					old.setBoxMbW(newEle.getBoxMbW());
					old.setBoxMbWInch(newEle.getBoxMbWInch());
				} else if (boxAry[i].equals("boxMbH")) {
					old.setBoxMbH(newEle.getBoxMbH());
					old.setBoxMbHInch(newEle.getBoxMbHInch());
				} else if (boxAry[i].equals("boxMbCount")) {
					old.setBoxMbCount(newEle.getBoxMbCount());
				} else if (boxAry[i].equals("boxMbTypeId")) {
					old.setBoxMbTypeId(newEle.getBoxMbTypeId());
				} else if (boxAry[i].equals("boxMbPrice")) {
					old.setBoxMbPrice(newEle.getBoxMbPrice());
				} else if (boxAry[i].equals("boxObL")) {
					old.setBoxObL(newEle.getBoxObL());
					old.setBoxObLInch(newEle.getBoxObLInch());
				} else if (boxAry[i].equals("boxObW")) {
					old.setBoxObW(newEle.getBoxObW());
					old.setBoxObWInch(newEle.getBoxObWInch());
				} else if (boxAry[i].equals("boxObH")) {
					old.setBoxObH(newEle.getBoxObH());
					old.setBoxObHInch(newEle.getBoxObHInch());
				} else if (boxAry[i].equals("boxObCount")) {
					old.setBoxObCount(newEle.getBoxObCount());
				} else if (boxAry[i].equals("boxObTypeId")) {
					old.setBoxObTypeId(newEle.getBoxObTypeId());
				} else if (boxAry[i].equals("boxObPrice")) {
					old.setBoxObPrice(newEle.getBoxObPrice());
				} else if (boxAry[i].equals("eleSizeDesc")) {
					old.setEleSizeDesc(newEle.getEleSizeDesc());
				} else if (boxAry[i].equals("eleInchDesc")) {
					old.setEleInchDesc(newEle.getEleInchDesc());
				} else if (boxAry[i].equals("packingPrice")) {
					old.setPackingPrice(newEle.getPackingPrice());
				} else if (boxAry[i].equals("boxCbm")) {
					old.setBoxCbm(newEle.getBoxCbm());
					old.setBoxCuft(newEle.getBoxCuft());
				} else if (boxAry[i].equals("boxNetWeigth")) {
					old.setBoxGrossWeigth(newEle.getBoxGrossWeigth());
					old.setBoxNetWeigth(newEle.getBoxNetWeigth());
				} else if (boxAry[i].equals("boxRemark")) {
					old.setBoxRemark(newEle.getBoxRemark());
				}else if (boxAry[i].equals("boxRemarkCn")) {
					old.setBoxRemarkCn(newEle.getBoxRemarkCn());
				} else if (boxAry[i].equals("boxTDesc")) {
					old.setBoxTDesc(newEle.getBoxTDesc());
				} else if (boxAry[i].equals("boxBDesc")) {
					old.setBoxBDesc(newEle.getBoxBDesc());
				}
			}
		}

		if (otherStr != null && !"".equals(otherStr)) {
			String[] otherAry = otherStr.split(",");
			for (int i = 0; i < otherAry.length; i++) {
				if (otherAry[i].equals("box20Count")) {
					old.setBox20Count(newEle.getBox20Count());
				} else if (otherAry[i].equals("box40Count")) {
					old.setBox40Count(newEle.getBox40Count());
				} else if (otherAry[i].equals("box40hqCount")) {
					old.setBox40hqCount(newEle.getBox40hqCount());
				} else if (otherAry[i].equals("box45Count")) {
					old.setBox45Count(newEle.getBox45Count());
				}
			}
		}
		old.setChilds(null);
		old.setCotFile(null);
		old.setCotPictures(null);
		old.setCotPriceFacs(null);
		old.setCotEleFittings(null);
		old.setCotElePrice(null);
		old.setCotElePacking(null);
		return old;
	}

	// 根据同步选择项同步更新报价
	public CotPriceDetail setPriceByTong(CotPriceDetail old,
			CotElementsNew newEle, String eleStr, String boxStr, String otherStr) {
		old.setEleId(newEle.getEleId());

		if (eleStr != null && !"".equals(eleStr)) {
			String[] eleAry = eleStr.split(",");
			for (int i = 0; i < eleAry.length; i++) {
				if (eleAry[i].equals("eleTypeidLv1")) {
					old.setEleTypeidLv1(newEle.getEleTypeidLv1());
				} else if (eleAry[i].equals("eleUnit")) {
					old.setEleUnit(newEle.getEleUnit());
				} else if (eleAry[i].equals("eleFlag")) {
					old.setEleFlag(newEle.getEleFlag());
				} else if (eleAry[i].equals("eleUnitNum")) {
					old.setEleUnitNum(newEle.getEleUnitNum());
				} else if (eleAry[i].equals("eleNameEn")) {
					old.setEleNameEn(newEle.getEleNameEn());
				} else if (eleAry[i].equals("factoryId")) {
					old.setFactoryId(newEle.getFactoryId());
				} else if (eleAry[i].equals("factoryNo")) {
					old.setFactoryNo(newEle.getFactoryNo());
				} else if (eleAry[i].equals("eleName")) {
					old.setEleName(newEle.getEleName());
				} else if (eleAry[i].equals("eleFrom")) {
					old.setEleFrom(newEle.getEleFrom());
				} else if (eleAry[i].equals("eleCol")) {
					old.setEleCol(newEle.getEleCol());
				} else if (eleAry[i].equals("eleProTime")) {
					old.setEleProTime(newEle.getEleProTime());
				} else if (eleAry[i].equals("eleForPerson")) {
					old.setEleForPerson(newEle.getEleForPerson());
				} else if (eleAry[i].equals("eleGrade")) {
					old.setEleGrade(newEle.getEleGrade());
				} else if (eleAry[i].equals("eleMod")) {
					old.setEleMod(newEle.getEleMod());
				} else if (eleAry[i].equals("eleTypeidLv2")) {
					old.setEleTypeidLv2(newEle.getEleTypeidLv2());
				} else if (eleAry[i].equals("eleTypenameLv2")) {
					old.setEleTypenameLv2(newEle.getEleTypenameLv2());
				} else if (eleAry[i].equals("eleHsid")) {
					old.setEleHsid(newEle.getEleHsid());
				} else if (eleAry[i].equals("boxWeigth")) {
					old.setBoxWeigth(newEle.getBoxWeigth());
				} else if (eleAry[i].equals("cube")) {
					old.setCube(newEle.getCube());
				} else if (eleAry[i].equals("priceFac")) {
					old.setPriceFac(newEle.getPriceFac());
					old.setPriceFacUint(newEle.getPriceFacUint());
				} else if (eleAry[i].equals("priceOut")) {
					old.setPricePrice(newEle.getPriceOut());
					old.setCurrencyId(newEle.getPriceOutUint());
					old.setPriceOut(newEle.getPriceOut());
					old.setPriceOutUint(newEle.getPriceOutUint());
				} else if (eleAry[i].equals("tuiLv")) {
					old.setTuiLv(newEle.getTuiLv());
				} else if (eleAry[i].equals("liRun")) {
					old.setLiRun(newEle.getLiRun());
				} else if (eleAry[i].equals("barcode")) {
					old.setBarcode(newEle.getBarcode());
				} else if (eleAry[i].equals("eleDesc")) {
					old.setEleDesc(newEle.getEleDesc());
				} else if (eleAry[i].equals("eleRemark")) {
					old.setEleRemark(newEle.getEleRemark());
				}
			}
		}

		if (boxStr != null && !"".equals(boxStr)) {
			String[] boxAry = boxStr.split(",");
			for (int i = 0; i < boxAry.length; i++) {
				if (boxAry[i].equals("boxL")) {
					old.setBoxL(newEle.getBoxL());
					old.setBoxLInch(newEle.getBoxLInch());
				} else if (boxAry[i].equals("boxW")) {
					old.setBoxW(newEle.getBoxW());
					old.setBoxWInch(newEle.getBoxWInch());
				} else if (boxAry[i].equals("boxH")) {
					old.setBoxH(newEle.getBoxH());
					old.setBoxHInch(newEle.getBoxHInch());
				} else if (boxAry[i].equals("boxTypeId")) {
					old.setBoxTypeId(newEle.getBoxTypeId());
				} else if (boxAry[i].equals("boxPbL")) {
					old.setBoxPbL(newEle.getBoxPbL());
					old.setBoxPbLInch(newEle.getBoxPbLInch());
				} else if (boxAry[i].equals("boxPbW")) {
					old.setBoxPbW(newEle.getBoxPbW());
					old.setBoxPbWInch(newEle.getBoxPbWInch());
				} else if (boxAry[i].equals("boxPbH")) {
					old.setBoxPbH(newEle.getBoxPbH());
					old.setBoxPbHInch(newEle.getBoxPbHInch());
				} else if (boxAry[i].equals("boxPbCount")) {
					old.setBoxPbCount(newEle.getBoxPbCount());
				} else if (boxAry[i].equals("boxPbTypeId")) {
					old.setBoxPbTypeId(newEle.getBoxPbTypeId());
				} else if (boxAry[i].equals("boxPbPrice")) {
					old.setBoxPbPrice(newEle.getBoxPbPrice());
				} else if (boxAry[i].equals("boxIbL")) {
					old.setBoxIbL(newEle.getBoxIbL());
					old.setBoxIbLInch(newEle.getBoxIbLInch());
				} else if (boxAry[i].equals("boxIbW")) {
					old.setBoxIbW(newEle.getBoxIbW());
					old.setBoxIbWInch(newEle.getBoxIbWInch());
				} else if (boxAry[i].equals("boxIbH")) {
					old.setBoxIbH(newEle.getBoxIbH());
					old.setBoxIbHInch(newEle.getBoxIbHInch());
				} else if (boxAry[i].equals("boxIbCount")) {
					old.setBoxIbCount(newEle.getBoxIbCount());
				} else if (boxAry[i].equals("boxIbTypeId")) {
					old.setBoxIbTypeId(newEle.getBoxIbTypeId());
				} else if (boxAry[i].equals("boxIbPrice")) {
					old.setBoxIbPrice(newEle.getBoxIbPrice());
				} else if (boxAry[i].equals("boxMbL")) {
					old.setBoxMbL(newEle.getBoxMbL());
					old.setBoxMbLInch(newEle.getBoxMbLInch());
				} else if (boxAry[i].equals("boxMbW")) {
					old.setBoxMbW(newEle.getBoxMbW());
					old.setBoxMbWInch(newEle.getBoxMbWInch());
				} else if (boxAry[i].equals("boxMbH")) {
					old.setBoxMbH(newEle.getBoxMbH());
					old.setBoxMbHInch(newEle.getBoxMbHInch());
				} else if (boxAry[i].equals("boxMbCount")) {
					old.setBoxMbCount(newEle.getBoxMbCount());
				} else if (boxAry[i].equals("boxMbTypeId")) {
					old.setBoxMbTypeId(newEle.getBoxMbTypeId());
				} else if (boxAry[i].equals("boxMbPrice")) {
					old.setBoxMbPrice(newEle.getBoxMbPrice());
				} else if (boxAry[i].equals("boxObL")) {
					old.setBoxObL(newEle.getBoxObL());
					old.setBoxObLInch(newEle.getBoxObLInch());
				} else if (boxAry[i].equals("boxObW")) {
					old.setBoxObW(newEle.getBoxObW());
					old.setBoxObWInch(newEle.getBoxObWInch());
				} else if (boxAry[i].equals("boxObH")) {
					old.setBoxObH(newEle.getBoxObH());
					old.setBoxObHInch(newEle.getBoxObHInch());
				} else if (boxAry[i].equals("boxObCount")) {
					old.setBoxObCount(newEle.getBoxObCount());
				} else if (boxAry[i].equals("boxObTypeId")) {
					old.setBoxObTypeId(newEle.getBoxObTypeId());
				} else if (boxAry[i].equals("boxObPrice")) {
					old.setBoxObPrice(newEle.getBoxObPrice());
				} else if (boxAry[i].equals("eleSizeDesc")) {
					old.setEleSizeDesc(newEle.getEleSizeDesc());
				} else if (boxAry[i].equals("eleInchDesc")) {
					old.setEleInchDesc(newEle.getEleInchDesc());
				} else if (boxAry[i].equals("packingPrice")) {
					old.setPackingPrice(newEle.getPackingPrice());
				} else if (boxAry[i].equals("boxCbm")) {
					old.setBoxCbm(newEle.getBoxCbm());
					old.setBoxCuft(newEle.getBoxCuft());
				} else if (boxAry[i].equals("boxNetWeigth")) {
					old.setBoxGrossWeigth(newEle.getBoxGrossWeigth());
					old.setBoxNetWeigth(newEle.getBoxNetWeigth());
				} else if (boxAry[i].equals("boxRemark")) {
					old.setBoxRemark(newEle.getBoxRemark());
				} else if (boxAry[i].equals("boxRemarkCn")) {
					old.setBoxRemarkCn(newEle.getBoxRemarkCn());
				} else if (boxAry[i].equals("boxTDesc")) {
					old.setBoxTDesc(newEle.getBoxTDesc());
				} else if (boxAry[i].equals("boxBDesc")) {
					old.setBoxBDesc(newEle.getBoxBDesc());
				}
			}
		}

		if (otherStr != null && !"".equals(otherStr)) {
			String[] otherAry = otherStr.split(",");
			for (int i = 0; i < otherAry.length; i++) {
				if (otherAry[i].equals("box20Count")) {
					old.setBox20Count(newEle.getBox20Count());
				} else if (otherAry[i].equals("box40Count")) {
					old.setBox40Count(newEle.getBox40Count());
				} else if (otherAry[i].equals("box40hqCount")) {
					old.setBox40hqCount(newEle.getBox40hqCount());
				} else if (otherAry[i].equals("box45Count")) {
					old.setBox45Count(newEle.getBox45Count());
				}
			}
		}
		return old;
	}

	// 根据报价的图片转换成样品图片
	public CotElePic changeElePic(String type, Integer srcId, CotElePic pic) {
		CotElePic cotElePic = null;
		if (pic != null) {
			cotElePic = pic;
		} else {
			cotElePic = new CotElePic();
		}
		if (("price").equals(type)) {
			String hql = "from CotPricePic obj where obj.fkId=" + srcId;
			List list = this.getPriceDao().find(hql);
			CotPricePic p = (CotPricePic) list.get(0);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("order").equals(type)) {
			String hql = "from CotOrderPic obj where obj.fkId=" + srcId;
			List list = this.getPriceDao().find(hql);
			CotOrderPic p = (CotOrderPic) list.get(0);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("given").equals(type)) {
			String hql = "from CotGivenPic obj where obj.fkId=" + srcId;
			List list = this.getPriceDao().find(hql);
			CotGivenPic p = (CotGivenPic) list.get(0);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("none").equals(type)) {
			byte[] zwtp = this.getZwtpPic();
			cotElePic.setEleId("");
			cotElePic.setPicImg(zwtp);
			cotElePic.setPicName("");
			cotElePic.setPicSize(zwtp.length);
		}
		return cotElePic;
	}

	// 同步不同的样品的图片
	public Map getPriceByDisEles(Map<String, String> disMap) {
		Map map = new HashMap();
		Iterator<?> it = disMap.keySet().iterator();
		while (it.hasNext()) {
			String eleId = (String) it.next();
			Object[] obj = new Object[2];
			CotPriceDetail priceDetail = (CotPriceDetail) this
					.getPriceMapValue(disMap.get(eleId));
			obj[0] = priceDetail;

			CotElePic cotElePic = null;
			if (priceDetail.getType() == null) {
				String hql = "from CotPricePic obj where obj.fkId="
						+ priceDetail.getId();
				List list = this.getPriceDao().find(hql);
				CotPricePic cotPricePic = (CotPricePic) list.get(0);
				cotElePic = new CotElePic();
				cotElePic.setEleId(cotPricePic.getEleId());
				cotElePic.setPicImg(cotPricePic.getPicImg());
				cotElePic.setPicName(cotPricePic.getPicName());
				cotElePic.setPicSize(cotPricePic.getPicImg().length);
			} else {
				String type = priceDetail.getType();
				Integer srcId = priceDetail.getSrcId();
				cotElePic = changeElePic(type, srcId, null);
			}
			obj[1] = cotElePic;
			map.put(eleId.toLowerCase(), obj);
		}
		return map;
	}

	// 同步相同的样品的图片
	public Object[] getPriceByEle(String rdm, CotElePic cotElePic) {
		Object[] obj = new Object[2];
		CotPriceDetail priceDetail = (CotPriceDetail) this
				.getPriceMapValue(rdm);
		obj[0] = priceDetail;
		CotElePic pic = null;
		if (priceDetail.getType() == null) {
			String hql = "from CotPricePic obj where obj.fkId="
					+ priceDetail.getId();
			List list = this.getPriceDao().find(hql);
			CotPricePic cotPricePic = (CotPricePic) list.get(0);
			pic = cotElePic;
			pic.setEleId(cotPricePic.getEleId());
			pic.setPicImg(cotPricePic.getPicImg());
			pic.setPicName(cotPricePic.getPicName());
			pic.setPicSize(cotPricePic.getPicImg().length);
		} else {
			String type = priceDetail.getType();
			Integer srcId = priceDetail.getSrcId();
			pic = changeElePic(type, srcId, cotElePic);
		}
		obj[1] = pic;
		return obj;
	}

	// 根据样品货号字符串查询明细
	public void updateToEle(String[] same, String[] sameRdm, String[] dis,
			String[] disRdm, String eleStr, String boxStr, String otherStr,
			boolean isPic) {
		// 相同时处理
		if (same != null) {
			String t = "";
			// key为eleId,value为随机数
			Map<String, String> sameMap = new HashMap<String, String>();
			for (int i = 0; i < same.length; i++) {
				sameMap.put(same[i], sameRdm[i]);
				t += "'" + same[i] + "',";
			}
			// 获得样品和图片集合
			String elePichql = "select e,e.eleId,obj from CotElementsNew e,CotElePic obj where obj.fkId=e.id and e.eleId in ("
					+ t.substring(0, t.length() - 1) + ")";
			List<?> elePicList = this.getPriceDao().find(elePichql);

			// 给样品设置id
			List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
			// 修改样品图片表
			List<CotElePic> listPic = new ArrayList<CotElePic>();
			for (int i = 0; i < elePicList.size(); i++) {
				Object[] obj = (Object[]) elePicList.get(i);
				// 获得报价明细,样品图片
				Object[] temp = (Object[]) getPriceByEle(sameMap.get(obj[1]
						.toString()), (CotElePic) obj[2]);
				CotElementsNew ele = setEleByTong((CotElementsNew) obj[0],
						(CotPriceDetail) temp[0], eleStr, boxStr, otherStr);
				listTemp.add(ele);
				listPic.add((CotElePic) temp[1]);
			}

			this.getPriceDao().updateRecords(listTemp);
			if (isPic == true) {
				for (int i = 0; i < listTemp.size(); i++) {
					CotElementsNew elementsNew = (CotElementsNew) listTemp
							.get(i);
					CotElePic cotElePic = (CotElePic) listPic.get(i);
					cotElePic.setFkId(elementsNew.getId());
					if (cotElePic.getEleId().equals("")) {
						cotElePic.setEleId(elementsNew.getEleId());
					}
					listPic.add(cotElePic);
				}
				this.getPriceDao().updateRecords(listPic);
			}
		}
		// 不同的处理
		if (dis != null) {
			// key为eleId,value为随机数
			Map<String, String> disMap = new HashMap<String, String>();
			for (int i = 0; i < dis.length; i++) {
				disMap.put(dis[i], disRdm[i]);
			}
			// 获得报价明细,样品图片(key为eleId,value为obj[2])
			Map map = getPriceByDisEles(disMap);

			List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
			for (int i = 0; i < dis.length; i++) {
				Object[] temp = (Object[]) map.get(dis[i].toLowerCase());
				CotElementsNew newEle = new CotElementsNew();
				CotElementsNew ele = setEleByTong(newEle,
						(CotPriceDetail) temp[0], eleStr, boxStr, otherStr);
				if (ele.getEleFlag() == null) {
					ele.setEleFlag(0l);
				}
				listTemp.add(ele);
			}

			try {
				this.getPriceDao().saveRecords(listTemp);
				// 新建样品图片表
				List<CotElePic> listPic = new ArrayList<CotElePic>();
				for (int i = 0; i < listTemp.size(); i++) {
					CotElementsNew e = listTemp.get(i);
					Object[] temp = (Object[]) map.get(e.getEleId()
							.toLowerCase());
					CotElePic cotElePic = (CotElePic) temp[1];
					cotElePic.setFkId(e.getId());
					if (isPic != true) {
						byte[] zwtp = this.getZwtpPic();
						cotElePic.setEleId(e.getEleId());
						cotElePic.setPicImg(zwtp);
						cotElePic.setPicName(e.getEleId());
						cotElePic.setPicSize(zwtp.length);
					}
					listPic.add(cotElePic);
				}
				this.getPriceDao().saveRecords(listPic);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic() {
		// 获得系统路径
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath + "common/images/zwtp.png");
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[((int) picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			return b;
		} catch (Exception e1) {
			logger.error("获得暂无图片的图片字节错误!");
			return null;
		}
	}

	// 判断该主单的明细是否已存在该产品货号
	@SuppressWarnings("unchecked")
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId) {
		List<Integer> res = this.getPriceDao().find(
				"select c.id from CotPriceDetail c where c.priceId = " + mainId
						+ " and c.eleId='" + eleId + "'");
		if (res.size() == 0) {
			return false;
		} else if (res.size() == 1) {
			if (!res.get(0).toString().equals(eId.toString())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	// 清空明细Map
	public void clearMap(String typeName) {
		SystemUtil.clearSessionByType(null, typeName);
	}

	// 清空盘点机Map
	public void clearCheckMap() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("CheckMachine");
	}

	// 通过key获取Map的value
	@SuppressWarnings("unchecked")
	public CotPriceDetail getPriceMapValue(String rdm) {
		Object obj = SystemUtil.getObjBySession(null, "price");
		Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
		if (priceMap != null) {
			CotPriceDetail cotPriceDetail = (CotPriceDetail) priceMap.get(rdm);
			return cotPriceDetail;
		}
		return null;
	}

	// 储存Map
	public void setPriceMap(String rdm, CotPriceDetail cotPriceDetail) {
		Object obj = SystemUtil.getObjBySession(null, "price");
		if (obj == null) {
			Map<String, CotPriceDetail> priceMap = new HashMap<String, CotPriceDetail>();
			priceMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(null, priceMap, "price");
		} else {
			Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
			priceMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(null, priceMap, "price");
		}

	}

	// 通过货号修改Map中对应的征样明细
	public boolean updateMapValueByEleId(String rdm, String property,
			String value) {
		CotPriceDetail cotPriceDetail = getPriceMapValue(rdm);
		if (cotPriceDetail == null)
			return false;
		try {
			ConvertUtils.register(new IntegerConverter(null), Integer.class);
			BeanUtils.setProperty(cotPriceDetail, property, value);
//			if("factoryId".equals(property)){
//				if("".equals(value)){
//					cotPriceDetail.setFactoryId(null);
//				}else{
//					cotPriceDetail.setFactoryId(Integer.parseInt(value));
//				}
//			}else if("eleTypeidLv1".equals(property)){
//				if("".equals(value)){
//					cotPriceDetail.setEleTypeidLv1(null);
//				}else{
//					cotPriceDetail.setEleTypeidLv1(Integer.parseInt(value));
//				}
//			}else if ("eleHsid".equals(property)) {
//				if ("".equals(value)) {
//					cotPriceDetail.setEleHsid(null);
//				} else {
//					cotPriceDetail.setEleHsid(Integer.parseInt(value));
//				}
//			}else if("eleTypeidLv2".equals(property)){
//				if("".equals(value)){
//					cotPriceDetail.setEleTypeidLv2(null);
//				}else{
//					cotPriceDetail.setEleTypeidLv2(Integer.parseInt(value));
//				}
//			}else{
//				BeanUtils.setProperty(cotPriceDetail, property, value);
//			}
			this.setPriceMap(rdm, cotPriceDetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 清除Map中eleId对应的映射
	@SuppressWarnings("unchecked")
	public void delMapByKey(String rdm) {
		Object obj = SystemUtil.getObjBySession(null, "price");
		Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
		if (priceMap != null) {
			if (priceMap.containsKey(rdm)) {
				priceMap.remove(rdm);
			}
		}
	}

	// 在Action中清除Map中eleId对应的映射
	public void delMapByKeyAction(String rdm, HttpSession session) {
		Map<String, CotPriceDetail> priceMap = this.getMapAction(session);
		if (priceMap != null) {
			if (priceMap.containsKey(rdm)) {
				priceMap.remove(rdm);
			}
		}
	}

	// Action获取signMap
	@SuppressWarnings("unchecked")
	public Map<String, CotPriceDetail> getMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "price");
		Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
		return priceMap;
	}

	// Action储存signMap
	@SuppressWarnings("unchecked")
	public void setMapAction(HttpSession session, String rdm,
			CotPriceDetail cotPriceDetail) {
		Object obj = SystemUtil.getObjBySession(session, "price");
		if (obj == null) {
			Map<String, CotPriceDetail> priceMap = new HashMap<String, CotPriceDetail>();
			priceMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(session, priceMap, "price");
		} else {
			Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
			priceMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(session, priceMap, "price");
		}

	}

	// 根据明细货号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId) {
		CotPriceDetail cotPriceDetail = (CotPriceDetail) this.getPriceDao()
				.getById(CotPriceDetail.class, detailId);
		if (cotPriceDetail != null) {
			return cotPriceDetail.getPicImg();
		}
		return null;
	}

	// 删除明细图片picImg
	public boolean deletePicImg(Integer detailId) {
		String classPath = CotPriceServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		// CotPriceDetail cotPriceDetail = this.getPriceDetailById(detailId);
		// String fileLength = ContextUtil.getProperty("sysconfig.properties",
		// "maxLength");
		List imgList = new ArrayList();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotPricePic pricePic = impOpService.getPricePic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			if (pricePic == null)
				return true;
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			pricePic.setPicImg(b);
			pricePic.setPicSize(b.length);
			imgList.add(pricePic);
			// cotPriceDetail.setPicImg(b);
			// cotPriceDetail.setPicName("zwtp");
			// this.getPriceDao().update(cotPriceDetail);
			impOpService.modifyImg(imgList);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除明细图片错误!");
			return false;
		}
	}

	// 更新征样图片picImg字段
	public void updatePicImg(String filePath, Integer detailId) {

		CotPriceDetail cotPriceDetail = this.getPriceDetailById(detailId);
		List imgList = new ArrayList();
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotPricePic pricePic = impOpService.getPricePic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			if (pricePic == null) {
				pricePic = new CotPricePic();
				pricePic.setFkId(detailId);
				pricePic.setEleId(cotPriceDetail.getEleId());
				pricePic.setPicName(cotPriceDetail.getEleId());
			}
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			pricePic.setPicImg(b);
			pricePic.setPicSize(b.length);
			imgList.add(pricePic);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			impOpService.saveOrUpdateImg(imgList);
		} catch (Exception e) {
			logger.error("修改征样明细图片错误!");
		}
	}

	// 得到公司的集合
	public List<?> getCompanyList() {
		List<?> list = this.getPriceDao().getRecords("CotCompany");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotCompany cotCompany = (CotCompany) it.next();
			cotCompany.setCompanyLogo(null);
		}
		return list;
	}

	// 得到客户的集合
	public List<?> getCustomerList() {
		List<?> list = this.getPriceDao().getRecords("CotCustomer");
		List<CotCustomer> newList = new ArrayList<CotCustomer>();
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			CotCustomer clone = (CotCustomer) SystemUtil.deepClone(cotCustomer);
			if (clone != null) {
				clone.setPicImg(null);
				clone.setCustImg(null);
			}
			newList.add(clone);
		}

		return newList;
	}

	// 得到员工的集合
	public List<?> getEmpsList() {
		List<?> list = this.getPriceDao().getRecords("CotEmps");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotEmps cotEmps = (CotEmps) it.next();
			cotEmps.setCustomers(null);
		}
		return list;
	}

	// 根据上传的盘点机数据存入内存
	public void saveCheckMachine(List<String> list, HttpServletRequest request) {
		Map<String, List<CotPriceDetail>> map = new HashMap<String, List<CotPriceDetail>>();
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotElementsNew.class);
		// 获得币种为美元的编号
		String sqlCur = "select obj.id from CotCurrency obj where obj.curNameEn='USD'";
		List<?> listCur = this.getPriceDao().find(sqlCur);
		// 如果没有名称为USD的币种,不能导入
		if (listCur == null || listCur.size() == 0) {
			map = null;
			return;
		}
		Integer curUSD = (Integer) listCur.get(0);
		try {
			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i);
				String[] price = temp.split(",");
				map.put(price[0].trim(), null);
			}
			for (Iterator<?> itor = map.keySet().iterator(); itor.hasNext();) {
				String field = (String) itor.next();
				List<CotPriceDetail> listDetail = new ArrayList<CotPriceDetail>();
				for (int i = 0; i < list.size(); i++) {
					String temp = list.get(i);
					String[] price = temp.split(",");
					if (field.equals(price[0].trim())) {
						String sql = "from CotElementsNew obj where obj.eleId='"
								+ price[1].trim() + "'";
						List<?> listOld = this.getPriceDao().find(sql);
						if (listOld.size() > 0) {
							CotElementsNew cotElements = (CotElementsNew) listOld
									.get(0);
							CotPriceDetail priceDetail = new CotPriceDetail();
							for (int j = 0; j < properties.length; j++) {
								if ("cotPictures".equals(properties[j])
										|| "id".equals(properties[j])
										|| "cotFile".equals(properties[j])
										|| "childs".equals(properties[j])
										|| "picImg".equals(properties[j])
										|| "cotPriceFacs".equals(properties[j])
										|| "cotElePrice".equals(properties[j])
										|| "cotEleFittings"
												.equals(properties[j])
										|| "eleFittingPrice"
												.equals(properties[j])
										|| "cotElePacking"
												.equals(properties[j])
										|| "packingPrice".equals(properties[j]))
									continue;

								String value = BeanUtils.getProperty(
										cotElements, properties[j]);
								if (value != null) {
									BeanUtils.setProperty(priceDetail,
											properties[j], value);
								}
							}
							// 同一币种为美元
							priceDetail.setCurrencyId(curUSD);
							priceDetail.setPricePrice(Float.parseFloat(price[2]
									.trim()));
							priceDetail.setEleMod(Integer.parseInt(price[3]
									.trim()));
							priceDetail.setType("ele");
							String rdm = "1"
									+ RandomStringUtils.randomNumeric(8);
							priceDetail.setRdm(rdm);
							priceDetail.setSrcId(cotElements.getId());
							listDetail.add(priceDetail);
						}
					}
				}
				map.put(field, listDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = null;
		}
		request.getSession().setAttribute("CheckMachine", map);
	}

	// 从session中取得上传的盘点机流水号
	@SuppressWarnings("unchecked")
	public List<String> getMachineNum() {
		List<String> list = new ArrayList<String>();
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckMachine");
		if (obj != null) {
			Map<String, List<CotPriceDetail>> map = (Map<String, List<CotPriceDetail>>) obj;
			for (Iterator<?> itor = map.keySet().iterator(); itor.hasNext();) {
				String field = (String) itor.next();
				list.add(field);
			}
		}
		return list;
	}

	// 根据盘点机流水号获得明细字符串集合
	@SuppressWarnings("unchecked")
	public List<String> getMachineDetails(String checkNo) {
		List<String> list = new ArrayList<String>();
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckMachine");
		if (obj != null) {
			Map<String, List<CotPriceDetail>> map = (Map<String, List<CotPriceDetail>>) obj;
			List<CotPriceDetail> listDetail = map.get(checkNo);
			for (int i = 0; i < listDetail.size(); i++) {
				CotPriceDetail cotPriceDetail = (CotPriceDetail) listDetail
						.get(i);
				String detail = cotPriceDetail.getEleId() + "---"
						+ cotPriceDetail.getPricePrice() + "---"
						+ cotPriceDetail.getEleMod();
				list.add(detail);
			}
		}
		return list;
	}

	// 根据盘点机流水号获得明细对象集合
	@SuppressWarnings("unchecked")
	public List<CotPriceDetail> getMachineDetailList(String checkNo) {
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckMachine");
		if (obj != null) {
			Map<String, List<CotPriceDetail>> map = (Map<String, List<CotPriceDetail>>) obj;
			List<CotPriceDetail> listDetail = map.get(checkNo);
			return listDetail;
		}
		return null;
	}

	// 根据单号查询该单的客户
	public CotCustomer getCusByPriceId(Integer orderId) {
		if (orderId == null) {
			return null;
		}
		CotPrice cotPrice = (CotPrice) this.getPriceDao().getById(
				CotPrice.class, orderId);
		if (cotPrice != null) {
			CotCustomer cotCustomer = (CotCustomer) this.getPriceDao().getById(
					CotCustomer.class, cotPrice.getCustId());
			if (cotCustomer != null) {
				cotCustomer.setPicImg(null);
				cotCustomer.setCustImg(null);
			}
			return cotCustomer;
		} else {
			return null;
		}
	}

	// 根据员工编号获得员工
	public CotEmps getEmpsById(Integer empId) {
		CotEmps cotEmps = (CotEmps) this.getPriceDao().getById(CotEmps.class,
				empId);
		cotEmps.setCustomers(null);
		return cotEmps;
	}

	// 得到单样品利润公式
	@SuppressWarnings("unchecked")
	public String getLiRunCau() {
		// 获得单样品利润价格公式(名字固定是叫'InverseMargin')
		String cauHql = "select c.expessionIn from CotCalculation c where c.calName='InverseMargin_FOB'";
		List<String> cauList = this.getPriceDao().find(cauHql);
		if (cauList.size() != 0) {
			return cauList.get(0);
		} else {
			return "";
		}
	}

	// 获得新利润率
	public Float getNewLiRun(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotPriceDetail obj = this.getPriceMapValue(rdm);
		if (obj == null) {
			return null;
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 利润率
		Float priceProfit = 0f;
		if (obj.getLiRun() != null) {
			priceProfit = obj.getLiRun();
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("priceFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Float priceRate = 0f;
		Object priceRateObj = map.get("priceRate");
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			List<?> listCur = this.getDicList("currency");
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("priceCharge");
		if (priceChargeObj != null && !"".equals(priceChargeObj.toString())) {
			priceCharge = Float.parseFloat(priceChargeObj.toString());
		}
		// 集装箱类型
		Integer containerTypeId = 0;
		Object containerTypeIdObj = map.get("containerTypeId");
		if (containerTypeIdObj != null
				&& !"".equals(containerTypeIdObj.toString())) {
			containerTypeId = Integer.parseInt(containerTypeIdObj.toString());
		}
		// 保险费率
		Float insureRate = 0f;
		Object insureRateObj = map.get("insureRate");
		if (insureRateObj != null && !"".equals(insureRateObj.toString())) {
			insureRate = Float.parseFloat(insureRateObj.toString());
		}
		// 保险加成率
		Float insureAddRate = 0f;
		Object insureAddRateObj = map.get("insureAddRate");
		if (insureAddRateObj != null && !"".equals(insureAddRateObj.toString())) {
			insureAddRate = Float.parseFloat(insureAddRateObj.toString());
		}
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.00");
		// 获得公式
		String cau = this.getLiRunCau();
		//如果没有配置名叫"InverseMargin"的公式
		if("".equals(cau)){
			return null;
		}

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();

		// 计算报价
		// 先根据该的币种的汇率转成人民币,在根据选中的币种的汇率转成该币种的值
		String curHql = "from CotCurrency obj where obj.id="
				+ obj.getPriceFacUint();
		Float fac = obj.getPriceFac();
		List<?> listCur = this.getPriceDao().find(curHql);
		// 币种对象不能不为空
		if (listCur.size() == 0) {
			return null;
		}
		CotCurrency cur = (CotCurrency) listCur.get(0);
		Float rate = cur.getCurRate();
		Float rmb = rate * fac;
		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", priceFob.toString());
		evaluator.putVariable("priceProfit", priceProfit.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getPriceDao().getRecords(
				"CotContainerType");
		for (int j = 0; j < containerList.size(); j++) {
			CotContainerType cot = (CotContainerType) containerList.get(j);
			if (cot.getId().intValue() == containerTypeId) {
				if (cot.getContainerCube() != null) {
					cube = cot.getContainerCube();
				}
				break;
			}
		}
		// 装箱率
		Long boxObCount = obj.getBoxObCount();
		if (boxObCount == null) {
			boxObCount = 0l;
		}
		evaluator.putVariable("priceCharge", priceCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());

		if (obj.getTuiLv() != null) {
			evaluator.putVariable("tuiLv", obj.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}

		// 新价格
		Float price = 0f;
		Object priceObj = map.get("price");
		if (priceObj != null && !"".equals(priceObj.toString())) {
			price = Float.parseFloat(priceObj.toString());
		}else{
			//calLiRun方法需要
			price = obj.getPricePrice();
		}
		float res = 0;
		try {
			// 计算利润率
			evaluator.putVariable("price", price.toString());
			String result = evaluator.evaluate(cau);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	// 返回该货号的子货号的id集合
	public List<?> getChildrens(String eleId) {
		String hql = "select obj.id from CotElementsNew obj where obj.eleParent='"
				+ eleId + "'";
		List<?> list = this.getPriceDao().find(hql);
		return list;
	}

	// 根据币种换算价格
	public float updatePrice(Float price, Integer oldCurId, Integer newCurId) {
		if (price == null || oldCurId == null) {
			return 0;
		}
		Float obj = 0f;
		Float rmb = 0f;
		List<?> listCur = this.getDicList("currency");
		// 取得厂家报价的人民币值
		for (int j = 0; j < listCur.size(); j++) {
			CotCurrency cur = (CotCurrency) listCur.get(j);
			if (cur.getId().intValue() == oldCurId.intValue()) {
				rmb = price * cur.getCurRate();
				break;
			}
		}
		for (int j = 0; j < listCur.size(); j++) {
			CotCurrency cur = (CotCurrency) listCur.get(j);
			if (cur.getId().intValue() == newCurId.intValue()) {
				obj = rmb / cur.getCurRate();
				break;
			}
		}
		DecimalFormat nbf = new DecimalFormat("0.000");
		obj = Float.parseFloat(nbf.format(obj));

		return obj;
	}

	// 根据文件路径导入
	public List<?> saveReport(Integer orderId, String filename, boolean cover,
			Integer currencyId, boolean excelFlag) {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("priceReport");
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 格式化样品包装数据
		DecimalFormat df = new DecimalFormat("#.####");
		DecimalFormat dfTwo = new DecimalFormat("#.##");
		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());

		// 先查询所有订单明细的货号
		List<?> detailEles = this.getReportDao().getPriceDetails(orderId);
		List<CotPriceDetail> detailElesBak = new ArrayList();
		for (int i = 0; i < detailEles.size(); i++) {
			CotPriceDetail det = (CotPriceDetail) detailEles.get(i);
			detailElesBak.add(det);
		}

		// 查询所有样品编号
		TreeMap<String, CotPriceDetail> mapEleId = new TreeMap<String, CotPriceDetail>();
		// 查询所有样品材质(中文KEY)
		Map<String, Integer> mapTypeCn = this.getReportDao().getAllTypeCn();
		// 查询所有样品材质(英文KEY)
		Map<String, Integer> mapTypeEn = this.getReportDao().getAllTypeEn();
		// 查询所有产品类别
		Map<String, Integer> mapEleType = this.getReportDao().getAllEleType();
		// 查询所有包装类型(中文KEY)
		Map<String, String[]> mapBoxTypeCn = this.getReportDao()
				.getAllBoxTypeByCn();
		// 查询所有包装类型(英文KEY)
		Map<String, String[]> mapBoxTypeEn = this.getReportDao()
				.getAllBoxTypeByEn();
		// 查询所有币种
		Map<String, Integer> mapCurrency = this.getReportDao().getAllCurrency();
		// 查询所有厂家简称
		Map<String, Integer> mapShortName = this.getReportDao()
				.getAllFactoryShortName();
		// 查询所有海关编码
		Map<String, Integer> mapHsCode = this.getReportDao().getAllHsId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 取得默认的20/40/40HQ/45的柜体积,数据字典没设置的话默认为24/54/68/86)
		Float[] cubes = this.getReportDao().getContainerCube();

		// 查询所有包装材料
		Map boxPackMap = this.getReportDao().getAllBoxPacking();
		// 获得tomcat路径
		String classPath = CotPriceServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + "upload/" + filename);
		try {
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setLocale(locale);
			setting.setEncoding("ISO-8859-1");
			workbook = Workbook.getWorkbook(file, setting);

		} catch (Exception e) {
			file.delete();
			return null;
		}

		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		// 限制一次性只能导入300条记录
		if (sheet.getRows() > 304) {
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(1);
			cotMsgVO.setMsg("导入失败！一次最多只能导入300条样品！");
			msgList.add(cotMsgVO);
			file.delete();
			return msgList;
		}
		//如果没有序列号这列或者序列号的值是空,自动填值
		int sortTemp = 0;
		boolean isHaveSort = false;
		
		boolean rowChk = false;
		for (int i = 4; i < sheet.getRows(); i++) {
			// 新建样品对象
			CotPriceDetail detail = null;
			// 定义厂家
			CotFactory cotFactory = new CotFactory();
			// 是否有成功标识
			boolean isSuccess = true;
			// 用于计算CBM和CUFT
			float boxObL = 0f;
			float boxObW = 0f;
			float boxObH = 0f;

			// 产品长宽高
			float boxL = 0f;
			float boxW = 0f;
			float boxH = 0f;
			float boxLInch = 0f;
			float boxWInch = 0f;
			float boxHInch = 0f;

			// 单重
			float boxWeigth = 0f;
			float gWeigh = 0f;
			float nWeigh = 0f;

			// 厂价
			Float priceFac = 0f;
			// 外销价
			Float priceOut = 0f;

			String eleSize = "";
			String eleSizeInch = "";
			boolean isChangeL=false;

			// 用于计算装箱数
			long boxObCount = 0;

			Integer hsIdTemp = 0;

			// 判断是否是子货号
			boolean isChild = false;
			float tuiLv = 0f;

			for (int j = 0; j < sheet.getColumns(); j++) {
				// 表头
				Cell headCtn = sheet.getCell(j, 1);
				Cell row = sheet.getCell(j, i);
				String rowCtn = row.getContents();
				//如果没转换成数字cell,默认的最长小数位是3位
				if (row.getType() == CellType.NUMBER
						|| row.getType() == CellType.NUMBER_FORMULA) {
					NumberCell nc = (NumberCell) row;
					rowCtn =df.format(nc.getValue());
				}

				if (headCtn.getContents().equals("ELE_ID")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. can not be empty");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The length of Article No. can not exceed 100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (cover == true) {
						boolean ck = false;
						for (int k = 0; k < detailElesBak.size(); k++) {
							CotPriceDetail priceDetail = (CotPriceDetail) detailElesBak
									.get(k);
							if (rowCtn.trim().toLowerCase().equals(
									priceDetail.getEleId().toLowerCase())) {
								ck = true;
								rowChk = true;
								detail = priceDetail;
								detailElesBak.remove(priceDetail);
								coverNum++;
								break;
							}
						}
						if (ck == false && rowChk == false) {
							rowChk = false;
							detail = new CotPriceDetail();
							detail.setEleId(rowCtn.trim().toUpperCase());
							if (cotEleCfg != null) {
								// 设置样品默认值
								detail = this.getReportDao().setPriceDefault(
										cotEleCfg, detail);
							}
							if (excelFlag == false) {
								detail.setPriceOutUint(currencyId);
							}
						}
						if (ck == false && rowChk == true) {
							isSuccess = false;
							rowChk = false;
							break;
						}
					} else {
						detail = new CotPriceDetail();
						detail.setEleId(rowCtn.trim().toUpperCase());
						if (cotEleCfg != null) {
							// 设置样品默认值
							detail = this.getReportDao().setPriceDefault(
									cotEleCfg, detail);
						}
						if (excelFlag == false) {
							detail.setPriceOutUint(currencyId);
						}
					}
					if (detail.getBoxObCount() != null) {
						boxObCount = detail.getBoxObCount();
					}
					//处理中英文规格
					if (detail.getBoxL()!= null) {
						boxL = detail.getBoxL();
					}
					if (detail.getBoxW()!= null) {
						boxW = detail.getBoxW();
					}
					if (detail.getBoxH()!= null) {
						boxH = detail.getBoxH();
					}
					if (detail.getBoxLInch()!= null) {
						boxLInch = detail.getBoxLInch();
					}
					if (detail.getBoxWInch()!= null) {
						boxWInch = detail.getBoxWInch();
					}
					if (detail.getBoxHInch()!= null) {
						boxHInch = detail.getBoxHInch();
					}
				}

				if (headCtn.getContents().equals("HS_ID") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					// Integer flag = this.getReportDao().isExistHsId(
					// rowCtn.trim());
					Object hsId = mapHsCode.get(rowCtn.trim().toLowerCase());
					if (hsId == null) {
						CotEleOther cotEleOther = new CotEleOther();
						cotEleOther.setHscode(rowCtn.trim());
						cotEleOther.setCnName(rowCtn.trim());
						if (tuiLv != 0) {
							cotEleOther.setTaxRate(tuiLv);
						}
						try {
							this.getReportDao().create(cotEleOther);
							// 将新类别添加到已有的map中
							mapHsCode.put(rowCtn.trim().toLowerCase(),
									cotEleOther.getId());
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存海关编码异常");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					detail.setEleHsid(mapHsCode
							.get(rowCtn.trim().toLowerCase()));
					hsIdTemp = mapHsCode.get(rowCtn.trim().toLowerCase());
				}
				if (headCtn.getContents().equals("SORT_NO")) {
					isHaveSort = true;
					// 覆盖的时候,不覆盖序列号
					if (detail.getId() == null) {
						if(rowCtn != null && !rowCtn.trim().equals("")){
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setSortNo(elemod.intValue());
						}else{
							sortTemp++;
							detail.setSortNo(sortTemp);
						}
					}
				}
				if (headCtn.getContents().equals("ELE_NAME") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleName(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_TYPENAME_LV2")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleTypenameLv2(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_NAME_EN")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					// 如果英文名超过200长度,截取
					if (rowCtn.trim().length() > 200) {
						detail.setEleNameEn(rowCtn.trim().substring(0, 200));
					} else {
						detail.setEleNameEn(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_FLAG")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						if (rowCtn.trim().equals("套件")) {
							detail.setEleFlag(1l);
						}
						if (rowCtn.trim().equals("单件")) {
							detail.setEleFlag(0l);
						}
						if (rowCtn.trim().equals("组件")) {
							detail.setEleFlag(3l);
						}
					}
				}
				if (headCtn.getContents().equals("ELE_PARENT")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					Integer temp = this.getReportDao().isExistEleId(
							rowCtn.trim());
					if (temp == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Parent Article No. does not exist!");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						detail.setEleParentId(temp);
						detail.setEleParent(rowCtn.trim());
						detail.setEleFlag(2l);
						isChild = true;
					}
				}
				if (headCtn.getContents().equals("CUST_NO") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setCustNo(rowCtn.trim());
				}
				if (headCtn.getContents().equals("FACTORY_NO")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setFactoryNo(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_DESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FROM") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleFrom(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FACTORY")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleFactory(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_COL") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleCol(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BARCODE") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					if (rowCtn.trim().length() > 30) {
						detail.setBarcode(rowCtn.trim().substring(0, 30));
					} else {
						detail.setBarcode(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_COMPOSETYPE")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleComposeType(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleUnit(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_GRADE") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleGrade(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FOR_PERSON")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleForPerson(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_PRO_TIME")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (row.getType() == CellType.DATE) {
						DateCell dc = (DateCell) row;
						detail.setEleProTime(new java.sql.Date(dc.getDate()
								.getTime()));
					} else {
						try {
							java.util.Date date = sdf.parse(rowCtn.trim());

							detail.setEleProTime(new java.sql.Date(date
									.getTime()));
						} catch (ParseException e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Date format is incorrect");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
				}
				if (headCtn.getContents().equals("ELE_SIZE_DESC")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					eleSize = rowCtn.trim();
				}
				if (headCtn.getContents().equals("TAO_UNIT") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setTaoUnit(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_INCH_DESC")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					eleSizeInch = rowCtn.trim();
				}
				if (headCtn.getContents().equals("ELE_REMARK")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().length() > 500) {
						detail.setEleRemark(rowCtn.trim().substring(0, 500));
					} else {
						detail.setEleRemark(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_TYPENAME_LV1")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						// 去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");

						Integer typeId = mapTypeCn.get(temp.toLowerCase());
						if (typeId == null) {
							typeId = mapTypeEn.get(temp.toLowerCase());
						}
						if (typeId == null) {
							// 新增一条材质,中英文名相同
							CotTypeLv1 cotTypeLv1 = new CotTypeLv1();
							cotTypeLv1.setTypeName(temp);
							cotTypeLv1.setTypeEnName(temp);
							List<CotTypeLv1> listType = new ArrayList<CotTypeLv1>();
							listType.add(cotTypeLv1);
							try {
								this.getReportDao().saveRecords(listType);
								typeId = cotTypeLv1.getId();
								mapTypeCn.put(temp.toLowerCase(), typeId);
							} catch (DAOException e) {
								e.printStackTrace();
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存样品材质异常!");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setEleTypenameLv1(temp);
						detail.setEleTypeidLv1(typeId);
					}
				}

				if (headCtn.getContents().equals("ELE_TYPEID_LV2")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object typeId = mapEleType.get(rowCtn.trim()
								.toLowerCase());
						if (typeId == null) {
							CotTypeLv2 cotTypeLv2 = new CotTypeLv2();
							cotTypeLv2.setTypeName(rowCtn.trim());
							try {
								this.getReportDao().create(cotTypeLv2);
								// 将新类别添加到已有的map中
								mapEleType.put(rowCtn.trim().toLowerCase(),
										cotTypeLv2.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存产品分类异常");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setEleTypeidLv2(mapEleType.get(rowCtn.trim()
								.toLowerCase()));
					}

				}

				// 厂家简称不存在时新建厂家
				if (headCtn.getContents().equals("SHORT_NAME")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						// 去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");
						Object factoryId = mapShortName.get(temp.toLowerCase());
						if (factoryId == null) {
							cotFactory.setFactoryName(temp);
							cotFactory.setShortName(temp);
							cotFactory.setFactroyTypeidLv1(1);
							try {
								CotSeqService cotSeqServiceImpl=new CotSeqServiceImpl();
								String facNo =cotSeqServiceImpl.getFacNo();
//								String facNo = seq.getAllSeqByType("facNo",
//										null);
								cotFactory.setFactoryNo(facNo);
								this.getReportDao().create(cotFactory);
								cotSeqServiceImpl.saveSeq("factoryNo");
								//seq.saveSeq("facNo");
								// 将新类别添加到已有的map中
								mapShortName.put(temp.toLowerCase(), cotFactory
										.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Supplier exception");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setFactoryId(mapShortName
								.get(temp.toLowerCase()));
						detail.setFactoryShortName(temp);
					}
				}
				// -----------------------------包装信息
				try {
					if (headCtn.getContents().equals("ELE_MOD")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setEleMod(elemod.intValue());
					}
					if (headCtn.getContents().equals("ELE_UNITNUM")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setEleUnitNum(elemod.intValue());
					}
					if (headCtn.getContents().equals("cube") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						detail.setCube(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_L") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxL = Float.parseFloat(dfTwo.format(num));
						} else {
							boxL = num;
						}
						boxLInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxL(boxL);
						detail.setBoxLInch(boxLInch);
					}
					if (headCtn.getContents().equals("BOX_L_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxLInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxLInch = num;
						}
						boxL = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxL(boxL);
						detail.setBoxLInch(boxLInch);

					}
					if (headCtn.getContents().equals("BOX_W") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxW = Float.parseFloat(dfTwo.format(num));
						} else {
							boxW = num;
						}
						boxWInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxW(boxW);
						detail.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_W_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxWInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxWInch = num;
						}
						boxW = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxW(boxW);
						detail.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_H") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxH = Float.parseFloat(dfTwo.format(num));
						} else {
							boxH = num;
						}
						boxHInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxH(boxH);
						detail.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_H_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxHInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxHInch = num;
						}
						boxH = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxH(boxH);
						detail.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_HANDLEH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxHandleH(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_PB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbL(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxPbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbW(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxPbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbH(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxPbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}
					if (headCtn.getContents().equals("BOX_PB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxPbCount(elemod.longValue());
						}
					}

					if (headCtn.getContents().equals("BOX_IB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbL(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxIbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbW(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxIbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbH(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxIbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbL(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxMbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbW(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxMbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbH(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxMbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_OB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObL = Float.parseFloat(rowCtn.trim());
						detail.setBoxObL(boxObL);
						detail.setBoxObLInch(boxObL / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}

					if (headCtn.getContents().equals("BOX_OB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObW = Float.parseFloat(rowCtn.trim());
						detail.setBoxObW(boxObW);
						detail.setBoxObWInch(boxObW / 2.54f);
						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}

					if (headCtn.getContents().equals("BOX_OB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObH = Float.parseFloat(rowCtn.trim());
						detail.setBoxObH(boxObH);
						detail.setBoxObHInch(boxObH / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}
					if (headCtn.getContents().equals("BOX_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxWeigth = Float.parseFloat(rowCtn.trim());
						detail.setBoxWeigth(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_GROSS_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						gWeigh = Float.parseFloat(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BOX_NET_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						nWeigh = Float.parseFloat(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BOX_IB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxIbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_MB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxMbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_OB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							boxObCount = elemod.longValue();
							detail.setBoxObCount(boxObCount);
							float cbm = Float.parseFloat(df.format(boxObL
									* boxObW * boxObH * 0.000001F));
							// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

							if (boxObCount != 0 && cbm != 0) {
								int count20 = (int) ((cubes[0] / cbm) * boxObCount);
								int count40 = (int) ((cubes[2] / cbm) * boxObCount);
								int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
								int count45 = (int) ((cubes[3] / cbm) * boxObCount);
								detail.setBox20Count(new Float(count20));
								detail.setBox40Count(new Float(count40));
								detail.setBox40hqCount(new Float(count40hq));
								detail.setBox45Count(new Float(count45));
							}
						}
					}

					if (headCtn.getContents().equals("LI_RUN")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						float priceProfit = Float.parseFloat(rowCtn.trim());
						detail.setLiRun(priceProfit);
					}
					if (headCtn.getContents().equals("TUI_LV")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						tuiLv = Float.parseFloat(rowCtn.trim());
						detail.setTuiLv(tuiLv);
						if (hsIdTemp != 0) {
							CotEleOther cotEleOther = (CotEleOther) this
									.getReportDao().getById(CotEleOther.class,
											hsIdTemp);
							cotEleOther.setTaxRate(tuiLv);
							List list = new ArrayList();
							list.add(cotEleOther);
							this.getReportDao().updateRecords(list);
						}
					}

					if (headCtn.getContents().equals("PRICE_FAC")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceFac = Float.parseFloat(rowCtn.trim());
						if (priceFac < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("PRICE_OUT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceOut = Float.parseFloat(rowCtn.trim());
						if (priceOut < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("BOX_CBM")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						float cbm = Float.parseFloat(rowCtn.trim());
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装
						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}
					if (headCtn.getContents().equals("BOX_TYPE_ID")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							String[] box = mapBoxTypeCn.get(rowCtn.trim()
									.toLowerCase());
							if (box == null) {
								box = mapBoxTypeEn.get(rowCtn.trim()
										.toLowerCase());
							}
							if (box == null) {
								// 新增一条包装类型,中英文名相同
								CotBoxType cotBoxType = new CotBoxType();
								cotBoxType.setTypeName(rowCtn.trim());
								cotBoxType.setTypeNameEn(rowCtn.trim());
								List<CotBoxType> listType = new ArrayList<CotBoxType>();
								listType.add(cotBoxType);
								try {
									this.getReportDao().saveRecords(listType);
									box = new String[5];
									box[0] = cotBoxType.getId().toString();
									box[1] = null;
									box[2] = null;
									box[3] = null;
									box[4] = null;
									mapBoxTypeCn.put(rowCtn.trim()
											.toLowerCase(), box);
								} catch (DAOException e) {
									e.printStackTrace();
									CotMsgVO cotMsgVO = new CotMsgVO(i, j,
											"Save Packing Way exception!");
									msgList.add(cotMsgVO);
									isSuccess = false;
									break;
								}
							}
							if (box[0] != null) {
								detail.setBoxTypeId(Integer.parseInt(box[0]));
							}
							if (box[1] != null) {
								detail.setBoxIbTypeId(Integer.parseInt(box[1]));
							}
							if (box[2] != null) {
								detail.setBoxMbTypeId(Integer.parseInt(box[2]));
							}
							if (box[3] != null) {
								detail.setBoxObTypeId(Integer.parseInt(box[3]));
							}
							if (box[4] != null) {
								detail.setBoxPbTypeId(Integer.parseInt(box[4]));
							}
						}
					}
				} catch (Exception e) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The number of cell values can only be!");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				if (headCtn.getContents().equals("BOX_UINT") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxUint(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_TDESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxTDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_BDESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxBDesc(rowCtn.trim());
				}

				if (headCtn.getContents().equals("BOX_REMARK")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxRemark(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_REMARK_CN") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxRemarkCn(rowCtn.trim());
				}

				// ---------------------报价信息
				if (headCtn.getContents().equals("PRICE_UINT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object curId = mapCurrency.get(rowCtn.trim()
								.toLowerCase());
						if (curId == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						detail.setPriceFacUint(mapCurrency.get(rowCtn.trim()
								.toLowerCase()));
					}
				}

				if (headCtn.getContents().equals("PRICE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object curId = mapCurrency.get(rowCtn.trim()
								.toLowerCase());
						if (curId == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						detail.setPriceOutUint(mapCurrency.get(rowCtn.trim()
								.toLowerCase()));
					}
				}
			}
			if (isSuccess == true) {
				// 设置序列号
				if (isHaveSort == false) {
					sortTemp++;
					if(detail.getSortNo()==null){
						detail.setSortNo(sortTemp);
					}
				}
				
				// 判断是否是子货号
				if (isChild) {
					detail.setEleFlag(2l);
				}
				// 保存样品信息
				detail.setEleAddTime(now);
				// 设置中英文规格
				if (!eleSize.equals("")) {
					detail.setEleSizeDesc(eleSize);
				} else {
					if(isChangeL)
						detail.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
				}
				if (!eleSizeInch.equals("")) {
					detail.setEleInchDesc(eleSizeInch);
				} else {
					if(isChangeL)
						detail.setEleInchDesc(boxLInch + "*" + boxWInch + "*"
								+ boxHInch);
				}
				// 设置毛净重
				if (gWeigh == 0) {
					Float cfgGross = 0f;
					if (cotEleCfg != null && cotEleCfg.getGrossNum() != null) {
						cfgGross = cotEleCfg.getGrossNum();
					}
					float grossWeight = boxWeigth * boxObCount / 1000
							+ cfgGross;
					detail.setBoxGrossWeigth(grossWeight);
				} else {
					detail.setBoxGrossWeigth(gWeigh);
				}
				if (nWeigh == 0) {
					float netWeight = boxWeigth * boxObCount / 1000;
					detail.setBoxNetWeigth(netWeight);
				} else {
					detail.setBoxNetWeigth(nWeigh);
				}

				// 计算包材价格
				detail = this.getReportDao().calPrice(boxPackMap, detail);

				// 1.excel都没有厂价和外销价(新增时重新计算,覆盖时不计算)
				if (priceFac == 0 && priceOut == 0) {
					if (cover == true && rowChk == true) {
						if (detail.getPriceFac() != null) {
							priceFac = detail.getPriceFac();
						}
						if (detail.getPricePrice() != null) {
							priceOut = detail.getPricePrice();
						}
					} else {
						priceFac = this.sumPriceFac(cotEleCfg
								.getExpessionFacIn(), detail);
						priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(),
								priceFac, detail);
					}
				}
				// 2.excel有厂价没有外销价,只计算外销价
				if (priceFac != 0 && priceOut == 0) {
					if (cover == true && rowChk == true) {
						if (detail.getPriceOut() != null) {
							priceOut = detail.getPriceOut();
						}
					} else {
						priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(),
								priceFac, detail);
					}
				}
				// 3.只有外销价
				if (priceFac == 0 && priceOut != 0) {
					if (cover == true && rowChk == true) {
						if (detail.getPriceFac() != null) {
							priceFac = detail.getPriceFac();
						}
					} else {
						priceFac = this.sumPriceFac(cotEleCfg
								.getExpessionFacIn(), detail);
					}
				}
				detail.setPriceFac(priceFac);
				detail.setPriceOut(priceOut);

				// 新增设置默认锁定外销价
				if (detail.getId() == null) {
					detail.setCheckFlag(1);
				}

				// 取得随机数做KEY
				String rdm = RandomStringUtils.randomNumeric(8);
				mapEleId.put(rdm, detail);
				// 增加成功条数
				successNum++;
			}
			if (rowChk == true) {
				i--;
			} else {
				detailElesBak = new ArrayList();
				for (int m = 0; m < detailEles.size(); m++) {
					CotPriceDetail det = (CotPriceDetail) detailEles.get(m);
					detailElesBak.add(det);
				}
			}

		}
		// 增加影响行数
		CotMsgVO cotMsgVO = new CotMsgVO();
		cotMsgVO.setFlag(0);
		cotMsgVO.setSuccessNum(successNum);
		cotMsgVO.setCoverNum(coverNum);
		cotMsgVO.setFailNum(msgList.size());
		msgList.add(cotMsgVO);
		// 将错误信息存入系统日志
		// this.saveErrorMsgToFile(msgList);
		ctx.getSession().setAttribute("priceReport", mapEleId);
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		file.delete();
		return msgList;
	}

	// 根据文件路径和行号导入excel
	public List<?> updateOneReport(String filename, Integer rowNum,
			Integer orderId, boolean cover, Integer currencyId,
			boolean excelFlag) {
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 格式化样品包装数据
		DecimalFormat df = new DecimalFormat("#.####");
		DecimalFormat dfTwo = new DecimalFormat("#.##");
		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();

		// 先查询所有明细的货号
		List<?> detailEles = this.getReportDao().getPriceDetails(orderId);
		List<CotPriceDetail> detailElesBak = new ArrayList();
		for (int i = 0; i < detailEles.size(); i++) {
			CotPriceDetail det = (CotPriceDetail) detailEles.get(i);
			detailElesBak.add(det);
		}

		// 取得默认的20/40/40HQ/45的柜体积,数据字典没设置的话默认为24/54/68/86)
		WebContext ctx = WebContextFactory.get();
		Float[] cubes = this.getReportDao().getContainerCube();

		// 查询所有包装材料
		Map boxPackMap = this.getReportDao().getAllBoxPacking();

		// 查询所有样品材质(中文KEY)
		Map<String, Integer> mapTypeCn = this.getReportDao().getAllTypeCn();
		// 查询所有样品材质(英文KEY)
		Map<String, Integer> mapTypeEn = this.getReportDao().getAllTypeEn();
		// 查询所有包装类型(中文KEY)
		Map<String, String[]> mapBoxTypeCn = this.getReportDao()
				.getAllBoxTypeByCn();
		// 查询所有包装类型(英文KEY)
		Map<String, String[]> mapBoxTypeEn = this.getReportDao()
				.getAllBoxTypeByEn();
		// 获得tomcat路径
		String classPath = CotReportService.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + "upload/" + filename);
		try {
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setEncoding("ISO-8859-1");
			setting.setLocale(locale);
			workbook = Workbook.getWorkbook(file,setting);
		} catch (Exception e) {
			file.delete();
			return null;
		}
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int i = rowNum;
		// 新建样品对象
		CotPriceDetail detail = new CotPriceDetail();
		// 定义厂家
		CotFactory cotFactory = new CotFactory();
		// 是否有成功标识
		boolean isSuccess = true;
		// 用于计算CBM和CUFT
		float boxObL = 0f;
		float boxObW = 0f;
		float boxObH = 0f;
		// 产品长宽高
		float boxL = 0f;
		float boxW = 0f;
		float boxH = 0f;
		float boxLInch = 0f;
		float boxWInch = 0f;
		float boxHInch = 0f;

		// 单重
		float boxWeigth = 0f;
		float gWeigh = 0f;
		float nWeigh = 0f;

		// 厂价
		Float priceFac = 0f;
		// 外销价
		Float priceOut = 0f;

		String eleSize = "";
		String eleSizeInch = "";
		boolean isChangeL=false;
		// 判断是否有删除最后一行
		boolean isDel = false;
		// 用于计算装箱数
		long boxObCount = 0;
		Integer hsIdTemp = 0;
		float tuiLv = 0f;

		// 判断是否是子货号
		boolean isChild = false;

		ctx.getSession().removeAttribute("priceReport");
		TreeMap<String, CotPriceDetail> mapObj = new TreeMap<String, CotPriceDetail>();
		boolean rowChk = false;
		for (int h = 0; h < 1; h++) {
			for (int j = 0; j < sheet.getColumns(); j++) {

				// 表头
				Cell headCtn = sheet.getCell(j, 1);
				Cell row = null;
				try {
					row = sheet.getCell(j, i);
				} catch (Exception e) {
					isDel = true;
					isSuccess = false;
					break;
				}

				String rowCtn = row.getContents();
				
				//如果没转换成数字cell,默认的最长小数位是3位
				if (row.getType() == CellType.NUMBER
						|| row.getType() == CellType.NUMBER_FORMULA) {
					NumberCell nc = (NumberCell) row;
					rowCtn =df.format(nc.getValue());
				}
				
				if (headCtn.getContents().equals("ELE_ID")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. can not be empty");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The length of Article No. can not exceed 100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (cover == true) {
						boolean ck = false;
						for (int k = 0; k < detailElesBak.size(); k++) {
							CotPriceDetail priceDetail = (CotPriceDetail) detailElesBak
									.get(k);
							if (rowCtn.trim().toLowerCase().equals(
									priceDetail.getEleId().toLowerCase())) {
								ck = true;
								rowChk = true;
								detail = priceDetail;
								detailElesBak.remove(priceDetail);
								break;
							}
						}
						if (ck == false && rowChk == false) {
							rowChk = false;
							detail = new CotPriceDetail();
							detail.setEleId(rowCtn.trim().toUpperCase());
							if (cotEleCfg != null) {
								// 设置样品默认值
								detail = this.getReportDao().setPriceDefault(
										cotEleCfg, detail);
							}
							if (excelFlag == false) {
								detail.setPriceOutUint(currencyId);
							}
						}
						if (ck == false && rowChk == true) {
							isSuccess = false;
							rowChk = false;
							break;
						}
					} else {
						detail = new CotPriceDetail();
						detail.setEleId(rowCtn.trim().toUpperCase());
						if (cotEleCfg != null) {
							// 设置样品默认值
							detail = this.getReportDao().setPriceDefault(
									cotEleCfg, detail);
						}
						if (excelFlag == false) {
							detail.setPriceOutUint(currencyId);
						}
					}
					if (detail.getBoxObCount() != null) {
						boxObCount = detail.getBoxObCount();
					}
					//处理中英文规格
					if (detail.getBoxL()!= null) {
						boxL = detail.getBoxL();
					}
					if (detail.getBoxW()!= null) {
						boxW = detail.getBoxW();
					}
					if (detail.getBoxH()!= null) {
						boxH = detail.getBoxH();
					}
					if (detail.getBoxLInch()!= null) {
						boxLInch = detail.getBoxLInch();
					}
					if (detail.getBoxWInch()!= null) {
						boxWInch = detail.getBoxWInch();
					}
					if (detail.getBoxHInch()!= null) {
						boxHInch = detail.getBoxHInch();
					}
				}
				if (headCtn.getContents().equals("HS_ID") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					Integer flag = this.getReportDao().isExistHsId(
							rowCtn.trim());
					if (flag == null) {
						CotEleOther cotEleOther = new CotEleOther();
						cotEleOther.setHscode(rowCtn.trim());
						cotEleOther.setCnName(rowCtn.trim());
						if (tuiLv != 0) {
							cotEleOther.setTaxRate(tuiLv);
						}
						try {
							this.getReportDao().create(cotEleOther);
							detail.setEleHsid(cotEleOther.getId());
							hsIdTemp = cotEleOther.getId();
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存海关编码异常");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						detail.setEleHsid(flag);
						hsIdTemp = flag;
					}
				}

				if (headCtn.getContents().equals("SORT_NO") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					// 覆盖的时候,不覆盖序列号
					if (detail.getId() == null) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setSortNo(elemod.intValue());
					}
				}

				if (headCtn.getContents().equals("ELE_NAME") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleName(rowCtn.trim());
				}

				if (headCtn.getContents().equals("ELE_TYPENAME_LV2")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleTypenameLv2(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_NAME_EN")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					// 如果英文名超过200长度,截取
					if (rowCtn.trim().length() > 200) {
						detail.setEleNameEn(rowCtn.trim().substring(0, 200));
					} else {
						detail.setEleNameEn(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_FLAG")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						if (rowCtn.trim().equals("套件")) {
							detail.setEleFlag(1l);
						}
						if (rowCtn.trim().equals("单件")) {
							detail.setEleFlag(0l);
						}
						if (rowCtn.trim().equals("组件")) {
							detail.setEleFlag(3l);
						}
					}
				}
				if (headCtn.getContents().equals("ELE_PARENT")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					Integer flag = this.getReportDao().isExistEleId(
							rowCtn.trim().toLowerCase());
					if (flag == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Parent Article No. does not exist!");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						detail.setEleParentId(flag);
						detail.setEleParent(rowCtn.trim());
						detail.setEleFlag(2l);
						isChild = true;
					}
				}
				if (headCtn.getContents().equals("CUST_NO") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setCustNo(rowCtn.trim());
				}
				if (headCtn.getContents().equals("FACTORY_NO")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setFactoryNo(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_DESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FROM") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleFrom(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FACTORY")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleFactory(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_COL") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleCol(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BARCODE") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					if (rowCtn.trim().length() > 30) {
						detail.setBarcode(rowCtn.trim().substring(0, 30));
					} else {
						detail.setBarcode(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_COMPOSETYPE")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleComposeType(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleUnit(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_GRADE") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setEleGrade(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FOR_PERSON")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleForPerson(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_PRO_TIME")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (row.getType() == CellType.DATE) {
						DateCell dc = (DateCell) row;
						detail.setEleProTime(new java.sql.Date(dc.getDate()
								.getTime()));
					} else {
						try {
							java.util.Date date = sdf.parse(rowCtn.trim());
							detail.setEleProTime(new java.sql.Date(date
									.getTime()));
						} catch (ParseException e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Date format is incorrect");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
				}
				if (headCtn.getContents().equals("ELE_SIZE_DESC")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					eleSize = rowCtn.trim();
				}
				if (headCtn.getContents().equals("TAO_UNIT") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setTaoUnit(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_INCH_DESC")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					eleSizeInch = rowCtn.trim();
				}
				if (headCtn.getContents().equals("ELE_REMARK")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().length() > 500) {
						detail.setEleRemark(rowCtn.trim().substring(0, 500));
					} else {
						detail.setEleRemark(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_TYPENAME_LV1")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						// 去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");

						Integer typeId = mapTypeCn.get(temp.toLowerCase());
						if (typeId == null) {
							typeId = mapTypeEn.get(temp.toLowerCase());
						}
						if (typeId == null) {
							// 新增一条材质,中英文名相同
							CotTypeLv1 cotTypeLv1 = new CotTypeLv1();
							cotTypeLv1.setTypeName(temp);
							cotTypeLv1.setTypeEnName(temp);
							List<CotTypeLv1> listType = new ArrayList<CotTypeLv1>();
							listType.add(cotTypeLv1);
							try {
								this.getReportDao().saveRecords(listType);
								typeId = cotTypeLv1.getId();
								mapTypeCn.put(temp.toLowerCase(), typeId);
							} catch (DAOException e) {
								e.printStackTrace();
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存样品材质异常!");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setEleTypenameLv1(temp);
						detail.setEleTypeidLv1(typeId);
					}
				}

				if (headCtn.getContents().equals("ELE_TYPEID_LV2")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Integer flag = this.getReportDao().isExistEleTypeName(
								rowCtn.trim());
						CotTypeLv2 cotTypeLv2 = null;
						if (flag == null) {
							cotTypeLv2 = new CotTypeLv2();
							cotTypeLv2.setTypeName(rowCtn.trim());
							try {
								this.getReportDao().create(cotTypeLv2);
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存产品分类异常");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						} else {
							cotTypeLv2 = (CotTypeLv2) this.getReportDao()
									.getById(CotTypeLv2.class, flag);
						}
						detail.setEleTypeidLv2(cotTypeLv2.getId());
					}
				}

				if (headCtn.getContents().equals("SHORT_NAME")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Integer factoryFlag = this.getReportDao()
								.isExistFactoryShortName(rowCtn.trim());
						Integer fId = 0;
						if (factoryFlag == null) {
							cotFactory.setFactoryName(rowCtn.trim());
							cotFactory.setShortName(rowCtn.trim());
							cotFactory.setFactroyTypeidLv1(1);
							try {
								CotSeqService cotSeqServiceImpl=new CotSeqServiceImpl();
								String facNo =cotSeqServiceImpl.getFacNo();
//								String facNo = seq.getAllSeqByType("facNo",
//										null);
								cotFactory.setFactoryNo(facNo);
								this.getReportDao().create(cotFactory);
								cotSeqServiceImpl.saveSeq("factoryNo");
							//	seq.saveSeq("facNo");
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Supplier exception");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
							fId = cotFactory.getId();
						} else {
							fId = factoryFlag;
						}
						detail.setFactoryId(fId);
						detail.setFactoryShortName(rowCtn.trim());
					}

				}
				// -----------------------------包装信息
				try {
					if (headCtn.getContents().equals("ELE_MOD")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setEleMod(elemod.intValue());
					}
					if (headCtn.getContents().equals("ELE_UNITNUM")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setEleUnitNum(elemod.intValue());
					}
					if (headCtn.getContents().equals("cube") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						detail.setCube(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_L") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxL = Float.parseFloat(dfTwo.format(num));
						} else {
							boxL = num;
						}
						boxLInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxL(boxL);
						detail.setBoxLInch(boxLInch);
					}
					if (headCtn.getContents().equals("BOX_L_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxLInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxLInch = num;
						}
						boxL = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxL(boxL);
						detail.setBoxLInch(boxLInch);

					}
					if (headCtn.getContents().equals("BOX_W") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxW = Float.parseFloat(dfTwo.format(num));
						} else {
							boxW = num;
						}
						boxWInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxW(boxW);
						detail.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_W_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxWInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxWInch = num;
						}
						boxW = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxW(boxW);
						detail.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_H") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxH = Float.parseFloat(dfTwo.format(num));
						} else {
							boxH = num;
						}
						boxHInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxH(boxH);
						detail.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_H_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxHInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxHInch = num;
						}
						boxH = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxH(boxH);
						detail.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_HANDLEH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxHandleH(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_PB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbL(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxPbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbW(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxPbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbH(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxPbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}
					if (headCtn.getContents().equals("BOX_PB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxPbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_IB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbL(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxIbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbW(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxIbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbH(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxIbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbL(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxMbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbW(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxMbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbH(Float.parseFloat(rowCtn.trim()));
						detail
								.setBoxMbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_OB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObL = Float.parseFloat(rowCtn.trim());
						detail.setBoxObL(boxObL);
						detail.setBoxObLInch(boxObL / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}

					}

					if (headCtn.getContents().equals("BOX_OB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObW = Float.parseFloat(rowCtn.trim());
						detail.setBoxObW(boxObW);
						detail.setBoxObWInch(boxObW / 2.54f);
						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}

					if (headCtn.getContents().equals("BOX_OB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObH = Float.parseFloat(rowCtn.trim());
						detail.setBoxObH(boxObH);
						detail.setBoxObHInch(boxObH / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}
					if (headCtn.getContents().equals("BOX_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxWeigth = Float.parseFloat(rowCtn.trim());
						detail.setBoxWeigth(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_GROSS_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						gWeigh = Float.parseFloat(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BOX_NET_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						nWeigh = Float.parseFloat(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BOX_IB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxIbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_MB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxMbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_OB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							boxObCount = elemod.longValue();
							detail.setBoxObCount(boxObCount);
							float cbm = Float.parseFloat(df.format(boxObL
									* boxObW * boxObH * 0.000001F));
							// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

							if (boxObCount != 0 && cbm != 0) {
								int count20 = (int) ((cubes[0] / cbm) * boxObCount);
								int count40 = (int) ((cubes[2] / cbm) * boxObCount);
								int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
								int count45 = (int) ((cubes[3] / cbm) * boxObCount);
								detail.setBox20Count(new Float(count20));
								detail.setBox40Count(new Float(count40));
								detail.setBox40hqCount(new Float(count40hq));
								detail.setBox45Count(new Float(count45));
							}
						}
					}

					if (headCtn.getContents().equals("LI_RUN")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						float priceProfit = Float.parseFloat(rowCtn.trim());
						detail.setLiRun(priceProfit);
					}

					if (headCtn.getContents().equals("TUI_LV")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						tuiLv = Float.parseFloat(rowCtn.trim());
						detail.setTuiLv(tuiLv);
						if (hsIdTemp != 0) {
							CotEleOther cotEleOther = (CotEleOther) this
									.getReportDao().getById(CotEleOther.class,
											hsIdTemp);
							cotEleOther.setTaxRate(tuiLv);
							List list = new ArrayList();
							list.add(cotEleOther);
							this.getReportDao().updateRecords(list);
						}
					}

					if (headCtn.getContents().equals("PRICE_FAC")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceFac = Float.parseFloat(rowCtn.trim());
						if (priceFac < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("PRICE_OUT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceOut = Float.parseFloat(rowCtn.trim());
						if (priceOut < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("BOX_CBM")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						float cbm = Float.parseFloat(rowCtn.trim());
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装
						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}
					if (headCtn.getContents().equals("BOX_TYPE_ID")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							String[] box = mapBoxTypeCn.get(rowCtn.trim()
									.toLowerCase());
							if (box == null) {
								box = mapBoxTypeEn.get(rowCtn.trim()
										.toLowerCase());
							}
							if (box == null) {
								// 新增一条包装类型,中英文名相同
								CotBoxType cotBoxType = new CotBoxType();
								cotBoxType.setTypeName(rowCtn.trim());
								cotBoxType.setTypeNameEn(rowCtn.trim());
								List<CotBoxType> listType = new ArrayList<CotBoxType>();
								listType.add(cotBoxType);
								try {
									this.getReportDao().saveRecords(listType);
									box = new String[5];
									box[0] = cotBoxType.getId().toString();
									box[1] = null;
									box[2] = null;
									box[3] = null;
									box[4] = null;
									mapBoxTypeCn.put(rowCtn.trim()
											.toLowerCase(), box);
								} catch (DAOException e) {
									e.printStackTrace();
									CotMsgVO cotMsgVO = new CotMsgVO(i, j,
											"Save Packing Way exception!!");
									msgList.add(cotMsgVO);
									isSuccess = false;
									break;
								}
							}
							if (box[0] != null) {
								detail.setBoxTypeId(Integer.parseInt(box[0]));
							}
							if (box[1] != null) {
								detail.setBoxIbTypeId(Integer.parseInt(box[1]));
							}
							if (box[2] != null) {
								detail.setBoxMbTypeId(Integer.parseInt(box[2]));
							}
							if (box[3] != null) {
								detail.setBoxObTypeId(Integer.parseInt(box[3]));
							}
							if (box[4] != null) {
								detail.setBoxPbTypeId(Integer.parseInt(box[4]));
							}
						}
					}
				} catch (Exception e) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The number of cell values can only be!");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				if (headCtn.getContents().equals("BOX_UINT") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxUint(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_TDESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxTDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_BDESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxBDesc(rowCtn.trim());
				}

				if (headCtn.getContents().equals("BOX_REMARK")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxRemark(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_REMARK_CN") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					detail.setBoxRemarkCn(rowCtn.trim());
				}
				// ---------------------报价信息
				if (headCtn.getContents().equals("PRICE_UINT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						// 判断该币种是否存在
						Integer flag = this.getReportDao().isExistCurUnit(
								rowCtn.trim());
						if (flag == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						} else {
							detail.setPriceFacUint(flag);
						}
					}

				}

				if (headCtn.getContents().equals("PRICE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						// 判断该币种是否存在
						Integer flag = this.getReportDao().isExistCurUnit(
								rowCtn.trim());
						if (flag == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						} else {
							detail.setPriceOutUint(flag);
						}
					}
				}

			}
			if (isSuccess == true) {

				// 判断是否是子货号
				if (isChild) {
					detail.setEleFlag(2l);
				}
				detail.setEleAddTime(now);
				// 设置中英文规格
				if (!eleSize.equals("")) {
					detail.setEleSizeDesc(eleSize);
				} else {
					if(isChangeL)
						detail.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
				}
				if (!eleSizeInch.equals("")) {
					detail.setEleInchDesc(eleSizeInch);
				} else {
					if(isChangeL)
						detail.setEleInchDesc(boxLInch + "*" + boxWInch + "*"
								+ boxHInch);
				}
				// 设置毛净重
				if (gWeigh == 0) {
					Float cfgGross = 0f;
					if (cotEleCfg != null && cotEleCfg.getGrossNum() != null) {
						cfgGross = cotEleCfg.getGrossNum();
					}
					float grossWeight = boxWeigth * boxObCount / 1000
							+ cfgGross;
					detail.setBoxGrossWeigth(grossWeight);
				} else {
					detail.setBoxGrossWeigth(gWeigh);
				}
				if (nWeigh == 0) {
					float netWeight = boxWeigth * boxObCount / 1000;
					detail.setBoxNetWeigth(netWeight);
				} else {
					detail.setBoxNetWeigth(nWeigh);
				}

				// 计算包材价格
				detail = this.getReportDao().calPrice(boxPackMap, detail);

				// 1.excel都没有厂价和外销价(新增时重新计算,覆盖时不计算)
				if (priceFac == 0 && priceOut == 0) {
					if (cover == true && rowChk == true) {
						if (detail.getPriceFac() != null) {
							priceFac = detail.getPriceFac();
						}
						if (detail.getPricePrice() != null) {
							priceOut = detail.getPricePrice();
						}
					} else {
						priceFac = this.sumPriceFac(cotEleCfg
								.getExpessionFacIn(), detail);
						priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(),
								priceFac, detail);
					}
				}
				// 2.excel有厂价没有外销价,只计算外销价
				if (priceFac != 0 && priceOut == 0) {
					if (cover == true && rowChk == true) {
						if (detail.getPriceOut() != null) {
							priceOut = detail.getPriceOut();
						}
					} else {
						priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(),
								priceFac, detail);
					}
				}
				// 3.只有外销价
				if (priceFac == 0 && priceOut != 0) {
					if (cover == true && rowChk == true) {
						if (detail.getPriceFac() != null) {
							priceFac = detail.getPriceFac();
						}
					} else {
						priceFac = this.sumPriceFac(cotEleCfg
								.getExpessionFacIn(), detail);
					}
				}
				detail.setPriceFac(priceFac);
				detail.setPriceOut(priceOut);
				// 新增设置默认锁定外销价
				if (detail.getId() == null) {
					detail.setCheckFlag(1);
				}

				// 取得随机数做KEY
				String rdm = RandomStringUtils.randomNumeric(8);
				mapObj.put(rdm, detail);
				ctx.getSession().setAttribute("priceReport", mapObj);
				successNum++;
			}
			if (rowChk == true) {
				h = -1;
			}
		}

		if (!isDel) {
			// 增加影响行数
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(0);
			cotMsgVO.setSuccessNum(successNum);
			cotMsgVO.setFailNum(msgList.size());
			msgList.add(cotMsgVO);
			// 将错误信息存入系统日志
			// this.saveErrorMsgToFile(msgList);
		}
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		file.delete();
		return msgList;
	}

	// 清空excel的缓存
	public void removeExcelSession() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("priceReport");
	}

	// 清空Pan的缓存
	public void removePanSession() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("CheckMachine");
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}

	// 判断明细中是否存在该货号
	public boolean checkIsExistEle(Integer mainId, String eleId) {
		String sql = "select obj.id from CotPriceDetail obj from where obj.priceId="
				+ mainId
				+ " and upper(obj.eleId)='"
				+ eleId.toUpperCase()
				+ "'";
		List list = this.getPriceDao().find(sql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 根据客户编号查找客户编号，简称，和联系人
	public String[] getTeCustById(Integer id) {
		String sql = "select obj.id,obj.customerShortName,obj.priContact,obj.customerEmail from CotCustomer obj where obj.id="
				+ id;
		List<?> list = this.getPriceDao().find(sql);
		String[] str = new String[4];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			if (obj[1] != null) {
				str[1] = obj[1].toString();
			} else {
				str[1] = "";
			}
			if (obj[2] != null) {
				str[2] = obj[2].toString();
			} else {
				str[2] = "";
			}
			if (obj[3] != null) {
				str[3] = obj[3].toString();
			} else {
				str[3] = "";
			}
		}
		return str;
	}

	// 根据员工编号获得员工编号和名称
	public String[] getTeEmpsById(Integer empId) {
		String sql = "select obj.id,obj.empsName from CotEmps obj where obj.id="
				+ empId;
		List list = this.getPriceDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
	}

	// 根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id) {
		String sql = "select obj.id,obj.targetPortEnName from CotTargetPort obj where obj.id="
				+ id;
		List<?> list = this.getPriceDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			if (obj[1] != null) {
				str[1] = obj[1].toString();
			} else {
				str[1] = "";
			}
		}
		return str;
	}

	// 根据起运港港编号获得起运港名称
	public String[] getShipPortNameById(Integer id) {
		String sql = "select obj.id,obj.shipPortNameEn from CotShipPort obj where obj.id="
				+ id;
		List<?> list = this.getPriceDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			if (obj[1] != null) {
				str[1] = obj[1].toString();
			} else {
				str[1] = "";
			}
		}
		return str;
	}

	// 查询VO记录
	public List<?> getPriceVOList(QueryInfo queryInfo) {
		List<CotPriceVO> listVo = new ArrayList<CotPriceVO>();
		try {
			List<?> list = this.getPriceDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotPriceVO priceVO = new CotPriceVO();
				Object[] obj = (Object[]) list.get(i);
				priceVO.setId((Integer) obj[0]);
				priceVO.setPriceTime((Timestamp) obj[1]);
				priceVO.setCustomerShortName((String) obj[2]);
				priceVO.setPriceNo((String) obj[3]);
				priceVO.setEmpsName((String) obj[4]);
				priceVO.setClauseId((Integer) obj[5]);
				priceVO.setCurrencyId((Integer) obj[6]);
				priceVO.setSituationId((Integer) obj[7]);
				priceVO.setValidMonths((Integer) obj[8]);
				priceVO.setPriceStatus((Long) obj[9]);
				priceVO.setPriceRate((Float) obj[10]);
				priceVO.setPriceCompose((String) obj[11]);
				priceVO.setCustId((Integer) obj[12]);
				listVo.add(priceVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 根据客号查找报价明细表中的货号(取最近报价时间)
	public Object[] findEleByCustNo(String custNo, Integer custId,
			String clauseId, String pTime) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		java.util.Date ptimeDate = null;
		try {
			 ptimeDate = format1.parse(pTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Object[] rtn = new Object[2];

		Object[] obj = new Object[4];
		obj[0] = custId;
		obj[1] = custNo;
		String str = " and p.clauseId=?";
		if ("".equals(clauseId)) {
			obj[2] = "0";
			str = " and p.clauseId is null and 0=?";
		} else {
			obj[2] = Integer.parseInt(clauseId);
		}
		obj[3] = ptimeDate;
		// 报价
		String hql = "select obj,p.priceTime from CotPrice p,CotPriceDetail obj,CotCustomer c "
				+ "where obj.priceId=p.id and p.custId=c.id and c.id=?"
				+ " and obj.custNo=?"
				+ str
				+ " and p.priceTime<=?"
				+ " order by p.priceTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> list = this.getPriceDao().queryForLists(hql, obj);
		// 订单
		Object[] obj3 = new Object[4];
		obj3[0] = custId;
		obj3[1] = custNo;
		String str2 = " and p.clauseTypeId=?";
		if ("".equals(clauseId)) {
			obj3[2] = "0";
			str2 = " and p.clauseTypeId is null and 0=?";
		} else {
			obj3[2] = Integer.parseInt(clauseId);
		}
		obj3[3] = ptimeDate;
		String hqlOrder = "select obj,p.orderTime from CotOrder p,CotOrderDetail obj,CotCustomer c "
				+ "where obj.orderId=p.id and p.custId=c.id and c.id=?"
				+ " and obj.custNo=?"
				+ str2
				+ " and p.orderTime<=?"
				+ " order by p.orderTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listOrder = this.getPriceDao().queryForLists(hqlOrder, obj3);

		Object[] obj2 = new Object[3];
		obj2[0] = custId;
		obj2[1] = custNo;
		obj2[2] = ptimeDate;
		// 送样
		String hqlGiven = "select obj,p.givenTime from CotGiven p,CotGivenDetail obj,CotCustomer c "
				+ "where obj.givenId=p.id and p.custId=c.id and c.id=?"
				+ " and obj.custNo=?"
				+ " and p.givenTime<=?"
				+ " order by p.givenTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listGiven = this.getPriceDao().queryForLists(hqlGiven, obj2);

		Timestamp priceTime = null;
		Timestamp orderTime = null;
		Timestamp givenTime = null;
		TreeMap sort = new TreeMap();
		if (list.size() > 0) {
			Object[] detail = (Object[]) list.get(0);
			if (detail[1] != null) {
				priceTime = (Timestamp) detail[1];
				sort.put(priceTime.getTime(), 1);
			}
		}
		if (listOrder.size() > 0) {
			Object[] detailOrder = (Object[]) listOrder.get(0);
			if (detailOrder[1] != null) {
				orderTime = (Timestamp) detailOrder[1];
				sort.put(orderTime.getTime(), 2);
			}
		}
		if (listGiven.size() > 0) {
			Object[] detailGiven = (Object[]) listGiven.get(0);
			if (detailGiven[1] != null) {
				givenTime = (Timestamp) detailGiven[1];
				sort.put(givenTime.getTime(), 3);
			}
		}
		if (sort.keySet().size() == 0) {
			return null;
		}
		Integer flag = (Integer) sort.get(sort.lastKey());
		if (flag == 1) {
			Object[] objTemp = (Object[]) list.get(0);
			CotPriceDetail detail = (CotPriceDetail) objTemp[0];
//			// 填充厂家简称
//			List<?> facList = this.getPriceDao().getRecords("CotFactory");
//			for (int i = 0; i < facList.size(); i++) {
//				CotFactory cotFactory = (CotFactory) facList.get(i);
//				if (detail.getFactoryId() != null
//						&& cotFactory.getId().intValue() == detail
//								.getFactoryId().intValue()) {
//					detail.setFactoryShortName(cotFactory.getShortName());
//				}
//			}
			rtn[0] = 1;
			rtn[1] = detail;
			return rtn;
		} else if (flag == 2) {
			Object[] objTemp = (Object[]) listOrder.get(0);
			CotOrderDetail detail = (CotOrderDetail) objTemp[0];
//			// 填充厂家简称
//			// List<?> facList = getDicList("factory");
//			List<?> facList = this.getPriceDao().getRecords("CotFactory");
//			for (int i = 0; i < facList.size(); i++) {
//				CotFactory cotFactory = (CotFactory) facList.get(i);
//				if (detail.getFactoryId() != null
//						&& cotFactory.getId().intValue() == detail
//								.getFactoryId().intValue()) {
//					detail.setFactoryShortName(cotFactory.getShortName());
//				}
//			}
			rtn[0] = 2;
			rtn[1] = detail;
			return rtn;
		} else {
			Object[] objTemp = (Object[]) listGiven.get(0);
			CotGivenDetail detail = (CotGivenDetail) objTemp[0];
//			// 填充厂家简称
//			// List<?> facList = getDicList("factory");
//			List<?> facList = this.getPriceDao().getRecords("CotFactory");
//			for (int i = 0; i < facList.size(); i++) {
//				CotFactory cotFactory = (CotFactory) facList.get(i);
//				if (detail.getFactoryId() != null
//						&& cotFactory.getId().intValue() == detail
//								.getFactoryId().intValue()) {
//					detail.setFactoryShortName(cotFactory.getShortName());
//				}
//			}
			rtn[0] = 3;
			rtn[1] = detail;
			return rtn;
		}
	}

	// 将该货号对应的样品转成报价明细
	public CotPriceDetail changeEleToPriceDetail(String eleId) {
		// 查找样品对象
		String hql = "from CotElementsNew obj where obj.eleId = '" + eleId
				+ "'";
		List<?> list = this.getPriceDao().find(hql);
		if (list.size() == 0) {
			return null;
		}
		CotElementsNew cotElements = (CotElementsNew) list.get(0);
		// 新建报价明细对象
		CotPriceDetail cotPriceDetail = new CotPriceDetail();
		// 1.使用反射获取对象的所有属性名称
		String[] propEle = ReflectionUtils
				.getDeclaredFields(CotElementsNew.class);

		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter, Date.class);
		// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
				new PropertyUtilsBean());
		boolean flag = true;
		for (int i = 0; i < propEle.length; i++) {
			try {
				String value = beanUtils.getProperty(cotElements, propEle[i]);
				if ("cotPictures".equals(propEle[i])
						|| "cotFile".equals(propEle[i])
						|| "childs".equals(propEle[i])
						|| "picImg".equals(propEle[i])
						|| "cotPriceFacs".equals(propEle[i])
						|| "cotElePrice".equals(propEle[i])
						|| "cotEleFittings".equals(propEle[i])
						|| "cotElePacking".equals(propEle[i])) {
					continue;
				}
				if (value != null) {
					beanUtils.setProperty(cotPriceDetail, propEle[i], value);
				}
			} catch (Exception e) {
				logger.error(propEle[i] + ":转换错误!");
				flag = false;
				break;
			}
		}
		if (flag == false) {
			return null;
		} else {
			// List facList = getDicList("factory");
			List<?> facList = this.getPriceDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (cotPriceDetail.getFactoryId() != null
						&& cotFactory.getId().intValue() == cotPriceDetail
								.getFactoryId().intValue()) {
					cotPriceDetail.setFactoryShortName(cotFactory
							.getShortName());
				}
			}
			return cotPriceDetail;
		}
	}

	// 保存主报价单
	public Integer saveByExcel(CotPrice cotPrice, String priceTime)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			cotPrice.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
			if (priceTime != null && !"".equals(priceTime)) {
				cotPrice.setPriceTime(new Timestamp(sdf.parse(priceTime)
						.getTime()));// 报价时间
			}
			cotPrice.setAddPerson(cotEmps.getEmpsName());// 操作人
			cotPrice.setEmpId(cotEmps.getId());// 操作人编号
			if (cotPrice.getPriceRate() != null && cotPrice.getPriceRate() != 0) {
			} else {
				CotCurrency cotCurrency = (CotCurrency) this.getPriceDao()
						.getById(CotCurrency.class, cotPrice.getCurrencyId());
				cotPrice.setPriceRate(cotCurrency.getCurRate());// 汇率
			}
			if (cotPrice.getId() == null) // 新增时更新客户序列号
			{
				// 更新客户序列号
//				this.getCustService().updateCustSeqByType(cotPrice.getCustId(),
//						"price", cotPrice.getPriceTime().toString());
				// 更新全局序列号
//				SetNoServiceImpl setNoService = new SetNoServiceImpl();
//				setNoService.saveSeq("price", cotPrice.getPriceTime()
//						.toString());
				CotSeqService cotSeqService=new CotSeqServiceImpl();
				cotSeqService.saveCustSeq(cotPrice.getCustId(), "price",cotPrice.getPriceTime().toString());
				cotSeqService.saveSeq("price");
			}
			List<CotPrice> list = new ArrayList<CotPrice>();
			list.add(cotPrice);
			this.getPriceDao().saveOrUpdateRecords(list);
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主报价单成功");
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotPrice.getPriceNo());
			sysLogService.addSysLogByObj(cotSyslog);
			// 保存明细
			this.saveDetail(cotPrice.getId(), cotPrice.getCurrencyId(),
					cotPrice.getCustId());

			return cotPrice.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存报价单出错！");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主报价单失败:失败原因:" + e.getMessage());
			cotSyslog.setOpModule("price");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotPrice.getPriceNo());
			sysLogService.addSysLogByObj(cotSyslog);
			throw new Exception("保存报价单出错");
		}
	}

	// 保存明细
	public void saveDetail(Integer priceId, Integer currencyId, Integer custId) {
		Object obj = SystemUtil.getObjBySession(null, "excelTempPrice");
		Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
		List<CotPriceDetail> records = new ArrayList<CotPriceDetail>();
		List<CotPricePic> imgList = new ArrayList<CotPricePic>();

		if (priceMap != null) {
			Iterator<?> it = priceMap.keySet().iterator();
			while (it.hasNext()) {
				String rdm = (String) it.next();
				CotPriceDetail cotPriceDetail = priceMap.get(rdm);
				if (cotPriceDetail.getPriceId() == null
						|| (cotPriceDetail.getPriceId() != null && cotPriceDetail
								.getPriceId().intValue() == priceId.intValue())) {
					// 表格下拉框选择"请选择"时
					if (cotPriceDetail.getFactoryId() != null
							&& cotPriceDetail.getFactoryId() == 0) {
						cotPriceDetail.setFactoryId(null);
					}
					cotPriceDetail.setCurrencyId(currencyId);
					cotPriceDetail.setPriceId(priceId);
					cotPriceDetail.setEleAddTime(new Date(System
							.currentTimeMillis()));// 添加时间
					// 判断利润率是否超过数据库范围
					Float liRes = cotPriceDetail.getLiRun();
					if (liRes == null) {
						cotPriceDetail.setLiRun(0f);
					} else {
						if (liRes <= -1000) {
							cotPriceDetail.setLiRun(-999f);
						}
						if (liRes >= 1000) {
							cotPriceDetail.setLiRun(999f);
						}
					}
					records.add(cotPriceDetail);
				}
			}
		}
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		this.getPriceDao().saveOrUpdateRecords(records);
		// 保存到系统日记表
		CotSyslog cotSyslog = new CotSyslog();
		cotSyslog.setEmpId(empId);
		cotSyslog.setOpMessage("excel导入后保存报价单明细成功");
		cotSyslog.setOpModule("price");
		cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
		cotSyslog.setOpType(1);
		sysLogService.addSysLogByObj(cotSyslog);
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		byte[] zwtpByte = this.getZwtpPic();
		for (int i = 0; i < records.size(); i++) {
			CotPriceDetail cotPriceDetail = records.get(i);
			String type = cotPriceDetail.getType();
			Integer srcId = cotPriceDetail.getSrcId();
			if (type != null) {
				CotPricePic pricePic = null;
				// 查询该明细是否已经有图片
				if (cotPriceDetail.getId() != null) {
					String hql = "from CotPricePic obj where obj.fkId="
							+ cotPriceDetail.getId();
					List ls = this.getPriceDao().find(hql);
					if (ls != null && ls.size() > 0) {
						pricePic = (CotPricePic) ls.get(0);
					} else {
						pricePic = new CotPricePic();
					}
				} else {
					pricePic = new CotPricePic();
				}
				if (type.equals("ele")) {
					CotElePic cotElePic = impOpService
							.getElePicImgByEleId(srcId);
					pricePic.setEleId(cotElePic.getEleId());
					pricePic.setPicImg(cotElePic.getPicImg());
					pricePic.setPicSize(cotElePic.getPicImg().length);
					pricePic.setPicName(cotElePic.getEleId());
				}
				if (type.equals("price")) {
					CotPricePic cotPricePic = impOpService.getPricePic(srcId);
					pricePic.setEleId(cotPricePic.getEleId());
					pricePic.setPicImg(cotPricePic.getPicImg());
					pricePic.setPicSize(cotPricePic.getPicImg().length);
					pricePic.setPicName(cotPricePic.getEleId());
				}
				if (type.equals("order")) {
					CotOrderPic cotOrderPic = impOpService.getOrderPic(srcId);
					pricePic.setEleId(cotOrderPic.getEleId());
					pricePic.setPicImg(cotOrderPic.getPicImg());
					pricePic.setPicSize(cotOrderPic.getPicImg().length);
					pricePic.setPicName(cotOrderPic.getEleId());
				}
				if (type.equals("given")) {
					CotGivenPic cotGivenPic = impOpService.getGivenPic(srcId);
					pricePic.setEleId(cotGivenPic.getEleId());
					pricePic.setPicImg(cotGivenPic.getPicImg());
					pricePic.setPicSize(cotGivenPic.getPicImg().length);
					pricePic.setPicName(cotGivenPic.getEleId());
				}
				if (type.equals("none")) {
					if(pricePic.getPicImg()==null){
						// excel导入后查找样品档案有没有该货号,有就取样品档案的图片
						String strSql = "select pic from CotElementsNew obj,CotElePic pic where obj.id=pic.fkId and obj.eleId = '"
							+ cotPriceDetail.getEleId() + "'";
						List list = this.getPriceDao().find(strSql);
						if (list != null && list.size() > 0) {
							CotElePic cotElePic = (CotElePic) list.get(0);
							pricePic.setEleId(cotElePic.getEleId());
							pricePic.setPicImg(cotElePic.getPicImg());
							pricePic.setPicSize(cotElePic.getPicImg().length);
							pricePic.setPicName(cotElePic.getEleId());
						} else {
							pricePic.setEleId(cotPriceDetail.getEleId());
							pricePic.setPicImg(zwtpByte);
							pricePic.setPicSize(zwtpByte.length);
							pricePic.setPicName(cotPriceDetail.getEleId());
						}
					}
				}
				pricePic.setFkId(cotPriceDetail.getId());
				// 添加到图片数组
				imgList.add(pricePic);
			}
		}

		// 插入报价图片信息表
		for (int i = 0; i < imgList.size(); i++) {
			CotPricePic cotPricePic = (CotPricePic) imgList.get(i);
			List picList = new ArrayList();
			picList.add(cotPricePic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveOrUpdateImg(picList);
		}

		// 保存报价明细的配件及成本
		List listFitting = new ArrayList();
		List listPrice = new ArrayList();
		for (int i = 0; i < records.size(); i++) {
			CotPriceDetail detail = (CotPriceDetail) records.get(i);
			String type = detail.getType();
			Integer srcId = detail.getSrcId();
			if (type == null) {
				continue;
			}
			if (detail.getId() != null) {
				continue;
			}
			// 从样品来或excel
			if (type.equals("ele") || type.equals("none")) {
				// 从excel,要再根据货号获取编号
				if (type.equals("none")) {
					String tempSql = "select obj.id from CotElementsNew obj where obj.eleId='"
							+ detail.getEleId() + "'";
					List list = this.getPriceDao().find(tempSql);
					if (list != null && list.size() > 0) {
						srcId = (Integer) list.get(0);
					}
				}
				if (srcId == 0) {
					continue;
				}
				// 获得原配件信息
				String hql = "from CotEleFittings obj where obj.eleId=" + srcId;
				List list = this.getPriceDao().find(hql);
				for (int j = 0; j < list.size(); j++) {
					CotEleFittings eleFittings = (CotEleFittings) list.get(j);
					CotPriceFittings priceFittings = new CotPriceFittings();
					priceFittings.setPriceDetailId(detail.getId());
					priceFittings.setPriceId(detail.getPriceId());
					priceFittings.setEleId(detail.getEleId());
					priceFittings.setEleName(detail.getEleName());
					// priceFittings.setBoxCount(boxCount);
					priceFittings.setFitNo(eleFittings.getFitNo());
					priceFittings.setFitName(eleFittings.getFitName());
					priceFittings.setFitDesc(eleFittings.getFitDesc());
					priceFittings.setFacId(eleFittings.getFacId());
					priceFittings.setFitUseUnit(eleFittings.getFitUseUnit());
					priceFittings
							.setFitUsedCount(eleFittings.getFitUsedCount());
					priceFittings.setFitCount(eleFittings.getFitCount());
					priceFittings.setFitPrice(eleFittings.getFitPrice());
					// priceFittings.setOrderFitCount()
					priceFittings.setFitTotalPrice(eleFittings
							.getFitTotalPrice());
					priceFittings.setFitRemark(eleFittings.getFitRemark());
					priceFittings.setFittingId(eleFittings.getFittingId());

					listFitting.add(priceFittings);
				}
				// 获得原成本信息
				String hqlPrice = "from CotElePrice obj where obj.eleId="
						+ srcId;
				List listTemp = this.getPriceDao().find(hqlPrice);
				for (int j = 0; j < listTemp.size(); j++) {
					CotElePrice elePrice = (CotElePrice) listTemp.get(j);
					CotPriceEleprice priceEleprice = new CotPriceEleprice();

					priceEleprice.setPriceDetailId(detail.getId());
					priceEleprice.setPriceId(detail.getPriceId());
					priceEleprice.setPriceName(elePrice.getPriceName());
					priceEleprice.setPriceUnit(elePrice.getPriceUnit());
					priceEleprice.setPriceAmount(elePrice.getPriceAmount());
					priceEleprice.setRemark(elePrice.getRemark());

					listPrice.add(priceEleprice);
				}
			}
			// 从报价来
			if (type.equals("price")) {
				// 获得原配件信息
				String hql = "from CotPriceFittings obj where obj.priceDetailId="
						+ srcId;
				List list = this.getPriceDao().find(hql);
				for (int j = 0; j < list.size(); j++) {
					CotPriceFittings priceFittings = (CotPriceFittings) list
							.get(j);
					CotPriceFittings clone = (CotPriceFittings) SystemUtil
							.deepClone(priceFittings);
					clone.setId(null);
					clone.setPriceDetailId(detail.getId());
					clone.setPriceId(detail.getPriceId());
					listFitting.add(clone);
				}

				// 获得原成本信息
				String hqlPrice = "from CotPriceEleprice obj where obj.priceDetailId="
						+ srcId;
				List listTemp = this.getPriceDao().find(hqlPrice);
				for (int j = 0; j < listTemp.size(); j++) {
					CotPriceEleprice eleprice = (CotPriceEleprice) listTemp
							.get(j);
					CotPriceEleprice clone = (CotPriceEleprice) SystemUtil
							.deepClone(eleprice);
					clone.setId(null);
					clone.setPriceDetailId(detail.getId());
					clone.setPriceId(detail.getPriceId());
					listPrice.add(clone);
				}
			}
			// 从订单来
			if (type.equals("order")) {
				// 获得原配件信息
				String hql = "from CotOrderFittings obj where obj.orderDetailId="
						+ srcId;
				List list = this.getPriceDao().find(hql);
				for (int j = 0; j < list.size(); j++) {
					CotOrderFittings orderFittings = (CotOrderFittings) list
							.get(j);
					CotPriceFittings priceFittings = new CotPriceFittings();
					priceFittings.setPriceDetailId(detail.getId());
					priceFittings.setPriceId(detail.getPriceId());
					priceFittings.setEleId(detail.getEleId());
					priceFittings.setEleName(detail.getEleName());
					// priceFittings.setBoxCount(boxCount);
					priceFittings.setFitNo(orderFittings.getFitNo());
					priceFittings.setFitName(orderFittings.getFitName());
					priceFittings.setFitDesc(orderFittings.getFitDesc());
					priceFittings.setFacId(orderFittings.getFacId());
					priceFittings.setFitUseUnit(orderFittings.getFitUseUnit());
					priceFittings.setFitUsedCount(orderFittings
							.getFitUsedCount());
					priceFittings.setFitCount(orderFittings.getFitCount());
					priceFittings.setFitPrice(orderFittings.getFitPrice());
					// priceFittings.setOrderFitCount()
					priceFittings.setFitTotalPrice(orderFittings
							.getFitTotalPrice());
					priceFittings.setFitRemark(orderFittings.getFitRemark());
					priceFittings.setFittingId(orderFittings.getFittingId());

					listFitting.add(priceFittings);
				}

				// 获得原成本信息
				String hqlPrice = "from CotOrderEleprice obj where obj.orderDetailId="
						+ srcId;
				List listTemp = this.getPriceDao().find(hqlPrice);
				for (int j = 0; j < listTemp.size(); j++) {
					CotOrderEleprice orderEleprice = (CotOrderEleprice) listTemp
							.get(j);
					CotPriceEleprice priceEleprice = new CotPriceEleprice();
					priceEleprice.setPriceDetailId(detail.getId());
					priceEleprice.setPriceId(detail.getPriceId());
					priceEleprice.setPriceName(orderEleprice.getPriceName());
					priceEleprice.setPriceUnit(orderEleprice.getPriceUnit());
					priceEleprice
							.setPriceAmount(orderEleprice.getPriceAmount());
					priceEleprice.setRemark(orderEleprice.getRemark());
					listPrice.add(priceEleprice);
				}
			}
		}
		try {
			this.getPriceDao().saveRecords(listFitting);
			this.getPriceDao().saveRecords(listPrice);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "price";
		while (it.hasNext()) {
			CotPriceDetail detail = (CotPriceDetail) it.next();
			String eleId = detail.getEleId();
			String custNo = detail.getCustNo();
			String eleNameEn = detail.getEleNameEn();

			if (custNo != null && !custNo.trim().equals("")) {
				elecustMap.put(eleId, custNo);
			}
			if (eleNameEn != null && !eleNameEn.trim().equals("")) {
				elenameenMap.put(eleId, eleNameEn);
			}
		}
		if (elecustMap.size() != 0 && elenameenMap.size() != 0) {
			this.getCotElementsService().saveEleCustNoByCustList(elecustMap,
					elenameenMap, custId, type);
		}
		//清空内存
		ctx.getSession().removeAttribute("SessionEXCELTEMPPRICE");
	}

	// 通过货号修改Map中对应的明细并返回对象
	public CotPriceDetail updateObjByEle(String eleId, String property,
			String value) {
		CotPriceDetail cotPriceDetail = getPriceMapValue(eleId.toLowerCase());
		if (cotPriceDetail == null)
			return null;
		try {
			BeanUtils.setProperty(cotPriceDetail, property, value);
			this.setPriceMap(eleId.toLowerCase(), cotPriceDetail);
			return cotPriceDetail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// 得到excel的内存map的key集合
	public List<?> getRdmList() {
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("priceReport");
		List<String> list = new ArrayList<String>();
		if (obj == null) {
			return list;
		}
		TreeMap<String, CotPriceDetail> map = (TreeMap<String, CotPriceDetail>) obj;
		Iterator<?> it = map.keySet().iterator();
		while (it.hasNext()) {
			String rdm = (String) it.next();
			list.add(rdm);
		}
		return list;
	}

	// 返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(sdf.parse(msgBeginDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.DATE, +10);
		String dateString = sdf.format(cal.getTime());
		return dateString;
	}

	// 查询配件信息
	public List findFittingsByIds(String ids, Integer rdm) {
		CotPriceDetail detail = this.getPriceMapValue(rdm.toString());

		// 查找配件字符串ids中是否已导过
		String hql = "select obj.fittingId from CotPriceFittings obj where obj.priceDetailId="
				+ detail.getId()
				+ " and obj.fittingId in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		List list = this.getPriceDao().find(hql);
		if (list != null && list.size() > 0) {
			String temp = "";
			for (int i = 0; i < list.size(); i++) {
				temp += list.get(i) + ",";
			}
			String str = "from CotFittings obj where obj.id not in("
					+ temp.substring(0, temp.length() - 1)
					+ ") and obj.id in (" + ids.substring(0, ids.length() - 1)
					+ ")";
			return this.getPriceDao().find(str);
		} else {
			String str = "from CotFittings obj where obj.id in ("
					+ ids.substring(0, ids.length() - 1) + ")";
			return this.getPriceDao().find(str);
		}
	}

	// 增加
	public void addList(List list) {
		try {
			// 如果配件号为空,或者不是从配件库来的,不生成
			List listNew = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				CotPriceFittings priceFittings = (CotPriceFittings) list.get(i);
				// getFittingId
				if (!"".equals(priceFittings.getFitNo())
						&& priceFittings.getFittingId() != 0) {
					listNew.add(priceFittings);
				}
			}
			this.getPriceDao().saveRecords(listNew);
			//保存到配件分析表
			
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 增加成本
	public void addElePriceList(List list) {
		try {
			this.getPriceDao().saveRecords(list);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void deleteList(List list, String tabName) {
		try {
			this.getPriceDao().deleteRecordByIds(list, tabName);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 修改
	public void modifyList(List list) {
		try {
			this.getPriceDao().updateRecords(list);
		} catch (Exception e) {
		}
	}

	// 获得报价配件对象
	public CotPriceFittings getPriceFittingById(Integer id) {
		CotPriceFittings priceFittings = (CotPriceFittings) this.getPriceDao()
				.getById(CotPriceFittings.class, id);
		return priceFittings;
	}

	// 获得报价明细成本对象
	public CotPriceEleprice getElePriceById(Integer id) {
		CotPriceEleprice priceEleprice = (CotPriceEleprice) this.getPriceDao()
				.getById(CotPriceEleprice.class, id);
		return priceEleprice;
	}

	// 判断输入的配件号是否存在.完全存在的话直接加到表格中,有模糊数据弹出层.
	public List findIsExistFitNo(String fitNo, Integer priceDetailId) {
		String hql = "from CotFittings obj where obj.fitNo=?";
		Object[] obj = new Object[1];
		obj[0] = fitNo;
		List list = this.getPriceDao().queryForLists(hql, obj);
		if (list != null && list.size() == 1) {
			CotFittings fittings = (CotFittings) list.get(0);
			// 判断该条配件是否已经添加
			String str = "select obj.fittingId from CotPriceFittings obj where obj.priceDetailId="
					+ priceDetailId + " and obj.fittingId=" + fittings.getId();
			List listStr = this.getPriceDao().find(str);
			if (listStr != null && listStr.size() > 0) {
				List temp = new ArrayList();
				temp.add(0);
				return temp;
			} else {
				return list;
			}
		} else {
			String hqlTemp = "";
			if (" ".equals(fitNo)) {
				hqlTemp = "from CotFittings obj";
			} else {
				hqlTemp = "from CotFittings obj where obj.fitNo like '%"
						+ fitNo + "%'";
			}
			List listTemp = this.getPriceDao().find(hqlTemp);
			if (listTemp != null && listTemp.size() > 0) {
				return listTemp;
			} else {
				return null;
			}
		}
	}

	// 取得默认的样品配置
	public CotEleCfg getDefaultEleCfg() {
		String hql = "from CotEleCfg obj where obj.cfgFlag=1";
		List<?> list = this.getPriceDao().find(hql);
		if (list.size() > 0) {
			return (CotEleCfg) list.get(0);
		} else {
			return null;
		}
	}

	// 计算明细的生产价
	public CotPriceDetail updatePriceFac(Integer priceDetailId, Integer rdm,Map<Object, Object> map) {
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getDefaultEleCfg();
		if(cotEleCfg==null || cotEleCfg.getExpessionFacIn()==null || "".equals(cotEleCfg.getExpessionFacIn())){
			return null;
		}
		// 得到内存的币种
		List listCur = this.getDicList("currency");
		CotPriceDetail detail = this.getPriceMapValue(rdm.toString());
		if (detail.getPriceFacUint() == null) {
			return null;
		}
		try {
			// 计算出配件成本的RMB总和
			Float allRmbMoney = 0f;
			String hql = "from CotPriceFittings obj where obj.priceDetailId="
					+ priceDetailId;
			List list = this.getPriceDao().find(hql);
			for (int i = 0; i < list.size(); i++) {
				CotPriceFittings priceFittings = (CotPriceFittings) list.get(i);
				Double mo = priceFittings.getFitTotalPrice();
				allRmbMoney += mo.floatValue();
			}

			// 计算出成本的RMB总和
			Float allPriceMoney = 0f;
			String hqlStr = "from CotPriceEleprice obj where obj.priceDetailId="
					+ priceDetailId;
			List listPrice = this.getPriceDao().find(hqlStr);
			for (int i = 0; i < listPrice.size(); i++) {
				CotPriceEleprice priceEleprice = (CotPriceEleprice) listPrice
						.get(i);
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == priceEleprice.getPriceUnit()
							.intValue()) {
						Double mo = priceEleprice.getPriceAmount()
								* cur.getCurRate();
						allPriceMoney += mo.floatValue();
						break;
					}
				}
			}

			Evaluator evaluator = new Evaluator();
			// 定义公式的变量
			evaluator.putVariable("eleFittingPrice", allRmbMoney.toString());
			evaluator.putVariable("elePrice", allPriceMoney.toString());

			if (detail.getPackingPrice() == null) {
				evaluator.putVariable("packingPrice", "0");
			} else {
				evaluator.putVariable("packingPrice", detail.getPackingPrice()
						.toString());
			}
			// 根据公式算出RMB价格,必须要有生产币种
			float rate = 0f;
			String result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == detail.getPriceFacUint()
						.intValue()) {
					rate = cur.getCurRate();
					break;
				}
			}
			Float fac = Float.parseFloat(result) / rate;

			// 保存生产价
			detail.setPriceFac(fac);
			detail.setElePrice(allPriceMoney);
			detail.setEleFittingPrice(allRmbMoney);
			
			if(map!=null){
				map.put("priceFac", fac);
				Float pricePrice= this.getNewPriceByPriceFac(map,rdm.toString());
				detail.setPricePrice(pricePrice);
			}
			List listDetail = new ArrayList();
			listDetail.add(detail);
			this.getPriceDao().updateRecords(listDetail);
			WebContext ctx = WebContextFactory.get();
			this.setMapAction(ctx.getSession(), rdm.toString(), detail);
			return detail;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit() {
		List<CotEleCfg> list = new ArrayList<CotEleCfg>();
		String hql = " from CotEleCfg obj ";
		list = this.getPriceDao().find(hql);
		if (list != null && list.size() > 0) {
			CotEleCfg cotEleCfg = (CotEleCfg) list.get(0);
			return cotEleCfg;
		} else {
			return null;
		}
	}

	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String rdm, String packingPrice) {
		CotPriceDetail detail = this.getPriceMapValue(rdm);
		if (detail == null) {
			return null;
		}
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		if (cotEleCfg != null) {
			Float elePrice = 0.0f;
			Float eleFittingPrice = 0.0f;

			if (detail.getElePrice() != null) {
				elePrice = detail.getElePrice();
			}
			if (detail.getEleFittingPrice() != null) {
				eleFittingPrice = detail.getEleFittingPrice();
			}
			String expessionFacIn = cotEleCfg.getExpessionFacIn();
			// 定义jeavl对象,用于计算字符串公式
			Evaluator evaluator = new Evaluator();
			evaluator.putVariable("elePrice", elePrice.toString());
			evaluator
					.putVariable("eleFittingPrice", eleFittingPrice.toString());
			evaluator.putVariable("packingPrice", packingPrice);
			Float res = -1f;
			try {
				if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
					res = detail.getPriceFac();
				} else {
					String result = evaluator.evaluate(expessionFacIn);
					res = Float.parseFloat(result);
				}
				detail.setPriceFac(res);
				detail.setPackingPrice(Float.parseFloat(packingPrice));
				this.setPriceMap(rdm, detail);
			} catch (EvaluationException e) {
				e.printStackTrace();
				return -1f;
			}
			return res;
		} else {
			return -1f;
		}
	}

	// 修改报价审核状态
	public void updatePriceStatus(Integer priceId, Integer priceStatus) {
		CotPrice cotPrice = (CotPrice) this.getPriceDao().getById(
				CotPrice.class, priceId);
		cotPrice.setCotPriceDetails(null);
		cotPrice.setPriceStatus(priceStatus.longValue());
		List list = new ArrayList();
		list.add(cotPrice);
		this.getPriceDao().updateRecords(list);
	}

	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId) {
		return (CotBoxPacking) this.getPriceDao().getById(CotBoxPacking.class,
				boxPackingId);
	}

	// 计算价格
	public String calPrice(CotPriceDetail elements, Integer boxPackingId) {
		CotBoxPacking boxPacking = this.getCalculation(boxPackingId);
		Evaluator evaluator = new Evaluator();
		// 产品长、宽、高
		if (elements.getBoxL() == null || elements.getBoxL().equals("")) {
			evaluator.putVariable("boxL", "0.0");
		} else {
			evaluator.putVariable("boxL", elements.getBoxL().toString());
		}
		if (elements.getBoxW() == null || elements.getBoxW().equals("")) {
			evaluator.putVariable("boxW", "0.0");
		} else {
			evaluator.putVariable("boxW", elements.getBoxW().toString());
		}
		if (elements.getBoxH() == null || elements.getBoxH().equals("")) {
			evaluator.putVariable("boxH", "0.0");
		} else {
			evaluator.putVariable("boxH", elements.getBoxH().toString());
		}
		// 产品包装长、宽、高
		if (elements.getBoxPbL() == null || elements.getBoxPbL().equals("")) {
			evaluator.putVariable("boxPbL", "0.0");
		} else {
			evaluator.putVariable("boxPbL", elements.getBoxPbL().toString());
		}
		if (elements.getBoxPbW() == null || elements.getBoxPbW().equals("")) {
			evaluator.putVariable("boxPbW", "0.0");
		} else {
			evaluator.putVariable("boxPbW", elements.getBoxPbW().toString());
		}
		if (elements.getBoxPbH() == null || elements.getBoxPbH().equals("")) {
			evaluator.putVariable("boxPbH", "0.0");
		} else {
			evaluator.putVariable("boxPbH", elements.getBoxPbH().toString());
		}
		// 中盒包装长、宽、高
		if (elements.getBoxMbL() == null || elements.getBoxMbL().equals("")) {
			evaluator.putVariable("boxMbL", "0.0");
		} else {
			evaluator.putVariable("boxMbL", elements.getBoxMbL().toString());
		}
		if (elements.getBoxMbW() == null || elements.getBoxMbW().equals("")) {
			evaluator.putVariable("boxMbW", "0.0");
		} else {
			evaluator.putVariable("boxMbW", elements.getBoxMbW().toString());
		}
		if (elements.getBoxMbH() == null || elements.getBoxMbH().equals("")) {
			evaluator.putVariable("boxMbH", "0.0");
		} else {
			evaluator.putVariable("boxMbH", elements.getBoxMbH().toString());
		}
		// 内盒包装长、宽、高
		if (elements.getBoxIbL() == null || elements.getBoxIbL().equals("")) {
			evaluator.putVariable("boxIbL", "0.0");
		} else {
			evaluator.putVariable("boxIbL", elements.getBoxIbL().toString());
		}
		if (elements.getBoxIbW() == null || elements.getBoxIbW().equals("")) {
			evaluator.putVariable("boxIbW", "0.0");
		} else {
			evaluator.putVariable("boxIbW", elements.getBoxIbW().toString());
		}
		if (elements.getBoxIbH() == null || elements.getBoxIbH().equals("")) {
			evaluator.putVariable("boxIbH", "0.0");
		} else {
			evaluator.putVariable("boxIbH", elements.getBoxIbH().toString());
		}
		// 外箱包装长、宽、高
		if (elements.getBoxObL() == null || elements.getBoxObL().equals("")) {
			evaluator.putVariable("boxObL", "0.0");
		} else {
			evaluator.putVariable("boxObL", elements.getBoxObL().toString());
		}
		if (elements.getBoxObW() == null || elements.getBoxObW().equals("")) {
			evaluator.putVariable("boxObW", "0.0");
		} else {
			evaluator.putVariable("boxObW", elements.getBoxObW().toString());
		}
		if (elements.getBoxObH() == null || elements.getBoxObH().equals("")) {
			evaluator.putVariable("boxObH", "0.0");
		} else {
			evaluator.putVariable("boxObH", elements.getBoxObH().toString());
		}

		// 摆放长、宽、高
		if (elements.getPutL() == null || elements.getPutL().equals("")) {
			evaluator.putVariable("putL", "0.0");
		} else {
			evaluator.putVariable("putL", elements.getPutL().toString());
		}
		if (elements.getPutW() == null || elements.getPutW().equals("")) {
			evaluator.putVariable("putW", "0.0");
		} else {
			evaluator.putVariable("putW", elements.getPutW().toString());
		}
		if (elements.getPutH() == null || elements.getPutH().equals("")) {
			evaluator.putVariable("putH", "0.0");
		} else {
			evaluator.putVariable("putH", elements.getPutH().toString());
		}
		// 材料单价
		if (boxPacking.getMaterialPrice() == null
				|| boxPacking.getMaterialPrice().equals("")) {
			evaluator.putVariable("materialPrice", "0.0");
		} else {
			evaluator.putVariable("materialPrice", boxPacking
					.getMaterialPrice().toString());
		}

		try {
			if (boxPacking.getFormulaIn() == null
					|| boxPacking.getFormulaIn().trim().equals("")) {
				return "0.0";
			}
			String result = evaluator.evaluate(boxPacking.getFormulaIn());
			return result;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return "0.0";
		}
	}

	// 计算价格
	public Float[] calPriceAll(String rdm) {
		CotPriceDetail elements = this.getPriceMapValue(rdm);
		if (elements == null) {
			return null;
		}
		Float[] res = new Float[7];
		DecimalFormat dfTwo = new DecimalFormat("#.00");
		Float pb = 0f;
		if (elements.getBoxPbTypeId() != null && elements.getBoxPbTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxPbTypeId());
			pb = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float ib = 0f;
		if (elements.getBoxIbTypeId() != null && elements.getBoxIbTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxIbTypeId());
			ib = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float mb = 0f;
		if (elements.getBoxMbTypeId() != null && elements.getBoxMbTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxMbTypeId());
			mb = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float ob = 0f;
		if (elements.getBoxObTypeId() != null && elements.getBoxObTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxObTypeId());
			ob = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float ig = 0f;
		if (elements.getInputGridTypeId() != null
				&& elements.getInputGridTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getInputGridTypeId());
			ig = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		res[0] = pb;
		res[1] = ib;
		res[2] = mb;
		res[3] = ob;
		res[6] = ig;
		// 计算单个价格
		if (elements.getBoxObCount() == null || elements.getBoxObCount() == 0) {
			res[4] = 0f;
		} else {
			Float pRes = 0f;
			Float iRes = 0f;
			Float mRes = 0f;
			if (elements.getBoxPbCount() != null
					&& elements.getBoxPbCount() != 0) {
				pRes = pb
						* (elements.getBoxObCount().floatValue() / elements
								.getBoxPbCount());
			}
			if (elements.getBoxIbCount() != null
					&& elements.getBoxIbCount() != 0) {
				iRes = ib
						* (elements.getBoxObCount().floatValue() / elements
								.getBoxIbCount());
			}
			if (elements.getBoxMbCount() != null
					&& elements.getBoxMbCount() != 0) {
				mRes = mb
						* (elements.getBoxObCount().floatValue() / elements
								.getBoxMbCount());
			}
			Float all = (pRes + iRes + mRes + ob + ig)
					/ elements.getBoxObCount();
			res[4] = Float.parseFloat(dfTwo.format(all));
		}
		// 计算生产价
		res[5] = 0f;
		Float elePrice = 0.0f;
		Float eleFittingPrice = 0.0f;
		if (elements.getId() != null) {
			if (elements.getElePrice() != null) {
				elePrice = elements.getElePrice();
			}
			if (elements.getEleFittingPrice() != null) {
				eleFittingPrice = elements.getEleFittingPrice();
			}
		} else {
			if ("price".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotPriceDetail obj where obj.id="
						+ elements.getSrcId();
				List list = this.getPriceDao().find(hql);
				if (list != null && list.size() > 0) {
					Object[] obj = (Object[]) list.get(0);
					if (obj[0] != null) {
						elePrice = (Float) obj[0];
					}
					if (obj[1] != null) {
						eleFittingPrice = (Float) obj[1];
					}
				}
			}
			if ("ele".equals(elements.getType())
					|| "given".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotElementsNew obj where obj.id="
						+ elements.getSrcId();
				List list = this.getPriceDao().find(hql);
				if (list != null && list.size() > 0) {
					Object[] obj = (Object[]) list.get(0);
					if (obj[0] != null) {
						elePrice = (Float) obj[0];
					}
					if (obj[1] != null) {
						eleFittingPrice = (Float) obj[1];
					}
				}
			}
			if ("order".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotOrderDetail obj where obj.id="
						+ elements.getId();
				List list = this.getPriceDao().find(hql);
				if (list != null && list.size() > 0) {
					Object[] obj = (Object[]) list.get(0);
					if (obj[0] != null) {
						elePrice = (Float) obj[0];
					}
					if (obj[1] != null) {
						eleFittingPrice = (Float) obj[1];
					}
				}
			}
		}
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		if (cotEleCfg != null) {
			String expessionFacIn = cotEleCfg.getExpessionFacIn();
			// 定义jeavl对象,用于计算字符串公式
			Evaluator evaluator = new Evaluator();
			evaluator.putVariable("elePrice", elePrice.toString());
			evaluator
					.putVariable("eleFittingPrice", eleFittingPrice.toString());
			evaluator.putVariable("packingPrice", res[4].toString());
			try {
				if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
					res[5] = -1f;
				} else {
					String result = evaluator.evaluate(expessionFacIn);
					res[5] = Float.parseFloat(dfTwo.format(Float
							.parseFloat(result)));
				}
			} catch (EvaluationException e) {
				e.printStackTrace();
				res[5] = -1f;
			}
		} else {
			res[5] = -1f;
		}
		return res;
	}

	// 重新排序
	public boolean updateSortNo(Integer id, Integer type, String field,
			String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<String, CotPriceDetail> pricerMap = this.getMapAction(ctx
				.getSession());
		List list = new ArrayList();
		Iterator<?> it = pricerMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			CotPriceDetail detail = pricerMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotPriceDetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotPriceDetail detail = (CotPriceDetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getPriceDao().updateRecords(listNew);
		return true;
	}

	// 储存Map
	public void setPriceMapAndDel(String rdm, CotPriceDetail cotPriceDetail) {
		Object obj = SystemUtil.getObjBySession(null, "price");
		if (obj == null) {
			Map<String, CotPriceDetail> priceMap = new HashMap<String, CotPriceDetail>();
			priceMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(null, priceMap, "price");
		} else {
			Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
			if (cotPriceDetail.getId() != null) {
				Iterator<?> it = priceMap.keySet().iterator();
				while (it.hasNext()) {
					String temp = (String) it.next();
					CotPriceDetail priceDetail = priceMap.get(temp);
					if (priceDetail.getId() != null
							&& cotPriceDetail.getId().intValue() == priceDetail
									.getId().intValue()) {
						it.remove();
					}
				}
			}
			priceMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(null, priceMap, "price");
		}

	}

	// 计算生产价
	public Float sumPriceFac(String expessionFacIn, CotPriceDetail detail) {
		if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
			return 0f;
		}
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		Float elePrice = 0f;
		if (detail.getElePrice() != null) {
			elePrice = detail.getElePrice();
		}
		Float fitPrice = 0f;
		if (detail.getEleFittingPrice() != null) {
			fitPrice = detail.getEleFittingPrice();
		}
		Float packPrice = 0f;
		if (detail.getPackingPrice() != null) {
			packPrice = detail.getPackingPrice();
		}
		evaluator.putVariable("elePrice", elePrice.toString());
		evaluator.putVariable("eleFittingPrice", fitPrice.toString());
		evaluator.putVariable("packingPrice", packPrice.toString());
		try {
			String result = evaluator.evaluate(expessionFacIn);
			result = ContextUtil.getObjByPrecision(Float.parseFloat(result),"facPrecision");
			return Float.parseFloat(result);
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}

	}

	// 计算外销价
	public Float sumPriceOut(String expessionIn, Float priceFac,
			CotPriceDetail detail) {
		if (expessionIn == null || expessionIn.trim().equals("")) {
			return 0f;
		}
		// 利润率
		Float lirun = 0f;
		if (detail.getLiRun() != null) {
			lirun = detail.getLiRun();
		}
		// 汇率
		Float rate = 0f;
		if (detail.getPriceOutUint() != null) {
			rate = this.getReportDao().getCurRate(detail.getPriceOutUint());
		}
		// 退税率
		Float tui = 0f;
		if (detail.getTuiLv() != null) {
			tui = detail.getTuiLv();
		}
		// CBM
		Float cb = 0f;
		if (detail.getBoxCbm() != null) {
			cb = detail.getBoxCbm();
		}
		// 外包装
		Long bc = 0l;
		if (detail.getBoxObCount() != null) {
			bc = detail.getBoxObCount();
		}

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("priceProfit", lirun.toString());
		evaluator.putVariable("priceFac", priceFac.toString());
		evaluator.putVariable("priceRate", rate.toString());
		evaluator.putVariable("tuiLv", tui.toString());
		evaluator.putVariable("cbm", cb.toString());
		evaluator.putVariable("boxObCount", bc.toString());
		Float res = null;
		try {
			String result = evaluator.evaluate(expessionIn);
			result = ContextUtil.getObjByPrecision(Float.parseFloat(result),"outPrecision");
			return Float.parseFloat(result);
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}
	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getReportDao().getJsonData(queryInfo);
	}

	// 查询含有rdm的报价记录
	public String getListData(HttpServletRequest request, QueryInfo queryInfo)
			throws DAOException {
		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		// 清空内存map
		session.removeAttribute("SessionPRICE");

		GridServerHandler gd = null;
		if (queryInfo.getExcludes() != null)
			gd = new GridServerHandler(queryInfo.getExcludes());
		else
			gd = new GridServerHandler();
		int count = this.getPriceDao().getRecordsCount(queryInfo);
		List res = this.getPriceDao().findRecords(queryInfo);
		Iterator<?> it = res.iterator();
		while (it.hasNext()) {
			CotPriceDetail cotPriceDetail = (CotPriceDetail) it.next();
			cotPriceDetail.setPicImg(null);
			String rdm = "1" + RandomStringUtils.randomNumeric(8);
			cotPriceDetail.setRdm(rdm);
			this.setMapAction(session, rdm, cotPriceDetail);
		}

		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
	}

	// 得到盘点机的内存map的key集合
	public void savePanList(String no,Integer priceId) {
		// 取得内存中的最大排序号
		Object price = SystemUtil.getObjBySession(null, "price");
		int temp = 0;
		if (price != null) {
			Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) price;
			Iterator<?> it = priceMap.keySet().iterator();
			while (it.hasNext()) {
				String rdm = (String) it.next();
				CotPriceDetail priceDetail = priceMap.get(rdm);
				if (priceDetail.getSortNo() != null
						&& priceDetail.getSortNo() > temp) {
					temp = priceDetail.getSortNo();
				}
			}
		}

		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckMachine");
		Map<String, List<CotPriceDetail>> map = (Map<String, List<CotPriceDetail>>) obj;
		List<CotPriceDetail> list = map.get(no);
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				temp++;
				CotPriceDetail priceDetail = list.get(i);
				priceDetail.setSortNo(temp);
				priceDetail.setPriceId(priceId);
				//this.setPriceMapAndDel(priceDetail.getRdm(), priceDetail);
			}
			this.addPriceDetails(list);
			CotOpImgService impOpService = (CotOpImgService) SystemUtil
			.getService("CotOpImgService");
			byte[] zwtpByte = this.getCotElementsService().getZwtpPic();
			//保存图片,图片取样品档案的
			for (int i = 0; i < list.size(); i++) {
				CotPriceDetail detail = list.get(i);
				// 新建图片
				CotPricePic pricePic = new CotPricePic();
				pricePic.setFkId(detail.getId());
				if (detail.getType().equals("ele")) {
					CotElePic cotElePic = impOpService.getElePicImgByEleId(detail
							.getSrcId());
					if(cotElePic!=null){
						pricePic.setEleId(cotElePic.getEleId());
						pricePic.setPicImg(cotElePic.getPicImg());
						pricePic.setPicSize(cotElePic.getPicImg().length);
						pricePic.setPicName(cotElePic.getEleId());
					}else{
						pricePic.setEleId(detail.getEleId());
						pricePic.setPicImg(zwtpByte);
						pricePic.setPicSize(zwtpByte.length);
						pricePic.setPicName(detail.getEleId());
					}
				}
				// 添加到图片数组
				List<CotPricePic> imgList = new ArrayList<CotPricePic>();
				imgList.add(pricePic);
				// 逐条添加，避免数据量大的时候，内存溢出
				impOpService.saveImg(imgList);
			}
		}
	}

	// 样品配件信息同步到报价
	public boolean deleteAndTongEleFitting(Integer priceDetailId, String eleId) {
		CotPriceDetail priceDetail = (CotPriceDetail) this.getPriceDao()
				.getById(CotPriceDetail.class, priceDetailId);
		//0.先查询该货号的样品id,如果找不到该货号不同步
		Integer eId=0;
		String str="select obj.id from CotElementsNew obj where obj.eleId='"+eleId+"'";
		List chk = this.getPriceDao().find(str);
		if(chk==null || chk.size()==0){
			return false;
		}else{
			eId=(Integer) chk.get(0);
		}
		// 1.先删除报价原来的配件信息
		String hql = "select obj.id from CotPriceFittings obj where obj.priceDetailId="
				+ priceDetailId;
		List list = this.getPriceDao().find(hql);
		try {
			if (list.size() > 0) {
				this.getPriceDao().deleteRecordByIds(list, "CotPriceFittings");
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
		List listNew = new ArrayList();
		// 2.再查找该货号的所有样品配件信息,插入到该报价明细中
		String sql = "select obj from CotEleFittings obj where obj.eleId="+eId;
		List listEle = this.getPriceDao().find(sql);
		for (int i = 0; i < listEle.size(); i++) {
			CotEleFittings eleFittings = (CotEleFittings) listEle.get(i);
			CotPriceFittings priceFittings = new CotPriceFittings();
			priceFittings.setPriceDetailId(priceDetailId);
			priceFittings.setEleId(eleId);
			priceFittings.setEleName(priceDetail.getEleName());
			priceFittings.setFitNo(eleFittings.getFitNo());
			priceFittings.setFitName(eleFittings.getFitName());
			priceFittings.setFitDesc(eleFittings.getFitDesc());
			priceFittings.setFacId(eleFittings.getFacId());
			priceFittings.setFitUseUnit(eleFittings.getFitUseUnit());
			priceFittings.setFitUsedCount(eleFittings.getFitUsedCount());
			priceFittings.setFitCount(eleFittings.getFitCount());
			priceFittings.setFitPrice(eleFittings.getFitPrice());
			priceFittings.setFitTotalPrice(eleFittings.getFitTotalPrice());
			priceFittings.setFitRemark(eleFittings.getFitRemark());
			priceFittings.setPriceId(priceDetail.getPriceId());
			priceFittings.setFittingId(eleFittings.getFittingId());
			listNew.add(priceFittings);
		}
		try {
			this.getPriceDao().saveRecords(listNew);
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// 报价同步到样品配件
	public boolean deleteAndTongFitting(Integer priceDetailId, String eleId) throws DAOException {
		CotPriceDetail priceDetail = (CotPriceDetail) this.getPriceDao()
				.getById(CotPriceDetail.class, priceDetailId);
		//0.先查询该货号的样品id,如果找不到该货号不同步
		Integer eId=0;
		String str="select obj.id from CotElementsNew obj where obj.eleId='"+eleId+"'";
		List chk = this.getPriceDao().find(str);
		if(chk==null || chk.size()==0){
			return false;
		}else{
			eId=(Integer) chk.get(0);
		}
		// 1.先删除样品库原来的配件信息
		String hql="delete from CotEleFittings obj where obj.eleId=:eleId";
		Map map = new HashMap();
		map.put("eleId", eId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		this.getPriceDao().executeUpdate(queryInfo, map);
		
		List listNew = new ArrayList();
		// 2.再查找该货号的所有报价配件信息,插入到样品配件
		String sql = "select obj from CotPriceFittings obj where obj.priceDetailId="+priceDetailId;
		List listEle = this.getPriceDao().find(sql);
		for (int i = 0; i < listEle.size(); i++) {
			CotPriceFittings priceFittings = (CotPriceFittings) listEle.get(i);
			CotEleFittings eleFittings = new CotEleFittings();
			eleFittings.setEleId(eId);
			eleFittings.setFitNo(priceFittings.getFitNo());
			eleFittings.setFitName(priceFittings.getFitName());
			eleFittings.setFitDesc(priceFittings.getFitDesc());
			eleFittings.setFacId(priceFittings.getFacId());
			eleFittings.setFitUseUnit(priceFittings.getFitUseUnit());
			eleFittings.setFitUsedCount(priceFittings.getFitUsedCount());
			eleFittings.setFitCount(priceFittings.getFitCount());
			eleFittings.setFitPrice(priceFittings.getFitPrice());
			eleFittings.setFitTotalPrice(priceFittings.getFitTotalPrice());
			eleFittings.setFitRemark(priceFittings.getFitRemark());
			eleFittings.setFittingId(priceFittings.getFittingId());
			listNew.add(eleFittings);
		}
		try {
			this.getPriceDao().saveRecords(listNew);
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//修改样品生产价
	public boolean modifyPriceFacByEleId(String eleId){
		//0.先查询该货号的样品id,如果找不到该货号不同步
		Integer eId=0;
		String str="select obj.id from CotElementsNew obj where obj.eleId='"+eleId+"'";
		List chk = this.getPriceDao().find(str);
		if(chk==null || chk.size()==0){
			return false;
		}else{
			eId=(Integer) chk.get(0);
		}
		this.getCotElementsService().modifyPriceFacByEleId(eId);
		return true;
	}
	
	// 储存Map
	public void setExcelMap(String rdm, CotPriceDetail cotPriceDetail) {
		Object obj = SystemUtil.getObjBySession(null, "excelTempPrice");
		if (obj == null) {
			Map<String, CotPriceDetail> orderMap = new HashMap<String, CotPriceDetail>();
			orderMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(null, orderMap, "excelTempPrice");
		} else {
			Map<String, CotPriceDetail> orderMap = (HashMap<String, CotPriceDetail>) obj;
			orderMap.put(rdm, cotPriceDetail);
			SystemUtil.setObjBySession(null, orderMap, "excelTempPrice");
		}

	}
	
	//计算勾选的报价明细的InverseMargin
	public Map calLiRun(Map<?, ?> map, List rdmAry){
		Map mapNew = new HashMap();
		Iterator<?> it = rdmAry.iterator();
		while(it.hasNext()){
			String rdm = (String) it.next();
			Float lirun = this.getNewLiRun(map,rdm);
			if(lirun!=null){
				mapNew.put(rdm, lirun);
				//更改内存值
				CotPriceDetail detail=this.getPriceMapValue(rdm);
				detail.setLiRun(lirun);
				this.setPriceMap(rdm, detail);
			}
		}
		return mapNew;
	}
	
	//用过货号查找样品id
	public Integer getEleIdByEleName(String eleId){
		String hql = "select obj.id from CotElementsNew obj where obj.eleId='"+eleId+"'";
		List list = this.getPriceDao().find(hql);
		if(list!=null && list.size()>0){
			return (Integer) list.get(0);
		}else{
			return 0;
		}
	}
	
	
	/**
	 * 先查询出所有明细,再克隆主订单保存后将新主单的id填充到旧明细中,保存旧明细.再以旧明细的编号为key 新明细为value组成map
	 * 再根据旧明细编号查询出旧明细图片,再根据旧明细编号所对应的新明细的编号填到旧明细图片的fkid, 再组成一个新明细图片,保存
	 * [另存时不另存订单的其他费用.订单总金额更改为货物总金额] 需要拷贝订单明细的配件和成本信息
	 */

	public boolean saveAs(String newOrderNo, Integer mainId) throws Exception {
		// 判断新单号是否存在
		String hql = "select obj.id from CotPrice obj where obj.priceNo='"
				+ newOrderNo + "'";
		List<?> res = this.getPriceDao().find(hql);
		if (res.size() > 0) {
			return false;
		} else {
			// 获得所有报价明细
			String dhql = "from CotPriceDetail obj where obj.priceId=" + mainId;
			List<?> resDetail = this.getPriceDao().find(dhql);
			CotPrice cotPrice = (CotPrice) this.getPriceDao().getById(
					CotPrice.class, mainId);
			CotPrice clone = (CotPrice) SystemUtil.deepClone(cotPrice);
			clone.setId(null);
			clone.setPriceNo(newOrderNo);
			
			
			List temp = new ArrayList();
			temp.add(clone);
			try {
				// 保存主单
				this.getPriceDao().saveRecords(temp);
				
			} catch (DAOException e) {
				e.printStackTrace();
				throw new Exception("另存报价单失败!");
			}
			String ids = "";
			Map map = new HashMap();
			List newDetail = new ArrayList();
			for (int i = 0; i < resDetail.size(); i++) {
				CotPriceDetail cotPriceDetail = (CotPriceDetail) resDetail
						.get(i);
				cotPriceDetail.setPriceId(clone.getId());
				ids += cotPriceDetail.getId() + ",";
				map.put(cotPriceDetail.getId(), cotPriceDetail);
				cotPriceDetail.setId(null);
				newDetail.add(cotPriceDetail);
			}
			if (resDetail.size() != 0) {
				// 图片
				String pichql = "from CotPricePic obj where obj.fkId in ("
						+ ids.substring(0, ids.length() - 1) + ")";
				List<?> picList = this.getPriceDao().find(pichql);

				// 配件
				String fithql = "from CotPriceFittings obj where obj.priceDetailId in ("
						+ ids.substring(0, ids.length() - 1) + ")";
				List<?> fitList = this.getPriceDao().find(fithql);

				// 成本
				String pricehql = "from CotPriceEleprice obj where obj.priceDetailId in ("
						+ ids.substring(0, ids.length() - 1) + ")";
				List<?> priceList = this.getPriceDao().find(pricehql);

				try {
					// 另存报价明细
					this.getPriceDao().saveRecords(newDetail);
					// 拷贝配件和成本信息

				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("另存报价明细失败!");
				}
				// 图片
				List list = new ArrayList();
				for (int i = 0; i < picList.size(); i++) {
					CotPricePic cotPricePic = (CotPricePic) picList.get(i);
					CotPriceDetail newde = (CotPriceDetail) map.get(cotPricePic
							.getFkId());
					cotPricePic.setFkId(newde.getId());
					cotPricePic.setId(null);
					list.add(cotPricePic);
				}
				// 配件
				List listFit = new ArrayList();
				for (int i = 0; i < fitList.size(); i++) {
					CotPriceFittings priceFittings = (CotPriceFittings) fitList
							.get(i);
					CotPriceDetail newde = (CotPriceDetail) map
							.get(priceFittings.getPriceDetailId());
					priceFittings.setPriceDetailId(newde.getId());
					priceFittings.setPriceId(clone.getId());
					priceFittings.setId(null);
					listFit.add(priceFittings);
				}

				// 成本
				List listPrice = new ArrayList();
				for (int i = 0; i < priceList.size(); i++) {
					CotPriceEleprice priceEleprice = (CotPriceEleprice) priceList
							.get(i);
					CotPriceDetail newde = (CotPriceDetail) map
							.get(priceEleprice.getPriceDetailId());
					priceEleprice.setPriceDetailId(newde.getId());
					priceEleprice.setPriceId(clone.getId());
					priceEleprice.setId(null);
					listPrice.add(priceEleprice);
				}
				try {
					// 另存订单明细的图片
					this.getPriceDao().saveRecords(list);
					// 另存配件
					this.getPriceDao().saveRecords(listFit);
					// 另存成本
					this.getPriceDao().saveRecords(listPrice);

				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("另存失败!请联系管理员!");
				}
			}
			return true;
		}
	}
	
	public Float getTuiLv(Integer hsId){
		CotEleOther cotEleOther = (CotEleOther) this.getPriceDao().getById(CotEleOther.class, hsId);
		return cotEleOther.getTaxRate();
	}
	
	// 查找默认的报表
	public CotRptFile getRptDefault(Integer rptTypeId) {
		List<?> list = this.getPriceDao().find(
				"from CotRptFile f where f.flag is not null and f.flag=1 and f.rptType=" + rptTypeId);
		if(list!=null && list.size()>0){
			return (CotRptFile) list.get(0);		
		}else{
			return null;
		}
	}
	
	//发邮件
	public String sendMail(String priceId,String priceNo,String reportTemple){
		 WebContext ctx = WebContextFactory.get();
		 String path = SystemUtil.getRptFilePath() + "price/"
		 + priceNo + ".pdf";
			String rptXMLpath = ctx.getHttpServletRequest().getRealPath("/")
					+ File.separator + reportTemple;
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("IMG_PATH", ctx.getHttpServletRequest().getRealPath(
					"/"));
			paramMap.put("HEADER_PER_PAGE", "true");
			paramMap.put("exlSheet", "false");
			paramMap.put("STR_SQL", "obj.PRICE_ID=" + priceId);

			File file = new File(SystemUtil.getRptFilePath() + "price");
			if (!file.exists()) {
				file.mkdirs();
			}

			this.getExportRptDao().exportRptToPDF(rptXMLpath, path, paramMap);
		
		return path;
	}

	public CotBaseDao getPriceDao() {
		return priceDao;
	}

	public void setPriceDao(CotBaseDao priceDao) {
		this.priceDao = priceDao;
	}

	public CotExportRptDao getExportRptDao() {
		return exportRptDao;
	}

	public void setExportRptDao(CotExportRptDao exportRptDao) {
		this.exportRptDao = exportRptDao;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public CotSysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(CotSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public CotReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(CotReportDao reportDao) {
		this.reportDao = reportDao;
	}

	public CotElementsService getCotElementsService() {
		return cotElementsService;
	}

	public void setCotElementsService(CotElementsService cotElementsService) {
		this.cotElementsService = cotElementsService;
	}

	public CotCustomerService getCustService() {
		return custService;
	}

	public void setCustService(CotCustomerService custService) {
		this.custService = custService;
	}
}
