/**
 * 
 */
package com.sail.cot.service;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * <p>Title: 旗航外贸管理软件V8.0</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Feb 8, 2014 10:18:39 PM </p>
 * <p>Class Name: JsonFloatValueProcessor.java </p>
 * @author achui
 *
 */
public class JsonFloatValueProcessor implements JsonValueProcessor {


	/* (non-Javadoc)
	 * @see net.sf.json.processors.JsonValueProcessor#processObjectValue(java.lang.String, java.lang.Object, net.sf.json.JsonConfig)
	 */
	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		System.out.println("==============="+value);
		if (value instanceof Float) {  
	        return value.toString();  
	    }  
	    return null;  
	}

	/* (non-Javadoc)
	 * @see net.sf.json.processors.JsonValueProcessor#processArrayValue(java.lang.Object, net.sf.json.JsonConfig)
	 */
	@Override
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		return null;
	}

}
