/**
 * 
 */
package com.sail.cot.action.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotEmpsService;
import com.sail.cot.service.system.CotPopedomService;
import com.sail.cot.util.SystemUtil;

/**
 * 员工管理
 * @author qh-chzy
 *
 */
public class CotEmpsAction extends AbstractAction {

	private CotEmpsService cotEmpsService;

	public CotEmpsService getCotEmpsService() {
		if (cotEmpsService == null) {
			cotEmpsService = (CotEmpsService) super.getBean("CotEmpsService");
		}
		return cotEmpsService;
	}
	
	private CotPopedomService cotPopedomService;

	public CotPopedomService getCotPopedomService() {
		if (cotPopedomService == null) {
			cotPopedomService = (CotPopedomService) super
					.getBean("CotPopedomService");
		}
		return cotPopedomService;
	}
	
	//跳转到员工添加页面
	public ActionForward addEmps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addEmps");
	}
	
	public ActionForward queryEmps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryEmpsSuccess");
	}
	
	public ActionForward queryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String empsName = request.getParameter("empsNameFind");
		String empsId = request.getParameter("empsIdFind");
		String deptId = request.getParameter("deptId");
		String empsStatus = request.getParameter("empsStatusFind");
		String companyId = request.getParameter("companyId");
		String shenFlag = request.getParameter("shenFlag");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryListSuccess");
		
		StringBuffer queryString = new StringBuffer();
		//不显示admin用户
		queryString.append(" where obj.empsId!='admin'");
		
		if(empsName!=null && !empsName.trim().equals("")){
			queryString.append(" and obj.empsName like '%"+empsName.trim()+"%'");
		}
		if(empsId!=null && !empsId.trim().equals("")){
			queryString.append(" and obj.empsId like '%"+empsId.trim()+"%'");
		}
		if(deptId!=null && !deptId.equals("")){
			queryString.append(" and obj.deptId="+deptId);
		}
		if(empsStatus!=null && !empsStatus.equals("")){
			if(empsStatus.equals("-1")){
				empsStatus="0";
			}
			queryString.append(" and obj.empsStatus="+empsStatus);
		}
		if(companyId!=null && !companyId.equals("")){
			queryString.append(" and obj.companyId="+companyId);
		}
		if(shenFlag!=null && !shenFlag.equals("") && !shenFlag.equals("0")){
			queryString.append(" and obj.shenFlag="+shenFlag);
		}
		
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"customers"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotEmps obj"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("from CotEmps obj"); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotEmpsService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.action.AbstractAction#remove(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	public ActionForward loginAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse respons)
	{
		System.out.println(request.getParameter("username"));
		HttpSession session = null;
		session = request.getSession();
		//获取员工工号
		String empId = request.getParameter("username");
		if(empId != null && !empId.trim().equals(""))
		{
			CotEmps emp = this.getCotEmpsService().getEmpsByEmpId(empId);
			session.setAttribute("emp", emp);
			session.setAttribute("empId",emp.getId());
			
			Map popedomMap = this.getCotPopedomService().getPopedomByEmp(emp.getId());
			session.setAttribute("popedomMap",popedomMap);
			
		}
		
		return mapping.findForward("loginSuccess");
	}
	public ActionForward modPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse respons)
	{
		return mapping.findForward("pwdSuccess");
	}
}
