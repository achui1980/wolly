 
package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.query.QueryInfo;
 
public interface CotSysLogService {
	
	//添加系统日志信息(批量)
	public void addSysLog(List<CotSyslog> logList);
	
	//添加系统日志信息
	public void addSysLogByObj(CotSyslog obj);
	
	//通过id获取系统日志对象
	public CotSyslog getSyslogById(Integer Id);
	
	//删除系统日志 
	public Boolean deleteSyslogById(Integer id);
	 
	//批量删除系统日志
	public Boolean deleteSyslogByIds(List<Integer> ids);
 
	// 根据条件查询系统日志记录
	public List<?> getSyslogList(QueryInfo queryInfo);
	
	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo);
	
	//查询所有员工
	public Map<?, ?> getEmpsMap();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
