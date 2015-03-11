package com.sail.cot.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sail.cot.email.util.Constants;

/**
 * Servlet基本类，重写doGet doPost方法，访问该类继承的子类，
 * url格式为/servlet访问名?KEY值=子类中要访问的方法名
 *
 */
@SuppressWarnings("serial")
public class BasicServlet extends HttpServlet{	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private Logger logger = Logger.getLogger(BasicServlet.class);
	/**
	 * 重写doPost方法
	 */
	@Override
	protected void doPost(HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
		logger.debug("");
		// 把容器所给的request和response设置给自己的属性
		this.request = req;
		this.response = res;
		// 访问的方法名，如/book.php?m=方法名
		String m = req.getParameter(Constants.BASICSERVLET_METHOD_NAME);
		try {
			// 查找当前类中的方法名,this指子类
			// 在子类中的类结构中查找方法名叫m的方法，
			// 原来找参数类型为[HttpServletRequest,HttpServletResponse]
			// 后没有参数
			Method method = this.getClass().getMethod(m);
			// 执行找到的方法
			method.invoke(this);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
		this.doPost(req, res);
	}
	/**
	 * 转发
	 * @param url
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void dispatch(String url) throws ServletException, IOException{
		request.getRequestDispatcher(url)
			.forward(request, response);
		
	}
	/**
	 * 重定向
	 * @param url
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void redirect(String url) throws ServletException, IOException{
		response.sendRedirect(request.getContextPath() 
						+ url);
	}
}






