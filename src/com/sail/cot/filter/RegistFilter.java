/**
 * 
 */
package com.sail.cot.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.jason.core.Application;
import com.sail.cot.service.system.CotRegistService;
 

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Sep 28, 2008 5:40:49 PM </p>
 * <p>Class Name: RegistFilter.java </p>
 * @author achui
 *
 */
public class RegistFilter   implements Filter {

	 
	public void destroy() {
		 
	}

	 
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		 
	 
		CotRegistService cotRegistService = (CotRegistService) this.getService("CotRegistService");
		boolean isExist = cotRegistService.isExistMesg();
		System.out.println("isExist="+isExist);
		if(!isExist){
			RequestDispatcher rd=request.getRequestDispatcher("/system/serverInfo/cotserverInfo.jsp");
			request.setAttribute("message", "友情提示：您尚未注册，请注册后再登陆！");
			rd.forward(request, response);
			return;
		} 
		boolean isSame = cotRegistService.isSameRegeditKey();
		System.out.println("isSame="+isSame);
		if(!isSame)
		{
			RequestDispatcher rd=request.getRequestDispatcher("/system/serverInfo/cotserverInfoUpdata.jsp");
			request.setAttribute("message", "友情提示：您的注册码有误，请重新注册！");
			rd.forward(request, response);
			return;
		}
		 
		chain.doFilter(request, response);
	}

	 
	public void init(FilterConfig arg0) throws ServletException {
		 
		System.out.println(System.getProperty("java.library.path"));
	}
	
	private Object getService(String strSerivce)
	{
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}

}
