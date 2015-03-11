/**
 * 
 */
package com.sail.cot.action;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotLoginInfo;
import com.sail.cot.domain.CotServerInfo;
import com.sail.cot.service.QueryService;
import com.sail.cot.service.system.CotEmpsService;
import com.sail.cot.service.system.CotLoginInfoService;
import com.sail.cot.service.system.CotPopedomService;
import com.sail.cot.service.system.CotRegistService;
import com.sail.cot.util.SystemDicUtil;


/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 22, 2008 10:37:47 AM </p>
 * <p>Class Name: LoginAction.java </p>
 * @author achui
 *
 */
public class LoginAction extends AbstractAction{

	private CotEmpsService cotEmpsService;

	public CotEmpsService getCotEmpsService() {
		if (cotEmpsService == null) {
			cotEmpsService = (CotEmpsService) super.getBean("CotEmpsService");
		}
		return cotEmpsService;
	}
	
	private QueryService queryService;

	public QueryService getQueryService() {
		if (queryService == null) {
			queryService = (QueryService) super.getBean("QueryService");
		}
		return queryService;
	}
	
	private CotPopedomService cotPopedomService;

	public CotPopedomService getCotPopedomService() {
		if (cotPopedomService == null) {
			cotPopedomService = (CotPopedomService) super
					.getBean("CotPopedomService");
		}
		return cotPopedomService;
	}
	
	private CotLoginInfoService cotLoginInfoService;

	public CotLoginInfoService getCotLoginInfoService() {
		if (cotLoginInfoService == null) {
			cotLoginInfoService = (CotLoginInfoService) super.getBean("CotLoginInfoService");
		}
		return cotLoginInfoService;
	}
	
	private CotRegistService cotRegistService;

	public CotRegistService getCotRegistService() {
		if (cotRegistService == null) {
			cotRegistService = (CotRegistService) super.getBean("CotRegistService");
		}
		return cotRegistService;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#add(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#modify(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#query(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
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
	public ActionForward loginAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse respons)
	{
		HttpSession session = null;
		session = request.getSession();
		String sessionId =session.getId();
		ServletContext application = session.getServletContext();
		
		//获取员工工号
		String empId = request.getParameter("username");
		
		if(empId != null && !empId.trim().equals(""))
		{
			CotEmps emp = this.getCotEmpsService().getEmpsByEmpId(empId);
			emp.setCustomers(null);
			session.setAttribute("emp", emp);
			session.setAttribute("empId",emp.getId());
			session.setAttribute("empNo", empId);
			ConcurrentHashMap<String,Long> onLineMap = (ConcurrentHashMap<String,Long>)application.getAttribute("onLineMap");
			if(onLineMap == null)
				onLineMap = new ConcurrentHashMap<String,Long>();
			//存放当前登录员工的IP和当前登录时间，用于退出的判断
			onLineMap.put(session.getId(), System.currentTimeMillis());
			application.setAttribute("onLineMap", onLineMap);
			Map popedomMap = this.getCotPopedomService().getPopedomByEmp(emp.getId());
			session.setAttribute("popedomMap",popedomMap);
			CotLoginInfo cotLoginInfo = this.getCotLoginInfoService().findIsExistLoginInfo(session.getId());
			//往登录人数表中添加记录
			if(cotLoginInfo==null){
				cotLoginInfo = new CotLoginInfo();
				cotLoginInfo.setLoginEmpid(emp.getId().toString());
				cotLoginInfo.setLoginName(emp.getEmpsId());
				cotLoginInfo.setLoginIpaddr(request.getRemoteAddr());
				cotLoginInfo.setSessionId(sessionId);
			}
			cotLoginInfo.setLoginTime(new Date());
			this.getCotLoginInfoService().saveLoginInfo(cotLoginInfo);
			session.setAttribute("loginInfo", cotLoginInfo);
			//获取系统注册信息
			String mKey = this.getCotRegistService().getMechineKey();
			CotServerInfo cotServerInfo = this.getCotRegistService().getIdByMechineKey(mKey);
			session.setAttribute("CotServerInfo", cotServerInfo);
			//获取系统常用数据字典
			SystemDicUtil dicUtil = new SystemDicUtil(); 
			Map map = dicUtil.getSysDicMap();
			session.setAttribute("sysdic", map);
			
//			//获得20/40/40HQ/45的柜体积
//			Float[] cubes = this.getQueryService().getContainerCube();
//			session.setAttribute("cubes", cubes);
			//将登陆工号写入cookie，客户端可以读取
			Cookie cookie = new Cookie("username",empId);
			cookie.setMaxAge(60*60*24*365);//设置有效期1年
			respons.addCookie(cookie);		
		}
		
		return mapping.findForward("loginSuccess");
	}
	
	public ActionForward loginDemoAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse respons)
	{
		HttpSession session = null;
		session = request.getSession();
		//获取员工工号
		String empId = request.getParameter("username");
		if(empId != null && !empId.trim().equals(""))
		{
			CotEmps emp = this.getCotEmpsService().getEmpsByEmpId(empId);
			emp.setCustomers(null);
			session.setAttribute("emp", emp);
			session.setAttribute("empId",emp.getId());
			session.setAttribute("empNo", empId);
			
			Map popedomMap = this.getCotPopedomService().getPopedomByEmp(emp.getId());
			session.setAttribute("popedomMap",popedomMap);
			CotLoginInfo cotLoginInfo = this.getCotLoginInfoService().findIsExistLoginInfo(request.getRemoteAddr());
			//往登录人数表中添加记录
			if(cotLoginInfo==null){
				cotLoginInfo = new CotLoginInfo();
				cotLoginInfo.setLoginEmpid(emp.getId().toString());
				cotLoginInfo.setLoginName(emp.getEmpsId());
				cotLoginInfo.setLoginIpaddr(request.getRemoteAddr());
			}
			cotLoginInfo.setLoginTime(new Date(System.currentTimeMillis()));
			this.getCotLoginInfoService().saveLoginInfo(cotLoginInfo);
			
			
			//获取试用系统注册信息
			String mechineKey = "D3FC9B36";
			CotServerInfo cotServerInfo = this.getCotRegistService().getIdByMechineKey(mechineKey);
			session.setAttribute("CotServerInfo", cotServerInfo);
		}
		
		return mapping.findForward("loginSuccess");
	}
	
	public ActionForward logoutAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse respons)
	{
		HttpSession session = request.getSession();
		
		//删除登录人数表中的记录
//		this.getCotLoginInfoService().deleteLoginInfos(request.getRemoteAddr());
		this.getCotLoginInfoService().deleteLoginInfos(session.getId());
	
		session.removeAttribute("empId");
		session.removeAttribute("emp");
		session.removeAttribute("popedomMap");
		session.removeAttribute("JasperPrint");
		session.removeAttribute("CotServerInfo");
		session.removeAttribute("sysdic");
		session.removeAttribute("loginInfo");
		//session.invalidate();
		System.out.println("--------------"+session.getAttribute("empId"));
		ServletContext application = session.getServletContext();
		ConcurrentHashMap<String,Long> onLineMap = (ConcurrentHashMap<String,Long>)application.getAttribute("onLineMap");
		//删除内存中的登录记录
		if(onLineMap != null )
			onLineMap.remove(request.getRemoteAddr());
		return mapping.findForward("logoutSuccess");
	}
	public ActionForward modifyLoginInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		CotLoginInfo loginInfo = (CotLoginInfo)session.getAttribute("loginInfo");
		if(loginInfo == null) return null;
		loginInfo.setLoginTime(new Date(System.currentTimeMillis()));
		try {
			System.out.println("更新登录时间");
			this.getCotEmpsService().updateLoginInfo(loginInfo);
		} catch (Exception e) {
			System.out.println("更新系统时间异常,登录ID:"+loginInfo.getId());
		}
		return null;
	}
	
	//查询在线人数树
	public ActionForward queryLoginTree(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String type = request.getParameter("flag");
		try {
			String jsonTree = this.getCotLoginInfoService().getLoginTree(Integer.parseInt(type));
			response.getWriter().write(jsonTree);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		Date   date   =   new   java.sql.Date(System.currentTimeMillis());   
		System.out.println(date);   

	}

}
