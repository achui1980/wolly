/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.systemdic.CotGivenTypeDao;
import com.sail.cot.domain.CotGivenType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotGivenTypeService;
import com.sail.cot.util.Log4WebUtil;


public class CotGivenTypeServiceImpl implements CotGivenTypeService {

	private CotGivenTypeDao givenTypeDao;
	private Logger logger = Log4WebUtil.getLogger(CotGivenTypeServiceImpl.class);

	//保存送样方式
	public Boolean addGivenTypes(List<?> givenTypesList){
		List<CotGivenType> list = new ArrayList<CotGivenType>();
		for (int i = 0; i < givenTypesList.size(); i++) {
			CotGivenType cotGivenType = (CotGivenType)givenTypesList.get(i);
			Integer id = this.getGivenTypeDao().isExistGivenName(cotGivenType.getGivenName());
			if(id==null){
				list.add(cotGivenType);
			}
		}
		try {
			this.getGivenTypeDao().saveRecords(list);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加送样方式异常", e);
			return false;
		}
		return true;
	}
	
	//更新送样方式
	public Boolean modifyGivenTypes(List<?> givenTypesList){
		List<CotGivenType> list = new ArrayList<CotGivenType>();
		for (int i = 0; i < givenTypesList.size(); i++) {
			CotGivenType cotGivenType = (CotGivenType)givenTypesList.get(i);
			Integer id = this.getGivenTypeDao().isExistGivenName(cotGivenType.getGivenName());
			if(id==null || id.toString().equals(cotGivenType.getId().toString())){
				list.add(cotGivenType);
			}
		}
		try {
			this.getGivenTypeDao().updateRecords(list);
		} catch (Exception e) {
			logger.error("更新送样方式异常", e);
		}
		return true;
	}
	
	//删除费用结算方式
	public Boolean deleteGivenTypes(List<?> givenTypesList){
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < givenTypesList.size(); i++) {
			CotGivenType cotGivenType = (CotGivenType)givenTypesList.get(i);
			ids.add(cotGivenType.getId());
		}
		try {
			this.getGivenTypeDao().deleteRecordByIds(ids, "CotGivenType");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除送样方式异常", e);
			return false;
		}
		return true;
	}
	
	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo){
		try {
			return this.getGivenTypeDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}
	
	//根据条件查询主报价单记录
	public List<?> getList(QueryInfo queryInfo){
		try {
			return this.getGivenTypeDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getGivenTypeDao().getJsonData(queryInfo);
	}
	
	public CotGivenTypeDao getGivenTypeDao() {
		return givenTypeDao;
	}

	public void setGivenTypeDao(CotGivenTypeDao givenTypeDao) {
		this.givenTypeDao = givenTypeDao;
	}

	
	
}
