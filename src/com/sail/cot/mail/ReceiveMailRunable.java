/**
 * 
 */
package com.sail.cot.mail;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.mail.service.MailRecvService;
import com.sail.cot.util.ContextUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 16, 2009 6:26:35 PM </p>
 * <p>Class Name: ReceiveMailRunable.java </p>
 * @author achui
 *
 */
public class ReceiveMailRunable implements Runnable{

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private String flag;
	private MailRecvService mailService;
	private CotEmps cotEmps;
	public CotEmps getCotEmps() {
		return cotEmps;
	}
	public void setCotEmps(CotEmps cotEmps) {
		this.cotEmps = cotEmps;
	}
	public MailRecvService getMailService() {
		if(mailService == null)
			mailService = (MailRecvService) ContextUtil.getBean("recvMailService");
		return mailService;
	}
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.getMailService().saveReciveMail(null, null, null, null, this.flag,this.cotEmps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
