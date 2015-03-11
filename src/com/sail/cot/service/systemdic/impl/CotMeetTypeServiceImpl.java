/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.systemdic.CotMeetTypeDao;
import com.sail.cot.domain.CotMeetType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotMeetTypeService;
import com.sail.cot.util.Log4WebUtil;


public class CotMeetTypeServiceImpl implements CotMeetTypeService {

	private CotMeetTypeDao meetTypeDao;
	private Logger logger = Log4WebUtil.getLogger(CotMeetTypeServiceImpl.class);
	//根据接单编号取得对象
	public CotMeetType getMeetTypeById(Integer id){
		Object object = this.getMeetTypeDao().getById(CotMeetType.class, id);
		if(object!=null){
			return (CotMeetType)object;
		}else{
			return null;
		}
	}
	
	//保存接单方式
	public Boolean addMeetTypes(List<?> meetTypesList){
		for (int i = 0; i < meetTypesList.size(); i++) {
			CotMeetType cotMeetType = (CotMeetType)meetTypesList.get(i);
			Integer id = this.getMeetTypeDao().isExistMeetName(cotMeetType.getMeetName());
			if(id!=null){
				return false;
			}
		}
		try {
			this.getMeetTypeDao().saveRecords(meetTypesList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加接单方式异常", e);
			return false;
		}
		return true;
	}
	
	//更新接单方式
	public Boolean modifyMeetTypes(List<?> meetTypesList){
		for (int i = 0; i < meetTypesList.size(); i++) {
			CotMeetType cotMeetType = (CotMeetType)meetTypesList.get(i);
			Integer id = this.getMeetTypeDao().isExistMeetName(cotMeetType.getMeetName());
			if(id!=null && !id.toString().equals(cotMeetType.getId().toString())){
				return false;
			}
		}
		try {
			this.getMeetTypeDao().updateRecords(meetTypesList);
		} catch (Exception e) {
			logger.error("更新接单方式异常", e);
		}
		return true;
	}
	
	//删除接单方式
	public Boolean deleteMeetTypes(List<?> meetTypesList){
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < meetTypesList.size(); i++) {
			CotMeetType cotMeetType = (CotMeetType)meetTypesList.get(i);
			ids.add(cotMeetType.getId());
		}
		try {
			this.getMeetTypeDao().deleteRecordByIds(ids, "CotMeetType");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除接单方式异常", e);
			return false;
		}
		return true;
	}

	public CotMeetTypeDao getMeetTypeDao() {
		return meetTypeDao;
	}

	public void setMeetTypeDao(CotMeetTypeDao meetTypeDao) {
		this.meetTypeDao = meetTypeDao;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getMeetTypeDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getMeetTypeDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getMeetTypeDao().getJsonData(queryInfo);
	}
	
}
