package com.sail.cot.service.customer;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustLog;
import com.sail.cot.query.QueryInfo;


public interface CotCustLogService {

	//添加或修改
	public boolean saveOrUpdateCustLog(CotCustLog custLog,String logDate);
	
	//批量删除
	public int deleteCustLogByList(List<Integer> ids);
	
	public CotCustLog geCustLogById(Integer id);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);
	
	// 查询VO记录
	public List<?> getCustVOList(QueryInfo queryInfo);
}
