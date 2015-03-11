/**
 * 
 */
package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
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

public class DownOrderFacRptServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JasperPrint jasperPrint = (JasperPrint) request.getSession().getAttribute("JasperPrint");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
		String printType = request.getParameter("printType");// 导出类型
		String headerFlag = request.getParameter("headerFlag");
		String orderFacNo = request.getParameter("orderFacNo");
		if(orderFacNo == null)
			orderFacNo = "采购单";
		if(headerFlag == null)
			headerFlag = "false";
		StringBuffer queryString = new StringBuffer();
		queryString.append(" 1=1");
		// 用于区分是在预览页面导出还是采购页面导出
		String queryAgain = request.getParameter("queryAgain");
		if (queryAgain == null) {
			String orderId = request.getParameter("orderId");// 主采购单号
			if (orderId != null && !orderId.trim().equals("")) {
				queryString.append(" and obj.order_id=" + orderId);
			}else{
				return;
			}
			String reportTemple = request.getParameter("reportTemple");// 导出模板
			String rptXMLpath = request.getRealPath("/") + File.separator + reportTemple;
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("STR_SQL", queryString.toString());
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			paramMap.put("HEADER_PER_PAGE", headerFlag);
			jasperPrint = rptService.getJasperPrint(rptXMLpath, paramMap);
			request.getSession().setAttribute("JasperPrint", jasperPrint);
		}
		
		OutputStream ouputStream = response.getOutputStream();
		if ("XLS".equals(printType)) {
			String exlSheet = request.getParameter("exlSheet");
			if(exlSheet == null)
				exlSheet = "false";
			JRXlsExporter exporter = new JRXlsExporter();
			this.setFileDownloadResponseHeader(response, orderFacNo+".xls");
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
			List pages = jasperPrint.getPages();
			JRPrintText text = null;
			String[] sheetNames = new String[pages.size()];
			for(int i=0; i<pages.size(); i++)
			{
				
				JRPrintPage printPage = (JRPrintPage)pages.get(i);
				for(int j=0; j<printPage.getElements().size(); j++)
				{
					JRPrintElement printele = (JRPrintElement)printPage.getElements().get(j);
					if( printele instanceof JRPrintText)
					{
						text = (JRPrintText)printele;
						if(printele.getKey().equals("ele_detail"))
						{
							System.out.println("Text:"+ text.getText());
							sheetNames[i] = text.getText();
							break;
						}
					}
				}
				
			}
			if(exlSheet.equals("true")&& sheetNames.length > 0)
			{
				exporter.setParameter(
						JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
						Boolean.TRUE);
				exporter.setParameter(
						JRXlsAbstractExporterParameter.SHEET_NAMES,sheetNames);
			}
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if ("PDF".equals(printType)) {
			JRPdfExporter exporter = new JRPdfExporter();
			JRPdfExporter exporter_view = new JRPdfExporter();
			this.setFileDownloadResponseHeader(response, orderFacNo+".pdf");
			
			File file = new File(SystemUtil.getRptFilePath()+"orderfac");
			if (!file.exists()) {
				file.mkdirs();
			}
			
			System.out.println( SystemUtil.getRptFilePath());
			String rptPath = SystemUtil.getRptFilePath()+"/orderfac/PO-"+ orderFacNo+".pdf";
			//File outFile = new File(rptPath);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rptPath);
			exporter_view.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter_view.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
			
			try {
				exporter.exportReport();
				exporter_view.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if ("HTML".equals(printType)) {
			JRHtmlExporter exporter = new JRHtmlExporter();
			this.setFileDownloadResponseHeader(response, orderFacNo+".html");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/CotSystem/servlets/image?image=");
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "servlets/image?image=");
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
			try {
				exporter.exportReport();
			} catch (JRException e) {
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
		
		downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
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
		response.setHeader("Content-Disposition", "attachment; filename="+ java.net.URLEncoder.encode(fileName, "UTF-8"));
		// response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
}
