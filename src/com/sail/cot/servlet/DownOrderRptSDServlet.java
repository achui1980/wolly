/**
 * 
 */
package com.sail.cot.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;

import com.jason.core.Application;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.util.RMB;
import com.sail.cot.util.RptDesign4SD;
import com.sail.cot.util.SystemUtil;

public class DownOrderRptSDServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException{

		CotOrderService orderService = (CotOrderService) SystemUtil.getService("CotOrderService");
		CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
		
		JasperPrint jasperPrint = null;
		
		StringBuffer queryString = new StringBuffer();
		String printType = request.getParameter("printType");// 导出类型
		String orderNo = request.getParameter("orderNo");
		
		queryString.append(" 1=1");
		String mainId = request.getParameter("orderId");// 主单号
		if (mainId != null && !mainId.trim().equals("")) {
			queryString.append(" and obj.ORDER_ID=" + mainId);
			String reportTemple = request.getParameter("reportTemple");// 导出模板
			String rptXMLpath = request.getRealPath("/") + File.separator + reportTemple;
			
			//设置传递参数
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("STR_SQL", queryString.toString());
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			
			//String eleNum = request.getParameter("eleNum");  //每页货号数量
			String HEADER_PER_PAGE = request.getParameter("HEADER_PER_PAGE");  //表头分页显示
			//String mHead = request.getParameter("mHead");  //唛头资料打印
			String eleDesc = request.getParameter("eleDesc");  //货物描述部分作为附页
			String insure = request.getParameter("insure");  //打印保险类别
			String elenum = request.getParameter("eleNum"); //每页显示记录数
			String bank = request.getParameter("bank");  //打印银行资料 
			String Sum = request.getParameter("Sum");  //打印合计数 
			String handlingFee = request.getParameter("handlingFee");  //额外费用 
			String isTran = request.getParameter("isTran");  //允许分批装运和转运 
			String clauseType = request.getParameter("clauseType");  //价格条款
			String isGiven = request.getParameter("isGiven");  //是否寄样
			
			String mHeadText = request.getParameter("mHeadText");  //文本唛头
			String mHeadPic = request.getParameter("mHeadPic");  //图片唛头
			
			String orderByOutEle = request.getParameter("orderByOutEle");  //外商货号-(输出排列顺序)
			String orderByInEle = request.getParameter("orderByInEle");  //我司货号-(输出排列顺序)
			String orderByEleType = request.getParameter("orderByEleType");  //产品种类-(输出排列顺序)
			
			String outEle = request.getParameter("outEle");  //外商货号-(输出货号选择)
			String inEle = request.getParameter("inEle");  //我司货号-(输出货号选择)
			String facEle = request.getParameter("facEle");  //供商货号-(输出货号选择)
			boolean hasDetail = false;
			if(eleDesc != null)
				hasDetail = true;
			//表头分页显示
			if(HEADER_PER_PAGE!=null){
				paramMap.put("HEADER_PER_PAGE", HEADER_PER_PAGE);
			}
			
			//打印保险类别
			if(insure==null){
				//paramMap.put("insure", "");
			}else{
				paramMap.put("insure", insure);
			}
			
			//打印银行资料
			if(bank==null){
				//paramMap.put("bank", "");
			}else{
				paramMap.put("bank", bank);
			}
			
			//打印合计数 
			if(Sum==null){
				//paramMap.put("Sum", "");
				//paramMap.put("enSum", "");
			}else{
				CotOrder cotOrder = orderService.getOrderById(Integer.parseInt(mainId));
				Integer totalContainer = cotOrder.getTotalContainer();
				Double totalCBM = cotOrder.getTotalCBM();
				Float handlefee = cotOrder.getHandleFee();
				Float totalMoney = cotOrder.getTotalMoney().floatValue();
				if(handlingFee != null)
				{
					handlefee = handlefee == null?0:handlefee;
					Sum = "Total:     "+totalContainer+" Ctns     "+totalCBM+" CBM     U.S.Dollar "+totalMoney;
					totalMoney = totalMoney +handlefee;
					paramMap.put("otherCharge", cotOrder.getFeeName()+":"+handlefee);
					paramMap.put("totalCharge", "Total:"+totalMoney );
					
				}
				else {
					totalMoney = totalMoney +handlefee;
					Sum = "Total:     "+totalContainer+" Ctns     "+totalCBM+" CBM     U.S.Dollar "+totalMoney;
				}
				
				paramMap.put("Sum", Sum);
				String enTotalMoney	 = RMB.convertToEnglish(totalMoney.toString());
				String enSum = "Total: U.S.Dollar  "+enTotalMoney+"  ONLY.";
				paramMap.put("enSum", enSum);
			}
			
			//允许分批装运和转运
			if(isTran==null){
				//paramMap.put("isTran", "");
			}else{
				isTran = " with transshipment and partial shipments allowed. ";
				paramMap.put("isTran", isTran);
			}
			 
			//价格条款
			if(clauseType==null){
				//paramMap.put("clauseType", "");
			}else{
				clauseType = "Terms of payment: T/T 30% FOR DEPOSIT 70& AT THE SIGHT OF COPY OF BANK NOTE \n";
			    paramMap.put("clauseType", clauseType);
			}
			 
			//是否寄样
			if(isGiven==null){
				//paramMap.put("isGiven", "");
			}else{
				isGiven = "Samples: 32PCS SAMPLES TO BE SENT TO BUYER BY MAY.30 2000 WITH FREIGHT PREPAID \n";
			    paramMap.put("isGiven", isGiven);
			}
			//设置没有选项，需要显示的属性
			paramMap.put("shipdate", "shipdate");
			paramMap.put("remark", "remark");
			if(mHeadText == null)
			{
				paramMap.put("mHeadText", "mHeadText");
			}
			
			//图片唛头
			if(mHeadPic==null){
				//paramMap.put("mHeadPic", "");
			}else{
				try {
					byte[] orderMB = orderService.getPicImgByOrderId(Integer.parseInt(mainId));
					InputStream in = new ByteArrayInputStream(orderMB);
					paramMap.put("mHeadPic", in);
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
//				String ORDERBY = "";
//				
//				//外商货号-(输出排列顺序)
//				if(orderByOutEle!=null){
//					ORDERBY += " obj.CUST_NO  ";
//				    paramMap.put("ORDERBY", ORDERBY);
//				}
//				
//				//我司货号-(输出排列顺序)
//				if(orderByInEle!=null){
//				    if(ORDERBY==""){
//						ORDERBY += " obj.FACTORY_NO  ";
//					}else{
//						ORDERBY += ", obj.FACTORY_NO  ";
//					}
//					paramMap.put("ORDERBY", ORDERBY);
//				}
//				
//				//产品种类-(输出排列顺序)
//				if(orderByEleType!=null){
//					if(ORDERBY==""){
//						ORDERBY += " obj.ELE_TYPEID_LV1  ";
//					}else{
//						ORDERBY += ", obj.ELE_TYPEID_LV1  ";
//					}
//					paramMap.put("ORDERBY", ORDERBY);
//				}
//				paramMap.put("ORDERBY", ORDERBY );
			//动态生成报表数据
			Map disColMap = new HashMap();
			String display = "";
			if(inEle != null)
				display +="$F{obj_ELE_ID}+";
			if(outEle != null)
				display += "($F{obj_CUST_NO}==null?\"\":\"/\"+$F{obj_CUST_NO}+\"/\")+";
			if(facEle != null)
				display +="($F{obj_FACTORY_NO}==null?\"\":\"/\"+$F{obj_FACTORY_NO}+\"/\")+";
			if(!"".equals(display) && !display.equals("$F{obj_ELE_ID}+"))
			{
				System.out.println("old:"+display);
				display = display.substring(0,display.length() -6)+")";
				System.out.println(display);
				disColMap.put("eleNo", display);
			}
			else if(display.equals("$F{obj_ELE_ID}+"))
			{
				System.out.println("old:"+display);
				display = display.substring(0,display.length() -1);
				System.out.println(display);
				disColMap.put("eleNo", display);
			}
			
			int pageSize = 0;
			if(elenum != null && !elenum.equals(""))
				pageSize = Integer.parseInt(elenum);
			try {
				JasperDesign design = RptDesign4SD.getDesign(rptXMLpath, paramMap, "ColumnFooter",disColMap,pageSize);
				if(hasDetail) //附件显示
				{
					design = RptDesign4SD.moveDesign(design, "PageHeader",false);
					design = RptDesign4SD.moveDesign(design, "ColumnFooter",true);
				}
				String rptXMLpath2 = request.getRealPath("/") + File.separator + "reportfile/orderRpt2.jrxml";
				net.sf.jasperreports.engine.xml.JRXmlWriter.writeReport((JRReport)design, rptXMLpath2, "UTF-8");
				
				jasperPrint = rptService.getJasperPrint(rptXMLpath2, paramMap);
				String rndFlag = RandomStringUtils.randomAlphabetic(10);
				request.getSession().setAttribute("JasperPrint"+rndFlag, jasperPrint);
				
			} catch (JRException e) {

				e.printStackTrace();
			}
		} 
		
		OutputStream ouputStream = response.getOutputStream();
		if ("XLS".equals(printType)) {
			String exlSheet = request.getParameter("exlSheet");
			if(exlSheet == null)
				exlSheet = "false";
			JRXlsExporter exporter = new JRXlsExporter();
			this.setFileDownloadResponseHeader(response, orderNo+".xls");
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
			this.setFileDownloadResponseHeader(response, orderNo+".pdf");
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

		String strURL = "cotorder.do";
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
		String strURL = "cotorder.do";//权限URL，对应数据库中的VALIDURL
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
