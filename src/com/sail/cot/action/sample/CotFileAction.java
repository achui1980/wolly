package com.sail.cot.action.sample;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotFileService;
import com.sail.cot.util.SystemUtil;

public class CotFileAction extends AbstractAction {

	private CotFileService cotFileService;
	
	public CotFileService getCotFileService() {
		if(cotFileService==null)
		{
			cotFileService=(CotFileService)super.getBean("CotFileService");
		}
		return cotFileService;
	}

	public void setCotFileService(CotFileService cotFileService) {
		this.cotFileService = cotFileService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
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
		 
		StringBuffer sql=new StringBuffer();
		
		String eleId=request.getParameter("eleId");
		
		if(null!=eleId && !"".equals(eleId))
		{
			eleId=eleId.trim();
			sql.append(" and obj.eleId="+eleId);
		}
		
		QueryInfo queryInfo = new QueryInfo();
		
		String [] filter = {"fileContent"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFile obj where 1=1"+sql.toString());
		//设置查询记录语句
		queryInfo.setSelectString("from CotFile obj where 1=1"); 
		//设置查询条件语句
		queryInfo.setQueryString(sql.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotFileService().getJsonData(queryInfo);
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

	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("queryFileSuccess");
	}
	
	public ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("uploadFile");
	}
}
