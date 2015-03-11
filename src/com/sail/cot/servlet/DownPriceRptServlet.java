/**
 * 
 */
package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import org.apache.commons.lang.StringUtils;

import com.jason.core.Application;
import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.util.SystemUtil;

public class DownPriceRptServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JasperPrint jasperPrint = (JasperPrint) request.getSession()
				.getAttribute("JasperPrint");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
		StringBuffer queryString = new StringBuffer();
		String printType = request.getParameter("printType");// 导出类型
		String headerFlag = request.getParameter("headerFlag");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String priceNo = request.getParameter("priceNo");
		System.out.println(priceNo);
		if(priceNo == null)
			priceNo = "报价单";
		if(headerFlag == null)
			headerFlag = "false";
		queryString.append(" 1=1");
		// 用于区分是在预览页面导出还是样品页面导出
		String queryAgain = request.getParameter("queryAgain");
		if (queryAgain == null) {
			String priceId = request.getParameter("priceId");// 主报价单号
			if (priceId != null && !priceId.trim().equals("")) {
				queryString.append(" and obj.PRICE_ID=" + priceId);
			}
			String reportTemple = request.getParameter("reportTemple");// 导出模板
			String rptXMLpath = request.getRealPath("/") + File.separator
			+ reportTemple;
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("STR_SQL", queryString.toString());
			if(startTime!=null && !"".equals(startTime)){
				paramMap.put("startTime", startTime.toString());
			}
			if(endTime!=null && !"".equals(endTime)){
				paramMap.put("endTime", endTime.toString());
			}
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
			this.setFileDownloadResponseHeader(response, priceNo+".xls");
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
			this.setFileDownloadResponseHeader(response, priceNo+".pdf");
			
			File file = new File(SystemUtil.getRptFilePath()+"price");
			if (!file.exists()) {
				file.mkdirs();
			}
			
			System.out.println( SystemUtil.getRptFilePath());
			String rptPath = SystemUtil.getRptFilePath()+"/price/"+ priceNo+".pdf";
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
			this.setFileDownloadResponseHeader(response, priceNo+".html");
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

		String strURL = "cotprice.do";
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
		String strURL = "cotprice.do";//权限URL，对应数据库中的VALIDURL
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
				+ encodingFileName(fileName));
		// response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
	
	public static String encodingFileName(String fileName) {
        String returnFileName = "";   
        try {
        	//执行下面编码转换后会把" "转成"+"号,会把"+"转成"%2B"
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            //执行下面把"+"替换成"%20",传到浏览器解析后就成空格了
            returnFileName = StringUtils.replace(returnFileName, "+", "%20"); 
            //执行下面把"%2B"替换成"+"
            returnFileName = StringUtils.replace(returnFileName, "%2B", "+");   
            if (returnFileName.length() > 150) {   
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");   
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");   
            }   
        } catch (UnsupportedEncodingException e) {   
            e.printStackTrace();   
        }   
        return returnFileName;   
    }  
}
