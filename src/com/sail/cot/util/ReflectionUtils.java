/**
 * 
 */
package com.sail.cot.util;

import java.lang.reflect.Field;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Oct 16, 2008 2:25:03 PM </p>
 * <p>Class Name: ReflectionUtils.java </p>
 * @author achui
 *
 */
public class ReflectionUtils {
	public static String[] getDeclaredFields(Class c) {
		String[] properties = null;
		Field[] fields = c.getDeclaredFields();
		
		properties = new String[fields.length];
		for (int i=0; i<properties.length; i++) {
			properties[i] = fields[i].getName();
		}
		return properties;
	}
}
