/**
 * 
 */
package com.sail.cot.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.QueryService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

public class QueryAllAction extends AbstractAction {

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

	// 综合查询
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("querySuccess");
	}

	// 查询报价
	public ActionForward queryPriceDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("queryPrice");
	}

	// 查询订单
	public ActionForward queryOrderDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("queryOrder");
	}

	// 查询出货
	public ActionForward queryOrderOutdetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("queryOrderOut");
	}

	// 查询征样
	public ActionForward querySignDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("querySign");
	}

	// 查询送样
	public ActionForward queryGivenDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("queryGiven");
	}

	// 查询国内采购
	public ActionForward queryOrderFacdetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("queryOrderFac");
	}

	// 查询样品档案
	public ActionForward querySampledetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("querySample");
	}
	
	// 查询厂家报价
	public ActionForward queryFacRecdetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("queryPriceFac");
	}

	// 获得查询参数
	public StringBuffer getParams(HttpServletRequest request,boolean flag) {
		String eleId = request.getParameter("eleId");// 样品编号
		String eleName = request.getParameter("eleName");// 中文名
		String eleNameEn = request.getParameter("eleNameEn");// 英文名
		String factoryId = request.getParameter("factoryId");// 厂家
		String eleCol = request.getParameter("eleCol");// 颜色
		String custNo = request.getParameter("custNo");// 客号

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
		
		String barcode =request.getParameter("barcode");//电脑标
		String eleSizeDesc =request.getParameter("eleSizeDesc");//中文规格

		StringBuffer queryString = new StringBuffer();
		if(barcode !=null && !barcode.toString().trim().equals("")){
			queryString.append(" and obj.barcode like '%"
					+barcode.toString().trim() + "%'");
		}
		
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
		if (factoryId != null && !factoryId.toString().equals("") && flag==false) {
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
		if (eleSizeDesc != null && !eleSizeDesc.toString().equals("")) {
			queryString.append(" and obj.eleSizeDesc like '%"
					+ eleSizeDesc.toString().trim() + "%'");
		}

		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and obj.eleForPerson like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
				queryString.append(" and obj.boxL between " + boxLS + " and "
						+ boxLE);
			}
		}

		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
				queryString.append(" and obj.boxW between " + boxWS + " and "
						+ boxWE);
			}
		}

		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
				queryString.append(" and obj.boxH between " + boxHS + " and "
						+ boxHE);
			}
		}
		return queryString;
	}

	// 查询该报价单的报价明细产品的信息
	public ActionForward queryPrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 设置查询参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryPrice");

		StringBuffer queryString = new StringBuffer();
		String opNo = request.getParameter("opNo");// 单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		queryString
				.append(" where obj.priceId=p.id and p.businessPerson = e.id and p.custId=c.id ");
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
					queryString.append(" and p.businessPerson =" + emp.getId());
				}
			}
		}
		if (opNo != null && !opNo.toString().trim().equals("")) {
			queryString.append(" and p.priceNo like '%"
					+ opNo.toString().trim() + "%'");
		}
		if (custId != null && !custId.toString().equals("")) {
			queryString.append(" and p.custId=" + custId.toString());
		}
		if (businessPerson != null && !businessPerson.toString().equals("")) {
			queryString.append(" and p.businessPerson="
					+ businessPerson.toString());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.priceTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.priceTime <='" + endTime + " 23:59:59'");
		}
		queryString.append(getParams(request,false));

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotPriceDetail obj,CotPrice p,CotEmps e,CotCustomer c"
						+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select p.id," + "obj.eleId,"
				+ "obj.eleName," + "obj.pricePrice," + "p.priceNo,"
				+ "p.priceTime," + "c.customerShortName," + "e.empsName,"
				+ "p.currencyId, " + "obj.id, " + "obj.priceFac, "
				+ "obj.priceFacUint, "
				+ "p.contactId "
				+ "from CotPriceDetail obj,CotPrice p,CotEmps e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.priceTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getPriceVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询订单
	public ActionForward queryOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrder");

		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		String opNo = request.getParameter("opNo");// 单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		queryString
				.append(" where obj.orderId=p.id and p.bussinessPerson = e.id and p.custId=c.id");
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
					queryString
							.append(" and p.bussinessPerson =" + emp.getId());
				}
			}
		}
		if (opNo != null && !opNo.toString().trim().equals("")) {
			queryString.append(" and p.orderNo like '%"
					+ opNo.toString().trim() + "%'");
		}
		if (custId != null && !custId.toString().equals("")) {
			queryString.append(" and p.custId=" + custId.toString());
		}
		if (businessPerson != null && !businessPerson.toString().equals("")) {
			queryString.append(" and p.bussinessPerson="
					+ businessPerson.toString());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.orderTime <='" + endTime + " 23:59:59'");
		}
		queryString.append(getParams(request,false));

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderDetail obj,CotOrder p,CotEmps e,CotCustomer c"
						+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select p.id," + "obj.eleId,"
				+ "obj.eleName," + "obj.orderPrice," + "p.orderNo,"
				+ "p.orderTime," + "c.customerShortName," + "e.empsName,"
				+ "p.currencyId, " + "obj.id, " + "p.poNo, " + "obj.priceFac, "
				+ "obj.priceFacUint,obj.boxCount,obj.containerCount,obj.unBoxCount,obj.boxObCount,p.contactId "
				+ "from CotOrderDetail obj,CotOrder p,CotEmps e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.orderTime desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		int count = this.getQueryService().getRecordCount(queryInfo);

		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getOrderVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询出货
	public ActionForward queryOrderOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("queryOrderOut");
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		String opNo = request.getParameter("opNo");// 单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		queryString
				.append(" where obj.orderId=p.id and p.businessPerson = e.id and p.custId=c.id");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderout.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorderout.do",
						"DEPT");
				if (dept == true) {
					queryString.append(" and e.deptId=" + emp.getDeptId());
				} else {
					queryString.append(" and p.businessPerson =" + emp.getId());
				}
			}
		}
		if (opNo != null && !opNo.toString().trim().equals("")) {
			queryString.append(" and p.orderNo like '%"
					+ opNo.toString().trim() + "%'");
		}
		if (custId != null && !custId.toString().equals("")) {
			queryString.append(" and p.custId=" + custId.toString());
		}
		if (businessPerson != null && !businessPerson.toString().equals("")) {
			queryString.append(" and p.businessPerson="
					+ businessPerson.toString());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.orderTime <='" + endTime + " 23:59:59'");
		}
		queryString.append(getParams(request,false));

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderOutdetail obj,CotOrderOut p,CotEmps e,CotCustomer c"
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select p.id,"
						+ "obj.eleId,"
						+ "obj.eleName,"
						+ "obj.orderPrice,"
						+ "p.orderNo,"
						+ "p.orderTime,"
						+ "c.customerShortName,"
						+ "e.empsName,"
						+ "p.currencyId, "
						+ "obj.id, "
						+ "obj.boxCbm "
						+ "from CotOrderOutdetail obj,CotOrderOut p,CotEmps e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.orderTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getOrderOutVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询征样
	public ActionForward querySign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySign");
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		String opNo = request.getParameter("opNo");// 单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		
		queryString
				.append(" where obj.givenId=p.id and s.factoryId=obj.factoryId and obj.signFlag=1 and s.givenId=p.id and s.bussinessPerson = e.id and s.custId=c.id");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotsign.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotsign.do",
						"DEPT");
				if (dept == true) {
					queryString.append(" and e.deptId=" + emp.getDeptId());
				} else {
					queryString
							.append(" and s.bussinessPerson =" + emp.getId());
				}
			}
		}
		if (opNo != null && !opNo.toString().trim().equals("")) {
			queryString.append(" and s.signNo like '%"
					+ opNo.toString().trim() + "%'");
		}
		if (custId != null && !custId.toString().equals("")) {
			queryString.append(" and s.custId=" + custId.toString());
		}
		if (businessPerson != null && !businessPerson.toString().equals("")) {
			queryString.append(" and s.bussinessPerson="
					+ businessPerson.toString());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and s.signTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and s.signTime <='" + endTime + " 23:59:59'");
		}
		queryString.append(getParams(request,false));

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotGivenDetail obj,CotGiven p,CotSign s,CotEmps e,CotCustomer c"
						+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select obj.id," + "obj.eleId,"
				+ "obj.eleName," + "s.signNo," + "s.signTime,"
				+ "c.customerShortName," + "e.empsName, "
				+ "s.id, " + "obj.priceOut, " + "obj.priceOutUint "
				+ "from CotGivenDetail obj,CotGiven p,CotSign s,CotEmps e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by s.signTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getSignVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询送样
	public ActionForward queryGiven(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryGiven");
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		String opNo = request.getParameter("opNo");// 单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		queryString
				.append(" where obj.givenId=p.id and p.bussinessPerson = e.id and p.custId=c.id");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotgiven.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotgiven.do",
						"DEPT");
				if (dept == true) {
					queryString.append(" and e.deptId=" + emp.getDeptId());
				} else {
					queryString
							.append(" and p.bussinessPerson =" + emp.getId());
				}
			}
		}
		if (opNo != null && !opNo.toString().trim().equals("")) {
			queryString.append(" and p.givenNo like '%"
					+ opNo.toString().trim() + "%'");
		}
		if (custId != null && !custId.toString().equals("")) {
			queryString.append(" and p.custId=" + custId.toString());
		}
		if (businessPerson != null && !businessPerson.toString().equals("")) {
			queryString.append(" and p.bussinessPerson="
					+ businessPerson.toString());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.givenTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.givenTime <='" + endTime + " 23:59:59'");
		}
		queryString.append(getParams(request,false));

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotGivenDetail obj,CotGiven p,CotEmps e,CotCustomer c"
						+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select p.id," + "obj.eleId,"
				+ "obj.eleName," + "p.givenNo," + "p.givenTime,"
				+ "p.givenAddr," + "c.customerShortName," + "e.empsName, "
				+ "obj.id, " + "obj.priceOut, " + "obj.priceOutUint "
				+ "from CotGivenDetail obj,CotGiven p,CotEmps e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.givenTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getGivenVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询国内采购
	public ActionForward queryOrderFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrderFac");
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		String opNo = request.getParameter("opNo");// 单号
		// String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		queryString
				.append(" where obj.orderId=p.id and p.businessPerson = e.id and p.factoryId=c.id");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderfac.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorderfac.do",
						"DEPT");
				if (dept == true) {
					queryString.append(" and e.deptId=" + emp.getDeptId());
				} else {
					queryString.append(" and p.businessPerson =" + emp.getId());
				}
			}
		}
		if (opNo != null && !opNo.toString().trim().equals("")) {
			queryString.append(" and p.orderNo like '%"
					+ opNo.toString().trim() + "%'");
		}
		// if (custId != null && !custId.toString().equals("")) {
		// queryString.append(" and p.custId=" + custId.toString());
		// }
		if (businessPerson != null && !businessPerson.toString().equals("")) {
			queryString.append(" and p.businessPerson="
					+ businessPerson.toString());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.orderTime <='" + endTime + " 23:59:59'");
		}
		queryString.append(getParams(request,false));

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderFacdetail obj,CotOrderFac p,CotEmps e,CotFactory c"
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select p.id,"
						+ "obj.eleId,"
						+ "obj.eleName,"
						+ "obj.priceFac,"
						+ "p.orderNo,"
						+ "p.orderTime,"
						+ "c.shortName,"
						+ "e.empsName,"
						+ "obj.priceFacUint, "
						+ "obj.id "
						+ "from CotOrderFacdetail obj,CotOrderFac p,CotEmps e,CotFactory c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.orderTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		// 取得排序信息
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getOrderFacVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询样品档案
	public ActionForward querySample(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySample");
		// 设置查询参数
		String eleId = request.getParameter("eleId");// 样品编号
		String custNo = request.getParameter("custNo");// 单号
		String eleName = request.getParameter("eleName");// 中文名
		String eleNameEn = request.getParameter("eleNameEn");// 英文名
		String factoryId = request.getParameter("factoryId");// 厂家
		String eleCol = request.getParameter("eleCol");// 颜色
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
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

		String custId = request.getParameter("custId");
		String barcode =request.getParameter("barcode");//电脑标
		String eleSizeDesc =request.getParameter("eleSizeDesc");//中文规格
		
		StringBuffer queryString = new StringBuffer();
		// if (custNo == null || custNo.trim().equals("")) {
		// queryString.append(" where 1=1");
		// }else {
		queryString.append(" where obj.ELE_ID = c.ELE_ID");
		// }
		if(barcode !=null && !barcode.toString().trim().equals("")){
			queryString.append(" and obj.barcode like '%"
					+barcode.toString().trim() + "%'");
		}
		
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and obj.ELE_ID like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and obj.ELE_NAME like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and obj.ELE_NAME_EN like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and obj.FACTORY_ID=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and obj.ELE_COL like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and obj.ELE_TYPEID_LV1="
					+ eleTypeidLv1.toString());
		}
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and obj.ELE_TYPEID_LV2="
					+ eleTypeidLv2.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and obj.ELE_GRADE like '%"
					+ eleGrade.toString().trim() + "%'");
		}

		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and obj.ELE_FORPERSON like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.ELE_PRO_TIME >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.ELE_PRO_TIME <='" + endTime
					+ " 23:59:59'");
		}

		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
				queryString.append(" and obj.BOX_L between " + boxLS + " and "
						+ boxLE);
			}
		}

		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
				queryString.append(" and obj.BOX_W between " + boxWS + " and "
						+ boxWE);
			}
		}

		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
				queryString.append(" and obj.BOX_H between " + boxHS + " and "
						+ boxHE);
			}
		}

		if (custNo != null && !custNo.toString().equals("")) {
			queryString.append(" and c.CUST_NO like '%"
					+ custNo.toString().trim() + "%'");
		}
		if (custId != null && !custId.toString().equals("")) {
			queryString.append(" and c.cust_id =" + custId.trim());
		}
		
		if (eleSizeDesc != null && !eleSizeDesc.toString().equals("")) {
			queryString.append(" and obj.ELE_SIZE_DESC like '%"
					+ eleSizeDesc.toString().trim() + "%'");
		}

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from cot_elements_new obj,cot_ele_cust c"
						+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("SELECT " + "obj.ID as id,"
				+ "obj.ELE_ID as eleId," + "obj.ELE_NAME as eleName,"
				+ "obj.ELE_NAME_EN as eleNameEn,"
				+ "obj.FACTORY_ID as factoryId," + "obj.PRICE_FAC as priceFac,"
				+ "obj.PRICE_OUT as priceOut,"
				+ "obj.PRICE_FAC_UINT as priceFacUint,"
				+ "c.CUST_NO as custNo," + "c.CUST_ID as custId,"
				+ "c.ID as cid," + "obj.price_out_unit as priceOutUint"
				+ " FROM cot_ele_cust c,cot_elements_new obj ");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.ELE_ID asc");
		
		queryInfo.setQueryObjType("CotSampleVO");
		int count = this.getQueryService().getRecordCountJDBC(queryInfo);
		
		// 取得排序信息
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getSampleVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询某条订单明细出到了哪几张出货单中
	public ActionForward queryOrderOutByEleId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=p.id");
		
		String oId = request.getParameter("oId");// 客户
		if (oId != null && !oId.toString().equals("")) {
			queryString.append(" and obj.orderDetailId=" + oId.toString());
		}

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderOutdetail obj,CotOrderOut p"
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj.id,"
						+ "p.orderNo,"
						+ "obj.eleId,"
						+ "obj.eleName,"
						+ "obj.boxCount, "
						+ "obj.containerCount, "
						+ "obj.unSendNum "
						+ "from CotOrderOutdetail obj,CotOrderOut p");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.orderTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getOrderDetailVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
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
	
	//查询厂家报价
	public ActionForward queryPriceFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("queryPriceFac");
		
		String facId = request.getParameter("factoryId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		StringBuffer queryString=new StringBuffer();
		queryString.append(" where p.eleId=obj.id and p.facId=f.id");
		
		if (facId != null && !facId.toString().equals("")) {
			queryString.append(" and p.facId="+ facId.toString());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.addTime <='" + endTime
					+ " 23:59:59'");
		}
		//追加样品档案查询参数
		queryString.append(getParams(request,true));
		
		//设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		//设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPriceFac p,CotElementsNew obj,CotFactory f"+ queryString);
		// 查询语句
		queryInfo.setSelectString("select p.id," +
				"obj.eleId," +
				"obj.eleName," +
				"p.addTime," +
				"p.priceFac," +
				"p.priceUint," +
				"f.shortName," +
				"p.priceRemark," +
				"obj.id," +
				"obj.eleSizeDesc" +
				" from CotPriceFac p,CotElementsNew obj,CotFactory f");
		 
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by p.addTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		//得到limit对象
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getPriceFacVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	// 查询国内采购
	public ActionForward queryOrderStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String factoryId=request.getParameter("factoryId");
		String companyId=request.getParameter("companyId");
		String sortStr = request.getParameter("sort");//排序
		String dirStr = request.getParameter("dir");
		String typeLv1Id =request.getParameter("typeLv1IdFind");
		String themeStr = request.getParameter("themeStr");//Theme Name
		String allPinName = request.getParameter("allPinName");//allPinName
		String currencyId = request.getParameter("currencyId");//currencyId
		String clauseTypeId = request.getParameter("clauseTypeId");//clauseTypeId
		if (start == null || limit == null)
			return mapping.findForward("queryOrderStatus");
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String orderNo = request.getParameter("orderNoFind");// 订单编号
		String orderFacNo = request.getParameter("orderFacNoFind");// 生产合同号
		String custId = request.getParameter("custId");// 客户
		String nationId = request.getParameter("nationId");// 国
		String empId = request.getParameter("empId");// 业务员
		String isReady = request.getParameter("canOut");
		System.out.println("isReady:"+isReady);
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderstatus.do", "ALL");
//			if (all == false) {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotorderfac.do",
//						"DEPT");
//				if (dept == true) {
//					//queryString.append(" and obj.deptId=" + emp.getDeptId());
//				} else {
//					//queryString.append(" and obj.empId =" + emp.getId());
//				}
//			}
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotorderstatus.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nationId in(").append(nationStr).append(")");
				}
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorderstatus.do", "DEPT");
				if (dept == true) {
					String deptStr = json.getString("dept");
					queryString.append(" and obj.typeLv1Id in(").append(deptStr).append(")");
				} else {
					//queryString.append(" and obj.BUSINESS_PERSON =" + emp.getId());
				}
			}
		}
		if (factoryId != null && !"".equals(factoryId.trim())) {
			queryString.append(" and obj.facId =" +Integer.parseInt(factoryId));
		}
		if (currencyId != null && !"".equals(currencyId.trim())) {
			queryString.append(" and obj.currencyId =" +Integer.parseInt(currencyId));
		}
		if (companyId != null && !"".equals(companyId.trim())) {
			queryString.append(" and obj.companyId =" +Integer.parseInt(companyId));
		}
		if (orderNo != null && !"".equals(orderNo.trim())) {
			queryString.append(" and obj.orderNo like'%" + orderNo + "%'");
		}
		if (orderFacNo != null && !"".equals(orderFacNo.trim())) {
			queryString.append(" and obj.orderFacNo like'%" + orderFacNo + "%'");
		}
		if (custId != null && !"".equals(custId.trim())) {
			queryString.append(" and obj.custId =" + Integer.parseInt(custId.trim()));
		}
		if (nationId != null && !"".equals(nationId.trim())) {
			queryString.append(" and obj.nationId =" + Integer.parseInt(nationId.trim()));
		}
		if (empId != null && !"".equals(empId.trim())) {
			queryString.append(" and obj.empId =" + Integer.parseInt(empId.trim()));
		}
		if (isReady != null && !"".equals(isReady.trim())) {
			if("0".equals(isReady))
				queryString.append(" and (obj.canOut = 0 or obj.canOut = 3) ");
			else
				queryString.append(" and obj.canOut ='" + isReady + "'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderLcDate >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderLcDate <='" + endTime + " 23:59:59'");
		}
		//department
		if (typeLv1Id!= null && !"".equals(typeLv1Id.trim())) {
			//String hql = "select id from CotTypeLv1 obj where obj.typeEnName like '"+typeLv1Id+"%'";
			List list = this.getQueryService().getList("CotTypeLv1");
			if(CollectionUtils.isEmpty(list)){
				queryString.append(" and obj.typeLv1Id = '0'");
			}else {
				List idsList = new ArrayList();
				for(int i = 0;i<list.size();i++){
					CotTypeLv1 lv1 = (CotTypeLv1)list.get(i);
					if(lv1.getTypeEnName().toLowerCase().indexOf(typeLv1Id.toLowerCase()) > -1){
						idsList.add(lv1.getId());
					}
				}
				String ids  = "0";
				if(idsList.size() > 0){
					ids = StringUtils.join(idsList.toArray(),",");
				}
				queryString.append(" and obj.typeLv1Id in("+ids+")");
			}
//				queryString.append(" and obj.typeLv1Id =" +Integer.parseInt(typeLv1Id));
		}
		//themeStr
		if (themeStr!= null && !"".equals(themeStr.trim())) {
			queryString.append(" and obj.themeStr like'%" + themeStr + "%'");
		}
		//clauseTypeId
		if (clauseTypeId!= null && !"".equals(clauseTypeId.trim())) {
			queryString.append(" and obj.clauseTypeId = "+ clauseTypeId );
		}
		//allPinName
		if (allPinName!= null && !"".equals(allPinName.trim())) {
			String hql = "select distinct orderId from CotOrderDetail obj where obj.eleNameEn like '%"+allPinName+"%'";
			
			List list = this.getQueryService().getListBy(hql);
			if(CollectionUtils.isEmpty(list)){
				queryString.append(" and obj.allPinName = '0'");
			}else {
				String ids = StringUtils.join(list.toArray(),",");
				queryString.append(" and obj.orderId in("+ids+")");
			}
		}
		//queryString.append(getParams(request,false));

		if(sortStr==null)
			sortStr = "orderNo";
		if(dirStr==null)
			dirStr = "ASC";

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 200;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from VOrderOrderfacStatusId obj where 1=1 "
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from VOrderOrderfacStatusId obj where 1=1 ");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj."+sortStr+" "+dirStr); 
		//queryInfo.setOrderString(" order by obj.orderNo desc");
		//queryInfo.setOrderString(" order by obj.orderTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		// 取得排序信息
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		GridServerHandler gd = new GridServerHandler();
		try {
			String json = this.getQueryService().getHomeData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// 查询国内采购
	public ActionForward queryOrderStatusDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryDetailStatus");
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		Map valueMap = new HashMap();
		valueMap.put("SAMPLE", " obj.simpleSampleDeadline");
		valueMap.put("PACKAGE", " obj.facDeadline");
		valueMap.put("SAMPLEOUT", " obj.samplePicDeadline");
		valueMap.put("QC", " obj.qcDeadline");
		valueMap.put("OUT", " obj.shippingDeadline");
		
//		String planStartTime = request.getParameter("planStartTime");// 起始时间
//		String planEndTime = request.getParameter("planEndTime");// 结束时间
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String orderNo = request.getParameter("orderNoFind");// 订单编号
		String orderFacNo = request.getParameter("orderFacNoFind");// 生产合同号
		String type = request.getParameter("type");
		//deadline的查询条件
		//1：today 当前时间以前，忽略endtime
		//2：This week start date 为这周一，end date为周天
		//3：Next week star date为下周一，end date 为下周天
		String queryType = request.getParameter("queryType");//deadline的查询条件
		
		String canOut = request.getParameter("canOut");// 状态
//		String statusdetail = request.getParameter("statusdetail");// 明细状态
		String sortField = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String typeLv1Id =request.getParameter("typeLv1IdFind");
		String factoryId =request.getParameter("factoryId");
		String nationId =request.getParameter("nationId");
		String clauseTypeId =request.getParameter("clauseTypeIdFind");
		String zheType =request.getParameter("zheType");
		String flag =request.getParameter("flag");
		String statusCol = "";
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderstatus.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotorderstatus.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nationId in(").append(nationStr).append(")");
				}
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorderstatus.do", "DEPT");
				if (dept == true) {
					String deptStr = json.getString("dept");
					queryString.append(" and obj.typeLv1Id in(").append(deptStr).append(")");
				} else {
					//queryString.append(" and obj.BUSINESS_PERSON =" + emp.getId());
				}
			}
		}
		if (factoryId != null && !"".equals(factoryId.trim())) {
			queryString.append(" and obj.factoryId =" +Integer.parseInt(factoryId));
		}
		if (nationId != null && !"".equals(nationId.trim())) {
			queryString.append(" and obj.nationId =" +Integer.parseInt(nationId));
		}
		if (orderNo != null && !"".equals(orderNo.trim())) {
			queryString.append(" and obj.orderNo like'%" + orderNo + "%'");
		}
		if (orderFacNo != null && !"".equals(orderFacNo.trim())) {
			queryString.append(" and obj.orderfacNo like'%" + orderFacNo + "%'");
		}
		if(StringUtils.isEmpty(zheType) || "0".equals(zheType))
			queryString.append(" and (obj.zheType = 0 or zheType is null)");
		else {
			queryString.append(" and obj.zheType="+zheType);
		}
		if(StringUtils.isEmpty(canOut) || "0".equals(canOut))
			queryString.append(" and (obj.canOut = 0 or canOut = 3)");
		else {
			queryString.append(" and obj.canOut="+canOut);
		}
		//department
		if (typeLv1Id!= null && !"".equals(typeLv1Id.trim())) {
			//String hql = "select id from CotTypeLv1 obj where obj.typeEnName like '"+typeLv1Id+"%'";
			List list = this.getQueryService().getList("CotTypeLv1");
			if(CollectionUtils.isEmpty(list)){
				queryString.append(" and obj.typeLv1Id = '0'");
			}else {
				List idsList = new ArrayList();
				for(int i = 0;i<list.size();i++){
					CotTypeLv1 lv1 = (CotTypeLv1)list.get(i);
					if(lv1.getTypeEnName().toLowerCase().indexOf(typeLv1Id.toLowerCase()) > -1){
						idsList.add(lv1.getId());
					}
				}
				String ids  = "0";
				if(idsList.size() > 0){
					ids = StringUtils.join(idsList.toArray(),",");
				}
				queryString.append(" and obj.typeLv1Id in("+ids+")");
			}
//				queryString.append(" and obj.typeLv1Id =" +Integer.parseInt(typeLv1Id));
		}
		if (clauseTypeId!= null && !"".equals(clauseTypeId.trim())) {
			queryString.append(" and obj.clauseTypeId =" +Integer.parseInt(clauseTypeId));
		}
		Map<String, String> map = processDate(queryType);
		System.out.println("------------------>"+map);
		if (!StringUtils.isEmpty(queryType) && (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime))) {
			startTime = map.get("start");
			endTime = map.get("end");
			statusCol = colMapping(type);
		}
		if(StringUtils.isNotEmpty(startTime)){
			queryString.append(" and "+valueMap.get(type)+" >= '"+startTime+"'");
		}
		if(StringUtils.isNotEmpty(endTime)){
			queryString.append(" and "+valueMap.get(type)+" <= '"+endTime+" 23:59:59'");
		}
		if(StringUtils.isNotEmpty(statusCol)){
		    queryString.append(" and (obj.").append(statusCol).append(" = ").append("0");
		    queryString.append(" or obj.").append(statusCol).append(" is null )");
		}
		System.out.println("=============>"+queryString);
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 200;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句 VDetailStatusId
		queryInfo
				.setCountQuery("select count(*) from VDetailStatusId obj where 1=1 "
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from VDetailStatusId obj where 1=1 ");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		String orderString = " order by obj.orderNo asc";
		if(sortField != null && !"".equals(sortField))
			orderString= " order by "+sortField;
		if(dir != null && !"".equals(dir))
			orderString += " "+dir;
		queryInfo.setOrderString(orderString);
		// 取得排序信息
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getQueryService().getListData(queryInfo, type,flag);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward queryPanDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("querySuccess");

		String eleNameEn = request.getParameter("eleNameEn");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String addPerson = request.getParameter("addPerson");
		String customer = request.getParameter("customerStr");
		String factory = request.getParameter("factoryStr");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.panId=e.id");
		
		if (eleNameEn != null && !eleNameEn.trim().equals("")) {
			queryString.append(" and e.eleNameEn='" + eleNameEn.trim()
					+ "'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.addTime <='" + endTime + " 23:59:59'");
		}
		if (addPerson != null && !"".equals(addPerson.trim())) {
			queryString.append(" and obj.addPerson=" + addPerson);
		}
		if (customer != null && !customer.trim().equals("")) {
			queryString.append(" and obj.customerStr like '%" + customer.trim()
					+ "%'");
		}
		if (factory != null && !factory.trim().equals("")) {
			queryString.append(" and obj.factoryStr like '%" + factory.trim()
					+ "%'");
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotPanDetail obj,CotPanEle e" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotPanDetail obj,CotPanEle e");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取个个状态的上传文件
	 */
	public ActionForward queryOrderStatusFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String orderId = request.getParameter("orderId");
		String orderStatus = request.getParameter("orderStatus");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1 ");
		if(StringUtils.isNotEmpty(orderId)){
			queryString.append(" and obj.orderId="+orderId);
		}
		if(StringUtils.isNotEmpty(orderStatus)){
			queryString.append(" and obj.orderStatus='"+orderStatus+"'");
		}
		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderstatusFile obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotOrderstatusFile obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Map<String, String> processDate(String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> map = new HashMap<String, String>();
		Calendar today = Calendar.getInstance();
		String start = null,
			   end = null;
		if("1".equals(type)){
			end = sdf.format(today.getTime());
		}else if("2".equals(type)){
			List<Calendar> dayOfWeekList = getDayOfWeek(0);
			start = sdf.format(dayOfWeekList.get(0).getTime());
			end = sdf.format(dayOfWeekList.get(dayOfWeekList.size()-1).getTime());
		}else if("3".equals(type)){
			List<Calendar> dayOfWeekList = getDayOfWeek(1);
			start = sdf.format(dayOfWeekList.get(0).getTime());
			end = sdf.format(dayOfWeekList.get(dayOfWeekList.size()-1).getTime());
		}
		map.put("start", start);
		map.put("end", end);
		return map;
	}
	
	private List<Calendar> getDayOfWeek(int weekcount){
		List<Calendar> list = new ArrayList<Calendar>();
		Date theWeek = DateUtils.addWeeks(Calendar.getInstance().getTime(), weekcount);
		Iterator<Calendar> iterator = DateUtils.iterator(theWeek, DateUtils.RANGE_WEEK_MONDAY);
		list = IteratorUtils.toList(iterator);
		return list;
	}
	
	private String colMapping(String type){
	    String col = "";
	    if("SAMPLE".equals(type) ){
	        col = "smapleStatus";
	    }else if("PACKAGE".equals(type)){
	        col = "packetStatus";
	    }else if("SAMPLEOUT".equals(type)){
	        col = "sampleOutStatus";
        }else if("QC".equals(type)){
            col = "qcStatus";
        }else if("OUT".equals(type)){
            col = "outStatus";
        }
	    return col;
	}
}
