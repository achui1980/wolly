package com.sail.cot.email.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.email.service.MailTemplateService;
import com.zhao.mail.entity.MailPerson;

public class MailTemplateServiceImpl implements MailTemplateService {
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}
	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	//获取当前模版
	public String getCurrentMailTemplate(Integer empsId,String nodeText){
		CotEmps cotEmps = this.getCotEmps(empsId);
		if(nodeText.equals("新邮件信纸")){
			return cotEmps.getEmpsMailTemplate();
		}
		if(nodeText.equals("回复邮件信纸")){
			return cotEmps.getEmpsMailTemplateReply();
		}
		if(nodeText.equals("转发邮件信纸")){
			return cotEmps.getEmpsMailTemplateTransmit();
		}
		return null;
	}
	
	//保存模版
	public void saveCotEmpsMailTemplate(Integer empsId,String template,String nodeText){
		CotEmps cotEmps = this.getCotEmps(empsId);
		cotEmps.setCustomers(null);
		if(nodeText.equals("新邮件信纸")){
			cotEmps.setEmpsMailTemplate(template);	
		}
		if(nodeText.equals("回复邮件信纸")){
			cotEmps.setEmpsMailTemplateReply(template);	
		}
		if(nodeText.equals("转发邮件信纸")){
			cotEmps.setEmpsMailTemplateTransmit(template);	
		}
		List<CotEmps> records =new ArrayList<CotEmps>();
		records.add(cotEmps);
		this.getCotBaseDao().saveOrUpdateRecords(records);
	}
	
	//保存签名
	public boolean savecotEmps(Integer empId,String empsSign){
		System.out.println(empsSign.length());
		if(empsSign.length()>2000){
			return false;
		}
		CotEmps cotEmps=this.getCotEmps(empId);
		cotEmps.setCustomers(null);
		List<CotEmps> records =new ArrayList<CotEmps>();
		cotEmps.setEmpsSign(empsSign);
		
		records.add(cotEmps);
		this.getCotBaseDao().saveOrUpdateRecords(records);
		return true;
	}
	
	public String getFile() throws IOException{
		String path = MailTemplateServiceImpl.class.getResource("/").getPath()+"mailrules/New/design.htm";
		
   	    System.out.println(path);
		File f = new File(path);    
        FileReader fileReader = new FileReader(f);    
        BufferedReader br = new BufferedReader(fileReader);    
        String str;
        String s = "";
        while((str = br.readLine() ) != null)    
        {    
            s=s+str;
        }  
        return s;
	}
	//读取各种模版
	public String getHtmFile(String file,String logo){
		String path = null;
		if(logo.equals("New")){
			path = MailTemplateServiceImpl.class.getResource("/").getPath()+"mailrules/New/"+file+".htm";
		}
		if(logo.equals("Forward")){
			path = MailTemplateServiceImpl.class.getResource("/").getPath()+"mailrules/Forward/"+file+".htm";
		}
		if(logo.equals("Reply")){
			path = MailTemplateServiceImpl.class.getResource("/").getPath()+"mailrules/Reply/"+file+".htm";
		}
		File f = new File(path);    
		if(!f.exists()) return null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        String str;
        String s = "";
        try {
			while((str = br.readLine() ) != null)    
			{    
			    s=s+str;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
		System.out.println(s);
		String realStr = null;
		try {
			realStr = new String(s.getBytes("iso-8859-1"),"utf-8") ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return s;
	}
	
	
	//获得模版  回复和转发
	public String getMailTemplate(Integer empId,String logo,CotMail cotMail){
		try
        {
            Velocity.init();
        }
        catch(Exception e)
        {
        }
        //从员工表读取签名
        String empsSign =this.isExistEmpsSign(empId);
        if(empsSign==null){
        	empsSign="";
        }
        
        String templateStr=this.getEmpsMailTemplate(empId, logo);
        if(templateStr==null ||templateStr.trim().equals("")){
        	if(logo.equals("Reply")){
        		templateStr =this.getHtmFile("纯文本", "Reply");
        	}
        	if(logo.equals("Forward")){
        		templateStr =this.getHtmFile("纯文本", "Forward");
        	}
        };
        VelocityContext context = new VelocityContext();

		if(logo.equals("Reply")){
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
        	context.put("date", df.format(new Date()));
        	context.put("empsSign", empsSign);
        	String sendName = "";
        	if(cotMail.getSendName()!=null)
        		sendName = cotMail.getSendName();
        	else if(cotMail.getSendUrl()!=null)
        		sendName = cotMail.getSendUrl();
        	context.put("sendNewMailName", "<span tage='sendMailToName' style='display:none'>$sendNewMailName</span>");
        	context.put("sendName", sendName);
        	context.put("sendTime", df.format(cotMail.getSendTime()));
        	List<MailPerson> list= cotMail.getTo();
        	String toShow = "";
        	for(int i=0;i<list.size();i++){
        		MailPerson mailPerson=list.get(i);
        		System.out.println(mailPerson.getName());
        		if(mailPerson.getName()==null||mailPerson.getName().trim().equals("")){
        			toShow +=mailPerson.getEmailUrl()+";&nbsp;";
        		}else{
        			toShow +=mailPerson.getName()+"<span style='color:#666666;font-size:10px;'>&nbsp;&lt;"+mailPerson.getEmailUrl()+"&gt;;</span>";
        		}
        	}
        	context.put("toName", toShow);
        	System.out.println(toShow);
        	context.put("ccName", cotMail.getCcName());
        	context.put("subject", cotMail.getSubject());
        	String body=cotMail.getBody();
        	System.out.println(body);
        	if(body.toLowerCase().indexOf("<body")!=-1&&body.toLowerCase().indexOf("</body>")!=-1){
        		int bodyIndex = body.toLowerCase().indexOf("<body");
        		bodyIndex = body.indexOf(">",bodyIndex);
        		int bodyIndexEnd = body.toLowerCase().indexOf("</body>");
        		String data= "<div>"+body.substring(bodyIndex+1,bodyIndexEnd)+"</div>";
        		context.put("body", data);
        	}else{
        		context.put("body",cotMail.getBody());
        	}
		}
		if(logo.equals("Forward")){
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
        	context.put("date", df.format(new Date()));
        	context.put("empsSign", empsSign);
        	context.put("sendNewMailName", "<span tage='sendMailToName' style='display:none'>$sendNewMailName</span>");
        	String sendName = "";
        	if(cotMail.getSendName()!=null)
        		sendName = cotMail.getSendName();
        	else if(cotMail.getSendUrl()!=null)
        		sendName = cotMail.getSendUrl();
        	context.put("sendName", sendName);
        	if(cotMail.getSendTime()!=null)
        		context.put("sendTime", df.format(cotMail.getSendTime()));
        	else if(cotMail.getAddTime()!=null)
        		context.put("sendTime", df.format(cotMail.getAddTime()));
        	List<MailPerson> list= cotMail.getTo();
        	String toShow = "";
        	for(int i=0;i<list.size();i++){
        		MailPerson mailPerson=list.get(i);
        		System.out.println(mailPerson.getName());
        		if(mailPerson.getName()==null||mailPerson.getName().trim().equals("")){
        			toShow +=mailPerson.getEmailUrl()+";&nbsp;";
        		}else{
        			toShow +=mailPerson.getName()+"<span style='color:#666666;font-size:10px;'>&nbsp;&lt;"+mailPerson.getEmailUrl()+"&gt;;</span>";
        		}
        	}
        	context.put("toName", toShow);
        	context.put("ccName", cotMail.getCcName());
        	context.put("subject", cotMail.getSubject());
        	String body=cotMail.getBody();
        	if(body.toLowerCase().indexOf("<body")!=-1&&body.toLowerCase().indexOf("</body>")!=-1){
        		int bodyIndex = body.toLowerCase().indexOf("<body");
        		bodyIndex = body.indexOf(">",bodyIndex);
        		int bodyIndexEnd = body.toLowerCase().indexOf("</body>");
        		String data= "<div>"+body.substring(bodyIndex+1,bodyIndexEnd)+"</div>";
        		context.put("body", data);
        	}else{
        		context.put("body", cotMail.getBody());
        	}
		}
		StringWriter w = new StringWriter();
        try
        {
            Velocity.evaluate( context, w, "template",templateStr);
        }
        catch( ParseErrorException pee )
        {
            System.out.println("ParseErrorException : " + pee );
        }
        catch( MethodInvocationException mee )
        {
            System.out.println("MethodInvocationException : " + mee );
        }
        catch( Exception e )
        {
            System.out.println("Exception : " + e );
        }

        return w.toString();
	   
	}
	
	
	//获得新建模版
	public String getNewMailTemplate(Integer empId,String logo){
		try
        {
            Velocity.init();
        }
        catch(Exception e)
        {
        }
        //从员工表读取签名
        String empsSign =this.isExistEmpsSign(empId);
        if(empsSign==null){
        	empsSign="";
        }
        String templateStr=this.getEmpsMailTemplate(empId, logo);
        if(templateStr==null ||templateStr.trim().equals("")) {
        	if(logo.equals("New")){
        		templateStr =this.getHtmFile("纯文本", "New");
        	}
        }
        VelocityContext context = new VelocityContext();
        if(logo.equals("New")){
        	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
        	context.put("date", df.format(new Date()));
        	context.put("empsSign", empsSign);
        	context.put("sendNewMailName", "<span tage='sendMailToName' style='display:none'>$sendNewMailName</span>");
		}
		StringWriter w = new StringWriter();
        try
        {
            Velocity.evaluate( context, w, "template",templateStr);
        }
        catch( ParseErrorException pee )
        {
            System.out.println("ParseErrorException : " + pee );
        }
        catch( MethodInvocationException mee )
        {
            System.out.println("MethodInvocationException : " + mee );
        }
        catch( Exception e )
        {
            System.out.println("Exception : " + e );
        }

        return w.toString();
	   
	}
	
	//通过ID获取员工对象
	public CotEmps getCotEmps(Integer empId) {
		// TODO Auto-generated method stub
		String sql = " from CotEmps obj where obj.id="+empId;
		List<CotEmps> res = this.getCotBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	//从员工表读取签名
	public String isExistEmpsSign(Integer empId){
		CotEmps cotEmps =this.getCotEmps(empId);
		String sign=cotEmps.getEmpsSign();
		if(sign==null)return null;
		return cotEmps.getEmpsSign();
	}
	
	//从员工表读取模版
	public String getEmpsMailTemplate(Integer empId,String logo){
		CotEmps cotEmps =this.getCotEmps(empId);
		String sign = null;
		if(logo.equals("New")){
			sign=cotEmps.getEmpsMailTemplate();
		}
		if(logo.equals("Reply")){
			sign=cotEmps.getEmpsMailTemplateReply();
		}
		if(logo.equals("Forward")){
			sign=cotEmps.getEmpsMailTemplateTransmit();
		}
		if(sign==null){
			return null;
		}else{
			return sign;
		}
	}
	
	
}
