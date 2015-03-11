package com.sail.cot.action.systemdic;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotProvinceService;
import com.sail.cot.util.SystemUtil;

public class CotProvinceAction extends AbstractAction {

	private CotProvinceService cotProvinceService;
	
	public CotProvinceService getCotProvinceService() {
		if(cotProvinceService==null){
			cotProvinceService = (CotProvinceService) super.getBean("CotProvinceService");
		}
		return cotProvinceService;
	}

	public void setCotProvinceService(CotProvinceService cotProvinceService) {
		this.cotProvinceService = cotProvinceService;
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
		
		String provinceName = request.getParameter("provinceName");
		String nationId = request.getParameter("nationId");
		StringBuffer sql=new StringBuffer();
		if(null!=nationId && !"".equals(nationId))
		{
			nationId=nationId.trim();
			sql.append(" and obj.nationId ="+nationId);
		}
		if(null!=provinceName && !"".equals(provinceName))
		{
			provinceName=provinceName.trim();
			sql.append(" and obj.provinceName like '%"+provinceName+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		
		queryInfo.setCountQuery("select count(*) from CotProvince obj where 1=1"+sql.toString());
		 
		queryInfo.setSelectString("from CotProvince obj where 1=1");
		 
		queryInfo.setQueryString(sql.toString());
	 
		queryInfo.setOrderString("");
		 
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotProvinceService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	    return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}

}
