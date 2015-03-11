package com.sail.cot.service.sample;

import java.util.List;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotRptType; 
import com.sail.cot.query.QueryInfo;

public interface CotRptTypeService {

	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	List<?> getRptTypeList();
	 
	CotRptType getRptTypeById(Integer Id);
	 
	void addRptType(List<CotRptType> rpttypeList);
 
	void modifyRptType(List<CotRptType> rpttypeList);
	 
	int deleteRptType(List<CotRptType> rpttypeList);
 
	boolean findExistByName(String name);
	
	
}
