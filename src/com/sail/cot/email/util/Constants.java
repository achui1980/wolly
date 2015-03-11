package com.sail.cot.email.util;

/**
 * 邮件常量
 * @author zhao
 *
 */
public class Constants {
	public static final String BASICSERVLET_METHOD_NAME = "m";
	
	// 公共邮箱
	public static final String MAIL_CONFIG_INFO = "MAIL_CONFIG_INFO";
	public static final String MAIL_SERVER = "EMAIL_SERVER";
	public static final String MAIL_RECIVE_SERVER = "EMAIL_RECIVE_SERVER";
	public static final String MAIL_SEND_SERVER = "MAIL_SEND_SERVER";
	
	// 私人邮箱
	public static final String MAIL_EMP_SERVER = "EMAIL_EMP_SERVER";
	public static final String MAIL_EMP_RECIVE_SERVER = "EMAIL_EMP_RECIVE_SERVER";
	public static final String MAIL_EMP_SEND_SERVER = "MAIL_EMP_SEND_SERVER";
	
	// 邮件类型
	public static final int MAIL_LOCAL_TYPE_SEND = 0;
	public static final int MAIL_LOCAL_TYPE_DRAFT = 1;
	public static final int MAIL_LOCAL_TYPE_INBOX = 2;
	public static final int MAIL_LOCAL_TYPE_CHECK = 3;
	// 邮件状态
	// 0：新邮件，1：新邮件、回执，2：已读，3：指派未读，4：指派已读，5：错误邮件 ，6:指派回执未读
	//-1：未发送，-2：已发送，-3：发送失败，-4：群发未发送，-5：群发已发送，-6：群发失败
	//-7:审核通过，-8审核不通过，-9：待审核，-10群发审核通过，-11群发审核不通过,-12：群发待审核
	public static final int MAIL_LOCAL_STATUS_NOREAD = 0;
	public static final int MAIL_LOCAL_STATUS_NOTIFICATION = 1;
	public static final int MAIL_LOCAL_STATUS_READ = 2;
	public static final int MAIL_LOCAL_STATUS_ASSIGNNOREAD = 3;
	public static final int MAIL_LOCAL_STATUS_ASSIGNREAD = 4;
	public static final int MAIL_LOCAL_STATUS_ERROR = 5;
	public static final int MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION = 6;
	public static final int MAIL_LOCAL_STATUS_NOSEND = -1;
	public static final int MAIL_LOCAL_STATUS_SENDED = -2;
	public static final int MAIL_LOCAL_STATUS_SENDERROR = -3;
	public static final int MAIL_LOCAL_STATUS_NOPARTINGSEND = -4;
	public static final int MAIL_LOCAL_STATUS_PARTINGSENDED = -5;
	public static final int MAIL_LOCAL_STATUS_PARTINGSENDERROR = -6;
	public static final int MAIL_LOCAL_STATUS_CHECKPASS = -7;
	public static final int MAIL_LOCAL_STATUS_CHECKNOTGO = -8;
	public static final int MAIL_LOCAL_STATUS_WAITONCHECK = -9;
	public static final int MAIL_LOCAL_STATUS_PARTINGCHECKPASS = -10;
	public static final int MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO = -11;
	public static final int MAIL_LOCAL_STATUS_PARTINGWAITONCHECK = -12;
	
	
	public final static String MAIL_ATTACH_UPLOAD_PATH = "mail/attach/temp/send";
	public final static String MAIL_ATTACH_EXCEL_PATH = "mail/attach/temp/excel";
	public final static String MAIL_ATTACH_EML_PATH = "mail/attach/temp/eml";
	public final static String MAIL_ATTACH_SAVE_INBOX_PATH = "mail/attach/save/inbox";
	public final static String MAIL_ATTACH_SAVE_SEND_PATH = "mail/attach/save/send";
	public final static String MAIL_ATTACH_SAVE_EML_PATH = "mail/attach/save/eml";
	// 邮件字段分割符
	public final static String MAIL_FIELD_DELIMITER = "%#&*^!;";
	public final static String MAIL_FIELD_DELIMITER_REGEX = "%#&\\*\\^!;";
	// 邮箱类型
	public final static String MAIL_TREE_FLAG_GINBOX = "G";
	public final static String MAIL_TREE_FLAG_INBOX = "R";
	public final static String MAIL_TREE_FLAG_SEND = "S";
	public final static String MAIL_TREE_FLAG_DRATF = "C";
	public final static String MAIL_TREE_FLAG_DEL = "D";
	public final static String MAIL_TREE_FLAG_CHECK = "P";
	
	// 邮件标记
	public final static String MAIL_TAG_REPALY = "R";	// 已回复
	public final static String MAIL_TAG_FORWARD = "F";	// 已转发
	
	// 邮件连接错误信息
	public final static String MAIL_CONNECT_ERROR_POP_HOST = "Connect failed";
	public final static String MAIL_CONNECT_ERROR_POP_PORT = "Connection refused: connect";
	public final static String MAIL_CONNECT_ERROR_SMTP_HOST = "Unknown SMTP host";
	public final static String MAIL_CONNECT_ERROR_SMTP_PORT = "Could not connect to SMTP host";
	public final static String MAIL_CONNECT_ERROR_LOGON = "Unable to log on";
	public final static String MAIL_CONNECT_ERROR_LOGIN_OR_PASSWORD = "Invalid login or password";
	public final static String MAIL_CONNECT_ERROR_LOGON_FAILED = "Login failed";
	public final static String MAIL_CONNECT_ERROR_LOGON_FAILED_FREQ = "Login failed <Freq>";
	
	// 邮件连接错误代号
	public final static int MAIL_CONNECT_NO_ERROR_STATUS = 0;	// 连接正确
	public final static int MAIL_CONNECT_ERROR_STATUS = 1;	// 连接错误，但是未发现的错误信息
	public final static int MAIL_CONNECT_ERROR_POP_HOST_STATUS = 2;
	public final static int MAIL_CONNECT_ERROR_POP_PORT_STATUS = 3;
	public final static int MAIL_CONNECT_ERROR_SMTP_HOST_STATUS = 4;
	public final static int MAIL_CONNECT_ERROR_SMTP_PORT_STATUS = 5;
	public final static int MAIL_CONNECT_ERROR_LOGIN_STATUS = 6;
	public final static int MAIL_CONNECT_ERROR_LOGIN_FAILED_STATUS = 7;
	public final static int MAIL_CONNECT_ERROR_LOGIN_FAILED_FREQ_STATUS = 8;
	
	
	public final static int MAIL_SEND_TYPE_STATUS_DRATF = 1; // 草稿
	public final static int MAIL_SEND_TYPE_STATUS_REPLAY = 2; // 回复
	public final static int MAIL_SEND_TYPE_STATUS_REPLAYALL = 3; // 回复全部
	public final static int MAIL_SEND_TYPE_STATUS_REPEAT = 4; // 再次发送
	public final static int MAIL_SEND_TYPE_STATUS_FORWARD = 5; // 转发
	public final static int MAIL_SEND_TYPE_STATUS_UPDATE = 6; // 审核修改
	
	public final static int MAIL_SEND_NO_ERROR_STATUS = 0;
	public final static int MAIL_SEND_ERROR_STATUS = 1;	// 发送错误，但是未发现的错误信息
	public final static int MAIL_SEND_CHECK_STATUS = 2;	// 审核
	
	//邮件缓存保存关键字
	public final static String MAIL_CACHE_KEY = "MAIL_CACHE";
	/**资源文件路径*/
	public static final String MAIL_ATTACH_PATH_KEY = "attach_path";
	/**邮件上传存放附件的名字所替换的正则*/
	public static final String MAIL_SEND_ATTACH_UPLOAD_RG = "[ #+&']";
	
}
