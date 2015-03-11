package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotTargetPort;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTargetPortService;
import com.sail.cot.util.Log4WebUtil;

public class CotTargetPortServiceImpl implements CotTargetPortService {

private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotTargetPortServiceImpl.class);
	
	public void addTargetPort(List TargetPortList) {
		try {
			this.getCotBaseDao().saveRecords(TargetPortList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加目的港信息异常",e);
		}

	}

	public int deleteTargetPort(List TargetPortList) {
		List ids = new ArrayList();
		int res = 0;
		for (int i = 0; i < TargetPortList.size(); i++) {
			CotTargetPort cotTargetPort = (CotTargetPort) TargetPortList.get(i);
			ids.add(cotTargetPort.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotTargetPort");
		} catch (DAOException e) {
			logger.error("删除目的港信息异常",e);
			res = -1;
		}
		return res;
	}
	
	public boolean modifyTargetPort(List TargetPortList) {
		try {
			this.getCotBaseDao().updateRecords(TargetPortList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新目的港信息异常",e);
			return false;
		}

	}

	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotTargetPort obj where obj.targetPortEnName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotTargetPort obj where obj.targetPortEnName='"+name+"'");
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
	public CotTargetPort getTargetPortById(Integer Id) {
		 
		return (CotTargetPort) this.getCotBaseDao().getById(CotTargetPort.class, Id);
	}

	public List getTargetPortList() {
		return this.getCotBaseDao().getRecords("CotTargetPort");
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
