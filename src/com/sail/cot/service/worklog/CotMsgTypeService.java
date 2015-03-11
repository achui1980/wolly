package com.sail.cot.service.worklog;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotMsgType;
import com.sail.cot.query.QueryInfo;

public interface CotMsgTypeService {

	List getMsgTypeList();
	 
	CotMsgType getMsgTypeById(Integer Id);
	 
	void addMsgType(List MsgTypeList);
 
	boolean modifyMsgType(List MsgTypeList);
	 
	int deleteMsgType(List MsgTypeList);
 
	boolean findExistByName(String name);
	
	public List getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public Integer isExistMsgTypeId(String msgTypeName);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
