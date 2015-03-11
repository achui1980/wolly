 
package com.sail.cot.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps; 
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotSysLogService;

 
public class CotSysLogServiceImpl implements CotSysLogService {

	private CotBaseDao logDao;
	public CotBaseDao getLogDao() {
		return logDao;
	}

	public void setLogDao(CotBaseDao logDao) {
		this.logDao = logDao;
	}
	
	//添加系统日志信息(批量)
	public  void addSysLog(List<CotSyslog> logList) {
		try {
			this.getLogDao().saveRecords(logList);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	//添加系统日志信息
	public void addSysLogByObj(CotSyslog obj) {
		this.getLogDao().create(obj);	
	}
	
	//删除系统日志 
	public Boolean deleteSyslogById(Integer id) {
		try {
			this.getLogDao().deleteRecordById(id, "CotSyslog");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//批量删除系统日志 
	public Boolean deleteSyslogByIds(List<Integer> ids) {
		try {
			this.getLogDao().deleteRecordByIds(ids, "CotSyslog");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//查询所有员工
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getLogDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 根据条件查询系统日志记录
	public List<?> getSyslogList(QueryInfo queryInfo) {
	
			List<?> records = this.getLogDao().getRecords("CotSyslog");
			return records;
		
	}

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getLogDao().getRecordsCountJDBC(queryInfo);
	}

	//通过id获取系统日志对象
	public CotSyslog getSyslogById(Integer Id) {
		return (CotSyslog) this.getLogDao().getById(CotSyslog.class, Id);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotSysLogService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getLogDao().getJsonData(queryInfo);
	}

}
