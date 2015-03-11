/**
 * 
 */
package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;

import com.jason.core.Application;
import com.sail.cot.service.sample.CotExportRptService;

public class DownFitRptServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JasperPrint jasperPrint = (JasperPrint) request.getSession()
				.getAttribute("JasperPrint");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
		StringBuffer queryString = new StringBuffer();
		String printType = request.getParameter("printType");// 导出类型
		String headerFlag = request.getParameter("headerFlag");
		if (headerFlag == null)
			headerFlag = "false";
		queryString.append(" 1=1");
		// 用于区分是在预览页面导出还是样品页面导出
		String queryAgain = request.getParameter("queryAgain");
		if (queryAgain == null) {
			String fitId = request.getParameter("fitId");// 主单号
			if (fitId != null && !fitId.trim().equals("")) {
				queryString.append(" and obj.id=" + fitId);
			} else {
				return;
			}
			String reportTemple = request.getParameter("reportTemple");// 导出模板
			String rptXMLpath = request.getRealPath("/") + File.separator
					+ reportTemple;
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("STR_SQL", queryString.toString());
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			paramMap.put("HEADER_PER_PAGE", headerFlag);
			jasperPrint = rptService.getJasperPrint(rptXMLpath, paramMap);
			request.getSession().setAttribute("JasperPrint", jasperPrint);
		}
		OutputStream ouputStream = response.getOutputStream();
		if ("XLS".equals(printType)) {
			JRXlsExporter exporter = new JRXlsExporter();
			this.setFileDownloadResponseHeader(response, "配件单.xls");
			response.setContentType("application/xls");
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
			this.setFileDownloadResponseHeader(response, "配件单.pdf");
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
		if ("HTML".equals(printType)) {
			JRHtmlExporter exporter = new JRHtmlExporter();
			this.setFileDownloadResponseHeader(response, "配件单.html");
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
//					"/CotSystem/servlets/image?image=");
		"servlets/image?image=");
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					ouputStream);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if ("PRINT".equals(printType)) {
			try {
				int index = 0;
				index = jasperPrint.getPages().size() - 1;
				if (index <= 0)
					index = 0;
				JasperPrintManager.printPages(jasperPrint, 0, index, false);
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		downFile(request, response);
	}

	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer()
				.getComponent(strSerivce);
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
		response.setHeader("Content-Disposition", "attachment; filename="
				+ java.net.URLEncoder.encode(fileName, "UTF-8"));
		// response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
}
