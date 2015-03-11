package com.sail.cot.servlet;
 
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.core.Application;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFittingsOrderdetailPic;
import com.sail.cot.domain.CotFittingsPic;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotOrderoutPic;
import com.sail.cot.domain.CotOrderouthsPic;
import com.sail.cot.domain.CotPanOtherPic;
import com.sail.cot.domain.CotPanPic;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotSignPic;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.orderfac.CotOrderFacService;
import com.sail.cot.service.orderout.CotOrderOutService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.system.CotCompanyService;
import com.sail.cot.util.SystemUtil;

public class DownServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer()
				.getComponent(strSerivce);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}

	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String detailId = request.getParameter("detailId");
		String flag = request.getParameter("flag");
		byte[] picImg = null;
		String fileName = null;
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			//最高权限
			boolean picSel = SystemUtil.isAction(request, "cotpicture.do", "SEL");
			if(picSel==false){
				CotElementsService cotElementsService = (CotElementsService) this.getService("CotElementsService");
				picImg =  cotElementsService.getNoPicSel();
				fileName = "noElePicSel";
				try{
					response.setContentType("application/octet-stream; CHARSET=utf8");
					response.setHeader("Content-Disposition", "attachment; filename="+fileName+".png");
					response.setHeader("Pragma","No-cache"); 
					response.setHeader("Cache-Control","no-cache"); 
					response.setDateHeader("Expires", 0); 
					OutputStream out = response.getOutputStream();
					out.write(picImg);
					out.flush();
					out.close();
					out = null;
					return;
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		if(flag.equals("noPic")){
			CotElementsService cotElementsService = (CotElementsService) this.getService("CotElementsService");
			picImg =  cotElementsService.getZwtpPic();
			fileName = "zwtp";
			try{
				response.setContentType("application/octet-stream; CHARSET=utf8");
				response.setHeader("Content-Disposition", "attachment; filename="+fileName+".png");
				response.setHeader("Pragma","No-cache"); 
				response.setHeader("Cache-Control","no-cache"); 
				response.setDateHeader("Expires", 0); 
				OutputStream out = response.getOutputStream();
				out.write(picImg);
				out.flush();
				out.close();
				out = null;
				return;
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(flag.equals("pic")){
			String picId = request.getParameter("picId");
			CotElementsService cotElementsService = (CotElementsService) this.getService("CotElementsService");
			CotPicture cotPicture = cotElementsService.getPicById(Integer.parseInt(picId));
			if(cotPicture != null){
				picImg =  cotPicture.getPicImg();
				fileName = cotPicture.getPicName();
				System.out.println("xx"+fileName);
			}
		}
		else if(flag.equals("ele")){
			String elementId = request.getParameter("elementId");
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotElePic cotElePic = opImgService.getElePicImgByEleId(Integer.parseInt(elementId));
			if(cotElePic != null) {
				picImg =  cotElePic.getPicImg();	
				fileName = cotElePic.getPicName();
			}
		}
		//通过货号获取样品图片
		else if (flag.equals("elePic")) {
			String eleId = request.getParameter("eleId");
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotElePic cotElePic = opImgService.getElePicImgByEleName(eleId);
			if(cotElePic != null) {
				picImg =  cotElePic.getPicImg();	
				fileName = cotElePic.getPicName();
			}
		}
		else if (flag.equals("sign")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotSignPic cotElePic = opImgService.getSignPic(Integer.parseInt(detailId));
			picImg = cotElePic.getPicImg();
			fileName = cotElePic.getPicName();
		}
		else if (flag.equals("given")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotGivenPic cotGivenPic = opImgService.getGivenPic(Integer.parseInt(detailId));
			picImg = cotGivenPic.getPicImg();
			fileName = cotGivenPic.getPicName();
		}
		else if (flag.equals("price")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotPricePic cotElePic = opImgService.getPricePic(Integer.parseInt(detailId));
			picImg = cotElePic.getPicImg();
			fileName = cotElePic.getPicName();
		}
		else if (flag.equals("order")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotOrderPic cotElePic = opImgService.getOrderPic(Integer.parseInt(detailId));
			picImg = cotElePic.getPicImg();
			fileName = cotElePic.getPicName();
		}
		else if (flag.equals("orderOut")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotOrderoutPic cotOrderoutPic = opImgService.getOrderOutPic(Integer.parseInt(detailId));
			picImg = cotOrderoutPic.getPicImg();
			fileName = cotOrderoutPic.getPicName();
		}
		else if (flag.equals("orderfac")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotOrderfacPic cotOrderfacPic = opImgService.getOrderFacPic(Integer.parseInt(detailId));
			picImg = cotOrderfacPic.getPicImg();
			fileName = cotOrderfacPic.getPicName();
		}
		else if (flag.equals("custMB")) {
			CotOrderService cotOrderService = (CotOrderService) this.getService("CotOrderService");
			picImg = cotOrderService.getPicImgByCustId(Integer.parseInt(detailId));
		}
		else if (flag.equals("orderMB")) {
			CotOrderService cotOrderService = (CotOrderService) this.getService("CotOrderService");
			picImg = cotOrderService.getPicImgByOrderId(Integer.parseInt(detailId));
		}
		else if (flag.equals("companyLogo")) {
			CotCompanyService cotCompanyService = (CotCompanyService) this.getService("CotCompanyService");
			picImg = cotCompanyService.getCompanyLogoById(Integer.parseInt(detailId));
		}
		else if (flag.equals("custPhoto")) {
			CotCustomerService cotCustomerService = (CotCustomerService) this.getService("CotCustomerService");
			picImg = cotCustomerService.getCustImgById(Integer.parseInt(detailId));
		}
		else if (flag.equals("custMb")) {
			CotCustomerService cotCustomerService = (CotCustomerService) this.getService("CotCustomerService");
			picImg = cotCustomerService.getPicImgById(Integer.parseInt(detailId));
		}
		else if (flag.equals("orderOutMB")) {
			CotOrderOutService cotOrderOutService = (CotOrderOutService) this.getService("CotOrderOutService");
			picImg = cotOrderOutService.getPicImgByOrderId(Integer.parseInt(detailId));
		}
		else if (flag.equals("orderFacMB")) {
			CotOrderFacService cotOrderFacService = (CotOrderFacService) this.getService("CotOrderFacService");
			picImg = cotOrderFacService.getFacMbByOrderFacId(Integer.parseInt(detailId));
		}
		else if (flag.equals("cha")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotOrderouthsPic cotOrderoutPic = opImgService.getOrderOuthsPic(Integer.parseInt(detailId));
			picImg = cotOrderoutPic.getPicImg();
			fileName = cotOrderoutPic.getPicName();
		}
		else if (flag.equals("fit")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotFittingsPic cotFittingsPic = opImgService.getFitPicImgById(Integer.parseInt(detailId));
			picImg = cotFittingsPic.getPicImg();
			fileName=cotFittingsPic.getPicName();
		}else if (flag.equals("fitorder")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotFittingsOrderdetailPic cotFittingsOrderdetailPic = opImgService.getFitOrderPicImgById(Integer.parseInt(detailId));
			picImg = cotFittingsOrderdetailPic.getPicImg();
			fileName=cotFittingsOrderdetailPic.getPicName();
		}else if (flag.equals("pan")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotPanPic cotPanPic = opImgService.getPanPic(Integer.parseInt(detailId));
			picImg = cotPanPic.getPicImg();
			fileName=cotPanPic.getPicName();
		}else if (flag.equals("panother")) {
			CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
			CotPanOtherPic cotPanOtherPic = opImgService.getPanOtherPic(Integer.parseInt(detailId));
			picImg = cotPanOtherPic.getPicImg();
			fileName=cotPanOtherPic.getPicName();
		}
		if(fileName==null){
			fileName="picture";
		}
		if(picImg!=null){
			try
			{
				response.setContentType("application/octet-stream; CHARSET=utf8");
				response.setHeader("Content-Disposition", "attachment; filename="+java.net.URLEncoder.encode(fileName,"UTF-8")+".jpg");	
				System.out.println("========"+fileName);
				response.setHeader("Pragma","No-cache"); 
				response.setHeader("Cache-Control","no-cache"); 
				response.setDateHeader("Expires", 0); 				
				OutputStream out = response.getOutputStream();
				out.write(picImg);
				out.flush();
				out.close();
				out = null;
			}
			catch(Exception ex)
			{
				//response.getWriter().write("<script>alert('读取图片失败：找不到图片文件');</script>");
				ex.printStackTrace();
			
			}
		}
		
	}	

}
