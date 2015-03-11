/**
 * 
 */
package com.sail.cot.action.orderout;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFittingOrder;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotOrderOutdetailDel;
import com.sail.cot.domain.CotOrderOuthsdetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderoutPic;
import com.sail.cot.domain.CotOrderouthsPic;
import com.sail.cot.domain.CotPackingOrder;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.VOrderFitorderId;
import com.sail.cot.domain.VOrderOrderfacId;
import com.sail.cot.domain.VOrderPackorderId;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.orderout.CotOrderOutService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotOrderOutAction extends AbstractAction {

	private CotOrderOutService orderOutService;

	// 查询出货单数据
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		String orderNoFind = request.getParameter("orderNoFind");// 订单单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		String currencyId = request.getParameter("currencyId");// 币种
		String companyId = request.getParameter("companyId");// 币种
		String status = request.getParameter("orderStatus");// 出货状态
		String odNo = request.getParameter("odNo");// 
		String typeLv1Id =request.getParameter("typeLv1IdFind");
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderout.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotorderout.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nation_Id in(").append(nationStr).append(")");
				}
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorderout.do", "DEPT");
				if (dept == true) {
					String deptStr = json.getString("dept");
					queryString.append(" and obj.typeLv1_Id in(").append(deptStr).append(")");
				} else {
					//queryString.append(" and obj.BUSINESS_PERSON =" + emp.getId());
				}
			}
		}

		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.ORDER_NO like '%" + orderNoFind.trim()
					+ "%'");
		}
		if (status != null && !status.trim().equals("")) {
			if("1".equals(status)){
				queryString.append(" and obj.ORDER_STATUS=0");
			}else{
				if("2".equals(status)){
					queryString.append(" and obj.ORDER_STATUS=2");
				}
			}
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.CUST_ID=" + custId.trim());
		}
		if (companyId != null && !companyId.trim().equals("")) {
			queryString.append(" and obj.COMPANY_ID=" + companyId.trim());
		}
		if (odNo != null && !odNo.trim().equals("")) {
			queryString.append(" and h.order_no like '%" + odNo.trim()+"%'");
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and obj.CURRENCY_ID=" + currencyId.trim());
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.BUSINESS_PERSON=" + businessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.ORDER_TIME >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.ORDER_TIME <='" + endTime
					+ " 23:59:59'");
		}
		//department
		if (typeLv1Id!= null && !"".equals(typeLv1Id.trim())) {
			//String hql = "select id from CotTypeLv1 obj where obj.typeEnName like '"+typeLv1Id+"%'";
			List list = this.getOrderOutService().getList("CotTypeLv1");
			if(CollectionUtils.isEmpty(list)){
				queryString.append(" and obj.typeLv1_id = '0'");
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
				queryString.append(" and obj.typeLv1_id in("+ids+")");
			}
			//queryString.append(" and obj.typeLv1_id =" +Integer.parseInt(typeLv1Id));
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
		queryInfo.setCountQuery("select count(*) from cot_order_out AS obj"
				+ " left join cot_order h on obj.orderId=h.id "
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id "
				+ "left join cot_customer c on obj.CUST_ID=c.id" + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," + "obj.ORDER_NO as orderNo,"
				+ "obj.ORDER_TIME as orderTime,"  + "obj.ORDER_STATUS as orderStatus,"
				+ "c.CUSTOMER_SHORT_NAME as customerShortName," + "e.EMPS_NAME as empsName, " + "obj.TOTAL_COUNT as totalCount,"
				+ "obj.TOTAL_CONTAINER as totalContainer," + "obj.TOTAL_CBM as totalCbm," 
				+ "h.order_no as odNo," 
				+ "obj.traffic_id as trafficId ," +"obj.typeLv1_id as typeLv1Id ,"+
						"c.id as cId,obj.TOTAL_MONEY as totalMoney," +
						"obj.TOTAL_Hs_MONEY as totalHsMoney,obj.CLAUSE_TYPE_ID as clauseTypeId,obj.paTypeId as paTypeId,"
						+ "h.PO_NO as poNo,"
						+ "h.order_lc_date as orderLcDate,"
						+ "h.SEND_TIME as sendTime,"
						+ "h.order_lc_delay as orderLcDelay,"
						+ "f.SHORT_NAME as shortName,"
						+ "h.all_pin_name as allPinName,"+
						"obj.SHIPPORT_ID as shipportId"+ 
						",obj.TARGETPORT_ID as targetportId "+
						",obj.taxTotalMoney as taxTotalMoney "+
						",obj.CURRENCY_ID as currencyId "
				+ "from cot_order_out AS obj "
				+ "left join cot_order h on obj.orderId=h.id "
				+ "left join cot_factory f on h.factory_id=f.id "
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id "
				+ "left join cot_customer c on obj.CUST_ID=c.id");

		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.order_no desc");
		//queryInfo.setOrderString(" order by obj.id asc");
		queryInfo.setQueryObjType("CotOrderOutVO");
		int totalCount = this.getOrderOutService().getRecordCountJDBC(queryInfo);
		// 得到limit对象
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getOrderOutService().getOrderVOList(queryInfo);
		GridServerHandler gd = new GridServerHandler();
		gd.setData(list);
		gd.setTotalCount(totalCount);

		String json = gd.getLoadResponseText();
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询作废的出货单数据
	public ActionForward queryDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySuccessDel");
		String orderNoFind = request.getParameter("orderNoFind");// 订单单号
		String custId = request.getParameter("custId");// 客户
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		String currencyId = request.getParameter("currencyId");// 币种
		String companyId = request.getParameter("companyId");// 币种
		String orderStatus = request.getParameter("orderStatus");// 币种
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderout.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotorderout.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nation_Id in(").append(nationStr).append(")");
				}
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorderout.do", "DEPT");
				if (dept == true) {
					String deptStr = json.getString("dept");
					queryString.append(" and obj.typeLv1_Id in(").append(deptStr).append(")");
				} else {
					//queryString.append(" and obj.BUSINESS_PERSON =" + emp.getId());
				}
			}
		}

		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.ORDER_NO like '%" + orderNoFind.trim()
					+ "%'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.CUST_ID=" + custId.trim());
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and obj.CURRENCY_ID=" + currencyId.trim());
		}
		if (companyId != null && !companyId.trim().equals("")) {
			queryString.append(" and obj.COMPANY_ID=" + companyId.trim());
		}
		if (orderStatus != null && !orderStatus.trim().equals("") && !orderStatus.trim().equals("0")) {
			queryString.append(" and obj.ORDER_STATUS=" + orderStatus.trim());
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.BUSINESS_PERSON=" + businessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.ORDER_TIME >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.ORDER_TIME <='" + endTime
					+ " 23:59:59'");
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
		queryInfo.setCountQuery("select count(*) from cot_order_out_del AS obj"
				+ " left join cot_order h on obj.orderId=h.id "
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id "
				+ "left join cot_customer c on obj.CUST_ID=c.id" + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," + "obj.ORDER_NO as orderNo,"
				+ "obj.ORDER_TIME as orderTime,"  + "obj.ORDER_STATUS as orderStatus,"
				+ "c.CUSTOMER_SHORT_NAME as customerShortName," + "e.EMPS_NAME as empsName, " + "obj.TOTAL_COUNT as totalCount,"
				+ "obj.TOTAL_CONTAINER as totalContainer," + "obj.TOTAL_CBM as totalCbm," 
				+ "h.order_no as odNo," 
				+ "obj.traffic_id as trafficId ," +"obj.typeLv1_id as typeLv1Id ,"+
						"c.id as cId,obj.TOTAL_MONEY as totalMoney," +
						"obj.TOTAL_Hs_MONEY as totalHsMoney,obj.CLAUSE_TYPE_ID as clauseTypeId,obj.paTypeId as paTypeId,"
						+ "h.PO_NO as poNo,"
						+ "h.order_lc_date as orderLcDate,"
						+ "h.SEND_TIME as sendTime,"
						+ "h.order_lc_delay as orderLcDelay,"
						+ "f.SHORT_NAME as shortName,"
						+ "h.all_pin_name as allPinName,"+
						"obj.SHIPPORT_ID as shipportId"+ 
						",obj.TARGETPORT_ID as targetportId "+
						",obj.taxTotalMoney as taxTotalMoney "+
						",obj.CURRENCY_ID as currencyId "
				+ "from cot_order_out_del AS obj "
				+ "left join cot_order h on obj.orderId=h.id "
				+ "left join cot_factory f on h.factory_id=f.id "
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id "
				+ "left join cot_customer c on obj.CUST_ID=c.id");

		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.order_no asc");
		queryInfo.setQueryObjType("CotOrderOutVO");
		int totalCount = this.getOrderOutService().getRecordCountJDBC(queryInfo);
		// 得到limit对象
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getOrderOutService().getOrderVOList(queryInfo);
		GridServerHandler gd = new GridServerHandler();
		gd.setData(list);
		gd.setTotalCount(totalCount);

		String json = gd.getLoadResponseText();
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询所有未全部出货的订单明细数据
	public ActionForward queryOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String custIdFind = request.getParameter("custIdFind");// 客户
		String orderNoFind = request.getParameter("orderNoFind");// 订单单号或者poNO
		String flag = request.getParameter("flag");

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.canOut=1 and obj.bussinessPerson=e.id and obj.custId=c.id and obj.id=d.orderId and d.unBoxSend=0 and d.boxCount!=0");
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
		if (custIdFind != null && !custIdFind.trim().equals("")) {
			queryString.append(" and obj.custId=" + custIdFind.trim());
		}
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			if ("0".equals(flag)) {
				queryString.append(" and obj.orderNo like '%"
						+ orderNoFind.trim() + "%'");
			} else {
				queryString.append(" and obj.poNo like '%" + orderNoFind.trim()
						+ "%'");
			}
		}
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 总条数语句
		queryInfo
				.setCountQuery("select count(distinct obj) from CotOrder obj,CotEmps e,CotCustomer c,CotOrderDetail d"
						+ queryString.toString());
		// 查询语句
		queryInfo
				.setSelectString("select distinct obj from CotOrder obj,CotEmps e,CotCustomer c,CotOrderDetail d");

		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		// 根据起始
		List<?> list = this.getOrderOutService().getList(queryInfo);

		int count = this.getOrderOutService().getRecordCount(queryInfo);

		String[] excludes = { "orderMBImg", "cotOrderDetails" };
		try {
			GridServerHandler gd = new GridServerHandler(excludes);
			gd.setData(list);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	// 查询所有未全部出货的订单明细数据
	public ActionForward queryOrderOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String orderId = request.getParameter("orderId");// 订单单号
		String eleIdFind = request.getParameter("eleIdFind");// 货号

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.id=d.orderId and d.unBoxSend=0 and d.boxCount!=0");

		if (eleIdFind != null && !eleIdFind.trim().equals("")) {
			queryString
					.append(" and d.eleId like '%" + eleIdFind.trim() + "%'");
		}
		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.id=" + orderId);
		}
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		queryInfo
				.setCountQuery("select count(*) from CotOrder obj,CotOrderDetail d"
						+ queryString);
		// 查询语句
		queryInfo
				.setSelectString("select d from CotOrder obj,CotOrderDetail d");

		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by d.sortNo");

		// 根据起始
		List<?> list = this.getOrderOutService().getList(queryInfo);
		int count = this.getOrderOutService().getRecordCount(queryInfo);
		String[] excludes = { "picImg" };
		try {
			GridServerHandler gd = new GridServerHandler(excludes);
			gd.setData(list);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询该出货单明细产品的信息
	public ActionForward queryOrderDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String flag = request.getParameter("flag");

		if (start == null || limit == null)
			return null;

		String orderId = request.getParameter("orderId");// 主出货单号
		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.orderDetailId=od.id and od.orderId=p.id and obj.orderId="
						+ orderId);

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderOutdetail obj,CotOrderDetail od,CotOrder p"
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj,od.unBoxCount,p.orderNo,p.poNo from CotOrderOutdetail obj,CotOrderDetail od,CotOrder p");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.eleId asc");

		int count = this.getOrderOutService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 根据起始
		List<?> list = this.getOrderOutService()
				.getOrderDetailVOList(queryInfo);

		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		// 清空内存map
		session.removeAttribute("SessionORDEROUT");
		for (int i = 0; i < list.size(); i++) {
			CotOrderOutdetail cotOrderOutdetail = (CotOrderOutdetail) list
					.get(i);
			cotOrderOutdetail.setPicImg(null);
			this.getOrderOutService().setMapAction(session,
					cotOrderOutdetail.getOrderDetailId(), cotOrderOutdetail);
		}

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
	
	// 查询该出货作废单明细产品的信息
	public ActionForward queryOrderDetailDel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String flag = request.getParameter("flag");

		if (start == null || limit == null)
			return null;

		String orderId = request.getParameter("orderId");// 主出货单号
		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.orderId="+ orderId);

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderOutdetailDel obj"
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotOrderOutdetailDel obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.eleId asc");

		int count = this.getOrderOutService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 根据起始
		List<?> list = this.getOrderOutService()
				.getList(queryInfo);

		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		for (int i = 0; i < list.size(); i++) {
			CotOrderOutdetailDel cotOrderOutdetail = (CotOrderOutdetailDel) list
					.get(i);
			cotOrderOutdetail.setPicImg(null);
			this.getOrderOutService().setMapDelAction(session,
					cotOrderOutdetail.getOrderDetailId(), cotOrderOutdetail);
		}

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

	// 查询明细产品的样品信息
	public ActionForward queryEleFrame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryEleFrame");
	}

	// 查询差额明细产品的样品信息
	public ActionForward queryEleFrameOut(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("queryEleFrameOut");
	}

	// 跳转到出货添加页面
	public ActionForward addOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// request.setAttribute("allFactoryName", this.getOrderOutService()
		// .getFactoryMap());
		request.setAttribute("allCurrencyName", this.getOrderOutService()
				.getCurrencyMap(request));
		return mapping.findForward("oOutDetail");
	}
	
	// 跳转到出货作废单编辑页面
	public ActionForward addOrderDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// request.setAttribute("allFactoryName", this.getOrderOutService()
		// .getFactoryMap());
		request.setAttribute("allCurrencyName", this.getOrderOutService()
				.getCurrencyMap(request));
		return mapping.findForward("oOutDetailDel");
	}

	// 保存出货明细
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 内存数据
		HttpSession session = request.getSession();
		HashMap<Integer, CotOrderOutdetail> orderMap = this
				.getOrderOutService().getMapAction(session);

		List<CotOrderOutdetail> records = new ArrayList<CotOrderOutdetail>();
		List<CotOrderoutPic> imgList = new ArrayList<CotOrderoutPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		String orderId = request.getParameter("orderPrimId");
		String currencyId = request.getParameter("currencyId");

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object orderDetailId = jsonObject.get("orderDetailId");
			if (orderDetailId != null && !"".equals(orderDetailId.toString())) {
				CotOrderOutdetail orderOutdetail = orderMap
						.get((Integer) orderDetailId);
				// 设置序列号
				Integer sortNo = (Integer) jsonObject.get("sortNo");
				orderOutdetail.setSortNo(sortNo);
				// 设置是否锁定价格
				Object checkJian = jsonObject.get("checkJian");
				if (checkJian == null || checkJian.toString().equals("null")) {
					orderOutdetail.setCheckJian(0);
				} else {
					if ((Boolean) checkJian == false) {
						orderOutdetail.setCheckJian(0);
					} else {
						orderOutdetail.setCheckJian(1);
					}
				}
				records.add(orderOutdetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object orderDetailId = jsonObject.get("orderDetailId");
				if (orderDetailId != null
						&& !"".equals(orderDetailId.toString())) {
					CotOrderOutdetail cotOrderDetail = orderMap
							.get((Integer) orderDetailId);
					// 设置序列号
					Integer sortNo = (Integer) jsonObject.get("sortNo");
					cotOrderDetail.setSortNo(sortNo);
					// 设置是否锁定价格
					Object checkJian = jsonObject.get("checkJian");
					if (checkJian == null
							|| checkJian.toString().equals("null")) {
						cotOrderDetail.setCheckJian(0);
					} else {
						if (checkJian instanceof Integer) {
							cotOrderDetail.setCheckJian((Integer) checkJian);
						}
						if (checkJian instanceof Boolean) {
							if ((Boolean) checkJian == false) {
								cotOrderDetail.setCheckJian(0);
							} else {
								cotOrderDetail.setCheckJian(1);
							}
						}
					}
					records.add(cotOrderDetail);
				}
			}
		}
		// 用于修改订单明细数量
		String detailIds = "";
		for (int i = 0; i < records.size(); i++) {
			CotOrderOutdetail detail = records.get(i);
			if (detail == null) {
				continue;
			}
			detail.setCurrencyId(Integer.parseInt(currencyId));
			detail.setOrderId(Integer.parseInt(orderId));
			detail.setEleAddTime(new Date(System.currentTimeMillis()));// 添加时间
			detail.setAddTime(new Date(System.currentTimeMillis()));// 编辑时间

			// 设置总数
			if (detail.getBoxCount() == null) {
				detail.setBoxCount(0l);
			}
			if (detail.getContainerCount() == null) {
				detail.setContainerCount(0l);
			}
			if (detail.getBoxCbm() == null) {
				detail.setTotalCbm(0f);
				detail.setRemainTotalCbm(0f);// 剩余总体积
			} else {
				float totalCbm = detail.getContainerCount()
						* detail.getBoxCbm();
				detail.setTotalCbm(totalCbm);
				detail.setRemainTotalCbm(totalCbm);// 剩余总体积
			}
			if (detail.getPriceFac() == null) {
				detail.setTotalFac(0f);
			} else {
				detail.setTotalFac(detail.getContainerCount()
						* detail.getPriceFac());
			}
			if (detail.getBoxGrossWeigth() == null) {
				detail.setTotalGrossWeigth(0f);
			} else {
				detail.setTotalGrossWeigth(detail.getContainerCount()
						* detail.getBoxGrossWeigth());
			}
			if (detail.getBoxNetWeigth() == null) {
				detail.setTotalNetWeigth(0f);
			} else {
				detail.setTotalNetWeigth(detail.getContainerCount()
						* detail.getBoxNetWeigth());
			}
			if (detail.getBoxCount() == null || detail.getOrderPrice() == null) {
				detail.setTotalMoney(0f);
			} else {
				detail.setTotalMoney(detail.getBoxCount()
						* detail.getOrderPrice());
			}
			// 初始化
			detail.setOrderFlag(0l);// 是否排载
			detail.setRemainBoxCount(detail.getBoxCount());// 剩余总数量

			// 累加要修改的订单明细字符串
			detailIds += detail.getOrderDetailId() + "-"
					+ detail.getUnSendNum() + ",";

			// 设置出货图片
			CotOrderoutPic cotOrderoutPic = new CotOrderoutPic();
			CotOrderPic cotOrderPic = impOpService.getOrderPic(detail
					.getOrderDetailId());
			detail.setPicName(cotOrderPic.getPicName());
			cotOrderoutPic.setEleId(cotOrderPic.getEleId());
			cotOrderoutPic.setPicImg(cotOrderPic.getPicImg());
			cotOrderoutPic.setPicSize(cotOrderPic.getPicImg().length);
			cotOrderoutPic.setPicName(cotOrderPic.getEleId());
			// 添加到图片数组
			imgList.add(cotOrderoutPic);
		}

		// 保存
		try {
			this.getOrderOutService().modifyOrderOutTotalAction(records,
					request);
			// 存储最新的出货明细统计信息
			this.getOrderOutService().getTotalOutDetail(request,
					Integer.parseInt(orderId));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 修改订单明细未出货数量
//		this.getOrderOutService().updateOrderDetail(detailIds);
		// 保存报价图片
		for (int i = 0; i < records.size(); i++) {
			CotOrderOutdetail detail = (CotOrderOutdetail) records.get(i);
			CotOrderoutPic cotOrderPic = (CotOrderoutPic) imgList.get(i);
			cotOrderPic.setFkId(detail.getId());
			List picList = new ArrayList();
			picList.add(cotOrderPic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(picList);
		}

		return null;
	}

	// 保存新添加的差额数据
	public ActionForward addCha(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 内存数据
		HttpSession session = request.getSession();
		HashMap<Integer, CotOrderOuthsdetail> orderMap = this
				.getOrderOutService().getChaMapAction(session);

		List<CotOrderOuthsdetail> records = new ArrayList<CotOrderOuthsdetail>();
		List<CotOrderouthsPic> imgList = new ArrayList<CotOrderouthsPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		String orderId = request.getParameter("orderPrimId");
		String currencyId = request.getParameter("currencyId");

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object orderDetailId = jsonObject.get("orderDetailId");
			if (orderDetailId != null && !"".equals(orderDetailId.toString())) {
				CotOrderOuthsdetail orderOutdetail = orderMap.get(Integer
						.parseInt(orderDetailId.toString()));
				// 设置序列号
				Object temp = jsonObject.get("sortNo");
				if ((temp instanceof JSONObject) == false) {
					Integer sortNo = (Integer) jsonObject.get("sortNo");
					orderOutdetail.setSortNo(sortNo);
				}
				records.add(orderOutdetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object orderDetailId = jsonObject.get("orderDetailId");
				if (orderDetailId != null
						&& !"".equals(orderDetailId.toString())) {
					CotOrderOuthsdetail cotOrderDetail = orderMap.get(Integer
							.parseInt(orderDetailId.toString()));
					// 设置序列号
					Object temp = jsonObject.get("sortNo");
					if ((temp instanceof JSONObject) == false) {
						Integer sortNo = (Integer) jsonObject.get("sortNo");
						cotOrderDetail.setSortNo(sortNo);
					}
					records.add(cotOrderDetail);
				}
			}
		}
		byte[] zwtp = this.getOrderOutService().getZwtpPic();
		// 用于修改订单明细数量
		String detailIds = "";
		for (int i = 0; i < records.size(); i++) {
			CotOrderOuthsdetail detail = records.get(i);
			if (detail == null) {
				continue;
			}
			detail.setCurrencyId(Integer.parseInt(currencyId));
			detail.setOrderId(Integer.parseInt(orderId));
			detail.setEleAddTime(new Date(System.currentTimeMillis()));// 添加时间
			detail.setAddTime(new Date(System.currentTimeMillis()));// 编辑时间

			// 设置总数
			if (detail.getBoxCount() == null) {
				detail.setBoxCount(0l);
			}
			if (detail.getContainerCount() == null) {
				detail.setContainerCount(0l);
			}
			if (detail.getBoxCbm() == null) {
				detail.setTotalCbm(0f);
			} else {
				detail.setTotalCbm(detail.getContainerCount()
						* detail.getBoxCbm());
			}
			if (detail.getPriceFac() == null) {
				detail.setTotalFac(0f);
			} else {
				detail.setTotalFac(detail.getBoxCount() * detail.getPriceFac());
			}
			if (detail.getBoxGrossWeigth() == null) {
				detail.setTotalGrossWeigth(0f);
			} else {
				detail.setTotalGrossWeigth(detail.getContainerCount()
						* detail.getBoxGrossWeigth());
			}
			if (detail.getBoxNetWeigth() == null) {
				detail.setTotalNetWeigth(0f);
			} else {
				detail.setTotalNetWeigth(detail.getContainerCount()
						* detail.getBoxNetWeigth());
			}
			if (detail.getOrderPrice() == null) {
				detail.setTotalMoney(0f);
			} else {
				detail.setTotalMoney(detail.getBoxCount()
						* detail.getOrderPrice());
			}

			CotOrderouthsPic cotOrderoutPic = new CotOrderouthsPic();
			CotOrderPic cotOrderPic = impOpService.getOrderPic(detail
					.getOrderDetailId());
			if (cotOrderPic != null) {
				detail.setPicName(cotOrderPic.getPicName());
				cotOrderoutPic.setEleId(cotOrderPic.getEleId());
				cotOrderoutPic.setPicImg(cotOrderPic.getPicImg());
				cotOrderoutPic.setPicSize(cotOrderPic.getPicImg().length);
				cotOrderoutPic.setPicName(cotOrderPic.getEleId());
			} else {
				cotOrderoutPic.setEleId(detail.getEleId());
				cotOrderoutPic.setPicImg(zwtp);
				cotOrderoutPic.setPicSize(zwtp.length);
				cotOrderoutPic.setPicName(detail.getEleId());
			}
			// 添加到图片数组
			imgList.add(cotOrderoutPic);
		}

		// 保存
		try {
			this.getOrderOutService().modifyChaTotalAction(records, request);
			// 存储最新的出货明细统计信息
			this.getOrderOutService().getTotalChaDetail(request,
					Integer.parseInt(orderId));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 保存报关明细图片
		for (int i = 0; i < records.size(); i++) {
			CotOrderOuthsdetail detail = (CotOrderOuthsdetail) records.get(i);
			CotOrderouthsPic cotOrderPic = (CotOrderouthsPic) imgList.get(i);
			cotOrderPic.setFkId(detail.getId());
			List picList = new ArrayList();
			picList.add(cotOrderPic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(picList);
		}

		return null;
	}

	// 编辑时添加参数
	public CotOrderOutdetail setOrderDetailVal(CotOrderOutdetail detail) {
		if (detail.getBoxCount() == null) {
			detail.setBoxCount(0l);
		}
		if (detail.getContainerCount() == null) {
			detail.setContainerCount(0l);
		}

		if (detail.getBoxCbm() != null) {
			detail.setTotalCbm(detail.getContainerCount() * detail.getBoxCbm());
		} else {
			detail.setTotalCbm(0f);
		}
		if (detail.getPriceFac() != null) {
			detail.setTotalFac(detail.getBoxCount() * detail.getPriceFac());
		} else {
			detail.setTotalFac(0f);
		}
		if (detail.getBoxGrossWeigth() != null) {
			detail.setTotalGrossWeigth(detail.getContainerCount()
					* detail.getBoxGrossWeigth());
		} else {
			detail.setTotalGrossWeigth(0f);
		}
		if (detail.getBoxNetWeigth() != null) {
			detail.setTotalNetWeigth(detail.getContainerCount()
					* detail.getBoxNetWeigth());
		} else {
			detail.setTotalNetWeigth(0f);
		}
		if (detail.getOrderPrice() != null) {
			detail.setTotalMoney(detail.getBoxCount() * detail.getOrderPrice());
		} else {
			detail.setTotalMoney(0f);
		}
		// 克隆对象,避免造成指针混用
		CotOrderOutdetail cloneObj = (CotOrderOutdetail) SystemUtil
				.deepClone(detail);
		return cloneObj;
	}

	// 编辑时添加参数
	public CotOrderOuthsdetail setOrderChaVal(CotOrderOuthsdetail detail) {
		if (detail.getBoxCount() == null) {
			detail.setBoxCount(0l);
		}
		if (detail.getContainerCount() == null) {
			detail.setContainerCount(0l);
		}
		if (detail.getBoxCbm() != null) {
			detail.setTotalCbm(detail.getContainerCount() * detail.getBoxCbm());
		} else {
			detail.setTotalCbm(0f);
		}
		if (detail.getPriceFac() != null) {
			detail.setTotalFac(detail.getBoxCount() * detail.getPriceFac());
		} else {
			detail.setTotalFac(0f);
		}
		if (detail.getBoxGrossWeigth() != null) {
			detail.setTotalGrossWeigth(detail.getContainerCount()
					* detail.getBoxGrossWeigth());
		} else {
			detail.setTotalGrossWeigth(0f);
		}
		if (detail.getBoxNetWeigth() != null) {
			detail.setTotalNetWeigth(detail.getContainerCount()
					* detail.getBoxNetWeigth());
		} else {
			detail.setTotalNetWeigth(0f);
		}
		// detail.setTotalMoney(detail.getBoxCount()*detail.getOrderPrice());
		// 克隆对象,避免造成指针混用
		CotOrderOuthsdetail cloneObj = (CotOrderOuthsdetail) SystemUtil
				.deepClone(detail);
		return cloneObj;
	}

	// 更新出货明细
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// 内存数据
		HttpSession session = request.getSession();
		HashMap<Integer, CotOrderOutdetail> orderMap = this
				.getOrderOutService().getMapAction(session);

		String orderId = request.getParameter("orderPrimId");

		List<CotOrderOutdetail> records = new ArrayList<CotOrderOutdetail>();
		// 用于修改订单明细数量
		String detailIds = "";
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object orderDetailId = jsonObject.get("orderDetailId");
			if (orderDetailId != null && !"".equals(orderDetailId.toString())) {
				CotOrderOutdetail cotOrderDetail = orderMap
						.get((Integer) orderDetailId);
				// 设置是否锁定价格
				Object checkJian = jsonObject.get("checkJian");
				if (checkJian instanceof Integer) {
					cotOrderDetail.setCheckJian((Integer) checkJian);
				}
				if (checkJian instanceof Boolean) {
					if ((Boolean) checkJian == false) {
						cotOrderDetail.setCheckJian(0);
					} else {
						cotOrderDetail.setCheckJian(1);
					}
				}
				records.add(setOrderDetailVal(cotOrderDetail));
				// 累加要修改的订单明细字符串
				detailIds += cotOrderDetail.getOrderDetailId() + "-"
						+ cotOrderDetail.getUnSendNum() + ",";
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object orderDetailId = jsonObject.get("orderDetailId");
				if (orderDetailId != null
						&& !"".equals(orderDetailId.toString())) {
					CotOrderOutdetail cotOrderDetail = orderMap
							.get((Integer) orderDetailId);
					// 设置是否锁定价格
					Object checkJian = jsonObject.get("checkJian");
					if (checkJian instanceof Integer) {
						cotOrderDetail.setCheckJian((Integer) checkJian);
					}
					if (checkJian instanceof Boolean) {
						if ((Boolean) checkJian == false) {
							cotOrderDetail.setCheckJian(0);
						} else {
							cotOrderDetail.setCheckJian(1);
						}
					}
					records.add(setOrderDetailVal(cotOrderDetail));
					// 累加要修改的订单明细字符串
					detailIds += cotOrderDetail.getOrderDetailId() + "-"
							+ cotOrderDetail.getUnSendNum() + ",";
				}
			}
		}

		try {
			this.getOrderOutService().modifyOrderOutTotalAction(records,
					request);
			// 存储最新的出货明细统计信息
			this.getOrderOutService().getTotalOutDetail(request,
					Integer.parseInt(orderId));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 修改订单明细未出货数量
//		this.getOrderOutService().updateOrderDetail(detailIds);
		return null;
	}

	// 更新差额数据
	public ActionForward modifyCha(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// 内存数据
		HttpSession session = request.getSession();
		HashMap<Integer, CotOrderOuthsdetail> orderMap = this
				.getOrderOutService().getChaMapAction(session);

		String orderId = request.getParameter("orderPrimId");

		List<CotOrderOuthsdetail> records = new ArrayList<CotOrderOuthsdetail>();
		// 用于修改订单明细数量
		String detailIds = "";
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object orderDetailId = jsonObject.get("orderDetailId");
			if (orderDetailId != null && !"".equals(orderDetailId.toString())) {
				CotOrderOuthsdetail cotOrderDetail = orderMap
						.get((Integer) orderDetailId);
				records.add(setOrderChaVal(cotOrderDetail));
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object orderDetailId = jsonObject.get("orderDetailId");
				if (orderDetailId != null
						&& !"".equals(orderDetailId.toString())) {
					CotOrderOuthsdetail cotOrderDetail = orderMap
							.get((Integer) orderDetailId);
					records.add(setOrderChaVal(cotOrderDetail));
				}
			}
		}

		try {
			this.getOrderOutService().modifyChaTotalAction(records, request);
			// 存储最新的出货明细统计信息
			this.getOrderOutService().getTotalChaDetail(request,
					Integer.parseInt(orderId));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	// 删除出货明细
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		String orderId = request.getParameter("orderPrimId");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}

		this.getOrderOutService().deleteOrderDetails(request, ids);
		// 存储最新的出货明细统计信息
		this.getOrderOutService().getTotalOutDetail(request,
				Integer.parseInt(orderId));

		return null;
	}

	// 删除差额数据
	public ActionForward removeCha(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		String orderId = request.getParameter("orderPrimId");
		String flag = request.getParameter("flag");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		if (flag != null) {
			this.getOrderOutService().deleteChaDetails(request, ids, 1);
		} else {
			this.getOrderOutService().deleteChaDetails(request, ids, 0);
		}

		// 存储最新的出货明细统计信息
		this.getOrderOutService().getTotalChaDetail(request,
				Integer.parseInt(orderId));

		return null;
	}

	// 跳转到出货单明细编辑页面
	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("detail");
	}

	// 跳转到上传麦标页面
	public ActionForward showUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showUpload");
	}

	// 跳转到差额数据页面
	public ActionForward showCha(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("chaDetail");
	}
	
	// 跳转到作废单的差额数据页面
	public ActionForward showChaDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("chaDetailDel");
	}

	// 查询差额数据
	public ActionForward queryChaInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String orderId = request.getParameter("orderId");// 主出货单号
		if (orderId == null || "null".equals(orderId))
			orderId = "0";
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderOuthsdetail obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderOuthsdetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.eleId asc");

		try {
			String json = this.getOrderOutService().getListChaData(request,
					queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询差额数据
//	public ActionForward queryChaInfoDel(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//
//		String start = request.getParameter("start");
//		String limit = request.getParameter("limit");
//		if (start == null || limit == null)
//			return null;
//
//		String orderId = request.getParameter("orderId");// 主出货单号
//		if (orderId == null || "null".equals(orderId))
//			orderId = "0";
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where obj.orderId=" + orderId);
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		int startIndex = Integer.parseInt(start);
//		queryInfo.setStartIndex(startIndex);
//		// 设置每页显示多少行
//		int pageCount = Integer.parseInt(limit);
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotOrderOuthsdetailDel obj"
//				+ queryString);
//		// 设置查询记录语句
//		queryInfo.setSelectString("from CotOrderOuthsdetailDel obj");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.sortNo asc,obj.eleId asc");
//
//		try {
//			String json = this.getOrderOutService().getListChaDelData(request,
//					queryInfo);
//			response.getWriter().write(json);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	/*---------------------------- 应付帐款操作部分 --------------------------------------*/
	// 查询出货的应付款其他费用
	public ActionForward queryFinanceDeal(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryFinanceDeal");

		String fkId = request.getParameter("fkId");// 外键
		String type = request.getParameter("type");// 类型 0:应收 1：应付

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.source in ('orderfacToOut','FacDeal','FitDeal','PackDeal','FacOther','FitOther','PackOther','yiDeal','FacMoney','FitMoney','PackMoney')");

		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}
		if (type != null && !type.trim().equals("")) {
			queryString.append(" and obj.type=" + type.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceOther obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("from CotFinaceOther obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 采购单未冲完应付帐款记录
	public ActionForward queryFacDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String facId = request.getParameter("facId");// 单号
		String flag = request.getParameter("flag");// 类型标志 orderfac：产品
													// fitorder：配件 packorder：配件

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (facId != null && !facId.trim().equals("")) {
			String temp = facId.trim();
			queryString.append(" and obj.fkId=" + temp);
		} else {
			queryString.append(" and 1=0");
		}

		if (flag != null && !flag.trim().equals("")) {
			queryString.append(" and obj.source='" + flag + "'");
		}
		queryString.append(" and obj.zhRemainAmount > 0");

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceAccountdeal obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("from CotFinaceAccountdeal obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 采购单其他费用减项记录
	public ActionForward queryFacOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String facId = request.getParameter("facId");// 单号
		String flag = request.getParameter("flag");// 类型标志 orderfac：产品
		// fitorder：配件

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where 1=1 and obj.source in ('orderfac','fitorder','packorder') and obj.remainAmount>0 and (obj.outFlag is null or obj.outFlag=0)");

		if (facId != null && !facId.trim().equals("")) {
			String temp = facId.trim();
			queryString.append(" and obj.fkId=" + temp);
		} else {
			queryString.append(" and 1=0");
		}

		if (flag != null && !flag.trim().equals("")) {
			queryString.append(" and obj.source='" + flag + "'");
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
		queryInfo.setCountQuery("select count(*) from CotFinaceOther obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotFinaceOther obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 付款记录-溢付款
	public ActionForward queryOverFee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String facId = request.getParameter("facId");// 单号
		String flag = request.getParameter("flag");// 类型标志 orderfac：产品
		// fitorder：配件
		Integer factoryId = null;

		if ("orderfac".equals(flag)) {
			CotOrderFac tmp = this.getOrderOutService().getOrderFacById(
					Integer.parseInt(facId));
			factoryId = tmp.getFactoryId();
		}

		if ("fitorder".equals(flag)) {
			CotFittingOrder tmp = this.getOrderOutService().getFitOrderById(
					Integer.parseInt(facId));
			factoryId = tmp.getFactoryId();
		}
		if ("packorder".equals(flag)) {
			CotPackingOrder tmp = this.getOrderOutService().getPackOrderById(
					Integer.parseInt(facId));
			factoryId = tmp.getFactoryId();
		}

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.amount>0 "
				+ "and obj.source is null and obj.finaceName='溢付款'");

		if (factoryId != null) {
			queryString.append(" and obj.factoryId=" + factoryId);
		} else {
			queryString.append(" and 1=0");
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
		queryInfo.setCountQuery("select count(*) from CotFinaceOther obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotFinaceOther obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存出货应付款其他费用
	public ActionForward addOrderFacOther(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String fkId = request.getParameter("mainId");// 主单编号

		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();

		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinaceOther.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				String name = jsonObject.getString("finaceName");
				if (!"".equals(name)) {
					CotFinaceOther finaceOther = new CotFinaceOther();
					for (int i = 0; i < properties.length; i++) {
						BeanUtils.setProperty(finaceOther, properties[i],
								jsonObject.get(properties[i]));
					}
					finaceOther.setType(1);// 类型 0:应收 1：应付
					finaceOther.setBusinessPerson(empId);
					finaceOther.setStatus(0);
					if (finaceOther.getFactoryId() != 0) {
						records.add(finaceOther);
					}
				}
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					String name = jsonObject.getString("finaceName");
					if (!"".equals(name)) {
						CotFinaceOther finaceOther = new CotFinaceOther();
						for (int j = 0; j < properties.length; j++) {
							BeanUtils.setProperty(finaceOther, properties[j],
									jsonObject.get(properties[j]));
						}
						finaceOther.setType(1);// 类型 0:应收 1：应付
						finaceOther.setBusinessPerson(empId);
						finaceOther.setStatus(0);
						if (finaceOther.getFactoryId() != 0) {
							records.add(finaceOther);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 其他费用
		List other = new ArrayList();
		// 应付款
		List facDeal = new ArrayList();

		// 付款记录到出货的溢付款
		List amountYi = new ArrayList();

		for (int i = 0; i < records.size(); i++) {
			CotFinaceOther cotFinaceOther = records.get(i);
			cotFinaceOther.setFkId(Integer.parseInt(fkId));

			if (cotFinaceOther.getSource().equals("FacOther")
					|| cotFinaceOther.getSource().equals("FitOther")
					|| cotFinaceOther.getSource().equals("PackOther")) {
				other.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("FacDeal")
					|| cotFinaceOther.getSource().equals("FitDeal")
					|| cotFinaceOther.getSource().equals("PackDeal")) {
				facDeal.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("yiDeal")) {
				amountYi.add(cotFinaceOther);
			}
		}
		// 保存
		this.getOrderOutService().addOrderOutOther(request, records);
		// 修改其他费用
		if (other.size() > 0) {
			this.getOrderOutService().updateOrderIsImport(request, other);
		}
		// 修改应付款
		if (facDeal.size() > 0) {
			this.getOrderOutService().updateFacDeal(request, facDeal);
		}
		// 更改溢付款
		if (amountYi.size() > 0) {
			this.getOrderOutService().updateDealRemain(request, amountYi);
		}
		return null;
	}

	// 修改应付款其他费用
	public ActionForward modifyOrderFacOther(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinaceOther.class);
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFinaceOther finaceOther = this.getOrderOutService()
						.getFinaceOtherById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if (jsonObject.get(properties[i]) != null) {
						BeanUtils.setProperty(finaceOther, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				finaceOther.setBusinessPerson(empId);
				if (finaceOther.getFactoryId() != 0) {
					records.add(finaceOther);
				}
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotFinaceOther finaceOther = this.getOrderOutService()
							.getFinaceOtherById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if (jsonObject.get(properties[j]) != null) {
							BeanUtils.setProperty(finaceOther, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					finaceOther.setBusinessPerson(empId);
					if (finaceOther.getFactoryId() != 0) {
						records.add(finaceOther);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 其他费用
		List other = new ArrayList();
		// 应付款
		List facDeal = new ArrayList();

		// 收款记录到出货的溢收款
		List amountYi = new ArrayList();

		// 填充订单主单编号
		for (int i = 0; i < records.size(); i++) {
			CotFinaceOther cotFinaceOther = records.get(i);
			if (cotFinaceOther.getSource().equals("FacOther")
					|| cotFinaceOther.getSource().equals("FitOther")
					|| cotFinaceOther.getSource().equals("PackOther")) {
				other.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("FacDeal")
					|| cotFinaceOther.getSource().equals("FitDeal")
					|| cotFinaceOther.getSource().equals("PackDeal")) {
				facDeal.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("yiDeal")
					&& cotFinaceOther.getOutFlag() != null) {
				amountYi.add(cotFinaceOther);
			}
		}

		// 修改其他费用
		if (other.size() > 0) {
			this.getOrderOutService().updateOtherRemain(request, other);
		}
		// 修改应付款
		if (facDeal.size() > 0) {
			this.getOrderOutService().updateDealMod(request, facDeal);
		}
		// 更改溢付款
		if (amountYi.size() > 0) {
			this.getOrderOutService().updateDealYiRemain(request, amountYi);
		}

		// 保存
		this.getOrderOutService().updateDealOtherList(records);

		return null;
	}

	// 删除出其他费用
	public ActionForward removeOrderFacOther(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		for (int i = 0; i < ids.size(); i++) {
			Integer id = (Integer) ids.get(i);
			this.getOrderOutService().modifyOtherRemainDel(id);
		}

		this.getOrderOutService().deleteOtherList(ids);
		JSONObject suc = new JSONObject();
		suc.put("success", true);
		suc.put("msg", "成功");
		try {
			response.getWriter().write(suc.toString());
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
		}
		return null;
	}

	// 查询应付帐款
	public ActionForward queryDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryDeal");

		String orderNo = request.getParameter("orderNo");// 单号
		String fkId = request.getParameter("fkId");// 外键

		StringBuffer queryString = new StringBuffer();

		// 最高权限
		boolean all = SystemUtil.isAction(request, "cotdeal.do", "ALL");
		// 部门权限
		boolean dept = SystemUtil.isAction(request, "cotdeal.do", "DEPT");

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

		queryString
				.append(" and obj.source in ('orderout','orderfacOther','orderfacDeal')");

		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo='" + orderNo.trim() + "'");
		}
		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
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
				.setCountQuery("select count(*) from CotFinaceAccountdeal obj,CotEmps e"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotFinaceAccountdeal obj,CotEmps e");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除
	public ActionForward removeDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}

		// this.getOrderOutService().deleteDealList(ids);
		JSONObject suc = new JSONObject();
		suc.put("success", true);
		suc.put("msg", "成功");
		try {
			response.getWriter().write(suc.toString());
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
		}
		return null;
	}

	// 查询应付帐款冲帐明细
	public ActionForward queryDealDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryDealDetail");

		String dealId = request.getParameter("dealId");// 客户

		StringBuffer queryString = new StringBuffer();

		// 最高权限
		boolean all = SystemUtil.isAction(request, "cotdeal.do", "ALL");
		// 部门权限
		boolean dept = SystemUtil.isAction(request, "cotdeal.do", "DEPT");

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
		// queryString.append(" and obj.status = 0");

		if (dealId != null && !dealId.trim().equals("")) {
			queryString.append(" and obj.dealId=" + dealId.trim());
		} else {
			return null;
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
				.setCountQuery("select count(*) from CotFinacegivenDetail obj,CotEmps e"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotFinacegivenDetail obj,CotEmps e");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 合同分配
	public ActionForward conToAllocate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("conToAllocate");
	}

	// 查询出货货号对应的采购单
	public ActionForward queryOrderFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrderFac");

		String orderId = request.getParameter("orderDetailid");// 订单明细ID
		// String eleId = request.getParameter("eleId");// 货号

		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderDetailid=" + orderId);

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from VOrderOrderfacId obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from VOrderOrderfacId obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询出货货号对应的配件采购单
	public ActionForward queryFitOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryFitOrderDetail");

		String orderId = request.getParameter("orderId");// 订单明细ID

		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from VOrderFitorderId obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from VOrderFitorderId obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询出货货号对应的配件采购单
	public ActionForward queryPackOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryPackOrderDetail");

		String orderId = request.getParameter("orderId");// 订单明细ID

		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from VOrderPackorderId obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from VOrderPackorderId obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询数量指定明细
	public ActionForward queryAllocateDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryAllocateDetail");

		String orderDetailId = request.getParameter("orderDetailId");// 采购明细单号
		String eleId = request.getParameter("eleId");// 货号

		if (orderDetailId == null || "".equals(orderDetailId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();

		queryString.append(" where obj.orderDetailid=" + orderDetailId);

		if (eleId.trim() != null && !eleId.trim().equals("")) {
			queryString.append(" and obj.eleId = '" + eleId.trim() + "'");
		}

		queryString.append(" and obj.allocateFlag = 1");
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from VOrderOrderfacId obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from VOrderOrderfacId obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getOrderOutService().getList(queryInfo);

		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		Map<?, ?> facMap = this.getOrderOutService().getFactoryNameMap(request);

		for (int i = 0; i < list.size(); i++) {
			VOrderOrderfacId cotOrderFacdetail = (VOrderOrderfacId) list
					.get(i);
			//cotOrderFacdetail.setPicImg(null);
			Integer factoryId = this.getOrderOutService().findFactoryIdById(
					cotOrderFacdetail.getId());
			if (factoryId != 0) {
//				cotOrderFacdetail.setFactoryShortName(facMap.get(
//						factoryId.toString()).toString());
			}
			this.getOrderOutService().setFacMapAction(session,
					cotOrderFacdetail.getOrderDetailid(), cotOrderFacdetail);
		}

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询数量指定明细
	public ActionForward queryFitFacDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryFitFacDetail");

		String orderId = request.getParameter("orderId");// 订单明细ID

		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderId);

		queryString.append(" and obj.allocateFlag = 1");
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from VOrderFitorderId obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from VOrderFitorderId obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getOrderOutService().getList(queryInfo);

		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		Map<?, ?> facMap = this.getOrderOutService().getFactoryNameMap(request);

		for (int i = 0; i < list.size(); i++) {
			VOrderFitorderId fitorderDetail = (VOrderFitorderId) list.get(i);

			Integer factoryId = this.getOrderOutService().findFitFactoryIdById(
					fitorderDetail.getId());
			if (factoryId != 0) {
				fitorderDetail.setFactoryShortName(facMap.get(
						factoryId.toString()).toString());
			}
			this.getOrderOutService().setFitFacMapAction(session,
					fitorderDetail.getId(), fitorderDetail);
		}

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询数量指定明细
	public ActionForward queryPackFacDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryPackFacDetail");

		String orderId = request.getParameter("orderId");// 订单明细ID

		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderId);

		queryString.append(" and obj.allocateFlag = 1");
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from VOrderPackorderId obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from VOrderPackorderId obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getOrderOutService().getList(queryInfo);

		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		Map<?, ?> facMap = this.getOrderOutService().getFactoryNameMap(request);

		for (int i = 0; i < list.size(); i++) {
			VOrderPackorderId packorderDetail = (VOrderPackorderId) list.get(i);

			Integer factoryId = this.getOrderOutService()
					.findPackFactoryIdById(packorderDetail.getPackingOrderId());
			if (factoryId != 0) {
				packorderDetail.setFactoryShortName(facMap.get(
						factoryId.toString()).toString());
			}
			this.getOrderOutService().setPackFacMapAction(session,
					packorderDetail.getId(), packorderDetail);
		}

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward addOrderFacdetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("allFacMapping", this.getOrderOutService()
				.getFactoryMap());

		return mapping.findForward("addOrderFacdetail");
	}

	public ActionForward removeOrderFacdetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("allFacMapping", this.getOrderOutService()
				.getFactoryMap());

		return mapping.findForward("removeOrderFacdetail");
	}

	public ActionForward modifyOrderFacdetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("allFacMapping", this.getOrderOutService()
				.getFactoryMap());

		return mapping.findForward("modifyOrderFacdetail");
	}

	public ActionForward queryOrderFacToAdd(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		// String facIds = request.getParameter("facIds");// 单号
		//
		// StringBuffer queryString = new StringBuffer();
		// queryString.append(" where 1=1");
		//
		// if (facIds != null && !facIds.trim().equals("")) {
		// String temp = facIds.trim();
		// queryString.append(" and obj.id in ("
		// + temp.substring(0, temp.length() - 1) + ")");
		// } else {
		// queryString.append(" and 1=0");
		// }
		//
		// QueryInfo queryInfo = new QueryInfo();
		// // 设置每页显示多少行
		// int pageCount = 10;
		// // 得到limit对象
		// Limit limit = RequestUtils.getLimit(request, "facTable");
		// // 取得页面选择的每页显示条数
		// String pc = request.getParameter("facTable_crd");
		// if (pc != null) {
		// pageCount = Integer.parseInt(pc);
		// }
		//
		// // 设定每页显示记录数
		// queryInfo.setCountOnEachPage(pageCount);
		// // 设置查询记录总数语句
		// queryInfo.setCountQuery("select count(*) from CotOrderFac obj"
		// + queryString);
		// // 设置查询记录语句
		// queryInfo.setSelectString("from CotOrderFac obj");
		// // 设置条件语句
		// queryInfo.setQueryString(queryString.toString());
		// // 设置排序语句
		// queryInfo.setOrderString(" order by obj.id asc");
		//
		// // 取得排序信息
		// Sort sort = limit.getSort();
		// Map<?, ?> sortValueMap = sort.getSortValueMap();
		// if (sortValueMap != null && !sortValueMap.isEmpty()) {
		// // 设置排序语句
		// queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
		// sortValueMap));
		// }
		//
		// // 每次翻页不重新计算总行数
		// int totalRows = RequestUtils.getTotalRowsFromRequest(request);
		// if (totalRows < 0) {
		// totalRows = this.getOrderOutService().getRecordCount(queryInfo);
		// }
		// // 设置总行数和每页显示多少条
		// limit.setRowAttributes(totalRows, pageCount);
		// int startIndex = limit.getRowStart();
		// queryInfo.setStartIndex(startIndex);
		//
		// List<?> list = this.getOrderOutService().getList(queryInfo);
		//
		// request.setAttribute("cotorderfac", list);
		// request.setAttribute("facMapping", this.getOrderOutService()
		// .getFactoryMap());

		return mapping.findForward("queryOrderFacToAdd");
	}

	// 查询采购单
	public ActionForward queryOrderFacByFinace(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;

		String orderOutId = request.getParameter("orderOutId");// 出货单号
		String facIds = this.getOrderOutService().checkIsHasFacOrders(
				Integer.parseInt(orderOutId));

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (facIds != null && !facIds.trim().equals("")) {
			String temp = facIds.trim();
			queryString.append(" and obj.id in ("
					+ temp.substring(0, temp.length() - 1) + ")");
		} else {
			queryString.append(" and 1=0");
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderFac obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderFac obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		String excludes[] = { "cotOrderFacdetails", "orderMBImg" };
		queryInfo.setExcludes(excludes);

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询配件采购单
	public ActionForward queryFitOrderByFinace(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;

		String orderOutId = request.getParameter("orderOutId");// 出货单号
		String facIds = this.getOrderOutService().checkIsHasFitOrders(
				Integer.parseInt(orderOutId));

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (facIds != null && !facIds.trim().equals("")) {
			String temp = facIds.trim();
			queryString.append(" and obj.id in ("
					+ temp.substring(0, temp.length() - 1) + ")");
		} else {
			queryString.append(" and 1=0");
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFittingOrder obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotFittingOrder obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		String excludes[] = { "cotFittingsOrderdetails" };
		queryInfo.setExcludes(excludes);

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询包材采购单
	public ActionForward queryPackOrderByFinace(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;

		String orderOutId = request.getParameter("orderOutId");// 出货单号
		String facIds = this.getOrderOutService().checkIsHasPackOrders(
				Integer.parseInt(orderOutId));

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (facIds != null && !facIds.trim().equals("")) {
			String temp = facIds.trim();
			queryString.append(" and obj.id in ("
					+ temp.substring(0, temp.length() - 1) + ")");
		} else {
			queryString.append(" and 1=0");
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPackingOrder obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotPackingOrder obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		String excludes[] = { "cotPackingOrderdetails" };
		queryInfo.setExcludes(excludes);

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*---------------------------- 应付帐款操作部分 over--------------------------------------*/

	// 添加提醒消息
	public ActionForward addToMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addToMessage");
	}

	// 查询出货的应收款其他费用
	public ActionForward queryRecvOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("queryRecvOther");

		String fkId = request.getParameter("fkId");// 外键

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.type=0 and obj.source in ('orderout','orderRecv','orderOther','yi')");

		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceOther as obj"
				+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceOther AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存出货应收款其他费用
	public ActionForward addOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String orderNo = request.getParameter("mainNo");// 订单单号
		String fkId = request.getParameter("mainId");// 订单主单编号

		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinaceOther.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				String name = jsonObject.getString("finaceName");
				if (!"".equals(name)) {
					CotFinaceOther finaceOther = new CotFinaceOther();
					for (int i = 0; i < properties.length; i++) {
						BeanUtils.setProperty(finaceOther, properties[i],
								jsonObject.get(properties[i]));
					}
					finaceOther.setType(0);// 类型 0:应收 1：应付
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					finaceOther.setStatus(0);
					records.add(finaceOther);
				}
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					String name = jsonObject.getString("finaceName");
					if (!"".equals(name)) {
						CotFinaceOther finaceOther = new CotFinaceOther();
						for (int j = 0; j < properties.length; j++) {
							BeanUtils.setProperty(finaceOther, properties[j],
									jsonObject.get(properties[j]));
						}
						finaceOther.setType(0);// 类型 0:应收 1：应付
						finaceOther.setOrderNo(orderNo);
						finaceOther.setBusinessPerson(empId);
						finaceOther.setStatus(0);
						records.add(finaceOther);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 订单到出货的其他费用
		List amountReal = new ArrayList();

		// 订单到出货的应收帐
		List accReal = new ArrayList();

		// 收款记录到出货的溢收款
		List amountYi = new ArrayList();

		// 填充订单主单编号
		for (int i = 0; i < records.size(); i++) {
			CotFinaceOther cotFinaceOther = records.get(i);
			// 如果source没值时.设置为orderout
			if (cotFinaceOther.getSource() == null
					|| "".equals(cotFinaceOther.getSource())) {
				cotFinaceOther.setSource("orderout");
			}

			if (cotFinaceOther.getSource().equals("orderOther")
					&& cotFinaceOther.getOutFlag() != null) {
				amountReal.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("orderRecv")
					&& cotFinaceOther.getOutFlag() != null) {
				accReal.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("yi")
					&& cotFinaceOther.getOutFlag() != null) {
				amountYi.add(cotFinaceOther);
			}
			cotFinaceOther.setFkId(Integer.parseInt(fkId));
		}
		// 调用service更新记录
		this.getOrderOutService().addOtherList(request, records);
		// 更改订单其他费用
		if (amountReal.size() > 0) {
			this.getOrderOutService().updateOrderIsImport(request, amountReal);
		}
		// 更改订单应收帐
		if (accReal.size() > 0) {
			this.getOrderOutService().updateOrderRecvIsImport(request, accReal);
		}
		// 更改订单溢收款
		if (amountYi.size() > 0) {
			this.getOrderOutService().updateRecvRemain(request, amountYi);
		}
		return null;
	}

	// 修改出货应收款的其他费用
	public ActionForward modifyOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String orderNo = request.getParameter("orderNo");// 单号

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinaceOther.class);
		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFinaceOther finaceOther = this.getOrderOutService()
						.getFinaceOtherById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if (jsonObject.get(properties[i]) != null) {
						BeanUtils.setProperty(finaceOther, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				finaceOther.setOrderNo(orderNo);
				finaceOther.setBusinessPerson(empId);
				records.add(finaceOther);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotFinaceOther finaceOther = this.getOrderOutService()
							.getFinaceOtherById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if (jsonObject.get(properties[j]) != null) {
							BeanUtils.setProperty(finaceOther, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					records.add(finaceOther);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 订单到出货的其他费用
		List amountReal = new ArrayList();

		// 订单到出货的应收帐
		List accReal = new ArrayList();

		// 收款记录到出货的溢收款
		List amountYi = new ArrayList();

		// 填充订单主单编号
		for (int i = 0; i < records.size(); i++) {
			CotFinaceOther cotFinaceOther = records.get(i);
			if (cotFinaceOther.getSource().equals("orderOther")
					&& cotFinaceOther.getOutFlag() != null) {
				amountReal.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("orderRecv")
					&& cotFinaceOther.getOutFlag() != null) {
				accReal.add(cotFinaceOther);
			}
			if (cotFinaceOther.getSource().equals("yi")
					&& cotFinaceOther.getOutFlag() != null) {
				amountYi.add(cotFinaceOther);
			}
		}

		// 更改订单其他费用的导入标识
		if (amountReal.size() > 0) {
			this.getOrderOutService().updateOtherRemain(request, amountReal);
		}
		// 更改订单应收帐
		if (accReal.size() > 0) {
			this.getOrderOutService().updateRecvMod(request, accReal);
		}
		// 更改溢收款
		if (amountYi.size() > 0) {
			this.getOrderOutService().updateYiRemain(request, amountYi);
		}
		this.getOrderOutService().updateOtherList(request, records);
		return null;
	}

	// 删除
	public ActionForward removeOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// // 获取页面传入的所有recordKey的属性值
		// String[] recordKeys = request
		// .getParameterValues(TableConstants.RECORDKEY_NAME);
		// int[] resultCodes = new int[0];
		// String[] messages = null;
		// if (recordKeys != null) {
		// List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
		// for (int i = 0; i < recordKeys.length; i++) {
		// CotFinaceOther finaceOther = new CotFinaceOther();
		// finaceOther.setId(new Integer(recordKeys[i]));
		// list.add(finaceOther);
		//
		// }
		// this.getOrderOutService().deleteOtherList(list);
		// resultCodes = new int[recordKeys.length];
		// for (int i = 0; i < resultCodes.length; i++) {
		// resultCodes[i] = 1;
		// }
		// }
		// List<CotFinaceOther> list = new ArrayList<CotFinaceOther>();
		// // 获取页面传入的所有recordKey的属性值
		// String[] isImport = request.getParameterValues("outFlag");
		// for (int i = 0; i < isImport.length; i++) {
		// if (isImport != null) {
		// Integer temp = Integer.parseInt(isImport[i]);
		// if (temp != 0) {
		// CotFinaceOther cotFinaceOther = this.getOrderOutService()
		// .getFinaceOtherById(temp);
		// cotFinaceOther.setOutFlag(0);
		// list.add(cotFinaceOther);
		// }
		// }
		// }
		// // 修改订单的其他费用的导入标识为0
		// this.getOrderOutService().updateOtherList(list);
		//
		// try {
		// super.defaultAjaxResopnse("finaceOtherTable", recordKeys,
		// resultCodes, messages, request, response);
		// } catch (ServletException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return null;
	}

	// 查找订单的未冲完帐的应收帐
	public ActionForward queryOrderByFinace(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null) {
			return mapping.findForward("queryOther");
		}

		String orderNo = request.getParameter("orderNoFind");// 生产合同号
		String recvIds = request.getParameter("recvIds");// 订单主单编号字符串

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

		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNo.trim()
					+ "%'");
		}
		if (recvIds != null && !recvIds.trim().equals("")) {
			queryString.append(" and obj.id in ("
					+ recvIds.substring(0, recvIds.length() - 1) + ")");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// / 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrder as obj,"
				+ "CotEmps AS e,CotCustomer c" + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.orderNo,obj.id "
				+ "from CotOrder AS obj,CotEmps AS e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		int count = this.getOrderOutService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 根据起始
		List<?> list = this.getOrderOutService().getList(queryInfo);
		List<?> listVo = this.getOrderOutService().getOrderNoList(list);
		try {
			GridServerHandler gd = new GridServerHandler();
			gd.setData(listVo);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 查询订单的未生成应收帐的其他费用,并且未导到其他出货单
	public ActionForward queryOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null) {
			return mapping.findForward("queryOther");
		}

		String orderId = request.getParameter("orderId");// 订单主单编号字符串

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where 1=1 and obj.source='order' and obj.remainAmount>0 and (obj.outFlag is null or obj.outFlag=0)");

		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.fkId =" + orderId);
		} else {
			queryString.append(" and 1=0");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceOther obj"
				+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceOther obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询订单对应的未冲完帐的应收帐(过滤掉没冲过帐的预收货款)
	public ActionForward queryNoWanAccount(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOther");

		String fkId = request.getParameter("orderId");// 订单编号

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.source='order' and obj.zhRemainAmount>0");

		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinaceAccountrecv as obj"
						+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceAccountrecv AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		int count = this.getOrderOutService().getRecordCount(queryInfo);

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 根据起始
		List<?> list = this.getOrderOutService().getList(queryInfo);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotFinaceAccountrecv accountrecv = (CotFinaceAccountrecv) list
					.get(i);
			if (accountrecv.getFinaceName().equals("预收货款")) {
				if (accountrecv.getRealAmount() > 0) {
					listNew.add(accountrecv);
				} else {
					count--;
				}
			} else {
				listNew.add(accountrecv);
			}
		}

		try {
			GridServerHandler gd = new GridServerHandler();
			gd.setData(listNew);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 查询应收帐主单数据
	public ActionForward queryRecv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryRecv");

		String orderNo = request.getParameter("orderNo");// 单号
		String fkId = request.getParameter("fkId");// 外键
		String source = request.getParameter("source");// 金额源来
		String custId = request.getParameter("custId");// 客户

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.source='orderout'");

		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo='" + orderNo.trim() + "'");
		}
		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}
		if (source != null && !source.trim().equals("")) {
			queryString.append(" and obj.source='" + source.trim() + "'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId=" + custId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinaceAccountrecv as obj"
						+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceAccountrecv AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询应收帐主单数据
	public ActionForward queryRecvDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String recvId = request.getParameter("recvId");// 应收款ID

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where detail.finaceRecvid=p.id");

		if (recvId != null && !recvId.trim().equals("")) {
			queryString.append(" and detail.recvId=" + recvId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinacerecv p,CotFinacerecvDetail detail"
						+ queryString);
		// 查询语句
		queryInfo
				.setSelectString("select detail,p.finaceNo from CotFinacerecv p,CotFinacerecvDetail detail");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by detail.id desc");

		// 根据起始
		List<?> list = this.getOrderOutService().getList(queryInfo);

		try {
			GridServerHandler gd = new GridServerHandler();
			List res = this.getOrderOutService().getRecvDetailList(list);
			int count = this.getOrderOutService().getRecordCount(queryInfo);
			gd.setData(res);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询客户溢收款
	public ActionForward queryYiMoney(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null) {
			return mapping.findForward("queryOther");
		}

		String custId = request.getParameter("custId");// 客户

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where detail.factoryId=c.id and detail.fkId=f.id and detail.finaceName='溢收款' and detail.amount!=0 ");

		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and detail.factoryId=" + custId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinaceOther detail,CotCustomer c,CotFinacerecv f"
						+ queryString);
		// 查询语句
		queryInfo
				.setSelectString("select detail.id,detail.finaceName,detail.amount,detail.currencyId,c.customerShortName,f.finaceNo from CotFinaceOther detail,CotCustomer c,CotFinacerecv f");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by detail.id desc");

		int count = this.getOrderOutService().getRecordCount(queryInfo);

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 根据起始
		List<?> list = this.getOrderOutService().getList(queryInfo);
		List<?> listVo = this.getOrderOutService().getYiList(list);

		try {
			GridServerHandler gd = new GridServerHandler();
			gd.setData(listVo);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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

	// 跳转到excel导入界面
	public ActionForward showExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showExcel");
	}

	// 跳转到excel导入界面
	public ActionForward queryOrderOutNo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("orderDetail");
		String custId = request.getParameter("custId");
		String query = request.getParameter("query");// Ext下拉框模糊查询关键字
		String sql = " from CotOrderOut obj where 1=1 ";
		String sqlCount = " Select count(*) from CotOrderOut obj where 1=1 ";
		String condition = "";
		if (custId != null && !custId.equals("")) {
			condition += " and obj.custId=" + custId;
		}
		if (query != null && !query.trim().equals("")) {
			condition += " and obj.orderNo like'%" + query + "%'";
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
		queryInfo.setCountQuery(sqlCount + condition);
		// 查询语句
		queryInfo.setSelectString(sql);

		// 设置条件语句
		queryInfo.setQueryString(condition);
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		// 得到limit对象
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String excludes[] = { "cotOrderOutdetails", "cotOrderOuthsdetails",
				"cotSplitInfos", "cotHsInfos", "cotShipments", "cotSymbols",
				"cotOrderouthsRpt", "orderMBImg" };
		queryInfo.setExcludes(excludes);
		String json = "";
		try {
			json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
