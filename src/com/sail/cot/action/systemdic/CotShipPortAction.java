/**
 * 
 */
package com.sail.cot.action.systemdic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotShipPortService;

/**
 * 起运港管理
 * @author qh-chzy
 *
 */
public class CotShipPortAction extends AbstractAction {

	private CotShipPortService cotShipPortService;

	public CotShipPortService getCotShipPortService() {
		if (cotShipPortService == null) {
			cotShipPortService = (CotShipPortService) super.getBean("CotShipPortService");
		}
		return cotShipPortService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//跳转到添加页面
	public ActionForward addShipPort(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("add");
	}
	
	public ActionForward queryShipPort(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("modify");
	}
	
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String shipPortNameFind = request.getParameter("shipPortName");
		String shipPortNameEnFind = request.getParameter("shipPortNameEn");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(shipPortNameFind!=null && !shipPortNameFind.trim().equals("")){
			queryString.append(" and obj.shipPortName like '%"+shipPortNameFind.trim()+"%'");
		}
		if(shipPortNameEnFind!=null && !shipPortNameEnFind.trim().equals("")){
			queryString.append(" and obj.shipPortNameEn like '%"+shipPortNameEnFind.trim()+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotShipPort obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotShipPort obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getCotShipPortService().getJsonData(queryInfo);
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
