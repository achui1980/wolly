package com.sail.cot.action.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.mail.service.MailCfgService;

public class CotMailCfgAction extends AbstractAction {

	private MailCfgService mailCfgService;

	public MailCfgService getMailCfgService() {
		if (mailCfgService == null) {
			mailCfgService = (MailCfgService) super
					.getBean("MailCfgService");
		}
		return mailCfgService;
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
		return mapping.findForward("queryDetail");
	}
	public ActionForward empQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emps = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("isEmpAction", true);
		request.setAttribute("empId", emps.getId());
		return mapping.findForward("queryDetail");
	}
	

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
}
