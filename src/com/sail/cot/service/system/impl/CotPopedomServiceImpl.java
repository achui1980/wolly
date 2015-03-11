/**
 * 
 */
package com.sail.cot.service.system.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.system.CotPopedomDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.service.system.CotPopedomService;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 13, 2008 3:22:35 PM </p>
 * <p>Class Name: CotPopedomServiceImpl.java </p>
 * @author achui
 *
 */
public class CotPopedomServiceImpl implements CotPopedomService {

	private CotPopedomDao cotPopedomDao;
	private Logger logger = Log4WebUtil.getLogger(CotPopedomServiceImpl.class);
	public CotPopedomDao getCotPopedomDao() {
		return cotPopedomDao;
	}

	public void setCotPopedomDao(CotPopedomDao cotPopedomDao) {
		this.cotPopedomDao = cotPopedomDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#addPopedom(java.util.List)
	 */
	public int addPopedom(List popedomList) {
		try {
			this.getCotPopedomDao().addPopedom(popedomList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("添加权限信息异常",e);
			return -1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#deletePopedom(java.lang.Integer)
	 */
	public int deletePopedom(List empIds) {
		// TODO Auto-generated method stub
		try {
			this.getCotPopedomDao().deletePopedom(empIds);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("删除权限信息异常",e);
			return -1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#getPopedomByEmpId(java.lang.Integer)
	 */
	public List getPopedomByEmpId(Integer empId) {
		List res = null;
		try {
			res = this.getCotPopedomDao().getPopedomByEmpId(empId);
		} catch (DAOException e) {
			logger.error("获取权限信息异常",e);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#deletePopedomByEmpId(java.lang.String)
	 */
	public int deletePopedomByEmpId(int empId) {
		int res = 0;
		try {
			res = this.getCotPopedomDao().deletePopedomByEmpId(empId);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			logger.error("获取权限信息异常",e);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#getPopedomByUrl(java.lang.String, java.lang.Integer)
	 */
	public Map getPopedomByUrl(String Url, Integer empId) {
		Map map = null;
		try {
			map = this.getCotPopedomDao().getPopedomByUrl(Url, empId);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#getPopedomByEmp(java.lang.Integer)
	 */
	public Map getPopedomByEmp(Integer empId) {	
		Map popedomMap = null;
		try {
			popedomMap = this.getCotPopedomDao().getPopedomByEmp(empId);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return popedomMap;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#getPopedomByUrl(java.lang.String)
	 */
	public Map getPopedomByMenu(String Url) {
		
		HttpSession session = WebContextFactory.get().getSession();
		// TODO Auto-generated method stub
		Map popedomOp = null;
		Map poepdomMap = (Map) session.getAttribute("popedomMap");
		System.out.println("poepdomMap:"+poepdomMap);
		if(poepdomMap != null)
		{
			popedomOp = (Map)poepdomMap.get(Url);
		}
		System.out.println(popedomOp);
		return popedomOp;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotPopedomService#getLoginEmpId()
	 */
	public String getLoginEmpId() {
		HttpSession session = WebContextFactory.get().getSession();
		String empId = (String) session.getAttribute("empNo");
		return empId;
	}
	
	public CotEmps getLoginEmp() {
		HttpSession session =  WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		return emp;
	}
	
	public Integer getLogId() {
		HttpSession session = WebContextFactory.get().getSession();
		Integer empId = (Integer) session.getAttribute("empId");
		return empId;
	}

}
