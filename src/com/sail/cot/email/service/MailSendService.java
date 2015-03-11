package com.sail.cot.email.service;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
/**
 * 发送邮件到服务器上
 * @author zhao
 *
 */
public interface MailSendService {
	/**
	 * 发送邮件
	 * @param mailContent
	 * @return
	 */
	public int addSendout(CotMail cotMail,String random);

	

	/**
	 * 添加草稿
	 * @param cotMail
	 * @param random
	 * @return
	 */
	public int addDraft(CotMail cotMail,String random);
	/**
	 * 审核发送，通过则发送，并保存在已发送，不通过，发送失败，则保存草稿箱
	 * @param checked 是否通过
	 * @param id 邮件ID
	 * @param msg 审核信息
	 * @return
	 */
	public int checkSend(boolean checked,String id,String msg);

}
