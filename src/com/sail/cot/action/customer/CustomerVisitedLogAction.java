package com.sail.cot.action.customer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CustomerVisitedLogService;
import com.sail.cot.util.SystemUtil;

public class CustomerVisitedLogAction extends AbstractAction {

	private CustomerVisitedLogService customerVisitedLogService;
	
	public CustomerVisitedLogService getCustomerVisitedLogService() {
		if(customerVisitedLogService == null){
			customerVisitedLogService = (CustomerVisitedLogService) super.getBean("CustomerVisitedLogService");
		}
		return customerVisitedLogService;
	}

	public void setCustomerVisitedLogService(
			CustomerVisitedLogService customerVisitedLogService) {
		this.customerVisitedLogService = customerVisitedLogService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String custId=request.getParameter("custId");
		request.setAttribute("custId", custId);
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1 = 1");
		
		String custId = request.getParameter("custId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		if (custId != null && !"".equals(custId.trim())) {
			queryString.append(" and obj.cotCustomerId =" + custId.trim());
		}else {
			return null;
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.visitTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.visitTime <='" + endTime
					+ " 23:59:59'");
		}

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		queryInfo.setCountQuery("select count(*) from CustomerVisitedLog obj "+ queryString.toString());
		queryInfo.setSelectString("from CustomerVisitedLog obj");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCustomerVisitedLogService().getJsonData(queryInfo);
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
