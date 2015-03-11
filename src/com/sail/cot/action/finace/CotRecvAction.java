package com.sail.cot.action.finace;

import java.util.Iterator;
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
import com.sail.cot.service.finace.CotRecvService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

public class CotRecvAction extends AbstractAction {

	private CotRecvService recvService;

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		String financeNo = request.getParameter("financeNo");// 单号
		String startTime = request.getParameter("startTime");// 付款日期
		String endTime = request.getParameter("endTime");
		String custId = request.getParameter("custId");// 客户
		String currencyId = request.getParameter("currencyId");// 币种

		StringBuffer queryString = new StringBuffer();

		// 最高权限
		boolean all = SystemUtil.isAction(request, "cotrecv.do", "ALL");
		// 部门权限
		boolean dept = SystemUtil.isAction(request, "cotrecv.do", "DEPT");

		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.businessPerson=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				queryString.append(" where obj.businessPerson=e.id");
			} else {
				// 判断是否有查看该部门权限
				if (dept == true) {
					queryString
							.append(" where obj.businessPerson = e.id and e.deptId="
									+ emp.getDeptId());
				} else {
					queryString
							.append(" where obj.businessPerson = e.id and obj.businessPerson ="
									+ emp.getId());
				}
			}
		}

		if (financeNo != null && !financeNo.trim().equals("")) {
			queryString.append(" and obj.finaceNo like '%" + financeNo.trim()
					+ "%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.amountDate >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.amountDate <='" + endTime
					+ " 23:59:59'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId=" + custId.trim());
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and obj.currencyId=" + currencyId.trim());
		}
		
		//如果是预收货款不过滤,如果不是,判断已冲帐金额和未流转金额是不是都为0
		queryString.append(" and (obj.finaceName='预收货款' or (obj.realAmount>0 or obj.zhRemainAmount>0))");

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinaceAccountrecv obj,CotEmps e"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotFinaceAccountrecv obj,CotEmps e");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {

			String json = this.getCotRecvService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 查询应收帐冲帐明细
	public ActionForward queryRecvDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryRecv");
		String recvId = request.getParameter("recvId");// 应收款ID

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where detail.finaceRecvid=p.id");

		if (recvId != null && !recvId.trim().equals("")) {
			queryString.append(" and detail.recvId=" + recvId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinacerecv p,CotFinacerecvDetail detail"
						+ queryString);
		// 查询语句
		queryInfo
				.setSelectString("select p.finaceNo,detail.addTime,"
						+ "detail.currencyId,detail.currentAmount from CotFinacerecv p,CotFinacerecvDetail detail");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());

		// 设置排序语句
		queryInfo.setOrderString(" order by detail.id desc");

		int totalCount = this.getCotRecvService().getRecordCount(queryInfo);

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotRecvService().getList(queryInfo);

		List<?> listVo = this.getCotRecvService().getRecvDetailList(list);
		GridServerHandler gd = new GridServerHandler();
		gd.setData(listVo);
		gd.setTotalCount(totalCount);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询应收帐的流转记录
	public ActionForward queryLiuRecv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryRecv");
		String recvId = request.getParameter("recvId");// 应收款ID

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where detail.fkId=p.id and detail.source='orderRecv'");

		if (recvId != null && !recvId.trim().equals("")) {
			queryString.append(" and detail.outFlag=" + recvId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinaceOther detail,CotOrderOut p"
						+ queryString);
		// 查询语句
		queryInfo
				.setSelectString("select p.orderNo," +
						"detail.finaceName," +
						"detail.flag," +
						"detail.currencyId," +
						"detail.amount " +
						"from CotFinaceOther detail,CotOrderOut p");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by detail.id desc");
		int totalCount = this.getCotRecvService().getRecordCount(queryInfo);

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotRecvService().getList(queryInfo);
		List<?> listVo = this.getCotRecvService().getLiuDetailList(list);
		GridServerHandler gd = new GridServerHandler();
		gd.setData(listVo);
		gd.setTotalCount(totalCount);
		try {
			response.getWriter().write(gd.getLoadResponseText());
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

	public CotRecvService getCotRecvService() {
		if (recvService == null) {
			recvService = (CotRecvService) super.getBean("CotRecvService");
		}
		return recvService;
	}

}
