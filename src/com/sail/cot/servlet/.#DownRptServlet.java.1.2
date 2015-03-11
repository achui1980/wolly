
package com.sail.cot.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.jason.core.Application;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.util.RMB;


public class DownRptServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	CotExportRptService rptService = (CotExportRptService) getService("CotRptService");
	CotOrderService orderService = (CotOrderService) getService("CotOrderService");
	
	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		JasperPrint jasperPrint = (JasperPrint) request.getSession().getAttribute("JasperPrint");
		
		StringBuffer queryString = new StringBuffer();
		String printType = request.getParameter("printType");// 导出类型
		String headerFlag = request.getParameter("headerFlag");
		if(headerFlag == null)
			headerFlag = "false";
		queryString.append(" 1=1");
		// 用于区分是在预览页面导出还是样品页面导出
		String queryAgain = request.getParameter("queryAgain");
		if (queryAgain == null) {
			// 主单号
			String mainId = request.getParameter("mainId");
			if (mainId != null && !mainId.trim().equals("")) {
				queryString.append(" and obj.ORDER_ID=" + mainId);
			}else{
				return;
			}
			// 导出模板
			String reportTemple = request.getParameter("reportTemple");
			String rptXMLpath = request.getRealPath("/") + File.separator + reportTemple;
			
			//设置传递参数
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("STR_SQL", queryString.toString());
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			
			//String eleNum = request.getParameter("eleNum");  //每页货号数量
			String HEADER_PER_PAGE = request.getParameter("HEADER_PER_PAGE");  //表头分页显示
			//String mHead = request.getParameter("mHead");  //唛头资料打印
			//String eleDesc = request.getParameter("eleDesc");  //货物描述部分作为附页
			String insure = request.getParameter("insure");  //打印保险类别
			String bank = request.getParameter("bank");  //打印银行资料 
			String Sum = request.getParameter("Sum");  //打印合计数 
			//String handlingFee = request.getParameter("handlingFee");  //额外费用 
			String isTran = request.getParameter("isTran");  //允许分批装运和转运 
			String clauseType = request.getParameter("clauseType");  //价格条款
			String isGiven = request.getParameter("isGiven");  //是否寄样
			
			//String mHeadText = request.getParameter("mHeadText");  //文本唛头
			String mHeadPic = request.getParameter("mHeadPic");  //图片唛头
			
			String orderByOutEle = request.getParameter("orderByOutEle");  //外商货号-(输出排列顺序)
			String orderByInEle = request.getParameter("orderByInEle");  //我司货号-(输出排列顺序)
			String orderByEleType = request.getParameter("orderByEleType");  //产品种类-(输出排列顺序)
			
			String outEle = request.getParameter("outEle");  //外商货号-(输出货号选择)
			String inEle = request.getParameter("inEle");  //我司货号-(输出货号选择)
			String facEle = request.getParameter("facEle");  //供商货号-(输出货号选择)
			
			//表头分页显示
			if(HEADER_PER_PAGE!=null){
				paramMap.put("HEADER_PER_PAGE", HEADER_PER_PAGE);
			}
			
			//打印保险类别
			if(insure==null){
				paramMap.put("insure", "");
			}else{
				insure = "Insurance: √ To be effected by the sellers for 110% invoice value covering            □ W. A.         □ all risks and \n" +
				"                   war risk as per Ocean Marine Cargo and War Clauses (CIC) dated 1/1/1981. \n" +
				"                  □ To be effected by the buyer.";
				paramMap.put("insure", insure);
			}
			
			//打印银行资料
			if(bank==null){
				paramMap.put("bank", "");
			}else{
				bank = "Banking Details:\n" +
			      "Beneficiary:  STAR (HONG KONG) TRADING COMPANY  \n"+
			      "Address:  FLAT E, 8/F., CONTINENTAL MANSION, 294 KING'S ROAD, NORTH POINT, HONG KONG  \n"+
			      "Advising Bank: CHIYU BANKING CORPORATION LTD.  \n"+
			      "Address: 78 DES VOEUX ROAD C., HONG KONG  \n"+
			      "Swift Address: CIYU HK HH                                       Cable Address: \"CHIYU\" HONG KONG  \n"+
			      "Telex No.: 83488 CHIYU HX                                       A/C No.(US$): 03973192046668  \n"+
			      "Address: 78 DES VOEUX ROAD C., HONG KONG  \n";
				paramMap.put("bank", bank);
			}
			
			//打印合计数 
			if(Sum==null){
				paramMap.put("Sum", "");
				paramMap.put("enSum", "");
			}else{
				CotOrder cotOrder = orderService.getOrderById(Integer.parseInt(mainId));
				Integer totalContainer = cotOrder.getTotalContainer();
				Double totalCBM = cotOrder.getTotalCBM();
				Float totalMoney = cotOrder.getTotalMoney().floatValue();
				Sum = "Total:     "+totalContainer+" Ctns     "+totalCBM+" CBM     U.S.Dollar "+totalMoney;
				paramMap.put("Sum", Sum);
				
				String enTotalMoney	 = RMB.convertToEnglish(totalMoney.toString());
				String enSum = "Total: U.S.Dollar  "+enTotalMoney+"  ONLY.";
				paramMap.put("enSum", enSum);
			}
			
			//允许分批装运和转运
			if(isTran==null){
				paramMap.put("isTran", "");
			}else{
				isTran = " with transshipment and partial shipments allowed. ";
				paramMap.put("isTran", isTran);
			}
			 
			//价格条款
			if(clauseType==null){
				paramMap.put("clauseType", "");
			}else{
				clauseType = "Terms of payment: T/T 30% FOR DEPOSIT 70& AT THE SIGHT OF COPY OF BANK NOTE \n";
			    paramMap.put("clauseType", clauseType);
			}
			 
			//是否寄样
			if(isGiven==null){
				paramMap.put("isGiven", "");
			}else{
				isGiven = "Samples: 32PCS SAMPLES TO BE SENT TO BUYER BY MAY.30 2000 WITH FREIGHT PREPAID \n";
			    paramMap.put("isGiven", isGiven);
			}
			
			
			//图片唛头
			if(mHeadPic==null){
				//paramMap.put("mHeadPic", "");
			}else{
				byte[] orderMB = orderService.getPicImgByOrderId(Integer.parseInt(mainId));
				InputStream in = new ByteArrayInputStream(orderMB);
				paramMap.put("mHeadPic", in);
				in.close();
				in = null;
			}
			
			
			String ORDERBY = "";
			
			//外商货号-(输出排列顺序)
			if(orderByOutEle!=null){
				ORDERBY += " obj.CUST_NO desc ";
			    paramMap.put("ORDERBY", ORDERBY);
			}
			
			//我司货号-(输出排列顺序)
			if(orderByInEle!=null){
			    if(ORDERBY==""){
					ORDERBY += " obj.FACTORY_NO desc ";
				}else{
					ORDERBY += ", obj.FACTORY_NO desc ";
				}
				paramMap.put("ORDERBY", ORDERBY);
			}
			
			//产品种类-(输出排列顺序)
			if(orderByEleType!=null){
				if(ORDERBY==""){
					ORDERBY += " obj.ELE_TYPEID_LV1 desc ";
				}else{
					ORDERBY += ", obj.ELE_TYPEID_LV1 desc ";
				}
				paramMap.put("ORDERBY", ORDERBY);
			}
			
			//动态生成报表数据
			JasperDesign design = this.getJasperDesign(rptXMLpath,outEle,inEle,facEle);
			try {
				String rptXMLpath2 = request.getRealPath("/") + File.separator + "reportfile/orderRpt2.jrxml";
				net.sf.jasperreports.engine.xml.JRXmlWriter.writeReport((JRReport)design, rptXMLpath2, "UTF-8");
				
				jasperPrint = rptService.getJasperPrint(rptXMLpath2, paramMap);
				request.getSession().setAttribute("JasperPrint", jasperPrint);
			} catch (JRException e) {

				e.printStackTrace();
			}
			 
			//jasperPrint = rptService.getJasperPrint(rptXMLpath, paramMap);
			//request.getSession().setAttribute("JasperPrint", jasperPrint);
		}
		
		OutputStream ouputStream = response.getOutputStream();
		if ("XLS".equals(printType)) {
			JRXlsExporter exporter = new JRXlsExporter();
			this.setFileDownloadResponseHeader(response, "orderRpt.xls");
			response.setContentType("application/xls");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,ouputStream);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if ("PDF".equals(printType)) {
			JRPdfExporter exporter = new JRPdfExporter();
			this.setFileDownloadResponseHeader(response, "orderRpt.pdf");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,ouputStream);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if ("HTML".equals(printType)) {
			JRHtmlExporter exporter = new JRHtmlExporter();
			this.setFileDownloadResponseHeader(response, "orderRpt.html");
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

		String strURL = "cotorder.do";
		String empId = (String) request.getSession().getAttribute("empNo");
		//admin用户不做权限判断
		if (!"admin".equals(empId)) {
			Map<?, ?> popedomMap = (Map<?, ?>) request.getSession().getAttribute(
					"popedomMap");
			Map<?, ?> map = (Map<?, ?>) popedomMap.get(strURL);
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
		
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}

	//判断是否有打印权限
	private boolean checkPrintPopedom(HttpServletRequest request,
			HttpServletResponse response) {
		String strURL = "cotorder.do";//权限URL，对应数据库中的VALIDURL
		String empId = (String) request.getSession().getAttribute("empNo");
		//admin用户不做权限判断
		if (!"admin".equals(empId)) {
			Map<?, ?> popedomMap = (Map<?, ?>) request.getSession().getAttribute(
					"popedomMap");
			Map<?, ?> map = (Map<?, ?>) popedomMap.get(strURL);
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

	 
	//设置文件下载的header
	private void setFileDownloadResponseHeader(HttpServletResponse response,
			String fileName) throws UnsupportedEncodingException {
		 
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
	}
	
	//动态生成报表数据
	public JasperDesign getJasperDesign(String file,String outEle,String inEle,String facEle)
	{
		JasperDesign design = null;
		try {
			design = JRXmlLoader.load(file);
			//JRField[] fieldList = design.getFields();
			JRElement[] elementList =  design.getColumnHeader().getElements();
			List<JRElement> arr = new ArrayList<JRElement>();
			//不显示密码列
			//int index = 0;
			for(JRElement element : elementList)
			{
				String key = element.getKey();
				System.out.println(key);
				if(outEle==null){
					if("staticText_custNo".equals(key))
					{
						continue;
					}
				} 
				if(inEle==null){
					if("staticText_eleNo".equals(key))
					{
						continue;
					}
				} 
				if(facEle==null){
					if("staticText_facNo".equals(key))
					{
						continue;
					}
				} 
				arr.add(element);
			}
			JRDesignBand band = new JRDesignBand();
			band.setHeight(40);
			int currentX = 0;
			for(JRElement element : arr)
			{
				element.setX(currentX);
				currentX += element.getWidth();
				band.addElement((JRDesignStaticText)element);
			}
			design.setColumnHeader(band);
			
			band = new JRDesignBand();
			band.setHeight(40);
			JRElement[] elementDetailList = design.getDetail().getElements();
			
			arr = new ArrayList<JRElement>();
			currentX = 0;
			for(JRElement field : elementDetailList)
			{
				String key = field.getKey();
				System.out.println("aaa:"+key);
				if(outEle==null){
					if("textField_custNo".equals(key))
					{
						continue;
					}
				} 
				if(inEle==null){
					if("textField_eleNo".equals(key))
					{
						continue;
					}
				} 
				if(facEle==null){
					if("textField_facNo".equals(key))
					{
						continue;
					}
				} 
				arr.add(field);
			}
			for(JRElement element : arr)
			{
				element.setX(currentX);
				currentX += element.getWidth();
				
				if(element instanceof JRDesignTextField)
					band.addElement((JRDesignTextField)element);
				if(element instanceof JRDesignStaticText)
					band.addElement((JRDesignStaticText)element);
			}
			design.setDetail(band);
		} catch (JRException e) {
			
			e.printStackTrace();
		}
		return design;
	}
	
	
}
