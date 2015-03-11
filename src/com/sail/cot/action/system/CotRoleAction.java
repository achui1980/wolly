package com.sail.cot.action.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.jcreate.e3.tree.Node;
import net.jcreate.e3.tree.TreeDirector;
import net.jcreate.e3.tree.TreeModel;
import net.jcreate.e3.tree.UncodeException;
import net.jcreate.e3.tree.UserDataUncoder;
import net.jcreate.e3.tree.ext.ExtTreeBuilder;
import net.jcreate.e3.tree.support.AbstractWebTreeModelCreator;
import net.jcreate.e3.tree.support.DefaultTreeDirector;
import net.jcreate.e3.tree.support.WebTreeNode;
import net.jcreate.e3.tree.xtree.XTreeBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotRole;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotRoleService;
import com.sail.cot.util.SystemUtil;
/**
 * <p>
 * Title: 工艺品管理系统
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Aug 1, 2008 3:02:54 PM
 * </p>
 * <p>
 * Class Name: CotRoleAction.java
 * </p>
 * 
 * @author qh-chchh
 * 
 */

public class CotRoleAction extends AbstractAction{

	private CotRoleService cotRoleService;
	
	public CotRoleService getCotRoleService() {
		if (cotRoleService == null) {
			cotRoleService = (CotRoleService) super
					.getBean("CotRoleService");
		}
		return cotRoleService;
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
		return null;
	}

	@Override
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
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotRole obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotRole obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);		
		queryInfo.setStartIndex(startIndex);
		try {
			String json = getCotRoleService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
//	public ActionForward queryList(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		Limit limit=RequestUtils.getLimit(request,"roleTable");
//		Map sortValueMap = limit.getSort().getSortValueMap();
//		Map filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//		StringBuffer queryString = new StringBuffer();
//		//不显示admin用户
//		queryString.append(" where 1=1");
//
//		QueryInfo queryInfo = new QueryInfo();
//		NavRequest navRequest = HTMLTableHelper.getNavRequest("roleTable", request);
//		queryInfo.setNavRequest(navRequest);
//		
//		// 设置每页显示多少行
//		int pageCount = 5;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("roleTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		//设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotRole obj"+queryString);
//		//设置查询记录语句
//		queryInfo.setSelectString("from CotRole obj");
//		//设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		//设置排序语句
//		queryInfo.setOrderString("");
//		DataModel dataModel = this.getCotRoleService().findListByPage(queryInfo);
//		int totalCount = this.getCotRoleService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 3);
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		
//		request.setAttribute("cotRole", dataModel);
//
//		Map map = new HashMap();
//		map.put("0", "禁用");
//		map.put("1", "启用");
//		request.setAttribute("statusMapping", map);
//		request.setAttribute("cotRole", this.getCotRoleService().getList(queryInfo));
//		return mapping.findForward("querySuccess");
//	}

	public ActionForward queryRoleById(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("querySuc");
	}
}
