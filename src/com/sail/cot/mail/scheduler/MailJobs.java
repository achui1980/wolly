/**
 * 
 */
package com.sail.cot.mail.scheduler;

import java.util.Date;
import com.sail.cot.util.SystemUtil;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sail.cot.mail.service.MailRecvService;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: May 11, 2009 11:39:52 AM </p>
 * <p>Class Name: MailJobs.java </p>
 * @author achui
 *
 */
public class MailJobs implements Job {

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		// TODO Auto-generated method stub
		//获取参数
		JobDataMap map = ctx.getJobDetail().getJobDataMap();
		String smtpHost = map.getString("smtpHost");
		String pop3Host = map.getString("pop3Host");
		String account = map.getString("account");
		String pwd = map.getString("pwd");
		String flag = map.getString("flag");
		//ReciveMailServiceImpl recv = new ReciveMailServiceImpl();
		MailRecvService recv = (MailRecvService)SystemUtil.getService("recvMailService");
		try {
			recv.saveReciveMail(smtpHost, pop3Host, account, pwd,flag,null);
			System.out.println("用户名："+account+"接收邮件完毕   "+ new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JobExecutionException(e.getMessage());
			
		}
	}

}
