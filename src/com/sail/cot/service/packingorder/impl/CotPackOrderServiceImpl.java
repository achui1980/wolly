package com.sail.cot.service.packingorder.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFittingOrder;
import com.sail.cot.domain.CotFittingsAnys;
import com.sail.cot.domain.CotFittingsOrderdetail;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFittings;
import com.sail.cot.domain.CotOrderMb;
import com.sail.cot.domain.CotPackMb;
import com.sail.cot.domain.CotPackingAnys;
import com.sail.cot.domain.CotPackingOrder;
import com.sail.cot.domain.CotPackingOrderdetail;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.query.QueryInfo;

import com.sail.cot.service.packingorder.CotPackOrderService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.ListSort;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotPackOrderServiceImpl implements CotPackOrderService {

	private Logger logger = Log4WebUtil
			.getLogger(CotPackOrderServiceImpl.class);

	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	// =======================配件采购=====================================

	// 得到总记录数
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 查询记录
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getCotBaseDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有包材名称
	public Map<?, ?> getBoxTypeNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotBoxType");
		for (int i = 0; i < list.size(); i++) {
			CotBoxType cotBoxType = (CotBoxType) list.get(i);
			map.put(cotBoxType.getId().toString(), cotBoxType.getTypeName());
		}
		return map;
	}

	// 查询所有包材名称
	public Map<?, ?> getPackValueMap(HttpServletRequest request) {
		String typeName = "";
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotBoxPacking");
		for (int i = 0; i < list.size(); i++) {
			CotBoxPacking cotBoxPacking = (CotBoxPacking) list.get(i);
			map.put(cotBoxPacking.getId().toString(), cotBoxPacking.getValue());
		}
		return map;
	}

	// 查询所有包材类型
	public Map<?, ?> getPackTypeMap(HttpServletRequest request) {
		String typeName = "";
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotBoxPacking");
		for (int i = 0; i < list.size(); i++) {
			CotBoxPacking cotBoxPacking = (CotBoxPacking) list.get(i);
			if (cotBoxPacking.getType().equals("PB")) {
				typeName = "产品包装";
			}
			if (cotBoxPacking.getType().equals("IB")) {
				typeName = "内盒包装";
			}
			if (cotBoxPacking.getType().equals("MB")) {
				typeName = "中盒包装";
			}
			if (cotBoxPacking.getType().equals("OB")) {
				typeName = "外箱包装";
			}
			map.put(cotBoxPacking.getId().toString(), typeName);
		}
		return map;
	}

	// 查询配件厂家
	public Map<?, ?> getPackFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getCotBaseDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			if (cotFactory.getFactroyTypeidLv1() != null
					&& cotFactory.getFactroyTypeidLv1() == 3) {
				map.put(cotFactory.getId().toString(), cotFactory
						.getShortName());
			}
		}
		return map;
	}

	// 查询所有公司
	public Map<?, ?> getCompanyNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("company");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany
					.getCompanyShortName());
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

	// 根据编号获得对象
	public CotPackingOrder getPackOrderById(Integer id) {
		CotPackingOrder cotOrder = (CotPackingOrder) this.getCotBaseDao().getById(
				CotPackingOrder.class, id);
		if (cotOrder != null) {
			CotPackingOrder custClone = (CotPackingOrder) SystemUtil.deepClone(cotOrder);
			custClone.setCotPackingOrderdetails(null);
			return custClone;
		}
		return null;
	}

	// Action储存PackOrderMap
	@SuppressWarnings("unchecked")
	public void setPackOrderMapAction(HttpSession session, String rdm,
			CotPackingOrderdetail cotPackingOrderdetail) {
		Object obj = SystemUtil.getObjBySession(session, "packorder");
		if (obj == null) {
			TreeMap<String, CotPackingOrderdetail> packorderMap = new TreeMap<String, CotPackingOrderdetail>();
			packorderMap.put(rdm, cotPackingOrderdetail);
			SystemUtil.setObjBySession(session, packorderMap, "packorder");
		} else {
			TreeMap<String, CotPackingOrderdetail> packorderMap = (TreeMap<String, CotPackingOrderdetail>) obj;
			packorderMap.put(rdm, cotPackingOrderdetail);
			SystemUtil.setObjBySession(session, packorderMap, "packorder");
		}
	}

	// Action获取packOrderMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotPackingOrderdetail> getPackNoMapAction(
			HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "packorder");
		TreeMap<String, CotPackingOrderdetail> packorderMap = (TreeMap<String, CotPackingOrderdetail>) obj;
		return packorderMap;
	}

	// 通过配件编号修改Map中对应的配件采购明细
	public boolean updateMapValueByPackNo(String rdm, String property,
			String value) {
		CotPackingOrderdetail cotPackingOrderdetail = getPackNoMapValue(rdm);
		if (cotPackingOrderdetail == null)
			return false;
		try {
			BeanUtils.setProperty(cotPackingOrderdetail, property, value);
			this.setPackNoMap(rdm, cotPackingOrderdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 储存PackOrderMap
	@SuppressWarnings("unchecked")
	public void setPackNoMap(String rdm,
			CotPackingOrderdetail cotPackingOrderdetail) {
		Object obj = SystemUtil.getObjBySession(null, "packorder");
		if (obj == null) {
			TreeMap<String, CotPackingOrderdetail> packorderMap = new TreeMap<String, CotPackingOrderdetail>();
			packorderMap.put(rdm, cotPackingOrderdetail);
			SystemUtil.setObjBySession(null, packorderMap, "packorder");
		} else {
			TreeMap<String, CotPackingOrderdetail> packorderMap = (TreeMap<String, CotPackingOrderdetail>) obj;
			packorderMap.put(rdm, cotPackingOrderdetail);
			SystemUtil.setObjBySession(null, packorderMap, "packorder");
		}

	}

	// 获取FitOrderMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotPackingOrderdetail> getPackOrderMap() {
		Object obj = SystemUtil.getObjBySession(null, "packorder");
		TreeMap<String, CotPackingOrderdetail> packorderMap = (TreeMap<String, CotPackingOrderdetail>) obj;
		return packorderMap;
	}

	// 通过key获取packOrderMap的value
	public CotPackingOrderdetail getPackNoMapValue(String rdm) {
		TreeMap<String, CotPackingOrderdetail> packorderMap = this
				.getPackOrderMap();
		if (packorderMap != null) {
			CotPackingOrderdetail cotPackingOrderdetail = (CotPackingOrderdetail) packorderMap
					.get(rdm);
			return cotPackingOrderdetail;
		}
		return null;
	}

	// 清空PackOrderMap
	public void clearPackNoMap() {
		SystemUtil.clearSessionByType(null, "packorder");
	}

	// 清除PackOrderMap中PackNo对应的映射
	public void delPackNoMapByKey(String rdm) {
		TreeMap<String, CotPackingOrderdetail> packorderMap = this
				.getPackOrderMap();
		if (packorderMap != null) {
			if (packorderMap.containsKey(rdm)) {
				packorderMap.remove(rdm);
			}
		}
	}

	// 在Action中清除packOrderMap中packNo对应的映射
	public void delPackOrderMapByKeyAction(String rdm, HttpSession session) {
		TreeMap<String, CotPackingOrderdetail> packorderMap = this
				.getPackNoMapAction(session);
		if (packorderMap != null) {
			if (packorderMap.containsKey(rdm)) {
				packorderMap.remove(rdm);
			}
		}
	}

	// 保存主配件采购单
	public Integer saveOrUpdate(CotPackingOrder cotPackingOrder,
			String orderDate, String sendDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<CotPackingOrder> records = new ArrayList<CotPackingOrder>();

		// 保存主配件采购单
		try {
			if (orderDate != null && !"".equals(orderDate)) {
				cotPackingOrder.setOrderDate(new Date(sdf.parse(orderDate)
						.getTime()));
			}
			if (sendDate != null && !"".equals(sendDate)) {
				cotPackingOrder.setSendDate(new Date(sdf.parse(sendDate)
						.getTime()));
			}

			records.add(cotPackingOrder);
			this.getCotBaseDao().saveOrUpdateRecords(records);

		} catch (Exception e) {
			logger.error("保存配件采购单出错！");
			return null;
		}
		return cotPackingOrder.getId();
	}

	// 更新配件采购明细
	public Boolean modifyPackOrderDetail(List<CotPackingOrderdetail> detailList) {
		try {
			this.getCotBaseDao().updateRecords(detailList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 根据编号获得对象
	public CotPackingOrderdetail getPackingDetailById(Integer id) {
		return (CotPackingOrderdetail) this.getCotBaseDao().getById(
				CotPackingOrderdetail.class, id);
	}

	// 根据编号获得对象
	public CotPackingAnys getPackingAnysById(Integer id) {
		return (CotPackingAnys) this.getCotBaseDao().getById(
				CotPackingAnys.class, id);
	}

	// 根据送样明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids) {
		try {
			this.getCotBaseDao()
					.deleteRecordByIds(ids, "CotPackingOrderdetail");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 删除包材采购主单
	@SuppressWarnings( { "unchecked", "deprecation" })
	public Integer deletePackOrderList(List<?> orderIds) {
		String ids = "";
		for (int i = 0; i < orderIds.size(); i++) {
			ids += orderIds.get(i) + ",";
		}
		try {
			// 有应付帐款不删除
			String hqlAccount = "select obj.fkId from CotFinaceAccountdeal obj where obj.source='packorder' and obj.fkId in ("
					+ ids.substring(0, ids.length() - 1) + ")";
			List checkAccountList = this.getCotBaseDao().find(hqlAccount);
			if (checkAccountList.size() > 0) {
				for (int i = 0; i < checkAccountList.size(); i++) {
					Integer dId = (Integer) checkAccountList.get(i);
					orderIds.remove(dId);
				}
			}
			//如果有加减费用导入到出货不能再删除
			String hqlOther = "select b.fkId from CotFinaceOther a,CotFinaceOther b where a.outFlag=b.id and a.source='PackOther' and b.fkId in ("
				+ ids.substring(0, ids.length() - 1) + ")";
			List otherList = this.getCotBaseDao().find(hqlOther);
			if (otherList.size() > 0) {
				for (int i = 0; i < otherList.size(); i++) {
					Integer dId = (Integer) otherList.get(i);
					orderIds.remove(dId);
				}
			}

			// 获取操作人
			for (int i = 0; i < orderIds.size(); i++) {
				Integer fitId = (Integer) orderIds.get(i);
				// 清空配件分析表中id及设置分析状态
				this.modifyIdAndFlagByOrderId(fitId);
				// 删除该单关联的其他费用
				this.delOtherFee(fitId);
			}
			if (orderIds.size() > 0) {
				this.getCotBaseDao().deleteRecordByIds(orderIds,
						"CotPackingOrder");
			}

			if (checkAccountList.size() > 0) {
				return 1;
			}
			if (otherList.size()>0) {
				return 2;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除包材采购单出错");
			return 3;
		}
	}

	// 删除主单关联的其他费用
	public void delOtherFee(Integer packorderId) {

		String hql = " from CotFinaceOther obj where obj.source='packorder' and obj.fkId="
				+ packorderId;
		List<?> list = this.getCotBaseDao().find(hql);

		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			CotFinaceOther other = (CotFinaceOther) list.get(i);
			ids.add(other.getId());
		}
		try {
			if (ids != null && ids.size() > 0) {
				this.getCotBaseDao().deleteRecordByIds(ids, "CotFinaceOther");
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 修改配件分析表中的id及分析状态
	public void modifyPackIdAndFlag(Integer packDetailId) {

		List<CotPackingAnys> resList = new ArrayList<CotPackingAnys>();
		String hql = " from CotPackingAnys obj where obj.packingdetailId ="
				+ packDetailId;
		List<?> list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotPackingAnys cotPackingAnys = (CotPackingAnys) list.get(i);
			cotPackingAnys.setPackingOrderid(null);
			cotPackingAnys.setPackingdetailId(null);
			cotPackingAnys.setAnyFlag("U");

			resList.add(cotPackingAnys);

		}
		this.getCotBaseDao().updateRecords(resList);
	}

	// 根据主采购id修改配件分析表中的id及分析状态
	public void modifyIdAndFlagByOrderId(Integer packOrderId) {

		List<CotPackingAnys> resList = new ArrayList<CotPackingAnys>();
		String hql = " from CotPackingAnys obj where obj.packingOrderid ="
				+ packOrderId;
		List<?> list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotPackingAnys cotPackingAnys = (CotPackingAnys) list.get(i);
			cotPackingAnys.setPackingOrderid(null);
			cotPackingAnys.setPackingdetailId(null);
			cotPackingAnys.setAnyFlag("U");

			resList.add(cotPackingAnys);

		}
		this.getCotBaseDao().updateRecords(resList);
	}

	// 根据主采购id修改总金额
	public void modifyPackOrderAmount(Integer packOrderId, Double total) {

		List<CotPackingOrder> resList = new ArrayList<CotPackingOrder>();
		String hql = " from CotPackingOrder obj where obj.id =" + packOrderId;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() == 1) {
			CotPackingOrder cotPackingOrder = (CotPackingOrder) list.get(0);
			cotPackingOrder.setTotalAmount(cotPackingOrder.getTotalAmount()
					- total);

			cotPackingOrder.setCotPackingOrderdetails(null);

			resList.add(cotPackingOrder);
		}
		this.getCotBaseDao().updateRecords(resList);
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

	// 根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id) {
		CotFinaceOther finaceOther = (CotFinaceOther) this.getCotBaseDao()
				.getById(CotFinaceOther.class, id);
		return finaceOther;
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

	// 根据编号获得对象
	public CotFinaceAccountdeal getGivenDealById(Integer id) {
		CotFinaceAccountdeal deal = (CotFinaceAccountdeal) this.getCotBaseDao()
				.getById(CotFinaceAccountdeal.class, id);
		return deal;
	}

	// 删除应付帐款
	public Boolean deleteDealList(List<?> details) {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < details.size(); i++) {
			Integer dealId = (Integer) details.get(i);
			ids.add(dealId);
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

	// 修改其他费用状态 0：未生成 1: 已生成
	public void modifyFinOtherStatus(Integer fkId, String finaceName,
			String flag) {
		String hql = " from CotFinaceOther obj where obj.source='packorder' and obj.fkId="
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

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getCotBaseDao().getRecords(objName);
	}

	// 生成应付帐款单号
	public String createDealNo(Integer facId) {
//		Map idMap = new HashMap<String, Integer>();
//		idMap.put("CH", facId);
//		GenAllSeq seq = new GenAllSeq();
//		String finaceNo = seq.getAllSeqByType("fincaeaccountdealNo", idMap);
		CotSeqService seq =new CotSeqServiceImpl();
		String finaceNo =seq.getFinaceNeeGivenNo(facId);
		return finaceNo;
	}

	// 保存应付帐款单号
	public void savaDealNoSeq() {
	//	GenAllSeq seq = new GenAllSeq();
		CotSeqService seq =new CotSeqServiceImpl();
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
			dealDetail.setSource("packorder");
			dealDetail.setEmpId(cotEmps.getId());
			if (dealDetail.getFinaceName().equals("预付货款")) {
				dealDetail.setZhRemainAmount(0.0);
			} else {
				dealDetail.setZhRemainAmount(dealDetail.getAmount());
			}

			records.add(dealDetail);
			this.getCotBaseDao().saveRecords(records);
			// 保存单号
			this.savaDealNoSeq();
			// 修改其他费用状态 0：未生成 1: 已生成
			this.modifyFinOtherStatus(dealDetail.getFkId(), dealDetail
					.getFinaceName(), "add");

			if (!"".equals(priceScal) && !"".equals(prePrice)) {
				// 将预收货款比例和金额保存到订单
				CotFittingOrder packingOrder = (CotFittingOrder) this
						.getCotBaseDao().getById(CotFittingOrder.class,
								dealDetail.getFkId());
				CotFittingOrder obj = (CotFittingOrder) SystemUtil
						.deepClone(packingOrder);
				obj.setPrePrice(Float.parseFloat(prePrice));
				obj.setPriceScal(Float.parseFloat(priceScal));
				List<CotFittingOrder> list = new ArrayList<CotFittingOrder>();
				list.add(obj);
				this.getCotBaseDao().updateRecords(list);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
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

	// 根据id判断该生产合同是否已存在应付帐款
	public Integer getDealNumById(Integer packorderId) {
		String hql = " from CotFinaceAccountdeal obj where obj.fkId="
				+ packorderId;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() != 0) {
			return list.size();
		} else {
			return -1;
		}
	}

	// 判断生产合同是否有应付帐款记录
	public String checkIsHaveDeal(List<CotPackingOrder> orderList) {
		String orderNos = new String();
		for (int i = 0; i < orderList.size(); i++) {

			CotPackingOrder cotPackingOrder = this.getPackOrderById((orderList
					.get(i)).getId());

			List<?> list = this.getCotBaseDao().find(
					" from CotFinaceAccountdeal obj where obj.fkId="
							+ cotPackingOrder.getId());
			if (list.size() > 0) {
				orderNos += cotPackingOrder.getPackingOrderNo() + "、";
			}
		}
		if (orderNos.length() != 0) {
			return orderNos.substring(0, orderNos.length() - 1);
		} else {
			return null;
		}
	}

	// 计算单个价格
	public Double calOnePrice(Integer detailId, Float sizeL, Float sizeW,
			Float sizeH) {
		Double price = 0.0;
		String hql = " from CotPackingOrderdetail obj where obj.id=" + detailId;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() == 1) {
			CotPackingOrderdetail detail = (CotPackingOrderdetail) list.get(0);
			CotOrderDetail orderDetail = (CotOrderDetail) this.getCotBaseDao()
					.getById(CotOrderDetail.class, detail.getOrderDetailId());

			CotBoxPacking bx = (CotBoxPacking) this.getCotBaseDao().getById(
					CotBoxPacking.class, detail.getBoxPackingId());

			if (detail.getBoxTypeId() == 0) {
				orderDetail.setBoxPbL(sizeL);
				orderDetail.setBoxPbW(sizeW);
				orderDetail.setBoxPbH(sizeH);
			}
			if (detail.getBoxTypeId() == 1) {
				orderDetail.setBoxIbL(sizeL);
				orderDetail.setBoxIbW(sizeW);
				orderDetail.setBoxIbH(sizeH);
			}
			if (detail.getBoxTypeId() == 2) {
				orderDetail.setBoxMbL(sizeL);
				orderDetail.setBoxMbW(sizeW);
				orderDetail.setBoxMbH(sizeH);
			}
			if (detail.getBoxTypeId() == 3) {
				orderDetail.setBoxObL(sizeL);
				orderDetail.setBoxObW(sizeW);
				orderDetail.setBoxObH(sizeH);
			}
			if (detail.getBoxTypeId() == 4) {
				orderDetail.setPutL(sizeL);
				orderDetail.setPutW(sizeW);
				orderDetail.setPutH(sizeH);
			}

			price = this.sumPrice(bx.getFormulaIn(), orderDetail, bx
					.getMaterialPrice());
		}
		return price;
	}

	// =======================配件采购(结束)====================================

	// 根据订单主单编号获得明细
	public List getEleIdsFromOrderId(Integer orderId) {
		String hql = "from CotOrderDetail obj where obj.orderId=" + orderId;
		return this.getCotBaseDao().find(hql);
	}

	// 计算包材分析的单价(cau为计算公式)
	public Double sumPrice(String cau, CotOrderDetail detail,
			Double materialPrice) {
		if (cau == null || "".equals(cau.trim())) {
			return 0.0;
		}
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.00");
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		// 定义公式变量
		// 材料单价
		if (materialPrice != null) {
			evaluator.putVariable("materialPrice", materialPrice.toString());
		} else {
			evaluator.putVariable("materialPrice", "0");
		}
		// 产品长、宽、高
		if (detail.getBoxL() == null || detail.getBoxL().equals("")) {
			evaluator.putVariable("boxL", "0.0");
		} else {
			evaluator.putVariable("boxL", detail.getBoxL().toString());
		}
		if (detail.getBoxW() == null || detail.getBoxW().equals("")) {
			evaluator.putVariable("boxW", "0.0");
		} else {
			evaluator.putVariable("boxW", detail.getBoxW().toString());
		}
		if (detail.getBoxH() == null || detail.getBoxH().equals("")) {
			evaluator.putVariable("boxH", "0.0");
		} else {
			evaluator.putVariable("boxH", detail.getBoxH().toString());
		}

		// 产品长
		if (detail.getBoxPbL() != null) {
			evaluator.putVariable("boxPbL", detail.getBoxPbL().toString());
		} else {
			evaluator.putVariable("boxPbL", "0");
		}
		// 产品宽
		if (detail.getBoxPbW() != null) {
			evaluator.putVariable("boxPbW", detail.getBoxPbW().toString());
		} else {
			evaluator.putVariable("boxPbW", "0");
		}
		// 产品高
		if (detail.getBoxPbH() != null) {
			evaluator.putVariable("boxPbH", detail.getBoxPbH().toString());
		} else {
			evaluator.putVariable("boxPbH", "0");
		}
		// 内盒长
		if (detail.getBoxIbL() != null) {
			evaluator.putVariable("boxIbL", detail.getBoxIbL().toString());
		} else {
			evaluator.putVariable("boxIbL", "0");
		}
		// 内盒宽
		if (detail.getBoxIbW() != null) {
			evaluator.putVariable("boxIbW", detail.getBoxIbW().toString());
		} else {
			evaluator.putVariable("boxIbW", "0");
		}
		// 内盒高
		if (detail.getBoxIbH() != null) {
			evaluator.putVariable("boxIbH", detail.getBoxIbH().toString());
		} else {
			evaluator.putVariable("boxIbH", "0");
		}
		// 中盒长
		if (detail.getBoxMbL() != null) {
			evaluator.putVariable("boxMbL", detail.getBoxMbL().toString());
		} else {
			evaluator.putVariable("boxMbL", "0");
		}
		// 中盒宽
		if (detail.getBoxMbW() != null) {
			evaluator.putVariable("boxMbW", detail.getBoxMbW().toString());
		} else {
			evaluator.putVariable("boxMbW", "0");
		}
		// 中盒高
		if (detail.getBoxMbH() != null) {
			evaluator.putVariable("boxMbH", detail.getBoxMbH().toString());
		} else {
			evaluator.putVariable("boxMbH", "0");
		}

		// 外箱长
		if (detail.getBoxObL() != null) {
			evaluator.putVariable("boxObL", detail.getBoxObL().toString());
		} else {
			evaluator.putVariable("boxObL", "0");
		}
		// 外箱宽
		if (detail.getBoxObW() != null) {
			evaluator.putVariable("boxObW", detail.getBoxObW().toString());
		} else {
			evaluator.putVariable("boxObW", "0");
		}
		// 外箱高
		if (detail.getBoxObH() != null) {
			evaluator.putVariable("boxObH", detail.getBoxObH().toString());
		} else {
			evaluator.putVariable("boxObH", "0");
		}

		// 摆放长
		if (detail.getPutL() != null) {
			evaluator.putVariable("putL", detail.getPutL().toString());
		} else {
			evaluator.putVariable("putL", "0");
		}
		// 摆放宽
		if (detail.getPutW() != null) {
			evaluator.putVariable("putW", detail.getPutW().toString());
		} else {
			evaluator.putVariable("putW", "0");
		}
		// 摆放高
		if (detail.getPutH() != null) {
			evaluator.putVariable("putH", detail.getPutH().toString());
		} else {
			evaluator.putVariable("putH", "0");
		}

		try {
			String result = evaluator.evaluate(cau);
			return Double.parseDouble(df.format(Double.parseDouble(result)));
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	// 根据包材资料
	public List getBoxPackingById(Integer detailId, Integer boxPackingId) {
		CotOrderDetail detail = (CotOrderDetail) this.getCotBaseDao().getById(
				CotOrderDetail.class, detailId);
		CotBoxPacking bx = (CotBoxPacking) this.getCotBaseDao().getById(
				CotBoxPacking.class, boxPackingId);
		List list = new ArrayList();
		Double price = this.sumPrice(bx.getFormulaIn(), detail, bx
				.getMaterialPrice());
		list.add(price);
		list.add(bx.getFacId());
		return list;
	}

	// 根据包装类型获得包材资料
	public List getBoxPackingsByType(Integer type) {
		String str = "";
		if (type == -1) {
			str = "XX";
		}
		if (type == 0) {
			str = "PB";
		}
		if (type == 1) {
			str = "IB";
		}
		if (type == 2) {
			str = "MB";
		}
		if (type == 3) {
			str = "OB";
		}
		String hql = "from CotBoxPacking obj where obj.type='" + str + "'";
		return this.getCotBaseDao().find(hql);
	}

	// 保存
	public void addList(List<?> list) {
		try {
			this.getCotBaseDao().saveRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 修改
	public void updateList(List<?> list) {
		try {
			this.getCotBaseDao().updateRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void deleteList(List<Integer> ids) {
		// 如果是已采购的,不让删除
		Iterator<?> it = ids.iterator();
		while (it.hasNext()) {
			Integer id = (Integer) it.next();
			CotPackingAnys packingAnys = (CotPackingAnys) this.getCotBaseDao()
					.getById(CotPackingAnys.class, id);
			if ("C".equals(packingAnys.getAnyFlag())) {
				it.remove();
			}
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotPackingAnys");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 根据分析对象编号和长宽高重新计算单价
	public Double getNewPrice(Integer anyId, String size, Float value) {
		CotPackingAnys packingAnys = (CotPackingAnys) this.getCotBaseDao()
				.getById(CotPackingAnys.class, anyId);
		CotOrderDetail detail = (CotOrderDetail) this.getCotBaseDao().getById(
				CotOrderDetail.class, packingAnys.getOrderdetailId());
		CotBoxPacking boxPacking = (CotBoxPacking) this.getCotBaseDao()
				.getById(CotBoxPacking.class, packingAnys.getBoxPackingId());
		if (packingAnys.getBoxTypeId() == 0) {
			if ("sizeL".equals(size)) {
				detail.setBoxPbL(value);
			}
			if ("sizeW".equals(size)) {
				detail.setBoxPbW(value);
			}
			if ("sizeH".equals(size)) {
				detail.setBoxPbH(value);
			}
		}
		if (packingAnys.getBoxTypeId() == 1) {
			if ("sizeL".equals(size)) {
				detail.setBoxIbL(value);
			}
			if ("sizeW".equals(size)) {
				detail.setBoxIbW(value);
			}
			if ("sizeH".equals(size)) {
				detail.setBoxIbH(value);
			}
		}
		if (packingAnys.getBoxTypeId() == 2) {
			if ("sizeL".equals(size)) {
				detail.setBoxMbL(value);
			}
			if ("sizeW".equals(size)) {
				detail.setBoxMbW(value);
			}
			if ("sizeH".equals(size)) {
				detail.setBoxMbH(value);
			}
		}
		if (packingAnys.getBoxTypeId() == 3) {
			if ("sizeL".equals(size)) {
				detail.setBoxObL(value);
			}
			if ("sizeW".equals(size)) {
				detail.setBoxObW(value);
			}
			if ("sizeH".equals(size)) {
				detail.setBoxObH(value);
			}
		}
		if (packingAnys.getBoxTypeId() == 4) {
			if ("sizeL".equals(size)) {
				detail.setPutL(value);
			}
			if ("sizeW".equals(size)) {
				detail.setPutW(value);
			}
			if ("sizeH".equals(size)) {
				detail.setPutH(value);
			}
		}
		return this.sumPrice(boxPacking.getFormulaIn(), detail, boxPacking
				.getMaterialPrice());

	}

	// 根据报价编号查找报价单信息
	public CotOrder getOrderById(Integer id) {
		CotOrder cotOrder = (CotOrder) this.getCotBaseDao().getById(
				CotOrder.class, id);
		if (cotOrder != null) {
			CotOrder custClone = (CotOrder) SystemUtil.deepClone(cotOrder);
			custClone.setCotOrderDetails(null);
			custClone.setOrderMBImg(null);
			return custClone;
		}
		return null;
	}

	// 生成包材采购单号
//	public String createRecvNo(Integer facId, String orderNo, Integer custId) {
//		Map idMap = new HashMap<String, Integer>();
//		idMap.put("CH", facId);
//		idMap.put("kH", custId);
//		idMap.put("ORDERNO", orderNo);
//		GenAllSeq seq = new GenAllSeq();
//		String finaceNo = seq.getAllSeqByType("packingNo", idMap);
//		return finaceNo;
//	}

	// 保存包材采购单号
	public void savaSeq() {
//		GenAllSeq seq = new GenAllSeq();
//		seq.saveSeq("packingNo");
		CotSeqService cotSeqService=new CotSeqServiceImpl();
		cotSeqService.saveSeq("packing");
	}

	// 保存包材采购明细
	public List savePackOrderDetails(List listAnys,
			List<CotPackingOrderdetail> detailList, Integer orderId,
			Integer facId) {
		for (int i = 0; i < listAnys.size(); i++) {
			CotPackingAnys packingAnys = (CotPackingAnys) listAnys.get(i);
			CotPackingOrderdetail packingOrderdetail = new CotPackingOrderdetail();
			packingOrderdetail.setSortNo(i+1);
			packingOrderdetail.setEleId(packingAnys.getEleId());
			packingOrderdetail.setCustNo(packingAnys.getCustNo());
			packingOrderdetail.setEleName(packingAnys.getEleName());
			packingOrderdetail.setSizeL(packingAnys.getSizeL());
			packingOrderdetail.setSizeW(packingAnys.getSizeW());
			packingOrderdetail.setSizeH(packingAnys.getSizeH());
			packingOrderdetail.setBoxTypeId(packingAnys.getBoxTypeId());
			packingOrderdetail.setBoxPackingId(packingAnys.getBoxPackingId());
			packingOrderdetail.setPackCount(packingAnys.getPackCount()
					.intValue());
			packingOrderdetail.setPackPrice(packingAnys.getPackPrice());
			packingOrderdetail.setRemark(packingAnys.getPackRemark());
			packingOrderdetail.setAddTime(new Date());
			packingOrderdetail.setPackingOrderId(orderId);
			packingOrderdetail.setOrderId(packingAnys.getOrderId());// 订单编号
			packingOrderdetail.setOrderDetailId(packingAnys.getOrderdetailId());
			packingOrderdetail.setBoxRemark(packingAnys.getBoxRemark());
			packingOrderdetail.setPackAnysIds(packingAnys.getId() + ",");
			packingOrderdetail.setTotalAmmount(packingAnys.getPackPrice()
					* packingAnys.getPackCount());// 总金额
			packingOrderdetail.setBoxObCount(packingAnys.getBoxObCount());// 包装率
			detailList.add(packingOrderdetail);
		}
		return detailList;
	}

	// 更改分析表数据.添加采购单主单编号.明细编号,修改状态
	public void updatePackAnys(List list, List detailList) {
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			CotPackingAnys fa = (CotPackingAnys) list.get(i);
			map.put(fa.getId(), fa);
		}
		List listNew = new ArrayList();
		for (int i = 0; i < detailList.size(); i++) {
			CotPackingOrderdetail fd = (CotPackingOrderdetail) detailList
					.get(i);
			String[] ids = fd.getPackAnysIds().split(",");
			for (int j = 0; j < ids.length; j++) {
				CotPackingAnys fa = (CotPackingAnys) map.get(Integer
						.parseInt(ids[j]));
				fa.setAnyFlag("C");
				fa.setPackingOrderid(fd.getPackingOrderId());
				fa.setPackingdetailId(fd.getId());
				listNew.add(fa);
			}
		}
		this.getCotBaseDao().updateRecords(listNew);
	}

	// 将包材采购分析生成包材采购单,(ids有值只处理勾选的分析数据)
	public Boolean savePackOrderByAnys(Integer orderId, String ids) {
		// 0.获得订单数据
		CotOrder order = this.getOrderById(orderId);
		//获得订单麦标
		String hqlMb = "from CotOrderMb obj where obj.fkId=" + orderId;
		List listMb = this.getCotBaseDao().find(hqlMb);
		CotOrderMb orderMb = null;
		if (listMb != null && listMb.size() > 0) {
			orderMb = (CotOrderMb) listMb.get(0);
		}

		long isCheck = 1;
		// 取得业务配置的是否审核
		List listCfg = this.getCotBaseDao().getRecords("CotPriceCfg");
		if (listCfg != null && listCfg.size() > 0) {
			CotPriceCfg priceCfg = (CotPriceCfg) listCfg.get(0);
			if (priceCfg.getIsCheck() != null) {
				isCheck = priceCfg.getIsCheck();
			}
		}

		// 1.查询未生成采购单的分析数据,并且有厂家
		String hql = "from CotPackingAnys obj where obj.factoryId is not null and obj.anyFlag='U' and obj.orderId="
				+ orderId;
		if (!"".equals(ids)) {
			hql += " and obj.id in (" + ids.substring(0, ids.length() - 1)
					+ ")";
		}
		List list = this.getCotBaseDao().find(hql);
		// 2.该list中不同的供应商(key为厂家编号,value为分析数据LIST)
		Map<Integer, List<?>> map = new HashMap<Integer, List<?>>();
		for (int i = 0; i < list.size(); i++) {
			CotPackingAnys packingAnys = (CotPackingAnys) list.get(i);
			if (packingAnys.getFactoryId() == null) {
				continue;
			}
			Object obj = map.get(packingAnys.getFactoryId());
			if (obj == null) {
				List tempList = new ArrayList();
				tempList.add(packingAnys);
				map.put(packingAnys.getFactoryId(), tempList);
			} else {
				List tempList = (List) obj;
				tempList.add(packingAnys);
				map.put(packingAnys.getFactoryId(), tempList);
			}
		}
		// 3.查找该map中的厂家是否已经生成采购单
		Iterator<?> it = map.keySet().iterator();
		List detailList = new ArrayList();
		while (it.hasNext()) {
			Integer facId = (Integer) it.next();
			// 排除没有厂家的分析数据
			if (facId == null) {
				continue;
			}
			// 获得总金额
			List listAnys = map.get(facId);
			Double sum = 0.0;
			for (int i = 0; i < listAnys.size(); i++) {
				CotPackingAnys packingAnys = (CotPackingAnys) listAnys.get(i);
				packingAnys.setTotalAmount(packingAnys.getPackPrice()
						* packingAnys.getPackCount());
				sum += packingAnys.getTotalAmount();
			}
			String orderHql = "from CotPackingOrder obj where obj.orderId="
					+ orderId + " and obj.factoryId=" + facId;
			List listOrder = this.getCotBaseDao().find(orderHql);
			if (listOrder.size() == 0) {
				// 新建包材采购主单
				CotPackingOrder packingOrder = new CotPackingOrder();
				packingOrder.setOrderDate(order.getOrderTime());
				
//				String no = this.createRecvNo(facId, order.getOrderNo(), order
//						.getCustId());// 获取单号
				CotSeqService seq =new CotSeqServiceImpl();
				String no =seq.getPackingorderNo();
				
				packingOrder.setPackingOrderNo(no);
				packingOrder.setSendDate(order.getSendTime());
				packingOrder.setEmpId(order.getBussinessPerson());// 采购人员取订单的业务员
				packingOrder.setFactoryId(facId);
				packingOrder.setOrderNo(order.getOrderNo());
				packingOrder.setOrderId(orderId);
				packingOrder.setCompanyId(order.getCompanyId());
				// 是否审核
				if (isCheck == 1) {
					packingOrder.setOrderStatus(0);
				} else {
					packingOrder.setOrderStatus(9);
				}
				packingOrder.setTotalAmount(sum);
				packingOrder.setRealMoney(sum);

				//保存麦明细
				packingOrder.setOrderCM(order.getOrderCM());
				packingOrder.setOrderNM(order.getOrderNM());
				packingOrder.setOrderZHM(order.getOrderZHM());
				packingOrder.setOrderZM(order.getOrderZM());

				List records = new ArrayList();
				records.add(packingOrder);
				// 保存主采购单
				try {
					this.getCotBaseDao().saveRecords(records);
					this.savaSeq();// 保存单号

					//保存包材麦标
					if (orderMb != null) {
						List mb = new ArrayList();
						for (int i = 0; i < records.size(); i++) {
							CotPackingOrder tempPack = (CotPackingOrder) records
									.get(i);
							CotPackMb packMb = new CotPackMb();
							packMb.setFkId(tempPack.getId());
							packMb.setPicSize(orderMb.getPicSize());
							packMb.setPicImg(orderMb.getPicImg());
							mb.add(packMb);
						}
						this.getCotBaseDao().saveRecords(mb);
					}

					// 生成采购明细,添加到detailList中
					savePackOrderDetails(listAnys, detailList, packingOrder
							.getId(), facId);
				} catch (DAOException e) {
					e.printStackTrace();
				}
			} else {
				CotPackingOrder forder = (CotPackingOrder) listOrder.get(0);
				forder.setCotPackingOrderdetails(null);
				// 生成采购明细,添加到detailList中
				savePackOrderDetails(listAnys, detailList, forder.getId(),
						facId);
				// 更新采购主单总金额
				forder.setTotalAmount(forder.getTotalAmount() + sum);
				forder.setRealMoney(forder.getRealMoney() + sum);
				List records = new ArrayList();
				records.add(forder);
				this.getCotBaseDao().updateRecords(records);
			}
		}

		// 保存采购明细
		try {
			this.getCotBaseDao().saveRecords(detailList);
			// 5.更改分析表数据.添加采购单主单编号.明细编号,修改状态
			updatePackAnys(list, detailList);
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// 根据订单编号取得订单号
	public String getOrderNoByOrdeId(Integer orderId) {
		String hql = "select obj.orderNo from CotOrder obj where obj.id="
				+ orderId;
		List list = this.getCotBaseDao().find(hql);
		if (list != null && list.size() > 0) {
			return (String) list.get(0);
		} else {
			return "";
		}
	}

	// 修改订单审核状态
	public void updateOrderStatus(Integer orderId, Integer orderStatus) {
		CotPackingOrder packingOrder = (CotPackingOrder) this.getCotBaseDao()
				.getById(CotPackingOrder.class, orderId);
		packingOrder.setCotPackingOrderdetails(null);
		packingOrder.setOrderStatus(orderStatus);
		List list = new ArrayList();
		list.add(packingOrder);
		this.getCotBaseDao().updateRecords(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.packingorder.CotPackOrderService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

	// 编辑包材时计算单价
	public Double getPackPriceByType(CotOrderDetail detail, Integer boxPackingId) {
		CotBoxPacking bx = (CotBoxPacking) this.getCotBaseDao().getById(
				CotBoxPacking.class, boxPackingId);
		Double price = this.sumPrice(bx.getFormulaIn(), detail, bx
				.getMaterialPrice());

		return price;
	}

	// 查找包材麦标PicImg
	public byte[] getPicImgByOrderId(Integer fkId) {
		List<?> list = this.getCotBaseDao().find(
				"from CotPackMb obj where obj.fkId=" + fkId);
		CotPackMb cotPackMb = new CotPackMb();
		if (list.size() == 1) {
			cotPackMb = (CotPackMb) list.get(0);
		}
		if (cotPackMb != null) {
			return cotPackMb.getPicImg();
		}
		return null;
	}

	// 删除唛标明细图片
	public boolean deleteMBPicImg(Integer orderId) {
		String classPath = CotPackOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		String hql = "from CotPackMb obj where obj.fkId=" + orderId;
		List list = this.getCotBaseDao().find(hql);
		CotPackMb cotPackMb = (CotPackMb) list.get(0);

		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotPackMb.setPicImg(b);
			cotPackMb.setPicSize(b.length);
			this.getCotBaseDao().update(cotPackMb);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除唛标图片错误!");
			return false;
		}
	}

	//从订单拷贝唛头信息
	public String[] updatePackMb(Integer packId) throws DAOException {
		CotPackingOrder packingOrder = (CotPackingOrder) this.getCotBaseDao()
				.getById(CotPackingOrder.class, packId);
		CotOrder order = (CotOrder) this.getCotBaseDao().getById(
				CotOrder.class, packingOrder.getOrderId());
		//获得订单麦标
		// 获得订单麦标
		String hqlMb = "from CotOrderMb obj where obj.fkId="
				+ packingOrder.getOrderId();
		List listMb = this.getCotBaseDao().find(hqlMb);
		CotOrderMb orderMb = null;
		if (listMb != null && listMb.size() > 0) {
			orderMb = (CotOrderMb) listMb.get(0);
		}
		String faclMb = "update CotPackMb obj set obj.picImg=:picImg,obj.picSize=:picSize  where obj.fkId=:id";
		Map map = new HashMap();
		map.put("picImg", orderMb.getPicImg());
		map.put("picSize", orderMb.getPicSize());
		map.put("id", packId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(faclMb);
		int result = this.getCotBaseDao().executeUpdate(queryInfo, map);
		// 更新正册麦
		String temp = "update CotPackingOrder obj set "
				+ "obj.orderCM=:orderCM," + "obj.orderZM=:orderZM,"
				+ "obj.orderZHM=:orderZHM," + "obj.orderNM=:orderNM  "
				+ "where obj.id=:id";
		Map mapTp = new HashMap();
		mapTp.put("orderCM", order.getOrderCM());
		mapTp.put("orderZM", order.getOrderZM());
		mapTp.put("orderZHM", order.getOrderZHM());
		mapTp.put("orderNM", order.getOrderNM());
		mapTp.put("id", packId);
		QueryInfo query2 = new QueryInfo();
		query2.setSelectString(temp);
		int result2 = this.getCotBaseDao().executeUpdate(query2, mapTp);
		String[] str = new String[4];
		str[0] = order.getOrderCM();
		str[1] = order.getOrderZM();
		str[2] = order.getOrderZHM();
		str[3] = order.getOrderNM();
		return str;
	}

	// 更新麦标图片
	public void updateMBImg(String filePath, Integer mainId) {
		String hql = "from CotPackMb obj where obj.fkId=" + mainId;
		List<?> list = this.getCotBaseDao().find(hql);
		CotPackMb cotPackMb = new CotPackMb();
		if (list.size() == 1) {
			cotPackMb = (CotPackMb) list.get(0);
		}
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotPackMb.setPicImg(b);
			cotPackMb.setFkId(mainId);
			cotPackMb.setPicSize(b.length);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			List<CotPackMb> rec = new ArrayList<CotPackMb>();
			rec.add(cotPackMb);
			this.getCotBaseDao().saveOrUpdateRecords(rec);
		} catch (Exception e) {
			logger.error("更新包材的麦标图片错误!");
		}
	}

	// 分析订单明细集合,生成包材采购分析数据
	public boolean savePackAnysAgain(Integer orderId) {
		// 1查询是否有采购的数据,有的话删除采购单,更改分析数据状态为U
		String delOrderHql = "from CotPackingAnys obj where obj.anyFlag='C' and obj.orderId="
				+ orderId;
		List delOrder = this.getCotBaseDao().find(delOrderHql);
		List delOrderIds = new ArrayList();
		List newOds = new ArrayList();
		for (int i = 0; i < delOrder.size(); i++) {
			CotPackingAnys packingAnys = (CotPackingAnys) delOrder.get(i);
			delOrderIds.add(packingAnys.getPackingOrderid());
			packingAnys.setAnyFlag("U");
			newOds.add(packingAnys);
		}

		if (delOrderIds != null && delOrderIds.size() > 0) {
			try {
				this.getCotBaseDao().deleteRecordByIds(delOrderIds,
						"CotPackingOrder");
				this.getCotBaseDao().updateRecords(newOds);
			} catch (DAOException e) {
				e.printStackTrace();
				return false;
			}
		}

		// 2.删除未采购的分析数据
		String delHql = "select obj.id from CotPackingAnys obj where obj.anyFlag='U' and obj.orderId="
				+ orderId;
		List delIds = this.getCotBaseDao().find(delHql);
		try {
			if (delIds != null && delIds.size() > 0) {
				this.getCotBaseDao()
						.deleteRecordByIds(delIds, "CotPackingAnys");
			}
		} catch (DAOException e1) {
			e1.printStackTrace();
			return false;
		}

		// 3.添加新的分析数据
		String sql = "from CotOrderDetail obj where obj.orderId=" + orderId;
		List orderDetails = this.getCotBaseDao().find(sql);
		List listAnys = new ArrayList();
		for (int i = 0; i < orderDetails.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) orderDetails.get(i);
			if (detail.getBoxPbTypeId() != null) {
				CotPackingAnys packingAnys = new CotPackingAnys();
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
				packingAnys.setBoxTypeId(0);// 产品0/内1/中2/外3 包材分析
				packingAnys.setOrderId(detail.getOrderId());
				packingAnys.setOrderdetailId(detail.getId());
				packingAnys.setAnyFlag("U");

				// 包装数量(自动进一)
				Double count = Math.ceil(detail.getBoxCount().doubleValue()
						/ detail.getBoxPbCount());
				packingAnys.setPackCount(count.longValue());
				// 包装材料
				packingAnys.setBoxPackingId(detail.getBoxPbTypeId());
				// 查询包装材料的单价
				CotBoxPacking boxPacking = (CotBoxPacking) this.getCotBaseDao()
						.getById(CotBoxPacking.class, detail.getBoxPbTypeId());
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
				listAnys.add(packingAnys);
			}
			if (detail.getBoxIbTypeId() != null) {
				CotPackingAnys packingAnys = new CotPackingAnys();
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
				packingAnys.setBoxTypeId(1);// 产品0/内1/中2/外3 包材分析
				packingAnys.setOrderId(detail.getOrderId());
				packingAnys.setOrderdetailId(detail.getId());
				packingAnys.setAnyFlag("U");

				Double count = Math.ceil(detail.getBoxCount().doubleValue()
						/ detail.getBoxIbCount());
				packingAnys.setPackCount(count.longValue());
				packingAnys.setBoxPackingId(detail.getBoxIbTypeId());
				CotBoxPacking boxPacking = (CotBoxPacking) this.getCotBaseDao()
						.getById(CotBoxPacking.class, detail.getBoxIbTypeId());
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
				listAnys.add(packingAnys);
			}
			if (detail.getBoxMbTypeId() != null) {
				CotPackingAnys packingAnys = new CotPackingAnys();
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
				packingAnys.setBoxTypeId(2);// 产品0/内1/中2/外3 包材分析
				packingAnys.setOrderId(detail.getOrderId());
				packingAnys.setOrderdetailId(detail.getId());
				packingAnys.setAnyFlag("U");
				// 包装数量(自动进一)
				Double count = Math.ceil(detail.getBoxCount().doubleValue()
						/ detail.getBoxMbCount());
				packingAnys.setPackCount(count.longValue());
				packingAnys.setBoxPackingId(detail.getBoxMbTypeId());
				CotBoxPacking boxPacking = (CotBoxPacking) this.getCotBaseDao()
						.getById(CotBoxPacking.class, detail.getBoxMbTypeId());
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
				listAnys.add(packingAnys);
			}
			if (detail.getBoxObTypeId() != null) {
				CotPackingAnys packingAnys = new CotPackingAnys();
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
				packingAnys.setBoxTypeId(3);// 产品0/内1/中2/外3 包材分析
				packingAnys.setOrderId(detail.getOrderId());
				packingAnys.setOrderdetailId(detail.getId());
				packingAnys.setAnyFlag("U");
				packingAnys.setPackCount(detail.getContainerCount());
				packingAnys.setBoxPackingId(detail.getBoxObTypeId());
				CotBoxPacking boxPacking = (CotBoxPacking) this.getCotBaseDao()
						.getById(CotBoxPacking.class, detail.getBoxObTypeId());
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
				listAnys.add(packingAnys);
			}
			if (detail.getInputGridTypeId() != null) {
				CotPackingAnys packingAnys = new CotPackingAnys();
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
				packingAnys.setBoxTypeId(4);// 产品0/内1/中2/外3 包材分析
				packingAnys.setOrderId(detail.getOrderId());
				packingAnys.setOrderdetailId(detail.getId());
				packingAnys.setAnyFlag("U");
				packingAnys.setPackCount(detail.getContainerCount());
				packingAnys.setBoxPackingId(detail.getInputGridTypeId());
				CotBoxPacking boxPacking = (CotBoxPacking) this.getCotBaseDao()
						.getById(CotBoxPacking.class,
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
				listAnys.add(packingAnys);
			}
		}
		try {
			if (listAnys.size() > 0) {
				this.getCotBaseDao().saveRecords(listAnys);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//判断其他费用是否存在
	public boolean findIsExistName(String name,Integer orderId,Integer recId){
		String hql="select obj.id from CotFinaceOther obj where obj.fkId=? and obj.source='packorder' and obj.finaceName=?";
		Object[] obj = new Object[2];
		obj[0] = orderId;
		obj[1] = name;
		List list =this.getCotBaseDao().queryForLists(hql, obj);
		if(list!=null && list.size()>0){
			Integer tId = (Integer) list.get(0);
			if(tId.intValue()!=recId.intValue()){
				return true;
			}
		}
		return false;
	}
	
	// 更新包材采购的实际金额
	public Double modifyRealMoney(Integer orderId) {
		CotPackingOrder temp = (CotPackingOrder) this.getCotBaseDao().getById(CotPackingOrder.class, orderId);
		CotPackingOrder packingOrder = (CotPackingOrder) SystemUtil.deepClone(temp);
		packingOrder.setCotPackingOrderdetails(null);
		WebContext ctx = WebContextFactory.get();
		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute("sysdic");
		List<?> list = (List<?>) dicMap.get("currency");

		// 查询该采购单的所有其他费用
		String hql = "from CotFinaceOther obj where obj.source='packorder' and obj.fkId="
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
		Double realMoney = packingOrder.getTotalAmount() + allMoney;
		packingOrder.setRealMoney(realMoney);
		List<CotPackingOrder> listOrder = new ArrayList<CotPackingOrder>();
		listOrder.add(packingOrder);
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
				CotPackingOrder order = this.getPackOrderById(orderId);
				order.setRealMoney(order.getRealMoney() - allRmb);
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
				String hql = "select obj.id from CotFinaceOther obj where obj.source='PackDeal' and obj.outFlag="
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
	
	// Action获取signMap
	@SuppressWarnings("unchecked")
	public Map<String, CotPackingOrderdetail> getMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "packorder");
		TreeMap<String, CotPackingOrderdetail> priceMap = (TreeMap<String, CotPackingOrderdetail>) obj;
		return priceMap;
	}
	
	// 重新排序
	public boolean updateSortNo(Integer type, String field,
			String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<String, CotPackingOrderdetail> pricerMap = this.getMapAction(ctx
				.getSession());
		List list = new ArrayList();
		Iterator<?> it = pricerMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			CotPackingOrderdetail detail = pricerMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotPackingOrderdetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotPackingOrderdetail detail = (CotPackingOrderdetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getCotBaseDao().updateRecords(listNew);
		return true;
	}
	
	// 查询配件单号是否存在
	public Integer findIsExistPackOrderNo(String packingOrderNo, String id) {
		String hql = "select obj.id from CotPackingOrder obj where obj.packingOrderNo='"
				+ packingOrderNo + "'";
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
}
