/**
 * 
 */
package com.sail.cot.mail;

import java.util.List;

import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotMail;
import com.sail.cot.mail.service.MailService;
import com.sail.cot.util.ContextUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 16, 2009 5:40:21 PM </p>
 * <p>Class Name: SendMailRunable.java </p>
 * @author achui
 *
 */
public class SendMailRunable implements Runnable{

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private CotMail mail;
	private List<CotMail> mailList;
	private String flag;
	private Boolean success;
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public SendMailRunable(CotMail mailObj)
	{
		this.mail = mailObj;
	}
	public SendMailRunable(CotMail mailObj,List<CotMail> mailList)
	{
		this.mail = mailObj;
		this.mailList = mailList;
	}
	private MailService mailService;
	
	public MailService getMailService() {
		if(mailService == null)
			mailService = (MailService) ContextUtil.getBean("mailService");
		return mailService;
	}

	public void run() {
		// TODO Auto-generated method stub
		if(flag.equals("add")) //新增发送
			getMailService().addAndSendMail(this.mail);
		else if(flag.equals("update"))
			getMailService().updateAndSendMail(this.mail);
		else if(flag.equals("group"))
			getMailService().addSendMail(mailList);
		
	}

}
