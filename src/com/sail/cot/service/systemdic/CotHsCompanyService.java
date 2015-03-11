package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotHsCompany;
import com.sail.cot.query.QueryInfo;

public interface CotHsCompanyService {

	public void addHsCompany(CotHsCompany cotHsCompany);  
	
	public void modifyHsCompany(CotHsCompany cotHsCompany);   
	
	public void deleteHsCompany(List<CotHsCompany> HsCompanyList);  
	
	public CotHsCompany getHsCompanyById(Integer id);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List getHsCompanyList();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
