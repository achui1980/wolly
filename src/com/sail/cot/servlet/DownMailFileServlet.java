package com.sail.cot.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sail.cot.email.util.ContentTypeUtil;
import com.sail.cot.email.util.MailLocalUtil;
import com.sail.cot.util.Log4WebUtil;
import com.zhao.mail.util.CodeUtil;
 
@SuppressWarnings("serial")
public class DownMailFileServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet  {
	private Logger logger = Log4WebUtil.getLogger(DownMailFileServlet.class);
	
	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		logger.debug("下载文件");
		try {
			String path = request.getParameter("path");
			String name = request.getParameter("name");
			String t = request.getParameter("t");
			//打开指定文件的流信息
			java.io.InputStream fs = null;
			
			path = CodeUtil.deCode(path);
			String filePath = MailLocalUtil.getProPath()+path;
			name = name==null?"":name;
			String fileName = URLDecoder.decode(name, "UTF-8"); //文件名，输出到用户的下载对话框
			System.out.println(fileName);
			try {
				fs = new FileInputStream(new File(filePath));
			} catch (java.io.FileNotFoundException e) {
				logger.error("文件不存在", e);
				return;
			}
			String postfixName = fileName.substring(fileName.lastIndexOf(".")+1);
			if (t!=null&&t.equals("down")||!ContentTypeUtil.setContentType(postfixName, response)) {
				response.setContentType("application/octet-stream; CHARSET=utf8");
				response.setHeader("Content-Disposition", "attachment; filename="
						+ URLEncoder.encode(fileName, "UTF-8"));		
			}
			//设置响应头和保存文件名 
			//写出流信息
			int b = 0;
			try {
				PrintWriter out = response.getWriter();
				while ((b = fs.read()) != -1) {
					out.write(b);
				}
				fs.close();
				out.close();
				logger.debug("文件下载完毕");
			} catch (Exception e) {
				logger.error("文件下载失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
}
