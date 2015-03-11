
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

 
public class DownCustServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute("JasperPrint");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");

		StringBuffer sql = new StringBuffer();
		sql.append(" 1=1");
		 
		    String flag = request.getParameter("flag");// 区分是否是表格中的导出功能
			if (flag != null) {
				String ids = request.getParameter("ids");// 选择的客户ID 
				if(ids.length()!=0){
					sql.append(" and obj.ID in (" + ids+ ")");
				}
			} else {
				String customerNo = request.getParameter("custNo");
				String customerShortName = request.getParameter("custShortName");
				String custTypeId = request.getParameter("custTypeId");
				String custLvId = request.getParameter("custLvId");
				String nationId = request.getParameter("nationId");
				String provinceId = request.getParameter("provinceId");
				String cityId = request.getParameter("cityId");
				String eId = request.getParameter("empsId");
				 
				if(null!=customerNo && !"".equals(customerNo))
				{
					customerNo = customerNo.trim();
					sql.append(" and obj.CUSTOMER_NO like '%"+customerNo+"%'");
				}
				if(null!=customerShortName && !"".equals(customerShortName))
				{
					customerShortName = customerShortName.trim();
					sql.append(" and obj.CUSTOMER_SHORT_NAME like '%"+customerShortName+"%'");
				}
				if(null!=custTypeId && !"".equals(custTypeId))
				{
					custTypeId = custTypeId.trim();
					sql.append(" and obj.CUST_TYPE_ID ="+custTypeId);
				}
				if(null!=custLvId && !"".equals(custLvId))
				{
					custLvId = custLvId.trim();
					sql.append(" and obj.CUST_LV_ID ="+custLvId);
				}
				if(null!=nationId && !"".equals(nationId))
				{
					nationId = nationId.trim();
					sql.append(" and obj.NATION_ID ="+nationId);
				}
				if(null!=provinceId && !"".equals(provinceId))
				{
					provinceId = provinceId.trim();
					sql.append(" and obj.PROVINCE_ID ="+provinceId);
				}
				if(null!=cityId && !"".equals(cityId))
				{
					cityId = cityId.trim();
					sql.append(" and obj.CITY_ID ="+cityId);
				}
				if(null!=eId && !"".equals(eId))
				{
					eId = eId.trim();
					sql.append(" and obj.EMP_ID ="+eId);
				}
			}
			String rptXMLpath = request.getRealPath("/reportfile") + File.separator
			+ "cust_report.jrxml"; 
			
			System.out.println("query=" + sql.toString());
			HashMap paramMap = new HashMap();
			paramMap.put("STR_SQL", sql.toString()); 
			jasperPrint = rptService.getJasperPrint(rptXMLpath, paramMap);
			request.getSession().setAttribute("JasperPrint", jasperPrint);
		 
			OutputStream ouputStream = response.getOutputStream();
		 
			JRXlsExporter exporter = new JRXlsExporter();
			this.setFileDownloadResponseHeader(response, "file.xls");
			response.setContentType("application/xls");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean hasPopedom = this.checkPopedom(request, response);
		if(hasPopedom)
			downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean hasPopedom = this.checkPopedom(request, response);
		if(hasPopedom)
			downFile(request, response);
	}
	
	private boolean checkPopedom(HttpServletRequest request,
			HttpServletResponse response)
	{
		return this.checkExpPopedom(request, response);
	}
	
	//判断导出权限
	private boolean checkExpPopedom(HttpServletRequest request,
			HttpServletResponse response)
	{
		String strURL ="cotcustomer.do";
		String empId = (String) request.getSession().getAttribute("empNo");
		//admin用户不做权限判断
		if(!"admin".equals(empId))
		{
			Map popedomMap = (Map)request.getSession().getAttribute("popedomMap");
			Map map = (Map)popedomMap.get(strURL);
			System.out.println("map:"+map);
			if(map == null || map.get("EXP") == null)
			{
				RequestDispatcher rd=request.getRequestDispatcher("/common/home.jsp");
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
		response.setHeader("Content-Disposition", "attachment; filename="
				+ java.net.URLEncoder.encode(fileName, "UTF-8"));
		// response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
	}
}
