/**
 * 
 */
package com.sail.cot.dao.system;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.jason.core.exception.ServiceException;
import com.sail.cot.dao.CotBaseDao;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 13, 2008 2:52:35 PM </p>
 * <p>Class Name: CotPopdeomDao.java </p>
 * @author achui
 *
 */
public interface CotPopedomDao extends CotBaseDao{
	
	//添加权限
	public int addPopedom(List popedomList)  throws  DAOException;
	//删除权限
	public int deletePopedom(List empId) throws  DAOException;
	//获取权限
	public List getPopedomByEmpId(Integer empId) throws  DAOException;
	
	//删除权限
	public int deletePopedomByEmpId(int empId) throws  DAOException;
	
	//根据URL获取模块权限
	Map getPopedomByUrl(String Url,Integer empId) throws  DAOException;
	
	//根据员工ID获取权限 其中ID为员工表的主键
	Map getPopedomByEmp(Integer empId) throws  DAOException;
	
	//获取权限
	public List getPopedom(Integer empId) throws  DAOException;
	
	//根据员工列表批量删除该员工数据
	public void deletePopedomByEmpsList(List empIdList);
	
	//根据员工列表批量添加该员工权限数据
	//public void addPopedomByEmpsList(List empIdList);
	
	
	


}
