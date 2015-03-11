/**
 * 
 */
package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotMeetType;
import com.sail.cot.query.QueryInfo;


public interface CotMeetTypeService {
	
	//根据接单编号取得对象
	public CotMeetType getMeetTypeById(Integer id);
	
	//保存接单方式
	public Boolean addMeetTypes(List<?> meetTypesList);
	
	//更新接单方式
	public Boolean modifyMeetTypes(List<?> meetTypesList);
	
	//删除接单方式
	public Boolean deleteMeetTypes(List<?> meetTypesList);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
