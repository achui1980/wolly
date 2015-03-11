package com.sail.cot.service.systemdic;

import java.util.List;
import java.util.Map;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotNation;
import com.sail.cot.query.QueryInfo;

public interface CotNationService {

	List<?> getNationList();
	 
	CotNation getNationById(Integer Id);
	 
	void addNation(List<CotNation> nationList);
	 
	void modifyNation(List<CotNation> nationList);
 
	int deleteNation(List<CotNation> nationList);
 
	boolean findExistByName(String name);
	
	Map<?, ?> getMap();
	
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
}
