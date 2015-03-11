/**
 * 
 */
package com.sail.cot.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.system.CotEmpsDao;
import com.sail.cot.dao.system.CotPopedomDao;
import com.sail.cot.dao.system.CotRolePopedomDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotPopedom;
import com.sail.cot.domain.CotRolepopedom;
import com.sail.cot.service.system.CotRolePopedomService;
import com.sail.cot.util.Log4WebUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 19, 2008 4:18:55 PM </p>
 * <p>Class Name: CotRolePopedomServiceImpl.java </p>
 * @author achui
 *
 */
public class CotRolePopedomServiceImpl implements  CotRolePopedomService{

	private CotRolePopedomDao cotRolePopedomDao;
	private CotPopedomDao cotPopedomDao;
	private CotEmpsDao cotEmpsDao;
	private Logger logger = Log4WebUtil.getLogger(CotRolePopedomServiceImpl.class);
	
	public CotRolePopedomDao getCotRolePopedomDao() {
		return cotRolePopedomDao;
	}

	public void setCotRolePopedomDao(CotRolePopedomDao cotRolePopedomDao) {
		this.cotRolePopedomDao = cotRolePopedomDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRolePopedomService#addRolePopedom(java.util.List)
	 */
	public int addRolePopedom(List popedomList) {
		try {
			this.getCotRolePopedomDao().addRolePopedom(popedomList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("添加权限信息异常",e);
			return -1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRolePopedomService#deletePopedomByRoleId(int)
	 */
	public int deletePopedomByRoleId(int roleId) {
		int res = 0;
		try {
			res = this.getCotRolePopedomDao().deletePopedomByRoleId(roleId);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("获取权限信息异常",e);
		}
		return res;
	}


	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRolePopedomService#deleteRolePopedom(java.util.List)
	 */
	public int deleteRolePopedom(List roleIds) {
		try {
			this.getCotRolePopedomDao().deletePopedom(roleIds);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("删除角色权限信息异常",e);
			return -1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRolePopedomService#getPopedomByRoleId(java.lang.Integer)
	 */
	public List getPopedomByRoleId(Integer roleId) {
		List res = null;
		try {
			res = this.getCotRolePopedomDao().getPopedomByRoleId(roleId);
		} catch (DAOException e) {
			logger.error("获取权限信息异常",e);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRolePopedomService#updateEmpsPopedom(java.lang.Integer)
	 */
	public void updateEmpsPopedom(Integer roleId,List rolePopedomList) {
		//1、根据角色ID获取属于该角色的用户信息
		List emps = this.getCotEmpsDao().find(" from CotEmps obj where obj.roleId="+roleId);
		List empIdList = new ArrayList();
		List empPopedoms = new ArrayList();
		for(int i=0; i<emps.size(); i++)
		{	
			CotEmps emp = (CotEmps)emps.get(i);
			empIdList.add(emp.getId());	
			for(int j=0; j<rolePopedomList.size(); j++)
			{
				CotPopedom popedom = new CotPopedom();
				CotRolepopedom rolePopedom = (CotRolepopedom)rolePopedomList.get(j);
				popedom.setEmpsId(emp.getId());
				popedom.setModuleId(rolePopedom.getModuleId());
				empPopedoms.add(popedom);
			}
		}
		//2、删除该用户的权限
		this.getCotPopedomDao().deletePopedomByEmpsList(empIdList);
		//3、根据当前角色所拥有的权限，更新该用户权限
		try {
			this.cotPopedomDao.addPopedom(empPopedoms);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public CotPopedomDao getCotPopedomDao() {
		return cotPopedomDao;
	}

	public void setCotPopedomDao(CotPopedomDao cotPopedomDao) {
		this.cotPopedomDao = cotPopedomDao;
	}

	public CotEmpsDao getCotEmpsDao() {
		return cotEmpsDao;
	}

	public void setCotEmpsDao(CotEmpsDao cotEmpsDao) {
		this.cotEmpsDao = cotEmpsDao;
	}

}
