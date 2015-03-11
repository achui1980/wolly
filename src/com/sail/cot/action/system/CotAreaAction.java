package com.sail.cot.action.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import net.jcreate.e3.tree.Node;
import net.jcreate.e3.tree.TreeDirector;
import net.jcreate.e3.tree.TreeModel;
import net.jcreate.e3.tree.UncodeException;
import net.jcreate.e3.tree.UserDataUncoder;
import net.jcreate.e3.tree.support.AbstractWebTreeModelCreator;
import net.jcreate.e3.tree.support.DefaultTreeDirector;
import net.jcreate.e3.tree.support.WebTreeNode;
import net.jcreate.e3.tree.xtree.XTreeBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.ApplicationContext;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.vo.CotTreeNode;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotAreaService;
import com.sail.cot.util.SystemUtil;
 
 

public class CotAreaAction extends AbstractAction {

	private CotAreaService cotAreaService;
	public CotAreaService getCotAreaService()
	{
		if(cotAreaService==null)
		{
			cotAreaService=(CotAreaService) super.getBean("CotAreaService");
		}
		return cotAreaService;
	}
	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		   return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		 
		String areaName = request.getParameter("areaName");
		String areaCode = request.getParameter("areaCode");
		String cityId = request.getParameter("cityId");
		System.out.println("cityId:"+cityId);
		StringBuffer sql=new StringBuffer();
		if(null!=cityId && !"".equals(cityId) && !"0".equals(cityId))
		{
			cityId=cityId.trim();
			sql.append(" and obj.cityId ="+cityId);
		}
		if(null!=areaCode && !"".equals(areaCode) )
		{
			areaCode=areaCode.trim();
			sql.append(" and obj.areaCode like '%"+areaCode+"%'");
		}
		if(null!=areaName && !"".equals(areaName))
		{
			areaName=areaName.trim();
			sql.append(" and obj.areaName like '%"+areaName+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotArea obj where 1=1"+sql.toString());
		//设置查询记录语句
		queryInfo.setSelectString("from CotArea obj where 1=1"); 
		//设置查询条件语句
		queryInfo.setQueryString(sql.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotAreaService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}

	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
		
	}
	@SuppressWarnings("unchecked")
	public ActionForward queryTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List<CotTreeNode> list = null;
		list = (List<CotTreeNode>) this.getCotAreaService().getCityAreaTree();
		
		UserDataUncoder menuUncoder = new UserDataUncoder(){   
		    //获取当前节点编号   
		    public Object getID(Object userData) throws UncodeException {   
		    	CotTreeNode module = (CotTreeNode)userData;   
		        return module.getNodeid();
		    }   
		    //获取父亲节点编号   
		    public Object getParentID(Object userData) throws UncodeException {   
		    	CotTreeNode module = (CotTreeNode)userData;
		        return module.getParentnodeid();
		    }   
		};   
		//Tree模型构造器，用于生成树模型   
		AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator(){   
		    //该方法负责将业务数据映射到树型节点   
		    protected Node createNode(Object userData, UserDataUncoder uncoder) {   
		    	CotTreeNode module = (CotTreeNode)userData;
		        WebTreeNode result = new WebTreeNode(module.getNodename(), "node" + module.getNodeid());   
		        result.setAction("javascript:doAction('" + module.getNodeid() +"')"); 
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
		return mapping.findForward("queryAreaSuccess");
	}
	 
}
