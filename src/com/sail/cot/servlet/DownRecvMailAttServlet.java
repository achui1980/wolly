package com.sail.cot.servlet;
 
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.core.Application;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotOrderoutPic;
import com.sail.cot.domain.CotOrderouthsPic;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotSignPic;
import com.sail.cot.mail.service.MailRecvService;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.orderfac.CotOrderFacService;
import com.sail.cot.service.orderout.CotOrderOutService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.system.CotCompanyService;
import com.sail.cot.util.SystemUtil;

public class DownRecvMailAttServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

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
		
//		String msgId = request.getParameter("msgId");
//		byte[] attach = null;
//		String fileName = null;
//		
//			MailRecvService mailRecvService = (MailRecvService)this.getService("mailRecvService");
//			CotMailAttach cotMailAttach = mailRecvService.getMailAttach(msgId);
//			attach = cotMailAttach.getFileContent();
//			fileName = cotMailAttach.getFileName();
//			System.out.println("fileName="+fileName);
//		if(attach!=null){
//			try
//			{
//				response.setContentType("application/octet-stream; CHARSET=utf8");
//				response.setHeader("Content-Disposition","attachment; filename="+
//							java.net.URLEncoder.encode(fileName,"UTF-8"));	
//				response.setHeader("Pragma","No-cache"); 
//				response.setHeader("Cache-Control","no-cache"); 
//				response.setDateHeader("Expires", 0); 
//				InputStream is = new ByteArrayInputStream(attach);
//				DataOutputStream os=new DataOutputStream(response.getOutputStream()); 
//				byte[] readBytes=new byte[512];
//				while(is.read(readBytes)!=-1){
//					os.write(readBytes);
//				}
//				os.close();  
//				is.close();  
//			}
//			catch(Exception ex)
//			{
//				//response.getWriter().write("<script>alert('读取图片失败：找不到图片文件');</script>");
//				ex.printStackTrace();
//			}
//		}
	}	
}
