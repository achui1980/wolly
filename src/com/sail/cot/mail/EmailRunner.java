///**
// * 
// */
//package com.sail.cot.mail;
//
//import java.io.File;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import com.sail.cot.dao.CotBaseDao;
//import com.sail.cot.domain.CotMail;
//import com.sail.cot.mail.service.MailService;
//import com.sail.cot.mail.service.impl.MailServiceImpl;
//
///**
// * <p>Title: 工艺品管理系统</p>
// * <p>Description:</p>
// * <p>Copyright: Copyright (c) 2008</p>
// * <p>Company: </p>
// * <p>Create Time: Oct 17, 2008 9:41:40 AM </p>
// * <p>Class Name: EmailRunner.java </p>
// * @author achui
// *
// */
//public class EmailRunner implements Runnable{
//
//	 SimpleMailMessage simpleMailMessage;   
//     MimeMessage mimeMessage; 
//
//     JavaMailSender javaMailSender;
//     ExecutorService executorService;  
//     CotMail mailObj;
//     CotBaseDao mailDao;
//     
//     
//     public void addAttachmentOrImg(MimeMessageHelper helper, Map map, boolean isAttachment) throws MessagingException {   
//    	 sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
//    	 for (Iterator it = map.entrySet().iterator(); it.hasNext();) {   
//             Map.Entry entry = (Map.Entry) it.next();   
//             String key = (String) entry.getKey();   
//             String value = (String) entry.getValue();  
//             //value = "=?GBK?B?"+enc.encode(value.getBytes())+"?=";
//             if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {   
//                 FileSystemResource file = new FileSystemResource(new File(value));   
//                 if (!file.exists()) continue;   
//                 if (isAttachment) { 
//                	 key = "=?GBK?B?"+enc.encode(key.getBytes())+"?=";
//                     helper.addAttachment(key, file);   
//                 } else {   
//                     helper.addInline(key, file);   
//                 }   
//             }   
//         }   
//     }   
//     /**  
//      * 构造简单文本邮件  
//      * @param simpleMailMessage  
//      */  
//     public EmailRunner(SimpleMailMessage simpleMailMessage) {   
//         if (mimeMessage == null) {   
//             this.simpleMailMessage = simpleMailMessage;   
//         }   
//         
//     }   
//     /**  
//      * 构造复杂邮件，可以添加附近，图片，等等  
//      * @param mimeMessage  
//      */  
//     public EmailRunner(MimeMessage mimeMessage) {   
//         if (simpleMailMessage == null) {   
//             this.mimeMessage = mimeMessage;   
//         }   
//     }   
//	/* (non-Javadoc)
//	 * @see java.lang.Runnable#run()
//	 */
//	public void run(){
//    	
//		try {  
//            if (simpleMailMessage != null) {   
//                javaMailSender.send(this.simpleMailMessage);   
//            } else if (mimeMessage != null) { 
//            	System.out.println("---正在放送");
//                javaMailSender.send(this.mimeMessage);   
//            }  
//            MailServiceImpl mailService = new MailServiceImpl();
//        	mailService.setMailDao(this.mailDao);
//        	mailService.updateMail(this.mailObj, 3, "Success"); //状态3:发送成功
//               
//        } catch (Exception e) {   
//        	MailServiceImpl mailService = new MailServiceImpl();
//        	mailService.setMailDao(this.mailDao);
//        	mailService.updateMail(this.mailObj, 1, e.getMessage()); //状态1:发送失败
//        	e.printStackTrace();
////            if (logger.isDebugEnabled()) {   
////                logger.debug("logger something here", e);   
////            }   
//        }     
//		
//	}
//
//}
