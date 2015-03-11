package com.sail.cot.action.systemdic;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotCustomerLvService;
import com.sail.cot.util.SystemUtil;

public class CotCustomerLvAction extends AbstractAction {

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

	private CotCustomerLvService cotCustomerLvService;
	private CotCustomerLvService getCotCustomerLvService() {
		// TODO Auto-generated method stub
		if(cotCustomerLvService==null)
		{
			cotCustomerLvService = (CotCustomerLvService) super.getBean("CotCustomerLvService");
		}
		return cotCustomerLvService;
	}
 
	public void setCotCustomerLvService(CotCustomerLvService cotCustomerLvService) {
		this.cotCustomerLvService = cotCustomerLvService;
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
		
		StringBuffer queryString = new StringBuffer();
		//不显示admin用户
		queryString.append(" where 1=1");
		
		
		
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotCustomerLv obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotCustomerLv obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		try {
			String json = this.getCotCustomerLvService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
