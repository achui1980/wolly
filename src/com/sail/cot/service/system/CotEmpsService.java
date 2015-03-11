/**
 * 
 */
package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotLoginInfo;
import com.sail.cot.query.QueryInfo;


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
public interface CotEmpsService {
	
	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);
	
	//根据条件查询主报价单记录
	public List<?> getEmpsList(QueryInfo queryInfo);
	
	//根据员工编号取得对象
	public CotEmps getEmpsById(Integer Id);
	
	//保存员工
	public int addEmps(List<CotEmps> empsList);
	
	//更新员工
	public int modifyEmps(List<?> empsList);
	
	//删除员工
	public Boolean deleteEmps(Integer empId)throws DAOException ;
	
	//根据公司查询所有部门
	public List<?> getDeptListByCompanyId(Integer companyId);
	
	//查询所有角色
	public List<?> getRoleList();
	
	//查询所有部门名称
	public Map<?, ?> getDeptNameMap();
	
	//查询所有角色名称
	public Map<?, ?> getRoleNameMap();
	
	//查询所有公司名称
	public Map<?, ?> getCompanyNameMap();
	
	//查询所有员工状态
	public Map<?, ?> getEmpsStatusMap();
	
	//登录验证(0代表工号不存在,1代表密码不正确.2代表成功)
	public int login(String empsId,String empsPwd,String otp);
	
	//登录验证(0代表工号不存在,1代表密码不正确,2代表成功)---(试用版)
	public int login_demo(String empsId,String empsPwd);
	
	//根据员工工号取得对象
	public CotEmps getEmpsByEmpId(String empId);
	
	//获取登陆员工,从Session取
	public CotEmps getLoginEmp();
	
	//修改员工密码
	public int modifyPwdByEmpId(String oldPwd,String newPwd);
	
	//判断目前登录总人数是否超过范围
	public boolean checkIsMaxLoginCount(String ip,Integer max);
	
	//根据角色ID获取员工信息
	public List getEmpsByRoleId(String roleId);
	
	//更新登录时间
	public void updateLoginInfo(CotLoginInfo loginInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	// 判断登录号是否存在
	public boolean findIsExistEmpsId(String empsId, String eId);
	
	// 判断用户名是否存在
	public boolean findIsExistEmpsName(String empsName, String eId);
	
	//获取已配置邮件的员工列表
	public List<CotEmps> getMailEmpsList();
	
	//获取用户邮件的消息提示
	public String getMailAlarmByEmpId(Integer empId,boolean isPublic);
	
	// 同步动态密码 (0:该员工帐户不存在;1:同步动态密码失败;2:同步成功)
	public int tong(String empId, String oneOtp, String twoOtp);
	
	public List getList(String tbName);
	
	public Integer copyEmpRight(Integer empsId_from, Integer empsId_to)throws Exception;

	
}
