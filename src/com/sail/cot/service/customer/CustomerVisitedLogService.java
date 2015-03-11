package com.sail.cot.service.customer;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CustomerVisitedLog;
import com.sail.cot.query.QueryInfo;

public interface CustomerVisitedLogService {

	List<?> getCustomerVisitedLogList();
	 
	CustomerVisitedLog getCustomerVisitedLogById(Integer Id);
	 
	void addCustomerVisitedLog(List<CustomerVisitedLog> CustomerVisitedLogList);
 
	void modifyCustomerVisitedLog(List<CustomerVisitedLog> CustCustomerVisitedLogList);
	 
	void deleteCustomerVisitedLog(List<CustomerVisitedLog> CustomerVisitedLogList);
	
	void deleteById(Integer Id);
 
	boolean findExistByName(String name);
	
	Map<?, ?> getCustomerMap();
	
	Timestamp getTimestamp(String time);
	
	String getStringTime(Timestamp time);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
