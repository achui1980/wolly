///**
// * 
// */
//package com.sail.cot.mail;
//
//import java.io.File;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Properties;
//
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.internet.MimeMessage;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
///**
// * <p>Title: 工艺品管理系统</p>
// * <p>Description:</p>
// * <p>Copyright: Copyright (c) 2008</p>
// * <p>Company: </p>
// * <p>Create Time: Oct 16, 2008 5:54:19 PM </p>
// * <p>Class Name: EasyMailServieImpl.java </p>
// * @author achui
// *
// */
//public class EasyMailServieImpl implements EasyMailService{
//	//private static transient Log logger = LogFactory.getLog(EasyMailServieImpl.class);    
//    
//    //注入MailSender   
//   // private JavaMailSender javaMailSender;  
//    private JavaMailSenderImpl javaMailSender;
//    
//       
//    //注入线程池   
//    private EasyMailExecutorPool easyMailExecutorPool;   
//       
//    //设置发件人   
//    private String from;   
//       
//    public void setEasyMailExecutorPool(EasyMailExecutorPool easyMailExecutorPool) {   
//        this.easyMailExecutorPool = easyMailExecutorPool;   
//    }   
//  
//    public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {   
//        this.javaMailSender = javaMailSender;  
//    }   
//       
//    public void setFrom(String from) {   
//        this.from = from;   
//    }   
//  
//    /**  
//     * 简单的邮件发送接口，感兴趣的同学可以在这个基础上继续添加  
//     * @param to  
//     * @param subject  
//     * @param text  
//     */  
//    public void sendMessage(EmailEntity email) throws MessagingException{   
//    	email.setFrom(email.getFrom());
//    	email.setTo(email.getTo());
//    	javaMailSender.setHost(email.getMailhost());
//    	javaMailSender.setPassword(email.getPassword());
//    	javaMailSender.setUsername(email.getUsername());
//    	Properties prop = new Properties();
//    	prop.put("mail.debug",String.valueOf(email.isDebug()) );
//    	prop.put("mail.smtp.auth", String.valueOf(email.isDebug()));
//    	prop.put("mail.smtp.timeout", "25000");
//    	javaMailSender.setJavaMailProperties(prop);
//    	email.generateSimpleMailMessage(javaMailSender, easyMailExecutorPool.getService());    	
//        email.send();      
//    }   
//       
//    /**  
//     * 发送复杂格式邮件的接口，可以添加附件，图片，等等，但是需要修改这个方法，  
//     * 如何做到添加附件和图片论坛上有例子了，需要的同学搜一下,  
//     * 事实上这里的text参数最好是来自于模板，用模板生成html页面，然后交给javamail去发送，  
//     * 如何使用模板来生成html见 {@link http://www.javaeye.com/topic/71430 }  
//     *   
//     * @param to  
//     * @param subject  
//     * @param text  
//     * @throws MessagingException  
//     */  
//    public void sendMimeMessage(EmailEntity email) throws MessagingException {   
//    	email.setFrom(email.getFrom()); 
//    	email.setTo(email.getTo());
//    	javaMailSender.setHost(email.getMailhost());
//    	javaMailSender.setPassword(email.getPassword());
//    	javaMailSender.setUsername(email.getUsername());
//    	Properties prop = new Properties();
//    	prop.put("mail.debug",String.valueOf(email.isDebug()) );
//    	prop.put("mail.smtp.auth", String.valueOf(email.isAuth()));
//    	prop.put("mail.smtp.timeout", "25000");
//    	javaMailSender.setJavaMailProperties(prop);
//        email.generateMimeMailMessage(javaMailSender, easyMailExecutorPool.getService());   
//        email.send();      
//    }        
//}
