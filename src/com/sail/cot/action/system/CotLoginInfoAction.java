package com.sail.cot.action.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotLoginInfoService;
 
 

public class CotLoginInfoAction extends AbstractAction {

	private CotLoginInfoService cotLoginInfoService;
	public CotLoginInfoService getCotLoginInfoService()
	{
		if(cotLoginInfoService==null)
		{
			cotLoginInfoService=(CotLoginInfoService) super.getBean("CotLoginInfoService");
		}
		return cotLoginInfoService;
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
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(startTime!=null && endTime!=null){
			if(!"".equals(startTime.trim()) && "".equals(endTime.trim())){
				queryString.append(" and obj.loginTime >'"+startTime+"'");
			}
			if(!"".equals(endTime.trim()) && "".equals(startTime.trim())){
				queryString.append(" and obj.loginTime <'"+endTime+"'");
			}
			if(!"".equals(startTime.trim()) && !"".equals(endTime.trim())){
				queryString.append(" and obj.loginTime between '"+startTime+"' and '"+endTime+"'");
			}
		}
		
		QueryInfo queryInfo = new QueryInfo();
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(10);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotLoginInfo obj "+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotLoginInfo obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		request.setAttribute("allEmpsName", this.getCotLoginInfoService().getEmpsNameMap());
	    return mapping.findForward("querySuccess");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
}
