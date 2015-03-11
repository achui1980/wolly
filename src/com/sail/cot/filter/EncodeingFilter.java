/**
 * 
 */
package com.sail.cot.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: Ext+DWR+Spring</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 10:00:54 AM </p>
 * <p>Class Name: EncodeingFilter.java </p>
 * @author achui
 *
 */
public class EncodeingFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	private String defaultEncodeing = "GBK";
	private boolean useEncodeing = true;
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		
		httpRequest.setCharacterEncoding(defaultEncodeing);
		httpResponse.setContentType("text/html; charset=" + defaultEncodeing);
		httpResponse.setCharacterEncoding(defaultEncodeing);
        System.out.println("----defaultEncodeing-----"+defaultEncodeing);
        System.out.println("url is " + httpRequest.getRequestURI());
        //response.setContentType("text/xml;charset=utf-8");
        chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		if(config.getInitParameter("useEncodeing") != null)
		{
			useEncodeing = Boolean.parseBoolean(config.getInitParameter("useEncodeing"));
		}
		if(config.getInitParameter("encoding") != null)
		{
			defaultEncodeing = config.getInitParameter("encoding");
		}

	}

}
