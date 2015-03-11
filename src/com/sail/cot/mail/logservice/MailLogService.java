/**
 * 
 */
package com.sail.cot.mail.logservice;

import java.util.List;

import com.sail.cot.domain.CotMailLog;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 27, 2010 5:48:53 PM </p>
 * <p>Class Name: MailLogService.java </p>
 * @author achui
 *
 */
public interface MailLogService {
	
	public void saveMailLog(List<CotMailLog> logs);
}
