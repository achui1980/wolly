/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.systemdic.CotBalanceTypeDao;
import com.sail.cot.domain.CotBalanceType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotBalanceTypeService;
import com.sail.cot.util.Log4WebUtil;


public class CotBalanceTypeServiceImpl implements CotBalanceTypeService {

	private CotBalanceTypeDao balanceTypeDao;
	private Logger logger = Log4WebUtil.getLogger(CotBalanceTypeServiceImpl.class);
	
	//根据费用结算编号取得对象
	public CotBalanceType getBalanceTypeById(Integer id){
		Object object = this.getBalanceTypeDao().getById(CotBalanceType.class, id);
		if(object!=null){
			return (CotBalanceType)object;
		}else{
			return null;
		}
	}
	
	//保存费用结算方式
	public Boolean addBalanceTypes(List<?> balanceTypesList){
		for (int i = 0; i < balanceTypesList.size(); i++) {
			CotBalanceType cotBalanceType = (CotBalanceType)balanceTypesList.get(i);
			Integer id = this.getBalanceTypeDao().isExistBalanceName(cotBalanceType.getBalanceName());
			if(id!=null){
				return false;
			}
		}
		try {
			this.getBalanceTypeDao().saveRecords(balanceTypesList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加费用结算方式异常", e);
			return false;
		}
		return true;
	}
	
	//更新费用结算方式
	public Boolean modifyBalanceTypes(List<?> balanceTypesList){
		for (int i = 0; i < balanceTypesList.size(); i++) {
			CotBalanceType cotBalanceType = (CotBalanceType)balanceTypesList.get(i);
			Integer id = this.getBalanceTypeDao().isExistBalanceName(cotBalanceType.getBalanceName());
			if(id!=null && !id.toString().equals(cotBalanceType.getId().toString())){
				return false;
			}
		}
		try {
			this.getBalanceTypeDao().updateRecords(balanceTypesList);
		} catch (Exception e) {
			logger.error("更新费用结算方式异常", e);
		}
		return true;
	}
	
	//删除费用结算方式
	public Boolean deleteBalanceTypes(List<?> balanceTypesList){
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < balanceTypesList.size(); i++) {
			CotBalanceType cotBalanceType = (CotBalanceType)balanceTypesList.get(i);
			ids.add(cotBalanceType.getId());
		}
		try {
			this.getBalanceTypeDao().deleteRecordByIds(ids, "CotBalanceType");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除费用结算方式异常", e);
			return false;
		}
		return true;
	}

	public CotBalanceTypeDao getBalanceTypeDao() {
		return balanceTypeDao;
	}

	public void setBalanceTypeDao(CotBalanceTypeDao balanceTypeDao) {
		this.balanceTypeDao = balanceTypeDao;
	}

	 
	public List<?> getList(QueryInfo queryInfo){
		 
		try {
			return this.getBalanceTypeDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 e.printStackTrace();
		}
		return  null;
	}

	 
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getBalanceTypeDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 e.printStackTrace();
		}
		return 0;
	}

	 
	
	 
	
}
