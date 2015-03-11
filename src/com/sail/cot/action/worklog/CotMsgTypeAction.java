package com.sail.cot.action.worklog;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.worklog.CotMsgTypeService;
import com.sail.cot.util.SystemUtil;


public class CotMsgTypeAction extends AbstractAction{

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

	private CotMsgTypeService cotMsgTypeService;
	private CotMsgTypeService getCotMsgTypeService() {
		// TODO Auto-generated method stub
		if(cotMsgTypeService==null)
		{
			cotMsgTypeService = (CotMsgTypeService) super.getBean("CotMsgTypeService");
		}
		return cotMsgTypeService;
	}
 
	public void setCotMsgTypeService(CotMsgTypeService cotMsgTypeService) {
		this.cotMsgTypeService = cotMsgTypeService;
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
		queryInfo.setCountQuery("select count(*) from CotMsgType obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotMsgType obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		
		queryInfo.setStartIndex(startIndex);
		
		String json;
		try {
			json = this.getCotMsgTypeService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
