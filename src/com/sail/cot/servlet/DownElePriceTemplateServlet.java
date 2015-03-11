package com.sail.cot.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DownElePriceTemplateServlet extends HttpServlet implements Servlet {

	private void downEleTemplate(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 设置下载头
		setDownloadResponseHeader(response, "eleprice.xls");
		// 要导出的样品模板文件
		String filePath = "reportfile/eleprice.xls";
		// 获得tomcat路径
		String classPath = DownElePriceTemplateServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		try {
//			Workbook oldbook = Workbook.getWorkbook(new File(systemPath
//					+ filePath));
//			// 打开一个文件的副本，并且指定数据写回到原文件
//			WritableWorkbook book = Workbook.createWorkbook(new File(systemPath
//					+ filePath), oldbook);
//			WritableSheet sheet = book.getSheet(0);
//			int rows = sheet.getRows();
//			// 清空原来的数据
//			while (rows>=3) {
//				sheet.removeRow(3);
//				rows--;
//			}
//			book.write();
//			book.close();

			File file = new File(systemPath + filePath);
			DataInputStream is = new DataInputStream(new FileInputStream(file));
			DataOutputStream os = new DataOutputStream(response
					.getOutputStream());
			byte[] readBytes = new byte[128];
			while (is.read(readBytes) != -1) {
				os.write(readBytes);
			}
			os.close();
			is.close();

		} catch (Exception e) {
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
				+ java.net.URLEncoder.encode(fileName, "UTF-8"));
		// response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
}
