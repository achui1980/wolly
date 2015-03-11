package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotTargetPort;
import com.sail.cot.query.QueryInfo;

public interface CotTargetPortService {

	List getTargetPortList();
	 
	CotTargetPort getTargetPortById(Integer Id);
	 
	void addTargetPort(List TargetPortList);
 
	boolean modifyTargetPort(List TargetPortList);
 
	int deleteTargetPort(List TargetPortList);
 
	boolean findExistByName(String name);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;

}
