package com.sail.cot.action.worklog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.worklog.CotMessageService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;


public class CotMessageAction extends AbstractAction{

	private CotMessageService cotMessageService;
	private CotMessageService getCotMessageService() {
		// TODO Auto-generated method stub
		if(cotMessageService==null)
		{
			cotMessageService = (CotMessageService) super.getBean("CotMessageService");
		}
		return cotMessageService;
	}
 
	public void setCotMessageService(CotMessageService cotMessageService) {
		this.cotMessageService = cotMessageService;
	}
	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("modify");
	}
	
	public ActionForward send(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("send");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String msgOrderNo=request.getParameter("noFind");//单号
		String msgStatus = request.getParameter("msgStatusFind");
		String msgType = request.getParameter("msgTypeFind");
		String msgTypeId = request.getParameter("msgTypeIdFind");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		StringBuffer queryString = new StringBuffer();
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		//如果是admin可以看到全部
		if (!emp.getEmpsId().equals("admin") && (msgType == null || msgType.trim().equals(""))) {
			queryString.append(" where (obj.msgToPerson=0 or obj.msgToPerson="+emp.getId()+" or obj.msgFromPerson ="+emp.getId()+")");
		}
		if(msgType != null && !msgType.trim().equals(""))
		{
			msgType = msgType.trim();
			if (msgType.trim().equals("send")) {
				queryString.append(" where obj.msgFromPerson="+emp.getId());
			}
			if (msgType.trim().equals("recv")) {
				queryString.append(" where obj.msgToPerson="+emp.getId());
			}
		}
		if(msgType != null && !msgType.trim().equals(""))
		{
			msgType = msgType.trim();
			if (msgType.trim().equals("send")) {
				queryString.append(" and obj.msgFromPerson="+emp.getId());
			}
			if (msgType.trim().equals("recv")) {
				queryString.append(" and obj.msgToPerson="+emp.getId());
			}
		}
		if(msgOrderNo != null && !msgOrderNo.trim().equals(""))
		{
			msgOrderNo = msgOrderNo.trim();
			queryString.append(" and obj.msgOrderNo like '%"+msgOrderNo+"%'");
		}
		if (msgTypeId != null && !msgTypeId.trim().equals("")) {
			queryString.append(" and obj.msgTypeId=" + msgTypeId.trim());
		}
		if (msgStatus != null && !msgStatus.trim().equals("")) {
			queryString.append(" and obj.msgStatus=" + msgStatus.trim());
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.msgBeginDate >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.msgBeginDate <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.msgBeginDate between '"+startTime+"' and '"+endTime+"'");
		}
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotMessage obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotMessage obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getCotMessageService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public ActionForward queryPlatForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps)request.getSession().getAttribute("emp");
		String warnhtml = this.getCotMessageService().getWarnMessage(emp.getId(), emp.getEmpsId());
		String mutualhtml = this.getCotMessageService().getMutualMessage(emp.getId(), emp.getEmpsId());
		String grouphtml = this.getCotMessageService().getGroupMessage();
		String agencyhtml = this.getCotMessageService().getAgencyMessage(emp.getId(), emp.getEmpsId());
		request.setAttribute("warnMessage", warnhtml);
		request.setAttribute("mutualMessage", mutualhtml);
		request.setAttribute("groupMessage", grouphtml);
		request.setAttribute("agencyMessage", agencyhtml);
		return mapping.findForward("queryPlatForm");
	}
	public ActionForward queryUnreadMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		Limit limit=RequestUtils.getLimit(request,"messageTable");
//		Map sortValueMap = limit.getSort().getSortValueMap();
//		Map filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//
//		String msgOrderNo=request.getParameter("No");//单号
//		String startTime = request.getParameter("startTime");
//		String endTime = request.getParameter("endTime");
//		
//		StringBuffer queryString = new StringBuffer();
//		//不显示admin用户
//		//queryString.append(" where 1=1");
//		
//		
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
////		if ("admin".equals(emp.getEmpsId())) {
////			queryString.append(" where 1=1");
////		} else {
//		queryString.append(" where obj.msgToPerson="+emp.getId()+" and obj.msgStatus=0 and obj.msgFlag =1");
////		}
//		
//		if(msgOrderNo != null && !msgOrderNo.trim().equals(""))
//		{
//			msgOrderNo = msgOrderNo.trim();
//			queryString.append(" and obj.msgOrderNo like '%"+msgOrderNo+"%'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
//			queryString.append(" and obj.msgBeginDate >='"+startTime+"'");
//		}
//		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.msgBeginDate <='"+endTime+"'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.msgBeginDate between '"+startTime+"' and '"+endTime+"'");
//		}
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 5;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("messageTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		//设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotMessage obj"+queryString);
//		//设置查询记录语句
//		queryInfo.setSelectString("from CotMessage obj");
//		//设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		//设置排序语句
//		queryInfo.setOrderString("");
//		int totalCount = this.getCotMessageService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 3);
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		Map mapFlag = new HashMap();
//		mapFlag.put("0", "否");
//		mapFlag.put("1", "是");
//		
//		Map mapStatus = new HashMap();
//		mapStatus.put("0", "未读");
//		mapStatus.put("1", "已读,未处理");
//		mapStatus.put("2", "已读,已处理");
//		mapStatus.put("3", "未处理,提醒结束");
//		
//		Map mapType = new HashMap();
//		mapType.put("send", "发送");
//		mapType.put("recv", "接受");
//		
//		request.setAttribute("flagMapping", mapFlag);
//		request.setAttribute("statusMapping", mapStatus);
//		request.setAttribute("typeMapping", mapType);
//		request.setAttribute("msgTypeMapping", this.getCotMessageService().getMsgTypeMap());
//		request.setAttribute("sendMapping", this.getCotMessageService().getEmpsMap());
//		request.setAttribute("recvMapping", this.getCotMessageService().getEmpsMap());
//		request.setAttribute("cotMessage", this.getCotMessageService().getList(queryInfo));
		return mapping.findForward("queryUnreadMessage");
	}
	public ActionForward queryUnHandleMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		Limit limit=RequestUtils.getLimit(request,"messageTable");
//		Map sortValueMap = limit.getSort().getSortValueMap();
//		Map filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//
//		String msgOrderNo=request.getParameter("No");//单号
//		String startTime = request.getParameter("startTime");
//		String endTime = request.getParameter("endTime");
//		
//		StringBuffer queryString = new StringBuffer();
//		//不显示admin用户
//		//queryString.append(" where 1=1");
//		
//		
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
////		if ("admin".equals(emp.getEmpsId())) {
////			queryString.append(" where 1=1");
////		} else {
//		queryString.append(" where obj.msgToPerson="+emp.getId()+" and obj.msgStatus=1");
////		}
//		
//		if(msgOrderNo != null && !msgOrderNo.trim().equals(""))
//		{
//			msgOrderNo = msgOrderNo.trim();
//			queryString.append(" and obj.msgOrderNo like '%"+msgOrderNo+"%'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
//			queryString.append(" and obj.msgBeginDate >='"+startTime+"'");
//		}
//		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.msgBeginDate <='"+endTime+"'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.msgBeginDate between '"+startTime+"' and '"+endTime+"'");
//		}
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 5;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("messageTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		//设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotMessage obj"+queryString);
//		//设置查询记录语句
//		queryInfo.setSelectString("from CotMessage obj");
//		//设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		//设置排序语句
//		queryInfo.setOrderString("");
//		int totalCount = this.getCotMessageService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 3);
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		Map mapFlag = new HashMap();
//		mapFlag.put("0", "否");
//		mapFlag.put("1", "是");
//		
//		Map mapStatus = new HashMap();
//		mapStatus.put("0", "未读");
//		mapStatus.put("1", "已读,未处理");
//		mapStatus.put("2", "已读,已处理");
//		mapStatus.put("3", "未处理,提醒结束");
////		mapStatus.put("4", "已处理,提醒结束");
//		
//		Map mapType = new HashMap();
//		mapType.put("send", "发送");
//		mapType.put("recv", "接受");
//		
//		request.setAttribute("flagMapping", mapFlag);
//		request.setAttribute("statusMapping", mapStatus);
//		request.setAttribute("typeMapping", mapType);
//		request.setAttribute("msgTypeMapping", this.getCotMessageService().getMsgTypeMap());
//		request.setAttribute("sendMapping", this.getCotMessageService().getEmpsMap());
//		request.setAttribute("recvMapping", this.getCotMessageService().getEmpsMap());
//		request.setAttribute("cotMessage", this.getCotMessageService().getList(queryInfo));
		return mapping.findForward("queryUnHandleMessage");
	}
	public ActionForward queryOverMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		Limit limit=RequestUtils.getLimit(request,"messageTable");
//		Map sortValueMap = limit.getSort().getSortValueMap();
//		Map filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//
//		String msgOrderNo=request.getParameter("No");//单号
//		String startTime = request.getParameter("startTime");
//		String endTime = request.getParameter("endTime");
//		
//		StringBuffer queryString = new StringBuffer();
//		//不显示admin用户
//		//queryString.append(" where 1=1");
//		
//		
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
////		if ("admin".equals(emp.getEmpsId())) {
////			queryString.append(" where 1=1");
////		} else {
//		queryString.append(" where obj.msgToPerson="+emp.getId()+" and obj.msgStatus=3");
////		}
//		
//		if(msgOrderNo != null && !msgOrderNo.trim().equals(""))
//		{
//			msgOrderNo = msgOrderNo.trim();
//			queryString.append(" and obj.msgOrderNo like '%"+msgOrderNo+"%'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
//			queryString.append(" and obj.msgBeginDate >='"+startTime+"'");
//		}
//		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.msgBeginDate <='"+endTime+"'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.msgBeginDate between '"+startTime+"' and '"+endTime+"'");
//		}
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 5;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("messageTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		//设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotMessage obj"+queryString);
//		//设置查询记录语句
//		queryInfo.setSelectString("from CotMessage obj");
//		//设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		//设置排序语句
//		queryInfo.setOrderString("");
//		int totalCount = this.getCotMessageService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 3);
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		Map mapFlag = new HashMap();
//		mapFlag.put("0", "否");
//		mapFlag.put("1", "是");
//		
//		Map mapStatus = new HashMap();
//		mapStatus.put("0", "未读");
//		mapStatus.put("1", "已读,未处理");
//		mapStatus.put("2", "已读,已处理");
//		mapStatus.put("3", "未处理,提醒结束");
////		mapStatus.put("4", "已处理,提醒结束");
//		
//		Map mapType = new HashMap();
//		mapType.put("send", "发送");
//		mapType.put("recv", "接受");
//		
//		request.setAttribute("flagMapping", mapFlag);
//		request.setAttribute("statusMapping", mapStatus);
//		request.setAttribute("typeMapping", mapType);
//		request.setAttribute("msgTypeMapping", this.getCotMessageService().getMsgTypeMap());
//		request.setAttribute("sendMapping", this.getCotMessageService().getEmpsMap());
//		request.setAttribute("recvMapping", this.getCotMessageService().getEmpsMap());
//		request.setAttribute("cotMessage", this.getCotMessageService().getList(queryInfo));
		return mapping.findForward("queryOverMessage");
	}
	
	// 查询提醒,互发,公告信息,工作计划
	public ActionForward queryByModel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String msgTypeId = request.getParameter("flag");
		
		if(start == null || limit == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.msgTypeId = "+msgTypeId+" and obj.msgFlag = 1 " +
						"and (obj.msgStatus = 0 or obj.msgStatus = 1) ");
		
		// 获得登录人
		if(!"4".equals(msgTypeId)){
			CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
			queryString.append("and obj.msgToPerson="+emp.getId());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotMessage obj "+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotMessage obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		
		int count = this.getCotMessageService().getRecordCount(queryInfo);

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotMessageService().getList(queryInfo);
		
		try {
			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
