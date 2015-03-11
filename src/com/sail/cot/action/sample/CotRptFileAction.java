package com.sail.cot.action.sample;
 

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
  

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jason.core.exception.DAOException;
import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotRptFileService;
import com.sail.cot.util.SystemUtil;

public class CotRptFileAction extends AbstractAction {

	private CotRptFileService cotRptFileService;
	
	public CotRptFileService getCotRptFileService() {
		if(cotRptFileService==null)
		{
			cotRptFileService = (CotRptFileService) super.getBean("CotRptFileService");
		}
		return cotRptFileService;
	}

	public void setCotRptFileService(CotRptFileService cotRptFileService) {
		this.cotRptFileService = cotRptFileService;
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
		String rptType =request.getParameter("rptType"); //报表类型
		String rptFile =request.getParameter("rptFile"); //报表名称
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (rptType != null && !rptType.trim().equals("")) {
			queryString.append(" and obj.rptType="
					+ Integer.parseInt(rptType.trim()));
		}
		if (rptFile != null && !rptFile.trim().equals("")) {
			queryString.append(" and obj.id="
					+ Integer.parseInt(rptFile.trim()));
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 设定每页显示记录数
		pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotRptFile obj"+ queryString);
		queryInfo.setSelectString("from CotRptFile obj ");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.rptType ,obj.id");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		try {
			String json = this.getCotRptFileService().getJsonData(queryInfo);
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
	
	public ActionForward uploadImg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("uploadImg");
	}
	
	public ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		/*try {
			//request.setCharacterEncoding("GBK");
			System.out.println(request.getParameter("rName"));
			String rName = new String(request.getParameter("rName").getBytes("iso-8859-1"),"gbk");
			 
			System.out.println("================"+rName);
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}*/
		  
		  return mapping.findForward("uploadFile");
	}
 
}
