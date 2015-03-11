package com.sail.cot.action.system;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.service.QueryService;

public class CombinationAction extends AbstractAction {

	
	private QueryService queryService;
	
	public QueryService getQueryService()
	{
		if(queryService==null)
		{
			queryService=(QueryService) super.getBean("QueryService");
		}
		return queryService;
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
		return mapping.findForward("combinationPage");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ActionForward combinationFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String srcFacId = request.getParameter("srcFacId");
		String desFacId = request.getParameter("desFacId");
		
		boolean flag = this.getQueryService().updateFacId(Integer.parseInt(srcFacId), Integer.parseInt(desFacId));
		
		JSONObject error = new JSONObject();
		
		if (flag) {
			error.put("success", true);
			error.put("msg", "合并成功");
		}else {
			error.put("success", false);
			error.put("msg", "合并成功");
		}
		try {
			response.getWriter().write(error.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward combinationCust(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String srcCustId = request.getParameter("srcCustId");
		String desCustId = request.getParameter("desCustId");
		
		boolean flag = this.getQueryService().updateCustId(Integer.parseInt(srcCustId), Integer.parseInt(desCustId));
		JSONObject error = new JSONObject();
		if (flag) {
			error.put("success", true);
			error.put("msg", "合并成功");
		}else {
			error.put("success", false);
			error.put("msg", "合并成功");
		}
		try {
			response.getWriter().write(error.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
