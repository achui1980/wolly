//                        1.EXT_VIEWPORT
// 11.mailToolbar  12.mailTree  13.mailCard 14.mailAllGrid 15.mailDefault 16.dbMailDefault 17.historyCard 1.8 mailGrid

//             mailDefault
// checkMsg modifyItem prevBtn nextBtn allInfoPanel formPanel bodyPanel attachPanel
EXT_VIEWPORT = '';

EXT_MAIL_PAGE_START = 0;
EXT_MAIL_PAGE_LIMIT = 15;
EXT_MAIL_PAGE_DISPLAY_SIZE = '10|15|20|30|50|100';

EXT_MAIL_HISTORY_PAGE_START = 0;
EXT_MAIL_HISTORY_PAGE_LIMIT = 10;

EXT_MAIL_SEND_PAGE_START = 0;
EXT_MAIL_SEND_PAGE_LIMIT = 10;

EXT_MAIL_ID_OUTBOUND_FROMPANEL = 'EXT_MAIL_ID_OUTBOUND_FROMPANEL';

EXT_MAIL_ID_VIEWPORT = 'EXT_MAIL_ID_VIEWPORT';

EXT_MAIL_MAX_SIZE = 10*1024*1024;

EXT_MAIL_PERSON_MENU = 'EXT_MAIL_PERSON_MENU';

MAIL_CONNECT_NO_ERROR_STATUS = 0;	// 连接正确
MAIL_CONNECT_ERROR_STATUS = 1;	// 连接错误，但是未发现的错误信息
MAIL_CONNECT_ERROR_POP_HOST_STATUS = 2;
MAIL_CONNECT_ERROR_POP_PORT_STATUS = 3;
MAIL_CONNECT_ERROR_SMTP_HOST_STATUS = 4;
MAIL_CONNECT_ERROR_SMTP_PORT_STATUS = 5;
MAIL_CONNECT_ERROR_LOGON_STATUS = 6;
MAIL_CONNECT_ERROR_LOGIN_FAILED_STATUS = 7;
MAIL_CONNECT_ERROR_LOGIN_FAILED_FREQ_STATUS = 8;

// 邮件状态
	// 0：新邮件，1：新邮件、回执，2：已读，3：指派未读，4：指派已读，5：错误邮件 ，6:指派回执未读
	//-1：未发送，-2：已发送，-3：发送失败，-4：群发未发送，-5：群发已发送，-6：群发失败
	//-7:审核通过，-8审核不通过，-9：待审核，-10群发审核通过，-11群发审核不通过,12：群发待审核
MAIL_LOCAL_STATUS_NOREAD = 0;
MAIL_LOCAL_STATUS_NOTIFICATION = 1;
MAIL_LOCAL_STATUS_READ = 2;
MAIL_LOCAL_STATUS_ASSIGNNOREAD = 3;
MAIL_LOCAL_STATUS_ASSIGNREAD = 4;
MAIL_LOCAL_STATUS_ERROR = 5;
MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION = 6;
MAIL_LOCAL_STATUS_NOSEND = -1;
MAIL_LOCAL_STATUS_SENDED = -2;
MAIL_LOCAL_STATUS_SENDERROR = -3;
MAIL_LOCAL_STATUS_NOPARTINGSEND = -4;
MAIL_LOCAL_STATUS_PARTINGSENDED = -5;
MAIL_LOCAL_STATUS_PARTINGSENDERROR = -6;
MAIL_LOCAL_STATUS_CHECKPASS = -7;
MAIL_LOCAL_STATUS_CHECKNOTGO = -8;
MAIL_LOCAL_STATUS_WAITONCHECK = -9;
MAIL_LOCAL_STATUS_PARTINGCHECKPASS = -10;
MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO = -11;
MAIL_LOCAL_STATUS_PARTINGWAITONCHECK = -12;


MAIL_SEND_NO_ERROR_STATUS = 0;
MAIL_SEND_ERROR_STATUS = 1;	// 发送错误，但是未发现的错误信息
MAIL_SEND_CHECK_STATUS = 2;	// 审核

MAIL_SEND_ATTACH_UPLOAD_RG = /[\s\#+&']/g;

MAIL_SEND_TYPE_STATUS_DRATF = 1; // 草稿
MAIL_SEND_TYPE_STATUS_REPLAY = 2; // 回复
MAIL_SEND_TYPE_STATUS_REPLAYALL = 3; // 回复全部
MAIL_SEND_TYPE_STATUS_REPEAT = 4; // 再次发送
MAIL_SEND_TYPE_STATUS_FORWARD = 5; // 转发
MAIL_SEND_TYPE_STATUS_UPDATE = 6; // 修改

// 邮件标记
MAIL_TAG_REPALY = "R";	// 已回复
MAIL_TAG_FORWARD = "F"; // 已转发

// 邮件类型
MAIL_LOCAL_TYPE_SEND = 0;
MAIL_LOCAL_TYPE_DRAFT = 1;
MAIL_LOCAL_TYPE_INBOX = 2;
MAIL_LOCAL_TYPE_CHECK = 3;

