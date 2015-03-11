/**
 * 
 */
package com.sail.cot.service.finace.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFinacegivenDetail;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.vo.CotDealDetailVO;
import com.sail.cot.domain.vo.CotRecvDetailVO;
import com.sail.cot.domain.vo.CotTransDetailVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.finace.CotDealService;
import com.sail.cot.util.Log4WebUtil;

/**
 * 财务管理模块
 * 应付账款
 * @author ch-chh
 * 
 */
public class CotDealServiceImpl implements CotDealService {
	
	private CotBaseDao dealDao;
	
	public CotBaseDao getDealDao() {
		return dealDao;
	}

	public void setDealDao(CotBaseDao dealDao) {
		this.dealDao = dealDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotDealServiceImpl.class);
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getDealDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getDealDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
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
	
	//查询所有员工
	public Map<?, ?> getEmpsMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getDealDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}
	
	//查询出货单号
	public Map<?, ?> getOrderFacNoMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getDealDao().getRecords("CotOrderFac");
		for (int i = 0; i < list.size(); i++) {
			CotOrderFac orderfac = (CotOrderFac) list.get(i);
			map.put(orderfac.getId().toString(), orderfac.getOrderNo());
		}
		return map;
	}
	
	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getDealDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}
	
	// 查询所有公司
	public Map<?, ?> getCompanyNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getDealDao().getRecords("CotCompany");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany.getCompanyShortName());
		}
		return map;
	}
	
	//根据id获取付款记录信息
	public CotFinaceAccountdeal getCotDealById(Integer id){
		
		return (CotFinaceAccountdeal) this.getDealDao().getById(CotFinaceAccountdeal.class, id);
		
	}
	
	//获取系统当前登陆员工
	public CotEmps getCurEmp(){
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		return cotEmps;
	}

	// 根据条件查询冲帐明细记录
	public List<?> getDealDetailList(List<?> list) {
		List<CotRecvDetailVO> listVo = new ArrayList<CotRecvDetailVO>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotRecvDetailVO recvDetailVO = new CotRecvDetailVO();
			recvDetailVO.setFinaceNo((String) obj[0]);
			recvDetailVO.setAddTime((Date) obj[1]);
			recvDetailVO.setCurrencyId((Integer) obj[2]);
			recvDetailVO.setCurrentAmount((Double) obj[3]);
			listVo.add(recvDetailVO);
		}
		return listVo;
	}
	
	// 根据条件查询流转记录
	public List<?> getTransDetailList(List<?> list) {
		List<CotRecvDetailVO> listVo = new ArrayList<CotRecvDetailVO>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotRecvDetailVO recvDetailVO = new CotRecvDetailVO();
			recvDetailVO.setOrderNo((String) obj[0]);
			recvDetailVO.setFinaceName((String) obj[1]);
			recvDetailVO.setFlag((String) obj[2]);
			recvDetailVO.setCurrencyId((Integer) obj[3]);
			recvDetailVO.setAmount((Double) obj[4]);
			listVo.add(recvDetailVO);
		}
		return listVo;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.finace.CotDealService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getDealDao().getJsonData(queryInfo);
	}
}
