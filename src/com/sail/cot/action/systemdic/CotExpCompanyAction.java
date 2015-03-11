package com.sail.cot.action.systemdic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotExpCompanyService;

public class CotExpCompanyAction extends AbstractAction {

	private CotExpCompanyService cotExpCompanyService;
	private CotExpCompanyService getCotExpCompanyService() {
		// TODO Auto-generated method stub
		if(cotExpCompanyService==null)
		{
			cotExpCompanyService = (CotExpCompanyService) super.getBean("CotExpCompanyService");
		}
		return cotExpCompanyService;
	}
	
	public void setCotExpCompanyService(CotExpCompanyService cotExpCompanyService) {
		this.cotExpCompanyService = cotExpCompanyService;
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
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String expCompanyName = request.getParameter("expCompanyName");
		String expCompanyNameEn = request.getParameter("expCompanyNameEn");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(expCompanyName!=null && !expCompanyName.trim().equals("")){
			queryString.append(" and obj.expCompanyName like '%"+expCompanyName.trim()+"%'");
		}
		if(expCompanyNameEn!=null && !expCompanyNameEn.trim().equals("")){
			queryString.append(" and obj.expCompanyNameEn like '%"+expCompanyNameEn.trim()+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotExpCompany obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotExpCompany obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getCotExpCompanyService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}
