package com.sail.cot.action.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotCompanyService;
import com.sail.cot.util.SystemUtil;

public class CotCompanyAction extends AbstractAction {

	private CotCompanyService cotCompanyService;

	public CotCompanyService getCotCompanyService() {
		if (cotCompanyService == null) {
			cotCompanyService = (CotCompanyService) super
					.getBean("CotCompanyService");
		}
		return cotCompanyService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
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
		
		String companyShortName = request.getParameter("companyShortName");
	
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		
		if(companyShortName!=null && !companyShortName.trim().equals("")){
			queryString.append(" and obj.companyShortName like '%"+companyShortName.trim()+"%'");
		}
		
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"companyLogo"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotCompany obj"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("from CotCompany obj"); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotCompanyService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}

	 
}
