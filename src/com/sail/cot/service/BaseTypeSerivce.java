/**
 * 
 */
package com.sail.cot.service;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.query.QueryInfo;

/**
 * <p>Title: ���а칫�Զ���ϵͳ��OA��</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: May 4, 2009 2:22:43 PM </p>
 * <p>Class Name: BaseTypeSerivce.java </p>
 * @author achui
 *
 */
public interface BaseTypeSerivce {

	public List getTyepList(String objName);
	
	public void addTypeList(List objList) throws DAOException;
	
	public void modifyTypeList(List objList);
	
	public void deleteTypeList(List ids,String objName) throws DAOException;
	
	public Object getObjById(Integer id,Class clzz);
	
	public List getListByPage(QueryInfo queryInfo) throws DAOException;
	
	public int getRecordCount(QueryInfo queryInfo) throws DAOException;
	
	public abstract int deleteList(List objList) throws Exception;
	
	
}
