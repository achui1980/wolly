package com.sail.cot.action.sign;
 
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 

import net.sf.json.JSONArray;
import net.sf.json.JSONObject; 
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.sail.cot.action.AbstractAction;  
import com.sail.cot.domain.CotEmps; 
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.given.CotGivenService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.sign.CotSignService; 
import com.sail.cot.util.SystemUtil; 

public class CotSignAction extends AbstractAction {
	
	private CotSignService cotSignService;
	  
	public CotSignService getCotSignService() {
		if(cotSignService==null){
			cotSignService = (CotSignService) super.getBean("CotSignService");
		}
		return cotSignService;
	}

	public void setCotSignService(CotSignService cotSignService) {
		this.cotSignService = cotSignService;
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
	
	private CotElementsService cotElementsService;
	
	public CotElementsService getCotElementsService() {
		if (cotElementsService==null) {
			cotElementsService = (CotElementsService) super.getBean("CotElementsService");
		}
		return cotElementsService;
	}
	
	public void setCotElementsService(CotElementsService cotElementsService){
		this.cotElementsService = cotElementsService;
	}

	

	@Override
	//进入添加征样明细页面
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		 request.setAttribute("facMapping", this.getCotSignService().getFacMap());
		 request.setAttribute("typeMapping", this.getCotSignService().getTypeMap());
		 request.setAttribute("boxTypeMapping", this.getCotGivenService().getBoxTypeMap());
		 return mapping.findForward("add");
	}

	@Override
	//进入修改征样明细页面
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("facMapping", this.getCotSignService().getFacMap());
		request.setAttribute("typeMapping", this.getCotSignService().getTypeMap());
		request.setAttribute("boxTypeMapping", this.getCotGivenService().getBoxTypeMap());
		// 查询该单的所有征样明细的产品货号
		String id = request.getParameter("id");// 主征样单号
		String eleIds = this.getCotSignService().findEleBySignId(Integer.parseInt(id));
		request.setAttribute("eleIds", eleIds);
		return mapping.findForward("modify");
	}
	
	// 跳转到征样明细编辑页面
	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("detail");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	@Override
	//查询主征样单信息
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		String signNo = request.getParameter("signNoFind");
		String factoryId = request.getParameter("factoryId");
		
		String givenId = request.getParameter("givenId");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");		 

		StringBuffer sql=new StringBuffer();
		sql.append(" where 1=1");
		
		if(givenId!=null && !givenId.toString().equals("")){
			sql.append(" and obj.givenId ="+givenId.toString());
		}else {
			return null;
		}
		
		if (signNo != null && !signNo.trim().equals("")) {
			sql.append(" and obj.signNo like '%" + signNo.trim() + "%'");
		}
	
		if (factoryId != null && !factoryId.trim().equals("")) {
			sql.append(" and obj.factoryId ="+factoryId.trim());
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotSign obj"+sql);
		//设置查询记录语句
		queryInfo.setSelectString("from CotSign obj"); 
		//设置查询条件语句
		queryInfo.setQueryString(sql.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotGivenService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	 
	// 查询征样明细
	public ActionForward querySignDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String flag = request.getParameter("flag");//用于区分是详细页面,添加页面还是编辑页面调用
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward(flag);

		StringBuffer queryString = new StringBuffer();
		
		queryString.append(" where 1=1 and obj.signFlag=1");
		
//		String signId = request.getParameter("sId");// 主征样单号
		
		String givenId = request.getParameter("givenId");//主送样单号
		String factoryId = request.getParameter("factoryId");//厂家
		
		if (givenId == null || "".equals(givenId)) {
			return null;
		}else {
			queryString.append(" and obj.givenId=" + givenId);
		}

		if (factoryId == null || "".equals(factoryId)) {
			return null;
		}else {
			queryString.append(" and obj.factoryId=" + factoryId);
		}
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		
		String [] filter = {"cotGivenDetails","cotSign"};
		queryInfo.setExcludes(filter);
		
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotGivenDetail obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotGivenDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		List<?> list = this.getCotGivenService().getList(queryInfo);
		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		// 清空内存map
		//session.removeAttribute("SessionPRICE");
		// 将明细存到内存中的map
		Map<?, ?> facMap = this.getCotGivenService().getFacMap();
		for (int i = 0; i < list.size(); i++) {
			CotGivenDetail cotGivenDetail = (CotGivenDetail) list.get(i);
			cotGivenDetail.setPicImg(null);
			if(cotGivenDetail.getFactoryId()!=null){
				cotGivenDetail.setFactoryShortName(facMap.get(cotGivenDetail.getFactoryId().toString()).toString());
			}
			String eleNo = cotGivenDetail.getEleId();
			this.getCotGivenService().setGivenMapAction(session,eleNo.toLowerCase(), cotGivenDetail);
		}

		try {
			String json = this.getCotGivenService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	//添加征样明细
	@SuppressWarnings("deprecation")
	public ActionForward addSignDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CotEmps cotEmps = (CotEmps) request.getSession().getAttribute("emp");
		//内存数据
		HttpSession session = request.getSession();
		Map<String, CotGivenDetail> givenMap = this.getCotGivenService()
				.getGivenMapAction(session);
		if (givenMap == null) {
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
			try {
				response.getWriter().write(error.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		
		String gId = request.getParameter("givenPrimId");
		String factoryId = request.getParameter("factoryId");
		String customerId = request.getParameter("custId");
		Integer custId = Integer.parseInt(customerId);

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object eleId = jsonObject.get("eleId");
			if(eleId!=null && !"".equals(eleId.toString())){
				CotGivenDetail cotGivenDetail = givenMap.get(eleId.toString().toLowerCase());
				cotGivenDetail.setGivenId(Integer.parseInt(gId));
				cotGivenDetail.setFactoryId(Integer.parseInt(factoryId));//工厂
				cotGivenDetail.setSignFlag(1);
				cotGivenDetail.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
				cotGivenDetail.setAddPerson(cotEmps.getEmpsName());// 操作人
				records.add(cotGivenDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object eleId = jsonObject.get("eleId");
				if(eleId!=null && !"".equals(eleId.toString())){
					CotGivenDetail cotGivenDetail = givenMap.get(eleId.toString().toLowerCase());
					cotGivenDetail.setGivenId(Integer.parseInt(gId));
					cotGivenDetail.setFactoryId(Integer.parseInt(factoryId));//工厂
					cotGivenDetail.setSignFlag(1);
					cotGivenDetail.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
					cotGivenDetail.setAddPerson(cotEmps.getEmpsName());// 操作人
					records.add(cotGivenDetail);
				}
			}
		}

		// 保存
		this.getCotGivenService().addGivenDetails(records);

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "given";
		while (it.hasNext()) {
			CotGivenDetail detail = (CotGivenDetail) it.next();
			String eleId = detail.getEleId();
			String custNo = detail.getCustNo();
			String eleNameEn = detail.getEleNameEn();

			if (custNo != null && !custNo.trim().equals("")) {
				elecustMap.put(eleId, custNo);
			}
			if (eleNameEn != null && !eleNameEn.trim().equals("")) {
				elenameenMap.put(eleId, eleNameEn);
			}
		}
		if (elecustMap.size() != 0 && elenameenMap.size() != 0) {
			this.getCotElementsService().saveEleCustNoByCustList(elecustMap,
					elenameenMap, custId, type);
		}
		
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
	
	//删除征样明细
	@SuppressWarnings("deprecation")
	public ActionForward removeSignDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
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
		
		for(int i=0;i<ids.size();i++){
			CotGivenDetail givenDetail = this.getCotGivenService().getGivenDetailById((Integer) ids.get(i));
			givenDetail.setSignFlag(0);
			records.add(givenDetail);
		}
		this.getCotGivenService().addGivenDetails(records);
		
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
	
	//修改征样明细
	public ActionForward modifySignDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		//内存数据
		HttpSession session = request.getSession();
		Map<String, CotGivenDetail> givenMap = this.getCotGivenService().getGivenMapAction(session);
		if (givenMap == null) {
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
			try {
				response.getWriter().write(error.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		String customerId = request.getParameter("custId");
		Integer custId = Integer.parseInt(customerId);

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object eleId = jsonObject.get("eleId");
			if(eleId!=null && !"".equals(eleId.toString())){
				CotGivenDetail cotGivenDetail = givenMap.get(eleId.toString().toLowerCase());
				//cotGivenDetail.setSignCount(cotGivenDetail.getGivenCount());
				records.add(cotGivenDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object eleId = jsonObject.get("eleId");
				if(eleId!=null && !"".equals(eleId.toString())){
					CotGivenDetail cotGivenDetail = givenMap.get(eleId.toString().toLowerCase());
					//cotGivenDetail.setSignCount(cotGivenDetail.getGivenCount());
					records.add(cotGivenDetail);
				}
			}
		}

		// 保存
		this.getCotGivenService().modifyGivenDetail(records);

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "given";
		while (it.hasNext()) {
			CotGivenDetail detail = (CotGivenDetail) it.next();
			String eleId = detail.getEleId();
			String custNo = detail.getCustNo();
			String eleNameEn = detail.getEleNameEn();

			if (custNo != null && !custNo.trim().equals("")) {
				elecustMap.put(eleId, custNo);
			}
			if (eleNameEn != null && !eleNameEn.trim().equals("")) {
				elenameenMap.put(eleId, eleNameEn);
			}
		}
		if (elecustMap.size() != 0 && elenameenMap.size() != 0) {
			this.getCotElementsService().saveEleCustNoByCustList(elecustMap,
					elenameenMap, custId, type);
		}
		
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
	
	// 查询报价单数据
	public ActionForward queryPrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String custId = request.getParameter("customerId");// 客户ID
//		String priceNoFind = request.getParameter("priceNoFind");// 报价单号
//		String fullNameCn = request.getParameter("fullNameCn");// 客户
//		String startTime = request.getParameter("startTime");// 报价起始日期
//		String endTime = request.getParameter("endTime");// 报价结束日期
//		StringBuffer queryString = new StringBuffer();
//
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if ("admin".equals(emp.getEmpsId())) {
//			queryString.append(" where obj.custId = c.id and obj.bussinessPerson=e.id");
//		} else {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotprice.do", "ALL");
//			if (all == true) {
//				queryString
//						.append(" where obj.custId = c.id and obj.bussinessPerson=e.id");
//			} else {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotprice.do",
//						"DEPT");
//				if (dept == true) {
//					queryString
//							.append(" where obj.custId = c.id and obj.bussinessPerson = e.id and e.deptId="
//									+ emp.getDeptId());
//				} else {
//					queryString
//							.append(" where obj.custId = c.id and obj.bussinessPerson = e.id and obj.bussinessPerson ="
//									+ emp.getId());
//				}
//			}
//		}
//		if (custId != null && !custId.trim().equals("")) {
//			queryString.append(" and obj.custId =" + custId.trim());
//		}
//		if (priceNoFind != null && !priceNoFind.trim().equals("")) {
//			queryString.append(" and obj.priceNo like '%" + priceNoFind.trim()
//					+ "%'");
//		}
//		if (fullNameCn != null && !fullNameCn.trim().equals("")) {
//			queryString.append(" and c.fullNameCn like '%" + fullNameCn.trim()
//					+ "%'");
//		}
//		if (startTime != null && endTime != null) {
//			if (!"".equals(startTime.trim()) && "".equals(endTime.trim())) {
//				queryString.append(" and obj.priceTime >'" + startTime + "'");
//			}
//			if (!"".equals(endTime.trim()) && "".equals(startTime.trim())) {
//				queryString.append(" and obj.priceTime <'" + endTime
//						+ " 23:59:59'");
//			}
//			if (!"".equals(startTime.trim()) && !"".equals(endTime.trim())) {
//				queryString.append(" and obj.priceTime between '" + startTime
//						+ "' and '" + endTime + " 23:59:59'");
//			}
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 5;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("priceTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//				.setCountQuery("select count(*) from CotPrice obj,CotCustomer c,CotEmps e"
//						+ queryString);
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj.id,obj.priceNo,c.customerNo,"
//						+ "c.fullNameCn,obj.clauseId,obj.currencyId,"
//						+ "obj.commisionScale,obj.priceProfit,"
//						+ "obj.priceTime,obj.addPerson from CotPrice obj,CotCustomer c,CotEmps e");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "priceTable");
//		// 取得排序信息
//		Sort sort = limit.getSort();
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			// 设置排序语句
//			for (Iterator<?> itor = sortValueMap.keySet().iterator(); itor
//					.hasNext();) {
//				String field = (String) itor.next();
//				String ord = (String) sortValueMap.get(field);
//				if (field.equals("customerNo") || field.equals("fullNameCn")) {
//					queryString.append(" ORDER BY ").append("c").append(".")
//							.append(field).append(" ").append(ord);
//				} else {
//					queryInfo.setOrderString(SystemUtil.getDefaultSortSQL(
//							"obj", sortValueMap));
//				}
//				break;
//			}
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getCotSignService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getCotSignService().getPriceList(queryInfo);
//		request.setAttribute("cotprice", list);
//		request.setAttribute("allCusName", this.getCotSignService().getCusNameMap());
		return mapping.findForward("queryPrice");
	}
	
	// 查询该报价单的报价明细产品的信息
	public ActionForward queryPriceDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//		String priceId = request.getParameter("priceId");// 主报价单号
//		String eleId = request.getParameter("eleIdFind");// 样品编号
//		String eleName = request.getParameter("eleNameFind");// 中文名
//		String factoryId = request.getParameter("factoryIdFind");// 厂家
//		String eleCol = request.getParameter("eleColFind");// 颜色
//		String typeId = request.getParameter("eleTypeidLv1Find");// 大类
//
//		if (priceId == null || "".equals(priceId)) {
//			return null;
//		}
//
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where obj.priceId=" + priceId);
//		if (eleId != null && !eleId.toString().trim().equals("")) {
//			queryString.append(" and obj.eleId like '%"
//					+ eleId.toString().trim() + "%'");
//		}
//		if (eleName != null && !eleName.toString().trim().equals("")) {
//			queryString.append(" and obj.eleName like '%"
//					+ eleName.toString().trim() + "%'");
//		}
//		if (factoryId != null && !factoryId.toString().equals("")) {
//			queryString.append(" and obj.factoryId=" + factoryId.toString());
//		}
//		if (eleCol != null && !eleCol.toString().trim().equals("")) {
//			queryString.append(" and obj.eleCol like '%"
//					+ eleCol.toString().trim() + "%'");
//		}
//		if (typeId != null && !typeId.toString().equals("")) {
//			queryString.append(" and obj.typeId=" + typeId.toString());
//		}
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 5;
//
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "orderPriceDetail");
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("orderPriceDetail_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotPriceDetail obj"
//				+ queryString);
//		// 设置查询记录语句
//		queryInfo.setSelectString("from CotPriceDetail obj");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//
//		// 取得排序信息
//		Sort sort = limit.getSort();
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			// 设置排序语句
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getCotSignService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//
//		List<?> list = this.getCotSignService().getList(queryInfo);
//		request.setAttribute("detail", list);
//		request.setAttribute("allCurrencyName", this.getCotSignService().getCurrencyMap());
		return mapping.findForward("queryPrice");
	}
	
	// 查询该报价单的报价明细产品的信息
	public ActionForward queryEleFrame(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		return mapping.findForward("queryEleFrame");
	}
	
	//从征样单添加提醒消息
	public ActionForward addToMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("addToMessage");
	}
	
	
}
