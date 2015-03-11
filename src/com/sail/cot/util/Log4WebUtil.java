package com.sail.cot.util;

/**
 @author:achui
 Description:
 Company:厦门纵横科技
 2008-1-31
 */
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4WebUtil {

	public Log4WebUtil() {

	}

	public static boolean initLog4j() {
		System.out.println("initLog4j");
		String strPath = Log4WebUtil.class.getClassLoader().getResource("")
				.getPath();
		PropertyConfigurator.configure(strPath + "log4j.properties");
		return true;
	}

	public static Logger getLogger(Class obj) {
		Logger logger4j = Logger.getLogger(obj);
		return logger4j;
	}

	public static void info(Class obj, String msg) {
		getLogger(obj).info(msg);
	}

	public static void error(Class obj, String errMsg) {
		getLogger(obj).error(errMsg);
	}
	public static void error(Class obj,String errMsg,Throwable e)
	{
		getLogger(obj).error(errMsg,e);
	}
	
}
