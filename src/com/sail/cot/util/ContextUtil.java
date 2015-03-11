/**
 * 
 */
package com.sail.cot.util;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.achui.mail.MailCfg;
import com.achui.mail.MailObject;
import com.achui.mail.SendMailService;
import com.achui.mail.SendMailServiceImpl;
import com.jason.core.Application;
import com.sail.cot.domain.CotOrderDetail;

/**
 * <p>Title: Ext+DWR+Spring</p>
 * <p>Description:获取Spring Bean对象的工具类</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 9, 2008 4:19:03 PM </p>
 * @author achui
 *
 */

public class ContextUtil {
	
	/**
	 * @param name:bean对象名
	 * @return
	 * Object
	 */
	private static Hashtable<String,Properties> propList = new Hashtable<String,Properties>();
	private static Cache cache4Mail ;
	private static Cache cache4MailMsg;
	public static Object getBean(String name)
	{
		return Application.getInstance().getContainer().getComponent(name);
	}

	
	
	/**
	 * @param filename
	 * @param key
	 * @return
	 * String
	 * 用法:
	 * String strEjbAddress = ContextUtil.getProperty("sysconfig.properties",
				"ftpimgpath");

	 * 
	 */
	public static String getProperty(String filename,String key)
	{
		Properties p = null;
		if(propList.containsKey(filename))
			p = propList.get(filename);
		else
		{
			p = new Properties();
			try {
				p.load(ContextUtil.class.getResourceAsStream("/"+filename));
				propList.put(filename, p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return p.getProperty(key);
		
	}
	
	public static String getCurrentDate(String formate)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(formate);      
		Calendar rightNow = Calendar.getInstance();
		Date now = rightNow.getTime();
		String today = sdf.format(now).toString();
		return today;
	}
	//
	/**
	 * 描述：根据精度格式化对象
	 * @param obj  需要格式化的对象
	 * @param type 配置文件中的对象精度
	 * @return
	 * 返回值：Object
	 */
	public static String getObjByPrecision(Object obj,String type)
	{
		String cfg = ContextUtil.getProperty("remoteaddr.properties", type);
		int precision = 3;
		if(cfg == null)
			precision = 3;
		else 
			precision = Integer.parseInt(cfg);
		String res = null;
		//float tmp = Float.parseFloat(obj.toString());
		String formater = "#.";
		for(int i=1; i<=precision ; i++)
		{
			formater += "0";
		}
		if(obj == null)
			obj = 0.000;
		DecimalFormat format = new DecimalFormat(formater);
		res = format.format(obj);
		return res;
	}
	public static Cache getCache4Mail(){
		try {
			if(cache4Mail == null){
				String path = ContextUtil.class.getResource("/").getPath()+"ehcache.xml";
				//String path = "E:/ehcache.xml";
				CacheManager manager = CacheManager.create(path);
				cache4Mail = manager.getCache("cotMailCache");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cache4Mail;
	}
	
	public static Cache getCache4MailMsg(){
		try {
			if(cache4MailMsg == null){
				String path = ContextUtil.class.getResource("/").getPath()+"ehcache.xml";
				CacheManager manager = CacheManager.create(path);
				cache4MailMsg = manager.getCache("cotMailMsgCache");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cache4Mail;
	}

	public static boolean checkComunication(String orderNo,String type){
		String path = SystemUtil.getRptFilePath()+"/"+type+"/"+orderNo+".pdf";
		File file = new File(path);
		return file.exists();
	}
	public static String getRealPath(){
		String path = ContextUtil.class.getResource("/").getPath();
		try {
			//添加中文解析
			path = URLDecoder.decode(path,"UTF-8");
			path = path.substring(1,path.length()-16);
		} catch (UnsupportedEncodingException e) {
		}
		return path;
	}
	public static boolean sendMail(String orderNo,String type,String emailAddr){
		String smpt = ContextUtil.getProperty("remoteaddr.properties", "wolly_mail_smtp");
		String mailAddr = ContextUtil.getProperty("remoteaddr.properties", "wolly_mail_mailaddr");
		String pwd = ContextUtil.getProperty("remoteaddr.properties", "wolly_mail_pwd");
		SendMailService sendMailService = new SendMailServiceImpl();
		MailCfg cfg = new MailCfg();
		cfg.setHostname(smpt);
		cfg.setPwd(pwd);
		cfg.setUsername("achui_1980");
		MailObject mailContent = new MailObject();
		InternetAddress address = null;
		try {
			address = new InternetAddress(emailAddr);
		} catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		ArrayList<InternetAddress> arr = new ArrayList();
		arr.add(address);
		mailContent.setTo(arr);
		mailContent.setFrom(mailAddr);
		mailContent.setSubject("");
		String msg = " ";
		mailContent.setMsg(msg);
		
		EmailAttachment attachment = new EmailAttachment();
		String path = ContextUtil.getRealPath()+"saverptfile"+File.separator+type+File.separator+orderNo+".pdf";
		attachment.setPath(path);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription(orderNo+".pdf");
		try {
			sendMailService.sendMultiMail(cfg, mailContent,attachment);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static void main(String[] args)
	{
		CotOrderDetail orderDetail = new CotOrderDetail();
		boolean b = PropertyUtils.isReadable(orderDetail, "achui");
		String path = ContextUtil.getRealPath();
		System.out.println(path);
	}

}
