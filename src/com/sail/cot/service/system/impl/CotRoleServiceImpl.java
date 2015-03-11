package com.sail.cot.service.system.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotRole;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotRoleService;
import com.sail.cot.util.Log4WebUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:45:09 PM </p>
 * <p>Class Name: CotRoleServiceImpl.java </p>
 * @author qh-chchh
 *
 */

public class CotRoleServiceImpl implements CotRoleService {
	
 
	private Logger logger = Log4WebUtil.getLogger(CotRoleServiceImpl.class);
	private CotBaseDao roleDao;

	private CotBaseDao getRoleDao() {
		// TODO Auto-generated method stub
		return roleDao;
	}
	public void setRoleDao(CotBaseDao roleDao) {
		this.roleDao = roleDao;
	}
	public void addRole(List roleList) {
		// TODO Auto-generated method stub
		try {
			this.getRoleDao().saveRecords(roleList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加员工角色信息异常",e);
		}
	}
	public int deleteRole(List roleList) {
		// TODO Auto-generated method stub
		List ids = new ArrayList();
		int res = 0;
		for(int i=0; i<roleList.size(); i++)
		{
			CotRole cotRole = (CotRole)roleList.get(i);
			ids.add(cotRole.getId());
			
		}
		try {
			this.getRoleDao().deleteRecordByIds(ids, "CotRole");
		} catch (DAOException e) {
			logger.error("删除员工角色信息异常",e);
			res = -1;
		}
		return res;
	}
	public boolean findExistByName(String name) {
		// TODO Auto-generated method stub
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotRole obj where obj.roleName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotRole obj where obj.roleName='"+name+"'");
		try {
			int count = this.getRoleDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}
	public CotRole getRoleById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotRole) this.getRoleDao().getById(CotRole.class, Id);
	}
	public List getRoleList() {
		// TODO Auto-generated method stub
		return this.getRoleDao().getRecords("CotRole");
	}
	public boolean modifyRole(List roleList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < roleList.size(); i++) {
			CotRole cotRole = (CotRole)roleList.get(i);
			Integer id = this.isExistRoleId(cotRole.getRoleName());
			if (id!=null && !id.toString().equals(cotRole.getId().toString())) {
				return false;
			}
		}
		try
		{
			this.getRoleDao().updateRecords(roleList);		
		}
		catch(Exception ex)
		{
			logger.error("更新员工角色信息异常", ex);
		}
		return true;
	}
	public Integer isExistRoleId(String roleName) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = roleDao.find("select r.id from CotRole r where r.roleName='"+roleName+"'");
		if (res.size()!=1) {
			return null;
		}else {
			return res.get(0);
		}
	}
	public List getList(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getRoleDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getRoleDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRoleService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getRoleDao().getJsonData(queryInfo);
	}

}
