package com.sail.cot.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipOutputStream;

import com.jason.core.Application;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.email.util.MailLocalUtil;
import com.sail.cot.util.Log4WebUtil;

 
@SuppressWarnings("serial")
public class DownMailAllFileServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet  {
	private Logger logger = Log4WebUtil.getLogger(DownMailAllFileServlet.class);
	private MailLocalService mailLocalService;
	public MailLocalService getMailLocalService(){
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)Application.getInstance().getContainer().getComponent("MailLocalService");
		}
		return mailLocalService;
	}
	@SuppressWarnings("deprecation")
	private void downFile(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		logger.debug("下载文件");
		try {
			String mailId = request.getParameter("mailId");
			
			if(mailId!=null&&!mailId.trim().equals("")){
				CotMail cotMail = this.getMailLocalService().readMailAllInfo(mailId);
				String fileName = cotMail.getSubject();
				if(cotMail.getAttachs().size()==0)
					return;
				if(fileName==null||fileName.trim().equals(""))
					fileName = "无主题";
				response.setContentType("application/octet-stream; CHARSET=utf8");
				response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(fileName+".zip", "UTF-8"));	
				response.setHeader("Pragma","No-cache"); 
				response.setHeader("Cache-Control","no-cache"); 
				response.setDateHeader("Expires", 0); 
				ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
				for (CotMailAttach attach : cotMail.getAttachs()) {
					out.putNextEntry(new org.apache.tools.zip.ZipEntry(attach.getName()));
					String filePath = MailLocalUtil.getProPath()+attach.getUrl();
				    InputStream fs = new FileInputStream(new File(filePath)); 
				    int b = 0;
				    while ((b = fs.read()) != -1) {
						out.write(b);
					}
				    fs.close();
				}
				out.flush();
				out.close();
				out = null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
}
