/**
 * 
 */
package com.sail.cot.action.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jcreate.e3.tree.Node;
import net.jcreate.e3.tree.TreeDirector;
import net.jcreate.e3.tree.TreeModel;
import net.jcreate.e3.tree.UncodeException;
import net.jcreate.e3.tree.UserDataUncoder;
import net.jcreate.e3.tree.support.AbstractWebTreeModelCreator;
import net.jcreate.e3.tree.support.DefaultTreeDirector;
import net.jcreate.e3.tree.support.WebTreeBuilder;
import net.jcreate.e3.tree.support.WebTreeNode;
import net.jcreate.e3.tree.xtree.CheckXTreeBuilder;
import net.jcreate.e3.tree.xtree.XTreeBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotModule;
import com.sail.cot.domain.CotRole;
import com.sail.cot.domain.vo.TreeNode;
import com.sail.cot.service.system.CotEmpsService;
import com.sail.cot.service.system.CotModuleService;
import com.sail.cot.service.system.CotPopedomService;
import com.sail.cot.service.system.CotRoleService;
import com.sail.cot.util.GridServerHandler;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 13, 2008 2:39:07 PM </p>
 * <p>Class Name: CotPodedomAction.java </p>
 * @author achui
 *
 */
public class CotPodedomAction extends AbstractAction {

	private CotModuleService cotModuleService;

	public CotModuleService getCotModuleService() {
		if (cotModuleService == null) {
			cotModuleService = (CotModuleService) super
					.getBean("CotModuelService");
		}
		return cotModuleService;
	}
	private CotEmpsService cotEmpsService;

	public CotEmpsService getCotEmpsService() {
		if (cotEmpsService == null) {
			cotEmpsService = (CotEmpsService) super.getBean("CotEmpsService");
		}
		return cotEmpsService;
	}
	
	
	private CotRoleService cotRoleService;
	
	public CotRoleService getCotRoleService() {
		if (cotRoleService == null) {
			cotRoleService = (CotRoleService) super
					.getBean("CotRoleService");
		}
		return cotRoleService;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#add(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#modify(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#query(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps)request.getSession().getAttribute("emp");
		Integer id = new Integer(request.getParameter("id"));
		//获取选中员工编号
		CotEmps selEmp = this.getCotEmpsService().getEmpsById(id);
		List<CotModule> list = null;
		System.out.println("empId:________"+emp.getId().toString());
		if("admin".equals(emp.getEmpsName()))
			list = this.getCotModuleService().getAllModuleListByAdmin();
		else
			list = this.getCotModuleService().getAllModuleListByEmId(emp.getId().toString());
		UserDataUncoder menuUncoder = new UserDataUncoder(){   
		    //获取当前节点编号   
		    public Object getID(Object userData) throws UncodeException {   
		    	CotModule module = (CotModule)userData;   
		        return module.getId();
		    }   
		    //获取父亲节点编号   
		    public Object getParentID(Object userData) throws UncodeException {   
		    	CotModule module = (CotModule)userData;
		        return module.getMoudleParent();
		    }   
		};   
		//Tree模型构造器，用于生成树模型   
		AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator(){   
		    //该方法负责将业务数据映射到树型节点   
		    protected Node createNode(Object userData, UserDataUncoder uncoder) {   
		    	CotModule module = (CotModule)userData;   
//		        WebTreeNode result = new WebTreeNode(module.getModuleName()+module.getId(), "node" + module.getId());   
		        WebTreeNode result = new WebTreeNode(module.getModuleName(), "node" + module.getId());   
		        System.out.println("url:"+module.getModuleUrl());
		        result.setValue(module.getId().toString());
		        //result.setAction("javascript:doAction('" + module.getId().toString() +"')"); 
		        return result;   
		    }   
		};   
		treeModelCreator.init(request);   
		TreeModel treeModel = treeModelCreator.create(list, menuUncoder);   
		TreeDirector director = new DefaultTreeDirector();//构造树导向器   
		//ExtTreeBuilder treeBuilder = new ExtTreeBuilder();//构造树Builder   
		WebTreeBuilder treeBuilder = new CheckXTreeBuilder();   
		treeBuilder.init(request);   
		//treeBuilder.setTitle("eRedC2WP V 0.1");   
		director.build(treeModel, treeBuilder);//执行构造   
		String treeScript = treeBuilder.getTreeScript();//获取构造树的脚本 
		System.out.println(treeScript);
		request.setAttribute("chkTreeScript", treeScript); 
		request.setAttribute("empname", selEmp.getEmpsName());
		return mapping.findForward("querySuccess");
	}

	public ActionForward queryRolePopedom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps)request.getSession().getAttribute("emp");
		Integer id = new Integer(request.getParameter("id"));
		CotRole role = this.getCotRoleService().getRoleById(id);
		try {
			System.out.println(new String (request.getParameter("rolename").getBytes("UTF-8"),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<CotModule> list = null;
		if("admin".equals(emp.getEmpsName()))
			list = this.getCotModuleService().getAllModuleListByAdmin();
		else
			list = this.getCotModuleService().getAllModuleListByEmId(emp.getId().toString());
		UserDataUncoder menuUncoder = new UserDataUncoder(){   
		    //获取当前节点编号   
		    public Object getID(Object userData) throws UncodeException {   
		    	CotModule module = (CotModule)userData;   
		        return module.getId();
		    }   
		    //获取父亲节点编号   
		    public Object getParentID(Object userData) throws UncodeException {   
		    	CotModule module = (CotModule)userData;
		        return module.getMoudleParent();
		    }   
		};   
		//Tree模型构造器，用于生成树模型   
		AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator(){   
		    //该方法负责将业务数据映射到树型节点   
		    protected Node createNode(Object userData, UserDataUncoder uncoder) {   
		    	CotModule module = (CotModule)userData;   
		        WebTreeNode result = new WebTreeNode(module.getModuleName(), "node" + module.getId());   
		        System.out.println("url:"+module.getModuleUrl());
		        result.setValue(module.getId().toString());
		        return result;   
		    }   
		};   
		treeModelCreator.init(request);   
		TreeModel treeModel = treeModelCreator.create(list, menuUncoder);   
		TreeDirector director = new DefaultTreeDirector();//构造树导向器   
		//ExtTreeBuilder treeBuilder = new ExtTreeBuilder();//构造树Builder   
		WebTreeBuilder treeBuilder = new CheckXTreeBuilder();   
		treeBuilder.init(request);   
		//treeBuilder.setTitle("eRedC2WP V 0.1");   
		director.build(treeModel, treeBuilder);//执行构造   
		String treeScript = treeBuilder.getTreeScript();//获取构造树的脚本 
		request.setAttribute("chkRolePopedomTreeScript", treeScript);   
		request.setAttribute("rolename", role.getRoleName());
		return mapping.findForward("queryRolePopedomSuccess");
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#remove(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
