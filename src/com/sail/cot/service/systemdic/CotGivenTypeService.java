/**
 * 
 */
package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.query.QueryInfo;

public interface CotGivenTypeService {
	
	//保存送样方式
	public Boolean addGivenTypes(List<?> givenTypesList);
	
	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);
	
	//根据条件查询主报价单记录
	public List<?> getList(QueryInfo queryInfo);
	
	//更新送样方式
	public Boolean modifyGivenTypes(List<?> givenTypesList);
	
	//删除送样方式
	public Boolean deleteGivenTypes(List<?> givenTypesList);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
}
