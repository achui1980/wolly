package com.sail.cot.action.mail;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.quartz.SchedulerException;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.vo.MailSysServiceVo;
import com.sail.cot.mail.sysservice.MailRecvPublicSchedule;
import com.sail.cot.mail.sysservice.MailRecvSchedule;
import com.sail.cot.util.GridServerHandler;



public class CotMailImportAction extends AbstractAction{

	private MailRecvPublicSchedule mailRecvPublicSchedule;
	public MailRecvPublicSchedule getMailRecvPublicSchedule() {
		if(mailRecvPublicSchedule==null)
		{
			mailRecvPublicSchedule = (MailRecvPublicSchedule) super.getBean("MailRecvPublicSchedule");
			
		}
		return mailRecvPublicSchedule;
	}

	public void setMailRecvPublicSchedule(
			MailRecvPublicSchedule mailRecvPublicSchedule) {
		this.mailRecvPublicSchedule = mailRecvPublicSchedule;
	}
	
	private MailRecvSchedule mailRecvSchedule;
	private MailRecvSchedule getMailRecvSchedule() {
		// TODO Auto-generated method stub
		if(mailRecvSchedule==null)
		{
			mailRecvSchedule = (MailRecvSchedule) super.getBean("MailRecvSchedule");
			
		}
		return mailRecvSchedule;
	}
 
	public void setMailRecvSchedule(MailRecvSchedule mailRecvSchedule) {
		this.mailRecvSchedule = mailRecvSchedule;
	}
	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("querySuccess");
	}

    @Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}



}
