package com.sail.cot.action.mail;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.vo.MailSysServiceVo;
import com.sail.cot.mail.sysservice.MailRecvPublicSchedule;
import com.sail.cot.mail.sysservice.MailRecvSchedule;
import com.sail.cot.util.GridServerHandler;



public class CotMailControlAction extends AbstractAction{

	private MailRecvPublicSchedule mailRecvPublicSchedule;
	public MailRecvPublicSchedule getMailRecvPublicSchedule() {
		if(mailRecvPublicSchedule==null)
		{
			mailRecvPublicSchedule = (MailRecvPublicSchedule) super.getBean("MailRecvPublicSchedule");
			
		}
		return mailRecvPublicSchedule;
	}

	public void setMailRecvPublicSchedule(
			MailRecvPublicSchedule mailRecvPublicSchedule) {
		this.mailRecvPublicSchedule = mailRecvPublicSchedule;
	}
	
	private MailRecvSchedule mailRecvSchedule;
	private MailRecvSchedule getMailRecvSchedule() {
		// TODO Auto-generated method stub
		if(mailRecvSchedule==null)
		{
			mailRecvSchedule = (MailRecvSchedule) super.getBean("MailRecvSchedule");
			
		}
		return mailRecvSchedule;
	}
 
	public void setMailRecvSchedule(MailRecvSchedule mailRecvSchedule) {
		this.mailRecvSchedule = mailRecvSchedule;
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
		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("querySuccess");
	}
	public ActionForward queryJobs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List<MailSysServiceVo> list = null;
		int count = 0;
		String json;
		String flag =request.getParameter("flag");
	    if(flag==null){
	    	//this.getMailRecvSchedule().startSchedule();
			list =this.getMailRecvSchedule().getAllJobs();		
			count =list.size();
	    }
		if("public".equals(flag)){
			//this.getMailRecvPublicSchedule().startSchedule();
			list =this.getMailRecvPublicSchedule().getAllJobs();		
			count =list.size();
		}
		json =this.toJSONString(list, count);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
		//return mapping.findForward("querySuccess");
	}
    @Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	//重启邮箱服务
	public ActionForward restartAllJobs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		int count=0;
		List<MailSysServiceVo> list=null;
		String json;
		String flag =request.getParameter("flag");
		if(flag==null){
			this.getMailRecvSchedule().restartSchedule();
			list =this.getMailRecvSchedule().getAllJobs();
			count =list.size();
		}
		
		if("public".equals(flag)){
			this.getMailRecvPublicSchedule().restartSchedule();
			list =this.getMailRecvPublicSchedule().getAllJobs();	
			count =list.size();
		}
		json =this.toJSONString(list, count);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//停止指定名称服务
	public ActionForward restartJobByName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<MailSysServiceVo> list = null;
		int count = 0;
		String json;
		String flag=request.getParameter("flag");
		if("public".equals(flag)){
			this.getMailRecvPublicSchedule().restartSchedule();
			list =this.getMailRecvPublicSchedule().getAllJobs();
			count =list.size();
		}
		
		if(flag==null){
			String jobName = request.getParameter("jobName");
			int empId =Integer.parseInt(request.getParameter("empId"));
			this.getMailRecvSchedule().restartJobByName(jobName, empId);
			list =this.getMailRecvSchedule().getAllJobs();
			count =list.size();
		}	
		json =this.toJSONString(list, count);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
    //启动邮箱全部服务
	public ActionForward startAllJobs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){	
		List<MailSysServiceVo> list = null;
		int count = 0;
		String json;
		String flag =request.getParameter("flag");
	    if(flag==null){
	    	this.getMailRecvSchedule().startSchedule();
			list =this.getMailRecvSchedule().getAllJobs();		
			count =list.size();
	    }
		if("public".equals(flag)){
			this.getMailRecvPublicSchedule().startSchedule();
			list =this.getMailRecvPublicSchedule().getAllJobs();		
			count =list.size();
		}
		json =this.toJSONString(list, count);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	//重启公共邮箱全部服务
//	public ActionForward restartAllPublicJobs(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response){
//		this.getMailRecvPublicSchedule().restartSchedule();
//		List<MailSysServiceVo> list =this.getMailRecvPublicSchedule().getAllJobs();	
//		int count =list.size();
//		try {
//			GridServerHandler grid = new GridServerHandler();
//			grid.setTotalCount(count);
//			grid.setData(list);
//			String json = grid.getLoadResponseText();
//			response.getWriter().write(json);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public String toJSONString(List<MailSysServiceVo> list,int count){
		String json = null;
		try {
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			 json= grid.getLoadResponseText();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	

	

}
