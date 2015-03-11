package com.sail.cot.service.price;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCalculation;
import com.sail.cot.query.QueryInfo;

public interface CotCalculationService {

	public void addCalculation(CotCalculation cotCalculation);  
	
	public boolean modifyCalculation(CotCalculation cotCalculation);   
	
	public Integer deleteCalculation(List<CotCalculation> CalculationList);  
	
	public CotCalculation getCalculationById(Integer id);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getCalculationList(); 
	
	public boolean checkCalculation(String str);
	
	public boolean findExistByName(String name);
	
	public String replace(String strOriginal,String strOld,String strNew);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
