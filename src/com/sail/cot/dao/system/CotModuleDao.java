/**
 * 
 */
package com.sail.cot.dao.system;

import java.util.List;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotModule;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:55:16 PM </p>
 * <p>Class Name: CotModuleDao.java </p>
 * @author achui
 *
 */
public interface CotModuleDao extends CotBaseDao {
	
	/**
	 * 根据员工编号获得模块列表
	 * @param EmpId
	 * @return
	 */
	List<CotModule> getMenuByEmpId(String strEmpId);
	
    /**
	 * 查询模块级别为0和1,并且编号不是id的模块
	 * @param id
	 * @return
	 */
    List<CotModule> getParentModuleList(Integer id);
    
    /**
     * 判断模块名称是否重复
     * @param moduleName
     * @return
     */
    Boolean isExistModuleName(String moduleName);
    
    /**
     * 查询最大的模块id
     * @return
     */
    Integer findMaxModuleId();
    
    /**
     * 根据父类模块查询他的子模块
     * @param id
     * @return
     */
    List<CotModule> findModuleListByParentId(Integer id);
    
    //根据工号获取权限树
    List getAllModuleListByEmId(String empId);
    
    /**
     * 根据URI获取模块信息
     * @param id
     * @return
     * List<CotModule>
     */
    List<CotModule> getListByUrl(Integer empId);
    
    public List<CotModule> getMenus();
    
	//获取工号是Admin的权限树
	List getAllModuleListByAdmin() ;
	
	//获取权限
	public List getPopedom(Integer empId) ;
	
	List getModuleList();
	
	List getModuleListByParentId(String empId,String empName,int parnetId);
	
	//根据软件注册版本获取对应的菜单信息
	public List<CotModule> getModuleListBySoftVer(String softVer);
	//根据注册信息更新菜单(屏蔽不需要的菜单)
	public void updateModuleBySoftVerClose(String softVer);
	//更新根据注册信息更新菜单（开放需要的菜单）
	public void updateModuleBySoftVerOpen(String softVer);
	//获取数据字典菜单结构
	public List getSystDicList(String strVer);

	
	
}
