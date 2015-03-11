///**
// * 
// */
//package com.sail.cot.mail;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import com.sail.cot.dao.CotBaseDao;
//import com.sail.cot.domain.CotMail;
//import com.sail.cot.mail.service.MailService;
//
///**
// * <p>Title: 工艺品管理系统</p>
// * <p>Description:</p>
// * <p>Copyright: Copyright (c) 2008</p>
// * <p>Company: </p>
// * <p>Create Time: Oct 16, 2008 5:55:36 PM </p>
// * <p>Class Name: EmailEntity.java </p>
// * @author achui
// *
// */
//public class EmailEntity extends EmailRunner{
//	/**
//	 * @param mimeMessage
//	 */
//	public EmailEntity(MimeMessage mimeMessage) {
//		super(mimeMessage);
//	}
//	public EmailEntity(SimpleMailMessage simpleMailMessage) {
//		super(simpleMailMessage);
//	}
//	
//	private String to;     		//邮件接收人
//	  
//	private String subject;   	//邮件主题
//  
//	private String text;  		//邮件内容
//    
//	private String from;		//邮件发送人
//    
//	private boolean isAuth;		//邮件是否需要鉴权(某些邮件服务器需要鉴权)
//    
//	private String mailhost;	//邮件服务器地址 如：smtp.sina.com.cn(默认从SPRING的配置文件中取)
//    
//	private String username;	//发送邮件的用户帐号 如：achui_1980（默认从SPRING的配置文件中取)
//    
//	private String password;	//发送邮件帐号的密码 如：123456(默认从Spring配置文件中取)
//    
//	private boolean isDebug;    //是否打印调试信息;
//	
//  
//    //邮件附件   
//    Map<String, String> attachment = new HashMap<String, String>();   
//  
//    //邮件图片   
//    Map<String, String> img = new HashMap<String, String>();
//    
//    private CotMail mailObj ;
//    
//    private CotBaseDao mailDao;
//    
//
//	public CotMail getMailObj() {
//		return mailObj;
//	}
//	public void setMailObj(CotMail mailObj) {
//		this.mailObj = mailObj;
//	}
//
//	public boolean isAuth() {
//		return isAuth;
//	}
//	public void setAuth(boolean isAuth) {
//		this.isAuth = isAuth;
//	}
//	public String getMailhost() {
//		return mailhost;
//	}
//	public void setMailhost(String mailhost) {
//		this.mailhost = mailhost;
//	}
//	public String getUsername() {
//		return username;
//	}
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	public boolean isDebug() {
//		return isDebug;
//	}
//	public void setDebug(boolean isDebug) {
//		this.isDebug = isDebug;
//	}
//	public String getTo() {
//		return to;
//	}
//
//	public void setTo(String to) {
//		this.to = to;
//	}
//
//	public String getSubject() {
//		return subject;
//	}
//
//	public void setSubject(String subject) {
//		this.subject = subject;
//	}
//
//	public String getText() {
//		return text;
//	}
//
//	public void setText(String text) {
//		this.text = text;
//	}
//
//	public Map<String, String> getAttachment() {
//		return attachment;
//	}
//
//	public void setAttachment(Map<String, String> attachment) {
//		this.attachment = attachment;
//		
//	}
//
//	public Map<String, String> getImg() {
//		return img;
//	}
//
//	public void setImg(Map<String, String> img) {
//		this.img = img;
//	}   
//	 /**  
//     * 构造简单文本邮件  
//     * @param simpleMailMessage  
//     */  
//    public void generateSimpleMailMessage(JavaMailSender javaMailSender, ExecutorService executorService) {   
//        super.javaMailSender = javaMailSender;   
//        super.executorService = executorService;   
//        super.mailObj = this.mailObj;
//        super.mailDao = this.mailDao;
//        simpleMailMessage = new SimpleMailMessage();   
//        simpleMailMessage.setTo(to);   
//        simpleMailMessage.setSubject(subject);   
//        simpleMailMessage.setText(text);   
//        simpleMailMessage.setFrom(from);  
//        
//    }   
//       
//  
//    /**  
//     * 构造复杂邮件，可以添加附近，图片，等等  
//     * @param mimeMessage  
//     * @throws MessagingException   
//     */  
//    public void generateMimeMailMessage(JavaMailSender javaMailSender, ExecutorService executorService) throws MessagingException {   
//        super.javaMailSender = javaMailSender;   
//        super.executorService = executorService;   
//        super.mailObj = this.mailObj;
//        super.mailDao = this.mailDao;   
//        MimeMessage message = javaMailSender.createMimeMessage();   
//        super.mimeMessage = message;
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "gbk");   
//           
//        helper.setTo(to);   
//        helper.setFrom(from);   
//        helper.setSubject(subject);
//           
//        super.addAttachmentOrImg(helper, attachment, true);   
//        super.addAttachmentOrImg(helper, img, false);   
//           
//        //这里的text是html格式的, 可以使用模板引擎来生成html模板, velocity或者freemarker都可以做到   
//        helper.setText(text);   
//    }   
//  
//  
//    /**  
//     * 发送邮件方法, 在这个方法调用之前必须调用 generateMimeMailMessage 或者 generateSimpleMailMessage，  
//     * @see EasyMailServiceImpl#sendMimeMessage(EmailEntity)  
//     * @see EasyMailServiceImpl#sendMessage(EmailEntity)  
//     */  
//    public void send() {   
//        if(super.javaMailSender != null && super.executorService != null){   
//            super.executorService.execute(this);   
//        }   
//    }
//
//	public String getFrom() {
//		return from;
//	}
//
//	public void setFrom(String from) {
//		this.from = from;
//	}
//	public CotBaseDao getMailDao() {
//		return mailDao;
//	}
//	public void setMailDao(CotBaseDao mailDao) {
//		this.mailDao = mailDao;
//	}   
//}
