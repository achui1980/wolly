package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustomerLv;
import com.sail.cot.query.QueryInfo;

public interface CotCustomerLvService {

	List getCustomerLvList();
	 
	CotCustomerLv getCustomerLvById(Integer Id);
	 
	void addCustomerLv(List CustomerLvList);
 
	boolean modifyCustomerLv(List CustomerLvList);
	 
	int deleteCustomerLv(List CustomerLvList);
 
	boolean findExistByName(String name);

    public	List getList(QueryInfo queryInfo);

    public int getRecordCount(QueryInfo queryInfo);
	
	Integer isExistCustomLvId(String customerLvName);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
