package com.sail.cot.service.system;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotRole;
import com.sail.cot.query.QueryInfo;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 6, 2008 10:23:31 AM </p>
 * <p>Class Name: CotRoleService.java </p>
 * @author qh-chchh
 *
 */
public interface CotRoleService {
	
	//获取员工角色信息列表
	List getRoleList();
	//获取员工角色信息
	CotRole getRoleById(Integer Id);
	//添加员工角色信息
	void addRole(List roleList);
	//修改员工角色信息
	boolean modifyRole(List roleList);
	//删除员工角色信息
	int deleteRole(List roleList);
	//根据名称判断是否存在
	boolean findExistByName(String name);
	//根据员工角色名称判断ID是否已存在
	public Integer isExistRoleId(String roleName);
	
	public int getRecordCount(QueryInfo queryInfo);

	public List getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
