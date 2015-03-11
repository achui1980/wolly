package com.sail.cot.action.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotPriceFacService;

public class CotPriceFacAction extends AbstractAction {

	private CotPriceFacService cotPriceFacService;
	
	public CotPriceFacService getCotPriceFacService() {
		if(cotPriceFacService==null)
		{
			cotPriceFacService=(CotPriceFacService)super.getBean("CotPriceFacService");
		}
		return cotPriceFacService;
	}

	public void setCotPriceFacService(CotPriceFacService cotPriceFacService) {
		this.cotPriceFacService = cotPriceFacService;
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
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryList");
		String eleId = request.getParameter("mainId");
		String facId = request.getParameter("facId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		StringBuffer queryString=new StringBuffer();
		queryString.append(" where 1=1");
		
		if (eleId != null && !eleId.toString().equals("")) {
			queryString.append(" and obj.eleId="+ eleId.toString());
		}
		if (facId != null && !facId.toString().equals("")) {
			queryString.append(" and obj.facId="+ facId.toString());
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.addTime >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.addTime <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.addTime between '"+startTime+"' and '"+endTime+"'");
		}
		
		//设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		//设置每页显示多少行
		int pageCount =15;
		//取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPriceFac obj"+ queryString);
		// 查询语句
		queryInfo.setSelectString("select obj from CotPriceFac obj");
		
		 
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		//得到limit对象
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		try {
			String json = this.getCotPriceFacService().getJsonData(queryInfo);
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
