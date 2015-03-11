package com.sail.cot.service.fittingorder.impl;

import java.io.File;
import java.io.FileInputStream;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFittingOrder;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotFittingsAnys;
import com.sail.cot.domain.CotFittingsOrderdetail;
import com.sail.cot.domain.CotFittingsOrderdetailPic;
import com.sail.cot.domain.CotFittingsPic;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFittings;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.query.QueryInfo;

import com.sail.cot.service.fittingorder.CotFitOrderService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.impl.CotElementsServiceImpl;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.ListSort;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;

public class CotFitOrderServiceImpl implements CotFitOrderService {

	private Logger logger = Log4WebUtil.getLogger(CotFitOrderServiceImpl.class);

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

	// 查询配件厂家
	public Map<?, ?> getFitFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
		// "sysdic");
		// List<?> list = (List<?>) dicMap.get("factory");
		List<?> list = this.getCotBaseDao().getRecords("CotFactory");
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
	public CotFittingOrder getFitOrderById(Integer id) {
		CotFittingOrder cotOrder = (CotFittingOrder) this.getCotBaseDao().getById(
				CotFittingOrder.class, id);
		if (cotOrder != null) {
			CotFittingOrder custClone = (CotFittingOrder) SystemUtil.deepClone(cotOrder);
			custClone.setCotFittingsOrderdetails(null);
			return custClone;
		}
		return null;
	}

	// Action储存FitOrderMap
	@SuppressWarnings("unchecked")
	public void setFitOrderMapAction(HttpSession session, String rdm,
			CotFittingsOrderdetail cotFittingsOrderdetail) {
		Object obj = SystemUtil.getObjBySession(session, "fitorder");
		if (obj == null) {
			TreeMap<String, CotFittingsOrderdetail> fitorderMap = new TreeMap<String, CotFittingsOrderdetail>();
			fitorderMap.put(rdm, cotFittingsOrderdetail);
			SystemUtil.setObjBySession(session, fitorderMap, "fitorder");
		} else {
			TreeMap<String, CotFittingsOrderdetail> fitorderMap = (TreeMap<String, CotFittingsOrderdetail>) obj;
			fitorderMap.put(rdm, cotFittingsOrderdetail);
			SystemUtil.setObjBySession(session, fitorderMap, "fitorder");
		}
	}

	// Action获取FitOrderMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotFittingsOrderdetail> getFitNoMapAction(
			HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "fitorder");
		TreeMap<String, CotFittingsOrderdetail> fitorderMap = (TreeMap<String, CotFittingsOrderdetail>) obj;
		return fitorderMap;
	}

	// 通过配件编号修改Map中对应的配件采购明细
	public boolean updateMapValueByFitNo(String rdm, String property,
			String value) {
		CotFittingsOrderdetail cotFittingsOrderdetail = getFitNoMapValue(rdm);
		if (cotFittingsOrderdetail == null)
			return false;
		try {
			BeanUtils.setProperty(cotFittingsOrderdetail, property, value);
			this.setFitNoMap(rdm, cotFittingsOrderdetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 储存FitOrderMap
	@SuppressWarnings("unchecked")
	public void setFitNoMap(String rdm,
			CotFittingsOrderdetail cotFittingsOrderdetail) {
		Object obj = SystemUtil.getObjBySession(null, "fitorder");
		if (obj == null) {
			TreeMap<String, CotFittingsOrderdetail> fitorderMap = new TreeMap<String, CotFittingsOrderdetail>();
			fitorderMap.put(rdm, cotFittingsOrderdetail);
			SystemUtil.setObjBySession(null, fitorderMap, "fitorder");
		} else {
			TreeMap<String, CotFittingsOrderdetail> fitorderMap = (TreeMap<String, CotFittingsOrderdetail>) obj;
			fitorderMap.put(rdm, cotFittingsOrderdetail);
			SystemUtil.setObjBySession(null, fitorderMap, "fitorder");
		}

	}

	// 获取FitOrderMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotFittingsOrderdetail> getFitOrderMap() {
		Object obj = SystemUtil.getObjBySession(null, "fitorder");
		TreeMap<String, CotFittingsOrderdetail> fitorderMap = (TreeMap<String, CotFittingsOrderdetail>) obj;
		return fitorderMap;
	}

	// 通过key获取FitOrderMap的value
	public CotFittingsOrderdetail getFitNoMapValue(String rdm) {
		TreeMap<String, CotFittingsOrderdetail> fitorderMap = this
				.getFitOrderMap();
		if (fitorderMap != null) {
			CotFittingsOrderdetail cotFittingsOrderdetail = (CotFittingsOrderdetail) fitorderMap
					.get(rdm);
			return cotFittingsOrderdetail;
		}
		return null;
	}

	// 清空FitOrderMap
	public void clearFitNoMap() {
		SystemUtil.clearSessionByType(null, "fitorder");
	}

	// 清除FitOrderMap中fitNo对应的映射
	public void delFitNoMapByKey(String rdm) {
		TreeMap<String, CotFittingsOrderdetail> fitorderMap = this
				.getFitOrderMap();
		if (fitorderMap != null) {
			if (fitorderMap.containsKey(rdm)) {
				fitorderMap.remove(rdm);
			}
		}
	}

	// 在Action中清除FitOrderMap中fitNo对应的映射
	public void delFitOrderMapByKeyAction(String rdm, HttpSession session) {
		TreeMap<String, CotFittingsOrderdetail> fitorderMap = this
				.getFitNoMapAction(session);
		if (fitorderMap != null) {
			if (fitorderMap.containsKey(rdm)) {
				fitorderMap.remove(rdm);
			}
		}
	}

	// 保存主配件采购单
	public Integer saveOrUpdate(CotFittingOrder cotFittingOrder,
			String orderDate, String sendDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<CotFittingOrder> records = new ArrayList<CotFittingOrder>();

		// 保存主配件采购单
		try {
			if (orderDate != null && !"".equals(orderDate)) {
				cotFittingOrder.setOrderDate(new Date(sdf.parse(orderDate)
						.getTime()));
			}
			if (sendDate != null && !"".equals(sendDate)) {
				cotFittingOrder.setSendDate(new Date(sdf.parse(sendDate)
						.getTime()));
			}

			records.add(cotFittingOrder);
			this.getCotBaseDao().saveOrUpdateRecords(records);

		} catch (Exception e) {
			logger.error("保存配件采购单出错！");
			return null;
		}
		return cotFittingOrder.getId();
	}

	// 更新配件采购明细
	public Boolean modifyFitOrderDetail(List<CotFittingsOrderdetail> detailList) {
		try {
			this.getCotBaseDao().updateRecords(detailList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 根据编号获得对象
	public CotFittingsOrderdetail getFittingDetailById(Integer id) {
		return (CotFittingsOrderdetail) this.getCotBaseDao().getById(
				CotFittingsOrderdetail.class, id);
	}

	// 根据送样明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids) {
		try {
			this.getCotBaseDao().deleteRecordByIds(ids,
					"CotFittingsOrderdetail");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 删除配件采购主单
	@SuppressWarnings( { "unchecked", "deprecation" })
	public Integer deleteFitOrderList(List<?> orderIds) {
		String ids = "";
		for (int i = 0; i < orderIds.size(); i++) {
			ids += orderIds.get(i) + ",";
		}
		try {
			// 有应付帐款不删除
			String hqlAccount = "select obj.fkId from CotFinaceAccountdeal obj where obj.source='fitorder' and obj.fkId in ("
					+ ids.substring(0, ids.length() - 1) + ")";
			List checkAccountList = this.getCotBaseDao().find(hqlAccount);
			if (checkAccountList.size() > 0) {
				for (int i = 0; i < checkAccountList.size(); i++) {
					Integer dId = (Integer) checkAccountList.get(i);
					orderIds.remove(dId);
				}
			}
			//如果有加减费用导入到出货不能再删除
			String hqlOther = "select b.fkId from CotFinaceOther a,CotFinaceOther b where a.outFlag=b.id and a.source='FitOther' and b.fkId in ("
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
			if(orderIds.size()>0){
				this.getCotBaseDao().deleteRecordByIds(orderIds, "CotFittingOrder");
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
			logger.error("删除配件采购单出错");
			return 3;
		}
	}

	// 删除主单关联的其他费用
	public void delOtherFee(Integer fitorderId) {

		String hql = " from CotFinaceOther obj where obj.source='fitorder' and obj.fkId="
				+ fitorderId;
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

	// 更改默认图片
	public void updateDefaultPic(String filePath, Integer fkId)
			throws Exception {
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotFittingsOrderdetailPic cotFittingsOrderdetailPic = impOpService
				.getFitOrderPicImgById(fkId);
		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int) picFile.length()];
		while (in.read(b) != -1) {
		}
		in.close();
		cotFittingsOrderdetailPic.setPicImg(b);
		if (filePath.indexOf("common/images/zwtp.png") < 0) {
			picFile.delete();
		}
		List<CotFittingsOrderdetailPic> imgList = new ArrayList<CotFittingsOrderdetailPic>();
		imgList.add(cotFittingsOrderdetailPic);
		impOpService.modifyImg(imgList);
	}

	// 删除配件图片
	public boolean deletePicImg(Integer Id) {
		String classPath = CotElementsServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		String filePath = systemPath + "common/images/zwtp.png";
		CotFittingsOrderdetailPic cotFittingsOrderdetailPic = impOpService
				.getFitOrderPicImgById(Id);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotFittingsOrderdetailPic.setPicImg(b);
			List<CotFittingsOrderdetailPic> imgList = new ArrayList<CotFittingsOrderdetailPic>();
			imgList.add(cotFittingsOrderdetailPic);
			impOpService.modifyImg(imgList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除主样品图片错误!");
			return false;
		}
	}

	// 修改配件分析表中的id及分析状态
	public void modifyFitIdAndFlag(Integer fitDetailId) {

		List<CotFittingsAnys> resList = new ArrayList<CotFittingsAnys>();
		String hql = " from CotFittingsAnys obj where obj.fittingsDetailid ="
				+ fitDetailId;
		List<?> list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotFittingsAnys cotFittingsAnys = (CotFittingsAnys) list.get(i);
			cotFittingsAnys.setFittingsOrderid(null);
			cotFittingsAnys.setFittingsDetailid(null);
			cotFittingsAnys.setAnyFlag("U");

			resList.add(cotFittingsAnys);

		}
		this.getCotBaseDao().updateRecords(resList);
	}

	// 根据主采购id修改配件分析表中的id及分析状态
	public void modifyIdAndFlagByOrderId(Integer fitOrderId) {

		List<CotFittingsAnys> resList = new ArrayList<CotFittingsAnys>();
		String hql = " from CotFittingsAnys obj where obj.fittingsOrderid ="
				+ fitOrderId;
		List<?> list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotFittingsAnys cotFittingsAnys = (CotFittingsAnys) list.get(i);
			cotFittingsAnys.setFittingsOrderid(null);
			cotFittingsAnys.setFittingsDetailid(null);
			cotFittingsAnys.setAnyFlag("U");

			resList.add(cotFittingsAnys);

		}
		this.getCotBaseDao().updateRecords(resList);
	}

	// 根据主采购id修改总金额
	public void modifyFitOrderAmount(Integer fitOrderId, Double total) {

		List<CotFittingOrder> resList = new ArrayList<CotFittingOrder>();
		String hql = " from CotFittingOrder obj where obj.id =" + fitOrderId;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() == 1) {
			CotFittingOrder cotFittingOrder = (CotFittingOrder) list.get(0);
			cotFittingOrder.setTotalAmmount(cotFittingOrder.getTotalAmmount()
					- total);

			cotFittingOrder.setCotFittingsOrderdetails(null);

			resList.add(cotFittingOrder);
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

	// 修改其他费用状态 0：未生成 1: 已生成
	public void modifyFinOtherStatus(Integer fkId, String finaceName,
			String flag) {
		String hql = " from CotFinaceOther obj where obj.source='fitorder' and obj.fkId="
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
		//GenAllSeq seq = new GenAllSeq();
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
			dealDetail.setSource("fitorder");
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
				CotFittingOrder fittingOrder = (CotFittingOrder) this
						.getCotBaseDao().getById(CotFittingOrder.class,
								dealDetail.getFkId());
				CotFittingOrder obj = (CotFittingOrder) SystemUtil
						.deepClone(fittingOrder);
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
	public Integer getDealNumById(Integer fitorderId) {
		String hql = " from CotFinaceAccountdeal obj where obj.fkId="
				+ fitorderId;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() != 0) {
			return list.size();
		} else {
			return -1;
		}
	}

	// 判断生产合同是否有应付帐款记录
	public String checkIsHaveDeal(List<CotFittingOrder> orderList) {
		String orderNos = new String();
		for (int i = 0; i < orderList.size(); i++) {

			CotFittingOrder cotFittingOrder = this.getFitOrderById((orderList
					.get(i)).getId());

			List<?> list = this.getCotBaseDao().find(
					" from CotFinaceAccountdeal obj where obj.fkId="
							+ cotFittingOrder.getId());
			if (list.size() > 0) {
				orderNos += cotFittingOrder.getFittingOrderNo() + "、";
			}
		}
		if (orderNos.length() != 0) {
			return orderNos.substring(0, orderNos.length() - 1);
		} else {
			return null;
		}
	}

	// =======================配件采购(结束)=====================================

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
			CotFittingsAnys fittingsAnys = (CotFittingsAnys) this
					.getCotBaseDao().getById(CotFittingsAnys.class, id);
			if ("C".equals(fittingsAnys.getAnyFlag())) {
				it.remove();
			}
		}

		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFittingsAnys");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获得配件采购分析
	public CotFittingsAnys getFitAnysById(Integer id) {
		return (CotFittingsAnys) this.getCotBaseDao().getById(
				CotFittingsAnys.class, id);
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

	public List saveFitOrderDetails(List listAnys,
			List<CotFittingsOrderdetail> detailList, Integer orderId,
			Integer facId) {
		for (int i = 0; i < listAnys.size(); i++) {
			CotFittingsAnys fittingsAnys = (CotFittingsAnys) listAnys.get(i);
			CotFittings fittings = null;
			if (fittingsAnys.getFittingId() != null) {
				fittings = (CotFittings) this.getCotBaseDao().getById(
						CotFittings.class, fittingsAnys.getFittingId());
			}
			CotFittingsOrderdetail fittingsOrderdetail = new CotFittingsOrderdetail();
			fittingsOrderdetail.setSortNo(i+1);
			fittingsOrderdetail.setOrderId(orderId);

			fittingsOrderdetail.setFitNo(fittingsAnys.getFitNo());
			fittingsOrderdetail.setFitName(fittingsAnys.getFitName());
			fittingsOrderdetail.setUseUnit(fittingsAnys.getFitBuyUnit());

			fittingsOrderdetail.setFitDesc(fittingsAnys.getFitDesc());
			fittingsOrderdetail.setFitRemark(fittingsAnys.getRemark());
			fittingsOrderdetail.setFacId(facId);
			// 需求数量=分析表的订单需求数量
			fittingsOrderdetail.setRequeirCount(fittingsAnys.getOrderFitCount()
					.doubleValue());
			// 采购数量=分析表的订单需求数量/换算率
			if (fittings.getFitTrans() != null && fittings.getFitTrans() != 0) {
				double oc = fittingsAnys.getOrderFitCount()
						/ fittings.getFitTrans();
				fittingsOrderdetail.setOrderCount(oc);
				fittingsOrderdetail.setFitPrice(fittingsAnys.getFitPrice()
						* fittings.getFitTrans());
			} else {
				fittingsOrderdetail.setFitPrice(fittingsAnys.getFitPrice());
				fittingsOrderdetail.setOrderCount(fittingsAnys
						.getOrderFitCount().doubleValue());
			}
			fittingsOrderdetail.setTotalAmmount(fittingsAnys.getTotalAmount());
			fittingsOrderdetail.setEleId(fittingsAnys.getEleId());
			fittingsOrderdetail.setAddTime(new Date());
			fittingsOrderdetail.setFitAnysIds(fittingsAnys.getId() + ",");
			// 配件库数据
			if (fittings != null) {
				fittingsOrderdetail.setBuyUnit(fittings.getBuyUnit());
				fittingsOrderdetail.setFitTrans(fittings.getFitTrans());
				fittingsOrderdetail.setFitMinCount(fittings.getFitMinCount());
				fittingsOrderdetail.setFitQualityStander(fittings
						.getFitQualityStander());
				fittingsOrderdetail.setTypeLv3Id(fittings.getTypeLv3Id());
			}
			fittingsOrderdetail.setOrderDetailId(fittingsAnys
					.getOrderdetailId());
			detailList.add(fittingsOrderdetail);
		}
		return detailList;
	}

	// 生成应付帐款单号
//	public String createRecvNo(Integer facId, String orderNo, Integer custId) {
//		Map idMap = new HashMap<String, Integer>();
//		idMap.put("CH", facId);
//		idMap.put("kH", custId);
//		idMap.put("ORDERNO", orderNo);
//		GenAllSeq seq = new GenAllSeq();
//		String finaceNo = seq.getAllSeqByType("accessNo", idMap);
//		return finaceNo;
//	}

	// 保存应收帐款单号
	public void savaSeq() {
//		GenAllSeq seq = new GenAllSeq();
//		seq.saveSeq("accessNo");
		CotSeqService cotSeqService=new CotSeqServiceImpl();
		cotSeqService.saveSeq("access");
	}

	// 将配件采购分析生成配件采购单,(ids有值只处理勾选的分析数据,flag为true要合并相同配件)
	public Boolean saveFitOrderByAnys(Integer orderId, String ids, boolean flag) {
		// 0.获得订单数据
		CotOrder order = this.getOrderById(orderId);
		long isCheck = 1;
		// 取得业务配置的是否审核
		List listCfg = this.getCotBaseDao().getRecords("CotPriceCfg");
		if (listCfg != null && listCfg.size() > 0) {
			CotPriceCfg priceCfg = (CotPriceCfg) listCfg.get(0);
			if (priceCfg.getIsCheck() != null) {
				isCheck = priceCfg.getIsCheck();
			}
		}

		// 1.查询未生成采购单的分析数据
		String hql = "from CotFittingsAnys obj where obj.anyFlag='U' and obj.orderId="
				+ orderId;
		if (!"".equals(ids)) {
			hql += " and obj.id in (" + ids.substring(0, ids.length() - 1)
					+ ")";
		}
		List list = this.getCotBaseDao().find(hql);
		// 2.该list中不同的供应商(key为厂家编号,value为分析数据LIST)
		Map<Integer, List<?>> map = new HashMap<Integer, List<?>>();
		for (int i = 0; i < list.size(); i++) {
			CotFittingsAnys fittingsAnys = (CotFittingsAnys) list.get(i);
			if (fittingsAnys.getFacId() == null) {
				continue;
			}
			Object obj = map.get(fittingsAnys.getFacId());
			if (obj == null) {
				List tempList = new ArrayList();
				tempList.add(fittingsAnys);
				map.put(fittingsAnys.getFacId(), tempList);
			} else {
				List tempList = (List) obj;
				tempList.add(fittingsAnys);
				map.put(fittingsAnys.getFacId(), tempList);
			}
		}
		// 3.查找该map中的厂家是否已经生成采购单
		Iterator<?> it = map.keySet().iterator();
		List detailList = new ArrayList();
		while (it.hasNext()) {
			Integer facId = (Integer) it.next();
			// 获得总金额
			List listAnys = map.get(facId);
			Double sum = 0.0;
			for (int i = 0; i < listAnys.size(); i++) {
				CotFittingsAnys fittingsAnys = (CotFittingsAnys) listAnys
						.get(i);
				sum += fittingsAnys.getTotalAmount();
			}
			String orderHql = "from CotFittingOrder obj where obj.orderId="
					+ orderId + " and obj.factoryId=" + facId;
			List listOrder = this.getCotBaseDao().find(orderHql);
			if (listOrder.size() == 0) {
				// 新建配件采购主单
				CotFittingOrder fittingOrder = new CotFittingOrder();
				fittingOrder.setOrderDate(order.getOrderTime());
				
//				String no = this.createRecvNo(facId, order.getOrderNo(), order
//						.getCustId());// 获取单号
				CotSeqService seq =new CotSeqServiceImpl();
				String no =seq.getFitingorderNo();
				
				fittingOrder.setFittingOrderNo(no);
				
				fittingOrder.setSendDate(order.getSendTime());
				fittingOrder.setEmpId(order.getBussinessPerson());// 采购人员取订单的业务员
				fittingOrder.setFactoryId(facId);
				fittingOrder.setOrderNo(order.getOrderNo());
				fittingOrder.setOrderId(orderId);
				fittingOrder.setCompanyId(order.getCompanyId());
				fittingOrder.setTotalAmmount(sum);
				fittingOrder.setRealMoney(sum);
				// 是否审核
				if (isCheck == 1) {
					fittingOrder.setOrderStatus(0);
				} else {
					fittingOrder.setOrderStatus(9);
				}
				List records = new ArrayList();
				records.add(fittingOrder);
				// 保存主采购单
				try {
					this.getCotBaseDao().saveRecords(records);
					this.savaSeq();// 保存单号
					// 生成采购明细,添加到detailList中
					saveFitOrderDetails(listAnys, detailList, fittingOrder
							.getId(), facId);
				} catch (DAOException e) {
					e.printStackTrace();
				}
			} else {
				CotFittingOrder forder = (CotFittingOrder) listOrder.get(0);
				forder.setCotFittingsOrderdetails(null);
				// 生成采购明细,添加到detailList中
				saveFitOrderDetails(listAnys, detailList, forder.getId(), facId);
				// 更新采购主单总金额
				forder.setTotalAmmount(forder.getTotalAmmount() + sum);
				forder.setRealMoney(forder.getRealMoney() + sum);
				List records = new ArrayList();
				records.add(forder);
				this.getCotBaseDao().updateRecords(records);
			}
		}
		// 4.是否合并相同配件数据(配件号、配件名称、规格型号、供应商一致)
		if (flag) {
			Map<String, CotFittingsOrderdetail> mapDetail = new HashMap<String, CotFittingsOrderdetail>();

			for (int i = 0; i < detailList.size(); i++) {
				CotFittingsOrderdetail fo = (CotFittingsOrderdetail) detailList
						.get(i);
				String fn = fo.getFitNo();
				if (fn == null) {
					fn = "";
				}
				String fm = fo.getFitName();
				if (fm == null) {
					fm = "";
				}
				String fds = fo.getFitDesc();
				if (fds == null) {
					fds = "";
				}
				Integer fi = fo.getFacId();
				if (fi == null) {
					fi = 0;
				}
				String sameStr = fn.trim() + "#_" + fm.trim() + "#_"
						+ fds.trim() + "#_" + fi;

				Object obj = mapDetail.get(sameStr);
				if (obj == null) {
					mapDetail.put(sameStr, fo);
				} else {
					CotFittingsOrderdetail fd = (CotFittingsOrderdetail) obj;
					// 合并订单数量,订单需求数量,总金额
					fd.setOrderCount(fo.getOrderCount() + fd.getOrderCount());
					fd.setRequeirCount(fo.getRequeirCount()
							+ fd.getRequeirCount());
					fd.setTotalAmmount(fo.getTotalAmmount()
							+ fd.getTotalAmmount());
					fd.setFitAnysIds(fd.getFitAnysIds() + fo.getFitAnysIds());
					fd.setEleId(fd.getEleId() + "/" + fo.getEleId());
					mapDetail.put(sameStr, fd);
				}
			}
			List realList = new ArrayList();
			Iterator<?> itReal = mapDetail.keySet().iterator();
			while (itReal.hasNext()) {
				String sameStr = (String) itReal.next();
				CotFittingsOrderdetail fo = mapDetail.get(sameStr);
				realList.add(fo);
			}
			// 保存采购明细
			try {
				this.getCotBaseDao().saveRecords(realList);
				// 5.更改分析表数据.添加采购单主单编号.明细编号,修改状态,并添加明细的图片
				updateFittingsAnys(list, realList);
			} catch (DAOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			// 保存采购明细
			try {
				this.getCotBaseDao().saveRecords(detailList);
				// 5.更改分析表数据.添加采购单主单编号.明细编号,修改状态,并添加明细的图片
				updateFittingsAnys(list, detailList);
			} catch (DAOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic() {
		// 获得默认图片的
		// String fileLength =
		// ContextUtil.getProperty("sysconfig.properties","maxLength");
		// 获得tomcat路径
		String classPath = CotFitOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath + "common/images/zwtp.png");
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			return b;
		} catch (Exception e1) {
			logger.error("设置样品图片错误!");
			return null;
		}
	}

	// 更改分析表数据.添加采购单主单编号.明细编号,修改状态,并添加明细的图片
	public void updateFittingsAnys(List list, List detailList) {
		CotOpImgService opImgImpl = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		List<CotFittingsOrderdetailPic> imgList = new ArrayList<CotFittingsOrderdetailPic>();
		// 得到暂无图片的字节
		byte[] zwtpImg = this.getZwtpPic();
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			CotFittingsAnys fa = (CotFittingsAnys) list.get(i);
			map.put(fa.getId(), fa);
		}
		List listNew = new ArrayList();
		for (int i = 0; i < detailList.size(); i++) {
			CotFittingsOrderdetail fd = (CotFittingsOrderdetail) detailList
					.get(i);
			String[] ids = fd.getFitAnysIds().split(",");
			// 获得配件库的对应编号,好找到配件图片
			Integer oldPicId = null;
			for (int j = 0; j < ids.length; j++) {
				CotFittingsAnys fa = (CotFittingsAnys) map.get(Integer
						.parseInt(ids[j]));
				fa.setAnyFlag("C");
				fa.setFittingsOrderid(fd.getOrderId());
				fa.setFittingsDetailid(fd.getId());
				listNew.add(fa);
				oldPicId = fa.getFittingId();
			}
			if (oldPicId == null) {
				// 添加样品默认图片为("zwtp")
				CotFittingsOrderdetailPic pic = new CotFittingsOrderdetailPic();
				pic.setEleId(fd.getFitNo());
				pic.setFkId(fd.getId());
				pic.setPicImg(zwtpImg);
				pic.setPicName(fd.getFitName());
				pic.setPicSize(new Integer(zwtpImg.length));
				imgList.add(pic);
			} else {
				// 配件库图片
				String hql = "from CotFittingsPic obj where obj.fkId="
						+ oldPicId;
				List listPic = this.getCotBaseDao().find(hql);
				CotFittingsPic fittingsPic = (CotFittingsPic) listPic.get(0);
				CotFittingsOrderdetailPic pic = new CotFittingsOrderdetailPic();
				pic.setEleId(fd.getFitNo());
				pic.setFkId(fd.getId());
				pic.setPicImg(fittingsPic.getPicImg());
				pic.setPicName(fd.getFitName());
				pic.setPicSize(new Integer(fittingsPic.getPicImg().length));
				imgList.add(pic);
			}
		}
		opImgImpl.saveImg(imgList);

		this.getCotBaseDao().updateRecords(listNew);
	}

	// 根据订单主单编号获得明细
	public List getEleIdsFromOrderId(Integer orderId) {
		String hql = "from CotOrderDetail obj where obj.orderId=" + orderId;
		return this.getCotBaseDao().find(hql);
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		SystemDicUtil systemDicUtil = new SystemDicUtil();
		return systemDicUtil.getDicListByName(objName);
	}

	// 根据样品配件编号获得样品配件信息
	public CotFittings getFittingById(Integer id) {
		CotFittings fittings = (CotFittings) this.getCotBaseDao().getById(
				CotFittings.class, id);
		// if (fittings.getFacId() != null) {
		// List list = this.getCotBaseDao().getRecords("CotFactory");
		// for (int i = 0; i < list.size(); i++) {
		// CotFactory fac = (CotFactory) list.get(i);
		// if (fac.getId().intValue() == fittings.getFacId().intValue()) {
		// fittings.setFacShortName(fac.getShortName());
		// break;
		// }
		// }
		// }
		return fittings;
	}

	// 分析订单明细集合,生成配件采购分析数据
	public boolean saveFitAnysAgain(Integer orderId) {
		// 1查询是否有采购的数据,有的话删除采购单,更改分析数据状态为U
		String delOrderHql = "from CotFittingsAnys obj where obj.anyFlag='C' and obj.orderId="
				+ orderId;
		List delOrder = this.getCotBaseDao().find(delOrderHql);
		List delOrderIds = new ArrayList();
		List newOds = new ArrayList();
		for (int i = 0; i < delOrder.size(); i++) {
			CotFittingsAnys fittingsAnys = (CotFittingsAnys) delOrder.get(i);
			delOrderIds.add(fittingsAnys.getFittingsOrderid());
			fittingsAnys.setAnyFlag("U");
			newOds.add(fittingsAnys);
		}

		if (delOrderIds != null && delOrderIds.size() > 0) {
			try {
				this.getCotBaseDao().deleteRecordByIds(delOrderIds,
						"CotFittingOrder");
				this.getCotBaseDao().updateRecords(newOds);
			} catch (DAOException e) {
				e.printStackTrace();
				return false;
			}
		}

		// 2.删除未采购的分析数据
		String delHql = "select obj.id from CotFittingsAnys obj where obj.anyFlag='U' and obj.orderId="
				+ orderId;
		List delIds = this.getCotBaseDao().find(delHql);
		try {
			if (delIds != null && delIds.size() > 0) {
				this.getCotBaseDao().deleteRecordByIds(delIds,
						"CotFittingsAnys");
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
			CotOrderDetail cotOrderDetail = (CotOrderDetail) orderDetails
					.get(i);
			String hql = "select obj from CotOrderFittings obj where obj.orderDetailId="
					+ cotOrderDetail.getId();
			List listEleFits = this.getCotBaseDao().find(hql);
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
				this.getCotBaseDao().saveRecords(listAnys);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
		CotFittingOrder fittingOrder = (CotFittingOrder) this.getCotBaseDao()
				.getById(CotFittingOrder.class, orderId);
		fittingOrder.setCotFittingsOrderdetails(null);
		fittingOrder.setOrderStatus(orderStatus);
		List list = new ArrayList();
		list.add(fittingOrder);
		this.getCotBaseDao().updateRecords(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.fittingorder.CotFitOrderService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

	// 查询配件
	public CotFittings findFittingByFitNo(String fitNo) {
		String hql = "from CotFittings obj where obj.fitNo like ?";
		Object[] obj = new Object[1];
		obj[0] = '%' + fitNo + "%";
		List list = this.getCotBaseDao().queryForLists(hql, obj);
		if (list != null && list.size() == 1) {
			return (CotFittings) list.get(0);
		} else {
			if (list != null && list.size() > 1) {
				CotFittings t = new CotFittings();
				return t;
			}
			return null;
		}
	}
	
	//判断其他费用是否存在
	public boolean findIsExistName(String name,Integer orderId,Integer recId){
		String hql="select obj.id from CotFinaceOther obj where obj.fkId=? and obj.source='fitorder' and obj.finaceName=?";
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
	
	// 更新配件采购的实际金额
	public Double modifyRealMoney(Integer orderId) {
		CotFittingOrder temp = (CotFittingOrder) this.getCotBaseDao().getById(CotFittingOrder.class, orderId);
		CotFittingOrder fittingOrder = (CotFittingOrder) SystemUtil.deepClone(temp);
		fittingOrder.setCotFittingsOrderdetails(null);
		WebContext ctx = WebContextFactory.get();
		Map<?, ?> dicMap = (Map<?, ?>) ctx.getSession().getAttribute("sysdic");
		List<?> list = (List<?>) dicMap.get("currency");

		// 查询该采购单的所有其他费用
		String hql = "from CotFinaceOther obj where obj.source='fitorder' and obj.fkId="
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
		Double realMoney = fittingOrder.getTotalAmmount() + allMoney;
		fittingOrder.setRealMoney(realMoney);
		List<CotFittingOrder> listOrder = new ArrayList<CotFittingOrder>();
		listOrder.add(fittingOrder);
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
				CotFittingOrder order = this.getFitOrderById(orderId);
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
				String hql = "select obj.id from CotFinaceOther obj where obj.source='FitDeal' and obj.outFlag="
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
	public Map<String, CotFittingsOrderdetail> getMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "fitorder");
		TreeMap<String, CotFittingsOrderdetail> priceMap = (TreeMap<String, CotFittingsOrderdetail>) obj;
		return priceMap;
	}
	
	// 重新排序
	public boolean updateSortNo(Integer type, String field,
			String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<String, CotFittingsOrderdetail> pricerMap = this.getMapAction(ctx
				.getSession());
		List list = new ArrayList();
		Iterator<?> it = pricerMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			CotFittingsOrderdetail detail = pricerMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotFittingsOrderdetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotFittingsOrderdetail detail = (CotFittingsOrderdetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getCotBaseDao().updateRecords(listNew);
		return true;
	}
	
	// 查询配件单号是否存在
	public Integer findIsExistFitOrderNo(String fittingOrderNo, String id) {
		String hql = "select obj.id from CotFittingOrder obj where obj.fittingOrderNo='"
				+ fittingOrderNo + "'";
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
