/**
 * 
 */
package com.sail.cot.action.sample;

import java.io.IOException;
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
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.vo.CotEleIdCustNo;
import com.sail.cot.domain.vo.CotTreeNode;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.QueryService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

/**
 * 样品管理
 * 
 * @author qh-chzy
 * 
 */
public class CotElementsAction extends AbstractAction {

	private CotElementsService cotElementsService;
	private QueryService queryService;

	public CotElementsService getCotElementsService() {
		if (cotElementsService == null) {
			cotElementsService = (CotElementsService) super
					.getBean("CotElementsService");
		}
		return cotElementsService;
	}
	
	public QueryService getQueryService() {
		if (queryService == null) {
			queryService = (QueryService) super.getBean("QueryService");
		}
		return queryService;
	}

	// 跳转到样品添加页面
	public ActionForward addElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addElements");
	}
	
	// 双击主样品列表后跳转到样品编辑页面
	public ActionForward queryElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryElementsSuccess");
	}
	
	// 双击跳转到子样品编辑页面
	public ActionForward queryChild(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String flag = request.getParameter("flag");
		if(flag.equals("new")){
			return mapping.findForward("queryChild");
		}else{
			return mapping.findForward("queryChildMod");
		}
	}

	// 跳转到样品批量修改页面
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("modifyElements");
	}

	// 页面加载时调用,和根据条件查询时调用加载所有主样品
	public ActionForward queryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryListSuccess");
		
		String eleId = request.getParameter("eleIdFind");// 样品编号
		String child = request.getParameter("childFind");// 子货号标识
		String factoryNo = request.getParameter("factoryNoFind");// 厂号
		String eleName = request.getParameter("eleNameFind");// 中文名
		String eleNameEn = request.getParameter("eleNameEnFind");// 英文名
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String factoryId2 = request.getParameter("factoryIdFind2");// 厂家
		String eleCol = request.getParameter("eleColFind");// 颜色
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
		String eleTypeidLv12 = request.getParameter("eleTypeidLv1Find2");// 大类
		String eleTypeidLv2 = request.getParameter("eleTypeidLv2");// 中类
		String eleTypeidLv3 = request.getParameter("eleTypeidLv3");// 小类
		String eleHsid = request.getParameter("eleHsid");// 海关编码
		String eleGrade = request.getParameter("eleGradeFind");// 等级
		String eleDesc = request.getParameter("eleDescFind");// 产品描述
		String eleForPerson = request.getParameter("eleForPersonFind");// 开发对象
		String eleTypenameLv2 = request.getParameter("eleTypenameLv2");// 所属年份
		
		String boxLS = request.getParameter("boxLS");// 产品起始长
		String boxLE = request.getParameter("boxLE");// 产品结束长
		String boxWS = request.getParameter("boxWS");// 产品起始宽
		String boxWE = request.getParameter("boxWE");// 产品结束宽
		String boxHS = request.getParameter("boxHS");// 产品起始高
		String boxHE = request.getParameter("boxHE");// 产品结束高
		String eleSizeDesc = request.getParameter("eleSizeDesc");// 中文规格
		String eleInchDesc = request.getParameter("eleInchDesc");// 英文规格

		StringBuffer queryString = new StringBuffer();
		//只显示主货号和普通货号(eleFlag=2时为子货号)
		queryString.append(" where 1=1");
		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and ele.eleFlag=2");
		}else{
			queryString.append(" and (ele.eleFlag is null or ele.eleFlag!=2)");
		}
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (factoryNo != null && !factoryNo.toString().trim().equals("")) {
			queryString.append(" and ele.factoryNo like '%"
					+ factoryNo.toString().trim() + "%'");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and ele.eleName like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and ele.eleNameEn like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId.toString());
		}
		if (factoryId2 != null && !factoryId2.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId2.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and ele.eleCol like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1="
					+ eleTypeidLv1.toString());
		}
		if (eleTypeidLv12 != null && !eleTypeidLv12.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1="
					+ eleTypeidLv12.toString());
		}
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv2="
					+ eleTypeidLv2.toString());
		}
		if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv3="
					+ eleTypeidLv3.toString());
		}
		if (eleHsid != null && !eleHsid.toString().equals("")) {
			queryString.append(" and ele.eleHsid="
					+ eleHsid.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and ele.eleGrade like '%"
					+ eleGrade.toString().trim() + "%'");
		}
		if (eleDesc != null && !eleDesc.toString().equals("")) {
			queryString.append(" and ele.eleDesc like '%"
					+ eleDesc.toString().trim() + "%'");
		}
		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and ele.eleForPerson like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString
					.append(" and ele.eleAddTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and ele.eleAddTime <='" + endTime
					+ " 23:59:59'");
		}
		
		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
				queryString.append(" and ele.boxL between "
						+ boxLS + " and " + boxLE);
			}
		}
		
		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
				queryString.append(" and ele.boxW between "
						+ boxWS + " and " + boxWE);
			}
		}
		
		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
				queryString.append(" and ele.boxH between "
						+ boxHS + " and " + boxHE);
			}
		}
		if (eleTypenameLv2 != null && !eleTypenameLv2.toString().equals("")) {
			queryString.append(" and ele.eleTypenameLv2=" + eleTypenameLv2.toString());
		}
		
		if (eleSizeDesc != null && !eleSizeDesc.toString().equals("")) {
			queryString.append(" and ele.eleSizeDesc like '%"
					+ eleSizeDesc.toString().trim() + "%'");
		}
		if (eleInchDesc != null && !eleInchDesc.toString().equals("")) {
			queryString.append(" and ele.eleInchDesc like '%"
					+ eleInchDesc.toString().trim() + "%'");
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotElementsNew ele"
						+ queryString);
		String sql = "from CotElementsNew ele";

		queryInfo.setSelectString(sql);
		// 设置查询参数
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by ele.id desc");
		int totalRows = this.getCotElementsService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotElementsService().getList(queryInfo);
		//判断是否有权限查看样品图片
		this.getQueryService().findIsSelPic(request, list);
		//判断是否有权限查看报价信息
		try {
			List<?> listNew = this.getQueryService().findIsSelPrice(request, list);
			String excludes[] = {"cotPictures","childs","cotFile","cotPriceFacs","cotEleFittings",
					"cotElePrice","cotElePacking"};
			GridServerHandler gd = new GridServerHandler(excludes);
			gd.setData(listNew);
			gd.setTotalCount(totalRows);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String queryType = "factory";
		queryType = request.getParameter("type");
		if (queryType == null)
			queryType = "factory";
		List<CotTreeNode> list = null;
		list = this.getCotElementsService().getQueryCondition(queryType);
		String excludes[] = {"map","parentMap"};
		GridServerHandler gd = new GridServerHandler(excludes);
		gd.setData(list);
		gd.setTotalCount(list.size());
		JSONObject jsonObj = gd.getLoadResponseJSON();
		JSONArray jsonArray = (JSONArray)jsonObj.get("data");
		System.out.println("sdfsdfs:"+jsonArray.toString());
		try {
			response.getWriter().write(jsonArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//加载样品报价记录
	public ActionForward loadPriceInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("loadPriceInfo");
		String priceNoFind = request.getParameter("priceNoFind");// 报价单号
		String eleId = request.getParameter("eleId");// 货号
		String bussinessPerson = request.getParameter("businessPersonFind");
		String custId = request.getParameter("custId");// 客户ID
		String cId = request.getParameter("cId");// 客户ID
		String startTime = request.getParameter("startTime");// 报价起始日期
		String endTime = request.getParameter("endTime");// 报价结束日期
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.priceId = p.id and p.custId = c.id");
		
		if (priceNoFind != null && !priceNoFind.trim().equals("")) {
			queryString.append(" and p.priceNo like '%" + priceNoFind.trim()
					+ "%'");
		}
		
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and p.custId =" + custId.trim());
		}
		if (cId != null && !cId.trim().equals("")) {
			queryString.append(" and p.custId =" + cId.trim());
		}
		if (startTime!=null && !"".equals(startTime.trim())) {
			queryString.append(" and p.priceTime >='" + startTime + "'");
		}
		if (endTime!=null && !"".equals(endTime.trim())) {
			queryString.append(" and p.priceTime <='" + endTime
					+ " 23:59:59'");
		}
		if(null!=eleId && !"".equals(eleId))
		{
			eleId=eleId.trim();
			queryString.append(" and obj.eleId='"+eleId+"'"); 
		}
		if(null!=bussinessPerson && !"".equals(bussinessPerson))
		{
			bussinessPerson = bussinessPerson.trim();
			queryString.append(" and p.businessPerson = "+bussinessPerson); 
		}
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPriceDetail obj, CotPrice p,CotCustomer c" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select p.id,p.priceTime,p.priceNo,p.empId,c.customerShortName,"
									+"obj.priceOut,p.currencyId,p.clauseId,p.businessPerson,obj.pricePrice, " 
									+"obj.boxTypeId,p.validMonths,p.priceCompose,p.priceRate,obj.priceFac,obj.priceFacUint,p.priceStatus "
									+"from CotPriceDetail obj, CotPrice p,CotCustomer c");
		// 设置条件语句
		
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		// 取得排序信息
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		//List<?> list = this.getCotElementsService().getPriceList(queryInfo);
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		List res = this.getCotElementsService().getPriceList(queryInfo);
		int count = this.getCotElementsService().getRecordCount(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//加载样品订单记录
	public ActionForward loadOrderInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("loadOrderInfo");
		
		String orderNoFind = request.getParameter("orderNoFind");// 报价单号
		String eleId = request.getParameter("eleId");// 货号
		String bussinessPerson = request.getParameter("businessPersonFind");
		String b = request.getParameter("businessPerson");
		String custId = request.getParameter("custId");// 客户
		String cId = request.getParameter("cId");// 客户
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId = o.id and o.custId = c.id");
		
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and o.orderNo like '%" + orderNoFind.trim()
					+ "%'");
		}
		
		if(null!=eleId && !"".equals(eleId))
		{
			eleId=eleId.trim();
			queryString.append(" and obj.eleId='"+eleId+"'"); 
		}
		if(null!=bussinessPerson && !"".equals(bussinessPerson))
		{
			bussinessPerson = bussinessPerson.trim();
			queryString.append(" and o.bussinessPerson = "+bussinessPerson); 
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and o.custId=" + custId.trim());
		}
		if (cId != null && !cId.trim().equals("")) {
			queryString.append(" and o.custId=" + cId.trim());
		}
		if (startTime!=null && !"".equals(startTime.trim())) {
			queryString.append(" and o.orderTime >='" + startTime + "'");
		}
		if (endTime!=null && !"".equals(endTime.trim())) {
			queryString.append(" and o.orderTime <='" + endTime
					+ " 23:59:59'");
		}
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount  = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderDetail obj, CotOrder o,CotCustomer c" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select o.id,o.orderTime,o.totalCount,obj.totalMoney," +
				" o.orderNo,o.empId,c.customerShortName,obj.priceFac,obj.priceFacUint," +
				" obj.priceOut,obj.priceOutUint,o.bussinessPerson,obj.orderPrice,o.currencyId,obj.boxCount, " 
				+"obj.boxTypeId,o.orderCompose,o.orderStatus "
				+"from CotOrderDetail obj, CotOrder o,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		List res = this.getCotElementsService().getOrderList(queryInfo);
		int count = this.getCotElementsService().getRecordCount(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//加载样品征样记录
	public ActionForward loadSignInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("loadSignInfo");
		// 设置查询参数
		StringBuffer queryString = new StringBuffer();
		String eleId = request.getParameter("eleId");// 货号
		String opNo = request.getParameter("signNoFind");// 单号
		String factoryId = request.getParameter("factoryId");// 厂家
		String businessPerson = request.getParameter("bussinessPerson");// 业务员
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
		if(null!=eleId && !"".equals(eleId)){
			eleId=eleId.trim();
			queryString.append(" and obj.eleId='"+eleId+"'"); 
		}
		if (opNo != null && !opNo.toString().trim().equals("")) {
			queryString.append(" and s.signNo like '%"
					+ opNo.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and s.factoryId=" + factoryId.toString());
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
		queryInfo.setSelectString("select s.id," 
				+ "s.signTime,"
				+ "s.signNo,"
				+ "e.empsName, "
				+ "c.customerShortName," 
				+ "s.factoryId, " 
				+ "s.requireTime,"
				+ "s.arriveTime"
				+ " from CotGivenDetail obj,CotGiven p,CotSign s,CotEmps e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by s.signTime desc");
		int count = this.getQueryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getSignVoByEle(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//加载样品送样记录
	public ActionForward loadGivenInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("loadGivenInfo"); 
		String eleId = request.getParameter("eleId");// 货号
		String givenNoFind = request.getParameter("givenNoFind");
		String bussinessPerson = request.getParameter("businessPersonFind");
		String custId = request.getParameter("custId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.givenId = g.id and g.custId = c.id ");
		
		if (givenNoFind != null && !givenNoFind.trim().equals("")) {
			queryString.append(" and g.givenNo like '%" + givenNoFind.trim()
					+ "%'");
		}
		
		if(null!=eleId && !"".equals(eleId))
		{
			eleId=eleId.trim();
			queryString.append(" and obj.eleId='"+eleId+"'"); 
		}
		if(null!=bussinessPerson && !"".equals(bussinessPerson))
		{
			bussinessPerson = bussinessPerson.trim();
			queryString.append(" and g.bussinessPerson = "+bussinessPerson); 
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and g.custId ="+custId.trim());
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and g.givenTime >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and g.givenTime <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and g.givenTime between '"+startTime+"' and '"+endTime+"'");
		}
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotGivenDetail obj, CotGiven g,CotCustomer c" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select g.id,g.givenTime,g.givenNo,g.bussinessPerson,c.customerShortName,"
				+ " obj.factoryId,obj.signRequire,g.realGiventime,g.givenAddr from CotGivenDetail obj, CotGiven g,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		//List<?> list = this.getCotElementsService().getGivenList(queryInfo);
		 
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		List res = this.getCotElementsService().getGivenList(queryInfo);
		int count = this.getCotElementsService().getRecordCount(queryInfo);
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
	
	//加载样品客号信息
	public ActionForward loadCustNoInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("loadCustNoInfo");
		
		 
		String eleId = request.getParameter("eleId");// 货号
		String custId = request.getParameter("custId"); 
		
		CotEleIdCustNo queryCondition = new CotEleIdCustNo(); 
		if (eleId != null && !eleId.trim().equals("")) {
			queryCondition.setEleId(eleId);
		}
		if (custId != null && !custId.trim().equals("")) {
			queryCondition.setCustId(custId);
		}
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		 
		int startIndex = Integer.parseInt(start);	
		
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		List res = this.getCotElementsService().getCotEleIdCustNoList(startIndex, pageCount, queryCondition);
		int count = this.getCotElementsService().findCotEleIdCustNoCount(queryCondition);
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
	
	public ActionForward showOtherPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showOtherPic");
	}

	//浏览样品其他图片
	public ActionForward showPicture(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String eId = request.getParameter("eId");
		
		if(start == null || limit == null)
			return mapping.findForward("showPicture");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (eId != null
				&& !eId.equals("")) {
			queryString.append(" and obj.eleId = " + eId);
		}
		QueryInfo queryInfo = new QueryInfo();
		String sql = "FROM CotPicture obj";
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		queryInfo.setCountQuery("select count(*) from CotPicture obj " + queryString.toString());
		
		int pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String excludes[] = {"picImg"};
			queryInfo.setExcludes(excludes);
			String json =  this.getCotElementsService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	// 加载主样品的子样品
	public ActionForward loadChildInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("loadChildInfo");
		
		String mainId = request.getParameter("mainId");// 主货号编号
		StringBuffer queryString = new StringBuffer();
		//只显示子货号
		queryString.append(" where ele.eleFlag=2");
		if (mainId != null && !mainId.toString().equals("")) {
			queryString.append(" and ele.eleParentId=" + mainId.toString());
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotElementsNew ele"
						+ queryString);
		String sql = "from CotElementsNew ele";

		queryInfo.setSelectString(sql);
		// 设置查询参数
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by ele.id desc");
		int totalRows = this.getCotElementsService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotElementsService().getList(queryInfo);
		//判断是否有权限查看样品图片
		this.getQueryService().findIsSelPic(request, list);
		//判断是否有权限查看报价信息
		try {
			List<?> listNew = this.getQueryService().findIsSelPrice(request, list);
			String excludes[] = {"cotPictures","childs","cotFile","cotPriceFacs","cotEleFittings",
					"cotElePrice","cotElePacking"};
			GridServerHandler gd = new GridServerHandler(excludes);
			gd.setData(listNew);
			gd.setTotalCount(totalRows);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//用于查询子货号配件 和 父货号配件
	public ActionForward loadFittingInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String fitNo = request.getParameter("fnFind");// 材料编号
		String facId = request.getParameter("facIdFind");// 供应商
		String flag = request.getParameter("Flag");//标识子货号还是父货号
		String eleId = request.getParameter("mainId");//父货号ID
		String childId = request.getParameter("childId");
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("loadFittingInfo");
		
		StringBuffer queryString=new StringBuffer();
		queryString.append(" where 1=1");
		
		QueryInfo queryInfo = new QueryInfo();
		
		if (fitNo != null && !fitNo.trim().equals("")) {
			queryString.append(" and obj.fitNo like '%" + fitNo.trim()+"%'");
		}
		if (facId != null && !facId.trim().equals("")) {
			queryString.append(" and obj.facId=" + facId.trim());
		}
		
		if ("child".equals(flag)) {
			if (childId != null && !childId.toString().equals("")) {
				queryString.append(" and obj.eleChildId is not null and obj.eleChildId="+ childId);
			}
		}
		
		if (eleId != null && !eleId.toString().equals("")) {
			queryString.append(" and obj.eleId="+ eleId.toString());
		}		
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotEleFittings obj"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotEleFittings obj");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotElementsService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//添加配件明细
	@SuppressWarnings("deprecation")
	public ActionForward addEleFitting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String json = request.getParameter("data");
		String id = request.getParameter("eId");//可能是子ID 也可能是父ID,看flag
		String eleId = request.getParameter("mainId");//当子货号时才存储这个父ID
		String flag = request.getParameter("Flag");//parent或child
		String eleNo = request.getParameter("eleNo");//子货号货号,在flag=child才存储
		
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if(single){
			JSONObject jsonObject = JSONObject.fromObject(json); 
			CotEleFittings obj  = (CotEleFittings)jsonObject.toBean(jsonObject, CotEleFittings.class);
			if(obj.getFitNo().trim().equals("")){
				return null;
			}
			if (id != null) {
				obj.setEleChildId(Integer.parseInt(id));
			}
//			if (obj.getId() == 0) {
//				obj.setId(null);
//			}
			if("child".equals(flag)){
				obj.setEleChildId(Integer.parseInt(id));
				obj.setEleChild(eleNo);
				obj.setEleId(Integer.parseInt(eleId));
			}else{
				obj.setEleId(Integer.parseInt(id));
				obj.setEleChildId(null);
			}
//			if (obj.getFitUsedCount() != null && obj.getFitCount()!= null && obj.getFitPrice()!= null) {
//				obj.setFitTotalPrice(obj.getFitUsedCount()* obj.getFitCount()*obj.getFitPrice());
//			}
			List list = new ArrayList();
			list.add(obj);
			//保存
			this.getCotElementsService().addEleFitting(list);
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			List list = jarray.toList(jarray, CotEleFittings.class);
			List listNew = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				CotEleFittings cotEleFittings = (CotEleFittings)list.get(i);
				if (id != null) {
					cotEleFittings.setEleChildId(Integer.parseInt(id));
				}
				if (cotEleFittings.getId()!=null && cotEleFittings.getId() == 0) {
					cotEleFittings.setId(null);
				}
				if("child".equals(flag)){
					cotEleFittings.setEleChildId(Integer.parseInt(id));
					cotEleFittings.setEleChild(eleNo);
					cotEleFittings.setEleId(Integer.parseInt(eleId));
				}else{
					cotEleFittings.setEleId(Integer.parseInt(id));
					cotEleFittings.setEleChildId(null);
				}
//				if (cotEleFittings.getFitUsedCount() != null && cotEleFittings.getFitCount()!= null && cotEleFittings.getFitPrice()!= null) {
//					cotEleFittings.setFitTotalPrice(cotEleFittings.getFitUsedCount()* cotEleFittings.getFitCount()*cotEleFittings.getFitPrice());
//				}
				if(!cotEleFittings.getFitNo().trim().equals("")){
					listNew.add(cotEleFittings);
				}
			}
			if(listNew.size()>0){
				//保存
				this.getCotElementsService().addEleFitting(listNew);
			}
		}
		return null;
	}
	
	//修改配件明细
	public ActionForward modifyEleFitting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String json = request.getParameter("data");
		List list = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if(single){
			JSONObject jsonObject = JSONObject.fromObject(json); 
			CotEleFittings obj  = (CotEleFittings)jsonObject.toBean(jsonObject, CotEleFittings.class);
			list = new ArrayList();
			list.add(obj);
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, CotEleFittings.class);
		}
		//保存
		this.getCotElementsService().modifyEleFitting(list);
		return null;
	}	
	
	//删除配件明细
	@SuppressWarnings("deprecation")
	public ActionForward removeEleFitting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
//		String eleId = request.getParameter("eleId");
//		String flag = request.getParameter("Flag");
//		String eleNo = request.getParameter("eleNo");
//		
//		String[] properties = ReflectionUtils.getDeclaredFields(CotEleFittings.class);
//		String[] recordKeys = request.getParameterValues(TableConstants.RECORDKEY_NAME);
//		List<CotEleFittings> records = new ArrayList<CotEleFittings>();
//		
//		for (int i = 0; i < recordKeys.length; i++) {
//			CotEleFittings eleFittings = this.getCotElementsService().getEleFittingById(new Integer(recordKeys[i]));
//			records.add(eleFittings);
//		}
//		// 设置对象属性信息
//		for (int i = 0; i < properties.length; i++) {
//			if (properties[i].equals("op") || properties[i].equals("chk")) {
//				break;
//			}
//			if (properties[i].equals("fitNo")) {
//				String[] values = request.getParameterValues(properties[i]);
//				if (values != null) {
//					for (int j = 0; j < values.length; j++) {
//						try {
//							BeanUtils.setProperty(records.get(j),properties[i], values[j]);
//						} catch (IllegalAccessException e) {
//							e.printStackTrace();
//						} catch (InvocationTargetException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//		// 更新数据
//		int[] resultCodes = new int[records.size()];
//		for (int i = 0; i < resultCodes.length; i++) {
//			resultCodes[i] = 1;
//		}
//		String[] messages = null;
//
//		List<Integer> ids = new ArrayList<Integer>();
//		List<String> finaceNos = new ArrayList<String>();
//		
//		HttpSession session = request.getSession();
//		
//		for (int i = 0; i < records.size(); i++) {
//			CotEleFittings eleFittings = (CotEleFittings) records.get(i);
//			ids.add(eleFittings.getId());
//			finaceNos.add(eleFittings.getFitNo());
//			
//			String fitNo = eleFittings.getFitNo();
//			
//			//在Action中清除Map中fitNo对应的映射
//			this.getCotElementsService().delFitMapByKeyAction(fitNo, session);
//		}
//		this.getCotElementsService().deleteEleFittingByIds(ids);
//		
////		if ("child".equals(flag)) {
////			//更新子货号配件成本
////			this.getCotElementsService().modifyChildFitPrice(Integer.parseInt(eleId),eleNo);
////		}
////		//修改父货号配件成本
////		this.getCotElementsService().modifyParentFitPrice(Integer.parseInt(eleId));
//
//		try {
//			super.defaultAjaxResopnse("eleFittingTable", recordKeys, resultCodes,messages, request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	//用于查询包材成本
//	public ActionForward loadPackingInfo(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		
//		
//		String eleId = request.getParameter("mainId");//父货号ID
//
//		StringBuffer queryString=new StringBuffer();
//		queryString.append(" where 1=1");
//
//		
//		if (eleId != null && !eleId.toString().equals("")) {
//			queryString.append(" and obj.eleId="+ eleId.toString());
//		}
//		
//		//设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		//设置每页显示多少行
//		int pageCount =20;
//		//取得页面选择的每页显示条数
//		String pc = request.getParameter("elePackingTable_crd");
//		if(pc!=null){
//			pageCount = Integer.parseInt(pc);
//		}
//		//设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotElePacking obj"+ queryString);
//		// 查询语句
//		queryInfo.setSelectString("select obj from CotElePacking obj");
//		
//		 
//		//设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		//设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		//得到limit对象
//		Limit limit=RequestUtils.getLimit(request,"elePackingTable");
//		//取得排序信息
//		Sort sort=limit.getSort();
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			//设置排序语句
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getCotElementsService().getRecordCount(queryInfo);
//		}
//		//设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		//根据起始
//		List<?> list = this.getCotElementsService().getList(queryInfo);
//		
//		request.setAttribute("cotElePacking", list);
//		request.setAttribute("allFactoryName", this.getCotElementsService().getPackingFacMap());
	    return mapping.findForward("loadPackingInfo");
	}
	//加载样品采购记录
	public ActionForward loadOrderFacInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("loadOrderFacInfo");
		
		String eleId = request.getParameter("eleId");// 货号
		String businessPerson = request.getParameter("businessPersonFind");
		String orderNoFind = request.getParameter("orderNoFind"); //发票编号
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId = o.id");
		
		if(null!=eleId && !"".equals(eleId))
		{
			eleId=eleId.trim();
			queryString.append(" and obj.eleId='"+eleId+"'"); 
		}
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and o.orderNo like '%" + orderNoFind.trim() + "%'");
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and o.businessPerson=" + businessPerson.trim());
		}
		if (startTime!=null && !"".equals(startTime.trim())) {
			queryString.append(" and o.orderTime >='" + startTime + "'");
		}
		if (endTime!=null && !"".equals(endTime.trim())) {
			queryString.append(" and o.orderTime <='" + endTime + " 23:59:59'");
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
		queryInfo.setCountQuery("select count(*) from CotOrderFacdetail obj, CotOrderFac o" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select o.id,o.orderTime,o.factoryId," +
				" o.orderNo,o.businessPerson,obj.priceFac,obj.priceFacUint,obj.totalFac,obj.boxCount, " 
				+"o.orderStatus,obj.boxTypeId "
				+"from CotOrderFacdetail obj, CotOrderFac o");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by o.id desc");
		// 取得排序信息
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		List res = this.getCotElementsService().getOrderFacList(queryInfo);
		int count = this.getCotElementsService().getRecordCount(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	//修改图片名称
	public ActionForward modifyPics(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 前台传递来的数据
		String json = request.getParameter("data");
		List<CotPicture> records = new ArrayList<CotPicture>();
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			String picName = jsonObject.getString("picName");
			Integer pId=jsonObject.getInt("id");
			CotPicture picture=this.getCotElementsService().getPicById(pId);
			picture.setPicName(picName);
			records.add(picture);
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				String picName = jsonObject.getString("picName");
				Integer pId=jsonObject.getInt("id");
				CotPicture picture=this.getCotElementsService().getPicById(pId);
				picture.setPicName(picName);
				records.add(picture);
			}
		}
		this.getCotElementsService().updateOtherPic(records);
		return null;
	}
	
}
