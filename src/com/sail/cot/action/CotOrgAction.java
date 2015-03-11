/**
 * 
 */
package com.sail.cot.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.CotOrgService;

public class CotOrgAction extends AbstractAction {

	private CotOrgService orgService;

	public CotOrgService getOrgService() {
		if (orgService == null) {
			orgService = (CotOrgService) super.getBean("CotOrgService");
		}
		return orgService;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;
		
		String startTime = request.getParameter("startTimeOrg");
		String endTime = request.getParameter("endTimeOrg");
		String filePath = request.getParameter("filePathFind");
		String remark = request.getParameter("remarkFind");
		String empId = request.getParameter("empIdFind");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (filePath != null && !filePath.trim().equals("")) {
			queryString.append(" and obj.filePath like '%" + filePath.trim()
					+ "%'");
		}
		if (remark != null && !remark.trim().equals("")) {
			queryString.append(" and obj.remark like '%" + remark.trim()
					+ "%'");
		}
		if (empId != null && !empId.trim().equals("")) {
			queryString.append(" and obj.empId=" + empId);
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.uploadTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.uploadTime <='" + endTime + " 23:59:59'");
		}

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		queryInfo
				.setCountQuery("select count(*) from CotOrg obj"
						+ queryString.toString());
		queryInfo
				.setSelectString(" from CotOrg obj");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrgService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public ActionForward orgEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("orgEdit");
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

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
