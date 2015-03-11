/**
 * 
 */
package com.sail.cot.mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 10, 2008 4:26:47 PM </p>
 * <p>Class Name: Pop3Bean.java </p>
 * @author achui
 *
 */
public class Pop3Bean {
	private int mailCounter; //邮件计数

	  private int mailIndex; //邮件编号，即邮件在messages数组中的位置

	  private int mailDownErrorCounter; //正在下载邮件时，出错的计数器

	  private boolean[] recordFailure; //记录下载出错的邮件的序号

	  private int totalRetryTimes; //总共重试次数

	  private int retryTimeCounter; //记下重试的次数

	  private boolean otherError; //若是在邮件正式下载之前出错，则置该值为true

	  private String extension=".eml"; //文件扩展名

	  private Store store;

	  private Folder folder;

	  private Message[] messages;

	  private Message message;

	  private Part part;

	  private String emlName;

	  private String attachName;

	  private int allMessageCount;

	  private int messageCount;

	  private String dateformat; //默认的日前显示格式

	  //  private String propFile = MailConstants.PROPS_FILE_NAME;//用这个接口类的好处是更改配置文件路径的时候不需要更改每个类

	  private String protocol="pop3"; //服务协议

	  private String mailHost = "smtp.163.com"; //服务器地址

	  private String userName = "achui_1980"; //用户名

	  private String password = "344135"; //密码

	  private String saveAttachPath; //附件下载后的存放目录

	  private String saveEmlPath="E:\\"; //保存eml文件的路径

	  public Pop3Bean() throws IOException
	  {
	    /*   FileProperties fp = new FileProperties(propFile);
	       fp.load();
	       protocol = fp.getProperty(MailConstants.RECV_PROTO);
	       mailHost = fp.getProperty(MailConstants.RECV_HOST);
	       userName = fp.getProperty(MailConstants.RECV_USER);
	       password = fp.getProperty(MailConstants.RECV_PASS);
	       saveAttachPath = fp.getProperty(MailConstants.RECV_ATTACH);
	       saveEmlPath = fp.getProperty(MailConstants.RECV_ROOT);
	       dateformat = fp.getProperty("mail.receive.dtfmat");
	       extension = fp.getProperty("mail.receive.extension");
	       totalRetryTimes = Integer
	               .parseInt(fp.getProperty("mail.receive.retry"));*/
	  }

	  /**
	   * 设置邮件主机
	   */
	  public void setMailHost(String mailHost)
	  {
	    this.mailHost = mailHost;
	  }

	  /**
	   * 获取邮件主机
	   */
	  public String getMailHost()
	  {
	    return this.mailHost;
	  }

	  /**
	   * 设置邮件帐号
	   */
	  public void setUserName(String userName)
	  {
	    this.userName = userName;
	  }

	  /**
	   * 获取邮件帐号
	   */
	  public String getUserName()
	  {
	    return this.userName;
	  }

	  /**
	   * 设置邮件密码
	   */
	  public void setPassword(String password)
	  {
	    this.password = password;
	  }

	  /**
	   * 设置Store
	   */
	  public void setStore(Store store)
	  {
	    this.store = store;
	  }

	  /**
	   * 设置邮箱文件夹
	   */
	  public void setFolder(Folder folder)
	  {
	    this.folder = folder;
	  }

	  /**
	   * 设置messages数组
	   */
	  public void setMessages(Message[] messages)
	  {
	    this.messages = messages;
	  }

	  /**
	   * 设置message
	   */
	  public void setMessage(Message message)
	  {
	    this.message = message;
	  }
	 public void setCurMessage(int i)
	 {
	   this.message = this.messages[i];
	 }
	  /**
	   * 获取message
	   */
	  public Message getMessage()
	  {
	    return this.message;
	  }

	  /**
	   * 获取folder中的message数量
	   *
	   * @throws MessagingException
	   */
	  public int getAllMessageCount() throws MessagingException
	  {
	    this.allMessageCount = folder.getMessageCount();
	    return allMessageCount;
	  }

	  /**
	   * 设置allMessageCount
	   *
	   * @throws MessagingException
	   */
	  private void setAllMessageCount() throws MessagingException
	  {
	    this.allMessageCount = this.folder.getMessageCount();
	  }

	  /**
	   * 获取messages中message的数量
	   *
	   * @return
	   */
	  public int getMessageCount()
	  {
	    this.messageCount = this.messages.length;
	    return messageCount;
	  }

	  /**
	   * 获得folder中新邮件的数量
	   *
	   * @return
	   * @throws MessagingException
	   */
	  public int getNewMessageCount() throws MessagingException
	  {
	    return this.folder.getNewMessageCount();
	  }

	  /**
	   * 获得folder中未读邮件的数量
	   *
	   * @return
	   * @throws MessagingException
	   */
	  public int getUnreadMessageCount() throws MessagingException
	  {
	    return this.folder.getUnreadMessageCount();
	  }

	  /**
	   * 获取Part
	   */
	  public Part getPart()
	  {
	    return (Part) message;
	  }

	  /**
	   * 设置Part
	   */
	  public void setPart(Part part)
	  {
	    this.part = part;
	  }

	  /**
	   * 设置附件存放路径
	   */

	  public void setAttachPath(String attachPath)
	  {
	    this.saveAttachPath = attachPath;
	  }

	  /**
	   * 获得附件存放路径
	   */

	  public String getAttachPath()
	  {
	    return saveAttachPath;
	  }

	  /**
	   * 设置eml存放路径
	   */

	  public void setEmlPath(String emlPath)
	  {
	    this.saveEmlPath = emlPath;
	  }

	  /**
	   * 获得eml存放路径
	   */

	  public String getEmlPath()
	  {
	    return saveEmlPath;
	  }

	  public void setEmlName(String emlName)
	  {
	    this.emlName = emlName;
	  }

	  public String getEmlName()
	  {
	    return emlName;
	  }

	  public void setAttachName(String attachName)
	  {
	    this.attachName = attachName;
	  }

	  public String getAttachName()
	  {
	    return attachName;
	  }

	  public void setExtension(String extension)
	  {
	    this.extension = extension;
	  }

	  public String getExtension()
	  {
	    return extension;
	  }

	  /**
	   * 设置日期显示格式
	   */

	  public void setDateFormat(String format) throws Exception
	  {
	    this.dateformat = format;
	  }

	  /**
	   * 获取日期显示格式
	   */
	  public String getDateFormat(String format) throws Exception
	  {
	    return this.dateformat;
	  }

	  /**
	   * 获得发件人的地址和姓名
	   *
	   * @throws Exception
	   */
	  public String getFrom() throws Exception
	  {
	    return getFrom(this.message);
	  }

	  public String getFrom(Message mimeMessage) throws Exception
	  {
	    InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
	    String from = address[0].getAddress();
	    if (from == null)
	      from = "";
	    String personal = address[0].getPersonal();
	    if (personal == null)
	      personal = "";
	    String fromaddr = personal + "<" + from + ">";
	    return fromaddr;
	  }

	  /**
	   * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 * "to"----收件人 "cc"---抄送人地址
	   * "bcc"---密送人地址
	   */
	  public String getTOAddress() throws Exception
	  {
	    return getMailAddress("TO", this.message);
	  }

	  public String getCCAddress() throws Exception
	  {
	    return getMailAddress("CC", this.message);
	  }

	  public String getBCCAddress() throws Exception
	  {
	    return getMailAddress("BCC", this.message);
	  }

	  public String getTOAddress(Message mimeMessage) throws Exception
	  {
	    return getMailAddress("TO", mimeMessage);
	  }

	  public String getCCAddress(Message mimeMessage) throws Exception
	  {
	    return getMailAddress("CC", mimeMessage);
	  }

	  public String getBCCAddress(Message mimeMessage) throws Exception
	  {
	    return getMailAddress("BCC", mimeMessage);
	  }

	  public String getMailAddress(String type) throws Exception
	  {
	    return getMailAddress(type, this.message);
	  }

	  public String getMailAddress(String type, Message mimeMessage) throws Exception
	  {
	    String mailaddr = "";
	    String addtype = type.toUpperCase();
	    InternetAddress[] address = null;
	    if (addtype.equals("TO") || addtype.equals("CC")
	        || addtype.equals("BCC"))
	    {
	      if (addtype.equals("TO"))
	      {
	        address = (InternetAddress[]) mimeMessage
	            .getRecipients(Message.RecipientType.TO);
	      }
	      else if (addtype.equals("CC"))
	      {
	        address = (InternetAddress[]) mimeMessage
	            .getRecipients(Message.RecipientType.CC);
	      }
	      else
	      {
	        address = (InternetAddress[]) mimeMessage
	            .getRecipients(Message.RecipientType.BCC);
	      }
	      if (address != null)
	      {
	        for (int i = 0; i < address.length; i++)
	        {
	          String email = address[i].getAddress();
	          if (email == null)
	            email = "";
	          else
	          {
	            email = MimeUtility.decodeText(email);
	          }
	          String personal = address[i].getPersonal();
	          if (personal == null)
	            personal = "";
	          else
	          {
	            personal = MimeUtility.decodeText(personal);
	          }
	          String compositeto = personal + "<" + email + ">";
	          mailaddr += "," + compositeto;
	        }
	        mailaddr = mailaddr.substring(1);
	      }
	    }
	    else
	    {
	      throw new Exception("Error emailaddr type!");
	    }
	    return mailaddr;
	  }

	  /**
	   * 获得邮件主题
	   */
	  public String getSubject() throws MessagingException
	  {
	    return getSubject(this.message);
	  }

	  public String getSubject(Message mimeMessage) throws MessagingException
	  {
	    String subject = "";
	    try
	    {
	      subject = MimeUtility.decodeText(mimeMessage.getSubject());
	      if (subject == null)
	        subject = "";
	    }
	    catch (Exception exce)
	    {
	    }
	    return subject;
	  }

	  /**
	   * 获得邮件发送日期
	   */
	  public String getSentDate() throws Exception
	  {
	    return getSentDate(this.message);
	  }

	  public String getSentDate(Message mimeMessage) throws Exception
	  {
	    Date sentdate = mimeMessage.getSentDate();
	    SimpleDateFormat format = new SimpleDateFormat(dateformat);
	    return format.format(sentdate);
	  }

	  /**
	   * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
	   */
	  public boolean getReplySign() throws MessagingException
	  {
	    return getReplySign(this.message);
	  }

	  public boolean getReplySign(Message mimeMessage) throws MessagingException
	  {
	    boolean replysign = false;
	    String needreply[] = mimeMessage
	        .getHeader("Disposition-Notification-To");
	    if (needreply != null)
	    {
	      replysign = true;
	    }
	    return replysign;
	  }

	  /**
	   * 获得此邮件的Message-ID
	   */
	  public String getMessageId() throws MessagingException
	  {
	    return getMessageId(this.message);
	  }

	  public String getMessageId(Message mimeMessage) throws MessagingException
	  {
	    return ( (MimeMessage) mimeMessage).getMessageID();
	  }

	  /**
	   * 初始化出错邮件数组
	   *
	   */
	  private void setRecordFailure()
	  {
	    this.recordFailure = new boolean[getMessageCount()];
	  }

	  /**
	   * 返回出错数组
	   *
	   * @return
	   */
	  public boolean[] getRecordFailure()
	  {
	    return this.recordFailure;
	  }

	  /**
	   * 判断此邮件是否已读，如果未读返回返回false,反之返回true
	   */
	  public boolean isNew() throws MessagingException
	  {
	    return isNew(this.message);
	  }

	  /**
	   * 判断此邮件是否已读，如果未读返回返回false,反之返回true
	   */
	  public boolean isNew(Message mimeMessage) throws MessagingException
	  {
	    boolean isnew = false;
	    Flags flags = mimeMessage.getFlags();
	    Flags.Flag[] flag = flags.getSystemFlags();
	    for (int i = 0; i < flag.length; i++)
	    {
	      if (flag[i] == Flags.Flag.SEEN)
	      {
	        isnew = true;
	        break;
	      }
	    }
	    System.out.println("isnew:"+isnew);
	    return isnew;
	  }

	  /**
	   * 判断此邮件是否包含附件
	   */
	  public boolean isContainAttach() throws Exception
	  {
	    return isContainAttach(this.part);
	  }

	  /**
	   * 判断此邮件是否包含附件
	   */
	  public boolean isContainAttach(Part part) throws Exception
	  {
	    boolean attachflag = false;
	    String contentType = part.getContentType();
	    if (part.isMimeType("multipart/*"))
	    {
	      Multipart mp = (Multipart) part.getContent();
	      for (int i = 0; i < mp.getCount(); i++)
	      {
	        BodyPart mpart = mp.getBodyPart(i);
	        String disposition = mpart.getDisposition();
	        if ( (disposition != null)
	            && ( (disposition.equals(Part.ATTACHMENT)) || (disposition
	            .equals(Part.INLINE))))
	          attachflag = true;
	        else if (mpart.isMimeType("multipart/*"))
	        {
	          attachflag = isContainAttach( (Part) mpart);
	        }
	        else
	        {
	          String contype = mpart.getContentType();
	          if (contype.toLowerCase().indexOf("application") != -1)
	            attachflag = true;
	          if (contype.toLowerCase().indexOf("name") != -1)
	            attachflag = true;
	        }
	      }
	    }
	    else if (part.isMimeType("message/rfc822"))
	    {
	      attachflag = isContainAttach( (Part) part.getContent());
	    }
	    return attachflag;
	  }

	  /**
	   * 连到server，创建folder对象，创建message对象
	   */
	  public void getConn()
	  {
	    try
	    {
	      this.getStoreFromServer();
	      this.getFolderFromStore();
	    }
	    catch (Exception e)
	    {
	      otherError = true;
	      mailDownErrorCounter++;
	      System.out.print(e.getLocalizedMessage());
	    }
	  }

	  /**
	   * 建立Store连接
	   */
	  private Store getStoreFromServer() throws Exception
	  {
	    //创建session
	    Session session = Session.getDefaultInstance(System.getProperties(),
	        null);
	    //session.setDebug(true);

	    //创建store,建立连接
	    Store store = session.getStore(protocol);
	    System.out.println("connecting");
	    store.connect(mailHost, userName, password);
	    System.out.println("connected successfully");
	    setStore(store);
	    return store;
	  }

	  /**
	   * 打开INBox文件夹
	   */
	  private Folder getFolderFromStore()
	  {
	    //打开邮件相应文件夹
	    Folder getFolder;
	    try
	    {
	      getFolder = store.getFolder("INBOX");
	      getFolder.open(Folder.READ_ONLY);
	      setFolder(getFolder);
	      return getFolder;
	    }
	    catch (MessagingException e)
	    {
	      // TODO Auto-generated catch block
	      System.err.println("获取Folder失败！");
	      e.printStackTrace();
	    }
	    return null;
	  }

	  /**
	   * 从folder中提取所有的messages
	   *
	   * @throws MessagingException
	   */
	  public void getAllMessages() throws MessagingException
	  {
	    //从邮件文件夹获取邮件信息
	    Message[] messages = folder.getMessages();
	    setMessages(messages);
	    setRecordFailure(); //初始化出错数组
	    //        setMessageCount();
	  }

	  /**
	   * 获得messageNums数组指定的message
	   *
	   * @param messageNums
	   * @throws MessagingException
	   */
	  public void getMessages(int[] messageNums) throws MessagingException
	  {
	    Message[] messages = folder.getMessages(messageNums);
	    setMessages(messages);
	    setRecordFailure(); //初始化出错数组
	    //        setMessageCount();
	  }

	  /**
	   * 获得start和end之间的message
	   *
	   * @param start
	   * @param end
	   * @throws MessagingException
	   */
	  public void getMessages(int start, int end) throws MessagingException
	  {
	    Message[] messages = folder.getMessages(start, end);
	    setMessages(messages);
	    setRecordFailure(); //初始化出错数组
	    //        setMessageCount();
	  }

	  /**
	   * 关闭连接
	   */
	  public void closeConnection()
	  {
	    try
	    {
	      messages = null;
	      message = null;
	      if (folder.isOpen())
	        folder.close(true);
	      store.close();
	      System.out.println("close");
	    }
	    catch (Exception e)
	    {
	      System.out.println("关闭和邮件服务器之间连接时出错！");
	      e.printStackTrace();
	    }
	  }

	  /**
	   * 获得当前邮件的基本方法 Pop3Bean内部应该调用这个方法 以便在调用函数中加入重试机制
	   *
	   * @throws MessagingException
	   * @throws MessagingException
	   *
	   */
	  public void getMail() throws Throwable
	  { //抛出异常，用以重掷
	    try
	    {
	      saveMessageAs(message);
	      parseMessage(message);
	    }
	    catch (IOException e)
	    {
	      // TODO Auto-generated catch block
	      System.err.println("保存邮件出错，检查保存路径");
	      throw new IOException("保存邮件出错，检查保存路径");
	    }
	    catch (MessagingException e)
	    {
	      // TODO Auto-generated catch block
	      System.err.println("邮件转换出错");
	      throw new MessagingException("邮件转换出错");
	    }
	    catch (Exception e)
	    {
	      System.err.println("未知错误");
	      otherError = true;
	      e.printStackTrace();
	      throw new Exception("未知错误");
	    }
	  }

	  /**
	   * 获得指定的邮件
	   *
	   * @param index
	   */
	  public void getMail(int index)
	  {
	    mailDownErrorCounter = 0; //邮件下载出错计数器置零
	    try
	    { //获取邮件下载之前的错误
	      setMessage(messages[index]); //设置当前message
	      System.out.println("正在获取第" + index + "封邮件. . .");
	      getMail(); //获取当前message
	      System.out.println("成功获取第" + index + "封邮件");
	    }
	    catch (Throwable e)
	    { //获得重掷异常
	      recordFailure[index] = true;
	      mailDownErrorCounter++;
	      System.err.println("下载第" + index + "封邮件时出错");
	      retry();
	    }
	  }

	  /**
	   * 获取messages中的所有邮件
	   */
	  public void getAllMail()
	  {
	    int mailArrayLength; //将要下载的邮件的数量。若是重试时，则为还未下载的邮件数量

	    mailArrayLength = getMessageCount();

	    System.out.println("一共有邮件" + mailArrayLength + "封");

	    mailDownErrorCounter = 0; //邮件下载出错计数器置零
	    mailCounter = 0;
	    for (int index = 0; index < mailArrayLength; index++)
	    {
	      try
	      {
	        setMessage(messages[index]); //设置当前message
	        System.out.println("正在获取第" + index + "封邮件. . .");
	        getMail(); //获取当前message
	        System.out.println("成功获取第" + index + "封邮件");
	        mailCounter++;
	      }
	      catch (Throwable e)
	      {
	        otherError = false;
	        recordFailure[index] = true;
	        mailDownErrorCounter++;
	        System.err.println("下载第" + index + "封邮件时出错");
	      }
	    }
	    System.out.println("成功下载" + mailCounter + "封邮件");
	    mailCounter = 0;
	    if (mailDownErrorCounter != 0)
	      retry();
	  }

	  /**
	   * 保存邮件源文件
	   */

	  public void saveMessageAs(Message message)
	  {
	    String oriFileName;
	    String fileExtend;

	    try
	    {
	      oriFileName = getInfoBetweenBrackets(getMessageId(message)
	          .toString());
	      //设置文件后缀名。若是附件则设法取得其文件后缀名作为将要保存文件的后缀名，若是正文部分则用.htm做后缀名
	      String emlName = oriFileName;
	      String fileNameWidthExtension = getEmlPath() + oriFileName
	          + getExtension();
	      File storeFile = new File(fileNameWidthExtension);
	      for (int i = 0; storeFile.exists(); i++)
	      {
	        emlName = oriFileName + i;
	        fileNameWidthExtension = getEmlPath() + emlName
	            + getExtension();
	        storeFile = new File(fileNameWidthExtension);
	      }
	      setEmlName(emlName);
	      System.out.println("storefile's path: " + fileNameWidthExtension);
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      message.writeTo(baos);
	      StringReader in = new StringReader(baos.toString());
	      saveFile(fileNameWidthExtension, in);
	    }
	    catch (MessagingException e)
	    {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	    catch (Exception e)
	    {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }

	  /*
	   * 解析邮件
	   */
	  public void parseMessage(Message message) throws IOException,
	      MessagingException
	  {
		  
		  //message.setFlag(Flags.Flag.DELETED,true);
	    Object content = message.getContent();
	    if (content instanceof Multipart)
	    {
	      handleMultipart( (Multipart) content);
	    }
	    else
	    {
	      handlePart(message);
	    }
	   
	    this.isNew(message);
	  }

	  /*
	   * 解析Multipart
	   */
	  public void handleMultipart(Multipart multipart) throws MessagingException,
	      IOException
	  {
	    for (int i = 0, n = multipart.getCount(); i < n; i++)
	    {
	      handlePart(multipart.getBodyPart(i));
	    }
	  }

	  /*
	   * 解析指定part,从中提取文件
	   */
	  public void handlePart(Part part) throws MessagingException, IOException
	  {
	    String disposition = part.getDisposition(); // Find attachment
	    String contentType = part.getContentType();
	    String str;
	    InputStreamReader sbis = new InputStreamReader(part.getInputStream());
	    if (disposition == null)
	    { // When just body
	      System.out.println("Null: " + contentType);
	      // Check if plain
	      if ( (contentType.length() >= 9)
	          && (contentType.toLowerCase().substring(0, 9)
	          .equals("text/plai")))
	      {

	        System.out.println(getAttachPath() + getEmlName() + ".txt");
	        saveFile(getAttachPath() + getEmlName() + ".txt", sbis);
	      }
	      else if ( (contentType.length() >= 8) // Check if html
	          && (contentType.toLowerCase().substring(0, 8)
	          .equals("text/htm")))
	      {
	        saveFile(getAttachPath() + getEmlName() + ".html", sbis);
	      }
	      else if ( (contentType.length() >= 9) // Check if html
	          && (contentType.toLowerCase().substring(0, 9)
	          .equals("image/gif")))
	      {
	        saveFile(getAttachPath() + getEmlName() + ".gif", sbis);
	      }
	      else if ( (contentType.length() >= 10)
	          && contentType.toLowerCase().substring(0, 10).equals(
	          "multipart/"))
	      { // Check if multipart
	        System.out.println("multipart body: " + contentType);
	        Multipart mp = (Multipart) (part.getContent());
	        handleMultipart(mp);
	      }
	      else
	      { // Unknown type
	        System.out.println("Other body: " + contentType);
	        saveFile(getAttachPath() + getEmlName() + ".txt", sbis);
	      }
	    }
	    else if (disposition.equalsIgnoreCase(Part.ATTACHMENT))
	    {
	      System.out.println("Attachment: " + part.getFileName() + " : "
	          + contentType);
	      //outToFile.println("Attachment: " + part.getFileName() + " : "
	      //        + contentType);
	      saveFile(getAttachPath() + part.getFileName(), sbis);
	    }
	    else if (disposition.equalsIgnoreCase(Part.INLINE))
	    {
	      System.out.println("Inline: " + part.getFileName() + " : "
	          + contentType);
	      //outToFile.println("Inline: " + part.getFileName() + " : "
	      //        + contentType);
	      saveFile(getAttachPath() + part.getFileName(), sbis);
	    }
	    else
	    { // Should never happen
	      System.out.println("Other: " + disposition);
	      //            outToFile.println("Other: " + disposition);
	    }
	  }

	  public void saveFile(String fileName, Reader input) throws IOException
	  {
	    if (fileName == null)
	    {
	      fileName = File.createTempFile(getAttachPath() + "xx", ".out")
	          .getName();
	    }
	    // Do no overwrite existing file
	    File file = new File(fileName);
	    int lastDot = fileName.lastIndexOf(".");
	    String extension = fileName.substring(lastDot);
	    String fullFileName = fileName;
	    fileName = fileName.substring(0, lastDot);
	    for (int i = 0; file.exists(); i++)
	    {
	      file = new File(fileName + i + extension);
	    }
	    FileWriter fos = new FileWriter(file);
	    BufferedWriter bos = new BufferedWriter(fos);
	    BufferedReader bis = new BufferedReader(input);
	    int aByte;
	    while ( (aByte = bis.read()) != -1)
	    {
	      bos.write(aByte);
	    }
	    bos.flush();
	    bos.close();
	    bis.close();
	  }

	  public void readEmlFile(String fileName) throws MessagingException
	  {
	    try
	    {
	      //TODO readEmlFile
	      InputStream fis = new FileInputStream(fileName);
	      Object emlObj = (Object) fis;
	      Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
	      MimeMessage msg = new MimeMessage(mailSession, fis);
	      message = msg;
	    }
	    catch (FileNotFoundException e)
	    {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }

	  private String getInfoBetweenBrackets(String str) throws Exception
	  {
	    int i, j; //用于标识字符串中的"<"和">"的位置
	    if (str == null)
	    {
	      str = "error";
	      return str;
	    }
	    i = str.lastIndexOf("<");
	    j = str.lastIndexOf(">");
	    if (i != -1 && j != -1)
	      str = str.substring(i + 1, j);
	    return str;
	  }

	  //当有邮件无法下载时进行重试
	  private void retry()
	  {
	    mailCounter = 0;
	    while (retryTimeCounter < totalRetryTimes && mailDownErrorCounter != 0)
	    {
	      if (!store.isConnected() || !folder.isOpen())
	      {
	        System.err.println("与服务器连接断开，请重新连接");
	        closeConnection();
	        return;
	      }

	      System.out.println("第" + (retryTimeCounter + 1) + "次重试");

	      mailDownErrorCounter = 0; //邮件下载出错计数器置零

	      for (int index = 0; index < getMessageCount(); index++)
	      {
	        if (recordFailure[index])
	        {
	          try
	          {
	            setMessage(messages[index]); //设置当前message
	            System.out.println("正在获取第" + index + "封邮件. . .");
	            getMail(); //获取当前message
	            System.out.println("成功获取第" + index + "封邮件");
	            mailCounter++;
	            recordFailure[index] = false;
	          }
	          catch (Throwable e)
	          {
	            otherError = false;
	            recordFailure[index] = true;
	            mailDownErrorCounter++;
	            System.err.println("重新下载第" + index + "封邮件时出错");
	          }
	        }
	      }
	      retryTimeCounter++;
	    }
	    System.out.println("成功下载" + mailCounter + "封邮件");
	    mailCounter = 0; //将邮件计数置零
	    mailDownErrorCounter = 0; //下载错误数量归零
	  }

	      public static void main(String[] args) throws Throwable
	      {

	        try
	        {
	          Pop3Bean mail;
	          mail = new Pop3Bean();
	          mail.setUserName("achui_1980");
	          mail.setMailHost("pop3.163.com");
	          mail.setPassword("344135");
	          mail.setAttachPath("e:/");
	          mail.setExtension(".eml");
	          mail.setDateFormat("yyyydddd");

	          mail.getConn();
	          System.out.println("Count of messages in folder: " + mail.getAllMessageCount());
	          System.out.println("Count of new messages in folder: " + mail.getNewMessageCount());
	          System.out.println("Count of unread messages in folder: " + mail.getUnreadMessageCount());
	          mail.getAllMessages();
	          System.out.println("Count of loaded messages: " + mail.getMessageCount());
	          mail.getAllMail();
	          mail.setCurMessage(0);
	         System.out.println(mail.getSubject());
	          mail.closeConnection();
	  //   ReadEml mail = new ReadEml("H:\\My_Soft_Works\\java\\jteam\\jmail\\received\\41C95D0F.008CD1.01099.eml");

	        }
	        catch (Exception e)
	        {
	          System.out.println("出现未预料的错误！");
	          e.printStackTrace();
	        }
	      }


}
