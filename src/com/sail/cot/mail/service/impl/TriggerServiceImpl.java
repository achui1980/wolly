/**
 * 
 */
package com.sail.cot.mail.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.sail.cot.domain.CotMail;
import com.sail.cot.mail.service.MailService;
import com.sail.cot.mail.service.TriggerService;
import com.sail.cot.util.Log4WebUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 7, 2008 2:54:41 PM </p>
 * <p>Class Name: TriggerServiceImpl.java </p>
 * @author achui
 *
 */
public class TriggerServiceImpl implements TriggerService,InitializingBean {

	private MailService mailService;
	private Logger logger = Log4WebUtil.getLogger(TriggerServiceImpl.class);
	public MailService getMailService() {
		return mailService;
	}
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.mail.service.TriggerService#doSendMail()
	 */
	public void doSendMail() {
		logger.info("执行doSendMail()方法");
		this.getMailService().sendMail(true);

	}
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	protected void SetMailListStatus()
	{
		
	}
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		 
		System.out.println("---begin update");
		List<CotMail> mailList = (List<CotMail>) this.getMailService().getMailListByStatus(4); //获取所有未发送邮件
		this.getMailService().updateMailList(mailList, 0);                         //更新为待发送状态
	}
}
