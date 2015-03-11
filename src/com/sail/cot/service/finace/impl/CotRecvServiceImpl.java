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
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.vo.CotFinaceOtherVO;
import com.sail.cot.domain.vo.CotRecvDetailVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.finace.CotRecvService;
import com.sail.cot.util.Log4WebUtil;

/**
 * 财务管理模块
 * 应收账款
 * @author ch-chh
 * 
 */
public class CotRecvServiceImpl implements CotRecvService {
	
	private CotBaseDao recvDao;
	
	public CotBaseDao getRecvDao() {
		return recvDao;
	}

	public void setRecvDao(CotBaseDao recvDao) {
		this.recvDao = recvDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotRecvServiceImpl.class);
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getRecvDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getRecvDao().findRecords(queryInfo);
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
		List<?> list = this.getRecvDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}
	
	// 查询所有客户简称
	public Map<?, ?> getCustNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getRecvDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer.getCustomerShortName());
		}
		return map;
	}
	
	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getRecvDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}
	
	// 查询所有公司
	public Map<?, ?> getCompanyNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getRecvDao().getRecords("CotCompany");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany.getCompanyShortName());
		}
		return map;
	}
	
	//根据id获取付款记录信息
	public CotFinaceAccountdeal getCotDealById(Integer id){
		
		return (CotFinaceAccountdeal) this.getRecvDao().getById(CotFinaceAccountdeal.class, id);
		
	}
	
	//获取系统当前登陆员工
	public CotEmps getCurEmp(){
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		return cotEmps;
	}
	
	// 根据条件查询冲帐明细记录
	public List<?> getRecvDetailList(List<?> list) {
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
	
	// 根据条件查询流转明细记录
	public List<?> getLiuDetailList(List<?> list) {
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
	
	// 保存应收帐款
	public Boolean addList(List<?> details) {
		try {
			this.getRecvDao().saveRecords(details);
			return true;
		} catch (DAOException e) {
			logger.error("保存应收帐款出错!");
			return false;
		}
	}
	
	//更新应收帐款
	public Boolean updateList(List<?> details){
		try {
			this.getRecvDao().updateRecords(details);
		} catch (Exception e) {
			logger.error("更新应收帐款异常", e);
		}
		return true;
	}
	
	//删除应收帐款
	public Boolean deleteList(List<?> details){
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < details.size(); i++) {
			CotFinaceAccountrecv finaceAccountrecv = (CotFinaceAccountrecv)details.get(i);
			ids.add(finaceAccountrecv.getId());
		}
		try {
			this.getRecvDao().deleteRecordByIds(ids, "CotFinaceAccountrecv");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除应收帐款异常", e);
			return false;
		}
		return true;
	}
	
	//删除应收帐款
	public Boolean deleteByIds(List<?> ids){
		try {
			this.getRecvDao().deleteRecordByIds(ids, "CotFinaceAccountrecv");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除应收帐款异常", e);
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.sail.cot.service.finace.CotRecvService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getRecvDao().getJsonData(queryInfo);
	}

}
