package com.sail.cot.action.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.util.RMB;
import com.sail.cot.util.RptDesign4SD;

public class NewReportAction extends AbstractAction {

	private CotExportRptService rptService;
	public CotExportRptService getRptService() {
		if (rptService == null) {
			rptService = (CotExportRptService) super.getBean("CotRptService");
		}
		return rptService;
	}

	public void setRptService(CotExportRptService rptService) {
		this.rptService = rptService;
	}
	
	
	private CotOrderService orderService;
	public CotOrderService getOrderService() {
		if (orderService == null) {
			orderService = (CotOrderService) super.getBean("CotOrderService");
		}
		return orderService;
	}

	public void setOrderService(CotOrderService orderService) {
		this.orderService = orderService;
	}

	@SuppressWarnings("deprecation")
	public ActionForward queryRpt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		JasperPrint jasperPrint = null;
		
		StringBuffer queryString = new StringBuffer();
		//String printType = request.getParameter("printType");// 导出类型
		
		queryString.append(" 1=1");
		
		//页面预览翻页是保留参数
		String queryAgain = request.getParameter("queryAgain");
		if (queryAgain == null) {
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
					CotOrder cotOrder = this.getOrderService().getOrderById(Integer.parseInt(mainId));
					Integer totalContainer = cotOrder.getTotalContainer();
					Double totalCBM = cotOrder.getTotalCBM();
					Float handlefee = cotOrder.getHandleFee();
					Double totalMoney = cotOrder.getTotalMoney();
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
						byte[] orderMB = this.getOrderService().getPicImgByOrderId(Integer.parseInt(mainId));
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
					
					jasperPrint = this.getRptService().getJasperPrint(rptXMLpath2, paramMap);
					String rndFlag = RandomStringUtils.randomAlphabetic(10);
					request.getSession().setAttribute("JasperPrint"+rndFlag, jasperPrint);
					request.setAttribute("rndFlag", rndFlag);
					//request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
					
				} catch (JRException e) {

					e.printStackTrace();
				}
			} 
		}
		
		//预览跳转
		return mapping.findForward("previewSuccess");
	}

	 
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
 
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	 
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	 
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

}
