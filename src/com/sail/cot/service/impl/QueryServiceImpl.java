/**
 * 公共查询
 */
package com.sail.cot.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.CotExportRptDao;
import com.sail.cot.domain.CotClause;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotContainerType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotDept;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotLoginInfo;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.domain.CotMessage;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotPanEle;
import com.sail.cot.domain.CotPayType;
import com.sail.cot.domain.CotPrice;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotRptFile;
import com.sail.cot.domain.CotSignDetail;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.VDetailStatusId;
import com.sail.cot.domain.VOrderOrderfacStatusId;
import com.sail.cot.domain.vo.CotCustVO;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.domain.vo.CotGivenVO;
import com.sail.cot.domain.vo.CotOrderFacVO;
import com.sail.cot.domain.vo.CotOrderVO;
import com.sail.cot.domain.vo.CotPriceFacVO;
import com.sail.cot.domain.vo.CotPriceVO;
import com.sail.cot.domain.vo.CotSignVO;
import com.sail.cot.email.util.Constants;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.QueryService;
import com.sail.cot.service.sample.impl.CotReportServiceImpl;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;

/**
 * 订单管理模块
 * 
 * @author qh-chzy
 * 
 */
public class QueryServiceImpl implements QueryService {

	private CotBaseDao baseDao;
	private CotExportRptDao exportRptDao;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	private Logger logger = Log4WebUtil.getLogger(QueryServiceImpl.class);
	// 操作日志
	CotSysLogService sysLogService;

	public void setSysLogService(CotSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}
	
	public CotExportRptDao getExportRptDao() {
		return exportRptDao;
	}

	public void setExportRptDao(CotExportRptDao exportRptDao) {
		this.exportRptDao = exportRptDao;
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
//		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
//				"sysdic");
//		List<?> list = (List<?>) dicMap.get("factory");
		List<?> list =this.getBaseDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有新厂家
	public Map<?, ?> getNewFactoryNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有订单号
	public Map<?, ?> getOrderNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotOrder");
		for (int i = 0; i < list.size(); i++) {
			CotOrder cotOrder = (CotOrder) list.get(i);
			map.put(cotOrder.getId().toString(), cotOrder.getOrderNo());
		}
		return map;
	}

	// 查询所有发票
	public Map<?, ?> getOrderOutNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotOrderOut");
		for (int i = 0; i < list.size(); i++) {
			CotOrderOut cotOrderOut = (CotOrderOut) list.get(i);
			map.put(cotOrderOut.getId().toString(), cotOrderOut.getOrderNo());
		}
		return map;
	}

	// 查询所有集装箱
	public Map<?, ?> getContainerNoMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		//List<?> list = (List<?>) dicMap.get("container");
		List<?> list = this.getBaseDao().getRecords("CotContainerType");
		for (int i = 0; i < list.size(); i++) {
			CotContainerType cotContainerType = (CotContainerType) list.get(i);
			map.put(cotContainerType.getId().toString(), cotContainerType
					.getContainerName());
		}
		return map;
	}

	// 查询所有客户
	public Map<?, ?> getCusNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map
					.put(cotCustomer.getId().toString(), cotCustomer
							.getFullNameCn());
		}
		return map;
	}

	// 查询所有客户编号
	public Map<?, ?> getCustNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map
					.put(cotCustomer.getId().toString(), cotCustomer
							.getCustomerNo());
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

	// 查询所有材质
	public Map<?, ?> getTypeMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("typelv1");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) list.get(i);
			map.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		return map;
	}

	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 查询所有部门
	public Map<?, ?> getDeptMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotDept");
		for (int i = 0; i < list.size(); i++) {
			CotDept cotDept = (CotDept) list.get(i);
			map.put(cotDept.getId().toString(), cotDept.getDeptName());
		}
		return map;
	}

	// 查询所有公司简称
	public Map<?, ?> getCompanyMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotCompany");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany
					.getCompanyShortName());
		}
		return map;
	}

	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 查询VO记录
	public List<?> getPriceVOList(HttpServletRequest request,QueryInfo queryInfo) {
		String empNo = (String) request.getSession().getAttribute("empNo");
		boolean fac = SystemUtil.isAction(request, "cotpricefac.do", "SEL");
		boolean out = SystemUtil.isAction(request, "cotpriceout.do", "SEL");
		
		
		List<CotPriceVO> listVo = new ArrayList<CotPriceVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotPriceVO priceVO = new CotPriceVO();
				Object[] obj = (Object[]) list.get(i);
				priceVO.setId((Integer) obj[0]);
				priceVO.setPriceNo((String) obj[1]);
				priceVO.setPriceTime((Timestamp) obj[2]);
				priceVO.setClauseId((Integer) obj[3]);
				priceVO.setCustomerShortName((String) obj[4]);
				priceVO.setEleId((String) obj[5]);
				priceVO.setEleNameEn((String) obj[6]);
				
				if(fac==false && !"admin".equals(empNo)){
					priceVO.setPriceFac(-1f);
				}else{
					priceVO.setPriceFac((Float) obj[7]);
				}
				if(fac==false && !"admin".equals(empNo)){
					priceVO.setPriceFacUint(0);
				}else{
					priceVO.setPriceFacUint((Integer) obj[8]);
				}
				
				priceVO.setPricePrice((Float) obj[9]);
				priceVO.setCurrencyId((Integer) obj[10]);
				priceVO.setEleSizeDesc((String) obj[11]);
				listVo.add(priceVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	
	// 查询VO记录
	public List<?> getOrderVOs(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
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
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 查询VO记录
	public List<?> getOrderVOList(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setOrderNo((String) obj[1]);
				orderVO.setOrderTime((Timestamp) obj[2]);
				orderVO.setCustomerShortName((String) obj[3]);
				orderVO.setEmpsName((String) obj[4]);
				orderVO.setEleId((String) obj[5]);
				orderVO.setEleNameEn((String) obj[6]);
				orderVO.setBoxCount((Long) obj[7]);
				orderVO.setTotalMoney(((Double) obj[8]).floatValue());
				orderVO.setOrderPrice(((Double) obj[9]).floatValue());
				orderVO.setCurrencyId((Integer) obj[10]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 查询VO记录
	public List<?> getGivenVOList(QueryInfo queryInfo) {
		List<CotGivenVO> listVo = new ArrayList<CotGivenVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotGivenVO givenVO = new CotGivenVO();
				Object[] obj = (Object[]) list.get(i);
				givenVO.setId((Integer) obj[0]);
				givenVO.setGivenNo((String) obj[1]);
				givenVO.setCustomerShortName((String) obj[2]);
				givenVO.setGivenTime((Timestamp) obj[3]);
				givenVO.setCustRequiretime((Date) obj[4]);
				givenVO.setGivenAddr((String) obj[5]);
				givenVO.setEmpsName((String) obj[6]);
				givenVO.setEleId((String) obj[7]);
				givenVO.setEleNameEn((String) obj[8]);
				listVo.add(givenVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 根据条件查询主单记录
	public List<?> getOrderList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getBaseDao().findRecords(queryInfo,
					CotOrder.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询主订单出错");
		}
		return null;
	}

	// 根据条件查询订单明细记录
	public List<?> getOrderDetailList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getBaseDao().findRecords(queryInfo,
					CotOrderDetail.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询订单明细出错");
		}
		return null;
	}

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getBaseDao().getRecordsCountJDBC(queryInfo);
	}

	// 设置是否有权限查看样品图片
	public void findIsSelPic(HttpServletRequest request, List<?> list) {
		String empNo = (String) request.getSession().getAttribute("empNo");
		if ("admin".equals(empNo)) {
			return;
		}
		// 判断是否有查看图片的权限
		boolean all = SystemUtil.isAction(request, "cotpicture.do", "SEL");
		if (all == false) {
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				CotElementsNew cotElementsNew = (CotElementsNew) it.next();
				cotElementsNew.setPicPath("common/images/noElePicSel.png");
			}
		}
	}

	// 判断是否有权限查看厂家报价
	public List<?> findIsSelPrice(HttpServletRequest request, List list)
			throws Exception {
		String empNo = (String) request.getSession().getAttribute("empNo");
		Map facMap = this.getFactoryNameMap(request);
		Map curMap = this.getCurrencyMap(request);
		boolean fac = SystemUtil.isAction(request, "cotpricefac.do", "SEL");
		boolean out = SystemUtil.isAction(request, "cotpriceout.do", "SEL");
		if (!"admin".equals(empNo)) {
			List<CotElementsVO> listNew = new ArrayList<CotElementsVO>();
			// 1.使用反射获取对象的所有属性名称
			String[] propEle = ReflectionUtils
					.getDeclaredFields(CotElementsNew.class);
			String[] propVO = ReflectionUtils
					.getDeclaredFields(CotElementsVO.class);
			for (int k = 0; k < list.size(); k++) {
				CotElementsNew cotElementsNew = (CotElementsNew) list.get(k);
				CotElementsVO cotElementsVO = new CotElementsVO();
				for (int i = 0; i < propVO.length; i++) {
					for (int j = 0; j < propEle.length; j++) {
						if (propVO[i].equals(propEle[j])) {
							String value = BeanUtils.getProperty(
									cotElementsNew, propEle[j]);
							if(propVO[i].equals("priceFac") || propVO[i].equals("priceFacUint") ||propVO[i].equals("priceOut") ||propVO[i].equals("priceOutUint")){
								if(fac==false){
									BeanUtils.setProperty(cotElementsVO, "priceFac","*");
									BeanUtils.setProperty(cotElementsVO, "priceFacUint","0");
								}else{
									if (value != null) {
										BeanUtils.setProperty(cotElementsVO, propVO[i],
												value);
									}
								}
								if(out==false){
									BeanUtils.setProperty(cotElementsVO, "priceOut","*");
									BeanUtils.setProperty(cotElementsVO, "priceOutUint","0");
								}else{
									if (value != null) {
										BeanUtils.setProperty(cotElementsVO, propVO[i],
												value);
									}
								}
							}else {	
								if (value != null) {
									BeanUtils.setProperty(cotElementsVO, propVO[i],
											value);
								}
							}
						}
					}
				}
				if (cotElementsVO.getFactoryId() != null) 
					cotElementsVO.setFacShortName((String)facMap.get(cotElementsVO.getFactoryId().toString()));
				if (cotElementsVO.getPriceFacUint() != null) 
					cotElementsVO.setPriceFacUintName((String)curMap.get(cotElementsVO.getPriceFacUint().toString()));
				if (cotElementsVO.getPriceOutUint() != null) 
					cotElementsVO.setPriceOutUintName((String)curMap.get(cotElementsVO.getPriceOutUint().toString()));
				listNew.add(cotElementsVO);
			}
			return listNew;
		}
		else {
			for(int i=0; i<list.size(); i++)
			{
				CotElementsNew elementsNew = (CotElementsNew)list.get(i);
				if (elementsNew.getFactoryId() != null) 
					elementsNew.setFacShortName((String)facMap.get(elementsNew.getFactoryId().toString()));
				if (elementsNew.getPriceFacUint() != null) 
					elementsNew.setPriceFacUintName((String)curMap.get(elementsNew.getPriceFacUint().toString()));
				if (elementsNew.getPriceOutUint() != null) 
					elementsNew.setPriceOutUintName((String)curMap.get(elementsNew.getPriceOutUint().toString()));
				list.set(i, elementsNew);
			}
		}
		return list;
	}

	// 查询所有客户简称
	public Map<?, ?> getCusShortNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBaseDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer
					.getCustomerShortName());
		}
		return map;
	}

	// 根据报价编号字符串查找报价明细
	@SuppressWarnings("unchecked")
	public List<CotOrderDetail> findOrderDetailByIds(String ids) {
		String hql = "from CotOrderDetail obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ") order by obj.sortNo";
		List<CotOrderDetail> list = this.getBaseDao().find(hql);
//		Iterator<?> it = list.iterator();
//		while (it.hasNext()) {
//			CotOrderDetail cotOrderDetail = (CotOrderDetail) it.next();
//			cotOrderDetail.setPicImg(null);
//			//List facList = getDicList("factory");
//			for (int i = 0; i < facList.size(); i++) {
//				CotFactory cotFactory = (CotFactory) facList.get(i);
//				if (cotOrderDetail.getFactoryId() != null
//						&& cotFactory.getId().intValue() == cotOrderDetail
//								.getFactoryId().intValue()) {
//					cotOrderDetail.setFactoryShortName(cotFactory
//							.getShortName());
//				}
//			}
//		}
		return list;
	}

	// 根据报价编号字符串查找报价明细
	@SuppressWarnings("unchecked")
	public List<CotPriceDetail> findPriceDetailByIds(String ids) {
		String hql = "from CotPriceDetail obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ") order by obj.sortNo";
		List<CotPriceDetail> list = this.getBaseDao().find(hql);
//		Iterator<?> it = list.iterator();
//		while (it.hasNext()) {
//			CotPriceDetail cotPriceDetail = (CotPriceDetail) it.next();
//			cotPriceDetail.setPicImg(null);
//			for (int i = 0; i < facList.size(); i++) {
//				CotFactory cotFactory = (CotFactory) facList.get(i);
//				if (cotPriceDetail.getFactoryId() != null
//						&& cotFactory.getId().intValue() == cotPriceDetail
//								.getFactoryId().intValue()) {
//					cotPriceDetail.setFactoryShortName(cotFactory
//							.getShortName());
//				}
//			}
//		}
		return list;
	}
	
	// 根据报价编号字符串查找报价明细
	public List<CotPanEle> findPanEleByIds(String ids) {
		String hql = "from CotPanEle obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		List<CotPanEle> list = this.getBaseDao().find(hql);
		return list;
	}

	// 查找送样明细
	@SuppressWarnings("unchecked")
	public List<CotGivenDetail> findGivenDetailByIds(String ids) {
		String hql = "from CotGivenDetail obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")  order by obj.sortNo";
		List<CotGivenDetail> list = this.getBaseDao().find(hql);
//		Iterator<?> it = list.iterator();
//		while (it.hasNext()) {
//			CotGivenDetail cotGivenDetail = (CotGivenDetail) it.next();
//			cotGivenDetail.setPicImg(null);
//			//List facList = getDicList("factory");
//			for (int i = 0; i < facList.size(); i++) {
//				CotFactory cotFactory = (CotFactory) facList.get(i);
//				if (cotGivenDetail.getFactoryId() != null
//						&& cotFactory.getId().intValue() == cotGivenDetail
//								.getFactoryId().intValue()) {
//					cotGivenDetail.setFactoryShortName(cotFactory
//							.getShortName());
//				}
//			}
//		}
		return list;
	}

	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getBaseDao().findRecords(queryInfo,
					CotPrice.class);
			return records;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getBaseDao().getRecords(objName);
	}

	// 更新(样品转明细)
	@SuppressWarnings("unchecked")
	public Object modifyElementsToDetail(CotElementsNew e,
			Map<String, String> map) throws DAOException {
		
		String sqlString = " from CotEleFittings obj where obj.eleId=" + e.getId();
		// 获取样品配件信息
		List fitList = this.getBaseDao().find(sqlString);

		String str = " from CotElePrice obj where obj.eleId=" + e.getId();
		// 获取样品成本信息
		List priceList = this.getBaseDao().find(str);

		String eleProTime = map.get("eleProTime").toString();// 开发时间
		String type = map.get("type").toString();// 样品转换后的类型
		String newEleId = map.get("newEleId").toString();// 拷贝的货号
		String pic = map.get("pic").toString();// 是否更改过图片

		// 创建默认样品图片对象
		CotElePic cotElePic = new CotElePic();
		// 图片处理
		if (!"".equals(pic)) {
			// 获得tomcat路径
			String classPath = QueryServiceImpl.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			File picFile = new File(systemPath + pic);
			FileInputStream in;
			byte[] b = new byte[(int) picFile.length()];
			try {
				in = new FileInputStream(picFile);
				while (in.read(b) != -1) {
				}
				in.close();
				if (!"common/images/zwtp.png".equals(pic)) {
					// 删除上传的图片
					picFile.delete();
				}
				cotElePic.setEleId(e.getEleId());
				cotElePic.setPicImg(b);
				cotElePic.setPicName(e.getEleId());
				cotElePic.setPicSize(b.length);
			} catch (Exception e1) {
				logger.error("设置样品图片错误!");
			}
		} else {
			String sql = "from CotElePic obj where obj.eleId='" + newEleId
					+ "'";
			List list = this.getBaseDao().find(sql);
			if (list.size() == 0) {
				// 得到暂无图片的字节
				// 获得tomcat路径
				String classPath = CotReportServiceImpl.class.getResource("/")
						.toString();
				String systemPath = classPath.substring(6,
						classPath.length() - 16);
				File picFile = new File(systemPath + "common/images/zwtp.png");
				FileInputStream in;
				try {
					in = new FileInputStream(picFile);
					byte[] b = new byte[(int) picFile.length()];
					while (in.read(b) != -1) {
					}
					in.close();
					cotElePic.setEleId(e.getEleId());
					cotElePic.setPicImg(b);
					cotElePic.setPicName(e.getEleId());
					cotElePic.setPicSize(b.length);
				} catch (Exception e1) {
					logger.error("设置样品图片错误!");
				}
			} else {
				CotElePic oldPic = (CotElePic) list.get(0);
				cotElePic.setEleId(e.getEleId());
				cotElePic.setPicImg(oldPic.getPicImg());
				cotElePic.setPicName(e.getEleId());
				cotElePic.setPicSize(oldPic.getPicImg().length);
			}
		}
		// 创建时间
		e.setEleAddTime(new Date(System.currentTimeMillis()));
		// 开发时间
		if (!"".equals(eleProTime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = new Date(sdf.parse(eleProTime).getTime());
				e.setEleProTime(date);
			} catch (ParseException e2) {
				logger.error("设置样品开发时间错误!");
			}
		}
		e.setElePrice(null);
		e.setEleFittingPrice(null);
		this.getBaseDao().create(e);
		cotElePic.setFkId(e.getId());
		// 保存图片
		this.getBaseDao().create(cotElePic);
		
		// 同步样品成本信息
		List elePriceList = new ArrayList();
		if (priceList != null) {
			Iterator iterator = priceList.iterator();
			while (iterator.hasNext()) {
				CotElePrice elePrice = (CotElePrice) iterator.next();
				elePrice.setId(null);
				elePrice.setEleId(e.getId());
				elePriceList.add(elePrice);
			}
		}
		this.getBaseDao().saveRecords(elePriceList);
		// 同步配件信息
		List eleFittingsList = new ArrayList();

		if (fitList != null) {
			Iterator iterator = fitList.iterator();
			while (iterator.hasNext()) {
				CotEleFittings eleFittings = (CotEleFittings) iterator.next();
				eleFittings.setId(null);
				eleFittings.setEleId(e.getId());
				eleFittings.setEleChild(null);
				eleFittings.setEleChildId(null);
				eleFittingsList.add(eleFittings);
			}
		}
		this.getBaseDao().saveRecords(eleFittingsList);
		
		// 转换对象
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotElementsNew.class);

		Object obj = null;
		if (type.equals("order"))// 从订单对象转换
			obj = new CotOrderDetail();
		else if (type.equals("price"))// 从报价对象转换
			obj = new CotPriceDetail();
		else if (type.equals("given"))// 从送样对象转换
			obj = new CotGivenDetail();
		else if (type.equals("sign"))// //从征样对象转换
			obj = new CotSignDetail();

		for (int i = 0; i < properties.length; i++) {
			if ("cotPictures".equals(properties[i])
					|| "cotFile".equals(properties[i])
					|| "childs".equals(properties[i])
					|| "picImg".equals(properties[i])
					|| "cotPriceFacs".equals(properties[i])
					|| "cotElePrice".equals(properties[i])
					|| "cotEleFittings".equals(properties[i])
					|| "eleFittingPrice".equals(properties[i])
					|| "cotElePacking".equals(properties[i])
					|| "packingPrice".equals(properties[i]))
				continue;
			try {
				String value = BeanUtils.getProperty(e, properties[i]);
				if (value != null)
					BeanUtils.setProperty(obj, properties[i], value);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error("转化异常!");
			}
		}
		return obj;
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}

	// 根据币种换算价格
	public float updatePrice(Float price, Integer oldCurId, Integer newCurId) {
		if (price == null || oldCurId==null || newCurId==null) {
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
		DecimalFormat nbf = new DecimalFormat("0.0000");
		obj = Float.parseFloat(nbf.format(obj));

		return obj;
	}

	public void updateCotOrderDetail(CotOrderDetail cotOrderDetail) {
		this.getBaseDao().update(cotOrderDetail);
	}

	public CotBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(CotBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public Integer addToMessage(CotMessage cotMessage, String msgBeginDate,
			String msgEndDate, String no, Integer empId,Integer gid) {
		// 设置时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat mdf = new SimpleDateFormat("yyyyMMddhhmmss");

		// 添加时间为系统当前时间
		cotMessage.setAddTime(new Date(System.currentTimeMillis()));
		// 发送人默认为当前登陆用户
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		cotMessage.setMsgFromPerson(cotEmps.getId());
		// 设置消息提醒单号
		Date date = new Date(System.currentTimeMillis());
		cotMessage.setMsgOrderNo("MG-" + mdf.format(date));
		// 设置消息接收人
		cotMessage.setMsgToPerson(empId);
		// 设置消息状态默认为未读
		cotMessage.setMsgStatus(0L);
		// 设置对应表的类名
		cotMessage.setMsgTable(cotMessage.getMsgTable());
		// 设置msgAction
		String url = null;
		String msgAction = null;
		if ((cotMessage.getMsgTable()).trim().equals("CotGiven")) {
			url = "cotgiven.do?method=query";
			msgAction = url + "&No=" + no;
			cotMessage.setMsgAction(msgAction);
			cotMessage.setMsgEditAction("cotgiven.do?method=add&id="+gid);
		}
		if ((cotMessage.getMsgTable()).trim().equals("CotSign")) {
			url = "cotsign.do?method=query";
			msgAction = url + "&No=" + no;
			cotMessage.setMsgAction(msgAction);
		}
		if ((cotMessage.getMsgTable()).trim().equals("CotOrderFac")) {
			url = "cotorderfac.do?method=query";
			msgAction = url + "&orderNoFind=" + no;
			cotMessage.setMsgAction(msgAction);
			cotMessage.setMsgEditAction("cotorderfac.do?method=add&id="+gid);
		}	
		if ((cotMessage.getMsgTable()).trim().equals("CotOrder")) {
			url = "cotorder.do?method=query";
			msgAction = url + "&orderNoFind=" + no;
			cotMessage.setMsgAction(msgAction);
			cotMessage.setMsgEditAction("cotorder.do?method=addOrder&id="+gid);
		}
		if ((cotMessage.getMsgTable()).trim().equals("CotPrice")) {
			url = "cotorder.do?method=query";
			msgAction = url + "&noFind=" + no;
			cotMessage.setMsgAction(msgAction);
			cotMessage.setMsgEditAction("cotprice.do?method=addPrice&id="+gid);
		}
		List<CotMessage> records = new ArrayList<CotMessage>();

		// 保存消息单
		try {
			if (msgBeginDate != null && !"".equals(msgBeginDate)) {
				cotMessage.setMsgBeginDate(new Date(sdf.parse(msgBeginDate)
						.getTime()));
			}
			if (msgEndDate != null && !"".equals(msgEndDate)) {
				cotMessage.setMsgEndDate(new Date(sdf.parse(msgEndDate)
						.getTime()));
			}
			// 判断该单号是否已经添加提醒
			List<CotMessage> res = new ArrayList<CotMessage>();
			res = this.getBaseDao().find("from CotMessage obj");
			CotMessage oldCotMessage = null;
			Integer recds = 0;
			Iterator<?> iterator = res.iterator();
			while (iterator.hasNext()) {
				oldCotMessage = (CotMessage) iterator.next();
				if (oldCotMessage.getMsgAction() != null
						&& !"".equals(oldCotMessage.getMsgAction())) {
					if (oldCotMessage.getMsgAction().equals(msgAction)) {
						recds = recds + 1;
					}
				}
			}
			if (recds != 1) {
				records.add(cotMessage);
				this.getBaseDao().saveRecords(records);
				// 添加操作日志
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(cotEmps.getId());
				syslog.setItemNo(cotMessage.getMsgOrderNo());
				syslog.setOpMessage("添加提醒消息成功");
				syslog.setOpModule("message");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			} else {
				List<CotMessage> resList = new ArrayList<CotMessage>();
				resList = this.getBaseDao().find(
						"from CotMessage obj where obj.msgAction ='"
								+ msgAction + "'");
				oldCotMessage = (CotMessage) resList.get(0);
				cotMessage.setMsgOrderNo(oldCotMessage.getMsgOrderNo());
				cotMessage.setMsgStatus(0L);
				cotMessage.setMsgTable(oldCotMessage.getMsgTable());
				cotMessage.setId(oldCotMessage.getId());
				cotMessage.setMsgAction(oldCotMessage.getMsgAction());
				cotMessage.setAddTime(new Date(System.currentTimeMillis()));
				records.add(cotMessage);
				this.getBaseDao().updateRecords(records);
			}
		} catch (Exception e) {
			logger.error("保存消息单出错！");
			e.printStackTrace();
			return null;
		}
		return cotMessage.getId();
	}
	
	//返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate){
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

	// 本地打印所有报表
	public boolean localPrintAll() {
		return true;
	}

	// 查询报价VO记录
	public List<?> getPriceVO(QueryInfo queryInfo) {
		List<CotPriceVO> listVo = new ArrayList<CotPriceVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotPriceVO priceVO = new CotPriceVO();
				Object[] obj = (Object[]) list.get(i);
				priceVO.setId((Integer) obj[0]);
				priceVO.setEleId((String) obj[1]);
				priceVO.setEleName((String) obj[2]);
				priceVO.setPricePrice((Float) obj[3]);
				priceVO.setPriceNo((String) obj[4]);
				priceVO.setPriceTime((Timestamp) obj[5]);
				priceVO.setCustomerShortName((String) obj[6]);
				priceVO.setEmpsName((String) obj[7]);
				priceVO.setCurrencyId((Integer) obj[8]);
				priceVO.setDetailId((Integer)obj[9]);
				priceVO.setPriceFac((Float)obj[10]);
				priceVO.setPriceFacUint((Integer)obj[11]);
				priceVO.setContactId((Integer) obj[12]);
				listVo.add(priceVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 查询订单VO记录
	public List<?> getOrderVO(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setEleId((String) obj[1]);
				orderVO.setEleName((String) obj[2]);
				orderVO.setOrderPrice((Float) obj[3]);
				orderVO.setOrderNo((String) obj[4]);
				if(obj[5] != null && obj[5].getClass().getName().equals("java.sql.Date"))
				{
					orderVO.setOrderTime(new Timestamp(((Date)obj[5]).getTime()));
				}
				else {
					orderVO.setOrderTime((Timestamp) obj[5]);
				}
				orderVO.setCustomerShortName((String) obj[6]);
				orderVO.setEmpsName((String) obj[7]);
				orderVO.setCurrencyId((Integer) obj[8]);
				orderVO.setDetailId((Integer)obj[9]);
				orderVO.setPoNo((String)obj[10]);
				orderVO.setPriceFac((Float)obj[11]);
				orderVO.setPriceFacUint((Integer)obj[12]);
				orderVO.setBoxCount((Long)obj[13]);
				orderVO.setContainerCount((Long)obj[14]);
				orderVO.setUnBoxCount((Float)obj[15]);
				orderVO.setBoxObCount((Long)obj[16]);
				orderVO.setContactId((Integer) obj[17]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	
	// 查询出货VO记录
	public List<?> getOrderOutVO(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setEleId((String) obj[1]);
				orderVO.setEleName((String) obj[2]);
				orderVO.setOrderPrice((Float) obj[3]);
				orderVO.setOrderNo((String) obj[4]);
				if(obj[5] != null && obj[5].getClass().getName().equals("java.sql.Date"))
				{
					orderVO.setOrderTime(new Timestamp(((Date)obj[5]).getTime()));
				}
				else {
					orderVO.setOrderTime((Timestamp) obj[5]);
				}
				orderVO.setCustomerShortName((String) obj[6]);
				orderVO.setEmpsName((String) obj[7]);
				orderVO.setCurrencyId((Integer) obj[8]);
				orderVO.setDetailId((Integer)obj[9]);
				orderVO.setBoxCbm((Float)obj[10]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	
	// 查询出货明细VO记录
	public List<?> getOrderDetailVO(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setOrderNo((String) obj[1]);
				orderVO.setEleId((String) obj[2]);
				orderVO.setEleName((String) obj[3]);
				orderVO.setBoxCount((Long) obj[4]);
				orderVO.setContainerCount((Long) obj[5]);
				orderVO.setUnSendNum((Integer) obj[6]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	
	// 查询厂家报价VO记录
	public List<?> getPriceFacVO(QueryInfo queryInfo) {
		List<CotPriceFacVO> listVo = new ArrayList<CotPriceFacVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotPriceFacVO priceFacVO = new CotPriceFacVO();
				Object[] obj = (Object[]) list.get(i);
				priceFacVO.setId((Integer) obj[0]);
				priceFacVO.setEleId((String) obj[1]);
				priceFacVO.setEleName((String) obj[2]);
				priceFacVO.setAddTime((Date) obj[3]);
				priceFacVO.setPriceFac((Float) obj[4]);
				priceFacVO.setPriceUint((Integer) obj[5]);
				priceFacVO.setShortName((String) obj[6]);
				priceFacVO.setPriceRemark((String) obj[7]);
				priceFacVO.setElId((Integer) obj[8]);
				priceFacVO.setEleSizeDesc((String) obj[9]);
				listVo.add(priceFacVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 查询征样VO记录
	public List<?> getSignVO(QueryInfo queryInfo) {
		List<CotSignVO> listVo = new ArrayList<CotSignVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotSignVO signVO = new CotSignVO();
				Object[] obj = (Object[]) list.get(i);
				signVO.setId((Integer) obj[0]);
				signVO.setEleId((String) obj[1]);
				signVO.setEleName((String) obj[2]);
				signVO.setSignNo((String) obj[3]);
				signVO.setSignTime((Date) obj[4]);
				signVO.setCustomerShortName((String) obj[5]);
				signVO.setEmpsName((String) obj[6]);
				signVO.setSignId((Integer) obj[7]);
				signVO.setPriceOut((Float) obj[8]);
				signVO.setPriceOutUint((Integer) obj[9]);
				listVo.add(signVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	
	// 样品档案查询征样VO记录
	public List<?> getSignVoByEle(QueryInfo queryInfo) {
		List<CotSignVO> listVo = new ArrayList<CotSignVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotSignVO signVO = new CotSignVO();
				Object[] obj = (Object[]) list.get(i);
				signVO.setId((Integer) obj[0]);
				signVO.setSignTime((Date) obj[1]);
				signVO.setSignNo((String) obj[2]);
				signVO.setEmpsName((String) obj[3]);
				signVO.setCustomerShortName((String) obj[4]);
				signVO.setFactoryId((Integer) obj[5]);
				signVO.setRequireTime((Date) obj[6]);
				signVO.setArriveTime((Date) obj[7]);
				listVo.add(signVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 查询送样VO记录
	public List<?> getGivenVO(QueryInfo queryInfo) {
		List<CotGivenVO> listVo = new ArrayList<CotGivenVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotGivenVO givenVO = new CotGivenVO();
				Object[] obj = (Object[]) list.get(i);
				givenVO.setId((Integer) obj[0]);
				givenVO.setEleId((String) obj[1]);
				givenVO.setEleName((String) obj[2]);
				givenVO.setGivenNo((String) obj[3]);
				givenVO.setGivenTime((Timestamp) obj[4]);
				givenVO.setGivenAddr((String) obj[5]);
				givenVO.setCustomerShortName((String) obj[6]);
				givenVO.setEmpsName((String) obj[7]);
				givenVO.setGivenId((Integer) obj[8]);
				givenVO.setPriceOut((Float) obj[9]);
				givenVO.setPriceOutUint((Integer) obj[10]);
				listVo.add(givenVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 查询国内采购VO记录
	public List<?> getOrderFacVO(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setEleId((String) obj[1]);
				orderVO.setEleName((String) obj[2]);
				orderVO.setOrderPrice((Float) obj[3]);
				orderVO.setOrderNo((String) obj[4]);
				if(obj[5] != null && obj[5].getClass().getName().equals("java.sql.Date"))
				{
					orderVO.setOrderTime(new Timestamp(((Date)obj[5]).getTime()));
				}
				else {
					orderVO.setOrderTime((Timestamp) obj[5]);
				}
				//orderVO.setOrderTime((Timestamp) obj[5]);
				orderVO.setShortName((String) obj[6]);
				orderVO.setEmpsName((String) obj[7]);
				orderVO.setCurrencyId((Integer) obj[8]);
				orderVO.setDetailId((Integer) obj[9]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 查询样品VO
	public List<?> getSampleVO(QueryInfo queryInfo) {

		try {
			List<?> list = this.getBaseDao().findRecordsJDBC(queryInfo);
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 模糊查询
	public List<?> find(String tabName,int pageIdx, int maxsize, String field,
			String param, String fNo, String qF, String qV) {
		if (tabName == null || "".equals(tabName) || field == null
				|| "".equals(field)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		QueryInfo queryInfo = new QueryInfo();
		// 设定每页显示记录数
		queryInfo.setStartIndex((pageIdx-1)*maxsize);
		queryInfo.setCountOnEachPage(maxsize);
		if (tabName.equals("CotCustomer")) {
			return this.findWithCus(pageIdx,maxsize, param,fNo);
		} else {
			queryString.append(" where 1=1");

			String str = "select obj.id,obj." + field;
			if (fNo != null && !"".equals(fNo.trim())) {
				str += ",obj." + fNo;
			}

			// 设置查询记录语句
			queryInfo.setSelectString(str + " from " + tabName + " obj");
		}
		if (!"".equals(param)) {
			String temp = param.replaceAll("’", "'");
			queryString
					.append(" and obj." + field + " like \"" + param + "%\" or obj." + field + " like \"" + temp + "%\"");
			
		}
		if (!"".equals(qF)) {
			queryString.append(" and obj." + qF + "='" + qV + "'");
		}
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj." + field);
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			List<String[]> newlist = new ArrayList<String[]>();
			for (int i = 0; i < list.size(); i++) {
				String[] str=new String[3];
				Object[] obj = (Object[]) list.get(i);
				str[0] = obj[0].toString();
				if (obj[1] != null) {
					str[1] = obj[1].toString();
				} else {
					str[1] = "";
				}
				if (fNo != null && !"".equals(fNo.trim())) {
					if (obj[2] != null) {
						str[2] = obj[2].toString();
					} else {
						str[2] = "";
					}
				}else{
					str[2] = "";
				}
				newlist.add(str);
			}
			return newlist;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 模糊查询客户
	public List<?> findWithCus(int pageIdx,int maxsize, String param, String fNo) {
		WebContext ctx = WebContextFactory.get();
		StringBuffer queryString = new StringBuffer();
		QueryInfo queryInfo = new QueryInfo();
		// 设定每页显示记录数
		queryInfo.setStartIndex(pageIdx);
		queryInfo.setCountOnEachPage(maxsize);
		queryString.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) ctx.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(ctx.getHttpServletRequest(),
					"cotcustomer.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(ctx.getHttpServletRequest(),
						"cotcustomer.do", "DEPT");
				if (dept == true) {
					queryString.append(" and e.dept_id=" + emp.getDeptId());
				} else {
					queryString.append(" and obj.emp_id =" + emp.getId());
				}
			}
		}
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj.id,obj.CUSTOMER_SHORT_NAME as customerShortName,obj.CUSTOMER_NO as customerNo from cot_customer obj left join cot_emps e on obj.emp_id = e.id");
		
		if (!"".equals(param)) {
			String temp = param.replaceAll("’", "'");
			queryString
					.append(" and obj.CUSTOMER_SHORT_NAME like \"" + param + "%\" or obj.CUSTOMER_SHORT_NAME like \""+temp+ "%\"");
		}
		
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.CUSTOMER_SHORT_NAME");
		queryInfo.setQueryObjType("CotCustVO");
		try {
			List<?> list = this.getBaseDao().findRecordsJDBC(queryInfo);
			List<String[]> newlist = new ArrayList<String[]>();
			for (int i = 0; i < list.size(); i++) {
				String[] str = new String[3];
				CotCustVO custVO = (CotCustVO) list.get(i);
				str[0] = custVO.getId().toString();
				if (custVO.getCustomerShortName() != null) {
					str[1] = custVO.getCustomerShortName();
				} else {
					str[1] = "";
				}
				if (fNo != null && !"".equals(fNo.trim())) {
					if (custVO.getCustomerNo() != null) {
						str[2] = custVO.getCustomerNo();
					} else {
						str[2] = "";
					}
				}else{
					str[2] = "";
				}
				newlist.add(str);
			}
			return newlist;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<?> getHistoryPriceVOList(QueryInfo queryInfo) {
		List<CotPriceVO> listVo = new ArrayList<CotPriceVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotPriceVO priceVO = new CotPriceVO();
				Object[] obj = (Object[]) list.get(i);
				priceVO.setId((Integer) obj[0]);
				priceVO.setEleId((String) obj[1]);
				priceVO.setPriceNo((String) obj[2]);
				priceVO.setCustomerShortName((String) obj[3]);
				priceVO.setPricePrice((Float) obj[4]);
				priceVO.setPriceTime((Timestamp) obj[5]);
				listVo.add(priceVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询错误");
			return null;
		}
	}

	public List<?> getHistoryOrderFacVOList(QueryInfo queryInfo) {
		List<CotOrderFacVO> listVo = new ArrayList<CotOrderFacVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderFacVO orderFacVO = new CotOrderFacVO();
				Object[] obj = (Object[]) list.get(i);
				orderFacVO.setId((Integer) obj[0]);
				orderFacVO.setEleId((String) obj[1]);
				orderFacVO.setOrderNo((String) obj[2]);
				orderFacVO.setShortName((String) obj[3]);
				orderFacVO.setPriceFac((Float) obj[4]);
				orderFacVO.setAddTime((Date) obj[5]);
				listVo.add(orderFacVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询错误");
			return null;
		}
	}

	public List<?> getHistoryOrderVOList(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setEleId((String) obj[1]);
				orderVO.setOrderNo((String) obj[2]);
				orderVO.setCustomerShortName((String) obj[3]);
				orderVO.setOrderPrice((Float) obj[4]);
				orderVO.setOrderTime((Timestamp) obj[5]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询错误");
			return null;
		}
	}

	// 得到新厂家并存入内存数据字典
	public List<?> getNewFactoryList() {
		List list = this.getBaseDao().getRecords("CotFactory");
		WebContext ctx = WebContextFactory.get();
		// 存到内存中
		Map dicMap = (Map) ctx.getSession().getAttribute("sysdic");
		dicMap.put("factory", list);
		return list;
	}

	// 删除登录信息
	public void deleteLoginInfos() {
		WebContext ctx = WebContextFactory.get();
		String hql = "from CotLoginInfo c where c.loginIpaddr='"
				+ ctx.getHttpServletRequest().getRemoteAddr() + "'";
		List<?> list = this.getBaseDao().find(hql);
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			CotLoginInfo cotLoginInfo = (CotLoginInfo) list.get(i);
			ids.add(cotLoginInfo.getId());
		}
		if (ids.size() != 0) {
			try {
				this.getBaseDao().deleteRecordByIds(ids, "CotLoginInfo");
			} catch (DAOException e) {
				e.printStackTrace();
				logger.error("删除在线登录信息表异常", e);
			}
		}
	}

	// 编辑页面初始化时显示下拉框的文本
	public String showText(String tabName, String field, int id) {
		String hql = "select obj." + field + " from " + tabName
				+ " obj where obj.id=" + id;
		List<?> list = this.getBaseDao().find(hql);
		if (list.size() == 1) {
			return (String) list.get(0);
		} else {
			return "";
		}
	}

	// 根据位数取随机数
	public String getRandom(int byteNum) {
		// 生成一个32位的ID
		String msgId = RandomStringUtils.randomNumeric(byteNum);
		return msgId;
	}

	// 获得20/40/40HQ/45的柜体积(默认24/54/68/86)
	public Float[] getContainerCube() {
		Float[] temp = new Float[4];
		temp[0] = 24f;
		temp[1] = 54f;
		temp[2] = 68f;
		temp[3] = 86f;
		//List<?> container = this.getDicList("container");
		List<?> container = this.getBaseDao().getRecords("CotContainerType");
		for (int i = 0; i < container.size(); i++) {
			CotContainerType containerType = (CotContainerType) container
					.get(i);
			if (containerType.getContainerName().equals("20C") && containerType.getContainerCube()!=null) {
				temp[0] = containerType.getContainerCube();
			}
			if (containerType.getContainerName().equals("40C") && containerType.getContainerCube()!=null) {
				temp[1] = containerType.getContainerCube();
			}
			if (containerType.getContainerName().equals("40HC") && containerType.getContainerCube()!=null) {
				temp[2] = containerType.getContainerCube();
			}
			if (containerType.getContainerName().equals("45C") && containerType.getContainerCube()!=null) {
				temp[3] = containerType.getContainerCube();
			}
		}
		return temp;
	}

	// 获得20/40/40HQ/45的最大可装重(默认17500/22000/22000/29000)
	public Float[] getContainerWeigh() {
		Float[] temp = new Float[4];
		temp[0] = 17500f;
		temp[1] = 22000f;
		temp[2] = 22000f;
		temp[3] = 29000f;
		//List<?> container = this.getDicList("container");
		List<?> container = this.getBaseDao().getRecords("CotContainerType");
		for (int i = 0; i < container.size(); i++) {
			CotContainerType containerType = (CotContainerType) container
					.get(i);
			if (containerType.getContainerName().equals("20C") && containerType.getContainerWeigh()!=null) {
				temp[0] = containerType.getContainerWeigh();
			}
			if (containerType.getContainerName().equals("40C") && containerType.getContainerWeigh()!=null) {
				temp[1] = containerType.getContainerWeigh();
			}
			if (containerType.getContainerName().equals("40HC") && containerType.getContainerWeigh()!=null) {
				temp[2] = containerType.getContainerWeigh();
			}
			if (containerType.getContainerName().equals("45C") && containerType.getContainerWeigh()!=null) {
				temp[3] = containerType.getContainerWeigh();
			}
		}
		return temp;
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
	
	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId) {
		List<?> list = this.getBaseDao().find(
				"from CotRptFile f where f.rptType=" + rptTypeId);
		return list;
	}
	
	// 查找默认的报表
	public CotRptFile getRptDefault(Integer rptTypeId) {
		List<?> list = this.getBaseDao().find(
				"from CotRptFile f where f.flag is not null and f.flag=1 and f.rptType=" + rptTypeId);
		if(list!=null && list.size()>0){
			return (CotRptFile) list.get(0);		
		}else{
			return null;
		}
	}
	
	//获得材质名称
	public String getTypeName(Integer typeId){
		WebContext ctx = WebContextFactory.get();
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("typelv1");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) list.get(i);
			map.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		String name = map.get(typeId.toString());
		return name;
	}
	
	//获得厂家名称
	public String getFacName(Integer facId){
		WebContext ctx = WebContextFactory.get();
		Map<String, String> map = new HashMap<String, String>();
//		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute(
//				"sysdic");
//		List<?> list = (List<?>) dicMap.get("factory");
		List<?> list =this.getBaseDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		String name = map.get(facId.toString());
		return name;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getBaseDao().getJsonData(queryInfo);
	}
	
	public String getHomeData(QueryInfo queryInfo) throws DAOException {
		GridServerHandler gd = null;
		if (queryInfo.getExcludes() != null)
			gd = new GridServerHandler(queryInfo.getExcludes());
		else
			gd = new GridServerHandler();
		int count = this.getBaseDao().getRecordsCount(queryInfo);
		List res = this.getBaseDao().findRecords(queryInfo);
		Iterator<?> it = res.iterator();
		while (it.hasNext()) {
			VOrderOrderfacStatusId vDetailStatusId = (VOrderOrderfacStatusId) it.next();
			//判断delivery date是否要显示红色
			String shipId = vDetailStatusId.getShipportId()==null?"0":vDetailStatusId.getShipportId().toString();
			String tarId = vDetailStatusId.getTargetportId()==null?"0":vDetailStatusId.getTargetportId().toString();
			Integer temp = this.findDays(shipId,tarId);
			
			java.util.Date date = this.addDate(vDetailStatusId.getOrderLcDate(), temp);
			java.util.Date newDate = vDetailStatusId.getSendTime();
			if(newDate.getTime()<date.getTime()){
				vDetailStatusId.setChk(1);
			}else{
				java.util.Date date2 = this.addDate(date, 37);
				if(newDate.getTime()>=date2.getTime()){
					vDetailStatusId.setChk(1);
				}
			}
		}

		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
	}
	
	// 判断该产品货号是否存在
	public Integer findIsExistEleByEleId(String eleId) {
		String hql = "select obj.id from CotElementsNew obj where obj.eleId='"
				+ eleId + "'";
		List<?> list = this.getBaseDao().find(hql);
		if (list.size() != 0) {
			return (Integer) list.get(0);
		} else {
			return null;
		}
	}
	
	//查询默认公司的全称
	public CotCompany findDefaultCompany(){
		String hql = "from CotCompany obj where obj.companyIsdefault=1";
		List<?> list = this.getBaseDao().find(hql);
		if (list.size() != 0) {
			CotCompany com = (CotCompany) list.get(0);
			CotCompany companyClone = (CotCompany) SystemUtil.deepClone(com);
			if (companyClone != null)
				companyClone.setCompanyLogo(null);
			return companyClone;
		} else {
			return null;
		}
	}
	//调用存储过程更新工厂数据
	public boolean updateFacId(Integer srcFacId,Integer desFacId){
		boolean flag = false;
		Integer [] values = new Integer[2];
		values[0] = srcFacId;
		values[1] = desFacId;
		try {
			flag = this.getBaseDao().callProc("{call update_fac_id(?,?)}", values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	//调用存储过程更新客户数据
	public boolean updateCustId(Integer srcCustId,Integer desCustId){
		boolean flag = false;
		Integer [] values = new Integer[2];
		values[0] = srcCustId;
		values[1] = desCustId;
		try {
			flag = this.getBaseDao().callProc("{call update_cust_id(?,?)}", values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.QueryService#findRecords(com.sail.cot.query.QueryInfo)
	 */
	public List findRecords(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//查询报价明细是否已含有该配件
	public CotFittings getFittingByPrice(Integer orderDetailId , Integer fitId){
		String hql = "select obj.id from CotPriceFittings obj where obj.priceDetailId="+orderDetailId+" and obj.fittingId="+fitId;
		List list = this.getBaseDao().find(hql);
		if(list.size()>0){
			return null;
		}else{
			return (CotFittings) this.getBaseDao().getById(CotFittings.class, fitId);
		}
	}
	
	//查询订单明细是否已含有该配件
	public CotFittings getFittingByOrder(Integer orderDetailId , Integer fitId){
		String hql = "select obj.id from CotOrderFittings obj where obj.orderDetailId="+orderDetailId+" and obj.fittingId="+fitId;
		List list = this.getBaseDao().find(hql);
		if(list.size()>0){
			return null;
		}else{
			return (CotFittings) this.getBaseDao().getById(CotFittings.class, fitId);
		}
	}
	
	// 根据父货号和子货号字符串合并子货号
	public String[] getComChilds(String ids, Integer currenyId) {
		String[] res = new String[22];
		String hql = "from CotElementsNew obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		List<?> list = this.getBaseDao().find(hql);
		String eleIds = "";
		String chn = "";
		String en = "";
		Float price = 0f;
		Float gw = 0f;
		Float nw = 0f;
		Float cbm = 0f;
		String taoUnit = "S/" + list.size();
		Float priceFac = 0f;
		Float priceOut = 0f;
		String eleName = "";
		String eleNameEn = "";
		Long bIO = 0l;
		Long bMO = 0l;
		Long bOO = 0l;
		Float bOL = 0f;
		Float bOW = 0f;
		Float bOH = 0f;
		Float bOLInch = 0f;
		Float bOWInch = 0f;
		Float bOHInch = 0f;
		for (int i = 0; i < list.size(); i++) {
			CotElementsNew elementsNew = (CotElementsNew) list.get(i);
			eleIds += elementsNew.getEleId() + "/";// 货号
			if (elementsNew.getEleSizeDesc() != null
					&& !"".equals(elementsNew.getEleSizeDesc())) {
				chn += elementsNew.getEleSizeDesc() + "/";// 中文规格
			}
			if (elementsNew.getEleInchDesc() != null
					&& !"".equals(elementsNew.getEleInchDesc())) {
				en += elementsNew.getEleInchDesc() + "/";// 英文规格
			}
			if (elementsNew.getBoxGrossWeigth() != null) {
				gw += elementsNew.getBoxGrossWeigth();// 毛重
			}
			if (elementsNew.getBoxNetWeigth() != null) {
				nw += elementsNew.getBoxNetWeigth();// 净重
			}
			if (elementsNew.getBoxCbm() != null) {
				if (elementsNew.getBoxCbm() > cbm) {
					cbm = elementsNew.getBoxCbm();// 取最大CBM
				}
			}
			if (elementsNew.getBoxIbCount() != null) {
				if (elementsNew.getBoxIbCount() > bIO) {
					bIO = elementsNew.getBoxIbCount();// 取最大内包装数
				}
			}
			if (elementsNew.getBoxMbCount() != null) {
				if (elementsNew.getBoxMbCount() > bMO) {
					bMO = elementsNew.getBoxMbCount();// 取最大中包装数
				}
			}
			if (elementsNew.getBoxObCount() != null) {
				if (elementsNew.getBoxObCount() > bOO) {
					bOO = elementsNew.getBoxObCount();// 取最大外包装数
				}
			}
			if (elementsNew.getBoxObL() != null) {
				if (elementsNew.getBoxObL() > bOL) {
					bOL = elementsNew.getBoxObL();// 取最大外箱长
				}
			}
			if (elementsNew.getBoxObLInch() != null) {
				if (elementsNew.getBoxObLInch() > bOLInch) {
					bOLInch = elementsNew.getBoxObLInch();// 取最大外箱英寸长
				}
			}
			if (elementsNew.getBoxObW() != null) {
				if (elementsNew.getBoxObW() > bOW) {
					bOW = elementsNew.getBoxObW();// 取最大外箱宽
				}
			}
			if (elementsNew.getBoxObWInch() != null) {
				if (elementsNew.getBoxObWInch() > bOWInch) {
					bOWInch = elementsNew.getBoxObWInch();// 取最大外箱英寸宽
				}
			}
			if (elementsNew.getBoxObH() != null) {
				if (elementsNew.getBoxObH() > bOH) {
					bOH = elementsNew.getBoxObH();// 取最大外箱高
				}
			}
			if (elementsNew.getBoxObHInch() != null) {
				if (elementsNew.getBoxObHInch() > bOHInch) {
					bOHInch = elementsNew.getBoxObHInch();// 取最大外箱英寸高
				}
			}
			if (elementsNew.getPriceOut() != null
					&& elementsNew.getPriceOutUint() != null) {
				float newPrice = this.updatePrice(elementsNew.getPriceOut(),
						elementsNew.getPriceOutUint(), currenyId);
				price += newPrice;
			}
			if (elementsNew.getPriceOut() != null) {
				priceOut += elementsNew.getPriceOut();
			}
			if (elementsNew.getPriceFac() != null) {
				float newPrice = this.updatePrice(elementsNew.getPriceFac(),
						elementsNew.getPriceFacUint(), currenyId);
				priceFac += newPrice;
			}
			if (elementsNew.getEleName() != null
					&& !"".equals(elementsNew.getEleName())) {
				eleName += elementsNew.getEleName() + "/";// 中文品名
			}
			if (elementsNew.getEleNameEn() != null
					&& !"".equals(elementsNew.getEleNameEn())) {
				eleNameEn += elementsNew.getEleNameEn() + "/";// 英文品名
			}
		}
		res[0] = eleIds.substring(0, eleIds.length() - 1);
		res[1] = price.toString();
		if (!chn.equals("")) {
			res[2] = chn.substring(0, chn.length() - 1);
		} else {
			res[2] = "";
		}
		if (!en.equals("")) {
			res[3] = en.substring(0, en.length() - 1);
		} else {
			res[3] = "";
		}
		res[4] = cbm.toString();
		res[5] = gw.toString();
		res[6] = nw.toString();
		res[7] = taoUnit;
		res[8] = priceFac.toString();
		if (!eleName.equals("")) {
			res[9] = eleName.substring(0, eleName.length() - 1);
		} else {
			res[9] = "";
		}
		res[10] = bIO.toString();
		res[11] = bMO.toString();
		res[12] = bOO.toString();
		res[13] = bOL.toString();
		res[14] = bOW.toString();
		res[15] = bOH.toString();
		res[16] = list.size() + "";
		if (!eleNameEn.equals("")) {
			res[17] = eleNameEn.substring(0, eleNameEn.length() - 1);
		} else {
			res[17] = "";
		}
		res[18] = bOLInch.toString();
		res[19] = bOLInch.toString();
		res[20] = bOLInch.toString();
		res[21] = priceOut.toString();
		return res;
	}
	
	// 查询客户
	public CotCustomer getCustById(Integer custId) {
		CotCustomer cust = (CotCustomer) this.getBaseDao().getById(
				CotCustomer.class, custId);
		CotCustomer cotCustomer = (CotCustomer) SystemUtil.deepClone(cust);
		if (cotCustomer != null) {
			cotCustomer.setPicImg(null);
			cotCustomer.setCustImg(null);
			cotCustomer.setCustomerClaim(null);
			cotCustomer.setCotCustContacts(null);
			cotCustomer.setCustomerVisitedLogs(null);
		}
		return cotCustomer;
	}
	
	// 查询厂家
	public CotFactory getFactoryById(Integer facId) {
		CotFactory cotFactory = (CotFactory) this.getBaseDao().getById(
				CotFactory.class, facId);
		return cotFactory;
	}
	
	// 查询联系人
	public CotContact getCotContactById(Integer facId) {
		CotContact cotContact = (CotContact) this.getBaseDao().getById(
				CotContact.class, facId);
		return cotContact;
	}
	
	// 保存并获取邮件对象Id
	@SuppressWarnings("deprecation")
	public String saveMail(Map<?, ?> map) {
		String type = map.get("type").toString();
		String custEmail = map.get("custEmail").toString();
		Integer custId = Integer.parseInt(map.get("custId").toString());
		String custName = map.get("custName").toString();
		Integer pId = Integer.parseInt(map.get("pId").toString());
		String pNo = map.get("pNo").toString();
		String reportTemple = map.get("reportTemple").toString();
		String printType = map.get("printType").toString();
		String headerFlag = map.get("headerFlag").toString();
		String exlSheet = map.get("exlSheet").toString();

		WebContext ctx = WebContextFactory.get();
		String rptXMLpath = ctx.getHttpServletRequest().getRealPath("/")
				+ File.separator + reportTemple;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("IMG_PATH", ctx.getHttpServletRequest().getRealPath("/"));
		paramMap.put("HEADER_PER_PAGE", headerFlag);
		paramMap.put("exlSheet", exlSheet);
		
		if("order".equals(type)){
			paramMap.put("STR_SQL", "obj.ORDER_ID=" + pId);
		}
		if("price".equals(type)){
			paramMap.put("STR_SQL", "obj.PRICE_ID=" + pId);
		}
		if("given".equals(type)){
			paramMap.put("STR_SQL", "obj.GIVEN_ID=" + pId);
		}
		if("orderout".equals(type)){
			paramMap.put("STR_SQL", "obj.ORDER_ID=" + pId);
		}
		if("fitting".equals(type)){
			paramMap.put("STR_SQL", "obj.ORDER_ID=" + pId);
		}
		if("orderfac".equals(type)){
			paramMap.put("STR_SQL", "obj.ORDER_ID=" + pId);
		}
		if("packing".equals(type)){
			paramMap.put("STR_SQL", "obj.packing_orderId=" + pId);
		}
		if("split".equals(type)){
			paramMap.put("STR_SQL", "obj.ORDER_ID=" + pId);
		}
		
		Object empNo = ctx.getSession().getAttribute("empNo");
		if (empNo == null) {
			return "";

		}
//		CotEmps emp = (CotEmps) ctx.getSession().getAttribute("emp");
		String servicePath = ctx.getServletContext().getRealPath("/"+Constants.MAIL_ATTACH_EXCEL_PATH);
		File file = new File(servicePath + File.separator + empNo.toString());
		if (!file.exists()) {
			file.mkdirs();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String attachPath = "";
		String attachName = "";
		// 导出报价单
		if ("XLS".equals(printType)) {
			attachName = sdf.format(new java.util.Date()) + "_" + pNo + ".xls";
			// 设置导出路径
			attachPath = empNo.toString() + File.separator + attachName;// 附件路径
			String exportPath = servicePath + File.separator + attachPath;// 实际路径
			this.getExportRptDao().exportRptToXLS(rptXMLpath, exportPath,
					paramMap);
		}
		else if ("PDF".equals(printType)) {
			attachName = sdf.format(new java.util.Date()) + "_" + pNo + ".pdf";
			// 设置导出路径
			attachPath = empNo.toString() + File.separator + attachName;// 附件路径
			String exportPath = servicePath + File.separator + attachPath;// 实际路径
			this.getExportRptDao().exportRptToPDF(rptXMLpath, exportPath,
					paramMap);
		}else {
			return "";
		}
		
		CotMail cotMail = new CotMail();
		cotMail.setCustId(custId);
		cotMail.setToName(custName);
		cotMail.setToUrl(custEmail);
		cotMail.setSubject(attachName);
		
		CotMailAttach mailAttach = new CotMailAttach();
		mailAttach.setName(attachName);
		mailAttach.setUrl(Constants.MAIL_ATTACH_EXCEL_PATH+File.separator+attachPath);
		File attach = new File(servicePath+File.separator+attachPath);
		Long size = attach.length();
		mailAttach.setSize(size.intValue());
		cotMail.getAttachs().add(mailAttach);
		
		String key = "mailattach"+attachName;
		ctx.getSession().setAttribute(key, cotMail);
		return key;
	}
	
	// 保存并获取邮件对象Id
	public String savePanMail(Map<?, ?> map) {
		String custEmail = map.get("custEmail").toString();
		Integer custId = Integer.parseInt(map.get("custId").toString());
		String custName = map.get("custName").toString();
		String pNo = map.get("pNo").toString();

		WebContext ctx = WebContextFactory.get();
		
		Object empNo = ctx.getSession().getAttribute("empNo");
		if (empNo == null) {
			return "";

		}
		
		String attachName = "";
		
		CotMail cotMail = new CotMail();
		cotMail.setCustId(custId);
		cotMail.setToName(custName);
		cotMail.setToUrl(custEmail);
		cotMail.setSubject(attachName);
		
		String key = "mailattach"+attachName;
		ctx.getSession().setAttribute(key, cotMail);
		return key;
	}
	
	// 查询客户VO
	public List<?> getCustVO(QueryInfo queryInfo) {
		try {
			List<?> list = this.getBaseDao().findRecordsJDBC(queryInfo);
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	// 根据id查找视图对象
	public VDetailStatusId getVDetailStatusIdById(Integer id){
		VDetailStatusId vDetailStatusId = (VDetailStatusId) this.getBaseDao().getById(VDetailStatusId.class, id);		
		return vDetailStatusId;
	}
	public void saveOrUpdateVDetailStatusId(VDetailStatusId vDetailStatusId){
		List list = new ArrayList();
		list.add(vDetailStatusId);
		this.getBaseDao().saveOrUpdateRecords(list);
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
	
	//查询订单是否有提问,以及是否全部答复
	public String getListData(QueryInfo queryInfo,String type,String flag)
			throws DAOException {
		GridServerHandler gd = null;
		if (queryInfo.getExcludes() != null)
			gd = new GridServerHandler(queryInfo.getExcludes());
		else
			gd = new GridServerHandler();
		int count = this.getBaseDao().getRecordsCount(queryInfo);
		List res = this.getBaseDao().findRecords(queryInfo);
		if("SAMPLE".equals(type) || "PACKAGE".equals(type) || "SAMPLEOUT".equals(type)|| "QC".equals(type)|| "OUT".equals(type)){
			int gp = 1;
			if("PACKAGE".equals(type)){
				gp = 2;
			}
			if("SAMPLEOUT".equals(type)){
				gp = 3;
			}
			if("QC".equals(type)){
				gp = 4;
			}
			if("OUT".equals(type)){
				gp = 5;
			}
			Iterator<?> it = res.iterator();
			while (it.hasNext()) {
				int tg=0;
				VDetailStatusId vDetailStatusId = (VDetailStatusId) it.next();
				String hql ="select obj.id from CotQuestion obj where obj.orderId="+vDetailStatusId.getOrderId()+" and obj.flag="+gp;
				List tmp=this.getBaseDao().find(hql);
				if(tmp.size()==0){
					tg=1;
				}else{
					boolean chk=false;
					for (int i = 0; i < tmp.size(); i++) {
						String anhql ="select obj.id from CotAnwser obj where obj.questionId="+tmp.get(i);
						List anTmp=this.getBaseDao().find(anhql);
						if(anTmp.size()==0){
							chk=true;
							break;
						}
					}
					if(chk){
						tg=2;
					}else{
						tg=3;
					}
				}
				if(flag!=null && !flag.equals("0") && !flag.equals("")){
					if(tg!=Integer.parseInt(flag)){
						it.remove();
						count--;
					}
				}
				vDetailStatusId.setFlag(tg);
				
				//判断delivery date是否要显示红色
				String shipId = vDetailStatusId.getShipportId()==null?"0":vDetailStatusId.getShipportId().toString();
				String tarId = vDetailStatusId.getTargetportId()==null?"0":vDetailStatusId.getTargetportId().toString();
				Integer temp = this.findDays(shipId,tarId);
				
				java.util.Date date = this.addDate(vDetailStatusId.getOrderLcDate(), temp);
				java.util.Date newDate = vDetailStatusId.getSendTime();
				if(newDate.getTime()<date.getTime()){
					vDetailStatusId.setChk(1);
				}else{
					java.util.Date date2 = this.addDate(date, 37);
					if(newDate.getTime()>=date2.getTime()){
						vDetailStatusId.setChk(1);
					}
				}
			}
		}

		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
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
		List list =this.getBaseDao().find(hql);
		if(list!=null && list.size()>0){
			return (Integer) list.get(0);
		}else{
			return 40;
		}
	}
	
	// 查询VO记录
	public List<?> getOrderFacVOList(QueryInfo queryInfo) {
		List<CotOrderVO> listVo = new ArrayList<CotOrderVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotOrderVO orderVO = new CotOrderVO();
				Object[] obj = (Object[]) list.get(i);
				orderVO.setId((Integer) obj[0]);
				orderVO.setOrderNo((String) obj[1]);
				orderVO.setOrderTimeFac((Date) obj[2]);
				orderVO.setEmpsName((String) obj[3]);
				orderVO.setEleId((String) obj[4]);
				orderVO.setEleNameEn((String) obj[5]);
				orderVO.setBoxCount((Long) obj[6]);
				orderVO.setTotalMoney((Float) obj[7]);
				orderVO.setPriceFac((Float) obj[8]);
				orderVO.setCurrencyId((Integer) obj[9]);
				orderVO.setCustNo((String) obj[10]);
				orderVO.setBoxTypeId((Integer) obj[11]);
				listVo.add(orderVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
	
	// 根据报价编号字符串查找报价明细
	@SuppressWarnings("unchecked")
	public List<CotOrderFacdetail> findOrderFacDetailByIds(String ids) {
		String hql = "from CotOrderFacdetail obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ") order by obj.sortNo";
		List<CotOrderFacdetail> list = this.getBaseDao().find(hql);
//		Iterator<?> it = list.iterator();
//		while (it.hasNext()) {
//			CotOrderDetail cotOrderDetail = (CotOrderDetail) it.next();
//			cotOrderDetail.setPicImg(null);
//			//List facList = getDicList("factory");
//			for (int i = 0; i < facList.size(); i++) {
//				CotFactory cotFactory = (CotFactory) facList.get(i);
//				if (cotOrderDetail.getFactoryId() != null
//						&& cotFactory.getId().intValue() == cotOrderDetail
//								.getFactoryId().intValue()) {
//					cotOrderDetail.setFactoryShortName(cotFactory
//							.getShortName());
//				}
//			}
//		}
		return list;
	}
	
	//查询公司简称+"\n"+公司地址
	public String findCompanyStr(Integer companyId){
		String hql = "select obj.companyShortName,obj.companyEnAddr from CotCompany obj where obj.id="+companyId;
		List<?> list = this.getBaseDao().find(hql);
		if (list.size() != 0) {
			Object[] obj = (Object[]) list.get(0);
			String companyShortName = (String)obj[0];
			String companyEnAddr = (String)obj[1];
			if(companyShortName==null) 
				companyShortName="";
			if(companyEnAddr==null) 
				companyEnAddr="";
			return  companyShortName+" \n"+companyEnAddr;
		} else {
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.QueryService#getListBy(java.lang.String)
	 */
	@Override
	public List getListBy(String sql) {
		return this.getBaseDao().find(sql);
	}

}
