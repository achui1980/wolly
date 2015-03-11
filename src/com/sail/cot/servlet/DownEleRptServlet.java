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
import net.sf.jasperreports.engine.export.JRXlsExporter;

import com.jason.core.Application;
import com.sail.cot.service.sample.CotExportRptService;

/**
 * <p>
 * Title: 工艺品管理系统
 * </p>
 * <p>
 * Description:样品档案报表下载
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Sep 6, 2008 6:09:15 PM
 * </p>
 * <p>
 * Class Name: DownEleRptServlet.java
 * </p>
 * 
 * @author achui
 * 
 */
public class DownEleRptServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JasperPrint jasperPrint = (JasperPrint) request.getSession()
				.getAttribute("JasperPrint");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
		String printType = request.getParameter("printType");// 导出类型
		String headerFlag = request.getParameter("headerFlag");
		if(headerFlag == null)
			headerFlag = "false";
		StringBuffer queryString = new StringBuffer();
		queryString.append(" 1=1");
		// 用于区分是在预览页面导出还是样品页面导出
		String queryAgain = request.getParameter("queryAgain");
		if (queryAgain == null) {
			String reportTemple = request.getParameter("reportTemple");// 导出模板
			String ids = request.getParameter("ids");// 选择的样品编号
			
			String eleId = request.getParameter("eleIdFind");// 样品编号
			String child = request.getParameter("childFind");// 子货号标识
			String eleName = request.getParameter("eleNameFind");// 中文名
			String eleNameEn = request.getParameter("eleNameEnFind");// 英文名
			String factoryId = request.getParameter("factoryIdFind");// 厂家
			String eleCol = request.getParameter("eleColFind");// 颜色
			String startTime = request.getParameter("startTime");// 起始时间
			String endTime = request.getParameter("endTime");// 结束时间
			String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
			String eleTypeidLv2 = request.getParameter("eleTypeidLv2");// 中类
			String eleTypeidLv3 = request.getParameter("eleTypeidLv3");// 小类
			String eleGrade = request.getParameter("eleGradeFind");// 等级
			String eleForPerson = request.getParameter("eleForPersonFind");// 开发对象
			String eleHsid = request.getParameter("eleHsid");// 海关编码
			String eleDesc = request.getParameter("eleDescFind");// 产品描述
			String eleTypenameLv2 = request.getParameter("eleTypenameLv2");// 所属年份
			String eleSizeDesc = request.getParameter("eleSizeDesc");// 中文规格
			String eleInchDesc = request.getParameter("eleInchDesc");// 英文规格
			
			String boxLS = request.getParameter("boxLS");// 产品起始长
			String boxLE = request.getParameter("boxLE");// 产品结束长
			String boxWS = request.getParameter("boxWS");// 产品起始宽
			String boxWE = request.getParameter("boxWE");// 产品结束宽
			String boxHS = request.getParameter("boxHS");// 产品起始高
			String boxHE = request.getParameter("boxHE");// 产品结束高
			
			String priceId = request.getParameter("priceId");//报价单预览时的主报价单编号
			String orderId = request.getParameter("orderId");//订单预览时的主订单编号
			String signId = request.getParameter("signId");//征样预览时的主订单编号
			String givenId = request.getParameter("givenId");//送样预览时的主订单编号
			String addEmp = request.getParameter("addEmp");//条形码预览时的添加人Id
			

			if (ids != null && !ids.toString().trim().equals("")
					&& !ids.toString().trim().equals("all")) {
				String temp = ids.toString();
				queryString.append(" and ele.ID in("
						+ temp.substring(0, temp.length() - 1) + ")");
			}
			
			else {
				if (child != null && child.toString().trim().equals("true")) {
					queryString.append(" and ele.ELE_FLAG=2");
				}else{
					queryString.append(" and (ele.ELE_FLAG is null or ele.ELE_FLAG!=2)");
				}
	
				if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
					queryString.append(" and e.ELE_TYPEID_LV2="
							+ eleTypeidLv2.toString());
				}
				if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
					queryString.append(" and e.ELE_TYPEID_LV3="
							+ eleTypeidLv2.toString());
				}
				if (eleHsid != null && !eleHsid.toString().equals("")) {
					queryString.append(" and e.HS_ID="
							+ eleHsid.toString());
				}
				if (eleDesc != null && !eleDesc.toString().equals("")) {
					queryString.append(" and e.ELE_DESC like '%"
							+ eleDesc.toString().trim() + "%'");
				}
				if (eleTypenameLv2 != null && !eleTypenameLv2.toString().equals("")) {
					queryString.append(" and e.ELE_TYPENAME_LV2=" + eleTypenameLv2.toString());
				}
				
				if (eleSizeDesc != null && !eleSizeDesc.toString().equals("")) {
					queryString.append(" and e.ELE_SIZE_DESC like '%"
							+ eleSizeDesc.toString().trim() + "%'");
				}
				if (eleInchDesc != null && !eleInchDesc.toString().equals("")) {
					queryString.append(" and e.ELE_INCH_DESC like '%"
							+ eleInchDesc.toString().trim() + "%'");
				}
				if (eleId != null && !eleId.toString().trim().equals("")) {
					queryString.append(" and ele.ELE_ID like '%"
							+ eleId.toString().trim() + "%'");
				}
				if (eleName != null && !eleName.toString().trim().equals("")) {
					queryString.append(" and ele.ELE_NAME like '%"
							+ eleName.toString().trim() + "%'");
				}
				if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
					queryString.append(" and ele.ELE_NAME_EN like '%"
							+ eleNameEn.toString().trim() + "%'");
				}
				if (factoryId != null && !factoryId.toString().equals("")) {
					queryString.append(" and ele.FACTORY_ID=" + factoryId.toString());
				}
				if (eleCol != null && !eleCol.toString().trim().equals("")) {
					queryString.append(" and ele.ELE_COL like '%"
							+ eleCol.toString().trim() + "%'");
				}
				if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
					queryString.append(" and ele.ELE_TYPEID_LV1="
							+ eleTypeidLv1.toString());
				}
				if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
					queryString.append(" and ele.ELE_TYPEID_LV2="
							+ eleTypeidLv2.toString());
				}
				if (eleGrade != null && !eleGrade.toString().equals("")) {
					queryString.append(" and ele.ELE_GRADE like '%"
							+ eleGrade.toString().trim() + "%'");
				}
				
				if (eleForPerson != null && !eleForPerson.toString().equals("")) {
					queryString.append(" and ele.ELE_FOR_PERSON like '%"
							+ eleForPerson.toString().trim() + "%'");
				}
	
				if (startTime != null && !"".equals(startTime.trim())) {
					queryString
							.append(" and ele.ELE_ADD_TIME >='" + startTime + "'");
				}
				if (endTime != null && !"".equals(endTime.trim())) {
					queryString.append(" and ele.ELE_ADD_TIME <='" + endTime
							+ " 23:59:59'");
				}
				
				if (boxLS != null && boxLE != null) {
					if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
						queryString.append(" and ele.BOX_L between "
								+ boxLS + " and " + boxLE);
					}
				}
				
				if (boxWS != null && boxWE != null) {
					if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
						queryString.append(" and ele.BOX_W between "
								+ boxWS + " and " + boxWE);
					}
				}
				
				if (boxHS != null && boxHE != null) {
					if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
						queryString.append(" and ele.BOX_H between "
								+ boxHS + " and " + boxHE);
					}
				}			
			}
			//----------------报价预览条件(固定报表中sql中的明细表的别名为obj)-----------------------------------
			if (priceId != null && !priceId.trim().equals("")) {
				queryString.append(" and obj.PRICE_ID=" + priceId);
			}
			if (orderId != null && !orderId.trim().equals("")) {
				queryString.append(" and obj.ORDER_ID=" + orderId);
			}
			if (signId != null && !signId.trim().equals("")) {
				queryString.append(" and obj.SIGN_ID=" + signId);
			}
			if (givenId != null && !givenId.trim().equals("")) {
				queryString.append(" and obj.GIVEN_ID=" + givenId);
			}
			if (addEmp != null && !addEmp.trim().equals("")) {
				queryString.append(" and obj.ADD_EMP=" + addEmp);
			}
			//----------------报价预览条件(固定报表中sql中的明细表的别名为obj)-----------------------------------
			
			String rptXMLpath = request.getRealPath("/") + File.separator
					+ reportTemple;
			HashMap paramMap = new HashMap();
			paramMap.put("STR_SQL", queryString.toString());
			System.out.println("STR_SQL:"+queryString.toString());
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
			this.setFileDownloadResponseHeader(response, "file.pdf");
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
				e.printStackTrace();
			}
		}
		if ("PRINT".equals(printType)) {
			try {
				String pageIndex = request.getParameter("pageIndex");// 导出类型
				if(pageIndex.equals("0")){
					pageIndex = "1";
				}
				JasperPrintManager.printPages(jasperPrint, Integer.parseInt(pageIndex)-1, Integer.parseInt(pageIndex)-1, false);
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if ("PRINTALL".equals(printType)) {
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

	private boolean checkPopedom(HttpServletRequest request,
			HttpServletResponse response) {
		String printType = request.getParameter("printType");// 导出类型
		if ("PRINT".equals(printType)) {
			return this.checkPrintPopedom(request, response);
		} else
			return this.checkExpPopedom(request, response);
	}

	// 判断导出权限
	private boolean checkExpPopedom(HttpServletRequest request,
			HttpServletResponse response) {

		String strURL = "previewrpt.do";
		String empId = (String) request.getSession().getAttribute("empNo");
		// admin用户不做权限判断
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

	// 判断是否有打印权限
	private boolean checkPrintPopedom(HttpServletRequest request,
			HttpServletResponse response) {
		String strURL = "previewrpt.do";// 权限URL，对应数据库中的VALIDURL
		String empId = (String) request.getSession().getAttribute("empNo");
		// admin用户不做权限判断
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
