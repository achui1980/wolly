/**
 * 
 */
package com.sail.cot.email.util;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;

import com.sail.cot.domain.CotMailCache;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.Log4WebUtil;


/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 23, 2010 2:15:17 PM </p>
 * <p>Class Name: MailCacheUtil.java </p>
 * @author achui
 *
 */
public class MailCacheUtil {
	private static Logger logger = Log4WebUtil.getLogger(MailCacheUtil.class);
	/**
	 * 描述： 将邮件缓存表中的数据读入缓存
	 * @param cacheList
	 * @return
	 * 返回值：boolean
	 */
	public static boolean initMailList2Cache(List<CotMailCache> cacheList){
		Cache mailCache = ContextUtil.getCache4Mail();
		MultiMap mhm = new MultiValueMap();
		for (CotMailCache cotMailCache : cacheList) {
			mhm.put(cotMailCache.getMailAddr(), cotMailCache.getMsgId());
		}
		Element element = new Element(Constants.MAIL_CACHE_KEY,mhm);
		mailCache.put(element);
		return true;
	}
	/**
	 * 描述：判断缓存列表中是否存在指定数据
	 * @param msgId 邮件ID
	 * @param mailAddr 邮箱账号
	 * @return
	 * 返回值：boolean
	 */
	public synchronized static boolean isExistMsgId(String msgId,String mailAddr){
		try{
			Cache mailCache = ContextUtil.getCache4Mail();
			Element result = mailCache.get(Constants.MAIL_CACHE_KEY);
			MultiMap mhm = (MultiMap)result.getObjectValue();
			List msgIds = (List)mhm.get(mailAddr);
			if(msgIds !=null && msgIds.contains(msgId)){
				//存在缓存列表
				return true;
			}else{
				mhm.put(mailAddr, msgId);
				Element resultNew = new Element(Constants.MAIL_CACHE_KEY,mhm);
				mailCache.put(resultNew);
				logger.info("不存在缓存列中，账号:"+mailAddr+", msgId:"+msgId);
				return false;
			}
		}catch (Exception e) {
			logger.error("判断邮件ID是否在缓存列表中失败",e);
		}
		return false;
	}
	
	/**
	 * 描述： 从缓存列表中移除数据
	 * @param msgId 邮件ID
	 * @param mailAddr 邮箱账号
	 * @return
	 * 返回值：boolean
	 */
	public synchronized static boolean removeMsgIdFromCache(String msgId,String mailAddr){
		Cache mailCache = ContextUtil.getCache4Mail();
		Element result = mailCache.get(Constants.MAIL_CACHE_KEY);
		MultiMap mhm = (MultiMap)result.getObjectValue();
		mhm.remove(mailAddr,msgId);
		result = new Element(Constants.MAIL_CACHE_KEY,mhm);
		Element element = new Element(Constants.MAIL_CACHE_KEY,mhm);
		mailCache.put(element);
		logger.info("从缓存列中删除，账号:"+mailAddr+", msgId:"+msgId);
		return true;
	}
	
	public static List<String> getCacheListByMailAddr(String mailAddr){
		Cache mailCache = ContextUtil.getCache4Mail();
		Element result = mailCache.get(Constants.MAIL_CACHE_KEY);
		MultiMap mhm = (MultiMap)result.getObjectValue();
		List<String> res = (List<String>)mhm.get(mailAddr);
		return res;	
	}
}
