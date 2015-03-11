package com.sail.cot.action.system;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotContactService;

public class CotContactAction extends AbstractAction {

	private CotContactService cotContactService;
	public CotContactService getCotContactService() {
		if(cotContactService==null)
		{
			cotContactService=(CotContactService)super.getBean("CotContactService");
		}
		return cotContactService;
	}
    public void setCotContactService(CotContactService cotContactService) {
		this.cotContactService = cotContactService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String fId=request.getParameter("fId");
		request.setAttribute("fId", fId);
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String fId=request.getParameter("fId");
		request.setAttribute("fId", fId);
		try {
			String name = URLDecoder.decode(request.getParameter("name"),"utf-8");
			request.setAttribute("name", name);
		} catch (Exception e) {
		}
		return mapping.findForward("modify");
	}
	
	public ActionForward modifyWithPan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("modifyWithPan");
	}


	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String contactFind = request.getParameter("contactFind");
		String factoryId = request.getParameter("factoryId");
		
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer sql = new StringBuffer();
		sql.append(" where 1=1");
		
		if(null!=contactFind && !"".equals(contactFind) ){
			sql.append(" and obj.contactPerson like '%"+contactFind.trim()+"%'");
		}
		
		if(null!=factoryId && !"".equals(factoryId))
		{
			sql.append(" and obj.factoryId ="+factoryId);
		}
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotContact obj "+ sql.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("from CotContact obj ");
		// 设置条件语句
		queryInfo.setQueryString(sql.toString());
		// 设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotContactService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
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
