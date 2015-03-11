/**
 * 
 */
package com.sail.cot.dao.system;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 19, 2008 3:23:44 PM </p>
 * <p>Class Name: CotRolePopedomDao.java </p>
 * @author achui
 *
 */
public interface CotRolePopedomDao extends CotBaseDao{
	//添加角色权限
	public int addRolePopedom(List popedomList)  throws  DAOException;
	//删除角色权限
	public int deletePopedom(List empId) throws  DAOException;
	//获取角色权限
	public List getPopedomByRoleId(Integer roleId) throws  DAOException;
	
	//删除角色权限
	public int deletePopedomByRoleId(int roleId) throws  DAOException;
	
	
}
