package com.sail.cot.email.service;

import java.util.List;

import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailCache;
/**
 * 邮件缓存表操作
 * @author zhao
 *
 */
public interface MailCacheService {
	/**
	 * 获得邮件缓存表中所有缓存信息
	 * @return
	 */
	public List<CotMailCache> getMailListCache(); 
	/**
	 * 保存缓存信息到邮件缓存表中
	 * @param mailCache
	 */
	public void saveMailCache(String mailAddr,String msgId);
	/**
	 * 审核
	 * @param cotMail
	 * @param empId
	 * @return
	 */
	public boolean getIsMailCheck(CotMail cotMail,Integer empId);
}
