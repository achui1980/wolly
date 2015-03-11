package com.sail.cot.service.orderfac.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.sql.Timestamp;
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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.itextpdf.text.DocumentException;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.CotExportRptDao;
import com.sail.cot.domain.CotAnwser;
import com.sail.cot.domain.CotArtWork;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotConsignCompany;
import com.sail.cot.domain.CotContract;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotNation;
import com.sail.cot.domain.CotNationCity;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderFacdetailCopy;
import com.sail.cot.domain.CotOrderMb;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderStatus;
import com.sail.cot.domain.CotOrderfacMb;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotOrderstatusFile;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.domain.CotProductFacMb;
import com.sail.cot.domain.CotProductMb;
import com.sail.cot.domain.CotQuestion;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.VProformaInvoice;
import com.sail.cot.domain.VPurchaseOrder;
import com.sail.cot.domain.vo.CotArtWordVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.QueryService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.order.impl.CotOrderServiceImpl;
import com.sail.cot.service.orderfac.CotOrderFacService;
import com.sail.cot.service.sign.impl.CotSignServiceImpl;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.SetNoService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.ListSort;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;
import com.sail.cot.util.pdf.PIService;
import com.sail.cot.util.pdf.POService;
import com.sail.cot.util.pdf.create.ArtWordPdf;
import com.sail.cot.util.pdf.create.POPdf;
import com.sail.cot.util.pdf.create.SSAPdf;
import com.sail.cot.util.pdf.impl.PIServiceImpl;
import com.sail.cot.util.pdf.impl.POServiceImpl;

/**
 * <p>
 * Title: 旗行办公自动化系统（OA）
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: May 20, 2010 3:53:05 PM
 * </p>
 * <p>
 * Class Name: CotOrderFacServiceImpl.java
 * </p>
 * 
 * @author achui
 * 
 */
public class CotOrderFacServiceImpl implements CotOrderFacService {

	private Logger logger = Log4WebUtil.getLogger(CotOrderFacServiceImpl.class);

	private CotBaseDao cotBaseDao;
	private CotExportRptDao exportRptDao;
	private CotOrderService orderService;

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public CotExportRptDao getExportRptDao() {
		return exportRptDao;
	}

	public void setExportRptDao(CotExportRptDao exportRptDao) {
		this.exportRptDao = exportRptDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private QueryService queryService;

	public QueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	private CotOpImgService imgService;

	public CotOpImgService getImgService() {
		return imgService;
	}

	public void setImgService(CotOpImgService imgService) {
		this.imgService = imgService;
	}

	// 操作日志
	CotSysLogService sysLogService;

	public void setSysLogService(CotSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	private SetNoService noService;

	public void setNoService(SetNoService noService) {
		this.noService = noService;
	}

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}

	// 得到总记录数(JDBC查询)
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getCotBaseDao().getRecordsCountJDBC(queryInfo);
	}

	// 根据条件查询记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询主单记录
	public List<?> getOrderFacList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getCotBaseDao().findRecords(queryInfo,
					CotOrderFac.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 查询采购单单号是否存在
	public Integer findIsExistOrderNo(String orderNo, String id) {
		String hql = "select obj.id from CotOrderFac obj where obj.orderNo='"
				+ orderNo + "'";
		List<?> res = this.getCotBaseDao().find(hql);
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

	// 得到objName的集合
	public List<?> getObjList(String objName) {
		return this.getCotBaseDao().getRecords(objName);
	}

	// 查找所有订单
	@SuppressWarnings("unchecked")
	public List<CotOrder> getCotOrderList() {
		List<CotOrder> orderList = new ArrayList<CotOrder>();
		List<CotOrder> records = this.getCotBaseDao().getRecords("CotOrder");
		if (records.size() > 0) {
			for (int i = 0; i < records.size(); i++) {
				CotOrder cotOrder = records.get(i);
				cotOrder.setOrderMBImg(null);
				cotOrder.setCotOrderDetails(null);
				orderList.add(cotOrder);
			}
			return orderList;
		}
		return null;
	}

	// 根据id查找采购单信息
	public CotOrderFac getOrderFacById(Integer id) {
		CotOrderFac cotOrderFac = (CotOrderFac) this.getCotBaseDao().getById(
				CotOrderFac.class, id);
		if (cotOrderFac != null) {
			CotOrderFac orderFacClone = (CotOrderFac) SystemUtil
					.deepClone(cotOrderFac);
			orderFacClone.setCotOrderFacdetails(null);
			orderFacClone.setOrderMBImg(null);
			return orderFacClone;
		}
		return null;
	}

	// 根据采购明细id查找采购明细单信息
	public CotOrderFacdetail getOrderFacDetailById(Integer id) {
		CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) this
				.getCotBaseDao().getById(CotOrderFacdetail.class, id);
		if (cotOrderFacdetail != null) {
			cotOrderFacdetail.setPicImg(null);
			return cotOrderFacdetail;
		}
		return null;
	}

	// 根据采购明细id查找采购图片对象
	public CotOrderfacPic getOrderFacPicByDetailId(Integer detailId) {
		List<?> picList = this.getCotBaseDao().find(
				"from CotOrderfacPic obj where obj.fkId =" + detailId);
		if (picList.size() > 0) {
			CotOrderfacPic cotOrderfacPic = (CotOrderfacPic) picList.get(0);
			cotOrderfacPic.setPicImg(null);
			return cotOrderfacPic;
		}
		return null;
	}

	// 判断该主单的明细是否已存在该产品货号
	@SuppressWarnings("unchecked")
	public boolean findIsExistEleId(Integer mainId, String eleId,
			Integer detailId) {
		List<Integer> res = this.getCotBaseDao().find(
				"select c.id from CotOrderFacdetail c where c.orderId = "
						+ mainId + " and c.eleId='" + eleId + "'");
		if (res.size() == 0) {
			return false;
		} else if (res.size() == 1) {
			if (!res.get(0).toString().equals(detailId.toString())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	// 根据订单编号字符串组装采购明细
	@SuppressWarnings("unchecked")
	public List<CotOrderFacdetail> findOrderFacDetailByIds(String ids) {
		List<CotOrderDetail> list = new ArrayList<CotOrderDetail>();
		List<CotOrderFacdetail> records = new ArrayList<CotOrderFacdetail>();
		String hql = "from CotOrderDetail obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		list = this.getCotBaseDao().find(hql);
		if (list.size() == 0) {
			return null;
		}
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotOrderDetail orderDetail = (CotOrderDetail) it.next();

			// 新建采购明细对象
			CotOrderFacdetail cotOrderFacdetail = new CotOrderFacdetail();
			// 使用反射获取对象的所有属性名称
			String[] propEle = ReflectionUtils
					.getDeclaredFields(CotOrderDetail.class);

			ConvertUtilsBean convertUtils = new ConvertUtilsBean();
			SqlDateConverter dateConverter = new SqlDateConverter();
			convertUtils.register(dateConverter, Date.class);
			// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
			BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
					new PropertyUtilsBean());

			for (int i = 0; i < propEle.length; i++) {
				try {
					String value = beanUtils.getProperty(orderDetail,
							propEle[i]);
					if ("op".equals(propEle[i]) || "id".equals(propEle[i])
							|| "picImg".equals(propEle[i])
							|| "addTime".equals(propEle[i])
							|| "eleHsid".equals(propEle[i])
							|| "eleComposeType".equals(propEle[i])
							|| "eleUnitNum".equals(propEle[i])
							|| "boxBDesc".equals(propEle[i])
							|| "boxTDesc".equals(propEle[i])
							|| "boxHandleH".equals(propEle[i])
							|| "liRun".equals(propEle[i])
							|| "addPerson".equals(propEle[i])
							|| "unBoxCount".equals(propEle[i])
							|| "unBoxSend".equals(propEle[i])
							|| "unBoxSend4OrderFac".equals(propEle[i])
							|| "unBoxCount4OrderFac".equals(propEle[i])
							|| "boxCount".equals(propEle[i])
							|| "orderId".equals(propEle[i])
							|| "tuiLv".equals(propEle[i])
							|| "orderFacno".equals(propEle[i])
							|| "orderFacnoid".equals(propEle[i])
							|| "outHasBoxCount4OrderFac".equals(propEle[i])
							|| "eleFittingPrice".equals(propEle[i])
							|| "elePrice".equals(propEle[i])) {
						continue;
					}
					if (value != null) {
						beanUtils.setProperty(cotOrderFacdetail, propEle[i],
								value);
					}
				} catch (Exception e) {
					logger.error(propEle[i] + ":转换错误!");
				}
			}
			cotOrderFacdetail.setHsId(orderDetail.getEleHsid());// 海关编码
			cotOrderFacdetail
					.setEleComposetype(orderDetail.getEleComposeType());
			cotOrderFacdetail.setEleUnitnum(orderDetail.getEleUnitNum());
			cotOrderFacdetail.setBoxBdesc(orderDetail.getBoxBDesc());
			cotOrderFacdetail.setBoxTdesc(orderDetail.getBoxTDesc());
			cotOrderFacdetail.setBoxHandleh(orderDetail.getBoxHandleH());
			cotOrderFacdetail.setOrderDetailId(orderDetail.getId());// 设置所取订单明细Id
			cotOrderFacdetail.setBoxCount(orderDetail.getUnBoxCount4OrderFac());// 未采购数量
			cotOrderFacdetail.setOutRemain(orderDetail.getBoxCount());// 剩余出货数量
			if (orderDetail.getContainerCount() != 0
					&& orderDetail.getContainerCount() != null) {
				if (orderDetail.getUnBoxCount4OrderFac()
						% (orderDetail.getBoxCount() / orderDetail
								.getContainerCount()) != 0) {
					cotOrderFacdetail.setUnContainerCount(orderDetail
							.getUnBoxCount4OrderFac()
							/ (orderDetail.getBoxCount() / orderDetail
									.getContainerCount()) + 1);
				} else {
					cotOrderFacdetail.setUnContainerCount(orderDetail
							.getUnBoxCount4OrderFac()
							/ (orderDetail.getBoxCount() / orderDetail
									.getContainerCount()));
				}
			} else {
				cotOrderFacdetail.setUnContainerCount(0L);
			}

			records.add(cotOrderFacdetail);
		}
		return records;
	}

	// 获取采购主单厂家id,用于比较是否订单明细的厂家
	public Integer getOrderfacFactoryId(Integer orderid) {
		CotOrderFac orderFac = this.getOrderFacById(orderid);
		if (orderFac != null && orderFac.getFactoryId() != null) {
			return orderFac.getFactoryId();
		} else {
			return null;
		}
	}

	// 查询采购明细中是否有该订单明细的采购记录
	public CotOrderFacdetail isExistOrderFacDetail(CotOrderDetail detail) {
		CotOrderFacdetail facdetail = new CotOrderFacdetail();

		String hql = " from CotOrderFacdetail obj where obj.orderDetailId = "
				+ detail.getId();
		List<?> list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			facdetail = (CotOrderFacdetail) list.get(i);
			// 获取采购主单厂家id,用于比较是否订单明细的厂家
			Integer factoryId = this.getOrderfacFactoryId(facdetail
					.getOrderId());
			if (detail.getFactoryId().intValue() == factoryId.intValue()) {
				return facdetail;
			}
		}
		return null;
	}

	// 根据订单明细id字符串组装采购明细，并修改订单未采购数量
	@SuppressWarnings("unchecked")
	public List<CotOrderFacdetail> updateOrderDetailForFac(String ids) {
		List<CotOrderDetail> list = new ArrayList<CotOrderDetail>();
		List<CotOrderFacdetail> records = new ArrayList<CotOrderFacdetail>();
		String hql = "from CotOrderDetail obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		list = this.getCotBaseDao().find(hql);
		if (list.size() == 0) {
			return null;
		}
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			// 要转化的订单明细对象
			CotOrderDetail orderDetail = (CotOrderDetail) it.next();
			// 过滤掉没有厂家的订单明细
			if (orderDetail.getFactoryId() == null)
				continue;
			Long unBoxCount4OrderFac = orderDetail.getUnBoxCount4OrderFac();
			// 未采购数量>0
			if (unBoxCount4OrderFac > 0) {
				// 新建采购明细对象
				CotOrderFacdetail cotOrderFacdetail = new CotOrderFacdetail();
				// 查询采购明细中是否有该订单明细的采购记录
				CotOrderFacdetail facDetail = this
						.isExistOrderFacDetail(orderDetail);
				if (facDetail != null) {
					cotOrderFacdetail = facDetail;
				}
				// 使用反射获取对象的所有属性名称
				String[] propEle = ReflectionUtils
						.getDeclaredFields(CotOrderDetail.class);

				ConvertUtilsBean convertUtils = new ConvertUtilsBean();
				SqlDateConverter dateConverter = new SqlDateConverter();
				convertUtils.register(dateConverter, Date.class);
				// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
				BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
						new PropertyUtilsBean());

				for (int i = 0; i < propEle.length; i++) {
					try {
						String value = beanUtils.getProperty(orderDetail,
								propEle[i]);
						if ("op".equals(propEle[i]) || "id".equals(propEle[i])
								|| "picImg".equals(propEle[i])
								|| "addTime".equals(propEle[i])
								|| "eleHsid".equals(propEle[i])
								|| "unBoxCount".equals(propEle[i])
								|| "unBoxSend".equals(propEle[i])
								|| "unBoxCount4OrderFac".equals(propEle[i])
								|| "unBoxSend4OrderFac".equals(propEle[i])
								|| "boxCount".equals(propEle[i])
								|| "liRun".equals(propEle[i])
								|| "tuiLv".equals(propEle[i])
								|| "orderFacno".equals(propEle[i])
								|| "orderFacnoid".equals(propEle[i])
								|| "outHasBoxCount4OrderFac".equals(propEle[i])) {
							continue;
						}
						if (value != null) {
							beanUtils.setProperty(cotOrderFacdetail,
									propEle[i], value);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(propEle[i] + ":转换错误!");
					}
				}
				cotOrderFacdetail.setHsId(orderDetail.getEleHsid());// 海关编码
				cotOrderFacdetail.setOrderDetailId(orderDetail.getId());// 设置所取订单明细Id
				if (facDetail != null) {
					cotOrderFacdetail.setBoxCount(facDetail.getBoxCount()
							+ orderDetail.getUnBoxCount4OrderFac());
					cotOrderFacdetail.setOutRemain(facDetail.getBoxCount());
				} else {
					cotOrderFacdetail.setBoxCount(orderDetail
							.getUnBoxCount4OrderFac());// 未采购数量
					cotOrderFacdetail.setOutRemain(orderDetail
							.getUnBoxCount4OrderFac());// 剩余出货数量
				}
				records.add(cotOrderFacdetail);

				// 修改订单未采购数量
				orderDetail.setUnBoxCount4OrderFac(new Long(0));
				orderDetail.setUnBoxSend4OrderFac(1);// 1:全部发货
				List<CotOrderDetail> orderDetailList = new ArrayList<CotOrderDetail>();
				orderDetailList.add(orderDetail);
				this.getCotBaseDao().updateRecords(orderDetailList);
			}
		}
		return records;
	}

	// 根据采购主单id查找采购明细
	@SuppressWarnings("unchecked")
	public List<CotOrderFacdetail> getFacDetailByOrderId(Integer orderId) {
		List<CotOrderFacdetail> facdetailList = this.getCotBaseDao().find(
				"from CotOrderFacdetail obj where obj.orderId=" + orderId);
		if (facdetailList.size() > 0) {
			return facdetailList;
		}
		return null;
	}

	// 保存或者更新主采购单
	public Integer saveOrUpdateOrderFac(CotOrderFac cotOrderFac,
			String orderTime, String sendTime, String shipmentDate,
			String addTime, boolean flag,String oderFacText) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		try {
			if (cotOrderFac.getId() == null) {
				cotOrderFac.setAddTime(new Timestamp(sdf.parse(addTime)
						.getTime()));// 创建时间
			} else {
				cotOrderFac
						.setModTime(new Timestamp(System.currentTimeMillis()));// 修改时间
				cotOrderFac.setModPerson(empId);
			}

			// cotOrderFac.setAddTime(new Date(System.currentTimeMillis()));//
			// 添加时间
			if (orderTime != null && !"".equals(orderTime)) {
				cotOrderFac.setOrderTime(new Date(sdf.parse(orderTime)
						.getTime()));// 下单时间
			} else {
				cotOrderFac.setOrderTime(null);
			}
			if (sendTime != null && !"".equals(sendTime)) {
				cotOrderFac
						.setSendTime(new Date(sdf.parse(sendTime).getTime()));// 交货时间
			} else {
				cotOrderFac.setSendTime(null);
			}
			if (shipmentDate != null && !"".equals(shipmentDate)) {
				cotOrderFac.setShipmentDate(new Date(sdf.parse(shipmentDate)
						.getTime()));// Shipment(船期)
			}
			if (oderFacText != null && !"".equals(oderFacText)) {
				cotOrderFac
						.setOderFacText(new Date(sdf.parse(oderFacText).getTime()));// 交货时间
			} else {
				cotOrderFac.setOderFacText(null);
			}
			cotOrderFac.setEmpId(empId);// 操作人编号
			// 审核状态 0:(未审核),1(已审核)通过,2(审核不通过)
			// cotOrderFac.setOrderStatus(new Long(0));// 审核状态
			cotOrderFac.setOrderIscheck(new Long(0));// 是否需要审核
			// 是否需要审核 0:不需要审核 1:需要审核 （默认0）

			List<CotOrderFac> list = new ArrayList<CotOrderFac>();
			CotOrderfacMb facMb = new CotOrderfacMb();
			CotProductFacMb proFacMb = new CotProductFacMb();
			boolean isNew = cotOrderFac.getId() == null ? true : false;
			if (cotOrderFac.getId() == null) {
				// 更新全局序列号
				// SetNoServiceImpl setNoService = new SetNoServiceImpl();
				// setNoService.saveSeq("orderfac", cotOrderFac.getOrderTime()
				// .toString());
				CotSeqService cotSeqService = new CotSeqServiceImpl();
				cotSeqService.saveSeq("orderfac");
			} else {
				// 主单总数量,总箱数,总体积,总金额
				CotOrderFac orderFac = (CotOrderFac) this.getCotBaseDao()
						.getById(CotOrderFac.class, cotOrderFac.getId());
				cotOrderFac.setTotalCbm(orderFac.getTotalCbm());// 总CBM
				cotOrderFac.setTotalContainer(orderFac.getTotalContainer());// 总箱数
				cotOrderFac.setTotalCount(orderFac.getTotalCount());// 总数量
				cotOrderFac.setTotalMoney(orderFac.getTotalMoney());// 总金额
				// modifyCotOrderFacTotal(cotOrderFac.getId());
			}
			cotOrderFac
					.setAddTime(new Timestamp(sdf2.parse(addTime).getTime()));// 创建时间
			list.add(cotOrderFac);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			// 主单保存后，保存麦标和产品标(新建时)
			if (isNew) {
				List listMb = new ArrayList();
				byte[] b = this.getZwtpPic();
				facMb.setPicImg(b);
				facMb.setPicSize(b.length);
				facMb.setFkId(cotOrderFac.getId());
				listMb.add(facMb);
				this.getCotBaseDao().saveOrUpdateRecords(listMb);

				List listFacMb = new ArrayList();
				proFacMb.setPicImg(b);
				proFacMb.setPicSize(b.length);
				proFacMb.setFkId(cotOrderFac.getId());
				listFacMb.add(proFacMb);
				this.getCotBaseDao().saveOrUpdateRecords(listFacMb);
			}
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setItemNo(cotOrderFac.getOrderNo());
			syslog.setOpMessage("添加或修改主采购单成功");
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);

			// 如果币种改变时,更改所有明细价格
			// if (flag == true) {
			// String hql = "from CotOrderFacdetail obj where obj.orderId="+
			// cotOrderFac.getId();
			// List<?> detailList = this.getCotBaseDao().find(hql);
			// Iterator<?> it = detailList.iterator();
			// while (it.hasNext()) {
			// CotOrderFacdetail facDetail = (CotOrderFacdetail) it.next();
			// Long boxCount = facDetail.getBoxCount();//采购数量
			// Float oldPriceFac = facDetail.getPriceFac();//原厂价
			// Integer oldCurId = facDetail.getPriceFacUint();//原厂价币种
			// Integer newCurId = cotOrderFac.getCurrencyId();//新厂价币种
			// Float newPriceFac =
			// this.getQueryService().updatePrice(oldPriceFac, oldCurId,
			// newCurId);//新厂价
			// Float newTotalFac = newPriceFac * boxCount;//新总厂价
			// facDetail.setPriceFac(newPriceFac);
			// facDetail.setPriceFacUint(newCurId);
			// facDetail.setTotalFac(newTotalFac);
			// }
			// this.getCotBaseDao().updateRecords(detailList);
			//				
			// // 保存到系统日记表
			// CotSyslog syslog2 = new CotSyslog();
			// syslog2.setEmpId(empId);
			// syslog2.setOpMessage("修改采购单明细成功");
			// syslog2.setOpModule("orderfac");
			// syslog2.setOpTime(new Date(System.currentTimeMillis()));
			// syslog2.setOpType(2);
			// syslog2.setItemNo(cotOrderFac.getOrderNo());
			// sysLogService.addSysLogByObj(syslog2);
			// // 修改主单的总数量,总箱数,总体积,总金额
			// this.modifyCotOrderFacTotal(cotOrderFac.getId());
			// }

			return cotOrderFac.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存主采购单出错！");

			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setItemNo(cotOrderFac.getOrderNo());
			syslog.setOpMessage("添加或修改主采购单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);

			return null;
		}
	}

	// 修改主采购单
	public void updateOrderFac(CotOrderFac cotOrderFac) {
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		List<CotOrderFac> list = new ArrayList<CotOrderFac>();
		// 还原唛标
		CotOrderFac orderOld = (CotOrderFac) this.getCotBaseDao().getById(
				CotOrderFac.class, cotOrderFac.getId());
		System.out.println(new java.util.Date().toGMTString());
		System.out.println("==============old"+cotOrderFac.getOderFacText()+"=======");
		cotOrderFac.setOrderMBImg(orderOld.getOrderMBImg());
		cotOrderFac.setAddTime(orderOld.getAddTime());
		cotOrderFac.setModTime(orderOld.getModTime());
		list.add(cotOrderFac);
		try {
			this.getCotBaseDao().updateRecords(list);
			System.out.println("==============new"+cotOrderFac.getOderFacText()+"=======");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotOrderFac.getOrderNo());
			syslog.setOpMessage("修改主采购单成功");
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			updateOrderOut(cotOrderFac);
		} catch (Exception e) {
			e.printStackTrace();
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotOrderFac.getOrderNo());
			syslog.setOpMessage("修改主采购单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		}

	}
	private void updateOrderOut(CotOrderFac orderFac){
		List list = this.getCotBaseDao().find(" from CotOrderOut obj where obj.orderId="+orderFac.getOrderId());
		if(CollectionUtils.isNotEmpty(list)){
			CotOrderOut orderOut = (CotOrderOut)list.get(0);
			java.sql.Date date = new java.sql.Date(orderFac.getEtdDate().getTime());
			orderOut.setSendTime(date);
			orderOut.setCotOrderOutdetails(null);
			orderOut.setCotOrderOuthsdetails(null);
			orderOut.setCotShipments(null);
			orderOut.setCotSplitInfos(null);
			orderOut.setCotOrderouthsRpt(null);
			orderOut.setCotSymbols(null);
			orderOut.setCotHsInfos(null);
			list.clear();
			list.add(orderOut);
			this.getCotBaseDao().saveOrUpdateRecords(list);
		}
		
	}
	// 修改主单的总数量,总箱数,总体积,总金额
	public void modifyFacTotalByMap(HashMap<Integer, Integer> map) {
		for (Iterator<Integer> itor = map.keySet().iterator(); itor.hasNext();) {
			Integer factoryId = (Integer) itor.next();
			Integer orderFacId = (Integer) map.get(factoryId);
			this.modifyCotOrderFacTotal(orderFacId);
		}
	}

	// 修改主单的总数量,总箱数,总体积,总金额,返回总金额
	public Double modifyCotOrderFacTotal(Integer orderId) {
		// 获取操作人
		// 查找主单号对象,修改总数量,总箱数,总体积,订单总金额
		CotOrderFac cotOrderFac = (CotOrderFac) this.getCotBaseDao().getById(
				CotOrderFac.class, orderId);
		double totalFac = 0.0;
		int totalCount = 0;
		int totalContainer = 0;
		double totalCbm = 0.0;
		// String hql = "from CotOrderFacdetail obj where obj.orderId = " +
		// orderId;
		// List<?> details = this.getCotBaseDao().find(hql);
		// Iterator<?> it = details.iterator();
		Set<?> set = cotOrderFac.getCotOrderFacdetails();
		Iterator<?> it = set.iterator();
		while (it.hasNext()) {
			CotOrderFacdetail detail = (CotOrderFacdetail) it.next();
			if (detail.getTotalFac() == null) {
				detail.setTotalFac(0f);
			}
			if (detail.getBoxCount() == null) {
				detail.setBoxCount(0L);
			}
			if (detail.getContainerCount() == null) {
				detail.setContainerCount(0L);
			}
			if (detail.getBoxCbm() == null) {
				detail.setBoxCbm(0f);
			}
			totalFac += detail.getTotalFac();
			totalCount += detail.getBoxCount();
			totalContainer += detail.getContainerCount();
			totalCbm += detail.getBoxCbm() * detail.getContainerCount();
		}
		try {
			cotOrderFac.setTotalCbm(totalCbm);// 总CBM
			cotOrderFac.setTotalContainer(totalContainer);// 总箱数
			cotOrderFac.setTotalCount(totalCount);// 总数量

			// this.getCotBaseDao().update(cotOrderFac);
			// 累加其他费用的总额
			String hql = " from CotFinaceOther obj where obj.source='orderfac' and obj.fkId="
					+ orderId;
			List<?> listOther = this.getCotBaseDao().find(hql);
			Iterator<?> itOther = listOther.iterator();
			Double total = 0.0;
			while (itOther.hasNext()) {
				CotFinaceOther other = (CotFinaceOther) itOther.next();
				if (other != null) {
					if (other.getFlag().equals("A")) {
						total += other.getAmount();
					} else {
						total -= other.getAmount();
					}
				}
			}
			Double totalMoney = totalFac + total;
			cotOrderFac.setTotalMoney(totalFac);// 货款金额
			cotOrderFac.setRealMoney(totalMoney);// 实际金额

			// 克隆对象,避免造成指针混用
			CotOrderFac cloneObj = (CotOrderFac) SystemUtil
					.deepClone(cotOrderFac);
			List<CotOrderFac> list = new ArrayList<CotOrderFac>();
			list.add(cloneObj);
			this.getCotBaseDao().updateRecords(list);
			return totalMoney;
			/*
			 * //添加操作日志 CotSyslog syslog = new CotSyslog();
			 * syslog.setEmpId(cotEmps.getId());
			 * syslog.setItemNo(cotOrderFac.getOrderNo());
			 * syslog.setOpMessage("修改采购单总额成功"); syslog.setOpModule("orderfac");
			 * syslog.setOpTime(new Date(System.currentTimeMillis()));
			 * syslog.setOpType(2); //1:添加 2：修改 3：删除 List<CotSyslog> logList =
			 * new ArrayList<CotSyslog>(); logList.add(syslog);
			 * sysLogService.addSysLog(logList);
			 */

		} catch (Exception e) {
			logger.error("修改采购单总额出错!");
			e.printStackTrace();
			return null;
			/*
			 * //添加操作日志 CotSyslog syslog = new CotSyslog();
			 * syslog.setEmpId(cotEmps.getId());
			 * syslog.setItemNo(cotOrderFac.getOrderNo());
			 * syslog.setOpMessage("修改采购单总额失败，失败原因："+e.getMessage());
			 * syslog.setOpModule("orderfac"); syslog.setOpTime(new
			 * Date(System.currentTimeMillis())); syslog.setOpType(2); //1:添加
			 * 2：修改 3：删除 List<CotSyslog> logList = new ArrayList<CotSyslog>();
			 * logList.add(syslog); sysLogService.addSysLog(logList);
			 */
		}
	}

	// 查询采购单的总金额
	public Float findTotalMoney(Integer orderId) {
		String hql = "select obj.totalFac from CotOrderFacdetail obj where obj.orderId="
				+ orderId;
		List<?> list = this.getCotBaseDao().find(hql);
		Iterator<?> it = list.iterator();
		float total = 0.0f;
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				total += (Float) obj;
			}
		}
		return total;
	}

	// 删除主采购单
	@SuppressWarnings("deprecation")
	public Boolean deleteOrderFacs(List<CotOrderFac> orderFacList) {
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < orderFacList.size(); i++) {
			CotOrderFac cotOrderFac = (CotOrderFac) orderFacList.get(i);
			Integer orderId = cotOrderFac.getId();
			// 获取该采购单下所有明细的集合
			List<CotOrderFacdetail> list = this.getFacDetailByOrderId(orderId);
			if (list != null) {
				for (int j = 0; j < list.size(); j++) {
					CotOrderFacdetail cotOrderFacdetail = list.get(j);
					// 采购明细采购数量
					Long boxCount = cotOrderFacdetail.getBoxCount();
					Integer orderDetailId = cotOrderFacdetail
							.getOrderDetailId();
					// 获取对应订单明细对象
					CotOrderDetail cotOrderDetail = this
							.getOrderDetailById(orderDetailId);
					if (cotOrderDetail != null) {
						// 订单明细下单数量
						Long orderBoxCount = cotOrderDetail.getBoxCount();
						// 订单明细未采购数量
						Long unBoxCount4OrderFac = cotOrderDetail
								.getUnBoxCount4OrderFac();
						// 还原数量
						Long returnBoxCount = boxCount + unBoxCount4OrderFac;
						if (returnBoxCount > 0) {
							if (returnBoxCount > orderBoxCount) {
								cotOrderDetail
										.setUnBoxCount4OrderFac(orderBoxCount);
								cotOrderDetail.setUnBoxSend4OrderFac(0);
							} else {
								cotOrderDetail
										.setUnBoxCount4OrderFac(returnBoxCount);
								cotOrderDetail.setUnBoxSend4OrderFac(0);
							}
							cotOrderDetail.setOrderFacno(null);
							cotOrderDetail.setOrderFacnoid(null);
							this.updateCotOrderDetail(cotOrderDetail);
						}
					}
				}
			}
			// 删除该单关联的其他费用
			this.delOtherFee(orderId);

			ids.add(orderId);
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotOrderFac");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除主采购单成功");
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (DAOException e) {
			logger.error("删除主采购单出错");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除主采购单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
		return true;
	}

	// 删除主单关联的其他费用
	public void delOtherFee(Integer orderId) {

		String hql = " from CotFinaceOther obj where obj.fkId=" + orderId;
		List<?> list = this.getCotBaseDao().find(hql);

		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			CotFinaceOther other = (CotFinaceOther) list.get(i);
			ids.add(other.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFinaceOther");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 保存采购单的产品明细
	public Boolean addOrderFacDetails(List<CotOrderFacdetail> details) {
		// 获取操作人
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
			this.getCotBaseDao().saveRecords(details);
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加采购单明细成功");
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return true;
		} catch (DAOException e) {
			logger.error("保存采购单的产品明细出错!");
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加采购单明细失败，失败原因：" + e.getMessage());
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
	}

	// 更新采购单明细
	public Boolean modifyOrderFacDetail(CotOrderFacdetail e, String eleProTime) {
		// 获取操作人
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
		// // 没选择厂家时,设置厂家为'未定义'
		// if (e.getFactoryId() == null) {
		// String hql = "from CotFactory obj where obj.shortName='未定义'";
		// List<?> list = this.getCotBaseDao().find(hql);
		// CotFactory facDefault = (CotFactory) list.get(0);
		// e.setFactoryId(facDefault.getId());
		// }
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

		// 设置图片
		// String hql = "select obj.picImg from CotOrderFacdetail obj where
		// obj.id="+e.getId();
		// List<?> list = this.getCotBaseDao().find(hql);
		// e.setPicImg((byte[])list.get(0));
		try {
			List<CotOrderFacdetail> list = new ArrayList<CotOrderFacdetail>();
			list.add(e);
			this.getCotBaseDao().updateRecords(list);
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setItemNo(e.getId().toString());
				syslog.setOpMessage("修改采购单明细成功");
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}

		} catch (Exception ex) {
			logger.error("保存采购明细错误!");
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setItemNo(e.getId().toString());
				syslog.setOpMessage("修改采购单明细失败，失败原因：" + ex.getMessage());
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
		return true;
	}

	// 批量更新采购单的产品明细
	public Boolean modifyOrderFacDetails(List<CotOrderFacdetail> details) {
		// 获取操作人
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
			this.getCotBaseDao().updateRecords(details);
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改采购单明细成功");
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return true;
		} catch (Exception e) {
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改采购单明细失败，失败原因：" + e.getMessage());
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}

	}

	// 根据采购明细产品的ids删除
	public boolean deleteDetailByIds(List<Integer> ids) {
		// 获取操作人
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
		for (int i = 0; i < ids.size(); i++) {
			Integer detailId = ids.get(i);
			// 通过id获取采购明细对象
			CotOrderFacdetail cotOrderFacdetail = this
					.getOrderFacDetailById(detailId);
			if (cotOrderFacdetail != null) {
				// 采购明细采购数量
				Long boxCount = cotOrderFacdetail.getBoxCount();
				// 获取对应订单明细对象
				Integer orderDetailId = cotOrderFacdetail.getOrderDetailId();
				CotOrderDetail orderDetail = this
						.getOrderDetailById(orderDetailId);
				if (orderDetail != null) {
					// 订单明细下单数量
					Long orderBoxCount = orderDetail.getBoxCount();
					// 订单明细未采购数量
					Long unBoxCount4OrderFac = orderDetail
							.getUnBoxCount4OrderFac();
					// 还原数量
					Long returnBoxCount = boxCount + unBoxCount4OrderFac;
					if (returnBoxCount > 0) {
						if (returnBoxCount > orderBoxCount) {
							orderDetail.setUnBoxCount4OrderFac(orderBoxCount);
							orderDetail.setUnBoxSend4OrderFac(0);
						} else {
							orderDetail.setUnBoxCount4OrderFac(returnBoxCount);
							orderDetail.setUnBoxSend4OrderFac(0);
						}
						this.updateCotOrderDetail(orderDetail);
					}
				}
			}
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotOrderFacdetail");
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量删除采购单明细成功");
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量删除采购单明细失败，失败原因：" + e.getMessage());
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
	}

	// 通过id获取订单明细对象
	public CotOrderDetail getOrderDetailById(Integer orderDetailId) {
		CotOrderDetail cotOrderDetail = (CotOrderDetail) this.getCotBaseDao()
				.getById(CotOrderDetail.class, orderDetailId);
		if (cotOrderDetail != null) {
			return cotOrderDetail;
		}
		return null;
	}

	// 更新订单明细对象
	public boolean updateCotOrderDetail(CotOrderDetail cotOrderDetail) {
		// 获取操作人

		try {
			List<CotOrderDetail> list = new ArrayList<CotOrderDetail>();
			list.add(cotOrderDetail);
			this.getCotBaseDao().updateRecords(list);
			/*
			 * //添加操作日志 CotSyslog syslog = new CotSyslog();
			 * syslog.setEmpId(cotEmps.getId());
			 * syslog.setItemNo(cotOrderDetail.getId().toString());
			 * syslog.setOpMessage("修改订单明细成功"); syslog.setOpModule("orderfac");
			 * syslog.setOpTime(new Date(System.currentTimeMillis()));
			 * syslog.setOpType(2); //1:添加 2：修改 3：删除 List<CotSyslog> logList =
			 * new ArrayList<CotSyslog>(); logList.add(syslog);
			 * sysLogService.addSysLog(logList);
			 */
			return true;
		} catch (Exception e) {
			/*
			 * //添加操作日志 CotSyslog syslog = new CotSyslog();
			 * syslog.setEmpId(cotEmps.getId());
			 * syslog.setItemNo(cotOrderDetail.getId().toString());
			 * syslog.setOpMessage("修改订单明细失败，失败原因："+e.getMessage());
			 * syslog.setOpModule("orderfac"); syslog.setOpTime(new
			 * Date(System.currentTimeMillis())); syslog.setOpType(2); //1:添加
			 * 2：修改 3：删除 List<CotSyslog> logList = new ArrayList<CotSyslog>();
			 * logList.add(syslog); sysLogService.addSysLog(logList);
			 */
			return false;
		}

	}

	// 通过id获取订单中的未采购数量
	public Long getUnBoxCount4OrderFacById(Integer orderDetailId) {
		CotOrderDetail cotOrderDetail = (CotOrderDetail) this.getCotBaseDao()
				.getById(CotOrderDetail.class, orderDetailId);
		if (cotOrderDetail != null) {
			return cotOrderDetail.getUnBoxCount4OrderFac();
		}
		return null;
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMapAction(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?,
		// ?>)request.getSession().getAttribute("sysdic");
		// List<?> list = (List<?>)dicMap.get("factory");
		List<?> list = this.getCotBaseDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有公司
	public Map<?, ?> getCompanyNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotCompany");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany
					.getCompanyShortName());
		}
		return map;
	}

	// 查询所有订单号
	public Map<?, ?> getOrderNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotOrder");
		for (int i = 0; i < list.size(); i++) {
			CotOrder cotOrder = (CotOrder) list.get(i);
			map.put(cotOrder.getId().toString(), cotOrder.getOrderNo());
		}
		return map;
	}

	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 查询所有币种
	public Map<?, ?> getCurrencyMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotCurrency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId().toString(), cotCurrency.getCurNameEn());
		}
		return map;
	}

	// 获取默认公司ID
	public Integer getDefaultCompanyId() {
		List<?> ids = this
				.getCotBaseDao()
				.find(
						"select obj.id from CotCompany obj where obj.companyIsdefault=1");
		if (ids != null & ids.size() > 0) {
			return (Integer) ids.get(0);
		}
		return null;
	}

	// 从数据字典中取生产合同合同条款
	public String getContract() {
		List<?> list = this.getCotBaseDao().find(
				"from CotContract obj where obj.contractType=2");
		if (list != null && list.size() > 0) {
			String orderFacContract = "\n";
			for (int i = 0; i < list.size(); i++) {
				CotContract contract = (CotContract) list.get(i);
				String contractContent = contract.getContractContent();
				orderFacContract += contractContent + "\n";
			}
			return orderFacContract;
		}
		return null;
	}

	// 判断货号字符串是否已经添加
	public List<String> checkExistList(String detailIds) {
		String[] detailIdAry = detailIds.split(",");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < detailIdAry.length; i++) {
			boolean flag = this.checkExist(detailIdAry[i]);
			if (!flag) {
				list.add(detailIdAry[i]);
			}
		}
		return list;
	}

	// 判断货号是否已经添加
	@SuppressWarnings("unchecked")
	public boolean checkExist(String detailId) {
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (orderFacMap != null) {
			if (orderFacMap.containsKey(detailId)) {
				return true;
			}
		}
		return false;
	}

	// 清空明细Map
	public void clearMap() {
		SystemUtil.clearSessionByType(null, "orderfac");
	}

	// 清空明细(addMap)
	public void clearAddMap() {
		SystemUtil.clearSessionByType(null, "orderfacAdd");
	}

	// 通过key获取Map的value
	@SuppressWarnings("unchecked")
	public CotOrderFacdetail getOrderFacMapValue(String eleId) {
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (orderFacMap != null) {
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) orderFacMap
					.get(eleId);
			return cotOrderFacdetail;
		}
		return null;
	}

	// 通过key获取(addMap)的value
	@SuppressWarnings("unchecked")
	public CotOrderFacdetail getAddMapValue(String orderDetailId) {
		Object obj = SystemUtil.getObjBySession(null, "orderfacAdd");
		HashMap<String, CotOrderFacdetail> addMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (addMap != null) {
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) addMap
					.get(orderDetailId);
			return cotOrderFacdetail;
		}
		return null;
	}

	// 储存Map
	@SuppressWarnings("unchecked")
	public void setOrderFacMap(String eleId, CotOrderFacdetail cotOrderFacdetail) {
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		if (obj == null) {
			HashMap<String, CotOrderFacdetail> orderFacMap = new HashMap<String, CotOrderFacdetail>();
			orderFacMap.put(eleId, cotOrderFacdetail);
			SystemUtil.setObjBySession(null, orderFacMap, "orderfac");
		} else {
			HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
			orderFacMap.put(eleId, cotOrderFacdetail);
			SystemUtil.setObjBySession(null, orderFacMap, "orderfac");
		}
	}

	// 储存(addMap)
	@SuppressWarnings("unchecked")
	public void setAddMap(String orderDetailId,
			CotOrderFacdetail cotOrderFacdetail) {
		Object obj = SystemUtil.getObjBySession(null, "orderfacAdd");
		if (obj == null) {
			HashMap<String, CotOrderFacdetail> addMap = new HashMap<String, CotOrderFacdetail>();
			addMap.put(orderDetailId, cotOrderFacdetail);
			SystemUtil.setObjBySession(null, addMap, "orderfacAdd");
		} else {
			HashMap<String, CotOrderFacdetail> addMap = (HashMap<String, CotOrderFacdetail>) obj;
			addMap.put(orderDetailId, cotOrderFacdetail);
			SystemUtil.setObjBySession(null, addMap, "orderfacAdd");
		}
	}

	// 通过detailId修改Map中对应的采购明细
	public boolean updateMapValueByDetailId(String eleId, String property,
			String value) {
		CotOrderFacdetail cotOrderFacdetail = getOrderFacMapValue(eleId
				.toLowerCase());
		if (cotOrderFacdetail == null)
			return false;
		try {
			BeanUtils.setProperty(cotOrderFacdetail, property, value);
			this.setOrderFacMap(eleId.toLowerCase(), cotOrderFacdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 通过orderDetailId修改(addMap)中对应的采购明细
	public boolean updateAddMap(String orderDetailId, String property,
			String value) {
		CotOrderFacdetail cotOrderFacdetail = getAddMapValue(orderDetailId);
		if (cotOrderFacdetail == null)
			return false;
		try {
			BeanUtils.setProperty(cotOrderFacdetail, property, value);
			this.setAddMap(orderDetailId, cotOrderFacdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 通过货号修改Map中对应的征样明细
	public boolean updateMapValueByEleId(String eleId, String property,
			String value) {
		CotOrderFacdetail cotOrderFacDetail = getOrderFacMapValue(eleId
				.toLowerCase());
		if (cotOrderFacDetail == null)
			return false;
		try {
			BeanUtils.setProperty(cotOrderFacDetail, property, value);
			this.setOrderFacMap(eleId.toLowerCase(), cotOrderFacDetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 通过addBoxCount修改addMap中相应的值
	public boolean updateAddMapByAddBoxCount(String orderDetailId,
			Integer addBoxCount) {
		CotOrderFacdetail cotOrderFacdetail = getAddMapValue(orderDetailId);
		if (cotOrderFacdetail == null)
			return false;
		try {
			Long oldBoxCount = cotOrderFacdetail.getBoxCount(); // 采购数量
			Long boxObCount = cotOrderFacdetail.getBoxObCount();// 外装数
			Float priceFac = cotOrderFacdetail.getPriceFac(); // 厂价单价
			Float orderPrice = cotOrderFacdetail.getOrderPrice(); // 单价

			Long boxCount = oldBoxCount + addBoxCount;
			Float totalFac = boxCount * priceFac;
			Float totalMoney = boxCount * orderPrice;
			Long containerCount;
			if (boxCount % boxObCount != 0) {
				containerCount = (boxCount / boxObCount) + 1;
			} else {
				containerCount = boxCount / boxObCount;
			}
			cotOrderFacdetail.setBoxCount(boxCount);
			cotOrderFacdetail.setTotalFac(totalFac);
			cotOrderFacdetail.setTotalMoney(totalMoney);
			cotOrderFacdetail.setContainerCount(containerCount);
			this.setAddMap(orderDetailId, cotOrderFacdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 清除Map中eleId对应的映射
	@SuppressWarnings("unchecked")
	public void delMapByKey(String eleId) {
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (orderFacMap != null) {
			if (orderFacMap.containsKey(eleId)) {
				orderFacMap.remove(eleId);
			}
		}
	}

	// 清除(addMap)中orderDetailId对应的映射
	@SuppressWarnings("unchecked")
	public void delAddMapByKey(String orderDetailId) {
		Object obj = SystemUtil.getObjBySession(null, "orderfacAdd");
		HashMap<String, CotOrderFacdetail> addMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (addMap != null) {
			if (addMap.containsKey(orderDetailId)) {
				addMap.remove(orderDetailId);
			}
		}
	}

	// Action获取Map
	@SuppressWarnings("unchecked")
	public HashMap<String, CotOrderFacdetail> getMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "orderfac");
		HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
		return orderFacMap;
	}

	// Action获取(addMap)
	@SuppressWarnings("unchecked")
	public HashMap<String, CotOrderFacdetail> getAddMapAction(
			HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "orderfacAdd");
		HashMap<String, CotOrderFacdetail> addMap = (HashMap<String, CotOrderFacdetail>) obj;
		return addMap;
	}

	// Action储存Map
	@SuppressWarnings("unchecked")
	public void setMapAction(HttpSession session, String eleId,
			CotOrderFacdetail cotOrderFacdetail) {
		Object obj = SystemUtil.getObjBySession(session, "orderfac");
		if (obj == null) {
			HashMap<String, CotOrderFacdetail> orderFacMap = new HashMap<String, CotOrderFacdetail>();
			orderFacMap.put(eleId, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, orderFacMap, "orderfac");
		} else {
			HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
			orderFacMap.put(eleId, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, orderFacMap, "orderfac");
		}

	}

	// Action储存(addMap)
	@SuppressWarnings("unchecked")
	public void setAddMapAction(HttpSession session, String orderDetailId,
			CotOrderFacdetail cotOrderFacdetail) {
		Object obj = SystemUtil.getObjBySession(session, "orderfacAdd");
		if (obj == null) {
			HashMap<String, CotOrderFacdetail> addMap = new HashMap<String, CotOrderFacdetail>();
			addMap.put(orderDetailId, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, addMap, "orderfacAdd");
		} else {
			HashMap<String, CotOrderFacdetail> addMap = (HashMap<String, CotOrderFacdetail>) obj;
			addMap.put(orderDetailId, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, addMap, "orderfacAdd");
		}
	}

	// 在Action中清除Map中eleId对应的映射
	public void delMapByKeyAction(String eleId, HttpSession session) {
		HashMap<String, CotOrderFacdetail> orderFacMap = this
				.getMapAction(session);
		if (orderFacMap != null) {
			if (orderFacMap.containsKey(eleId)) {
				orderFacMap.remove(eleId);
			}
		}
	}

	// 在Action中清除(addMap)中orderDetailId对应的映射
	public void delAddMapByKeyAction(String orderDetailId, HttpSession session) {
		HashMap<String, CotOrderFacdetail> addMap = this
				.getAddMapAction(session);
		if (addMap != null) {
			if (addMap.containsKey(orderDetailId)) {
				addMap.remove(orderDetailId);
			}
		}
	}

	// 判断该采购明细是否已存在，并将添加的对象储存到后台addMap中
	@SuppressWarnings("unchecked")
	public Integer saveToMap(CotOrderFacdetail detail) {
		Boolean test = true;
		String orderDetailId = detail.getOrderDetailId().toString(); // 订单明细id（新增）
		Long boxCount = detail.getBoxCount(); // 新增采购数量
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		// 已存在map为空
		if (obj == null) {
			this.setAddMap(orderDetailId, detail); // 将明细detail存到addMap
		} else {
			HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
			for (Iterator<String> itor = orderFacMap.keySet().iterator(); itor
					.hasNext();) {
				String oldId = (String) itor.next(); // 采购明细id
				CotOrderFacdetail old = (CotOrderFacdetail) orderFacMap
						.get(oldId);
				if (old != null) {
					// 获取订单明细id（已存在）进行比较
					String orderDetailId2 = old.getOrderDetailId().toString();
					if (orderDetailId2.equals(orderDetailId)) {
						// 计算采购数量
						Long allBox = boxCount + old.getBoxCount();
						old.setBoxCount(allBox);// 采购数量相加
						// 计算箱数
						Long boxOCount = old.getBoxObCount(); // 外箱数
						Long containerBox;
						if (allBox % boxOCount != 0) {
							containerBox = allBox / boxOCount + 1;
						} else {
							containerBox = allBox / boxOCount;
						}
						old.setContainerCount(containerBox);// 箱数相加
						// 计算总厂价
						Float priceFac = old.getPriceFac();
						Float totalFac = priceFac * allBox;
						old.setTotalFac(totalFac);
						// 计算总价
						Float orderPrice = old.getOrderPrice();
						Float totalMoney = orderPrice * allBox;
						old.setTotalMoney(totalMoney);
						// id设为空
						old.setId(null);

						// 将修改后的map保存到(addMap)
						this.setAddMap(orderDetailId, old);
						// 删除已存在map中的这条记录
						itor.remove();
						// 删除数据库中的这条记录
						// List delList = new ArrayList();
						// delList.add(Integer.parseInt(oldId));
						// this.deleteDetailByIds(delList);
						test = false;
						return old.getId();
					}
				}
			}
			if (test) {
				this.setAddMap(orderDetailId, detail); // 将明细detail存到addMap
			}
		}
		return null;
	}

	// 根据住单编号查找唛标图片（orderMBImg）
	public byte[] getFacMbByOrderFacId(Integer orderfacId) {
		List<?> list = this.getCotBaseDao().find(
				"from CotOrderfacMb obj where obj.fkId=" + orderfacId);
		CotOrderfacMb cotOrderFac = new CotOrderfacMb();
		if (list.size() == 1) {
			cotOrderFac = (CotOrderfacMb) list.get(0);
		}
		if (cotOrderFac != null) {
			return cotOrderFac.getPicImg();
		}
		return null;
	}

	// 更新采购单唛标图片
	public void updateMBImg(String filePath, Integer orderfacId,
			HttpServletRequest request) {
		CotEmps cotEmps = (CotEmps) request.getSession().getAttribute("emp");

		String hql = "from CotOrderfacMb obj where obj.fkId=" + orderfacId;
		CotOrderfacMb cotOrderfacMb = new CotOrderfacMb();
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() == 1) {
			cotOrderfacMb = (CotOrderfacMb) list.get(0);
		}
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderfacMb.setPicImg(b);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			cotOrderfacMb.setFkId(orderfacId);
			cotOrderfacMb.setPicSize(b.length);

			List<CotOrderfacMb> rec = new ArrayList<CotOrderfacMb>();
			rec.add(cotOrderfacMb);
			this.getCotBaseDao().saveOrUpdateRecords(rec);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(orderfacId.toString());
			syslog.setOpMessage("修改采购单唛标图片成功");
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改采购单唛标图片错误!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(orderfacId.toString());
			syslog.setOpMessage("修改采购单唛标图片失败，失败原因：" + e.getMessage());
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		}
	}

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic() {
		// 获得tomcat路径
		String classPath = CotOrderFacServiceImpl.class.getResource("/")
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

	// 根据明细货号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId) {
		List<?> picList = this.getCotBaseDao().find(
				"from CotOrderfacPic obj where obj.fkId =" + detailId);
		if (picList.size() > 0) {
			CotOrderfacPic cotOrderfacPic = (CotOrderfacPic) picList.get(0);
			return cotOrderfacPic.getPicImg();
		}
		return null;
	}

	// 删除明细图片picImg
	public boolean deletePicImg(Integer detailId) {
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		String classPath = CotSignServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";

		CotOrderfacPic cotOrderfacPic = this.getOrderFacPicByDetailId(detailId);
		CotOrderFacdetail cotOrderFacdetail = this
				.getOrderFacDetailById(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderfacPic.setPicImg(b);
			cotOrderfacPic.setPicSize(b.length);
			cotOrderfacPic.setPicName("zwtp");
			cotOrderfacPic.setEleId(cotOrderFacdetail.getEleId());

			List<CotOrderfacPic> picList = new ArrayList<CotOrderfacPic>();
			picList.add(cotOrderfacPic);
			this.getCotBaseDao().updateRecords(picList);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("删除采购单明细图片成功");
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除明细图片错误!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("删除采购单明细图片失败，失败原因：" + e.getMessage());
			syslog.setOpModule("orderfac");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
	}

	// 更新图片picImg字段
	public void updatePicImg(String filePath, Integer detailId) {
		CotOrderFacdetail cotOrderFacdetail = this
				.getOrderFacDetailById(detailId);
		List imgList = new ArrayList();
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotOrderfacPic cotOrderfacPic = impOpService.getOrderFacPic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			if (cotOrderfacPic == null) {
				cotOrderfacPic = new CotOrderfacPic();
				cotOrderfacPic.setFkId(detailId);
				cotOrderfacPic.setEleId(cotOrderFacdetail.getEleId());
				cotOrderfacPic.setPicName(cotOrderFacdetail.getEleId());
			}
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderfacPic.setPicImg(b);
			cotOrderfacPic.setPicSize(b.length);
			imgList.add(cotOrderfacPic);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			impOpService.saveOrUpdateImg(imgList);
		} catch (Exception e) {
			logger.error("修改采购明细图片错误!");
		}
	}

	// 同步到订单
	public void updateToOrder(String facdetailIds) {
		if (!"".equals(facdetailIds)) {
			List<CotOrderDetail> records = new ArrayList<CotOrderDetail>();
			List<CotOrderPic> picRecords = new ArrayList<CotOrderPic>();

			String[] facdetailIdAry = facdetailIds.split(",");
			String t = "";
			for (int i = 0; i < facdetailIdAry.length; i++) {
				t += facdetailIdAry[i] + ",";
			}
			// 查找同步的采购明细对象
			String hql = "from CotOrderFacdetail obj where obj.id in ("
					+ t.substring(0, t.length() - 1) + ")";
			List<?> list = this.getCotBaseDao().find(hql);
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				CotOrderFacdetail facdetail = (CotOrderFacdetail) it.next();
				// 获取采购图片对象
				CotOrderfacPic facPic = this.getImgService().getOrderFacPic(
						facdetail.getId());
				// 获取相应订单明细对象和订单图片对象
				CotOrderDetail orderDetail = this.getOrderDetailById(facdetail
						.getOrderDetailId());
				if (orderDetail != null) {
					CotOrderPic orderPic = this.getImgService().getOrderPic(
							orderDetail.getId());
					if (orderPic != null) {
						// 修改订单图片对象
						orderPic.setPicImg(facPic.getPicImg());
						orderPic.setPicSize(facPic.getPicSize());
						picRecords.add(orderPic);
					}
					// 使用反射获取采购明细对象的所有属性名称
					String[] propEle = ReflectionUtils
							.getDeclaredFields(CotOrderFacdetail.class);
					ConvertUtilsBean convertUtils = new ConvertUtilsBean();
					SqlDateConverter dateConverter = new SqlDateConverter();
					convertUtils.register(dateConverter, Date.class);
					// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
					BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
							new PropertyUtilsBean());
					for (int i = 0; i < propEle.length; i++) {
						try {
							String value = beanUtils.getProperty(facdetail,
									propEle[i]);
							if ("op".equals(propEle[i])
									|| "id".equals(propEle[i])
									|| "picImg".equals(propEle[i])
									|| "addTime".equals(propEle[i])
									|| "eleComposetype".equals(propEle[i])
									|| "eleUnitnum".equals(propEle[i])
									|| "boxBdesc".equals(propEle[i])
									|| "boxTdesc".equals(propEle[i])
									|| "boxHandleh".equals(propEle[i])
									|| "hsId".equals(propEle[i])
									|| "orderDetailId".equals(propEle[i])
									|| "orderId".equals(propEle[i])) {
								continue;
							}
							if (value != null) {
								beanUtils.setProperty(orderDetail, propEle[i],
										value);
							}
						} catch (Exception e) {
							logger.error(propEle[i] + ":转换错误!");
						}
					}
					orderDetail
							.setEleComposeType(facdetail.getEleComposetype());
					orderDetail.setEleUnitNum(facdetail.getEleUnitnum());
					orderDetail.setBoxBDesc(facdetail.getBoxBdesc());
					orderDetail.setBoxTDesc(facdetail.getBoxTdesc());
					orderDetail.setBoxHandleH(facdetail.getBoxHandleh());
					orderDetail.setEleHsid(facdetail.getHsId());
					records.add(orderDetail);
				}
			}
			this.getCotBaseDao().updateRecords(records);
			this.getCotBaseDao().updateRecords(picRecords);
		}
	}

	// 生成应付帐款单号
	public String createDealNo(Integer facId) {
		// Map idMap = new HashMap<String, Integer>();
		// idMap.put("CH", facId);
		// GenAllSeq seq = new GenAllSeq();
		// String finaceNo = seq.getAllSeqByType("fincaeaccountdealNo", idMap);

		CotSeqService seq = new CotSeqServiceImpl();
		String finaceNo = seq.getFinaceNeeGivenNo(facId);
		return finaceNo;
	}

	// 保存应付帐款单号
	public void savaSeq() {
		// GenAllSeq seq = new GenAllSeq();
		// seq.saveSeq("fincaeaccountdealNo");
		CotSeqService seq = new CotSeqServiceImpl();
		seq.saveSeq("fincaeaccountdeal");
	}

	// 保存应付帐款
	public void saveAccountdeal(CotFinaceAccountdeal dealDetail,
			String amountDate, String priceScal, String prePrice) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			if (!"".equals(amountDate) && amountDate != null) {
				dealDetail.setAmountDate(new java.sql.Date(sdf
						.parse(amountDate).getTime()));
			}

			WebContext ctx = WebContextFactory.get();
			CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

			List<CotFinaceAccountdeal> records = new ArrayList<CotFinaceAccountdeal>();

			dealDetail.setAddDate(new Date(System.currentTimeMillis()));
			dealDetail.setRealAmount(0.0);
			dealDetail.setRemainAmount(dealDetail.getAmount());
			dealDetail.setStatus(0);
			dealDetail.setSource("orderfac");
			dealDetail.setEmpId(cotEmps.getId());
			if (dealDetail.getFinaceName().equals("预付货款")) {
				dealDetail.setZhRemainAmount(0.0);
			} else {
				dealDetail.setZhRemainAmount(dealDetail.getAmount());
			}

			records.add(dealDetail);
			this.getCotBaseDao().saveRecords(records);
			// 保存单号
			this.savaSeq();
			// 修改其他费用状态 0：未生成 1: 已生成
			this.modifyFinOtherStatus(dealDetail.getFkId(), dealDetail
					.getFinaceName(), "add");

			if (!"".equals(priceScal) && !"".equals(prePrice)) {
				// 将预收货款比例和金额保存到订单
				CotOrderFac orderfac = (CotOrderFac) this.getCotBaseDao()
						.getById(CotOrderFac.class, dealDetail.getFkId());
				CotOrderFac obj = (CotOrderFac) SystemUtil.deepClone(orderfac);
				obj.setPrePrice(Float.parseFloat(prePrice));
				obj.setPriceScal(Float.parseFloat(priceScal));
				List<CotOrderFac> list = new ArrayList<CotOrderFac>();
				list.add(obj);
				this.getCotBaseDao().updateRecords(list);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	// 根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id) {
		CotFinaceOther finaceOther = (CotFinaceOther) this.getCotBaseDao()
				.getById(CotFinaceOther.class, id);
		return finaceOther;
	}

	// 根据编号获得对象
	public CotFinaceAccountdeal getGivenDealById(Integer id) {
		CotFinaceAccountdeal deal = (CotFinaceAccountdeal) this.getCotBaseDao()
				.getById(CotFinaceAccountdeal.class, id);
		return deal;
	}

	// 保存其他费用
	public Boolean addOtherList(List<?> details) {
		try {
			this.getCotBaseDao().saveRecords(details);
			return true;
		} catch (DAOException e) {
			logger.error("保存其他费用出错!");
			return false;
		}
	}

	// 更新其他费用
	public Boolean updateOtherList(List<?> details) {
		try {
			this.getCotBaseDao().updateRecords(details);
		} catch (Exception e) {
			logger.error("更新其他费用异常", e);
		}
		return true;
	}

	// 删除其他费用
	public Boolean deleteOtherList(List<?> details) {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < details.size(); i++) {
			Integer id = (Integer) details.get(i);

			ids.add(id);
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFinaceOther");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 删除应付帐款
	public Boolean deleteDealList(List<?> details) {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < details.size(); i++) {
			Integer id = (Integer) details.get(i);

			ids.add(id);
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFinaceAccountdeal");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除应付帐款异常", e);
			return false;
		}
		return true;
	}

	// 获取当前登陆员工信息
	public Boolean checkCurrEmpsIsSuperAd() {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		if ("admin".equals(cotEmps.getEmpsName())) {
			return true;
		} else {
			return false;
		}
	}

	// 批量删除时对审核通过单据的处理
	public String isOrderStatus(List<CotOrderFac> orderfacList) {
		String orderNos = new String();

		for (int i = 0; i < orderfacList.size(); i++) {
			CotOrderFac cotOrderFac = (CotOrderFac) orderfacList.get(i);
			Integer orderfacId = cotOrderFac.getId();

			CotOrderFac orderfac = (CotOrderFac) this.getCotBaseDao().getById(
					CotOrderFac.class, orderfacId);
			if (orderfac.getOrderStatus() == 2) {
				orderNos += orderfac.getOrderNo() + "、";
			}
		}

		if (orderNos.length() != 0) {
			return orderNos.substring(0, orderNos.length() - 1);
		} else {
			return null;
		}
	}

	// 更新生产合同审核状态
	public int saveOrderStatus(Integer id, Long status) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		CotOrderFac orderFac = this.getOrderFacById(id);
		List<CotOrderFac> list = new ArrayList<CotOrderFac>();
		orderFac.setOrderStatus(status);

		// 如果状态为通过或不通过时,保存审核人姓名
		// if (orderFac.getOrderStatus() == 1 || orderFac.getOrderStatus() == 2)
		// {
		// orderFac.setCheckPerson(cotEmps.getEmpsName());
		// }

		list.add(orderFac);

		this.getCotBaseDao().updateRecords(list);
		return orderFac.getId();
	}

	// 查询采购单其他费用的总金额
	public Double findTotalOtherFee(Integer orderId) {
		String hql = " from CotFinaceOther obj where obj.source='orderfac' and obj.fkId="
				+ orderId;
		List<?> list = this.getCotBaseDao().find(hql);
		Iterator<?> it = list.iterator();
		Double total = 0.0;
		while (it.hasNext()) {
			CotFinaceOther other = (CotFinaceOther) it.next();
			if (other != null) {
				if (other.getFlag().equals("A")) {
					total += other.getAmount();
				} else {
					total -= other.getAmount();
				}
			}
		}
		return total;
	}

	// 修改其他费用状态 0：未生成 1: 已生成
	public void modifyFinOtherStatus(Integer fkId, String finaceName,
			String flag) {
		String hql = " from CotFinaceOther obj where obj.source='orderfac' and obj.fkId="
				+ fkId + " and obj.finaceName='" + finaceName + "'";
		List<CotFinaceOther> res = new ArrayList<CotFinaceOther>();
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() == 1) {
			CotFinaceOther other = (CotFinaceOther) list.get(0);
			if ("add".equals(flag)) {
				other.setStatus(1);
			} else {
				other.setStatus(0);
			}

			res.add(other);
			this.getCotBaseDao().updateRecords(res);
		}
	}

	// 判断应付帐款是否有冲帐明细
	public String checkIsHaveDetail(Integer[] ids) {
		String finaceNames = new String();
		for (int i = 0; i < ids.length; i++) {
			CotFinaceAccountdeal deal = this.getGivenDealById(ids[i]);

			List<?> list = this.getCotBaseDao().find(
					" from CotFinacegivenDetail obj where obj.dealId="
							+ deal.getId());
			if (list.size() > 0) {
				finaceNames += deal.getFinaceName() + "、";
			}
		}
		if (finaceNames.length() != 0) {
			return finaceNames.substring(0, finaceNames.length() - 1);
		} else {
			return null;
		}
	}

	// 根据id判断该生产合同是否已存在应付帐款
	public Integer getDealNumById(Integer orderfacid) {
		String hql = " from CotFinaceAccountdeal obj where 1=1 and obj.source='orderfac' and obj.fkId="
				+ orderfacid;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() != 0) {
			return list.size();
		} else {
			return -1;
		}
	}

	// 判断生产合同是否有应付帐款记录
	public String checkIsHaveDeal(List<CotOrderFac> orderfacList) {
		String orderNos = new String();
		for (int i = 0; i < orderfacList.size(); i++) {

			CotOrderFac orderFac = this.getOrderFacById((orderfacList.get(i))
					.getId());

			List<?> list = this.getCotBaseDao().find(
					" from CotFinaceAccountdeal obj where obj.source='orderfac' and obj.fkId="
							+ orderFac.getId());
			if (list.size() > 0) {
				orderNos += orderFac.getOrderNo() + "、";
			}
		}
		if (orderNos.length() != 0) {
			return orderNos.substring(0, orderNos.length() - 1);
		} else {
			return null;
		}
	}

	// 将采购合同id及单号存入订单明细中
	public void saveIdAndNoToOrderDetail(Integer orderfacId) {

		CotOrderFac orderFac = this.getOrderFacById(orderfacId);
		List<CotOrderDetail> records = new ArrayList<CotOrderDetail>();

		String hql = " from CotOrderFacdetail obj where obj.orderId="
				+ orderfacId;

		List<?> list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotOrderFacdetail orderFacdetail = (CotOrderFacdetail) list.get(i);

			CotOrderDetail orderDetail = (CotOrderDetail) this.getCotBaseDao()
					.getById(CotOrderDetail.class,
							orderFacdetail.getOrderDetailId());

			orderDetail.setOrderFacnoid(orderFac.getId());
			orderDetail.setOrderFacno(orderFac.getOrderNo());

			records.add(orderDetail);
		}
		this.getCotBaseDao().updateRecords(records);
	}

	// 判断应付帐款是否有流转记录
	public String checkIsHaveTrans(Integer[] ids) {
		String finaceNames = new String();
		for (int i = 0; i < ids.length; i++) {
			CotFinaceAccountdeal deal = this.getGivenDealById(ids[i]);

			List<?> list = this.getCotBaseDao().find(
					" from CotFinaceOther obj where obj.source='orderfacDeal' and obj.outFlag="
							+ deal.getId());
			if (list.size() > 0) {
				finaceNames += deal.getFinaceName() + "、";
			}
		}
		if (finaceNames.length() != 0) {
			return finaceNames.substring(0, finaceNames.length() - 1);
		} else {
			return null;
		}
	}

	// 判断其他费用是否已导入订单
	public String checkIsInputOrder(Integer[] ids) {
		String finaceNames = new String();
		for (int i = 0; i < ids.length; i++) {

			List<?> list = this.getCotBaseDao().find(
					" from CotFinaceOther obj where obj.source='orderfac' and obj.id="
							+ ids[i]);
			if (list.size() == 1) {
				CotFinaceOther other = (CotFinaceOther) list.get(0);
				finaceNames += other.getFinaceName() + "、";
			}
		}
		if (finaceNames.length() != 0) {
			return finaceNames.substring(0, finaceNames.length() - 1);
		} else {
			return null;
		}
	}

	// 根据id判断该生产合同的其他费用是否导入订单
	public Integer getOtherNumById(Integer orderfacid) {
		String hql = " from CotFinaceOther obj where obj.source='orderfac' and obj.fkId="
				+ orderfacid + " and obj.isImport =1";
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() != 0) {
			return list.size();
		} else {
			return -1;
		}
	}

	// 根据id判断该生产合同的其他费用是否导入出货
	public Integer getOtherOutNumById(Integer orderfacid) {
		String hqlOther = "select b.fkId from CotFinaceOther a,CotFinaceOther b where a.outFlag=b.id and a.source='FacOther' and b.fkId="
				+ orderfacid;
		List<?> list = this.getCotBaseDao().find(hqlOther);
		if (list.size() != 0) {
			return list.size();
		} else {
			return -1;
		}
	}

	// 判断生产合同的其他费用是否导入订单
	public String checkIsOtherToOrder(List<CotOrderFac> orderfacList) {
		String orderNos = new String();
		for (int i = 0; i < orderfacList.size(); i++) {

			CotOrderFac orderFac = this.getOrderFacById((orderfacList.get(i))
					.getId());

			List<?> list = this.getCotBaseDao().find(
					" from CotFinaceOther obj where  obj.source='orderfac' and obj.fkId="
							+ orderFac.getId() + " and obj.isImport =1");
			if (list.size() > 0) {
				orderNos += orderFac.getOrderNo() + "、";
			}
		}
		if (orderNos.length() != 0) {
			return orderNos.substring(0, orderNos.length() - 1);
		} else {
			return null;
		}
	}

	// 判断生产合同的其他费用是否导入出货
	public String checkIsOtherToOrderOut(List<CotOrderFac> orderfacList) {
		String orderNos = new String();
		for (int i = 0; i < orderfacList.size(); i++) {

			CotOrderFac orderFac = this.getOrderFacById((orderfacList.get(i))
					.getId());
			String hqlOther = "select b.fkId from CotFinaceOther a,CotFinaceOther b where a.outFlag=b.id and a.source='FacOther' and b.fkId="
					+ orderFac.getId();
			List<?> list = this.getCotBaseDao().find(hqlOther);
			if (list.size() > 0) {
				orderNos += orderFac.getOrderNo() + "、";
			}
		}
		if (orderNos.length() != 0) {
			return orderNos.substring(0, orderNos.length() - 1);
		} else {
			return null;
		}
	}

	// 获取订单分解后的采购单id
	public List<Integer> getOrderFacId(HashMap<Integer, Integer> map) {
		List<Integer> ids = new ArrayList<Integer>();
		for (Iterator<Integer> itor = map.keySet().iterator(); itor.hasNext();) {
			Integer factoryId = (Integer) itor.next();
			Integer orderFacId = (Integer) map.get(factoryId);
			if (!orderFacId.equals("") && orderFacId != null) {
				ids.add(orderFacId);
			}
		}
		return ids;
	}

	// 根据订单明细id获取采购单id
	public String getOrderFacIdsByDetailId(Integer detailId) {
		String hql = " from CotOrderFacdetail obj where obj.orderDetailId = "
				+ detailId;
		List<?> list = this.getCotBaseDao().find(hql);
		String facIds = "";
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CotOrderFacdetail facDetail = (CotOrderFacdetail) list.get(i);
				facIds += facDetail.getOrderId() + ",";
			}
		}
		return facIds;
	}

	// 根据订单id获取采购单ids
	public String getOrderFacIds(Integer orderId) {
		String orderfacIds = "";

		String hql = " from CotOrderDetail obj where obj.orderId = " + orderId;
		List<?> list = this.getCotBaseDao().find(hql);

		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) list.get(i);

			String orderfacId = this.getOrderFacIdsByDetailId(detail.getId());

			orderfacIds += orderfacId;
		}
		return orderfacIds;
	}

	// 判断是否已存在该厂家的采购单
	public boolean checkIsFactory(Integer factoryId, Integer orderId) {
		String orderfacIdStr = this.getOrderFacIds(orderId);
		// 将数字字符串转为数字数组
		String[] idsStr = orderfacIdStr.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			String hql = " from CotOrderFac obj where obj.id ="
					+ Integer.parseInt(idsStr[i]);
			List<?> list = this.getCotBaseDao().find(hql);
			if (list.size() == 1) {
				CotOrderFac orderFac = (CotOrderFac) list.get(0);
				if (orderFac.getFactoryId().intValue() == factoryId) {
					return true;
				}
			}
		}
		return false;
	}

	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit() {
		List<CotEleCfg> list = new ArrayList<CotEleCfg>();
		String hql = " from CotEleCfg obj ";
		list = this.getCotBaseDao().find(hql);
		CotEleCfg cotEleCfg = (CotEleCfg) list.get(0);
		return cotEleCfg;
	}

	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId) {
		return (CotBoxPacking) this.getCotBaseDao().getById(
				CotBoxPacking.class, boxPackingId);
	}

	// 计算价格
	public String calPrice(CotOrderFacdetail elements, Integer boxPackingId) {
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
	public Float[] calPriceAll(CotOrderFacdetail elements) {
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
			String hql = "select obj.elePrice,obj.eleFittingPrice from CotOrderFacdetail obj where obj.id="
					+ elements.getId();
			List list = this.getCotBaseDao().find(hql);
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
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		String expessionFacIn = cotEleCfg.getExpessionFacIn();
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("elePrice", elePrice.toString());
		evaluator.putVariable("eleFittingPrice", eleFittingPrice.toString());
		evaluator.putVariable("packingPrice", res[4].toString());
		try {
			if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
				res[5] = 0f;
			} else {
				String result = evaluator.evaluate(expessionFacIn);
				res[5] = Float.parseFloat(dfTwo
						.format(Float.parseFloat(result)));
			}
		} catch (EvaluationException e) {
			e.printStackTrace();
			res[5] = 0f;
		}
		return res;
	}

	// 重新计算序列号
	public boolean updateSortNo(Integer id, Integer type, String field,
			String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<String, CotOrderFacdetail> givenMap = this.getMapByRdmAction(ctx
				.getSession());
		List list = new ArrayList();
		Iterator<?> it = givenMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			CotOrderFacdetail detail = givenMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotOrderFacdetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotOrderFacdetail detail = (CotOrderFacdetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getCotBaseDao().updateRecords(listNew);
		return true;
	}

	/***************************************************************************
	 * 修改于：2010.01.22 描述：用随机数作为key操作Map 添加人：chi_ch
	 */

	// Action储存(orderfacMap)
	@SuppressWarnings("unchecked")
	public void setMapByRdmAction(HttpSession session, String rdm,
			CotOrderFacdetail cotOrderFacdetail) {
		Object obj = SystemUtil.getObjBySession(session, "orderfac");
		if (obj == null) {
			HashMap<String, CotOrderFacdetail> orderfacMap = new HashMap<String, CotOrderFacdetail>();
			orderfacMap.put(rdm, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, orderfacMap, "orderfac");
		} else {
			HashMap<String, CotOrderFacdetail> addMap = (HashMap<String, CotOrderFacdetail>) obj;
			addMap.put(rdm, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, addMap, "orderfac");
		}
	}

	// Action获取(orderfacMap)
	@SuppressWarnings("unchecked")
	public HashMap<String, CotOrderFacdetail> getMapByRdmAction(
			HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "orderfac");
		HashMap<String, CotOrderFacdetail> orderfacMap = (HashMap<String, CotOrderFacdetail>) obj;
		return orderfacMap;
	}

	// 在Action中清除orderfacMap中rdm对应的映射
	public void delMapByRdmAction(String rdm, HttpSession session) {
		Map<String, CotOrderFacdetail> orderfacMap = this
				.getMapByRdmAction(session);
		if (orderfacMap != null) {
			if (orderfacMap.containsKey(rdm)) {
				orderfacMap.remove(rdm);
			}
		}
	}

	// 储存(orderfacMap)
	@SuppressWarnings("unchecked")
	public void setOrderFacMapValueByRdm(String rdm,
			CotOrderFacdetail cotOrderFacdetail) {
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		if (obj == null) {
			HashMap<String, CotOrderFacdetail> orderfacMap = new HashMap<String, CotOrderFacdetail>();
			orderfacMap.put(rdm, cotOrderFacdetail);
			SystemUtil.setObjBySession(null, orderfacMap, "orderfac");
		} else {
			HashMap<String, CotOrderFacdetail> orderfacMap = (HashMap<String, CotOrderFacdetail>) obj;
			orderfacMap.put(rdm, cotOrderFacdetail);
			SystemUtil.setObjBySession(null, orderfacMap, "orderfac");
		}
	}

	// 通过key获取(orderfacMap)的value
	@SuppressWarnings("unchecked")
	public CotOrderFacdetail getOrderFacMapValueByRdm(String rdm) {
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		HashMap<String, CotOrderFacdetail> orderfacMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (orderfacMap != null) {
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) orderfacMap
					.get(rdm);
			return cotOrderFacdetail;
		}
		return null;
	}

	// 通过rdm修改orderfacMap中对应的采购明细
	public boolean updateOrderFacMapValueByRdm(String rdm, String property,
			String value) {
		CotOrderFacdetail cotOrderFacdetail = getOrderFacMapValueByRdm(rdm);
		if (cotOrderFacdetail == null)
			return false;
		try {
			BeanUtils.setProperty(cotOrderFacdetail, property, value);
			this.setOrderFacMapValueByRdm(rdm, cotOrderFacdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 清除(orderfacMap)中rdm对应的映射
	@SuppressWarnings("unchecked")
	public void delOrderFacMapByRdm(String rdm) {
		Object obj = SystemUtil.getObjBySession(null, "orderfac");
		HashMap<String, CotOrderFacdetail> orderfacMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (orderfacMap != null) {
			if (orderfacMap.containsKey(rdm)) {
				orderfacMap.remove(rdm);
			}
		}
	}

	// 清空明细(orderfacMap)
	public void clearOrderFacMap() {
		SystemUtil.clearSessionByType(null, "orderfac");
	}

	/**
	 * 描述：通过addBoxCount修改orderfacMap中相应的值
	 */
	public boolean updateOrderFacMapByAddBoxCount(String rdm,
			Integer addBoxCount) {
		CotOrderFacdetail cotOrderFacdetail = getOrderFacMapValueByRdm(rdm);
		if (cotOrderFacdetail == null)
			return false;
		try {
			Long oldBoxCount = cotOrderFacdetail.getBoxCount(); // 采购数量
			Long boxObCount = cotOrderFacdetail.getBoxObCount();// 外装数
			Float priceFac = cotOrderFacdetail.getPriceFac(); // 厂价单价
			Float orderPrice = cotOrderFacdetail.getOrderPrice(); // 单价

			Long boxCount = oldBoxCount + addBoxCount;
			Float totalFac = boxCount * priceFac;
			Float totalMoney = boxCount * orderPrice;
			Long containerCount;
			if (boxCount % boxObCount != 0) {
				containerCount = (boxCount / boxObCount) + 1;
			} else {
				containerCount = boxCount / boxObCount;
			}
			cotOrderFacdetail.setBoxCount(boxCount);
			cotOrderFacdetail.setTotalFac(totalFac);
			cotOrderFacdetail.setTotalMoney(totalMoney);
			cotOrderFacdetail.setContainerCount(containerCount);
			this.setOrderFacMapValueByRdm(rdm, cotOrderFacdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/** ***************************************************************************** */

	/**
	 * 描述：根据包材价格调整生产价 作者：chi_ch 日期：2010-01-26
	 */
	public Float calPriceFacByPackPrice(String rdm, String packingPrice) {
		CotOrderFacdetail detail = this.getOrderFacMapValueByRdm(rdm);
		if (detail == null) {
			return null;
		}
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
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
		evaluator.putVariable("eleFittingPrice", eleFittingPrice.toString());
		evaluator.putVariable("packingPrice", packingPrice);
		Float res = null;
		try {
			if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
				res = detail.getPriceFac();
			} else {
				String result = evaluator.evaluate(expessionFacIn);
				res = Float.parseFloat(result);
			}
			detail.setPriceFac(res);
			detail.setPackingPrice(Float.parseFloat(packingPrice));
			this.setOrderFacMapValueByRdm(rdm, detail);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		return res;
	}

	// 通过订单明细获取主订单id
	public Integer getOrderIdByDetailId(Integer detailId) {
		Integer orderId = null;
		String hql = " from CotOrderDetail obj where obj.id=" + detailId;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() == 1) {
			CotOrderDetail detail = (CotOrderDetail) list.get(0);
			orderId = detail.getOrderId();
		}
		return orderId;
	}

	// 获取定单ids
	public List<Integer> getCotOrderIds(Integer orderfacId) {
		List<Integer> orderIds = new ArrayList<Integer>();
		String hql = " from CotOrderFacdetail obj where obj.orderId="
				+ orderfacId;
		List<?> list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotOrderFacdetail detail = (CotOrderFacdetail) list.get(i);
			if (detail.getOrderDetailId() != null
					&& !"".equals(detail.getOrderDetailId())) {

				Integer orderId = this.getOrderIdByDetailId(detail
						.getOrderDetailId());
				if (orderId != null) {
					orderIds.add(orderId);
				}
			}
		}
		return orderIds;
	}

	// 从订单中导入唛头信息
	public boolean updateCotOrderFacMb(Integer orderfacId, Integer orderId) {
		boolean flag = false;
		// byte[] zwtpByte = this.getZwtpPic();
		CotOrder order = (CotOrder) this.getCotBaseDao().getById(
				CotOrder.class, orderId);

		List<CotOrderFac> listRes = new ArrayList<CotOrderFac>();

		List<CotOrderfacMb> imgRes = new ArrayList<CotOrderfacMb>();
		List<CotProductFacMb> imgFacRes = new ArrayList<CotProductFacMb>();

		// 获取订单mb图片
		List<?> listMB = new ArrayList<Integer>();
		if (orderId != null) {
			String hql = " from CotOrderMb obj where obj.fkId=" + orderId;
			listMB = this.getCotBaseDao().find(hql);
		}

		// 获取采购单mb图片
		List<?> facMB = new ArrayList<Integer>();
		if (orderfacId != null) {
			String hql = " from CotOrderfacMb obj where obj.fkId=" + orderfacId;
			facMB = this.getCotBaseDao().find(hql);
		}

		// 获取订单产品标图片
		List<?> productMB = new ArrayList<Integer>();
		if (orderId != null) {
			String hql = " from CotProductMb obj where obj.fkId=" + orderId;
			productMB = this.getCotBaseDao().find(hql);
		}

		// 获取采购单产品标图片
		List<?> productFacMB = new ArrayList<Integer>();
		if (orderfacId != null) {
			String hql = " from CotProductFacMb obj where obj.fkId="
					+ orderfacId;
			productFacMB = this.getCotBaseDao().find(hql);
		}

		CotOrderFac orderFac = (CotOrderFac) this.getOrderFacById(orderfacId);

		CotOrderfacMb cotOrderfacMb = new CotOrderfacMb();
		CotProductFacMb cotProductFacMb = new CotProductFacMb();
		if (orderFac != null) {
			// 唛信息同步订单
			if (listMB.size() == 1) {
				CotOrderMb mb = (CotOrderMb) listMB.get(0);

				if (facMB.size() == 1) {
					cotOrderfacMb = (CotOrderfacMb) facMB.get(0);
					cotOrderfacMb.setId(cotOrderfacMb.getId());
				}
				cotOrderfacMb.setFkId(orderfacId);
				cotOrderfacMb.setPicImg(mb.getPicImg());
				cotOrderfacMb.setPicSize(mb.getPicSize());
			}
			// 产品标同步订单
			if (productMB.size() == 1) {
				CotProductMb mb = (CotProductMb) productMB.get(0);

				if (productFacMB.size() == 1) {
					cotProductFacMb = (CotProductFacMb) productFacMB.get(0);
					cotProductFacMb.setId(cotProductFacMb.getId());
				}
				cotProductFacMb.setFkId(orderfacId);
				cotProductFacMb.setPicImg(mb.getPicImg());
				cotProductFacMb.setPicSize(mb.getPicSize());
			}
			orderFac.setOrderZm(order.getOrderZM());
			orderFac.setOrderZhm(order.getOrderZHM());
			orderFac.setOrderCm(order.getOrderCM());
			orderFac.setOrderNm(order.getOrderNM());
			orderFac.setOrderMb(order.getOrderMB());
			orderFac.setProductM(order.getProductM());
			orderFac.setCotOrderFacdetails(null);
		}
		imgRes.add(cotOrderfacMb);
		imgFacRes.add(cotProductFacMb);

		listRes.add(orderFac);
		try {
			this.getCotBaseDao().updateRecords(listRes);
			this.getCotBaseDao().updateRecords(imgRes);
			this.getCotBaseDao().updateRecords(imgFacRes);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	// 更新中文品名
	public boolean updateOrderFacDetail(Integer orderfacDetailId, String eleName) {
		List<CotOrderFacdetail> records = new ArrayList<CotOrderFacdetail>();
		CotOrderFacdetail detail = (CotOrderFacdetail) this.getCotBaseDao()
				.getById(CotOrderFacdetail.class, orderfacDetailId);
		if (detail != null) {
			detail.setEleName(eleName);
		}
		records.add(detail);
		try {
			this.getCotBaseDao().updateRecords(records);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.orderfac.CotOrderFacService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

	// 查找该产品是否有订单
	public Object findIsExistDetail(String id) {

		Object rtn = new Object();
		String hql = "";

		CotOrderFacdetail facdetail = new CotOrderFacdetail();
		// 订单
		hql = "select obj from CotOrderDetail obj where obj.id =" + id;

		List<?> list = this.getCotBaseDao().find(hql);

		Object objTemp = (Object) list.get(0);
		CotOrderDetail detail = (CotOrderDetail) objTemp;

		// 使用反射获取订单明细对象的所有属性名称
		String[] propEle = ReflectionUtils
				.getDeclaredFields(CotOrderDetail.class);

		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter, Date.class);
		// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
				new PropertyUtilsBean());
		for (int i = 0; i < propEle.length; i++) {
			try {
				String value = beanUtils.getProperty(detail, propEle[i]);
				if ("op".equals(propEle[i]) || "unBoxCount".equals(propEle[i])
						|| "srcId".equals(propEle[i])
						|| "unBoxSend".equals(propEle[i])
						|| "liRun".equals(propEle[i])
						|| "orderFacno".equals(propEle[i])
						|| "checkFlag".equals(propEle[i])
						|| "boxHandleH".equals(propEle[i])
						|| "unBoxCount4OrderFac".equals(propEle[i])
						|| "boxBDesc".equals(propEle[i])
						|| "eleComposeType".equals(propEle[i])
						|| "orderFacnoid".equals(propEle[i])
						|| "tuiLv".equals(propEle[i])
						|| "eleUnitNum".equals(propEle[i])
						|| "addPerson".equals(propEle[i])
						|| "unBoxSend4OrderFac".equals(propEle[i])
						|| "outHasBoxCount4OrderFac".equals(propEle[i])
						|| "boxTDesc".equals(propEle[i])
						|| "eleHsid".equals(propEle[i])) {
					continue;
				}
				if (value != null) {
					beanUtils.setProperty(facdetail, propEle[i], value);
				}
			} catch (Exception e) {
				logger.error(propEle[i] + ":转换错误!");
			}
		}

		facdetail.setBoxCount(detail.getUnBoxCount4OrderFac());// 未采购数量
		facdetail.setOutRemain(detail.getBoxCount());// 剩余出货数量
		if (detail.getContainerCount() != 0
				&& detail.getContainerCount() != null) {
			if (detail.getUnBoxCount4OrderFac()
					% (detail.getBoxCount() / detail.getContainerCount()) != 0) {
				facdetail.setUnContainerCount(detail.getUnBoxCount4OrderFac()
						/ (detail.getBoxCount() / detail.getContainerCount())
						+ 1);
			} else {
				facdetail.setUnContainerCount(detail.getUnBoxCount4OrderFac()
						/ (detail.getBoxCount() / detail.getContainerCount()));
			}
		} else {
			facdetail.setUnContainerCount(0L);
		}

		facdetail.setOrderDetailId(detail.getId());
		facdetail.setBoxTdesc(detail.getBoxTDesc());
		facdetail.setEleUnitnum(detail.getEleUnitNum());
		facdetail.setEleComposetype(detail.getEleComposeType());
		facdetail.setBoxBdesc(detail.getBoxBDesc());
		facdetail.setBoxHandleh(detail.getBoxHandleH());
		facdetail.setPicImg(null);
		// 填充厂家简称
		// List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
		// for (int i = 0; i < facList.size(); i++) {
		// CotFactory cotFactory = (CotFactory) facList.get(i);
		// if (detail.getFactoryId() != null
		// && cotFactory.getId().intValue() == detail
		// .getFactoryId().intValue()) {
		// facdetail.setFactoryShortName(cotFactory.getShortName());
		// }
		// }
		rtn = facdetail;

		return rtn;

	}

	// 判断要更新到订单的明细货号哪些重复
	public Map<String, List<String>> findIsExistInOrder(String[] key) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String eles = "";
		List<String> allEleList = new ArrayList<String>();
		List<String> sameList = new ArrayList<String>();
		List<String> disList = new ArrayList<String>();

		for (int i = 0; i < key.length; i++) {
			eles += "'" + key[i] + "',";
			allEleList.add(key[i]);
		}

		String hql = "select obj.eleId from CotOrderDetail obj where obj.eleId in ("
				+ eles.substring(0, eles.length() - 1) + ")";
		List<?> list = this.getCotBaseDao().find(hql);
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

	// 同步相同的订单的图片
	public Object[] getOrderByEle(String rdm, CotOrderPic cotOrderPic) {
		Object[] obj = new Object[2];
		CotOrderFacdetail orderfacDetail = (CotOrderFacdetail) this
				.getOrderFacMapValueByRdm(rdm);
		obj[0] = orderfacDetail;
		CotOrderPic pic = null;
		if (orderfacDetail.getType() == null) {
			String hql = "from CotOrderfacPic obj where obj.fkId="
					+ orderfacDetail.getId();
			List list = this.getCotBaseDao().find(hql);
			CotOrderfacPic cotOrderfacPic = (CotOrderfacPic) list.get(0);
			pic = cotOrderPic;
			pic.setEleId(cotOrderfacPic.getEleId());
			pic.setPicImg(cotOrderfacPic.getPicImg());
			pic.setPicName(cotOrderfacPic.getPicName());
			pic.setPicSize(cotOrderfacPic.getPicImg().length);
		} else {
			Integer id = orderfacDetail.getId();
			pic = changeOrderPic(id, cotOrderPic);
		}
		obj[1] = pic;
		return obj;
	}

	// 根据采购的图片转换成订单图片
	public CotOrderPic changeOrderPic(Integer id, CotOrderPic pic) {
		CotOrderPic cotOrderPic = null;
		if (pic != null) {
			cotOrderPic = pic;
		} else {
			cotOrderPic = new CotOrderPic();
		}

		CotOrderfacPic p = (CotOrderfacPic) this.getCotBaseDao().getById(
				CotOrderfacPic.class, id);
		cotOrderPic.setEleId(p.getEleId());
		cotOrderPic.setPicImg(p.getPicImg());
		cotOrderPic.setPicName(p.getPicName());
		cotOrderPic.setPicSize(p.getPicImg().length);

		return cotOrderPic;
	}

	// 根据同步选择项同步更新订单
	public CotOrderDetail setOrderByTong(CotOrderDetail old,
			CotOrderFacdetail newEle, String eleStr, String boxStr,
			String otherStr) {
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
					old.setEleUnitNum(newEle.getEleUnitnum());
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
					old.setEleHsid(null);
				} else if (eleAry[i].equals("boxWeigth")) {
					old.setBoxWeigth(newEle.getBoxWeigth());
				} else if (eleAry[i].equals("cube")) {
					old.setCube(newEle.getCube());
				} else if (eleAry[i].equals("priceFac")) {
					old.setPriceFac(newEle.getPriceFac());
					old.setPriceFacUint(newEle.getPriceFacUint());
				} else if (eleAry[i].equals("priceOut")) {
					old.setPriceOut(newEle.getOrderPrice());
					old.setPriceOutUint(newEle.getCurrencyId());
				} else if (eleAry[i].equals("tuiLv")) {
					old.setTuiLv(null);
				} else if (eleAry[i].equals("liRun")) {
					old.setLiRun(null);
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

	// 同步不同的订单明细的图片
	public Map getOrderByDisEles(Map<String, String> disMap) {
		Map map = new HashMap();
		Iterator<?> it = disMap.keySet().iterator();
		while (it.hasNext()) {
			String eleId = (String) it.next();
			Object[] obj = new Object[2];
			CotOrderFacdetail orderfacDetail = (CotOrderFacdetail) this
					.getOrderFacMapValueByRdm(disMap.get(eleId));
			obj[0] = orderfacDetail;

			CotOrderPic cotOrderPic = null;
			if (orderfacDetail.getType() == null) {
				String hql = "from CotOrderFacPic obj where obj.fkId="
						+ orderfacDetail.getId();
				List list = this.getCotBaseDao().find(hql);
				CotOrderfacPic cotOrderFacPic = (CotOrderfacPic) list.get(0);
				cotOrderPic = new CotOrderPic();
				cotOrderPic.setEleId(cotOrderFacPic.getEleId());
				cotOrderPic.setPicImg(cotOrderFacPic.getPicImg());
				cotOrderPic.setPicName(cotOrderFacPic.getPicName());
				cotOrderPic.setPicSize(cotOrderFacPic.getPicImg().length);
			} else {
				// String type = orderfacDetail.getType();
				cotOrderPic = changeOrderPic(orderfacDetail.getId(), null);
			}
			obj[1] = cotOrderPic;
			map.put(eleId.toLowerCase(), obj);
		}
		return map;
	}

	// 根据订单明细货号字符串查询明细
	public void updateToOrderDetail(String[] same, String[] sameRdm,
			String[] dis, String[] disRdm, String eleStr, String boxStr,
			String otherStr, boolean isPic) {
		// 相同时处理
		if (same != null) {
			String t = "";
			// key为eleId,value为随机数
			Map<String, String> sameMap = new HashMap<String, String>();
			for (int i = 0; i < same.length; i++) {
				sameMap.put(same[i], sameRdm[i]);
				t += "'" + same[i] + "',";
			}
			// 获得订单明细和图片集合
			String elePichql = "select e,e.eleId,obj from CotOrderDetail e,CotOrderPic obj where obj.fkId=e.id and e.eleId in ("
					+ t.substring(0, t.length() - 1) + ")";
			List<?> elePicList = this.getCotBaseDao().find(elePichql);

			// 给订单明细设置id
			List<CotOrderDetail> listTemp = new ArrayList<CotOrderDetail>();
			// 修改订单明细图片表
			List<CotOrderPic> listPic = new ArrayList<CotOrderPic>();
			for (int i = 0; i < elePicList.size(); i++) {
				Object[] obj = (Object[]) elePicList.get(i);
				// 获得报价明细,样品图片
				Object[] temp = (Object[]) getOrderByEle(sameMap.get(obj[1]
						.toString()), (CotOrderPic) obj[2]);
				CotOrderDetail ele = setOrderByTong((CotOrderDetail) obj[0],
						(CotOrderFacdetail) temp[0], eleStr, boxStr, otherStr);
				listTemp.add(ele);
				listPic.add((CotOrderPic) temp[1]);
			}

			this.getCotBaseDao().updateRecords(listTemp);
			if (isPic == true) {
				for (int i = 0; i < listTemp.size(); i++) {
					CotOrderDetail orderDetail = (CotOrderDetail) listTemp
							.get(i);
					CotOrderPic cotOrderPic = (CotOrderPic) listPic.get(i);
					cotOrderPic.setFkId(orderDetail.getId());
					if (cotOrderPic.getEleId().equals("")) {
						cotOrderPic.setEleId(orderDetail.getEleId());
					}
					listPic.add(cotOrderPic);
				}
				this.getCotBaseDao().updateRecords(listPic);
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
			Map map = getOrderByDisEles(disMap);

			List<CotOrderDetail> listTemp = new ArrayList<CotOrderDetail>();
			for (int i = 0; i < dis.length; i++) {
				Object[] temp = (Object[]) map.get(dis[i].toLowerCase());
				CotOrderDetail newEle = new CotOrderDetail();
				CotOrderDetail ele = setOrderByTong(newEle,
						(CotOrderFacdetail) temp[0], eleStr, boxStr, otherStr);
				if (ele.getEleFlag() == null) {
					ele.setEleFlag(0l);
				}
				listTemp.add(ele);
			}

			try {
				this.getCotBaseDao().saveRecords(listTemp);
				// 新建订单明细图片表
				List<CotOrderPic> listPic = new ArrayList<CotOrderPic>();
				for (int i = 0; i < listTemp.size(); i++) {
					CotOrderDetail e = listTemp.get(i);
					Object[] temp = (Object[]) map.get(e.getEleId()
							.toLowerCase());
					CotOrderPic cotOrderPic = (CotOrderPic) temp[1];
					cotOrderPic.setFkId(e.getId());
					if (isPic != true) {
						byte[] zwtp = this.getZwtpPic();
						cotOrderPic.setEleId(e.getEleId());
						cotOrderPic.setPicImg(zwtp);
						cotOrderPic.setPicName(e.getEleId());
						cotOrderPic.setPicSize(zwtp.length);
					}
					listPic.add(cotOrderPic);
				}
				this.getCotBaseDao().saveRecords(listPic);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}

	// 根据货号分解订单，分3部完成
	public void savaOrderFacByEleId(Integer eleId, Integer orderId) {
		try {
			// 图片操作类
			CotOpImgService impOpService = (CotOpImgService) SystemUtil
					.getService("CotOpImgService");
			// 1、获取需要分解的订单明细
			CotPriceCfg cfg = (CotPriceCfg) (this.getCotBaseDao().getRecords(
					"CotPriceCfg").get(0));

			CotOrder cotOrder = (CotOrder) this.getCotBaseDao().getById(
					CotOrder.class, orderId);
			CotOrderDetail orderDetail = (CotOrderDetail) this.getCotBaseDao()
					.getById(CotOrderDetail.class, eleId);
			CotOrderFacdetail orderFacdetail = this
					.isExistOrderFacDetail(orderDetail);
			Calendar calendar = Calendar.getInstance();
			if (cotOrder.getGivenDate() != null)
				calendar.setTime(cotOrder.getGivenDate());
			String currDate = ContextUtil.getCurrentDate("yyyy-MM-dd");
			// 获得单号
			String orderFacNo = noService.getAutoOrderFacNo(cotOrder
					.getOrderNo(), orderDetail.getFactoryId(), currDate);
			// 如果没有业务配置没有添加提前天数,默认为10天
			if (cfg.getAppendDay() == null) {
				cfg.setAppendDay(10);
			}
			calendar.set(Calendar.DATE, (0 - cfg.getAppendDay()));
			List list = new ArrayList();
			ConvertUtilsBean convertUtils = new ConvertUtilsBean();
			SqlDateConverter dateConverter = new SqlDateConverter();
			convertUtils.register(dateConverter, Date.class);
			// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
			BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
					new PropertyUtilsBean());
			// 如果厂家为空，过滤
			if (orderDetail.getFactoryId() == null)
				return;
			CotOrderFac orderFac = null;
			CotOrderFacdetail facdetail = null;
			// 2、生成生产合同主单
			if (orderFacdetail == null) {
				orderFac = new CotOrderFac();
				orderFac.setId(null);
				orderFac.setEmpId(cotOrder.getEmpId());
				orderFac.setOrderMBImg(null);
				orderFac.setOrderNo(orderFacNo);
				orderFac.setFactoryId(orderDetail.getFactoryId());
				// orderFac.setAddTime(new Date(System.currentTimeMillis()));
				orderFac.setBusinessPerson(cotOrder.getBussinessPerson());
				orderFac.setCurrencyId(orderDetail.getCurrencyId());
				orderFac.setOrderTime(new Date(calendar.getTime().getTime()));
				orderFac.setGivenAddress(cfg.getGivenAddress());
				orderFac.setOrderAddress(cfg.getOrderAddress());
				orderFac.setCompanyId(cotOrder.getCompanyId());

				orderFac.setOrderZm(cotOrder.getOrderZM());
				orderFac.setOrderZhm(cotOrder.getOrderZHM());
				orderFac.setOrderCm(cotOrder.getOrderCM());
				orderFac.setOrderNm(cotOrder.getOrderNM());
				orderFac.setOrderMb(cotOrder.getOrderMB());
				orderFac.setProductM(cotOrder.getProductM());

				orderFac.setOrderIscheck(0L);
				orderFac.setOrderStatus(9L);// 不需审核
				if (cfg.getIsCheck() != null && cfg.getIsCheck() == 1) // 审核
					orderFac.setOrderIscheck(1L);
				orderFac.setTotalCbm(new Double(orderDetail.getTotalCbm()));
				orderFac.setTotalContainer(new Integer(orderDetail
						.getContainerCount().intValue()));
				orderFac.setTotalCount(new Integer(orderDetail.getBoxCount()
						.intValue()));
				orderFac.setTotalMoney(new Double(orderDetail.getBoxCount()
						* orderDetail.getOrderPrice()));

				// 其它信息
				orderFac.setQuality(cotOrder.getQuality());
				orderFac.setColours(cotOrder.getColours());
				orderFac.setSaleUnit(cotOrder.getSaleUnit());
				orderFac.setHandleUnit(cotOrder.getHandleUnit());
				orderFac.setAssortment(cotOrder.getAssortment());
				orderFac.setComments(cotOrder.getComments());
				orderFac.setShippingMark(cotOrder.getShippingMark());
				orderFac.setBuyer(cotOrder.getBuyer());
				orderFac.setSeller(cotOrder.getSeller());
				orderFac.setAgent(cotOrder.getAgent());
			} else {
				orderFac = (CotOrderFac) this.getCotBaseDao().getById(
						CotOrderFac.class, orderFacdetail.getOrderId());
			}
			list.add(orderFac);
			// 1、先保存主单,获得主单ID
			try {
				this.getCotBaseDao().saveOrUpdateRecords(list);
			} catch (Exception e) {
				// e.printStackTrace();
			}

			if (orderFacdetail == null) {
				// 2、生成生产合同明细
				String[] properties = ReflectionUtils
						.getDeclaredFields(CotOrderFacdetail.class);
				facdetail = new CotOrderFacdetail();
				for (String property : properties) {
					try {

						if ("op".equals(property) || "id".equals(property)
								|| "picImg".equals(property)
								|| "addTime".equals(property)
								|| "eleHsid".equals(property)
								|| "unBoxCount".equals(property)
								|| "unBoxSend".equals(property)
								|| "unBoxCount4OrderFac".equals(property)
								|| "unBoxSend4OrderFac".equals(property)
								|| "boxCount".equals(property)
								|| "liRun".equals(property)
								|| "tuiLv".equals(property)
								|| "orderFacno".equals(property)
								|| "orderFacnoid".equals(property)
								|| "outHasBoxCount4OrderFac".equals(property)) {
							continue;
						}
						String value = beanUtils.getProperty(orderDetail,
								property);
						// System.out.println("attribute:"+property+"
						// value:"+value);
						if (value != null)
							beanUtils.setProperty(facdetail, property, value);
					} catch (Exception e) {
					}
				}
				facdetail.setBoxCount(orderDetail.getUnBoxCount4OrderFac());// 未采购数量
				facdetail.setOutRemain(orderDetail.getUnBoxCount4OrderFac());// 剩余出货数量
				facdetail.setOrderId(orderFac.getId());
				facdetail.setOrderDetailId(orderDetail.getId());
			} else {
				facdetail = orderFacdetail;
				facdetail.setBoxCount(orderFacdetail.getBoxCount()
						+ orderDetail.getUnBoxCount4OrderFac());
				facdetail.setOutRemain(orderFacdetail.getBoxCount());

			}
			// 2、保存明细单，获得明细单Id
			list = new ArrayList();
			facdetail.setOrderId(orderFac.getId());
			facdetail.setAddTime(new Date(System.currentTimeMillis()));
			list.add(facdetail);
			try {
				this.getCotBaseDao().saveOrUpdateRecords(list);
			} catch (Exception e) {
				// e.printStackTrace();
			}

			// 3、生成生产合同图片信息，数据从订单图片中获取
			CotOrderfacPic facPic = null;
			// 获取订单图片信息
			CotOrderPic orderPic = this.getImgService().getOrderPic(
					orderDetail.getId());
			facPic = this.getOrderFacPicByDetailId(facdetail.getId());
			if (facPic == null) {
				facPic = new CotOrderfacPic();
				facPic.setPicImg(orderPic.getPicImg());
				facPic.setPicSize(orderPic.getPicSize());
				facPic.setEleId(orderPic.getEleId());
				facPic.setFkId(facdetail.getId());
			} else {
				facPic.setPicImg(orderPic.getPicImg());
				facPic.setPicSize(orderPic.getPicSize());
			}
			list = new ArrayList();
			list.add(facPic);
			try {
				impOpService.saveOrUpdateImg(list);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 4、生成麦标信息，数据从订单麦标图片获取，处理逻辑通获取图片信息
			CotOrderMb orderMb = (CotOrderMb) this.getMbInfo("CotOrderMb",
					cotOrder.getId());
			CotOrderfacMb facMb = null;
			facMb = (CotOrderfacMb) this.getMbInfo("CotOrderfacMb", orderFac
					.getId());
			if (facMb == null) {
				facMb = new CotOrderfacMb();
				facMb.setPicImg(orderMb.getPicImg());
				facMb.setPicSize(orderMb.getPicSize());
				facMb.setFkId(orderFac.getId());
			} else {
				facMb.setPicImg(orderMb.getPicImg());
				facMb.setPicSize(orderMb.getPicSize());
			}
			list = new ArrayList();
			list.add(facMb);
			try {
				impOpService.saveOrUpdateImg(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 4.5生成产品标，数据从订单产品标图片获取，处理逻辑通获取图片信息
			CotProductMb productMb = (CotProductMb) this.getMbInfo(
					"CotProductMb", cotOrder.getId());
			CotProductFacMb productFacMb = null;
			productFacMb = (CotProductFacMb) this.getMbInfo("CotProductFacMb",
					orderFac.getId());
			if (productFacMb == null) {
				productFacMb = new CotProductFacMb();
				productFacMb.setPicImg(productMb.getPicImg());
				productFacMb.setPicSize(productMb.getPicSize());
				productFacMb.setFkId(orderFac.getId());
			} else {
				productFacMb.setPicImg(productMb.getPicImg());
				productFacMb.setPicSize(productMb.getPicSize());
			}
			list = new ArrayList();
			list.add(productFacMb);
			try {
				impOpService.saveOrUpdateImg(list);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 5、修改订单未采购数量
			orderDetail.setUnBoxCount4OrderFac(new Long(0));
			orderDetail.setUnBoxSend4OrderFac(1);// 1:全部发货
			list = new ArrayList();
			list.add(orderDetail);
			try {
				this.getCotBaseDao().saveOrUpdateRecords(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 6、更新订单数量

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateOrderDeail(List orderdeList) {
		this.getCotBaseDao().saveOrUpdateRecords(orderdeList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.orderfac.CotOrderFacService#savaOrderFacByEleId(java.util.List)
	 */
	public Object getMbInfo(String from, Integer fkId) {
		// TODO Auto-generated method stub
		// 获取订单mb图片
		List listMB = new ArrayList();
		if (from != null) {
			String hql = " from " + from + " obj where obj.fkId=" + fkId;
			listMB = this.getCotBaseDao().find(hql);
			if (listMB != null && listMB.size() > 0)
				return listMB.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.orderfac.CotOrderFacService#savaOrderFacByEleId(java.util.List,
	 *      java.lang.Integer)
	 */
	public void savaOrderFacByEleIdList(List<String> eleIdList, Integer orderId) {
		// TODO Auto-generated method stub
		for (String id : eleIdList) {
			Integer eleId = new Integer(id);
			this.savaOrderFacByEleId(eleId, orderId);
		}
	}

	public boolean deleteMBPicImg(Integer orderfacId) {
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		String hql = "from CotOrderfacMb obj where obj.fkId=" + orderfacId;
		List list = this.getCotBaseDao().find(hql);
		CotOrderfacMb cotOrderfacMb = (CotOrderfacMb) list.get(0);

		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderfacMb.setPicImg(b);
			cotOrderfacMb.setPicSize(b.length);
			this.getCotBaseDao().update(cotOrderfacMb);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除唛标图片错误!");
			return false;
		}
	}

	// 判断其他费用是否存在
	public boolean findIsExistName(String name, Integer orderId, Integer recId) {
		String hql = "select obj.id from CotFinaceOther obj where obj.fkId=? and obj.source='orderfac' and obj.finaceName=?";
		Object[] obj = new Object[2];
		obj[0] = orderId;
		obj[1] = name;
		List list = this.getCotBaseDao().queryForLists(hql, obj);
		if (list != null && list.size() > 0) {
			Integer tId = (Integer) list.get(0);
			if (tId.intValue() != recId.intValue()) {
				return true;
			}
		}
		return false;
	}

	// 更新产品采购的实际金额
	public Double modifyRealMoney(Integer orderId) {
		CotOrderFac orderFac = this.getOrderFacById(orderId);
		WebContext ctx = WebContextFactory.get();
		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute("sysdic");
		List<?> list = (List<?>) dicMap.get("currency");

		// 查询该采购单的所有其他费用
		String hql = "from CotFinaceOther obj where obj.source='orderfac' and obj.fkId="
				+ orderId;
		List<?> details = this.getCotBaseDao().find(hql);
		float allMoney = 0f;
		for (int i = 0; i < details.size(); i++) {
			CotFinaceOther finaceOther = (CotFinaceOther) details.get(i);
			float cuRate = 0f;
			for (int j = 0; j < list.size(); j++) {
				CotCurrency currency = (CotCurrency) list.get(j);
				if (currency.getId().intValue() == finaceOther.getCurrencyId()
						.intValue()) {
					cuRate = currency.getCurRate();
					break;
				}
			}

			if (finaceOther.getFlag().equals("A")) {
				allMoney += finaceOther.getAmount() * cuRate;
			}
			if (finaceOther.getFlag().equals("M")) {
				allMoney -= finaceOther.getAmount() * cuRate;
			}
		}
		// 实际金额=货款金额-折扣+加减费用
		Double realMoney = orderFac.getTotalMoney() + allMoney;
		orderFac.setRealMoney(realMoney);
		List<CotOrderFac> listOrder = new ArrayList<CotOrderFac>();
		listOrder.add(orderFac);
		this.getCotBaseDao().updateRecords(listOrder);
		return realMoney;
	}

	// 查询所有币种
	public Map<Integer, CotCurrency> getCurrencyObjMap(
			HttpServletRequest request) {
		Map<Integer, CotCurrency> map = new HashMap<Integer, CotCurrency>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		if (dicMap == null) {
			return null;
		}
		List<?> list = (List<?>) dicMap.get("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId(), cotCurrency);
		}
		return map;
	}

	// 删除其他费用
	public Double deleteByIds(List<?> ids) {
		try {
			String idStr = "";
			for (int i = 0; i < ids.size(); i++) {
				idStr += ids.get(i) + ",";
			}
			// 获得币种
			WebContext ctx = WebContextFactory.get();
			Map<Integer, CotCurrency> map = getCurrencyObjMap(ctx
					.getHttpServletRequest());

			// 查询要删除的费用的总金额
			String hql = "from CotFinaceOther obj where obj.id in ("
					+ idStr.substring(0, idStr.length() - 1) + ")";
			List listTemp = this.getCotBaseDao().find(hql);
			Double allRmb = 0.0;
			Integer orderId = 0;
			for (int i = 0; i < listTemp.size(); i++) {
				CotFinaceOther finaceOther = (CotFinaceOther) listTemp.get(i);
				orderId = finaceOther.getFkId();
				CotCurrency old = map.get(finaceOther.getCurrencyId());
				if (finaceOther.getFlag().equals("A")) {
					allRmb += finaceOther.getAmount() * old.getCurRate();
				} else {
					allRmb -= finaceOther.getAmount() * old.getCurRate();
				}
			}
			// 更新主订单的实际金额
			if (allRmb != 0) {
				CotOrderFac order = this.getOrderFacById(orderId);
				if (order.getRealMoney() == null) {
					order.setRealMoney(0 - allRmb);
				} else {
					order.setRealMoney(order.getRealMoney() - allRmb);
				}
				List listOrder = new ArrayList();
				listOrder.add(order);
				this.getCotBaseDao().updateRecords(listOrder);
			}
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFinaceOther");
			return allRmb;
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 更新其他费用的导入标识为1,剩余金额为0
	public Boolean updateStatus(String ids) {
		try {
			String hql = "from CotFinaceOther obj where obj.id in ("
					+ ids.substring(0, ids.length() - 1) + ")";
			List<CotFinaceOther> list = this.getCotBaseDao().find(hql);
			List<CotFinaceOther> listNew = new ArrayList<CotFinaceOther>();
			for (int i = 0; i < list.size(); i++) {
				CotFinaceOther finaceOther = (CotFinaceOther) list.get(i);
				finaceOther.setRemainAmount(0.0);
				finaceOther.setStatus(1);
				listNew.add(finaceOther);
			}
			this.getCotBaseDao().updateRecords(listNew);
		} catch (Exception e) {
			logger.error("更新其他费用异常", e);
		}
		return true;
	}

	// 判断应付帐是否导到出货,是否有付款记录
	public List<Integer> checkIsImport(List<Integer> ids) {
		List<Integer> listNew = new ArrayList();
		for (int i = 0; i < ids.size(); i++) {
			Integer id = ids.get(i);
			String tempHql = "select obj.dealId from CotFinacegivenDetail obj where obj.dealId ="
					+ id;
			List listTemp = this.getCotBaseDao().find(tempHql);
			if (listTemp.size() == 0) {
				String hql = "select obj.id from CotFinaceOther obj where obj.source='FacDeal' and obj.outFlag="
						+ id;
				List list = this.getCotBaseDao().find(hql);
				if (list.size() == 0) {
					listNew.add(id);
				}
			}
		}

		return listNew;
	}

	// 删除应付帐,还原其他其他费用
	public Boolean deleteByAccount(List<?> ids) {
		try {
			List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
			for (int i = 0; i < ids.size(); i++) {
				CotFinaceAccountdeal recv = (CotFinaceAccountdeal) this
						.getCotBaseDao().getById(CotFinaceAccountdeal.class,
								(Integer) ids.get(i));
				if (recv.getFinaceOtherId() != null) {
					// 还原订单的其他费用的剩余金额
					CotFinaceOther cotFinaceOther = (CotFinaceOther) this
							.getCotBaseDao().getById(CotFinaceOther.class,
									recv.getFinaceOtherId());
					cotFinaceOther.setRemainAmount(cotFinaceOther
							.getRemainAmount()
							+ recv.getAmount());
					cotFinaceOther.setStatus(0);
					list.add(cotFinaceOther);
				}
			}
			this.getCotBaseDao().updateRecords(list);
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFinaceAccountdeal");
			return true;
		} catch (DAOException e) {
			return false;
		}
	}

	// 保存应付帐款
	public void saveAccountDeal(CotFinaceAccountdeal recvDetail,
			String amountDate, String priceScal, String prePrice) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (!"".equals(amountDate) && amountDate != null) {
				recvDetail.setAmountDate(new java.sql.Date(sdf
						.parse(amountDate).getTime()));
			}
			WebContext ctx = WebContextFactory.get();
			Integer empId = (Integer) ctx.getSession().getAttribute("empId");
			List<CotFinaceAccountdeal> records = new ArrayList<CotFinaceAccountdeal>();
			recvDetail.setAddDate(new Date(System.currentTimeMillis()));
			recvDetail.setRealAmount(0.0);
			recvDetail.setRemainAmount(recvDetail.getAmount());
			if (recvDetail.getFinaceName().equals("预付货款")) {
				recvDetail.setZhRemainAmount(0.0);
			} else {
				recvDetail.setZhRemainAmount(recvDetail.getAmount());
			}
			recvDetail.setStatus(0);
			recvDetail.setSource("orderfac");
			recvDetail.setEmpId(empId);
			records.add(recvDetail);
			this.getCotBaseDao().saveRecords(records);
			// 保存单号
			// Sequece sequece = new Sequece(false);
			// sequece.saveSeq("fincaeaccountrecvNo");
			// GenAllSeq seq = new GenAllSeq();
			// seq.saveSeq("fincaeaccountdeal");

			if (!"".equals(priceScal) && !"".equals(prePrice)) {
				// 将预收货款比例和金额保存到采购单
				CotOrderFac order = (CotOrderFac) this.getCotBaseDao().getById(
						CotOrderFac.class, recvDetail.getFkId());
				CotOrderFac obj = (CotOrderFac) SystemUtil.deepClone(order);
				obj.setPrePrice(Float.parseFloat(prePrice));
				obj.setPriceScal(Float.parseFloat(priceScal));
				List<CotOrderFac> list = new ArrayList<CotOrderFac>();
				list.add(obj);
				this.getCotBaseDao().updateRecords(list);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	// 更新产品标图片
	public void updateProMBImg(String filePath, Integer mainId) {
		String hql = "from CotProductFacMb obj where obj.fkId=" + mainId;
		List<?> list = this.getCotBaseDao().find(hql);
		CotProductFacMb cotProductMb = new CotProductFacMb();
		if (list.size() == 1) {
			cotProductMb = (CotProductFacMb) list.get(0);
		}
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotProductMb.setPicImg(b);
			cotProductMb.setFkId(mainId);
			cotProductMb.setPicSize(b.length);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			List<CotProductFacMb> rec = new ArrayList<CotProductFacMb>();
			rec.add(cotProductMb);
			this.getCotBaseDao().saveOrUpdateRecords(rec);
		} catch (Exception e) {
			logger.error("更新生产合同的产品标图片错误!");
		}
	}

	// 查找产品标PicImg
	public byte[] getProPicImgByOrderId(Integer custId) {

		List<?> list = this.getCotBaseDao().find(
				"from CotProductFacMb obj where obj.fkId=" + custId);
		CotProductFacMb cotProductMb = new CotProductFacMb();
		if (list.size() == 1) {
			cotProductMb = (CotProductFacMb) list.get(0);
		}
		if (cotProductMb != null) {
			return cotProductMb.getPicImg();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.orderfac.CotOrderFacService#getOrderStatusById(java.lang.Integer)
	 */
	public CotOrderStatus getOrderStatusById(Integer id) {
		CotOrderStatus orderStatus = (CotOrderStatus) this.getCotBaseDao()
				.getById(CotOrderStatus.class, id);
		return orderStatus;
	}

	// 删除已经存在的采购单明细
	public void deleteFacDetail(Integer orderId) {
		String sql = "select obj.id from CotOrderFac obj where obj.orderId="
				+ orderId;
		List listChk = this.getCotBaseDao().find(sql);
		List<Integer> ids = new ArrayList<Integer>();
		if (listChk != null && listChk.size() > 0) {
			Integer orderFacId = (Integer) listChk.get(0);
			String hql = " from CotOrderFacdetail obj where obj.orderId="
					+ orderFacId;
			List<CotOrderFacdetail> list = this.getCotBaseDao().find(hql);
			if (list.size() > 0) {
				for (CotOrderFacdetail detail : list) {
					ids.add(detail.getId());
				}
			}
		}
		if (ids != null) {
			if (ids.size() > 0) {
				try {
					this.getCotBaseDao().deleteRecordByIds(ids,
							"CotOrderFacdetail");
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.orderfac.CotOrderFacService#saveOrderFacByDecomposeOrder(java.lang.Integer)
	 */
	public Map saveOrderFacByDecomposeOrder(Integer orderId) throws Exception {
		// 保存OrderStatus表
		// List<CotOrderStatus> listOrderStatus =
		// this.getCotBaseDao().find("from CotOrderStatus obj where
		// obj.orderId="+orderId+" and orderType='ORDER'");
		// Map statusMap = new HashMap();
		// for(CotOrderStatus orderStatus : listOrderStatus){
		// statusMap.put(orderStatus.getFlagName(), orderStatus);
		// }

		// 先判断是否已经生产合同
		// String hql="select obj.id from CotOrderFac obj where
		// obj.orderId="+orderId;
		// List listChk=this.getCotBaseDao().find(hql);
		// if(listChk!=null && listChk.size()>0){
		// Integer orderFacId =(Integer) listChk.get(0);
		// List ids =this.deleteFacDetail(orderFacId);
		// if(ids !=null){
		// if(ids.size()>0){
		// this.getCotBaseDao().deleteRecordByIds(ids, "CotOrderFacdetail");
		// }
		// }
		// }
		Map facIdMap = new HashMap();
		// 先查找是否存在采购单
		List<CotOrderFac> orderFacList = this.getCotBaseDao().find(
				"select obj from CotOrderFac obj where obj.orderId=" + orderId);
		Integer facUnit=0;
		List artWorkList = new ArrayList();
		
		String pd="";
		if (orderFacList.size() > 0) {
			// 当采购单存在,只更新采购单明细
			List<CotOrderDetail> orderDetailList = this.getCotBaseDao().find(
					"select obj from CotOrderDetail obj where obj.orderId="
							+ orderId);
			CotOrderFac fac = orderFacList.get(0);
			Integer orderfacId = fac.getId();
			
			//查找客户的product in字段
			String hql="select obj.productIn from CotCustomer obj,CotOrder k,CotOrderFac f where f.orderId=k.id and k.custId=obj.id and f.id="+orderfacId;
			List listProduct = this.getCotBaseDao().find(hql);
			if(listProduct!=null && listProduct.size()>0){
				String tp=(String) listProduct.get(0);
				if(tp!=null){
					pd=tp;
				}
			}
			
			
			//获得订单明细的采购币种
			CotOrderDetail cotOrderDetail=orderDetailList.get(0);
			facUnit=cotOrderDetail.getPriceFacUint();
			fac.setCurrencyId(facUnit);
			List tp = new ArrayList();
			fac.setCotOrderFacdetails(null);
			tp.add(fac);
			this.getCotBaseDao().updateRecords(tp);

			List<CotOrderFacdetail> facdetaiList = this.getOrderDetailForFac(
					orderDetailList, orderfacId, fac.getOrderNo());
			this.getCotBaseDao().saveOrUpdateRecords(facdetaiList);
			// 处理图片信息
			List orderFacPics = new ArrayList();
			Map<Integer, CotOrderfacPic> facMap = this
					.getOrderFacImgByOrderId(facdetaiList);
			for (CotOrderFacdetail orderFacdetail : facdetaiList) {
				CotOrderfacPic facPic = facMap.get(orderFacdetail
						.getOrderDetailId());
				if (facPic == null)
					continue;
				facPic.setFkId(orderFacdetail.getId());
				orderFacPics.add(facPic);
				
				//12.5号更新===先判断pi明细id对应的art work记录是否存在,如果存在就更新ele_id为最新po的id
				String hqlArt="from CotArtWork obj where obj.piDetailId="+orderFacdetail.getOrderDetailId();
				List listArt=this.getCotBaseDao().find(hqlArt);
				if(listArt.size()>0){
					CotArtWork artWork =(CotArtWork) listArt.get(0);
					artWork.setEleId(orderFacdetail.getId());
					artWorkList.add(artWork);
				}else{
					CotArtWork artWork = new CotArtWork();
					artWork.setPiDetailId(orderFacdetail.getOrderDetailId());
					artWork.setEleId(orderFacdetail.getId());
					artWork.setOrderId(orderFacdetail.getOrderId());
					artWork.setSizeInch(orderFacdetail.getEleInchDesc());
					artWork.setRemark(pd);
					artWorkList.add(artWork);
				}
				
			}
			this.getCotBaseDao().saveOrUpdateRecords(orderFacPics);
			//为了重新计算总数量等
			facIdMap.put(orderfacId, orderfacId);
		} else {
			// 获取未分解的订单明细ID
			List<CotOrderDetail> orderDetailList = this.getCotBaseDao().find(
					"select obj from CotOrderDetail obj where obj.orderId="
							+ orderId);
			
			//获得订单明细的采购币种
			CotOrderDetail detail=orderDetailList.get(0);
			facUnit=detail.getPriceFacUint();
			
			// + " and obj.boxCount>0 and obj.unBoxCount4OrderFac > 0");
			// 过滤出厂家集合
			MultiMap multiMap_Composed = new MultiValueMap();// 已分解的生产合同
			MultiMap multiMap_UnComposed = new MultiValueMap();// 未分解的生产合同
			Map facOrderIdMap = new HashMap();// 厂家和生产合同主单ID对应关系，针对已分解的
			// Map facIdMap = new HashMap();
			
			//存储订单明细的生产币种
			Integer orId=null;
			for (CotOrderDetail cotOrderDetail : orderDetailList) {
				orId=cotOrderDetail.getPriceFacUint();
				// if(cotOrderDetail.getFactoryId() == null) continue;
				if (cotOrderDetail.getOrderFacnoid() != null) {
					multiMap_Composed.put(cotOrderDetail.getFactoryId(),
							cotOrderDetail);
					facOrderIdMap.put(cotOrderDetail.getFactoryId(),
							cotOrderDetail);
					facIdMap.put(cotOrderDetail.getOrderFacnoid(),
							cotOrderDetail.getOrderFacnoid());
				} else {
					multiMap_UnComposed.put(cotOrderDetail.getFactoryId(),
							cotOrderDetail);
				}
			}
			// 创建生产合同主单
			CotOrder cotOrder = (CotOrder) this.getCotBaseDao().getById(
					CotOrder.class, orderId);
			String orderNo = cotOrder.getOrderNo(); // 主订单编号
			Integer bussinessPerson = cotOrder.getBussinessPerson(); // 主订单业务员

			// 获取图片
			// List listMB = null;
			// List listProMB = null;
			// if (orderId != null) {
			// String hql = "from CotOrderMb obj where obj.fkId= " + orderId;
			// listMB = this.getCotBaseDao().find(hql);
			// String proHql = "from CotProductMb obj where obj.fkId= " +
			// orderId;
			// listProMB = this.getCotBaseDao().find(proHql);
			// }
			// 获取登陆员工
			WebContext ctx = WebContextFactory.get();
			CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			// 获取当前时间
			Date addTime = new Date(System.currentTimeMillis());
			// 获取默认公司ID
			Integer defaultCompanyId = getDefaultCompanyId();
			// 从数据字典中取生产合同合同条款
			String orderFacContract = this.getContract();
			// 获取默认配置对象
			List<?> res = this.getCotBaseDao().getRecords("CotPriceCfg");
			CotPriceCfg cotPriceCfg = new CotPriceCfg();
			if (res.size() > 0) {
				cotPriceCfg = (CotPriceCfg) res.get(0);
			}
			// 对未分解的厂家进行处理
			Iterator<Integer> iterator = multiMap_UnComposed.keySet()
					.iterator();
			// while(iterator.hasNext()){
			Integer facId = cotOrder.getFactoryId();
			CotFactory factory = (CotFactory) this.getCotBaseDao().getById(
					CotFactory.class, facId);
			// CotCustomer customer =
			// (CotCustomer)this.getCotBaseDao().getById(CotCustomer.class,
			// cotOrder.getCustId());
			// Cotco
			// 生成单号
			CotSeqService seq = new CotSeqServiceImpl();
			String orderFacNo = seq.getAutoOrderFacNo(facId, orderId);
			seq.saveSeq("autoOrderfacNo");// 直接更新序列号
			CotOrderFac cotOrderFac = new CotOrderFac();
			cotOrderFac.setThemes("No");
			cotOrderFac.setBusinessPerson(bussinessPerson); // 业务员
			cotOrderFac.setOrderClause(orderFacContract); // 合同条款
			// if (cotPriceCfg.getIsCheck() == null
			// || cotPriceCfg.getIsCheck() == 0) {
			// cotOrderFac.setOrderStatus(9L);// 审核状态 0:未审核 1:通过 2:不通过
			// // 3:请审核 9:不审核
			// } else {
			cotOrderFac.setOrderStatus(0L);
			// }

			// 设置生产合同交货时间
			Calendar cal = Calendar.getInstance();
			if (cotOrder.getSendTime() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				cal.setTime(cotOrder.getSendTime());
				// 如果没有默认配置.提前时间为20天
				// if (cotPriceCfg.getAppendDay() != null) {
				// cal.add(Calendar.DATE, -cotPriceCfg.getAppendDay());
				// } else {
				// cal.add(Calendar.DATE, -20);
				// }
				String dateString = sdf.format(cal.getTime());
				java.util.Date timeDate = sdf.parse(dateString);// util类型
				java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());// sql类型
				cotOrderFac.setSendTime(dateTime);
			}

			cotOrderFac.setOrderZm(cotOrder.getOrderZM());
			cotOrderFac.setOrderZhm(cotOrder.getOrderZHM());
			cotOrderFac.setOrderCm(cotOrder.getOrderCM());
			cotOrderFac.setOrderNm(cotOrder.getOrderNM());
			cotOrderFac.setOrderMb(cotOrder.getOrderMB());
			cotOrderFac.setNewRemark(cotOrder.getNewRemark());
			// 产品标
			cotOrderFac.setProductM(cotOrder.getProductM());
			// 设置订单ID
			cotOrderFac.setOrderId(orderId);
			cotOrderFac.setAllPinName(cotOrder.getAllPinName());
			cotOrderFac.setShipmentDate(cotOrder.getOrderLcDate());
			cotOrderFac.setShipportId(cotOrder.getShipportId());
			cotOrderFac.setTargetportId(cotOrder.getTargetportId());
			cotOrderFac.setPayTypeId(factory.getPayTypeId());
			cotOrderFac.setZheNum(cotOrder.getZheNum());
			cotOrderFac.setZheType(cotOrder.getZheType());
			cotOrderFac.setContainerTypeId(cotOrder.getContainerTypeId());
			cotOrderFac.setClauseTypeId(cotOrder.getClauseTypeId());
			cotOrderFac.setFactoryId(facId); // 采购厂家
			cotOrderFac.setPoNo(cotOrder.getPoNo());
			// cotOrderFac.setForwardingAgent(forwardingAgent)
			// cotOrderFac.setOrderRemark(cotOrder.getOrderRemark());

			if (cotOrder.getCompanyId() == null)
				cotOrderFac.setCompanyId(defaultCompanyId); // 出口商设为默认公司
			else {
				cotOrderFac.setCompanyId(cotOrder.getCompanyId());
			}
//			cotOrderFac.setCurrencyId(cotPriceCfg.getFacPriceUnit()); // 币种
			cotOrderFac.setCurrencyId(orId); // 取订单某一条明细的生产币种
			// cotOrderFac.setOrderAddress(cotPriceCfg.getOrderAddress());//
			// 签约地点
			// cotOrderFac.setGivenAddress(cotPriceCfg.getGivenAddress());//
			// 交货地点
			cotOrderFac.setOrderNo(orderFacNo); // 发票编号
			// cotOrderFac.setAddTime(addTime); // 添加时间
			cotOrderFac.setOrderTime(addTime);// 下单时间
			cotOrderFac.setEmpId(cotEmps.getId()); // 操作人编号
			cotOrderFac.setSampleOutStatus(0);// 出货样确认状态
			cotOrderFac.setSampleStatus(0);// 产前样确认状态
			cotOrderFac.setQcStatus(0);// QC样确认状态
			cotOrderFac.setPacketStatus(0);// 包装确认状态
			cotOrderFac.setOutStatus(0);// 出货确认状态
			// cotOrderFac.setOrderStatus(new Long(0)); // 审核状态
			// 0:(未审核),1(已审核)通过,2(审核不通过)
			cotOrderFac.setOrderIscheck(new Long(0)); // 是否需要审核 0:不需要审核
			// 获得佣金
			cotOrderFac.setCommisionScale(cotOrder.getCommisionScale());
			// 付款方式
			CotFactory fast = (CotFactory) this.getCotBaseDao().getObjectById(
					"CotFactory", cotOrder.getFactoryId());
			cotOrderFac.setPayTypeId(fast.getPayTypeId());
			// department
			cotOrderFac.setTypeLv1Id(cotOrder.getTypeLv1Id());

			// TOTALCBM
			cotOrderFac.setOrderEarnest(cotOrder.getOrderEarnest());
			// 其它信息
			cotOrderFac.setQuality(cotOrder.getQuality());
			cotOrderFac.setColours(cotOrder.getColours());
			cotOrderFac.setSaleUnit(cotOrder.getSaleUnit());
			cotOrderFac.setHandleUnit(cotOrder.getHandleUnit());
			cotOrderFac.setAssortment(cotOrder.getAssortment());
			cotOrderFac.setComments(cotOrder.getComments());
			cotOrderFac.setNationId(cotOrder.getNationId());
			cotOrderFac.setShippingMark(cotOrder.getShippingMark());
			CotCustomer cust = (CotCustomer) this.getCotBaseDao()
					.getObjectById("CotCustomer", cotOrder.getCustId());
			if (cust.getForwardingAgent() != null) {
				cotOrderFac.setForwardingAgent(cust.getForwardingAgent());
				CotConsignCompany cotConsignCompany = (CotConsignCompany) this
						.getCotBaseDao().getById(CotConsignCompany.class,
								cust.getForwardingAgent());
				cotOrderFac.setBooking(cotConsignCompany.getCompanyRemark());
			}
			// TODO:
			if (cotOrder.getIsCheckAgent() != null
					&& cotOrder.getIsCheckAgent() == 1) {
				cotOrderFac.setIsCheckAgent(cotOrder.getIsCheckAgent());
				cotOrderFac.setBuyer(cotOrder.getBuyer());
				cotOrderFac.setSeller(cotOrder.getSeller());
				System.out.println(cotOrder.getSeller());
				cotOrderFac.setAgent(cotOrder.getAgent());
			} else {
				// 不够选
				cotOrderFac.setIsCheckAgent(cotOrder.getIsCheckAgent());
				cotOrderFac.setBuyer(cotOrder.getSeller());
				// PO seller提取suplier
				int factoryId = cotOrder.getFactoryId();
				String str = "";
				CotFactory cotFactory = (CotFactory) this.getCotBaseDao()
						.getById(CotFactory.class, factoryId);
				if (cotFactory != null) {
					String city = "";
					String nation = "";
					String fullName = "";
					String adr = "";
					String post = "";
					String facName = cotFactory.getFactoryName();
					if (facName != null && !facName.equals("")) {
						fullName = facName + " \n";
					}
					String factoryAddr = cotFactory.getFactoryAddr();
					if (factoryAddr != null && !factoryAddr.equals("")) {
						adr = factoryAddr + " \n";
					}
					// 获得邮编
					String facPost = cotFactory.getPost();
					if (facPost != null && !facPost.equals("")) {
						post = facPost + ",";
					}
					if (cotFactory.getCityId() != null
							&& !cotFactory.getCityId().equals("")
							&& !cotFactory.getCityId().equals("null")) {
						System.out.println(cotFactory.getCityId());
						CotNationCity cotNationCity = (CotNationCity) this
								.getCotBaseDao().getById(CotNationCity.class,
										cotFactory.getCityId());
						String nationCity = cotNationCity.getCityName();
						if (!nationCity.equals("")) {
							city = nationCity + " \n";
						}else{
							post = facPost + " \n";
						}
					}
					// 获得国家名
					if (cotFactory.getNationId() != null
							&& !cotFactory.getNationId().equals("")
							&& !cotFactory.getNationId().equals("null")) {
						CotNation cotNation = (CotNation) this.getCotBaseDao()
								.getById(CotNation.class,
										cotFactory.getNationId());
						String country = cotNation.getNationName();
						if (!country.equals("")) {
							nation = country;
						}
					}
					str = fullName + adr + post + city + nation;
					System.out.println(str);
					int start = str.indexOf(" ");
					int end = str.lastIndexOf(" ");
					int strLength = str.length() - 2;
					int oneLength = str.length() - 1;
					String lastStr = str.substring(strLength, strLength + 1);
					String oneStr = str.substring(oneLength, oneLength + 1);
					if (start == end) {
						if (start == strLength) {
							str = str.substring(0, strLength);
						}
					} else {
						if (lastStr.equals(" ")) {
							str = str.substring(0, end);
						}
						if (oneStr == " ") {
							str = str.substring(0, end);
						}
					}
					// str=fullName+ adr+post+city+nation;
					// int start=str.indexOf(',');
					// int end=str.lastIndexOf(',');
					// if(start==end){
					//						
					// }else{
					// str =str.substring(0,end);
					// }
				}
				cotOrderFac.setSeller(str);
				cotOrderFac.setAgent(cotOrder.getAgent());
			}
			cotOrderFac.setConsignee(cotOrder.getBuyer());
			// 审核时间
			Calendar calChk = Calendar.getInstance();
			calChk.setTime(cotOrder.getModTime());
			// 审核后14天
			int d = calChk.get(Calendar.DATE);
			calChk.set(Calendar.DATE, d + 14);
			// 船期前30天
			Calendar calShip30 = Calendar.getInstance();
			calShip30.setTime(cotOrder.getOrderLcDate());
			int c = calShip30.get(Calendar.DATE);
			calShip30.set(Calendar.DATE, c - 30);

			// 船期前17天
			Calendar calShip17 = Calendar.getInstance();
			calShip17.setTime(cotOrder.getOrderLcDate());
			int e = calShip17.get(Calendar.DATE);
			calShip17.set(Calendar.DATE, e - 17);

			// 船期前7天
			Calendar calShip7 = Calendar.getInstance();
			calShip7.setTime(cotOrder.getOrderLcDate());
			int f = calShip7.get(Calendar.DATE);
			calShip7.set(Calendar.DATE, f - 7);

			// 船期后10天
			Calendar calShipH10 = Calendar.getInstance();
			calShipH10.setTime(cotOrder.getOrderLcDate());
			int h = calShipH10.get(Calendar.DATE);
			calShipH10.set(Calendar.DATE, h + 10);
			
			// Develiy date - 56
			Calendar cal56 = Calendar.getInstance();
			cal56.setTime(cotOrder.getOrderLcDate());
			int k = cal56.get(Calendar.DATE);
			cal56.set(Calendar.DATE, k -56 );

			// 设定计划日期
			cotOrderFac.setSimpleSampleDeadline(calChk.getTime());
			cotOrderFac.setCompleteSampleDeadline(null);
			cotOrderFac.setFacDeadline(calShip30.getTime());
			cotOrderFac.setPcaketDeadline(null);
			cotOrderFac.setSamplePicDeadline(null);
			cotOrderFac.setSampleOutDeadline(calShip17.getTime());
			cotOrderFac.setQcDeadline(calShip7.getTime());
			cotOrderFac.setShippingDeadline(calShip17.getTime());
			cotOrderFac.setLoadingDeadline(calShipH10.getTime());
			cotOrderFac.setSampleOutDeadline(cal56.getTime());

			// 设定生产合同的创建时间和创建人
//			cotOrderFac.setAddPerson(cotEmps.getId());
			//2012-1-4修改
//			cotOrderFac.setAddPerson(cotOrder.getCheckPerson());
			cotOrderFac.setAddPerson(cotOrder.getAddPerson());
			cotOrderFac.setAddTime(new Timestamp(System.currentTimeMillis()));

			// 1:需要审核 （默认0）
			List<CotOrderFac> list = new ArrayList<CotOrderFac>();
			
			cotOrderFac.setCurrencyId(facUnit);
			list.add(cotOrderFac);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			Integer orderfacId = cotOrderFac.getId();
			facIdMap.put(orderfacId, orderfacId);
			// 唛标信息
			CotOrderfacMb facMb = new CotOrderfacMb();
			// if (listMB != null && listMB.size() > 0) {
			// CotOrderMb orderMb = (CotOrderMb) listMB.get(0);
			// facMb.setFkId(orderfacId);
			// facMb.setPicImg(orderMb.getPicImg());
			// facMb.setPicSize(orderMb.getPicSize());
			//
			// } else {
			byte[] zwtpByte = this.getZwtpPic();
			facMb.setFkId(orderfacId);
			facMb.setPicImg(zwtpByte);
			facMb.setPicSize(zwtpByte.length);
			// }
			List listFacMB = new ArrayList();
			listFacMB.add(facMb);
			this.getCotBaseDao().saveOrUpdateRecords(listFacMB);
			// 保存产品标
			CotProductFacMb proMb = new CotProductFacMb();
			// if (listProMB != null && listProMB.size() > 0) {
			// CotProductMb orderMb = (CotProductMb) listProMB.get(0);
			// proMb.setFkId(orderfacId);
			// proMb.setPicImg(orderMb.getPicImg());
			// proMb.setPicSize(orderMb.getPicSize());
			//
			// } else {
			// byte[] zwtpByte = this.getZwtpPic();
			proMb.setFkId(orderfacId);
			proMb.setPicImg(zwtpByte);
			proMb.setPicSize(zwtpByte.length);
			// }
			List temp = new ArrayList();
			temp.add(proMb);
			this.getCotBaseDao().saveOrUpdateRecords(temp);

			// Iterator<String> iterator2 = statusMap.keySet().iterator();
			// while(iterator2.hasNext()){
			// String flag = iterator2.next();
			// CotOrderStatus orderStatus = (CotOrderStatus)statusMap.get(flag);
			// //CotOrderStatus orderStatus2 = new ;
			//				
			// orderStatus.setOrderFacId(orderfacId);
			// orderStatus.setOrderType("ORDERFAC");
			// statusList.add(orderStatus);
			// }
			// List<CotOrderFacdetail> facdetaiList =
			// this.getOrderDetailForFac((List<CotOrderDetail>)multiMap_UnComposed.get(facId),orderfacId,
			// cotOrderFac.getOrderNo());
			List<CotOrderFacdetail> facdetaiList = this.getOrderDetailForFac(
					orderDetailList, orderfacId, cotOrderFac.getOrderNo());
			this.getCotBaseDao().saveOrUpdateRecords(facdetaiList);
			// 处理图片信息
			List orderFacPics = new ArrayList();
			
			Map<Integer, CotOrderfacPic> facMap = this
					.getOrderFacImgByOrderId(facdetaiList);
			
			//查找客户的product in字段
			String hql="select obj.productIn from CotCustomer obj,CotOrder k,CotOrderFac f where f.orderId=k.id and k.custId=obj.id and f.id="+orderfacId;
			List listProduct = this.getCotBaseDao().find(hql);
			if(listProduct!=null && listProduct.size()>0){
				String tp=(String) listProduct.get(0);
				if(tp!=null){
					pd=tp;
				}
			}
			
			for (CotOrderFacdetail orderFacdetail : facdetaiList) {
				CotOrderfacPic facPic = facMap.get(orderFacdetail
						.getOrderDetailId());
				if (facPic == null)
					continue;
				facPic.setFkId(orderFacdetail.getId());
				orderFacPics.add(facPic);
				
				//12.5号更新===先判断pi明细id对应的art work记录是否存在,如果存在就更新ele_id为最新po的id
				String hqlArt="from CotArtWork obj where obj.piDetailId="+orderFacdetail.getOrderDetailId();
				List listArt=this.getCotBaseDao().find(hqlArt);
				if(listArt.size()>0){
					CotArtWork artWork =(CotArtWork) listArt.get(0);
					artWork.setEleId(orderFacdetail.getId());
					artWorkList.add(artWork);
				}else{
					CotArtWork artWork = new CotArtWork();
					artWork.setEleId(orderFacdetail.getId());
					artWork.setOrderId(orderFacdetail.getOrderId());
					artWork.setPiDetailId(orderFacdetail.getOrderDetailId());
					artWork.setSizeInch(orderFacdetail.getEleInchDesc());
					artWork.setRemark(pd);
					artWorkList.add(artWork);
				}
			}
			this.getCotBaseDao().saveOrUpdateRecords(orderFacPics);
		}
		this.getCotBaseDao().saveOrUpdateRecords(artWorkList);
		// }
		// 对已分解的厂家进行处理，只需处理明细部分
		// iterator = multiMap_Composed.keySet().iterator();
		// while(iterator.hasNext()){
		// Integer facId = iterator.next();
		// //facIdMap.put(facId, facId);
		// CotOrderDetail orderDetail =
		// (CotOrderDetail)facOrderIdMap.get(facId);
		// List<CotOrderFacdetail> facdetaiList =
		// this.getOrderDetailForFac((List<CotOrderDetail>)multiMap_Composed.get(facId),orderDetail.getOrderFacnoid(),
		// orderDetail.getOrderFacno());
		// this.getCotBaseDao().saveOrUpdateRecords(facdetaiList);
		// }

		return facIdMap;
	}

	// 对未分解生产合同的明细进行解析，从订单明细转为生产合同明细
	public List<CotOrderFacdetail> getOrderDetailForFac(
			List<CotOrderDetail> orderDetailList, Integer orderFacId,
			String orderFacno) {
		List<CotOrderFacdetail> records = new ArrayList<CotOrderFacdetail>();
		Map<Integer, CotOrderFacdetail> orderfacdetailsMap = this
				.getExistOrderDetails(orderFacId);
		Iterator<CotOrderDetail> it = orderDetailList.iterator();
		List<CotOrderDetail> orderdetails = new ArrayList<CotOrderDetail>();
		while (it.hasNext()) {
			// 要转化的订单明细对象
			CotOrderDetail orderDetail = it.next();
			Long unBoxCount4OrderFac = orderDetail.getUnBoxCount4OrderFac();
			// 未采购数量>0
			if (true || unBoxCount4OrderFac > 0) {
				// 新建采购明细对象
				CotOrderFacdetail cotOrderFacdetail = new CotOrderFacdetail();
				// 查询采购明细中是否有该订单明细的采购记录
				CotOrderFacdetail facDetail = orderfacdetailsMap
						.get(orderDetail.getId());
				if (facDetail != null) {
					cotOrderFacdetail = facDetail;
				}
				// 使用反射获取对象的所有属性名称
				String[] propEle = ReflectionUtils
						.getDeclaredFields(CotOrderDetail.class);

				ConvertUtilsBean convertUtils = new ConvertUtilsBean();
				SqlDateConverter dateConverter = new SqlDateConverter();
				convertUtils.register(dateConverter, Date.class);
				// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
				BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
						new PropertyUtilsBean());

				for (int i = 0; i < propEle.length; i++) {
					try {
						String value = beanUtils.getProperty(orderDetail,
								propEle[i]);
						if ("op".equals(propEle[i]) || "id".equals(propEle[i])
								|| "picImg".equals(propEle[i])
								|| "addTime".equals(propEle[i])
								|| "eleHsid".equals(propEle[i])
								|| "unBoxCount".equals(propEle[i])
								|| "unBoxSend".equals(propEle[i])
								|| "unBoxCount4OrderFac".equals(propEle[i])
								|| "unBoxSend4OrderFac".equals(propEle[i])
								|| "boxCount".equals(propEle[i])
								|| "liRun".equals(propEle[i])
								|| "tuiLv".equals(propEle[i])
								|| "orderFacno".equals(propEle[i])
								|| "orderFacnoid".equals(propEle[i])
								|| "outHasBoxCount4OrderFac".equals(propEle[i])) {
							continue;
						}
						if (value != null) {
							beanUtils.setProperty(cotOrderFacdetail,
									propEle[i], value);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(propEle[i] + ":转换错误!");
					}
				}
				cotOrderFacdetail.setHsId(orderDetail.getEleHsid());// 海关编码
				cotOrderFacdetail.setOrderDetailId(orderDetail.getId());// 设置所取订单明细Id
				cotOrderFacdetail.setOrderId(orderFacId);
				if (facDetail != null) {
					cotOrderFacdetail.setBoxCount(facDetail.getBoxCount()
							+ orderDetail.getUnBoxCount4OrderFac());
					cotOrderFacdetail.setOutRemain(facDetail.getBoxCount());
				} else {
					cotOrderFacdetail.setBoxCount(orderDetail.getBoxCount());// 未采购数量
					cotOrderFacdetail.setOutRemain(orderDetail.getBoxCount());
					// cotOrderFacdetail.setOutRemain(orderDetail.getUnBoxCount4OrderFac());//
					// 剩余出货数量

				}
				records.add(cotOrderFacdetail);

				// 修改订单未采购数量
				orderDetail.setUnBoxCount4OrderFac(new Long(0));
				orderDetail.setUnBoxSend4OrderFac(1);// 1:全部发货
				orderDetail.setOrderFacno(orderFacno);
				orderDetail.setOrderFacnoid(orderFacId);
				orderdetails.add(orderDetail);
			}
			this.getCotBaseDao().saveOrUpdateRecords(orderDetailList);
		}
		return records;
	}

	private Map<Integer, CotOrderfacPic> getOrderFacImgByOrderId(
			List<CotOrderFacdetail> details) {
		String ids = "";
		for (CotOrderFacdetail facdetail : details) {
			ids += facdetail.getOrderDetailId() + ",";
		}
		if (ids.equals(""))
			return null;
		ids = ids.substring(0, ids.length() - 1);
		List<CotOrderPic> pics = this.getCotBaseDao().find(
				" from CotOrderPic obj where obj.fkId in (" + ids + ")");
		Map<Integer, CotOrderfacPic> map = new HashMap<Integer, CotOrderfacPic>();
		for (CotOrderPic pic : pics) {
			CotOrderfacPic facPic = new CotOrderfacPic();
			facPic.setEleId(pic.getEleId());
			facPic.setFkId(null);
			facPic.setPicImg(pic.getPicImg());
			facPic.setPicName(pic.getPicName());
			facPic.setPicSize(pic.getPicSize());
			map.put(pic.getFkId(), facPic);
		}
		return map;
	}

	// 获取已存在的生产合同明细
	private Map<Integer, CotOrderFacdetail> getExistOrderDetails(
			Integer orderfadId) {
		List<CotOrderFacdetail> list = this.getCotBaseDao().find(
				" from CotOrderFacdetail obj where obj.orderId=" + orderfadId);
		Map<Integer, CotOrderFacdetail> map = new HashMap<Integer, CotOrderFacdetail>();
		for (int i = 0; i < list.size(); i++) {
			CotOrderFacdetail orderfacdetail = list.get(i);
			map.put(orderfacdetail.getOrderDetailId(), orderfacdetail);
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.orderfac.CotOrderFacService#updateOrderStatus(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	public Integer[] updateOrderStatusCon(Integer orderfacId, Integer orderId)
			throws Exception {
		// TODO:调用存储过程
		Object[] param = new Object[2];
		param[0] = orderfacId;
		param[1] = orderId;
		this.getCotBaseDao().callProc("{call update_orderfac_status(?,?)}",
				param);

		CotOrder cotOrder = (CotOrder) this.getCotBaseDao().getById(
				CotOrder.class, orderId);
		if (cotOrder.getCanOut() != null && cotOrder.getCanOut() == 1) {
			// 生成没有单号的出货单
			this.getOrderService().saveInvoice(orderId, false);
		}
		//有可能之前填了所有时间,并且已经生成出货单,这时再去掉某个时间,需要把invoice删除
		if (cotOrder.getCanOut() != null && cotOrder.getCanOut() == 0) {
			// 查询该订单是否有出货
			String hqlOut = "select obj.id from CotOrderOut obj where obj.orderId="
					+ orderId;
			List listOut = this.getCotBaseDao().find(hqlOut);
			if (listOut != null && listOut.size() > 0) {
				Integer outId=(Integer) listOut.get(0);
				this.getCotBaseDao().deleteRecordById(outId, "CotOrderOut");
			}
		}
		Integer[] status = new Integer[5];
		CotOrderFac orderfac = this.getOrderFacById(orderfacId);
//		if(orderfac.getPreNum()!=null){
//			status[0]=0;
//		}else{
			status[0]=orderfac.getSampleStatus();
//		}
//		if(orderfac.getArtNum()!=null){
//			status[1]=0;
//		}else{
			status[1]=orderfac.getPacketStatus();
//		}
//		if(orderfac.getSamNum()!=null){
//			status[2]=0;
//		}else{
			status[2]=orderfac.getSampleOutStatus();
//		}
//		if(orderfac.getQcNum()!=null){
//			status[3]=0;
//		}else{
			status[3]=orderfac.getQcStatus();
//		}
//		if(orderfac.getShipNum()!=null){
//			status[4]=0;
//		}else{
			status[4]=orderfac.getOutStatus();
//		}
		return status;
	}

	// 修改审核状态
	public Timestamp updateOrderStatus(Integer orderId, Integer orderStatus,
			Integer checkPerson) throws DAOException {
		CotOrderFac cotOrder = (CotOrderFac) this.getCotBaseDao().getById(
				CotOrderFac.class, orderId);
		cotOrder.setCotOrderFacdetails(null);
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		Timestamp tmp = new Timestamp(System.currentTimeMillis());
		// 修改人
		cotOrder.setModTime(tmp);// 修改时间
		cotOrder.setModPerson(cotEmps.getId());
		// 审核人
		if (checkPerson != null) {
			cotOrder.setCheckPerson(checkPerson);
		}
		// 如果审核通过,生成报表
		if (orderStatus == 2) {
			cotOrder.setCheckDate(new java.util.Date(System.currentTimeMillis()));
			try {
				File file = new File(SystemUtil.getRptFilePath() + "orderfac");
				if (!file.exists()) {
					file.mkdirs();
				}
				File fileProduct = new File(SystemUtil.getRptFilePath() + "home/Product");
				if (!fileProduct.exists()) {
					fileProduct.mkdirs();
				}
				File fileShipment = new File(SystemUtil.getRptFilePath() + "home/Shipment");
				if (!fileShipment.exists()) {
					fileShipment.mkdirs();
				}
				// 自定义报表
				String rptPath = SystemUtil.getRptFilePath() + "orderfac/PO-"
						+ cotOrder.getOrderNo() + ".pdf";
				POService poService = new POServiceImpl();
				VPurchaseOrder vpOrder = poService.getCotPOVO(orderId);
				List<CotOrderFacdetail> dList = poService.getDetailList(orderId);
				
				POPdf poPdf = new POPdf();
				poPdf.setvpOrder(vpOrder);
				poPdf.setDetailList(dList);
				poPdf.createPOPDF(rptPath);
				
				//TODO:生成Product Shipment报表
				//生成Product报表
				String rptPathProduct = SystemUtil.getRptFilePath() + "home/Product/Color Approval Report_"
				+ cotOrder.getOrderNo() + ".pdf";
				poPdf.setIsReport("true");
				poPdf.createColorPOPDF(rptPathProduct);
				
				//生成Shipment报表
				String rptPathShipment = SystemUtil.getRptFilePath() + "home/Shipment/Shipment Sample Approval Report_"
				+ cotOrder.getOrderNo() + ".pdf";
				SSAPdf ssaPdf = new SSAPdf();
				ssaPdf.setVpOrder(vpOrder);
				ssaPdf.setDetailList(dList);
				ssaPdf.createSSAPDF(rptPathShipment);
				
				//生成Packing报表
//				PIService piService = new PIServiceImpl();
//				VProformaInvoice vpInvoice = piService.getCotPIVO(cotOrder.getOrderId());
//				ArtWordPdf artWordPdf = new ArtWordPdf();
//				String rptPathPacking = SystemUtil.getRptFilePath() + "home/Product/Art work Briefing_"
//				+ cotOrder.getOrderNo() + ".pdf";
//				artWordPdf.setvpOrder(vpOrder);
//				artWordPdf.setVpInvoice(vpInvoice);
//				List<CotArtWordVO> aDetaiList = poService.getArtWordList(orderId);
//				artWordPdf.setDetailList(aDetaiList);
//				artWordPdf.createPOPDF(rptPathPacking);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		// 如果变成请求审核状态时,并且如果旧单已经审核过了
		if (orderStatus == 3 && cotOrder.getOrderStatus() == 2) {
			// 如果已生成invoice 则删除
//			String outHql = "delete from CotOrderOut obj where obj.orderId="
//					+ cotOrder.getOrderId();
//			QueryInfo queryInfo = new QueryInfo();
//			queryInfo.setSelectString(outHql);
//			this.getCotBaseDao().executeUpdate(queryInfo, null);
			
			//修改订单canOut状态为0
			QueryInfo queryInfoOrder = new QueryInfo();
			String upSql="update CotOrder obj set obj.canOut=0 where obj.id="+cotOrder.getOrderId();
			queryInfoOrder.setSelectString(upSql);
			this.getCotBaseDao().executeUpdate(queryInfoOrder, null);
			cotOrder.setCheckDate(null);
		}
		List list = new ArrayList();
		cotOrder.setOrderStatus(orderStatus.longValue());
		list.add(cotOrder);
		this.getCotBaseDao().updateRecords(list);

		return tmp;
	}

	// 通过订单ID获得客户对象
	public CotCustomer getCustByOrderId(Integer orderId) {
		CotOrder cotOrder = (CotOrder) this.getCotBaseDao().getById(
				CotOrder.class, orderId);
		if (cotOrder != null) {
			CotOrder custClone = (CotOrder) SystemUtil.deepClone(cotOrder);
			custClone.setCotOrderDetails(null);
			custClone.setOrderMBImg(null);
			int custId = custClone.getCustId();
			CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao()
					.getById(CotCustomer.class, custId);
			if (cotCustomer != null) {
				CotCustomer custCl = (CotCustomer) SystemUtil
						.deepClone(cotCustomer);
				custCl.setPicImg(null);
				custCl.setCustImg(null);
				custCl.setCustomerClaim(null);
				custCl.setCotCustContacts(null);
				custCl.setCustomerVisitedLogs(null);
				return custCl;
			}

		}
		return null;

	}

	public CotOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(CotOrderService orderService) {
		this.orderService = orderService;
	}

	// 返回审核数 CotOrderFac
	public Integer getCountPO() {
		HttpSession session = WebContextFactory.get().getSession();
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		CotEmps emp = (CotEmps) session.getAttribute("emp");
		Integer empId = emp.getId();
		String hql;
		if (!"admin".equals(emp.getEmpsId())) {
			hql = " from CotOrderFac obj where obj.orderStatus=3 and obj.checkPerson="
					+ empId;
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotorder.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					hql += " and obj.nationId in("+nationStr+")";
				}
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorder.do", "DEPT");
				if (dept == true) {
					String deptStr = json.getString("dept");
					hql += "and obj.typeLv1Id in("+deptStr+")";
				}
			}
		} else {
			hql = " from CotOrderFac obj where obj.orderStatus=3";
		}
		List<CotOrderFac> list = this.getCotBaseDao().find(hql);
		if (list.size() > 0) {
			return list.size();
		}
		return 0;
	}

	// 根据id查找采购单信息
	public Integer getCanOutByOrderId(Integer orderId) {
		String hql = "select obj.canOut from CotOrder obj where obj.id="
				+ orderId;
		List list = this.getCotBaseDao().find(hql);
		if (list != null && list.size() > 0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	
	public List<?> getOrderFacVO(QueryInfo queryInfo) {
		try {
			List<?> list = this.getCotBaseDao().findRecordsJDBC(queryInfo);
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	
	//给5大标签的厂家评分
	public void updateOrderFacFen(Integer orderFacId,String status,Integer fen,String remark,Integer man) throws DAOException{
		String hql ="";
		if(status.equals("SAMPLE")){
			hql = "update CotOrderFac obj set obj.preNum=:fen,obj.preText=:remark,obj.preMan=:man where obj.id="+orderFacId;
		}
		if(status.equals("PACKAGE")){
			hql = "update CotOrderFac obj set obj.artNum=:fen,obj.artText=:remark,obj.artMan=:man where obj.id="+orderFacId;
		}
		if(status.equals("SAMPLEOUT")){
			hql = "update CotOrderFac obj set obj.samNum=:fen,obj.samText=:remark,obj.samMan=:man where obj.id="+orderFacId;
		}
		if(status.equals("QC")){
			hql = "update CotOrderFac obj set obj.qcNum=:fen,obj.qcText=:remark,obj.qcMan=:man where obj.id="+orderFacId;
		}
		if(status.equals("OUT")){
			hql = "update CotOrderFac obj set obj.shipNum=:fen,obj.shipText=:remark,obj.shipMan=:man where obj.id="+orderFacId;
		}
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		Map map = new HashMap();
		map.put("fen", fen);
		map.put("remark", remark);
		map.put("man", man);
		this.getCotBaseDao().executeUpdate(queryInfo, map);
	}
	
	// 修改
	public void updateList(List<?> list) {
		try {
			this.getCotBaseDao().updateRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createArtWork(Integer orderId) throws Exception{
		CotOrderFac cotOrder = (CotOrderFac) this.getCotBaseDao().getById(
				CotOrderFac.class, orderId);
		POService poService = new POServiceImpl();
		VPurchaseOrder vpOrder = poService.getCotPOVO(orderId);
		//生成Packing报表
		PIService piService = new PIServiceImpl();
		VProformaInvoice vpInvoice = piService.getCotPIVO(cotOrder.getOrderId());
		ArtWordPdf artWordPdf = new ArtWordPdf();
		//创建文件夹
		File fileProduct = new File(SystemUtil.getRptFilePath() + "home/Product");
		if (!fileProduct.exists()) {
			fileProduct.mkdirs();
		}
		String rptPathPacking = SystemUtil.getRptFilePath() + "home/Product/Art work Briefing_"
		+ cotOrder.getOrderNo() + ".pdf";
		artWordPdf.setvpOrder(vpOrder);
		artWordPdf.setVpInvoice(vpInvoice);
		List<CotArtWordVO> aDetaiList = poService.getArtWordList(orderId);
		artWordPdf.setDetailList(aDetaiList);
		artWordPdf.createPOPDF(rptPathPacking);
	}
	
	public void updateAddPerson() throws DAOException{
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		String hql="update CotOrderFac fac set fac.addPerson="+cotEmps.getId();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		this.getCotBaseDao().executeUpdate(queryInfo, null);
	}

	@Override
	public boolean getApprove(Integer orderfacId) {
		// TODO Auto-generated method stub
		CotOrderFac orderFac = (CotOrderFac)this.getCotBaseDao().getById(CotOrderFac.class, orderfacId);
		if(orderFac.getOrderId() != null){
			CotOrder order = (CotOrder)this.getCotBaseDao().getById(CotOrder.class, orderFac.getOrderId());
			if(order.getOrderStatus() != 2){
				//PI还未审核通过，PO不能审核通过
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.orderfac.CotOrderFacService#doComment()
	 */
	@Override
	public void doComment(Integer orderFacid,String comment,String approve,String type) throws Exception {
		// TODO Auto-generated method stub
		//1、保存生产合同
		CotOrderFac orderFac = (CotOrderFac)this.getCotBaseDao().getById(CotOrderFac.class, orderFacid);
		orderFac.setCotOrderFacdetails(null);
		JSONObject approveJson = JSONObject.fromObject(approve);
		if("sample".equalsIgnoreCase(type)){
			orderFac.setSampleApprove(approveJson.getString("approve"));
			orderFac.setApprovalCommentsample(comment);
		}else if ("sampleout".equalsIgnoreCase(type)) {
			orderFac.setSampleOutApprove(approveJson.getString("approve"));
			orderFac.setApprovalCommentsampleOut(comment);
			if("Yes".equals(approveJson.getString("washed"))){
				orderFac.setSampleOutWash(approveJson.getString("degree"));
			}else {
				orderFac.setSampleOutWash(approveJson.getString("washed"));
			}
			orderFac.setSampleOutPassed(approveJson.getString("passed"));
		}
		List list = new ArrayList();
		list.add(orderFac);
		this.getCotBaseDao().saveOrUpdateRecords(list);
		//2、生成PDF
		//生成Product报表
		POService poService = new POServiceImpl();
		VPurchaseOrder vpOrder = poService.getCotPOVO(orderFacid);
		String fileName = "";
		String filePath = "";
		if("sample".equalsIgnoreCase(type)){
			POPdf poPdf = new POPdf();
			poPdf.setvpOrder(vpOrder);
			List<CotOrderFacdetail> dList = poService.getDetailList(orderFacid);
			poPdf.setDetailList(dList);
			fileName = "Color Approval Report_"+ orderFac.getOrderNo() + ".pdf";
			filePath = "saverptfile/home/Product/"+fileName;
			String rptPathProduct = SystemUtil.getRptFilePath() + "home/Product/Color Approval Report_"
			+ orderFac.getOrderNo() + ".pdf";
			poPdf.setIsReport("true");
			poPdf.createColorPOPDF(rptPathProduct);
		}else if ("sampleout".equalsIgnoreCase(type)) {
		//生成Shipment报表
			fileName = "Shipment Sample Approval Report_"+ orderFac.getOrderNo() + ".pdf";
			filePath = "saverptfile/home/Shipment/"+fileName;
			String rptPathShipment = SystemUtil.getRptFilePath() + "home/Shipment/Shipment Sample Approval Report_"
			+ orderFac.getOrderNo() + ".pdf";
			SSAPdf ssaPdf = new SSAPdf();
			List<CotOrderFacdetail> dList = poService.getDetailList(orderFacid);
			List<CotOrderFacdetailCopy> dCopyList = poService.getDetailCopyList(orderFacid);
			ssaPdf.setVpOrder(vpOrder);
			ssaPdf.setDetailList(dList);
			ssaPdf.setDetailCopyList(dCopyList);
			ssaPdf.createSSAPDF(rptPathShipment);
		}
		//3、写入上传文件表
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		//3、写入上传文件表
		CotOrderstatusFile orderstatusFile = new CotOrderstatusFile();
		String sql = "from CotOrderstatusFile obj where obj.filePath = '"+filePath+"'";
		List countList = this.getCotBaseDao().find(sql);
		File file = new File(SystemUtil.getRootPath()+filePath);
		Long size = 0l;
		if(file.exists()){
			size = file.length();
		}
		if(CollectionUtils.isEmpty(countList)){
			orderstatusFile.setEmpsId(cotEmps.getId());
			orderstatusFile.setFileName(fileName);
			orderstatusFile.setFilePath(filePath);
			orderstatusFile.setFileSize(size.intValue());
			orderstatusFile.setOrderId(orderFac.getOrderId());
			orderstatusFile.setOrderStatus(type.toUpperCase());
		}else {
			orderstatusFile = (CotOrderstatusFile)countList.get(0);
			orderstatusFile.setFileSize(size.intValue());
		}
		list.clear();
		list.add(orderstatusFile);
		this.getCotBaseDao().saveOrUpdateRecords(list);
		//4、写入Daily Report.
		CotQuestion question = new CotQuestion();
		question.setOrderId(orderFac.getOrderId());
		question.setQueryPerson(cotEmps.getId());
		if("sampleout".equalsIgnoreCase(type)){
			question.setQueryText(orderFac.getApprovalCommentsampleOut());
			question.setFlag(33);
		}else if("sample".equalsIgnoreCase(type)) {
			question.setQueryText(orderFac.getApprovalCommentsample());
			question.setFlag(11);
		}
		question.setQueryTime(new Timestamp(System.currentTimeMillis()));
		list.clear();
		list.add(question);
		this.getCotBaseDao().saveOrUpdateRecords(list);
		
	}
	
	public void confirmByPassword(Integer orderfacId,String type) throws Exception{
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		//1、更新状态
		CotOrderFac orderFac = (CotOrderFac)this.getCotBaseDao().getById(CotOrderFac.class, orderfacId);
		orderFac.setCotOrderFacdetails(null);
		orderFac.setConfirmBy(cotEmps.getEmpsId());
		if("sample".equalsIgnoreCase(type)){
			orderFac.setSampleStatus(1);
			if(orderFac.getSimpleSampleApproval() == null){
				orderFac.setSimpleSampleApproval(new Date(System.currentTimeMillis()));
			}
			if(orderFac.getSimpleSampleDeadline() == null){
				orderFac.setSimpleSampleDeadline(new Date(System.currentTimeMillis()));
			}
			if(orderFac.getCompleteSampleApproval() == null){
				orderFac.setCompleteSampleApproval(new Date(System.currentTimeMillis()));
			}
			if(orderFac.getCompleteSampleDeadline() == null){
				orderFac.setCompleteSampleDeadline(new Date(System.currentTimeMillis()));
			}
		}else if("sampleout".equalsIgnoreCase(type)){
			orderFac.setSampleOutStatus(1);
			if(orderFac.getSamplePicApproval() == null){
				orderFac.setSamplePicApproval(new Date(System.currentTimeMillis()));
			}
			if(orderFac.getSamplePicDeadline() == null){
				orderFac.setSamplePicDeadline(new Date(System.currentTimeMillis()));
			}
			if(orderFac.getSampleOutApproval() == null){
				orderFac.setSampleOutApproval(new Date(System.currentTimeMillis()));
			}
			if(orderFac.getSampleOutDeadline() == null){
				orderFac.setSampleOutDeadline(new Date(System.currentTimeMillis()));
			}
		}
		List list = new ArrayList();
		list.add(orderFac);
		this.getCotBaseDao().saveOrUpdateRecords(list);
		//2、生成PDF
		//生成Product报表
		POService poService = new POServiceImpl();
		VPurchaseOrder vpOrder = poService.getCotPOVO(orderfacId);
		String fileName = "";
		String filePath = "";
		
		if("sample".equalsIgnoreCase(type)){
			POPdf poPdf = new POPdf();
			poPdf.setvpOrder(vpOrder);
			List<CotOrderFacdetail> dList = poService.getDetailList(orderfacId);
			poPdf.setDetailList(dList);
			fileName = "Color Approval Report_"+ orderFac.getOrderNo() + ".pdf";
			filePath = "saverptfile/home/Product/"+fileName;
			String rptPathProduct = SystemUtil.getRptFilePath() + "home/Product/Color Approval Report_"
			+ orderFac.getOrderNo() + ".pdf";
			poPdf.setIsReport("true");
			poPdf.createColorPOPDF(rptPathProduct);
		}else if ("sampleout".equalsIgnoreCase(type)) {
		//生成Shipment报表
			fileName = "Shipment Sample Approval Report_"+ orderFac.getOrderNo() + ".pdf";
			filePath = "saverptfile/home/Shipment/"+fileName;
			String rptPathShipment = SystemUtil.getRptFilePath() + "home/Shipment/Shipment Sample Approval Report_"
			+ orderFac.getOrderNo() + ".pdf";
			SSAPdf ssaPdf = new SSAPdf();
			ssaPdf.setVpOrder(vpOrder);
			List<CotOrderFacdetail> dList = poService.getDetailList(orderfacId);
			List<CotOrderFacdetailCopy> dListCopy = poService.getDetailCopyList(orderfacId);
			ssaPdf.setDetailList(dList);
			ssaPdf.setDetailCopyList(dListCopy);
			ssaPdf.createSSAPDF(rptPathShipment);
		}
		//3、写入上传文件表
		CotOrderstatusFile orderstatusFile = new CotOrderstatusFile();
		String sql = "from CotOrderstatusFile obj where obj.filePath = '"+filePath+"'";
		List countList = this.getCotBaseDao().find(sql);
		File file = new File(SystemUtil.getRootPath()+filePath);
		Long size = 0l;
		if(file.exists()){
			size = file.length();
		}
		if(CollectionUtils.isEmpty(countList)){
			orderstatusFile.setEmpsId(cotEmps.getId());
			orderstatusFile.setFileName(fileName);
			orderstatusFile.setFilePath(filePath);
			orderstatusFile.setFileSize(size.intValue());
			orderstatusFile.setOrderId(orderFac.getOrderId());
			orderstatusFile.setOrderStatus(type.toUpperCase());
		}else {
			orderstatusFile = (CotOrderstatusFile)countList.get(0);
			orderstatusFile.setFileSize(size.intValue());
		}
		list.clear();
		list.add(orderstatusFile);
		this.getCotBaseDao().saveOrUpdateRecords(list);
		//4、写入Daily Report.
		CotQuestion question = new CotQuestion();
		question.setOrderId(orderFac.getOrderId());
		question.setQueryPerson(cotEmps.getId());
		String msg = orderFac.getOrderNo()+"[XX] was confirmed by "+cotEmps.getEmpsId()+" using password.";
		if("sample".equalsIgnoreCase(type)){
			msg = msg.replace("XX", "Pre-production");
			question.setQueryText(msg);
			question.setFlag(11);
		}else if("sampleout".equalsIgnoreCase(type)) {
			msg = msg.replace("XX", "Shipment Samples");
			question.setQueryText(orderFac.getApprovalCommentsample());
			question.setFlag(33);
		}
		question.setQueryTime(new Timestamp(System.currentTimeMillis()));
		list.clear();
		list.add(question);
		this.getCotBaseDao().saveOrUpdateRecords(list);
	}

	@Override
	public void saveCopy(String orderfacJson) throws Exception {
		// TODO Auto-generated method stub
		JSONObject json = JSONObject.fromObject(orderfacJson);
		Integer orderfacdetailId = json.getInt("id");
		CotOrderFacdetailCopy detail  = (CotOrderFacdetailCopy)this.getCotBaseDao().getById(CotOrderFacdetailCopy.class, orderfacdetailId);
		Iterator<String> iterator = json.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = json.get(key);
			BeanUtils.setProperty(detail, key, value);
		}
		List records = new ArrayList();
		detail.setId(orderfacdetailId);
		records.add(detail);
		this.getCotBaseDao().saveOrUpdateRecords(records);
	}
	
	public void saveDetailCopy(Integer orderfacId) {
		//调用过程，生成一份明细副本
		String sql ="{call sp_orderfacdetail_copy(?)}";
		Integer[] values = new Integer[1];
		values[0] =orderfacId;
		//this.getCotBaseDao().callProc(sql, values);
		try {
			this.getCotBaseDao().callProc(sql, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
