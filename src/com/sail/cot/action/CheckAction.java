/**
 * 
 */
package com.sail.cot.action;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.QueryService;
import com.sail.cot.service.given.CotGivenService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.orderfac.CotOrderFacService;
import com.sail.cot.service.orderout.CotOrderOutService;
import com.sail.cot.service.price.CotPriceService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

public class CheckAction extends AbstractAction {
	
	private CotPriceService priceService;
	
	public CotPriceService getPriceService() {
		if (priceService == null) {
			priceService = (CotPriceService) super.getBean("CotPriceService");
		}
		return priceService;
	}

	public void setPriceService(CotPriceService priceService) {
		this.priceService = priceService;
	}

	private QueryService queryService;

	public QueryService getQueryService() {
		if (queryService == null) {
			queryService = (QueryService) super.getBean("QueryService");
		}
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}
	
	private CotOrderService orderService;
	public CotOrderService getOrderService() {
		if (orderService == null) {
			orderService = (CotOrderService) super.getBean("CotOrderService");
		}
		return orderService;
	}

	public void setOrderService(CotOrderService orderService) {
		this.orderService = orderService;
	}
	
	private CotOrderOutService orderOutService;
	public CotOrderOutService getOrderOutService() {
		if (orderOutService == null) {
			orderOutService = (CotOrderOutService) super
					.getBean("CotOrderOutService");
		}
		return orderOutService;
	}

	public void setOrderOutService(CotOrderOutService orderOutService) {
		this.orderOutService = orderOutService;
	}
	
	private CotOrderFacService orderFacService;
	public CotOrderFacService getOrderFacService() {
		if (orderFacService == null) {
			orderFacService = (CotOrderFacService) super.getBean("CotOrderFacService");
		}
		return orderFacService;
	}

	public void setOrderFacService(CotOrderFacService orderFacService) {
		this.orderFacService = orderFacService;
	}
	
	private CotGivenService cotGivenService; 
	public CotGivenService getCotGivenService() {
		if (cotGivenService==null) {
			cotGivenService = (CotGivenService) super.getBean("CotGivenService");
		}
		return cotGivenService;
	}
	
	public void setCotGivenService(CotGivenService cotGivenService){
		this.cotGivenService = cotGivenService;
	}
	
	// 综合查询
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("querySuccess");
	}
	
	// 查询报价
	public ActionForward queryPriceDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryPrice");
	}
	
	// 查询订单
	public ActionForward queryOrderDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryOrder");
	}
	
	// 查询出货
	public ActionForward queryOrderOutdetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryOrderOut");
	}
	
	// 查询送样
	public ActionForward queryGivenDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryGiven");
	}
	
	// 查询国内采购
	public ActionForward queryOrderFacdetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryOrderFac");
	}
	
	//获得查询参数
	public StringBuffer getParams(HttpServletRequest request){
		String eleId = request.getParameter("eleId");// 样品编号
		String eleName = request.getParameter("eleName");// 中文名
		String eleNameEn = request.getParameter("eleNameEn");// 英文名
		String factoryId = request.getParameter("factoryId");// 厂家
		String eleCol = request.getParameter("eleCol");// 颜色
		String custNo = request.getParameter("custNo");//客号
		
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1");// 大类
		String eleTypeidLv2 = request.getParameter("eleTypeidLv2");// 大类
		String eleGrade = request.getParameter("eleGrade");// 等级
		String eleForPerson = request.getParameter("eleForPerson");// 开发对象
		
		String boxLS = request.getParameter("boxLS");// 产品起始长
		String boxLE = request.getParameter("boxLE");// 产品结束长
		String boxWS = request.getParameter("boxWS");// 产品起始宽
		String boxWE = request.getParameter("boxWE");// 产品结束宽
		String boxHS = request.getParameter("boxHS");// 产品起始高
		String boxHE = request.getParameter("boxHE");// 产品结束高

		StringBuffer queryString = new StringBuffer();
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and obj.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and obj.eleName like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and obj.eleNameEn like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and obj.eleCol like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (custNo != null && !custNo.toString().trim().equals("")) {
			queryString.append(" and obj.custNo like '%"
					+ custNo.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and obj.eleTypeidLv1="
					+ eleTypeidLv1.toString());
		}
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and obj.eleTypeidLv2="
					+ eleTypeidLv2.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and obj.eleGrade like '%"
					+ eleGrade.toString().trim() + "%'");
		}
		
		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and obj.eleForPerson like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		
		
		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
				queryString.append(" and obj.boxL between "
						+ boxLS + " and " + boxLE);
			}
		}
		
		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
				queryString.append(" and obj.boxW between "
						+ boxWS + " and " + boxWE);
			}
		}
		
		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
				queryString.append(" and obj.boxH between "
						+ boxHS + " and " + boxHE);
			}
		}
		return queryString;
	}
	
	// 查询该报价单的报价明细产品的信息
	public ActionForward queryPrice(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("queryPrice");
		
		String custId = request.getParameter("custId");// 客户ID
		String priceNoFind = request.getParameter("opNo");// 报价单号
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 报价起始日期
		String endTime = request.getParameter("endTime");// 报价结束日期
		String status = request.getParameter("status");// 审核状态

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.businessPerson = e.id and obj.custId=c.id");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotprice.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotprice.do",
						"DEPT");
				if (dept == true) {
					queryString.append(" and e.deptId=" + emp.getDeptId());
				} else {
					queryString.append(" and obj.businessPerson ="
							+ emp.getId());
				}
			}
		}
		if (status != null && !status.trim().equals("")) {
			queryString.append(" and obj.priceStatus =" + status.trim());
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId =" + custId.trim());
		}
		if (priceNoFind != null && !priceNoFind.trim().equals("")) {
			queryString.append(" and obj.priceNo like '%" + priceNoFind.trim()
					+ "%'");
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.businessPerson="
					+ businessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.priceTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.priceTime <='" + endTime
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
		queryInfo.setCountQuery("select count(*) from CotPrice as obj,"
				+ "CotEmps AS e,CotCustomer c" + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," + 
				"obj.priceTime," + 
				"c.customerShortName," + 
				"obj.priceNo,"+ 
				"e.empsName,"+ 
				"obj.clauseId," + 
				"obj.currencyId,"+ 
				"obj.situationId,"+ 
				"obj.validMonths," + 
				"obj.priceStatus, " + 
				"obj.priceRate," +
				"obj.priceCompose " +
				" from CotPrice AS obj,"
				+ "CotEmps AS e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		// 得到limit对象
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		List res = this.getPriceService().getPriceVOList(queryInfo);
		int count = this.getPriceService().getRecordCount(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 查询订单
	public ActionForward queryOrder(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("queryOrder");
		
		String orderNoFind = request.getParameter("opNo");// 订单单号
		String custId = request.getParameter("custId");// 客户
		String bussinessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		String status = request.getParameter("status");// 审核状态
		
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.bussinessPerson = e.id and obj.custId=c.id");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorder.do",
						"DEPT");
				if (dept == true) {
					queryString.append(" and e.deptId=" + emp.getDeptId());
				} else {
					queryString.append(" and obj.bussinessPerson ="
							+ emp.getId());
				}
			}
		}
		
		if (status != null && !status.trim().equals("")) {
			queryString.append(" and obj.orderStatus =" + status.trim());
		}
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim()
					+ "%'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId=" + custId.trim());
		}
		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
			queryString.append(" and obj.bussinessPerson="
					+ bussinessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderTime <='" + endTime
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
		queryInfo.setCountQuery("select count(*) from CotOrder as obj,"
				+ "CotEmps AS e,CotCustomer c" + queryString);
		// 查询语句
		queryInfo
				.setSelectString("select obj.id," +
						"obj.orderTime," +
						"obj.sendTime," +
						"c.customerShortName," +
						"obj.orderNo," +
						"e.empsName, " +
						"obj.clauseTypeId," +
						"obj.currencyId," +
						"obj.payTypeId," +
						"obj.totalCount," +
						"obj.totalContainer," +
						"obj.totalCBM," +
						"obj.totalMoney," +
						"obj.orderStatus," + "obj.poNo, "+
						"obj.orderRate," + "obj.orderCompose,"
						+ "obj.totalGross ,c.id as cId " + 
						"from CotOrder AS obj,"
						+ "CotEmps AS e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		// 得到limit对象
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		List res = this.getOrderService().getOrderVOList(queryInfo);
		int count = this.getPriceService().getRecordCount(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询出货
	public ActionForward queryOrderOut(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryOrderOut");
		String orderNoFind = request.getParameter("opNo");// 单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		String status = request.getParameter("status");// 审核状态
		
		StringBuffer queryString = new StringBuffer();
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.businessPerson = e.id");
		} else {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
			if (all == true) {
				queryString.append(" where obj.businessPerson = e.id");
			} else {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorder.do",
						"DEPT");
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
		
		if (status != null && !status.trim().equals("")) {
			queryString.append(" and obj.orderStatus =" + status.trim());
		}
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim()
					+ "%'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId=" + custId.trim());
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.businessPerson="
					+ businessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderTime <='" + endTime
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
		queryInfo.setCountQuery("select count(*) from CotOrderOut as obj,"
				+ "CotEmps AS e" + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj from CotOrderOut AS obj,"
				+ "CotEmps AS e");

		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		// 得到limit对象
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);

		String excludes[] = {"orderMBImg","cotOrderOutdetails","cotOrderOuthsdetails",
		"cotSplitInfos"	,"cotHsInfos","cotShipments","cotSymbols","cotOrderouthsRpt"};
		queryInfo.setExcludes(excludes);
		// 根据起始
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询征样
	public ActionForward querySign(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// 设置查询参数
//		StringBuffer queryString = new StringBuffer();
//		String opNo = request.getParameter("opNo");// 单号
//		String custId = request.getParameter("custId");// 客户
//		String businessPerson = request.getParameter("businessPerson");// 业务员
//		String startTime = request.getParameter("startTime");// 起始时间
//		String endTime = request.getParameter("endTime");// 结束时间
//		queryString.append(" where obj.signId=p.id and p.bussinessPerson = e.id and p.custId=c.id");
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if (!"admin".equals(emp.getEmpsId())) {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotsign.do", "ALL");
//			if (all == false) {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotsign.do",
//						"DEPT");
//				if (dept == true) {
//					queryString.append(" and e.deptId=" + emp.getDeptId());
//				} else {
//					queryString.append(" and p.bussinessPerson ="
//							+ emp.getId());
//				}
//			}
//		}
//		if (opNo != null && !opNo.toString().trim().equals("")) {
//			queryString.append(" and p.signNo like '%"
//					+ opNo.toString().trim() + "%'");
//		}
//		if (custId != null && !custId.toString().equals("")) {
//			queryString.append(" and p.custId=" + custId.toString());
//		}
//		if (businessPerson != null && !businessPerson.toString().equals("")) {
//			queryString.append(" and p.bussinessPerson=" + businessPerson.toString());
//		}
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString
//					.append(" and p.signTime >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
//			queryString.append(" and p.signTime <='" + endTime
//					+ " 23:59:59'");
//		}
//		queryString.append(getParams(request));
//		
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "conTable");
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("conTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotSignDetail obj,CotSign p,CotEmps e,CotCustomer c"
//				+ queryString);
//		// 设置查询记录语句
//		queryInfo.setSelectString("select p.id," +
//				"obj.eleId," +
//				"obj.eleName," +
//				"p.signNo," +
//				"p.signTime," +
//				"p.factoryId," +
//				"c.customerShortName," +
//				"e.empsName " +
//				"from CotSignDetail obj,CotSign p,CotEmps e,CotCustomer c");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.eleId asc");
//		// 取得排序信息
//		Sort sort = limit.getSort();
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			StringBuffer rs = new StringBuffer();
//			for (Iterator<?> itor = sortValueMap.keySet().iterator(); itor
//					.hasNext();) {
//				Object field = (String) itor.next();
//				String ord = (String) sortValueMap.get(field);
//				if (field.equals("empsName")) {
//					rs.append(" ORDER BY ").append("e").append(".").append(
//							field).append(" ").append(ord);
//				} else if (field.equals("signNo") || field.equals("signTime")|| field.equals("factoryId")) {
//					rs.append(" ORDER BY ").append("p").append(".").append(
//							field).append(" ").append(ord);
//				}else if (field.equals("customerShortName")) {
//					rs.append(" ORDER BY ").append("c").append(".").append(
//							field).append(" ").append(ord);
//				} else {
//					rs.append(" ORDER BY ").append("obj").append(".").append(
//							field).append(" ").append(ord);
//				}
//				break;
//			}
//			// 设置排序语句
//			queryInfo.setOrderString(rs.toString());
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		List<?> list = this.getQueryService().getSignVO(queryInfo);
//		request.setAttribute("conList", list);
//		request.setAttribute("allFacName", this.getQueryService().getFactoryNameMap(request));
		return mapping.findForward("querySign");
	}
	
	// 查询送样
	public ActionForward queryGiven(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("queryGiven");
		
		String givenNo=request.getParameter("opNo");//单号
		String custId = request.getParameter("custId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String bussinessPerson = request.getParameter("businessPerson");
		String status = request.getParameter("status");// 审核状态
		StringBuffer queryString=new StringBuffer();
		
		//最高权限
		boolean all = SystemUtil.isAction(request, "cotgiven.do", "ALL");
		//部门权限
		boolean dept = SystemUtil.isAction(request, "cotgiven.do","DEPT");		
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.bussinessPerson=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				queryString.append(" where obj.bussinessPerson=e.id");
			} else {
				// 判断是否有查看该部门征样的权限
				if (dept == true) {
					queryString .append(" where obj.bussinessPerson = e.id and e.deptId=" + emp.getDeptId());
				} else {
					queryString .append(" where obj.bussinessPerson = e.id and obj.bussinessPerson =" + emp.getId());
				}
			}
		}
		
		if (status != null && !status.trim().equals("")) {
			queryString.append(" and obj.givenIscheck =" + status.trim());
		}
		
		if(givenNo != null && !givenNo.trim().equals(""))
		{
			givenNo = givenNo.trim();
			queryString.append(" and obj.givenNo like '%"+givenNo+"%'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId ="+custId.trim());
		}
		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
			queryString.append(" and obj.bussinessPerson="+bussinessPerson.trim());
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.givenTime >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.givenTime <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.givenTime between '"+startTime+"' and '"+endTime+"'");
		}
		
		//设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		//设置每页显示多少行
		int pageCount =15;
		//取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotGiven obj,CotEmps e"+ queryString);
		// 查询语句
		queryInfo.setSelectString("select obj from CotGiven obj,CotEmps e");
		
		 
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		//得到limit对象
		int startIndex = Integer.parseInt(start);	
		String excludes[] = {"cotGivenDetails","cotSign"};
		queryInfo.setStartIndex(startIndex);
		queryInfo.setExcludes(excludes);
		//根据起始
		
		try {
			String json = this.getCotGivenService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	// 查询国内采购
	public ActionForward queryOrderFac(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryOrderFac");
		
		String orderNoFind = request.getParameter("opNo"); //发票编号
		String businessPerson = request.getParameter("businessPerson"); //业务员
		String startTime = request.getParameter("startTime"); //下单起始日期
		String endTime = request.getParameter("endTime"); //下单结束日期
		String status = request.getParameter("status");// 审核状态
		
		StringBuffer queryString = new StringBuffer();
		 
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.businessPerson=e.id");
		} else {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderfac.do", "ALL");
			if (all == true) {
				queryString.append(" where obj.businessPerson=e.id");
			} else {
				// 判断是否有查看该部门征样的权限
				boolean dept = SystemUtil.isAction(request, "cotorderfac.do","DEPT");
				if (dept == true) {
					queryString.append(" where obj.businessPerson = e.id and e.deptId="+ emp.getDeptId());
				} else {
					queryString.append(" where obj.businessPerson = e.id and obj.businessPerson ="+ emp.getId());
				}
			}
		}
		
		if (status != null && !status.trim().equals("")) {
			queryString.append(" and obj.orderStatus =" + status.trim());
		}
		 
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim() + "%'");
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.businessPerson=" + businessPerson.trim());
		}
		if (startTime!=null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.addTime >='" + startTime + "'");
		}
		if (endTime!=null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.addTime <='" + endTime + " 23:59:59'");
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
		queryInfo.setCountQuery("select count(*) from CotOrderFac obj,CotEmps e" + queryString);
		 
		// 查询语句
		queryInfo.setSelectString("select obj from CotOrderFac obj,CotEmps e");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		// 得到limit对象
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		String excludes[] = {"cotOrderFacdetails","orderMBImg"};
		queryInfo.setExcludes(excludes);
		// 根据起始
		try {
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
