/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotDays;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotDaysService;

 
public class CotDaysServiceImpl implements CotDaysService {
	 
	private CotBaseDao daysDao;

	public CotBaseDao getDaysDao() {
		return daysDao;
	}

	public void setDaysDao(CotBaseDao daysDao) {
		this.daysDao = daysDao;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getDaysDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getDaysDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getDaysDao().getJsonData(queryInfo);
	}
	
	public void saveOrUpdate(CotDays cotDays){
		List records = new ArrayList();
		records.add(cotDays);
		this.getDaysDao().saveOrUpdateRecords(records);
	}
	
	public void deleteDays(List ids) throws DAOException{
		this.getDaysDao().deleteRecordByIds(ids, "CotDays");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotDaysService#checkIsExist(java.lang.Integer, java.lang.Integer)
	 */
	public boolean checkIsExist(Integer shipId,Integer tarId,Integer id){
		String hql="select obj.id from CotDays obj where obj.shipId="+shipId+" and obj.tarId="+tarId;
		List list=this.getDaysDao().find(hql);
		if(list!=null && list.size()>0){
			if(list.get(0).toString().equals(id.toString())){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
}
