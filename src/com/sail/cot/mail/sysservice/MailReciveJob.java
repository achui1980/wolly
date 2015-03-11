/**
 * 
 */
package com.sail.cot.mail.sysservice;

import java.io.File;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailEmpsRule;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.mail.MailExecuteAction;
import com.sail.cot.mail.ruleservice.MailRuleService;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:私人邮箱任务</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 6, 2010 11:22:49 AM </p>
 * <p>Class Name: MailReciveJob.java </p>
 * @author achui
 *
 */
public class MailReciveJob implements Job{
	private Logger logger = Log4WebUtil.getLogger(MailReciveJob.class);
	/* (non-Javadoc)
	 * @see org.quartz.Job#exe cute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext ctx) throws JobExecutionException, NumberFormatException {
		
		// TODO 接收邮件操作
		JobDetail jobDetial = ctx.getJobDetail() ;
		JobDataMap map = jobDetial.getJobDataMap();
		CotEmps emps = (CotEmps)map.get("emp");
		System.out.println("empId:"+emps.getId());
		//TODO:需要修改
		//if(emps.getId() != 22) return;
		try {
			recvAsign(emps.getId());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		System.out.println("加入个人邮件缓存队列");
		System.out.println(new Date(System.currentTimeMillis())+":任务"+jobDetial.getName()+"正在接收邮件.....操作");
	}
	public void recvAsign(Integer empId) throws NumberFormatException, DAOException {
		logger.debug("执行员工任务指派方法");
		List<CotMail> list = this.getMailLocalService().saveMailByEmp(empId);
		if(list == null || list.size() == 0) return;
		CotMailEmpsRule cotMailEmpsRule =this.getMailRuleService().getDefaultCotMailEmpsRule(empId);
		if(cotMailEmpsRule == null) return;
		String empsRuleFile;
		if(cotMailEmpsRule.getRuleDefault()==1){//1表示执行个人规则
			 empsRuleFile = MailReciveJob.class.getResource("/").getPath()+"mailrules/Rule_EMPID_"+empId+".xml";
		}else{
			//0表示执行公共规则
			empsRuleFile = MailReciveJob.class.getResource("/").getPath()+"Rule_PUBLIC_.xml";
		}
		File ruleFile = new File(empsRuleFile);
		if(!ruleFile.exists()) return;//不存在邮件规则，返回
		for (CotMail cotMail : list) {
			System.out.println("接收到邮件："+cotMail.getSubject());
			List<MailExecuteAction> actionList = this.getMailRuleService().getRuleResult(cotMail, empsRuleFile);
			for (MailExecuteAction mailExecuteAction : actionList) {
				System.out.println("员工ID："+mailExecuteAction.getEmpId());
				System.out.println("MoveTo："+mailExecuteAction.getMoveTo());
				//执行移动动作
				mailExecuteAction.moveToNode();
				//执行客户归档动作
				mailExecuteAction.archiveCust();
				//执行厂家归档动作
				mailExecuteAction.archiveFac();
			}
		}
		Cache cache4MailMsg = ContextUtil.getCache4MailMsg();
		String msg = "您有%1$d封新邮件";
		msg = String.format(msg, list.size());
		Element element = new Element(empId,msg);
		//加入缓存
		cache4MailMsg.put(element);
	}
	MailRuleService  mailRuleService;
	MailLocalService mailLocalService;
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
}
