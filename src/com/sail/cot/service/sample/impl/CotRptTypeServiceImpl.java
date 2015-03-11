package com.sail.cot.service.sample.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotRptType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotRptTypeService;
import com.sail.cot.util.Log4WebUtil;

public class CotRptTypeServiceImpl implements CotRptTypeService {

	private CotBaseDao cotBaseDao;
	
	private Logger logger = Log4WebUtil.getLogger(CotRptTypeServiceImpl.class);
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
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

	public void addRptType(List<CotRptType> rpttypeList) {
		 
        try {
			this.getCotBaseDao().saveRecords(rpttypeList);
		} catch (DAOException e) {
			 e.printStackTrace();
			logger.error("添加报表类型异常",e);
		}
	}

	public int deleteRptType(List<CotRptType> rpttypeList) {
		 
        List<Integer> ids = new ArrayList<Integer>();
        int res = 0;
        for (int i = 0; i < rpttypeList.size(); i++) {
        	CotRptType cotRptType = (CotRptType)rpttypeList.get(i);
        	ids.add(cotRptType.getId());
		}
        try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotRptType");
		} catch (DAOException e) {
			 e.printStackTrace();
			logger.error("删除报表类型异常",e);
			res = -1;
		}
		 return res;
	}
	
	public void modifyRptType(List<CotRptType> rpttypeList) {
		 
		try {
			this.getCotBaseDao().updateRecords(rpttypeList);
		} catch (Exception e) {
			 e.printStackTrace();
			logger.error("修改报表类型异常",e);
		}
	}

	public boolean findExistByName(String name) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("select from CotRptType obj where obj.rptName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotRptType obj where obj.rptName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count>0)
			{
				isExist = true;
			}
			
		} catch (DAOException e) {
			 e.printStackTrace();
			logger.error("查找重复方法失败", e);
		}
		
		return isExist;
	}

	 

	public CotRptType getRptTypeById(Integer Id) {
		 
		return (CotRptType)this.getCotBaseDao().getById(CotRptType.class, Id);
	}

	public List<?> getRptTypeList() {
		 
		return this.getCotBaseDao().getRecords("CotRptType");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.sample.CotRptTypeService#getJsonData(com.sail.cot.query.QueryInfo)
	 */


	 

}
