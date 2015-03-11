/**
 * 
 */
package com.sail.cot.servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sail.cot.util.Log4WebUtil;


/**
 * <p>Title: 旗航ERP管理系统（QHERP）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 25, 2010 11:01:54 AM </p>
 * <p>Class Name: OnlineServlet.java </p>
 * @author achui
 *
 */
public class OnlineServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	private ServletContext application;
	Logger logger = Log4WebUtil.getLogger(OnlineServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//每隔30秒触发一次servlet，对当前登录IP的登录时间戳进行更新
		ConcurrentHashMap<String,Long> map = (ConcurrentHashMap<String,Long>)application.getAttribute("onLineMap");
		if(map != null){
			logger.info("当前时间："+new Date(System.currentTimeMillis()));
			if(request.getSession()!=null)
				map.put(request.getSession().getId(), System.currentTimeMillis());
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		application = config.getServletContext();
	}

}
