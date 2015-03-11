package com.sail.cot.email.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.jason.core.Application;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.email.service.MailTreeService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class MailLocalUtil {
	private Logger logger = Log4WebUtil.getLogger(MailLocalUtil.class);
	private CotBaseDao cotBaseDao;
	private MailTreeService mailTreeService;
	
	public static String getProPath(){
		String path = ContextUtil.getProperty("remoteaddr.properties", Constants.MAIL_ATTACH_PATH_KEY);
		if(path!=null&&!path.trim().equals("")){
			path = path.trim();
			if(path.substring(path.length()-1).equals("/"))
				return path;
			else
				return path+"/";
		}
		path = MailLocalUtil.class.getResource("/").getPath();
		path = path.substring(1,path.length()-16);
		return path;
	}
	/**
	 * 设置本地邮件状态
	 * @param dbIDs
	 * @param mailType
	 * @return
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public boolean setType(List dbIDs,Integer mailType) throws DAOException{
		if(dbIDs==null||dbIDs.size()==0)
			return false;
		
		String hql = "update CotMailRecv obj set obj.mailType = :mailType," +
					"obj.addTime = :addTime where obj.id in (:ids)";
		Map map = new HashMap();
		map.put("mailType", mailType);
		map.put("addTime",new Date());
		map.put("ids", dbIDs);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		int result = this.getCotBaseDao().executeUpdate(queryInfo,map);
		return result > 0;
	}
	/**
	 * 保存发信类型邮件，并保存附件
	 * @param mailVO
	 * @param random
	 * @param sendType
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public void saveSendMail(CotMail cotMail,String random,int sendType) throws DAOException{
		// TODO:有使用
		logger.debug("保存发信类型邮件");
		HttpSession session = WebContextFactory.get().getSession();
		if(cotMail!= null){
			List<CotMail> records = new ArrayList<CotMail>();		    
			// 调用员工信息
		    CotEmps cotEmps = (CotEmps) session.getAttribute("emp");
		    // 添加到数据库时间
		    cotMail.setAddTime(new Date());
//		    // 邮件类型
		    cotMail.setMailType(sendType);
		    // 员工ID
		    cotMail.setEmpId(cotEmps.getId());
		    // 设置节点
		    Integer nodeId = null;
		    if(sendType==Constants.MAIL_LOCAL_TYPE_SEND)
		    	nodeId = this.getMailTreeService().findMailSendId(cotEmps.getId());
		    else if(sendType == Constants.MAIL_LOCAL_TYPE_DRAFT)
		    	nodeId = this.getMailTreeService().findMailDraftId(cotEmps.getId());
		    else
		    	nodeId = this.getMailTreeService().findMailCheckId(cotEmps.getId());
		    logger.debug("设置保存节点ID:"+nodeId);
		    cotMail.setNodeId(nodeId);
		    // 是否包含附件
		    Map map = (Map) session.getAttribute("map"+random);
		    if(map!=null&&map.size()>0){
		    	cotMail.setIsContainAttach(true);
		    }else {
		    	cotMail.setIsContainAttach(false);
			}
		    records.add(cotMail);
			this.getCotBaseDao().saveRecords(records);
			List<String> mailIds = new ArrayList<String>();
			mailIds.add(cotMail.getId());
				
			// 保存附件
			this.saveSendAttachs(cotMail,random);
		}
	}
	/**
	 * 保存附件到数据库，并释放保存信息
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	private void saveSendAttachs(CotMail cotMail,String random) throws DAOException{
		// TODO：有使用
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps cotEmps = (CotEmps) session.getAttribute("emp");
		Map<String,CotMailAttach> map=(Map<String, CotMailAttach>) session.getAttribute("map"+random);			    
		if(map!=null&&map.size()>0){
			List<CotMailAttach> attachList = new ArrayList<CotMailAttach>();
			CotMailAttach cotMailAttach;
	    	Set keySet=map.keySet();
	    	Iterator ikey=keySet.iterator();
	    	String baseUrl = Constants.MAIL_ATTACH_SAVE_SEND_PATH + "/" + RandomStringUtils.randomAlphanumeric(10)+"/";
	    	while(ikey.hasNext()){
	    		String key=(String) ikey.next();
	    		CotMailAttach attach= map.get(key);	
	    		cotMailAttach = new CotMailAttach();
	    		cotMailAttach.setName(attach.getName());
	    		cotMailAttach.setSize(attach.getSize());
	    		cotMailAttach.setMailId(cotMail.getId());
	    		cotMailAttach.setCustUrl(cotMail.getToUrl()+cotMail.getCcUrl()+cotMail.getBccUrl());
	    		cotMailAttach.setEmpId(cotMail.getEmpId());
	    		cotMailAttach.setMailType(cotMail.getMailType());
	    		cotMailAttach.setSendTime(cotMail.getSendTime());
	    		cotMailAttach.setAddTime(new Date());
	    		String saveFileName = cotEmps.getEmpsId()+"/"+RandomStringUtils.randomAlphanumeric(20);
	    		cotMailAttach.setUrl(baseUrl+saveFileName);
	    		SystemUtil.copyRealFile(MailLocalUtil.getProPath()+attach.getUrl(),MailLocalUtil.getProPath()+cotMailAttach.getUrl()); // 从临时目录转移到保存目录
	    		SystemUtil.deleteRealFile(MailLocalUtil.getProPath()+attach.getUrl());	// 删除临时目录文件
	    		attachList.add(cotMailAttach);
	    	}
	    	this.getCotBaseDao().saveRecords(attachList);
		}
		session.removeAttribute("map"+random);
		session.removeAttribute("random"+random);
		session.removeAttribute("nameIndex"+random); // 该参数只会一直增长与前台发送页面的附件面板一致
	}
	public static int connErrInfo(MessagingException e) {
		String msg = e.getMessage();
		String nextMsg = null;
		if(e.getNextException()!=null)
			nextMsg = e.getNextException().getMessage();
		if(msg!=null)
			if(msg.indexOf(Constants.MAIL_CONNECT_ERROR_POP_HOST)!=-1){
				if(nextMsg!=null)
					if(nextMsg.indexOf(Constants.MAIL_CONNECT_ERROR_POP_PORT)!=-1)
						return Constants.MAIL_CONNECT_ERROR_POP_PORT_STATUS;
				else
					return Constants.MAIL_CONNECT_ERROR_POP_HOST_STATUS;
			}else if(msg.indexOf(Constants.MAIL_CONNECT_ERROR_SMTP_HOST)!=-1){
				return Constants.MAIL_CONNECT_ERROR_SMTP_HOST_STATUS;
			}else if(msg.indexOf(Constants.MAIL_CONNECT_ERROR_SMTP_PORT)!=-1){
				return Constants.MAIL_CONNECT_ERROR_SMTP_PORT_STATUS;
			}else if(msg.indexOf(Constants.MAIL_CONNECT_ERROR_LOGON)!=-1){
				return Constants.MAIL_CONNECT_ERROR_LOGIN_STATUS;
			}else if(msg.indexOf(Constants.MAIL_CONNECT_ERROR_LOGIN_OR_PASSWORD)!=-1){
				return Constants.MAIL_CONNECT_ERROR_LOGIN_STATUS;
			}else if(msg.indexOf(Constants.MAIL_CONNECT_ERROR_LOGON_FAILED)!=-1){
				return Constants.MAIL_CONNECT_ERROR_LOGIN_FAILED_STATUS;
			}else if(msg.indexOf(Constants.MAIL_CONNECT_ERROR_LOGON_FAILED_FREQ)!=-1){
				return Constants.MAIL_CONNECT_ERROR_LOGIN_FAILED_FREQ_STATUS;
			}
				
		return Constants.MAIL_CONNECT_ERROR_STATUS;
	}
	public static int sendErrInfo(EmailException e){
		return 1;
	}
	public CotBaseDao getCotBaseDao() {
		if(cotBaseDao == null){
			cotBaseDao = (CotBaseDao)Application.getInstance().getContainer().getComponent("CotBaseDao");
		}
		return cotBaseDao;
	}
	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	public MailTreeService getMailTreeService() {
		if(mailTreeService == null){
			mailTreeService = (MailTreeService)Application.getInstance().getContainer().getComponent("MailTreeService");
		}
		return mailTreeService;
	}
	public void setMailTreeService(MailTreeService mailTreeService) {
		this.mailTreeService = mailTreeService;
	}
}
