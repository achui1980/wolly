package com.sail.cot.action.systemdic;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotNationCityService;
import com.sail.cot.util.SystemUtil;

public class CotNationCityAction extends AbstractAction {

	private CotNationCityService cotNationCityService;
	
	public CotNationCityService getCotNationCityService() {
		if(cotNationCityService==null){
			cotNationCityService = (CotNationCityService) super.getBean("CotNationCityService");
		}
		return cotNationCityService;
	}

	public void setCotNationCityService(CotNationCityService cotNationCityService) {
		this.cotNationCityService = cotNationCityService;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotNationCity obj where 1=1");
		//设置查询记录语句
		queryInfo.setSelectString("from CotNationCity obj");
		//设置查询条件语句
		queryInfo.setQueryString("");
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		try {
			String json = this.getCotNationCityService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
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
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

}
