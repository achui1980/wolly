package com.sail.cot.filter;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;

import com.jason.core.Application;
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotMailCache;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.email.util.MailCacheUtil;
import com.sail.cot.mail.service.MailService;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.system.CotLoginInfoService;
import com.sail.cot.trigger.TriggerAction;
import com.sail.cot.trigger.TriggerOnlineAction;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;
import com.sailing.oa.domain.CotStatData;

/**
 * @author:achui Description: Company:厦门纵横科技 2008-1-31
 */

public class PtnWebFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	private MailService mailService;
	private CotLoginInfoService cotLoginInfoService;

	public CotLoginInfoService getCotLoginInfoService() {
		if (cotLoginInfoService == null) {
			cotLoginInfoService = (CotLoginInfoService) this
					.getBean("CotLoginInfoService");
		}
		return cotLoginInfoService;
	}

	public void doFilter(ServletRequest request, ServletResponse reponse,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String str = httpRequest.getHeader("Accept-Encoding");  
		System.out.println("Accept-Encoding:"+str);
		System.out.println("url is " + httpRequest.getRequestURI());
		System.out.println("url is " + httpRequest.getServletPath());
		String context = httpRequest.getContextPath() + "/";
		String queryString = httpRequest.getQueryString();
		System.out.println("=========" + queryString);
		String strURL = httpRequest.getRequestURI().replace(context, "");
		String validUrl = httpRequest.getRequestURI();
		if (!(validUrl.endsWith("index.do")
				|| validUrl.endsWith("cotmodule.do")
				|| validUrl.endsWith("cotElePicShow.do")
				|| validUrl.endsWith("cotquery.do")
				|| validUrl.endsWith("previewrpt.do") 
				|| validUrl.endsWith("cotstatistics.do") 
				|| validUrl.endsWith("cotmessage.do")
				|| validUrl.endsWith("cotfaq.do")
				|| validUrl.endsWith("customervisitedlog.do"))) {
			if (httpRequest.getSession().getAttribute("empId") == null) {
				System.out.println("userid is null");
				RequestDispatcher rd = request
						.getRequestDispatcher("/common/error.jsp");
				request.setAttribute("errorID", "");
				request.setAttribute("message", "Your session has expired!");

				// 删除登录人数表中的记录
//				this.getCotLoginInfoService().deleteLoginInfos(
//						request.getRemoteAddr());
//				this.getCotLoginInfoService().deleteLoginInfos(httpRequest.getSession().getId());

				rd.forward(request, reponse);
				return;
			} else {
				ThreadObject threadObject = new ThreadObject();
				threadObject.setEmpId((Integer) httpRequest.getSession()
						.getAttribute("empId"));
				ThreadLocalManager.getCurrentThread().set(threadObject);

			}
			String empId = (String) httpRequest.getSession().getAttribute(
					"empNo");
			System.out.println("____________URL______" + strURL);
			//如果是配件分析,查看员工是否有配件合同管理的权限
			if("cotfitanys.do".equals(strURL)){
				strURL="cotfittingorder.do";
			}
			//如果是包材分析,查看员工是否有包材合同管理的权限
			if("cotpackanys.do".equals(strURL)){
				strURL="cotpackingorder.do";
			}
			// TODO:azan--修改  如果有查看邮件的权限  
			if("cotmailsend.do".equals(strURL)){
				strURL="cotmail.do";
			}
			
			// admin用户不做权限判断
			if (!"admin".equals(empId)) {
				Map popedomMap = (Map) httpRequest.getSession().getAttribute(
						"popedomMap");
				Map map = (Map) popedomMap.get(strURL);
				System.out.println("map:" + map);
				if (map == null || map.get("SEL") == null) {
					
					RequestDispatcher rd = request
							.getRequestDispatcher("/common/home.jsp");
					request.setAttribute("errorID", "");
					request.setAttribute("message", "You do not have permission!");
					rd.forward(request, reponse);
					return;
				}
			}
		}

		Log4WebUtil.info(PtnWebFilter.class, "调用Log4j");

		chain.doFilter(request, reponse);
	}

	public void init(FilterConfig config) throws ServletException {
		// 初始化Log4j配置
		/*
		 * String tomcatHome = config.getInitParameter("TOMCAT_HOME");
		 * System.setProperty("TOMCAT_HOME", tomcatHome);
		 * 
		 */
		// Log4WebUtil.initLog4j();
		// System.out.println(System.getProperty("web.app"));
		try {
			// SystemDicUtil dicUtil = new SystemDicUtil();
			// 初始化部分数据字典
			// dicUtil.setSysDicMap();
			this.getCotLoginInfoService().deleteLoginInfo();
			
//			List<CotMail> mailList = (List<CotMail>) this.getMailService()
//					.getMailListByStatus(4); // 获取所有未发送邮件
//			this.getMailService().updateMailList(mailList, 0);
			Log4WebUtil.info(PtnWebFilter.class, "清除登录记录成功");
			List<CotMailCache> cacheList = this.getMailLocalService().getMailListCache();
			MailCacheUtil.initMailList2Cache(cacheList);
			Log4WebUtil.info(PtnWebFilter.class, "获取邮件缓存信息成功！");
			Log4WebUtil.info(PtnWebFilter.class, "临时目录:"+System.getProperty("java.io.tmpdir"));
			
			this.triggerAction();
			
			//触发在线人数扫描线程
			this.triggerOnLineEmpAction(config.getServletContext());

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log4WebUtil.error(PtnWebFilter.class, "清除登录记录失败", e);
		}

		Log4WebUtil.info(PtnWebFilter.class, "初始化完毕");
	}

	protected Object getBean(String name) {
		return Application.getInstance().getContainer().getComponent(name);
	}
	private void triggerOnLineEmpAction(ServletContext application)
    {
    	String triggerEmpSec = ContextUtil.getProperty("remoteaddr.properties","empsec");
    	int empsec = 30;
    	if(triggerEmpSec != null) empsec = Integer.parseInt(triggerEmpSec);
    	TriggerOnlineAction onlineAction = new TriggerOnlineAction();
    	try {
    		System.out.println("启动在线扫描线程.......");
			onlineAction.startJob(empsec,application);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void triggerAction()
    {
    	String triggerdate = ContextUtil.getProperty("remoteaddr.properties","triggerdate");
    	String triggerhour = ContextUtil.getProperty("remoteaddr.properties","triggerhour");
    	String triggermin = ContextUtil.getProperty("remoteaddr.properties","triggermin");
    	String istrigger = ContextUtil.getProperty("remoteaddr.properties","istrigger");
    	
    	int dayOfWeek = 3;
    	int hour = 12;
    	int minute = 30;
    	boolean triggerable = true;
    	
    	if(triggerdate != null) dayOfWeek = Integer.parseInt(triggerdate);
    	if(triggerhour != null) hour = Integer.parseInt(triggerhour);
    	if(triggermin != null) minute = Integer.parseInt(triggermin);
    	if(istrigger != null) triggerable = Boolean.parseBoolean(istrigger);
    	System.out.println("dayofweek:"+dayOfWeek);
    	System.out.println("hour:"+hour);
    	System.out.println("minute:"+minute);
    	if(triggerable)
    	{
    		System.out.println("启动出发线程.......");
    		//默认每周3中午12点30执行
	    	TriggerAction action = new TriggerAction();
	    	try {
				action.startJob(dayOfWeek, hour, minute);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    private MailLocalService mailLocalService;
	public MailLocalService getMailLocalService() {
		Log4WebUtil.info(PtnWebFilter.class, "获取缓存信息成功");
		if (mailLocalService == null)
			mailLocalService = (MailLocalService) this.getBean("MailLocalService");
		return mailLocalService;
	}
}
