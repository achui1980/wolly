/**
 * 
 */
package com.sail.cot.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

import org.apache.log4j.Logger;
import com.sail.cot.util.Log4WebUtil;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCity;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotCityService;

 
public class CotCityServiceImpl implements CotCityService {

	 
	private CotBaseDao cityDao;
	private Logger logger = Log4WebUtil.getLogger(CotCityServiceImpl.class);
	public void addCity(List<?> cityList) {
		try {
			this.getCityDao().saveRecords(cityList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加城市信息异常",e);
			
		}
	}

	 
	public int deleteCity(List<?> cityList) {
		List<Integer> ids = new ArrayList<Integer>();
		int res = 0;
		for(int i=0; i<cityList.size(); i++)
		{
			CotCity cotCity = (CotCity)cityList.get(i);
			ids.add(cotCity.getId());
			
		}
		try {
			this.getCityDao().deleteRecordByIds(ids, "CotCity");
		} catch (DAOException e) {
			logger.error("删除城市信息异常",e);
			res = -1;
		}
		return res;
	}

	 
	public CotCity getCityById(Integer Id) {
		
		return (CotCity) this.getCityDao().getById(CotCity.class, Id);
	}

	 
	public List<?> getCityList() {
		 
		return this.getCityDao().getRecords("CotCity");
	}

	 
	public void modifyCity(List<?> cityList) {
		try
		{
			this.getCityDao().updateRecords(cityList);		
		}
		catch(Exception ex)
		{
			logger.error("更新城市信息异常", ex);
		}
	}

	public CotBaseDao getCityDao() {
		return cityDao;
	}

	public void setCityDao(CotBaseDao cityDao) {
		this.cityDao = cityDao;
	}

	 
	 

	 
	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCity obj where obj.cityName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotCity obj where obj.cityName='"+name+"'");
		try {
			int count = this.getCityDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	 
	public Map<String, String> getMap() {
		 
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCityList();
		for(int i=0; i<res.size(); i++)
		{
			CotCity cotCity = (CotCity)res.get(i);
			retur.put(cotCity.getId().toString(), cotCity.getCityName());
			
		}
		return retur;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCityDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCityDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCityDao().getJsonData(queryInfo);
	}
}
