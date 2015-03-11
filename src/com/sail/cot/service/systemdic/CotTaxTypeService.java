package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotTaxType;
import com.sail.cot.query.QueryInfo;

public interface CotTaxTypeService {

	public void addTaxType(CotTaxType cotTaxType);  
	
	public void modifyTaxType(CotTaxType cotTaxType);   
	
	public void deleteTaxType(List<CotTaxType> TaxTypeList);  
	
	public CotTaxType getTaxTypeById(Integer id);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List getTaxTypeList();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
