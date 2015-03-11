package com.sail.cot.service.systemdic;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotClause;
import com.sail.cot.query.QueryInfo;

public interface CotClauseService {

	List getClauseList();
	 
	CotClause getClauseById(Integer Id);
	 
	void addClause(List ClauseList);
 
	boolean modifyClause(List ClauseList);
	 
	int deleteClause(List ClauseList);
 
	public Integer isExistClauseId(String claName);
	 
	boolean findExistByName(String name);

    public	int getRecordCount(QueryInfo queryInfo);

    public	List getList(QueryInfo queryInfo);

	Map getMap();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
