package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCutType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotCutTypeService;
import com.sail.cot.util.Log4WebUtil;

public class CotCutTypeServiceImpl implements CotCutTypeService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotCutTypeServiceImpl.class);

	public void addCutType(List CutTypeList) {
		try {
			this.getCotBaseDao().saveRecords(CutTypeList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加折扣类型信息异常",e);
		}

	}

	public int deleteCutType(List CutTypeList) {
		List ids = new ArrayList();
		int res = 0;
		for (int i = 0; i < CutTypeList.size(); i++) {
			CotCutType cotCutType = (CotCutType) CutTypeList.get(i);
			ids.add(cotCutType.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCutType");
		} catch (DAOException e) {
			logger.error("删除折扣类型信息异常",e);
			res = -1;
		}
		return res;
	}
	
	public void modifyCutType(List CutTypeList) {
		try {
			this.getCotBaseDao().updateRecords(CutTypeList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新折扣类型信息异常",e);
		}

	}

	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCutType obj where obj.cutName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotCutType obj where obj.cutName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}
	
	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name) {
		String strSql = " from CotCutType obj where obj.cutName = '"+name+"'";
		List<?> res = null;
		res = this.getCotBaseDao().find(strSql);
		if(res == null || res.size() == 0){  
			return false;
		}else{
			CotCutType cotCutType = (CotCutType) res.get(0);
			Integer expid = cotCutType.getId();
			if(expid.toString().equals(id)){
				return false;
			}
		}	
		return true;
	}
	
	public CotCutType getCutTypeById(Integer Id) {
		 
		return (CotCutType) this.getCotBaseDao().getById(CotCutType.class, Id);
	}

	public List getCutTypeList() {
		return this.getCotBaseDao().getRecords("CotCutType");
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
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}
}
