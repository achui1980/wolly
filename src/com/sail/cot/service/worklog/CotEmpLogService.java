package com.sail.cot.service.worklog;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotEmpLog;
import com.sail.cot.query.QueryInfo;


public interface CotEmpLogService {

	//添加或修改
	public boolean saveOrUpdateEmpLog(CotEmpLog empLog,String logDate);
	
	//批量删除
	public int deleteEmpLogByList(List<Integer> ids);
	
	public CotEmpLog geEmpLogById(Integer id);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
