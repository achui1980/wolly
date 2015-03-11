/**
 * 
 */
package com.sail.cot.action.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.jcreate.e3.tree.Node;
import net.jcreate.e3.tree.TreeDirector;
import net.jcreate.e3.tree.TreeModel;
import net.jcreate.e3.tree.UncodeException;
import net.jcreate.e3.tree.UserDataUncoder;
import net.jcreate.e3.tree.support.AbstractWebTreeModelCreator;
import net.jcreate.e3.tree.support.DefaultTreeDirector;
import net.jcreate.e3.tree.support.WebTreeNode;
import net.jcreate.e3.tree.xtree.XTreeBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotModule;
import com.sail.cot.domain.CotServerInfo;
import com.sail.cot.domain.vo.CotTreeNode;
import com.sail.cot.domain.vo.TreeNode;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotModuleService;
import com.sail.cot.util.GridServerHandler;
/**
 * 模块管理
 * @author qh-chzy
 *
 */
public class CotModuleAction extends AbstractAction {

	private CotModuleService cotModuleService;

	public CotModuleService getCotModuleService() {
		if (cotModuleService == null) {
			cotModuleService = (CotModuleService) super
					.getBean("CotModuelService");
		}
		return cotModuleService;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps)request.getSession().getAttribute("emp");
		List<CotModule> list = null;
		if("admin".equals(emp.getEmpsName()))
			list = this.getCotModuleService().getMenus();
		else
			list = this.getCotModuleService().getModuleListByEmpId(emp.getId().toString());
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
		        result.setAction("javascript:doAction('" + module.getModuleUrl() +"')"); 
		        return result;   
		    }   
		};   
		treeModelCreator.init(request);   
		TreeModel treeModel = treeModelCreator.create(list, menuUncoder);   
		TreeDirector director = new DefaultTreeDirector();//构造树导向器   
		//ExtTreeBuilder treeBuilder = new ExtTreeBuilder();//构造树Builder   
		XTreeBuilder treeBuilder = new XTreeBuilder();   
		treeBuilder.init(request);   
		//treeBuilder.setTitle("eRedC2WP V 0.1");   
		director.build(treeModel, treeBuilder);//执行构造   
		String treeScript = treeBuilder.getTreeScript();//获取构造树的脚本   
		request.setAttribute("treeScript", treeScript);   

		return mapping.findForward("querySuccess");
	}
	
	public ActionForward queryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//其中moduleTable为页面上table的ID
		QueryInfo queryInfo = new QueryInfo();
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(20);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotModule obj where 1=1");
		//设置查询记录语句
		queryInfo.setSelectString("from CotModule obj ");
		//设置查询条件语句
		queryInfo.setQueryString("");
		//设置排序语句
		queryInfo.setOrderString("");
		request.setAttribute("allModuleName", this.getCotModuleService().getModuleNameMap());
		return mapping.findForward("queryListSuccess");
	}
	
	public ActionForward queryModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryModuleSuccess");
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	public ActionForward queryTest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps)request.getSession().getAttribute("emp");
		//String html = this.getCotModuleService().getModuleHtml(emp.getId().toString(), emp.getEmpsId());
		//request.setAttribute("moduleHtml", html);
		return mapping.findForward("queryHtmlSuccess");
	}
	public ActionForward querySysDic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String flag = request.getParameter("query");
		if(flag == null)
			return mapping.findForward("querySysDic");
		List<TreeNode> list = null;
		HttpSession session = request.getSession();
		CotServerInfo serverInfo = (CotServerInfo)session.getAttribute("CotServerInfo");
		list = this.getCotModuleService().getSysDicTree(serverInfo.getSoftVer());
		String excludes[] = {"map","parentMap"};
		GridServerHandler gd = new GridServerHandler(excludes);
		gd.setData(list);
		gd.setTotalCount(list.size());
		JSONObject jsonObj = gd.getLoadResponseJSON();
		JSONArray jsonArray = (JSONArray)jsonObj.get("data");
		System.out.println("sdfsdfs:"+jsonArray.toString());
		try {
			response.getWriter().write(jsonArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
//		List<CotTreeNode> list = null;
//		HttpSession session = request.getSession();
//		CotServerInfo serverInfo = (CotServerInfo)session.getAttribute("CotServerInfo");
//		
//		list = this.getCotModuleService().getSystDicList(serverInfo.getSoftVer());
//		
//
//		UserDataUncoder menuUncoder = new UserDataUncoder() {
//			// 获取当前节点编号
//			public Object getID(Object userData) throws UncodeException {
//				CotTreeNode module = (CotTreeNode) userData;
//				return module.getNodeid();
//			}
//
//			// 获取父亲节点编号
//			public Object getParentID(Object userData) throws UncodeException {
//				CotTreeNode module = (CotTreeNode) userData;
//				return module.getParentnodeid();
//			}
//		};
//		// Tree模型构造器，用于生成树模型
//		AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator() {
//			// 该方法负责将业务数据映射到树型节点
//			protected Node createNode(Object userData, UserDataUncoder uncoder) {
//				CotTreeNode module = (CotTreeNode) userData;
//				WebTreeNode result = new WebTreeNode(module.getNodename(),
//						"node" + module.getNodeid());
//				result.setAction("javascript:doAction('" + module.getNodeid()
//						+ "','" + module.getNodeaction() + "')");
//				return result;
//			}
//		};
//		treeModelCreator.init(request);
//		TreeModel treeModel = treeModelCreator.create(list, menuUncoder);
//		TreeDirector director = new DefaultTreeDirector();// 构造树导向器
//		// ExtTreeBuilder treeBuilder = new ExtTreeBuilder();//构造树Builder
//		XTreeBuilder treeBuilder = new XTreeBuilder();
//		treeBuilder.init(request);
//		// treeBuilder.setTitle("eRedC2WP V 0.1");
//		director.build(treeModel, treeBuilder);// 执行构造
//		String treeScript = treeBuilder.getTreeScript();// 获取构造树的脚本
//		System.out.println(treeScript);
//		request.setAttribute("treeScript", treeScript);
//
//		return mapping.findForward("querySysDic");
	}
	

}
