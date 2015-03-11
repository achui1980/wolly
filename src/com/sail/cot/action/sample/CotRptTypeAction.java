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
import com.sail.cot.service.sample.CotRptTypeService;
import com.sail.cot.util.SystemUtil;

public class CotRptTypeAction extends AbstractAction {

	private CotRptTypeService cotRptTypeService;
	
	public CotRptTypeService getCotRptTypeService() {
		if (cotRptTypeService==null)
		{
			cotRptTypeService = (CotRptTypeService) super.getBean("CotRptTypeService");
		}
		return cotRptTypeService;
	}

	public void setCotRptTypeService(CotRptTypeService cotRptTypeService) {
		this.cotRptTypeService = cotRptTypeService;
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
//		Limit limit=RequestUtils.getLimit(request,"rpttypeTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		//Map<?, ?> filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//		
//		QueryInfo queryInfo = new QueryInfo();
//		 
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("rpttypeTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		//设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotRptType obj where 1=1");
//		//设置查询记录语句
//		queryInfo.setSelectString("from CotRptType obj ");
//		//设置条件语句
//		queryInfo.setQueryString("");
//		//设置排序语句
//		queryInfo.setOrderString("");
//		 
//		int totalCount = this.getCotRptTypeService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		List<?> list = this.getCotRptTypeService().getList(queryInfo);
//		request.setAttribute("cotRptType", list);
		return mapping.findForward("querySuccess");
		
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}

}
