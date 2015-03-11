/**
 * 
 */
package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotSignType;
import com.sail.cot.query.QueryInfo;

public interface CotSignTypeService {
	
	//根据送样编号取得对象
	public CotSignType getSignTypeById(Integer id);
	
	//保存送样方式
	public Boolean addSignTypes(List<?> signTypesList);
	
	//更新送样方式
	public Boolean modifySignTypes(List<?> signTypesList);
	
	//删除送样方式
	public Boolean deleteSignTypes(List<?> signTypesList);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;

}
