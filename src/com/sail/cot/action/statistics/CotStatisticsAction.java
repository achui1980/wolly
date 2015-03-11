/**
 * 
 */
package com.sail.cot.action.statistics;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jcreate.e3.tree.Node;
import net.jcreate.e3.tree.TreeDirector;
import net.jcreate.e3.tree.TreeModel;
import net.jcreate.e3.tree.UncodeException;
import net.jcreate.e3.tree.UserDataUncoder;
import net.jcreate.e3.tree.support.AbstractWebTreeBuilder;
import net.jcreate.e3.tree.support.AbstractWebTreeModelCreator;
import net.jcreate.e3.tree.support.DefaultTreeDirector;
import net.jcreate.e3.tree.support.WebTreeNode;
import net.jcreate.e3.tree.xtree.CheckXTreeBuilder;
import net.jcreate.e3.tree.xtree.XTreeBuilder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotModule;
import com.sail.cot.domain.CotStatistics;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.service.statistics.CotStatisticsService;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Dec 19, 2008 10:39:37 AM </p>
 * <p>Class Name: CotStatisticsAction.java </p>
 * @author achui
 *
 */
public class CotStatisticsAction extends AbstractAction {

	private CotStatisticsService cotStatisticsService;
	
	public CotStatisticsService getCotStatisticsService() {
		if(cotStatisticsService == null)
		{
			cotStatisticsService = (CotStatisticsService)super.getBean("CotStatisticsService");
		}
		return cotStatisticsService;
	}
	private CotExportRptService rptService ;
	public CotExportRptService getRptService() {
		if(rptService == null)
		{
			rptService = (CotExportRptService)super.getBean("CotRptService");
		}
		return rptService;
	}
	public void setRptService(CotExportRptService rptService) {
		this.rptService = rptService;
	}
	public void setCotStatisticsService(CotStatisticsService cotStatisticsService) {
		this.cotStatisticsService = cotStatisticsService;
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
		return mapping.findForward("queryStatsEdit");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#query(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps)request.getSession().getAttribute("emp");
		String isChkTree = request.getParameter("isChkTree");
		List<CotStatistics> list = null;
		if("admin".equals(emp.getEmpsName()))
			list = this.getCotStatisticsService().getStatisticsListByEmpId(null);
		else
			list = this.getCotStatisticsService().getStatisticsListByEmpId(emp.getId().toString());
		UserDataUncoder menuUncoder = new UserDataUncoder(){   
		    //获取当前节点编号   
		    public Object getID(Object userData) throws UncodeException {   
		    	CotStatistics module = (CotStatistics)userData;   
		        return module.getId();
		    }   
		    //获取父亲节点编号   
		    public Object getParentID(Object userData) throws UncodeException {   
		    	CotStatistics module = (CotStatistics)userData;
		        return module.getStatParent();
		    }   
		};   
		//Tree模型构造器，用于生成树模型   
		AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator(){   
		    //该方法负责将业务数据映射到树型节点   
		    protected Node createNode(Object userData, UserDataUncoder uncoder) {   
		    	CotStatistics module = (CotStatistics)userData;   
		        WebTreeNode result = new WebTreeNode(module.getStatName(), "node" + module.getId());   
		        result.setAction("javascript:doAction('" + module.getStatUrl() +"','"+module.getStatFile()+"','"+module.getStatOrderBy()+"','"+module.getStatName()+"')"); 
		        result.setValue(module.getId().toString());
		        return result;   
		    }   
		};   
		treeModelCreator.init(request);   
		TreeModel treeModel = treeModelCreator.create(list, menuUncoder);   
		TreeDirector director = new DefaultTreeDirector();//构造树导向器   
		//ExtTreeBuilder treeBuilder = new ExtTreeBuilder();//构造树Builder  
		AbstractWebTreeBuilder treeBuilder = null;
		if("true".equals(isChkTree))//生成checkboxTree
			treeBuilder = new CheckXTreeBuilder();
		else
			treeBuilder = new XTreeBuilder();    
		treeBuilder.init(request);   
		director.build(treeModel, treeBuilder);//执行构造   
		String treeScript = treeBuilder.getTreeScript();//获取构造树的脚本   
		request.setAttribute("treeScript", treeScript); 
		System.out.println(treeScript);
		if("true".equals(isChkTree))
				return mapping.findForward("queryChkTreeSuccess");
		return mapping.findForward("querySuccess");
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
	public ActionForward queryRptByArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute("JasperPrint");
		//
		String queryAgain = request.getParameter("queryAgain");
		String flag = request.getParameter("flag");
		
		if(queryAgain == null)
		{
			//获得查询条件
//			String eleId = request.getParameter("eleIdFind");//样品编号
//			String eleName = request.getParameter("eleNameFind");//中文名
//			String eleCol = request.getParameter("eleColFind");//颜色
//			String factoryId = request.getParameter("factoryIdFind");//厂家
			String startTime = request.getParameter("startTime");//起始时间
			String endTime = request.getParameter("endTime");//结束时间
//			String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");//样品类别
			String reportTemple = request.getParameter("reportTemple");//导出模板
//			String orderby = request.getParameter("orderby");
		
			StringBuffer queryString = new StringBuffer();
			queryString.append( " 1=1");
			
//			if(eleId!=null && !eleId.trim().equals("")){
//				queryString.append(" and ele.ELE_ID like '%"+eleId.trim()+"%'");
//			}
//			if(eleName!=null && !eleName.trim().equals("")){
//				queryString.append(" and ele.ELE_NAME like '%"+eleName.trim()+"%'");
//			}
//			if(factoryId!=null && !factoryId.trim().equals("")){
//				queryString.append(" and ele.FACTORY_ID="+factoryId);
//			}
//			if(eleCol!=null && !eleCol.trim().equals("")){
//				queryString.append(" and ele.ELE_COL like '%"+eleCol.trim()+"%'");
//			}
//			if(eleTypeidLv1!=null && !eleTypeidLv1.trim().equals("")){
//				queryString.append(" and ele.ELE_TYPEID_LV1="+eleTypeidLv1);
//			}
// 
			
//			//需要加排序字段
//			queryString.append(" order by ");
//			String[] arrs = orderby.split(";");
//			for(int i=0; i<arrs.length; i++)
//			{
//				if("".equals(arrs[i]))
//					continue;
//				queryString.append(" ").append(arrs[i]);
//				if(i< arrs.length -1)
//					queryString.append(" ,");
//			}
//			
			//报表模块文件的位置
			String rptXMLpath = request.getRealPath("/")+File.separator + reportTemple;
			//String exportPath = request.getRealPath("/reportfile")+File.separator + "elements_nopic.xls";
			//设置模板的参数(查询条件)
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			if(startTime!=null && !"".equals(startTime)){
				paramMap.put("startTime", startTime.toString());
			}
			if(endTime!=null && !"".equals(endTime)){
				paramMap.put("endTime", endTime.toString());
			}
		    jasperPrint = this.getRptService().getJasperPrint(rptXMLpath, paramMap);
		    request.getSession().setAttribute("JasperPrint",jasperPrint);
		    request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,   jasperPrint);
		}

		StringBuffer sbuffer = new StringBuffer();	
		JRHtmlExporter exporter = new JRHtmlExporter();
		
		 int pageIndex = 0;
		 int lastPageIndex = 0;
		 if (jasperPrint.getPages() != null){
		    lastPageIndex = jasperPrint.getPages().size();
		 }
		 request.setAttribute("lastPageIndex", lastPageIndex);
		 if(lastPageIndex==0){
			 return mapping.findForward("previewSuccess");
		 }
		 String pageStr = request.getParameter("pageIndex");
		 try{
		    if( pageStr != null)
		        pageIndex = Integer.parseInt(pageStr);
		 }catch(Exception e){
		    //e.printStackTrace();
		 }
		 
		 if (pageIndex < 0){
		    pageIndex = 0;
		 }
		 
		 if (pageIndex > lastPageIndex){
		    pageIndex = lastPageIndex;
		 }

		 exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		 exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
		 //exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,   "/CotSystem/servlets/image?image=");
		 exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,   "servlets/image?image=");
		 exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
   
		 exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
		 exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		 exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
		 
		 /*
		 HashMap imagesMap = new HashMap();
		 exporter.setParameter(JRExporterParameter.JASPER_PRINT,   jasperPrint);   
		 exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,   sbuffer);   
		 exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,   imagesMap);   
		 exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML,   "<br   style='page-break-before:always;'>");   
		 exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,   "/CotSystem/servlets/image?image=");   
		 exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);//   ?ColumnHeader   
		 exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.TRUE);   
		 */		
		try {
			System.out.println("*******"+sbuffer.toString());
			exporter.exportReport();
			request.setAttribute("preview", sbuffer.toString()); 
			//System.out.println("___"+sbuffer.toString());
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapping.findForward("queryRptSuccess");
	}
	
	//统计报表
	public ActionForward querystatistics(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String rptType =request.getParameter("rptType"); //报表类型
		String statName =request.getParameter("statName"); //报表名称
		if(start == null || limit == null)
			return mapping.findForward("queryStats");
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		queryString.append(" and obj.statParent <> 1");
		if (rptType != null && !rptType.trim().equals("")) {
			queryString.append(" and obj.statParent="+ Integer.parseInt(rptType.trim()));
		}
		if (statName != null && !statName.trim().equals("")) {
			queryString.append(" and obj.statName like '%"+statName.trim()+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 设定每页显示记录数
		pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotStatistics obj"+ queryString);
		queryInfo.setSelectString("from CotStatistics obj ");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		try {
			String json = this.getCotStatisticsService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		//return mapping.findForward("queryRpt"); 
	}

}
