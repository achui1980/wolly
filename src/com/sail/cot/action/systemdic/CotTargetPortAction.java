package com.sail.cot.action.systemdic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTargetPortService;

public class CotTargetPortAction extends AbstractAction {

	private CotTargetPortService cotTargetPortService;
	
	public CotTargetPortService getCotTargetPortService() {
		if (cotTargetPortService==null) {
			cotTargetPortService = (CotTargetPortService) super.getBean("CotTargetPortService");
		}
		return cotTargetPortService;
	}

	public void setCotTargetPortService(CotTargetPortService cotTargetPortService) {
		this.cotTargetPortService = cotTargetPortService;
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
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String targetPortName = request.getParameter("targetPortName");
		String targetPortEnName = request.getParameter("targetPortEnName");
		String targetPortNation=request.getParameter("targetPortNation");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(targetPortName!=null && !targetPortName.trim().equals("")){
			queryString.append(" and obj.targetPortName like '%"+targetPortName.trim()+"%'");
		}
		if(targetPortEnName!=null && !targetPortEnName.trim().equals("")){
			queryString.append(" and obj.targetPortEnName like '%"+targetPortEnName.trim()+"%'");
		}
		if(targetPortNation!=null && !targetPortNation.trim().equals("")){
			queryString.append(" and obj.targetPortNation like '%" +targetPortNation.trim()+"%'");
		}
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotTargetPort obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotTargetPort obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getCotTargetPortService().getJsonData(queryInfo);
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
		// TODO Auto-generated method stub
		return null;
	}

}
