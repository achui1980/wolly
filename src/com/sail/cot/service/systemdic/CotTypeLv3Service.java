package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotTypeLv3;
import com.sail.cot.query.QueryInfo;

public interface CotTypeLv3Service {

	List getTypeLvList();
	 
	CotTypeLv3 getTypeLvById(Integer Id);
	 
	void addTypeLv(List TypeLvList);
 
	boolean modifyTypeLv(List TypeLvList);
	 
	int deleteTypeLv(List TypeLvList);
 
	boolean findExistByName(String name);

    public	List getList(QueryInfo queryInfo);

    public int getRecordCount(QueryInfo queryInfo);
	
	Integer isExistTypeLvId(String typeLvName);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
