/**
 * 
 */
package com.sail.cot.mail.util;

import java.util.HashMap;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:获取邮件默认配置信息类</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 5, 2008 11:04:30 AM </p>
 * <p>Class Name: MailCfgUtil.java </p>
 * @author achui
 *
 */
public class MailCfgUtil {
	
	private static HashMap cfgMap = null;
	
	public static HashMap getMailCfgMap()
	{
		if(cfgMap == null)
		{
			cfgMap = new HashMap();			
		}
		return cfgMap;
	}
}
