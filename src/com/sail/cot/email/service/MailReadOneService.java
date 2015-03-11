package com.sail.cot.email.service;

import com.sail.cot.domain.CotMail;
/**
 * 获得本地邮件详情
 * @author zhao
 *
 */
public interface MailReadOneService {
	/**
	 * TODO：
	 * 读取本地单封邮件
	 * @param id
	 * @return 如果为空，则为null
	 */
	public CotMail readMailAllInfo(String id);
	/**
	 * 获得发件类型信息，不带附件
	 * @param id
	 * @return
	 */
	public CotMail getSendTypeInfo(String id, String random);
	/**
	 * TODO：
	 * 获得发件类型信息，并对附件记录保存在session，以提供回复
	 * @param id
	 * @param random
	 * @return
	 */
	public CotMail getSendTypeInfoAndAttach(String id, String random);
	
	/**
	 * TODO:
	 * 获得带Excel附件的邮件
	 * @param key 保存在session中的KEY值
	 * @param random
	 * @return 不存在返回null
	 */
	public CotMail getExcel(String key,String random);
}
