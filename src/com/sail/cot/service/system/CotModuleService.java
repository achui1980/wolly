/**
 * 
 */
package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;
import com.sail.cot.domain.CotModule;
import com.sail.cot.domain.vo.TreeNode;


/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:23:39 PM </p>
 * <p>Class Name: CotSystemMenu.java </p>
 * @author achui
 *
 */
public interface CotModuleService {
	
	//根据员工编号获得模块列表
	public List<CotModule> getModuleListByEmpId(String EmpId);
	
	// 根据模块编号取得对象
	public CotModule getModuleById(Integer id);
	
	//保存模块
	public Boolean addModule(List<?> moduleList);
	
	 // 批量更新模块
	public void modifyModule(List<?> moduleList);
	
	
	 //删除模块
	public void deleteModule(List<?> moduleList);
	
	//查询模块级别为0和1,并且编号不是id的模块
	public List<?> getParentModuleList(Integer id);
	
	//查询所有模块名称
	public Map<?, ?> getModuleNameMap();
	
	//获取所以某块列表
	public List getAllModuleListByEmId(String empId);
	
	public List<CotModule> getMenus();
	
	public List getAllModuleListByAdmin();
	
	public List getPopedom(Integer empId) ;
	
	public List<CotModule> getModuleList();
	
	public List<CotModule> getModuleListByParentId(int parentId);
	
	//根据软件注册版本获取对应的菜单信息
	public List<CotModule> getModuleListBySoftVer(String softVer);
	//根据注册信息更新菜单(屏蔽不需要的菜单)
	public void updateModuleBySoftVerClose(String softVer);
	//更新根据注册信息更新菜单（开放需要的菜单）
	public void updateModuleBySoftVerOpen(String softVer);
	//获取菜单表数据字典模块
	public List getSystDicList(String strVer);
	
	//获取版本信息
	public Integer getSoftVer();
	
	//获取树菜单,Extjs
	public List<TreeNode> getSysDicTree(String strVer);
	
	//查询登录人数
	public Integer findLoginNum();
	
	//根据员工编号查询QQ号
	public String getQqNum(Integer empId);
	
	//根据员工编号查询msn号
	public String getMsnNum(Integer empId);
	
	
}
