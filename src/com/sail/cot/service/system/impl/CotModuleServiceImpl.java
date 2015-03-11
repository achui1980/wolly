/**
 * 
 */
package com.sail.cot.service.system.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import com.jason.core.exception.DAOException;
import com.jason.core.exception.ServiceException;
import com.sail.cot.dao.system.CotModuleDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotModule;
import com.sail.cot.domain.CotServerInfo;
import com.sail.cot.domain.vo.TreeNode;
import com.sail.cot.service.system.CotModuleService;
import com.sail.cot.util.Log4WebUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:45:09 PM </p>
 * <p>Class Name: CotSystemMenuServiceImpl.java </p>
 * @author achui
 *
 */
public class CotModuleServiceImpl implements CotModuleService {

	private CotModuleDao moduleDao;
	private Logger logger = Log4WebUtil.getLogger(CotModuleServiceImpl.class);
	
	/**
	 * 根据员工编号获得模块列表
	 * @param EmpId
	 * @return
	 */
	public List<CotModule> getModuleListByEmpId(String EmpId) {
		return this.getModuleDao().getMenuByEmpId(EmpId);
	}
	
	/**
	 * 根据查询条件分页
	 * @param queryInfo
	 * @return
	 * @throws DAOException 
	 */
		/**
	 * 根据模块编号取得对象
	 * @param id
	 * @return
	 */
	public CotModule getModuleById(Integer id) {
		return (CotModule) this.getModuleDao().getById(CotModule.class, id);
	}
	
	/**
	 * 批量保存模块
	 * @param moduleList
	 */
	public Boolean addModule(List<?> moduleList){
		Boolean flag = false;
		List<CotModule> list = new ArrayList<CotModule>();
		for (int i = 0; i < moduleList.size(); i++) {
			CotModule cotModule = (CotModule)moduleList.get(i);
			//判断模块名是否存在
			flag = this.getModuleDao().isExistModuleName(cotModule.getModuleName());
			if(flag==true){
				return false;
			}
			//设置模块排序为最大的id+1
			Integer maxId = this.getModuleDao().findMaxModuleId();
			cotModule.setMoudleOrder(Long.parseLong(maxId.toString())+1);
			list.add(cotModule);
		}
		try {
			this.getModuleDao().saveRecords(list);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("保存模块异常", e);
		}
		return true;
	}
	
	/**
	 * 批量更新模块
	 * @param cotModuleList
	 * @throws ServiceException
	 */
	public void modifyModule(List<?> moduleList){
		try {
			this.getModuleDao().updateRecords(moduleList);
		} catch (Exception e) {
			logger.error("更新模块异常", e);
		}
		
	}
	
	/**
	 * 查询模块级别为0和1,并且编号不是id的模块
	 * @param id
	 * @return
	 */
	public List<CotModule> getParentModuleList(Integer id){
		List<CotModule> list = this.getModuleDao().getParentModuleList(id);
		return list;
	}
	
	/**
	 * 删除模块
	 * @param id
	 * @throws DAOException 
	 */
	public void deleteModule(List<?> moduleList){
		for (int i = 0; i < moduleList.size(); i++) {
			Integer id = ((CotModule)moduleList.get(i)).getId();
			List<CotModule> list = this.getModuleDao().findModuleListByParentId(id);
			List<Integer> ids = new ArrayList<Integer>();
			Iterator<?> it = list.iterator();
			while(it.hasNext()){
				CotModule cotModule = (CotModule)it.next();
				ids.add(cotModule.getId());
			}
			if(ids.size()!=0){
				try {
					this.getModuleDao().deleteRecordByIds(ids, "CotModule");
				} catch (DAOException e) {
					e.printStackTrace();
					logger.error("删除子模块异常", e);
				}
			}
			try {
				this.getModuleDao().deleteRecordById(id, "CotModule");
			} catch (DAOException e) {
				e.printStackTrace();
				logger.error("删除模块异常", e);
			}
		}
	}
	
	//查询所有模块名称
	public Map<?, ?> getModuleNameMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getModuleDao().getMenus();
		for (int i = 0; i < list.size(); i++) {
			CotModule cotModule = (CotModule)list.get(i);
			map.put(cotModule.getId().toString(), cotModule.getModuleName());
		}
		return map;
	}
	
	public CotModuleDao getModuleDao() {
		return moduleDao;
	}
	public void setModuleDao(CotModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getAllModuleListByEmId(java.lang.String)
	 */
	public List getAllModuleListByEmId(String empId) {
		
		return this.getModuleDao().getAllModuleListByEmId(empId);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getMenus()
	 */
	public List<CotModule> getMenus() {
		// TODO Auto-generated method stub
		return this.getModuleDao().getMenus();
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getAllModuleListByAdmin()
	 */
	public List getAllModuleListByAdmin() {
		// TODO Auto-generated method stub
		return this.getModuleDao().getAllModuleListByAdmin();
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getPopedom(java.lang.Integer)
	 */
	public List getPopedom(Integer empId) {
		// TODO Auto-generated method stub
		return this.getModuleDao().getPopedom(empId);
	}

	public List<CotModule> getModuleList() {
		// TODO Auto-generated method stub
		List<CotModule> res = this.getModuleDao().getModuleList();
		return res;
		
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getModuleByParentIdHtml(int)
	 */
	public List<CotModule> getModuleListByParentId(int parentId) {
		
		List<CotModule> res = this.getModuleDao().getModuleListByParentId(null,null, parentId);
		
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getModuleListBySoftVer(java.lang.String)
	 */
	public List<CotModule> getModuleListBySoftVer(String softVer) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#updateModuleBySoftVerClose(java.lang.String)
	 */
	public void updateModuleBySoftVerClose(String softVer) {
		this.getModuleDao().updateModuleBySoftVerClose(softVer);
		
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#updateModuleBySoftVerOpen(java.lang.String)
	 */
	public void updateModuleBySoftVerOpen(String softVer) {
		this.getModuleDao().updateModuleBySoftVerOpen(softVer);	
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getSystDicList()
	 */
	public List getSystDicList(String strVer) {
		return this.getModuleDao().getSystDicList(strVer);
	}
	
	//获取版本信息
	public Integer getSoftVer(){
		WebContext ctx = WebContextFactory.get();
		CotServerInfo cotServerInfo = (CotServerInfo) ctx.getSession().getAttribute("CotServerInfo");
		
		if (cotServerInfo != null) {
			if ("sample".equals(cotServerInfo.getSoftVer())) {
				return 1;
			}
			if ("price".equals(cotServerInfo.getSoftVer())) {
				return 2;
			}
			if ("trade".equals(cotServerInfo.getSoftVer())) {
				return 3;
			}
			if ("trade_f".equals(cotServerInfo.getSoftVer())) {
				return 4;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotModuleService#getTree(java.lang.String)
	 */
	public List<TreeNode> getSysDicTree(String strVer) {
		List<TreeNode> res = new ArrayList<TreeNode>();
		 List<CotModule> list = this.getSystDicList(strVer);
		 //List<CotModule> list1 = getAllModuleListByAdminExtByParent(new Integer(nodeId));
		 for(CotModule oaModule : list)
		 {
			 TreeNode node = new TreeNode(list);
			 System.out.println(oaModule.getModuleName());
			 node.setId(oaModule.getId().toString());
			 node.setText(oaModule.getModuleName());
			 node.setUrl(oaModule.getModuleUrl());
			 node.setHref(oaModule.getModuleUrl());
			 //node.setOaModule(oaModule);
			 res.add(node);
			 
		 }
		return res;
	}

	// 查询登录人数
	public Integer findLoginNum() {
		String hql = "select count(*) from CotLoginInfo";
		List list = this.getModuleDao().find(hql);
		if (list != null && list.size() > 0) {
			return (Integer) list.get(0);
		}
		return 0;
	}

	// 根据员工编号查询QQ号
	public String getQqNum(Integer empId) {
		WebContext ctx = WebContextFactory.get();
		Integer eId = (Integer) ctx.getSession().getAttribute("empId");
		if (eId.intValue() == empId.intValue()) {
			return "exist";
		}

		String hql = "select obj.qqNum from CotEmps obj where obj.id=" + empId;
		List listQQ = this.getModuleDao().find(hql);
		if (listQQ != null && listQQ.size() > 0) {
			return (String) listQQ.get(0);
		}
		return null;
	}

	// 根据员工编号查询msn号
	public String getMsnNum(Integer empId) {
		WebContext ctx = WebContextFactory.get();
		Integer eId = (Integer) ctx.getSession().getAttribute("empId");
		if (eId.intValue() == empId.intValue()) {
			return "exist";
		}

		String hql = "select obj.msnNum from CotEmps obj where obj.id=" + empId;
		List listQQ = this.getModuleDao().find(hql);
		if (listQQ != null && listQQ.size() > 0) {
			return (String) listQQ.get(0);
		}
		return null;
	}

}
