package com.sail.cot.action.systemdic;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotPriceCfgService;
import com.sail.cot.util.SystemUtil;

public class CotPriceCfgAction extends AbstractAction {

	private CotPriceCfgService cotPriceCfgService;
	
	public CotPriceCfgService getCotPriceCfgService() {
		if (cotPriceCfgService==null) {
			cotPriceCfgService = (CotPriceCfgService) super.getBean("CotPriceCfgService");
		}
		return cotPriceCfgService;
	}

	public void setCotPriceCfgService(CotPriceCfgService cotPriceCfgService) {
		this.cotPriceCfgService = cotPriceCfgService;
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
		return mapping.findForward("success");
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
