/**
 * 
 */
package com.sail.cot.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;

import org.apache.commons.lang.RandomStringUtils;

import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.util.ContextUtil;

/**
 * <p>Title: 旗航ERP管理系统（QHERP）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * <p>Create Time: Sep 26, 2010 3:49:38 PM </p>
 * <p>Class Name: DownStaticsticRptServlet.java </p>
 * @author achui
 *
 */
public class DownStaticsticRptServlet extends HttpServlet {

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
	
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		CotExportRptService rptService = (CotExportRptService)ContextUtil.getBean("CotRptService");
		JasperPrint jasperPrint = null;
		String printFlag = request.getParameter("printFlag");
		String reportTemple = request.getParameter("reportTemple");//导出模板
		Enumeration<String> enumeration = request.getParameterNames();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		while(enumeration.hasMoreElements()){
			String name = enumeration.nextElement();
			if("method".equals(name) || "random".equals(name) 
					|| "reportTemple".equals(name) || "printType".equals(name))
				continue;
			String value = request.getParameter(name);
			if(value != null && !"".equals(value)){
				if (name.toUpperCase().equals("ENDTIME") || name.toUpperCase().equals("COMPAREFROMEND") || name.toUpperCase().equals("COMPARETOEND")) {
					value += " 23:59:59";
				}
				System.out.println("-----name:value-----:" + name.toUpperCase()
						+ ":" + value);
				paramMap.put(name.toUpperCase(), value);
			}
		}
		//报表模块文件的位置
		String rptXMLpath = request.getRealPath("/")+ reportTemple;
		//设置模板的参数(查询条件)
		
		jasperPrint = rptService.getJasperPrint(rptXMLpath, paramMap);
		String printType = request.getParameter("printType");// 导出类型
		String title = request.getParameter("title");
		if(title == null)
			title = "统计报表";
		OutputStream ouputStream = response.getOutputStream();
		if ("XLS".equals(printType)) {
			String exlSheet = request.getParameter("exlSheet");
			if(exlSheet == null)
				exlSheet = "false";
			JExcelApiExporter exporter = new JExcelApiExporter();
			this.setFileDownloadResponseHeader(response, title+".xls");
			//response.setContentType("application/xls");
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					ouputStream);
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if ("PDF".equals(printType)) {
			JRPdfExporter exporter = new JRPdfExporter();
			this.setFileDownloadResponseHeader(response, title+".pdf");
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					ouputStream);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
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
		//response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
		 response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
}
