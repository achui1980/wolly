package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotConsignCompany;
import com.sail.cot.query.QueryInfo;

public interface CotConsignCompanyService {

	public void addConsignCompany(CotConsignCompany cotConsignCompany);  
	
	public void modifyConsignCompany(CotConsignCompany cotConsignCompany);   
	
	public void deleteConsignCompany(List<CotConsignCompany> ConsignCompanyList);  
	
	public CotConsignCompany getConsignCompanyById(Integer id);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getConsignCompanyList() ;
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
