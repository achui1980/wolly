package com.sail.cot.action.mail;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.vo.CotAddressVO;
import com.sail.cot.domain.vo.CotAutoCompleteVO;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.system.CotEmpsService;
import com.sail.cot.service.system.CotFactoryService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotMailTemplateAction extends AbstractAction{
	private Logger logger = Log4WebUtil.getLogger(CotMailAction.class);
	private CotFactoryService cotFactoryService;
	private CotCustomerService cotCustomerService;
	private CotEmpsService cotEmpsService;
	public CotEmpsService getCotEmpsService(){
		if(cotEmpsService==null){
			cotEmpsService=(CotEmpsService) super.getBean("CotEmpsService");
		}
		return cotEmpsService;
	}
	public CotFactoryService getCotFactoryService(){
		if(cotFactoryService==null){
			cotFactoryService=(CotFactoryService) super.getBean("CotFactoryService");
		}
		return cotFactoryService;
	}
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao(){
		if(cotBaseDao == null){
			cotBaseDao = (CotBaseDao)super.getBean("CotBaseDao");
		}
		return cotBaseDao;
	}
	public CotCustomerService getCotCustomerService(){
		if(cotCustomerService==null){
			cotCustomerService=(CotCustomerService) super.getBean("CotCustomerService");
		}
		return cotCustomerService;
	}
	private MailLocalService mailLocalService;
	public MailLocalService getMailLocalService(){
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)super.getBean("MailLocalService");
		}
		return mailLocalService;
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
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("empsId", emp.getId());
		return mapping.findForward("querySuccess");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
