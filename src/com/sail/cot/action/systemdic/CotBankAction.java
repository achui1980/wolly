/**
 * 
 */
package com.sail.cot.action.systemdic;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotBankService;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 23, 2009 2:24:36 PM </p>
 * <p>Class Name: CotBankAction.java </p>
 * @author achui
 *
 */
public class CotBankAction extends AbstractAction{

	private CotBankService bankService;
	
	public CotBankService getBankService() {
		if(bankService == null)
			bankService = (CotBankService)super.getBean("CotBankService");
		return bankService;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#add(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("add");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#modify(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("modify");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#query(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String bankName = request.getParameter("bankName");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(bankName!=null && !bankName.trim().equals("")){
			queryString.append(" and obj.bankName like '%"+bankName.trim()+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotBank obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotBank obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getBankService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#remove(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
