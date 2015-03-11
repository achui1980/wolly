package com.sail.cot.action.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotSeqService;

public class CotSeqAction extends AbstractAction {

	private CotSeqService cotSeqService;

	public CotSeqService getCotSeqService() {
		if (cotSeqService == null) {
			cotSeqService = (CotSeqService) super.getBean("CotSeqService");
		}
		return cotSeqService;
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
		String seqId=request.getParameter("orderNoFind");

		if (start == null || limit == null)
			return mapping.findForward("querySuccess");

		QueryInfo queryInfo = new QueryInfo();
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(seqId!=null && !seqId.equals("")){
			queryString.append(" and obj.id="+Integer.parseInt(seqId));
		}
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotSeq obj"+ queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotSeq obj");
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		try {
			String json = this.getCotSeqService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	public ActionForward queryCfg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotSeqCfg obj where 1=1");
		//设置查询记录语句
		queryInfo.setSelectString("from CotSeqCfg obj");
		//设置查询条件语句
		queryInfo.setQueryString("");
		//设置排序语句
		queryInfo.setOrderString(" order by obj.type,id");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		try {
			String json = this.getCotSeqService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
