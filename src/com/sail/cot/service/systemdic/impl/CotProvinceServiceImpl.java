package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
 
 
import com.sail.cot.domain.CotNation;
import com.sail.cot.domain.CotProvince;
import com.sail.cot.query.QueryInfo;
 
import com.sail.cot.service.systemdic.CotProvinceService;
import com.sail.cot.util.Log4WebUtil;

public class CotProvinceServiceImpl implements CotProvinceService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotProvinceServiceImpl.class);
	
	public void addProvince(List<CotProvince> provinceList) {
		try {
			this.getCotBaseDao().saveRecords(provinceList);
		} catch (DAOException e) {
			 
			e.printStackTrace();
			logger.error("添加省/州信息异常",e);
		}
	}

	public int deleteProvince(List<CotProvince> provinceList) {
		 List<Integer> ids=new ArrayList<Integer>();
         int res = 0;
         for(int i=0; i<provinceList.size(); i++)
 		{
        	CotProvince cotProvince = (CotProvince)provinceList.get(i);
 			ids.add(cotProvince.getId());
 		}
          try{
         	this.getCotBaseDao().deleteRecordByIds(ids, "CotProvince");
         }
         catch(DAOException e)
         {
         	logger.error("删除省/州信息异常",e);
         	res = -1;
 		}
 		return res;
	}

	public void modifyProvince(List<CotProvince> provinceList) {
		 try{
      	   this.getCotBaseDao().updateRecords(provinceList);
         }catch(Exception ex)
         {
      	   logger.error("更新省/州信息异常", ex);
         }

	}
	
	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotProvince obj where obj.provinceName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotProvince obj where obj.provinceName='"+name+"'");
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
		List<?> res = this.getCotBaseDao().getRecords("CotNation");
		for(int i=0; i<res.size(); i++)
		{
			CotNation cotNation = (CotNation)res.get(i);
			retur.put(cotNation.getId().toString(),cotNation.getNationName());
			
		}
		return retur;
	}

	public CotProvince getProvinceById(Integer Id) {
		return (CotProvince)this.getCotBaseDao().getById(CotProvince.class, Id);
	}

	public List<?> getProvinceList() {
		return this.getCotBaseDao().getRecords("CotProvince");
	}

	public List<?> getProvinceListByNationId(Integer nationId) {
		 
		return this.getCotBaseDao().find("from CotProvince obj where obj.nationId="+nationId);
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
	 * @see com.sail.cot.service.systemdic.CotProvinceService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	} 

}
