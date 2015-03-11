package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.query.QueryInfo;

public interface CotContactService {
	
public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);

	List<?> getContactList();
	 
	CotContact getContactById(Integer Id);
	
	public CotFactory getFactoryById(Integer Id);
	 
	boolean addContact(List<CotContact> contactList);
 
	boolean modifyContact(List<CotContact> contactList);
	 
	boolean deleteContact(List<CotContact> contactList);
 
	boolean findExistByName(String name,String factoryId);
	
	Integer findExistByEMail(String email);
	
	Map<?, ?> getFactoryMap();
	
	boolean findContactRecordsCount(String factoryId);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//查找是否重复
	public Integer findExistByNo(String No,String factoryId, String id);
	
	//保存和修改
	public boolean saveOrUpdate(CotContact contact);
	
	//判断联系人帐号是否重复 也不能和员工的账户重复
	public boolean findExistLoginName(String loginName,String id);
	
}
