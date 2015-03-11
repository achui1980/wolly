/**
 * 
 */
package com.sail.cot.action.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.sail.cot.action.AbstractAction;

import com.sail.cot.service.system.CotEmpsService;

import com.sail.cot.service.system.CotPopedomService;

/**
 * 员工管理
 * @author qh-chzy
 *
 */
public class CotPwdAction extends AbstractAction {

	private CotEmpsService cotEmpsService;

	public CotEmpsService getCotEmpsService() {
		if (cotEmpsService == null) {
			cotEmpsService = (CotEmpsService) super.getBean("CotEmpsService");
		}
		return cotEmpsService;
	}
	
	private CotPopedomService cotPopedomService;

	public CotPopedomService getCotPopedomService() {
		if (cotPopedomService == null) {
			cotPopedomService = (CotPopedomService) super
					.getBean("CotPopedomService");
		}
		return cotPopedomService;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.action.AbstractAction#remove(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActionForward modPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse respons)
	{
		return mapping.findForward("pwdSuccess");
	}
}
