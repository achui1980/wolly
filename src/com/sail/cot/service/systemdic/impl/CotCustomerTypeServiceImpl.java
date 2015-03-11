package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustomerType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotCustomerTypeService;
import com.sail.cot.util.Log4WebUtil;

public class CotCustomerTypeServiceImpl implements CotCustomerTypeService {

    private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotCustomerTypeServiceImpl.class);
	
	public void addCustomerType(List<CotCustomerType> customertypeList) {
		try {
			this.getCotBaseDao().saveRecords(customertypeList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加客户类型信息异常",e);
		}

	}

	public int deleteCustomerType(List<CotCustomerType> customertypeList) {
		List<Integer> ids = new ArrayList<Integer>();
		int res = 0;
		for (int i = 0; i < customertypeList.size(); i++) {
			CotCustomerType cotCustomerType = (CotCustomerType) customertypeList.get(i);
			ids.add(cotCustomerType.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCustomerType");
		} catch (DAOException e) {
			logger.error("删除客户类型信息异常",e);
			res = -1;
		}
		return res;
	}
	
	public boolean modifyCustomerType(List<CotCustomerType> customertypeList) {
		try {
			this.getCotBaseDao().updateRecords(customertypeList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新客户类型信息异常",e);
			return false;
		}

	}

	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCustomerType obj where obj.typeName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotCustomerType obj where obj.typeName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}
	public CotCustomerType getCustomerTypeById(Integer Id) {
		 
		return (CotCustomerType) this.getCotBaseDao().getById(CotCustomerType.class, Id);
	}

	public List<?> getCustomerTypeList() {
		return this.getCotBaseDao().getRecords("CotCustomerType");
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
