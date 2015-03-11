package com.sail.cot.action.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotPriceOutService;

public class CotPriceOutAction extends AbstractAction {
  
	private CotPriceOutService cotPriceOutService;
	
	public CotPriceOutService getCotPriceOutService() {
		if(cotPriceOutService==null)
		{
			cotPriceOutService=(CotPriceOutService)super.getBean("CotPriceOutService");
		}
		return cotPriceOutService;
	}

	public void setCotPriceOutService(CotPriceOutService cotPriceOutService) {
		this.cotPriceOutService = cotPriceOutService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		String eleId=request.getParameter("eleId");
		request.setAttribute("eleId", eleId);
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
	    return mapping.findForward("queryList");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}

}
