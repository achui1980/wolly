/**
 * 
 */
package com.sail.cot.servlet;

import java.util.HashMap;

import jxl.Workbook;

import com.jason.core.Application;
import com.jason.core.exception.ServiceException;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:上传各类文件的接口</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 13, 2008 10:13:22 AM </p>
 * <p>Class Name: UploadService.java </p>
 * @author achui
 *
 */
public abstract class UploadService {
	
	/**
	 * @param map 页面参数的Map集合
	 * @throws ServiceException
	 * void
	 */
	public abstract void  doPorcess(HashMap map) throws ServiceException;
	
	
	/**
	 * @param wb 从excel文件中读取出的数据对象
	 * @param map 页面上传入的其它参数
	 * @throws ServiceException
	 * void
	 */
	public abstract void  doPorcess(Workbook wb ,HashMap map) throws ServiceException;
	
	protected Object getService(String serviceName) {
		return Application.getInstance().getContainer().getComponent(serviceName);
	}
}
