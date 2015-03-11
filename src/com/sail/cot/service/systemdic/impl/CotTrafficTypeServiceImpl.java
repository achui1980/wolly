package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotInsureContract;
import com.sail.cot.domain.CotTrafficType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTrafficTypeService;

public class CotTrafficTypeServiceImpl implements CotTrafficTypeService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public boolean addTrafficType(CotTrafficType cotTrafficType) {
		 try {
			 this.getCotBaseDao().create(cotTrafficType);
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void modifyTrafficType(CotTrafficType cotTrafficType) {
		 
		this.getCotBaseDao().update(cotTrafficType);
	}

	public boolean deleteTrafficType(List<CotTrafficType> TrafficTypeList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < TrafficTypeList.size(); i++) {
			CotTrafficType cotTrafficType = (CotTrafficType) TrafficTypeList.get(i);
			ids.add(cotTrafficType.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotTrafficType");
			return true;
		} catch (DAOException e) {
			 
			e.printStackTrace();
			return false;
		}
	}

	public List<?> getList(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  null;
	}

	public CotTrafficType getTrafficTypeById(Integer id) {
		 
		return (CotTrafficType) this.getCotBaseDao().getById(CotTrafficType.class, id);
	}

	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotTrafficTypeService#getTrafficList()
	 */
	public List getTrafficList() {
		
		return this.getCotBaseDao().getRecords("CotTrafficType");
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}
	
	public Integer isExistName(String name) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotTrafficType c where c.trafficName='"+name+"'");
		if(res.size()!=1){
			return null;
		}else{
			return res.get(0);
		}
	}
	
	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name) {
		String strSql = " from CotTrafficType obj where obj.trafficName = '"+name+"'";
		List<?> res = null;
		res = this.getCotBaseDao().find(strSql);
		if(res == null || res.size() == 0){  
			return false;
		}else{
			CotTrafficType cotTrafficType = (CotTrafficType) res.get(0);
			Integer expid = cotTrafficType.getId();
			if(expid.toString().equals(id)){
				return false;
			}
		}	
		return true;
	}

}
