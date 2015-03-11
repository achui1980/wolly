/**
 * 
 */
package com.sail.cot.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.core.Application;
import com.mysql.jdbc.Field;
import com.sail.cot.domain.CotPanDetail;
import com.sail.cot.service.pan.CotPanService;
import com.sail.cot.util.ContextUtil;

/**
 * <p>Title: 旗航外贸管理软件V8.0</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Dec 17, 2012 9:56:12 AM </p>
 * <p>Class Name: DownPanAttachServlet.java </p>
 * @author achui
 *
 */
public class DownPanAttachServlet extends HttpServlet {

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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		downFile(request, response);
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
		downFile(request, response);
	}
	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}
	/**
	 * 设置文件下载的header
	 * 
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 */
	private void setFileDownloadResponseHeader(HttpServletResponse response,
			String fileName) throws UnsupportedEncodingException {
		// response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition","attachment;  filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
	
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		CotPanService panService = (CotPanService)getService("CotPanService");
		String panId = request.getParameter("panId");
		CotPanDetail detail = panService.getPanDetailById(Integer.valueOf(panId));
		String fileUrl = detail.getFileUrl();
		byte[] buf = new byte[1024];
		int b = 0;
		if(fileUrl != null){
			File file = new File(ContextUtil.getRealPath()+"/"+fileUrl);
			String fileName= file.getName();
			setFileDownloadResponseHeader(response, fileName);
			ServletOutputStream os = response.getOutputStream();
			FileInputStream fs = new FileInputStream(file);
			while((b = fs.read(buf)) != -1){
				os.write(buf, 0, b);
			}
			fs.close();
			os.close();
		}
	}
}
