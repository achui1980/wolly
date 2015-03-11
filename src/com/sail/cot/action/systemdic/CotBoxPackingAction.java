package com.sail.cot.action.systemdic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotBoxPackingService;
import com.sail.cot.util.SystemDicUtil;

public class CotBoxPackingAction extends AbstractAction {
	
	private CotBoxPackingService cotBoxPackingService;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	
	public CotBoxPackingService getCotBoxPackingService() {
		if(cotBoxPackingService==null){
			cotBoxPackingService = (CotBoxPackingService)super.getBean("CotBoxPackingService");
		}
		return cotBoxPackingService;
	}

	public void setCotBoxPackingService(CotBoxPackingService cotBoxPackingService) {
		this.cotBoxPackingService = cotBoxPackingService;
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
		
		String value = request.getParameter("valueFind");
		String valueEn = request.getParameter("valueEnFind");
		String type = request.getParameter("type");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(value!=null && !value.trim().equals("")){
			queryString.append(" and obj.value like '%"+value.trim()+"%'");
		}
		if(valueEn!=null && !valueEn.trim().equals("")){
			queryString.append(" and obj.valueEn like '%"+valueEn.trim()+"%'");
		}
		if (type != null && ! type.trim().equals("")) {
			queryString.append(" and obj.type ='"+type.trim()+"'");
		}
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotBoxPacking obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotBoxPacking obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getCotBoxPackingService().getJsonData(queryInfo);
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
	
	public ActionForward openCalculator(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("openCalculator");
	}
	public ActionForward addCalculator(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("addCalculator");
	}
}
