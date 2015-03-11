/**
 * 
 */
package com.sail.cot.service.orderout.impl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.CollectionUtils;
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
import com.sail.cot.domain.CotCommisionType;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotContainerType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFinacegiven;
import com.sail.cot.domain.CotFinacegivenDetail;
import com.sail.cot.domain.CotFinacerecv;
import com.sail.cot.domain.CotFinacerecvDetail;
import com.sail.cot.domain.CotFittingOrder;
import com.sail.cot.domain.CotFittingsOrderdetail;
import com.sail.cot.domain.CotHsInfo;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotNation;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutDel;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotOrderOutdetailDel;
import com.sail.cot.domain.CotOrderOuthsdetail;
import com.sail.cot.domain.CotOrderoutMb;
import com.sail.cot.domain.CotOrderoutPic;
import com.sail.cot.domain.CotOrderoutPicDel;
import com.sail.cot.domain.CotOrderouthsRpt;
import com.sail.cot.domain.CotPackingOrder;
import com.sail.cot.domain.CotPackingOrderdetail;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.domain.CotShipPort;
import com.sail.cot.domain.CotShipment;
import com.sail.cot.domain.CotSymbol;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.CotTargetPort;
import com.sail.cot.domain.CotTrafficType;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.CotTypeLv2;
import com.sail.cot.domain.VOrderFitorderId;
import com.sail.cot.domain.VOrderOrderfacId;
import com.sail.cot.domain.VOrderPackorderId;
import com.sail.cot.domain.vo.CotFinaceOtherVO;
import com.sail.cot.domain.vo.CotMsgVO;
import com.sail.cot.domain.vo.CotOrdeNameRptVO;
import com.sail.cot.domain.vo.CotOrderFacVO;
import com.sail.cot.domain.vo.CotOrderOutVO;
import com.sail.cot.domain.vo.CotOrderVO;
import com.sail.cot.mail.service.MailService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.order.impl.CotOrderServiceImpl;
import com.sail.cot.service.orderout.CotOrderOutService;
import com.sail.cot.service.price.impl.CotPriceServiceImpl;
import com.sail.cot.service.sign.impl.CotSignServiceImpl;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ListSort;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;

/**
 * 出货管理模块
 * 
 * @author qh-chzy
 * 
 */
public class CotOrderOutServiceImpl implements CotOrderOutService {

	private CotBaseDao orderOutDao;
	private CotExportRptDao exportRptDao;
	private MailService mailService;
	private CotSysLogService sysLogService;
	private CotOrderService orderService;
	private CotCustomerService custService;
	private CotReportDao reportDao;
	// private GenAllSeq seq = new GenAllSeq();
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	private Logger logger = Log4WebUtil.getLogger(CotOrderOutServiceImpl.class);

	// 保存或者更新主出货单
	public Integer[] saveOrUpdateOrderOut(CotOrderOut cotOrderOut,
			CotSymbol cotSymbol, CotHsInfo cotHsInfo,
			CotOrderouthsRpt cotOrderouthsRpt, String orderTime,String orderLcDate, boolean oldFlag) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CotOrderOut oldOrderOut = null;
		Integer[] idAry = new Integer[4];
		if (cotOrderOut.getId() == null) {
			cotOrderOut.setSplitFlag(0);
		}else{
			oldOrderOut = (CotOrderOut) this.getOrderOutDao().getById(CotOrderOut.class, cotOrderOut.getId());
		}
		// CotOrderoutMb cotOrderMb = null;
		// if (cotOrderOut.getId() == null) {
		// cotOrderMb = new CotOrderoutMb();
		// } else {
		// String hql = "from CotOrderoutMb obj where obj.fkId="
		// + cotOrderOut.getId();
		// List<?> listMB = this.getOrderOutDao().find(hql);
		// cotOrderMb = (CotOrderoutMb) listMB.get(0);
		// }

		// if (oldFlag) {
		// String hql = "select obj.picImg from CotCustMb obj where obj.fkId="
		// + cotOrderOut.getCustId();
		// List<?> listMB = this.getOrderOutDao().find(hql);
		// byte[] pm = (byte[]) listMB.get(0);
		// cotOrderMb.setPicImg(pm);
		// cotOrderMb.setPicSize(pm.length);
		// }

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			cotOrderOut.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
			if (orderTime != null && !"".equals(orderTime)) {
				cotOrderOut.setOrderTime(new Date(sdf.parse(orderTime).getTime()));// 签单时间
			}
			if (orderLcDate != null && !"".equals(orderLcDate)) {
				cotOrderOut.setOrderLcDate(new Date(sdf.parse(orderLcDate).getTime()));// 签单时间
			}
			cotOrderOut.setEmpId(cotEmps.getId());// 操作人编号
			List<CotOrderOut> list = new ArrayList<CotOrderOut>();

			// 如果状态为通过或不通过时,保存审核人姓名
			// if (cotOrderOut.getOrderStatus() == 1
			// || cotOrderOut.getOrderStatus() == 2) {
			// cotOrderOut.setCheckPerson(cotEmps.getEmpsName());
			// }

			if (oldOrderOut != null && (oldOrderOut.getOrderNo() == null || "".equals(cotOrderOut.getOrderNo()))) // 新增时更新客户序列号
			{
//				this.getCustService().updateCustSeqByType(
//						cotOrderOut.getCustId(), "orderout",
//						cotOrderOut.getOrderTime().toString());
				// 更新全局序列号
				// SetNoServiceImpl setNoService = new SetNoServiceImpl();
				// setNoService.saveSeq("orderout",
				// cotOrderOut.getOrderTime().toString());
//				CotSeqService cotSeqService = new CotSeqServiceImpl();
//				cotSeqService.saveCustSeq(cotOrderOut.getCustId(), "orderout",
//						cotOrderOut.getOrderTime().toString());
//				cotSeqService.saveSeq("orderout");
			}
			cotOrderOut.setOrderStatus(2l);
			
			if(cotOrderOut.getPaTypeId() != 8 )//Cash against doc.
			{
				// 更新pi的canOut
				String faclMb = "update CotOrder obj set obj.canOut=2  where obj.id=:id";
				Map map = new HashMap();
				map.put("id", cotOrderOut.getOrderId());
				QueryInfo queryInfo = new QueryInfo();
				queryInfo.setSelectString(faclMb);
				int result = this.getOrderOutDao().executeUpdate(queryInfo, map);
			}
			
			list.add(cotOrderOut);
			this.getOrderOutDao().saveOrUpdateRecords(list);
			// if (oldFlag) {
			// cotOrderMb.setFkId(cotOrderOut.getId());
			// List newMb = new ArrayList();
			// newMb.add(cotOrderMb);
			// this.getOrderOutDao().saveOrUpdateRecords(newMb);
			// }
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主出货单成功");
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotOrderOut.getOrderNo());
			sysLogService.addSysLogByObj(cotSyslog);

			// 保存抬头信息
			idAry[1] = null;
			if (cotSymbol != null) {
				cotSymbol.setOrderOutId(cotOrderOut.getId());
				List<CotSymbol> listSymbol = new ArrayList<CotSymbol>();
				listSymbol.add(cotSymbol);
				this.getOrderOutDao().saveOrUpdateRecords(listSymbol);
				idAry[1] = cotSymbol.getId();
			}

			// 保存报关信息
			idAry[2] = null;
			if (cotHsInfo != null) {
				cotHsInfo.setOrderOutId(cotOrderOut.getId());
				List<CotHsInfo> cotHsInfoList = new ArrayList<CotHsInfo>();
				cotHsInfoList.add(cotHsInfo);
				this.getOrderOutDao().saveOrUpdateRecords(cotHsInfoList);
				idAry[2] = cotHsInfo.getId();
			}

			// 保存合计信息
			idAry[3] = null;
			if (cotOrderouthsRpt != null) {
				cotOrderouthsRpt.setOrderOutId(cotOrderOut.getId());
				List<CotOrderouthsRpt> cotOrderouthsRptList = new ArrayList<CotOrderouthsRpt>();
				cotOrderouthsRptList.add(cotOrderouthsRpt);
				this.getOrderOutDao().saveOrUpdateRecords(cotOrderouthsRptList);
				idAry[3] = cotOrderouthsRpt.getId();
			}

			idAry[0] = cotOrderOut.getId();
			return idAry;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存出货单出错！");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(cotEmps.getId());
			cotSyslog.setOpMessage("添加或修改主出货单失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(cotOrderOut.getOrderNo());
			sysLogService.addSysLogByObj(cotSyslog);
			return null;
		}
	}

	// 根据币种换算价格
	public float updatePrice(Float price, Integer oldCurId, Integer newCurId) {
		if (price == null) {
			return 0;
		}
		// 先根据该的币种的汇率转成人民币,
		CotCurrency oldCur = (CotCurrency) this.getOrderOutDao().getById(
				CotCurrency.class, oldCurId);
		Float rmb = price * oldCur.getCurRate();
		// 在根据币种的汇率转成该币种的值
		CotCurrency newCur = (CotCurrency) this.getOrderOutDao().getById(
				CotCurrency.class, newCurId);
		Float obj = rmb / newCur.getCurRate();

		DecimalFormat nbf = new DecimalFormat("0.000");
		obj = Float.parseFloat(nbf.format(obj));
		return obj;
	}

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getOrderOutDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getOrderOutDao().getRecordsCountJDBC(queryInfo);
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getOrderOutDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getOrderOutDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}
	
	//查询数据字典 起运港和目的港的交货期限天数
	public Integer findDays(String shipId,String tarId){
		if(shipId==null || shipId.equals("")){
			shipId="0";
		}
		if(tarId==null || tarId.equals("")){
			tarId="0";
		}
		String hql="select obj.days from CotDays obj where obj.shipId="+shipId+" and obj.tarId="+tarId;
		List list =this.getOrderOutDao().find(hql);
		if(list!=null && list.size()>0){
			return (Integer) list.get(0);
		}else{
			return 40;
		}
	}
	
	public java.util.Date addDate(java.util.Date tp,int date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(tp.getTime());
		int d = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, d + date);
		java.util.Date newTp = new java.util.Date(
				calendar.getTimeInMillis());
		return newTp;
	}

	// 查询VO记录
	public List<?> getOrderVOList(QueryInfo queryInfo) {
		try {
			List<?> list = this.getOrderOutDao().findRecordsJDBC(queryInfo);
			Iterator<?> it =list.iterator();
			while(it.hasNext()){
				CotOrderOutVO cotOrderVO = (CotOrderOutVO) it.next();
				//判断delivery date是否要显示红色
				String shipId = cotOrderVO.getShipportId()==null?"0":cotOrderVO.getShipportId().toString();
				String tarId = cotOrderVO.getTargetportId()==null?"0":cotOrderVO.getTargetportId().toString();
				Integer temp = this.findDays(shipId,tarId);
				
				java.util.Date date = this.addDate(cotOrderVO.getSendTime(), temp);
				java.util.Date newDate = cotOrderVO.getOrderLcDate();
				if(newDate.getTime()<date.getTime()){
					cotOrderVO.setChk(1);
				}else{
					java.util.Date date2 = this.addDate(date, 37);
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

	// 根据条件查询主单记录
	public List<?> getOrderList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getOrderOutDao().findRecords(queryInfo,
					CotOrderOut.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询明细记录
	public List<?> getOrderMainList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getOrderOutDao().findRecords(queryInfo,
					CotOrder.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询明细记录
	public List<?> getOrderDetailList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getOrderOutDao().findRecords(queryInfo,
					CotOrderDetail.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getOrderOutDao().getRecords(objName);
	}

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId) {
		List<?> list = this.getOrderOutDao().find(
				"from CotRptFile f where f.rptType=" + rptTypeId);
		return list;
	}

	// 清空明细Map
	public void clearMap(String typeName) {
		SystemUtil.clearSessionByType(null, typeName);
	}

	// 得到公司的集合
	public List<?> getCompanyList() {
		List<?> list = this.getOrderOutDao().getRecords("CotCompany");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotCompany cotCompany = (CotCompany) it.next();
			cotCompany.setCompanyLogo(null);
		}
		return list;
	}

	// 得到员工的集合
	public List<?> getEmpsList() {
		List<?> list = this.getOrderOutDao().getRecords("CotEmps");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotEmps cotEmps = (CotEmps) it.next();
			cotEmps.setCustomers(null);
		}
		return list;
	}

	// 查询出货单号是否存在
	public Integer findIsExistOrderNo(String orderNo, String id) {
		String hql = "select obj.id from CotOrderOut obj where obj.orderNo='"
				+ orderNo + "'";
		List<?> res = this.getOrderOutDao().find(hql);
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

	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 查询所有客户编号
	public Map<?, ?> getCustMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map
					.put(cotCustomer.getId().toString(), cotCustomer
							.getCustomerNo());
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

	// 查询所有起运港
	public Map<?, ?> getShipPortMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotShipPort");
		for (int i = 0; i < list.size(); i++) {
			CotShipPort cotShipPort = (CotShipPort) list.get(i);
			map.put(cotShipPort.getId().toString(), cotShipPort
					.getShipPortNameEn());
		}
		return map;
	}

	// 查询所有目的港
	public Map<?, ?> getTargetPortMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotTargetPort");
		for (int i = 0; i < list.size(); i++) {
			CotTargetPort cotTargetPort = (CotTargetPort) list.get(i);
			map.put(cotTargetPort.getId().toString(), cotTargetPort
					.getTargetPortEnName());
		}
		return map;
	}

	// 查询所有运输方式
	public Map<?, ?> getTrafficTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotTrafficType");
		for (int i = 0; i < list.size(); i++) {
			CotTrafficType cotTrafficType = (CotTrafficType) list.get(i);
			map.put(cotTrafficType.getId().toString(), cotTrafficType
					.getTrafficNameEn());
		}
		return map;
	}

	// 查询所有出口商
	public Map<?, ?> getCompanyMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotCompany");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany
					.getCompanyShortName());
		}
		return map;
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有国家
	public Map<?, ?> getNationMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotNation");
		for (int i = 0; i < list.size(); i++) {
			CotNation cotNation = (CotNation) list.get(i);
			map.put(cotNation.getId().toString(), cotNation.getNationName());
		}
		return map;
	}

	// 查询所有价格条款
	public Map<?, ?> getClauseMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotClause");
		for (int i = 0; i < list.size(); i++) {
			CotClause cotClause = (CotClause) list.get(i);
			map.put(cotClause.getId().toString(), cotClause.getClauseName());
		}
		return map;
	}

	// 查询所有集装箱
	public Map<?, ?> getContainerTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotContainerType");
		for (int i = 0; i < list.size(); i++) {
			CotContainerType cotContainerType = (CotContainerType) list.get(i);
			map.put(cotContainerType.getId().toString(), cotContainerType
					.getContainerName());
		}
		return map;
	}

	// 查询所有佣金类型
	public Map<?, ?> getCommisionTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotCommisionType");
		for (int i = 0; i < list.size(); i++) {
			CotCommisionType cotCommisionType = (CotCommisionType) list.get(i);
			map.put(cotCommisionType.getId().toString(), cotCommisionType
					.getCommisionName());
		}
		return map;
	}

	// 查询所有主订单号
	public Map<?, ?> getAllOrderNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getOrderOutDao().getRecords("CotOrder");
		for (int i = 0; i < list.size(); i++) {
			CotOrder cotOrder = (CotOrder) list.get(i);
			map.put(cotOrder.getId().toString(), cotOrder.getOrderNo());
		}
		return map;
	}

	// Action储存出货明细Map
	@SuppressWarnings("unchecked")
	public void setMapAction(HttpSession session, Integer detailId,
			CotOrderOutdetail cotOrderOutdetail) {
		Object obj = SystemUtil.getObjBySession(session, "orderout");
		if (obj == null) {
			HashMap<Integer, CotOrderOutdetail> orderMap = new HashMap<Integer, CotOrderOutdetail>();
			orderMap.put(detailId, cotOrderOutdetail);
			SystemUtil.setObjBySession(session, orderMap, "orderout");
		} else {
			HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
			orderMap.put(detailId, cotOrderOutdetail);
			SystemUtil.setObjBySession(session, orderMap, "orderout");
		}

	}

	// Action储存出货明细Map
	@SuppressWarnings("unchecked")
	public void setMapDelAction(HttpSession session, Integer detailId,
			CotOrderOutdetailDel cotOrderOutdetail) {
		Object obj = SystemUtil.getObjBySession(session, "orderoutdel");
		if (obj == null) {
			HashMap<Integer, CotOrderOutdetailDel> orderMap = new HashMap<Integer, CotOrderOutdetailDel>();
			orderMap.put(detailId, cotOrderOutdetail);
			SystemUtil.setObjBySession(session, orderMap, "orderoutdel");
		} else {
			HashMap<Integer, CotOrderOutdetailDel> orderMap = (HashMap<Integer, CotOrderOutdetailDel>) obj;
			orderMap.put(detailId, cotOrderOutdetail);
			SystemUtil.setObjBySession(session, orderMap, "orderoutdel");
		}

	}

	// Action储存差额数据Map
	@SuppressWarnings("unchecked")
	public void setChaMapAction(HttpSession session, Integer detailId,
			CotOrderOuthsdetail cotOrderOutdetail) {
		Object obj = SystemUtil.getObjBySession(session, "cha");
		if (obj == null) {
			HashMap<Integer, CotOrderOuthsdetail> orderMap = new HashMap<Integer, CotOrderOuthsdetail>();
			orderMap.put(detailId, cotOrderOutdetail);
			SystemUtil.setObjBySession(session, orderMap, "cha");
		} else {
			HashMap<Integer, CotOrderOuthsdetail> orderMap = (HashMap<Integer, CotOrderOuthsdetail>) obj;
			orderMap.put(detailId, cotOrderOutdetail);
			SystemUtil.setObjBySession(session, orderMap, "cha");
		}

	}

	// Action储存差额数据Map
	@SuppressWarnings("unchecked")
	// public void setChaDelMapAction(HttpSession session, Integer detailId,
	// CotOrderOuthsdetailDel cotOrderOutdetail) {
	// Object obj = SystemUtil.getObjBySession(session, "chaDel");
	// if (obj == null) {
	// HashMap<Integer, CotOrderOuthsdetailDel> orderMap = new HashMap<Integer,
	// CotOrderOuthsdetailDel>();
	// orderMap.put(detailId, cotOrderOutdetail);
	// SystemUtil.setObjBySession(session, orderMap, "chaDel");
	// } else {
	// HashMap<Integer, CotOrderOuthsdetailDel> orderMap = (HashMap<Integer,
	// CotOrderOuthsdetailDel>) obj;
	// orderMap.put(detailId, cotOrderOutdetail);
	// SystemUtil.setObjBySession(session, orderMap, "chaDel");
	// }
	//
	// }
	// 根据出货编号查找出货单信息
	public CotOrderOut getOrderOutById(Integer id) {
		CotOrderOut cotOrderOut = (CotOrderOut) this.getOrderOutDao().getById(
				CotOrderOut.class, id);
		if (cotOrderOut != null) {
			CotOrderOut colone = (CotOrderOut) SystemUtil
					.deepClone(cotOrderOut);
			colone.setCotOrderOutdetails(null);
			colone.setCotOrderOuthsdetails(null);
			colone.setCotSplitInfos(null);
			colone.setCotHsInfos(null);
			colone.setCotShipments(null);
			colone.setCotSymbols(null);
			colone.setOrderMBImg(null);
			colone.setCotOrderouthsRpt(null);
			return colone;
		}
		return null;
	}

	// 根据出货编号查找作废单信息
	public CotOrderOutDel getOrderOutDelById(Integer id) {
		CotOrderOutDel cotOrderOutDel = (CotOrderOutDel) this.getOrderOutDao()
				.getById(CotOrderOutDel.class, id);
		if (cotOrderOutDel != null) {
			CotOrderOutDel colone = (CotOrderOutDel) SystemUtil
					.deepClone(cotOrderOutDel);
			// colone.setCotOrderOutdetails(null);
			// colone.setCotOrderOuthsdetails(null);
			// colone.setCotSplitInfos(null);
			// colone.setCotHsInfos(null);
			// colone.setCotShipments(null);
			// colone.setCotSymbols(null);
			// colone.setOrderMBImg(null);
			// colone.setCotOrderouthsRpt(null);
			return colone;
		}
		return null;
	}

	// 查询出货单的总金额
	public Float findTotalMoney(Integer orderId) {
		String hql = "select obj.totalMoney from CotOrderOutdetail obj where obj.orderId="
				+ orderId;
		List<?> list = this.getOrderOutDao().find(hql);
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

	// 查询差额单的总金额
	public Float findChaTotalMoney(Integer orderId) {
		String hql = "select obj.totalMoney from CotOrderOuthsdetail obj where obj.orderId="
				+ orderId;
		List<?> list = this.getOrderOutDao().find(hql);
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

	// 删除主出货单
	@SuppressWarnings("deprecation")
	public Boolean deleteOrders(List<?> ids, String invalidNo) throws Exception {
		// 还原所有订单数量
		String temp = "";
		for (int i = 0; i < ids.size(); i++) {
			temp += ids.get(i) + ",";
		}
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");

		String oId = temp.substring(0, temp.length() - 1);
		CotOrderOut out = (CotOrderOut) this.getOrderOutDao().getById(
				CotOrderOut.class, Integer.parseInt(oId));

		// 备份主单
		CotOrderOutDel orderOutDel = new CotOrderOutDel();
		CotOrderOutDel orderOutDel2 = new CotOrderOutDel();
		// 使用反射获取对象的所有属性名称
		String[] propEle = ReflectionUtils
				.getDeclaredFields(CotOrderOutDel.class);

		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter, Date.class);
		// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
				new PropertyUtilsBean());

		for (int i = 0; i < propEle.length; i++) {
			if (!propEle[i].equals("orderMBImg")
					&& !propEle[i].equals("cotOrderOutdetails")
					&& !propEle[i].equals("cotOrderOuthsdetails")
					&& !propEle[i].equals("cotSplitInfos")
					&& !propEle[i].equals("cotHsInfos")
					&& !propEle[i].equals("cotShipments")
					&& !propEle[i].equals("cotSymbols")
					&& !propEle[i].equals("cotOrderouthsRpt")) {

				String value = beanUtils.getProperty(out, propEle[i]);
				if (value != null) {
					beanUtils.setProperty(orderOutDel, propEle[i], value);
					beanUtils.setProperty(orderOutDel2, propEle[i], value);
				}
			}
		}

		orderOutDel.setId(null);
		orderOutDel.setOrderStatus(8l);//作废旧单
		orderOutDel2.setId(null);
		orderOutDel2.setOrderNo(invalidNo);
		orderOutDel2.setOrderStatus(9l);//作废新单
		List listDel = new ArrayList();
		listDel.add(orderOutDel);
		listDel.add(orderOutDel2);
		this.getOrderOutDao().saveRecords(listDel);

		List<CotOrderDetail> listDetail = new ArrayList<CotOrderDetail>();

		String hql = "from CotOrderOutdetail obj where obj.orderId in ("
				+ temp.substring(0, temp.length() - 1) + ")";
		Integer orderId = 0;
		List<?> list = this.getOrderOutDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotOrderOutdetail cotOrderOutdetail = (CotOrderOutdetail) list
					.get(i);
			CotOrderDetail cotOrderDetail = (CotOrderDetail) this
					.getOrderOutDao().getById(CotOrderDetail.class,
							cotOrderOutdetail.getOrderDetailId());
			orderId = cotOrderDetail.getOrderId();
			if (cotOrderDetail != null) {
				cotOrderDetail.setUnBoxCount(cotOrderDetail.getUnBoxCount()
						+ cotOrderOutdetail.getBoxCount());
				cotOrderDetail.setUnBoxSend(0);
				listDetail.add(cotOrderDetail);
			}

			// 备份明细
			CotOrderOutdetailDel orderOutdetailDel = new CotOrderOutdetailDel();
			CotOrderOutdetailDel orderOutdetailDel2 = new CotOrderOutdetailDel();
			// 使用反射获取对象的所有属性名称
			String[] propEleDetail = ReflectionUtils
					.getDeclaredFields(CotOrderOutdetail.class);

			for (int j = 0; j < propEleDetail.length; j++) {
				String value = beanUtils.getProperty(cotOrderOutdetail,
						propEleDetail[j]);
				if (value != null) {
					beanUtils.setProperty(orderOutdetailDel, propEleDetail[j],
							value);
					beanUtils.setProperty(orderOutdetailDel2, propEleDetail[j],
							value);
				}
			}
			orderOutdetailDel.setId(null);
			orderOutdetailDel2.setId(null);
			orderOutdetailDel.setOrderId(orderOutDel.getId());
			orderOutdetailDel2.setOrderId(orderOutDel2.getId());
			List<CotOrderOutdetailDel> listDelDetail = new ArrayList<CotOrderOutdetailDel>();
			listDelDetail.add(orderOutdetailDel);
			listDelDetail.add(orderOutdetailDel2);
			this.getOrderOutDao().saveRecords(listDelDetail);

			// 备份明细图片
			CotOrderoutPic orderPic = impOpService
					.getOrderOutPic(cotOrderOutdetail.getId());
			CotOrderoutPicDel orderoutPicDel = new CotOrderoutPicDel();
			CotOrderoutPicDel orderoutPicDel2 = new CotOrderoutPicDel();
			orderoutPicDel.setEleId(orderPic.getEleId());
			orderoutPicDel.setFkId(orderOutdetailDel.getId());
			orderoutPicDel.setPicImg(orderPic.getPicImg());
			orderoutPicDel.setPicName(orderPic.getPicName());
			orderoutPicDel.setPicSize(orderPic.getPicSize());

			orderoutPicDel2.setEleId(orderPic.getEleId());
			orderoutPicDel2.setFkId(orderOutdetailDel2.getId());
			orderoutPicDel2.setPicImg(orderPic.getPicImg());
			orderoutPicDel2.setPicName(orderPic.getPicName());
			orderoutPicDel2.setPicSize(orderPic.getPicSize());

			List<CotOrderoutPicDel> listPicDelDetail = new ArrayList<CotOrderoutPicDel>();
			listPicDelDetail.add(orderoutPicDel);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(listPicDelDetail);

			List<CotOrderoutPicDel> listPicDelDetail2 = new ArrayList<CotOrderoutPicDel>();
			listPicDelDetail2.add(orderoutPicDel2);
			impOpService.saveImg(listPicDelDetail2);
		}
		// this.getOrderOutDao().saveRecords(listPicDelDetail);

		if (listDetail.size() > 0) {
			this.getOrderOutDao().updateRecords(listDetail);
		}
		this.getOrderOutDao().deleteRecordByIds(ids, "CotOrderOut");
		// 不删除旧单,再根据订单生成一份新数据
		this.getOrderService().saveInvoice(orderId, true);

		return true;
	}

	// 判断货号是否已经添加
	@SuppressWarnings("unchecked")
	public boolean checkExist(String eleId) {
		Object obj = SystemUtil.getObjBySession(null, "orderout");
		HashMap<String, CotOrderOutdetail> orderMap = (HashMap<String, CotOrderOutdetail>) obj;
		if (orderMap != null) {
			if (orderMap.containsKey(eleId.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	// 判断该产品货号是否存在,并且返回该产品的默认图片
	public Integer findIsExistEleByEleId(String eleId) {
		String hql = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() != 0) {
			return (Integer) list.get(0);
		} else {
			return null;
		}
	}

	// 通过key获取Map的value
	@SuppressWarnings("unchecked")
	public CotOrderOutdetail getOrderMapValue(Integer orderDetailId) {
		Object obj = SystemUtil.getObjBySession(null, "orderout");
		HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
		if (orderMap != null) {
			CotOrderOutdetail cotOrderOutdetail = (CotOrderOutdetail) orderMap
					.get(orderDetailId);
			return cotOrderOutdetail;
		}
		return null;
	}

	// 通过key获取Map的value
	@SuppressWarnings("unchecked")
	public CotOrderOutdetailDel getOrderMapDelValue(Integer orderDetailId) {
		Object obj = SystemUtil.getObjBySession(null, "orderoutdel");
		HashMap<Integer, CotOrderOutdetailDel> orderMap = (HashMap<Integer, CotOrderOutdetailDel>) obj;
		if (orderMap != null) {
			CotOrderOutdetailDel cotOrderOutdetail = (CotOrderOutdetailDel) orderMap
					.get(orderDetailId);
			return cotOrderOutdetail;
		}
		return null;
	}

	// 通过key获取Map的value
	@SuppressWarnings("unchecked")
	public CotOrderOuthsdetail getChaOrderMapValue(Integer orderDetailId) {
		Object obj = SystemUtil.getObjBySession(null, "cha");
		HashMap<Integer, CotOrderOuthsdetail> orderMap = (HashMap<Integer, CotOrderOuthsdetail>) obj;
		if (orderMap != null) {
			CotOrderOuthsdetail cotOrderOuthsdetail = (CotOrderOuthsdetail) orderMap
					.get(orderDetailId);
			return cotOrderOuthsdetail;
		}
		return null;
	}

	// 通过key获取Map的value
	// public CotOrderOuthsdetailDel getChaOrderDelMapValue(Integer
	// orderDetailId) {
	// Object obj = SystemUtil.getObjBySession(null, "chaDel");
	// HashMap<Integer, CotOrderOuthsdetailDel> orderMap = (HashMap<Integer,
	// CotOrderOuthsdetailDel>) obj;
	// if (orderMap != null) {
	// CotOrderOuthsdetailDel cotOrderOuthsdetail = (CotOrderOuthsdetailDel)
	// orderMap
	// .get(orderDetailId);
	// return cotOrderOuthsdetail;
	// }
	// return null;
	// }

	// 储存Map
	@SuppressWarnings("unchecked")
	public void setOrderMap(Integer orderDetailId,
			CotOrderOutdetail cotOrderDetail) {
		Object obj = SystemUtil.getObjBySession(null, "orderout");
		if (obj == null) {
			HashMap<Integer, CotOrderOutdetail> orderMap = new HashMap<Integer, CotOrderOutdetail>();
			orderMap.put(orderDetailId, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "orderout");
		} else {
			HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
			orderMap.put(orderDetailId, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "orderout");
		}
	}

	// 储存Map
	@SuppressWarnings("unchecked")
	public void setChaOrderMap(Integer orderDetailId,
			CotOrderOuthsdetail cotOrderDetail) {
		Object obj = SystemUtil.getObjBySession(null, "cha");
		if (obj == null) {
			HashMap<Integer, CotOrderOuthsdetail> orderMap = new HashMap<Integer, CotOrderOuthsdetail>();
			orderMap.put(orderDetailId, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "cha");
		} else {
			HashMap<Integer, CotOrderOuthsdetail> orderMap = (HashMap<Integer, CotOrderOuthsdetail>) obj;
			orderMap.put(orderDetailId, cotOrderDetail);
			SystemUtil.setObjBySession(null, orderMap, "cha");
		}
	}

	// 通过货号修改Map中对应的明细
	public boolean updateMapValueByEleId(Integer orderDetailId,
			String property, String value) {
		CotOrderOutdetail cotOrderOutdetail = getOrderMapValue(orderDetailId);
		if (cotOrderOutdetail == null)
			return false;
		try {
			if ("factoryId".equals(property)) {
				if ("".equals(value)) {
					cotOrderOutdetail.setFactoryId(null);
				} else {
					cotOrderOutdetail.setFactoryId(Integer.parseInt(value));
				}
			} else if ("eleTypeidLv1".equals(property)) {
				if ("".equals(value)) {
					cotOrderOutdetail.setEleTypeidLv1(null);
				} else {
					cotOrderOutdetail.setEleTypeidLv1(Integer.parseInt(value));
				}
			} else if ("eleHsid".equals(property)) {
				if ("".equals(value)) {
					cotOrderOutdetail.setEleHsid(null);
				} else {
					cotOrderOutdetail.setEleHsid(Integer.parseInt(value));
				}
			} else {
				BeanUtils.setProperty(cotOrderOutdetail, property, value);
			}
			this.setOrderMap(orderDetailId, cotOrderOutdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 通过货号修改Map中对应的明细
	public boolean updateChaMapValueByEleId(Integer orderDetailId,
			String property, String value) {
		CotOrderOuthsdetail cotOrderOutdetail = getChaOrderMapValue(orderDetailId);
		if (cotOrderOutdetail == null)
			return false;
		try {
			if ("factoryId".equals(property)) {
				if ("".equals(value)) {
					cotOrderOutdetail.setFactoryId(null);
				} else {
					cotOrderOutdetail.setFactoryId(Integer.parseInt(value));
				}
			} else if ("eleTypeidLv1".equals(property)) {
				if ("".equals(value)) {
					cotOrderOutdetail.setEleTypeidLv1(null);
				} else {
					cotOrderOutdetail.setEleTypeidLv1(Integer.parseInt(value));
				}
			} else if ("eleHsid".equals(property)) {
				if ("".equals(value)) {
					cotOrderOutdetail.setEleHsid(null);
				} else {
					cotOrderOutdetail.setEleHsid(Integer.parseInt(value));
				}
			} else {
				BeanUtils.setProperty(cotOrderOutdetail, property, value);
			}
			this.setChaOrderMap(orderDetailId, cotOrderOutdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 修改Map中的差额数量
	public boolean updateChaMap(Integer orderDetailId, Integer chaValue) {
		CotOrderOuthsdetail cotOrderOutdetail = getChaOrderMapValue(orderDetailId);
		if (cotOrderOutdetail == null)
			return false;
		try {
			long boxCount = cotOrderOutdetail.getBoxCount() + chaValue;
			// 修改数量
			cotOrderOutdetail.setBoxCount(boxCount);
			// 外箱数
			long boxOCount = cotOrderOutdetail.getBoxObCount();
			long containerBox;
			if (boxCount % boxOCount != 0) {
				containerBox = boxCount / boxOCount + 1;
			} else {
				containerBox = boxCount / boxOCount;
			}
			// 修改箱数
			cotOrderOutdetail.setContainerCount(containerBox);
			this.setChaOrderMap(orderDetailId, cotOrderOutdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 修改Map中的出货数量
	public boolean updateMap(Integer orderDetailId, Integer chaValue) {
		CotOrderOutdetail cotOrderOutdetail = getOrderMapValue(orderDetailId);
		if (cotOrderOutdetail == null)
			return false;
		try {
			long boxCount = cotOrderOutdetail.getBoxCount() + chaValue;
			// 修改数量
			cotOrderOutdetail.setBoxCount(boxCount);
			// 修改未发货量
			cotOrderOutdetail.setUnSendNum(cotOrderOutdetail.getUnSendNum()
					- chaValue);
			// 外箱数
			long boxOCount = cotOrderOutdetail.getBoxObCount();
			long containerBox;
			if (boxCount % boxOCount != 0) {
				containerBox = boxCount / boxOCount + 1;
			} else {
				containerBox = boxCount / boxOCount;
			}
			// 修改箱数
			cotOrderOutdetail.setContainerCount(containerBox);
			this.setOrderMap(orderDetailId, cotOrderOutdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 根据明细编号查找明细单信息
	public CotOrderOutdetail getOrderDetailById(Integer id) {
		CotOrderOutdetail cotOrderDetail = (CotOrderOutdetail) this
				.getOrderOutDao().getById(CotOrderOutdetail.class, id);
		cotOrderDetail.setPicImg(null);
		return cotOrderDetail;
	}

	// 删除明细图片picImg
	public boolean deletePicImg(Integer detailId) {
		String classPath = CotOrderOutServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		// CotOrderOutdetail cotOrderDetail = this.getOrderDetailById(detailId);
		List<CotOrderoutPic> imgList = new ArrayList<CotOrderoutPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotOrderoutPic orderPic = impOpService.getOrderOutPic(detailId);
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
			// this.getOrderOutDao().update(cotOrderDetail);
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

	// Action获取出货明细Map
	@SuppressWarnings("unchecked")
	public HashMap<Integer, CotOrderOutdetail> getMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "orderout");
		HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
		return orderMap;
	}

	// Action获取差额数据Map
	@SuppressWarnings("unchecked")
	public HashMap<Integer, CotOrderOuthsdetail> getChaMapAction(
			HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "cha");
		HashMap<Integer, CotOrderOuthsdetail> orderMap = (HashMap<Integer, CotOrderOuthsdetail>) obj;
		return orderMap;
	}

	// 更新出货单的产品明细
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
			this.getOrderOutDao().updateRecords(details);
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改出货明细成功");
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return true;
		} catch (Exception e) {
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改出货明细失败,失败原因:" + e.getMessage());
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return false;
		}
	}

	// 更新出货单的差额明细
	public Boolean modifyChaOrderDetails(List<?> details) {
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
			this.getOrderOutDao().updateRecords(details);
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改出货差额明细成功");
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return true;
		} catch (Exception e) {
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改出货差额明细失败,失败原因:" + e.getMessage());
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return false;
		}
	}

	// 保存出货单的产品明细
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

		try {
			this.getOrderOutDao().saveOrUpdateRecords(details);
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量保存或修改出货明细成功");
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(1);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return true;
		} catch (Exception e) {
			logger.error("保存出货明细出错!");
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量保存或修改出货明细失败,失败原因:" + e.getMessage());
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(1);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return false;
		}
	}

	// 保存出货单的差额明细
	public Boolean addChaOrderDetails(List<?> details, Integer id) {
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

		for (int i = 0; i < details.size(); i++) {
			CotOrderOuthsdetail temp = (CotOrderOuthsdetail) details.get(i);
			// 删除有改订单编号的差额明细
			// String sql = "delete from CotOrderOuthsdetail obj where
			// obj.orderId="+id+" and
			// obj.orderDetailId="+temp.getOrderDetailId();
		}
		try {
			this.getOrderOutDao().saveOrUpdateRecords(details);
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量保存或修改出货差额明细成功");
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(1);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return true;
		} catch (Exception e) {
			logger.error("保存出货明细出错!");
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(cotEmps.getId());
				cotSyslog
						.setOpMessage("批量保存或修改出货差额明细失败,失败原因:" + e.getMessage());
				cotSyslog.setOpModule("orderOut");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(1);
				sysLogService.addSysLogByObj(cotSyslog);
			}
			return false;
		}
	}

	// 保存出货单的差额明细
	public Boolean addChaOrderDetailsAction(HttpServletRequest request,
			List<?> details, Integer id) {
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		for (int i = 0; i < details.size(); i++) {
			CotOrderOuthsdetail temp = (CotOrderOuthsdetail) details.get(i);
			// 删除有改订单编号的差额明细
			// String sql = "delete from CotOrderOuthsdetail obj where
			// obj.orderId="+id+" and
			// obj.orderDetailId="+temp.getOrderDetailId();
		}
		try {
			this.getOrderOutDao().saveOrUpdateRecords(details);
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量保存或修改出货差额明细成功");
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			sysLogService.addSysLogByObj(cotSyslog);
			return true;
		} catch (Exception e) {
			logger.error("保存出货明细出错!");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量保存或修改出货差额明细失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
	}

	// 清除Map中detailId对应的映射
	@SuppressWarnings("unchecked")
	public void delMapByKey(Integer detailId) {
		Object obj = SystemUtil.getObjBySession(null, "orderout");
		HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
		if (orderMap != null) {
			if (orderMap.containsKey(detailId)) {
				orderMap.remove(detailId);
			}
		}
	}

	// 清除Map中detailId对应的映射
	@SuppressWarnings("unchecked")
	public void delChaMapByKey(Integer detailId) {
		Object obj = SystemUtil.getObjBySession(null, "cha");
		HashMap<Integer, CotOrderOuthsdetail> orderMap = (HashMap<Integer, CotOrderOuthsdetail>) obj;
		if (orderMap != null) {
			if (orderMap.containsKey(detailId)) {
				orderMap.remove(detailId);
			}
		}
	}

	// 在Action中清除Map中detailId对应的映射
	public void delMapByKeyAction(Integer detailId, HttpSession session) {
		HashMap<Integer, CotOrderOutdetail> orderMap = this
				.getMapAction(session);
		if (orderMap != null) {
			if (orderMap.containsKey(detailId)) {
				orderMap.remove(detailId);
			}
		}
	}

	// 在Action中清除Map中detailId对应的映射
	public void delChaMapByKeyAction(Integer detailId, HttpSession session) {
		HashMap<Integer, CotOrderOuthsdetail> orderMap = this
				.getChaMapAction(session);
		if (orderMap != null) {
			if (orderMap.containsKey(detailId)) {
				orderMap.remove(detailId);
			}
		}
	}

	// 查询订单明细
	public List<?> findDetailByIds(String orderDetailIds) {
		String hql = "from CotOrderDetail obj where obj.id in("
				+ orderDetailIds.substring(0, orderDetailIds.length() - 1)
				+ ") order by obj.sortNo";
		return this.getOrderOutDao().find(hql);
	}

	// 修改主单的总数量,总箱数,总体积,总金额
	public void modifyOrderOutTotalAction(List<?> details,
			HttpServletRequest request) throws Exception {
		// 获得登录人
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		try {
			this.getOrderOutDao().saveOrUpdateRecords(details);
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量保存或修改出货明细成功");
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存出货明细出错!");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量保存或修改出货明细失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			sysLogService.addSysLogByObj(cotSyslog);
			throw new Exception("保存出货明细出错");
		}
	}

	// 修改主单的总数量,总箱数,总体积,总金额
	public void modifyChaTotalAction(List<?> details, HttpServletRequest request)
			throws Exception {
		// 获得登录人
		Integer empId = (Integer) request.getSession().getAttribute("empId");

		try {
			this.getOrderOutDao().saveOrUpdateRecords(details);
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量保存或修改出货明细成功");
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存出货明细出错!");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量保存或修改出货明细失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			sysLogService.addSysLogByObj(cotSyslog);
			throw new Exception("保存出货明细出错");
		}
	}

	// 存储最新的出货明细统计信息
	public void getTotalOutDetail(HttpServletRequest request, Integer orderId) {
		Float totalMoney = 0.0f;
		int totalCount = 0;
		int totalContainer = 0;
		Float totalCBM = 0.0f;
		Float totalNet = 0.0f;
		Float totalGross = 0.0f;
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.00");
		DecimalFormat dfWeigh = new DecimalFormat("#.000");
		String hql = "from CotOrderOutdetail obj where obj.orderId=" + orderId;
		List<?> details = this.getOrderOutDao().find(hql);
		BigDecimal temp = new BigDecimal("0");
		for (int i = 0; i < details.size(); i++) {
			CotOrderOutdetail detail = (CotOrderOutdetail) details.get(i);
			if (detail.getTotalMoney() == null) {
				detail.setTotalMoney(0f);
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
			if (detail.getTotalNetWeigth() == null) {
				detail.setTotalNetWeigth(0f);
			}
			if (detail.getTotalGrossWeigth() == null) {
				detail.setTotalGrossWeigth(0f);
			}
			temp = temp.add(new BigDecimal(detail.getTotalMoney().toString()));
			// totalMoney += detail.getTotalMoney();
			totalCount += detail.getBoxCount();
			totalContainer += detail.getContainerCount();
			totalCBM += detail.getTotalCbm();
			totalNet += detail.getTotalNetWeigth();
			totalGross += detail.getTotalGrossWeigth();
		}
		totalMoney = temp.floatValue();
		totalCBM = Float.parseFloat(dfWeigh.format(totalCBM));
		totalNet = Float.parseFloat(dfWeigh.format(totalNet));
		totalGross = Float.parseFloat(dfWeigh.format(totalGross));
		request.getSession().setAttribute("totalMoney", totalMoney);
		request.getSession().setAttribute("totalCount", totalCount);
		request.getSession().setAttribute("totalContainer", totalContainer);
		request.getSession().setAttribute("totalCBM", totalCBM);
		request.getSession().setAttribute("totalNet", totalNet);
		request.getSession().setAttribute("totalGross", totalGross);
	}

	// 存储最新的报关明细统计信息
	public void getTotalChaDetail(HttpServletRequest request, Integer orderId) {
		Float totalMoney = 0.0f;
		int totalCount = 0;
		int totalContainer = 0;
		Float totalCBM = 0.0f;
		Float totalNet = 0.0f;
		Float totalGross = 0.0f;
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.00");
		DecimalFormat dfWeigh = new DecimalFormat("#.000");
		String hql = "from CotOrderOuthsdetail obj where obj.orderId="
				+ orderId;
		List<?> details = this.getOrderOutDao().find(hql);
		for (int i = 0; i < details.size(); i++) {
			CotOrderOuthsdetail detail = (CotOrderOuthsdetail) details.get(i);
			if (detail.getTotalMoney() == null) {
				detail.setTotalMoney(0f);
			}
			if (detail.getBoxCount() == null) {
				detail.setBoxCount(0L);
			}
			if (detail.getContainerCount() == null) {
				detail.setContainerCount(0L);
			}
			if (detail.getTotalCbm() == null) {
				detail.setTotalCbm(0f);
			}
			if (detail.getTotalNetWeigth() == null) {
				detail.setTotalNetWeigth(0f);
			}
			if (detail.getTotalGrossWeigth() == null) {
				detail.setTotalGrossWeigth(0f);
			}
			totalMoney += detail.getTotalMoney();
			totalCount += detail.getBoxCount();
			totalContainer += detail.getContainerCount();
			totalCBM += detail.getTotalCbm();
			totalNet += detail.getTotalNetWeigth();
			totalGross += detail.getTotalGrossWeigth();
		}
		totalMoney = Float.parseFloat(df.format(totalMoney));
		totalCBM = Float.parseFloat(dfWeigh.format(totalCBM));
		totalNet = Float.parseFloat(dfWeigh.format(totalNet));
		totalGross = Float.parseFloat(dfWeigh.format(totalGross));
		request.getSession().setAttribute("totalHsMoney", totalMoney);
		request.getSession().setAttribute("totalHsCount", totalCount);
		request.getSession().setAttribute("totalHsContainer", totalContainer);
		request.getSession().setAttribute("totalHsCBM", totalCBM);
		request.getSession().setAttribute("totalHsNet", totalNet);
		request.getSession().setAttribute("totalHsGross", totalGross);
	}

	// 判断该主单的明细是否已存在该产品货号
	@SuppressWarnings("unchecked")
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId) {
		List<Integer> res = this.getOrderOutDao().find(
				"select c.id from CotOrderOutdetail c where c.orderId = "
						+ mainId + " and c.eleId='" + eleId + "'");
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

	// 更新出货单的产品明细
	public Boolean modifyOrderDetail(CotOrderOutdetail e, String eleProTime) {
		// // 没选择厂家时,设置厂家为'未定义'
		// if (e.getFactoryId() == null) {
		// String hql = "from CotFactory obj where obj.shortName='未定义'";
		// List<?> list = this.getOrderOutDao().find(hql);
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
			this.getOrderOutDao().update(e);
		} catch (Exception ex) {
			logger.error("保存样品错误!");
			return false;
		}
		return true;
	}

	// 更新出货明细图片picImg字段
	public void updatePicImg(String filePath, Integer detailId) {
		CotOrderOutdetail orderOutdetail = this.getOrderDetailById(detailId);
		List<CotOrderoutPic> imgList = new ArrayList<CotOrderoutPic>();
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotOrderoutPic orderOutPic = impOpService.getOrderOutPic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			if (orderOutPic == null) {
				orderOutPic = new CotOrderoutPic();
				orderOutPic.setFkId(detailId);
				orderOutPic.setEleId(orderOutdetail.getEleId());
				orderOutPic.setPicName(orderOutdetail.getEleId());
			}
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			orderOutPic.setPicImg(b);
			orderOutPic.setPicSize(b.length);
			imgList.add(orderOutPic);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			impOpService.saveOrUpdateImg(imgList);
		} catch (Exception e) {
			logger.error("修改出货明细图片错误!");
		}
	}

	// 得到抬头信息
	public CotSymbol getCotSymbolByOrderOutId(Integer orderOutId) {
		String sql = "from CotSymbol obj where obj.orderOutId=" + orderOutId;
		List<?> list = this.getOrderOutDao().find(sql);
		if (list.size() > 0) {
			return (CotSymbol) list.get(0);
		} else {
			return null;
		}

	}

	// 得到船务信息
	public CotShipment getCotShipmentByOrderOutId(Integer orderOutId) {
		String sql = "from CotShipment obj where obj.orderId=" + orderOutId;
		List<?> list = this.getOrderOutDao().find(sql);
		if (list.size() > 0) {
			return (CotShipment) list.get(0);
		} else {
			return null;
		}
	}

	// 得到报关信息
	public CotHsInfo getCotHsInfoByOrderOutId(Integer orderOutId) {
		String sql = "from CotHsInfo obj where obj.orderOutId=" + orderOutId;
		List<?> list = this.getOrderOutDao().find(sql);
		if (list.size() > 0) {
			return (CotHsInfo) list.get(0);
		} else {
			return null;
		}
	}

	// 得到合计信息
	public CotOrderouthsRpt getOrderouthsRptByOrderOutId(Integer orderOutId) {
		String sql = "from CotOrderouthsRpt obj where obj.orderOutId="
				+ orderOutId;
		List<?> list = this.getOrderOutDao().find(sql);
		if (list.size() > 0) {
			return (CotOrderouthsRpt) list.get(0);
		} else {
			return null;
		}
	}

	// 得到作废单的合计信息
	// public CotOrderouthsRptDel getOrderouthsRptByOrderOutIdDel(Integer
	// orderOutId) {
	// String sql = "from CotOrderouthsRptDel obj where obj.orderOutId="
	// + orderOutId;
	// List<?> list = this.getOrderOutDao().find(sql);
	// if (list.size() > 0) {
	// return (CotOrderouthsRptDel) list.get(0);
	// } else {
	// return null;
	// }
	// }

	// 删除出货单的产品明细
	@SuppressWarnings("deprecation")
	public Boolean deleteOrderDetails(HttpServletRequest request,
			List<?> details) {
		List<CotOrderDetail> list = new ArrayList<CotOrderDetail>();
		// 获得登录人
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		// 还原数量到订单中
		for (int i = 0; i < details.size(); i++) {
			Integer detailId = (Integer) details.get(i);
			CotOrderOutdetail cotOrderOutdetail = (CotOrderOutdetail) this
					.getOrderOutDao()
					.getById(CotOrderOutdetail.class, detailId);
			if (cotOrderOutdetail != null) {

				// 清除signMap中eleId对应的映射
				this.delMapByKeyAction(cotOrderOutdetail.getOrderDetailId(),
						request.getSession());

				CotOrderDetail cotOrderDetail = (CotOrderDetail) this
						.getOrderOutDao().getById(CotOrderDetail.class,
								cotOrderOutdetail.getOrderDetailId());
				if (cotOrderDetail != null) {
					cotOrderDetail.setUnBoxCount(cotOrderDetail.getUnBoxCount()
							+ cotOrderOutdetail.getBoxCount());
					cotOrderDetail.setUnBoxSend(0);
					list.add(cotOrderDetail);
				}
			}
		}

		try {
			this.getOrderOutDao().updateRecords(list);
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量修改订单明细成功");
			cotSyslog.setOpModule("order");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			sysLogService.addSysLogByObj(cotSyslog);
			this.getOrderOutDao().deleteRecordByIds(details,
					"CotOrderOutdetail");
			// 保存到系统日记表
			CotSyslog cotSyslog2 = new CotSyslog();
			cotSyslog2.setEmpId(empId);
			cotSyslog2.setOpMessage("批量删除出货明细成功");
			cotSyslog2.setOpModule("orderOut");
			cotSyslog2.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog2.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog2);
		} catch (DAOException e) {
			logger.error("批量删除出货明细失败");
			// 保存到系统日记表
			CotSyslog cotSyslog2 = new CotSyslog();
			cotSyslog2.setEmpId(empId);
			cotSyslog2.setOpMessage("批量删除出货明细失败,失败原因:" + e.getMessage());
			cotSyslog2.setOpModule("orderOut");
			cotSyslog2.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog2.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog2);
			return false;
		}
		return true;
	}

	// 删除出货单的差额数据
	@SuppressWarnings("deprecation")
	public Boolean deleteChaDetails(HttpServletRequest request, List<?> ids,
			Integer flag) {
		// 获得登录人
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		try {
			for (int i = 0; i < ids.size(); i++) {
				Integer detailId = (Integer) ids.get(i);
				CotOrderOuthsdetail cotOrderOutdetail = (CotOrderOuthsdetail) this
						.getOrderOutDao().getById(CotOrderOuthsdetail.class,
								detailId);
				if (cotOrderOutdetail != null && flag != 1) {
					this.delChaMapByKeyAction(cotOrderOutdetail
							.getOrderDetailId(), request.getSession());
				}
			}

			this.getOrderOutDao().deleteRecordByIds(ids, "CotOrderOuthsdetail");
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除出货差额明细成功");
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);

			return true;
		} catch (DAOException e) {
			logger.error("删除出货单的差额数据异常");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除出货差额明细失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
	}

	// 根据出货单编号删除出货单的差额数据
	public Boolean deleteChaDetailsByMainId(Integer mainId) {
		WebContext ctx = WebContextFactory.get();
		String hql = "select obj.id from CotOrderOuthsdetail obj where obj.orderId="
				+ mainId;
		List<?> ids = this.getOrderOutDao().find(hql);
		if (ids.size() > 0) {
			deleteChaDetails(ctx.getHttpServletRequest(), ids, 0);
			// this.getTotalChaDetail(request, details)
		}
		// 删除内存
		HashMap<Integer, CotOrderOuthsdetail> orderMap = new HashMap<Integer, CotOrderOuthsdetail>();
		SystemUtil.setObjBySession(null, orderMap, "cha");
		return true;
	}

	// 修改订单明细未出货数量
	public void updateOrderDetail(String detailIds) {
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
		String[] detail = detailIds.split(",");
		List<CotOrderDetail> list = new ArrayList<CotOrderDetail>();
		for (int i = 0; i < detail.length; i++) {
			String[] temp = detail[i].split("-");
			CotOrderDetail cotOrderDetail = (CotOrderDetail) this
					.getOrderOutDao().getById(CotOrderDetail.class,
							Integer.parseInt(temp[0]));
			if (cotOrderDetail != null) {
				float unBoxCount = Float.parseFloat(temp[1]);
				cotOrderDetail.setUnBoxCount(unBoxCount);
				if (unBoxCount == 0) {
					cotOrderDetail.setUnBoxSend(1);
				} else {
					cotOrderDetail.setUnBoxSend(0);
				}
				list.add(cotOrderDetail);
			}
		}
		try {
			this.getOrderOutDao().updateRecords(list);
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改订单明细成功");
				cotSyslog.setOpModule("order");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
		} catch (Exception e) {
			// 保存到系统日记表
			if (ctx != null || obj != null) {
				CotSyslog cotSyslog = new CotSyslog();
				cotSyslog.setEmpId(empId);
				cotSyslog.setOpMessage("批量修改订单明细失败,失败原因:" + e.getMessage());
				cotSyslog.setOpModule("order");
				cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
				cotSyslog.setOpType(2);
				sysLogService.addSysLogByObj(cotSyslog);
			}
		}

	}

	// 判断该订单明细是否已添加
	@SuppressWarnings("unchecked")
	public void findIsExistDetail(CotOrderOutdetail detail) {
		Integer orderDetailId = detail.getOrderDetailId();
		long boxCount = detail.getBoxCount();
		Object obj = SystemUtil.getObjBySession(null, "orderout");
		if (obj == null) {
			HashMap<Integer, CotOrderOutdetail> orderMap = new HashMap<Integer, CotOrderOutdetail>();
			orderMap.put(orderDetailId, detail);
			SystemUtil.setObjBySession(null, orderMap, "orderout");
		} else {
			HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
			CotOrderOutdetail old = orderMap.get(orderDetailId);
			if (old != null) {
				long allBox = boxCount + old.getBoxCount();
				// 外箱数
				long boxOCount = old.getBoxObCount();
				old.setBoxCount(allBox);// 数量相加
				long containerBox;
				if (allBox % boxOCount != 0) {
					containerBox = allBox / boxOCount + 1;
				} else {
					containerBox = allBox / boxOCount;
				}
				old.setContainerCount(containerBox);// 箱数相加
				old.setUnSendNum(detail.getUnSendNum());
				orderMap.put(orderDetailId, old);
			} else {
				orderMap.put(orderDetailId, detail);
			}
			SystemUtil.setObjBySession(null, orderMap, "orderout");
		}
	}

	// 判断该差额明细是否已添加
	@SuppressWarnings("unchecked")
	public void findIsExistCha(CotOrderOuthsdetail detail) {
		Integer orderDetailId = detail.getOrderDetailId();
		long boxCount = detail.getBoxCount();
		Object obj = SystemUtil.getObjBySession(null, "cha");
		if (obj == null) {
			HashMap<Integer, CotOrderOuthsdetail> orderMap = new HashMap<Integer, CotOrderOuthsdetail>();
			orderMap.put(orderDetailId, detail);
			SystemUtil.setObjBySession(null, orderMap, "cha");
		} else {
			HashMap<Integer, CotOrderOuthsdetail> orderMap = (HashMap<Integer, CotOrderOuthsdetail>) obj;
			CotOrderOuthsdetail old = orderMap.get(orderDetailId);
			if (old != null) {
				long allBox = boxCount + old.getBoxCount();
				// 外箱数
				long boxOCount = old.getBoxObCount();
				old.setBoxCount(allBox);// 数量相加
				long containerBox;
				if (allBox % boxOCount != 0) {
					containerBox = allBox / boxOCount + 1;
				} else {
					containerBox = allBox / boxOCount;
				}
				old.setContainerCount(containerBox);// 箱数相加
				orderMap.put(orderDetailId, old);
			} else {
				orderMap.put(orderDetailId, detail);
			}
			SystemUtil.setObjBySession(null, orderMap, "cha");
		}
	}

	// 根据编号查找麦标PicImg
	public byte[] getPicImgByOrderId(Integer custId) {
		List<?> list = this.getOrderOutDao().find(
				"from CotOrderoutMb obj where obj.fkId=" + custId);
		CotOrderoutMb cotOrderOut = new CotOrderoutMb();
		if (list.size() == 1) {
			cotOrderOut = (CotOrderoutMb) list.get(0);
		}
		if (cotOrderOut != null) {
			return cotOrderOut.getPicImg();
		}
		return null;
	}

	// 根据编号查找麦标PicImg
	// public byte[] getPicImgByOrderIdDel(Integer custId) {
	// List<?> list = this.getOrderOutDao().find(
	// "from CotOrderoutMbDel obj where obj.fkId=" + custId);
	// CotOrderoutMbDel cotOrderOut = new CotOrderoutMbDel();
	// if (list.size() == 1) {
	// cotOrderOut = (CotOrderoutMbDel) list.get(0);
	// }
	// if (cotOrderOut != null) {
	// return cotOrderOut.getPicImg();
	// }
	// return null;
	// }

	// 更新出货的麦标图片
	public void updateMBImg(String filePath, Integer mainId) {
		String hql = "from CotOrderoutMb obj where obj.fkId=" + mainId;
		CotOrderoutMb cotOrderoutMb = new CotOrderoutMb();
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			cotOrderoutMb = (CotOrderoutMb) list.get(0);
		}
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotOrderoutMb.setFkId(mainId);
			cotOrderoutMb.setPicImg(b);
			cotOrderoutMb.setPicSize(b.length);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			List<CotOrderoutMb> res = new ArrayList<CotOrderoutMb>();
			res.add(cotOrderoutMb);
			this.getOrderOutDao().saveOrUpdateRecords(res);
		} catch (Exception e) {
			logger.error("修改出货麦标图片错误!");
		}
	}

	// 根据编号查询公司
	public CotCompany getCompanyById(Integer companyId) {
		if (companyId == null) {
			return null;
		}
		CotCompany cotCompany = (CotCompany) this.getOrderOutDao().getById(
				CotCompany.class, companyId);
		if (cotCompany != null) {
			cotCompany.setCompanyLogo(null);
		}
		return cotCompany;
	}

	// 保存并获取邮件对象Id
	@SuppressWarnings("deprecation")
	public String saveMail(Map<?, ?> map) {
		String custEmail = map.get("custEmail").toString();
		Integer custId = Integer.parseInt(map.get("custId").toString());
		String custName = map.get("custName").toString();
		Integer orderId = Integer.parseInt(map.get("orderId").toString());
		String orderNo = map.get("orderNo").toString();
		String reportTemple = map.get("reportTemple").toString();
		String printType = map.get("printType").toString();
		Object factoryId = map.get("factoryId");

		WebContext ctx = WebContextFactory.get();
		String rptXMLpath = ctx.getHttpServletRequest().getRealPath("/")
				+ File.separator + reportTemple;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// 设置订单编号
		String sql = "obj.ORDER_ID=" + orderId;
		if (factoryId != null && !"".equals(factoryId.toString())) {
			sql += " and obj.FACTORY_ID=" + factoryId.toString();
		}
		paramMap.put("STR_SQL", sql);
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
					+ sdf.format(new java.util.Date()) + "_" + orderNo + ".xls";// 附件路径
			String exportPath = servicePath + File.separator + mailPath;// 实际路径
			this.getExportRptDao().exportRptToXLS(rptXMLpath, exportPath,
					paramMap);
		}
		if ("PDF".equals(printType)) {
			// 设置导出路径
			mailPath = empNo.toString() + File.separator
					+ sdf.format(new java.util.Date()) + "_" + orderNo + ".pdf";// 附件路径
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
		//		
		// cotMail.setSendCount(Integer.parseInt(defaultmcotMail.setMailDebug(Integer.parseInt(defaultmap.get("IsDebug")
		// .toString()));// 调试
		// cotMail.setMailAuth(Integer.parseInt(defaultmap.get("IsAuth")
		// .toString()));// 是否健全
		// cotMail.setMailPriv((Integer) defaultmap.get("Priv"));//
		// 优先级ap.get("SendCount")
		// .toString()));// 发送次数
		// 详细配置
		// cotMail.setMailPath(mailPath);// 附件路径
		cotMail.setBody(sdf.format(new java.util.Date()) + "_" + orderNo);// 邮件内容(日期加报价单)
		// cotMail.setMailCount(0);// 已发送次数
		// cotMail.setMailStatus(0);// 状态
		cotMail.setSubject(sdf.format(new java.util.Date()) + "_" + orderNo);// 主题
		// 保存邮件对象
		this.getOrderOutDao().create(cotMail);
		// 发送单封带附件的邮件
		// return this.getMailService().sendMimeMail(cotMail);
		return cotMail.getId();
	}

	// 根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id) {
		CotCustomer cotCustomer = (CotCustomer) this.getOrderOutDao().getById(
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

	// 根据员工编号获得员工
	public CotEmps getEmpsById(Integer empId) {
		CotEmps cotEmps = (CotEmps) this.getOrderOutDao().getById(
				CotEmps.class, empId);
		cotEmps.setCustomers(null);
		return cotEmps;
	}

	// 查询订单明细的未发货量
	public Float findUnBoxCount(Integer detailId) {
		String sql = "select obj.unBoxCount from CotOrderDetail obj where obj.id="
				+ detailId;
		List<?> list = this.getOrderOutDao().find(sql);
		return (Float) list.get(0);
	}

	// 根据订单号查询该单的客户
	public CotCustomer getCusByOrderId(Integer orderId) {
		if (orderId == null) {
			return null;
		}
		CotOrderOut cotOrder = (CotOrderOut) this.getOrderOutDao().getById(
				CotOrderOut.class, orderId);
		if (cotOrder != null) {
			CotCustomer cotCustomer = (CotCustomer) this.getOrderOutDao()
					.getById(CotCustomer.class, cotOrder.getCustId());
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
		}
		return null;

	}

	// 查询某张出货单下的明细含有几个厂家
	public Map getFactorysByMainId(Integer mainId) {
		String hql = "select distinct f from CotOrderOut obj,CotOrderOutdetail d,CotFactory f where obj.id="
				+ mainId + " and d.orderId=obj.id and d.factoryId=f.id";
		List<CotFactory> list = this.getOrderOutDao().find(hql);
		Map map = new HashMap();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotFactory factory = (CotFactory) it.next();
			map.put(factory.getId(), factory.getShortName());
		}
		return map;
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}

	// 根据客户编号查找客户编号，简称，和联系人
	public String[] getTeCustById(Integer id) {
		String sql = "select obj.id,obj.customerShortName,obj.priContact,obj.customerEmail from CotCustomer obj where obj.id="
				+ id;
		List<?> list = this.getOrderOutDao().find(sql);
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
		List list = this.getOrderOutDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
	}

	// 获得所有出货明细,并且编号不等于ids
	public List<?> getOrderOutDetailById(Integer mainId, String ids, String ods) {
		String sql = "from CotOrderOutdetail obj where obj.orderId=" + mainId
				+ " and id not in (" + ids.substring(0, ids.length() - 1) + ")";
		List list = this.getOrderOutDao().find(sql);
		if (!ods.equals("")) {
			Object obj = SystemUtil.getObjBySession(null, "orderout");
			String[] odsAry = ods.split(",");
			HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
			for (int i = 0; i < odsAry.length; i++) {
				CotOrderOutdetail orderOutdetail = (CotOrderOutdetail) orderMap
						.get(Integer.parseInt(odsAry[i]));
				list.add(orderOutdetail);
			}
		}
		return list;
	}

	// 获得更改的出货明细
	public List<?> getOrderOutDetailFromMap(String ods) {
		List list = new ArrayList();
		if (!ods.equals("")) {
			Object obj = SystemUtil.getObjBySession(null, "orderout");
			String[] odsAry = ods.split(",");
			HashMap<Integer, CotOrderOutdetail> orderMap = (HashMap<Integer, CotOrderOutdetail>) obj;
			for (int i = 0; i < odsAry.length; i++) {
				CotOrderOutdetail orderOutdetail = (CotOrderOutdetail) orderMap
						.get(Integer.parseInt(odsAry[i]));
				list.add(orderOutdetail);
			}
		}
		return list;
	}

	// 根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id) {
		String sql = "select obj.id,obj.targetPortEnName from CotTargetPort obj where obj.id="
				+ id;
		List list = this.getOrderOutDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
	}

	// 根据国别编号获得国别名称
	public String[] getNationNameById(Integer id) {
		String sql = "select obj.id,obj.nationName from CotNation obj where obj.id="
				+ id;
		List list = this.getOrderOutDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
	}

	// 合计所有订单品名信息
	public List findAllPin(Integer orderOutId) {
		String hql = "select obj.orderName,"
				+ "sum(obj.containerCount), "
				+ "sum(obj.boxCount), "
				+ "sum(obj.totalCbm), "
				+ "sum(obj.totalNetWeigth), "
				+ "sum(obj.totalGrossWeigth), "
				+ "sum(obj.totalMoney)/sum(obj.boxCount), "
				+ "sum(obj.totalMoney) "
				+ "from CotOrderOutdetail obj where obj.orderName is not null and obj.orderId="
				+ orderOutId + " group by obj.orderName order by obj.orderName";
		List list = this.getOrderOutDao().find(hql);
		List listVo = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotOrdeNameRptVO ordeNameRptVO = new CotOrdeNameRptVO();
			ordeNameRptVO.setOrderName((String) obj[0]);
			ordeNameRptVO.setTotalContainerCount((Long) obj[1]);
			ordeNameRptVO.setTotalCount((Long) obj[2]);
			ordeNameRptVO.setTotalCbm((Float) obj[3]);
			ordeNameRptVO.setTotalNet((Float) obj[4]);
			ordeNameRptVO.setTotalGross((Float) obj[5]);
			ordeNameRptVO.setAvgPrice((Float) obj[6]);
			ordeNameRptVO.setTotalMoney((Float) obj[7]);
			listVo.add(ordeNameRptVO);
		}

		return listVo;
	}

	// 合计所有报关品名信息
	public List findAllOutPin(Integer orderOutId) {
		String hql = "select obj.orderName,"
				+ "sum(obj.containerCount), "
				+ "sum(obj.boxCount), "
				+ "sum(obj.totalCbm), "
				+ "sum(obj.totalNetWeigth), "
				+ "sum(obj.totalGrossWeigth), "
				+ "sum(obj.totalMoney)/sum(obj.boxCount), "
				+ "sum(obj.totalMoney) "
				+ "from CotOrderOuthsdetail obj where obj.orderName is not null and obj.orderId="
				+ orderOutId + " group by obj.orderName order by obj.orderName";
		List list = this.getOrderOutDao().find(hql);
		List listVo = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotOrdeNameRptVO ordeNameRptVO = new CotOrdeNameRptVO();
			ordeNameRptVO.setOrderName((String) obj[0]);
			ordeNameRptVO.setTotalContainerCount((Long) obj[1]);
			ordeNameRptVO.setTotalCount((Long) obj[2]);
			ordeNameRptVO.setTotalCbm((Float) obj[3]);
			ordeNameRptVO.setTotalNet((Float) obj[4]);
			ordeNameRptVO.setTotalGross((Float) obj[5]);
			ordeNameRptVO.setAvgPrice((Float) obj[6]);
			ordeNameRptVO.setTotalMoney((Float) obj[7]);
			listVo.add(ordeNameRptVO);
		}

		return listVo;
	}

	// 查找相同报关品名的中文规格
	public String findAllSizeByName(String orderName, Integer orderId) {
		String hql = "select obj.eleSizeDesc from CotOrderOuthsdetail obj where obj.orderName=? and obj.orderId=?";
		Object[] obj = new Object[2];
		obj[0] = orderName;
		obj[1] = orderId;
		List list = this.getOrderOutDao().queryForLists(hql, obj);
		String str = "";
		if (list == null) {
			return "";
		}
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			String eleSizeDesc = (String) it.next();
			if (eleSizeDesc != null) {
				str += eleSizeDesc + ",";
			}
		}
		if (!str.equals("")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	// 更改主单的总箱数,总金额...
	public String[] updateOrderOutTotal(Integer orderId) throws Exception {
		// 获得登录人
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		CotOrderOut cotOrderOut = this.getOrderOutById(orderId);
		String[] ary = new String[12];
		try {
			Float totalMoney = (Float) ctx.getSession().getAttribute(
					"totalMoney");
			Integer totalCount = (Integer) ctx.getSession().getAttribute(
					"totalCount");
			Integer totalContainer = (Integer) ctx.getSession().getAttribute(
					"totalContainer");
			Float totalCBM = (Float) ctx.getSession().getAttribute("totalCBM");
			Float totalNet = (Float) ctx.getSession().getAttribute("totalNet");
			Float totalGross = (Float) ctx.getSession().getAttribute(
					"totalGross");

			Float totalHsMoney = (Float) ctx.getSession().getAttribute(
					"totalHsMoney");
			Integer totalHsCount = (Integer) ctx.getSession().getAttribute(
					"totalHsCount");
			Integer totalHsContainer = (Integer) ctx.getSession().getAttribute(
					"totalHsContainer");
			Float totalHsCBM = (Float) ctx.getSession().getAttribute(
					"totalHsCBM");
			Float totalHsNet = (Float) ctx.getSession().getAttribute(
					"totalHsNet");
			Float totalHsGross = (Float) ctx.getSession().getAttribute(
					"totalHsGross");

			ctx.getSession().removeAttribute("totalMoney");
			ctx.getSession().removeAttribute("totalCount");
			ctx.getSession().removeAttribute("totalContainer");
			ctx.getSession().removeAttribute("totalCBM");
			ctx.getSession().removeAttribute("totalNet");
			ctx.getSession().removeAttribute("totalGross");

			ctx.getSession().removeAttribute("totalHsMoney");
			ctx.getSession().removeAttribute("totalHsCount");
			ctx.getSession().removeAttribute("totalHsContainer");
			ctx.getSession().removeAttribute("totalHsCBM");
			ctx.getSession().removeAttribute("totalHsNet");
			ctx.getSession().removeAttribute("totalHsGross");
			if (cotOrderOut.getTotalMoneyChk() != null
					&& cotOrderOut.getTotalMoneyChk() == 1) {
				ary[0] = null;
			} else {
				if (totalMoney != null) {
					ary[0] = totalMoney.toString();
					cotOrderOut.setTotalMoney(totalMoney);
				} else {
					String hql = "select sum(obj.totalMoney) from CotOrderOutdetail obj where obj.orderId="
							+ orderId;
					List<?> details = this.getOrderOutDao().find(hql);
					Float temp = 0f;
					if (details != null && details.size() > 0) {
						temp = (Float) details.get(0);
					}
					cotOrderOut.setTotalMoney(temp);
					ary[0] = temp.toString();
				}
			}
			if (totalCount != null) {
				ary[1] = totalCount.toString();
				cotOrderOut.setTotalCount(totalCount);
			}
			if (totalContainer != null) {
				ary[2] = totalContainer.toString();
				cotOrderOut.setTotalContainer(totalContainer);
			}

			if (cotOrderOut.getTotalCbmChk() != null
					&& cotOrderOut.getTotalCbmChk() == 1) {
				ary[3] = null;
			} else {
				if (totalCBM != null) {
					ary[3] = totalCBM.toString();
					cotOrderOut.setTotalCbm(totalCBM);
				}
			}

			if (totalNet != null) {
				if (cotOrderOut.getTotalNetChk() != null
						&& cotOrderOut.getTotalNetChk() == 1) {
					ary[4] = null;
				} else {
					ary[4] = totalNet.toString();
					cotOrderOut.setTotalNet(totalNet);
				}
			}
			if (totalGross != null) {
				if (cotOrderOut.getTotalGrossChk() != null
						&& cotOrderOut.getTotalGrossChk() == 1) {
					ary[5] = null;
				} else {
					ary[5] = totalGross.toString();
					cotOrderOut.setTotalGross(totalGross);
				}
			}
			if (totalHsMoney != null) {
				if (cotOrderOut.getTotalHsMoneyChk() != null
						&& cotOrderOut.getTotalHsMoneyChk() == 1) {
					ary[6] = null;
				} else {
					ary[6] = totalHsMoney.toString();
					cotOrderOut.setTotalHsMoney(totalHsMoney);
				}
			}
			if (totalHsCount != null) {
				ary[7] = totalHsCount.toString();
				cotOrderOut.setTotalHsCount(totalHsCount);
			}
			if (totalHsContainer != null) {
				ary[8] = totalHsContainer.toString();
				cotOrderOut.setTotalHsContainer(totalHsContainer);
			}
			if (totalHsCBM != null) {
				if (cotOrderOut.getTotalHsCbmChk() != null
						&& cotOrderOut.getTotalHsCbmChk() == 1) {
					ary[9] = null;
				} else {
					ary[9] = totalHsCBM.toString();
					cotOrderOut.setTotalHsCbm(totalHsCBM);
				}
			}
			if (totalHsNet != null) {
				if (cotOrderOut.getTotalHsNetChk() != null
						&& cotOrderOut.getTotalHsNetChk() == 1) {
					ary[10] = null;
				} else {
					ary[10] = totalHsNet.toString();
					cotOrderOut.setTotalHsNet(totalHsNet);
				}
			}
			if (totalHsGross != null) {
				if (cotOrderOut.getTotalHsGrossChk() != null
						&& cotOrderOut.getTotalHsGrossChk() == 1) {
					ary[11] = null;
				} else {
					ary[11] = totalHsGross.toString();
					cotOrderOut.setTotalHsGross(totalHsGross);
				}
			}
			List list = new ArrayList();
			list.add(cotOrderOut);
			this.getOrderOutDao().updateRecords(list);
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("修改主出货单的总金额和总数量和总体积成功");
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setItemNo(cotOrderOut.getOrderNo());
			cotSyslog.setOpType(2);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改主出货单的总金额和总数量和总体积出错!");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("修改主出货单失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("orderOut");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setItemNo(cotOrderOut.getOrderNo());
			cotSyslog.setOpType(2);
			sysLogService.addSysLogByObj(cotSyslog);
			ary = null;
			throw new Exception("修改主出货单的总金额和总数量和总体积出错");
		}
		return ary;
	}

	// 更新订单总金额
	public Float updateOrderTotalMoney(WebContext ctx, CotOrderOut order) {
		CotOrderOut cotOrder = (CotOrderOut) SystemUtil.deepClone(order);
		cotOrder.setCotOrderOutdetails(null);
		cotOrder.setCotOrderOuthsdetails(null);
		cotOrder.setCotSplitInfos(null);
		cotOrder.setCotHsInfos(null);
		cotOrder.setCotShipments(null);
		cotOrder.setCotSymbols(null);
		// cotOrder.setOrderMBImg(null);
		cotOrder.setCotOrderouthsRpt(null);

		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute("sysdic");
		List<?> list = (List<?>) dicMap.get("currency");

		// 查询该订单的所有其他费用
		String hql = "from CotFinaceOther obj where (obj.source='orderout' or obj.source='orderRecv' or obj.source='orderOther' or obj.source='yi') and obj.fkId="
				+ cotOrder.getId();
		List<?> details = this.getOrderOutDao().find(hql);
		// 将其他费用的金额累加到订单总金额里
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
		// 保存订单总金额
		if (cotOrder.getTotalMoney() != null) {
			allMoney = cotOrder.getTotalMoney() + allMoney / cuRateOrder;
			cotOrder.setTotalMoney(allMoney);
		} else {
			cotOrder.setTotalMoney(allMoney / cuRateOrder);
		}
		List<CotOrderOut> listOrder = new ArrayList<CotOrderOut>();
		listOrder.add(cotOrder);
		this.getOrderOutDao().updateRecords(listOrder);
		return allMoney;
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

	// 根据生产合同编号查找采购信息
	public CotOrderFac getOrderFacById(Integer id) {
		return (CotOrderFac) this.getOrderOutDao().getById(CotOrderFac.class,
				id);
	}

	// 根据配件编号查找采购信息
	public CotFittingOrder getFitOrderById(Integer id) {
		return (CotFittingOrder) this.getOrderOutDao().getById(
				CotFittingOrder.class, id);
	}

	// 根据编号查找采购信息
	public CotPackingOrder getPackOrderById(Integer id) {
		return (CotPackingOrder) this.getOrderOutDao().getById(
				CotPackingOrder.class, id);
	}

	// 根据配件编号查找采购明细信息
	public CotFittingsOrderdetail getFitOrderDetailById(Integer id) {
		return (CotFittingsOrderdetail) this.getOrderOutDao().getById(
				CotFittingsOrderdetail.class, id);
	}

	// 根据包材编号查找采购明细信息
	public CotPackingOrderdetail getPackOrderDetailById(Integer id) {
		return (CotPackingOrderdetail) this.getOrderOutDao().getById(
				CotPackingOrderdetail.class, id);
	}

	/*---------------------------- 应付帐款操作部分 --------------------------------------*/

	// Action储存采购明细Map
	@SuppressWarnings("unchecked")
	public void setFacMapAction(HttpSession session, Integer detailId,
			VOrderOrderfacId cotOrderFacdetail) {
		Object obj = SystemUtil.getObjBySession(session, "orderfac");
		if (obj == null) {
			HashMap<Integer, VOrderOrderfacId> orderMap = new HashMap<Integer, VOrderOrderfacId>();
			orderMap.put(detailId, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, orderMap, "orderfac");
		} else {
			HashMap<Integer, VOrderOrderfacId> orderMap = (HashMap<Integer, VOrderOrderfacId>) obj;
			orderMap.put(detailId, cotOrderFacdetail);
			SystemUtil.setObjBySession(session, orderMap, "orderfac");
		}
	}

	// Action储存配件采购明细Map
	@SuppressWarnings("unchecked")
	public void setFitFacMapAction(HttpSession session, Integer detailId,
			VOrderFitorderId fittingsOrderdetail) {
		Object obj = SystemUtil.getObjBySession(session, "fitorder");
		if (obj == null) {
			HashMap<Integer, VOrderFitorderId> orderMap = new HashMap<Integer, VOrderFitorderId>();
			orderMap.put(detailId, fittingsOrderdetail);
			SystemUtil.setObjBySession(session, orderMap, "fitorder");
		} else {
			HashMap<Integer, VOrderFitorderId> orderMap = (HashMap<Integer, VOrderFitorderId>) obj;
			orderMap.put(detailId, fittingsOrderdetail);
			SystemUtil.setObjBySession(session, orderMap, "fitorder");
		}
	}

	// Action储存配件采购明细Map
	@SuppressWarnings("unchecked")
	public void setPackFacMapAction(HttpSession session, Integer detailId,
			VOrderPackorderId packingsOrderdetail) {
		Object obj = SystemUtil.getObjBySession(session, "packorder");
		if (obj == null) {
			HashMap<Integer, VOrderPackorderId> orderMap = new HashMap<Integer, VOrderPackorderId>();
			orderMap.put(detailId, packingsOrderdetail);
			SystemUtil.setObjBySession(session, orderMap, "packorder");
		} else {
			HashMap<Integer, VOrderPackorderId> orderMap = (HashMap<Integer, VOrderPackorderId>) obj;
			orderMap.put(detailId, packingsOrderdetail);
			SystemUtil.setObjBySession(session, orderMap, "packorder");
		}
	}

	// 通过采购明细id找厂家id
	public Integer findFactoryIdById(Integer orderfacDetailId) {
		// 通过采购明细id找主单id
		Integer orderfacId = this.findOrderFacId(orderfacDetailId);
		if (orderfacId != -1) {
			String hql = "select obj.factoryId from CotOrderFac obj where obj.id="
					+ orderfacId;
			List<?> list = this.getOrderOutDao().find(hql);
			if (list.size() == 1) {
				Integer factoryId = (Integer) list.get(0);
				return factoryId;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	// 通过配件采购明细id找厂家id
	public Integer findFitFactoryIdById(Integer fitDetailId) {
		String hql = "select obj.facId from CotFittingsOrderdetail obj where obj.id="
				+ fitDetailId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			Integer factoryId = (Integer) list.get(0);
			return factoryId;
		} else {
			return 0;
		}
	}

	// 通过包材采购主单id找厂家id
	public Integer findPackFactoryIdById(Integer packOrderId) {
		String hql = "select obj.factoryId from CotPackingOrder obj where obj.id="
				+ packOrderId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			Integer factoryId = (Integer) list.get(0);
			return factoryId;
		} else {
			return 0;
		}
	}

	// 通过采购明细id找主单id
	public Integer findOrderFacId(Integer orderfacDetailId) {
		String hql = "select obj.orderId from CotOrderFacdetail obj where obj.id="
				+ orderfacDetailId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			Integer orderfacId = (Integer) list.get(0);
			return orderfacId;
		} else {
			return -1;
		}
	}

	// 通过出货主单编号查找明细,返回所有明细List
	public List<CotOrderOutdetail> checkIsHasOrder(Integer orderoutId) {
		String hql = "from CotOrderOutdetail obj where obj.orderId="
				+ orderoutId;
		List<?> list = this.getOrderOutDao().find(hql);

		List<CotOrderOutdetail> detailList = new ArrayList<CotOrderOutdetail>();
		for (int i = 0; i < list.size(); i++) {
			CotOrderOutdetail detail = (CotOrderOutdetail) list.get(i);

			detailList.add(detail);
		}
		return detailList;
	}

	// 通过出货主单编号查找明细,在将明细中的生产合同主单编号组成字符串
	public List<CotOrderFac> checkIsHasFac(Integer orderoutId) {

		List<CotOrderFac> res = new ArrayList<CotOrderFac>();

		// 通过出货主单编号查找明细
		List<CotOrderOutdetail> list = this.checkIsHasOrder(orderoutId);

		for (int i = 0; i < list.size(); i++) {
			CotOrderOutdetail orderOutdetail = list.get(i);

			String hql = " from CotOrderDetail obj where obj.orderId="
					+ orderOutdetail.getOrderNoid() + " and obj.eleId='"
					+ orderOutdetail.getEleId() + "'";
			List<?> orderlist = this.getOrderOutDao().find(hql);
			for (int j = 0; j < orderlist.size(); j++) {
				CotOrderDetail orderDetail = (CotOrderDetail) orderlist.get(j);
				if (orderDetail.getOrderFacnoid() != null) {
					CotOrderFac cotOrderFac = this.getOrderFacById(orderDetail
							.getOrderFacnoid());
					CotOrderFac newOrderFac = new CotOrderFac();

					newOrderFac.setId(cotOrderFac.getId());
					newOrderFac.setOrderNo(cotOrderFac.getOrderNo());
					newOrderFac.setFactoryId(cotOrderFac.getFactoryId());

					res.add(newOrderFac);
				}
			}
		}
		return res;
	}

	// 通过出货主单编号查找明细,在将明细中的产品采购主单编号组成字符串
	public String checkIsHasFacOrders(Integer orderoutId) {
		// 通过出货主单编号查找产品采购主单编号集合
		String hql = "select distinct obj.orderId from CotOrderFacdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderoutId;
		List<?> orderAry = this.getOrderOutDao().find(hql);
		// 产品采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款时过滤掉
		String str = "";
		Iterator<?> it = orderAry.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			boolean flag = false;
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='orderfac'"
					+ " and (obj.outFlag is null or obj.outFlag = 0) and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				flag = true;
			}
			boolean flag2 = false;
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='orderfac'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				flag2 = true;
			}

			boolean flag3 = false;
			String yiHql = "select obj.id from CotFinaceOther obj,CotOrderFac f where obj.source is null"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				flag3 = true;
			}

			if (flag || flag2 || flag3) {
				str += orderId + ",";
			}
		}
		return str;
	}

	// 通过出货主单编号查找明细,在将明细中的配件采购主单编号组成字符串
	public String checkIsHasFitOrders(Integer orderoutId) {
		// 通过出货主单编号查找配件采购主单编号集合
		String hql = "select distinct obj.orderId from CotFittingsOrderdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderoutId;
		List<?> orderAry = this.getOrderOutDao().find(hql);
		// 配件采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款时过滤掉
		String str = "";
		Iterator<?> it = orderAry.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			boolean flag = false;
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='fitorder'"
					+ " and obj.outFlag != 1 and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				flag = true;
			}
			boolean flag2 = false;
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='fitorder'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				flag2 = true;
			}
			boolean flag3 = false;
			String yiHql = "select obj.id from CotFinaceOther obj,CotFittingOrder f where obj.source is null"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				flag3 = true;
			}

			if (flag || flag2 || flag3) {
				str += orderId + ",";
			}
		}
		return str;
	}

	// 通过出货主单编号查找明细,在将明细中的包材采购主单编号组成字符串
	public String checkIsHasPackOrders(Integer orderoutId) {
		// 通过出货主单编号查找包材采购主单编号集合
		String hql = "select distinct obj.packingOrderId from CotPackingOrderdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderoutId;
		List<?> orderAry = this.getOrderOutDao().find(hql);
		// 包材采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款,并且该厂家不含溢付款时过滤掉
		String str = "";
		Iterator<?> it = orderAry.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			CotPackingOrder packingOrder = (CotPackingOrder) this
					.getOrderOutDao().getById(CotPackingOrder.class, orderId);
			boolean flag = false;
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='packorder'"
					+ " and obj.outFlag != 1 and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				flag = true;
			}
			boolean flag2 = false;
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='packorder'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				flag2 = true;
			}
			boolean flag3 = false;
			String yiHql = "select obj.id from CotFinaceOther obj,CotPackingOrder f where obj.source is null"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				flag3 = true;
			}

			if (flag || flag2 || flag3) {
				str += orderId + ",";
			}
		}
		return str;
	}

	// 通过订单明细id查询采购合同id
	public String getOrderFacId(Integer orderDetailId) {
		String orderfacIds = "";

		String hql = " from CotOrderFacdetail obj where obj.orderDetailId="
				+ orderDetailId;
		List<?> orderfaclist = this.getOrderOutDao().find(hql);
		for (int j = 0; j < orderfaclist.size(); j++) {
			CotOrderFacdetail orderFacDetail = (CotOrderFacdetail) orderfaclist
					.get(j);

			if (orderFacDetail.getOrderId() != null) {
				orderfacIds += orderFacDetail.getOrderId() + ",";
			}
		}
		return orderfacIds;
	}

	// 通过订单明细id查询配件采购合同id
	public String getFitOrderId(Integer orderDetailId) {
		String fitorderIds = "";

		String hql = " from CotFittingsOrderdetail obj where obj.orderDetailId="
				+ orderDetailId;
		List<?> orderfaclist = this.getOrderOutDao().find(hql);
		for (int j = 0; j < orderfaclist.size(); j++) {
			CotFittingsOrderdetail fitOrderDetail = (CotFittingsOrderdetail) orderfaclist
					.get(j);

			if (fitOrderDetail.getOrderId() != null) {
				fitorderIds += fitOrderDetail.getOrderId() + ",";
			}
		}
		return fitorderIds;
	}

	// 通过出货主单编号查找生产合同主单编号并组成字符串
	public String checkIsHasFacs(Integer orderoutId) {
		String facIds = "";
		String hql = "select f.orderId from CotOrderOutdetail obj,CotOrderDetail d,CotOrderFacdetail f where obj.orderDetailId=d.id and f.orderDetailId=d.id and obj.orderId="
				+ orderoutId;
		List<?> orderlist = this.getOrderOutDao().find(hql);
		for (int i = 0; i < orderlist.size(); i++) {
			facIds += (Integer) orderlist.get(i) + ",";
		}
		return facIds;
	}

	// 通过出货主单编号查找配件采购主单编号并组成字符串
	public String checkIsHasFits(Integer orderoutId) {
		String facIds = "";
		String hql = "select f.orderId from CotOrderOutdetail obj,CotOrderDetail d,CotFittingsOrderdetail f where obj.orderDetailId=d.id and f.orderDetailId=d.id and obj.orderId="
				+ orderoutId;
		List<?> orderlist = this.getOrderOutDao().find(hql);
		for (int i = 0; i < orderlist.size(); i++) {
			facIds += (Integer) orderlist.get(i) + ",";
		}
		return facIds;
	}

	// 通过出货主单编号查找包材采购主单编号并组成字符串
	public String checkIsHasPacks(Integer orderoutId) {
		String facIds = "";
		String hql = "select f.packingOrderId from CotOrderOutdetail obj,CotOrderDetail d,CotPackingOrderdetail f where obj.orderDetailId=d.id and f.orderDetailId=d.id and obj.orderId="
				+ orderoutId;
		List<?> orderlist = this.getOrderOutDao().find(hql);
		for (int i = 0; i < orderlist.size(); i++) {
			facIds += (Integer) orderlist.get(i) + ",";
		}
		return facIds;
	}

	// 根据id获取生产合同其他费用信息
	public CotFinaceOther getCotFinaceOtherById(Integer id) {
		return (CotFinaceOther) this.getOrderOutDao().getById(
				CotFinaceOther.class, id);
	}

	// 保存应付款其他费用
	public Boolean addOrderOutOther(HttpServletRequest request, List<?> details) {
		try {
			this.getOrderOutDao().saveRecords(details);
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("保存其他费用出错!");
			return false;
		}
	}

	// 更新其他费用
	public Boolean updateOrderFacOther(List<?> details) {
		try {
			this.getOrderOutDao().updateRecords(details);
		} catch (Exception e) {
			logger.error("更新其他费用异常", e);
		}
		return true;
	}

	// 删除其他费用
	public Boolean deleteOrderFacOther(List<?> details) {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < details.size(); i++) {
			CotFinaceOther finaceOther = (CotFinaceOther) details.get(i);
			ids.add(finaceOther.getId());
		}
		try {
			this.getOrderOutDao().deleteRecordByIds(ids, "CotFinaceOther");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 根据id获取生产合同未冲完的应付帐款记录
	public CotFinaceAccountdeal getOrderFacDealById(Integer id) {
		return (CotFinaceAccountdeal) this.getOrderOutDao().getById(
				CotFinaceAccountdeal.class, id);
	}

	// 生成应付帐款单号
	// public String createDealNo(Integer otherId) {
	//
	// CotFinaceOther other = this.getCotFinaceOtherById(otherId);
	//
	// Map idMap = new HashMap<String, Integer>();
	// idMap.put("CH", other.getFactoryId());
	// GenAllSeq seq = new GenAllSeq();
	// String finaceNo = seq.getAllSeqByType("fincaeaccountdealNo", idMap);
	// return finaceNo;
	// }

	// 保存应付帐款单号
	public void savaSeq() {
		// GenAllSeq seq = new GenAllSeq();
		// seq.saveSeq("fincaeaccountdealNo");
		CotSeqService seq = new CotSeqServiceImpl();
		seq.saveSeq("fincaeaccountdeal");
	}

	// 保存应付帐款
	public void saveAccountdeal(Integer otherId,
			CotFinaceAccountdeal dealDetail, String amountDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		CotFinaceOther other = this.getCotFinaceOtherById(otherId);

		try {
			if (!"".equals(amountDate) && amountDate != null) {
				dealDetail.setAmountDate(new java.sql.Date(sdf
						.parse(amountDate).getTime()));
			}

			WebContext ctx = WebContextFactory.get();
			CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

			List<CotFinaceAccountdeal> records = new ArrayList<CotFinaceAccountdeal>();

			dealDetail.setCurrencyId(other.getCurrencyId());
			dealDetail.setFactoryId(other.getFactoryId());
			dealDetail.setAddDate(new Date(System.currentTimeMillis()));
			dealDetail.setRealAmount(0.0);
			dealDetail.setRemainAmount(dealDetail.getAmount());
			dealDetail.setStatus(0);
			dealDetail.setSource("orderout");
			dealDetail.setEmpId(cotEmps.getId());

			records.add(dealDetail);
			this.getOrderOutDao().saveRecords(records);
			// 保存单号
			this.savaSeq();
			// 修改其他费用状态 0：未生成 1: 已生成
			// this.modifyFinOtherStatus(dealDetail.getFkId(),dealDetail.getFinaceName(),"add");

		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	// 删除应付帐款---恢复其他费用的标识status为0
	public Boolean deleteDealList(List<?> details, Integer mainId) {
		try {
			String str = "";
			for (int i = 0; i < details.size(); i++) {
				str += details.get(i) + ",";
			}
			// 查询出可删除的应付款所包含的厂家id
			String hql = "select obj.factoryId from CotFinaceAccountdeal obj where obj.id in ("
					+ str.substring(0, str.length() - 1) + ")";
			List facList = this.getOrderOutDao().find(hql);
			String facStr = "";
			for (int i = 0; i < facList.size(); i++) {
				facStr += facList.get(i) + ",";
			}

			this.getOrderOutDao().deleteRecordByIds(details,
					"CotFinaceAccountdeal");

			String tempHql = "from CotFinaceOther obj where obj.source in ('orderfacToOut','FacMoney','FitMoney','PackMoney','FacOther','FitOther','PackOther','FacDeal','FitDeal','PackDeal','yiDeal') and obj.status=1 and obj.fkId="
					+ mainId
					+ " and obj.factoryId in ("
					+ facStr.substring(0, facStr.length() - 1) + ")";
			List<?> listOther = this.getOrderOutDao().find(tempHql);
			List<CotFinaceOther> listNew = new ArrayList<CotFinaceOther>();
			for (int i = 0; i < listOther.size(); i++) {
				CotFinaceOther cotFinaceOther = (CotFinaceOther) listOther
						.get(i);
				cotFinaceOther.setStatus(0);
				listNew.add(cotFinaceOther);
			}
			this.getOrderOutDao().updateRecords(listNew);
			return true;
		} catch (DAOException e) {
			return false;
		}
	}

	// 更新已出货数量
	public void updateFacDetail(Integer orderfacDetailId, Long outCurrent) {
		CotOrderFacdetail orderFacdetail = (CotOrderFacdetail) this
				.getOrderOutDao().getById(CotOrderFacdetail.class,
						orderfacDetailId);
		List<CotOrderFacdetail> list = new ArrayList<CotOrderFacdetail>();
		if (orderFacdetail != null) {
			// 剩余数量
			Long oldOutCurrent = 0L;
			if (orderFacdetail.getOutCurrent() != null) {
				oldOutCurrent = orderFacdetail.getOutCurrent();
			}
			Long outRemain = 0L;
			if (orderFacdetail.getOutRemain() != null) {
				outRemain = orderFacdetail.getOutRemain();
			}
			outRemain = outRemain + oldOutCurrent - outCurrent;
			orderFacdetail.setOutRemain(outRemain);
			orderFacdetail.setOutCurrent(outCurrent);
			orderFacdetail.setAllocateFlag(1);
		}
		list.add(orderFacdetail);
		this.getOrderOutDao().updateRecords(list);
	}

	// 更新配件已出货数量
	public void updateFitDetail(Integer fitorderDetailId, Double outCurrent) {
		CotFittingsOrderdetail fittingsOrderdetail = this
				.getFitOrderDetailById(fitorderDetailId);

		List<CotFittingsOrderdetail> list = new ArrayList<CotFittingsOrderdetail>();
		if (fittingsOrderdetail != null) {
			// 剩余数量
			Double oldOutCurrent = 0.0;
			if (fittingsOrderdetail.getOutCurrent() != null) {
				oldOutCurrent = fittingsOrderdetail.getOutCurrent();
			}
			Double outRemain = 0.0;
			if (fittingsOrderdetail.getOutRemain() != null) {
				outRemain = fittingsOrderdetail.getOutRemain();
			}
			outRemain = outRemain + oldOutCurrent - outCurrent;
			fittingsOrderdetail.setOutRemain(outRemain);
			fittingsOrderdetail.setOutCurrent(outCurrent);
			fittingsOrderdetail.setAllocateFlag(1);
		}
		list.add(fittingsOrderdetail);
		this.getOrderOutDao().updateRecords(list);
	}

	// 更新包材已出货数量
	public void updatePackDetail(Integer packorderDetailId, Long outCurrent) {
		CotPackingOrderdetail packingsOrderdetail = this
				.getPackOrderDetailById(packorderDetailId);

		List<CotPackingOrderdetail> list = new ArrayList<CotPackingOrderdetail>();
		if (packingsOrderdetail != null) {
			// 剩余数量
			Long oldOutCurrent = 0L;
			if (packingsOrderdetail.getOutCurrent() != null) {
				oldOutCurrent = packingsOrderdetail.getOutCurrent();
			}
			Long outRemain = 0L;
			if (packingsOrderdetail.getOutRemain() != null) {
				outRemain = packingsOrderdetail.getOutRemain();
			}
			outRemain = outRemain + oldOutCurrent - outCurrent;
			packingsOrderdetail.setOutRemain(outRemain);
			packingsOrderdetail.setOutCurrent(outCurrent);
			packingsOrderdetail.setAllocateFlag(1);
			list.add(packingsOrderdetail);
		}
		this.getOrderOutDao().updateRecords(list);
	}

	// 更新已出货数量-删除时
	public void updateFacDetailForDel(Integer orderfacDetailId) {
		CotOrderFacdetail orderFacdetail = (CotOrderFacdetail) this
				.getOrderOutDao().getById(CotOrderFacdetail.class,
						orderfacDetailId);
		List<CotOrderFacdetail> list = new ArrayList<CotOrderFacdetail>();
		if (orderFacdetail != null) {
			// 剩余数量
			Long outRemain = orderFacdetail.getBoxCount();

			orderFacdetail.setOutRemain(outRemain);
			orderFacdetail.setOutCurrent(0L);
			orderFacdetail.setAllocateFlag(0);

		}
		list.add(orderFacdetail);
		this.getOrderOutDao().updateRecords(list);
	}

	// 更新配件已出货数量-删除时
	public void updateFitDetailForDel(Integer detailId) {
		CotFittingsOrderdetail fittingsOrderdetail = (CotFittingsOrderdetail) this
				.getOrderOutDao().getById(CotFittingsOrderdetail.class,
						detailId);
		List<CotFittingsOrderdetail> list = new ArrayList<CotFittingsOrderdetail>();
		if (fittingsOrderdetail != null) {
			// 剩余数量
			Double outRemain = fittingsOrderdetail.getOrderCount();

			fittingsOrderdetail.setOutRemain(outRemain);
			fittingsOrderdetail.setOutCurrent(0.0);
			fittingsOrderdetail.setAllocateFlag(0);

		}
		list.add(fittingsOrderdetail);
		this.getOrderOutDao().updateRecords(list);
	}

	// 更新包材已出货数量-删除时
	public void updatePackDetailForDel(Integer detailId) {
		CotPackingOrderdetail packingOrderdetail = (CotPackingOrderdetail) this
				.getOrderOutDao()
				.getById(CotPackingOrderdetail.class, detailId);
		List<CotPackingOrderdetail> list = new ArrayList<CotPackingOrderdetail>();
		if (packingOrderdetail != null) {
			// 剩余数量
			Integer outRemain = packingOrderdetail.getPackCount();

			packingOrderdetail.setOutRemain(outRemain.longValue());
			packingOrderdetail.setOutCurrent(0L);
			packingOrderdetail.setAllocateFlag(0);

		}
		list.add(packingOrderdetail);
		this.getOrderOutDao().updateRecords(list);
	}

	// 插入一条出货其他费用
	public void saveOrderFacOther(Integer orderfacDetailId, Integer orderoutId) {

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();

		CotOrderFacdetail orderFacdetail = (CotOrderFacdetail) this
				.getOrderOutDao().getById(CotOrderFacdetail.class,
						orderfacDetailId);
		// 获取对应的主单信息
		CotOrderFac orderFac = (CotOrderFac) this.getOrderOutDao().getById(
				CotOrderFac.class, orderFacdetail.getOrderId());

		List<?> otherList = this.getOrderOutDao().find(
				" from CotFinaceOther obj where obj.outFlag="
						+ orderFac.getId() + " and obj.source='FacMoney'");

		CotFinaceOther other = new CotFinaceOther();

		if (otherList.size() == 1) {
			other = (CotFinaceOther) otherList.get(0);
			// other.setId(other.getId());
			Float amount = orderFacdetail.getOutHasOut()
					* orderFacdetail.getPriceFac();
			Double allAmout = amount + other.getAmount();
			other.setAmount(allAmout);
		} else {
			Float amount = orderFacdetail.getOutHasOut()
					* orderFacdetail.getPriceFac();
			other.setAmount(amount.doubleValue());
			other.setCurrencyId(orderFacdetail.getPriceFacUint());
			other.setFactoryId(orderFac.getFactoryId());
			other.setStatus(0);
			other.setFinaceName("生产合同货款");
			other.setFkId(orderoutId);
			other.setOrderNo(orderFac.getOrderNo());
			other.setFlag("A");
			other.setOutFlag(orderFac.getId());
			other.setSource("FacMoney");
			other.setType(1);
			other.setRemainAmount(amount.doubleValue());
			other.setBusinessPerson(cotEmps.getId());
		}
		list.add(other);

		this.getOrderOutDao().saveOrUpdateRecords(list);

	}

	// 插入一条出货其他费用--配件
	public void saveFitOrderOther(Integer detailId, Integer orderoutId) {

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<?> cfglist = this.getList("CotPriceCfg");
		CotPriceCfg cfg = new CotPriceCfg();
		if (cfglist.size() == 1) {
			cfg = (CotPriceCfg) cfglist.get(0);
		}

		List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();

		CotFittingsOrderdetail fitorderdetail = (CotFittingsOrderdetail) this
				.getOrderOutDao().getById(CotFittingsOrderdetail.class,
						detailId);
		// 获取对应的主单信息
		CotFittingOrder fittingOrder = (CotFittingOrder) this.getOrderOutDao()
				.getById(CotFittingOrder.class, fitorderdetail.getOrderId());

		List<?> otherList = this.getOrderOutDao().find(
				" from CotFinaceOther obj where obj.outFlag="
						+ fittingOrder.getId() + " and obj.source='FitMoney'");

		CotFinaceOther other = new CotFinaceOther();

		if (otherList.size() == 1) {
			other = (CotFinaceOther) otherList.get(0);
			// other.setId(other.getId());
			Double amount = fitorderdetail.getOutHasOut()
					* fitorderdetail.getFitPrice();
			Double allAmout = amount + other.getAmount();
			other.setAmount(allAmout);
		} else {
			Double amount = fitorderdetail.getOutHasOut()
					* fitorderdetail.getFitPrice();
			other.setAmount(amount.doubleValue());
			other.setCurrencyId(cfg.getFacPriceUnit());
			other.setFactoryId(fittingOrder.getFactoryId());
			other.setStatus(0);
			other.setFinaceName("配件货款");
			other.setFkId(orderoutId);
			other.setOrderNo(fittingOrder.getFittingOrderNo());
			other.setFlag("A");
			other.setOutFlag(fittingOrder.getId());
			other.setSource("FitMoney");
			other.setType(1);
			other.setRemainAmount(amount.doubleValue());
			other.setBusinessPerson(cotEmps.getId());
		}
		list.add(other);

		this.getOrderOutDao().saveOrUpdateRecords(list);

	}

	// 插入一条出货其他费用--包材
	public void savePackOrderOther(Integer detailId, Integer orderoutId) {

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<?> cfglist = this.getList("CotPriceCfg");
		CotPriceCfg cfg = new CotPriceCfg();
		if (cfglist.size() == 1) {
			cfg = (CotPriceCfg) cfglist.get(0);
		}

		List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();

		CotPackingOrderdetail packingOrderdetail = (CotPackingOrderdetail) this
				.getOrderOutDao()
				.getById(CotPackingOrderdetail.class, detailId);
		// 获取对应的主单信息
		CotPackingOrder packingOrder = (CotPackingOrder) this
				.getOrderOutDao()
				.getById(CotPackingOrder.class, packingOrderdetail.getOrderId());

		List<?> otherList = this.getOrderOutDao().find(
				" from CotFinaceOther obj where obj.outFlag="
						+ packingOrder.getId() + " and obj.source='PackMoney'");

		CotFinaceOther other = new CotFinaceOther();

		if (otherList.size() == 1) {
			other = (CotFinaceOther) otherList.get(0);
			// other.setId(other.getId());
			Double amount = packingOrderdetail.getOutHasOut()
					* packingOrderdetail.getPackPrice();
			Double allAmout = amount + other.getAmount();
			other.setAmount(allAmout);
		} else {
			Double amount = packingOrderdetail.getOutHasOut()
					* packingOrderdetail.getPackPrice();
			other.setAmount(amount.doubleValue());
			other.setCurrencyId(cfg.getFacPriceUnit());
			other.setFactoryId(packingOrder.getFactoryId());
			other.setStatus(0);
			other.setFinaceName("包材货款");
			other.setFkId(orderoutId);
			other.setOrderNo(packingOrder.getPackingOrderNo());
			other.setFlag("A");
			other.setOutFlag(packingOrder.getId());
			other.setSource("PackMoney");
			other.setType(1);
			other.setRemainAmount(amount.doubleValue());
			other.setBusinessPerson(cotEmps.getId());
		}
		list.add(other);

		this.getOrderOutDao().saveOrUpdateRecords(list);

	}

	// 插入一条出货其他费用--配件--未指定数量直接生成时
	public void saveFitOthersDeal(Integer orderoutId) {
		String fac = this.checkIsHasFits(orderoutId);
		if (fac.equals("")) {
			return;
		}
		// 获得rmb币种编号
		String rmbCur = "select obj.id from CotCurrency obj where obj.curNameEn='RMB'";
		List<?> curList = this.getOrderOutDao().find(rmbCur);
		Integer rmbId = null;
		if (curList.size() > 0) {
			rmbId = (Integer) curList.get(0);
		}

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		String hql = "from CotFittingOrder obj where obj.id in("
				+ fac.substring(0, fac.length() - 1) + ")";
		List list = new ArrayList();
		List acList = new ArrayList();
		List<?> facList = this.getOrderOutDao().find(hql);
		for (int i = 0; i < facList.size(); i++) {
			CotFittingOrder orderFac = (CotFittingOrder) facList.get(i);
			List<?> otherList = this.getOrderOutDao().find(
					" from CotFinaceOther obj where obj.outFlag="
							+ orderFac.getId() + " and obj.source='FitMoney'");

			if (otherList.size() == 1) {
				CotFinaceOther other = (CotFinaceOther) otherList.get(0);
				// 如果该出货其他费用已生成应付款,并且应付款已有冲帐明细不能再修改,否则该应付款扣掉旧的金额,如果剩余金额为0,删除应付款
				if (other.getStatus() == 1) {
					String sql = "from CotFinaceAccountdeal obj where obj.source='orderout' and obj.fkId="
							+ orderoutId
							+ " and obj.factoryId="
							+ other.getFactoryId();
					CotFinaceAccountdeal accountdeal = (CotFinaceAccountdeal) this
							.getOrderOutDao().find(sql).get(0);
					// 判断是否有冲帐
					String tempStr = "select obj.id from CotFinacegivenDetail obj where obj.dealId="
							+ accountdeal.getId();
					List givenList = this.getOrderOutDao().find(tempStr);
					if (givenList.size() == 0) {
						// 将旧的其他费用换算成该应付款的费用
						// 币种转换
						float mon = this.updatePrice(other.getAmount()
								.floatValue(), other.getCurrencyId(),
								accountdeal.getCurrencyId());
						double temp = accountdeal.getAmount() - mon;
						accountdeal.setAmount(temp);
						acList.add(accountdeal);
						other.setAmount(orderFac.getTotalAmmount());
						other.setCurrencyId(rmbId);
						other.setStatus(0);
						list.add(other);
					}
				} else {
					other.setAmount(orderFac.getTotalAmmount());
					other.setCurrencyId(rmbId);
					list.add(other);
				}
			} else {
				CotFinaceOther other = new CotFinaceOther();
				other.setAmount(orderFac.getTotalAmmount());
				other.setCurrencyId(rmbId);
				other.setFactoryId(orderFac.getFactoryId());
				other.setStatus(0);
				other.setFinaceName("配件货款");
				other.setFkId(orderoutId);
				other.setOrderNo(orderFac.getOrderNo());
				other.setFlag("A");
				other.setOutFlag(orderFac.getId());
				other.setSource("FitMoney");
				other.setType(1);
				other.setRemainAmount(orderFac.getTotalAmmount());
				other.setBusinessPerson(cotEmps.getId());
				list.add(other);
			}
		}
		this.getOrderOutDao().saveOrUpdateRecords(list);
	}

	// 插入一条出货其他费用--包材--未指定数量直接生成时
	public void savePackOthersDeal(Integer orderoutId) {
		String fac = this.checkIsHasPacks(orderoutId);
		if (fac.equals("")) {
			return;
		}
		// 获得rmb币种编号
		String rmbCur = "select obj.id from CotCurrency obj where obj.curNameEn='RMB'";
		List<?> curList = this.getOrderOutDao().find(rmbCur);
		Integer rmbId = null;
		if (curList.size() > 0) {
			rmbId = (Integer) curList.get(0);
		}
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		String hql = "from CotPackingOrder obj where obj.id in("
				+ fac.substring(0, fac.length() - 1) + ")";
		List list = new ArrayList();
		List acList = new ArrayList();
		List<?> facList = this.getOrderOutDao().find(hql);
		for (int i = 0; i < facList.size(); i++) {
			CotPackingOrder orderFac = (CotPackingOrder) facList.get(i);
			List<?> otherList = this.getOrderOutDao().find(
					" from CotFinaceOther obj where obj.outFlag="
							+ orderFac.getId() + " and obj.source='PackMoney'");

			if (otherList.size() == 1) {
				CotFinaceOther other = (CotFinaceOther) otherList.get(0);
				// 如果该出货其他费用已生成应付款,并且应付款已有冲帐明细不能再修改,否则该应付款扣掉旧的金额,如果剩余金额为0,删除应付款
				if (other.getStatus() == 1) {
					String sql = "from CotFinaceAccountdeal obj where obj.source='orderout' and obj.fkId="
							+ orderoutId
							+ " and obj.factoryId="
							+ other.getFactoryId();
					CotFinaceAccountdeal accountdeal = (CotFinaceAccountdeal) this
							.getOrderOutDao().find(sql).get(0);
					// 判断是否有冲帐
					String tempStr = "select obj.id from CotFinacegivenDetail obj where obj.dealId="
							+ accountdeal.getId();
					List givenList = this.getOrderOutDao().find(tempStr);
					if (givenList.size() == 0) {
						// 将旧的其他费用换算成该应付款的费用
						// 币种转换
						float mon = this.updatePrice(other.getAmount()
								.floatValue(), other.getCurrencyId(),
								accountdeal.getCurrencyId());
						double temp = accountdeal.getAmount() - mon;
						accountdeal.setAmount(temp);
						acList.add(accountdeal);
						other.setAmount(orderFac.getTotalAmount());
						other.setCurrencyId(rmbId);
						other.setStatus(0);
						list.add(other);
					}
				} else {
					other.setAmount(orderFac.getTotalAmount());
					other.setCurrencyId(rmbId);
					list.add(other);
				}
			} else {
				CotFinaceOther other = new CotFinaceOther();
				other.setAmount(orderFac.getTotalAmount());
				other.setCurrencyId(rmbId);
				other.setFactoryId(orderFac.getFactoryId());
				other.setStatus(0);
				other.setFinaceName("包材货款");
				other.setFkId(orderoutId);
				other.setOrderNo(orderFac.getOrderNo());
				other.setFlag("A");
				other.setOutFlag(orderFac.getId());
				other.setSource("PackMoney");
				other.setType(1);
				other.setRemainAmount(orderFac.getTotalAmount());
				other.setBusinessPerson(cotEmps.getId());
				list.add(other);
			}
		}
		this.getOrderOutDao().saveOrUpdateRecords(list);
	}

	// 自动分配
	public void autoOrderOut(Integer orderoutId) {
		this.saveOthersDeal(orderoutId);
		this.saveFitOthersDeal(orderoutId);
		this.savePackOthersDeal(orderoutId);
		// 删除出货应付款 金额为0的
		String temp = "select obj.id from CotFinaceAccountdeal obj where obj.amount=0 and obj.source='orderout' and obj.fkId="
				+ orderoutId;
		List delList = this.getOrderOutDao().find(temp);
		if (delList.size() > 0) {
			try {
				this.getOrderOutDao().deleteRecordByIds(delList,
						"CotFinaceAccountdeal");
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}

	// 判断出货数量是否等于采购数量
	public boolean checkIsEqualCount(Integer orderId, String eleId,
			Long boxCount) {

		String hql = " from VOrderOrderfacId obj where obj.orderId=" + orderId
				+ " and obj.eleId='" + eleId + "'";
		List<?> list = this.getOrderOutDao().find(hql);
		Long allBoxCount = 0L;
		for (int i = 0; i < list.size(); i++) {
			VOrderOrderfacId vOrderOrderfac = (VOrderOrderfacId) list.get(i);
			allBoxCount += vOrderOrderfac.getBoxCount();
		}
		if (allBoxCount.longValue() == boxCount.longValue()) {
			return true;
		} else {
			return false;
		}
	}

	// 更改采购剩余数量
	public void updateOrderFacOutRemain(Integer orderId, String eleId,
			Integer orderoutId) {

		String hql = " from VOrderOrderfacId obj where obj.orderId=" + orderId
				+ " and obj.eleId='" + eleId + "'";
		List<?> list = this.getOrderOutDao().find(hql);

		for (int i = 0; i < list.size(); i++) {
			VOrderOrderfacId vOrderOrderfac = (VOrderOrderfacId) list.get(i);
			// 修改剩余数量
			if (vOrderOrderfac.getAllocateFlag() == null
					|| vOrderOrderfac.getAllocateFlag() == 0) {
				this.updateFacDetail(vOrderOrderfac.getId(), vOrderOrderfac
						.getBoxCount());
				// if (vOrderOrderfac.getAllocateFlag() != 1) {
				// this.updateFacDetail(vOrderOrderfac.getOrderfacDetailId(),
				// vOrderOrderfac.getBoxCount());
			} else {
				continue;
			}
		}
	}

	// 更改配件采购剩余数量
	public void updateFitOrderOutRemain(Integer orderId) {

		String hql = " from VOrderFitorderId obj where obj.orderId=" + orderId;
		List<?> list = this.getOrderOutDao().find(hql);

		for (int i = 0; i < list.size(); i++) {
			VOrderFitorderId vOrderFitorderId = (VOrderFitorderId) list.get(i);
			// 修改剩余数量
			if (vOrderFitorderId.getAllocateFlag() == null
					|| vOrderFitorderId.getAllocateFlag() == 0) {
				this.updateFitDetail(vOrderFitorderId.getId(), vOrderFitorderId
						.getOrderCount());
				// if (vOrderFitorderId.getAllocateFlag() != 1 &&
				// vOrderFitorderId.getAllocateFlag() == null) {
				// this.updateFitDetail(vOrderFitorderId.getId(),
				// vOrderFitorderId.getOrderCount());
			} else {
				continue;
			}
		}
	}

	// 更改包材采购剩余数量
	public void updatePackOrderOutRemain(Integer orderId) {

		String hql = " from VOrderPackorderId obj where obj.orderId=" + orderId;
		List<?> list = this.getOrderOutDao().find(hql);

		for (int i = 0; i < list.size(); i++) {
			VOrderPackorderId vOrderPackorderId = (VOrderPackorderId) list
					.get(i);
			// 修改剩余数量
			if (vOrderPackorderId.getAllocateFlag() == null
					|| vOrderPackorderId.getAllocateFlag() == 0) {
				this.updatePackDetail(vOrderPackorderId.getId(),
						vOrderPackorderId.getPackCount().longValue());
			} else {
				continue;
			}
		}
	}

	// 获取所有出货明细
	public List<CotOrderOutdetail> getOrderOutdetail(Integer orderoutId) {
		List<CotOrderOutdetail> list = new ArrayList<CotOrderOutdetail>();
		String hql = " from CotOrderOutdetail obj where obj.orderId = "
				+ orderoutId;
		List<?> res = this.getOrderOutDao().find(hql);
		for (int i = 0; i < res.size(); i++) {
			CotOrderOutdetail detail = (CotOrderOutdetail) res.get(i);

			list.add(detail);
		}
		return list;
	}

	// 获取所有采购明细
	public List<CotOrderFacdetail> getOrderFacDetail(Integer orderDetailId) {
		List<CotOrderFacdetail> list = new ArrayList<CotOrderFacdetail>();
		String hql = " from CotOrderFacdetail obj where obj.orderDetailId = "
				+ orderDetailId;
		List<?> res = this.getOrderOutDao().find(hql);
		for (int i = 0; i < res.size(); i++) {
			CotOrderFacdetail detail = (CotOrderFacdetail) res.get(i);

			list.add(detail);
		}
		return list;
	}

	// 获取所有配件采购明细
	public List<VOrderFitorderId> getFitOrderDetail(Integer orderId) {
		List<VOrderFitorderId> list = new ArrayList<VOrderFitorderId>();
		String hql = " from VOrderFitorderId obj where obj.orderId = "
				+ orderId;
		List<?> res = this.getOrderOutDao().find(hql);
		for (int i = 0; i < res.size(); i++) {
			VOrderFitorderId detail = (VOrderFitorderId) res.get(i);

			list.add(detail);
		}
		return list;
	}

	// 获取所有包材采购明细
	public List<VOrderPackorderId> getPackOrderDetail(Integer orderId) {
		List<VOrderPackorderId> list = new ArrayList<VOrderPackorderId>();
		String hql = " from VOrderPackorderId obj where obj.orderId = "
				+ orderId;
		List<?> res = this.getOrderOutDao().find(hql);
		for (int i = 0; i < res.size(); i++) {
			VOrderPackorderId detail = (VOrderPackorderId) res.get(i);

			list.add(detail);
		}
		return list;
	}

	// 导入生产合同货款金额
	public void saveOthersDeal(Integer orderoutId) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		String fac = this.checkIsHasFacs(orderoutId);
		String hql = "from CotOrderFac obj where obj.id in("
				+ fac.substring(0, fac.length() - 1) + ")";
		List list = new ArrayList();
		List acList = new ArrayList();
		List<?> facList = this.getOrderOutDao().find(hql);
		for (int i = 0; i < facList.size(); i++) {
			CotOrderFac orderFac = (CotOrderFac) facList.get(i);
			List<?> otherList = this.getOrderOutDao().find(
					" from CotFinaceOther obj where obj.outFlag="
							+ orderFac.getId() + " and obj.source='FacMoney'");

			if (otherList.size() == 1) {
				CotFinaceOther other = (CotFinaceOther) otherList.get(0);
				// 如果该出货其他费用已生成应付款,并且应付款已有冲帐明细不能再修改,否则该应付款扣掉旧的金额,如果剩余金额为0,删除应付款
				if (other.getStatus() == 1) {
					String sql = "from CotFinaceAccountdeal obj where obj.source='orderout' and obj.fkId="
							+ orderoutId
							+ " and obj.factoryId="
							+ other.getFactoryId();
					CotFinaceAccountdeal accountdeal = (CotFinaceAccountdeal) this
							.getOrderOutDao().find(sql).get(0);
					// 判断是否有冲帐
					String tempStr = "select obj.id from CotFinacegivenDetail obj where obj.dealId="
							+ accountdeal.getId();
					List givenList = this.getOrderOutDao().find(tempStr);
					if (givenList.size() == 0) {
						// 将旧的其他费用换算成该应付款的费用
						// 币种转换
						float mon = this.updatePrice(other.getAmount()
								.floatValue(), other.getCurrencyId(),
								accountdeal.getCurrencyId());
						double temp = accountdeal.getAmount() - mon;
						accountdeal.setAmount(temp);
						acList.add(accountdeal);
						other.setAmount(orderFac.getTotalMoney());
						other.setCurrencyId(orderFac.getCurrencyId());
						other.setStatus(0);
						list.add(other);
					}
				} else {
					other.setAmount(orderFac.getTotalMoney());
					other.setCurrencyId(orderFac.getCurrencyId());
					list.add(other);
				}
			} else {
				CotFinaceOther other = new CotFinaceOther();
				other.setAmount(orderFac.getTotalMoney());
				other.setCurrencyId(orderFac.getCurrencyId());
				other.setFactoryId(orderFac.getFactoryId());
				other.setStatus(0);
				other.setFinaceName("生产合同货款");
				other.setFkId(orderoutId);
				other.setOrderNo(orderFac.getOrderNo());
				other.setFlag("A");
				other.setOutFlag(orderFac.getId());
				other.setSource("FacMoney");
				other.setType(1);
				other.setRemainAmount(orderFac.getTotalMoney());
				other.setBusinessPerson(cotEmps.getId());
				list.add(other);
			}
		}
		this.getOrderOutDao().saveOrUpdateRecords(list);
	}

	// 插入出货其他费用
	public void saveOrderFacOtherAuto(Integer orderfacDetailId,
			Integer orderoutId) {

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();

		CotOrderFacdetail orderFacdetail = (CotOrderFacdetail) this
				.getOrderOutDao().getById(CotOrderFacdetail.class,
						orderfacDetailId);
		// 获取对应的主单信息
		CotOrderFac orderFac = (CotOrderFac) this.getOrderOutDao().getById(
				CotOrderFac.class, orderFacdetail.getOrderId());

		List<?> otherList = this.getOrderOutDao().find(
				" from CotFinaceOther obj where obj.outFlag="
						+ orderFac.getId() + " and obj.source='FacMoney'");

		CotFinaceOther other = new CotFinaceOther();

		if (otherList.size() == 1) {
			other = (CotFinaceOther) otherList.get(0);
			other.setAmount(other.getAmount() + orderFacdetail.getBoxCount()
					* orderFacdetail.getPriceFac());
			// other.setId(other.getId());
		} else {
			Float amount = orderFacdetail.getBoxCount()
					* orderFacdetail.getPriceFac();
			other.setAmount(amount.doubleValue());
			other.setCurrencyId(orderFacdetail.getPriceFacUint());
			other.setFactoryId(orderFac.getFactoryId());
			other.setStatus(0);
			other.setFinaceName("生产合同货款");
			other.setFkId(orderoutId);
			other.setOrderNo(orderFac.getOrderNo());
			other.setFlag("A");
			other.setOutFlag(orderFac.getId());
			other.setSource("FacMoney");
			other.setType(1);
			other.setRemainAmount(amount.doubleValue());
			other.setBusinessPerson(cotEmps.getId());
		}
		list.add(other);

		this.getOrderOutDao().saveOrUpdateRecords(list);

	}

	// 查询采购单是否有应付帐款
	public Integer checkIsHasDeal(Integer orderfacId, String source) {
		String hql = " from CotFinaceAccountdeal obj where obj.source ='"
				+ source + "'" + " and obj.remainAmount>0 and obj.fkId ="
				+ orderfacId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() != 0) {
			return 1;
		} else {
			return null;
		}
	}

	// 查询采购单是否有其他费用
	public Integer checkIsHasOther(Integer orderfacId, String source) {
		String hql = " from CotFinaceOther obj where obj.source ='" + source
				+ "'"
				+ "and obj.type = 1 and obj.remainAmount>0 and obj.fkId ="
				+ orderfacId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() != 0) {
			return 1;
		} else {
			return null;
		}
	}

	// 其他费用余额
	public void modifyOtherRemain(Integer otherId, Double amount, String flag) {
		List<CotFinaceOther> res = new ArrayList<CotFinaceOther>();

		String hql = " from CotFinaceOther obj where obj.source='orderfac' and  obj.id ="
				+ otherId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			CotFinaceOther other = (CotFinaceOther) list.get(0);
			if ("add".equals(flag)) {
				other.setRemainAmount(other.getRemainAmount() - amount);
			} else {
				other.setRemainAmount(other.getRemainAmount() + amount);
			}
			res.add(other);

			this.getOrderOutDao().updateRecords(res);
		}

	}

	// 其他费用导入标志
	public void modifyGivenOther(Integer outOtherId, Integer facOtherId,
			Double amount, String flag) {
		List<CotFinaceOther> res = new ArrayList<CotFinaceOther>();

		String hql = " from CotFinaceOther obj where obj.source is null and obj.id ="
				+ facOtherId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			CotFinaceOther other = (CotFinaceOther) list.get(0);
			if ("add".equals(flag)) {
				// 溢付款已导入出货 -2
				other.setIsImport(-2);
				// 修改付款记录的剩余金额为0
				Double remainAmount = 0.0;
				this.modifyGivenRemainAmount(other.getFkId(), remainAmount);
			} else {
				// 判断总溢出金额是否等于剩余金额
				if (other.getAmount().doubleValue() == (other.getRemainAmount())
						.doubleValue()) {
					other.setIsImport(null);
				}
				// 修改付款记录的剩余金额
				this.modifyGivenRemainAmount(other.getFkId(), other
						.getRemainAmount());
			}
			res.add(other);

			this.getOrderOutDao().updateRecords(res);
		}
	}

	// 修改付款记录的剩余金额为0
	public void modifyGivenRemainAmount(Integer fingivenId, Double remainAmount) {
		CotFinacegiven given = new CotFinacegiven();
		List<?> res = this.getOrderOutDao().find(
				" from CotFinacegiven obj where obj.id=" + fingivenId);
		if (res.size() == 1) {
			given = (CotFinacegiven) res.get(0);
			given.setRemainAmount(remainAmount);
			given.setCotFinacegivenDetails(null);
		}
		List<CotFinacegiven> list = new ArrayList<CotFinacegiven>();

		list.add(given);

		this.getOrderOutDao().updateRecords(list);
	}

	// 应付款
	public void modifyDealRemain(Integer dealId, Double amount, String flag) {
		List<CotFinaceAccountdeal> res = new ArrayList<CotFinaceAccountdeal>();

		String hql = " from CotFinaceAccountdeal obj where obj.id =" + dealId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			CotFinaceAccountdeal deal = (CotFinaceAccountdeal) list.get(0);
			if ("add".equals(flag)) {
				deal.setZhRemainAmount(deal.getZhRemainAmount() - amount);
				deal.setIsImport(1);
				// deal.setRealAmount(deal.getRealAmount() + amount);
			} else {
				deal.setZhRemainAmount(deal.getZhRemainAmount() + amount);
				deal.setIsImport(0);
				// deal.setRealAmount(deal.getRealAmount() - amount);
			}
			res.add(deal);

			this.getOrderOutDao().updateRecords(res);
		}

	}

	// 剩余金额-删除时
	public void modifyOtherRemainDel(Integer otherId) {

		String hql = " from CotFinaceOther obj where obj.id =" + otherId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			CotFinaceOther other = (CotFinaceOther) list.get(0);
			if (other.getSource().equals("orderfacOther")) {
				this.modifyOtherRemain(other.getOutFlag(), other.getAmount(),
						"del");
			}
			if (other.getSource().equals("orderfacDeal")) {
				this.modifyDealRemain(other.getOutFlag(), other.getAmount(),
						"del");
			}
			if ("溢付款".equals(other.getFinaceName())) {
				this.modifyGivenOther(other.getId(), other.getOutFlag(), other
						.getAmount(), "del");
			}
		}

	}

	// 根据编号获得对象
	public CotFinaceAccountdeal getGivenDealById(Integer id) {
		CotFinaceAccountdeal deal = (CotFinaceAccountdeal) this
				.getOrderOutDao().getById(CotFinaceAccountdeal.class, id);
		return deal;
	}

	// 判断应付帐款是否有冲帐明细
	public List checkIsHaveDetail(List ids) {
		List delList = new ArrayList();
		for (int i = 0; i < ids.size(); i++) {
			Integer id = (Integer) ids.get(i);
			List<?> list = this.getOrderOutDao().find(
					"select obj.id from CotFinacegivenDetail obj where obj.dealId="
							+ id);
			if (list.size() == 0) {
				delList.add(id);
			}
		}
		return delList;
	}

	// 判断其对应的生产合同其他费用剩余金额是否已生成应付帐款
	public String checkIsHasDealed(Integer[] ids) {
		String finaceNames = new String();
		for (int i = 0; i < ids.length; i++) {
			CotFinaceOther other = this.getCotFinaceOtherById(ids[i]);

			List<?> list = this.getOrderOutDao().find(
					" from CotFinaceOther obj where obj.id="
							+ other.getOutFlag());
			if (list.size() == 1) {
				CotFinaceOther newOther = (CotFinaceOther) list.get(0);
				if (newOther.getStatus() == 1) {
					finaceNames += newOther.getFinaceName() + "、";
				}
			}
		}
		if (finaceNames.length() != 0) {
			return finaceNames.substring(0, finaceNames.length() - 1);
		} else {
			return null;
		}
	}

	// 删除未导入出货的、付款金额为0的应付款
	public void delFinaceGivenDeal(String orderfacIds) {
		List<Integer> ids = new ArrayList<Integer>();

		String[] orderfacId = orderfacIds.split(",");
		for (int i = 0; i < orderfacId.length; i++) {
			String hql = " from CotFinaceAccountdeal obj where obj.fkId="
					+ orderfacId[i] + " and (obj.isImport = 1)"
					+ " and (obj.realAmount = 0 or obj.realAmount is null)";
			List<?> list = this.getOrderOutDao().find(hql);
			for (int j = 0; j < list.size(); j++) {
				CotFinaceAccountdeal deal = (CotFinaceAccountdeal) list.get(j);
				ids.add(deal.getId());
				this.modifyFinOtherStatus(deal.getFkId(), deal.getFinaceName(),
						"del");
			}
		}
		try {
			this.getOrderOutDao()
					.deleteRecordByIds(ids, "CotFinaceAccountdeal");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 修改其他费用状态 0：未生成 1: 已生成
	public void modifyFinOtherStatus(Integer fkId, String finaceName,
			String flag) {
		String hql = " from CotFinaceOther obj where obj.source='orderfac' and obj.fkId="
				+ fkId + " and obj.finaceName='" + finaceName + "'";
		List<CotFinaceOther> res = new ArrayList<CotFinaceOther>();
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() == 1) {
			CotFinaceOther other = (CotFinaceOther) list.get(0);
			if ("add".equals(flag)) {
				other.setStatus(1);
			} else {
				other.setStatus(0);
			}

			res.add(other);
			this.getOrderOutDao().updateRecords(res);
		}
	}

	/*------------------------------ 应付帐款操作部分 over--------------------------------------*/

	// 保存应收款其他费用
	public Boolean addOtherList(HttpServletRequest request, List<?> details) {
		HttpSession session = request.getSession();
		Map dicMap = (Map) session.getAttribute("sysdic");
		List curList = (List) dicMap.get("currency");
		Map map = new HashMap();
		for (int i = 0; i < curList.size(); i++) {
			CotCurrency currency = (CotCurrency) curList.get(i);
			map.put(currency.getId(), currency);
		}
		// 应收款其他费用的RMB总和
		float recvRmb = 0;
		// 出货主单编号
		Integer orderId = 0;
		for (int i = 0; i < details.size(); i++) {
			CotFinaceOther cotFinaceOther = (CotFinaceOther) details.get(i);
			orderId = cotFinaceOther.getFkId();
			CotCurrency currency = (CotCurrency) map.get(cotFinaceOther
					.getCurrencyId());
			if ("A".equals(cotFinaceOther.getFlag())) {
				recvRmb += cotFinaceOther.getAmount() * currency.getCurRate();
			} else {
				recvRmb -= cotFinaceOther.getAmount() * currency.getCurRate();
			}

		}
		if (recvRmb != 0) {
			CotOrderOut cotOrderOut = (CotOrderOut) this
					.getOrderOutById(orderId);
			CotCurrency cur = (CotCurrency) map
					.get(cotOrderOut.getCurrencyId());
			float totalRecvOther = recvRmb / cur.getCurRate();
			if (cotOrderOut.getTotalRecvOther() != null) {
				cotOrderOut.setTotalRecvOther(cotOrderOut.getTotalRecvOther()
						+ totalRecvOther);
			} else {
				cotOrderOut.setTotalRecvOther(totalRecvOther);
			}

			List list = new ArrayList();
			list.add(cotOrderOut);
			this.getOrderOutDao().updateRecords(list);
		}
		try {
			this.getOrderOutDao().saveRecords(details);
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("保存其他费用出错!");
			return false;
		}
	}

	// 根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id) {
		CotFinaceOther finaceOther = (CotFinaceOther) this.getOrderOutDao()
				.getById(CotFinaceOther.class, id);
		return finaceOther;
	}

	// 更新其他费用
	public Boolean updateOtherList(HttpServletRequest request, List<?> details) {
		HttpSession session = request.getSession();
		Map dicMap = (Map) session.getAttribute("sysdic");
		List curList = (List) dicMap.get("currency");
		Map map = new HashMap();
		for (int i = 0; i < curList.size(); i++) {
			CotCurrency currency = (CotCurrency) curList.get(i);
			map.put(currency.getId(), currency);
		}

		// 新应收款其他费用的RMB总和
		float recvRmb = 0;
		// 旧应收款其他费用的RMB总和
		float oldRmb = 0;
		// 出货主单编号
		Integer orderId = 0;
		for (int i = 0; i < details.size(); i++) {
			CotFinaceOther newOther = (CotFinaceOther) details.get(i);
			orderId = newOther.getFkId();
			CotCurrency currency = (CotCurrency) map.get(newOther
					.getCurrencyId());
			if ("A".equals(newOther.getFlag())) {
				recvRmb += newOther.getAmount() * currency.getCurRate();
			} else {
				recvRmb -= newOther.getAmount() * currency.getCurRate();
			}

			CotFinaceOther oldOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", newOther.getId());

			CotCurrency currencyOld = (CotCurrency) map.get(oldOther
					.getCurrencyId());
			if ("A".equals(oldOther.getFlag())) {
				oldRmb += oldOther.getAmount() * currencyOld.getCurRate();
			} else {
				oldRmb -= oldOther.getAmount() * currencyOld.getCurRate();
			}

		}
		CotOrderOut cotOrderOut = (CotOrderOut) this.getOrderOutById(orderId);
		CotCurrency cur = (CotCurrency) map.get(cotOrderOut.getCurrencyId());
		float totalRecvOther = (recvRmb - oldRmb) / cur.getCurRate();
		if (cotOrderOut.getTotalRecvOther() != null) {
			cotOrderOut.setTotalRecvOther(cotOrderOut.getTotalRecvOther()
					+ totalRecvOther);
		}

		List list = new ArrayList();
		list.add(cotOrderOut);
		this.getOrderOutDao().updateRecords(list);
		try {
			this.getOrderOutDao().updateRecords(details);
		} catch (Exception e) {
			logger.error("更新其他费用异常", e);
		}
		return true;
	}

	// 更新应付款其他费用
	public Boolean updateDealOtherList(List<?> details) {
		try {
			this.getOrderOutDao().updateRecords(details);
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
			this.getOrderOutDao().deleteRecordByIds(ids, "CotFinaceOther");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 根据出货主单编号查找出货明细中的订单orderNoid
	public String findOrderId(Integer orderOutId) {
		String hql = "select distinct obj.orderNoid from CotOrderOutdetail obj where obj.orderId="
				+ orderOutId;
		List array = this.getOrderOutDao().find(hql);
		// 订单主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款时过滤掉
		String str = "";
		Iterator<?> it = array.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			boolean flag = false;
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='order'"
					+ " and (obj.outFlag is null or obj.outFlag = 0) and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				flag = true;
			}
			boolean flag2 = false;
			String dealHql = "select obj.id from CotFinaceAccountrecv obj where obj.source ='order'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				flag2 = true;
			}

			if (flag || flag2) {
				str += orderId + ",";
			}
		}
		return str;
	}

	// 根据条件查询冲帐明细记录
	public List<?> getOrderNoList(List<?> list) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotOrderVO vo = new CotOrderVO();
			vo.setOrderNo((String) obj[0]);
			vo.setId((Integer) obj[1]);
			listVo.add(vo);
		}
		return listVo;
	}

	// 根据条件查询冲帐明细记录
	public List<?> getOrderFacNoList(List<?> list) {
		List<CotOrderFacVO> listVo = new ArrayList<CotOrderFacVO>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotOrderFacVO vo = new CotOrderFacVO();
			vo.setOrderNo((String) obj[0]);
			vo.setId((Integer) obj[1]);
			listVo.add(vo);
		}
		return listVo;
	}

	// 查询所有币种
	public Map<Integer, CotCurrency> getCurrencyObjMap(
			HttpServletRequest request) {
		Map<Integer, CotCurrency> map = new HashMap<Integer, CotCurrency>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId(), cotCurrency);
		}
		return map;
	}

	// 保存时,更改订单其他费用的的剩余金额.如果剩余金额等于0,则outFlag为1
	public void updateOrderIsImport(HttpServletRequest request, List amountReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);

		List other = new ArrayList();
		for (int i = 0; i < amountReal.size(); i++) {
			CotFinaceOther finaceOther = (CotFinaceOther) amountReal.get(i);
			CotCurrency cur = map.get(finaceOther.getCurrencyId());
			Double realRmb = cur.getCurRate() * finaceOther.getAmount();

			CotFinaceOther fin = (CotFinaceOther) this.getOrderOutDao()
					.getById(CotFinaceOther.class, finaceOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double remainRmb = curFin.getCurRate() * fin.getRemainAmount();
			Double rmb = remainRmb - realRmb;

			Double money = rmb / curFin.getCurRate();

			fin.setRemainAmount(money);
			if (fin.getRemainAmount() == 0) {
				fin.setOutFlag(1);
			}
			other.add(fin);
		}
		this.getOrderOutDao().updateRecords(other);
	}

	// 更改订单其他费用的剩余金额.
	public void updateOtherRemain(HttpServletRequest request, List amountReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);

		List other = new ArrayList();
		for (int i = 0; i < amountReal.size(); i++) {
			CotFinaceOther newOther = (CotFinaceOther) amountReal.get(i);
			CotCurrency cur = map.get(newOther.getCurrencyId());
			Double newRmb = cur.getCurRate() * newOther.getAmount();

			// 获得旧的对象
			CotFinaceOther oldOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", newOther.getId());
			CotCurrency oldCur = map.get(oldOther.getCurrencyId());
			Double oldRmb = oldCur.getCurRate() * oldOther.getAmount();

			CotFinaceOther fin = (CotFinaceOther) this.getOrderOutDao()
					.getById(CotFinaceOther.class, newOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double remainRmb = curFin.getCurRate() * fin.getRemainAmount();
			Double rmb = remainRmb + oldRmb - newRmb;

			Double money = rmb / curFin.getCurRate();

			fin.setRemainAmount(money);
			if (money == 0) {
				fin.setOutFlag(1);
			} else {
				fin.setOutFlag(0);
			}
			other.add(fin);
		}
		this.getOrderOutDao().updateRecords(other);
	}

	// 更改订单应收帐的剩余金额.
	public void updateRecvMod(HttpServletRequest request, List accReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);

		List other = new ArrayList();
		for (int i = 0; i < accReal.size(); i++) {
			CotFinaceOther newOther = (CotFinaceOther) accReal.get(i);
			CotCurrency cur = map.get(newOther.getCurrencyId());
			Double newRmb = cur.getCurRate() * newOther.getAmount();

			// 获得旧的对象
			CotFinaceOther oldOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", newOther.getId());
			CotCurrency oldCur = map.get(oldOther.getCurrencyId());
			Double oldRmb = oldCur.getCurRate() * oldOther.getAmount();

			CotFinaceAccountrecv fin = (CotFinaceAccountrecv) this
					.getOrderOutDao().getById(CotFinaceAccountrecv.class,
							newOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double remainRmb = curFin.getCurRate() * fin.getZhRemainAmount();
			Double rmb = remainRmb + oldRmb - newRmb;

			Double money = rmb / curFin.getCurRate();

			fin.setZhRemainAmount(money);
			other.add(fin);
		}
		this.getOrderOutDao().updateRecords(other);

	}

	// 更改应付帐的剩余金额.
	public void updateDealMod(HttpServletRequest request, List accReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);

		List other = new ArrayList();
		for (int i = 0; i < accReal.size(); i++) {
			CotFinaceOther newOther = (CotFinaceOther) accReal.get(i);
			CotCurrency cur = map.get(newOther.getCurrencyId());
			Double newRmb = cur.getCurRate() * newOther.getAmount();

			// 获得旧的对象
			CotFinaceOther oldOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", newOther.getId());
			CotCurrency oldCur = map.get(oldOther.getCurrencyId());
			Double oldRmb = oldCur.getCurRate() * oldOther.getAmount();

			CotFinaceAccountdeal fin = (CotFinaceAccountdeal) this
					.getOrderOutDao().getById(CotFinaceAccountdeal.class,
							newOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double remainRmb = curFin.getCurRate() * fin.getZhRemainAmount();
			Double rmb = remainRmb + oldRmb - newRmb;

			Double money = rmb / curFin.getCurRate();

			fin.setZhRemainAmount(money);
			other.add(fin);
		}
		this.getOrderOutDao().updateRecords(other);

	}

	// 更改溢收款的剩余金额.
	public void updateYiRemain(HttpServletRequest request, List accReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);

		List other = new ArrayList();
		List<CotFinacerecv> recv = new ArrayList<CotFinacerecv>();
		for (int i = 0; i < accReal.size(); i++) {
			CotFinaceOther newOther = (CotFinaceOther) accReal.get(i);
			CotCurrency cur = map.get(newOther.getCurrencyId());
			Double newRmb = cur.getCurRate() * newOther.getAmount();

			// 获得旧的对象
			CotFinaceOther oldOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", newOther.getId());
			CotCurrency oldCur = map.get(oldOther.getCurrencyId());
			Double oldRmb = oldCur.getCurRate() * oldOther.getAmount();

			CotFinaceOther fin = (CotFinaceOther) this.getOrderOutDao()
					.getById(CotFinaceOther.class, newOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double remainRmb = curFin.getCurRate() * fin.getAmount();
			Double rmb = remainRmb + oldRmb - newRmb;

			Double money = rmb / curFin.getCurRate();

			fin.setAmount(money);
			other.add(fin);

			// 查询收款主单
			CotFinacerecv finacerecv = (CotFinacerecv) this.getOrderOutDao()
					.getById(CotFinacerecv.class, fin.getFkId());
			if (finacerecv != null) {
				CotFinacerecv clone = (CotFinacerecv) SystemUtil
						.deepClone(finacerecv);
				clone.setCotFinacerecvDetails(null);
				clone.setRemainAmount(money);
				recv.add(clone);
			}
		}
		this.getOrderOutDao().updateRecords(other);
		this.getOrderOutDao().updateRecords(recv);
	}

	// 更改溢付款的剩余金额.
	public void updateDealYiRemain(HttpServletRequest request, List accReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);

		List other = new ArrayList();
		List<CotFinacegiven> recv = new ArrayList<CotFinacegiven>();
		for (int i = 0; i < accReal.size(); i++) {
			CotFinaceOther newOther = (CotFinaceOther) accReal.get(i);
			CotCurrency cur = map.get(newOther.getCurrencyId());
			Double newRmb = cur.getCurRate() * newOther.getAmount();

			// 获得旧的对象
			CotFinaceOther oldOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", newOther.getId());
			CotCurrency oldCur = map.get(oldOther.getCurrencyId());
			Double oldRmb = oldCur.getCurRate() * oldOther.getAmount();

			CotFinaceOther fin = (CotFinaceOther) this.getOrderOutDao()
					.getById(CotFinaceOther.class, newOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double remainRmb = curFin.getCurRate() * fin.getAmount();
			Double rmb = remainRmb + oldRmb - newRmb;

			Double money = rmb / curFin.getCurRate();

			fin.setAmount(money);
			other.add(fin);

			// 查询收款主单
			CotFinacegiven finacerecv = (CotFinacegiven) this.getOrderOutDao()
					.getById(CotFinacegiven.class, fin.getFkId());
			if (finacerecv != null) {
				CotFinacegiven clone = (CotFinacegiven) SystemUtil
						.deepClone(finacerecv);
				clone.setCotFinacegivenDetails(null);
				clone.setRemainAmount(money);
				recv.add(clone);
			}
		}
		this.getOrderOutDao().updateRecords(other);
		this.getOrderOutDao().updateRecords(recv);
	}

	// 更改订单应收帐的剩余金额
	public void updateOrderRecvIsImport(HttpServletRequest request, List accReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);
		List<CotFinaceAccountrecv> other = new ArrayList<CotFinaceAccountrecv>();
		for (int i = 0; i < accReal.size(); i++) {
			// 其他费用金额的RMB值
			CotFinaceOther finaceOther = (CotFinaceOther) accReal.get(i);
			CotCurrency cur = map.get(finaceOther.getCurrencyId());
			Double realRmb = cur.getCurRate() * finaceOther.getAmount();
			// 对应的应收帐的未流转金额的RMB值
			CotFinaceAccountrecv fin = (CotFinaceAccountrecv) this
					.getOrderOutDao().getById(CotFinaceAccountrecv.class,
							finaceOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double zhRemainRmb = curFin.getCurRate() * fin.getZhRemainAmount();
			Double rmb = zhRemainRmb - realRmb;
			Double money = rmb / curFin.getCurRate();
			fin.setZhRemainAmount(money);

			other.add(fin);
		}
		this.getOrderOutDao().updateRecords(other);
	}

	// 更改收款记录的剩余金额
	public void updateRecvRemain(HttpServletRequest request, List amountYi) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);
		List<CotFinaceOther> other = new ArrayList<CotFinaceOther>();
		List<CotFinacerecv> recv = new ArrayList<CotFinacerecv>();
		for (int i = 0; i < amountYi.size(); i++) {
			// 其他费用金额的RMB值
			CotFinaceOther finaceOther = (CotFinaceOther) amountYi.get(i);
			CotCurrency cur = map.get(finaceOther.getCurrencyId());
			Double realRmb = cur.getCurRate() * finaceOther.getAmount();
			// 对应的溢收款的总金额的RMB值
			CotFinaceOther fin = (CotFinaceOther) this.getOrderOutDao()
					.getById(CotFinaceOther.class, finaceOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double allRmb = curFin.getCurRate() * fin.getAmount();
			Double rmb = allRmb - realRmb;
			Double money = rmb / curFin.getCurRate();
			fin.setAmount(money);
			other.add(fin);

			// 查询收款主单
			CotFinacerecv finacerecv = (CotFinacerecv) this.getOrderOutDao()
					.getById(CotFinacerecv.class, fin.getFkId());
			if (finacerecv != null) {
				CotFinacerecv clone = (CotFinacerecv) SystemUtil
						.deepClone(finacerecv);
				clone.setCotFinacerecvDetails(null);
				clone.setRemainAmount(money);
				recv.add(clone);
			}
		}
		try {
			this.getOrderOutDao().updateRecords(other);
			this.getOrderOutDao().updateRecords(recv);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 更改付款记录的剩余金额
	public void updateDealRemain(HttpServletRequest request, List amountYi) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);
		List<CotFinaceOther> other = new ArrayList<CotFinaceOther>();
		List<CotFinacegiven> recv = new ArrayList<CotFinacegiven>();
		for (int i = 0; i < amountYi.size(); i++) {
			// 其他费用金额的RMB值
			CotFinaceOther finaceOther = (CotFinaceOther) amountYi.get(i);
			CotCurrency cur = map.get(finaceOther.getCurrencyId());
			Double realRmb = cur.getCurRate() * finaceOther.getAmount();
			// 对应的溢收款的总金额的RMB值
			CotFinaceOther fin = (CotFinaceOther) this.getOrderOutDao()
					.getById(CotFinaceOther.class, finaceOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double allRmb = curFin.getCurRate() * fin.getAmount();
			Double rmb = allRmb - realRmb;
			Double money = rmb / curFin.getCurRate();
			fin.setAmount(money);
			other.add(fin);

			// 更改付款主单
			CotFinacegiven finacerecv = (CotFinacegiven) this.getOrderOutDao()
					.getById(CotFinacegiven.class, fin.getFkId());
			if (finacerecv != null) {
				CotFinacegiven clone = (CotFinacegiven) SystemUtil
						.deepClone(finacerecv);
				clone.setCotFinacegivenDetails(null);
				clone.setRemainAmount(money);
				recv.add(clone);
			}
		}
		try {
			this.getOrderOutDao().updateRecords(other);
			this.getOrderOutDao().updateRecords(recv);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 删除应收款其他费用
	public Float deleteByIds(List<Integer> ids) {
		try {
			// 获得币种
			WebContext ctx = WebContextFactory.get();
			Map<Integer, CotCurrency> map = getCurrencyObjMap(ctx
					.getHttpServletRequest());
			String idStr = "";
			for (int i = 0; i < ids.size(); i++) {
				idStr += ids.get(i) + ",";
			}

			// 查询要删除的费用的总金额
			String hql = "from CotFinaceOther obj where obj.id in ("
					+ idStr.substring(0, idStr.length() - 1) + ")";
			List listTemp = this.getOrderOutDao().find(hql);
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
			// 更新主订单的应收款的其他费用总金额
			if (allRmb != 0) {
				CotOrderOut orderOut = this.getOrderOutById(orderId);
				CotCurrency oldOrder = map.get(orderOut.getCurrencyId());
				Float curRate = oldOrder.getCurRate();

				Double totalRecvOther = allRmb /Double.parseDouble( oldOrder.getCurRate()+"");
				orderOut.setTotalRecvOther(orderOut.getTotalRecvOther()
						- totalRecvOther.floatValue());

				List listOrder = new ArrayList();
				listOrder.add(orderOut);
				this.getOrderOutDao().updateRecords(listOrder);
			}

			List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
			List<CotFinaceOther> listYi = new ArrayList<CotFinaceOther>();
			List<CotFinacerecv> recv = new ArrayList<CotFinacerecv>();
			List<CotFinaceAccountrecv> listRecv = new ArrayList<CotFinaceAccountrecv>();
			for (int i = 0; i < ids.size(); i++) {
				CotFinaceOther other = (CotFinaceOther) this.getOrderOutDao()
						.getById(CotFinaceOther.class, ids.get(i));
				Integer isOut = other.getOutFlag();
				String sou = other.getSource();
				if (isOut != null && isOut != 0) {
					CotCurrency curDel = map.get(other.getCurrencyId());
					Double mon = curDel.getCurRate() * other.getAmount();
					if ("orderOther".equals(sou)) {
						// 还原订单的其他费用的导入标识为0
						CotFinaceOther cotFinaceOther = (CotFinaceOther) this
								.getOrderOutDao().getById(CotFinaceOther.class,
										isOut);
						cotFinaceOther.setOutFlag(0);

						CotCurrency curRemain = map.get(cotFinaceOther
								.getCurrencyId());
						Double remain = curRemain.getCurRate()
								* cotFinaceOther.getRemainAmount();

						Double all = remain + mon;

						cotFinaceOther.setRemainAmount(all
								/ curRemain.getCurRate());
						list.add(cotFinaceOther);
					}
					if ("orderRecv".equals(sou)) {
						// 还原订单的应收帐的导入标识为0
						CotFinaceAccountrecv finaceAccountrecv = (CotFinaceAccountrecv) this
								.getOrderOutDao().getById(
										CotFinaceAccountrecv.class, isOut);

						CotCurrency curRemain = map.get(finaceAccountrecv
								.getCurrencyId());
						Double remain = curRemain.getCurRate()
								* finaceAccountrecv.getZhRemainAmount();
						Double all = remain + mon;
						finaceAccountrecv.setZhRemainAmount(all
								/ curRemain.getCurRate());
						listRecv.add(finaceAccountrecv);
					}
					if ("yi".equals(sou)) {
						// 还原溢收款的金额和收款记录中的剩余金额
						CotFinaceOther finaceOther = (CotFinaceOther) this
								.getOrderOutDao().getById(CotFinaceOther.class,
										isOut);

						CotCurrency curRemain = map.get(finaceOther
								.getCurrencyId());
						Double remain = curRemain.getCurRate()
								* finaceOther.getAmount();

						Double all = remain + mon;

						finaceOther.setAmount(all / curRemain.getCurRate());
						listYi.add(finaceOther);
						// 还原收款记录的剩余金额
						// 查询收款主单
						CotFinacerecv finacerecv = (CotFinacerecv) this
								.getOrderOutDao().getById(CotFinacerecv.class,
										finaceOther.getFkId());
						if (finacerecv != null) {
							CotFinacerecv clone = (CotFinacerecv) SystemUtil
									.deepClone(finacerecv);
							clone.setCotFinacerecvDetails(null);
							clone.setRemainAmount(all / curRemain.getCurRate());
							recv.add(clone);
						}
					}
				}
			}
			this.getOrderOutDao().deleteRecordByIds(ids, "CotFinaceOther");
			if (list.size() > 0) {
				this.getOrderOutDao().updateRecords(list);
			}
			if (listRecv.size() > 0) {
				this.getOrderOutDao().updateRecords(listRecv);
			}
			if (listYi.size() > 0) {
				this.getOrderOutDao().updateRecords(listYi);
				this.getOrderOutDao().updateRecords(recv);
			}
			// if (allRmb != 0) {
			// return res;
			// } else {
			// return null;
			// }
			return 0f;
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 删除应付款其他费用
	public Float deleteDealByIds(List<Integer> ids) {
		try {
			// 获得币种
			WebContext ctx = WebContextFactory.get();
			Map<Integer, CotCurrency> map = getCurrencyObjMap(ctx
					.getHttpServletRequest());
			String idStr = "";
			for (int i = 0; i < ids.size(); i++) {
				idStr += ids.get(i) + ",";
			}

			List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
			List<CotFinaceOther> listYi = new ArrayList<CotFinaceOther>();
			List<CotFinacegiven> recv = new ArrayList<CotFinacegiven>();
			List<CotFinaceAccountdeal> listRecv = new ArrayList<CotFinaceAccountdeal>();
			for (int i = 0; i < ids.size(); i++) {
				CotFinaceOther other = (CotFinaceOther) this.getOrderOutDao()
						.getById(CotFinaceOther.class, ids.get(i));
				Integer isOut = other.getOutFlag();
				String sou = other.getSource();
				if (isOut != null && isOut != 0) {
					CotCurrency curDel = map.get(other.getCurrencyId());
					Double mon = curDel.getCurRate() * other.getAmount();
					if ("FacOther".equals(sou) || "FitOther".equals(sou)
							|| "PackOther".equals(sou)) {
						CotFinaceOther cotFinaceOther = (CotFinaceOther) this
								.getOrderOutDao().getById(CotFinaceOther.class,
										isOut);
						cotFinaceOther.setOutFlag(0);

						CotCurrency curRemain = map.get(cotFinaceOther
								.getCurrencyId());
						Double remain = curRemain.getCurRate()
								* cotFinaceOther.getRemainAmount();

						Double all = remain + mon;

						cotFinaceOther.setRemainAmount(all
								/ curRemain.getCurRate());
						list.add(cotFinaceOther);
					}
					if ("FacDeal".equals(sou) || "FitDeal".equals(sou)
							|| "PackDeal".equals(sou)) {
						CotFinaceAccountdeal finaceAccountrecv = (CotFinaceAccountdeal) this
								.getOrderOutDao().getById(
										CotFinaceAccountdeal.class, isOut);
						CotCurrency curRemain = map.get(finaceAccountrecv
								.getCurrencyId());
						Double remain = curRemain.getCurRate()
								* finaceAccountrecv.getZhRemainAmount();
						Double all = remain + mon;
						finaceAccountrecv.setZhRemainAmount(all
								/ curRemain.getCurRate());
						listRecv.add(finaceAccountrecv);
					}
					if ("yiDeal".equals(sou)) {
						// 还原溢收款的金额和收款记录中的剩余金额
						CotFinaceOther finaceOther = (CotFinaceOther) this
								.getOrderOutDao().getById(CotFinaceOther.class,
										isOut);
						if (finaceOther != null) {
							CotCurrency curRemain = map.get(finaceOther
									.getCurrencyId());
							Double remain = curRemain.getCurRate()
									* finaceOther.getAmount();

							Double all = remain + mon;

							finaceOther.setAmount(all / curRemain.getCurRate());
							listYi.add(finaceOther);
							// 还原收款记录的剩余金额
							// 查询收款主单
							CotFinacegiven finacerecv = (CotFinacegiven) this
									.getOrderOutDao().getById(
											CotFinacegiven.class,
											finaceOther.getFkId());
							if (finacerecv != null) {
								CotFinacegiven clone = (CotFinacegiven) SystemUtil
										.deepClone(finacerecv);
								clone.setCotFinacegivenDetails(null);
								clone.setRemainAmount(all
										/ curRemain.getCurRate());
								recv.add(clone);
							}
						}
					}
				}
			}
			this.getOrderOutDao().deleteRecordByIds(ids, "CotFinaceOther");
			if (list.size() > 0) {
				this.getOrderOutDao().updateRecords(list);
			}
			if (listRecv.size() > 0) {
				this.getOrderOutDao().updateRecords(listRecv);
			}
			if (listYi.size() > 0) {
				this.getOrderOutDao().updateRecords(listYi);
				this.getOrderOutDao().updateRecords(recv);
			}
			return 0f;
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 查询出货单的应收帐,如果存在应收帐则删除旧单
	public Boolean deleteAccountrecvByFkId(Integer fkId) {
		CotFinaceAccountrecv CotFinaceAccountrecv = new CotFinaceAccountrecv();
		return true;
	}

	// 保存应收帐款
	public void saveAccountRecv(CotFinaceAccountrecv recvDetail,
			String otherIds, String amountDate) {
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
			recvDetail.setEmpId(empId);
			records.add(recvDetail);
			this.getOrderOutDao().saveOrUpdateRecords(records);
			// 保存单号
			CotSeqService seq = new CotSeqServiceImpl();
			seq.saveSeq("fincaeaccountrecv");
			if (!"".equals(otherIds)) {
				// 修改出货其他费用的状态
				String hql = "from CotFinaceOther obj where obj.id in ("
						+ otherIds.substring(0, otherIds.length() - 1) + ")";
				List<CotFinaceOther> list = this.getOrderOutDao().find(hql);
				List<CotFinaceOther> listNew = new ArrayList<CotFinaceOther>();
				for (int i = 0; i < list.size(); i++) {
					CotFinaceOther finaceOther = (CotFinaceOther) list.get(i);
					finaceOther.setStatus(1);
					listNew.add(finaceOther);
				}
				this.getOrderOutDao().updateRecords(listNew);
			}

			// 如果出货明细对应的订单的预收货款没有冲帐记录,直接删除
			// 1.先查询出对应的订单
			String sql = "select detail.orderNoid from CotOrderOut obj,CotOrderOutdetail detail "
					+ "where detail.orderId=obj.id and obj.id="
					+ recvDetail.getFkId();
			List listOrder = this.getOrderOutDao().find(sql);
			if (listOrder != null && listOrder.size() > 0) {
				String ids = "";
				for (int i = 0; i < listOrder.size(); i++) {
					Integer orderId = (Integer) listOrder.get(i);
					ids += orderId + ",";
				}
				// 2.删除预收货款
				if (!"".equals(ids)) {
					String temp = "select obj.id  from CotFinaceAccountrecv obj where obj.realAmount=0 and"
							+ " (obj.zhRemainAmount is null or obj.zhRemainAmount=0) "
							+ "and obj.finaceName='预收货款' and obj.source='order' "
							+ "and obj.fkId in ("
							+ ids.substring(0, ids.length() - 1) + ")";
					List delIds = this.getOrderOutDao().find(temp);
					if (delIds != null && delIds.size() > 0) {
						this.getOrderOutDao().deleteRecordByIds(delIds,
								"CotFinaceAccountrecv");
					}
				}
			}
		} catch (Exception e) {
			logger.error("保存应收帐款异常", e);
		}
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

	// 根据条件查询溢收款记录
	public List<?> getYiList(List<?> list) {
		List<CotFinaceOtherVO> listVo = new ArrayList<CotFinaceOtherVO>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotFinaceOtherVO vo = new CotFinaceOtherVO();
			vo.setId((Integer) obj[0]);
			vo.setFinaceName((String) obj[1]);
			vo.setAmount((Double) obj[2]);
			vo.setCurrencyId((Integer) obj[3]);
			vo.setCustomerShortName((String) obj[4]);
			vo.setFinaceNo((String) obj[5]);
			listVo.add(vo);
		}
		return listVo;
	}

	// 删除收款记录
	public Boolean deleteByRecvDetail(List<Integer> ids) {
		Integer mainId = 0;
		float allAmou = 0f;
		for (int i = 0; i < ids.size(); i++) {
			CotFinacerecvDetail finacerecvDetail = (CotFinacerecvDetail) this
					.getOrderOutDao().getById(CotFinacerecvDetail.class,
							ids.get(i));
			mainId = finacerecvDetail.getFinaceRecvid();
			allAmou += finacerecvDetail.getCurrentAmount();
			// 1.将货款转回应收帐
			CotFinaceAccountrecv acOld = (CotFinaceAccountrecv) this
					.getOrderOutDao().getById(CotFinaceAccountrecv.class,
							finacerecvDetail.getRecvId());
			CotFinaceAccountrecv ac = (CotFinaceAccountrecv) SystemUtil
					.deepClone(acOld);
			// 币种转换
			float mon = this.updatePrice(finacerecvDetail.getCurrentAmount()
					.floatValue(), finacerecvDetail.getCurrencyId(), ac
					.getCurrencyId());
			ac.setRealAmount(ac.getRealAmount() - mon);
			ac.setRemainAmount(ac.getRemainAmount() + mon);
			ac.setStatus(0);
			if ("预收货款".equals(ac.getFinaceName())) {
				ac.setZhRemainAmount(ac.getZhRemainAmount() - mon);
			} else {
				ac.setZhRemainAmount(ac.getZhRemainAmount() + mon);
			}

			List<CotFinaceAccountrecv> accountList = new ArrayList<CotFinaceAccountrecv>();
			accountList.add(ac);
			this.getOrderOutDao().updateRecords(accountList);
		}
		// 2.修改主单的未冲帐金额
		List<CotFinacerecv> recvList = new ArrayList<CotFinacerecv>();
		CotFinacerecv recvOld = (CotFinacerecv) this.getOrderOutDao().getById(
				CotFinacerecv.class, mainId);
		CotFinacerecv recv = (CotFinacerecv) SystemUtil.deepClone(recvOld);
		recv.setRemainAmount(recv.getRemainAmount() + allAmou);
		recvList.add(recv);

		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");

		// 查询该收款记录是否已存在溢收款,没有的新建
		String hql = " from CotFinaceOther obj where obj.finaceName='溢收款' and obj.fkId="
				+ recv.getId() + " and obj.factoryId=" + recv.getCustId();
		List list = this.getOrderOutDao().find(hql);
		List listNew = new ArrayList();
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
		this.getOrderOutDao().saveOrUpdateRecords(listNew);

		try {
			this.getOrderOutDao().deleteRecordByIds(ids, "CotFinacerecvDetail");
			this.getOrderOutDao().updateRecords(recvList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 删除付款记录
	public Boolean deleteByDealDetail(List<Integer> ids) {
		Integer mainId = 0;
		float allAmou = 0f;
		for (int i = 0; i < ids.size(); i++) {
			CotFinacegivenDetail detail = (CotFinacegivenDetail) this
					.getOrderOutDao().getById(CotFinacegivenDetail.class,
							ids.get(i));
			mainId = detail.getFinaceGivenid();
			allAmou += detail.getCurrentAmount();
			// 1.将货款转回应付帐
			CotFinaceAccountdeal acOld = (CotFinaceAccountdeal) this
					.getOrderOutDao().getById(CotFinaceAccountdeal.class,
							detail.getDealId());
			CotFinaceAccountdeal ac = (CotFinaceAccountdeal) SystemUtil
					.deepClone(acOld);
			// 币种转换
			float mon = this.updatePrice(
					detail.getCurrentAmount().floatValue(), detail
							.getCurrencyId(), ac.getCurrencyId());
			ac.setRealAmount(ac.getRealAmount() - mon);
			ac.setRemainAmount(ac.getRemainAmount() + mon);
			ac.setStatus(0);
			if ("预付货款".equals(ac.getFinaceName())) {
				ac.setZhRemainAmount(ac.getZhRemainAmount() - mon);
			} else {
				ac.setZhRemainAmount(ac.getZhRemainAmount() + mon);
			}

			List<CotFinaceAccountdeal> accountList = new ArrayList<CotFinaceAccountdeal>();
			accountList.add(ac);
			this.getOrderOutDao().updateRecords(accountList);
		}
		// 2.修改主单的未冲帐金额
		List<CotFinacegiven> recvList = new ArrayList<CotFinacegiven>();
		CotFinacegiven recvOld = (CotFinacegiven) this.getOrderOutDao()
				.getById(CotFinacegiven.class, mainId);
		CotFinacegiven recv = (CotFinacegiven) SystemUtil.deepClone(recvOld);
		recv.setRemainAmount(recv.getRemainAmount() + allAmou);
		recvList.add(recv);

		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");

		// 3.查询该付款记录是否已存在溢付款,没有的新建
		String hql = " from CotFinaceOther obj where obj.finaceName='溢付款' and obj.fkId="
				+ mainId + " and obj.factoryId=" + recv.getFactoryId();
		List list = this.getOrderOutDao().find(hql);
		List listNew = new ArrayList();
		CotFinaceOther finaceOther = null;
		if (list.size() == 0) {
			finaceOther = new CotFinaceOther();
			finaceOther.setAmount(recv.getRemainAmount());
			finaceOther.setBusinessPerson(empId);
			finaceOther.setCurrencyId(recv.getCurrencyId());
			finaceOther.setFactoryId(recv.getFactoryId());
			finaceOther.setFinaceName("溢付款");
			finaceOther.setFlag("M");
			finaceOther.setOrderNo(recv.getFinaceNo());
			finaceOther.setFkId(recv.getId());
		} else {
			finaceOther = (CotFinaceOther) list.get(0);
			finaceOther.setAmount(recv.getRemainAmount());
		}
		listNew.add(finaceOther);
		this.getOrderOutDao().saveOrUpdateRecords(listNew);

		try {
			this.getOrderOutDao()
					.deleteRecordByIds(ids, "CotFinacegivenDetail");
			this.getOrderOutDao().updateRecords(recvList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 删除应收帐
	public Boolean deleteByAccount(Integer accountId, Integer mainId) {
		try {
			List<Integer> list = new ArrayList<Integer>();
			list.add(accountId);
			this.getOrderOutDao().deleteRecordByIds(list,
					"CotFinaceAccountrecv");
			String hql = "from CotFinaceOther obj where obj.source in ('orderout','orderOther','orderRecv','yi') and obj.status=1 and obj.fkId="
					+ mainId;
			List<?> listOther = this.getOrderOutDao().find(hql);
			List<CotFinaceOther> listNew = new ArrayList<CotFinaceOther>();
			for (int i = 0; i < listOther.size(); i++) {
				CotFinaceOther cotFinaceOther = (CotFinaceOther) listOther
						.get(i);
				cotFinaceOther.setStatus(0);
				listNew.add(cotFinaceOther);
			}
			this.getOrderOutDao().updateRecords(listNew);
			return true;
		} catch (DAOException e) {
			return false;
		}
	}

	// 查询出货的应收帐
	public CotFinaceAccountrecv findAccountrecvByFkId(Integer fkId) {
		String hql = "from CotFinaceAccountrecv obj where obj.source='orderout' and obj.fkId="
				+ fkId;
		List<?> list = this.getOrderOutDao().find(hql);
		if (list.size() > 0) {
			return (CotFinaceAccountrecv) list.get(0);
		} else {
			return null;
		}
	}

	// 判断出货单是否已作废
	public int checkCanDelete(Integer orderOutId) {
		String splitHql = "select obj.id from CotOrderOutDel obj where obj.id="
				+ orderOutId;
		List<?> splitList = this.getOrderOutDao().find(splitHql);
		if (splitList.size() > 0) {
			return 1;
		}
		return 0;
	}

	// 判断出货单是否有应收帐,应付帐,是否被排载
	public List checkCanDeleteBatch(List<Integer> ids) {
		String str = "";
		for (int i = 0; i < ids.size(); i++) {
			str += ids.get(i) + ",";
		}
		String hql = "select obj.id from CotOrderOutDel obj where obj.id in ("
				+ str.substring(0, str.length() - 1) + ")";
		List<?> details = this.getOrderOutDao().find(hql);
		if (details.size() > 0) {
			ids.removeAll(details);
		}
		return ids;
	}

	// 修改出货审核状态
	public void updateOrderStatus(Integer orderId, Integer orderStatus) {
		CotOrderOut cotOrderOut = (CotOrderOut) this.getOrderOutDao().getById(
				CotOrderOut.class, orderId);
		cotOrderOut.setOrderStatus(orderStatus.longValue());
		try {
			this.getOrderOutDao().update(cotOrderOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit() {
		List<CotEleCfg> list = new ArrayList<CotEleCfg>();
		String hql = " from CotEleCfg obj ";
		list = this.getOrderOutDao().find(hql);
		if (list != null && list.size() > 0) {
			CotEleCfg cotEleCfg = (CotEleCfg) list.get(0);
			return cotEleCfg;
		} else {
			return null;
		}
	}

	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId) {
		return (CotBoxPacking) this.getOrderOutDao().getById(
				CotBoxPacking.class, boxPackingId);
	}

	// 计算价格
	public String calPrice(CotOrderOutdetail elements, Integer boxPackingId) {
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
	public String calPrice(CotOrderOuthsdetail elements, Integer boxPackingId) {
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
	public Float[] calPriceAllByOrderDetail(String detailId) {
		CotOrderOutdetail elements = this.getOrderMapValue(Integer
				.parseInt(detailId));
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
		if (elements.getElePrice() != null) {
			elePrice = elements.getElePrice();
		}
		if (elements.getEleFittingPrice() != null) {
			eleFittingPrice = elements.getEleFittingPrice();
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

	// 计算价格
	public Float[] calPriceAll(String detailId) {
		CotOrderOuthsdetail elements = this.getChaOrderMapValue(Integer
				.parseInt(detailId));
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
		if (elements.getElePrice() != null) {
			elePrice = elements.getElePrice();
		}
		if (elements.getEleFittingPrice() != null) {
			eleFittingPrice = elements.getEleFittingPrice();
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

	// 根据文件路径导入
	public List<?> saveReport(String filename, Integer orderId) {
		// 查询所有出货明细
		String hql = "from CotOrderOutdetail obj where obj.orderId=" + orderId;
		List listDetail = this.getOrderOutDao().find(hql);

		WebContext ctx = WebContextFactory.get();
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

		List listNew = new ArrayList();

		for (int k = 0; k < listDetail.size(); k++) {
			boolean rowCks = false;
			CotOrderOutdetail out = (CotOrderOutdetail) listDetail.get(k);
			for (int i = 4; i < sheet.getRows(); i++) {
				// 新建样品对象
				CotOrderOutdetail detail = null;
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

				// 单重
				float boxWeigth = 0f;
				float gWeigh = 0f;
				float nWeigh = 0f;

				// 厂价
				float priceFac = 0f;
				// 外销价
				float priceOut = 0f;

				// 用于计算装箱数
				long boxObCount = 0;

				Integer hsIdTemp = 0;
				float tuiLv = 0f;

				// 判断是否是子货号
				boolean isChild = false;

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
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "样品编号不能为空");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"样品编号长度不能大于100位");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}

						if (out.getEleId().toUpperCase().equals(
								rowCtn.trim().toUpperCase())) {
							detail = out;
						}
					}
					if (detail == null) {
						isSuccess = false;
						break;
					} else {
						if (detail.getBoxObCount() != null) {
							boxObCount = detail.getBoxObCount();
						}
						eleSize = detail.getEleSizeDesc();
						eleSizeInch = detail.getEleInchDesc();
					}

					if (headCtn.getContents().equals("HS_ID") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						// Integer flag = this.getReportDao().isExistHsId(
						// rowCtn.trim());
						Object hsId = mapHsCode
								.get(rowCtn.trim().toLowerCase());
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
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存海关编码异常");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setEleHsid(mapHsCode.get(rowCtn.trim()
								.toLowerCase()));
						hsIdTemp = mapHsCode.get(rowCtn.trim().toLowerCase());
					}
					if (headCtn.getContents().equals("ELE_NAME")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
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
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "父货号不存在!");
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
					if (headCtn.getContents().equals("CUST_NO")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setCustNo(rowCtn.trim());
					}
					if (headCtn.getContents().equals("FACTORY_NO")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setFactoryNo(rowCtn.trim());
					}
					if (headCtn.getContents().equals("ELE_DESC")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleDesc(rowCtn.trim());
					}
					if (headCtn.getContents().equals("ELE_FROM")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleFrom(rowCtn.trim());
					}
					if (headCtn.getContents().equals("ELE_FACTORY")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleFactory(rowCtn.trim());
					}
					if (headCtn.getContents().equals("ELE_COL")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleCol(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BARCODE")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						if (rowCtn.trim().length() > 30) {
							detail.setBarcode(rowCtn.trim().substring(0, 30));
						} else {
							detail.setBarcode(rowCtn.trim());
						}
					}
					if (headCtn.getContents().equals("ELE_UNIT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setEleUnit(rowCtn.trim());
						}
					}
					if (headCtn.getContents().equals("ELE_GRADE")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleGrade(rowCtn.trim());
					}
					if (headCtn.getContents().equals("ELE_FOR_PERSON")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleForPerson(rowCtn.trim());
					}
					if (headCtn.getContents().equals("ELE_SIZE_DESC")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						eleSize = rowCtn.trim();
					}
					if (headCtn.getContents().equals("TAO_UNIT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setTaoUnit(rowCtn.trim());
					}
					if (headCtn.getContents().equals("ELE_INCH_DESC")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						eleSizeInch = rowCtn.trim();
					}
					if (headCtn.getContents().equals("ELE_REMARK")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						if (rowCtn.trim().length() > 500) {
							detail
									.setEleRemark(rowCtn.trim().substring(0,
											500));
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
							Object factoryId = mapShortName.get(temp
									.toLowerCase());
							if (factoryId == null) {
								cotFactory.setFactoryName(temp);
								cotFactory.setShortName(temp);
								cotFactory.setFactroyTypeidLv1(1);
								try {
									CotSeqService cotSeqServiceImpl = new CotSeqServiceImpl();
									String facNo = cotSeqServiceImpl.getFacNo();

									// String facNo =
									// seq.getAllSeqByType("facNo",
									// null);
									cotFactory.setFactoryNo(facNo);
									this.getReportDao().create(cotFactory);
									cotSeqServiceImpl.saveSeq("factoryNo");
									// seq.saveSeq("facNo");
									// 将新类别添加到已有的map中
									mapShortName.put(temp.toLowerCase(),
											cotFactory.getId());
								} catch (Exception e) {
									CotMsgVO cotMsgVO = new CotMsgVO(i, j,
											"保存厂家异常");
									msgList.add(cotMsgVO);
									isSuccess = false;
									break;
								}
							}
							detail.setFactoryId(mapShortName.get(temp
									.toLowerCase()));
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
						if (headCtn.getContents().equals("cube")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setCube(Float.parseFloat(rowCtn.trim()));
						}
						if (headCtn.getContents().equals("BOX_L")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							float num = Float.parseFloat(rowCtn.trim());
							int temp = rowCtn.trim().lastIndexOf(".");
							if (temp > -1) {
								boxL = Float.parseFloat(dfTwo.format(num));
							} else {
								boxL = num;
							}
							boxLInch = Float.parseFloat(dfTwo
									.format(num / 2.54f));
							detail.setBoxL(boxL);
							detail.setBoxLInch(boxLInch);
						}
						if (headCtn.getContents().equals("BOX_L_INCH")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
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
						if (headCtn.getContents().equals("BOX_W")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							float num = Float.parseFloat(rowCtn.trim());
							int temp = rowCtn.trim().lastIndexOf(".");
							if (temp > -1) {
								boxW = Float.parseFloat(dfTwo.format(num));
							} else {
								boxW = num;
							}
							boxWInch = Float.parseFloat(dfTwo
									.format(num / 2.54f));
							detail.setBoxW(boxW);
							detail.setBoxWInch(boxWInch);
						}
						if (headCtn.getContents().equals("BOX_W_INCH")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
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
						if (headCtn.getContents().equals("BOX_H")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							float num = Float.parseFloat(rowCtn.trim());
							int temp = rowCtn.trim().lastIndexOf(".");
							if (temp > -1) {
								boxH = Float.parseFloat(dfTwo.format(num));
							} else {
								boxH = num;
							}
							boxHInch = Float.parseFloat(dfTwo
									.format(num / 2.54f));
							detail.setBoxH(boxH);
							detail.setBoxHInch(boxHInch);
						}
						if (headCtn.getContents().equals("BOX_H_INCH")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
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
						if (headCtn.getContents().equals("BOX_PB_L")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxPbL(Float.parseFloat(rowCtn.trim()));
							detail.setBoxPbLInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_PB_W")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxPbW(Float.parseFloat(rowCtn.trim()));
							detail.setBoxPbWInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_PB_H")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxPbH(Float.parseFloat(rowCtn.trim()));
							detail.setBoxPbHInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}
						if (headCtn.getContents().equals("BOX_PB_COUNT")) {
							if (rowCtn != null && !rowCtn.trim().equals("")) {
								Double elemod = Double.parseDouble(rowCtn
										.trim());
								detail.setBoxPbCount(elemod.longValue());
							}
						}
						if (headCtn.getContents().equals("BOX_IB_L")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxIbL(Float.parseFloat(rowCtn.trim()));
							detail.setBoxIbLInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_IB_W")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxIbW(Float.parseFloat(rowCtn.trim()));
							detail.setBoxIbWInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_IB_H")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxIbH(Float.parseFloat(rowCtn.trim()));
							detail.setBoxIbHInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_MB_L")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxMbL(Float.parseFloat(rowCtn.trim()));
							detail.setBoxMbLInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_MB_W")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxMbW(Float.parseFloat(rowCtn.trim()));
							detail.setBoxMbWInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_MB_H")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setBoxMbH(Float.parseFloat(rowCtn.trim()));
							detail.setBoxMbHInch(Float
									.parseFloat(rowCtn.trim()) / 2.54f);
						}

						if (headCtn.getContents().equals("BOX_OB_L")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							boxObL = Float.parseFloat(rowCtn.trim());
							detail.setBoxObL(boxObL);
							detail.setBoxObLInch(boxObL / 2.54f);

							float cbm = Float.parseFloat(df.format(boxObL
									* boxObW * boxObH * 0.000001F));
							float cuft = Float.parseFloat(df
									.format(cbm * 35.315f));
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
							float cbm = Float.parseFloat(df.format(boxObL
									* boxObW * boxObH * 0.000001F));
							float cuft = Float.parseFloat(df
									.format(cbm * 35.315f));
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

							float cbm = Float.parseFloat(df.format(boxObL
									* boxObW * boxObH * 0.000001F));
							float cuft = Float.parseFloat(df
									.format(cbm * 35.315f));
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
							detail
									.setBoxWeigth(Float.parseFloat(rowCtn
											.trim()));
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
								Double elemod = Double.parseDouble(rowCtn
										.trim());
								detail.setBoxIbCount(elemod.longValue());
							}
						}
						if (headCtn.getContents().equals("BOX_MB_COUNT")) {
							if (rowCtn != null && !rowCtn.trim().equals("")) {
								Double elemod = Double.parseDouble(rowCtn
										.trim());
								detail.setBoxMbCount(elemod.longValue());
							}
						}
						if (headCtn.getContents().equals("BOX_OB_COUNT")) {
							if (rowCtn != null && !rowCtn.trim().equals("")) {
								Double elemod = Double.parseDouble(rowCtn
										.trim());
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
									detail
											.setBox40hqCount(new Float(
													count40hq));
									detail.setBox45Count(new Float(count45));
								}
							}
						}

						if (headCtn.getContents().equals("TUI_LV")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							tuiLv = Float.parseFloat(rowCtn.trim());
							detail.setTuiLv(tuiLv);
							if (hsIdTemp != 0) {
								CotEleOther cotEleOther = (CotEleOther) this
										.getReportDao().getById(
												CotEleOther.class, hsIdTemp);
								cotEleOther.setTaxRate(tuiLv);
								List list = new ArrayList();
								list.add(cotEleOther);
								this.getReportDao().updateRecords(list);
							}
						}

						if (headCtn.getContents().equals("PRICE_FAC")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							priceFac = Float.parseFloat(rowCtn.trim());
							detail.setPriceFac(priceFac);
						}
						if (headCtn.getContents().equals("PRICE_OUT")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							priceOut = Float.parseFloat(rowCtn.trim());
							detail.setPriceOut(priceOut);
						}
						if (headCtn.getContents().equals("BOX_CBM")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							float cbm = Float.parseFloat(rowCtn.trim());
							float cuft = Float.parseFloat(df
									.format(cbm * 35.315f));
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
							detail.setBoxCount(Long.parseLong(rowCtn.trim()));
						}
						if (headCtn.getContents().equals("CONTAINER_COUNT")
								&& rowCtn != null && !rowCtn.trim().equals("")) {
							detail.setContainerCount(Long.parseLong(rowCtn
									.trim()));
						}
					} catch (Exception e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "导入时候数字转化异常");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					if (headCtn.getContents().equals("BOX_UINT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxUint(rowCtn.trim());
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
											"保存新包装类型错误!");
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
										"此货号币种没有定义，请先定义后再导入");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
							detail.setPriceFacUint(mapCurrency.get(rowCtn
									.trim().toLowerCase()));
						}
					}

					if (headCtn.getContents().equals("PRICE_UNIT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Object curId = mapCurrency.get(rowCtn.trim()
									.toLowerCase());
							if (curId == null) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"此货号币种没有定义，请先定义后再导入");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
							detail.setPriceOutUint(mapCurrency.get(rowCtn
									.trim().toLowerCase()));
						}
					}
				}
				if (isSuccess == true) {
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
						detail.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
					}
					if (!eleSizeInch.equals("")) {
						detail.setEleInchDesc(eleSizeInch);
					} else {
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
					if (detail.getBoxCount() != 0) {
						if (detail.getBoxObCount() != null) {
							if (detail.getBoxCount() % detail.getBoxObCount() != 0) {
								Double cc = Math.ceil(detail.getBoxCount()
										.floatValue()
										/ detail.getBoxObCount());
								detail.setContainerCount(cc.longValue());
							} else {
								detail.setContainerCount(detail.getBoxCount()
										/ detail.getBoxObCount());
							}
						}
					}

					// 设置毛净重
					if (gWeigh == 0) {
						Float cfgGross = 0f;
						if (cotEleCfg != null
								&& cotEleCfg.getGrossNum() != null) {
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

					// 重新计算箱数

					listNew.add(detail);
					// 增加成功条数
					successNum++;
				}
			}
		}

		if (listNew.size() > 0) {
			this.getOrderOutDao().updateRecords(listNew);
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
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		file.delete();
		return msgList;

	}

	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String rdm, String packingPrice) {
		CotOrderOuthsdetail detail = this.getChaOrderMapValue(Integer
				.parseInt(rdm));
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
				this.setChaOrderMap(Integer.parseInt(rdm), detail);
			} catch (EvaluationException e) {
				e.printStackTrace();
				return -1f;
			}
			return res;
		} else {
			return -1f;
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
		GridServerHandler gd = null;
		if (queryInfo.getExcludes() != null)
			gd = new GridServerHandler(queryInfo.getExcludes());
		else
			gd = new GridServerHandler();
		int count = this.getOrderOutDao().getRecordsCount(queryInfo);
		List res = this.getOrderOutDao().findRecords(queryInfo);
		Iterator<?> it = res.iterator();
		while (it.hasNext()) {
			CotOrderOutdetail cotOrderDetail = (CotOrderOutdetail) it.next();
			cotOrderDetail.setPicImg(null);
			this.setMapAction(session, cotOrderDetail.getOrderDetailId(),
					cotOrderDetail);
		}

		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
	}

	// 查询含有rdm的报价记录
	public String getListChaData(HttpServletRequest request, QueryInfo queryInfo)
			throws DAOException {
		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		GridServerHandler gd = null;
		if (queryInfo.getExcludes() != null)
			gd = new GridServerHandler(queryInfo.getExcludes());
		else
			gd = new GridServerHandler();
		int count = this.getOrderOutDao().getRecordsCount(queryInfo);
		List res = this.getOrderOutDao().findRecords(queryInfo);
		Iterator<?> it = res.iterator();
		while (it.hasNext()) {
			CotOrderOuthsdetail cotOrderDetail = (CotOrderOuthsdetail) it
					.next();
			cotOrderDetail.setPicImg(null);
			this.setChaMapAction(session, cotOrderDetail.getOrderDetailId(),
					cotOrderDetail);
		}

		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
	}

	// 查询含有rdm的报价记录
	// public String getListChaDelData(HttpServletRequest request, QueryInfo
	// queryInfo)
	// throws DAOException {
	// // 将明细存到内存中的map
	// HttpSession session = request.getSession();
	// GridServerHandler gd = null;
	// if (queryInfo.getExcludes() != null)
	// gd = new GridServerHandler(queryInfo.getExcludes());
	// else
	// gd = new GridServerHandler();
	// int count = this.getOrderOutDao().getRecordsCount(queryInfo);
	// List res = this.getOrderOutDao().findRecords(queryInfo);
	// Iterator<?> it = res.iterator();
	// while (it.hasNext()) {
	// CotOrderOuthsdetailDel cotOrderDetail = (CotOrderOuthsdetailDel) it
	// .next();
	// cotOrderDetail.setPicImg(null);
	// this.setChaDelMapAction(session, cotOrderDetail.getOrderDetailId(),
	// cotOrderDetail);
	// }
	//
	// gd.setData(res);
	// gd.setTotalCount(count);
	// return gd.getLoadResponseText();
	// }

	// 删除唛标明细图片
	public boolean deleteMBPicImg(Integer orderId) {
		String classPath = CotSignServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		String hql = "from CotOrderoutMb obj where obj.fkId=" + orderId;
		List list = this.getOrderOutDao().find(hql);
		CotOrderoutMb cotOrderMb = (CotOrderoutMb) list.get(0);

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
			this.getOrderOutDao().update(cotOrderMb);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除唛标图片错误!");
			return false;
		}
	}

	// 判断订单的其他费用是否已导入过出货
	public boolean findIsExistOther(Integer orderOutId, Integer otherId) {
		String hql = "from CotFinaceOther obj where obj.type=0 and obj.source='orderOther' and obj.fkId="
				+ orderOutId + " and obj.outFlag=" + otherId;
		List list = this.getOrderOutDao().find(hql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 判断订单的应收帐是否已导入过出货
	public boolean findIsExistRecv(Integer orderOutId, Integer recvId) {
		String hql = "from CotFinaceOther obj where obj.type=0 and obj.source='orderRecv' and obj.fkId="
				+ orderOutId + " and obj.outFlag=" + recvId;
		List list = this.getOrderOutDao().find(hql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 判断客户的溢收款是否已导入过出货
	public boolean findIsExistYi(Integer orderOutId, Integer yiId) {
		String hql = "from CotFinaceOther obj where obj.type=0 and obj.source='yi' and obj.fkId="
				+ orderOutId + " and obj.outFlag=" + yiId;
		List list = this.getOrderOutDao().find(hql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 重新排序出货明细
	public boolean updateSortNo(Integer type, String field, String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<Integer, CotOrderOutdetail> orderMap = this.getMapAction(ctx
				.getSession());
		List list = new ArrayList();
		Iterator<?> it = orderMap.keySet().iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			CotOrderOutdetail detail = orderMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotOrderOutdetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotOrderOutdetail detail = (CotOrderOutdetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getOrderOutDao().updateRecords(listNew);
		return true;
	}

	// 重新排序报关明细
	public boolean updateSortNoBao(Integer type, String field, String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<Integer, CotOrderOuthsdetail> orderMap = this.getChaMapAction(ctx
				.getSession());
		List list = new ArrayList();
		Iterator<?> it = orderMap.keySet().iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			CotOrderOuthsdetail detail = orderMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotOrderOuthsdetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotOrderOuthsdetail detail = (CotOrderOuthsdetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getOrderOutDao().updateRecords(listNew);
		return true;
	}

	// 查询VO记录
	public List<?> getOrderDetailVOList(QueryInfo queryInfo) {
		List<CotOrderOutdetail> listVo = new ArrayList<CotOrderOutdetail>();
		try {
			List<?> list = this.getOrderOutDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				CotOrderOutdetail detail = (CotOrderOutdetail) obj[0];
				Float temp = (Float) obj[1];
				detail.setUnSendNum(temp.intValue());
				detail.setOrderNo((String) obj[2]);
				detail.setPoNo((String) obj[3]);
				listVo.add(detail);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 判断出货应收款其他费用是否存在
	public boolean findIsExistName(String name, Integer orderId, Integer recId) {
		String hql = "select obj.id from CotFinaceOther obj where obj.fkId=? and obj.source='orderout' and obj.finaceName=?";
		Object[] obj = new Object[2];
		obj[0] = orderId;
		obj[1] = name;
		List list = this.getOrderOutDao().queryForLists(hql, obj);
		if (list != null && list.size() > 0) {
			Integer tId = (Integer) list.get(0);
			if (tId.intValue() != recId.intValue()) {
				return true;
			}
		}
		return false;
	}

	// 判断出货应付款其他费用是否存在
	public boolean findIsExistNameDeal(String name, Integer orderId,
			Integer recId) {
		String hql = "select obj.id from CotFinaceOther obj where obj.fkId=? and obj.source='orderfacToOut' and obj.finaceName=?";
		Object[] obj = new Object[2];
		obj[0] = orderId;
		obj[1] = name;
		List list = this.getOrderOutDao().queryForLists(hql, obj);
		if (list != null && list.size() > 0) {
			Integer tId = (Integer) list.get(0);
			if (tId.intValue() != recId.intValue()) {
				return true;
			}
		}
		return false;
	}

	// 更改应付帐的剩余金额
	public void updateFacDeal(HttpServletRequest request, List accReal) {
		// 获得币种
		Map<Integer, CotCurrency> map = getCurrencyObjMap(request);
		List<CotFinaceAccountdeal> other = new ArrayList<CotFinaceAccountdeal>();
		for (int i = 0; i < accReal.size(); i++) {
			// 其他费用金额的RMB值
			CotFinaceOther finaceOther = (CotFinaceOther) accReal.get(i);
			CotCurrency cur = map.get(finaceOther.getCurrencyId());
			Double realRmb = cur.getCurRate() * finaceOther.getAmount();
			// 对应的应收帐的未流转金额的RMB值
			CotFinaceAccountdeal fin = (CotFinaceAccountdeal) this
					.getOrderOutDao().getById(CotFinaceAccountdeal.class,
							finaceOther.getOutFlag());
			CotCurrency curFin = map.get(fin.getCurrencyId());
			Double zhRemainRmb = curFin.getCurRate() * fin.getZhRemainAmount();
			Double rmb = zhRemainRmb - realRmb;
			Double money = rmb / curFin.getCurRate();
			fin.setZhRemainAmount(money);
			other.add(fin);
		}
		this.getOrderOutDao().updateRecords(other);
	}

	// 查询订单到出货的可导入费用余额
	public Double findMaxMoney(Integer curId, String source, Integer outFlag) {
		if (source.equals("orderOther")) {
			CotFinaceOther srcOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", outFlag);
			if (curId.intValue() != srcOther.getCurrencyId().intValue()) {
				CotCurrency newCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, curId);
				CotCurrency srcCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, srcOther.getCurrencyId());

				return (srcOther.getRemainAmount() * srcCur.getCurRate())
						/ newCur.getCurRate();
			} else {
				return srcOther.getRemainAmount();
			}

		}

		if (source.equals("orderRecv")) {
			CotFinaceAccountrecv srcOther = (CotFinaceAccountrecv) this
					.getOrderOutDao().getObjectById("CotFinaceAccountrecv",
							outFlag);
			if (curId.intValue() != srcOther.getCurrencyId().intValue()) {
				CotCurrency newCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, curId);
				CotCurrency srcCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, srcOther.getCurrencyId());

				return (srcOther.getZhRemainAmount() * srcCur.getCurRate())
						/ newCur.getCurRate();
			} else {
				return srcOther.getZhRemainAmount();
			}

		}

		if (source.equals("yi")) {
			CotFinaceOther srcOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", outFlag);
			if (curId.intValue() != srcOther.getCurrencyId().intValue()) {
				CotCurrency newCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, curId);
				CotCurrency srcCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, srcOther.getCurrencyId());

				return (srcOther.getAmount() * srcCur.getCurRate())
						/ newCur.getCurRate();
			} else {
				return srcOther.getAmount();
			}

		}
		return null;
	}

	// 查询订单到出货的可导入费用余额
	public Double findMaxMoneyDeal(Integer curId, String source, Integer outFlag) {
		if (source.equals("FacOther") || source.equals("FitOther")
				|| source.equals("PackOther")) {
			CotFinaceOther srcOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", outFlag);
			if (curId.intValue() != srcOther.getCurrencyId().intValue()) {
				CotCurrency newCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, curId);
				CotCurrency srcCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, srcOther.getCurrencyId());

				return (srcOther.getRemainAmount() * srcCur.getCurRate())
						/ newCur.getCurRate();
			} else {
				return srcOther.getRemainAmount();
			}

		}

		if (source.equals("FacDeal") || source.equals("FitDeal")
				|| source.equals("PackDeal")) {
			CotFinaceAccountdeal srcOther = (CotFinaceAccountdeal) this
					.getOrderOutDao().getObjectById("CotFinaceAccountdeal",
							outFlag);
			if (curId.intValue() != srcOther.getCurrencyId().intValue()) {
				CotCurrency newCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, curId);
				CotCurrency srcCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, srcOther.getCurrencyId());

				return (srcOther.getZhRemainAmount() * srcCur.getCurRate())
						/ newCur.getCurRate();
			} else {
				return srcOther.getZhRemainAmount();
			}

		}

		if (source.equals("yiDeal")) {
			CotFinaceOther srcOther = (CotFinaceOther) this.getOrderOutDao()
					.getObjectById("CotFinaceOther", outFlag);
			if (curId.intValue() != srcOther.getCurrencyId().intValue()) {
				CotCurrency newCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, curId);
				CotCurrency srcCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, srcOther.getCurrencyId());

				return (srcOther.getAmount() * srcCur.getCurRate())
						/ newCur.getCurRate();
			} else {
				return srcOther.getAmount();
			}

		}
		return null;
	}

	// 查找原始费用值
	public Double findOldVal(Integer curId, Integer finaceOtherId) {
		CotFinaceOther ft = (CotFinaceOther) this.getOrderOutDao().getById(
				CotFinaceOther.class, finaceOtherId);
		if (ft != null) {
			if (curId.intValue() != ft.getCurrencyId().intValue()) {
				CotCurrency newCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, curId);
				CotCurrency srcCur = (CotCurrency) this.getOrderOutDao()
						.getById(CotCurrency.class, ft.getCurrencyId());

				return (ft.getAmount() * srcCur.getCurRate())
						/ newCur.getCurRate();
			} else {
				return ft.getAmount();
			}
		} else {
			return null;
		}
	}

	// 保存出货单的应付帐款
	public boolean saveDeal(Integer mainId) {
		// 获得出货主单
		CotOrderOut orderOut = this.getOrderOutById(mainId);
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		Map<Integer, CotCurrency> curMap = this.getCurrencyObjMap(ctx
				.getHttpServletRequest());
		// 获得rmb币种编号
		String rmbCur = "select obj.id from CotCurrency obj where obj.curNameEn='RMB'";
		List<?> curList = this.getOrderOutDao().find(rmbCur);
		Integer rmbId = null;
		if (curList.size() > 0) {
			rmbId = (Integer) curList.get(0);
		}

		// 出货主单币种
		// CotCurrency curMain = curMap.get(orderOut.getCurrencyId());
		// 查询该出货单的所有其他费用
		String otherHql = "from CotFinaceOther obj "
				+ "where obj.source in ('orderfacToOut','FacMoney','FitMoney','PackMoney','FacDeal','FitDeal','PackDeal',"
				+ "'FacOther','FitOther','PackOther'," + "'yiDeal') "
				+ "and obj.fkId=" + mainId;
		List<Integer> otherList = this.getOrderOutDao().find(otherHql);
		List<CotFinaceOther> otUpList = new ArrayList<CotFinaceOther>();
		Map<Integer, Double> facMap = new HashMap<Integer, Double>();
		// 将相同厂家的费用累加起来
		Iterator<?> it = otherList.iterator();
		while (it.hasNext()) {
			CotFinaceOther finaceOther = (CotFinaceOther) it.next();
			int flag = 1;
			if (finaceOther.getFlag().equals("M")) {
				flag = -1;
			}
			Double rmbMon = facMap.get(finaceOther.getFactoryId());
			CotCurrency cur = curMap.get(finaceOther.getCurrencyId());
			Double rm = finaceOther.getAmount() * cur.getCurRate() * flag;
			if (rmbMon == null) {
				facMap.put(finaceOther.getFactoryId(), rm);
			} else {
				facMap.put(finaceOther.getFactoryId(), rmbMon + rm);
			}
			finaceOther.setStatus(1);
			otUpList.add(finaceOther);
		}
		List<CotFinaceAccountdeal> accList = new ArrayList<CotFinaceAccountdeal>();
		// 查询该出货单的所有应付款
		String hql = "from CotFinaceAccountdeal obj where obj.source='orderout' and obj.fkId="
				+ mainId;
		List dealList = this.getOrderOutDao().find(hql);
		for (Integer facId : facMap.keySet()) {
			Double amount = facMap.get(facId);
			boolean isEx = false;
			CotFinaceAccountdeal deal = new CotFinaceAccountdeal();
			// 判断该厂家是否已生成应付款
			for (int i = 0; i < dealList.size(); i++) {
				CotFinaceAccountdeal finaceAccountdeal = (CotFinaceAccountdeal) dealList
						.get(i);
				if (finaceAccountdeal.getFactoryId().intValue() == facId
						.intValue()) {
					isEx = true;
					deal = finaceAccountdeal;
					break;
				}
			}
			if (isEx == false) {
				// 获得单号
				// Map idMap = new HashMap<String, Integer>();
				// idMap.put("CH", facId);
				// GenAllSeq seq = new GenAllSeq();
				// String finaceNo = seq.getAllSeqByType("fincaeaccountdealNo",
				// idMap);
				CotSeqService seq = new CotSeqServiceImpl();
				String finaceNo = seq.getFinaceNeeGivenNo(facId);

				deal.setFinaceNo(finaceNo);
				deal.setFinaceName("出货应付");
				deal.setAmount(amount);
				deal.setRealAmount(0.0);
				deal.setRemainAmount(amount);
				deal.setCurrencyId(rmbId);
				deal.setFactoryId(facId);
				deal.setSource("orderout");
				deal.setOrderNo(orderOut.getOrderNo());
				deal.setFkId(mainId);
				deal.setBusinessPerson(orderOut.getBusinessPerson());
				deal.setStatus(0);
				deal.setEmpId(empId);
				deal.setAmountDate(orderOut.getOrderTime());
				deal.setAddDate(new java.util.Date());
				deal.setCompanyId(orderOut.getCompanyId());
				deal.setZhRemainAmount(amount);
				// 保存单号
				this.savaSeq();
				accList.add(deal);
			} else {
				Double oldAmount = deal.getAmount();
				Double cha = amount - oldAmount;
				deal.setAmount(amount);
				deal.setRemainAmount(deal.getRemainAmount() + cha);
				deal.setAddDate(new java.util.Date());
				deal.setZhRemainAmount(deal.getZhRemainAmount() + cha);
				accList.add(deal);
			}
		}
		try {
			this.getOrderOutDao().saveOrUpdateRecords(accList);
			// 更新其他费用的状态
			this.getOrderOutDao().updateRecords(otherList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 自动保存
	public void saveOthers(Integer orderDetailId, Integer orderoutId) {

		String hql = " from CotOrderFacdetail obj where obj.orderDetailId = "
				+ orderDetailId;
		List<?> res = this.getOrderOutDao().find(hql);
		for (int i = 0; i < res.size(); i++) {
			CotOrderFacdetail detail = (CotOrderFacdetail) res.get(i);
			this.saveOrderFacOtherAuto(detail.getId(), orderoutId);
		}
	}

	// 插入一条出货其他费用--配件--未指定数量直接生成时
	public void saveFitOthers(Integer detailId, Integer orderoutId) {

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<?> cfglist = this.getList("CotPriceCfg");
		CotPriceCfg cfg = new CotPriceCfg();
		if (cfglist.size() == 1) {
			cfg = (CotPriceCfg) cfglist.get(0);
		}

		List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();

		CotFittingsOrderdetail fitorderdetail = (CotFittingsOrderdetail) this
				.getOrderOutDao().getById(CotFittingsOrderdetail.class,
						detailId);
		// 获取对应的主单信息
		CotFittingOrder fittingOrder = (CotFittingOrder) this.getOrderOutDao()
				.getById(CotFittingOrder.class, fitorderdetail.getOrderId());

		List<?> otherList = this.getOrderOutDao().find(
				" from CotFinaceOther obj where obj.outFlag="
						+ fittingOrder.getId() + " and obj.source='FitMoney'");

		CotFinaceOther other = new CotFinaceOther();

		if (otherList.size() == 1) {
			other = (CotFinaceOther) otherList.get(0);
			other.setAmount(other.getAmount() + fitorderdetail.getOutHasOut()
					* fitorderdetail.getFitPrice());
		} else {
			Double amount = fitorderdetail.getOutHasOut()
					* fitorderdetail.getFitPrice();
			other.setAmount(amount.doubleValue());
			other.setCurrencyId(cfg.getFacPriceUnit());
			other.setFactoryId(fittingOrder.getFactoryId());
			other.setStatus(0);
			other.setFinaceName("配件货款");
			other.setFkId(orderoutId);
			other.setOrderNo(fittingOrder.getFittingOrderNo());
			other.setFlag("A");
			other.setOutFlag(fittingOrder.getId());
			other.setSource("FitMoney");
			other.setType(1);
			other.setRemainAmount(amount.doubleValue());
			other.setBusinessPerson(cotEmps.getId());
		}
		list.add(other);

		this.getOrderOutDao().saveOrUpdateRecords(list);

	}

	// 插入一条出货其他费用--包材--未指定数量直接生成时
	public void savePackOthers(Integer detailId, Integer orderoutId) {

		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<?> cfglist = this.getList("CotPriceCfg");
		CotPriceCfg cfg = new CotPriceCfg();
		if (cfglist.size() == 1) {
			cfg = (CotPriceCfg) cfglist.get(0);
		}

		List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();

		CotPackingOrderdetail packorderdetail = (CotPackingOrderdetail) this
				.getOrderOutDao()
				.getById(CotPackingOrderdetail.class, detailId);
		// 获取对应的主单信息
		CotPackingOrder packingOrder = (CotPackingOrder) this.getOrderOutDao()
				.getById(CotPackingOrder.class, packorderdetail.getOrderId());

		List<?> otherList = this.getOrderOutDao().find(
				" from CotFinaceOther obj where obj.outFlag="
						+ packingOrder.getId() + " and obj.source='PackMoney'");

		CotFinaceOther other = new CotFinaceOther();

		if (otherList.size() == 1) {
			other = (CotFinaceOther) otherList.get(0);
			other.setAmount(other.getAmount() + packorderdetail.getOutHasOut()
					* packorderdetail.getPackPrice());
			// other.setId(other.getId());
		} else {
			Double amount = packorderdetail.getOutHasOut()
					* packorderdetail.getPackPrice();
			other.setAmount(amount.doubleValue());
			other.setCurrencyId(cfg.getFacPriceUnit());
			other.setFactoryId(packingOrder.getFactoryId());
			other.setStatus(0);
			other.setFinaceName("包材货款");
			other.setFkId(orderoutId);
			other.setOrderNo(packingOrder.getPackingOrderNo());
			other.setFlag("A");
			other.setOutFlag(packingOrder.getId());
			other.setSource("PackMoney");
			other.setType(1);
			other.setRemainAmount(amount.doubleValue());
			other.setBusinessPerson(cotEmps.getId());
		}
		list.add(other);

		this.getOrderOutDao().saveOrUpdateRecords(list);

	}

	// 查询港口名
	public String[] findPortNameById(Integer sportId, Integer tportId) {
		String hql = "select obj.shipPortNameEn from CotShipPort obj where obj.id="
				+ sportId;
		String thql = "select obj.targetPortEnName from CotTargetPort obj where obj.id="
				+ tportId;
		List list = this.getOrderOutDao().find(hql);
		List tlist = this.getOrderOutDao().find(thql);

		String[] temp = new String[2];
		if (list != null && list.size() > 0) {
			temp[0] = (String) list.get(0);
		} else {
			temp[0] = "";
		}
		if (tlist != null && tlist.size() > 0) {
			temp[1] = (String) tlist.get(0);
		} else {
			temp[1] = "";
		}
		return temp;
	}

	// 查询未导入的应收款的条数
	public int findNoImportNum(Integer orderOutId, Integer custId) {
		String hql = "select distinct obj.orderNoid from CotOrderOutdetail obj where obj.orderId="
				+ orderOutId;
		List array = this.getOrderOutDao().find(hql);
		int temp = 0;
		Iterator<?> it = array.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='order'"
					+ " and (obj.outFlag is null or obj.outFlag = 0) and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				temp += list.size();
			}
			String dealHql = "select obj.id from CotFinaceAccountrecv obj where obj.source ='order'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				temp += dealList.size();
			}
		}
		// 查询客户的溢收款
		String yiHql = "select detail.id from CotFinaceOther detail,CotCustomer c "
				+ "where detail.factoryId=c.id and detail.finaceName='溢收款' and detail.amount!=0  and detail.factoryId="
				+ custId;
		List yiList = this.getOrderOutDao().find(yiHql);
		if (yiList != null && yiList.size() > 0) {
			temp += yiList.size();
		}
		return temp;
	}

	// 查询未导入的应收款的编号
	public String[] findNoImportAll(Integer orderOutId, Integer custId) {
		String[] str = new String[3];
		str[0] = "";
		str[1] = "";
		str[2] = "";
		String hql = "select distinct obj.orderNoid from CotOrderOutdetail obj where obj.orderId="
				+ orderOutId;
		List array = this.getOrderOutDao().find(hql);
		Iterator<?> it = array.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='order'"
					+ " and (obj.outFlag is null or obj.outFlag = 0) and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					str[0] += list.get(i) + ",";
				}
			}
			String dealHql = "select obj.id from CotFinaceAccountrecv obj where obj.source ='order'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				for (int i = 0; i < dealList.size(); i++) {
					str[1] += dealList.get(i) + ",";
				}
			}
		}
		// 查询客户的溢收款
		String yiHql = "select detail.id from CotFinaceOther detail,CotCustomer c "
				+ "where detail.factoryId=c.id and detail.finaceName='溢收款' and detail.amount!=0  and detail.factoryId="
				+ custId;
		List yiList = this.getOrderOutDao().find(yiHql);
		if (yiList != null && yiList.size() > 0) {
			for (int i = 0; i < yiList.size(); i++) {
				str[2] += yiList.get(i) + ",";
			}
		}
		return str;
	}

	// 根据对象名和ids查找记录
	public List getListByTable(String tabName, String ids) {
		String hql = "from " + tabName + " obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		return this.getOrderOutDao().find(hql);
	}

	// 查询未导入的应付款的条数
	public int findNoImportNumFac(Integer orderOutId) {
		int temp = 0;
		// 生产合同的条数
		// 通过出货主单编号查找产品采购主单编号集合
		String hql = "select distinct obj.orderId from CotOrderFacdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderOutId;
		List<?> orderAry = this.getOrderOutDao().find(hql);
		// 产品采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款时过滤掉
		String str = "";
		Iterator<?> it = orderAry.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='orderfac'"
					+ " and (obj.outFlag is null or obj.outFlag = 0) and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				temp += list.size();
			}
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='orderfac'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				temp += dealList.size();
			}

			String yiHql = "select obj.id from CotFinaceOther obj,CotOrderFac f where obj.source is null and obj.finaceName='溢付款'"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				temp += yiList.size();
			}
		}

		// 配件合同的条数
		// 通过出货主单编号查找配件采购主单编号集合
		String hql2 = "select distinct obj.orderId from CotFittingsOrderdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderOutId;
		List<?> orderAry2 = this.getOrderOutDao().find(hql2);
		// 配件采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款时过滤掉
		Iterator<?> it2 = orderAry2.iterator();
		while (it2.hasNext()) {
			Integer orderId = (Integer) it2.next();
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='fitorder'"
					+ " and obj.outFlag != 1 and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				temp += list.size();
			}
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='fitorder'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				temp += dealList.size();
			}
			boolean flag3 = false;
			String yiHql = "select obj.id from CotFinaceOther obj,CotFittingOrder f where obj.source is null and obj.finaceName='溢付款'"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				temp += yiList.size();
			}
		}
		// 包材合同的条数
		// 通过出货主单编号查找包材采购主单编号集合
		String hql3 = "select distinct obj.packingOrderId from CotPackingOrderdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderOutId;
		List<?> orderAry3 = this.getOrderOutDao().find(hql3);
		// 包材采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款,并且该厂家不含溢付款时过滤掉
		Iterator<?> it3 = orderAry3.iterator();
		while (it3.hasNext()) {
			Integer orderId = (Integer) it3.next();
			CotPackingOrder packingOrder = (CotPackingOrder) this
					.getOrderOutDao().getById(CotPackingOrder.class, orderId);
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='packorder'"
					+ " and obj.outFlag != 1 and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				temp += list.size();
			}
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='packorder'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				temp += dealList.size();
			}
			String yiHql = "select obj.id from CotFinaceOther obj,CotPackingOrder f where obj.source is null and obj.finaceName='溢付款'"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				temp += yiList.size();
			}
		}
		return temp;
	}

	// 查询未导入的应付款的编号
	public String[] findNoImportAllFac(Integer orderOutId) {
		String[] str = new String[9];
		str[0] = "";
		str[1] = "";
		str[2] = "";
		str[3] = "";
		str[4] = "";
		str[5] = "";
		str[6] = "";
		str[7] = "";
		str[8] = "";
		// 生产合同的条数
		// 通过出货主单编号查找产品采购主单编号集合
		String hql = "select distinct obj.orderId from CotOrderFacdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderOutId;
		List<?> orderAry = this.getOrderOutDao().find(hql);
		// 产品采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款时过滤掉
		Iterator<?> it = orderAry.iterator();
		while (it.hasNext()) {
			Integer orderId = (Integer) it.next();
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='orderfac'"
					+ " and (obj.outFlag is null or obj.outFlag = 0) and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					str[0] += list.get(i) + ",";

				}
			}
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='orderfac'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				for (int i = 0; i < dealList.size(); i++) {
					str[1] += dealList.get(i) + ",";

				}
			}

			String yiHql = "select obj.id from CotFinaceOther obj,CotOrderFac f where obj.source is null and obj.finaceName='溢付款'"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				for (int i = 0; i < yiList.size(); i++) {
					str[2] += yiList.get(i) + ",";

				}
			}
		}

		// 配件合同的条数
		// 通过出货主单编号查找配件采购主单编号集合
		String hql2 = "select distinct obj.orderId from CotFittingsOrderdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderOutId;
		List<?> orderAry2 = this.getOrderOutDao().find(hql2);
		// 配件采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款时过滤掉
		Iterator<?> it2 = orderAry2.iterator();
		while (it2.hasNext()) {
			Integer orderId = (Integer) it2.next();
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='fitorder'"
					+ " and obj.outFlag != 1 and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					str[3] += list.get(i) + ",";

				}
			}
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='fitorder'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				for (int i = 0; i < dealList.size(); i++) {
					str[4] += dealList.get(i) + ",";

				}
			}
			boolean flag3 = false;
			String yiHql = "select obj.id from CotFinaceOther obj,CotFittingOrder f where obj.source is null and obj.finaceName='溢付款'"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				for (int i = 0; i < yiList.size(); i++) {
					str[5] += yiList.get(i) + ",";

				}
			}
		}
		// 包材合同的条数
		// 通过出货主单编号查找包材采购主单编号集合
		String hql3 = "select distinct obj.packingOrderId from CotPackingOrderdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ orderOutId;
		List<?> orderAry3 = this.getOrderOutDao().find(hql3);
		// 包材采购主单如果没有可以导入到出货的其他费用 并且没有可以导入到出货的应付款,并且该厂家不含溢付款时过滤掉
		Iterator<?> it3 = orderAry3.iterator();
		while (it3.hasNext()) {
			Integer orderId = (Integer) it3.next();
			CotPackingOrder packingOrder = (CotPackingOrder) this
					.getOrderOutDao().getById(CotPackingOrder.class, orderId);
			String otherHql = "select obj.id from CotFinaceOther obj where obj.source ='packorder'"
					+ " and obj.outFlag != 1 and obj.status!=1 and obj.fkId ="
					+ orderId;
			List<?> list = this.getOrderOutDao().find(otherHql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					str[6] += list.get(i) + ",";

				}
			}
			String dealHql = "select obj.id from CotFinaceAccountdeal obj where obj.source ='packorder'"
					+ " and obj.status!=1 and obj.zhRemainAmount>0 and obj.fkId ="
					+ orderId;
			List<?> dealList = this.getOrderOutDao().find(dealHql);
			if (dealList != null && dealList.size() > 0) {
				for (int i = 0; i < dealList.size(); i++) {
					str[7] += dealList.get(i) + ",";

				}
			}
			String yiHql = "select obj.id from CotFinaceOther obj,CotPackingOrder f where obj.source is null and obj.finaceName='溢付款'"
					+ " and obj.amount>0 and obj.factoryId =f.factoryId and f.id="
					+ orderId;
			List<?> yiList = this.getOrderOutDao().find(yiHql);
			if (yiList != null && yiList.size() > 0) {
				for (int i = 0; i < yiList.size(); i++) {
					str[8] += yiList.get(i) + ",";

				}
			}
		}
		return str;
	}

	// 通过订单Id获得厂家对象
	public CotFactory getFactoryByOrderId(Integer orderId) {
		CotOrder cotOrder = (CotOrder) this.getOrderOutDao().getById(
				CotOrder.class, orderId);
		if (cotOrder != null) {
			CotOrder custClone = (CotOrder) SystemUtil.deepClone(cotOrder);
			custClone.setCotOrderDetails(null);
			custClone.setOrderMBImg(null);
			int facId = custClone.getFactoryId();
			CotFactory cotFactory = (CotFactory) this.getOrderOutDao().getById(
					CotFactory.class, facId);
			if (cotFactory != null) {
				return cotFactory;
			}
		}
		return null;
	}

	public CotBaseDao getOrderOutDao() {
		return orderOutDao;
	}

	public void setOrderOutDao(CotBaseDao orderOutDao) {
		this.orderOutDao = orderOutDao;
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

	public CotCustomerService getCustService() {
		return custService;
	}

	public void setCustService(CotCustomerService custService) {
		this.custService = custService;
	}

	public CotReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(CotReportDao reportDao) {
		this.reportDao = reportDao;
	}

	public CotOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(CotOrderService orderService) {
		this.orderService = orderService;
	}

    @Override
    public boolean isInvoiceExistByPIId(Integer piId) {
        // TODO Auto-generated method stub
        List list = this.getOrderOutDao().find("from CotOrderOut obj where obj.orderId="+piId);
        if(CollectionUtils.isEmpty(list)){
            return false;
        }
        return true;
    }

}
