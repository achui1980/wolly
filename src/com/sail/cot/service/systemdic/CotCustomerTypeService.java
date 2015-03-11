package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustomerType;
import com.sail.cot.query.QueryInfo;

public interface CotCustomerTypeService {

	List<?> getCustomerTypeList();
	 
	CotCustomerType getCustomerTypeById(Integer Id);
	 
	void addCustomerType(List<CotCustomerType> customertypeList);
 
	boolean modifyCustomerType(List<CotCustomerType> customertypeList);
 
	int deleteCustomerType(List<CotCustomerType> customertypeList);
 
	boolean findExistByName(String name);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
