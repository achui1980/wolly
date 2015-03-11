package com.sail.cot.action.worklog;
 
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
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.util.SystemUtil;

public class CotSyslogAction extends AbstractAction {

	private  CotSysLogService cotSysLogService;
	public CotSysLogService getCotSysLogService() {
		if(cotSysLogService==null){
			cotSysLogService = (CotSysLogService)super.getBean("CotSysLogService");
		}
		return cotSysLogService;
	}

	public void setCotSysLogService(CotSysLogService cotSysLogService) {
		this.cotSysLogService = cotSysLogService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String empId = request.getParameter("empIdFind");//操作员工
		String opModule = request.getParameter("opModuleFind");// 操作模块
		String opType = request.getParameter("opTypeFind");// 操作类型
		String startTime = request.getParameter("startTime");// 操作起始日期
		String endTime = request.getParameter("endTime");// 操作结束日期
		
		StringBuffer queryString = new StringBuffer();
		
		if (empId != null && !empId.trim().equals("")) {
			queryString.append(" and obj.empId=" + empId.trim());
		}
		if (opModule != null && !opModule.trim().equals("")) {
			queryString.append(" and obj.opModule = '" + opModule.trim() + "'");
		}
		if (opType != null && !opType.trim().equals("")) {
			if(opType.equals("-1")){
				opType="0";
			}
			queryString.append(" and obj.opType =" + opType.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.opTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.opTime <='" + endTime
					+ " 23:59:59'");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotSyslog AS obj where 1=1 " + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj from CotSyslog AS obj where 1=1");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		
		try {
			String json = this.getCotSysLogService().getJsonData(queryInfo);
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
