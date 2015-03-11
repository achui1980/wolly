package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotNation;
import com.sail.cot.domain.CotNationCity;
import com.sail.cot.domain.CotProvince;
 
 
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotNationCityService;
import com.sail.cot.util.Log4WebUtil;

public class CotNationCityServiceImpl implements CotNationCityService {

private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotNationCityServiceImpl.class);
	
	public void addNationCity(List<CotNationCity> nationcityList) {
		try {
			this.getCotBaseDao().saveRecords(nationcityList);
		} catch (DAOException e) {
			 
			e.printStackTrace();
			logger.error("添加城市信息异常",e);
		}

	}

	public int deleteNationCity(List<CotNationCity> nationcityList) {
		 List<Integer> ids=new ArrayList<Integer>();
         int res = 0;
         for(int i=0; i<nationcityList.size(); i++)
 		{
        	CotNationCity cotNationCity = (CotNationCity)nationcityList.get(i);
 			ids.add(cotNationCity.getId());
 		}
          try{
         	this.getCotBaseDao().deleteRecordByIds(ids, "CotNationCity");
         }
         catch(DAOException e)
         {
         	logger.error("删除城市信息异常",e);
         	res = -1;
 		}
 		return res;
	}
	
	public void modifyNationCity(List<CotNationCity> nationcityList) {
		 try{
	      	   this.getCotBaseDao().updateRecords(nationcityList);
	         }catch(Exception ex)
	         {
	      	   logger.error("更新城市信息异常", ex);
	         }

	}

	public CotNationCity getNationCityById(Integer Id) {
		return (CotNationCity)this.getCotBaseDao().getById(CotNationCity.class, Id);
	}

	public List<?> getNationCityList() {
		return this.getCotBaseDao().getRecords("CotNationCity");
	}

	public Map<String, String> getNationMap() {
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotNation");
		for(int i=0; i<res.size(); i++)
		{
			CotNation cotNation = (CotNation)res.get(i);
			retur.put(cotNation.getId().toString(),cotNation.getNationName());
			
		}
		return retur;
	}

	public Map<String, String> getProvinceMap() {
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotProvince");
		for(int i=0; i<res.size(); i++)
		{
			CotProvince cotProvince = (CotProvince)res.get(i);
			retur.put(cotProvince.getId().toString(),cotProvince.getProvinceName());
			
		}
		return retur;
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
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getCotBaseDao().getJsonData(queryInfo);
	}
	
	//查询城市名称是否重复
	public Integer findExistByNo(String name, String id) {
		String hql = "select obj.id from CotNationCity obj where obj.cityName=?";
		Object[] obj = new Object[1];
		obj[0]=name;
		List<?> res = this.getCotBaseDao().queryForLists(hql, obj);
		if (res==null || res.size() == 0) {
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
	
	//保存
	public boolean saveOrUpdate(CotNationCity eleOther) {
		try {
			List list = new ArrayList();
			list.add(eleOther);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
