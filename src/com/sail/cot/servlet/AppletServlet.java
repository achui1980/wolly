/**
 * 
 */
package com.sail.cot.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sail.cot.util.SystemUtil;

import com.sail.cot.service.sample.CotExportRptService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Mar 11, 2009 3:41:38 PM </p>
 * <p>Class Name: AppletServlet.java </p>
 * @author achui
 *
 */
public class AppletServlet extends HttpServlet {

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

		doPost(request, response);
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
		System.out.println("============");
		String rndFlag = request.getParameter("rndFlag");
		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute("JasperPrint"+rndFlag);
		try {
			response.setContentType("application/octet-stream");   
            ServletOutputStream out = response.getOutputStream();   
            ObjectOutputStream os = new ObjectOutputStream(out);   
            os.writeObject(jasperPrint);   
            os.flush();   
            os.close();  
            request.getSession().removeAttribute("JasperPrint"+rndFlag);

		} catch (Exception e) {
			 
			e.printStackTrace();
		}
//		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute("JasperPrint");
//		//页面预览翻页是保留参数
//		String queryAgain = request.getParameter("queryAgain");
//		String flag = request.getParameter("flag");
//		String queryString = request.getParameter("queryString");
//		String headerFlag = request.getParameter("headerFlag");
//		if(headerFlag == null)
//			headerFlag = "false";
//		queryString = queryString.replaceAll("_E_", "=").replaceAll("_S_", " ");
//		//queryString = " 1=1 ";
//		String reportTemple = request.getParameter("reportTemple");
//		reportTemple = reportTemple.replaceAll("_Anti_", "/");
//		long st = System.currentTimeMillis();
//		if(queryAgain == null)
//		{	
//			//String reportTemple = request.getParameter("reportTemple");//导出模板	
//			//----------------报价预览条件(固定报表中sql中的明细表的别名为obj)-----------------------------------
//			
//			//报表模块文件的位置
//			String rptXMLpath = request.getRealPath("/")+ reportTemple;
//			//rptXMLpath = "E:\\apache-tomcat-6.0.18\\webapps\\CotSystem\\reportfile\\Quotation.jrxml";
//			//String exportPath = request.getRealPath("/reportfile")+File.separator + "elements_nopic.xls";
//			//设置模板的参数(查询条件)
//			HashMap<String, String> paramMap = new HashMap<String, String>();
//			paramMap.put("IMG_PATH", request.getRealPath("/"));
//			paramMap.put("STR_SQL", queryString.toString());
//			paramMap.put("HEADER_PER_PAGE", headerFlag);
//			System.out.println("___SQL:"+queryString);
//			
//		    jasperPrint = this.getRptService().getJasperPrint(rptXMLpath, paramMap);
//		}
//		try {
//			response.setContentType("application/octet-stream");   
//            ServletOutputStream out = response.getOutputStream();   
//            ObjectOutputStream os = new ObjectOutputStream(out);   
//            os.writeObject(jasperPrint);   
//            os.flush();   
//            os.close();  
//            long end = System.currentTimeMillis();
//            System.out.println("运行时间："+( end - st)+"ms");
//
//		} catch (Exception e) {
//			 
//			e.printStackTrace();
//		}
		

	}
	private CotExportRptService rptService;
	public CotExportRptService getRptService() {
		if(rptService == null)
		{
			rptService = (CotExportRptService)SystemUtil.getService("CotRptService");
		}
		return rptService;
	}
	public void setRptService(CotExportRptService rptService) {
		this.rptService = rptService;
	}
}
