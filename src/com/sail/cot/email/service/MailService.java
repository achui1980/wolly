package com.sail.cot.email.service;

import com.zhao.mail.entity.MailObject;
/**
 * 操作服务器上收件箱邮件
 * @author zhao
 *
 */
public interface MailService {

	/**
	 * 读取公共单封邮件
	 * @param id Message-ID
	 * @return
	 */
	public MailObject readMailDetail(String id);
	/**
	 * 读取私人单封邮件
	 * @param id Message-ID
	 * @return
	 */
	public MailObject readEmpMailDetail(String id);
	
	/**
	 * 删除公共邮件
	 * @param messageIDs
	 * @return
	 */
	public boolean deleteMails(String[] messageIDs);
	/**
	 * 删除私人邮件
	 * @param messageIDs
	 * @return
	 */
	public boolean deleteEmpMails(String[] messageIDs);
	
}
