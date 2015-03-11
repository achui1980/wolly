package com.sail.cot.service.worklog.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmpLog;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.worklog.CotEmpLogService;
import com.sail.cot.util.Log4WebUtil;

public class CotEmpLogServiceImpl implements CotEmpLogService {
	
	private CotBaseDao cotBaseDao ;
	private Logger logger = Log4WebUtil.getLogger(CotEmpLogServiceImpl.class);
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}
	
	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	public boolean saveOrUpdateEmpLog(CotEmpLog packing,String logDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (logDate != null && !"".equals(logDate)) {
				packing.setLogDate(new Timestamp(sdf.parse(logDate)
						.getTime()));
			}
			packing.setAddTime(new Date());
			List list = new ArrayList();
			list.add(packing);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int deleteEmpLogByList(List<Integer> ids) {
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotEmpLog");
			return 0;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("批量删除异常",e);
			return -1;
		}
	}

	public CotEmpLog geEmpLogById(Integer id) {
		return (CotEmpLog) this.getCotBaseDao().getById(CotEmpLog.class, id);
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return getCotBaseDao().getJsonData(queryInfo);
	}

}
