package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCutType;
import com.sail.cot.query.QueryInfo;

public interface CotCutTypeService {

	List getCutTypeList();
	 
	CotCutType getCutTypeById(Integer Id);
	 
	void addCutType(List CutTypeList);
 
	void modifyCutType(List CutTypeList);
 
	int deleteCutType(List CutTypeList);
 
	boolean findExistByName(String name);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
