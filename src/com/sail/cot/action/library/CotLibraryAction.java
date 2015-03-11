package com.sail.cot.action.library;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.library.CotLibraryService;
import com.sail.cot.util.SystemUtil;

public class CotLibraryAction extends AbstractAction {

	private CotLibraryService cotLibraryService;
	
	public CotLibraryService getCotLibraryService() {
		if(cotLibraryService==null)
		{
			cotLibraryService = (CotLibraryService) super.getBean("CotLibraryService");
		}
		return cotLibraryService;
	}

	public void setCotLibraryService(CotLibraryService cotLibraryService) {
		this.cotLibraryService = cotLibraryService;
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
//		Limit limit=RequestUtils.getLimit(request,"libraryTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		Map<?, ?> filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
		
//		String typeName=request.getParameter("tName");
		StringBuffer sql=new StringBuffer();
//		if(null!=typeName && !"".equals(typeName))
//		{
//			typeName = typeName.trim();
//			sql.append("  and obj.typeName like '%"+typeName+"%'");
//		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		String pc = request.getParameter("libraryTable_crd");
		if (pc != null) {
			pageCount = Integer.parseInt(pc);
		}
		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		queryInfo.setCountQuery("select count(*) from CotSaleInfo obj where 1=1"+sql.toString());
//		queryInfo.setSelectString("from CotSaleInfo obj where 1=1");
//		queryInfo.setQueryString(sql.toString());
//		queryInfo.setOrderString("");
//		 
//		int totalCount = this.getCotLibraryService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		List<?> list = this.getCotLibraryService().getList(queryInfo);
//		request.setAttribute("cotLibrary", list);
		return mapping.findForward("querySuccess");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}

}
