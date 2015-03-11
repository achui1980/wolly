package com.sail.cot.action.backtax;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotAudit;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.audit.CotAuditService;
import com.sail.cot.service.backtax.CotBackTaxService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;
 
 

public class CotBackTaxAction extends AbstractAction {

	private CotBackTaxService cotBackTaxService;
	public CotBackTaxService getCotBackTaxService()
	{
		if(cotBackTaxService==null)
		{
			cotBackTaxService=(CotBackTaxService) super.getBean("CotBackTaxService");
		}
		return cotBackTaxService;
	}
	
	private CotAuditService cotAuditService;
	public CotAuditService getCotAuditService()
	{
		if(cotAuditService==null)
		{
			cotAuditService=(CotAuditService) super.getBean("CotAuditService");
		}
		return cotAuditService;
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
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String taxStatus = request.getParameter("taxStatus");
		
		StringBuffer queryString=new StringBuffer();
		
		//最高权限
		boolean all = SystemUtil.isAction(request, "cotbacktax.do", "ALL");
		//部门权限
		boolean dept = SystemUtil.isAction(request, "cotbacktax.do","DEPT");		
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.empId=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				queryString.append(" where obj.empId=e.id");
			} else {
				// 判断是否有查看该部门征样的权限
				if (dept == true) {
					queryString .append(" where obj.empId = e.id and e.deptId=" + emp.getDeptId());
				} else {
					queryString .append(" where obj.empId = e.id and obj.empId =" + emp.getId());
				}
			}
		}
		
		if (taxStatus != null && !taxStatus.trim().equals("")) {
			queryString.append(" and obj.taxStatus="+taxStatus.trim());
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.taxDate >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.taxDate <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.taxDate between '"+startTime+"' and '"+endTime+"'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotBacktax obj,CotEmps e"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotBacktax obj,CotEmps e");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");
		 
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		
		try {
			
			String json = this.getCotBackTaxService().getJsonData(queryInfo);
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
	
	public ActionForward addAudit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String taxId = request.getParameter("taxId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		
		StringBuffer queryString=new StringBuffer();
		queryString.append("where 1 = 1 and obj.audit_status = 4 and obj.tax_id =" +taxId);
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotaudit.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门的权限
				boolean dept = SystemUtil.isAction(request, "cotaudit.do","DEPT");
				if (dept == true) {
					queryString.append(" and e.dept_id=" + emp.getDeptId());
				} else {
					queryString
							.append(" and (obj.business_person is null or obj.business_person =" + emp.getId() + ")");
				}
			}
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from cot_audit obj left join cot_emps e on obj.business_person = e.id "+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj.* from cot_audit obj left join cot_emps e on obj.business_person = e.id ");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		//核销单逾期或迟交判断
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		this.getCotAuditService().updateStatus(sf.format(System.currentTimeMillis()));
		
		try {
			List list = this.getCotAuditService().getAuditList(queryInfo);
			int totalCount = this.getCotAuditService().getRecordCount(queryInfo);
			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(totalCount);
			
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public ActionForward queryAudit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		
		StringBuffer queryString=new StringBuffer();
		queryString.append("where 1 = 1 and obj.audit_status = 4 and obj.tax_id is null");
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotaudit.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门的权限
				boolean dept = SystemUtil.isAction(request, "cotaudit.do","DEPT");
				if (dept == true) {
					queryString.append(" and e.dept_id=" + emp.getDeptId());
				} else {
					queryString
							.append(" and (obj.business_person is null or obj.business_person =" + emp.getId() + ")");
				}
			}
		}
		
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.audit_date >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.audit_date <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.audit_date between '"+startTime+"' and '"+endTime+"'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from cot_audit obj left join cot_emps e on obj.business_person = e.id "+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj.* from cot_audit obj left join cot_emps e on obj.business_person = e.id ");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");
		 
		
		int startIndex = Integer.parseInt(start);
		
		queryInfo.setStartIndex(startIndex);
		
		
		//核销单逾期或迟交判断
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		this.getCotAuditService().updateStatus(sf.format(System.currentTimeMillis()));
		
		try {
			List list = this.getCotAuditService().getAuditList(queryInfo);
			int totalCount = this.getCotAuditService().getRecordCount(queryInfo);
			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(totalCount);
			
			String json = gd.getLoadResponseText();
			System.out.println("json:"+json);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward modifAudit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//前台传递来的数据
		String taxIdString = request.getParameter("taxId");
		Integer taxId = Integer.parseInt(taxIdString);
		String json = request.getParameter("data");
		boolean single = json.startsWith("{");
		List<CotAudit> audit = new ArrayList<CotAudit>();
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Integer id = Integer.parseInt(jsonObject.getString("id"));
			CotAudit cotAudit = this.getCotAuditService().getCotAuditById(id);
			cotAudit.setTaxId(taxId);
			this.getCotAuditService().savaOrUpdateAudit(cotAudit);
		}
		else {
			JSONArray jarray = JSONArray.fromObject(json);
			for(int i=0; i<jarray.size();i++)
			{
				JSONObject jsonObject = jarray.getJSONObject(i);
				Integer id = Integer.parseInt(jsonObject.getString("id"));
				CotAudit cotAudit = this.getCotAuditService().getCotAuditById(id);
				cotAudit.setTaxId(taxId);
				this.getCotAuditService().savaOrUpdateAudit(cotAudit);
			}
		}
		
		return null;
		
	}
	public ActionForward delAudit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String taxIdString = request.getParameter("taxId");
		Integer taxId = Integer.parseInt(taxIdString);
		String json = request.getParameter("data");
		List ids = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if(!single){
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		this.getCotBackTaxService().modifyTaxId(taxId, ids);
		return null;
	}
	 
}
