/**
 * 
 */
package com.sail.cot.mail.scheduler;
import java.util.HashMap;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

import com.sail.cot.mail.scheduler.QuartzManager;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:设置管理计划任务</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: May 11, 2009 11:39:14 AM </p>
 * <p>Class Name: SchedulerMail.java </p>
 * @author achui
 *
 */
public class SchedulerMail {
	
	private static HashMap<Object, Object> triggerMap = new HashMap<Object, Object>();
	public void addMailAuto(String jobName,int mintues,String smtpHost,String pop3Host,String account,String pwd) throws Exception
	{
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("smtpHost", smtpHost);
		dataMap.put("pop3Host", pop3Host);
		dataMap.put("account", account);
		dataMap.put("pwd", pwd);
		MailJobs job = new MailJobs();
		//添加任务并启动
		//判断某个job是否正在运行
		triggerMap.put(jobName, true);
		QuartzManager.addJob(jobName, job, mintues,dataMap);
	}
	
	public void removeJob(String jobName)
	{
		try {
			QuartzManager.removeJob(jobName);
			triggerMap.put(jobName, false);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isRun(String jobName)
	{
		Object job =  triggerMap.get(jobName);
		if(job!=null){
			boolean bRun = (Boolean)job;
			return bRun;
		}else{
			return false;
		}
	}
	
}
