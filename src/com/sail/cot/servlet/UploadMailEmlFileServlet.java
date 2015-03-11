package com.sail.cot.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.jason.core.Application;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.email.util.Constants;
import com.sail.cot.email.util.MailLocalUtil;
import com.sail.cot.util.Log4WebUtil;
import com.zhao.mail.ReciveOneMailService;
import com.zhao.mail.impl.ReciveOneMailDefault;

@SuppressWarnings("serial")
public class UploadMailEmlFileServlet extends BasicServlet{
	private Logger logger = Log4WebUtil.getLogger(UploadMailEmlFileServlet.class);
	private MailLocalService mailLocalService;
	public MailLocalService getMailLocalService(){
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)Application.getInstance().getContainer().getComponent("MailLocalService");
		}
		return mailLocalService;
	}
	/**
	 * 上传文件方法
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void uploadFile(){
		logger.debug("上传邮件Eml");
		String nodeIdStr = request.getParameter("nodeId");
		InputStream inputStream = null;
		File importFile = null;
		try {
			ServletFUpload fUpload = new ServletFUpload();
			String realPath = MailLocalUtil.getProPath();
			fUpload.upload(request, realPath+Constants.MAIL_ATTACH_EML_PATH);
			List<File> uploadFiles = (List<File>) request.getAttribute(fUpload.uploadFile_key);
			if(uploadFiles.size()>0){
				for (File file : uploadFiles) {
					importFile = file;
					inputStream = new FileInputStream(file);
					Session mailSession = Session.getDefaultInstance(System.getProperties(),new Authenticator(){});
					MimeMessage msg = new MimeMessage(mailSession,inputStream);
					ReciveOneMailService<CotMail, CotMailAttach> oneMailService = new ReciveOneMailDefault<CotMail, CotMailAttach>(CotMail.class,CotMailAttach.class,false);
					CotMail cotMail = oneMailService.readAllMailInfo(msg, realPath+Constants.MAIL_ATTACH_SAVE_EML_PATH, null);
					Integer nodeId = null;
					if(nodeIdStr!=null&&!nodeIdStr.equals(""))
						nodeId = Integer.parseInt(nodeIdStr);
					this.getMailLocalService().saveMail(cotMail, nodeId);
					response.getWriter().write("Success");	
					inputStream.close();
					file.delete();
				}
			}
		} catch (Exception e) {
			logger.error("上传邮件附件失败",e);
			try {
				response.getWriter().write("Error");
			} catch (IOException e1) {
			}
		}finally{
			if(inputStream!=null)
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			if(importFile!=null)
				importFile.deleteOnExit();
		}
	}
}