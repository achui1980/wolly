package com.sail.cot.action.systemdic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTrailCarService;

public class CotTrailCarAction extends AbstractAction {

	private CotTrailCarService cotTrailCarService;
	
	public CotTrailCarService getCotTrailCarService() {
		if(cotTrailCarService==null){
			cotTrailCarService = (CotTrailCarService)super.getBean("CotTrailCarService");
		}
		return cotTrailCarService;
	}

	public void setCotTrailCarService(CotTrailCarService cotTrailCarService) {
		this.cotTrailCarService = cotTrailCarService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotTrailCar as obj" );
		// 查询语句
		queryInfo.setSelectString("select obj from CotTrailCar AS obj where 1=1");
		// 设置条件语句
		queryInfo.setQueryString("");
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		// 得到limit对象
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotTrailCarService().getJsonData(queryInfo);
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
