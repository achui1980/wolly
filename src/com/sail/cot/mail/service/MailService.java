/**
 * 
 */
package com.sail.cot.mail.service;

import java.util.HashMap;
import java.util.List;
import com.zhao.mail.SendMailService;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotMail; 
import com.sail.cot.query.QueryInfo;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 5, 2008 11:06:09 AM </p>
 * <p>Class Name: MailService.java </p>
 * @author achui
 *
 */
public interface MailService extends SendMailService{
	
	//从内存获取Map
	public  HashMap<?, ?> getMailCfgMap();
	
	//查询数据库获取Map
	public HashMap<?, ?> getMailCfgMapQuery();
	
	//批量发送简单邮件
	public void sendSimpleMailList(List<CotMail> mailList);
	
	//发送单封简单邮件
	public Boolean sendSimpleMail(CotMail mail);
	
	//批量发送带附件邮件
	public void sendMimeMailList(List<CotMail> mailList);
	
	//发送单封带附件邮件
	public Boolean sendMimeMail(CotMail mail);
	
	//获取需要发送邮件列表,且状态不是参数status
	public List<?> getMailListNotByStatus(QueryInfo queryInfo,int status);
	
	//更新代发送邮件状态
	public void updateMailList(List<CotMail> mailList,int status);
	
	//更新代发送邮件状态,失败信息
	public void updateMail(CotMail mail,int status,String errMsg);
	
	//群发邮件保存
	public String saveMail(CotMail mail,int status,String errMsg);
	
	//发送邮件
	public void sendMail(boolean isAttatchment);
	
	//根据状态获取所有邮件
	public List<?> getMailListByStatus(int status);
	
	//保存邮件
	public void saveMailRecords(List<CotMail> mailList);
	
	//根据状态删除邮件邮件
	public void deleteRecords(List<CotMail> mailList,int status,boolean delAll);

	
	//获取邮件列表
	List<?> getMaiList();
	
	//删除邮件
	public void deleteMail(List<CotMail> mailList);
	
	//根据id获取邮件信息
	CotMail getMailById(Integer Id);
	
    public	int getRecordCount(QueryInfo queryInfo);
    
    public	List<?> getList(QueryInfo queryInfo);

	public List<?> getList(QueryInfo queryInfo,int status);
	
	public int getRecordCount(QueryInfo queryInfo,int status);

	public String getFileName(CotMail cotMail);
	
	//添加并发送邮件
	public void addAndSendMail(CotMail mail);
	
	//修改并发送邮件
	public void updateAndSendMail(CotMail mail);
	
	//通过发件人（员工）id获取发件人编号（工号）
	public String getEmpsIdById(Integer id);
	
	//通过id删除邮件附件
	public Boolean delMailFile(String id);
	
	//通过附件路径删除附件
	public void delMailFileByMailPath(String mailPath);
	
	//通过id获取邮件发送状态
	public Long getMailStatusById(Integer id);
	
	//群发邮件
	public void addSendMail(List<CotMail> mailList);
	
	//群发简单邮件
	public Boolean groudSendSimpleMail(CotMail mail);
	
	//群发带附件邮件
	public Boolean groudSendMimeMail(CotMail mail);
	
	//直接保存邮件
	public void saveOrUpdateMail(CotMail mail);
	
	//获取新增邮件的ids
	public List getNewMailIds();
	
	public void sendMessageByThread(String flag,CotMail mail,List<CotMail> mailList);
	//保存群发邮件
	public List<CotMail> saveSendMail(CotMail mail,List<CotCustomer> ids) ;
	
	//获取邮件发送信息
	public String getSendMsg(Integer empId);
}
