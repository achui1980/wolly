/**
 * 
 */
package com.sail.cot.mail.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.domain.CotMailCfg;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 7, 2008 5:40:04 PM </p>
 * <p>Class Name: MailReciveServiceImpl.java </p>
 * @author achui
 *
 */
public class MailReciveServiceImpl  {
	
	public void reciveMail() throws Exception
	{
	    Store store=null;
	    Folder folder=null;
	    
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props);
	    store = session.getStore("pop3");
	    store.connect("pop3.163.com", "achui_1980", "344135");
	    folder = store.getFolder("inbox");
	    folder.open(Folder.READ_WRITE);
	    System.out.println( " You have  "   +  folder.getMessageCount()  +   "  messages in inbox " );
        System.out.println( " You hava  "   +  folder.getUnreadMessageCount()  +   "  unread messages in inbox ");
       
        Message []msgs  =  folder.getMessages();
        
        for  ( int  i = 0 ; i  <  msgs.length; i ++ ) {
        	System.out.println("--getReceivedDate--"+msgs[i].getFlags().getUserFlags()[0]);
        	System.out.println("--getSentDate--"+msgs[i].getSentDate());
        	System.out.println("--getFrom--"+msgs[i].getFrom()[0].toString());
        	MailReciveServiceImpl.extractPart(msgs[i]);
         
      }

	}
	  // 解析邮件内容 
    private   static   void  extractPart( final  Part part)  throws  MessagingException, IOException{
        if (part.getContent()  instanceof  Multipart) {
          Multipart mp = (Multipart)part.getContent();
            for ( int  i = 0 ;i < mp.getCount();i ++ ) {
              extractPart(mp.getBodyPart(i));
          } 
           return ;
      } 
      String fileName  =  part.getFileName();
      
        if (fileName  ==   null ) {
            System.out.println("======="+part.getContent());
       }   else   if (fileName  !=   null   &&   ! part.getContentType().startsWith( " text/plain " ))  {
           // 解析附件内容 
         
          InputStream in = part.getInputStream();
           // 保存附件，这里假设了一个文件，实际应该根据保存文件的类型来决定 
          FileOutputStream out  =   new  FileOutputStream( " e:\\att.txt " );

           byte [] buffer = new   byte [ 1024 ];
           int  count = 0 ;
           while ((count = in.read(buffer)) >= 0 ) 
              out.write(buffer, 0 ,count);
          in.close();
      } 
  } 

    public static void main(String[] args)
    {
    	MailReciveServiceImpl impl = new MailReciveServiceImpl();
    	try {
			impl.reciveMail();
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
    }
	public void closeFolder(boolean b) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public void deleteMailsByIndex(int[] msgnum) {
		// TODO Auto-generated method stub
		
	}
	public List<CotMail> getMailListFromMailServ(int start, int perPageCount) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getTotalMailCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Folder openFolder(CotMailCfg cfg) {
		// TODO Auto-generated method stub
		return null;
	}
	public void saveReciveMail(String mailSmtpHost, String mailPop3Host,
			String mailAccount, String Pwd,String flag) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public CotEmps curEmps() {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		return cotEmps;
	}
	public String getFileName(String msgId) {
		// TODO Auto-generated method stub
		return null;
	}
	public CotMail getMailByMsgId(String msgId) {
		// TODO Auto-generated method stub
		return null;
	}
	public void modifyMailStatus(List<CotMail> ids) {
		// TODO Auto-generated method stub
		
	}
	public CotMailAttach getMailAttach(String msgId) {
		// TODO Auto-generated method stub
		return null;
	}
	public void deleteAttach(String msgId) {
		// TODO Auto-generated method stub
	}
	public void deleteRecvMail(List<CotMail> mailRecvList) {
		// TODO Auto-generated method stub	
	}
	public CotMailCfg getCotMailCfg() {
		// TODO Auto-generated method stub
		return null;
	}
	public void modifyMailType(List<CotMail> ids) {
		// TODO Auto-generated method stub
		
	}
	public Integer getCoutUnreadMail() {
		// TODO Auto-generated method stub
		return null;
	}
	public CotEmps getEmpsById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	public void modifyMailRecvByIds(List<CotMail> ids, Integer empId,
			String empName, String empMail) {
		// TODO Auto-generated method stub
		
	}
	public String getMailContents(String msgId) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean checkMailRecv(String msgId) {
		// TODO Auto-generated method stub
		return false;
	}
	public void deleteDefaultMail(List<CotMail> mailRecvList) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.mail.service.MailRecvService#sendMessageByThread(java.lang.String)
	 */
	public void sendMessageByThread(String flag) {
		// TODO Auto-generated method stub
		
	}

}
