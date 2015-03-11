package com.sail.cot.action.customer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustContactService;

public class CotCustContactAction extends AbstractAction {

	private CotCustContactService cotCustContactService;
	public CotCustContactService getCotCustContactService() {
		if(cotCustContactService==null)
		{
			cotCustContactService=(CotCustContactService)super.getBean("CotCustContactService");
		}
		return cotCustContactService;
	}
    public void setCotCustContactService(CotCustContactService cotCustContactService) {
		this.cotCustContactService = cotCustContactService;
	}
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String custId=request.getParameter("custId");
		request.setAttribute("custId", custId);
		try {
			String name =request.getParameter("name");
			String emailUrl =request.getParameter("emailUrl");
			if(name==null){
				name="";
			}
			name = URLDecoder.decode(name,"utf-8");
			request.setAttribute("name", name);
			if(emailUrl==null){
				emailUrl="";
			}
			request.setAttribute("emailUrl", emailUrl);
			
		} catch (Exception e) {
		}
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String custId=request.getParameter("custId");
		request.setAttribute("custId", custId);
		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer sql=new StringBuffer();
		String customerId=request.getParameter("cusid");
		String custId=request.getParameter("custId");
		String custIdFind=request.getParameter("custIdFind");
		String contactPerson=request.getParameter("contactName");
		String emailId=request.getParameter("emailId");
		
		
		if(null!=customerId && !"".equals(customerId))
		{
			sql.append("  and obj.customerId ="+customerId);
		}
		if(null!=custId && !"".equals(custId))
		{
			sql.append("  and obj.customerId ="+custId);
		}
		if(null!=custIdFind && !"".equals(custIdFind))
		{
			sql.append("  and obj.customerId ="+custIdFind);
		}
		if(null!=contactPerson && !"".equals(contactPerson))
		{
			contactPerson = contactPerson.trim();
			sql.append(" and obj.contactPerson like '%"+contactPerson+"%'");
		}
		if(null!=emailId && !"".equals(emailId)){
			sql.append(" and obj.id ="+Integer.parseInt(emailId));
		}
		
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		queryInfo.setCountQuery("select count(*) from CotCustContact obj where 1=1"+sql.toString());
		queryInfo.setSelectString("from CotCustContact obj where 1=1");
		queryInfo.setQueryString(sql.toString());
		queryInfo.setOrderString("");
		
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		
		try {
			String json = this.getCotCustContactService().getJsonData(queryInfo);
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
