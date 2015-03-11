///**
// * 
// */
//package com.sail.cot.mail.service.impl;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.Security;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.UUID;
//
//import javax.mail.internet.MimeMessage;
//import javax.mail.*; 
//
//import org.apache.log4j.Logger;
//import org.directwebremoting.WebContext;
//import org.directwebremoting.WebContextFactory;
//
//import org.apache.commons.lang.RandomStringUtils;
//
//import com.achui.mail.AbstractReciveOneMail;
//import com.jason.core.exception.DAOException;
//import com.sail.cot.dao.CotBaseDao;
//import com.sail.cot.dao.impl.CotExportRptDaoImpl;
//import com.sail.cot.domain.CotEmps;
//import com.sail.cot.domain.CotMailAttach;
//import com.sail.cot.domain.CotMailCfg;
//import com.sail.cot.domain.CotMailRecv;
//import com.sail.cot.domain.CotMailRecvAttach;
//import com.sail.cot.mail.EasyMailExecutorPool;
//import com.sail.cot.mail.ReceiveMailRunable;
//import com.sail.cot.mail.service.MailRecvService;
//import com.sail.cot.util.ContextUtil;
//import com.sail.cot.util.Log4WebUtil;
//import com.sail.cot.util.SystemUtil;
//import com.sail.cot.util.ThreadLocalManager;
//
///**
// * <p>Title: 旗行办公自动化系统（OA）</p>
// * <p>Description:</p>
// * <p>Copyright: Copyright (c) 2008</p>
// * <p>Company: </p>
// * <p>Create Time: May 8, 2009 10:14:05 AM </p>
// * <p>Class Name: ReciveMailServiceImpl.java </p>
// * @author achui
// *
// */
//public class ReciveMailServiceImpl extends AbstractReciveOneMail implements
//		MailRecvService {
//	final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
//	private CotBaseDao baseDao;
//	private EasyMailExecutorPool easyMailExecutorPool;  
//	private Logger logger = Log4WebUtil.getLogger(ReciveMailServiceImpl.class);
//	private static Map mailRecvMessageMap = new HashMap();
//	private static Map publicMap = new HashMap();
//	/* (non-Javadoc)
//	 * 保存附件到数据库
//	 * @see com.achui.mail.AbstractReciveOneMail#saveAttachmentToDB(java.lang.String, java.io.InputStream, java.lang.String)
//	 */
//	@Override
//	public void saveAttachmentToDB(String fileName, InputStream in, String msgId) {
//		CotMailRecvAttach attach = new CotMailRecvAttach();
//		List lst = new ArrayList();
//		attach.setFileName(fileName);
//		int size = 0;
//		byte[] arrByte = null;
//		try {
//			size = in.available();
//			arrByte = new byte[size];
//			in.read(arrByte);
//		} catch (IOException e) { 
//			e.printStackTrace();
//		}
//		attach.setFileSize(Long.parseLong(String.valueOf(size)));
//		attach.setFileContent(arrByte);
//		attach.setMsgId(msgId);
//		lst.add(attach);
//		try {
//			this.getBaseDao().saveRecords(lst);
//		} catch (DAOException e) { 
//			e.printStackTrace();
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#reciveMail()
//	 */
//	public void saveReciveMail(String mailSmtpHost,String mailPop3Host,String mailAccount,String Pwd,String flag,CotEmps cotEmps) throws Exception {
//		
//		//获取当前员工信息
//		
//		CotMailCfg cfg = this.getCotMailCfg();
//		//CotEmps cotEmps = this.curEmps();
//		if(flag.equals("private"))
//		{
//			cfg.setDefaultIsSslpop3(cotEmps.getEmpsIsSSLPop3());
//			cfg.setDefaultHost(cotEmps.getEmpsMailHost());
//			cfg.setDefaultHostSmtp(cotEmps.getEmpsSmtpHost());
//			cfg.setDefaultIsSslsmtp(cotEmps.getEmpsIsSSLSmtp());
//			cfg.setDefaultAccount(cotEmps.getEmpsAccount());
//			cfg.setDefaultPwd(cotEmps.getEmpsMailPwd());
//			cfg.setDefaultPop3port(cotEmps.getEmpsPop3Port());
//			cfg.setDefaultSmtpPort(cotEmps.getEmpsSmtpPort());
//		}
//		Folder folder = this.openFolder(cfg);
//		if(folder == null) return;
//        Message message[] = folder.getMessages();
//        CotMailRecv recv = null;
//        for (int i = 0; i < message.length; i++) {
//        	 Boolean success = null;
//     	    String msg = null;
//        	this.setMimeMessage((MimeMessage) message[i]);
//        	this.getMailContent((Part) message[i]);
//            recv = new CotMailRecv(); 
//            //发件人
//            recv.setCustEmail(this.getFrom());
//            //收件人
//            recv.setEmpEmail(this.getMailAddress("TO"));
//            
//            //判断邮件接收类型为私有还是公有
//            
//            //取得当前登陆者信息
//            
//            if ("private".equals(flag)) {
//				recv.setEmpId(cotEmps.getId());
//				recv.setEmpName(cotEmps.getEmpsName());
//				//recv.setEmpEmail(cotEmps.getEmpsMail());
//			}
//            //是否含有附件
//            int attatchFlag = 0;
//            if(this.isContainAttach((Part) message[i]))
//            	attatchFlag = 1;
//            recv.setAttatchFlag(attatchFlag);
//            //邮件主题
//            recv.setMailSubject(this.getSubject());
//            //设置邮件内容
//            System.out.println("content:"+this.getBodyText());
//            recv.setMailContent(this.getBodyText());
//            //设置邮件日期 
//            DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");         
//            recv.setMailTime(format.parse(this.getSentDate()));
//            //设置接收日期
//            recv.setAddTime(new Date(System.currentTimeMillis()));
//            //设置邮件类型为收件箱
//            recv.setMailType(1);
//            //设置邮件状态为新邮件
//            recv.setMailStatus(0);
//            //设置邮件ID
//            //String msgId = UUID.randomUUID().toString();
//            //生成一个32位的ID
//            String msgId = RandomStringUtils.randomAlphabetic(32);
//            recv.setMsgId(msgId);
//            
//            List lst = new ArrayList();
//            lst.add(recv);
//            //保存邮件
//            this.getBaseDao().saveRecords(lst);
//          //保存邮件附件
//            this.saveAttachMent((Part) message[i],msgId); 
//            //删除服务器邮件
//            this.deleteMail();
//          
//    		msg = "主题："+recv.getMailSubject()+",接收完成";
//    		if("private".equals(flag))
//    			this.mailRecvMessageMap.put(recv.getEmpId()+"_"+recv.getMailSubject(), msg);
//    		else
//    			this.publicMap.put(recv.getEmpId()+"_"+recv.getMailSubject(), "[公共邮件]"+msg);
//        }  
//        folder.close(true);
//        //关闭文件夹后，在开启文件夹，进行后续操作
//        //openFolder( mailSmtpHost, mailPop3Host, mailAccount, Pwd);
//       System.out.println("publicMap:"+publicMap.toString());
//       //this.setPublicMap(publicMap);
//	}
//
//	public CotBaseDao getBaseDao() {
//		return baseDao;
//	}
//
//	public void setBaseDao(CotBaseDao baseDao) {
//		this.baseDao = baseDao;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#closeFolder(boolean)
//	 */
//	public void closeFolder(boolean b) throws Exception {
//		Folder folder = (Folder)ThreadLocalManager.getCurrentThread().get();
//		folder.close(b);	
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#openFolder(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@SuppressWarnings("unchecked")
//	public Folder openFolder(CotMailCfg cfg) throws Exception {
//		
//		Properties props = System.getProperties(); 
//		 URLName urln = null;
//		if(cfg.getDefaultIsSslpop3() == 1) //需要通过SSL验证
//		{
//			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider()); 
//			props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
//			props.setProperty("mail.pop3.socketFactory.fallback", "false");
//			props.setProperty("mail.pop3.port", cfg.getDefaultPop3port().toString());
//			props.setProperty("mail.pop3.socketFactory.port", cfg.getDefaultPop3port().toString());
//			urln = new URLName("pop3", cfg.getDefaultHost(), cfg.getDefaultPop3port().intValue(), null,   
//	        		cfg.getDefaultAccount(), cfg.getDefaultPwd());  
//		}
//		else
//		{
//			//props.put("mail.smtp.host", cfg.getDefaultHost());   
//	       // props.put("mail.smtp.auth", "true"); 
//			props.remove("mail.pop3.socketFactory.class");
//			props.remove("mail.pop3.socketFactory.fallback");
//			props.remove("mail.pop3.port");
//			props.remove("mail.pop3.socketFactory.port");
//	        urln = new URLName("pop3", cfg.getDefaultHost(), 110, null,   
//	        		cfg.getDefaultAccount(), cfg.getDefaultPwd());
//		}
//        Session session = Session.getDefaultInstance(props, null);   
//       // URLName urln = new URLName("pop3", cfg.getDefaultHost(), cfg.getDefaultPop3Port().intValue(), null,   
//        		//cfg.getDefaultAccount(), cfg.getDefaultPwd());  
//	    Store store = session.getStore(urln);   
//	    store.connect();   
//	    Folder folder = store.getFolder("INBOX");   
//	    folder.open(Folder.READ_WRITE); 
//	    ThreadLocalManager.getCurrentThread().set(folder);
//	    return folder;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#deleteMailsByIndex(int[])
//	 */
//	public void deleteMailsByIndex(int[] msgnum) {
//		// TODO Auto-generated method stub
//		
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#getMailListFromMailServ(int, int)
//	 */
//	public List<CotMailRecv> getMailListFromMailServ(int start, int perPageCount) {
//		Folder folder = (Folder)ThreadLocalManager.getCurrentThread().get();
//		CotMailRecv recv = null;
//		List<CotMailRecv> lst = new ArrayList<CotMailRecv>();
//		try
//		{
//			start = start<=0 ? 1 : start;
//			int end = perPageCount +start;
//			int totalCount = getTotalMailCount();
//			if(end > totalCount);
//				end = totalCount;
//			Message message[] = folder.getMessages(start, end);
//			
//		        for (int i = 0; i < message.length; i++) {
//		        	
//		        	this.setMimeMessage((MimeMessage) message[i]);
//		            recv = new CotMailRecv(); 
//		            //发件人
//		            recv.setCustEmail(this.getFrom());
//		            //收件人
//		            recv.setEmpEmail(this.getMailAddress("TO"));
//		            //是否含有附件
//		            int attatchFlag = 0;
//		            if(this.isContainAttach((Part) message[i]))
//		            	attatchFlag = 1;
//		            recv.setAttatchFlag(attatchFlag);
//		            //邮件主题
//		            recv.setMailSubject(this.getSubject());
//		            //设置邮件内容
//		            System.out.println("content:"+this.getBodyText());
//		            recv.setMailContent(this.getBodyText());
//		            //设置邮件日期 
//		            DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");         
//		            recv.setMailTime(format.parse(this.getSentDate()));
//		            //设置接收日期
//		            recv.setAddTime(new Date(System.currentTimeMillis()));
//		            //设置邮件状态为新邮件
//		            recv.setMailStatus(0);
//		            //设置邮件ID
//		            String msgId = UUID.randomUUID().toString();
//		            recv.setMsgId(msgId);
//		            lst.add(recv);
//		        }
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		return lst;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#getTotalMailCount()
//	 */
//	public int getTotalMailCount() {
//		Folder folder = (Folder)ThreadLocalManager.getCurrentThread().get();
//		try
//		{
//			if(folder == null) return 0;
//			else return folder.getMessageCount();
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		return 0;
//	}
//	//当前登陆员工
//	public CotEmps curEmps() {
//
//		WebContext ctx = WebContextFactory.get();
//		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
//		return cotEmps;
//	}
//	//根据id获取信息
//	public CotMailRecv getMailByMsgId(String msgId){
//		List<CotMailRecv> res = new ArrayList<CotMailRecv>();
//		res = baseDao.find("select obj from CotMailRecv obj where obj.msgId='"+msgId+"'");
//		if(res.size()!=1){
//			return null;
//		}else{
//			CotMailRecv mail = (CotMailRecv) SystemUtil.deepClone(res.get(0));
//			String custMail = mail.getCustEmail();
//			int idx1 = custMail.lastIndexOf("(");
//			int idx2 = custMail.lastIndexOf(")");
//			int length = custMail.length();
//			System.out.println("idx1:"+idx1+" idx2:"+idx2+" length:"+length);
//			custMail = custMail.substring(idx1+1,idx2);
//			mail.setCustEmail(custMail);
//			custMail = mail.getEmpEmail();
//			idx1 = custMail.lastIndexOf("(");
//			idx2 = custMail.lastIndexOf(")");
//			custMail = custMail.substring(idx1+1,idx2);
//			mail.setEmpEmail(custMail);
//			return mail;
//		}
//	}
//	//获取附件名称
//	public String getFileName(String msgId) {
//		List<String> res = new ArrayList<String>();
//		res = baseDao.find("select obj.fileName from CotMailAttach obj where obj.msgId='"+msgId+"'");
//		if(res.size()!=1){
//			return null;
//		}else{
//			return res.get(0);
//		}
//	}
//	//删除收信箱中的邮件
//	public void modifyMailType(List<CotMailRecv> ids){
//		List<CotMailRecv> res = new ArrayList<CotMailRecv>();
//		for (int i = 0; i < ids.size(); i++) {
//			CotMailRecv cotMailRecvOld = (CotMailRecv)ids.get(i);
//			res = baseDao.find("select obj from CotMailRecv obj where obj.msgId='"+cotMailRecvOld.getMsgId()+"'");
//			CotMailRecv cotMailRecvNew = (CotMailRecv)res.get(0);
//			cotMailRecvNew.setMailType(2);
//			List<CotMailRecv> logList = new ArrayList<CotMailRecv>();
//			logList.add(cotMailRecvNew);
//			baseDao.updateRecords(logList);
//		}
//	}
//	//修改收信箱中的邮件状态
//	public void modifyMailStatus(List<CotMailRecv> ids){
//		List<CotMailRecv> res = new ArrayList<CotMailRecv>();
//		for (int i = 0; i < ids.size(); i++) {
//			CotMailRecv cotMailRecvOld = (CotMailRecv)ids.get(i);
//			res = baseDao.find("select obj from CotMailRecv obj where obj.msgId='"+cotMailRecvOld.getMsgId()+"'");
//			CotMailRecv cotMailRecvNew = (CotMailRecv)res.get(0);
//			if (cotMailRecvNew.getMailStatus() == 0) {
//				cotMailRecvNew.setMailStatus(1);
//			}
//			List<CotMailRecv> logList = new ArrayList<CotMailRecv>();
//			logList.add(cotMailRecvNew);
//			baseDao.updateRecords(logList);
//		}
//	}	
//	//获取附件
//	public CotMailAttach getMailAttach(String msgId){
//		
//		List<?> list = this.getBaseDao().find(" from CotMailAttach obj where obj.msgId='"+msgId+"'");
//		CotMailAttach res = null;
//		if(list != null && list.size() > 0)
//			res = (CotMailAttach)list.get(0);
//		return res;
//	}
//	//删除附件	
//	public void deleteAttach(String msgId) {
//		List<CotMailAttach> list = new ArrayList<CotMailAttach>();
//		list = this.getBaseDao().find(" from CotMailAttach obj where obj.msgId='"+msgId+"'");
//		if (list.size() == 0) {
//			return;
//		}
//		for (int i = 0; i < list.size(); i++) {
//			CotMailAttach cotMailAttach = (CotMailAttach) list.get(i);
//			try {
//				this.getBaseDao().delete(cotMailAttach);
//			} catch (RuntimeException e) {
//				e.printStackTrace();
//				logger.error("删除附件异常",e);
//			}
//		}
//		
//	}
//	//从废件箱删除邮件
//	public void deleteRecvMail(List<CotMailRecv> mailRecvList) {
//		
//		for (int i = 0; i < mailRecvList.size(); i++) {
//			CotMailRecv cotMailRecv = (CotMailRecv) mailRecvList.get(i);
//			try {
//				List<CotMailRecv> list = new ArrayList<CotMailRecv>();
//				list = this.getBaseDao().find(" from CotMailRecv obj where obj.msgId='"+cotMailRecv.getMsgId()+"'");
//				 CotMailRecv res = (CotMailRecv) list.get(0);
//				 if (res.getAttatchFlag() == 1) {
//					 this.deleteAttach(cotMailRecv.getMsgId());
//				}
//				this.getBaseDao().delete(res);
//			} catch (RuntimeException e) {
//				e.printStackTrace();
//				logger.error("删除邮件异常",e);
//			}
//		}
//	}	
//	//获取邮箱默认配置信息
//	public CotMailCfg getCotMailCfg(){
//		
//		List<?> list = this.getBaseDao().find(" from CotMailCfg obj");
//		CotMailCfg res  = (CotMailCfg)list.get(0);
//		return res;
//	}
//	//获取未读邮件数量
//	public Integer getCoutUnreadMail()
//	{
//		CotEmps emp = (CotEmps) this.curEmps();
//		List<?> list = this.getBaseDao().find(" from CotMailRecv obj where obj.mailType=1 and obj.mailStatus=0 and obj.empId ="+ emp.getId());
//		if (list.size() != 0) {
//			return list.size();
//		}else {
//			return -1;
//		}
//	}
//	//根据id获得员工信息
//	public CotEmps getEmpsById(Integer id){
//		
//		CotEmps cotEmps = (CotEmps) this.getBaseDao().getById(CotEmps.class,id);
//		return cotEmps;
//	}
//	//邮件指派
//	public void modifyMailRecvByIds(List<CotMailRecv> ids,Integer empId,String empName,String empMail){
//		
//		List<CotMailRecv> res = new ArrayList<CotMailRecv>();
//		for (int i = 0; i < ids.size(); i++) {
//			CotMailRecv cotMailRecvOld = (CotMailRecv)ids.get(i);
//			res = baseDao.find("select obj from CotMailRecv obj where obj.msgId='"+cotMailRecvOld.getMsgId()+"'");
//			CotMailRecv cotMailRecvNew = (CotMailRecv)res.get(0);
//			cotMailRecvNew.setEmpId(empId);
//			cotMailRecvNew.setEmpName(empName);
//			cotMailRecvNew.setEmpEmail(empMail);
//			cotMailRecvNew.setMailStatus(2);//指派状态
//			List<CotMailRecv> logList = new ArrayList<CotMailRecv>();
//			logList.add(cotMailRecvNew);
//			baseDao.updateRecords(logList);
//		}
//	}
//	//获取邮件内容
//	public String getMailContents(String msgId){
//		
//		List<CotMailRecv> res = new ArrayList<CotMailRecv>();
//		res = baseDao.find("select obj from CotMailRecv obj where obj.msgId='"+msgId+"'");
//		String html = "";
//		CotMailRecv cotMailRecv = (CotMailRecv)res.get(0);
//		html = cotMailRecv.getMailContent();
//		//System.out.println(html);
//		return html;
//	}
//	//判断查看邮件者是否为邮件接收人
//	public boolean checkMailRecv(String msgId){
//		CotEmps cotEmps = this.curEmps();
//		List<CotMailRecv> res = new ArrayList<CotMailRecv>();
//		res = baseDao.find("select obj from CotMailRecv obj where obj.msgId='"+msgId+"'");
//		CotMailRecv cotMailRecv = (CotMailRecv)res.get(0);
//		if (cotMailRecv.getEmpName().equals( cotEmps.getEmpsName())) {
//			return true;
//		}else {
//			return false;
//		}
//	}
//	//从公共邮件箱删除邮件
//	public void deleteDefaultMail(List<CotMailRecv> mailRecvList) {
//		
//		for (int i = 0; i < mailRecvList.size(); i++) {
//			CotMailRecv cotMailRecv = (CotMailRecv) mailRecvList.get(i);
//			try {
//				List<CotMailRecv> list = new ArrayList<CotMailRecv>();
//				list = this.getBaseDao().find(" from CotMailRecv obj where obj.msgId='"+cotMailRecv.getMsgId()+"'");
//				 CotMailRecv res = (CotMailRecv) list.get(0);
//				 if (res.getAttatchFlag() == 1) {
//					 this.deleteAttach(cotMailRecv.getMsgId());
//				}
//				this.getBaseDao().delete(res);
//			} catch (RuntimeException e) {
//				e.printStackTrace();
//				logger.error("删除邮件异常",e);
//			}
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#sendMessageByThread(java.lang.String)
//	 */
//	public void sendMessageByThread(String flag,CotEmps cotEmps) {
//		ReceiveMailRunable receiveThread = new ReceiveMailRunable();
//		receiveThread.setFlag(flag);
//		receiveThread.setCotEmps(cotEmps);
//		this.getEasyMailExecutorPool().getService().execute(receiveThread);
//	}
//
//	public EasyMailExecutorPool getEasyMailExecutorPool() {
//		if(easyMailExecutorPool == null)
//			easyMailExecutorPool = (EasyMailExecutorPool)ContextUtil.getBean("easyMailExecutorPool");
//		return easyMailExecutorPool;
//	}
//
//	public void setEasyMailExecutorPool(EasyMailExecutorPool easyMailExecutorPool) {
//		this.easyMailExecutorPool = easyMailExecutorPool;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.sail.cot.mail.service.MailRecvService#getMailAttatch(java.lang.String, java.lang.String)
//	 */
//	public String[] getMailAttatch(String msgId, String empName) throws Exception {
//		List<CotMailRecvAttach> res = new ArrayList<CotMailRecvAttach>();
//		res = baseDao.find(" from CotMailRecvAttach obj where obj.msgId='"+msgId+"'");
//		if(res == null || res.size() == 0)
//			return null;
//		String[] fileName = new String[res.size()];
//		String classpath = ReciveMailServiceImpl.class.getResource("/").getPath();
//		int index = classpath.indexOf("WEB-INF");
//		String filePath = classpath.substring(1,index) + "mailfolder"+File.separator + empName;
//		File file = new File(filePath);
//		ByteArrayInputStream is = null;
//		FileOutputStream os = null;
//		
//		if(!file.exists())
//		{
//			file.mkdir();
//		}
//		int i=0;
//		for(CotMailRecvAttach attach : res)
//		{
//			byte[] buf = new byte[512];
//			is = new ByteArrayInputStream(attach.getFileContent());
//			file = new File(filePath+File.separator+attach.getFileName());
//			os = new FileOutputStream(file);
//			while(is.read(buf) != -1)
//			{
//				os.write(buf);
//			}
//			fileName[i] = empName+File.separator+attach.getFileName();
//			i++;
//		}
//		is.close();
//		os.close();
//		
//		return fileName;
//	}
//	public String getRecvMsg(Integer empId,boolean assign) {
//		// TODO Auto-generated method stub
//		Iterator iterator = this.mailRecvMessageMap.keySet().iterator();
//		String strMsg = "";
//		while(iterator.hasNext())
//		{
//			String key = (String)iterator.next();
//			String value = (String)this.mailRecvMessageMap.get(key);
//			String id = key.substring(0,key.indexOf("_"));
//			if(empId.toString().equals(id))
//				strMsg += value +"</br>";
//			this.mailRecvMessageMap.remove(key);
//		}
//		if(!assign) return strMsg;
//		iterator = this.publicMap.keySet().iterator();
//		while(iterator.hasNext())
//		{
//			String key = (String)iterator.next();
//			String value = (String)this.publicMap.get(key);
//			strMsg += value +"</br>";
//			this.publicMap.remove(key);
//		}
//		return strMsg;
//	}
//}
