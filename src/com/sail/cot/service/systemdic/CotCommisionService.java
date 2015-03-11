package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCommisionType;
import com.sail.cot.query.QueryInfo;

public interface CotCommisionService {

	List getCommisionList();
	 
	CotCommisionType getCommisionById(Integer Id);
	 
	void addCommision(List CommisionList);
 
	boolean modifyCommision(List CommisionList);
	 
	int deleteCommision(List CommisionList);
 
	boolean findExistByName(String name);
	
	public List getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public Integer isExistCommisionId(String commisionName);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
