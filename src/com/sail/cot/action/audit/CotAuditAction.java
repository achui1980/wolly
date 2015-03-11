package com.sail.cot.action.audit;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotAudit;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.audit.CotAuditService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;
 
 

public class CotAuditAction extends AbstractAction {

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
		 
		   return mapping.findForward("auditCreate");
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
		String auditStatus = request.getParameter("auditStatus");
		
		StringBuffer queryString=new StringBuffer();
		queryString.append("where 1 = 1");
		
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
		
		if (auditStatus != null && !auditStatus.trim().equals("")) {
			queryString.append(" and obj.audit_status="+auditStatus.trim());
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.receive_date >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.receive_date <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.receive_date between '"+startTime+"' and '"+endTime+"'");
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
		queryInfo.setOrderString(" order by obj.id desc");
		 
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

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}
	 
}
