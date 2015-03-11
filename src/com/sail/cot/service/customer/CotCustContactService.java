package com.sail.cot.service.customer;

import java.util.List;
import java.util.Map;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustContact;  
import com.sail.cot.query.QueryInfo;

public interface CotCustContactService {

	List<?> getCustContactList();
	 
	CotCustContact getCustContactById(Integer Id);
	
	CotCustContact addCustContact(CotCustContact cotCustContact);
	
	void modifyCustContact(CotCustContact cotCustContact);
	 
	void deleteCustContact(List<CotCustContact> CustContactList);
 
	boolean findExistByName(String name);
	
	Integer findExistByEMail(String email);
	
	Map<?, ?> getCustomerMap();
	
	void modify(CotCustContact cotCustContact);
	
	public List<?> getList(QueryInfo queryInfo);
		
	public int getRecordCount(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//增加联系人时--增加规则
	public void addRule(Integer custId,Integer custContactId,String em);
}

