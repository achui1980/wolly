/**
 * 
 */
package com.sail.cot.service.impl;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.BaseTypeSerivce;

/**
 * <p>Title: ���а칫�Զ���ϵͳ��OA��</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: May 4, 2009 2:11:05 PM </p>
 * <p>Class Name: BaseTypeServiceImpl.java </p>
 * @author achui
 *
 */
public abstract class BaseTypeServiceImpl implements BaseTypeSerivce{
	
	private CotBaseDao baseDao;
	
	public CotBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(CotBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public List getTyepList(String objName)
	{
		return this.getBaseDao().getRecords(objName);
	}
	public void addTypeList(List objList) throws DAOException
	{
		this.getBaseDao().saveRecords(objList);
	}
	public void modifyTypeList(List objList)
	{
		this.getBaseDao().updateRecords(objList);
	}
	public void deleteTypeList(List ids,String objName) throws DAOException
	{
		this.getBaseDao().deleteRecordByIds(ids, objName);
	}
	public Object getObjById(Integer id,Class clzz)
	{
		return this.getBaseDao().getById(clzz, id);
	}
	public int getRecordCount(QueryInfo queryInfo) throws DAOException
	{
		return this.getBaseDao().getRecordsCount(queryInfo);
	}
	public List getListByPage(QueryInfo queryInfo) throws DAOException
	{
		return this.getBaseDao().findRecords(queryInfo);
	}
	public abstract int deleteList(List objList);
}
