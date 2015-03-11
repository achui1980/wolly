/**
 * 
 */
package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.util.ThreadLocalManager;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 13, 2008 3:18:29 PM </p>
 * <p>Class Name: CotPopedomService.java </p>
 * @author achui
 *
 */
public interface CotPopedomService {
	//添加权限
	public int addPopedom(List popedomList);
	//删除权限
	public int deletePopedom(List empIds);
	//根据员工工号获取权限
	public List getPopedomByEmpId(Integer empId);
	
	//删除权限
	public int deletePopedomByEmpId(int empId) ;
	
	//根据URL获取模块权限
	Map getPopedomByUrl(String Url,Integer empId);
	
	//根据员工ID获取权限 其中ID为员工表的主键
	Map getPopedomByEmp(Integer empId);
	
	//根据URL获取模块权限配置
	Map getPopedomByMenu(String Url);
	
	//获取登陆员工工号
	String getLoginEmpId();
	
	//获取登陆员工编号
	public Integer getLogId();
	
	//获取当前登录员工
	public CotEmps getLoginEmp();

	
}
