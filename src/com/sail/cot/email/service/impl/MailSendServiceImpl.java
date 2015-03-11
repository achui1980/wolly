package com.sail.cot.email.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.jason.core.Application;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.email.service.MailSendService;
import com.sail.cot.email.service.MailTreeService;
import com.sail.cot.email.util.Constants;
import com.sail.cot.email.util.MailEntityConverUtil;
import com.sail.cot.email.util.MailLocalUtil;
import com.sail.cot.util.Log4WebUtil;
import com.zhao.mail.MailService;
import com.zhao.mail.SendMailService;
import com.zhao.mail.entity.MailCfg;
import com.zhao.mail.entity.MailPerson;
import com.zhao.mail.impl.MailServiceDefault;
import com.zhao.mail.impl.SendMailDefault;
import com.zhao.mail.util.CodeUtil;
/**
 * 发送邮件
 * @author zhao
 *
 */
public class MailSendServiceImpl implements MailSendService{
	private Logger logger = Log4WebUtil.getLogger(MailSendServiceImpl.class);
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}
	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	private MailLocalService mailLocalService;
	public MailLocalService getMailLocalService(){
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)Application.getInstance().getContainer().getComponent("MailLocalService");
		}
		return mailLocalService;
	}
	private MailTreeService mailTreeService;
	public MailTreeService getMailTreeService(){
		if(mailTreeService == null){
			mailTreeService = (MailTreeService)Application.getInstance().getContainer().getComponent("MailTreeService");
		}
		return mailTreeService;
	}
	
	
	/**
	 * 发送邮件
	 * @throws EmailException 
	 * @throws UnsupportedEncodingException 
	 * @throws EmailException 
	 * @throws UnsupportedEncodingException 
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public int addSendout(CotMail cotMail,String random){
		logger.debug("执行发送邮件并添加到数据库方法");
		try {
			
			HttpSession session = WebContextFactory.get().getSession();		
			CotEmps cotEmps = (CotEmps) session.getAttribute("emp");
			boolean isCheck = this.getMailLocalService().getIsMailCheck(cotMail, cotEmps.getId()); // 是否审核
//			boolean isCheck = false;
			// 初始化配置
			SendMailService<CotMail> sendMailService = initSendService(cotEmps);
			
			
			MailPerson sender = cotMail.getSender();
			if(sender==null||sender.getEmailUrl()==null||sender.getEmailUrl().trim().equals("")){ // 快速回复，回执
				MailPerson person = new MailPerson();
				person.setEmailUrl(cotEmps.getEmpsMail());
				cotMail.setSender(person);
			}
			if (cotMail.getIsNotification()==null)
				cotMail.setIsNotification(false);	// 是否需要回执
			Integer status = cotMail.getMailStatus();
			if(!isCheck){
				cotMail.setSendTime(new Date()); // 发送时间
				if(status!=null&&status==Constants.MAIL_LOCAL_STATUS_NOPARTINGSEND){// 群发，则取出to，分别发送
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_PARTINGSENDED);
					List<MailPerson> persons = cotMail.getTo(); // 取出保存发送人
					for (MailPerson mailPerson : persons) {
						List<MailPerson> to = new ArrayList<MailPerson>();
						to.add(mailPerson); // 分别设置收件人
						cotMail.setTo(to);
						// TODO:替换
						String name =mailPerson.getName();
						String body=cotMail.getBody();
						String str;
						int start;
						String tmp;
						if(body.indexOf("<span tage=\"sendMailToName\" style=\"display:none\">")==-1){
							start=body.indexOf("<span tage=\"sendMailToName\"");
							tmp =body.substring(start);
							int end=start +tmp.indexOf("</span>");
							str=body.substring(start+51,end);
						}else{
							start=body.indexOf("<span tage=\"sendMailToName\"");
							tmp =body.substring(start);
							int end=start +tmp.indexOf("</span>");
							str=body.substring(start+49,end);
						}
						//前面字符串
						String indexStr =cotMail.getBody().substring(0,start);
						
						tmp=tmp.replace(str, name);
						if(tmp.indexOf("style=\"display:none\"")==-1){
							
						}else{
							tmp=tmp.replace("style=\"display:none\"","style=\"display:inline\"");
						}
						//拼接字符串
						String value=indexStr +tmp;
						cotMail.setBody(value);
						this.sendMail(cotMail, sendMailService, random);
					}
					cotMail.setTo(persons); // 再回复保存的发送人
				}else{
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_SENDED);
					// TODO:替换 to 1
					this.sendMail(cotMail, sendMailService,random);  // 发送
				}
			}
			if(isCheck){
				if(status!=null&&status==Constants.MAIL_LOCAL_STATUS_NOPARTINGSEND){// 群发
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_PARTINGWAITONCHECK); // 群发待审核
				}else {
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_WAITONCHECK); // 待审核
				}
			}
			if(cotMail.getMailType()!=null)
				System.out.println("===mailType:"+cotMail.getMailType());
			
			this.updateOldMal(cotMail, isCheck); // 更新原邮件TAG
			// 删除原邮件，草稿，审核修改
			this.delOldMail(cotMail);
			
			if(isCheck){
				this.getMailLocalService().saveCheck(cotMail,random);  // 保存到待发送
				return Constants.MAIL_SEND_CHECK_STATUS;
			}else{
				this.getMailLocalService().saveSend(cotMail,random);  // 保存到发件箱
				return Constants.MAIL_SEND_NO_ERROR_STATUS;
			}
		}catch (EmailException e) {
			logger.error("发送邮件发生错误",e);
			return MailLocalUtil.sendErrInfo(e);
		}catch (Exception e) {
			logger.error("发送邮件发生错误",e);
			return Constants.MAIL_SEND_ERROR_STATUS;
		}
	}
	private void delOldMail(CotMail cotMail) {
		if(cotMail.getMsgId()!=null&&!cotMail.getMsgId().trim().equals("")){ // 如果cotMail.getMsgId()不为空
			CotMail oldCotMail = (CotMail) this.getCotBaseDao().getById(CotMail.class, cotMail.getMsgId());
			int mailType = oldCotMail.getMailType();
			// 修改错误审核，草稿
			if(mailType==Constants.MAIL_LOCAL_TYPE_CHECK||mailType==Constants.MAIL_LOCAL_TYPE_DRAFT){
				cotMail.setMailTag(oldCotMail.getMailTag());
				cotMail.setMsgId(oldCotMail.getMsgId());
				List<String> ids = new ArrayList<String>();
				ids.add(oldCotMail.getId());
				this.getMailLocalService().deleteMails(ids);
			}
		}
	}
	/**
	 * 更新原邮件TAG,审核原邮件，未发送新邮件加上原邮件数据库ID
	 * @param cotMail
	 * @param isCheck
	 */
	private void updateOldMal(CotMail cotMail, boolean isCheck) {
		if(cotMail.getMsgId()!=null&&!cotMail.getMsgId().trim().equals("")){
			try {
				String mailTag = cotMail.getMailTag();
				CotMail oldCotMail = (CotMail) this.getCotBaseDao()
						.getById(CotMail.class, cotMail.getMsgId());
				int mailType = oldCotMail.getMailType();
				if (mailType == Constants.MAIL_LOCAL_TYPE_DRAFT
						&&(mailTag==null||mailTag.trim().equals("")||!mailTag.equals(Constants.MAIL_TAG_FORWARD))) { // 草稿,并且可为空或不为转发
					mailTag = oldCotMail.getMailTag();
					if (mailTag != null&& mailTag.indexOf(Constants.MAIL_TAG_REPALY) != -1) { // 在草稿上发送，如果MSG_ID存在，则更新对应的邮件TAG
						if (oldCotMail.getMsgId() != null&& !oldCotMail.getMsgId().trim().equals("")) { // 不为空，则更新对应数据库邮件
							// 更新TAG
							setOldMailRTag(cotMail, isCheck, (CotMail) this.getCotBaseDao().getById(CotMail.class,oldCotMail.getMsgId()));
						}
					}
				} else if (mailType == Constants.MAIL_LOCAL_TYPE_INBOX) { // 收件箱，审核
					if (mailTag != null&& mailTag.indexOf(Constants.MAIL_TAG_REPALY) != -1) { // 在收件箱邮件上回复
						setOldMailRTag(cotMail, isCheck, oldCotMail); // 更新TAG
					}
				}
			} catch (Exception e) {
				// 可能存在原邮件已被删除
			}
		}
	}
	/**
	 * 初始化邮件配置
	 * @param cotEmps
	 * @return
	 */
	private SendMailService<CotMail> initSendService(CotEmps cotEmps) {
		MailCfg empMailCfg = MailEntityConverUtil.toMailCfg(cotEmps);
//			empMailCfg.setEmailPassword(PasswordEncrypt.decrypt(empMailCfg.getEmailPassword()));
		MailService mailService = new MailServiceDefault();
		mailService.setMailCfg(empMailCfg);
		mailService.initConfig();
		SendMailService<CotMail> sendMailService = new SendMailDefault<CotMail>();			
		sendMailService.setMailService(mailService);
		return sendMailService;
	}
	/**
	 * 给原邮件添加回复标识
	 * @param cotMail
	 * @param isCheck
	 * @param oldCotMail
	 */
	private void setOldMailRTag(CotMail cotMail, boolean isCheck,
			CotMail oldCotMail) {
		
		if(!isCheck){ // 更新收件箱邮件
			String oldMalTag = oldCotMail.getMailTag();
			// 不存在回复标识，则更新
			if(oldMalTag==null){// 为空，直接插入
				oldCotMail.setMailTag(Constants.MAIL_TAG_REPALY);
			}else if(oldMalTag.indexOf(Constants.MAIL_TAG_REPALY)==-1){// 不为空，加上回复标识 
				oldCotMail.setMailTag(oldMalTag+Constants.MAIL_TAG_REPALY);
			}
			List<CotMail> records = new ArrayList<CotMail>();
			records.add(oldCotMail);
			this.getCotBaseDao().saveOrUpdateRecords(records); // 更新
		}
	}
	
				
	/**
	 * 调用发送邮件方法
	 * @throws UnsupportedEncodingException 
	 * @throws MessagingException 
	 */
	@SuppressWarnings("unchecked")
	private void sendMail(CotMail cotMail,SendMailService sendMailService,String random) throws EmailException, UnsupportedEncodingException, MessagingException{
		logger.debug("执行发送邮件方法");
		List<EmailAttachment> list = new ArrayList<EmailAttachment>();
		List<CotMailAttach> attachList = cotMail.getAttachs();
		HttpSession session = WebContextFactory.get().getSession();	
		String path = MailLocalUtil.getProPath();
	    if(random!=null&&session.getAttribute("map"+random)!=null){
	    	Map<String,CotMailAttach> map=(Map<String, CotMailAttach>) session.getAttribute("map"+random);	    
	    	Set keySet=map.keySet();
	    	Iterator ikey=keySet.iterator();
	    	while(ikey.hasNext()){
	    		String filename=(String) ikey.next();
	    		CotMailAttach attach=map.get(filename);		    		
	    		EmailAttachment attachment = new EmailAttachment();
	    		attachment.setPath(path+attach.getUrl());
				attachment.setName(CodeUtil.enCodeB(attach.getName()));  
				list.add(attachment);	
	    	}
	    }else if(attachList!=null&&attachList.size()>0){
	    	for (CotMailAttach mailAttach : attachList) {
	    		EmailAttachment attachment = new EmailAttachment();
	    		attachment.setPath(path+mailAttach.getUrl());
				attachment.setName(CodeUtil.enCodeB(mailAttach.getName()));  
				list.add(attachment);	
			}
	    }
	    // TODO:MailService发送空的会报错，暂时在这里修改
	    if(cotMail.getBody()==null||cotMail.getBody().equals("")){
	    	cotMail.setBody(" ");
	    }
	    sendMailService.sendMail(cotMail, list,sendMailService.getMailService().getMailCfg());				
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailSendService#addDraft(com.sail.cot.domain.CotMail, java.lang.String)
	 */
	public int addDraft(CotMail cotMail, String random) {
		try {
			this.delOldMail(cotMail);
			this.getMailLocalService().saveDraft(cotMail, random);
			return Constants.MAIL_SEND_NO_ERROR_STATUS;
		} catch (Exception e) {
			return Constants.MAIL_SEND_ERROR_STATUS;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailSendService#checkSend(boolean, java.lang.String, java.lang.String)
	 */
	public int checkSend(boolean checked, String id, String msg) {
		CotMail cotMail = null;
		try {
			if (checked) {
				cotMail = this.getMailLocalService().readMailAllInfo(id);
				CotEmps cotEmps = (CotEmps) this.getCotBaseDao().getById(CotEmps.class, cotMail.getEmpId());
				SendMailService<CotMail> sendMailService = this.initSendService(cotEmps); // 初始化配置
				
				cotMail.setMailType(Constants.MAIL_LOCAL_TYPE_SEND);
				cotMail.setNodeId(this.getMailTreeService().findMailSendId(cotEmps.getId()));
				
				if(cotMail.getMailStatus()==Constants.MAIL_LOCAL_STATUS_WAITONCHECK) // 待审核
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_NOSEND); // 未发送
				else if(cotMail.getMailStatus()==Constants.MAIL_LOCAL_STATUS_PARTINGWAITONCHECK){ // 群发待审核
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_NOPARTINGSEND);  // 群发未已发送
				}else{
					return Constants.MAIL_SEND_ERROR_STATUS;
				}
				Integer status = cotMail.getMailStatus();
				cotMail.setSendTime(new Date()); // 发送时间
				if(status!=null&&status==Constants.MAIL_LOCAL_STATUS_NOPARTINGSEND){// 群发，则取出to，分别发送
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_PARTINGSENDED);
					List<MailPerson> persons = cotMail.getTo(); // 取出保存发送人
					for (MailPerson mailPerson : persons) {
						List<MailPerson> to = new ArrayList<MailPerson>();
						to.add(mailPerson); // 分别设置收件人
						cotMail.setTo(to);
						// TODO:替换 mailPerson.getName
						String name =mailPerson.getName();
						String body=cotMail.getBody();
						System.out.println(body);
						String str;
						int start;
						String value;
						if(body.indexOf("style=\"display:none\"")==-1){
							start=body.indexOf("<span tage=");
							String tmp =body.substring(start);
							int end=start +tmp.indexOf("</span>");
							str=body.substring(start+51,end);
							System.out.println(str);
							tmp=tmp.replace(str,name);
							String indexStr =cotMail.getBody().substring(0, start);
							value=indexStr +tmp;
						}else{
							start=body.indexOf("<span tage=");
							String tmp =body.substring(start);
							int end=start +tmp.indexOf("</span>");
							str=body.substring(start+49,end);
							System.out.println(str);
							tmp=tmp.replace(str,name);
							tmp=tmp.replace("style=\"display:none\"","style=\"display:inline\"");
							String indexStr =cotMail.getBody().substring(0, start);
					        value=indexStr +tmp;
						}
						cotMail.setBody(value);
						this.sendMail(cotMail, sendMailService, null);
					}
					cotMail.setTo(persons); // 再回复保存的发送人
				}else{
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_SENDED);
					// TODO:替换 to 1
					this.sendMail(cotMail, sendMailService,null);  // 发送
				}
				this.updateOldMal(cotMail, false);
			}else {
				cotMail = (CotMail) this.getCotBaseDao().getById(CotMail.class, id);
				
				if(cotMail.getMailStatus()==Constants.MAIL_LOCAL_STATUS_WAITONCHECK) // 待审核
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_CHECKNOTGO); // 审核不通过
				else if(cotMail.getMailStatus()==Constants.MAIL_LOCAL_STATUS_PARTINGWAITONCHECK){ // 群发待审核
					cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO);  // 群发审核不通过
				}else{
					return Constants.MAIL_SEND_ERROR_STATUS;
				}
			}
			cotMail.setErrMessage(msg);
			List<CotMail> records = new ArrayList<CotMail>();
			records.add(cotMail);
			this.getCotBaseDao().saveOrUpdateRecords(records);
			return Constants.MAIL_SEND_NO_ERROR_STATUS;
		} catch (Exception e) {
			logger.error("发送邮件发生错误",e);
//			System.out.println(e.getCause() instanceof );
			if(e instanceof EmailException&&cotMail.getMsgId()!=null&&!cotMail.getMsgId().trim().equals("")){
				try {
					if (cotMail.getMailStatus() == Constants.MAIL_LOCAL_STATUS_SENDED) // 
						cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_CHECKNOTGO); // 审核不通过
					else if (cotMail.getMailStatus() == Constants.MAIL_LOCAL_STATUS_PARTINGSENDED) { // 
						cotMail
								.setMailStatus(Constants.MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO); // 群发审核不通过
					} else {
						return Constants.MAIL_SEND_ERROR_STATUS;
					}
					cotMail.setErrMessage("邮件发送失败");
					cotMail.setMailType(Constants.MAIL_LOCAL_TYPE_CHECK);
					cotMail.setNodeId(this.getMailTreeService().findMailCheckId(cotMail.getEmpId()));
					List<CotMail> records = new ArrayList<CotMail>();
					records.add(cotMail);
					this.getCotBaseDao().saveOrUpdateRecords(records);
				} catch (Exception ex) {
				}
				return Constants.MAIL_SEND_NO_ERROR_STATUS;
			}else {
				return Constants.MAIL_SEND_ERROR_STATUS;
			}
		}
	}
}
	

