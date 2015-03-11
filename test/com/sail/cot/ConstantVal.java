/**
 * 
 */
package com.sail.cot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Apr 9, 2010 11:04:09 AM </p>
 * <p>Class Name: ConstantVal.java </p>
 * @author achui
 *
 */
public class ConstantVal {
	static final String cfg[] = {
//		"E:/工作/proj/COT_SYSTEM_EXT/COT_SYSTEM_EXT_CVS/COT_SYSTEM_EXT/code/src/config/spring/applicationContext-*.xml"
		"D:/junit/spring/applicationContext*.xml"
	};
	public static ApplicationContext getSpringContext()
	{
		ApplicationContext ctx =  new FileSystemXmlApplicationContext(cfg);
		 // = new ClassPathXmlApplicationContext("config/spring/applicationContext*.xml");
		return ctx;
	}
}
