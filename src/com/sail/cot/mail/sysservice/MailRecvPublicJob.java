/**
 * 
 */
package com.sail.cot.mail.sysservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotMail;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.mail.MailExecuteAction;
import com.sail.cot.mail.logservice.MailLogService;
import com.sail.cot.mail.ruleservice.MailRuleService;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:公共邮箱接收任务</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 7, 2010 10:54:16 AM </p>
 * <p>Class Name: MailRecvPublicJob.java </p>
 * @author achui
 *
 */
public class MailRecvPublicJob implements Job{
	private Logger logger = Log4WebUtil.getLogger(MailRecvPublicJob.class);
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	static String PUBLIC_RULE_FILE = MailRecvPublicJob.class.getResource("/").getPath()+"mailrules/Rule_PUBLIC_.xml";
	MailRuleService  mailRuleService;
	MailLocalService mailLocalService;
	MailLogService mailLogService;
	public void execute(JobExecutionContext ctx) throws JobExecutionException, NumberFormatException {
		System.out.println("XML PATH:"+PUBLIC_RULE_FILE);
		// TODO 公共邮接收
		JobDetail jobDetial = ctx.getJobDetail() ;
//		JobDataMap map = jobDetial.getJobDataMap();
		try {
			recvPublicAsign();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		System.out.println(new Date(System.currentTimeMillis())+":(公共邮箱)任务"+jobDetial.getName()+"正在接收邮件.....操作");
	}
	public void recvPublicAsign() throws NumberFormatException, DAOException {
		logger.debug("执行公共任务指派方法");
//		List allMailAction = new ArrayList();
		List<CotMail> list = this.getMailLocalService().saveMailAll();
		if(list == null || list.size() == 0) return;
		Map<String,List> allMailMapByEmpId = new HashMap<String,List>();
		for (CotMail cotMail : list) {
			List<MailExecuteAction> actionList = this.getMailRuleService().getRuleResult(cotMail, PUBLIC_RULE_FILE);
			for (MailExecuteAction mailExecuteAction : actionList) {
				System.out.println("员工ID："+mailExecuteAction.getEmpId());
				//执行指派动作
				mailExecuteAction.asignTo();
				//TODO：是否要执行移动动作？
				mailExecuteAction.moveToNode();
				//执行客户归档动作
				mailExecuteAction.archiveCust();
				//执行厂家归档动作
				mailExecuteAction.archiveFac();
//				CotMailLog mailLog = new CotMailLog();
//				mailLog.setAddTime(new Date(System.currentTimeMillis()));
//				mailLog.setEmpId(mailExecuteAction.getEmpId());
//				mailLog.setMailSubject(cotMailRecv.getMailSubject());
//				mailLog.setMsgId(cotMailRecv.getId());
//				logs.add(mailLog);
			}
		}
		//加入邮件日志
		//this.getMailLogService().saveMailLog(logs);
		String msg = "公共邮箱已指派%1$d封新邮件，请注意查看";
		Iterator iterator = allMailMapByEmpId.keySet().iterator();
		Cache cache4MailMsg = ContextUtil.getCache4MailMsg();
		msg = String.format(msg, list.size());
		Element element = new Element("PUBLIC_MAIL",msg);
		//加入缓存
		cache4MailMsg.put(element);
	}
	public MailRuleService getMailRuleService() {
		if(mailRuleService == null){
			mailRuleService = (MailRuleService)SystemUtil.getService("MailRuleService");
		}
		return mailRuleService;
	}
	public MailLocalService getMailLocalService() {
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)SystemUtil.getService("MailLocalService");
		}
		return mailLocalService;
	}

	public MailLogService getMailLogService() {
		if(mailLogService == null)
			mailLogService = (MailLogService)SystemUtil.getService("MailLogService");
		return mailLogService;
	}
	public void setMailLogService(MailLogService mailLogService) {
		this.mailLogService = mailLogService;
	}

}
