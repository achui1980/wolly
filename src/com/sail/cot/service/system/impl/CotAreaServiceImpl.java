package com.sail.cot.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotArea;
import com.sail.cot.domain.CotCity;
import com.sail.cot.domain.vo.CotTreeNode;
 
 
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotAreaService;
import com.sail.cot.util.Log4WebUtil;

public class CotAreaServiceImpl implements CotAreaService {

	private CotBaseDao areaDao;
	public CotBaseDao getAreaDao() {
		return areaDao;
	}

	public void setAreaDao(CotBaseDao areaDao) {
		this.areaDao = areaDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotAreaServiceImpl.class);
	
	public void addArea(List<CotArea> areaList) {
		 
        try {
			this.getAreaDao().saveRecords(areaList);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
	}

	public int deleteArea(List<CotArea> areaList) {
		 
            List<Integer> ids=new ArrayList<Integer>();
            int res = 0;
            for(int i=0; i<areaList.size(); i++)
    		{
    			CotArea cotArea = (CotArea)areaList.get(i);
    			ids.add(cotArea.getId());
    			
    		}
             try{
            	this.getAreaDao().deleteRecordByIds(ids, "CotArea");
            }
            catch(DAOException e)
            {
            	logger.error("删除地区信息异常",e);
            	res = -1;
    		}
    		return res;
	}

	public void modifyArea(List<CotArea> areaList) {
		 
           try{
        	   this.getAreaDao().updateRecords(areaList);
           }catch(Exception ex)
           {
        	   logger.error("更新地区信息异常", ex);
           }
	}
	
	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotArea obj where obj.areaName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotArea obj where obj.areaName='"+name+"'");
		try {
			int count = this.getAreaDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	 

	public CotArea getAreaById(Integer Id) {
		 
		return (CotArea)this.getAreaDao().getById(CotArea.class, Id);
	}

	public List<?> getAreaList() {
		 
		return this.getAreaDao().getRecords("CotArea");
	}

	public Map<?, ?> getMap() {
		 
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getAreaDao().getRecords("CotCity");
		for(int i=0; i<res.size(); i++)
		{
			CotCity cotCity = (CotCity)res.get(i);
			retur.put(cotCity.getId().toString(),cotCity.getCityName());
			
		}
		return retur;
	}
 
	@SuppressWarnings("unchecked")
	public List<CotArea> getAreaListByCityId(Integer cityId) {
		 
		return this.areaDao.find("from CotArea a where a.cityId="+cityId);
	}

	 
	public List<CotTreeNode> getCityAreaTree() {
		List<?> cityList = this.getAreaDao().getRecords("CotCity");
		List<?> areaList = this.getAreaDao().getRecords("CotArea");
		List<CotTreeNode> res = new ArrayList<CotTreeNode>();
		for(int i=0; i<cityList.size(); i++)
		{
			CotCity city = (CotCity)cityList.get(i);
			CotTreeNode rootNode = new CotTreeNode();
			rootNode.setNodeid("root_"+city.getId());
			rootNode.setNodename(city.getCityName());
			rootNode.setParentnodeid(null);
			res.add(rootNode);
			for(int j=0; j<areaList.size(); j++)
			{
				CotArea area = (CotArea)areaList.get(j);
				if(area.getCityId().intValue() != city.getId().intValue())
					continue;
				CotTreeNode node = new CotTreeNode();
				node.setNodeid(city.getId()+"_"+area.getId());
				node.setNodename(area.getAreaName());
				node.setParentnodeid(rootNode.getNodeid());
				res.add(node);
			}
		}
		return res;
	}
	
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getAreaDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getAreaDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotAreaService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getAreaDao().getJsonData(queryInfo);
	}
}
