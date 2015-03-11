/**
 * 
 */
package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
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

public class DownSplitRptServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JasperPrint jasperPrint = (JasperPrint) request.getSession()
				.getAttribute("JasperPrint");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
		String headerFlag = request.getParameter("headerFlag");
		if(headerFlag == null)
			headerFlag = "false";
		StringBuffer queryString = new StringBuffer();
		queryString.append(" 1=1");
		
		String splitId = request.getParameter("splitId");// 主排载单号
		if (splitId != null && !splitId.trim().equals("")) {
			queryString.append(" and obj.SPLIT_ID=" + splitId);
		}else{
			return;
		}
		
		String eleId = request.getParameter("eleIdFind");// 样品编号
		String eleName = request.getParameter("eleNameFind");// 中文名
		String eleCol = request.getParameter("eleColFind");// 颜色
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 样品类别
		String reportTemple = request.getParameter("reportTemple");// 导出模板
		String printType = request.getParameter("printType");// 导出类型
		if (eleId != null && !eleId.trim().equals("")) {
			queryString.append(" and ele.ELE_ID like '%" + eleId.trim() + "%'");
		}
		if (eleName != null && !eleName.trim().equals("")) {
			queryString.append(" and ele.ELE_NAME like '%" + eleName.trim()
					+ "%'");
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and ele.FACTORY_ID=" + factoryId);
		}
		if (eleCol != null && !eleCol.trim().equals("")) {
			queryString.append(" and ele.ELE_COL like '%" + eleCol.trim()
					+ "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.trim().equals("")) {
			queryString.append(" and ele.ELE_TYPEID_LV1=" + eleTypeidLv1);
		}

		String rptXMLpath = request.getRealPath("/") + File.separator
				+ reportTemple;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("STR_SQL", queryString.toString());
		paramMap.put("IMG_PATH", request.getRealPath("/"));
		jasperPrint = rptService.getJasperPrint(rptXMLpath, paramMap);
		paramMap.put("HEADER_PER_PAGE", headerFlag);
		request.getSession().setAttribute("JasperPrint", jasperPrint);

		OutputStream ouputStream = response.getOutputStream();
		if ("XLS".equals(printType)) {
			JRXlsExporter exporter = new JRXlsExporter();
			this.setFileDownloadResponseHeader(response, "file.xls");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("PDF".equals(printType)) {
			JRPdfExporter exporter = new JRPdfExporter();
			this.setFileDownloadResponseHeader(response, "file.pdf");
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					ouputStream);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("HTML".equals(printType)) {
			JRHtmlExporter exporter = new JRHtmlExporter();
			this.setFileDownloadResponseHeader(response, "file.html");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("PRINT".equals(printType)) {

			// PrintRequestAttributeSet printRequestAttributeSet = new
			// HashPrintRequestAttributeSet();
			// printRequestAttributeSet.add(MediaSizeName.ISO_A4);

			// PrintServiceAttributeSet printServiceAttributeSet = new
			// HashPrintServiceAttributeSet();
			// printServiceAttributeSet.add(new PrinterName("Epson Stylus 800
			// ESC/P 2", null));
			// printServiceAttributeSet.add(new PrinterName("HP LaserJet 4P",
			// null));

			// JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			//			
			// exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME,
			// "PRINT");
			// exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
			// printRequestAttributeSet);
			// exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
			// printServiceAttributeSet);
			// exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
			// Boolean.TRUE);
			// exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
			// Boolean.TRUE);

			try {
				// exporter.exportReport();
				//JasperPrintManager.printPages(jasperPrint, 0, jasperPrint.getPages().size()-1, false);
				//JasperPrintManager.printReport(jasperPrint, false);
				int index = 0;
				index = jasperPrint.getPages().size() - 1;
				if (index <= 0)
					index = 0;
				JasperPrintManager.printPages(jasperPrint, 0, index, false);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// response.setContentType("application/octet-stream");
			// ServletOutputStream ouputStream1 = response.getOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(ouputStream1);
			// oos.writeObject(jasperPrint);//将JasperPrint对象写入对象输出流中
			// oos.flush();
			// oos.close();
			// ouputStream1.flush();
			// ouputStream1.close();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean hasPopedom = this.checkPopedom(request, response);
		if (hasPopedom)
			downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean hasPopedom = this.checkPopedom(request, response);
		if (hasPopedom)
			downFile(request, response);
	}

	private boolean checkPopedom(HttpServletRequest request,
			HttpServletResponse response) {
		String printType = request.getParameter("printType");// 导出类型
		if ("PRINT".equals(printType)) {
			return this.checkPrintPopedom(request, response);
		} else
			return this.checkExpPopedom(request, response);
	}

	//判断导出权限
	private boolean checkExpPopedom(HttpServletRequest request,
			HttpServletResponse response) {

		String strURL = "previewrpt.do";
		String empId = (String) request.getSession().getAttribute("empNo");
		//admin用户不做权限判断
		if (!"admin".equals(empId)) {
			Map popedomMap = (Map) request.getSession().getAttribute(
					"popedomMap");
			Map map = (Map) popedomMap.get(strURL);
			System.out.println("map:" + map);
			if (map == null || map.get("EXP") == null) {
				RequestDispatcher rd = request
						.getRequestDispatcher("/common/home.jsp");
				request.setAttribute("errorID", "");
				request.setAttribute("message", "您没有权限下载");
				try {
					rd.forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
		return true;
	}

	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer()
				.getComponent(strSerivce);
	}

	//判断是否有打印权限
	private boolean checkPrintPopedom(HttpServletRequest request,
			HttpServletResponse response) {
		String strURL = "previewrpt.do";//权限URL，对应数据库中的VALIDURL
		String empId = (String) request.getSession().getAttribute("empNo");
		//admin用户不做权限判断
		if (!"admin".equals(empId)) {
			Map popedomMap = (Map) request.getSession().getAttribute(
					"popedomMap");
			Map map = (Map) popedomMap.get(strURL);
			System.out.println("map:" + map);
			if (map == null || map.get("PRINT") == null) {
				RequestDispatcher rd = request
						.getRequestDispatcher("/common/home.jsp");
				request.setAttribute("errorID", "");
				request.setAttribute("message", "您没有权限打印");
				try {
					rd.forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
		return true;
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
