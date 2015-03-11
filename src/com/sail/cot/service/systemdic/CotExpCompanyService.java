package com.sail.cot.service.systemdic;

import java.util.List;


import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotExpCompany;
import com.sail.cot.query.QueryInfo;

public interface CotExpCompanyService {

	List getExpCompanyList();
	 
	CotExpCompany getExpCompanyById(Integer Id);
	 
	void addExpCompany(List ExpCompanyList);
 
	boolean modifyExpCompany(List ExpCompanyList);
	 
	int deleteExpCompany(List ExpCompanyList);
	 
	boolean findExistByName(String name);

    public	List getList(QueryInfo queryInfo);

    public int getRecordCount(QueryInfo queryInfo);
	
	Integer isExistExpCompany(String expCompanyName);
	
	//判断快递公司是否存在 true:存在 false：不存在
	public boolean findExistExpCompany(String id,String name);
	
	//添加或修改
	public void saveOrUpdateExpCompany(List<CotExpCompany> expCompanyList);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
