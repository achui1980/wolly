/**
 * 
 */
package com.sail.cot.mail.service;

import java.util.List;

import javax.mail.Folder;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.domain.CotMailCfg;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 7, 2008 5:39:38 PM </p>
 * <p>Class Name: MailRecvService.java </p>
 * @author achui
 *
 */
public interface MailRecvService {
	
	public void saveReciveMail(String mailSmtpHost,String mailPop3Host,String mailAccount,String Pwd,String flag,CotEmps cotEmps) throws Exception;
	
	public Folder openFolder(CotMailCfg cfg) throws Exception;
	
	public void closeFolder(boolean b) throws Exception;
	
	public List<CotMail> getMailListFromMailServ(int start,int perPageCount);
	
	public int getTotalMailCount();
	
	public void deleteMailsByIndex(int[] msgnum);
	//当前登陆员工
	public CotEmps curEmps();
	//根据id获取信息
	public CotMail getMailByMsgId(String msgId);
	//获取附件名称
	public String getFileName(String msgId);
	//删除收信箱中的邮件
	public void modifyMailStatus(List<CotMail> ids);
	//获取附件
	public CotMailAttach getMailAttach(String msgId);	
	//从废件箱删除邮件
	public void deleteRecvMail(List<CotMail> mailRecvList);
	//删除附件
	public void deleteAttach(String msgId);
	//获取邮箱默认配置信息
	public CotMailCfg getCotMailCfg();	
	//删除收信箱中的邮件
	public void modifyMailType(List<CotMail> ids);
	//获取未读邮件数量
	public Integer getCoutUnreadMail();
	//根据id获得员工信息
	public CotEmps getEmpsById(Integer id);
	//邮件指派
	public void modifyMailRecvByIds(List<CotMail> ids,Integer empId,String empName,String empMail);
	//获取邮件内容
	public String getMailContents(String msgId);
	//判断查看邮件者是否为邮件接收人
	public boolean checkMailRecv(String msgId);
	//从公共邮件箱删除邮件
	public void deleteDefaultMail(List<CotMail> mailRecvList);
	
	public void sendMessageByThread(String flag,CotEmps cotEmps);
	
	//获取邮件附件
	public String[] getMailAttatch(String msgId,String empName) throws Exception;
	//
	public String getRecvMsg(Integer empId,boolean assign);
}
