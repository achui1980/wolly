/**
 * 
 */
package com.sail.cot.service.system;

import java.util.List;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 19, 2008 4:16:31 PM </p>
 * <p>Class Name: CotRolePopedomService.java </p>
 * @author achui
 *
 */
public interface CotRolePopedomService {
	//添加权限
	public int addRolePopedom(List popedomList);
	//删除权限
	public int deleteRolePopedom(List empIds);
	//获取权限
	public List getPopedomByRoleId(Integer empId);
	
	//删除权限
	public int deletePopedomByRoleId(int empId) ;
	
	//根据角色ID更新用户权限表
	public void updateEmpsPopedom(Integer roleId,List rolePopedom);
}
