///**
// * 
// */
//package com.sail.cot.mail.service.impl;
//
//
//import java.io.File;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import javax.mail.MessagingException;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import org.apache.commons.mail.EmailAttachment;
//import org.apache.commons.mail.EmailException;
//import org.apache.log4j.Logger;
//import org.directwebremoting.WebContext;
//import org.directwebremoting.WebContextFactory;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import sun.misc.BASE64Encoder;
//import com.achui.mail.MailCfg;
//import com.achui.mail.MailObject;
//import com.achui.mail.SendMailServiceImpl;
//import com.jason.core.exception.DAOException;
//import com.sail.cot.dao.CotBaseDao;
//import com.sail.cot.domain.CotCustomer;
//import com.sail.cot.domain.CotEmps;
//import com.sail.cot.domain.CotMail;
//import com.sail.cot.domain.CotMailCfg;
//import com.sail.cot.mail.EasyMailExecutorPool;
//import com.sail.cot.mail.EasyMailService;
//import com.sail.cot.mail.EmailEntity;
//import com.sail.cot.mail.SendMailRunable;
//import com.sail.cot.mail.service.MailService;
//import com.sail.cot.query.QueryInfo;
//import com.sail.cot.util.Log4WebUtil;
//import com.sail.cot.util.SystemUtil;
//
///**
// * <p>Title: 工艺品管理系统</p>
// * <p>Description:</p>
// * <p>Copyright: Copyright (c) 2008</p>
// * <p>Company: </p>
// * <p>Create Time: Nov 5, 2008 11:06:48 AM </p>
// * <p>Class Name: MailServiceImpl.java </p>
// * @author achui
// *
// */
//public class MailServiceImpl extends SendMailServiceImpl implements MailService {
//
//	private CotBaseDao mailDao ;
//	private static HashMap<Object, Object> cfgMap = null;
//	private int count;
//	private EasyMailService easyMailService;
//	private EasyMailExecutorPool easyMailExecutorPool; 
//	private static Map mailMessageMap = new HashMap();
//	
//	private Logger logger = Log4WebUtil.getLogger(MailServiceImpl.class);
//	
//	public EasyMailService getEasyMailService() {
//		return easyMailService;
//	}
//
//	public void setEasyMailService(EasyMailService easyMailService) {
//		this.easyMailService = easyMailService;
//	}
//
//	public CotBaseDao getMailDao() {
//		return mailDao;
//	}
//
//	public void setMailDao(CotBaseDao mailDao) {
//		this.mailDao = mailDao;
//	}
//
//
//	public int getCount() {
//		if(count == 0)
//			count = 10;
//		return count;
//	}
//
//	public void setCount(int count) {
//		this.count = count;
//	}
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#getMailCfgMap()
//	 */
//	@SuppressWarnings("unchecked")
//	public HashMap<?, ?> getMailCfgMap() {
//		if(cfgMap == null)
//		{
//			cfgMap = new HashMap<Object, Object>();
//			List<CotMailCfg> res = this.getMailDao().getRecords("CotMailCfg");
//			for(CotMailCfg cfg : res)
//			{
//				cfgMap.put("SmtpHost", cfg.getDefaultHostSmtp());
//				cfgMap.put("Account", cfg.getDefaultAccount());
//				cfgMap.put("PassWord", cfg.getDefaultPwd());
//				String debug = cfg.getDefaultDebug() == 1 ? "true":"false";
//				cfgMap.put("IsDebug", String.valueOf(cfg.getDefaultDebug()));
//				cfgMap.put("Debug", debug);
//				String auth = cfg.getDefaultAuth() == 1 ? "true":"false";
//				cfgMap.put("IsAuth", String.valueOf(cfg.getDefaultAuth()));
//				cfgMap.put("Auth", auth);
//				cfgMap.put("Priv", cfg.getDefaultPriv());
//				cfgMap.put("SendCount", cfg.getDefaultCount());
//			}
//		}
//		return cfgMap;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#getMailCfgMapQuery()
//	 */
//	@SuppressWarnings("unchecked")
//	public HashMap<?, ?> getMailCfgMapQuery() {
//		 
//		cfgMap = new HashMap<Object, Object>();
//		List<CotMailCfg> res = this.getMailDao().getRecords("CotMailCfg");
//		for(CotMailCfg cfg : res)
//		{
//			cfgMap.put("SmtpHost", cfg.getDefaultHost());
//			cfgMap.put("Account", cfg.getDefaultAccount());
//			cfgMap.put("Password", cfg.getDefaultPwd());
//			String debug = cfg.getDefaultDebug() == 1 ? "true":"false";
//			cfgMap.put("IsDebug", String.valueOf(cfg.getDefaultDebug()));
//			cfgMap.put("Debug", debug);
//			String auth = cfg.getDefaultAuth() == 1 ? "true":"false";
//			cfgMap.put("Auth", auth);
//			cfgMap.put("IsAuth", String.valueOf(cfg.getDefaultAuth()));
//			cfgMap.put("Priv", cfg.getDefaultPriv());
//			cfgMap.put("SendCount", cfg.getDefaultCount());
//		}
//		return cfgMap;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMail(java.util.List)
//	 */
//	public void sendSimpleMailList(List<CotMail> mailList) {
//		for(CotMail mail : mailList)
//		{
//			this.sendSimpleMail(mail);
//		}	
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMail(com.sail.cot.domain.CotMail)
//	 */
//	 
//	@SuppressWarnings("deprecation")
//	public Boolean sendSimpleMail_old(CotMail mail) {
//		EmailEntity entity = new EmailEntity( new SimpleMailMessage());
//		//获取邮件基础配置
//		HashMap<?, ?> defaultCfg = this.getMailCfgMap();
//		
//		/*Map<String,String> attatchment = new HashMap<String,String>();
//		//获取附件文件名
//		String fileName = SystemUtil.getFileName(mail.getMailPath());
//		String filePath = mail.getMailPath();
//		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//		filePath = request.getRealPath("mailfolder") + File.separator + filePath;
//		attatchment.put(fileName, filePath);
//		
//		//设置附件
//		entity.setAttachment(attatchment);*/
//		//设置鉴定权信息
//		entity.setAuth(Boolean.parseBoolean(String.valueOf(defaultCfg.get("Auth"))));
//		//设置调试信息
//		entity.setDebug(Boolean.parseBoolean(String.valueOf(defaultCfg.get("Debug"))));
//		//设置邮件服务器地址
//		entity.setMailhost(defaultCfg.get("SmtpHost").toString());
//		if(mail.getMailHost() != null)
//			entity.setMailhost(mail.getMailHost());
//		//设置邮件发送帐户
//		entity.setUsername(String.valueOf(defaultCfg.get("Account")));
//		if(mail.getEmpMailAccount() != null)
//			entity.setUsername(mail.getEmpMailAccount());
//		//设置发送帐户密码
//		entity.setPassword(String.valueOf(defaultCfg.get("Password")));
//		if(mail.getEmpMailPwd() != null)
//			entity.setPassword(mail.getEmpMailPwd());
//		//设置收件人地址
//		entity.setTo(mail.getCustEmail());
//		//设置发件件地址
//		entity.setFrom(mail.getEmpEmail());
//		//设置邮件内容
//		entity.setText(mail.getMailContent());
//		//设置邮件主题
//		entity.setSubject(mail.getMailSubject());
//		
//		entity.setMailObj(mail);
//		
//		entity.setMailDao(mailDao);
//		
//		try {
//			this.getEasyMailService().sendMessage(entity);
//			this.updateMail(mail, 4, "Success"); //状态4：正在发送
//			return true;
//		} catch (MessagingException e) {
//			 
//			logger.error("邮件发送失败", e);
//			this.updateMail(mail, 1, e.getMessage());//状态 1：发送失败
//			return false;
//		}
//			
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMimeMail(java.util.List)
//	 */
//	public void sendMimeMailList(List<CotMail> mailList) {
//		for(CotMail mail : mailList)
//		{
//			this.sendMimeMail(mail);
//		}	
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMimeMail(com.sail.cot.domain.vo.CotMailObject)
//	 */
//	public Boolean sendMimeMail_old(CotMail mail) {
//		JavaMailSender mailSender = new JavaMailSenderImpl();
//		EmailEntity entity = new EmailEntity( mailSender.createMimeMessage());
//		//获取邮件基础配置
//		HashMap<?, ?> defaultCfg = this.getMailCfgMap();
//		
//		Map<String,String> attatchment = new HashMap<String,String>();
//		//获取附件文件名
//		//String fileName = SystemUtil.getFileName(mail.getMailPath());
//		//String filePath = mail.getMailPath();
//		//String strPath = String.valueOf(System.getProperty("webapp.root"));
//		
//		//HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//		//filePath = strPath + "\\mailfolder\\" + filePath;
//		//attatchment.put(fileName, filePath);
//		
//		//设置附件
//		entity.setAttachment(attatchment);
//		//设置鉴定权信息
//		entity.setAuth(Boolean.parseBoolean(String.valueOf(defaultCfg.get("Auth"))));
//		//设置调试信息
//		entity.setDebug(Boolean.parseBoolean(String.valueOf(defaultCfg.get("Debug"))));
//		//设置邮件服务器地址
//		entity.setMailhost(defaultCfg.get("SmtpHost").toString());
//		if(mail.getMailHost() != null)
//			entity.setMailhost(mail.getMailHost());
//		//设置邮件发送帐户
//		entity.setUsername(String.valueOf(defaultCfg.get("Account")));
//		if(mail.getEmpMailAccount() != null)
//			entity.setUsername(mail.getEmpMailAccount());
//		//设置发送帐户密码
//		entity.setPassword(String.valueOf(defaultCfg.get("Password")));
//		if(mail.getEmpMailPwd() != null)
//			entity.setPassword(mail.getEmpMailPwd());
//		//设置收件人地址
//		entity.setTo(mail.getCustEmail());
//		//设置发件件地址
//		entity.setFrom(mail.getEmpEmail());
//		//设置邮件内容
//		entity.setText(mail.getMailContent());
//		//设置邮件主题
//		entity.setSubject(mail.getMailSubject());
//		
//		entity.setMailObj(mail);
//		
//		entity.setMailDao(mailDao);
//		//获取状态不是发送成功的邮件 0：新增 1：发送失败 3：发送成功 4：正在发送
//		try {
//			this.getEasyMailService().sendMimeMessage(entity);
//			this.updateMail(mail, 4, "Success"); //状态4：正在发送
//			return true;
//		} catch (Exception e) {
//			 
//			this.updateMail(mail, 1, e.getMessage());//状态 1：发送失败
//			e.printStackTrace();
//			return false;
//		}
//			
//		
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#getMailList(int, int)
//	 */
//	@SuppressWarnings("unchecked")
//	public List getMailListNotByStatus(QueryInfo queryInfo,int status) {
//		 
//		String sql = " from CotMail obj  where 1=1  ";
//		String sqlWhere = " and obj.mailStatus <  "+status; 	    //获取状态不是发送成功的邮件 0：新增 1：发送失败 3：发送成功 4：正在发送
//		sqlWhere += " and obj.mailCount <= obj.sendCount"; 			//获取重发次数尚未达到指定发送次数的记录
//		String sqlOrder = " order by obj.mailPriv desc ";  			//按照发送优先级取数据
//		queryInfo.setSelectString(sql);
//		queryInfo.setQueryString(sqlWhere);
//		queryInfo.setOrderString(sqlOrder);
//		queryInfo.setCountOnEachPage(this.getCount());		//每次发送记录数；
//		queryInfo.setStartIndex(0);							//从第0条开始取；
//		List<CotMail> mailList = new ArrayList<CotMail>();
//		try {
//			mailList = this.getMailDao().findRecords(queryInfo);
//			this.updateMailList(mailList,4);//设置为正在发送状态
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//		}
//		return mailList;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#updateMail(java.util.List, int)
//	 */
//	public void updateMailList(List<CotMail> mailList, int status) {
//		 
//		
//		List<CotMail> res = new ArrayList<CotMail>();
//		for(CotMail mail : mailList)
//		{
//			mail.setMailStatus(status);
//			mail.setMailCount(mail.getMailCount() + 1);//当发送一次			
//			mail.setSendTime(new Timestamp(System.currentTimeMillis()));
//			res.add(mail);
//		}
//		this.getMailDao().updateRecords(res);
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#updateMail(java.util.List, long, java.lang.String)
//	 */
//	public void updateMail(CotMail mail,int status,String errMsg) {
//		
//		mail.setMailStatus(status);
//		mail.setMailErrMessage(errMsg);
//		mail.setMailCount(mail.getMailCount() + 1);//当发送一次			
//		mail.setSendTime(new Timestamp(System.currentTimeMillis()));
//		this.getMailDao().update(mail);
//		
//	}
//	
//	public String saveMail(CotMail mail,int status,String errMsg) {
//		
//		List<CotMail> res = new ArrayList<CotMail>();		
//	    CotMail cotMail = new CotMail();
//		cotMail.setAddTime(new Timestamp(System.currentTimeMillis()));// 添加时间
//		// 客户信息
//		cotMail.setCustId(mail.getCustId());
//		cotMail.setCustName(mail.getCustName());
//		cotMail.setCustEmail(mail.getCustEmail());
//		// 操作人信息
//		cotMail.setEmpId(mail.getEmpId());
//		cotMail.setEmpName(mail.getEmpName());
//		cotMail.setEmpEmail(mail.getEmpEmail());
//		cotMail.setEmpMailAccount(mail.getEmpMailAccount());
//		cotMail.setEmpMailPwd(mail.getEmpMailPwd());
//		// 获得邮件基本配置
//		HashMap<?, ?> defaultmap = this.getMailCfgMap();
//		cotMail.setMailHost(mail.getMailHost());// 主机地址
//		cotMail.setMailDebug(Integer.parseInt(defaultmap.get("IsDebug").toString()));// 调试
//		cotMail.setMailAuth(Integer.parseInt(defaultmap.get("IsAuth").toString()));// 是否健全
//		cotMail.setMailPriv((Integer) defaultmap.get("Priv"));// 优先级
//		cotMail.setSendCount(Integer.parseInt(defaultmap.get("SendCount").toString()));//发送次数
//		cotMail.setMailIsSslsmtp(mail.getMailIsSslsmtp());
//		cotMail.setMailSmtpPort(mail.getMailSmtpPort());
//		//详细配置
//		cotMail.setMailContent(mail.getMailContent());// 邮件内容 
//		cotMail.setMailSubject(mail.getMailSubject());// 主题
//		cotMail.setMailCount(new Integer(0));// 已发送次数
//		cotMail.setMailStatus(status);// 正在发生状态
//		//String mailPath = mail.getMailPath();//附件路径		
//		cotMail.setSendTime(new Timestamp(System.currentTimeMillis()));
//		//cotMail.setMailPath(mailPath);
//		
//		this.getMailDao().create(cotMail);
//		return cotMail.getId();
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMail(boolean)
//	 */
//	@SuppressWarnings("unchecked")
//	public void sendMail(boolean isAttatchment) {
//		QueryInfo queryInfo = new QueryInfo();
//		List<CotMail> mailList = this.getMailListNotByStatus(queryInfo, 3);//发送失败,待发送
//		if(isAttatchment)
//			this.sendMimeMailList(mailList);
//		else
//			this.sendSimpleMailList(mailList);
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#getMailListByStatus(int)
//	 */
//	public List<?> getMailListByStatus(int status) {
//		String sql = "from CotMail obj where 1=1";
//			   sql += " and obj.mailStatus="+status;
//	    
//		return this.getMailDao().find(sql);
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#saveMailRecords(java.util.List)
//	 */
//	public void saveMailRecords(List<CotMail> mailList) {
//		try {
//			this.getMailDao().saveRecords(mailList);
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//		}
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#deleteRecords(java.util.List, long, boolean)
//	 */
//	public void deleteRecords(List<CotMail> mailList, int status,
//			boolean delAll) {
//		List<String> ids = new ArrayList<String>();
//		//是否全部删除
//		for(CotMail mail : mailList)
//		{
//			if(delAll)
//			{
//				ids.add(mail.getId());
//			}
//			else
//			{
//				if(mail.getMailStatus() != status) continue;
//				ids.add(mail.getId());
//			}
//		}	
//		try {
//			this.getMailDao().deleteRecordByIds(ids, "CotMail");
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//		}
//	}
//	//
//	
//	//获取邮件列表
//	public List<?> getMaiList() {
//		 
//		return this.getMailDao().getRecords("CotMail");
//	}
//	//删除邮件
//	public void deleteMail(List<CotMail> mailList) {
//		
//		List<String> ids = new ArrayList<String>();
//		for (int i = 0; i < mailList.size(); i++) {
//			CotMail cotMail = (CotMail) mailList.get(i);
//			String id = cotMail.getId();
//			this.delMailFile(id);
//			ids.add(id);
//		}
//		try {
//			this.getMailDao().deleteRecordByIds(ids, "CotMail");
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//			logger.error("删除邮件信息异常",e);
//		}
//	}
//	//根据id获取邮件信息
//	public CotMail getMailById(Integer Id) {
//		 
//		return (CotMail) this.getMailDao().getById(CotMail.class, Id);
//	}
//	
//	public List<?> getList(QueryInfo queryInfo,int status) {
//		 
//		try {
//			String strWhere = queryInfo.getQueryString();
//			strWhere += " and obj.mailStatus="+status;
//			queryInfo.setQueryString(strWhere);
//			return this.getMailDao().findRecords(queryInfo);
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//		}
//		return  null;
//	}
//	
//	public int getRecordCount(QueryInfo queryInfo,int status) {
//		 
//		try {
//			String strWhere = queryInfo.getCountQuery();
//			strWhere += " and obj.mailStatus="+status;
//			queryInfo.setCountQuery(strWhere);
//			return this.getMailDao().getRecordsCount(queryInfo);
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//		}
//		return  0;
//	}
//
//	public int getRecordCount(QueryInfo queryInfo) {
//		 
//		try {
//			return this.getMailDao().getRecordsCount(queryInfo);
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//		}
//		return 0;
//	}
//
//	public List<?> getList(QueryInfo queryInfo) {
//		 
//		try {
//			return this.getMailDao().findRecords(queryInfo);
//		} catch (DAOException e) {
//			 
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public String getFileName(CotMail cotMail) {
//		//String fileName = SystemUtil.getFileName(cotMail.getMailPath());
//		//return fileName;
//		return null;
//	}
//
//	 
//   //添加并发送邮件
//   public void addAndSendMail(CotMail mail) {
//	    Boolean success = null;
//	    String msg = null;
//		//String mailPath = mail.getMailPath();//附件路径
//		if(true){
//		//if(mailPath!=null&&mailPath!=""){
//			//mail.setMailPath(mailPath);//附件路径
//			//this.getMailDao().create(mail);
//			// 发送单封带附件的邮件
//			success = this.sendMimeMail(mail);
//			
//		}else{
//			//this.getMailDao().create(mail);
//			// 发送单封简单的邮件
//			success = this.sendSimpleMail(mail);
//		}
//		if(success != null && success.booleanValue())
//			msg = "主题："+mail.getMailSubject()+",邮件发送完成";
//		else
//			msg = "主题："+mail.getMailSubject()+",邮件发送失败，可能服务器配置错误，或用户名密码错误";
//		this.mailMessageMap.put(mail.getEmpId()+"_"+mail.getMailSubject(), msg);
//	}
//   //群发邮件
//   public List<CotMail> saveSendMail(CotMail mail,List<CotCustomer> ids) {
//	   List<CotMail> res = new ArrayList<CotMail>();
//	    
//		for (int i = 0; i < ids.size(); i++) {
//			CotMail cotMail = new CotMail();
//			CotCustomer cotCustomer = (CotCustomer) ids.get(i);
//			Integer id = cotCustomer.getId();
//			CotCustomer cotCustomerOld = (CotCustomer) this.getMailDao().getById(CotCustomer.class, id);
//		
//			
//			cotMail.setAddTime(new Timestamp(System.currentTimeMillis()));// 添加时间
//			// 客户信息
//			cotMail.setCustId(id);
//			cotMail.setCustName(cotCustomerOld.getCustomerShortName());
//			cotMail.setCustEmail(cotCustomerOld.getCustomerEmail());
//			// 操作人信息
//			cotMail.setEmpId(mail.getEmpId());
//			cotMail.setEmpName(mail.getEmpName());
//			cotMail.setEmpEmail(mail.getEmpEmail());
//			cotMail.setEmpMailAccount(mail.getEmpMailAccount());
//			cotMail.setEmpMailPwd(mail.getEmpMailPwd());
//			// 获得邮件基本配置
//			HashMap<?, ?> defaultmap = this.getMailCfgMap();
//			cotMail.setMailHost(mail.getMailHost());// 主机地址
//			cotMail.setMailDebug(Integer.parseInt(defaultmap.get("IsDebug").toString()));// 调试
//			cotMail.setMailAuth(Integer.parseInt(defaultmap.get("IsAuth").toString()));// 是否健全
//			cotMail.setMailPriv((Integer) defaultmap.get("Priv"));// 优先级
//			cotMail.setSendCount(Integer.parseInt(defaultmap.get("SendCount").toString()));//发送次数
//			cotMail.setMailIsSslsmtp(mail.getMailIsSslsmtp());
//			cotMail.setMailSmtpPort(mail.getMailSmtpPort());
//			//详细配置
//			cotMail.setMailContent(mail.getMailContent());// 邮件内容 
//			cotMail.setMailSubject(mail.getMailSubject());// 主题
//			cotMail.setMailCount(new Integer(0));// 已发送次数
//			cotMail.setMailStatus(new Integer(4));// 状态
////			//String mailPath = mail.getMailPath();//附件路径
////			
////			if(mailPath!=null&&!mailPath.equals("")){
////				cotMail.setMailPath(mailPath);//附件路径
////				//this.getMailDao().create(cotMail);
////				// 发送带附件的邮件
////				//this.groudSendMimeMail(cotMail);
////			}else{
////				this.getMailDao().create(cotMail);
////				// 发送简单的邮件
////				//this.groudSendSimpleMail(cotMail);
////			}
//			res.add(cotMail);
//		}
//		this.saveMailRecords(res);
//		return res;
//	}   
//   //群发邮件
//   public void addSendMail(List<CotMail> mailList) {
//	   List<CotMail> res = new ArrayList<CotMail>();
//	   Boolean success = null;
//	    String msg = null;
//		for (CotMail cotMail : mailList) {
//			
////			String mailPath = cotMail.getMailPath();
////			if(mailPath!=null&&!mailPath.equals("")){
////				//cotMail.setMailPath(mailPath);//附件路径
////				//this.getMailDao().create(cotMail);
////				// 发送带附件的邮件
////				success = this.groudSendMimeMail(cotMail);
////			}else{
////				//this.getMailDao().create(cotMail);
////				// 发送简单的邮件
////				success = this.groudSendSimpleMail(cotMail);
////			}
////			if(success != null && success.booleanValue())
////				msg = "邮件群发("+cotMail.getCustEmail()+")发送完成";
////			else
////				msg = "邮件发送失败，可能服务器配置错误，或用户名密码错误或用户邮箱("+cotMail.getCustEmail()+")错误";
////			this.mailMessageMap.put(cotMail.getEmpId()+"_"+cotMail.getCustEmail(), msg);
//		}
//	}   
//   //修改并发送邮件
//   public void updateAndSendMail(CotMail mail) {
//	    mail.setAddTime(new Timestamp(System.currentTimeMillis()));// 添加时间
//	    Boolean success = null;
////	    String msg = null;
////		String mailPath = mail.getMailPath();//附件路径
////		//this.updateMail(mail, 4, "Success"); //4：正在发发送
////		if(mailPath!=null&&mailPath!=""){
////			// 发送单封带附件的邮件
////			success = this.sendMimeMail(mail);
////			//this.getMailDao().update(mail);
////		}else{
////			// 发送单封简单的邮件
////			success = this.sendSimpleMail(mail);
////			//this.getMailDao().update(mail);
////		}
////		if(success != null && success.booleanValue())
////			msg = "主题："+mail.getMailSubject()+",邮件发送完成";
////		else
////			msg = "主题："+mail.getMailSubject()+",邮件发送失败，可能服务器配置错误，或用户名密码错误";
////		this.mailMessageMap.put(mail.getEmpId()+"_"+mail.getMailSubject(), msg);
//	}
//
//    //通过发件人（员工）id获取发件人编号（工号）
//	public String getEmpsIdById(Integer id) {
//		List<?> list = this.getMailDao().find("select obj.empsId from CotEmps obj where obj.id ="+id);
//		if(list.size()>0){
//			return (String) list.get(0);
//		}
//		return null;
//	}
//   
//	//通过id删除邮件附件
//	public Boolean delMailFile(String id){
//		// 获得tomcat路径
//		String classPath = MailServiceImpl.class.getResource("/") .toString();
//		String systemPath = classPath.substring(6, classPath.length() - 16);
//		
//		List<?> list = this.getMailDao().find("from CotMail obj where obj.id ="+id);
//		if(list.size()>0){
//			CotMail cotMail = (CotMail) list.get(0);
////			String mailPath = cotMail.getMailPath();
////			if(mailPath==null){
////				return false;
////			}
////			String filePath = systemPath+"mailfolder/"+mailPath;
////			System.out.println(filePath);
////			try {
////				File delFile = new File(filePath);
////				delFile.delete();
////			} catch (Exception e) {
////				System.out.println("删除附件操作出错");
////				e.printStackTrace();
////				return false;
////			}
////			cotMail.setMailPath(null);
//			this.getMailDao().update(cotMail);
//			return true;
//		}
//		return false;
//	}
//	
//	//通过附件路径删除附件
//	public void delMailFileByMailPath(String mailPath){
//		// 获得tomcat路径
//		String classPath = MailServiceImpl.class.getResource("/") .toString();
//		String systemPath = classPath.substring(6, classPath.length() - 16);
//		
//		String filePath = systemPath+"mailfolder/"+mailPath;
//		System.out.println(filePath);
//		try {
//			File delFile = new File(filePath);
//			delFile.delete();
//		} catch (Exception e) {
//			System.out.println("删除附件操作出错");
//			e.printStackTrace();
//		}
//	}
//	
//	//通过id获取邮件发送状态
//	public Long getMailStatusById(Integer id){
//		List<?> list = this.getMailDao().find("select obj.mailStatus from CotMail obj where obj.id="+id);
//		if(list.size()>0){
//			Long mailStatus = (Long) list.get(0);
//			return mailStatus;
//		}
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMimeMail(com.sail.cot.domain.CotMail)
//	 */
//	public Boolean sendMimeMail(CotMail mail) {
//		MailCfg cfg = new MailCfg();
//		
//		HashMap<?, ?> defaultCfg = this.getMailCfgMap();
//		
//		//设置邮件服务器地址
//		cfg.setHostname(defaultCfg.get("SmtpHost").toString());
//		if(mail.getMailHost() != null)
//			cfg.setHostname(mail.getMailHost());
//		//设置邮件发送帐户
//		cfg.setUsername(String.valueOf(defaultCfg.get("Account")));
//		if(mail.getEmpMailAccount() != null)
//			cfg.setUsername(mail.getEmpMailAccount());
//		//设置发送帐户密码
//		cfg.setPwd(String.valueOf(defaultCfg.get("Password")));
//		if(mail.getEmpMailPwd() != null)
//			cfg.setPwd(mail.getEmpMailPwd());
//		if(mail.getMailIsSslsmtp() != null && mail.getMailIsSslsmtp() == 1)
//			cfg.setSSLSmtp(true);
//		if(mail.getMailSmtpPort() != null)
//			cfg.setSmtpPort(mail.getMailSmtpPort().toString());
//		//设置邮件体
//		MailObject msg = new MailObject();
//		//设置收件人地址
//		ArrayList<InternetAddress> toList = new ArrayList<InternetAddress>();
//		InternetAddress address = null;
//		try {
//			address = new InternetAddress(mail.getCustEmail());
//		} catch (AddressException e1) {
//			 
//			e1.printStackTrace();
//		}
//		toList.add(address);
//		msg.setTo(toList);
//		//设置发件件地址
//		msg.setFrom(mail.getEmpEmail());
//		//设置邮件内容
//		msg.setMsg(mail.getMailContent());
//		//设置邮件主题
//		msg.setSubject(mail.getMailSubject());
//		
//		
//		//获取附件文件名
//		//String fileName = SystemUtil.getFileName(mail.getMailPath());
//		//String filePath = mail.getMailPath();
//		String strPath = String.valueOf(System.getProperty("webapp.root"));
//		//filePath = strPath + "\\mailfolder\\" + filePath;
//		//设置邮件附件
////		EmailAttachment attachment = new EmailAttachment();
////		attachment.setPath(filePath);
////		attachment.setDisposition(EmailAttachment.ATTACHMENT);
////		attachment.setDescription(fileName);
////		BASE64Encoder enc = new BASE64Encoder();
////		String tt = "=?GBK?B?"+enc.encode(fileName.getBytes())+"?=";
////		attachment.setName("=?GBK?B?"+enc.encode(fileName.getBytes())+"?=");
////		System.out.println("file:"+tt);
////		//attachment.setName(fileName);
////		try {
////			
////			this.sendMultiMail(cfg, msg, attachment);
////			this.updateMail(mail, 3, "Success"); //状态3：发送成功
////			return true;
////		} catch (EmailException e) {
////			logger.error("邮件发送失败", e);
////			this.updateMail(mail, 1, e.getMessage());//状态 1：发送失败
////		}
//		return false;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendSimpleMail(com.sail.cot.domain.CotMail)
//	 */
//	public Boolean sendSimpleMail(CotMail mail) {
//		MailCfg cfg = new MailCfg();
//		
//		HashMap<?, ?> defaultCfg = this.getMailCfgMap();
//		
//		//设置邮件服务器地址
//		cfg.setHostname(defaultCfg.get("SmtpHost").toString());
//		if(mail.getMailHost() != null)
//			cfg.setHostname(mail.getMailHost());
//		//设置邮件发送帐户
//		cfg.setUsername(String.valueOf(defaultCfg.get("Account")));
//		if(mail.getEmpMailAccount() != null)
//			cfg.setUsername(mail.getEmpMailAccount());
//		//设置发送帐户密码
//		cfg.setPwd(String.valueOf(defaultCfg.get("Password")));
//		if(mail.getEmpMailPwd() != null)
//			cfg.setPwd(mail.getEmpMailPwd());
//		if(mail.getMailIsSslsmtp() == 1)
//			cfg.setSSLSmtp(true);
//		if(mail.getMailSmtpPort() != null)
//			cfg.setSmtpPort(mail.getMailSmtpPort().toString());
//		//设置邮件体
//		MailObject msg = new MailObject();
//		//设置收件人地址
//		ArrayList<InternetAddress> toList = new ArrayList<InternetAddress>();
//		InternetAddress address = null;
//		try {
//			address = new InternetAddress(mail.getCustEmail());
//		} catch (AddressException e1) {
//			 
//			e1.printStackTrace();
//		}
//		toList.add(address);
//		msg.setTo(toList);
//		//设置发件件地址
//		msg.setFrom(mail.getEmpEmail());
//		//设置邮件内容
//		msg.setMsg(mail.getMailContent());
//		//设置邮件主题
//		msg.setSubject(mail.getMailSubject());
//		try {
//			this.sendSimpleMail(cfg, msg);
//			this.updateMail(mail, 3, "Success"); //状态3：正在发送
//			return true;
//		} catch (EmailException e) {
//			logger.error("邮件发送失败", e);
//			this.updateMail(mail, 1, e.getMessage());//状态 1：发送失败
//		}
//		return false;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendSimpleMail(com.sail.cot.domain.CotMail)
//	 */
//	public Boolean groudSendSimpleMail(CotMail mail) {
//		MailCfg cfg = new MailCfg();
//		
//		HashMap<?, ?> defaultCfg = this.getMailCfgMap();
//		
//		//设置邮件服务器地址
//		cfg.setHostname(defaultCfg.get("SmtpHost").toString());
//		if(mail.getMailHost() != null)
//			cfg.setHostname(mail.getMailHost());
//		//设置邮件发送帐户
//		cfg.setUsername(String.valueOf(defaultCfg.get("Account")));
//		if(mail.getEmpMailAccount() != null)
//			cfg.setUsername(mail.getEmpMailAccount());
//		//设置发送帐户密码
//		cfg.setPwd(String.valueOf(defaultCfg.get("Password")));
//		if(mail.getEmpMailPwd() != null)
//			cfg.setPwd(mail.getEmpMailPwd());
//		if(mail.getMailIsSslsmtp() == 1)
//			cfg.setSSLSmtp(true);
//		if(mail.getMailSmtpPort() != null)
//			cfg.setSmtpPort(mail.getMailSmtpPort().toString());
//		//设置邮件体
//		MailObject msg = new MailObject();
//		//设置收件人地址
//		ArrayList<InternetAddress> toList = new ArrayList<InternetAddress>();
//		InternetAddress address = null;
//		try {
//			address = new InternetAddress(mail.getCustEmail());
//		} catch (AddressException e1) {
//			 
//			e1.printStackTrace();
//		}
//		toList.add(address);
//		msg.setTo(toList);
//		//设置发件件地址
//		msg.setFrom(mail.getEmpEmail());
//		//设置邮件内容
//		msg.setMsg(mail.getMailContent());
//		//设置邮件主题
//		msg.setSubject(mail.getMailSubject());
//		try {
//			this.sendSimpleMail(cfg, msg);
//			this.updateMail(mail, 3, "Success"); //状态3：正在发送
//			return true;
//		} catch (EmailException e) {
//			logger.error("邮件发送失败", e);
//			this.updateMail(mail, 1, e.getMessage());//状态 1：发送失败
//		}
//		return false;
//	}
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMimeMail(com.sail.cot.domain.CotMail)
//	 */
//	public Boolean groudSendMimeMail(CotMail mail) {
//		MailCfg cfg = new MailCfg();
//		
//		HashMap<?, ?> defaultCfg = this.getMailCfgMap();
//		
//		//设置邮件服务器地址
//		cfg.setHostname(defaultCfg.get("SmtpHost").toString());
//		if(mail.getMailHost() != null)
//			cfg.setHostname(mail.getMailHost());
//		//设置邮件发送帐户
//		cfg.setUsername(String.valueOf(defaultCfg.get("Account")));
//		if(mail.getEmpMailAccount() != null)
//			cfg.setUsername(mail.getEmpMailAccount());
//		//设置发送帐户密码
//		cfg.setPwd(String.valueOf(defaultCfg.get("Password")));
//		if(mail.getEmpMailPwd() != null)
//			cfg.setPwd(mail.getEmpMailPwd());
//		if(mail.getMailIsSslsmtp() == 1)
//			cfg.setSSLSmtp(true);
//		if(mail.getMailSmtpPort() != null)
//			cfg.setSmtpPort(mail.getMailSmtpPort().toString());
//		//设置邮件体
//		MailObject msg = new MailObject();
//		//设置收件人地址
//		ArrayList<InternetAddress> toList = new ArrayList<InternetAddress>();
//		InternetAddress address = null;
//		try {
//			address = new InternetAddress(mail.getCustEmail());
//		} catch (AddressException e1) {
//			 
//			e1.printStackTrace();
//		}
//		toList.add(address);
//		msg.setTo(toList);
//		//设置发件件地址
//		msg.setFrom(mail.getEmpEmail());
//		//设置邮件内容
//		msg.setMsg(mail.getMailContent());
//		//设置邮件主题
//		msg.setSubject(mail.getMailSubject());
//		
//		
////		//获取附件文件名
////		String fileName = SystemUtil.getFileName(mail.getMailPath());
////		String filePath = mail.getMailPath();
////		String strPath = String.valueOf(System.getProperty("webapp.root"));
////		filePath = strPath + "\\mailfolder\\" + filePath;
////		//设置邮件附件
////		EmailAttachment attachment = new EmailAttachment();
////		attachment.setPath(filePath);
////		attachment.setDisposition(EmailAttachment.ATTACHMENT);
////		attachment.setDescription(fileName);
////		BASE64Encoder enc = new BASE64Encoder();     
////        //this.getName().getBytes()使用的是系统缺省的编码处理,这里是GBK     
////		attachment.setName("=?GBK?B?"+enc.encode(fileName.getBytes())+"?=");     
////		//attachment.setName(fileName);
////		try {
////			this.sendMultiMail(cfg, msg, attachment);
////			this.updateMail(mail, 3, "Success"); //状态3：发送成功
////			return true;
////		} catch (EmailException e) {
////			logger.error("邮件发送失败", e);
////			this.updateMail(mail, 1, e.getMessage());//状态 1：发送失败
////		}
//		return false;
//	}	
//	//直接保存邮件
//	public void saveOrUpdateMail(CotMail mail) {
//		
//		List<CotMail> res = new ArrayList<CotMail>();
//		
//		mail.setMailStatus(0);
//		mail.setMailErrMessage("New");
//		mail.setMailCount(0);
//		mail.setAddTime(new Timestamp(System.currentTimeMillis()));
//		//mail.setSendTime(new Timestamp(System.currentTimeMillis()));
//		res.add(mail);
//		this.getMailDao().saveOrUpdateRecords(res);
//	}
//	//获取新增邮件的ids
//	public List getNewMailIds(){
//		//获取当前登陆员工
//		WebContext ctx = WebContextFactory.get();
//		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
//		String hql = " from CotMail as obj where obj.mailStatus = 0 and obj.empId="+cotEmps.getId();
//		List<CotMail> res = new ArrayList<CotMail>();
//		res = this.getMailDao().find(hql);
//		
//		List<String> ids = new ArrayList<String>();
//		for (int i = 0; i < res.size(); i++) {
//			CotMail cotMail = res.get(i);
//			ids.add(cotMail.getId());
//		}
//		return ids;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#sendMessageByThread(java.lang.String)
//	 */
//	//带线程的发送
//	public void sendMessageByThread(String flag,CotMail mail,List<CotMail> mailList) {
//		// TODO Auto-generated method stub
//		SendMailRunable sendMailThread = new SendMailRunable(mail, mailList);
//		sendMailThread.setFlag(flag);
//		this.getEasyMailExecutorPool().getService().execute(sendMailThread);
//	}
//
//	public EasyMailExecutorPool getEasyMailExecutorPool() {
//		return easyMailExecutorPool;
//	}
//
//	public void setEasyMailExecutorPool(EasyMailExecutorPool easyMailExecutorPool) {
//		this.easyMailExecutorPool = easyMailExecutorPool;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailService#getSendMsg(java.lang.Integer)
//	 */
//	public String getSendMsg(Integer empId) {
//		// TODO Auto-generated method stub
//		Iterator iterator = this.mailMessageMap.keySet().iterator();
//		String strMsg = "";
//		while(iterator.hasNext())
//		{
//			String key = (String)iterator.next();
//			String value = (String)this.mailMessageMap.get(key);
//			String id = key.substring(0,key.indexOf("_"));
//			if(empId.toString().equals(id))
//				strMsg += value +"</br>";
//			this.mailMessageMap.remove(key);
//		}
//		return strMsg;
//	}
//	
//}
