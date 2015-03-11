package com.sail.cot.service.customer.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CustomerVisitedLog;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CustomerVisitedLogService;
import com.sail.cot.util.Log4WebUtil;

public class CustomerVisitedLogServiceImpl implements CustomerVisitedLogService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CustomerVisitedLogServiceImpl.class);
	
	public void addCustomerVisitedLog(List<CustomerVisitedLog> CustomerVisitedLogList) {
		 
		try {
			this.getCotBaseDao().saveRecords(CustomerVisitedLogList);
		} catch (DAOException e) {
			 
			e.printStackTrace();
			logger.error("添加拜访者信息异常",e);
		}
	}
	
	public Timestamp getTimestamp(String time){
		Timestamp timestamp = null;
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(time);
		    timestamp = new Timestamp(date.getTime());
		} catch (ParseException e1) {
			 
			e1.printStackTrace();
		}
		return timestamp;
	}
	
	public String getStringTime(Timestamp time){
		String stringTime =null;
		if(time!=null){
			stringTime = time.toString();
			return stringTime;
		}
		return stringTime;
	}

	public void deleteCustomerVisitedLog(List<CustomerVisitedLog> CustomerVisitedLogList) {
		 
		List<Integer> ids=new ArrayList<Integer>();
        for(int i=0; i<CustomerVisitedLogList.size(); i++)
		{
        	CustomerVisitedLog CustomerVisitedLog = (CustomerVisitedLog)CustomerVisitedLogList.get(i);
			ids.add(CustomerVisitedLog.getId());
		}
         try{
        	this.getCotBaseDao().deleteRecordByIds(ids, "CustomerVisitedLog");
        }
        catch(DAOException e)
        {
        	logger.error("删除拜访者信息异常",e);
        }
	}

	public void deleteById(Integer Id) {
		
		try {
			this.getCotBaseDao().deleteRecordById(Id,"CustomerVisitedLog");
		} catch (DAOException e) {
			logger.error("删除拜访者信息异常",e); 
		}
	}

	public boolean findExistByName(String name) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CustomerVisitedLog obj where obj.contactPerson='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CustomerVisitedLog obj where obj.contactPerson='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	 
   
	public CustomerVisitedLog getCustomerVisitedLogById(Integer Id) {
		 
		return (CustomerVisitedLog)this.getCotBaseDao().getById(CustomerVisitedLog.class, Id);
	}

	public List<?> getCustomerVisitedLogList() {
		 
		return this.getCotBaseDao().getRecords("CustomerVisitedLog");
	}

	public Map<?, ?> getCustomerMap() {
		 
		Map<String, String> map = new HashMap<String, String>();
		List<?> list=this.cotBaseDao.getRecords("CotCustomer");
		for(int i=0;i<list.size();i++)
		{
			CotCustomer cotCustomer=(CotCustomer)list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer.getCustomerShortName());
		}
		 return map;
	}

	public void modifyCustomerVisitedLog(List<CustomerVisitedLog> CustCustomerVisitedLogList) {
		 
		try{
	      	this.getCotBaseDao().updateRecords(CustCustomerVisitedLogList);
	    }catch(Exception ex)
	     {
	      	 logger.error("更新拜访者信息异常", ex);
	     }
	}

	public List<?> getList(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  null;
	}
	
	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  0;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}
}
