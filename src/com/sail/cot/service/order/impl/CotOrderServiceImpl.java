package com.sail.cot.service.order.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import net.sf.json.JSONObject;
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
import org.apache.commons.lang.StringUtils;
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
import com.sail.cot.domain.CotContract;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustPc;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFinacerecv;
import com.sail.cot.domain.CotFinacerecvDetail;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotFittingsAnys;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotNation;
import com.sail.cot.domain.CotNationCity;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderEleprice;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderFittings;
import com.sail.cot.domain.CotOrderMb;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotOrderPc;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderStatus;
import com.sail.cot.domain.CotOrderfacMb;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotOrderoutPic;
import com.sail.cot.domain.CotPackingAnys;
import com.sail.cot.domain.CotPayType;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotPriceEleprice;
import com.sail.cot.domain.CotPriceFittings;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotProductFacMb;
import com.sail.cot.domain.CotProductMb;
import com.sail.cot.domain.CotQuestion;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.CotTypeLv2;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.domain.vo.CotMsgVO;
import com.sail.cot.domain.vo.CotNewOrderVO;
import com.sail.cot.domain.vo.CotNewPriceVO;
import com.sail.cot.domain.vo.CotOrderVO;
import com.sail.cot.mail.service.MailService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.seq.Sequece;
import com.sail.cot.service.QueryService;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.price.impl.CotPriceServiceImpl;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.sample.CotReportService;
import com.sail.cot.service.sign.impl.CotSignServiceImpl;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.SetNoService;
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
import com.sail.cot.util.pdf.PIService;
import com.sail.cot.util.pdf.create.PIPdf;
import com.sail.cot.util.pdf.impl.PIServiceImpl;

/**
 * *********************************************
 * 
 * @Copyright (C),2008-2010
 * @CompanyName 厦门市旗航软件有限公司(xmqh.net)
 * @Version 1.0
 * @Date Aug 25, 2010
 * @author azan
 * @class CotOrderServiceImpl.java
 * @Description 外销合同
 */
public class CotOrderServiceImpl implements CotOrderService {

	private CotBaseDao orderDao;
	private CotReportDao reportDao;
	private CotExportRptDao exportRptDao;
	private MailService mailService;
	private CotSysLogService sysLogService;
	private CotElementsService cotElementsService;
	private QueryService queryService;
	private CotCustomerService custService;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	private SetNoService noService;
	private Logger logger = Log4WebUtil.getLogger(CotOrderServiceImpl.class);

	// private GenAllSeq seq = new GenAllSeq();

	public Integer saveOrUpdateOrder(CotOrder cotOrder, String orderTime,
			String sendTime, String orderLcDate, String orderLcDelay,
			String addTime, boolean oldFlag,String design) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			// 唛标
			CotOrderMb cotOrderMb = null;
			if (cotOrder.getId() == null) {
				cotOrderMb = new CotOrderMb();
				cotOrder
						.setAddTime(new Timestamp(sdf.parse(addTime).getTime()));// 创建时间
			} else {
				cotOrder.setModTime(new Timestamp(System.currentTimeMillis()));// 修改时间
				cotOrder.setModPerson(cotEmps.getId());
				String hql = "from CotOrderMb obj where obj.fkId="
						+ cotOrder.getId();
				List<?> listMB = this.getOrderDao().find(hql);
				cotOrderMb = (CotOrderMb) listMB.get(0);
			}
			if (oldFlag) {
				// String hql = "select obj.picImg from CotCustMb obj where
				// obj.fkId=" + cotOrder.getCustId();
				// List<?> listMB = this.getOrderDao().find(hql);
				byte[] pm = this.getZwtpPic();
				cotOrderMb.setPicImg(pm);
				cotOrderMb.setPicSize(pm.length);
			}
			// 产品标图片
			CotProductMb cotProductMb = null;
			if (cotOrder.getId() == null) {
				cotProductMb = new CotProductMb();
			}
			if (orderTime != null && !"".equals(orderTime)) {
				cotOrder.setOrderTime(new Timestamp(sdf.parse(orderTime)
						.getTime()));// 下单时间
			}
			if (sendTime != null && !"".equals(sendTime)) {
				cotOrder.setSendTime(new Date(sdf2.parse(sendTime).getTime()));// 交货时间
			}
			if (orderLcDate != null && !"".equals(orderLcDate)) {
				cotOrder.setOrderLcDate(new Date(sdf2.parse(orderLcDate)
						.getTime()));// Shipment(船期)
			}
			if (orderLcDelay != null && !"".equals(orderLcDelay)) {
				cotOrder.setOrderLcDelay(new Date(sdf2.parse(orderLcDelay)
						.getTime()));// ETA(到港日期)
			}
			if (design != null && !"".equals(design)) {
				cotOrder.setDesignTime(new Date(sdf2.parse(design)
						.getTime()));// Design
			}
			// cotOrder.setAddPerson(cotEmps.getEmpsName());// 操作人
			cotOrder.setEmpId(cotEmps.getId());// 操作人编号

			// 如果状态为通过或不通过时,保存审核人姓名
			// if (cotOrder.getOrderStatus() == 1 || cotOrder.getOrderStatus()
			// == 2) {
			// cotOrder.setCheckPerson(cotEmps.getEmpsName());
			// }

			if (cotOrder.getOrderRate() != null && cotOrder.getOrderRate() != 0) {
			} else {
				CotCurrency cotCurrency = (CotCurrency) this.getOrderDao()
						.getById(CotCurrency.class, cotOrder.getCurrencyId());
				cotOrder.setOrderRate(cotCurrency.getCurRate().doubleValue());// 汇率
			}
			String pldNo = "";
			if (cotOrder.getId() == null) {
				 CotSeqService cotSeqService = new CotSeqServiceImpl();
				 cotSeqService.saveCustSeq(cotOrder.getCustId(), "order",
				 cotOrder.getOrderTime().toString());
				 cotSeqService.saveSeq("order");
				cotOrder.setCanOut(0);
			} else {

				String buy = "";
				String sell = "";
				if (cotOrder.getIsCheckAgent() != null
						&& cotOrder.getIsCheckAgent() == 1) {
					buy = cotOrder.getBuyer();
					sell = cotOrder.getSeller();
				} else {
					// 不够选
					buy = cotOrder.getSeller();
					// PO seller提取suplier
					int factoryId = cotOrder.getFactoryId();
					String str = "";
					CotFactory cotFactory = (CotFactory) this.getOrderDao()
							.getById(CotFactory.class, cotOrder.getFactoryId());
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
							CotNationCity cotNationCity = (CotNationCity) this
									.getOrderDao().getById(CotNationCity.class,
											cotFactory.getCityId());
							String nationCity = cotNationCity.getCityName();
							if (!nationCity.equals("")) {
								city = nationCity + " \n";
							} else {
								if (facPost != null && !facPost.equals("")) {
									post = facPost + " \n";
								}
							}
						}
						// 获得国家名
						if (cotFactory.getNationId() != null
								&& !cotFactory.getNationId().equals("")
								&& !cotFactory.getNationId().equals("null")) {
							CotNation cotNation = (CotNation) this
									.getOrderDao().getById(CotNation.class,
											cotFactory.getNationId());
							String country = cotNation.getNationName();
							if (!country.equals("")) {
								nation = country;
							}
						}
						str = fullName + adr + post + city + nation;
						int start = str.indexOf(" ");
						int end = str.lastIndexOf(" ");
						int strLength = str.length() - 2;
						int oneLength = str.length() - 1;
						String lastStr = str
								.substring(strLength, strLength + 1);
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
					}
					sell = str;
				}

				// 判断pi的厂家是否和po的一样
				boolean ck = false;
				String ckHql = "select obj.id from CotOrderFac obj where obj.orderId="
						+ cotOrder.getId()
						+ " and obj.factoryId="
						+ cotOrder.getFactoryId();
				List listCk = this.getOrderDao().find(ckHql);
				if (listCk == null || listCk.size() == 0) {
					ck = true;
				}
				// 判断Agent是否改变
				boolean agentCk = false;
				String agetHql = "select obj.isCheckAgent from CotOrderFac obj where obj.orderId="
						+ cotOrder.getId();
				List listagentCk = this.getOrderDao().find(agetHql);
				if (listagentCk.size() > 0) {
					Integer oleAgent = (Integer) listagentCk.get(0);
					Integer newAgent = (Integer) cotOrder.getIsCheckAgent();
					if (oleAgent != newAgent) {
						agentCk = true;
					}
				}

				// 判断pi的客户是否有改变
				boolean ckCust = false;
				String ckCustHql = "select obj.id from CotOrder obj where obj.id="
						+ cotOrder.getId()
						+ " and obj.custId="
						+ cotOrder.getCustId();
				List listCustCk = this.getOrderDao().find(ckCustHql);
				if (listCustCk == null || listCustCk.size() == 0) {
					ckCust = true;
				}

				// 判断pi的单号是否有改变
				String ckNoHql = "select obj.orderNo from CotOrder obj where obj.id="
						+ cotOrder.getId();
				List listNoCk = this.getOrderDao().find(ckNoHql);
				pldNo = (String) listNoCk.get(0);

				Map map = new HashMap();
				map.put("orderNo", cotOrder.getOrderNo());
				map.put("shipmentDate", cotOrder.getOrderLcDate());

				// 船期前30天
				// Calendar calShip30 = Calendar.getInstance();
				// calShip30.setTime(cotOrder.getOrderLcDate());
				// int c = calShip30.get(Calendar.DATE);
				// calShip30.set(Calendar.DATE, c-30);
				// map.put("facDeadline", calShip30.getTime());

				// 船期前17天
				Calendar calShip17 = Calendar.getInstance();
				calShip17.setTime(cotOrder.getOrderLcDate());
				int e = calShip17.get(Calendar.DATE);
				calShip17.set(Calendar.DATE, e - 17);
				map.put("sampleOutDeadline", calShip17.getTime());
				map.put("shippingDeadline", calShip17.getTime());

				// 船期前7天
				Calendar calShip7 = Calendar.getInstance();
				calShip7.setTime(cotOrder.getOrderLcDate());
				int f = calShip7.get(Calendar.DATE);
				calShip7.set(Calendar.DATE, f - 7);
				map.put("qcDeadline", calShip7.getTime());

				// 船期后10天
				Calendar calShipH10 = Calendar.getInstance();
				calShipH10.setTime(cotOrder.getOrderLcDate());
				int h = calShipH10.get(Calendar.DATE);
				calShipH10.set(Calendar.DATE, h + 10);
				map.put("loadingDeadline", calShipH10.getTime());

				map.put("sendTime", cotOrder.getSendTime());
				map.put("containerTypeId", cotOrder.getContainerTypeId());
				map.put("shipportId", cotOrder.getShipportId());
				map.put("targetportId", cotOrder.getTargetportId());
				// map.put("orderRemark", cotOrder.getOrderRemark());
				map.put("allPinName", cotOrder.getAllPinName());
				map.put("factoryId", cotOrder.getFactoryId());
				map.put("poNo", cotOrder.getPoNo());
				map.put("id", cotOrder.getId());
				// TotalCBM,
				map.put("orderEarnest", cotOrder.getOrderEarnest());
				// Company
				map.put("companyId", cotOrder.getCompanyId());
				// department
				map.put("typeLv1Id", cotOrder.getTypeLv1Id());
				//nationId
				map.put("nationId",cotOrder.getNationId());
				map.put("themeStr", cotOrder.getThemeStr());
				if(StringUtils.isEmpty(cotOrder.getThemeStr())){
					map.put("themes", "No");
				}else {
					map.put("themes", "Yes");
				}
				// Sale
				map.put("businessPerson", cotOrder.getBussinessPerson());
				String ag = "";
				if (ck == true || agentCk == true) {
					ag = ",obj.isCheckAgent=:isCheckAgent,obj.agent=:agent,obj.buyer=:buyer,obj.seller=:seller";
					map.put("isCheckAgent", cotOrder.getIsCheckAgent());
					map.put("agent", cotOrder.getAgent());
					map.put("buyer", buy);
					map.put("seller", sell);
					if (ck == true) {
						ag += ",obj.payTypeId =:payTypeId";
						// 付款方式,
						CotFactory fast = (CotFactory) this.getOrderDao()
								.getObjectById("CotFactory",
										cotOrder.getFactoryId());
						map.put("payTypeId", fast.getPayTypeId());
					}
				}
				String agCust = "";
				if (ckCust == true) {
					agCust = ",obj.consignee=:consignee,obj.forwardingAgent=:forwardingAgent";
					map.put("consignee", cotOrder.getBuyer());
					CotCustomer cust = (CotCustomer) this.getOrderDao()
							.getObjectById("CotCustomer", cotOrder.getCustId());
					map.put("forwardingAgent", cust.getForwardingAgent());
				}

				// pi主单修改 要修改po的单号,Shipment delivery container 起运港 目的港,comments,
				// 付款方式,TotalCBM,
				// product

				String faclMb = "update CotOrderFac obj set obj.orderNo=:orderNo, obj.shipmentDate=:shipmentDate,"
						+ "obj.sendTime=:sendTime,"
						+ "obj.containerTypeId=:containerTypeId,"
						+ "obj.shipportId=:shipportId,"
						+ "obj.targetportId=:targetportId,"
						+ "obj.orderEarnest=:orderEarnest,"
						+ "obj.typeLv1Id = :typeLv1Id,"
						+ "obj.nationId = :nationId,"
						// "obj.orderRemark=:orderRemark," +
						+ "obj.allPinName=:allPinName,"
						+ "obj.poNo=:poNo,"
						+ "obj.businessPerson=:businessPerson,"
						+ "obj.companyId=:companyId,"
						+ "obj.factoryId=:factoryId,"
						+ "obj.sampleOutDeadline=:sampleOutDeadline,"
						+ "obj.shippingDeadline=:shippingDeadline,"
						+ "obj.qcDeadline=:qcDeadline,"
						+ "obj.themes=:themes,"
						+ "obj.themeStr=:themeStr,"
						+ "obj.loadingDeadline=:loadingDeadline"
						+ ag
						+ agCust
						+ " where obj.orderId=:id";
				QueryInfo queryInfo = new QueryInfo();
				queryInfo.setSelectString(faclMb);
				int result = this.getOrderDao().executeUpdate(queryInfo, map);
			}
			List<CotOrder> list = new ArrayList<CotOrder>();
			list.add(cotOrder);
			this.getOrderDao().saveOrUpdateRecords(list);
			if (oldFlag) {
				cotOrderMb.setFkId(cotOrder.getId());
				List newMb = new ArrayList();
				newMb.add(cotOrderMb);
				this.getOrderDao().saveOrUpdateRecords(newMb);
			}
			// 保存产品标
			if (cotProductMb != null) {
				cotProductMb.setFkId(cotOrder.getId());
				byte[] zwtp = this.getZwtpPic();
				cotProductMb.setPicImg(zwtp);
				cotProductMb.setPicSize(zwtp.length);
				List newMb = new ArrayList();
				newMb.add(cotProductMb);
				this.getOrderDao().saveRecords(newMb);
			}

			// 如果该单已审核通过,当有修改过单号时,修改pdf的文件名
			if (cotOrder.getOrderStatus() != null
					&& cotOrder.getOrderStatus() == 2
					&& !pldNo.equals(cotOrder.getOrderNo())) {
				// 获得旧报表
				File file = new File(SystemUtil.getRptFilePath() + "order/PI-"
						+ pldNo + ".pdf");
				File fileNew = new File(SystemUtil.getRptFilePath()
						+ "order/PI-" + cotOrder.getOrderNo() + ".pdf");
				if (file.exists()) {
					file.renameTo(fileNew);
				}
				// 获得po旧报表
				File filePo = new File(SystemUtil.getRptFilePath()
						+ "orderfac/PO-" + pldNo + ".pdf");
				File fileNewPo = new File(SystemUtil.getRptFilePath()
						+ "orderfac/PO-" + cotOrder.getOrderNo() + ".pdf");
				if (filePo.exists()) {
					filePo.renameTo(fileNewPo);
				}
			}

			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主订单成功");
			cotSyslog.setOpModule("order");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotOrder.getOrderNo());
			sysLogService.addSysLogByObj(cotSyslog);

			return cotOrder.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存订单出错！");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotOrder.getModPerson());
			cotSyslog.setOpMessage("添加或修改主订单失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("order");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotOrder.getOrderNo());
			sysLogService.addSysLogByObj(cotSyslog);
			return null;
		}

	}

	// 得到总记录数
	public Integer getRecordCount(String tableName, String whereStr) {
		String hql = "select count(*) from ";
		if (tableName != null && !"".equals(tableName)) {
			hql = hql + tableName;
			if (whereStr != null && !"".equals(whereStr)) {
				hql += whereStr;
			}
			List<?> list = this.getOrderDao().find(hql);
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}

	public Boolean addOrderDetails(List<?> details) {
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

		String fitPriceNum = ContextUtil.getProperty("remoteaddr.properties",
				"fitPrecision");
		if (fitPriceNum == null) {
			fitPriceNum = "2";
		}
		try {
			this.getOrderDao().saveRecords(details);

			// 生成包材分析数据
			this.savePackAnys(details);
			// 保存配件及成本信息
			List listFitting = new ArrayList();
			List listPrice = new ArrayList();
			for (int i = 0; i < details.size(); i++) {
				CotOrderDetail detail = (CotOrderDetail) details.get(i);
				String type = detail.getType();
				Integer srcId = detail.getSrcId();
				// 从样品来
				if (type.equals("ele") || type.equals("given")) {
					if (type.equals("given")) {
						Integer eId = this.getEleIdByEleName(detail.getEleId());
						if (eId != 0) {
							srcId = eId;
						} else {
							srcId = 0;
						}
					}
					// 获得原配件信息
					String hql = "from CotEleFittings obj where obj.eleId="
							+ srcId;
					List list = this.getOrderDao().find(hql);
					for (int j = 0; j < list.size(); j++) {
						CotEleFittings eleFittings = (CotEleFittings) list
								.get(j);
						CotOrderFittings orderFittings = new CotOrderFittings();
						orderFittings.setOrderDetailId(detail.getId());
						orderFittings.setOrderId(detail.getOrderId());
						orderFittings.setEleId(detail.getEleId());
						orderFittings.setEleName(detail.getEleName());
						orderFittings.setFitNo(eleFittings.getFitNo());
						orderFittings.setFitName(eleFittings.getFitName());
						orderFittings.setFitDesc(eleFittings.getFitDesc());
						orderFittings.setFacId(eleFittings.getFacId());
						orderFittings
								.setFitUseUnit(eleFittings.getFitUseUnit());
						orderFittings.setFitUsedCount(eleFittings
								.getFitUsedCount());
						orderFittings.setFitCount(eleFittings.getFitCount());
						orderFittings.setFitPrice(eleFittings.getFitPrice());
						orderFittings.setFitTotalPrice(eleFittings
								.getFitTotalPrice());
						orderFittings.setFitRemark(eleFittings.getFitRemark());
						orderFittings.setFittingId(eleFittings.getFittingId());

						listFitting.add(orderFittings);
					}
					// 获得原成本信息
					String hqlPrice = "from CotElePrice obj where obj.eleId="
							+ srcId;
					List listTemp = this.getOrderDao().find(hqlPrice);
					for (int j = 0; j < listTemp.size(); j++) {
						CotElePrice elePrice = (CotElePrice) listTemp.get(j);
						CotOrderEleprice orderEleprice = new CotOrderEleprice();

						orderEleprice.setOrderDetailId(detail.getId());
						orderEleprice.setOrderId(detail.getOrderId());
						orderEleprice.setPriceName(elePrice.getPriceName());
						orderEleprice.setPriceUnit(elePrice.getPriceUnit());
						orderEleprice.setPriceAmount(elePrice.getPriceAmount());
						orderEleprice.setRemark(elePrice.getRemark());

						listPrice.add(orderEleprice);
					}
				}
				// 从订单来
				if (type.equals("order")) {
					// 获得原配件信息
					String hql = "from CotOrderFittings obj where obj.orderDetailId="
							+ srcId;
					List list = this.getOrderDao().find(hql);
					for (int j = 0; j < list.size(); j++) {
						CotOrderFittings orderFittings = (CotOrderFittings) list
								.get(j);
						CotOrderFittings clone = (CotOrderFittings) SystemUtil
								.deepClone(orderFittings);

						String temp = "select obj from CotFittings obj,CotOrderFittings p "
								+ "where p.fittingId=obj.id and p.id="
								+ clone.getId()
								+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,"
								+ fitPriceNum + ")!=p.fitPrice)";
						List disFits = this.getOrderDao().find(temp);
						if (disFits.size() > 0) {
							CotFittings fittings = (CotFittings) disFits.get(0);
							clone.setFacId(fittings.getFacId());
							BigDecimal b = new BigDecimal(fittings
									.getFitPrice()
									/ fittings.getFitTrans());
							double f1 = b.setScale(
									Integer.parseInt(fitPriceNum),
									BigDecimal.ROUND_HALF_UP).doubleValue();
							clone.setFitPrice(f1);
						}
						clone.setId(null);
						clone.setOrderDetailId(detail.getId());
						clone.setOrderId(detail.getOrderId());
						listFitting.add(clone);
					}

					// 获得原成本信息
					String hqlPrice = "from CotOrderEleprice obj where obj.orderDetailId="
							+ srcId;
					List listTemp = this.getOrderDao().find(hqlPrice);
					for (int j = 0; j < listTemp.size(); j++) {
						CotOrderEleprice eleprice = (CotOrderEleprice) listTemp
								.get(j);
						CotOrderEleprice clone = (CotOrderEleprice) SystemUtil
								.deepClone(eleprice);
						clone.setId(null);
						clone.setOrderDetailId(detail.getId());
						clone.setOrderId(detail.getOrderId());
						listPrice.add(clone);
					}
				}
				// 从报价来
				if (type.equals("price")) {
					// 获得原配件信息
					String hql = "from CotPriceFittings obj where obj.priceDetailId="
							+ srcId;
					List list = this.getOrderDao().find(hql);
					for (int j = 0; j < list.size(); j++) {
						CotPriceFittings priceFittings = (CotPriceFittings) list
								.get(j);
						CotOrderFittings orderFittings = new CotOrderFittings();

						String temp = "select obj from CotFittings obj,CotPriceFittings p "
								+ "where p.fittingId=obj.id and p.id="
								+ priceFittings.getId()
								+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,"
								+ fitPriceNum + ")!=p.fitPrice)";
						List disFits = this.getOrderDao().find(temp);
						Integer facId = priceFittings.getFacId();
						Double pr = priceFittings.getFitPrice();
						if (disFits.size() > 0) {
							CotFittings fittings = (CotFittings) disFits.get(0);
							facId = fittings.getFacId();
							BigDecimal b = new BigDecimal(fittings
									.getFitPrice()
									/ fittings.getFitTrans());
							pr = b.setScale(Integer.parseInt(fitPriceNum),
									BigDecimal.ROUND_HALF_UP).doubleValue();
						}

						orderFittings.setOrderDetailId(detail.getId());
						orderFittings.setOrderId(detail.getOrderId());
						orderFittings.setEleId(detail.getEleId());
						orderFittings.setEleName(detail.getEleName());
						orderFittings.setFitNo(priceFittings.getFitNo());
						orderFittings.setFitName(priceFittings.getFitName());
						orderFittings.setFitDesc(priceFittings.getFitDesc());
						orderFittings.setFacId(facId);
						orderFittings.setFitUseUnit(priceFittings
								.getFitUseUnit());
						orderFittings.setFitUsedCount(priceFittings
								.getFitUsedCount());
						orderFittings.setFitCount(priceFittings.getFitCount());
						orderFittings.setFitPrice(pr);
						orderFittings.setFitTotalPrice(priceFittings
								.getFitTotalPrice());
						orderFittings
								.setFitRemark(priceFittings.getFitRemark());
						orderFittings
								.setFittingId(priceFittings.getFittingId());

						listFitting.add(orderFittings);
					}

					// 获得原成本信息
					String hqlPrice = "from CotPriceEleprice obj where obj.priceDetailId="
							+ srcId;
					List listTemp = this.getOrderDao().find(hqlPrice);
					for (int j = 0; j < listTemp.size(); j++) {
						CotPriceEleprice priceEleprice = (CotPriceEleprice) listTemp
								.get(j);
						CotOrderEleprice orderEleprice = new CotOrderEleprice();
						orderEleprice.setOrderDetailId(detail.getId());
						orderEleprice.setOrderId(detail.getOrderId());
						orderEleprice
								.setPriceName(priceEleprice.getPriceName());
						orderEleprice
								.setPriceUnit(priceEleprice.getPriceUnit());
						orderEleprice.setPriceAmount(priceEleprice
								.getPriceAmount());
						orderEleprice.setRemark(priceEleprice.getRemark());
						listPrice.add(orderEleprice);
					}
				}

			}
			this.getOrderDao().saveRecords(listFitting);
			this.getOrderDao().saveRecords(listPrice);

			// 生成配件分析数据
			this.saveFitAnys(details);

			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加订单明细成功");
				syslog.setOpModule("order");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				sysLogService.addSysLogByObj(syslog);
			}
			return true;
		} catch (DAOException e) {
			logger.error("批量添加订单明细出错!");
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加订单明细失败,失败原因:" + e.getMessage());
				syslog.setOpModule("order");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				sysLogService.addSysLogByObj(syslog);
			}
			return false;
		}
	}

	// 分析订单明细集合,生成配件采购分析数据
	public void saveFitAnys(List orderDetails) {
		List listAnys = new ArrayList();
		for (int i = 0; i < orderDetails.size(); i++) {
			CotOrderDetail cotOrderDetail = (CotOrderDetail) orderDetails
					.get(i);
			if (cotOrderDetail.getBoxCount() == null) {
				continue;
			}

			String hql = "select obj from CotOrderFittings obj where obj.orderDetailId="
					+ cotOrderDetail.getId();
			List listEleFits = this.getOrderDao().find(hql);
			for (int j = 0; j < listEleFits.size(); j++) {
				CotOrderFittings eleFittings = (CotOrderFittings) listEleFits
						.get(j);
				CotFittingsAnys fittingsAnys = new CotFittingsAnys();
				fittingsAnys.setBoxCount(cotOrderDetail.getBoxCount()
						.doubleValue());// 订单数量
				fittingsAnys.setEleId(cotOrderDetail.getEleId());// 货号
				fittingsAnys.setEleName(cotOrderDetail.getEleName());// 产品名称
				fittingsAnys.setFacId(eleFittings.getFacId());// 供应商
				fittingsAnys.setFitBuyUnit(eleFittings.getFitUseUnit());// 领用单位==使用单位
				fittingsAnys.setFitDesc(eleFittings.getFitDesc());
				fittingsAnys.setFitName(eleFittings.getFitName());
				fittingsAnys.setFitNo(eleFittings.getFitNo());
				// 单个用量(用量*数量)
				Double count = eleFittings.getFitUsedCount()
						* eleFittings.getFitCount();
				fittingsAnys.setFitUsedCount(eleFittings.getFitUsedCount());
				fittingsAnys.setFitCount(eleFittings.getFitCount());
				fittingsAnys.setFitPrice(eleFittings.getFitPrice());
				Double num = count * cotOrderDetail.getBoxCount();
				fittingsAnys.setOrderFitCount(num);
				fittingsAnys.setTotalAmount(num * eleFittings.getFitPrice());
				fittingsAnys.setOrderId(cotOrderDetail.getOrderId());
				fittingsAnys.setOrderdetailId(cotOrderDetail.getId());
				fittingsAnys.setAnyFlag("U");
				fittingsAnys.setFittingId(eleFittings.getFittingId());

				listAnys.add(fittingsAnys);
			}
		}
		try {
			if (listAnys.size() > 0) {
				this.getOrderDao().saveRecords(listAnys);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 计算包材分析的单价(cau为计算公式)
	// public Double sumPrice(String cau,CotOrderDetail detail,Float mPrice){
	// if(cau==null || "".equals(cau.trim())){
	// return 0.0;
	// }
	// // 格式化数字.保留两位小数
	// DecimalFormat df = new DecimalFormat("#.00");
	// // 定义jeavl对象,用于计算字符串公式
	// Evaluator evaluator = new Evaluator();
	// // 定义公式变量
	// //材料单价
	// if (mPrice != null) {
	// evaluator.putVariable("materialPrice", mPrice.toString());
	// }else{
	// evaluator.putVariable("materialPrice", "0");
	// }
	// //产品长
	// if (detail.getBoxPbL() != null) {
	// evaluator.putVariable("boxPbL", detail.getBoxPbL().toString());
	// }else{
	// evaluator.putVariable("boxPbL", "0");
	// }
	// //产品宽
	// if (detail.getBoxPbW() != null) {
	// evaluator.putVariable("boxPbW", detail.getBoxPbW().toString());
	// }else{
	// evaluator.putVariable("boxPbW", "0");
	// }
	// //产品高
	// if (detail.getBoxPbH() != null) {
	// evaluator.putVariable("boxPbH", detail.getBoxPbH().toString());
	// }else{
	// evaluator.putVariable("boxPbH", "0");
	// }
	// //内盒长
	// if (detail.getBoxIbL() != null) {
	// evaluator.putVariable("boxIbL", detail.getBoxIbL().toString());
	// }else{
	// evaluator.putVariable("boxIbL", "0");
	// }
	// //内盒宽
	// if (detail.getBoxIbW() != null) {
	// evaluator.putVariable("boxIbW", detail.getBoxIbW().toString());
	// }else{
	// evaluator.putVariable("boxIbW", "0");
	// }
	// //内盒高
	// if (detail.getBoxIbH() != null) {
	// evaluator.putVariable("boxIbH", detail.getBoxIbH().toString());
	// }else{
	// evaluator.putVariable("boxIbH", "0");
	// }
	// //中盒长
	// if (detail.getBoxMbL() != null) {
	// evaluator.putVariable("boxMbL", detail.getBoxMbL().toString());
	// }else{
	// evaluator.putVariable("boxMbL", "0");
	// }
	// //中盒宽
	// if (detail.getBoxMbW() != null) {
	// evaluator.putVariable("boxMbW", detail.getBoxMbW().toString());
	// }else{
	// evaluator.putVariable("boxMbW", "0");
	// }
	// //中盒高
	// if (detail.getBoxMbH() != null) {
	// evaluator.putVariable("boxMbH", detail.getBoxMbH().toString());
	// }else{
	// evaluator.putVariable("boxMbH", "0");
	// }
	//		
	// //外箱长
	// if (detail.getBoxObL() != null) {
	// evaluator.putVariable("boxObL", detail.getBoxObL().toString());
	// }else{
	// evaluator.putVariable("boxObL", "0");
	// }
	// //外箱宽
	// if (detail.getBoxObW() != null) {
	// evaluator.putVariable("boxObW", detail.getBoxObW().toString());
	// }else{
	// evaluator.putVariable("boxObW", "0");
	// }
	// //外箱高
	// if (detail.getBoxObH() != null) {
	// evaluator.putVariable("boxObH", detail.getBoxObH().toString());
	// }else{
	// evaluator.putVariable("boxObH", "0");
	// }
	//		
	// try {
	// String result = evaluator.evaluate(cau);
	// return Double.parseDouble(df.format(Double.parseDouble(result)));
	// } catch (EvaluationException e) {
	// e.printStackTrace();
	// return 0.0;
	// }
	// }

	// 根据订单明细,生成包材分析数据,已生成采购的分析数据不再修改
	public List getPackingAnysByOrderDetail(CotOrderDetail detail,
			Integer sizeType) {
		List<CotPackingAnys> list = new ArrayList<CotPackingAnys>();
		if (detail.getId() == null) {
			CotPackingAnys packingAnys = new CotPackingAnys();
			list.add(packingAnys);
		} else {
			// 查找所有该订单明细的包材分析数据(包含新添加,只是不修改新添加的材料名称.单价.规格.厂家)
			String hql = "from CotPackingAnys obj where obj.orderdetailId="
					+ detail.getId() + " and obj.orderId="
					+ detail.getOrderId() + " and obj.boxTypeId=" + sizeType;
			List<CotPackingAnys> temp = this.getOrderDao().find(hql);
			if (temp != null && temp.size() > 0) {
				list.addAll(temp);
			} else {
				// 可能之前订单明细没指定订单数量或者没包装数,没包装类型结果没产生包材分析数据
				// 也可能是已生成的分析数据被删除,当再次修改订单明细时,会重新生成分析数据
				CotPackingAnys packingAnys = new CotPackingAnys();
				list.add(packingAnys);
			}
		}
		List<CotPackingAnys> listNew = new ArrayList<CotPackingAnys>();
		for (int i = 0; i < list.size(); i++) {
			CotPackingAnys packingAnys = (CotPackingAnys) list.get(i);
			// 已生成采购单的不能再修改
			if ("C".equals(packingAnys.getAnyFlag())) {
				continue;
			}
			packingAnys.setEleId(detail.getEleId());// 货号
			packingAnys.setEleName(detail.getEleName());// 样品中文名称
			packingAnys.setCustNo(detail.getCustNo());// 客号
			packingAnys.setOrderCount(detail.getBoxCount());// 订单数量
			packingAnys.setContainerCount(detail.getContainerCount());// 订单箱数
			packingAnys.setBoxPbCount(detail.getBoxPbCount());// 产品包装数
			packingAnys.setBoxIbCount(detail.getBoxIbCount());// 内装数
			packingAnys.setBoxMbCount(detail.getBoxMbCount());// 中装数
			packingAnys.setBoxObCount(detail.getBoxObCount());// 外装数
			packingAnys.setBoxRemark(detail.getBoxRemark());// 包装说明
			packingAnys.setBoxTypeId(sizeType);// 产品0/内1/中2/外3 包材分析
			packingAnys.setOrderId(detail.getOrderId());
			packingAnys.setOrderdetailId(detail.getId());
			packingAnys.setAnyFlag("U");

			if (packingAnys.getFlag() == null) {
				packingAnys.setFlag(0);// 是否是原先订单的数据
			}
			if (sizeType == 0) {
				// 包装数量(自动进一)
				Double count = Math.ceil(detail.getBoxCount().doubleValue()
						/ detail.getBoxPbCount());
				packingAnys.setPackCount(count.longValue());
				// 不修改新添加的材料名称.单价.规格.厂家
				if (packingAnys.getFlag() != 1) {
					// 包装材料
					packingAnys.setBoxPackingId(detail.getBoxPbTypeId());
					// 查询包装材料的单价
					CotBoxPacking boxPacking = (CotBoxPacking) this
							.getOrderDao().getById(CotBoxPacking.class,
									detail.getBoxPbTypeId());
					// 计算单价
					// Double price =
					// this.sumPrice(boxPacking.getFormulaIn(),detail,detail.getBoxPbPrice());
					if (detail.getBoxPbPrice() != null) {
						packingAnys.setPackPrice(detail.getBoxPbPrice()
								.doubleValue());
					} else {
						packingAnys.setPackPrice(0.0);
					}

					if (detail.getBoxPbL() != null) {
						packingAnys.setSizeL(detail.getBoxPbL().doubleValue());
					}
					if (detail.getBoxPbW() != null) {
						packingAnys.setSizeW(detail.getBoxPbW().doubleValue());
					}
					if (detail.getBoxPbH() != null) {
						packingAnys.setSizeH(detail.getBoxPbH().doubleValue());
					}
					packingAnys.setFactoryId(boxPacking.getFacId());
				}
			}
			if (sizeType == 1) {
				Double count = Math.ceil(detail.getBoxCount().doubleValue()
						/ detail.getBoxIbCount());
				packingAnys.setPackCount(count.longValue());
				// 不修改新添加的材料名称.单价.规格.厂家
				if (packingAnys.getFlag() != 1) {
					packingAnys.setBoxPackingId(detail.getBoxIbTypeId());
					CotBoxPacking boxPacking = (CotBoxPacking) this
							.getOrderDao().getById(CotBoxPacking.class,
									detail.getBoxIbTypeId());
					if (detail.getBoxIbPrice() != null) {
						packingAnys.setPackPrice(detail.getBoxIbPrice()
								.doubleValue());
					} else {
						packingAnys.setPackPrice(0.0);
					}
					if (detail.getBoxIbL() != null) {
						packingAnys.setSizeL(detail.getBoxIbL().doubleValue());
					}
					if (detail.getBoxIbW() != null) {
						packingAnys.setSizeW(detail.getBoxIbW().doubleValue());
					}
					if (detail.getBoxIbH() != null) {
						packingAnys.setSizeH(detail.getBoxIbH().doubleValue());
					}
					packingAnys.setFactoryId(boxPacking.getFacId());
				}
			}
			if (sizeType == 2) {
				// 包装数量(自动进一)
				Double count = Math.ceil(detail.getBoxCount().doubleValue()
						/ detail.getBoxMbCount());
				packingAnys.setPackCount(count.longValue());
				// 不修改新添加的材料名称.单价.规格.厂家
				if (packingAnys.getFlag() != 1) {
					packingAnys.setBoxPackingId(detail.getBoxMbTypeId());
					CotBoxPacking boxPacking = (CotBoxPacking) this
							.getOrderDao().getById(CotBoxPacking.class,
									detail.getBoxMbTypeId());
					if (detail.getBoxMbPrice() != null) {
						packingAnys.setPackPrice(detail.getBoxMbPrice()
								.doubleValue());
					} else {
						packingAnys.setPackPrice(0.0);
					}
					if (detail.getBoxMbL() != null) {
						packingAnys.setSizeL(detail.getBoxMbL().doubleValue());
					}
					if (detail.getBoxMbW() != null) {
						packingAnys.setSizeW(detail.getBoxMbW().doubleValue());
					}
					if (detail.getBoxMbH() != null) {
						packingAnys.setSizeH(detail.getBoxMbH().doubleValue());
					}
					packingAnys.setFactoryId(boxPacking.getFacId());
				}
			}
			if (sizeType == 3) {
				packingAnys.setPackCount(detail.getContainerCount());
				// 不修改新添加的材料名称.单价.规格.厂家
				if (packingAnys.getFlag() != 1) {
					packingAnys.setBoxPackingId(detail.getBoxObTypeId());
					CotBoxPacking boxPacking = (CotBoxPacking) this
							.getOrderDao().getById(CotBoxPacking.class,
									detail.getBoxObTypeId());
					if (detail.getBoxObPrice() != null) {
						packingAnys.setPackPrice(detail.getBoxObPrice()
								.doubleValue());
					} else {
						packingAnys.setPackPrice(0.0);
					}
					if (detail.getBoxObL() != null) {
						packingAnys.setSizeL(detail.getBoxObL().doubleValue());
					}
					if (detail.getBoxObW() != null) {
						packingAnys.setSizeW(detail.getBoxObW().doubleValue());
					}
					if (detail.getBoxObH() != null) {
						packingAnys.setSizeH(detail.getBoxObH().doubleValue());
					}
					packingAnys.setFactoryId(boxPacking.getFacId());
				}
			}
			if (sizeType == 4) {
				packingAnys.setPackCount(detail.getContainerCount());
				// 不修改新添加的材料名称.单价.规格.厂家
				if (packingAnys.getFlag() != 1) {
					packingAnys.setBoxPackingId(detail.getInputGridTypeId());
					CotBoxPacking boxPacking = (CotBoxPacking) this
							.getOrderDao().getById(CotBoxPacking.class,
									detail.getInputGridTypeId());
					if (detail.getInputGridPrice() != null) {
						packingAnys.setPackPrice(detail.getInputGridPrice()
								.doubleValue());
					} else {
						packingAnys.setPackPrice(0.0);
					}
					if (detail.getPutL() != null) {
						packingAnys.setSizeL(detail.getPutL().doubleValue());
					}
					if (detail.getPutW() != null) {
						packingAnys.setSizeW(detail.getPutW().doubleValue());
					}
					if (detail.getPutH() != null) {
						packingAnys.setSizeH(detail.getPutH().doubleValue());
					}
					packingAnys.setFactoryId(boxPacking.getFacId());
				}
			}
			listNew.add(packingAnys);
		}

		return listNew;
	}

	// 删除该材料类型的包装分析数据(未生成采购单的)
	@SuppressWarnings("unchecked")
	public List<Integer> deleteAnys(CotOrderDetail detail, Integer sizeType) {
		List<Integer> list = new ArrayList<Integer>();
		if (detail.getId() != null) {
			String hql = "select obj.id from CotPackingAnys obj where obj.anyFlag='U' and obj.orderdetailId="
					+ detail.getId()
					+ " and obj.orderId="
					+ detail.getOrderId() + " and obj.boxTypeId=" + sizeType;
			list = this.getOrderDao().find(hql);
		}
		return list;
	}

	// 分析订单明细集合,生成包材采购分析数据
	@SuppressWarnings("unchecked")
	public void savePackAnys(List orderDetails) {
		// 保存包材分析对象
		List<CotPackingAnys> listAnys = new ArrayList<CotPackingAnys>();
		// 要删除的包材分析对象的编号集合
		List<Integer> delAnysIds = new ArrayList<Integer>();
		for (int i = 0; i < orderDetails.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) orderDetails.get(i);
			// 分析产品包装
			if (detail.getBoxCount() != null && detail.getBoxCount() != 0
					&& detail.getBoxPbCount() != null
					&& detail.getBoxPbCount() != 0
					&& detail.getBoxPbTypeId() != null) {
				listAnys.addAll(this.getPackingAnysByOrderDetail(detail, 0));
			} else {
				// 如果是修改,则删除所有该包装的分析数据(包含新添加的)
				delAnysIds.addAll(this.deleteAnys(detail, 0));
			}
			// 分析内包装
			if (detail.getBoxCount() != null && detail.getBoxCount() != 0
					&& detail.getBoxIbCount() != null
					&& detail.getBoxIbCount() != 0
					&& detail.getBoxIbTypeId() != null) {
				listAnys.addAll(this.getPackingAnysByOrderDetail(detail, 1));
			} else {
				// 如果是修改,则删除所有该包装的分析数据(包含新添加的)
				delAnysIds.addAll(this.deleteAnys(detail, 1));
			}
			// 分析中包装
			if (detail.getBoxCount() != null && detail.getBoxCount() != 0
					&& detail.getBoxMbCount() != null
					&& detail.getBoxMbCount() != 0
					&& detail.getBoxMbTypeId() != null) {
				listAnys.addAll(this.getPackingAnysByOrderDetail(detail, 2));
			} else {
				// 如果是修改,则删除所有该包装的分析数据(包含新添加的)
				delAnysIds.addAll(this.deleteAnys(detail, 2));
			}
			// 分析外包装
			if (detail.getBoxCount() != null && detail.getBoxCount() != 0
					&& detail.getBoxObCount() != null
					&& detail.getBoxObCount() != 0
					&& detail.getBoxObTypeId() != null) {
				listAnys.addAll(this.getPackingAnysByOrderDetail(detail, 3));
			} else {
				// 如果是修改,则删除所有该包装的分析数据(包含新添加的)
				delAnysIds.addAll(this.deleteAnys(detail, 3));
			}
			// 分析插格包装
			if (detail.getBoxCount() != null && detail.getBoxCount() != 0
					&& detail.getBoxObCount() != null
					&& detail.getBoxObCount() != 0
					&& detail.getInputGridTypeId() != null) {
				listAnys.addAll(this.getPackingAnysByOrderDetail(detail, 4));
			} else {
				// 如果是修改,则删除所有该包装的分析数据(包含新添加的)
				delAnysIds.addAll(this.deleteAnys(detail, 4));
			}
		}
		try {
			if (listAnys.size() > 0) {
				this.getOrderDao().saveOrUpdateRecords(listAnys);
			}
			if (delAnysIds.size() > 0) {
				this.getOrderDao().deleteRecordByIds(delAnysIds,
						"CotPackingAnys");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 更新订单的产品明细
	public Boolean modifyOrderDetails(List<?> details) {
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
			this.getOrderDao().updateRecords(details);
			// 生成包材分析数据
			this.savePackAnys(details);
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改订单明细成功");
				syslog.setOpModule("order");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2); // 1:添加 2：修改 3：删除
				sysLogService.addSysLogByObj(syslog);
			}
			return true;
		} catch (Exception e) {
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改订单明细失败,失败原因:" + e.getMessage());
				syslog.setOpModule("order");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2); // 1:添加 2：修改 3：删除
				sysLogService.addSysLogByObj(syslog);
			}
			return false;
		}
	}

	// 根据报价编号查找报价单信息
	public CotOrder getOrderById(Integer id) {
		CotOrder cotOrder = (CotOrder) this.getOrderDao().getById(
				CotOrder.class, id);
		if (cotOrder != null) {
			CotOrder custClone = (CotOrder) SystemUtil.deepClone(cotOrder);
			custClone.setCotOrderDetails(null);
			custClone.setOrderMBImg(null);
			return custClone;
		}
		return null;
	}

	// 根据报价明细编号查找报价明细单信息
	public CotOrderDetail getOrderDetailById(Integer id) {
		CotOrderDetail cotOrderDetail = (CotOrderDetail) this.getOrderDao()
				.getById(CotOrderDetail.class, id);
		cotOrderDetail.setPicImg(null);
		return cotOrderDetail;
	}

	// 判断该主单的明细是否已存在该产品货号
	@SuppressWarnings("unchecked")
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId) {
		List<Integer> res = this.getOrderDao().find(
				"select c.id from CotOrderDetail c where c.orderId = " + mainId
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

	// 根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id) {
		CotCustomer cotCustomer = (CotCustomer) this.getOrderDao().getById(
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

	// 根据客户编号查找客户编号，简称，和联系人
	public String[] getTeCustById(Integer id) {
		String sql = "select obj.id,obj.customerShortName,obj.priContact,obj.customerEmail from CotCustomer obj where obj.id="
				+ id;
		List<?> list = this.getOrderDao().find(sql);
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

	// 更新报价单的产品明细
	public Boolean modifyOrderDetail(CotOrderDetail e, String eleProTime) {
		// // 没选择厂家时,设置厂家为'未定义'
		// if (e.getFactoryId() == null) {
		// String hql = "from CotFactory obj where obj.shortName='未定义'";
		// List<?> list = this.getOrderDao().find(hql);
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
		try {
			this.getOrderDao().update(e);
		} catch (Exception ex) {
			logger.error("保存样品错误!");
			return false;
		}
		return true;
	}

	/**
	 * @Description 批量删除主订单
	 * @param orderIds
	 *            订单编号集合
	 * @return Integer
	 */
	@SuppressWarnings("deprecation")
	public int[] deleteOrders(List<?> orderIds) {
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		int[] temp = new int[6];
		int facNum = 0;
		int recvNum = 0;
		int outNum = 0;
		int fitNum = 0;
		int packNum = 0;
		int canNum = 0;
		String sys = SystemUtil.getRptFilePath();
		try {
			for (int i = 0; i < orderIds.size(); i++) {
				Integer oId = (Integer) orderIds.get(i);
				// 已出货不删除
				String hqlOut = "select obj.id from CotOrderOutdetail obj,CotOrderDetail d where obj.orderDetailId=d.id and d.orderId="
						+ oId;
				List checkOutList = this.getOrderDao().find(hqlOut);
				if (checkOutList.size() > 0) {
					outNum++;
					continue;
				}
				canNum++;

				// 判断pi的单号是否有改变
				String ckNoHql = "select obj.orderNo from CotOrder obj where obj.id="
						+ oId;
				List listNoCk = this.getOrderDao().find(ckNoHql);
				String pldNo = (String) listNoCk.get(0);

				List delList = new ArrayList();
				delList.add(oId);
				this.getOrderDao().deleteRecordByIds(delList, "CotOrder");

				// 删除旧报表
				File file = new File(sys + "order/PI-" + pldNo + ".pdf");
				File fileFac = new File(sys + "orderfac/PO-" + pldNo + ".pdf");
				if (file.exists()) {
					file.delete();
				}
				if (fileFac.exists()) {
					fileFac.delete();
				}
			}
			temp[0] = facNum;
			temp[1] = recvNum;
			temp[2] = outNum;
			temp[3] = fitNum;
			temp[4] = packNum;
			temp[5] = canNum;
			return temp;
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}

	}

	// 删除订单的产品明细
	@SuppressWarnings("deprecation")
	public Boolean deleteOrderDetails(List<?> ids) {
		Integer orderId = 0;
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
			Integer detailId = (Integer) ids.get(i);
			CotOrderDetail cotOrderDetail = (CotOrderDetail) this.getOrderDao()
					.getById(CotOrderDetail.class, detailId);
			orderId = cotOrderDetail.getOrderId();
		}
		try {
			this.getOrderDao().deleteRecordByIds(ids, "CotOrderDetail");
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量删除订单明细成功");
				cotSyslog.setOpModule("order");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(3);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			// 修改主单的总数量,总箱数,总体积,总金额
			this.modifyCotOrderTotal(orderId);
		} catch (DAOException e) {
			logger.error("删除订单的产品明细异常");
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(cotEmps.getId());
				cotSyslog.setOpMessage("批量删除订单明细失败,失败原因:" + e.getMessage());
				cotSyslog.setOpModule("order");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(3);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return false;
		}
		return true;
	}

	// 删除订单的产品明细
	@SuppressWarnings("deprecation")
	public Boolean deleteOrderDetailsAction(HttpServletRequest request,
			List<?> details) {
		CotEmps cotEmps = (CotEmps) request.getSession().getAttribute("emp");
		try {
			// b货号调用历史数据时,如果刚好是同个主单的明细a,如果刚好这条明细需要被删除,
			// 则后台action会先调用删除方法,把a删掉,数据库的外键级联删除明细图片,那么b明细保存时,无法找到引用的a图片
			// 解决方法是,先判断要删除的订单明细是否被其他订单引用,如果有,则暂时不删除该明细,把该明细的type字段设置为'delete',在保存的action中的最后再删除

			// 清除内存数据
			Object temp = SystemUtil.getObjBySession(request.getSession(),
					"order");
			Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) temp;
			// 可删除
			List newDetail = new ArrayList();
			if (orderMap != null) {
				// 1.先查找要删除的ids在内存中的对应对象
				Map<Integer, String> idRdm = new HashMap<Integer, String>();
				// 滞后删除
				List noDetail = new ArrayList();
				for (int i = 0; i < details.size(); i++) {
					Integer id = (Integer) details.get(i);
					boolean flg = false;
					Iterator<?> it = orderMap.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						CotOrderDetail detail = orderMap.get(key);
						if (detail.getId() != null
								&& detail.getId().intValue() == id.intValue()) {
							idRdm.put(detail.getId(), detail.getRdm());
						}
						if (detail.getId() == null
								&& detail.getSrcId().intValue() == id
										.intValue()) {
							flg = true;
						}
					}
					if (flg == false) {
						newDetail.add(id);
					} else {
						noDetail.add(id);
					}
				}
				// 把该明细的type字段设置为'delete'
				for (int i = 0; i < noDetail.size(); i++) {
					Integer id = (Integer) noDetail.get(i);
					String key = idRdm.get(id);
					CotOrderDetail detail = orderMap.get(key);
					detail.setType("delete");
					this.setMapAction(request.getSession(), key, detail);
				}

				for (int i = 0; i < newDetail.size(); i++) {
					Integer id = (Integer) newDetail.get(i);
					Iterator<?> it = orderMap.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						CotOrderDetail detail = orderMap.get(key);
						if (detail.getId() != null
								&& detail.getId().intValue() == id.intValue()) {
							it.remove();
						}
					}
				}
			}
			this.getOrderDao().deleteRecordByIds(newDetail, "CotOrderDetail");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("批量删除订单明细成功");
			cotSyslog.setOpModule("order");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (DAOException e) {
			logger.error("删除订单的产品明细异常");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("批量删除订单明细失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("order");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
		return true;
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getOrderDao().getRecords(objName);
	}

	// 根据主报价单号找报价明细
	public List<?> getDetailByOrderId(Integer orderId) {
		String hql = "from CotOrderDetail obj where obj.orderId=" + orderId;
		return this.getOrderDao().find(hql);
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getOrderDao().getRecords("CotFactory");
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
		List<?> list = this.getOrderDao().getRecords("CotFactory");
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

	// 查询所有客户
	public Map<?, ?> getCusNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderDao().getRecords("CotCustomer");
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
		List<?> list = this.getOrderDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer
					.getCustomerShortName());
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

	// 查询所有付款方式
	public Map<?, ?> getPayTypeMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("paytype");
		for (int i = 0; i < list.size(); i++) {
			CotPayType cotPayType = (CotPayType) list.get(i);
			map.put(cotPayType.getId().toString(), cotPayType.getPayName());
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
		List<?> list = this.getOrderDao().getRecords("CotTypeLv1");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) list.get(i);
			map.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		return map;
	}

	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 查询报价单单号是否存在
	public Integer findIsExistOrderNo(String orderNo, String id) {
		String hql = "select obj.id from CotOrder obj where obj.orderNo='"
				+ orderNo + "'";
		List<?> res = this.getOrderDao().find(hql);
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

	// 删除主报价单的原来图片
	@SuppressWarnings("deprecation")
	public void deleteOrderDetailImg(Integer detailId) {
		CotOrderDetail cotOrderDetail = (CotOrderDetail) this.getOrderDao()
				.getById(CotOrderDetail.class, detailId);
		WebContext ctx = WebContextFactory.get();
		String path = ctx.getHttpServletRequest().getRealPath("/");
		File file = new File(path + cotOrderDetail.getPicPath());
		if (file.exists()) {
			file.delete();
		}
	}

	// 根据条件查询样品记录
	public List<?> getElementList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getOrderDao().findRecords(queryInfo);
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

	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getOrderDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 如果是报价或订单的配件.需要判断该配件厂家或者采购价格是否和配件库一致,如果不同,用配件库的最新数据代替
	public List<?> getNewList(String tableName, List list) {
		String fitPriceNum = ContextUtil.getProperty("remoteaddr.properties",
				"fitPrecision");
		if (tableName.equals("CotPriceFittings")) {
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				CotPriceFittings priceFittings = (CotPriceFittings) it.next();
				String hql = "select obj from CotFittings obj,CotPriceFittings p "
						+ "where p.fittingId=obj.id and p.id="
						+ priceFittings.getId()
						+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,"
						+ fitPriceNum + ")!=p.fitPrice)";
				List disFits = this.getOrderDao().find(hql);
				if (disFits.size() > 0) {
					CotFittings fittings = (CotFittings) disFits.get(0);
					priceFittings.setFacId(fittings.getFacId());
					BigDecimal b = new BigDecimal(fittings.getFitPrice()
							/ fittings.getFitTrans());
					double f1 = b.setScale(Integer.parseInt(fitPriceNum),
							BigDecimal.ROUND_HALF_UP).doubleValue();
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
						+ " and (obj.facId!=p.facId or round(obj.fitPrice/obj.fitTrans,"
						+ fitPriceNum + ")!=p.fitPrice)";
				List disFits = this.getOrderDao().find(hql);
				if (disFits.size() > 0) {
					CotFittings fittings = (CotFittings) disFits.get(0);
					orderFittings.setFacId(fittings.getFacId());
					BigDecimal b = new BigDecimal(fittings.getFitPrice()
							/ fittings.getFitTrans());
					double f1 = b.setScale(Integer.parseInt(fitPriceNum),
							BigDecimal.ROUND_HALF_UP).doubleValue();
					orderFittings.setFitPrice(f1);
				}
			}
		}
		return list;
	}

	// 根据条件查询主单记录
	public List<?> getOrderList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getOrderDao().findRecords(queryInfo,
					CotOrder.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询冲帐明细记录
	public List<?> getRecvDetailList(List<?> list) {
		List<CotFinacerecvDetail> listVo = new ArrayList<CotFinacerecvDetail>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotFinacerecvDetail detail = (CotFinacerecvDetail) obj[0];
			detail.setFinaceNo((String) obj[1]);
			listVo.add(detail);
		}
		return listVo;
	}

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getOrderDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getOrderDao().getRecordsCountJDBC(queryInfo);
	}

	// 根据用户查找他拥有的客户编号
	public String getCustomerIds(Integer empId) {
		// 查找该用户的所有客户
		String hql = "select obj.id from CotCustomer obj where obj.empId="
				+ empId;
		String ids = "";
		List<?> list = this.getOrderDao().find(hql);
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Integer id = (Integer) it.next();
			ids += id + ",";
		}
		return ids;
	}

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId) {
		List<?> list = this.getOrderDao().find(
				"from CotRptFile f where f.rptType=" + rptTypeId);
		return list;
	}

	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价
	@SuppressWarnings("unchecked")
	public CotOrderDetail findDetailByEleId(CotOrderDetail cotOrderDetail,
			Map<?, ?> map, String liRunCau[]) {

		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.000");
		// 币种
		Object currencyIdObj = map.get("currencyId");
		Integer currencyId = 0;
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}

		Float facSum = this.getFacSum();

		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}

		// 取得厂家报价的人民币值
		List<?> listCur = this.getDicList("currency");
		Float rmb = 0f;
		if (cotOrderDetail.getPriceFac() != null
				&& cotOrderDetail.getPriceFacUint() != null) {
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == cotOrderDetail.getPriceFacUint()
						.intValue()) {
					Float rate = cur.getCurRate();
					Float fac = cotOrderDetail.getPriceFac();
					rmb = rate * fac;
					break;
				}
			}
		}
		String cau_Li = "";
		// 价格条款
		Object clauseObj = map.get("clauseTypeId");
		Integer clauseTypeId = 0;
		if (clauseObj != null && !"".equals(clauseObj.toString())) {
			clauseTypeId = Integer.parseInt(clauseObj.toString());
			if (clauseTypeId == 1 || clauseTypeId == 6) {
				cau_Li = liRunCau[0];
			}
			if (clauseTypeId == 3 || clauseTypeId == 7 || clauseTypeId == 8) {
				cau_Li = liRunCau[1];
			}
			if (clauseTypeId == 4) {
				cau_Li = liRunCau[2];
			}
		}
		// FOB费用
		Object priceFobObj = map.get("orderFob");
		Float priceFob = 0f;
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// 利润率
		Float priceProfit = 0f;
		if (cotOrderDetail.getLiRun() != null) {
			priceProfit = cotOrderDetail.getLiRun();
		}
		// Fob价总和
		Float fobSum = this.getFobSumDetail();

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", priceFob.toString());
		evaluator.putVariable("priceProfit", priceProfit.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		evaluator.putVariable("importTax", importTax.toString());
		evaluator.putVariable("facSum", facSum.toString());
		evaluator.putVariable("fobSum", fobSum.toString());
		// 整柜运费
		Object priceChargeObj = map.get("orderCharge");
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
		Float cbm = cotOrderDetail.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getOrderDao().getRecords(
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
		Long boxObCount = cotOrderDetail.getBoxObCount();
		if (boxObCount == null) {
			boxObCount = 0l;
		}
		// 样品数量
		Long boxCount = cotOrderDetail.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
		}
		evaluator.putVariable("priceCharge", priceCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("boxCount", boxCount.toString());
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
		if (cotOrderDetail.getTuiLv() != null) {
			evaluator
					.putVariable("tuiLv", cotOrderDetail.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}
		Double res = 0.0;
		try {
			// 根据选择的价格条款获得价格公式
			String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
					+ clauseTypeId;
			List<String> cauList = this.getOrderDao().find(cauHql);
			if (cauList.size() == 0) {
				return null;
			}
			String cau = cauList.get(0);
			String result = evaluator.evaluate(cau);
			res = Double.parseDouble(df.format(Double.parseDouble(result)));
		} catch (Exception e) {
			res = 0.0;
		}
		cotOrderDetail.setOrderPrice(res);
		// 最新RMB价
		Float rmbOut = res.floatValue();

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
		lirun.putVariable("boxCount", boxCount.toString());
		lirun.putVariable("insureRate", insureRate.toString());
		lirun.putVariable("insureAddRate", insureAddRate.toString());
		lirun.putVariable("importTax", importTax.toString());
		lirun.putVariable("facSum", facSum.toString());
		lirun.putVariable("fobSum", fobSum.toString());
		if (cotOrderDetail.getTuiLv() != null) {
			lirun.putVariable("tuiLv", cotOrderDetail.getTuiLv().toString());
		} else {
			lirun.putVariable("tuiLv", "0");
		}
		float liRes = 0;
		try {
			if (!cau_Li.equals("")) {
				String li = lirun.evaluate(cau_Li);
				liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
				if (liRes <= -1000) {
					liRes = -999;
				}
				if (liRes >= 1000) {
					liRes = 999;
				}
			}
		} catch (Exception e) {
			liRes = 0;
		}
		cotOrderDetail.setLiRun(liRes);
		return cotOrderDetail;

	}

	// 根据报价明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids) {
		try {
			this.getOrderDao().deleteRecordByIds(ids, "CotOrderDetail");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 查询该单的所有报价明细的产品货号
	public String findEleByOrderId(Integer orderId) {
		String hql = "select obj.eleId from CotOrderDetail obj where obj.orderId="
				+ orderId;
		List<?> list = this.getOrderDao().find(hql);
		String eleIds = "";
		for (int i = 0; i < list.size(); i++) {
			String eleId = (String) list.get(i);
			eleIds += eleId + ",";
		}
		return eleIds;
	}

	// 计算所有生产价*数量的总和 (rmb总和)
	public Float getFacSum() {
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
		Float facSum = 0f;
		if (orderMap == null) {
			return 0f;
		}
		Iterator<?> it = orderMap.keySet().iterator();
		while (it.hasNext()) {
			CotOrderDetail detail = orderMap.get(it.next());
			long boxCount = 0;
			if (detail.getBoxCount() != null) {
				boxCount = detail.getBoxCount();
			}
			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			if (detail.getPriceFac() != null
					&& detail.getPriceFacUint() != null) {
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == detail.getPriceFacUint()
							.intValue()) {
						Float rate = cur.getCurRate();
						Float fac = detail.getPriceFac();
						facSum += rate * fac * boxCount;
						break;
					}
				}
			}
		}
		return facSum;
	}

	// 计算FOB价总和--用明细的利润率
	public Float getFobSumDetail() {
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
		Float fobSum = 0f;
		if (orderMap == null) {
			return 0f;
		}
		Iterator<?> it = orderMap.keySet().iterator();
		while (it.hasNext()) {
			CotOrderDetail detail = orderMap.get(it.next());
			long boxCount = 0;
			if (detail.getBoxCount() != null) {
				boxCount = detail.getBoxCount();
			}
			// 生产价/汇率*(1+利润率/100)
			List<?> listCur = this.getDicList("currency");
			if (detail.getPriceFac() != null
					&& detail.getPriceFacUint() != null) {
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == detail.getPriceFacUint()
							.intValue()) {
						Float rate = cur.getCurRate();
						Float fac = detail.getPriceFac();
						Float priceProfit = detail.getLiRun();
						if (priceProfit == null) {
							priceProfit = 0f;
						}
						fobSum += (rate * fac * (1 + priceProfit / 100))
								* boxCount;
						break;
					}
				}
			}
		}
		return fobSum;
	}

	// 计算FOB价总和--用主单的利润率
	// public Float getFobSum(Float priceRate, Float priceProfit) {
	// WebContext ctx = WebContextFactory.get();
	// HttpSession session = ctx.getSession();
	// Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
	// Float fobSum = 0f;
	// if(orderMap==null){
	// return 0f;
	// }
	// Iterator<?> it = orderMap.keySet().iterator();
	// while (it.hasNext()) {
	// CotOrderDetail detail = orderMap.get(it.next());
	// long boxCount = 0;
	// if (detail.getBoxCount() != null) {
	// boxCount = detail.getBoxCount();
	// }
	// // 生产价/汇率*(1+利润率/100)
	// List<?> listCur = this.getDicList("currency");
	// if (detail.getPriceFac() != null
	// && detail.getPriceFacUint() != null) {
	// for (int j = 0; j < listCur.size(); j++) {
	// CotCurrency cur = (CotCurrency) listCur.get(j);
	// if (cur.getId().intValue() == detail.getPriceFacUint()
	// .intValue()) {
	// Float rate = cur.getCurRate();
	// Float fac = detail.getPriceFac();
	// fobSum += (rate * fac * (1 + priceProfit / 100))
	// * boxCount;
	// break;
	// }
	// }
	// }
	// }
	// return fobSum;
	// }

	// 计算平均利润率
	public Double sumAverageProfit(Double orderRate, Integer clauseTypeId) {
		Double averageProfit = 0.0;
		Float totalFac = this.getFacSum();
		if (totalFac == null || totalFac == 0) {
			return 0.0;
		}
		// FOB/Ex Works: (总销售价 – 总采购价)/总采购价
		// CIF/DDP/DAP/CFR: (总FOB销售价 – 总采购价)/总采购价
		//DDP:(总销售价 – （总采购价+海运费）*（1+进口关税）)/（（总采购价+海运费）*（1+进口关税））(js 实现)
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
		if (orderMap == null) {
			return 0.0;
		}
		Double total = 0.0;
		Double totalSales = 0.0;
		Integer orderId = 0;
		for (String key : orderMap.keySet()) {
			CotOrderDetail detail = orderMap.get(key);
			total += detail.getTotalMoney();
			orderId = detail.getOrderId();
		}
		CotOrder cotOrder = (CotOrder) this.getOrderDao().getById(
				CotOrder.class, orderId);
		totalSales = orderRate * total;
		if (clauseTypeId == 1 || clauseTypeId == 6) {
			averageProfit = (totalSales - totalFac) / totalFac;
		}else if(clauseTypeId == 4){
			double importTax = cotOrder.getImportTax() == null?0:Double.parseDouble(cotOrder.getImportTax());
			double totalFacPrice = (totalFac+cotOrder.getOrderCharge())*(1+importTax/100.0);
			averageProfit = (totalSales - totalFacPrice)/totalFacPrice;
		} else {
			Float totalFob = this.getFobSumDetail();
			averageProfit = (totalFob - totalFac) / totalFac.doubleValue();
		}
		return averageProfit;
	}

	// 计算美元的总利润
	public Double sumTotalProfit(Double rate) {
		// (总FOB销售价 – 总采购价)
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
		if (orderMap == null || rate == null || rate == 0) {
			return 0.0;
		}
		Float totalFac = this.getFacSum();
		Float totalFob = this.getFobSumDetail();
		Float averageProfit = totalFob - totalFac;
		// return averageProfit/rate;
		return averageProfit.doubleValue();
	}

	// 根据要更改的产品货号字符串 和 价格条件更改单价,再返回这组单价
	@SuppressWarnings("unchecked")
	public Map<String, Float[]> getNewOrder(String rdmIds, Map<?, ?> map,
			String liRunCau[]) {
		// 修改内存报价对象的值
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
		Float facSum = this.getFacSum();

		String[] rdmAry = rdmIds.split(",");
		Map<String, Float[]> newMap = new HashMap<String, Float[]>();
		if (rdmAry.length == 0) {
			return null;
		}
		List list = new ArrayList();
		for (int i = 0; i < rdmAry.length; i++) {
			CotOrderDetail cotOrderDetail = orderMap.get(rdmAry[i]
					.toLowerCase());
			if (cotOrderDetail != null) {
				list.add(cotOrderDetail);
			}
		}
		String cau_Li = "";
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseTypeId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
			if (clauseId == 1 || clauseId == 6) {
				cau_Li = liRunCau[0];
			}
			if (clauseId == 3 || clauseId == 7 || clauseId == 8) {
				cau_Li = liRunCau[1];
			}
			if (clauseId == 4 || clauseId == 9) {
				cau_Li = liRunCau[2];
			}
		}

		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("orderFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
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
		// Fob价总和
		Float fobSum = this.getFobSumDetail();
		// 整柜运费
		Float priceCharge = 0f;
		Object priceChargeObj = map.get("orderCharge");
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
		List<String> cauList = this.getOrderDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = null;
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail orderDetail = (CotOrderDetail) list.get(i);

			// 利润率
			Float priceProfit = 0f;
			if (orderDetail.getLiRun() != null) {
				priceProfit = orderDetail.getLiRun();
			}

			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			Float rmb = 0f;
			if (orderDetail.getPriceFac() != null
					&& orderDetail.getPriceFacUint() != null) {
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == orderDetail.getPriceFacUint()
							.intValue()) {
						Float rate = cur.getCurRate();
						Float fac = orderDetail.getPriceFac();
						rmb = rate * fac;
						break;
					}
				}
			}
			evaluator = new Evaluator();
			// 定义FOB公式的变量
			evaluator.putVariable("rmb", rmb.toString());
			evaluator.putVariable("priceFob", priceFob.toString());
			if (orderDetail.getTuiLv() != null) {
				evaluator.putVariable("tuiLv", orderDetail.getTuiLv()
						.toString());
			} else {
				evaluator.putVariable("tuiLv", "0");
			}
			evaluator.putVariable("priceProfit", priceProfit.toString());
			evaluator.putVariable("importTax", importTax.toString());
			evaluator.putVariable("facSum", facSum.toString());
			evaluator.putVariable("fobSum", fobSum.toString());
			evaluator.putVariable("priceRate", priceRate.toString());

			// 样品CBM
			Float cbm = orderDetail.getBoxCbm();
			if (cbm == null) {
				cbm = 0f;
			}
			// 柜体积
			Float cube = 0f;
			// List<?> containerList = this.getDicList("container");
			List<?> containerList = this.getOrderDao().getRecords(
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
			Long boxObCount = orderDetail.getBoxObCount();
			if (boxObCount == null) {
				boxObCount = 0l;
			}
			// 样品数量
			Long boxCount = orderDetail.getBoxCount();
			if (boxCount == null) {
				boxCount = 0l;
			}
			evaluator.putVariable("priceCharge", priceCharge.toString());
			evaluator.putVariable("cbm", cbm.toString());
			evaluator.putVariable("cube", cube.toString());
			evaluator.putVariable("boxObCount", boxObCount.toString());
			evaluator.putVariable("boxCount", boxCount.toString());
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

			// 修改内存map
			CotOrderDetail cotOrderDetail = orderMap.get(orderDetail.getRdm());
			cotOrderDetail.setOrderPrice(Double.parseDouble(df.format(res)));
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
			lirun.putVariable("boxCount", boxCount.toString());
			lirun.putVariable("insureRate", insureRate.toString());
			lirun.putVariable("insureAddRate", insureAddRate.toString());
			lirun.putVariable("importTax", importTax.toString());
			lirun.putVariable("facSum", facSum.toString());
			lirun.putVariable("fobSum", fobSum.toString());

			if (cotOrderDetail.getTuiLv() != null) {
				lirun
						.putVariable("tuiLv", cotOrderDetail.getTuiLv()
								.toString());
			} else {
				lirun.putVariable("tuiLv", "0");
			}
			float liRes = 0;
			try {
				if (!cau_Li.equals("")) {
					String li = lirun.evaluate(cau_Li);
					liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
					if (liRes <= -1000) {
						liRes = -999;
					}
					if (liRes >= 1000) {
						liRes = 999;
					}
					cotOrderDetail.setLiRun(Float.parseFloat(df.format(liRes)));
				}
			} catch (Exception e) {
				e.printStackTrace();
				liRes = 0;
			}
			op[1] = liRes;
			newMap.put(orderDetail.getRdm(), op);
			orderMap.put(orderDetail.getRdm(), cotOrderDetail);
			SystemUtil.setObjBySession(session, orderMap, "order");
		}
		return newMap;
	}

	// 币种换算(修改当前页的价格)
	public Map<String, Float[]> changePrice(Map<String, CotOrderDetail> map,
			String eleIds, Integer currencyId, Integer oldCur) {
		String[] eleAry = eleIds.split(",");
		Map<String, Float[]> newMap = new HashMap<String, Float[]>();
		for (int i = 0; i < eleAry.length; i++) {
			Float[] op = new Float[2];
			// 修改内存map
			CotOrderDetail cotOrderDetail = map.get(eleAry[i].toLowerCase());
			Double priceDouble = this.updatePrice(cotOrderDetail.getOrderPrice().floatValue(), oldCur,
					currencyId);
			op[0] = priceDouble.floatValue();
			cotOrderDetail.setOrderPrice(priceDouble);
			map.put(eleAry[i].toLowerCase(), cotOrderDetail);
			op[1] = null;
			newMap.put(cotOrderDetail.getEleId(), op);
		}
		return newMap;
	}

	// 根据币种换算价格
	public Double updatePrice(Float price, Integer oldCurId, Integer newCurId) {
		if (price == null || oldCurId == null) {
			return 0.0;
		}
		// 先根据该的币种的汇率转成人民币,
		CotCurrency oldCur = (CotCurrency) this.getOrderDao().getById(
				CotCurrency.class, oldCurId);
		Float rmb = price.floatValue() * oldCur.getCurRate();
		// 在根据币种的汇率转成该币种的值
		CotCurrency newCur = (CotCurrency) this.getOrderDao().getById(
				CotCurrency.class, newCurId);
		Float obj = rmb / newCur.getCurRate();

		DecimalFormat nbf = new DecimalFormat("0.000");
		return Double.parseDouble(nbf.format(obj));
	}

	// 根据要更改的产品货号字符串 和 价格条件更改单价,再返回这组单价
	@SuppressWarnings("unchecked")
	public Map<String, Float[]> getNewOrderCurreny(String eleIds,
			Map<?, ?> map, String liRunCau, Integer oldCur) {
		// 修改内存报价对象的值
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getSession();
		Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
		String[] eleAry = eleIds.split(",");
		Map<String, Float[]> newMap = new HashMap<String, Float[]>();
		if (eleAry.length == 0) {
			return null;
		}
		List list = new ArrayList();
		for (int i = 0; i < eleAry.length; i++) {
			CotOrderDetail cotOrderDetail = orderMap.get(eleAry[i]
					.toLowerCase());
			if (cotOrderDetail != null) {
				list.add(cotOrderDetail);
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
		Object clauseIdObj = map.get("clauseTypeId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		} else {
			// 币种换算(修改当前页的价格)
			return changePrice(orderMap, eleIds, currencyId, oldCur);
		}
		// 利润率
		Float priceProfit = 0f;
		Object priceProfitObj = map.get("orderProfit");
		if (priceProfitObj != null && !"".equals(priceProfitObj.toString())) {
			priceProfit = Float.parseFloat(priceProfitObj.toString());
		}
		// FOB费用
		Float priceFob = 0f;
		Object priceFobObj = map.get("orderFob");
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
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
		Object priceChargeObj = map.get("orderCharge");
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
		List<String> cauList = this.getOrderDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = null;
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail orderDetail = (CotOrderDetail) list.get(i);
			// 取得厂家报价的人民币值
			List<?> listCur = this.getDicList("currency");
			Float rmb = 0f;
			if (orderDetail.getPriceFac() != null
					&& orderDetail.getPriceFacUint() != null) {
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == orderDetail.getPriceFacUint()
							.intValue()) {
						Float rate = cur.getCurRate();
						Float fac = orderDetail.getPriceFac();
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
			// 样品CBM
			Float cbm = orderDetail.getBoxCbm();
			if (cbm == null) {
				cbm = 0f;
			}
			// 柜体积
			Float cube = 0f;
			// List<?> containerList = this.getDicList("container");
			List<?> containerList = this.getOrderDao().getRecords(
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
			// 修改内存map
			CotOrderDetail cotOrderDetail = orderMap.get(orderDetail.getEleId()
					.toLowerCase());
			// 装箱率
			Long boxObCount = orderDetail.getBoxObCount();
			if (boxObCount == null) {
				boxObCount = 0l;
			}
			// 样品数量
			Long boxCount = orderDetail.getBoxCount();
			if (boxCount == null) {
				boxCount = 0l;
			}
			evaluator.putVariable("priceCharge", priceCharge.toString());
			evaluator.putVariable("cbm", cbm.toString());
			evaluator.putVariable("cube", cube.toString());
			evaluator.putVariable("boxObCount", boxObCount.toString());
			evaluator.putVariable("boxCount", boxCount.toString());
			evaluator.putVariable("insureRate", insureRate.toString());
			evaluator.putVariable("insureAddRate", insureAddRate.toString());
			if (cotOrderDetail.getTuiLv() != null) {
				evaluator.putVariable("tuiLv", cotOrderDetail.getTuiLv()
						.toString());
			} else {
				evaluator.putVariable("tuiLv", "0");
			}
			Float[] op = new Float[2];
			Double res = 0.0;
			try {
				String result = evaluator.evaluate(cau);
				res = Double.parseDouble(df.format(Double.parseDouble(result)));
			} catch (Exception e) {
				res = 0.0;
			}
			op[0] = res.floatValue();
			cotOrderDetail.setOrderPrice(Double.parseDouble(df.format(res)));
			// 计算利润率
			Evaluator lirun = new Evaluator();
			// 将新价格转成RMB
			Float rmbNow = res.floatValue();
			lirun.putVariable("price", rmbNow.toString());
			lirun.putVariable("rmb", rmb.toString());
			lirun.putVariable("priceFob", priceFob.toString());
			lirun.putVariable("priceProfit", priceProfit.toString());
			lirun.putVariable("priceRate", priceRate.toString());
			lirun.putVariable("priceCharge", priceCharge.toString());
			lirun.putVariable("cbm", cbm.toString());
			lirun.putVariable("cube", cube.toString());
			lirun.putVariable("boxObCount", boxObCount.toString());
			lirun.putVariable("boxCount", boxCount.toString());
			lirun.putVariable("insureRate", insureRate.toString());
			lirun.putVariable("insureAddRate", insureAddRate.toString());
			if (cotOrderDetail.getTuiLv() != null) {
				lirun
						.putVariable("tuiLv", cotOrderDetail.getTuiLv()
								.toString());
			} else {
				lirun.putVariable("tuiLv", "0");
			}
			Float liRes = 0f;
			try {
				String li = lirun.evaluate(liRunCau);
				liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
				if (liRes <= -1000) {
					liRes = -999f;
				}
				if (liRes >= 1000) {
					liRes = 999f;
				}
				cotOrderDetail.setLiRun(Float.parseFloat(df.format(liRes)));
			} catch (Exception e) {
				liRes = 0f;
			}
			op[1] = liRes;
			newMap.put(orderDetail.getEleId(), op);
			orderMap.put(orderDetail.getEleId().toLowerCase(), cotOrderDetail);
			SystemUtil.setObjBySession(session, orderMap, "order");
		}
		return newMap;

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
		List<?> listGiven = this.getOrderDao().queryForLists(hqlGiven, obj);

		// 报价和订单多一个价格条款条件
		Object[] obj2 = new Object[4];
		obj2[0] = new Integer(cusId);
		obj2[1] = eleId;
		String str = " and p.clauseId=?";
		if ("".equals(clauseId)) {
			obj2[2] = new Integer(0);
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
		List<?> list = this.getOrderDao().queryForLists(hql, obj2);
		// 订单
		Object[] obj3 = new Object[4];
		obj3[0] = new Integer(cusId);
		obj3[1] = eleId;
		String str2 = " and p.clauseTypeId=?";
		if ("".equals(clauseId)) {
			obj3[2] = new Integer(0);
			str = " and p.clauseTypeId is null and 0=?";
		} else {
			obj3[2] = new Integer(clauseId);
		}
		obj3[3] = ptimeDate;
		String hqlOrder = "select obj,p.orderTime from CotOrder p,CotOrderDetail obj,CotCustomer c "
				+ "where obj.orderId=p.id and p.custId=c.id and c.id=?"
				+ " and obj.eleId=?"
				+ str2
				+ " and p.orderTime<=?"
				+ " order by p.orderTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listOrder = this.getOrderDao().queryForLists(hqlOrder, obj3);

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
			// // 填充厂家简称
			// List<?> facList = this.getOrderDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 1;
			rtn[1] = detail;
			return rtn;
		} else if (flag == 2) {
			Object[] objTemp = (Object[]) listOrder.get(0);
			CotOrderDetail detail = (CotOrderDetail) objTemp[0];
			detail.setPicImg(null);
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getOrderDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 2;
			rtn[1] = detail;
			return rtn;
		} else {
			Object[] objTemp = (Object[]) listGiven.get(0);
			CotGivenDetail detail = (CotGivenDetail) objTemp[0];
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getOrderDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 3;
			rtn[1] = detail;
			return rtn;
		}

	}

	// 判断该产品货号是否存在,并且返回该产品的默认图片
	public Integer findIsExistEleByEleId(String eleId) {
		String hql = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List<?> list = this.getOrderDao().find(hql);
		if (list.size() != 0) {
			return (Integer) list.get(0);
		} else {
			return null;
		}
	}

	// 根据产品货号查找对外报价
	public Float findPriceOutByEleId(String eleId) {
		String hql = "select e.priceOut from CotElementsNew e where lower(e.eleId)='"
				+ eleId.toLowerCase() + "'";
		List<?> list = this.getOrderDao().find(hql);
		if (list.size() != 0) {
			return (Float) list.get(0);
		} else {
			return null;
		}
	}

	// 判断同一张单中是否存在相同图片名称
	public Boolean findIsExistPicInOrder(Integer orderId, String picName) {
		String hql = "select obj.id from CotOrderDetail obj where obj.orderId="
				+ orderId + " and obj.picName='" + picName + "'";
		List<?> list = this.getOrderDao().find(hql);
		if (list.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	// 根据更改图片路径
	public void modifyPicPathByPriceId(Integer detailId, String priceNo,
			String picName) {
		CotOrderDetail cotOrderDetail = (CotOrderDetail) this.getOrderDao()
				.getById(CotOrderDetail.class, detailId);
		cotOrderDetail.setPicName(picName);
		cotOrderDetail.setPicPath("sampleImg/order/" + priceNo + "/" + picName
				+ ".png");
		this.getOrderDao().update(cotOrderDetail);
	}

	// 查询订单的总金额
	public Float findTotalMoney(Integer orderId) {
		String hql = "select sum(obj.totalMoney) from CotOrderDetail obj where obj.orderId="
				+ orderId;
		List<?> list = this.getOrderDao().find(hql);
		return (Float) list.get(0);
	}

	// 修改主单的总数量,总箱数,总体积,总金额
	public void modifyCotOrderTotal(Integer orderId) {
		// 获得登录人
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		// 查找主单号对象,修改总数量,总箱数,总体积,订单总金额
		CotOrder cotOrder = (CotOrder) this.getOrderDao().getById(
				CotOrder.class, orderId);
		float totalMoney = 0.0f;
		Integer totalCount = 0;
		Integer totalContainer = 0;
		double totalCBM = 0.0;
		String hql = "from CotOrderDetail obj where obj.orderId = " + orderId;
		List<?> details = this.getOrderDao().find(hql);
		Iterator<?> it = details.iterator();
		while (it.hasNext()) {
			CotOrderDetail detail = (CotOrderDetail) it.next();
			if (detail.getTotalMoney() == null) {
				detail.setTotalMoney(0.0);
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
			totalMoney += detail.getTotalMoney();
			totalCount += detail.getBoxCount().intValue();
			totalContainer += detail.getContainerCount().intValue();
			totalCBM += detail.getTotalCbm();
		}
		try {
			cotOrder.setTotalCBM(totalCBM);
			cotOrder.setTotalContainer(totalContainer);
			cotOrder.setTotalCount(totalCount);
			cotOrder.setTotalMoney(Double.valueOf(totalMoney));
			this.getOrderDao().update(cotOrder);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("修改主订单成功");
			syslog.setOpModule("order");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setItemNo(cotOrder.getOrderNo());
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			sysLogService.addSysLogByObj(syslog);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存订单总额出错!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("修改主订单失败,失败原因:" + e.getMessage());
			syslog.setOpModule("order");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setItemNo(cotOrder.getOrderNo());
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			sysLogService.addSysLogByObj(syslog);
		}
	}

	// 修改主单的总数量,总箱数,总体积,总金额
	public Float[] modifyCotOrderTotalAction(Integer orderId) {
		WebContext ctx = WebContextFactory.get();
		// 获得登录人
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		// 查找主单号对象,修改总数量,总箱数,总体积,订单总金额
		CotOrder cotOrder = (CotOrder) this.getOrderDao().getById(
				CotOrder.class, orderId);
		BigDecimal totalMoney = new BigDecimal("0.0");
		Integer totalCount = 0;
		Integer totalContainer = 0;
		Float totalCBM = 0.0f;
		Float totalGross = 0.0f;
		String hql = "from CotOrderDetail obj where obj.orderId = " + orderId;
		List<?> details = this.getOrderDao().find(hql);
		Iterator<?> it = details.iterator();
		while (it.hasNext()) {
			CotOrderDetail detail = (CotOrderDetail) it.next();
			if (detail.getTotalMoney() == null) {
				detail.setTotalMoney(0.0);
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
			totalMoney = totalMoney.add(new BigDecimal(detail.getTotalMoney()
					.toString()));
			totalCount += detail.getBoxCount().intValue();
			totalContainer += detail.getContainerCount().intValue();
			totalCBM += detail.getTotalCbm();
			totalGross += detail.getTotalGrossWeigth();
		}
		try {
			// 计算平均利润率
			Double average = this.sumAverageProfit(cotOrder.getOrderRate(),
					cotOrder.getClauseTypeId());
			Double totalProfit = this.sumTotalProfit(cotOrder.getOrderRate());
			cotOrder.setTotalCBM(totalCBM.doubleValue());
			cotOrder.setTotalGross(totalGross.doubleValue());
			cotOrder.setTotalContainer(totalContainer);
			cotOrder.setTotalCount(totalCount);
			cotOrder.setTotalMoney(totalMoney.doubleValue());// 货款金额
			cotOrder.setAverageProfit(average * 100);
			cotOrder.setTotalProfit(totalProfit);
			// 计算实际金额
			Double[] result = updateOrderTotalMoney(ctx, cotOrder);

			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("修改主订单成功");
			syslog.setOpModule("order");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setItemNo(cotOrder.getOrderNo());
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			sysLogService.addSysLogByObj(syslog);
			// 返回总金额和总体积
			Float[] temp = new Float[5];
			temp[0] = result[0].floatValue();
			temp[1] = totalCBM;
			temp[2] = result[1].floatValue();
			temp[3] = totalGross;
			temp[4] = average.floatValue();
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存订单总额出错!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("修改主订单失败,失败原因:" + e.getMessage());
			syslog.setOpModule("order");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setItemNo(cotOrder.getOrderNo());
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			sysLogService.addSysLogByObj(syslog);
			return null;
		}
	}

	// 更新订单总金额
	public Double[] updateOrderTotalMoney(WebContext ctx, CotOrder order) {
		CotOrder cotOrder = (CotOrder) SystemUtil.deepClone(order);
		cotOrder.setCotOrderDetails(null);
		// 折扣
		Float zheMoney = 0f;
		// 判断是按单价还是总额折扣，按单价折扣无需参与计算。按总金额打折才参与计算，折扣=货款金额*（1-折扣比例/100)
		if (cotOrder.getZheType() == 2) {
			if (cotOrder.getZheNum() != null) {
				zheMoney = order.getTotalMoney().floatValue()
						* (1 - cotOrder.getZheNum() * 0.01f);
			}
		}

		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute("sysdic");
		List<?> list = (List<?>) dicMap.get("currency");

		// 查询该订单的所有其他费用
		String hql = "from CotFinaceOther obj where obj.source='order' and obj.fkId="
				+ cotOrder.getId();
		List<?> details = this.getOrderDao().find(hql);
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
		float cuRateOrder = 0f;
		for (int j = 0; j < list.size(); j++) {
			CotCurrency currency = (CotCurrency) list.get(j);
			if (currency.getId().intValue() == cotOrder.getCurrencyId()
					.intValue()) {
				cuRateOrder = currency.getCurRate();
				break;
			}
		}
		Double totalMoney = cotOrder.getTotalMoney() - zheMoney;

		cotOrder.setTotalMoney(totalMoney);
		// 实际金额=货款金额-折扣+加减费用
		Double realMoney = totalMoney + allMoney / cuRateOrder;
		cotOrder.setRealMoney(realMoney.floatValue());
		List<CotOrder> listOrder = new ArrayList<CotOrder>();
		listOrder.add(cotOrder);
		this.getOrderDao().updateRecords(listOrder);
		Double[] temp = new Double[2];
		temp[0] = totalMoney;
		temp[1] = realMoney;
		return temp;
	}

	// 得到加减费用总和
	public String getRealMoney(Integer orderId, Integer currencyId) {
		WebContext ctx = WebContextFactory.get();
		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute("sysdic");
		List<?> list = (List<?>) dicMap.get("currency");

		// 查询该订单的所有其他费用
		BigDecimal allMoney = new BigDecimal("0");
		if (orderId != null && currencyId != null) {
			String hql = "from CotFinaceOther obj where obj.source='order' and obj.fkId="
					+ orderId;
			List<?> details = this.getOrderDao().find(hql);
			for (int i = 0; i < details.size(); i++) {
				CotFinaceOther finaceOther = (CotFinaceOther) details.get(i);
				float cuRate = 0f;
				for (int j = 0; j < list.size(); j++) {
					CotCurrency currency = (CotCurrency) list.get(j);
					if (currency.getId().intValue() == finaceOther
							.getCurrencyId().intValue()) {
						cuRate = currency.getCurRate();
						break;
					}
				}

				if (finaceOther.getFlag().equals("A")) {
					Double tb = finaceOther.getAmount() * cuRate;
					allMoney = allMoney.add(new BigDecimal(tb.toString()));
				}
				if (finaceOther.getFlag().equals("M")) {
					Double tb = finaceOther.getAmount() * cuRate;
					allMoney = allMoney.subtract(new BigDecimal(tb.toString()));
				}
			}
			Float cuRateOrder = 0f;
			for (int j = 0; j < list.size(); j++) {
				CotCurrency currency = (CotCurrency) list.get(j);
				if (currency.getId().intValue() == currencyId.intValue()) {
					cuRateOrder = currency.getCurRate();
					break;
				}
			}
			allMoney = allMoney.divide(new BigDecimal(cuRateOrder.toString()),
					2, BigDecimal.ROUND_HALF_UP);
		}
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(allMoney.doubleValue());
	}

	// 根据产品货号和客户编号查找该产品对该客户的历史报价
	@SuppressWarnings("unchecked")
	public CotNewPriceVO findNewPriceVO(String eleId, Integer cusId) {
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.00");
		CotNewPriceVO cotNewPriceVO = new CotNewPriceVO();
		cotNewPriceVO.setEleId(eleId);
		// 查找最近报价
		String newHql = "select d.orderPrice from CotOrder obj,CotOrderDetail "
				+ "d where obj.id=d.orderId and " + "obj.custId=" + cusId
				+ " and d.eleId='" + eleId
				+ "' order by obj.orderTime desc limit 0,1";
		List<Float> newList = this.getOrderDao().find(newHql);
		if (newList.size() != 0) {
			Float newPrice = newList.get(0);
			cotNewPriceVO.setNewPrice(newPrice);
		} else {
			cotNewPriceVO.setNewPrice(0.0f);
		}

		// 查找平均报价
		String avgHql = "select avg(d.orderPrice) from CotOrder obj,CotOrderDetail "
				+ "d where obj.id=d.orderId and "
				+ "obj.custId="
				+ cusId
				+ " and d.eleId='" + eleId + "'";
		List<Float> avgList = this.getOrderDao().find(avgHql);
		Float avgPrice = avgList.get(0);
		if (avgPrice == null) {
			cotNewPriceVO.setAvgPrice(0.0f);
		} else {
			cotNewPriceVO.setAvgPrice(Float.parseFloat(df.format(avgPrice)));
		}

		// 查找最低和最高报价
		String hql = "select min(d.orderPrice),max(d.orderPrice) from CotOrder obj,CotOrderDetail "
				+ "d where obj.id=d.orderId and "
				+ "obj.custId="
				+ cusId
				+ " and d.eleId='" + eleId + "'";
		List<?> list = this.getOrderDao().find(hql);
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

	// 根据同步选择项同步更新样品
	public CotElementsNew setEleByTong(CotElementsNew old,
			CotOrderDetail newEle, String eleStr, String boxStr, String otherStr) {
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
					old.setPriceOut(newEle.getOrderPrice().floatValue());
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
		old.setChilds(null);
		old.setCotFile(null);
		old.setCotPictures(null);
		old.setCotPriceFacs(null);
		old.setCotEleFittings(null);
		old.setCotElePrice(null);
		old.setCotElePacking(null);
		return old;
	}

	// 同步相同的样品的图片
	public Object[] getPriceByEle(String rdm, CotElePic cotElePic) {
		Object[] obj = new Object[2];
		CotOrderDetail orderDetail = (CotOrderDetail) this
				.getOrderMapValue(rdm);
		obj[0] = orderDetail;
		CotElePic pic = null;
		if (orderDetail.getType() == null) {
			String hql = "from CotOrderPic obj where obj.fkId="
					+ orderDetail.getId();
			List list = this.getOrderDao().find(hql);
			CotOrderPic cotOrderPic = (CotOrderPic) list.get(0);
			pic = cotElePic;
			pic.setEleId(cotOrderPic.getEleId());
			pic.setPicImg(cotOrderPic.getPicImg());
			pic.setPicName(cotOrderPic.getPicName());
			pic.setPicSize(cotOrderPic.getPicImg().length);
		} else {
			String type = orderDetail.getType();
			Integer srcId = orderDetail.getSrcId();
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
			List<?> elePicList = this.getOrderDao().find(elePichql);

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
						(CotOrderDetail) temp[0], eleStr, boxStr, otherStr);
				listTemp.add(ele);
				listPic.add((CotElePic) temp[1]);
			}

			this.getOrderDao().updateRecords(listTemp);
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
				this.getOrderDao().updateRecords(listPic);
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
						(CotOrderDetail) temp[0], eleStr, boxStr, otherStr);
				if (ele.getEleFlag() == null) {
					ele.setEleFlag(0l);
				}
				listTemp.add(ele);
			}

			try {
				this.getOrderDao().saveRecords(listTemp);
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
				this.getOrderDao().saveRecords(listPic);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}

	// 根据样品货号字符串查询明细
	// public void updateToEle(String[] same, String[] sameRdm, String[] dis,
	// String[] disRdm, boolean tongBase, boolean tongBox,
	// boolean tongPrice) {
	// // 相同时处理
	// if (same != null) {
	// String t = "";
	// // key为eleId,value为随机数
	// Map<String, String> sameMap = new HashMap<String, String>();
	// for (int i = 0; i < same.length; i++) {
	// sameMap.put(same[i], sameRdm[i]);
	// t += "'" + same[i] + "',";
	// }
	// // 获得样品和图片集合
	// String elePichql = "select e,e.eleId,obj from CotElementsNew e,CotElePic
	// obj where obj.fkId=e.id and e.eleId in ("
	// + t.substring(0, t.length() - 1) + ")";
	// List<?> elePicList = this.getOrderDao().find(elePichql);
	//
	// // 给样品设置id
	// List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
	// // 修改样品图片表
	// List<CotElePic> listPic = new ArrayList<CotElePic>();
	// for (int i = 0; i < elePicList.size(); i++) {
	// Object[] obj = (Object[]) elePicList.get(i);
	// // 获得报价明细,样品图片
	// Object[] temp = (Object[]) getPriceByEle(sameMap.get(obj[1]
	// .toString()), (CotElePic) obj[2]);
	// CotElementsNew ele = setEleByTong((CotElementsNew) obj[0],
	// (CotOrderDetail) temp[0], tongBase, tongBox, tongPrice);
	// listTemp.add(ele);
	// listPic.add((CotElePic) temp[1]);
	// }
	//
	// this.getOrderDao().updateRecords(listTemp);
	// if (tongBase == true) {
	// for (int i = 0; i < listTemp.size(); i++) {
	// CotElementsNew elementsNew = (CotElementsNew) listTemp
	// .get(i);
	// CotElePic cotElePic = (CotElePic) listPic.get(i);
	// cotElePic.setFkId(elementsNew.getId());
	// if (cotElePic.getEleId().equals("")) {
	// cotElePic.setEleId(elementsNew.getEleId());
	// }
	// listPic.add(cotElePic);
	// }
	// this.getOrderDao().updateRecords(listPic);
	// }
	// }
	// // 不同的处理
	// if (dis != null) {
	// // key为eleId,value为随机数
	// Map<String, String> disMap = new HashMap<String, String>();
	// for (int i = 0; i < dis.length; i++) {
	// disMap.put(dis[i], disRdm[i]);
	// }
	// // 获得报价明细,样品图片(key为eleId,value为obj[2])
	// Map map = getPriceByDisEles(disMap);
	//
	// List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
	// for (int i = 0; i < dis.length; i++) {
	// Object[] temp = (Object[]) map.get(dis[i].toLowerCase());
	// CotElementsNew newEle = new CotElementsNew();
	// CotElementsNew ele = setEleByTong(newEle,
	// (CotOrderDetail) temp[0], tongBase, tongBox, tongPrice);
	// if (ele.getEleFlag() == null) {
	// ele.setEleFlag(0l);
	// }
	// listTemp.add(ele);
	// }
	//
	// try {
	// this.getOrderDao().saveRecords(listTemp);
	// // 新建样品图片表
	// List<CotElePic> listPic = new ArrayList<CotElePic>();
	// for (int i = 0; i < listTemp.size(); i++) {
	// CotElementsNew e = listTemp.get(i);
	// Object[] temp = (Object[]) map.get(e.getEleId()
	// .toLowerCase());
	// CotElePic cotElePic = (CotElePic) temp[1];
	// cotElePic.setFkId(e.getId());
	// if (tongBase != true) {
	// byte[] zwtp = this.getZwtpPic();
	// cotElePic.setEleId(e.getEleId());
	// cotElePic.setPicImg(zwtp);
	// cotElePic.setPicName(e.getEleId());
	// cotElePic.setPicSize(zwtp.length);
	// }
	// listPic.add(cotElePic);
	// }
	// this.getOrderDao().saveRecords(listPic);
	// } catch (DAOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// 根据报价的图片转换成样品图片
	public CotElePic changeElePic(String type, Integer srcId, CotElePic pic) {
		CotElePic cotElePic = null;
		if (pic != null) {
			cotElePic = pic;
		} else {
			cotElePic = new CotElePic();
		}
		if (("price").equals(type)) {
			CotPricePic p = (CotPricePic) this.getOrderDao().getById(
					CotPricePic.class, srcId);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("order").equals(type)) {
			CotOrderPic p = (CotOrderPic) this.getOrderDao().getById(
					CotOrderPic.class, srcId);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("given").equals(type)) {
			CotGivenPic p = (CotGivenPic) this.getOrderDao().getById(
					CotGivenPic.class, srcId);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("ele").equals(type)) {
			cotElePic = (CotElePic) this.getOrderDao().getById(CotElePic.class,
					srcId);
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
			CotOrderDetail orderDetail = (CotOrderDetail) this
					.getOrderMapValue(disMap.get(eleId));
			obj[0] = orderDetail;

			CotElePic cotElePic = null;
			if (orderDetail.getType() == null) {
				String hql = "from CotOrderPic obj where obj.fkId="
						+ orderDetail.getId();
				List list = this.getOrderDao().find(hql);
				CotOrderPic cotOrderPic = (CotOrderPic) list.get(0);
				cotElePic = new CotElePic();
				cotElePic.setEleId(cotOrderPic.getEleId());
				cotElePic.setPicImg(cotOrderPic.getPicImg());
				cotElePic.setPicName(cotOrderPic.getPicName());
				cotElePic.setPicSize(cotOrderPic.getPicImg().length);
			} else {
				String type = orderDetail.getType();
				Integer srcId = orderDetail.getSrcId();
				cotElePic = changeElePic(type, srcId, null);
			}
			obj[1] = cotElePic;
			map.put(eleId.toLowerCase(), obj);
		}
		return map;
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
		List<?> list = this.getOrderDao().find(hql);
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

	// 清空明细Map
	public void clearMap(String typeName) {
		SystemUtil.clearSessionByType(null, typeName);
	}

	// 通过key获取Map的value
	@SuppressWarnings("unchecked")
	public CotOrderDetail getOrderMapValue(String rdm) {
		Object obj = SystemUtil.getObjBySession(null, "order");
		Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
		if (orderMap != null) {
			CotOrderDetail cotOrderDetail = (CotOrderDetail) orderMap.get(rdm);
			return cotOrderDetail;
		}
		return null;
	}

	// 储存Map
	@SuppressWarnings("unchecked")
	public void setOrderMap(String rdm, CotOrderDetail cotOrderDetail) {
		Object obj = SystemUtil.getObjBySession(null, "order");
		if (obj == null) {
			Map<String, CotOrderDetail> orderMap = new HashMap<String, CotOrderDetail>();
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "order");
		} else {
			Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "order");
		}

	}

	// 储存Map
	public void setOrderMapAndDel(String rdm, CotOrderDetail cotOrderDetail) {
		Object obj = SystemUtil.getObjBySession(null, "order");
		if (obj == null) {
			Map<String, CotOrderDetail> orderMap = new HashMap<String, CotOrderDetail>();
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "order");
		} else {
			Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
			if (cotOrderDetail.getId() != null) {
				Iterator<?> it = orderMap.keySet().iterator();
				while (it.hasNext()) {
					String temp = (String) it.next();
					CotOrderDetail orderDetail = orderMap.get(temp);
					if (orderDetail.getId() != null
							&& cotOrderDetail.getId().intValue() == orderDetail
									.getId().intValue()) {
						it.remove();
					}
				}
			}
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "order");
		}

	}

	// 通过货号修改Map中对应的征样明细
	public boolean updateMapValueByEleId(String rdm, String property,
			String value) {
		CotOrderDetail cotOrderDetail = getOrderMapValue(rdm);
		if (cotOrderDetail == null)
			return false;
		try {
			ConvertUtils.register(new IntegerConverter(null), Integer.class);
			BeanUtils.setProperty(cotOrderDetail, property, value);
			// if ("factoryId".equals(property)) {
			// if ("".equals(value)) {
			// cotOrderDetail.setFactoryId(null);
			// } else {
			// cotOrderDetail.setFactoryId(Integer.parseInt(value));
			// }
			// } else if ("eleTypeidLv1".equals(property)) {
			// if ("".equals(value)) {
			// cotOrderDetail.setEleTypeidLv1(null);
			// } else {
			// cotOrderDetail.setEleTypeidLv1(Integer.parseInt(value));
			// }
			// } else if ("eleHsid".equals(property)) {
			// if ("".equals(value)) {
			// cotOrderDetail.setEleHsid(null);
			// } else {
			// cotOrderDetail.setEleHsid(Integer.parseInt(value));
			// }
			// } else if("eleTypeidLv2".equals(property)){
			// if("".equals(value)){
			// cotOrderDetail.setEleTypeidLv2(null);
			// }else{
			// cotOrderDetail.setEleTypeidLv2(Integer.parseInt(value));
			// }
			// }else {
			// BeanUtils.setProperty(cotOrderDetail, property, value);
			// }
			this.setOrderMap(rdm, cotOrderDetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 清除Map中eleId对应的映射
	@SuppressWarnings("unchecked")
	public void delMapByKey(String rdm) {
		Object obj = SystemUtil.getObjBySession(null, "order");
		Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
		if (orderMap != null) {
			if (orderMap.containsKey(rdm)) {
				orderMap.remove(rdm);
			}
		}
	}

	// Action获取signMap
	@SuppressWarnings("unchecked")
	public Map<String, CotOrderDetail> getMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "order");
		Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
		return orderMap;
	}

	// Action储存signMap
	@SuppressWarnings("unchecked")
	public void setMapAction(HttpSession session, String rdm,
			CotOrderDetail cotOrderDetail) {
		Object obj = SystemUtil.getObjBySession(session, "order");
		if (obj == null) {
			Map<String, CotOrderDetail> orderMap = new HashMap<String, CotOrderDetail>();
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(session, orderMap, "order");
		} else {
			Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(session, orderMap, "order");
		}

	}

	// 在Action中清除Map中eleId对应的映射
	public void delMapByKeyAction(String rdm, HttpSession session) {
		Map<String, CotOrderDetail> orderMap = this.getMapAction(session);
		if (orderMap != null) {
			if (orderMap.containsKey(rdm)) {
				orderMap.remove(rdm);
			}
		}
	}

	// 查询某张单下的订单明细,将所有货号组成一个map
	public Map getEleMap(Integer mainId) {
		String sql = "select obj.eleId from CotOrderDetail obj from where obj.orderId="
				+ mainId;
		List list = this.getOrderDao().find(sql);
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			String ele = (String) list.get(i);
			map.put(ele.toLowerCase(), ele);
		}
		return map;
	}

	// 根据明细货号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId) {
		CotOrderDetail cotOrderDetail = (CotOrderDetail) this.getOrderDao()
				.getById(CotOrderDetail.class, detailId);
		if (cotOrderDetail != null) {
			return cotOrderDetail.getPicImg();
		}
		return null;
	}

	// 删除明细图片picImg
	public boolean deletePicImg(Integer detailId) {
		String classPath = CotSignServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		// CotOrderDetail cotOrderDetail = this.getOrderDetailById(detailId);
		List<CotOrderPic> imgList = new ArrayList<CotOrderPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotOrderPic orderPic = impOpService.getOrderPic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			// cotOrderDetail.setPicImg(b);
			// cotOrderDetail.setPicName("zwtp");
			// this.getOrderDao().update(cotOrderDetail);
			orderPic.setPicImg(b);
			orderPic.setPicSize(b.length);
			imgList.add(orderPic);
			impOpService.modifyImg(imgList);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除明细图片错误!");
			return false;
		}
	}

	// 删除唛标明细图片
	public boolean deleteMBPicImg(Integer orderId) {
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		String hql = "from CotOrderMb obj where obj.fkId=" + orderId;
		List list = this.getOrderDao().find(hql);
		CotOrderMb cotOrderMb = (CotOrderMb) list.get(0);

		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderMb.setPicImg(b);
			cotOrderMb.setPicSize(b.length);
			this.getOrderDao().update(cotOrderMb);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除唛标图片错误!");
			return false;
		}
	}

	// 删除产品标图片
	public boolean deleteProductMBPicImg(Integer orderId) {
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		String hql = "from CotProductMb obj where obj.fkId=" + orderId;
		List list = this.getOrderDao().find(hql);
		CotProductMb cotProductMb = (CotProductMb) list.get(0);

		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotProductMb.setPicImg(b);
			cotProductMb.setPicSize(b.length);
			this.getOrderDao().update(cotProductMb);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除产品标图片错误!");
			return false;
		}
	}

	// 更新征样图片picImg字段
	public void updatePicImg(String filePath, Integer detailId) {
		CotOrderDetail cotOrderDetail = this.getOrderDetailById(detailId);
		List<CotOrderPic> imgList = new ArrayList<CotOrderPic>();
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotOrderPic orderPic = impOpService.getOrderPic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			if (orderPic == null) {
				orderPic = new CotOrderPic();
				orderPic.setFkId(detailId);
				orderPic.setEleId(cotOrderDetail.getEleId());
				orderPic.setPicName(cotOrderDetail.getEleId());
			}
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			orderPic.setPicImg(b);
			orderPic.setPicSize(b.length);
			imgList.add(orderPic);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			// this.getOrderDao().update(cotOrderDetail);
			impOpService.saveOrUpdateImg(imgList);
		} catch (Exception e) {
			logger.error("修改征样明细图片错误!");
		}
	}

	// 更新订单的麦标图片
	public void updateMBImg(String filePath, Integer mainId) {
		String hql = "from CotOrderMb obj where obj.fkId=" + mainId;
		List<?> list = this.getOrderDao().find(hql);
		CotOrderMb cotOrderMb = new CotOrderMb();
		if (list.size() == 1) {
			cotOrderMb = (CotOrderMb) list.get(0);
		}
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderMb.setPicImg(b);
			cotOrderMb.setFkId(mainId);
			cotOrderMb.setPicSize(b.length);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			List<CotOrderMb> rec = new ArrayList<CotOrderMb>();
			rec.add(cotOrderMb);
			this.getOrderDao().saveOrUpdateRecords(rec);
		} catch (Exception e) {
			logger.error("更新订单的麦标图片错误!");
		}
	}

	// 更新订单的产品标图片
	public void updateProMBImg(String filePath, Integer mainId) {
		String hql = "from CotProductMb obj where obj.fkId=" + mainId;
		List<?> list = this.getOrderDao().find(hql);
		CotProductMb cotProductMb = new CotProductMb();
		if (list.size() == 1) {
			cotProductMb = (CotProductMb) list.get(0);
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
			List<CotProductMb> rec = new ArrayList<CotProductMb>();
			rec.add(cotProductMb);
			this.getOrderDao().saveOrUpdateRecords(rec);
		} catch (Exception e) {
			logger.error("更新订单的产品标图片错误!");
		}
	}

	// 根据客户编号查找麦标PicImg
	public byte[] getPicImgByCustId(Integer custId) {
		CotCustomer cotCustomer = (CotCustomer) this.getOrderDao().getById(
				CotCustomer.class, custId);
		if (cotCustomer != null) {
			return cotCustomer.getPicImg();
		}
		return null;
	}

	// 根据订单编号查找麦标PicImg
	public byte[] getPicImgByOrderId(Integer custId) {

		List<?> list = this.getOrderDao().find(
				"from CotOrderMb obj where obj.fkId=" + custId);
		CotOrderMb cotOrderMb = new CotOrderMb();
		if (list.size() == 1) {
			cotOrderMb = (CotOrderMb) list.get(0);
		}
		if (cotOrderMb != null) {
			return cotOrderMb.getPicImg();
		}
		return null;
	}

	// 根据订单编号查找产品标PicImg
	public byte[] getProPicImgByOrderId(Integer custId) {

		List<?> list = this.getOrderDao().find(
				"from CotProductMb obj where obj.fkId=" + custId);
		CotProductMb cotProductMb = new CotProductMb();
		if (list.size() == 1) {
			cotProductMb = (CotProductMb) list.get(0);
		}
		if (cotProductMb != null) {
			return cotProductMb.getPicImg();
		}
		return null;
	}

	public CotBaseDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(CotBaseDao orderDao) {
		this.orderDao = orderDao;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	// 获取样品图片的HTML显示，用图片演示用
	@SuppressWarnings("unchecked")
	public List getOrderImg(String custId, String startDate, String endDate,
			int start, int limit) {

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1 and obj.ORDER_ID = ord.ID");
		if (custId != null && !custId.equals("")) {
			queryString.append(" and ord.CUST_ID = " + custId);
		}
		if (startDate != null && !"".equals(startDate)) {
			queryString.append(" and ord.order_time >= '" + startDate + "'");
		}
		if (endDate != null && !"".equals(endDate)) {
			queryString.append(" and ord.order_time <= '" + endDate + "'");
		}
		QueryInfo queryInfo = new QueryInfo();
		String sql = "SELECT " + "obj.ID as id," + "obj.ELE_ID as eleId,"
				+ "obj.ELE_NAME as eleName," + "obj.ELE_NAME_EN as eleNameEn,"
				+ "obj.ELE_SIZE_DESC as eleSizeDesc,"
				+ "ord.order_time as orderTime "
				+ " FROM cot_order_detail obj , cot_order ord ";
		queryInfo.setSelectString(sql);
		queryInfo.setCountOnEachPage(limit);
		queryInfo.setStartIndex(start);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.ID desc");
		queryInfo.setQueryObjType("CotNewOrderVO");

		List<CotNewOrderVO> res = null;
		try {
			res = this.getOrderDao().findRecordsJDBC(queryInfo);

		} catch (DAOException e) {

			e.printStackTrace();
		}
		return res;
	}

	// 获取查询的记录数,图片演示用
	public int findPicCount(String custId, String startDate, String endDate) {
		QueryInfo queryInfo = new QueryInfo();

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1 and obj.orderId = ord.id");
		if (custId != null && !custId.equals("")) {
			queryString.append(" and ord.custId = " + custId);
		}
		if (startDate != null && !"".equals(startDate)) {
			queryString.append(" and ord.orderTime >= '" + startDate + "'");
		}
		if (endDate != null && !"".equals(endDate)) {
			queryString.append(" and ord.orderTime <= '" + endDate + "'");
		}
		System.out.println(custId);
		String sql = "SELECT Count(*) " +

		" FROM CotOrderDetail obj,CotOrder ord";
		queryInfo.setCountQuery(sql + queryString.toString());

		int res = 0;
		try {
			res = this.getOrderDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return res;
	}

	// 得到公司的集合
	public List<?> getCompanyList() {
		List<?> list = this.getOrderDao().getRecords("CotCompany");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotCompany cotCompany = (CotCompany) it.next();
			cotCompany.setCompanyLogo(null);
		}
		return list;
	}

	// 得到客户的集合
	public List<?> getCustomerList() {
		List<?> list = this.getOrderDao().getRecords("CotCustomer");
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
		List<?> list = this.getOrderDao().getRecords("CotEmps");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotEmps cotEmps = (CotEmps) it.next();
			cotEmps.setCustomers(null);
		}
		return list;
	}

	// 根据订单号查询该单的客户
	public CotCustomer getCusByOrderId(Integer orderId) {
		if (orderId == null) {
			return null;
		}
		CotOrder cotOrder = (CotOrder) this.getOrderDao().getById(
				CotOrder.class, orderId);
		if (cotOrder != null) {
			CotCustomer cotCustomer = (CotCustomer) this.getOrderDao().getById(
					CotCustomer.class, cotOrder.getCustId());
			if (cotCustomer != null) {
				cotCustomer.setPicImg(null);
				cotCustomer.setCustImg(null);
			}
			return cotCustomer;
		} else {
			return null;
		}
	}

	// 通过主订单id获取订单明细的集合(20090922新增条件,数量大于0,未分数量有大于0)
	public List<?> getDetailIdsByOrderId(Integer orderId) {
		List<?> ids = this
				.getOrderDao()
				.find(
						"select obj.id from CotOrderDetail obj where obj.orderId="
								+ orderId
								+ " and obj.boxCount>0 and obj.unBoxCount4OrderFac > 0");
		if (ids.size() > 0) {
			return ids;
		}
		return null;
	}

	// 获取默认公司ID
	public Integer getDefaultCompanyId() {
		List<?> ids = this
				.getOrderDao()
				.find(
						"select obj.id from CotCompany obj where obj.companyIsdefault=1");
		if (ids != null & ids.size() > 0) {
			return (Integer) ids.get(0);
		}
		return null;
	}

	// 从数据字典中取生产合同合同条款
	public String getContract() {
		List<?> list = this.getOrderDao().find(
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

	// 保存主采购单并返回map
	@SuppressWarnings("deprecation")
	public HashMap<?, ?> saveOrderFac(String[] factoryIdAry, Integer orderId) {
		byte[] zwtpByte = this.getZwtpPic();
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		// 通过orderId获取主订单对象
		CotOrder cotOrder = this.getOrderById(orderId);
		String orderNo = cotOrder.getOrderNo(); // 主订单编号
		// Integer currencyId = cotOrder.getCurrencyId(); // 主订单币种
		Integer bussinessPerson = cotOrder.getBussinessPerson(); // 主订单业务员

		// 获取图片
		List listMB = null;
		List listProMB = null;
		if (orderId != null) {
			String hql = "from CotOrderMb obj where obj.fkId= " + orderId;
			listMB = this.getOrderDao().find(hql);
			String proHql = "from CotProductMb obj where obj.fkId= " + orderId;
			listProMB = this.getOrderDao().find(proHql);
		}

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
		List<?> res = this.getList("CotPriceCfg");
		CotPriceCfg cotPriceCfg = new CotPriceCfg();
		if (res.size() > 0) {
			cotPriceCfg = (CotPriceCfg) res.get(0);
		}

		// 用HashSet过滤factoryIdAry中的相同厂家id
		HashSet<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(factoryIdAry));
		String[] factoryIdAry2 = (String[]) set.toArray(new String[0]);

		for (int i = 0; i < factoryIdAry2.length; i++) {
			if (factoryIdAry2[i].equals(""))
				continue;
			// boolean factoryisExist =
			// this.isExistFactory(Integer.parseInt(factoryIdAry2[i]),orderId);

			boolean factoryisExist = false;
			List<Integer> idres = this.getCotOrderFacIds(orderId);
			CotOrderFac orderFac = new CotOrderFac();
			for (int k = 0; k < idres.size(); k++) {
				orderFac = (CotOrderFac) this.getOrderDao().getById(
						CotOrderFac.class, idres.get(k));
				if (orderFac != null) {
					if (Integer.parseInt(factoryIdAry2[i]) == orderFac
							.getFactoryId().intValue()) {
						factoryisExist = true;
						break;
					}
				}
			}
			if (factoryisExist) {
				map.put(Integer.parseInt(factoryIdAry2[i]), orderFac.getId());
				continue;
			}
			Integer factoryId = Integer.parseInt(factoryIdAry2[i]);
			String currDate = ContextUtil.getCurrentDate("yyyy-MM-dd");

			CotSeqService seq = new CotSeqServiceImpl();
			String orderFacNo = seq.getAutoOrderFacNo(factoryId, orderId);
			seq.saveSeq("autoOrderfacNo");// 直接更新序列号
			try {
				CotOrderFac cotOrderFac = new CotOrderFac();
				cotOrderFac.setBusinessPerson(bussinessPerson); // 业务员
				cotOrderFac.setOrderClause(orderFacContract); // 合同条款
				if (cotPriceCfg.getIsCheck() == null
						|| cotPriceCfg.getIsCheck() == 0) {
					cotOrderFac.setOrderStatus(9L);// 审核状态 0:未审核 1:通过 2:不通过
					// 3:请审核 9:不审核
				} else {
					cotOrderFac.setOrderStatus(0L);
				}

				// 设置生产合同交货时间
				Calendar cal = Calendar.getInstance();
				if (cotOrder.getSendTime() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					cal.setTime(cotOrder.getSendTime());
					// 如果没有默认配置.提前时间为20天
					if (cotPriceCfg.getAppendDay() != null) {
						cal.add(Calendar.DATE, -cotPriceCfg.getAppendDay());
					} else {
						cal.add(Calendar.DATE, -20);
					}
					String dateString = sdf.format(cal.getTime());
					java.util.Date timeDate = sdf.parse(dateString);// util类型
					java.sql.Date dateTime = new java.sql.Date(timeDate
							.getTime());// sql类型
					cotOrderFac.setSendTime(dateTime);
				}

				cotOrderFac.setOrderZm(cotOrder.getOrderZM());
				cotOrderFac.setOrderZhm(cotOrder.getOrderZHM());
				cotOrderFac.setOrderCm(cotOrder.getOrderCM());
				cotOrderFac.setOrderNm(cotOrder.getOrderNM());
				cotOrderFac.setOrderMb(cotOrder.getOrderMB());
				// 产品标
				cotOrderFac.setProductM(cotOrder.getProductM());
				// 设置订单ID
				cotOrderFac.setOrderId(orderId);

				cotOrderFac.setFactoryId(factoryId); // 采购厂家
				if (cotOrder.getCompanyId() == null)
					cotOrderFac.setCompanyId(defaultCompanyId); // 出口商设为默认公司
				else {
					cotOrderFac.setCompanyId(cotOrder.getCompanyId());
				}
				cotOrderFac.setCurrencyId(cotPriceCfg.getFacPriceUnit()); // 币种
				cotOrderFac.setOrderAddress(cotPriceCfg.getOrderAddress());// 签约地点
				cotOrderFac.setGivenAddress(cotPriceCfg.getGivenAddress());// 交货地点
				cotOrderFac.setOrderNo(orderFacNo); // 发票编号
				// cotOrderFac.setAddTime(addTime); // 添加时间
				cotOrderFac.setOrderTime(addTime);// 下单时间
				cotOrderFac.setEmpId(cotEmps.getId()); // 操作人编号
				// cotOrderFac.setOrderStatus(new Long(0)); // 审核状态
				// 0:(未审核),1(已审核)通过,2(审核不通过)
				cotOrderFac.setOrderIscheck(new Long(0)); // 是否需要审核 0:不需要审核
				// 1:需要审核 （默认0）
				List<CotOrderFac> list = new ArrayList<CotOrderFac>();
				list.add(cotOrderFac);
				this.getOrderDao().saveOrUpdateRecords(list);
				Integer orderfacId = cotOrderFac.getId();

				// 唛标信息
				CotOrderfacMb facMb = new CotOrderfacMb();
				if (listMB != null && listMB.size() > 0) {
					CotOrderMb orderMb = (CotOrderMb) listMB.get(0);
					facMb.setFkId(orderfacId);
					facMb.setPicImg(orderMb.getPicImg());
					facMb.setPicSize(orderMb.getPicSize());

				} else {
					facMb.setFkId(orderfacId);
					facMb.setPicImg(zwtpByte);
					facMb.setPicSize(zwtpByte.length);
				}
				List listFacMB = new ArrayList();
				listFacMB.add(facMb);
				this.getOrderDao().saveOrUpdateRecords(listFacMB);
				// 保存产品标
				CotProductFacMb proMb = new CotProductFacMb();
				if (listProMB != null && listProMB.size() > 0) {
					CotProductMb orderMb = (CotProductMb) listProMB.get(0);
					proMb.setFkId(orderfacId);
					proMb.setPicImg(orderMb.getPicImg());
					proMb.setPicSize(orderMb.getPicSize());

				} else {
					proMb.setFkId(orderfacId);
					proMb.setPicImg(zwtpByte);
					proMb.setPicSize(zwtpByte.length);
				}
				List temp = new ArrayList();
				temp.add(proMb);
				this.getOrderDao().saveOrUpdateRecords(temp);
				map.put(factoryId, orderfacId);
				// 保存OrderStatus表
				List<CotOrderStatus> listOrderStatus = this.getOrderDao().find(
						"from CotOrderStatus obj where obj.orderId=" + orderId);

				// 添加操作日志
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(cotEmps.getId());
				syslog.setItemNo(cotOrderFac.getOrderNo());
				syslog.setOpMessage("分解订单添加到主采购单成功");
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);

			} catch (Exception e) {
				logger.error("保存主采购单出错！");
				e.printStackTrace();
				// 添加操作日志
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(cotEmps.getId());
				syslog.setOpMessage("分解订单添加到主要采购单失败，失败原因：" + e.getMessage());
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		}
		return map;
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

	// 根据factoryId获取map中的orderfacId
	public CotOrderFac getCotOrderFacByFacId(Integer factoryId,
			HashMap<?, ?> map) {
		if (map != null && !map.isEmpty()) {
			// 通过厂家id获取map中的主采购单id
			Integer orderfacId = (Integer) map.get(factoryId);
			CotOrderFac cotOrderFac = (CotOrderFac) this.getOrderDao().getById(
					CotOrderFac.class, orderfacId);
			return cotOrderFac;
		} else {
			return null;
		}
	}

	// 保存生产合同单号及id到订单明细中
	public void saveOrderFacIdAndNo(Integer factoryId, HashMap<?, ?> map,
			Integer orderId) {

		List<CotOrderDetail> records = new ArrayList<CotOrderDetail>();
		if (map != null && !map.isEmpty()) {
			// 通过厂家id获取map中的主采购单id
			Integer orderfacId = (Integer) map.get(factoryId);
			CotOrderFac cotOrderFac = (CotOrderFac) this.getOrderDao().getById(
					CotOrderFac.class, orderfacId);

			String hql = " from CotOrderDetail obj where obj.orderId ="
					+ orderId + " and obj.factoryId =" + factoryId;
			List<?> res = this.getOrderDao().find(hql);
			Iterator<?> it = res.iterator();
			while (it.hasNext()) {

				CotOrderDetail orderDetail = (CotOrderDetail) it.next();
				orderDetail.setOrderFacnoid(orderfacId);
				orderDetail.setOrderFacno(cotOrderFac.getOrderNo());

				records.add(orderDetail);
			}
			this.getOrderDao().updateRecords(records);
		}
	}

	// 保存采购单明细
	public Boolean saveOrderFacDetail(Integer factoryId, HashMap<?, ?> map,
			CotOrderFacdetail cotOrderFacdetail) {
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		if (map != null && !map.isEmpty()) {
			// 通过厂家id获取map中的主采购单id
			Integer orderfacId = (Integer) map.get(factoryId);

			// 保存生产合同单号及id到订单明细中
			this.saveOrderFacIdAndNo(factoryId, map, cotOrderFacdetail
					.getOrderId());

			// 查询主单的币种,将明细的生产价转换成对应值-----azan添加
			if (cotOrderFacdetail.getPriceFac() != null) {
				String hql = "select obj.currencyId from CotOrderFac obj where obj.id="
						+ orderfacId;
				List listTemp = this.getOrderDao().find(hql);
				Integer currencyId = (Integer) listTemp.get(0);
				Double mon = this.updatePrice(cotOrderFacdetail.getPriceFac(),
						cotOrderFacdetail.getPriceFacUint(), currencyId);
				cotOrderFacdetail.setPriceFac(mon.floatValue());
				cotOrderFacdetail.setPriceFacUint(currencyId);
			}

			// 设置厂家id
			cotOrderFacdetail.setOrderId(orderfacId);
			if (cotOrderFacdetail.getOutRemain() == null) {
				cotOrderFacdetail.setOutRemain(cotOrderFacdetail.getBoxCount());
			}
			// 设置总CBM
			if (cotOrderFacdetail.getBoxCount() == null
					|| cotOrderFacdetail.getBoxCbm() == null) {
				cotOrderFacdetail.setTotalCbm(0f);
			} else {
				cotOrderFacdetail.setTotalCbm(cotOrderFacdetail
						.getContainerCount()
						* cotOrderFacdetail.getBoxCbm());
			}
			// 设置总毛重
			if (cotOrderFacdetail.getBoxCount() == null
					|| cotOrderFacdetail.getBoxGrossWeigth() == null) {
				cotOrderFacdetail.setTotalGrossWeigth(0f);
			} else {
				cotOrderFacdetail.setTotalGrossWeigth(cotOrderFacdetail
						.getBoxCount()
						* cotOrderFacdetail.getBoxGrossWeigth());
			}
			// 设置总净重
			if (cotOrderFacdetail.getBoxCount() == null
					|| cotOrderFacdetail.getBoxNetWeigth() == null) {
				cotOrderFacdetail.setTotalNetWeigth(0f);
			} else {
				cotOrderFacdetail.setTotalNetWeigth(cotOrderFacdetail
						.getBoxCount()
						* cotOrderFacdetail.getBoxNetWeigth());
			}
			// 设置总厂价
			if (cotOrderFacdetail.getBoxCount() == null
					|| cotOrderFacdetail.getPriceFac() == null) {
				cotOrderFacdetail.setTotalFac(0f);
			} else {
				cotOrderFacdetail.setTotalFac(cotOrderFacdetail.getBoxCount()
						* cotOrderFacdetail.getPriceFac());
			}
			// 设置总金额
			if (cotOrderFacdetail.getBoxCount() == null
					|| cotOrderFacdetail.getOrderPrice() == null) {
				cotOrderFacdetail.setTotalMoney(0f);
			} else {
				cotOrderFacdetail.setTotalMoney(cotOrderFacdetail.getBoxCount()
						* cotOrderFacdetail.getOrderPrice());
			}

			// 新建采购图片对象
			CotOrderfacPic orderfacPic = new CotOrderfacPic();
			// 获取订单图片对象
			Integer orderDetailId = cotOrderFacdetail.getOrderDetailId();
			CotOrderPic orderPic = impOpService.getOrderPic(orderDetailId);
			if (orderPic != null) {
				orderfacPic.setEleId(cotOrderFacdetail.getEleId());
				orderfacPic.setPicName(cotOrderFacdetail.getEleId());
				orderfacPic.setPicSize(orderPic.getPicSize());
				orderfacPic.setPicImg(orderPic.getPicImg());
			} else {
				byte[] zwtpByte = this.getZwtpPic();
				orderfacPic.setEleId(cotOrderFacdetail.getEleId());
				orderfacPic.setPicName("zwtp");
				orderfacPic.setPicSize(zwtpByte.length);
				orderfacPic.setPicImg(zwtpByte);
			}
			WebContext ctx = WebContextFactory.get();
			CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			List<CotOrderFacdetail> list = new ArrayList<CotOrderFacdetail>();
			try {
				list.add(cotOrderFacdetail);
				this.getOrderDao().saveOrUpdateRecords(list);
				orderfacPic.setFkId(cotOrderFacdetail.getId());
				this.getOrderDao().create(orderfacPic);

				// 添加操作日志
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(cotEmps.getId());
				syslog.setItemNo(cotOrderFacdetail.getId().toString());
				syslog.setOpMessage("分解订单添加到采购单明细成功");
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);

				return true;

			} catch (Exception e) {
				logger.error("保存采购单明细出错！");
				e.printStackTrace();
				// 添加操作日志
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(cotEmps.getId());
				syslog.setItemNo(cotOrderFacdetail.getId().toString());
				syslog.setOpMessage("分解订单添加到采购单明细失败，失败原因：" + e.getMessage());
				syslog.setOpModule("orderfac");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);

				return false;
			}
		}
		return false;
	}

	// 根据员工编号获得员工
	public CotEmps getEmpsById(Integer empId) {
		CotEmps cotEmps = (CotEmps) this.getOrderDao().getById(CotEmps.class,
				empId);
		cotEmps.setCustomers(null);
		return cotEmps;
	}

	// 根据员工编号获得员工编号和名称
	public String[] getTeEmpsById(Integer empId) {
		String sql = "select obj.id,obj.empsName from CotEmps obj where obj.id="
				+ empId;
		List list = this.getOrderDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
	}

	// 得到单样品利润公式
	@SuppressWarnings("unchecked")
	public String[] getLiRunCau() {
		String[] caus = new String[3];

		// 获得单样品利润价格公式(名字固定是叫'InverseMarginFOB')
		String cauHql = "select c.expessionIn from CotCalculation c where c.calName='InverseMargin_FOB'";
		List<String> cauList = this.getOrderDao().find(cauHql);
		if (cauList.size() != 0) {
			caus[0] = cauList.get(0);
		} else {
			caus[0] = "";
		}

		// 获得单样品利润价格公式(名字固定是叫'InverseMarginCIF')
		String cifHql = "select c.expessionIn from CotCalculation c where c.calName='InverseMargin_CIF'";
		List<String> cifList = this.getOrderDao().find(cifHql);
		if (cifList.size() != 0) {
			caus[1] = cifList.get(0);
		} else {
			caus[1] = "";
		}

		// 获得单样品利润价格公式(名字固定是叫'InverseMarginDDP')
		String ddpHql = "select c.expessionIn from CotCalculation c where c.calName='InverseMargin_DDP'";
		List<String> ddpList = this.getOrderDao().find(ddpHql);
		if (ddpList.size() != 0) {
			caus[2] = ddpList.get(0);
		} else {
			caus[2] = "";
		}
		return caus;
	}

	// 返回该货号的子货号的id集合
	public List<?> getChildrens(String eleId) {
		String hql = "select obj.id from CotElementsNew obj where obj.eleParent='"
				+ eleId + "'";
		List list = this.getOrderDao().find(hql);
		return list;
	}

	// 获得新利润率
	public Float getNewLiRun(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotOrderDetail obj = this.getOrderMapValue(rdm);
		if (obj == null) {
			return null;
		}
		Float facSum = this.getFacSum();
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 利润率
		Float orderProfit = 0f;
		if (obj.getLiRun() != null) {
			orderProfit = obj.getLiRun();
		}
		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}
		// FOB费用
		Float orderFob = 0f;
		Object orderFobObj = map.get("orderFob");
		if (orderFobObj != null && !"".equals(orderFobObj.toString())) {
			orderFob = Float.parseFloat(orderFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
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
		// Fob价总和
		Float fobSum = this.getFobSumDetail();
		// 整柜运费
		Float orderCharge = 0f;
		Object orderChargeObj = map.get("orderCharge");
		if (orderChargeObj != null && !"".equals(orderChargeObj.toString())) {
			orderCharge = Float.parseFloat(orderChargeObj.toString());
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
		String liRunCau[] = this.getLiRunCau();
		String cau_Li = "";
		// 价格条款
		Object clauseObj = map.get("clauseTypeId");
		Integer clauseTypeId = 0;
		if (clauseObj != null && !"".equals(clauseObj.toString())) {
			clauseTypeId = Integer.parseInt(clauseObj.toString());
			if (clauseTypeId == 1 || clauseTypeId == 6) {
				cau_Li = liRunCau[0];
			}
			if (clauseTypeId == 3 || clauseTypeId == 7 || clauseTypeId == 8) {
				cau_Li = liRunCau[1];
			}
			if (clauseTypeId == 4 || clauseTypeId == 9) {
				cau_Li = liRunCau[2];
			}
		}

		// 如果没有配置名叫"InverseMargin"的公式
		if ("".equals(cau_Li)) {
			return null;
		}

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 计算报价
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
		evaluator.putVariable("priceFob", orderFob.toString());
		evaluator.putVariable("priceProfit", orderProfit.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		evaluator.putVariable("importTax", importTax.toString());
		evaluator.putVariable("facSum", facSum.toString());
		evaluator.putVariable("fobSum", fobSum.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getOrderDao().getRecords(
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
		// 样品数量
		Long boxCount = obj.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
		}
		evaluator.putVariable("priceCharge", orderCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("boxCount", boxCount.toString());
		if (obj.getTuiLv() != null) {
			evaluator.putVariable("tuiLv", obj.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());

		// 新价格
		Float price = 0f;
		Object orderObj = map.get("price");
		if (orderObj != null && !"".equals(orderObj.toString())) {
			price = Float.parseFloat(orderObj.toString());
		} else {
			// calLiRun方法需要
			price = obj.getOrderPrice().floatValue();
		}
		float res = 0;
		try {
			// 计算利润率
			evaluator.putVariable("price", price.toString());
			String result = evaluator.evaluate(cau_Li);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	// 根据明细中的利润率代入主单利润率算出最新价格
	@SuppressWarnings("unchecked")
	public Float getNewPriceByLiRun(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotOrderDetail obj = this.getOrderMapValue(rdm);
		if (obj == null) {
			return null;
		}

		Float facSum = this.getFacSum();
		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}

		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseTypeId");
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
		Float orderProfit = 0f;
		if (obj.getLiRun() != null) {
			orderProfit = obj.getLiRun();
		}
		// FOB费用
		Float orderFob = 0f;
		Object orderFobObj = map.get("orderFob");
		if (orderFobObj != null && !"".equals(orderFobObj.toString())) {
			orderFob = Float.parseFloat(orderFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
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
		// Fob价总和
		Float fobSum = this.getFobSumDetail();
		// 整柜运费
		Float orderCharge = 0f;
		Object orderChargeObj = map.get("orderCharge");
		if (orderChargeObj != null && !"".equals(orderChargeObj.toString())) {
			orderCharge = Float.parseFloat(orderChargeObj.toString());
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
		List<String> cauList = this.getOrderDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 计算报价
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
		evaluator.putVariable("priceFob", orderFob.toString());
		if (obj.getTuiLv() != null) {
			evaluator.putVariable("tuiLv", obj.getTuiLv().toString());
		} else {
			evaluator.putVariable("tuiLv", "0");
		}
		evaluator.putVariable("priceProfit", orderProfit.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		evaluator.putVariable("importTax", importTax.toString());
		evaluator.putVariable("facSum", facSum.toString());
		evaluator.putVariable("fobSum", fobSum.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getOrderDao().getRecords(
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
		// 装箱率
		Long boxCount = obj.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
		}
		evaluator.putVariable("priceCharge", orderCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("boxCount", boxCount.toString());
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

	// 根据明细中的退税率算出最新价格
	@SuppressWarnings("unchecked")
	public Float getNewPriceByTuiLv(Map<?, ?> map, String rdm) {
		// 获得要修改的报价明细对象
		CotOrderDetail obj = this.getOrderMapValue(rdm);
		if (obj == null) {
			return null;
		}
		Float facSum = this.getFacSum();

		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseTypeId");
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
		Float orderProfit = 0f;
		if (obj.getLiRun() != null) {
			orderProfit = obj.getLiRun();
		}
		// 退税率
		Float tuiLv = 0f;
		Object tuiLvObj = map.get("tuiLv");
		if (tuiLvObj != null && !"".equals(tuiLvObj.toString())) {
			tuiLv = Float.parseFloat(tuiLvObj.toString());
		}
		// FOB费用
		Float orderFob = 0f;
		Object orderFobObj = map.get("orderFob");
		if (orderFobObj != null && !"".equals(orderFobObj.toString())) {
			orderFob = Float.parseFloat(orderFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
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
		// Fob价总和
		Float fobSum = this.getFobSumDetail();
		// 整柜运费
		Float orderCharge = 0f;
		Object orderChargeObj = map.get("orderCharge");
		if (orderChargeObj != null && !"".equals(orderChargeObj.toString())) {
			orderCharge = Float.parseFloat(orderChargeObj.toString());
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
		List<String> cauList = this.getOrderDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 计算报价
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
		evaluator.putVariable("priceFob", orderFob.toString());
		evaluator.putVariable("priceProfit", orderProfit.toString());
		evaluator.putVariable("tuiLv", tuiLv.toString());
		evaluator.putVariable("priceRate", priceRate.toString());
		evaluator.putVariable("importTax", importTax.toString());
		evaluator.putVariable("facSum", facSum.toString());
		evaluator.putVariable("fobSum", fobSum.toString());
		// 样品CBM
		Float cbm = obj.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		// List<?> containerList = this.getDicList("container");
		List<?> containerList = this.getOrderDao().getRecords(
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
		// 样品数量
		Long boxCount = obj.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
		}
		evaluator.putVariable("priceCharge", orderCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("boxCount", boxCount.toString());
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
		CotOrderDetail obj = this.getOrderMapValue(rdm);
		if (obj == null) {
			return null;
		}
		Float facSum = this.getFacSum();

		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseTypeId");
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
		Float orderProfit = 0f;
		if (obj.getLiRun() != null) {
			orderProfit = obj.getLiRun();
		}
		// 生产价
		Float priceFac = 0f;
		Object priceFacObj = map.get("priceFac");
		if (priceFacObj != null && !"".equals(priceFacObj.toString())) {
			priceFac = Float.parseFloat(priceFacObj.toString());
		}
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
		Float orderFob = 0f;
		Object orderFobObj = map.get("orderFob");
		if (orderFobObj != null && !"".equals(orderFobObj.toString())) {
			orderFob = Float.parseFloat(orderFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
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
		// Fob价总和
		Float fobSum = this.getFobSumDetail();
		// 整柜运费
		Float orderCharge = 0f;
		Object orderChargeObj = map.get("orderCharge");
		if (orderChargeObj != null && !"".equals(orderChargeObj.toString())) {
			orderCharge = Float.parseFloat(orderChargeObj.toString());
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

		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getOrderDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return null;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", orderFob.toString());
		evaluator.putVariable("priceProfit", orderProfit.toString());
		evaluator.putVariable("importTax", importTax.toString());
		evaluator.putVariable("facSum", facSum.toString());
		evaluator.putVariable("fobSum", fobSum.toString());
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
		List<?> containerList = this.getOrderDao().getRecords(
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
		// 样品数量
		Long boxCount = obj.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
		}
		evaluator.putVariable("priceCharge", orderCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("boxCount", boxCount.toString());
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());
		Float res = 0f;
		try {
			String result = evaluator.evaluate(cau);
			result = ContextUtil.getObjByPrecision(Float.parseFloat(result),
					"orderPricePrecision");
			return Float.parseFloat(result);
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
		CotOrderDetail obj = this.getOrderMapValue(rdm);
		if (obj == null) {
			return null;
		}
		// 价格条款
		Integer clauseId = 0;
		Object clauseIdObj = map.get("clauseTypeId");
		if (clauseIdObj != null && !"".equals(clauseIdObj.toString())) {
			clauseId = Integer.parseInt(clauseIdObj.toString());
		} else {
			return null;
		}

		Float facSum = this.getFacSum();

		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}
		// 币种
		Integer currencyId = 0;
		Object currencyIdObj = map.get("currencyId");
		if (currencyIdObj != null && !"".equals(currencyIdObj.toString())) {
			currencyId = Integer.parseInt(currencyIdObj.toString());
		}
		// 生产价币种
		Integer priceFacUint = 0;
		Object priceFacUintObj = map.get("priceFacUint");
		if (priceFacUintObj != null && !"".equals(priceFacUintObj.toString())) {
			priceFacUint = Integer.parseInt(priceFacUintObj.toString());
		}
		// 利润率
		Float orderProfit = 0f;
		if (obj.getLiRun() != null) {
			orderProfit = obj.getLiRun();
		}
		Float rmb = 0f;
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
		// FOB费用
		Float orderFob = 0f;
		Object orderFobObj = map.get("orderFob");
		if (orderFobObj != null && !"".equals(orderFobObj.toString())) {
			orderFob = Float.parseFloat(orderFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// Fob价总和
		Float fobSum = this.getFobSumDetail();
		// 整柜运费
		Float orderCharge = 0f;
		Object orderChargeObj = map.get("orderCharge");
		if (orderChargeObj != null && !"".equals(orderChargeObj.toString())) {
			orderCharge = Float.parseFloat(orderChargeObj.toString());
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

		// 根据选择的价格条款获得价格公式
		String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
				+ clauseId;
		List<String> cauList = this.getOrderDao().find(cauHql);
		// 价格公式没设置的话,返回空
		if (cauList.size() == 0) {
			return 0f;
		}
		String cau = cauList.get(0);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 定义FOB公式的变量
		evaluator.putVariable("rmb", rmb.toString());
		evaluator.putVariable("priceFob", orderFob.toString());
		evaluator.putVariable("priceProfit", orderProfit.toString());
		evaluator.putVariable("importTax", importTax.toString());
		evaluator.putVariable("facSum", facSum.toString());
		evaluator.putVariable("fobSum", fobSum.toString());
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
		List<?> containerList = this.getOrderDao().getRecords(
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
		// 样品数量
		Long boxCount = obj.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
		}
		evaluator.putVariable("priceCharge", orderCharge.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("cube", cube.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		evaluator.putVariable("boxCount", boxCount.toString());
		evaluator.putVariable("insureRate", insureRate.toString());
		evaluator.putVariable("insureAddRate", insureAddRate.toString());
		float res = 0;
		try {
			String result = evaluator.evaluate(cau);
			result = ContextUtil.getObjByPrecision(Float.parseFloat(result),
					"orderPricePrecision");
			return Float.parseFloat(result);
		} catch (Exception e) {
			res = 0;
		}
		return res;

	}

	// 根据文件路径导入
	public List<?> saveReport(Integer orderId, String filename, boolean cover,
			Integer currencyId, boolean excelFlag) {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("orderReport");
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
		List<?> detailEles = this.getReportDao().getOrderDetails(orderId);
		List<CotOrderDetail> detailElesBak = new ArrayList();
		for (int i = 0; i < detailEles.size(); i++) {
			CotOrderDetail det = (CotOrderDetail) detailEles.get(i);
			CotOrderDetail clone = (CotOrderDetail) SystemUtil.deepClone(det);
			clone.setPicImg(null);
			detailElesBak.add(clone);
		}

		// 查询所有样品编号
		TreeMap<String, CotOrderDetail> mapEleId = new TreeMap<String, CotOrderDetail>();
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
		// 取得业务配置
		CotPriceCfg cotPriceCfg = this.getReportDao().getPriceCfg();
		Sequece sequece = new Sequece(false);

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
		// 如果没有序列号这列或者序列号的值是空,自动填值
		int sortTemp = 0;
		boolean isHaveSort = false;

		boolean rowChk = false;
		for (int i = 4; i < sheet.getRows(); i++) {
			// 新建样品对象
			CotOrderDetail detail = null;
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
			String eleSize = "";
			String eleSizeInch = "";
			boolean isChangeL = false;

			// 单重
			float boxWeigth = 0f;
			float gWeigh = 0f;
			float nWeigh = 0f;

			// 厂价
			Float priceFac = 0f;
			// 外销价
			Float priceOut = 0f;

			// 用于计算装箱数
			long boxObCount = 0;

			Integer hsIdTemp = 0;
			float tuiLv = 0f;

			// 判断是否是子货号
			boolean isChild = false;

			boolean boxCountFlag = false;
			boolean containerCountFlag = false;

			// 创建动态bean
			// BasicDynaBean db = new BasicDynaBean();

			for (int j = 0; j < sheet.getColumns(); j++) {
				// 表头
				Cell headCtn = sheet.getCell(j, 1);
				Cell row = sheet.getCell(j, i);
				String rowCtn = row.getContents();

				// 如果没转换成数字cell,默认的最长小数位是3位
				if (row.getType() == CellType.NUMBER
						|| row.getType() == CellType.NUMBER_FORMULA) {
					NumberCell nc = (NumberCell) row;
					rowCtn = df.format(nc.getValue());
				}
				if (headCtn.getContents().equals("ELE_ID")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"Article No. can not be empty");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"The length of Article No. can not exceed 100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (cover == true) {
						boolean ck = false;
						for (int k = 0; k < detailElesBak.size(); k++) {
							CotOrderDetail orderDetail = (CotOrderDetail) detailElesBak
									.get(k);
							if (rowCtn.trim().toLowerCase().equals(
									orderDetail.getEleId().toLowerCase())) {
								ck = true;
								rowChk = true;
								detail = orderDetail;
								detailElesBak.remove(orderDetail);
								coverNum++;
								break;
							}
						}
						if (ck == false && rowChk == false) {
							rowChk = false;
							detail = new CotOrderDetail();
							detail.setEleId(rowCtn.trim().toUpperCase());
							if (cotEleCfg != null) {
								// 设置样品默认值
								detail = this.getReportDao().setOrderDefault(
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
						detail = new CotOrderDetail();
						detail.setEleId(rowCtn.trim().toUpperCase());
						if (cotEleCfg != null) {
							// 设置样品默认值
							detail = this.getReportDao().setOrderDefault(
									cotEleCfg, detail);
						}
						if (excelFlag == false) {
							detail.setPriceOutUint(currencyId);
						}
					}
					if (detail.getBoxObCount() != null) {
						boxObCount = detail.getBoxObCount();
					}
					// 处理中英文规格
					if (detail.getBoxL() != null) {
						boxL = detail.getBoxL();
					}
					if (detail.getBoxW() != null) {
						boxW = detail.getBoxW();
					}
					if (detail.getBoxH() != null) {
						boxH = detail.getBoxH();
					}
					if (detail.getBoxLInch() != null) {
						boxLInch = detail.getBoxLInch();
					}
					if (detail.getBoxWInch() != null) {
						boxWInch = detail.getBoxWInch();
					}
					if (detail.getBoxHInch() != null) {
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
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setSortNo(elemod.intValue());
						} else {
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
					detail.setEleNameEn(rowCtn.trim());
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
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"Parent Article No. does not exist!");
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
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Date format is incorrect");
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
								// String facNo = sequece.getFactoryNo();
								CotSeqService cotSeqServiceImpl = new CotSeqServiceImpl();
								String facNo = cotSeqServiceImpl.getFacNo();
								// String facNo = seq.getAllSeqByType("facNo",
								// null);
								cotFactory.setFactoryNo(facNo);
								this.getReportDao().create(cotFactory);
								// sequece.saveSeq("factory");
								// seq.saveSeq("facNo");
								cotSeqServiceImpl.saveSeq("factoryNo");
								// 将新类别添加到已有的map中
								mapShortName.put(temp.toLowerCase(), cotFactory
										.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"Save Supplier exception");
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("PRICE_OUT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceOut = Float.parseFloat(rowCtn.trim());
						if (priceOut < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Priced at not less than 0");
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
					if (headCtn.getContents().equals("BOX_COUNT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setBoxCount(elemod.longValue());
						boxCountFlag = true;
					}
					if (headCtn.getContents().equals("CONTAINER_COUNT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setContainerCount(elemod.longValue());
						containerCountFlag = true;
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
					e.printStackTrace();
					CotMsgVO cotMsgVO = new CotMsgVO(i, j,
							"The number of cell values can only be!");
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
				if (headCtn.getContents().equals("BOX_REMARK_CN")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
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
					if (detail.getSortNo() == null) {
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
					if (isChangeL)
						detail.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
				}
				if (!eleSizeInch.equals("")) {
					detail.setEleInchDesc(eleSizeInch);
				} else {
					if (isChangeL)
						detail.setEleInchDesc(boxLInch + "*" + boxWInch + "*"
								+ boxHInch);
				}
				if (detail.getBoxCount() == null) {
					detail.setBoxCount(0l);
				}
				if (detail.getContainerCount() == null) {
					detail.setContainerCount(0l);
				}

				// 如果有数量,没填箱数
				if (detail.getBoxCount() != 0
						&& detail.getContainerCount() == 0) {
					if (boxObCount != 0) {
						if (detail.getBoxCount() % boxObCount != 0) {
							Double cc = Math.ceil(detail.getBoxCount()
									.floatValue()
									/ boxObCount);
							detail.setContainerCount(cc.longValue());
							// 判断业务配置是否满箱计算数据
							if (cotPriceCfg.getOrderTip() != null
									&& cotPriceCfg.getOrderTip() == 1) {
								detail.setBoxCount(cc.longValue() * boxObCount);
							}
						} else {
							detail.setContainerCount(detail.getBoxCount()
									/ boxObCount);
						}
					}
				}
				// 如果有箱数,没填数量
				if (detail.getBoxCount() == 0
						&& detail.getContainerCount() != 0) {
					detail.setBoxCount(detail.getContainerCount() * boxObCount);
				}

				// 如果是覆盖时
				if (cover && containerCountFlag && !boxCountFlag) {
					detail.setBoxCount(detail.getContainerCount() * boxObCount);
				}
				if (cover && !containerCountFlag && boxCountFlag) {
					if (boxObCount != 0) {
						if (detail.getBoxCount() % boxObCount != 0) {
							Double cc = Math.ceil(detail.getBoxCount()
									.floatValue()
									/ boxObCount);
							detail.setContainerCount(cc.longValue());
							// 判断业务配置是否满箱计算数据
							if (cotPriceCfg.getOrderTip() != null
									&& cotPriceCfg.getOrderTip() == 1) {
								detail.setBoxCount(cc.longValue() * boxObCount);
							}
						} else {
							detail.setContainerCount(detail.getBoxCount()
									/ boxObCount);
						}
					}
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
						if (detail.getOrderPrice() != null) {
							priceOut = detail.getOrderPrice().floatValue();
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

				// 取得随机数做KEY
				String rdm = RandomStringUtils.randomNumeric(8);

				// 判断该对象是否已存在mapEleId中,有的话,移除旧的
				if (detail.getId() != null) {
					Iterator<?> it = mapEleId.keySet().iterator();
					while (it.hasNext()) {
						String temp = (String) it.next();
						CotOrderDetail orderDetail = mapEleId.get(temp);
						if (orderDetail.getId() != null
								&& detail.getId().intValue() == orderDetail
										.getId().intValue()) {
							it.remove();
						}
					}
				}

				// 新增设置默认锁定外销价
				if (detail.getId() == null) {
					detail.setCheckFlag(1);
				}
				mapEleId.put(rdm, detail);
				// 增加成功条数
				successNum++;
			}
			if (rowChk == true) {
				i--;
			} else {
				detailElesBak = new ArrayList();
				for (int m = 0; m < detailEles.size(); m++) {
					CotOrderDetail det = (CotOrderDetail) detailEles.get(m);
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
		ctx.getSession().setAttribute("orderReport", mapEleId);
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
		// 取得业务配置
		CotPriceCfg cotPriceCfg = this.getReportDao().getPriceCfg();
		// 先查询所有订单明细的货号
		List<?> detailEles = this.getReportDao().getOrderDetails(orderId);
		List<CotOrderDetail> detailElesBak = new ArrayList();
		for (int i = 0; i < detailEles.size(); i++) {
			CotOrderDetail det = (CotOrderDetail) detailEles.get(i);
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
		Sequece sequece = new Sequece(false);
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
			workbook = Workbook.getWorkbook(file, setting);
		} catch (Exception e) {
			file.delete();
			return null;
		}
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int i = rowNum;
		// 新建样品对象
		CotOrderDetail detail = null;
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
		String eleSize = "";
		String eleSizeInch = "";
		boolean isChangeL = false;
		// 单重
		float boxWeigth = 0f;
		float gWeigh = 0f;
		float nWeigh = 0f;
		// 厂价
		Float priceFac = 0f;
		// 外销价
		Float priceOut = 0f;
		// 判断是否有删除最后一行
		boolean isDel = false;
		// 用于计算装箱数
		long boxObCount = 0;
		// 标识是否是子货号
		boolean isChild = false;

		boolean boxCountFlag = false;
		boolean containerCountFlag = false;

		Integer hsIdTemp = 0;
		float tuiLv = 0f;
		ctx.getSession().removeAttribute("orderReport");
		TreeMap<String, CotOrderDetail> mapObj = new TreeMap<String, CotOrderDetail>();
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
				// 如果没转换成数字cell,默认的最长小数位是3位
				if (row.getType() == CellType.NUMBER
						|| row.getType() == CellType.NUMBER_FORMULA) {
					NumberCell nc = (NumberCell) row;
					rowCtn = df.format(nc.getValue());
				}
				if (headCtn.getContents().equals("ELE_ID")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"Article No. can not be empty");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"The length of Article No. can not exceed 100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (cover == true) {
						boolean ck = false;
						for (int k = 0; k < detailElesBak.size(); k++) {
							CotOrderDetail orderDetail = (CotOrderDetail) detailElesBak
									.get(k);
							if (rowCtn.trim().toLowerCase().equals(
									orderDetail.getEleId().toLowerCase())) {
								ck = true;
								rowChk = true;
								detail = orderDetail;
								detailElesBak.remove(orderDetail);
								break;
							}
						}
						if (ck == false && rowChk == false) {
							rowChk = false;
							detail = new CotOrderDetail();
							detail.setEleId(rowCtn.trim().toUpperCase());
							if (cotEleCfg != null) {
								// 设置样品默认值
								detail = this.getReportDao().setOrderDefault(
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
						detail = new CotOrderDetail();
						detail.setEleId(rowCtn.trim().toUpperCase());
						if (cotEleCfg != null) {
							// 设置样品默认值
							detail = this.getReportDao().setOrderDefault(
									cotEleCfg, detail);
						}
						if (excelFlag == false) {
							detail.setPriceOutUint(currencyId);
						}
					}
					if (detail.getBoxObCount() != null) {
						boxObCount = detail.getBoxObCount();
					}
					// 处理中英文规格
					if (detail.getBoxL() != null) {
						boxL = detail.getBoxL();
					}
					if (detail.getBoxW() != null) {
						boxW = detail.getBoxW();
					}
					if (detail.getBoxH() != null) {
						boxH = detail.getBoxH();
					}
					if (detail.getBoxLInch() != null) {
						boxLInch = detail.getBoxLInch();
					}
					if (detail.getBoxWInch() != null) {
						boxWInch = detail.getBoxWInch();
					}
					if (detail.getBoxHInch() != null) {
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
					detail.setEleNameEn(rowCtn.trim());
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
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"Parent Article No. does not exist!");
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
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Date format is incorrect");
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
						// 去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");
						Integer factoryFlag = this.getReportDao()
								.isExistFactoryShortName(temp);
						if (factoryFlag == null) {
							cotFactory.setFactoryName(temp);
							cotFactory.setShortName(temp);
							cotFactory.setFactroyTypeidLv1(1);
							try {
								// String facNo = sequece.getFactoryNo();
								// String facNo = seq.getAllSeqByType("facNo",
								// null);
								// cotFactory.setFactoryNo(facNo);
								this.getReportDao().create(cotFactory);
								// sequece.saveSeq("factory");
								CotSeqService cotSeqServiceImpl = new CotSeqServiceImpl();
								cotSeqServiceImpl.saveSeq("factoryNo");
								// seq.saveSeq("facNo");
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"Save Supplier exception");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
							detail.setFactoryId(cotFactory.getId());
						} else {
							detail.setFactoryId(factoryFlag);
						}
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
						isChangeL = true;
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
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("PRICE_OUT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceOut = Float.parseFloat(rowCtn.trim());
						if (priceOut < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Priced at not less than 0!");
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
					if (headCtn.getContents().equals("BOX_COUNT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setBoxCount(elemod.longValue());
						boxCountFlag = true;
					}
					if (headCtn.getContents().equals("CONTAINER_COUNT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setContainerCount(elemod.longValue());
						containerCountFlag = true;
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
					CotMsgVO cotMsgVO = new CotMsgVO(i, j,
							"The number of cell values can only be!");
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
				if (headCtn.getContents().equals("BOX_REMARK_CN")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
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
					if (isChangeL)
						detail.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
				}
				if (!eleSizeInch.equals("")) {
					detail.setEleInchDesc(eleSizeInch);
				} else {
					if (isChangeL)
						detail.setEleInchDesc(boxLInch + "*" + boxWInch + "*"
								+ boxHInch);
				}

				if (detail.getBoxCount() == null) {
					detail.setBoxCount(0l);
				}
				if (detail.getContainerCount() == null) {
					detail.setContainerCount(0l);
				}

				// 如果有数量,没填箱数
				if (detail.getBoxCount() != 0
						&& detail.getContainerCount() == 0) {
					if (boxObCount != 0) {
						if (detail.getBoxCount() % boxObCount != 0) {
							Double cc = Math.ceil(detail.getBoxCount()
									.floatValue()
									/ boxObCount);
							detail.setContainerCount(cc.longValue());
							// 判断业务配置是否满箱计算数据
							if (cotPriceCfg.getOrderTip() != null
									&& cotPriceCfg.getOrderTip() == 1) {
								detail.setBoxCount(cc.longValue() * boxObCount);
							}
						} else {
							detail.setContainerCount(detail.getBoxCount()
									/ boxObCount);
						}
					}
				}
				// 如果有箱数,没填数量
				if (detail.getBoxCount() == 0
						&& detail.getContainerCount() != 0) {
					detail.setBoxCount(detail.getContainerCount() * boxObCount);
				}

				// 如果是覆盖时
				if (cover && containerCountFlag && !boxCountFlag) {
					detail.setBoxCount(detail.getContainerCount() * boxObCount);
				}
				if (cover && !containerCountFlag && boxCountFlag) {
					if (boxObCount != 0) {
						if (detail.getBoxCount() % boxObCount != 0) {
							Double cc = Math.ceil(detail.getBoxCount()
									.floatValue()
									/ boxObCount);
							detail.setContainerCount(cc.longValue());
							// 判断业务配置是否满箱计算数据
							if (cotPriceCfg.getOrderTip() != null
									&& cotPriceCfg.getOrderTip() == 1) {
								detail.setBoxCount(cc.longValue() * boxObCount);
							}
						} else {
							detail.setContainerCount(detail.getBoxCount()
									/ boxObCount);
						}
					}
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
						if (detail.getOrderPrice() != null) {
							priceOut = detail.getOrderPrice().floatValue();
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
				ctx.getSession().setAttribute("orderReport", mapObj);
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
		ctx.getSession().removeAttribute("orderReport");
	}

	// 清空Pan的缓存
	public void removePanSession() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("CheckOrderMachine");
	}

	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价(excel导入)
	public CotOrderDetail findDetailByEleIdExcel(String rdm,
			boolean isUsePriceOut, Map<?, ?> map, String liRunCau[]) {
		WebContext ctx = WebContextFactory.get();
		Object temp = ctx.getSession().getAttribute("orderReport");
		CotOrderDetail cotOrderDetail = null;
		if (temp != null) {
			TreeMap<String, CotOrderDetail> mapExcel = (TreeMap<String, CotOrderDetail>) temp;
			cotOrderDetail = mapExcel.get(rdm);
		} else {
			return null;
		}

		Float facSum = this.getFacSum();

		// 进口税
		Float importTax = 0f;
		Object importTaxObj = map.get("importTax");
		if (importTaxObj != null && !"".equals(importTaxObj.toString())) {
			importTax = Float.parseFloat(importTaxObj.toString());
		}

		// 格式化数字
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
		if (cotOrderDetail.getPriceFac() != null
				&& cotOrderDetail.getPriceFacUint() != null) {
			for (int i = 0; i < listCur.size(); i++) {
				CotCurrency cur = (CotCurrency) listCur.get(i);
				if (cur.getId().intValue() == cotOrderDetail.getPriceFacUint()
						.intValue()) {
					Float rate = cur.getCurRate();
					Float fac = cotOrderDetail.getPriceFac();
					rmb = rate * fac;
					break;
				}
			}
		}

		String cau_Li = "";
		// 价格条款
		Object clauseObj = map.get("clauseTypeId");
		Integer clauseId = 0;
		if (clauseObj != null && !"".equals(clauseObj.toString())) {
			clauseId = Integer.parseInt(clauseObj.toString());
			if (clauseId == 1 || clauseId == 6) {
				cau_Li = liRunCau[0];
			}
			if (clauseId == 3 || clauseId == 7 || clauseId == 8) {
				cau_Li = liRunCau[1];
			}
			if (clauseId == 4 || clauseId == 9) {
				cau_Li = liRunCau[2];
			}
		}

		// 利润率
		Float priceProfit = 0f;
		if (cotOrderDetail.getLiRun() != null) {
			priceProfit = cotOrderDetail.getLiRun();
		}
		// FOB费用
		Object priceFobObj = map.get("orderFob");
		Float priceFob = 0f;
		if (priceFobObj != null && !"".equals(priceFobObj.toString())) {
			priceFob = Float.parseFloat(priceFobObj.toString());
		}
		// 汇率
		Object priceRateObj = map.get("orderRate");
		Float priceRate = 0f;
		if (priceRateObj != null && !"".equals(priceRateObj.toString())) {
			priceRate = Float.parseFloat(priceRateObj.toString());
		}
		if (priceRate == 0) {
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == currencyId.intValue()) {
					priceRate = cur.getCurRate();
				}
			}
		}
		// Fob价总和
		Float fobSum = this.getFobSumDetail();
		// 整柜运费
		Object priceChargeObj = map.get("orderCharge");
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
		// 通过集装箱类型查找柜体积
		CotContainerType cotContainerType = (CotContainerType) this
				.getOrderDao().getById(CotContainerType.class, containerTypeId);
		// 样品CBM
		Float cbm = cotOrderDetail.getBoxCbm();
		if (cbm == null) {
			cbm = 0f;
		}
		// 柜体积
		Float cube = 0f;
		if (cotContainerType != null
				&& cotContainerType.getContainerCube() != null) {
			cube = cotContainerType.getContainerCube();
		}
		// 装箱率
		Long boxObCount = 0l;
		if (cotOrderDetail.getBoxObCount() != null) {
			boxObCount = cotOrderDetail.getBoxObCount();
		}
		// 样品数量
		Long boxCount = cotOrderDetail.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
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
			if (cotOrderDetail.getPriceOutUint() != null
					&& cotOrderDetail.getPriceOut() != null) {
				// 取得厂家报价的人民币值
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == cotOrderDetail
							.getPriceOutUint().intValue()) {
						obj = cotOrderDetail.getPriceOut() * cur.getCurRate();
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
				cotOrderDetail.setOrderPrice(Double.parseDouble(Float.toString(rmbOut)));
			} else {
				cotOrderDetail.setOrderPrice(0.0);
			}
		} else {
			// 根据选择的价格条款获得价格公式
			String cauHql = "select c.expessionIn from CotCalculation c, CotClause obj where c.id=obj.calId and obj.id="
					+ clauseId;
			List<String> cauList = this.getOrderDao().find(cauHql);
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
			evaluator.putVariable("boxCount", boxCount.toString());
			evaluator.putVariable("insureRate", insureRate.toString());
			evaluator.putVariable("insureAddRate", insureAddRate.toString());
			evaluator.putVariable("importTax", importTax.toString());
			evaluator.putVariable("facSum", facSum.toString());
			evaluator.putVariable("fobSum", fobSum.toString());
			if (cotOrderDetail.getTuiLv() != null) {
				evaluator.putVariable("tuiLv", cotOrderDetail.getTuiLv()
						.toString());
			} else {
				evaluator.putVariable("tuiLv", "0");
			}
			Double res = 0.0;
			try {
				String result = evaluator.evaluate(cau);
				result = ContextUtil.getObjByPrecision(
						Double.parseDouble(result), "orderPricePrecision");
				res = Double.parseDouble(result);

			} catch (Exception e) {
				res = 0.0;
			}
			cotOrderDetail.setOrderPrice(res);
			rmbOut = res.floatValue();
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
		lirun.putVariable("boxCount", boxCount.toString());
		lirun.putVariable("insureRate", insureRate.toString());
		lirun.putVariable("insureAddRate", insureAddRate.toString());
		lirun.putVariable("importTax", importTax.toString());
		lirun.putVariable("facSum", facSum.toString());
		lirun.putVariable("fobSum", fobSum.toString());
		if (cotOrderDetail.getTuiLv() != null) {
			lirun.putVariable("tuiLv", cotOrderDetail.getTuiLv().toString());
		} else {
			lirun.putVariable("tuiLv", "0");
		}
		float liRes = 0;
		try {
			if (!cau_Li.equals("")) {
				String li = lirun.evaluate(cau_Li);
				liRes = Float.parseFloat(df.format(Float.parseFloat(li)));
				if (liRes <= -1000) {
					liRes = -999;
				}
				if (liRes >= 1000) {
					liRes = 999;
				}
			}

		} catch (Exception e) {
			liRes = 0;
		}
		cotOrderDetail.setLiRun(liRes);
		return cotOrderDetail;
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}

	// 判断明细中是否存在该货号
	public boolean checkIsExistEle(Integer mainId, String eleId) {
		String sql = "select obj.id from CotOrderDetail obj from where obj.orderId="
				+ mainId
				+ " and upper(obj.eleId)='"
				+ eleId.toUpperCase()
				+ "'";
		List<?> list = this.getOrderDao().find(sql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 根据类型获得合同条款
	public List<?> getCotContractList(int type) {
		String sql = "from CotContract obj where obj.contractType=" + type
				+ " order by obj.id";
		return this.getOrderDao().find(sql);
	}

	// 查询VO记录
	public List<?> getOrderVOList(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getOrderDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setOrderTime((Timestamp) obj[1]);
				orderVO.setSendTime((Date) obj[2]);
				orderVO.setCustomerShortName((String) obj[3]);
				orderVO.setOrderNo((String) obj[4]);
				orderVO.setEmpsName((String) obj[5]);
				orderVO.setClauseTypeId((Integer) obj[6]);
				orderVO.setCurrencyId((Integer) obj[7]);
				orderVO.setPayTypeId((Integer) obj[8]);
				orderVO.setTotalCount((Integer) obj[9]);
				orderVO.setTotalContainer((Integer) obj[10]);
				orderVO.setTotalCBM((Double) obj[11]);
				orderVO.setTotalMoney((Float) obj[12]);
				orderVO.setOrderStatus((Long) obj[13]);
				orderVO.setPoNo((String) obj[14]);
				orderVO.setOrderRate((Double) obj[15]);
				orderVO.setOrderCompose((String) obj[16]);
				orderVO.setTotalGross((Double) obj[17]);
				orderVO.setCustId((Integer) obj[18]);
				orderVO.setAllPinName((String) obj[19]);
				orderVO.setOrderLcDate((Date) obj[20]);
				orderVO.setOrderLcDelay((Date) obj[21]);
				orderVO.setOrderEarnest((Double) obj[22]);
				orderVO.setFactoryId((Integer) obj[23]);
				orderVO.setCanOut((Integer) obj[24]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id) {
		String sql = "select obj.id,obj.targetPortEnName from CotTargetPort obj where obj.id="
				+ id;
		List<?> list = this.getOrderDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
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
		List<?> list = this.getOrderDao().queryForLists(hql, obj);
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
		List<?> listOrder = this.getOrderDao().queryForLists(hqlOrder, obj3);

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
		List<?> listGiven = this.getOrderDao().queryForLists(hqlGiven, obj2);

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
			// // 填充厂家简称
			// List<?> facList = this.getOrderDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 1;
			rtn[1] = detail;
			return rtn;
		} else if (flag == 2) {
			Object[] objTemp = (Object[]) listOrder.get(0);
			CotOrderDetail detail = (CotOrderDetail) objTemp[0];
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getOrderDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 2;
			rtn[1] = detail;
			return rtn;
		} else {
			Object[] objTemp = (Object[]) listGiven.get(0);
			CotGivenDetail detail = (CotGivenDetail) objTemp[0];
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getOrderDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 3;
			rtn[1] = detail;
			return rtn;
		}
	}

	// 将该货号对应的样品转成明细
	public CotOrderDetail changeEleToOrderDetail(String eleId) {
		// 查找样品对象
		String hql = "from CotElementsNew obj where obj.eleId = '" + eleId
				+ "'";
		List<?> list = this.getOrderDao().find(hql);
		if (list.size() == 0) {
			return null;
		}
		CotElementsNew cotElements = (CotElementsNew) list.get(0);
		// 新建报价明细对象
		CotOrderDetail cotOrderDetail = new CotOrderDetail();
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
					beanUtils.setProperty(cotOrderDetail, propEle[i], value);
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
			List facList = this.getOrderDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (cotOrderDetail.getFactoryId() != null
						&& cotFactory.getId().intValue() == cotOrderDetail
								.getFactoryId().intValue()) {
					cotOrderDetail.setFactoryShortName(cotFactory
							.getShortName());
				}
			}
			return cotOrderDetail;
		}
	}

	// 保存或者更新主订单
	public Integer saveByExcel(CotOrder cotOrder, String orderTime,
			String sendTime, boolean oldFlag) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

		CotOrderMb cotOrderMb = null;
		if (cotOrder.getId() == null) {
			cotOrderMb = new CotOrderMb();
		} else {
			String hql = "from CotOrderMb obj where obj.fkId="
					+ cotOrder.getId();
			List<?> listMB = this.getOrderDao().find(hql);
			cotOrderMb = (CotOrderMb) listMB.get(0);
		}

		if (oldFlag) {
			String hql = "select obj.picImg from CotCustMb obj where obj.fkId="
					+ cotOrder.getCustId();
			List<?> listMB = this.getOrderDao().find(hql);
			byte[] pm = (byte[]) listMB.get(0);
			cotOrderMb.setPicImg(pm);
			cotOrderMb.setPicSize(pm.length);
		}

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			// cotOrder.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
			if (orderTime != null && !"".equals(orderTime)) {
				cotOrder.setOrderTime(new Timestamp(sdf.parse(orderTime)
						.getTime()));// 下单时间
			}
			if (sendTime != null && !"".equals(sendTime)) {
				cotOrder.setSendTime(new Date(sdf2.parse(sendTime).getTime()));// 交货时间
			}
			// cotOrder.setAddPerson(cotEmps.getEmpsName());// 操作人
			cotOrder.setEmpId(cotEmps.getId());// 操作人编号

			if (cotOrder.getOrderRate() != null && cotOrder.getOrderRate() != 0) {
			} else {
				CotCurrency cotCurrency = (CotCurrency) this.getOrderDao()
						.getById(CotCurrency.class, cotOrder.getCurrencyId());
				cotOrder.setOrderRate(cotCurrency.getCurRate().doubleValue());// 汇率
			}
			if (cotOrder.getId() == null) {
				// 更新全局序列号
				// Sequece sequece = new Sequece(false);
				// sequece.saveSeq("order");
				// SetNoServiceImpl setNoService = new SetNoServiceImpl();
				// setNoService.saveSeq("order", cotOrder.getOrderTime()
				// .toString());
				CotSeqService cotSeqService = new CotSeqServiceImpl();
				cotSeqService.saveCustSeq(cotOrder.getCustId(), "order", cotOrder.getOrderTime().toString());
				cotSeqService.saveSeq("order");
			}
			List<CotOrder> list = new ArrayList<CotOrder>();
			list.add(cotOrder);
			this.getOrderDao().saveOrUpdateRecords(list);
			if (oldFlag) {
				cotOrderMb.setFkId(cotOrder.getId());
				List newMb = new ArrayList();
				newMb.add(cotOrderMb);
				this.getOrderDao().saveOrUpdateRecords(newMb);
			}
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主订单成功");
			cotSyslog.setOpModule("order");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotOrder.getOrderNo());
			sysLogService.addSysLogByObj(cotSyslog);
			// 保存明细
			this.saveDetail(cotOrder.getId(), cotOrder.getCurrencyId(),
					cotOrder.getCustId());
			// 修改主单的总数量,总箱数,总体积,总金额
			// this.modifyCotOrderTotal(cotOrder.getId());
			return cotOrder.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("excel导入保存订单出错");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主订单失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("order");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotOrder.getOrderNo());
			sysLogService.addSysLogByObj(cotSyslog);
			return -1;
			// throw new Exception("excel导入保存订单出错");
		}

	}

	// 保存明细
	public void saveDetail(Integer orderId, Integer currencyId, Integer custId) {
		Object obj = SystemUtil.getObjBySession(null, "excelTemp");
		Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
		List<CotOrderDetail> records = new ArrayList<CotOrderDetail>();
		List<CotOrderPic> imgList = new ArrayList<CotOrderPic>();
		// List custPcs=this.getCustPcs(custId);
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		if (orderMap != null) {
			byte[] zwtpByte = this.getZwtpPic();
			Iterator<?> it = orderMap.keySet().iterator();
			while (it.hasNext()) {
				String rdm = (String) it.next();
				CotOrderDetail cotOrderDetail = orderMap.get(rdm);
				if (cotOrderDetail.getOrderId() == null
						|| (cotOrderDetail.getOrderId() != null && cotOrderDetail
								.getOrderId().intValue() == orderId.intValue())) {
					// 表格下拉框选择"请选择"时
					if (cotOrderDetail.getFactoryId() != null
							&& cotOrderDetail.getFactoryId() == 0) {
						cotOrderDetail.setFactoryId(null);
					}
					cotOrderDetail.setCurrencyId(currencyId);
					cotOrderDetail.setOrderId(orderId);
					cotOrderDetail.setEleAddTime(new Date(System
							.currentTimeMillis()));// 添加时间
					cotOrderDetail.setAddTime(new Date(System
							.currentTimeMillis()));// 编辑时间

					if (cotOrderDetail.getBoxCount() == null) {
						cotOrderDetail.setBoxCount(0l);
					}

					// 如果是覆盖,并且数量有变化.判断是否有多少货导入出货
					if (cotOrderDetail.getId() != null) {
						// 判断数量有没有变化
						CotOrderDetail oldDetail = (CotOrderDetail) this
								.getOrderDao().getObjectById("CotOrderDetail",
										cotOrderDetail.getId());
						Long oldVal = oldDetail.getBoxCount();
						Long newVal = cotOrderDetail.getBoxCount();
						if (newVal > oldVal) {
							// 出货
							if (oldDetail.getUnBoxSend().intValue() == 1) {
								cotOrderDetail.setUnBoxSend(0);
							}
							cotOrderDetail.setUnBoxCount(oldDetail
									.getUnBoxCount()
									+ (newVal - oldVal));
							// 采购
							if (oldDetail.getUnBoxSend4OrderFac().intValue() == 1) {
								cotOrderDetail.setUnBoxSend4OrderFac(0);
							}
							cotOrderDetail.setUnBoxCount4OrderFac(oldDetail
									.getUnBoxCount4OrderFac()
									+ (newVal - oldVal));
						}
						// 如果减小数量
						if (newVal < oldVal) {
							// 出货
							if (oldDetail.getUnBoxSend().intValue() == 1) {
								cotOrderDetail.setBoxCount(oldDetail
										.getBoxCount());
							} else {
								if (newVal >= (oldVal - oldDetail
										.getUnBoxCount())) {
									cotOrderDetail.setUnBoxCount(oldDetail
											.getUnBoxCount()
											+ (newVal - oldVal));
									if (cotOrderDetail.getUnBoxCount() == 0) {
										cotOrderDetail.setUnBoxSend(1);
									}
								} else {
									cotOrderDetail.setBoxCount(oldDetail
											.getBoxCount());
								}
							}
							// 采购
							if (oldDetail.getUnBoxSend4OrderFac().intValue() == 1) {
								cotOrderDetail.setBoxCount(oldDetail
										.getBoxCount());
							} else {
								if (newVal >= (oldVal - oldDetail
										.getUnBoxCount4OrderFac())) {
									cotOrderDetail
											.setUnBoxCount4OrderFac(oldDetail
													.getUnBoxCount4OrderFac()
													+ (newVal - oldVal));
									if (cotOrderDetail.getUnBoxCount4OrderFac() == 0) {
										cotOrderDetail.setUnBoxSend4OrderFac(1);
									}
								} else {
									cotOrderDetail.setBoxCount(oldDetail
											.getBoxCount());
								}
							}
						}
					} else {
						cotOrderDetail.setUnBoxCount(new Float(cotOrderDetail
								.getBoxCount())); // 未出货数量
						cotOrderDetail.setUnBoxCount4OrderFac(new Long(
								cotOrderDetail.getBoxCount())); // 未采购数量
						cotOrderDetail.setUnBoxSend(0); // 是否完全出货 0：否 1：是
						cotOrderDetail.setUnBoxSend4OrderFac(0); // 是否完全采购
						// 0：否
					}

					// 设置总数
					if (cotOrderDetail.getContainerCount() == null
							|| cotOrderDetail.getBoxCbm() == null) {
						cotOrderDetail.setTotalCbm(0f);
					} else {
						cotOrderDetail.setTotalCbm(cotOrderDetail
								.getContainerCount()
								* cotOrderDetail.getBoxCbm());
					}
					if (cotOrderDetail.getBoxCount() == null
							|| cotOrderDetail.getPriceFac() == null) {
						cotOrderDetail.setTotalFac(0f);
					} else {
						cotOrderDetail.setTotalFac(cotOrderDetail.getBoxCount()
								* cotOrderDetail.getPriceFac());
					}
					if (cotOrderDetail.getContainerCount() == null
							|| cotOrderDetail.getBoxGrossWeigth() == null) {
						cotOrderDetail.setTotalGrossWeigth(0f);
					} else {
						cotOrderDetail.setTotalGrossWeigth(cotOrderDetail
								.getContainerCount()
								* cotOrderDetail.getBoxGrossWeigth());
					}
					if (cotOrderDetail.getContainerCount() == null
							|| cotOrderDetail.getBoxNetWeigth() == null) {
						cotOrderDetail.setTotalNetWeigth(0f);
					} else {
						cotOrderDetail.setTotalNetWeigth(cotOrderDetail
								.getContainerCount()
								* cotOrderDetail.getBoxNetWeigth());
					}
					if (cotOrderDetail.getBoxCount() == null
							|| cotOrderDetail.getOrderPrice() == null) {
						cotOrderDetail.setTotalMoney(0.0);
					} else {
						cotOrderDetail.setTotalMoney(cotOrderDetail
								.getBoxCount()
								* cotOrderDetail.getOrderPrice());
					}

					// 1：是

					// 判断利润率是否超过数据库范围
					Float liRes = cotOrderDetail.getLiRun();
					if (liRes == null) {
						cotOrderDetail.setLiRun(0f);
					} else {
						if (liRes <= -1000) {
							cotOrderDetail.setLiRun(-999f);
						}
						if (liRes >= 1000) {
							cotOrderDetail.setLiRun(999f);
						}
					}
					records.add(cotOrderDetail);
				}
			}
		}

		// 返回的结果数组，数组长度一般为增加对象的个数,1为成功，0为失败
		// 暂时都设为成功
		int[] resultCodes = new int[records.size()];
		if (records.size() == 0) {
			resultCodes = new int[1];
		}
		for (int i = 0; i < resultCodes.length; i++) {
			resultCodes[i] = 1;
		}

		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		this.getOrderDao().saveOrUpdateRecords(records);

		// 保存到系统日记表
		CotSyslog cotSyslog = new CotSyslog();
		cotSyslog.setEmpId(empId);
		cotSyslog.setOpMessage("excel导入后保存订单明细成功");
		cotSyslog.setOpModule("order");
		cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
		cotSyslog.setOpType(1);
		sysLogService.addSysLogByObj(cotSyslog);

		byte[] zwtpByte = this.getZwtpPic();
		for (int i = 0; i < records.size(); i++) {
			CotOrderDetail cotOrderDetail = records.get(i);

			// for (int j = 0; j < custPcs.size(); j++) {
			// CotCustPc cotCustPc = (CotCustPc) custPcs.get(j);
			// if(cotOrderDetail.getEleTypeidLv3()!=null &&
			// cotOrderDetail.getEleTypeidLv3().intValue()==cotCustPc.getCategoryId().intValue()){
			// CotOrderPc orderPc=new CotOrderPc();
			// orderPc.setCategoryId(cotOrderDetail.getEleTypeidLv3());
			// orderPc.setOrderId(orderId);
			// orderPc.setOrderDetailId(cotOrderDetail.getId());
			// orderPc.setPcRemark(cotCustPc.getPcRemark());
			// orderPc.setPhone(cotCustPc.getPhone());
			// List pcList = new ArrayList();
			// pcList.add(orderPc);
			// // 逐条添加，避免数据量大的时候，内存溢出
			// impOpService.saveImg(pcList);
			// }
			// }

			String type = cotOrderDetail.getType();
			Integer srcId = cotOrderDetail.getSrcId();
			if (type != null) {
				CotOrderPic orderPic = null;
				// 查询该明细是否已经有图片
				if (cotOrderDetail.getId() != null) {
					String hql = "from CotOrderPic obj where obj.fkId="
							+ cotOrderDetail.getId();
					List ls = this.getOrderDao().find(hql);
					if (ls != null && ls.size() > 0) {
						orderPic = (CotOrderPic) ls.get(0);
					} else {
						orderPic = new CotOrderPic();
					}
				} else {
					orderPic = new CotOrderPic();
				}
				if (type.equals("ele")) {
					CotElePic cotElePic = impOpService
							.getElePicImgByEleId(srcId);
					orderPic.setEleId(cotElePic.getEleId());
					orderPic.setPicImg(cotElePic.getPicImg());
					orderPic.setPicSize(cotElePic.getPicImg().length);
					orderPic.setPicName(cotElePic.getEleId());
				}
				if (type.equals("price")) {
					CotPricePic cotPricePic = impOpService.getPricePic(srcId);
					orderPic.setEleId(cotPricePic.getEleId());
					orderPic.setPicImg(cotPricePic.getPicImg());
					orderPic.setPicSize(cotPricePic.getPicImg().length);
					orderPic.setPicName(cotPricePic.getEleId());
				}
				if (type.equals("order")) {
					CotOrderPic cotOrderPic = impOpService.getOrderPic(srcId);
					orderPic.setEleId(cotOrderPic.getEleId());
					orderPic.setPicImg(cotOrderPic.getPicImg());
					orderPic.setPicSize(cotOrderPic.getPicImg().length);
					orderPic.setPicName(cotOrderPic.getEleId());
				}
				if (type.equals("given")) {
					CotGivenPic cotGivenPic = impOpService.getGivenPic(srcId);
					orderPic.setEleId(cotGivenPic.getEleId());
					orderPic.setPicImg(cotGivenPic.getPicImg());
					orderPic.setPicSize(cotGivenPic.getPicImg().length);
					orderPic.setPicName(cotGivenPic.getEleId());
				}
				if (type.equals("none")) {
					if (orderPic.getPicImg() == null) {
						// excel导入后查找样品档案有没有该货号,有就取样品档案的图片
						String strSql = "select pic from CotElementsNew obj,CotElePic pic where obj.id=pic.fkId and obj.eleId = '"
								+ cotOrderDetail.getEleId() + "'";
						List list = this.getOrderDao().find(strSql);
						if (list != null && list.size() > 0) {
							CotElePic cotElePic = (CotElePic) list.get(0);
							orderPic.setEleId(cotElePic.getEleId());
							orderPic.setPicImg(cotElePic.getPicImg());
							orderPic.setPicSize(cotElePic.getPicImg().length);
							orderPic.setPicName(cotElePic.getEleId());
						} else {
							orderPic.setEleId(cotOrderDetail.getEleId());
							orderPic.setPicImg(zwtpByte);
							orderPic.setPicSize(zwtpByte.length);
							orderPic.setPicName(cotOrderDetail.getEleId());
						}
					}
				}
				orderPic.setFkId(cotOrderDetail.getId());
				// 添加到图片数组
				imgList.add(orderPic);
			}
		}
		// 插入图片信息表
		for (int i = 0; i < imgList.size(); i++) {
			CotOrderPic cotOrderPic = (CotOrderPic) imgList.get(i);
			List picList = new ArrayList();
			picList.add(cotOrderPic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveOrUpdateImg(picList);
		}

		// 保存配件及成本信息
		List listFitting = new ArrayList();
		List listPrice = new ArrayList();
		for (int i = 0; i < records.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) records.get(i);
			String type = detail.getType();
			Integer srcId = detail.getSrcId();
			if (type == null) {
				continue;
			}
			if (detail.getId() != null) {
				continue;
			}
			// 从样品来
			if (type.equals("ele") || type.equals("none")) {
				// 从excel,要再根据货号获取编号
				if (type.equals("none")) {
					String tempSql = "select obj.id from CotElementsNew obj where obj.eleId='"
							+ detail.getEleId() + "'";
					List list = this.getOrderDao().find(tempSql);
					if (list != null && list.size() > 0) {
						srcId = (Integer) list.get(0);
					}
				}
				if (srcId == 0) {
					continue;
				}
				// 获得原配件信息
				String hql = "from CotEleFittings obj where obj.eleId=" + srcId;
				List list = this.getOrderDao().find(hql);
				for (int j = 0; j < list.size(); j++) {
					CotEleFittings eleFittings = (CotEleFittings) list.get(j);
					CotOrderFittings orderFittings = new CotOrderFittings();
					orderFittings.setOrderDetailId(detail.getId());
					orderFittings.setOrderId(detail.getOrderId());
					orderFittings.setEleId(detail.getEleId());
					orderFittings.setEleName(detail.getEleName());
					// priceFittings.setBoxCount(boxCount);
					orderFittings.setFitNo(eleFittings.getFitNo());
					orderFittings.setFitName(eleFittings.getFitName());
					orderFittings.setFitDesc(eleFittings.getFitDesc());
					orderFittings.setFacId(eleFittings.getFacId());
					orderFittings.setFitUseUnit(eleFittings.getFitUseUnit());
					orderFittings
							.setFitUsedCount(eleFittings.getFitUsedCount());
					orderFittings.setFitCount(eleFittings.getFitCount());
					orderFittings.setFitPrice(eleFittings.getFitPrice());
					// priceFittings.setOrderFitCount()
					orderFittings.setFitTotalPrice(eleFittings
							.getFitTotalPrice());
					orderFittings.setFitRemark(eleFittings.getFitRemark());
					orderFittings.setFittingId(eleFittings.getFittingId());

					listFitting.add(orderFittings);
				}
				// 获得原成本信息
				String hqlPrice = "from CotElePrice obj where obj.eleId="
						+ srcId;
				List listTemp = this.getOrderDao().find(hqlPrice);
				for (int j = 0; j < listTemp.size(); j++) {
					CotElePrice elePrice = (CotElePrice) listTemp.get(j);
					CotOrderEleprice orderEleprice = new CotOrderEleprice();

					orderEleprice.setOrderDetailId(detail.getId());
					orderEleprice.setOrderId(detail.getOrderId());
					orderEleprice.setPriceName(elePrice.getPriceName());
					orderEleprice.setPriceUnit(elePrice.getPriceUnit());
					orderEleprice.setPriceAmount(elePrice.getPriceAmount());
					orderEleprice.setRemark(elePrice.getRemark());

					listPrice.add(orderEleprice);
				}
			}
			// 从订单来
			if (type.equals("order")) {
				// 获得原配件信息
				String hql = "from CotOrderFittings obj where obj.orderDetailId="
						+ srcId;
				List list = this.getOrderDao().find(hql);
				for (int j = 0; j < list.size(); j++) {
					CotOrderFittings orderFittings = (CotOrderFittings) list
							.get(j);
					CotOrderFittings clone = (CotOrderFittings) SystemUtil
							.deepClone(orderFittings);
					clone.setId(null);
					clone.setOrderDetailId(detail.getId());
					clone.setOrderId(detail.getOrderId());
					listFitting.add(clone);
				}

				// 获得原成本信息
				String hqlPrice = "from CotOrderEleprice obj where obj.orderDetailId="
						+ srcId;
				List listTemp = this.getOrderDao().find(hqlPrice);
				for (int j = 0; j < listTemp.size(); j++) {
					CotOrderEleprice eleprice = (CotOrderEleprice) listTemp
							.get(j);
					CotOrderEleprice clone = (CotOrderEleprice) SystemUtil
							.deepClone(eleprice);
					clone.setId(null);
					clone.setOrderDetailId(detail.getId());
					clone.setOrderId(detail.getOrderId());
					listPrice.add(clone);
				}
			}
			// 从报价来
			if (type.equals("price")) {
				// 获得原配件信息
				String hql = "from CotPriceFittings obj where obj.priceDetailId="
						+ srcId;
				List list = this.getOrderDao().find(hql);
				for (int j = 0; j < list.size(); j++) {
					CotPriceFittings priceFittings = (CotPriceFittings) list
							.get(j);
					CotOrderFittings orderFittings = new CotOrderFittings();
					orderFittings.setOrderDetailId(detail.getId());
					orderFittings.setOrderId(detail.getOrderId());
					orderFittings.setEleId(detail.getEleId());
					orderFittings.setEleName(detail.getEleName());
					// orderFittings.setBoxCount(boxCount);
					orderFittings.setFitNo(priceFittings.getFitNo());
					orderFittings.setFitName(priceFittings.getFitName());
					orderFittings.setFitDesc(priceFittings.getFitDesc());
					orderFittings.setFacId(priceFittings.getFacId());
					orderFittings.setFitUseUnit(priceFittings.getFitUseUnit());
					orderFittings.setFitUsedCount(priceFittings
							.getFitUsedCount());
					orderFittings.setFitCount(priceFittings.getFitCount());
					orderFittings.setFitPrice(priceFittings.getFitPrice());
					// orderFittings.setOrderFitCount()
					orderFittings.setFitTotalPrice(priceFittings
							.getFitTotalPrice());
					orderFittings.setFitRemark(priceFittings.getFitRemark());
					orderFittings.setFittingId(priceFittings.getFittingId());

					listFitting.add(orderFittings);
				}

				// 获得原成本信息
				String hqlPrice = "from CotPriceEleprice obj where obj.priceDetailId="
						+ srcId;
				List listTemp = this.getOrderDao().find(hqlPrice);
				for (int j = 0; j < listTemp.size(); j++) {
					CotPriceEleprice priceEleprice = (CotPriceEleprice) listTemp
							.get(j);
					CotOrderEleprice orderEleprice = new CotOrderEleprice();
					orderEleprice.setOrderDetailId(detail.getId());
					orderEleprice.setOrderId(detail.getOrderId());
					orderEleprice.setPriceName(priceEleprice.getPriceName());
					orderEleprice.setPriceUnit(priceEleprice.getPriceUnit());
					orderEleprice
							.setPriceAmount(priceEleprice.getPriceAmount());
					orderEleprice.setRemark(priceEleprice.getRemark());
					listPrice.add(orderEleprice);
				}
			}

		}
		try {
			this.getOrderDao().saveRecords(listFitting);
			this.getOrderDao().saveRecords(listPrice);
			// 生成配件分析记录
			this.saveFitAnys(records);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		// 修改主单的总数量,总箱数,总体积,总金额
		// this.modifyCotOrderTotalAction(orderId);

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "order";
		while (it.hasNext()) {
			CotOrderDetail detail = (CotOrderDetail) it.next();
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
		// 清空内存
		ctx.getSession().removeAttribute("SessionEXCELTEMP");
	}

	// 根据起运港港编号获得起运港名称
	public String[] getShipPortNameById(Integer id) {
		String sql = "select obj.id,obj.shipPortNameEn from CotShipPort obj where obj.id="
				+ id;
		List<?> list = this.getOrderDao().find(sql);
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

	// 通过货号修改Map中对应的明细并返回对象
	public CotOrderDetail updateObjByEle(String eleId, String property,
			String value) {
		CotOrderDetail cotOrderDetail = getOrderMapValue(eleId.toLowerCase());
		if (cotOrderDetail == null)
			return null;
		try {
			BeanUtils.setProperty(cotOrderDetail, property, value);
			this.setOrderMap(eleId.toLowerCase(), cotOrderDetail);
			return cotOrderDetail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 先查询出所有明细,再克隆主订单保存后将新主单的id填充到旧明细中,保存旧明细.再以旧明细的编号为key 新明细为value组成map
	 * 再根据旧明细编号查询出旧明细图片,再根据旧明细编号所对应的新明细的编号填到旧明细图片的fkid, 再组成一个新明细图片,保存
	 * [另存时不另存订单的其他费用.订单总金额更改为货物总金额] 需要拷贝订单明细的配件和成本信息
	 */

	public boolean saveAs(String newOrderNo, Integer mainId) throws Exception {
		// 判断新单号是否存在
		String hql = "select obj.id from CotOrder obj where obj.orderNo='"
				+ newOrderNo + "'";
		List<?> res = this.getOrderDao().find(hql);
		if (res.size() > 0) {
			return false;
		} else {
			// 获得所有订单明细
			String dhql = "from CotOrderDetail obj where obj.orderId=" + mainId;
			List<?> resDetail = this.getOrderDao().find(dhql);
			CotOrder cotOrder = (CotOrder) this.getOrderDao().getById(
					CotOrder.class, mainId);
			CotOrder clone = (CotOrder) SystemUtil.deepClone(cotOrder);
			clone.setId(null);
			clone.setOrderNo(newOrderNo);
			clone.setCanOut(0);
			clone.setOrderStatus(0l);

			// 其它信息
			clone.setQuality(cotOrder.getQuality());
			clone.setColours(cotOrder.getColours());
			clone.setSaleUnit(cotOrder.getSaleUnit());
			clone.setHandleUnit(cotOrder.getHandleUnit());
			clone.setAssortment(cotOrder.getAssortment());
			clone.setComments(cotOrder.getComments());
			clone.setShippingMark(cotOrder.getShippingMark());
			clone.setIsCheckAgent(cotOrder.getIsCheckAgent());
			clone.setBuyer(cotOrder.getBuyer());
			clone.setSeller(cotOrder.getSeller());
			clone.setAgent(cotOrder.getAgent());
			// 佣金
			clone.setCommisionScale(cotOrder.getCommisionScale());
			
			clone.setClientFile(null);
			clone.setScFileName(null);

			// 更改主单的价格为所有明细的总额,因为不能克隆订单其他费用
			Double total = 0.0;
			for (int i = 0; i < resDetail.size(); i++) {
				CotOrderDetail cotOrderDetail = (CotOrderDetail) resDetail
						.get(i);
				total += cotOrderDetail.getTotalMoney();
			}
			clone.setTotalMoney(total.doubleValue());
			List temp = new ArrayList();
			temp.add(clone);
			try {
				// 保存单号
				CotSeqService cotSeqService = new CotSeqServiceImpl();
				cotSeqService.saveCustSeq(cotOrder.getCustId(), "order",
						cotOrder.getOrderTime().toString());
				cotSeqService.saveSeq("order");

				// 保存主单
				this.getOrderDao().saveRecords(temp);
				// 在拷贝原麦标
				String hqlMb = "from CotOrderMb obj where obj.fkId=" + mainId;
				List<?> listMb = this.getOrderDao().find(hqlMb);
				if (listMb.size() > 0) {
					CotOrderMb cotOrderMb = (CotOrderMb) listMb.get(0);
					CotOrderMb newMb = new CotOrderMb();
					newMb.setFkId(clone.getId());
					newMb.setPicImg(cotOrderMb.getPicImg());
					newMb.setPicSize(cotOrderMb.getPicImg().length);
					List tempMb = new ArrayList();
					tempMb.add(newMb);
					// 保存麦标
					this.getOrderDao().saveRecords(tempMb);
				}
				// 在拷贝原产品标
				String proMb = "from CotProductMb obj where obj.fkId=" + mainId;
				List<?> proMbList = this.getOrderDao().find(proMb);
				if (proMbList.size() > 0) {
					CotProductMb cotProductMb = (CotProductMb) proMbList.get(0);
					CotProductMb newMb = new CotProductMb();
					newMb.setFkId(clone.getId());
					newMb.setPicImg(cotProductMb.getPicImg());
					newMb.setPicSize(cotProductMb.getPicImg().length);
					List tempMb = new ArrayList();
					tempMb.add(newMb);
					// 保存产品标
					this.getOrderDao().saveRecords(tempMb);
				}

			} catch (DAOException e) {
				e.printStackTrace();
				throw new Exception("另存订单失败!");
			}
			String ids = "";
			Map map = new HashMap();
			List newDetail = new ArrayList();
			List idsList = new ArrayList();
			for (int i = 0; i < resDetail.size(); i++) {
				CotOrderDetail cotOrderDetail = (CotOrderDetail) resDetail
						.get(i);
				idsList.add(cotOrderDetail.getId());
				cotOrderDetail.setOrderId(clone.getId());
				ids += cotOrderDetail.getId() + ",";
				map.put(cotOrderDetail.getId(), cotOrderDetail);
				cotOrderDetail.setId(null);

				// 清空出货标识
				if (cotOrderDetail.getBoxCount() != null) {
					cotOrderDetail.setUnBoxCount(cotOrderDetail.getBoxCount()
							.floatValue());
				} else {
					cotOrderDetail.setUnBoxCount(0f);
				}
				cotOrderDetail.setUnBoxSend(0);// 0:没有全部发货

				// 清空生产合同标识
				cotOrderDetail.setUnBoxCount4OrderFac(cotOrderDetail
						.getBoxCount());
				cotOrderDetail.setUnBoxSend4OrderFac(0);// 0:没有全部发货
				cotOrderDetail.setOrderFacno(null);
				cotOrderDetail.setOrderFacnoid(null);
				newDetail.add(cotOrderDetail);
			}
			if (resDetail.size() != 0) {
				// 图片
				String pichql = "from CotOrderPic obj where obj.fkId in ("
						+ ids.substring(0, ids.length() - 1) + ")";
				List<?> picList = this.getOrderDao().find(pichql);

				// 配件
				String fithql = "from CotOrderFittings obj where obj.orderDetailId in ("
						+ ids.substring(0, ids.length() - 1) + ")";
				List<?> fitList = this.getOrderDao().find(fithql);

				// 成本
				String pricehql = "from CotOrderEleprice obj where obj.orderDetailId in ("
						+ ids.substring(0, ids.length() - 1) + ")";
				List<?> priceList = this.getOrderDao().find(pricehql);

				try {
					// 另存订单明细
					this.getOrderDao().saveRecords(newDetail);
					// 在拷贝order phone
					String pchql = "from CotOrderPc obj where obj.orderId="
							+ mainId;
					List<?> pcList = this.getOrderDao().find(pchql);
					if (pcList.size() > 0) {
						for (int j = 0; j < pcList.size(); j++) {
							CotOrderPc cotOrderPc = (CotOrderPc) pcList.get(j);
							for (int i = 0; i < idsList.size(); i++) {
								Integer oldId = (Integer) idsList.get(i);
								if (cotOrderPc.getOrderDetailId().intValue() == oldId
										.intValue()) {
									CotOrderDetail detail = (CotOrderDetail) newDetail
											.get(i);
									CotOrderPc newMb = new CotOrderPc();
									newMb.setCategoryId(cotOrderPc
											.getCategoryId());
									newMb.setOrderDetailId(detail.getId());
									newMb.setOrderId(detail.getOrderId());
									newMb.setTempLate(cotOrderPc.getTempLate());
									newMb.setPcRemark(cotOrderPc.getPcRemark());
									// newMb.setPhone(cotOrderPc.getPhone());
									List tempMb = new ArrayList();
									tempMb.add(newMb);
									// 保存order phone
									this.getOrderDao().saveRecords(tempMb);
								}
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("另存报价明细失败!");
				}
				// 图片
				List list = new ArrayList();
				for (int i = 0; i < picList.size(); i++) {
					CotOrderPic cotOrderPic = (CotOrderPic) picList.get(i);
					CotOrderDetail newde = (CotOrderDetail) map.get(cotOrderPic
							.getFkId());
					cotOrderPic.setFkId(newde.getId());
					cotOrderPic.setId(null);
					list.add(cotOrderPic);
				}
				// 配件
				List listFit = new ArrayList();
				for (int i = 0; i < fitList.size(); i++) {
					CotOrderFittings orderFittings = (CotOrderFittings) fitList
							.get(i);
					CotOrderDetail newde = (CotOrderDetail) map
							.get(orderFittings.getOrderDetailId());
					orderFittings.setOrderDetailId(newde.getId());
					orderFittings.setOrderId(clone.getId());
					orderFittings.setId(null);
					listFit.add(orderFittings);
				}

				// 成本
				List listPrice = new ArrayList();
				for (int i = 0; i < priceList.size(); i++) {
					CotOrderEleprice orderEleprice = (CotOrderEleprice) priceList
							.get(i);
					CotOrderDetail newde = (CotOrderDetail) map
							.get(orderEleprice.getOrderDetailId());
					orderEleprice.setOrderDetailId(newde.getId());
					orderEleprice.setOrderId(clone.getId());
					orderEleprice.setId(null);
					listPrice.add(orderEleprice);
				}
				try {
					// 生成包材分析数据
					this.savePackAnys(newDetail);
					// 另存订单明细的图片
					this.getOrderDao().saveRecords(list);
					// 另存配件
					this.getOrderDao().saveRecords(listFit);
					// 另存成本
					this.getOrderDao().saveRecords(listPrice);

					// 生成采购分析
					this.saveFitAnys(newDetail);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("另存失败!请联系管理员!");
				}
			}
			return true;
		}
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
		List<?> list = this.getOrderDao().find(hql);

		String eleSql = "from CotElePic obj where obj.fkId=";
		String picSql = "from CotOrderPic obj where obj.fkId=";
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
				CotOrderDetail orderDetail = this.getOrderMapValue(rdmAry[i]);
				CotOrderDetail newDetail = setPriceByTong(orderDetail,
						cotElements, eleStr, boxStr, otherStr);

				if (newDetail.getId() != null) {
					if (isPic) {
						List eleList = this.getOrderDao().find(
								eleSql + cotElements.getId());
						List picList = this.getOrderDao().find(
								picSql + newDetail.getId());
						CotElePic cotElePic = (CotElePic) eleList.get(0);
						CotOrderPic cotOrderPic = (CotOrderPic) picList.get(0);
						cotOrderPic.setEleId(newDetail.getEleId());
						cotOrderPic.setPicImg(cotElePic.getPicImg());
						cotOrderPic.setPicName(cotElePic.getPicName());
						cotOrderPic.setPicSize(cotElePic.getPicSize());
						detailPicList.add(cotOrderPic);
					}
				} else {
					newDetail.setType("ele");
					newDetail.setSrcId(cotElements.getId());
				}
				this.setOrderMap(rdmAry[i], newDetail);
				listNew.add(newDetail);
			}
		}
		// 同步样品图片到报价(已存在的明细直接覆盖图片)
		if (detailPicList.size() > 0) {
			this.getOrderDao().updateRecords(detailPicList);
		}
		return listNew;
	}

	// 根据同步选择项同步更新报价
	public CotOrderDetail setPriceByTong(CotOrderDetail old,
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
					old.setOrderPrice(Double.parseDouble(newEle.getPriceOut().toString()));
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
					if (old.getContainerCount() != null
							&& newEle.getBoxObCount() != null) {
						Long co = old.getContainerCount()
								* newEle.getBoxObCount();
						old.setBoxCount(co);
						if (old.getOrderPrice() != null) {
							old.setTotalMoney(old.getOrderPrice() * co);
						}
					}
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

	// 得到excel的内存map的key集合
	public List<?> getRdmList() {
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("orderReport");
		List<String> list = new ArrayList<String>();
		if (obj == null) {
			return list;
		}
		TreeMap<String, CotOrderDetail> map = (TreeMap<String, CotOrderDetail>) obj;
		Iterator<?> it = map.keySet().iterator();
		while (it.hasNext()) {
			String rdm = (String) it.next();
			list.add(rdm);
		}
		return list;
	}

	// 保存其他费用
	public Boolean addOtherList(List<?> details) {
		try {
			this.getOrderDao().saveRecords(details);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存其他费用出错!");
			return false;
		}
	}

	// 保存计划日期
	public void saveTime(Integer orderId) {
		try {
			// 调用存储过程更新订单对应的生产合同
			Integer[] parmas = new Integer[1];
			parmas[0] = orderId;
			this.getOrderDao()
					.callProc("{call insert_order_status(?)}", parmas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 保存应收帐款
	public Boolean addRecvList(List<?> details) {
		try {
			this.getOrderDao().saveRecords(details);
			return true;
		} catch (DAOException e) {
			logger.error("保存应收帐款出错!");
			return false;
		}
	}

	// 删除其他费用
	public Boolean deleteOtherList(List<?> details) {
		// 修改其他费用的导入标识为0
		List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < details.size(); i++) {
			CotFinaceOther finaceOther = (CotFinaceOther) this.getOrderDao()
					.getById(CotFinaceOther.class, (Integer) details.get(i));
			if (finaceOther.getIsImport() != null) {
				CotFinaceOther src = (CotFinaceOther) this.getOrderDao()
						.getById(CotFinaceOther.class,
								finaceOther.getIsImport());
				// 有可能来源的其他费用已被删除
				if (src != null) {
					src.setIsImport(0);
					list.add(src);
				}
			}
		}
		try {
			this.getOrderDao().updateRecords(list);
			this.getOrderDao().deleteRecordByIds(details, "CotFinaceOther");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("修改其他费用异常或删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 删除应收帐
	public Boolean deleteRecvList(List<?> details) {
		List<Integer> ids = new ArrayList<Integer>();
		List<CotFinaceOther> other = new ArrayList<CotFinaceOther>();
		for (int i = 0; i < details.size(); i++) {
			CotFinaceAccountrecv recv = (CotFinaceAccountrecv) details.get(i);
			ids.add(recv.getId());
			if (recv.getFinaceOtherId() != null) {
				CotFinaceOther finaceOther = (CotFinaceOther) this
						.getOrderDao().getById(CotFinaceOther.class,
								recv.getFinaceOtherId());
				finaceOther.setStatus(0);
				other.add(finaceOther);
			}
		}
		try {
			this.getOrderDao().deleteRecordByIds(ids, "CotFinaceAccountrecv");
			if (other.size() > 0) {
				this.getOrderDao().updateRecords(other);
			}
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 更改生产合同其他费用的导入标识为1
	public void updateOrderFacIsImport(String ids) {
		String hql = "from CotFinaceOther where id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		List<?> listTemp = this.getOrderDao().find(hql);
		Iterator<?> it = listTemp.iterator();
		while (it.hasNext()) {
			CotFinaceOther cotFinaceOther = (CotFinaceOther) it.next();
			cotFinaceOther.setIsImport(1);
		}
		this.getOrderDao().updateRecords(listTemp);
	}

	// 根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id) {
		CotFinaceOther finaceOther = (CotFinaceOther) this.getOrderDao()
				.getById(CotFinaceOther.class, id);
		return finaceOther;
	}

	// 根据编号获得对象
	public CotOrderStatus getOrderStatusById(Integer id) {
		CotOrderStatus orderStatus = (CotOrderStatus) this.getOrderDao()
				.getById(CotOrderStatus.class, id);
		return orderStatus;
	}

	// 更新其他费用
	public Boolean updateOtherList(List<?> details) {
		try {
			this.getOrderDao().updateRecords(details);
		} catch (Exception e) {
			logger.error("更新其他费用异常", e);
		}
		return true;
	}

	// 更新其他费用的导入标识为1,剩余金额为0
	public Boolean updateStatus(String ids) {
		try {
			String hql = "from CotFinaceOther obj where obj.id in ("
					+ ids.substring(0, ids.length() - 1) + ")";
			List<CotFinaceOther> list = this.getOrderDao().find(hql);
			List<CotFinaceOther> listNew = new ArrayList<CotFinaceOther>();
			for (int i = 0; i < list.size(); i++) {
				CotFinaceOther finaceOther = (CotFinaceOther) list.get(i);
				finaceOther.setRemainAmount(0.0);
				finaceOther.setStatus(1);
				listNew.add(finaceOther);
			}
			this.getOrderDao().updateRecords(listNew);
		} catch (Exception e) {
			logger.error("更新其他费用异常", e);
		}
		return true;
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
	public Float deleteByIds(List<?> ids, List<?> isImport, Float curRate) {
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
			List listTemp = this.getOrderDao().find(hql);
			Double allRmb = 0.0;
			Integer orderId = 0;
			for (int i = 0; i < listTemp.size(); i++) {
				CotFinaceOther finaceOther = (CotFinaceOther) listTemp.get(i);
				orderId = finaceOther.getFkId();
				CotCurrency old = map.get(finaceOther.getCurrencyId());
				if (finaceOther.getFlag().equals("A")) {
					allRmb += finaceOther.getAmount() * Double.parseDouble(old.getCurRate()+"");
				} else {
					allRmb -= finaceOther.getAmount() * Double.parseDouble(old.getCurRate()+"");
				}
			}
			// 更新主订单的总金额
			Float res = 0f;
			if (allRmb != 0) {
				CotOrder order = this.getOrderById(orderId);
				if (curRate == null || curRate == 0) {
					CotCurrency oldOrder = map.get(order.getCurrencyId());
					curRate = oldOrder.getCurRate();
				}
				Float cha = allRmb.floatValue() / curRate;
				res = order.getTotalMoney().floatValue() - cha;
				order.setTotalMoney(res.doubleValue());
				List listOrder = new ArrayList();
				listOrder.add(order);
				this.getOrderDao().updateRecords(listOrder);
			}

			this.getOrderDao().deleteRecordByIds(ids, "CotFinaceOther");
			List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
			for (int i = 0; i < isImport.size(); i++) {
				Integer isImp = (Integer) isImport.get(i);
				if (isImp != null && isImp != 0) {
					// 还原生产合同的其他费用的导入标识为0
					CotFinaceOther cotFinaceOther = (CotFinaceOther) this
							.getOrderDao().getById(CotFinaceOther.class, isImp);
					if (cotFinaceOther != null) {
						cotFinaceOther.setIsImport(0);
						list.add(cotFinaceOther);
					}
				}
			}
			this.getOrderDao().updateRecords(list);
			if (allRmb != 0) {
				return res;
			} else {
				return null;
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 删除应收帐
	public Boolean deleteByAccount(List<?> ids) {
		try {
			List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
			for (int i = 0; i < ids.size(); i++) {
				CotFinaceAccountrecv recv = (CotFinaceAccountrecv) this
						.getOrderDao().getById(CotFinaceAccountrecv.class,
								(Integer) ids.get(i));
				if (recv.getFinaceOtherId() != null) {
					// 还原订单的其他费用的剩余金额
					CotFinaceOther cotFinaceOther = (CotFinaceOther) this
							.getOrderDao().getById(CotFinaceOther.class,
									recv.getFinaceOtherId());
					cotFinaceOther.setRemainAmount(cotFinaceOther
							.getRemainAmount()
							+ recv.getAmount());
					cotFinaceOther.setStatus(0);
					list.add(cotFinaceOther);
				}
			}
			this.getOrderDao().updateRecords(list);
			this.getOrderDao().deleteRecordByIds(ids, "CotFinaceAccountrecv");
			return true;
		} catch (DAOException e) {
			return false;
		}
	}

	// 删除收款记录
	public Boolean deleteByRecvDetail(List<Integer> ids) {
		Integer mainId = 0;
		float allAmou = 0f;
		// 所有收款明细对应的应收款
		CotFinaceAccountrecv ac = null;
		Float cha = 0f;
		for (int i = 0; i < ids.size(); i++) {
			CotFinacerecvDetail finacerecvDetail = (CotFinacerecvDetail) this
					.getOrderDao().getById(CotFinacerecvDetail.class,
							ids.get(i));
			mainId = finacerecvDetail.getFinaceRecvid();
			allAmou += finacerecvDetail.getCurrentAmount();
			// 将货款转回应收帐
			if (ac == null) {
				CotFinaceAccountrecv acOld = (CotFinaceAccountrecv) this
						.getOrderDao().getById(CotFinaceAccountrecv.class,
								finacerecvDetail.getRecvId());
				ac = (CotFinaceAccountrecv) SystemUtil.deepClone(acOld);
			}
			// 币种转换
			Double mon = this.updatePrice(finacerecvDetail.getCurrentAmount()
					.floatValue(), finacerecvDetail.getCurrencyId(), ac
					.getCurrencyId());
			cha += mon.floatValue();
		}
		ac.setRealAmount(ac.getRealAmount() - cha);
		ac.setRemainAmount(ac.getRemainAmount() + cha);
		if ("预收货款".equals(ac.getFinaceName())) {
			ac.setZhRemainAmount(ac.getZhRemainAmount() - cha);
		} else {
			ac.setZhRemainAmount(ac.getZhRemainAmount() + cha);
		}
		ac.setStatus(0);
		List<CotFinaceAccountrecv> accountList = new ArrayList<CotFinaceAccountrecv>();
		accountList.add(ac);
		// 修改应收账款
		this.getOrderDao().updateRecords(accountList);
		// 修改主单的未冲帐金额
		List<CotFinacerecv> recvList = new ArrayList<CotFinacerecv>();
		CotFinacerecv recvOld = (CotFinacerecv) this.getOrderDao().getById(
				CotFinacerecv.class, mainId);
		CotFinacerecv recv = (CotFinacerecv) SystemUtil.deepClone(recvOld);
		recv.setRemainAmount(recv.getRemainAmount() + allAmou);
		recvList.add(recv);

		// 查询该收款记录是否已存在溢收款,没有的新建
		String hql = " from CotFinaceOther obj where obj.finaceName='溢收款' and obj.fkId="
				+ recv.getId() + " and obj.factoryId=" + recv.getCustId();
		List list = this.getOrderDao().find(hql);
		List listNew = new ArrayList();
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		CotFinaceOther finaceOther = null;
		if (list.size() == 0) {
			finaceOther = new CotFinaceOther();
			finaceOther.setAmount(recv.getRemainAmount());
			finaceOther.setBusinessPerson(empId);
			finaceOther.setCurrencyId(recv.getCurrencyId());
			finaceOther.setFactoryId(recv.getCustId());
			finaceOther.setFinaceName("溢收款");
			finaceOther.setFlag("M");
			finaceOther.setOrderNo(recv.getFinaceNo());
			finaceOther.setFkId(recv.getId());
		} else {
			finaceOther = (CotFinaceOther) list.get(0);
			finaceOther.setAmount(recv.getRemainAmount());
		}
		listNew.add(finaceOther);

		try {
			this.getOrderDao().deleteRecordByIds(ids, "CotFinacerecvDetail");
			this.getOrderDao().updateRecords(recvList);
			this.getOrderDao().saveOrUpdateRecords(listNew);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 通过订单主单编号查找明细,在将明细中的生产合同主单编号组成字符串
	public String checkIsHasFac(Integer orderId) {
		String hql = "from CotOrderDetail obj where obj.orderId=" + orderId;
		List<?> list = this.getOrderDao().find(hql);
		String facIds = "";
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail orderDetail = (CotOrderDetail) list.get(i);
			if (orderDetail.getOrderFacnoid() != null) {
				facIds += orderDetail.getOrderFacnoid() + ",";
			}
		}
		return facIds;
	}

	// 生成应收帐款单号
	public String createRecvNo(Integer custId) {
		// Map idMap = new HashMap<String, Integer>();
		// idMap.put("KH", custId);
		// GenAllSeq seq = new GenAllSeq();
		// String finaceNo = seq.getAllSeqByType("fincaeaccountrecvNo", idMap);
		CotSeqService seq = new CotSeqServiceImpl();
		String finaceNo = seq.getFinaceNeeRecGivenNo(custId);
		return finaceNo;
	}

	// 保存应收帐款
	public void saveAccountRecv(CotFinaceAccountrecv recvDetail,
			String amountDate, String priceScal, String prePrice) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (!"".equals(amountDate) && amountDate != null) {
				recvDetail.setAmountDate(new java.sql.Date(sdf
						.parse(amountDate).getTime()));
			}
			WebContext ctx = WebContextFactory.get();
			Integer empId = (Integer) ctx.getSession().getAttribute("empId");
			List<CotFinaceAccountrecv> records = new ArrayList<CotFinaceAccountrecv>();
			recvDetail.setAddDate(new Date(System.currentTimeMillis()));
			recvDetail.setRealAmount(0.0);
			recvDetail.setRemainAmount(recvDetail.getAmount());
			if (recvDetail.getFinaceName().equals("预收货款")) {
				recvDetail.setZhRemainAmount(0.0);
			} else {
				recvDetail.setZhRemainAmount(recvDetail.getAmount());
			}
			recvDetail.setStatus(0);
			recvDetail.setSource("order");
			recvDetail.setEmpId(empId);
			records.add(recvDetail);
			this.getOrderDao().saveRecords(records);
			// 保存单号
			// Sequece sequece = new Sequece(false);
			// sequece.saveSeq("fincaeaccountrecvNo");
			// GenAllSeq seq = new GenAllSeq();
			// seq.saveSeq("fincaeaccountrecvNo");
			CotSeqService seq = new CotSeqServiceImpl();
			seq.saveSeq("fincaeaccountrecv");

			if (!"".equals(priceScal) && !"".equals(prePrice)) {
				// 将预收货款比例和金额保存到订单
				CotOrder order = (CotOrder) this.getOrderDao().getById(
						CotOrder.class, recvDetail.getFkId());
				CotOrder obj = (CotOrder) SystemUtil.deepClone(order);
				obj.setPrePrice(Float.parseFloat(prePrice));
				obj.setPriceScal(Float.parseFloat(priceScal));
				List<CotOrder> list = new ArrayList<CotOrder>();
				list.add(obj);
				this.getOrderDao().updateRecords(list);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	// 根据币种英文名称查找编号
	public Integer getCurrenyId(String curNameEn) {
		String hql = "select obj.id from CotCurrency obj where obj.curNameEn='"
				+ curNameEn + "'";
		List list = this.getOrderDao().find(hql);
		return (Integer) list.get(0);
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

	// 判断该订单费用是否已被导入出货单和是否已生成应收帐
	public boolean checkIsOut(Integer otherId) {
		CotFinaceOther finaceOther = (CotFinaceOther) this.getOrderDao()
				.getById(CotFinaceOther.class, otherId);
		if (finaceOther.getOutFlag() != null
				&& finaceOther.getOutFlag().intValue() == 1) {
			return true;
		} else {
			if (finaceOther.getStatus() != null
					&& finaceOther.getStatus().intValue() == 1) {
				return true;
			}
			return false;
		}
	}

	// 判断应收帐是否导到出货,是否有收款记录
	public List<Integer> checkIsImport(List<Integer> ids) {
		List<Integer> listNew = new ArrayList();
		for (int i = 0; i < ids.size(); i++) {
			Integer id = ids.get(i);
			String tempHql = "select obj.recvId from CotFinacerecvDetail obj where obj.recvId ="
					+ id;
			List listTemp = this.getOrderDao().find(tempHql);
			if (listTemp.size() == 0) {
				String hql = "select obj.id from CotFinaceOther obj where obj.source='orderRecv' and obj.outFlag="
						+ id;
				List list = this.getOrderDao().find(hql);
				if (list.size() == 0) {
					listNew.add(id);
				}
			}
		}

		return listNew;
	}

	// 判断要删除的订单明细是否已经生成配件采购单
	public List<Integer> checkIsHasFitOrder(String details) {
		List<Integer> listNew = new ArrayList();
		String hql = " from CotFittingsAnys obj where obj.orderdetailId in ("
				+ details.substring(0, details.length() - 1) + ")";
		List list = this.getOrderDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotFittingsAnys fittingsAnys = (CotFittingsAnys) list.get(i);
			if (fittingsAnys.getAnyFlag().equals("C")) {
				listNew.add(fittingsAnys.getOrderdetailId());
			}
		}
		return listNew;
	}

	// 判断要删除的订单明细是否已经生成包材采购单
	public List<Integer> checkIsHasPackOrder(String details) {
		List<Integer> listNew = new ArrayList();
		String hql = " from CotPackingAnys obj where obj.orderdetailId in ("
				+ details.substring(0, details.length() - 1) + ")";
		List list = this.getOrderDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotPackingAnys packingAnys = (CotPackingAnys) list.get(i);
			if (packingAnys.getAnyFlag().equals("C")) {
				listNew.add(packingAnys.getOrderdetailId());
			}
		}
		return listNew;
	}

	// 查询配件信息
	public List findFittingsByIds(String ids, Integer rdm) {
		CotOrderDetail detail = this.getOrderMapValue(rdm.toString());

		// 查找配件字符串ids中是否已导过
		String hql = "select obj.fittingId from CotOrderFittings obj where obj.orderDetailId="
				+ detail.getId()
				+ " and obj.fittingId in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		List list = this.getOrderDao().find(hql);
		if (list != null && list.size() > 0) {
			String temp = "";
			for (int i = 0; i < list.size(); i++) {
				temp += list.get(i) + ",";
			}
			String str = "from CotFittings obj where obj.id not in("
					+ temp.substring(0, temp.length() - 1)
					+ ") and obj.id in (" + ids.substring(0, ids.length() - 1)
					+ ")";
			return this.getOrderDao().find(str);
		} else {
			String str = "from CotFittings obj where obj.id in ("
					+ ids.substring(0, ids.length() - 1) + ")";
			return this.getOrderDao().find(str);
		}
	}

	// 增加
	public void addList(List list) {
		try {
			// 如果配件号为空,或者不是从配件库来的,不生成
			List listNew = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				CotOrderFittings orderFittings = (CotOrderFittings) list.get(i);
				if (!"".equals(orderFittings.getFitNo())
						&& orderFittings.getFittingId() != 0) {
					listNew.add(orderFittings);
				}
			}
			this.getOrderDao().saveRecords(listNew);
			// 保存新的配件信息到分析表
			List listAnys = new ArrayList();
			CotOrderDetail cotOrderDetail = null;
			for (int j = 0; j < listNew.size(); j++) {
				CotOrderFittings eleFittings = (CotOrderFittings) listNew
						.get(j);
				// 判断分析表中是否已含有这个新增的配件
				String hql = "select obj.id from CotFittingsAnys obj where obj.orderdetailId="
						+ eleFittings.getOrderDetailId()
						+ " and obj.fittingId=" + eleFittings.getFittingId();
				List temp = this.getOrderDao().find(hql);
				if (temp != null && temp.size() > 0) {
					continue;
				}
				if (cotOrderDetail == null) {
					cotOrderDetail = (CotOrderDetail) this.getOrderDao()
							.getById(CotOrderDetail.class,
									eleFittings.getOrderDetailId());
				}

				CotFittingsAnys fittingsAnys = new CotFittingsAnys();
				fittingsAnys.setBoxCount(cotOrderDetail.getBoxCount()
						.doubleValue());// 订单数量
				fittingsAnys.setEleId(cotOrderDetail.getEleId());// 货号
				fittingsAnys.setEleName(cotOrderDetail.getEleName());// 产品名称
				fittingsAnys.setFacId(eleFittings.getFacId());// 供应商
				fittingsAnys.setFitBuyUnit(eleFittings.getFitUseUnit());// 领用单位==使用单位
				fittingsAnys.setFitDesc(eleFittings.getFitDesc());
				fittingsAnys.setFitName(eleFittings.getFitName());
				fittingsAnys.setFitNo(eleFittings.getFitNo());
				// 单个用量(用量*数量)
				Double count = eleFittings.getFitUsedCount()
						* eleFittings.getFitCount();
				fittingsAnys.setFitUsedCount(eleFittings.getFitUsedCount());
				fittingsAnys.setFitCount(eleFittings.getFitCount());
				fittingsAnys.setFitPrice(eleFittings.getFitPrice());
				Double num = count * cotOrderDetail.getBoxCount();
				fittingsAnys.setOrderFitCount(num);
				fittingsAnys.setTotalAmount(num * eleFittings.getFitPrice());
				fittingsAnys.setOrderId(cotOrderDetail.getOrderId());
				fittingsAnys.setOrderdetailId(cotOrderDetail.getId());
				fittingsAnys.setAnyFlag("U");
				fittingsAnys.setFittingId(eleFittings.getFittingId());
				listAnys.add(fittingsAnys);
			}
			try {
				if (listAnys.size() > 0) {
					this.getOrderDao().saveRecords(listAnys);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 增加成本
	public void addElePriceList(List list) {
		try {
			this.getOrderDao().saveRecords(list);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void deleteList(List list, String tabName) {
		try {
			List ids = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Integer orderFittingsId = (Integer) list.get(i);
				CotOrderFittings orderFitting = (CotOrderFittings) this
						.getOrderDao().getById(CotOrderFittings.class,
								orderFittingsId);
				String hql = "select obj.id from CotFittingsAnys obj where obj.anyFlag='U' and obj.fittingId="
						+ orderFitting.getFittingId()
						+ " and obj.orderdetailId="
						+ orderFitting.getOrderDetailId();
				List temp = this.getOrderDao().find(hql);
				if (temp != null && temp.size() > 0) {
					ids.add(temp.get(0));
				}
			}
			// 如果配件分析表 还没采购可以删除
			if (ids.size() > 0) {
				this.getOrderDao().deleteRecordByIds(ids, "CotFittingsAnys");
			}
			this.getOrderDao().deleteRecordByIds(list, tabName);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 修改
	public void modifyList(List list) {
		try {
			this.getOrderDao().updateRecords(list);
		} catch (Exception e) {
		}
	}

	// 获得订单配件对象
	public CotOrderFittings getOrderFittingById(Integer id) {
		CotOrderFittings orderFittings = (CotOrderFittings) this.getOrderDao()
				.getById(CotOrderFittings.class, id);
		return orderFittings;
	}

	// 获得订单明细成本对象
	public CotOrderEleprice getElePriceById(Integer id) {
		CotOrderEleprice orderEleprice = (CotOrderEleprice) this.getOrderDao()
				.getById(CotOrderEleprice.class, id);
		return orderEleprice;
	}

	// 判断输入的配件号是否存在.完全存在的话直接加到表格中,有模糊数据弹出层.
	public List findIsExistFitNo(String fitNo, Integer orderDetailId) {
		String hql = "from CotFittings obj where obj.fitNo=?";
		Object[] obj = new Object[1];
		obj[0] = fitNo;
		List list = this.getOrderDao().queryForLists(hql, obj);
		if (list != null && list.size() == 1) {
			CotFittings fittings = (CotFittings) list.get(0);
			// 判断该条配件是否已经添加
			String str = "select obj.fittingId from CotOrderFittings obj where obj.orderDetailId="
					+ orderDetailId + " and obj.fittingId=" + fittings.getId();
			List listStr = this.getOrderDao().find(str);
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
			List listTemp = this.getOrderDao().find(hqlTemp);
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
		List<?> list = this.getOrderDao().find(hql);
		if (list.size() > 0) {
			return (CotEleCfg) list.get(0);
		} else {
			return null;
		}
	}

	// 计算明细的生产价
	public CotOrderDetail updatePriceFac(Integer orderDetailId, Integer rdm,
			Map map) {
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getDefaultEleCfg();
		if (cotEleCfg == null || cotEleCfg.getExpessionFacIn() == null
				|| "".equals(cotEleCfg.getExpessionFacIn())) {
			return null;
		}
		// 得到内存的币种
		List listCur = this.getDicList("currency");
		CotOrderDetail detail = this.getOrderMapValue(rdm.toString());
		if (detail.getPriceFacUint() == null) {
			return null;
		}
		try {
			// 计算出配件成本的RMB总和
			Float allRmbMoney = 0f;
			String hql = "from CotOrderFittings obj where obj.orderDetailId="
					+ orderDetailId;
			List list = this.getOrderDao().find(hql);
			for (int i = 0; i < list.size(); i++) {
				CotOrderFittings orderFittings = (CotOrderFittings) list.get(i);
				Double mo = orderFittings.getFitTotalPrice();
				allRmbMoney += mo.floatValue();
			}

			// 计算出成本的RMB总和
			Float allPriceMoney = 0f;
			String hqlStr = "from CotOrderEleprice obj where obj.orderDetailId="
					+ orderDetailId;
			List listPrice = this.getOrderDao().find(hqlStr);
			for (int i = 0; i < listPrice.size(); i++) {
				CotOrderEleprice orderEleprice = (CotOrderEleprice) listPrice
						.get(i);
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == orderEleprice.getPriceUnit()
							.intValue()) {
						Double mo = orderEleprice.getPriceAmount()
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

			// 直接保存生产价
			detail.setPriceFac(fac);
			detail.setElePrice(allPriceMoney);
			detail.setEleFittingPrice(allRmbMoney);
			if (map != null) {
				map.put("priceFac", fac);
				Float pricePrice = this.getNewPriceByPriceFac(map, rdm
						.toString());
				detail.setOrderPrice(Double.parseDouble(pricePrice.toString()));
			}
			List listDetail = new ArrayList();
			listDetail.add(detail);
			this.getOrderDao().updateRecords(listDetail);
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
		list = this.getOrderDao().find(hql);
		if (list != null && list.size() > 0) {
			CotEleCfg cotEleCfg = (CotEleCfg) list.get(0);
			return cotEleCfg;
		} else {
			return null;
		}
	}

	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String rdm, String packingPrice) {
		CotOrderDetail detail = this.getOrderMapValue(rdm);
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
				this.setOrderMap(rdm, detail);
			} catch (EvaluationException e) {
				e.printStackTrace();
				return -1f;
			}
			return res;
		} else {
			return -1f;
		}
	}

	// 根据订单id获取采购单ids
	public String getOrderFacIds(Integer orderId) {
		String orderfacIds = "";

		String hql = " from CotOrderDetail obj where obj.orderId = " + orderId;
		List<?> list = this.getOrderDao().find(hql);

		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) list.get(i);

			String orderfacId = this.getOrderFacIdsByDetailId(detail.getId());
			if (orderfacIds.indexOf(orderfacId) == -1)
				orderfacIds += orderfacId;
		}
		return orderfacIds;
	}

	// 根据订单明细id获取采购单id
	public String getOrderFacIdsByDetailId(Integer detailId) {
		String hql = " from CotOrderFacdetail obj where obj.orderDetailId = "
				+ detailId;
		List<?> list = this.getOrderDao().find(hql);
		String facIds = "";
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CotOrderFacdetail facDetail = (CotOrderFacdetail) list.get(i);
				facIds += facDetail.getOrderId() + ",";
			}
		}
		return facIds;
	}

	// 修改订单审核状态
	public Timestamp updateOrderStatus(Integer orderId, Integer orderStatus,
			Integer checkPerson) throws Exception {
		CotOrder cotOrder = (CotOrder) this.getOrderDao().getById(
				CotOrder.class, orderId);
		cotOrder.setCotOrderDetails(null);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			// 判断有没有默认模板
			File file = new File(SystemUtil.getRptFilePath() + "order");
			if (!file.exists()) {
				file.mkdirs();
			}
			// 自定义报表
			String rptPath = SystemUtil.getRptFilePath() + "order/PI-"
					+ cotOrder.getOrderNo() + ".pdf";
			PIPdf piPdf = new PIPdf();
			PIService piService = new PIServiceImpl();
			piPdf.setVpInvoice(piService.getCotPIVO(orderId));
			piPdf.setDetailList(piService.getDetailList(orderId));
			piPdf.createPIPDF(rptPath);
			cotOrder.setGivenDate(new Date(System.currentTimeMillis()));
		}

		// 如果变成请求审核状态时,并且如果旧单已经审核过了
		if (orderStatus == 3 && cotOrder.getOrderStatus() == 2) {
			// 1.修改订单的出货状态
			cotOrder.setCanOut(0);
			// 2.如果po状态是审核通过,把状态改成请求审核,并且审核人是同一人
			String facHql = "from CotOrderFac obj where obj.orderId=" + orderId;
			List listFac = this.getOrderDao().find(facHql);
			CotOrderFac cotOrderfac = (CotOrderFac) listFac.get(0);
			if (cotOrderfac.getOrderStatus() == 2) {
				cotOrderfac.setOrderStatus(0l);
				cotOrderfac.setModTime(tmp);// 修改时间
				cotOrderfac.setModPerson(cotEmps.getId());
				if (checkPerson != null) {
					//2012-12-5 pi反审后po的审核人也要固定是MOX,id:47
//					cotOrderfac.setCheckPerson(checkPerson);
					cotOrderfac.setCheckPerson(null);
					//2012-1-4
//					cotOrderfac.setAddPerson(cotOrder.getCheckPerson());
					cotOrderfac.setAddPerson(cotOrder.getAddPerson());
				}
				List listTmp = new ArrayList();
				cotOrderfac.setOrderMBImg(null);
				cotOrderfac.setCotOrderFacdetails(null);
				listTmp.add(cotOrderfac);
				this.getOrderDao().updateRecords(listTmp);
				// 如果已生成invoice 则删除
//				String outHql = "delete from CotOrderOut obj where obj.orderId="
//						+ orderId;
//				QueryInfo queryInfo = new QueryInfo();
//				queryInfo.setSelectString(outHql);
//				this.getOrderDao().executeUpdate(queryInfo, null);

			}else{
				//2012-1-4
//				cotOrderfac.setAddPerson(cotOrder.getCheckPerson());
				cotOrderfac.setAddPerson(cotOrder.getAddPerson());
				List listTmp = new ArrayList();
				cotOrderfac.setOrderMBImg(null);
				cotOrderfac.setCotOrderFacdetails(null);
				listTmp.add(cotOrderfac);
				this.getOrderDao().updateRecords(listTmp);
			}
			cotOrder.setGivenDate(null);
		}
		cotOrder.setOrderStatus(orderStatus.longValue());
		List list = new ArrayList();
		list.add(cotOrder);
		this.getOrderDao().updateRecords(list);
		return tmp;
	}

	// 根据上传的盘点机数据存入内存
	public void saveCheckMachine(List<String> list, HttpServletRequest request) {
		Map<String, List<CotOrderDetail>> map = new HashMap<String, List<CotOrderDetail>>();
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotElementsNew.class);
		// 获得币种为美元的编号
		String sqlCur = "select obj.id from CotCurrency obj where obj.curNameEn='USD'";
		List<?> listCur = this.getOrderDao().find(sqlCur);
		// 如果没有名称为USD的币种,不能导入
		if (listCur == null || listCur.size() == 0) {
			map = null;
			return;
		}
		DecimalFormat df3 = new DecimalFormat("#.000");
		Integer curUSD = (Integer) listCur.get(0);
		try {
			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i);
				String[] price = temp.split(",");
				map.put(price[0].trim(), null);
			}
			for (Iterator<?> itor = map.keySet().iterator(); itor.hasNext();) {
				String field = (String) itor.next();
				List<CotOrderDetail> listDetail = new ArrayList<CotOrderDetail>();
				for (int i = 0; i < list.size(); i++) {
					String temp = list.get(i);
					String[] price = temp.split(",");
					if (field.equals(price[0].trim())) {
						String sql = "from CotElementsNew obj where obj.eleId='"
								+ price[1].trim() + "'";
						List<?> listOld = this.getOrderDao().find(sql);
						if (listOld.size() > 0) {
							CotElementsNew cotElements = (CotElementsNew) listOld
									.get(0);
							CotOrderDetail orderDetail = new CotOrderDetail();
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
									BeanUtils.setProperty(orderDetail,
											properties[j], value);
								}
							}
							// 数量
							Long num = Long.parseLong(price[3].trim());
							// 单价
							Double orderPrice =Double.parseDouble(price[2].trim());
							// 外箱数
							Long boxObCount = orderDetail.getBoxObCount();
							if (boxObCount != null && boxObCount != 0) {
								// 计算箱数
								Double containerCount = Math.ceil(num
										/ boxObCount.doubleValue());
								orderDetail.setContainerCount(containerCount
										.longValue());
							} else {
								orderDetail.setContainerCount(0l);
							}
							// boxCbm
							Float boxCbm = orderDetail.getBoxCbm();
							if (boxCbm != null) {
								orderDetail.setTotalCbm(Float.parseFloat(df3
										.format(boxCbm * num)));
							} else {
								orderDetail.setTotalCbm(0f);
							}

							// 同一币种为美元
							orderDetail.setCurrencyId(curUSD);
							orderDetail.setOrderPrice(orderPrice);
							orderDetail.setBoxCount(num);
							orderDetail.setTotalMoney(num * orderPrice);

							orderDetail.setType("ele");
							String rdm = "1"
									+ RandomStringUtils.randomNumeric(8);
							orderDetail.setRdm(rdm);
							orderDetail.setSrcId(cotElements.getId());
							listDetail.add(orderDetail);
						}
					}
				}
				map.put(field, listDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = null;
		}
		request.getSession().setAttribute("CheckOrderMachine", map);
	}

	// 清空盘点机Map
	public void clearCheckMap() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("CheckOrderMachine");
	}

	// 从session中取得上传的盘点机流水号
	@SuppressWarnings("unchecked")
	public List<String> getMachineNum() {
		List<String> list = new ArrayList<String>();
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckOrderMachine");
		if (obj != null) {
			Map<String, List<CotOrderDetail>> map = (Map<String, List<CotOrderDetail>>) obj;
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
		Object obj = ctx.getSession().getAttribute("CheckOrderMachine");
		if (obj != null) {
			Map<String, List<CotOrderDetail>> map = (Map<String, List<CotOrderDetail>>) obj;
			List<CotOrderDetail> listDetail = map.get(checkNo);
			for (int i = 0; i < listDetail.size(); i++) {
				CotOrderDetail cotOrderDetail = (CotOrderDetail) listDetail
						.get(i);
				String detail = cotOrderDetail.getEleId() + "---"
						+ cotOrderDetail.getOrderPrice() + "---"
						+ cotOrderDetail.getBoxCount();
				list.add(detail);
			}
		}
		return list;
	}

	// 根据盘点机流水号获得明细对象集合
	@SuppressWarnings("unchecked")
	public List<CotOrderDetail> getMachineDetailList(String checkNo) {
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckOrderMachine");
		if (obj != null) {
			Map<String, List<CotOrderDetail>> map = (Map<String, List<CotOrderDetail>>) obj;
			List<CotOrderDetail> listDetail = map.get(checkNo);
			return listDetail;
		}
		return null;
	}

	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId) {
		return (CotBoxPacking) this.getOrderDao().getById(CotBoxPacking.class,
				boxPackingId);
	}

	// 计算价格
	public String calPrice(CotOrderDetail elements, Integer boxPackingId) {
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
		CotOrderDetail elements = this.getOrderMapValue(rdm);
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

		elements.setBoxPbPrice(pb);
		elements.setBoxIbPrice(ib);
		elements.setBoxMbPrice(mb);
		elements.setBoxObPrice(ob);
		elements.setInputGridPrice(ig);
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
				List list = this.getOrderDao().find(hql);
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
				List list = this.getOrderDao().find(hql);
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
				List list = this.getOrderDao().find(hql);
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

	// 如果是预收货款的收款记录,如果有转移过,不能再删除收款记录
	public boolean checkIsLiu(Integer id) {
		CotFinacerecvDetail finacerecvDetail = (CotFinacerecvDetail) this
				.getOrderDao().getById(CotFinacerecvDetail.class, id);
		CotFinaceAccountrecv accountrecv = (CotFinaceAccountrecv) this
				.getOrderDao().getById(CotFinaceAccountrecv.class,
						finacerecvDetail.getRecvId());
		if ("预收货款".equals(accountrecv.getFinaceName())
				&& accountrecv.getZhRemainAmount().doubleValue() != accountrecv
						.getRealAmount().doubleValue()) {
			return false;
		} else {
			return true;
		}
	}

	// 重新排序
	public boolean updateSortNo(Integer id, Integer type, String field,
			String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<String, CotOrderDetail> orderMap = this.getMapAction(ctx
				.getSession());
		List list = new ArrayList();
		Iterator<?> it = orderMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			CotOrderDetail detail = orderMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotOrderDetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getOrderDao().updateRecords(listNew);
		return true;
	}

	// 判断该订单是否已分解完成
	public boolean checkUnCountIsZero(Integer orderId) {
		boolean flag = false;

		String hql = " from CotOrderDetail obj where obj.orderId=" + orderId;
		List<?> list = this.getOrderDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) list.get(i);
			if (detail.getUnBoxCount4OrderFac() > 0) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	// 判断该订单是否未分解
	public boolean checkUnCountIsEqual(Integer orderId) {
		boolean flag = false;

		String hql = " from CotOrderDetail obj where obj.orderId=" + orderId;
		List<?> list = this.getOrderDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) list.get(i);
			if (detail.getUnBoxCount4OrderFac().longValue() != detail
					.getBoxCount().longValue()) {
				flag = true; // 已分解过
				break;
			}
		}
		return flag;
	}

	// 判断订单是否已采购
	public boolean checkIsHaveOderFac(Integer orderId) {
		boolean flag = false;
		String hql = " from CotOrderDetail obj where obj.orderFacnoid "
				+ "is not null and obj.orderFacnoid !='' and obj.orderId="
				+ orderId;
		List<?> list = this.getOrderDao().find(hql);
		if (list.size() > 0) {
			flag = true;
		}
		return flag;
	}

	// 同步唛头信息至采购单
	public boolean updateOrderFacMb(Integer orderId) {
		boolean flag = false;
		byte[] zwtpByte = this.getZwtpPic();
		CotOrder order = this.getOrderById(orderId);

		List<CotOrderFac> listRes = new ArrayList<CotOrderFac>();

		// 获取图片
		List<?> listMB = new ArrayList<Integer>();
		if (orderId != null) {
			String hql = "select obj.orderMBImg from CotOrder obj where obj.id="
					+ orderId;
			listMB = this.getOrderDao().find(hql);
		}

		Set idSet = new TreeSet();

		String[] orderFacIds = this.getOrderFacIds(orderId).split(",");
		for (int i = 0; i < orderFacIds.length; i++) {
			if (!"".equals(orderFacIds[i])) {
				idSet.add(Integer.parseInt(orderFacIds[i]));
			}
		}
		Iterator iterator = idSet.iterator();
		while (iterator.hasNext()) {
			Integer id = (Integer) iterator.next();
			CotOrderFac orderFac = (CotOrderFac) this.getOrderDao().getById(
					CotOrderFac.class, id);
			if (orderFac != null) {
				// 唛信息同步订单
				if (listMB != null) {
					orderFac.setOrderMBImg((byte[]) listMB.get(0));
				} else {
					orderFac.setOrderMBImg(zwtpByte);
				}
				orderFac.setOrderZm(order.getOrderZM());
				orderFac.setOrderZhm(order.getOrderZHM());
				orderFac.setOrderCm(order.getOrderCM());
				orderFac.setOrderNm(order.getOrderNM());
				orderFac.setOrderMb(order.getOrderMB());
				orderFac.setCotOrderFacdetails(null);
			}
			listRes.add(orderFac);
		}
		try {
			this.getOrderDao().updateRecords(listRes);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	// 获取采购单ids
	public List<Integer> getCotOrderFacIds(Integer orderId) {

		List<Integer> listRes = new ArrayList<Integer>();

		Set idSet = new TreeSet();
		String[] orderFacIds = this.getOrderFacIds(orderId).split(",");
		for (int i = 0; i < orderFacIds.length; i++) {
			if (!"".equals(orderFacIds[i])) {
				idSet.add(Integer.parseInt(orderFacIds[i]));
			}
		}
		Iterator iterator = idSet.iterator();
		while (iterator.hasNext()) {
			Integer id = (Integer) iterator.next();
			listRes.add(id);
		}
		return listRes;
	}

	public boolean updateCotOrderFacMb(Integer orderId, String[] orderfacIds) {
		boolean flag = false;
		byte[] zwtpByte = this.getZwtpPic();
		CotOrder order = this.getOrderById(orderId);

		List<CotOrderFac> listRes = new ArrayList<CotOrderFac>();

		// 获取图片
		List<?> listMB = new ArrayList<Integer>();
		if (orderId != null) {
			String hql = "select obj.orderMBImg from CotOrder obj where obj.id="
					+ orderId;
			listMB = this.getOrderDao().find(hql);
		}

		for (int i = 0; i < orderfacIds.length; i++) {

			CotOrderFac orderFac = (CotOrderFac) this.getOrderDao().getById(
					CotOrderFac.class, Integer.parseInt(orderfacIds[i]));

			if (orderFac != null) {
				// 唛信息同步订单
				if (listMB != null) {
					orderFac.setOrderMBImg((byte[]) listMB.get(0));
				} else {
					orderFac.setOrderMBImg(zwtpByte);
				}
				orderFac.setOrderZm(order.getOrderZM());
				orderFac.setOrderZhm(order.getOrderZHM());
				orderFac.setOrderCm(order.getOrderCM());
				orderFac.setOrderNm(order.getOrderNM());
				orderFac.setOrderMb(order.getOrderMB());
				orderFac.setCotOrderFacdetails(null);
			}
			listRes.add(orderFac);
		}
		try {
			this.getOrderDao().updateRecords(listRes);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	// 订单分解时判断是否已有改厂家采购单
	public boolean isExistFactory(Integer facId, Integer orderId) {
		boolean flag = false;
		List<Integer> idres = this.getCotOrderFacIds(orderId);

		for (int i = 0; i < idres.size(); i++) {
			CotOrderFac orderFac = (CotOrderFac) this.getOrderDao().getById(
					CotOrderFac.class, idres.get(i));
			if (orderFac != null) {
				if (facId.intValue() == orderFac.getFactoryId().intValue()) {
					flag = true;
				} else {
					flag = false;
				}
			}
		}
		return flag;
	}

	// 计算生产价
	public Float sumPriceFac(String expessionFacIn, CotOrderDetail detail) {
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
			result = ContextUtil.getObjByPrecision(Float.parseFloat(result),
					"facPrecision");
			return Float.parseFloat(result);
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}

	}

	// 计算外销价
	public Float sumPriceOut(String expessionIn, Float priceFac,
			CotOrderDetail detail) {
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
		// 样品数量
		Long boxCount = detail.getBoxCount();
		if (boxCount == null) {
			boxCount = 0l;
		}

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("priceProfit", lirun.toString());
		evaluator.putVariable("priceFac", priceFac.toString());
		evaluator.putVariable("priceRate", rate.toString());
		evaluator.putVariable("tuiLv", tui.toString());
		evaluator.putVariable("cbm", cb.toString());
		evaluator.putVariable("boxObCount", bc.toString());
		evaluator.putVariable("boxCount", boxCount.toString());
		Float res = null;
		try {
			String result = evaluator.evaluate(expessionIn);
			result = ContextUtil.getObjByPrecision(Float.parseFloat(result),
					"outPrecision");
			res = Float.parseFloat(result);
			return res;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}
	}

	// 查询含有rdm的报价记录
	public String getListData(HttpServletRequest request, QueryInfo queryInfo,String ctPc)
			throws DAOException {
		GridServerHandler gd = null;
		if (queryInfo.getExcludes() != null)
			gd = new GridServerHandler(queryInfo.getExcludes());
		else
			gd = new GridServerHandler();
		int count = this.getOrderDao().getRecordsCount(queryInfo);
		List res = this.getOrderDao().findRecords(queryInfo);
		if(ctPc==null){
			// 将明细存到内存中的map
			HttpSession session = request.getSession();
			// 清空内存map
			session.removeAttribute("SessionORDER");
			Iterator<?> it = res.iterator();
			while (it.hasNext()) {
				CotOrderDetail cotOrderDetail = (CotOrderDetail) it.next();
				cotOrderDetail.setPicImg(null);
				String rdm = "1" + RandomStringUtils.randomNumeric(8);
				cotOrderDetail.setRdm(rdm);
				this.setMapAction(session, rdm, cotOrderDetail);
			}
		}

		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
	}

	// 得到盘点机的内存map的key集合
	public void savePanList(String no, Integer orderId) {
		// 取得内存中的最大排序号
		Object order = SystemUtil.getObjBySession(null, "order");
		int temp = 0;
		if (order != null) {
			Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) order;
			Iterator<?> it = orderMap.keySet().iterator();
			while (it.hasNext()) {
				String rdm = (String) it.next();
				CotOrderDetail orderDetail = orderMap.get(rdm);
				if (orderDetail.getSortNo() != null
						&& orderDetail.getSortNo() > temp) {
					temp = orderDetail.getSortNo();
				}
			}
		}

		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckOrderMachine");
		Map<String, List<CotOrderDetail>> map = (Map<String, List<CotOrderDetail>>) obj;
		List<CotOrderDetail> list = map.get(no);
		for (int i = 0; i < list.size(); i++) {
			temp++;
			CotOrderDetail orderDetail = list.get(i);
			orderDetail.setSortNo(temp);
			orderDetail.setOrderId(orderId);
			orderDetail.setTotalGrossWeigth(0f);
			orderDetail.setTotalNetWeigth(0f);
			// this.setOrderMapAndDel(orderDetail.getRdm(), orderDetail);
		}
		this.addOrderDetails(list);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		byte[] zwtpByte = this.getCotElementsService().getZwtpPic();
		// 保存图片,图片取样品档案的
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail detail = list.get(i);
			// 新建图片
			CotOrderPic pricePic = new CotOrderPic();
			pricePic.setFkId(detail.getId());
			if (detail.getType().equals("ele")) {
				CotElePic cotElePic = impOpService.getElePicImgByEleId(detail
						.getSrcId());
				if (cotElePic != null) {
					pricePic.setEleId(cotElePic.getEleId());
					pricePic.setPicImg(cotElePic.getPicImg());
					pricePic.setPicSize(cotElePic.getPicImg().length);
					pricePic.setPicName(cotElePic.getEleId());
				} else {
					pricePic.setEleId(detail.getEleId());
					pricePic.setPicImg(zwtpByte);
					pricePic.setPicSize(zwtpByte.length);
					pricePic.setPicName(detail.getEleId());
				}
			}
			// 添加到图片数组
			List<CotOrderPic> imgList = new ArrayList<CotOrderPic>();
			imgList.add(pricePic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(imgList);
		}
	}

	// 订单同步样品配件信息
	public boolean deleteAndTongEleFitting(Integer orderDetailId, String eleId) {
		CotOrderDetail orderDetail = (CotOrderDetail) this.getOrderDao()
				.getById(CotOrderDetail.class, orderDetailId);
		// 0.先查询该货号的样品id,如果找不到该货号不同步
		Integer eId = 0;
		String str = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List chk = this.getOrderDao().find(str);
		if (chk == null || chk.size() == 0) {
			return false;
		} else {
			eId = (Integer) chk.get(0);
		}
		// 1.先删除订单原来的配件信息
		String hql = "select obj.id from CotOrderFittings obj where obj.orderDetailId="
				+ orderDetailId;
		List list = this.getOrderDao().find(hql);
		try {
			if (list.size() > 0) {
				this.getOrderDao().deleteRecordByIds(list, "CotOrderFittings");
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
		List listNew = new ArrayList();
		// 2.再查找该货号的所有样品配件信息,插入到该订单明细中
		String sql = "select obj from CotEleFittings obj where obj.eleId="
				+ eId;
		List listEle = this.getOrderDao().find(sql);
		for (int i = 0; i < listEle.size(); i++) {
			CotEleFittings eleFittings = (CotEleFittings) listEle.get(i);
			CotOrderFittings orderFittings = new CotOrderFittings();
			orderFittings.setOrderDetailId(orderDetailId);
			orderFittings.setEleId(eleId);
			orderFittings.setEleName(orderDetail.getEleName());
			orderFittings.setFitNo(eleFittings.getFitNo());
			orderFittings.setFitName(eleFittings.getFitName());
			orderFittings.setFitDesc(eleFittings.getFitDesc());
			orderFittings.setFacId(eleFittings.getFacId());
			orderFittings.setFitUseUnit(eleFittings.getFitUseUnit());
			orderFittings.setFitUsedCount(eleFittings.getFitUsedCount());
			orderFittings.setFitCount(eleFittings.getFitCount());
			orderFittings.setFitPrice(eleFittings.getFitPrice());
			orderFittings.setFitTotalPrice(eleFittings.getFitTotalPrice());
			orderFittings.setFitRemark(eleFittings.getFitRemark());
			orderFittings.setOrderId(orderDetail.getOrderId());
			orderFittings.setFittingId(eleFittings.getFittingId());
			listNew.add(orderFittings);
		}
		try {
			this.getOrderDao().saveRecords(listNew);
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
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

	public void setNoService(SetNoService noService) {
		this.noService = noService;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.order.CotOrderService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getOrderDao().getJsonData(queryInfo);
	}

	// 订单单号
	public String getOrderNo(Integer custId, String orderTime) {
		Sequece sequece = new Sequece(false);
		Map map = new HashMap();
		map.put("CotCustomer", custId);
		String orderNo = sequece.getOrderNo(map, orderTime);
		return orderNo;
	}

	// 更新至产品采购和包材采购唛头信息
	public void updateMb(Integer orderId, String orderZM, String orderCM,
			String orderZHM, String orderNM, String productM) throws DAOException {
		// 保存新的麦标信息
		CotOrder order = this.getOrderById(orderId);
		order.setOrderCM(orderCM);
		order.setOrderZM(orderZM);
		order.setOrderZHM(orderZHM);
		order.setOrderNM(orderNM);
		order.setProductM(productM);
		List listOrder = new ArrayList();
		listOrder.add(order);
		this.getOrderDao().updateRecords(listOrder);

		// 查找属于该订单的所有产品采购主单
		String orderfacIds = this.getOrderFacIds(orderId);
		// 查找属于该订单的所有包材采购主单
		String orderHql = "select obj.id from CotPackingOrder obj where obj.orderId="
				+ orderId;
		List packIDs = this.getOrderDao().find(orderHql);
		if (!"".equals(orderfacIds) || packIDs.size() > 0) {
			// 获得订单麦标
			String hqlMb = "from CotOrderMb obj where obj.fkId=" + orderId;
			List listMb = this.getOrderDao().find(hqlMb);
			CotOrderMb orderMb = null;
			if (listMb != null && listMb.size() > 0) {
				orderMb = (CotOrderMb) listMb.get(0);
			}
			// 获得订单产品标
			String hqlProMb = "from CotProductMb obj where obj.fkId=" + orderId;
			List listProMb = this.getOrderDao().find(hqlProMb);
			CotProductMb productMb = null;
			if (listProMb != null && listProMb.size() > 0) {
				productMb = (CotProductMb) listProMb.get(0);
			}
			if (!"".equals(orderfacIds)) {
				String[] idsAry = orderfacIds.split(",");
				List dbIDs = new ArrayList<Integer>();
				for (String id : idsAry) {
					dbIDs.add(Integer.parseInt(id));
				}
				// 更新图片
				String faclMb = "update CotOrderfacMb obj set obj.picImg=:picImg,obj.picSize=:picSize  where obj.fkId in (:ids)";
				Map map = new HashMap();
				map.put("picImg", orderMb.getPicImg());
				map.put("picSize", orderMb.getPicSize());
				map.put("ids", dbIDs);
				QueryInfo queryInfo = new QueryInfo();
				queryInfo.setSelectString(faclMb);
				int result = this.getOrderDao().executeUpdate(queryInfo, map);
				// 更新产品标
				String proMb = "update CotProductFacMb obj set obj.picImg=:picImg,obj.picSize=:picSize  where obj.fkId in (:ids)";
				Map mapPro = new HashMap();
				mapPro.put("picImg", productMb.getPicImg());
				mapPro.put("picSize", productMb.getPicSize());
				mapPro.put("ids", dbIDs);
				QueryInfo queryInfoFac = new QueryInfo();
				queryInfoFac.setSelectString(proMb);
				this.getOrderDao().executeUpdate(queryInfoFac, mapPro);
				// 更新正册麦
				String temp = "update CotOrderFac obj set "
						+ "obj.orderCm=:orderCm," + "obj.orderZm=:orderZm,"
						+ "obj.orderZhm=:orderZhm," + "obj.orderNm=:orderNm,"
						+ "obj.productM=:productM  " + "where obj.id in (:ids)";
				Map mapTp = new HashMap();
				mapTp.put("orderCm", orderCM);
				mapTp.put("orderZm", orderZM);
				mapTp.put("orderZhm", orderZHM);
				mapTp.put("orderNm", orderNM);
				mapTp.put("orderNm", orderNM);
				mapTp.put("productM", productM);
				mapTp.put("ids", dbIDs);
				QueryInfo query2 = new QueryInfo();
				query2.setSelectString(temp);
				int result2 = this.getOrderDao().executeUpdate(query2, mapTp);
			}
			if (packIDs.size() > 0) {
				String faclMb = "update CotPackMb obj set obj.picImg=:picImg,obj.picSize=:picSize  where obj.fkId in (:ids)";
				String[] idsAry = orderfacIds.split(",");
				Map map = new HashMap();
				map.put("picImg", orderMb.getPicImg());
				map.put("picSize", orderMb.getPicSize());
				map.put("ids", packIDs);
				QueryInfo queryInfo = new QueryInfo();
				queryInfo.setSelectString(faclMb);
				int result = this.getOrderDao().executeUpdate(queryInfo, map);
				// 更新正册麦
				String temp = "update CotPackingOrder obj set "
						+ "obj.orderCM=:orderCM," + "obj.orderZM=:orderZM,"
						+ "obj.orderZHM=:orderZHM," + "obj.orderNM=:orderNM  "
						+ "where obj.id in (:ids)";
				Map mapTp = new HashMap();
				mapTp.put("orderCM", orderCM);
				mapTp.put("orderZM", orderZM);
				mapTp.put("orderZHM", orderZHM);
				mapTp.put("orderNM", orderNM);
				mapTp.put("ids", packIDs);
				QueryInfo query2 = new QueryInfo();
				query2.setSelectString(temp);
				int result2 = this.getOrderDao().executeUpdate(query2, mapTp);
			}
		}
	}

	// 储存Map
	public void setExcelMap(String rdm, CotOrderDetail cotOrderDetail) {
		Object obj = SystemUtil.getObjBySession(null, "excelTemp");
		if (obj == null) {
			Map<String, CotOrderDetail> orderMap = new HashMap<String, CotOrderDetail>();
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "excelTemp");
		} else {
			Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
			orderMap.put(rdm, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "excelTemp");
		}

	}

	// 判断订单是否有其他产品费用
	public boolean checkIsHaveFinace(Integer orderId) {
		// 产品采购费用
		String facIds = this.checkIsHasFac(orderId);
		if (facIds != null && !facIds.trim().equals("")) {
			String temp = facIds.trim();
			String hql = "select obj.id from CotFinaceOther obj,CotOrderFac d "
					+ "where obj.fkId in ("
					+ temp.substring(0, temp.length() - 1)
					+ ")"
					+ " and obj.fkId=d.id and obj.flag='A' and obj.source='orderfac' and (obj.isImport is null or obj.isImport=0)";
			List list = this.getOrderDao().find(hql);
			if (list.size() > 0) {
				return false;
			}
		}

		// 配件采购费用
		String hql2 = "select obj.id from CotFinaceOther obj,CotFittingOrder d "
				+ " where d.orderId="
				+ orderId
				+ " and obj.fkId=d.id and obj.flag='A' and obj.source='fitorder' and (obj.isImport is null or obj.isImport=0)";
		List list2 = this.getOrderDao().find(hql2);
		if (list2.size() > 0) {
			return false;
		}

		// 包材采购费用
		String hql3 = "select obj.id from CotFinaceOther obj,CotPackingOrder d "
				+ " where d.orderId="
				+ orderId
				+ " and obj.fkId=d.id and obj.flag='A' and obj.source='packorder' and (obj.isImport is null or obj.isImport=0)";
		List list3 = this.getOrderDao().find(hql3);
		if (list3.size() > 0) {
			return false;
		}

		return true;
	}

	// 判断订单其他费用是否存在
	public boolean findIsExistName(String name, Integer orderId, Integer recId) {
		String hql = "select obj.id from CotFinaceOther obj where obj.fkId=? and obj.source='order' and obj.finaceName=?";
		Object[] obj = new Object[2];
		obj[0] = orderId;
		obj[1] = name;
		List list = this.getOrderDao().queryForLists(hql, obj);
		if (list != null && list.size() > 0) {
			Integer tId = (Integer) list.get(0);
			if (tId.intValue() != recId.intValue()) {
				return true;
			}
		}
		return false;
	}

	// 需要判断该明细是否已生成出货和采购,如果是.不能删除
	public Integer findIsCanDel(Integer detailId) {
		// 判断是否已出货
		String hql = "select obj.id from CotOrderOutdetail obj where obj.orderDetailId="
				+ detailId;
		List list = this.getOrderDao().find(hql);
		if (list != null && list.size() > 0) {
			return 0;
		}

		// 判断是否已生成产品采购
		String facHql = "select obj.id from CotOrderFacdetail obj where obj.orderDetailId="
				+ detailId;
		List listFac = this.getOrderDao().find(facHql);
		if (listFac != null && listFac.size() > 0) {
			return 0;
		}

		// 判断是否已生成配件采购
		String fitHql = "select obj.id from CotFittingsAnys obj where obj.anyFlag='C' and obj.orderdetailId="
				+ detailId;
		List listFit = this.getOrderDao().find(fitHql);
		if (listFit != null && listFit.size() > 0) {
			return 0;
		}
		// 判断是否已生成包材采购
		String packHql = "select obj.id from CotPackingAnys obj where obj.anyFlag='C' and obj.orderdetailId="
				+ detailId;
		List listPack = this.getOrderDao().find(packHql);
		if (listPack != null && listPack.size() > 0) {
			return 0;
		}
		return 1;
	}

	// 计算勾选的订单明细的InverseMargin
	public Map calLiRun(Map<?, ?> map, List rdmAry) {
		Map mapNew = new HashMap();
		Iterator<?> it = rdmAry.iterator();
		while (it.hasNext()) {
			String rdm = (String) it.next();
			Float lirun = this.getNewLiRun(map, rdm);
			if (lirun != null) {
				mapNew.put(rdm, lirun);
				// 更改内存值
				CotOrderDetail detail = this.getOrderMapValue(rdm);
				if (detail.getLiRun() != null) {
					Float tmp = Math.abs(detail.getLiRun() - lirun);
					if (tmp >= 0.09) {
						detail.setLiRun(lirun);
					}
				}
				this.setOrderMap(rdm, detail);
			}
		}
		return mapNew;
	}

	// 用过货号查找样品id
	public Integer getEleIdByEleName(String eleId) {
		String hql = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List list = this.getOrderDao().find(hql);
		if (list != null && list.size() > 0) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}

	// 订单同步到样品配件
	public boolean deleteAndTongFitting(Integer orderDetailId, String eleId) throws DAOException {
		CotOrderDetail orderDetail = (CotOrderDetail) this.getOrderDao()
				.getById(CotOrderDetail.class, orderDetailId);
		// 0.先查询该货号的样品id,如果找不到该货号不同步
		Integer eId = 0;
		String str = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List chk = this.getOrderDao().find(str);
		if (chk == null || chk.size() == 0) {
			return false;
		} else {
			eId = (Integer) chk.get(0);
		}
		// 1.先删除样品库原来的配件信息
		String hql = "delete from CotEleFittings obj where obj.eleId=:eleId";
		Map map = new HashMap();
		map.put("eleId", eId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		this.getOrderDao().executeUpdate(queryInfo, map);

		List listNew = new ArrayList();
		// 2.再查找该货号的所有报价配件信息,插入到样品配件
		String sql = "select obj from CotOrderFittings obj where obj.orderDetailId="
				+ orderDetailId;
		List listEle = this.getOrderDao().find(sql);
		for (int i = 0; i < listEle.size(); i++) {
			CotOrderFittings orderFittings = (CotOrderFittings) listEle.get(i);
			CotEleFittings eleFittings = new CotEleFittings();
			eleFittings.setEleId(eId);
			eleFittings.setFitNo(orderFittings.getFitNo());
			eleFittings.setFitName(orderFittings.getFitName());
			eleFittings.setFitDesc(orderFittings.getFitDesc());
			eleFittings.setFacId(orderFittings.getFacId());
			eleFittings.setFitUseUnit(orderFittings.getFitUseUnit());
			eleFittings.setFitUsedCount(orderFittings.getFitUsedCount());
			eleFittings.setFitCount(orderFittings.getFitCount());
			eleFittings.setFitPrice(orderFittings.getFitPrice());
			eleFittings.setFitTotalPrice(orderFittings.getFitTotalPrice());
			eleFittings.setFitRemark(orderFittings.getFitRemark());
			eleFittings.setFittingId(orderFittings.getFittingId());
			listNew.add(eleFittings);
		}
		try {
			this.getOrderDao().saveRecords(listNew);
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 修改样品生产价
	public boolean modifyPriceFacByEleId(String eleId) {
		// 0.先查询该货号的样品id,如果找不到该货号不同步
		Integer eId = 0;
		String str = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List chk = this.getOrderDao().find(str);
		if (chk == null || chk.size() == 0) {
			return false;
		} else {
			eId = (Integer) chk.get(0);
		}
		this.getCotElementsService().modifyPriceFacByEleId(eId);
		return true;
	}

	// 根据订单主单id查找对应的生产合同主单id
	public Integer getOrderFacByOrderId(Integer orderId) {
		String hql = "select obj.id from CotOrderFac obj where obj.orderId="
				+ orderId;
		List list = this.getOrderDao().find(hql);
		if (list != null && list.size() > 0) {
			return (Integer) list.get(0);
		}
		return null;
	}

	public Float getTuiLv(Integer hsId) {
		CotEleOther cotEleOther = (CotEleOther) this.getOrderDao().getById(
				CotEleOther.class, hsId);
		return cotEleOther.getTaxRate();
	}

	// 将Pi生成Invoice
	public Integer saveInvoice(Integer orderId, boolean flag) {
		if (flag == false) {
			// 查询该订单是否有出货
			String hqlOut = "select obj.id from CotOrderOut obj where obj.orderId="
					+ orderId;
			List listOut = this.getOrderDao().find(hqlOut);
			if (listOut != null && listOut.size() > 0) {
				return (Integer) listOut.get(0);
			}
		}

		CotOrder order = (CotOrder) this.getOrderDao().getById(CotOrder.class,
				orderId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		// 生成单号
		// CotSeqService seq = new CotSeqServiceImpl();
		// String orderNo = seq.getOrderOutNo(order.getCustId(),
		// order.getBussinessPerson(),sdf.format(date));
		// seq.saveCustSeq(order.getCustId(), "orderout",date.toString());
		// seq.saveSeq("orderout");

		CotOrderOut orderOut = new CotOrderOut();
		// 客户
		orderOut.setCustId(order.getCustId());
		// 出货单号
		// orderOut.setOrderNo(orderNo);
		// 客户联系人
		orderOut.setContactId(order.getContactId());
		// 业务员
		orderOut.setBusinessPerson(order.getBussinessPerson());
		// 公司
		orderOut.setCompanyId(order.getCompanyId());
		// 出货日期(当前日期)
		orderOut.setOrderTime(date);
		// 币种
		orderOut.setCurrencyId(order.getCurrencyId());
		// 合同条款
		orderOut.setClauseTypeId(order.getClauseTypeId());
		// 利润率
		orderOut.setCutScale(order.getOrderProfit());
		// 付款方式
		orderOut.setPaTypeId(order.getPayTypeId());
		// 起运港
		orderOut.setShipportId(order.getShipportId());
		// 目的港
		orderOut.setTargetportId(order.getTargetportId());
		// 船期
		orderOut.setSendTime(order.getOrderLcDate());
		// Delivery Date
		orderOut.setOrderLcDate(order.getSendTime());
		// 集装箱
		orderOut.setContainerTypeId(order.getContainerTypeId());
		// 运输方式
		orderOut.setTrafficId(order.getTrafficId());
		// 备注
		orderOut.setOrderRemark(order.getOrderRemark());
		// 订单编号
		orderOut.setOrderId(orderId);
		// 总箱数
		orderOut.setTotalContainer(order.getTotalContainer());
		// 总数量
		orderOut.setTotalCount(order.getTotalCount());
		// 总金额
		orderOut.setTotalMoney(order.getTotalMoney().floatValue());
		// 设置税率
		orderOut.setTaxLv(0);
		// 设置税后总金额
		// Double taxTotalMoney = order.getTotalMoney()*(1+0.04);
		// orderOut.setTaxTotalMoney(taxTotalMoney.floatValue());
		// 佣金
		orderOut.setCommisionScale(order.getCommisionScale());
		// 其它信息
		orderOut.setQuality(order.getQuality());
		orderOut.setColours(order.getColours());
		orderOut.setSaleUnit(order.getSaleUnit());
		orderOut.setHandleUnit(order.getHandleUnit());
		orderOut.setAssortment(order.getAssortment());
		orderOut.setComments(order.getComments());
		orderOut.setShippingMark(order.getShippingMark());
		orderOut.setIsCheckAgent(order.getIsCheckAgent());
		orderOut.setBuyer(order.getBuyer());
		orderOut.setSeller(order.getSeller());
		orderOut.setAgent(order.getAgent());
		orderOut.setOrderStatus(0l);
		orderOut.setNationId(order.getNationId());
		// department
		orderOut.setTypeLv1Id(order.getTypeLv1Id());
		// Sale
		orderOut.setBusinessPerson(order.getBussinessPerson());

		List list = new ArrayList();
		list.add(orderOut);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		List<CotOrderoutPic> imgList = new ArrayList<CotOrderoutPic>();
		try {
			// 保存出货主单
			this.getOrderDao().saveRecords(list);
			// 将订单明细拷贝到出货明细
			String hql = "from CotOrderDetail obj where obj.orderId="
					+ order.getId();
			List record = new ArrayList();
			List recordOrder = new ArrayList();
			List orderDetails = this.getOrderDao().find(hql);
			Iterator<?> it = orderDetails.iterator();
			while (it.hasNext()) {
				CotOrderDetail orderDetail = (CotOrderDetail) it.next();
				CotOrderOutdetail cotOrderOutdetail = new CotOrderOutdetail();
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
							|| "liRun".equals(propEle[i])
							|| "tuiLv".equals(propEle[i])
							|| "orderFacno".equals(propEle[i])
							|| "orderFacnoid".equals(propEle[i])
							|| "outHasBoxCount4OrderFac".equals(propEle[i])) {
						continue;
					}
					if (value != null) {
						beanUtils.setProperty(cotOrderOutdetail, propEle[i],
								value);
					}
				}
				cotOrderOutdetail.setOrderId(orderOut.getId());
				cotOrderOutdetail.setOrderDetailId(orderDetail.getId());
				orderDetail.setUnBoxCount(0f);
				orderDetail.setUnBoxSend(1);
				record.add(cotOrderOutdetail);
				recordOrder.add(orderDetail);

				// 出货明细的图片
				CotOrderoutPic cotOrderoutPic = new CotOrderoutPic();
				CotOrderPic cotOrderPic = impOpService.getOrderPic(orderDetail
						.getId());
				cotOrderoutPic.setEleId(cotOrderPic.getEleId());
				cotOrderoutPic.setPicImg(cotOrderPic.getPicImg());
				cotOrderoutPic.setPicSize(cotOrderPic.getPicImg().length);
				cotOrderoutPic.setPicName(cotOrderPic.getEleId());
				// 添加到图片数组
				imgList.add(cotOrderoutPic);
			}
			this.getOrderDao().saveRecords(record);
			this.getOrderDao().updateRecords(recordOrder);
			// 修改订单的状态
			order.setCanOut(3);
			order.setCotOrderDetails(null);
			List orderList = new ArrayList();
			orderList.add(order);
			this.getOrderDao().updateRecords(orderList);

			// 保存报价图片
			for (int i = 0; i < record.size(); i++) {
				CotOrderOutdetail detail = (CotOrderOutdetail) record.get(i);
				CotOrderoutPic cotOrderPic = (CotOrderoutPic) imgList.get(i);
				cotOrderPic.setFkId(detail.getId());
				List picList = new ArrayList();
				picList.add(cotOrderPic);
				// 逐条添加，避免数据量大的时候，内存溢出
				impOpService.saveImg(picList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return orderOut.getId();
	}

	public List<?> getOrderVO(QueryInfo queryInfo) {
		try {
			List<?> list = this.getOrderDao().findRecordsJDBC(queryInfo);
			Iterator<?> it =list.iterator();
			while(it.hasNext()){
				CotOrderVO cotOrderVO = (CotOrderVO) it.next();
				//判断delivery date是否要显示红色
				String shipId = cotOrderVO.getShipportId()==null?"0":cotOrderVO.getShipportId().toString();
				String tarId = cotOrderVO.getTargetportId()==null?"0":cotOrderVO.getTargetportId().toString();
				Integer temp = this.getQueryService().findDays(shipId,tarId);
				
				java.util.Date date = this.getQueryService().addDate(cotOrderVO.getOrderLcDate(), temp);
				java.util.Date newDate = cotOrderVO.getSendTime();
				if(newDate.getTime()<date.getTime()){
					cotOrderVO.setChk(1);
				}else{
					java.util.Date date2 = this.getQueryService().addDate(date, 37);
					if(newDate.getTime()>=date2.getTime()){
						cotOrderVO.setChk(1);
					}
				}
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	public QueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	public CotExportRptDao getExportRptDao() {
		return exportRptDao;
	}

	public void setExportRptDao(CotExportRptDao exportRptDao) {
		this.exportRptDao = exportRptDao;
	}

	// 返回审核数
	public Integer getCountPI() {
		HttpSession session = WebContextFactory.get().getSession();
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		CotEmps emp = (CotEmps) session.getAttribute("emp");
		Integer empId = emp.getId();
		String hql;
		if (!"admin".equals(emp.getEmpsId())) {
			hql = " from CotOrder obj where obj.orderStatus=3 and obj.checkPerson="
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
			hql = " from CotOrder obj where obj.orderStatus=3";
		}

		List<CotOrder> list = this.getOrderDao().find(hql);
		if (list.size() > 0) {
			System.out.println(list.size());
			return list.size();
		}
		return 0;
	}

	public List getPIPO() {
		List array = new ArrayList();
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps) session.getAttribute("emp");
		Integer empId = emp.getId();
		String hql;
		String hqlPO;
		if (!"admin".equals(emp.getEmpsId())) {
			hql = " from CotOrder obj where obj.orderStatus=3 and obj.checkPerson="
					+ empId;
			hqlPO = " from CotOrderFac obj where obj.orderStatus=3 and obj.checkPerson="
					+ empId;
		} else {
			hql = " from CotOrder obj where obj.orderStatus=3";
			hqlPO = " from CotOrderFac obj where obj.orderStatus=3";
		}
		List<CotOrder> list = this.getOrderDao().find(hql);
		List<CotOrderFac> listPO = this.getOrderDao().find(hqlPO);
		// PI
		if (list.size() > 0) {
			System.out.println(list.size());
			array.add(list.size());
		} else {
			array.add(0);
		}
		// PO
		if (listPO.size() > 0) {
			array.add(listPO.size());
		} else {
			array.add(0);
		}
		return array;
	}

	// 保存包装图片
	public Integer saveOrderPc(CotOrderPc e, String picPath) {
		// 获得tomcat路径
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		if (e.getId() == null) {
			File picFile = new File(systemPath + picPath);
			FileInputStream in;
			byte[] b = new byte[(int) picFile.length()];
			try {
				in = new FileInputStream(picFile);

				while (in.read(b) != -1) {
				}
				in.close();
				if (!"common/images/zwtp.png".equals(picPath)) {
					// 删除上传的图片
					picFile.delete();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// 添加图片
			CotOpImgService impOpService = (CotOpImgService) SystemUtil
					.getService("CotOpImgService");
			CotOrderPc orderPc = new CotOrderPc();
			orderPc.setOrderId(e.getOrderId());
			orderPc.setCategoryId(e.getCategoryId());
			orderPc.setPhone(b);
			orderPc.setOrderDetailId(e.getOrderDetailId());
			orderPc.setTempLate(e.getTempLate());
			orderPc.setPcRemark(e.getPcRemark());
			orderPc.setFilePath(e.getFilePath());

			List imgList = new ArrayList();
			imgList.add(orderPc);
			impOpService.saveImg(imgList);
		} else {
			CotOrderPc old = (CotOrderPc) this.getOrderDao().getById(
					CotOrderPc.class, e.getId());
			e.setPhone(old.getPhone());
			// 如果pdf文件名不一致,删除旧的pdf
			if (!old.getFilePath().equals(e.getFilePath())) {
				File file = new File(systemPath + "artWorkPdf/order/"
						+ old.getFilePath());
				file.delete();
			}
			List list = new ArrayList();
			list.add(e);
			this.getOrderDao().saveOrUpdateRecords(list);
		}
		return e.getId();
	}

	// 删除包装图片
	public Boolean deleteOrderPcs(List<Integer> ids) {
		// 获取系统路径
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		try {
			List files = new ArrayList();
			for (int i = 0; i < ids.size(); i++) {
				Integer id = (Integer) ids.get(i);
				CotOrderPc cotOrderPc = (CotOrderPc) this.getOrderDao()
						.getById(CotOrderPc.class, id);
				files.add(cotOrderPc.getFilePath());
			}
			this.getOrderDao().deleteRecordByIds(ids, "CotOrderPc");
			// 删除文件
			for (int i = 0; i < files.size(); i++) {
				File file = new File(systemPath + "artWorkPdf/order/"
						+ (String) files.get(i));
				file.delete();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// 根据编号获得对象
	public CotOrderPc getOrderPcById(Integer id) {
		CotOrderPc cotOrder = (CotOrderPc) this.getOrderDao().getById(
				CotOrderPc.class, id);
		if (cotOrder != null) {
			CotOrderPc custClone = (CotOrderPc) SystemUtil.deepClone(cotOrder);
			custClone.setPhone(null);
			return custClone;
		}
		return null;
	}

	// 更改包装图片
	public void updateOrderPc(String filePath, Integer fkId) throws Exception {
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotOrderPc cotOrderPc = impOpService.getOrderPcImgById(fkId);
		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int) picFile.length()];
		while (in.read(b) != -1) {
		}
		in.close();
		cotOrderPc.setPhone(b);
		if (filePath.indexOf("common/images/zwtp.png") < 0) {
			picFile.delete();
		}
		List<CotOrderPc> imgList = new ArrayList<CotOrderPc>();
		imgList.add(cotOrderPc);
		impOpService.modifyImg(imgList);
	}

	// 删除包装图片
	public boolean deleteOrderPcImg(Integer Id) throws Exception {
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		String filePath = systemPath + "common/images/zwtp.png";
		CotOrderPc cotOrderPc = impOpService.getOrderPcImgById(Id);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderPc.setPhone(b);
			List<CotOrderPc> imgList = new ArrayList<CotOrderPc>();
			imgList.add(cotOrderPc);
			impOpService.modifyImg(imgList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("删除orderpc出错");
		}
	}

	// 根据客户编号获得客户的图片
	public List getCustPcs(Integer custId) {
		String hql = "from CotCustPc obj where obj.custId=" + custId;
		return this.getOrderDao().find(hql);
	}

	// 用过货号查找样品小类
	public Integer getEleTypeidLv3ByEleName(String eleId) {
		String hql = "select obj.eleTypeidLv3 from CotOrderDetail obj where obj.id="
				+ eleId;
		List list = this.getOrderDao().find(hql);
		if (list != null && list.size() > 0) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}

	// 根据客户小类图片ids生成orderPc
	public void saveOrderPcs(String ids, Integer orderId, Integer detailId) {
		// 获取系统路径
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		String[] idsAry = ids.split(",");
		for (int i = 0; i < idsAry.length; i++) {
			CotCustPc cotCustPc = (CotCustPc) this.getOrderDao().getById(
					CotCustPc.class, Integer.parseInt(idsAry[i]));
			CotOrderPc orderPc = new CotOrderPc();
			orderPc.setCategoryId(cotCustPc.getCategoryId());
			orderPc.setOrderId(orderId);
			orderPc.setOrderDetailId(detailId);
			orderPc.setTempLate(cotCustPc.getTempLate());
			orderPc.setPcRemark(cotCustPc.getPcRemark());
			orderPc.setPhone(cotCustPc.getPhone());
			// 拷贝客户的pdf到order文件夹
			String srcPath = "artWorkPdf/client/" + cotCustPc.getFilePath();
			// 重新生成个8位小数
			String path = cotCustPc.getFilePath();
			String str = path.substring(0, path.length() - 12);
			String toPath = str + RandomStringUtils.randomNumeric(8) + ".pdf";
			orderPc.setFilePath(toPath);
			SystemUtil.copyFile(srcPath, "artWorkPdf/order/" + toPath);
			List pcList = new ArrayList();
			pcList.add(orderPc);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(pcList);
		}
	}

	// 判断po是否审核通过
	public boolean checkIsGuoShen(Integer orderId) {
		CotOrderFac fac = (CotOrderFac) this.getOrderDao().getById(
				CotOrderFac.class, orderId);
		if (fac.getOrderStatus() == 2) {
			return true;
		}
		return false;
	}
	
	//存放home页面里面的sc文件名
	public void saveScFile(Integer orderId,String fileName) throws DAOException{
//		CotOrder order = this.getOrderById(orderId);
//		//删除旧文件
//		if(order.getScFileName()!=null && !"".equals(order.getScFileName())){
//			File file = new File(SystemUtil.getRootPath() + "reportfile/orderSc/"+ order.getScFileName());
//			if(file.exists()){
//				file.delete();
//			}
//		}
		String outHql = "update CotOrder obj set obj.scFileName=:scFileName where obj.id=:id";
		Map map = new HashMap();
		map.put("scFileName",fileName);
		map.put("id", orderId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(outHql);
		this.getOrderDao().executeUpdate(queryInfo, map);
	}
	
	//存放home页面里面的client order文件名
	public void saveClientFile(Integer orderId,String fileName) throws DAOException{
//		CotOrder order = this.getOrderById(orderId);
//		//删除旧文件
//		if(order.getClientFile()!=null && !"".equals(order.getClientFile())){
//			File file = new File(SystemUtil.getRootPath() + "reportfile/orderSc/"+ order.getClientFile());
//			if(file.exists()){
//				file.delete();
//			}
//		}
		String outHql = "update CotOrder obj set obj.clientFile=:clientFile where obj.id=:id";
		Map map = new HashMap();
		map.put("clientFile",fileName);
		map.put("id", orderId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(outHql);
		this.getOrderDao().executeUpdate(queryInfo, map);
	}
	
	//存放home页面里面的client order文件名
	public String[] getFileNames(Integer orderId) throws DAOException{
		CotOrder order = this.getOrderById(orderId);
		String[] temp = new String[2];
		if(order.getScFileName()==null){
			temp[0]="";
		}else{
			temp[0]=order.getScFileName();
		}
		if(order.getClientFile()==null){
			temp[1]="";
		}else{
			temp[1]=order.getClientFile();
		}
		return temp;
	}
	
	public String getPIComment(Integer orderId) throws DAOException{
		CotOrder order = this.getOrderById(orderId);
		return order.getOrderRemark();
	}
	
	// 判断是否已经生成art work报表
	public boolean checkIsHasArtWork(String orderNo) {
		String rptPathPacking = SystemUtil.getRptFilePath() + "home/Product/Art work Briefing_"
				+ orderNo + ".pdf";
		File file=new File(rptPathPacking);
		return file.exists();
	}
	
	public void saveOrderStaMark(Integer orderId,Integer staId,String staMark,Integer type) throws DAOException{
		String remark="";
		String id="";
		if(type==0){
			id="staId";
			remark="staMark";
		}
		if(type==1){
			id="preStaId";
			remark="preStaMark";
		}
		if(type==2){
			id="artStaId";
			remark="artStaMark";
		}
		if(type==3){
			id="shiStaId";
			remark="shiStaMark";
		}
		if(type==4){
			id="qcStaId";
			remark="qcStaMark";
		}
		if(type==5){
			id="spStaId";
			remark="spStaMark";
		}
		String outHql = "update CotOrder obj set obj."+id+"=:staId,obj."+remark+"=:staMark where obj.id=:id";
		Map map = new HashMap();
		map.put("staId",staId);
		map.put("staMark",staMark);
		map.put("id", orderId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(outHql);
		this.getOrderDao().executeUpdate(queryInfo, map);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.order.CotOrderService#updateClientPo(java.lang.Integer, java.lang.String)
	 */
	@Override
	public int updateClientPo(Integer orderId, String clientPo) {
		String outHql = "update CotOrder obj set obj.poNo = :clientPO where obj.id=:id";
		Map map = new HashMap();
		map.put("clientPO",clientPo);
		map.put("id", orderId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(outHql);
		try {
			return this.getOrderDao().executeUpdate(queryInfo, map);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  -1;
		}
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.order.CotOrderService#updateOrderStatusAndAddDayRpt(java.lang.Integer)
	 */
	@Override
	public int updateOrderStatusAndAddDayRpt(Integer orderId) {
		// 更新pi的canOut
		CotOrder order = (CotOrder)this.getOrderDao().getById(CotOrder.class, orderId);
        String faclMb = "update CotOrder obj set obj.canOut=2  where obj.id=:id";
        Map map = new HashMap();
        map.put( "id" , orderId);
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setSelectString(faclMb);
        int result = -1;
		try {
			result = this.getOrderDao().executeUpdate(queryInfo, map);
			WebContext ctx = WebContextFactory.get();
			CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			//4、写入Daily Report.
			CotQuestion question = new CotQuestion();
			question.setOrderId(orderId);
			question.setQueryPerson(cotEmps.getId());
			String text = ContextUtil.getProperty("remoteaddr.properties", "wolly_cash_receive");
			text = text.replace("[orderNo]", order.getOrderNo());
			question.setQueryText(text);
			question.setFlag(9);
			question.setQueryTime(new Timestamp(System.currentTimeMillis()));
			List list = new ArrayList();
			list.add(question);
			this.getOrderDao().saveOrUpdateRecords(list);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}

	@Override
	public CotOrderFac getOrderFacByPiId(Integer PIid) {
		String sql = "select obj from CotOrderFac obj where obj.orderId="
			+ PIid;
		List listChk = this.getOrderDao().find(sql);
		CotOrderFac orderFac = new CotOrderFac();
		if (listChk != null && listChk.size() > 0){
			orderFac = (CotOrderFac)listChk.get(0);
		}
			
		return orderFac;
	}
	
	public List getExistBarcode(String barcodes,String orderId){
		String hql = "select distinct(barcode) from CotOrderDetail where barcode in("+barcodes+")";
		if(StringUtils.isNotEmpty(orderId) && !"null".equals(orderId)){
			hql += " and orderId != "+orderId;
		}
		List lst = this.getOrderDao().find(hql);
		return lst;
	}
	
}
