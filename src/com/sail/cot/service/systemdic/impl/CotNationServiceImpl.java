package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
 
 
import com.sail.cot.domain.CotNation;
import com.sail.cot.query.QueryInfo;
 
import com.sail.cot.service.systemdic.CotNationService;
import com.sail.cot.util.Log4WebUtil;

public class CotNationServiceImpl implements CotNationService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotNationServiceImpl.class);

	public void addNation(List<CotNation> nationList) {
		try {
			this.getCotBaseDao().saveRecords(nationList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加国别信息异常",e);
		}
	}

	public int deleteNation(List<CotNation> nationList) {
		List<Integer> ids = new ArrayList<Integer>();
		int res = 0;
		for(int i=0; i<nationList.size(); i++)
		{
			CotNation cotNation = (CotNation)nationList.get(i);
			ids.add(cotNation.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotNation");
		} catch (DAOException e) {
			logger.error("删除国别信息异常",e);
			res = -1;
		}
		return res;
	}
	
	public void modifyNation(List<CotNation> nationList) {
		try {
			this.getCotBaseDao().updateRecords(nationList);		
		}
		catch(Exception ex) {
			logger.error("更新国别信息异常", ex);
		}
	}
	 

	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotNation obj where obj.nationName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotNation obj where obj.nationName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	 

	public Map<String, String> getMap() {
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getNationList();
		for(int i=0; i<res.size(); i++)
		{
			CotNation cotNation = (CotNation)res.get(i);
			retur.put(cotNation.getId().toString(), cotNation.getNationName());
			
		}
		return retur;
	}

	public CotNation getNationById(Integer Id) {
		return (CotNation) this.getCotBaseDao().getById(CotNation.class, Id);
	}

	public List<?> getNationList() {
		return this.getCotBaseDao().getRecords("CotNation");
	}

	 
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotNationService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

	 

	
}
