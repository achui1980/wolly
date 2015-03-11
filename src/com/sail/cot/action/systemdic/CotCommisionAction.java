package com.sail.cot.action.systemdic;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotCommisionService;
import com.sail.cot.util.SystemUtil;


public class CotCommisionAction extends AbstractAction{

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

	private CotCommisionService cotCommisionService;
	private CotCommisionService getCotCommisionService() {
		// TODO Auto-generated method stub
		if(cotCommisionService==null)
		{
			cotCommisionService = (CotCommisionService) super.getBean("CotCommisionService");
		}
		return cotCommisionService;
	}
 
	public void setCotCommisionService(CotCommisionService cotCommisionService) {
		this.cotCommisionService = cotCommisionService;
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
//		start = "0";
//			limit = "15";
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		
		String commisionNameFind = request.getParameter("commisionNameFind");
		StringBuffer queryString = new StringBuffer();
		//不显示admin用户
		queryString.append(" where 1=1");
		if(commisionNameFind!=null && !commisionNameFind.trim().equals("")){
			queryString.append(" and obj.commisionName like '%"+commisionNameFind.trim()+"%'");
		}
		
		
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotCommisionType obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotCommisionType obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getCotCommisionService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
