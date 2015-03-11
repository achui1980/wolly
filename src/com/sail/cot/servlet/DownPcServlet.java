package com.sail.cot.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownPcServlet extends HttpServlet implements Servlet {

	private void downEleTemplate(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 要导出的样品模板文件
//		String fileName = request.getParameter("fileName");
		String fileName = new String(request.getParameter("fileName").getBytes("ISO8859-1"), "UTF-8");
		String filePath = new String(request.getParameter("filePath").getBytes("ISO8859-1"), "UTF-8");
		
		// 获得tomcat路径
		String classPath = DownPcServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		try {
			File file = new File(systemPath + filePath);
			if(file.exists()){
				// 设置下载头
				setDownloadResponseHeader(response, fileName);
				DataInputStream is = new DataInputStream(new FileInputStream(file));
				DataOutputStream os = new DataOutputStream(response
						.getOutputStream());
				byte[] readBytes = new byte[128];
				while (is.read(readBytes) != -1) {
					os.write(readBytes);
				}
				os.close();
				is.close();
			}else{
				response.getWriter().write(
						"<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
			"<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downEleTemplate(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downEleTemplate(request, response);
	}

	/**
	 * 设置文件下载的header
	 * 
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 */
	private void setDownloadResponseHeader(HttpServletResponse response,
			String fileName) throws UnsupportedEncodingException {
		// response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(fileName));
		// response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
	
//	public static String encodingFileName(String fileName) {
//        String returnFileName = "";   
//        try {
//        	//执行下面编码转换后会把" "转成"+"号,会把"+"转成"%2B"
//            returnFileName = URLEncoder.encode(fileName, "UTF-8");
//            //执行下面把"+"替换成"%20",传到浏览器解析后就成空格了
//            returnFileName = StringUtils.replace(returnFileName, "+", "%20"); 
//            //执行下面把"%2B"替换成"+"
//            returnFileName = StringUtils.replace(returnFileName, "%2B", "+");   
//            if (returnFileName.length() > 150) {   
//                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");   
//                returnFileName = StringUtils.replace(returnFileName, " ", "%20");   
//            }   
//        } catch (UnsupportedEncodingException e) {   
//            e.printStackTrace();   
//        }   
//        return returnFileName;   
//    }  
}
