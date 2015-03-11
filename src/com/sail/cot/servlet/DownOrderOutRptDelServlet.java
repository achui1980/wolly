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
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.pdf.InService;
import com.sail.cot.util.pdf.create.InPdf;
import com.sail.cot.util.pdf.impl.InServiceImpl;

public class DownOrderOutRptDelServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JasperPrint jasperPrint = (JasperPrint) request.getSession()
				.getAttribute("JasperPrint");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
		StringBuffer queryString = new StringBuffer();
		String printType = request.getParameter("printType");// 导出类型
		String factoryId = request.getParameter("factoryId");// 出货明细的厂家
		String headerFlag = request.getParameter("headerFlag");
		String orderOutNo = request.getParameter("orderOutNo");
		String orderId = request.getParameter("orderId");// 主报价单号
		if(orderOutNo == null)
			orderOutNo = "出货单";
		if(headerFlag == null)
			headerFlag = "false";
		queryString.append(" 1=1");
		// 用于区分是在预览页面导出还是样品页面导出
		String queryAgain = request.getParameter("queryAgain");
		if (queryAgain == null) {
			if (orderId != null && !orderId.trim().equals("")) {
				queryString.append(" and obj.ORDER_ID=" + orderId);
			}else{
				return;
			}
			if (factoryId != null && !factoryId.trim().equals("")) {
				queryString.append(" and obj.FACTORY_ID=" + factoryId);
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
			this.setFileDownloadResponseHeader(response, orderOutNo+".xls");
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
			try {
				InPdf inPdf = new InPdf();
				InService inService = new InServiceImpl();
				inPdf.setvInvoice(inService.getCotInVO(Integer
						.parseInt(orderId),false));
				inPdf.setDetailList(inService.getDetailList(Integer
						.parseInt(orderId),false));
				//判断出货主单或作废主单的公司是否是wollly 如果是则返回true
				boolean chk=inService.isWolly(Integer.parseInt(orderId),true);
				inPdf.createInPDF(response, request, false,chk);
				//			JRPdfExporter exporter = new JRPdfExporter();
				//			JRPdfExporter exporter_view = new JRPdfExporter();
				//			this.setFileDownloadResponseHeader(response, orderOutNo+".pdf");
				//			
				//			File file = new File(SystemUtil.getRptFilePath()+"orderoutdel");
				//			if (!file.exists()) {
				//				file.mkdirs();
				//			}
				//			
				//			System.out.println( SystemUtil.getRptFilePath());
				//			String rptPath = SystemUtil.getRptFilePath()+"/orderoutdel/"+ orderOutNo+".pdf";
				//			//File outFile = new File(rptPath);
				//			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rptPath);
				//			exporter_view.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				//			exporter_view.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
				//			
				//			try {
				//				exporter.exportReport();
				//				exporter_view.exportReport();
				//			} catch (JRException e) {
				//				e.printStackTrace();
				//			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if ("HTML".equals(printType)) {
			JRHtmlExporter exporter = new JRHtmlExporter();
			this.setFileDownloadResponseHeader(response, orderOutNo+".html");
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
		//boolean hasPopedom = this.checkPopedom(request, response);
		//if (hasPopedom)
			downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//boolean hasPopedom = this.checkPopedom(request, response);
		//if (hasPopedom)
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

		String strURL = "cotorderout.do";
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
		String strURL = "cotorderout.do";//权限URL，对应数据库中的VALIDURL
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
