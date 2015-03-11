/**
 * 
 */
package com.sail.cot.dao.system.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.jfree.chart.block.EmptyBlock;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.system.CotModuleDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotModule;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.vo.CotTreeNode;
/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:58:48 PM </p>
 * <p>Class Name: CotModuleDaoImpl.java </p>
 * @author achui
 *
 */
public class CotMoudleDaoImpl extends CotBaseDaoImpl implements CotModuleDao {

	/**
	 * 根据员工编号获得模块列表
	 * @param EmpId
	 * @return
	 */
	public List<CotModule> getMenuByEmpId(String strEmpId) {
		List<CotModule> res = new ArrayList<CotModule>();
		String strSql = "SELECT t.id,t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MODULE_IMGURL,t.MOUDLE_PARENT,t.MODULE_LV,t.MOUDLE_TYPE,t.MOUDLE_ORDER,t.MODULE_FLAG  " 
						+"FROM cot_module t,cot_popedom p where 1=1"
						+" and t.id = p.module_id"
						+" and t.MODULE_LV <=1"
						+" and p.emps_id = "+strEmpId
						+ " order by t.MOUDLE_ORDER asc";
		//根据权限获取菜单列表
		res = super.getSession().createSQLQuery(strSql).addEntity(CotModule.class).list();
		return res;
	}
	public List<CotModule> getMenus()
	{

		List<CotModule> res = new ArrayList<CotModule>();
		res = super.find(" from CotModule c where c.moduleLv<=1 order by c.moudleOrder asc");
		return res;
	}

	/**
	 * 根据模块编号取得对象
	 * @param Id
	 * @return
	 */
	public CotModule getModuleById(Integer Id) {
		return (CotModule) super.getById(CotModule.class, Id);
	}
	
	/**
	 * 查询模块级别为0和1,并且编号不是id,并且不包含它的子类模块的菜单
	 * @param id
	 * @return
	 */
	public List<CotModule> getParentModuleList(Integer id){
		List<CotModule> res = new ArrayList<CotModule>();
		res = super.find("from CotModule c where c.moduleLv<=1 and c.id<>"+id+" and c.id not in (select m.id from CotModule m where m.moudleParent="+id+")");
		return res;
	}
	
	 /**
     * 判断模块名称是否重复
     * @param moduleName
     * @return
     */
	public Boolean isExistModuleName(String moduleName){
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select count(*) from CotModule c where c.moduleName='"+moduleName+"'");
		if(res.get(0)!=0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
     * 查询最大的模块id
     * @return
     */
	public Integer findMaxModuleId() {
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select max(c.id) from CotModule c");
		return res.get(0);
	}
	
	/**
     * 根据父类模块查询他的子模块
     * @param id
     * @return
     */
    public List<CotModule> findModuleListByParentId(Integer id){
    	List<CotModule> res = new ArrayList<CotModule>();
		res = super.find("from CotModule c where c.moudleParent="+id);
		return res;
    }

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getAllModuleListByEmId(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getAllModuleListByEmId(java.lang.String)
	 */
	public List getAllModuleListByAdmin() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("Select t.id,t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MODULE_IMGURL,t.MOUDLE_PARENT,t.MODULE_LV,t.MOUDLE_TYPE,t.MOUDLE_ORDER,t.MODULE_FLAG from cot_module t where 1=1 ");
		sb.append(" and t.MODULE_LV < 7 ");
		sb.append(" order by t.MOUDLE_ORDER asc");
		List res = super.getSession().createSQLQuery(sb.toString()).addEntity(CotModule.class).list();
		return res;
	}
	
	public List getAllModuleListByEmId(String empId) {
		// TODO Auto-generated method stub
		String strSql = "SELECT t.id,t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MODULE_IMGURL,t.MOUDLE_PARENT,t.MODULE_LV,t.MOUDLE_TYPE,t.MOUDLE_ORDER,t.MODULE_FLAG  " 
			+"FROM cot_module t,cot_popedom p where 1=1"
			+" and t.MODULE_LV < 7 "
			+" and t.id = p.module_id"
			+" and p.emps_id = "+empId
			+ " order by t.MOUDLE_ORDER asc";
		List res = super.getSession().createSQLQuery(strSql).addEntity(CotModule.class).list();
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getListByUrl(java.lang.Integer)
	 */
	public List<CotModule> getListByUrl(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getPopedom(java.lang.Integer)
	 */
	public List getPopedom(Integer empId) {

		List factoryList = super.getRecords("CotFactory");
		List typelv1 = super.getRecords("CotTypeLv1");
		List res = new ArrayList();
		for(int i=0; i<factoryList.size(); i++)
		{
			CotTreeNode rootNode= new CotTreeNode();
			CotFactory fac = (CotFactory)factoryList.get(i);
			rootNode.setNodeid("root_"+fac.getId());
			rootNode.setNodename(fac.getFactoryName());
			rootNode.setParentnodeid(null);
			res.add(rootNode);
			for(int j=0; j<typelv1.size(); j++)
			{
				CotTypeLv1 type = (CotTypeLv1)typelv1.get(j);
				CotTreeNode node= new CotTreeNode();
				node.setNodeid(fac.getId()+"_"+type.getId());
				node.setNodename(type.getTypeName());
				node.setParentnodeid(rootNode.getNodeid());
				res.add(node);
				
			}
		}
	    return res;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getModuleList(java.lang.String)
	 */
	public List getModuleList() {
		List<CotModule> res = new ArrayList<CotModule>();
		String strSql = "";
		WebContext ctx = WebContextFactory.get();
		CotEmps emp = (CotEmps) ctx.getSession().getAttribute("emp");

		if(!"admin".equals(emp.getEmpsId()))
		{
			strSql = "SELECT t.id,t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MODULE_IMGURL,t.MOUDLE_PARENT,t.MODULE_LV,t.MOUDLE_TYPE,t.MOUDLE_ORDER,t.MODULE_FLAG  " 
				+"FROM cot_module t,cot_popedom p where 1=1"
				+" and t.id = p.module_id"
				+" and t.MODULE_LV =1"
				+" and t.id != 105"	
				+" and t.MOUDLE_TYPE ='MODULE'";
				
			strSql += " and p.emps_id = "+emp.getId();
			strSql += " order by t.MOUDLE_ORDER asc";
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Select t.id,t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MODULE_IMGURL,t.MOUDLE_PARENT,t.MODULE_LV,t.MOUDLE_TYPE,t.MOUDLE_ORDER,t.MODULE_FLAG from cot_module t where 1=1 ");
			sb.append(" and t.MOUDLE_TYPE ='MODULE'");
			sb.append(" and t.MODULE_LV =1");
			sb.append(" and t.id != 105");
			sb.append(" order by t.MOUDLE_ORDER asc");
			strSql = sb.toString();
		}
				
		//根据权限获取菜单列表
		res = super.getSession().createSQLQuery(strSql).addEntity(CotModule.class).list();
		return res;
		
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getModuleListByParentId(java.lang.String, java.lang.String, int)
	 */
	public List getModuleListByParentId(String empId, String empName,
			int parnetId) {
		List<CotModule> res = new ArrayList<CotModule>();
		String strSql = "";
		WebContext ctx = WebContextFactory.get();
		CotEmps emp = (CotEmps) ctx.getSession().getAttribute("emp");
		if(!"admin".equals(emp.getEmpsId()))
		{
			strSql = "SELECT t.id,t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MODULE_IMGURL,t.MOUDLE_PARENT,t.MODULE_LV,t.MOUDLE_TYPE,t.MOUDLE_ORDER,t.MODULE_FLAG  " 
				+"FROM cot_module t,cot_popedom p where 1=1"
				+" and t.id = p.module_id"
				+" and t.MODULE_LV =1"
				+" and t.MOUDLE_TYPE ='MENU'";
			strSql += " and p.emps_id = "+emp.getId();
			strSql += " and t.MOUDLE_PARENT = "+parnetId;
			strSql += " order by t.MOUDLE_ORDER asc";
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Select t.id,t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MODULE_IMGURL,t.MOUDLE_PARENT,t.MODULE_LV,t.MOUDLE_TYPE,t.MOUDLE_ORDER,t.MODULE_FLAG from cot_module t where 1=1 ");
			//sb.append(" and t.MOUDLE_TYPE ='MODULE'");
			sb.append(" and t.MODULE_LV =1");
			sb.append(" and t.MOUDLE_PARENT = "+parnetId);
			sb.append(" order by t.MOUDLE_ORDER asc");
			strSql = sb.toString();
		}
				
		//根据权限获取菜单列表
		res = super.getSession().createSQLQuery(strSql).addEntity(CotModule.class).list();
		return res;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getModuleListBySoftVer(java.lang.String)
	 */
	public List<CotModule> getModuleListBySoftVer(String softVer) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#updateModuleBySoftVer(java.lang.String)
	 * MODULE_LV ： 8：都不开启，9：需要开启，2：不能改变
	 * 关闭不是softVer版本的菜单
	 */
	public void updateModuleBySoftVerClose(String softVer) {
		String condition = "";
		if(softVer.equals("sample"))
			condition = "('price','trade','trade_f','email')";//如果是样品版本，屏蔽报价和外贸
		else if(softVer.equals("price"))
			condition = "('trade','trade_f','email')";//如果是报价版本，屏蔽外贸
		else if(softVer.equals("trade"))
			condition = "('trade_f','email')";		//如果是外贸标准版，关闭企业版菜单
		else if(softVer.equals("trade_f"))
			condition = "('email')";
		else if(softVer.equals("email"))
			condition = "('price','trade','trade_f','sample')";
		String strSql = "update cot_module set MODULE_LV = 9  where 1=1 " +
				" and (MODULE_LV <> 2 or MODULE_LV <> 8) " +
				" and  module_flag in "+condition+
				" and module_flag is not null";
		
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try
		{
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			res = pstm.executeUpdate();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if(pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#openModuleBySoftVer(java.lang.String)
	 * 开启注册模块所需要显示的菜单，更新MODULE_LV = 9的记录，根据MOUDLE_TYPE判断MODULE_LV的值
	 * MOUDLE_TYPE = MODULE -> MODULE_LV = 1
	 * MOUDLE_TYPE = MENU -> MODULE_LV = 1
	 * MOUDLE_TYPE = 非MODULE或MENU -> MODULE_LV = 3
	 */
	public void updateModuleBySoftVerOpen(String softVer) {
		String condition = "";
		if(softVer.equals("sample"))
			condition = "('sample')";//如果是样品版本，屏蔽报价和外贸
		else if(softVer.equals("price"))
			condition = "('sample','price')";//如果是报价版本，屏蔽外贸
		else if(softVer.equals("trade"))
			condition = "('sample','price','trade')";		//如果是外贸标准版，关闭企业版菜单
		else if(softVer.equals("trade_f"))
			condition = "('sample','price','trade','trade_f')";
		else if(softVer.equals("email"))
			condition = "('email')";
		
		String strSql_1 = "update cot_module set MODULE_LV = 1  where 1=1 " +
		" and MODULE_LV = 9 " +
		" and MOUDLE_TYPE in('MODULE','MENU')" +
		" and  module_flag in "+condition;
		
		String strSql_2 = "update cot_module set MODULE_LV = 3  where 1=1 " +
		" and MODULE_LV = 9 " +
		" and MOUDLE_TYPE not in('MODULE','MENU')" +
		" and  module_flag in "+condition;
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try
		{
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql_1);
			pstm.executeUpdate();
			pstm = conn.prepareStatement(strSql_2);		
			pstm.executeUpdate();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if(pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
				
			}
	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotModuleDao#getSystDicList()
	 */
	public List getSystDicList(String strVer) {
		List<CotModule> res = null;
		List result = new ArrayList();
		res = super.find(" from CotModule obj where obj.moudleParent = 105 and obj.moduleLv <=7 order by obj.moudleOrder ");
//		Map rootNodeMap = new HashMap();
//		Map nodeMap = new HashMap();
//		for(CotModule module : res)
//		{
//			rootNodeMap.put(module.getModuleFlag(), module.getModuleFlag());
//		}
//		if("sample".equals(strVer))
//		{
//			rootNodeMap.remove("PRICE");
//			rootNodeMap.remove("TRADE");
//		}
//		else if("price".equals(strVer))
//		{
//			rootNodeMap.remove("TRADE");
//		}
//		Iterator iterator = rootNodeMap.keySet().iterator();
//		 
//		CotTreeNode topNode = new CotTreeNode();
//		topNode.setNodeid("top");
//		topNode.setNodename("数据字典");
//		topNode.setParentnodeid(null);
//		result.add(topNode);
//		//while(iterator.hasNext())
//		//{
///*			String rootName = iterator.next().toString();
//			String nodeName = "";
//			if(rootName.equals("SAMPLE"))
//				nodeName = "样品数据字典";
//			else if(rootName.equals("PRICE"))
//				nodeName = "报价数据字典";
//			else if(rootName.equals("TRADE"))
//				nodeName = "外贸数据字典";
//				
//			CotTreeNode rootNode = new CotTreeNode();
//			rootNode.setNodeid(rootName+"_root");
//			rootNode.setNodename(nodeName);
//			rootNode.setParentnodeid(topNode.getNodeid());
//			result.add(rootNode);*/
//			for(CotModule module:res)
//			{
//				//if(!topNode.equals(module.getModuleFlag()))
//					//continue;
//				CotTreeNode treeNode = new CotTreeNode();
//				treeNode.setNodeid(module.getId().toString());
//				treeNode.setNodename(module.getModuleName());
//				treeNode.setNodeaction(module.getModuleUrl());
//				treeNode.setParentnodeid(topNode.getNodeid());
//				result.add(treeNode);
//			}
//			
//		//}

		return res;
	}
	
}
