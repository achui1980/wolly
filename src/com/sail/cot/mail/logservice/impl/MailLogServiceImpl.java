/**
 * 
 */
package com.sail.cot.mail.logservice.impl;

import java.util.List;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotMailLog;
import com.sail.cot.mail.logservice.MailLogService;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 27, 2010 5:49:17 PM </p>
 * <p>Class Name: MailLogServiceImpl.java </p>
 * @author achui
 *
 */
public class MailLogServiceImpl implements MailLogService{
	private CotBaseDao baseDao;

	public CotBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(CotBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public void saveMailLog(List<CotMailLog> logs) {
		// TODO Auto-generated method stub
		this.getBaseDao().saveOrUpdateRecords(logs);
	}
}
