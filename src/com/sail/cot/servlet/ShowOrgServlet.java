package com.sail.cot.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.core.Application;

public class ShowOrgServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 获得tomcat路径
		String classPath = ShowOrgServlet.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = new String(request.getParameter("filePath").getBytes(
				"ISO8859-1"), "UTF-8");
		int last = filePath.lastIndexOf("?");
		int dian = filePath.lastIndexOf(".", last);
		String hou = filePath.substring(dian + 1, last);
		filePath = filePath.substring(0, last);

		try {
			File file = new File(systemPath + filePath);
			if (file.exists()) {
				FileInputStream in = new FileInputStream(file);
				byte[] picImg = new byte[((int) file.length())];
				while (in.read(picImg) != -1) {
				}
				in.close();
				if (hou.equals("pdf")) {
					response.setContentType("application/pdf");
				} else {
					response.setContentType("image/jpeg");
				}
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				OutputStream out = response.getOutputStream();
				out.write(picImg);
				out.flush();
				out.close();
				out = null;
			} else {
				response.getWriter()
						.write("<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.getWriter().write(
					"<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
		}

	}

	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer()
				.getComponent(strSerivce);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}

}
