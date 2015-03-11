package com.sail.cot.action.given;
 
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
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.given.CotGivenService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

public class CotGivenAction extends AbstractAction {

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
	
	// 跳转到excel导入界面
	public ActionForward showExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showExcel");
	}
	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		request.setAttribute("facMapping", this.getCotGivenService().getFacMap());
		request.setAttribute("typeMapping", this.getCotGivenService().getTypeMap());
		request.setAttribute("boxTypeMapping", this.getCotGivenService().getBoxTypeMap());
		// 查询该单的所有送样明细的产品货号
		String id = request.getParameter("id");// 主送样单号
		String eleIds = this.getCotGivenService().findEleByGivenId(Integer.parseInt(id));
		request.setAttribute("eleIds", eleIds);
		return mapping.findForward("modify");
	}

	// 跳转到送样明细编辑页面
	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("detail");
	}
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		String flag = request.getParameter("flag");//用于客户页面调用
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		String givenNo=request.getParameter("givenNoFind");//单号
		String custId = request.getParameter("custId");
		String bussinessPerson = request.getParameter("bussinessPerson");
		String startTime = request.getParameter("startTime");// 起始日期
		String endTime = request.getParameter("endTime");// 结束日期
		
		if (start == null || limit == null){
			if ("customerPage".equals(flag)) {
				return mapping.findForward("queryGiven");
			}else {
				return mapping.findForward("querySuccess");
			}
		}

		StringBuffer sql=new StringBuffer();
		sql.append(" where obj.bussinessPerson=e.id and obj.custId=c.id");
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotgiven.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门征样的权限
				boolean dept = SystemUtil.isAction(request, "cotgiven.do","DEPT");		
				if (dept == true) {
					sql .append(" and e.deptId=" + emp.getDeptId());
				} else {
					sql .append(" and obj.bussinessPerson =" + emp.getId());
				}
			}
		}		
		
		if(givenNo != null && !givenNo.trim().equals(""))
		{
			givenNo = givenNo.trim();
			sql.append(" and obj.givenNo like '%"+givenNo+"%'");
		}
		if (custId != null && !custId.trim().equals("")) {
			sql.append(" and obj.custId ="+custId.trim());
		}
		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
			sql.append(" and obj.bussinessPerson="+bussinessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			sql.append(" and obj.givenTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			sql.append(" and obj.givenTime <='" + endTime
					+ " 23:59:59'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"cotGivenDetails","cotSign","customers"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotGiven obj,CotEmps e,CotCustomer c"+sql);
		//设置查询记录语句
		queryInfo.setSelectString("select obj.id, obj.givenNo," +
				"obj.givenTime," +
				"c.customerShortName," +
				"obj.custId," +
				"e.empsName," +
				"obj.custRequiretime," +
				"obj.checkComplete," +
				"obj.realGiventime," +
				"obj.givenStatus," +
				"obj.givenAddr, "+
				"obj.givenIscheck "+
				" from CotGiven obj,CotEmps e,CotCustomer c"); 
		//设置查询条件语句
		queryInfo.setQueryString(sql.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		
		int count = this.getCotGivenService().getRecordCount(queryInfo);
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotGivenService().getGivenVOList(queryInfo);
		
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

	public ActionForward queryCustomer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String customerNo = request.getParameter("customerNoFind");// 客户编号
//		String fullNameCn = request.getParameter("fullNameCnFind");// 客户中文名
//		// 获得登录人的empId
//		Integer empId = (Integer) request.getSession().getAttribute("empId");
//		String empNo = (String) request.getSession().getAttribute("empNo");
//
//		StringBuffer queryString = new StringBuffer();
//		if (!"admin".equals(empNo)) {
//			queryString.append(" where obj.empId =" + empId);
//		} else {
//			queryString.append(" where 1=1");
//		}
//		if (customerNo != null && !customerNo.toString().trim().equals("")) {
//			queryString.append(" and obj.customerNo like '%"
//					+ customerNo.toString().trim() + "%'");
//		}
//		if (fullNameCn != null && !fullNameCn.toString().trim().equals("")) {
//			queryString.append(" and obj.fullNameCn like '%"
//					+ fullNameCn.toString().trim() + "%'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 5;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("cusTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotCustomer obj"
//				+ queryString);
//		// 查询语句
//		queryInfo.setSelectString("from CotCustomer obj");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "cusTable");
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
//			totalRows = this.getCotGivenService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getCotGivenService().getList(queryInfo);
//		request.setAttribute("customerList", list);
//		request.setAttribute("allEmpsName", this.getCotGivenService().getEmpsMap());
		return mapping.findForward("queryCustomer");
	}
	
	public ActionForward queryFactory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
//		Limit limit=RequestUtils.getLimit(request);
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		//Map<?, ?> filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//		
//		StringBuffer sql=new StringBuffer();
//		String factoryNo = request.getParameter("facNo");
//		String shortName = request.getParameter("facShortName");
//		
//		boolean temp1 = false;
//		temp1 = org.apache.commons.lang.StringUtils.isBlank(factoryNo);
//		if(factoryNo!=null && !factoryNo.trim().equals("")&&temp1==false){
//			sql.append(" and obj.factoryNo like '%"+factoryNo.trim()+"%'");
//		}
//		boolean temp2 = false;
//		temp2 = org.apache.commons.lang.StringUtils.isBlank(shortName);
//		if(shortName!=null && !shortName.trim().equals("")&&temp2==false){
//			sql.append(" and obj.shortName like '%"+shortName.trim()+"%'");
//		}
//		System.out.println(sql.toString()); 
//		
//		QueryInfo queryInfo = new QueryInfo();
//	
//		queryInfo.setCountOnEachPage(15);
//		queryInfo.setCountQuery("select count(*) from CotFactory obj where 1=1"+sql.toString());
//		queryInfo.setSelectString("from CotFactory obj where 1=1");
//		queryInfo.setQueryString(sql.toString());
//		queryInfo.setOrderString("");
//		int totalCount = this.getCotGivenService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		
//		request.setAttribute("cotFactory", this.getCotGivenService().getList(queryInfo));
		return mapping.findForward("queryFactory");
	}
	
	// 查询该送样单的送样明细产品的信息
	public ActionForward queryGivenDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String flag = request.getParameter("flag");//用于区分是详细页面,添加页面还是编辑页面调用
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward(flag);

		StringBuffer queryString = new StringBuffer();
		String givenId = request.getParameter("pId");// 主报价单号
		if (givenId == null || "".equals(givenId)) {
			return null;
		}

		queryString.append(" where obj.givenId=" + givenId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
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
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.id asc");

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
	
	//添加送样明细
	@SuppressWarnings("deprecation")
	public ActionForward addGivenDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
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
		List<CotGivenPic> imgList = new ArrayList<CotGivenPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		byte[] zwtpByte = this.getCotElementsService().getZwtpPic();
		String gId = request.getParameter("givenPrimId");
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
				// 设置序列号
				Integer sortNo = (Integer) jsonObject.get("sortNo");
				cotGivenDetail.setSortNo(sortNo);
				cotGivenDetail.setSignFlag(0);
				records.add(cotGivenDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object eleId = jsonObject.get("eleId");
				if(eleId!=null && !"".equals(eleId.toString())){
					CotGivenDetail cotGivenDetail = givenMap.get(eleId.toString().toLowerCase());
					// 设置序列号
					Integer sortNo = (Integer) jsonObject.get("sortNo");
					cotGivenDetail.setSortNo(sortNo);
					cotGivenDetail.setSignFlag(0);
					records.add(cotGivenDetail);
				}
			}
		}

		for (int i = 0; i < records.size(); i++) {
			CotGivenDetail detail = records.get(i);
			if (detail == null) {
				continue;
			}
			detail.setGivenId(Integer.parseInt(gId));
			detail.setEleAddTime(new Date(System.currentTimeMillis()));// 添加时间
			//新建图片
			CotGivenPic givenPic = new CotGivenPic();
			if (detail.getType().equals("ele")) {
				CotElePic cotElePic = impOpService.getElePicImgByEleId(detail
						.getSrcId());
				detail.setPicName(cotElePic.getEleId());
				givenPic.setEleId(cotElePic.getEleId());
				givenPic.setPicImg(cotElePic.getPicImg());
				givenPic.setPicSize(cotElePic.getPicImg().length);
				givenPic.setPicName(cotElePic.getEleId());
			}
			if (detail.getType().equals("price")) {
				CotPricePic cotPricePic = impOpService.getPricePic(detail
						.getSrcId());
				detail.setPicName(cotPricePic.getEleId());
				givenPic.setEleId(cotPricePic.getEleId());
				givenPic.setPicImg(cotPricePic.getPicImg());
				givenPic.setPicSize(cotPricePic.getPicImg().length);
				givenPic.setPicName(cotPricePic.getEleId());
			}
			if (detail.getType().equals("order")) {
				CotOrderPic cotOrderPic = impOpService.getOrderPic(detail
						.getSrcId());
				detail.setPicName(cotOrderPic.getEleId());
				givenPic.setEleId(cotOrderPic.getEleId());
				givenPic.setPicImg(cotOrderPic.getPicImg());
				givenPic.setPicSize(cotOrderPic.getPicImg().length);
				givenPic.setPicName(cotOrderPic.getEleId());
			}
			if (detail.getType().equals("given")) {
				CotGivenPic cotGivenPic = impOpService.getGivenPic(detail
						.getSrcId());
				
				// 新增的这行 引用的图片刚好是同个表格被删掉的行,先执行删除,已经删掉图片
				if (cotGivenPic != null) {
					detail.setPicName(cotGivenPic.getEleId());
					givenPic.setEleId(cotGivenPic.getEleId());
					givenPic.setPicImg(cotGivenPic.getPicImg());
					givenPic.setPicSize(cotGivenPic.getPicImg().length);
					givenPic.setPicName(cotGivenPic.getEleId());
				} else {
					detail.setPicName(detail.getEleId());
					givenPic.setEleId(detail.getEleId());
					givenPic.setPicImg(zwtpByte);
					givenPic.setPicSize(zwtpByte.length);
					givenPic.setPicName(detail.getEleId());
				}
			}
			if (detail.getType().equals("none")) {
				detail.setPicName(detail.getEleId());
				givenPic.setEleId(detail.getEleId());
				givenPic.setPicImg(zwtpByte);
				givenPic.setPicSize(zwtpByte.length);
				givenPic.setPicName(detail.getEleId());
			}
			// 添加到图片数组
			imgList.add(givenPic);
		}

		// 保存
		this.getCotGivenService().addGivenDetails(records);
		// 保存报价图片
		for (int i = 0; i < records.size(); i++) {
			CotGivenDetail detail = (CotGivenDetail) records.get(i);
			CotGivenPic cotGivenPic = (CotGivenPic) imgList.get(i);
			cotGivenPic.setFkId(detail.getId());
			List picList = new ArrayList();
			picList.add(cotGivenPic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(picList);
		}

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
	
	//删除送样明细
	@SuppressWarnings("deprecation")
	public ActionForward removeGivenDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

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
		
		this.getCotGivenService().deleteDetailByIds(request,ids);
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
	
	//修改送样明细
	public ActionForward modifyGivenDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		//内存数据
		HttpSession session = request.getSession();
		
		Map<String, CotGivenDetail> givenMap = this.getCotGivenService().getGivenMapAction(session);
		if (givenMap == null) {
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败,session丢失");
			try {
				response.getWriter().write(error.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		String customerId = request.getParameter("custId");
		String gId = request.getParameter("givenPrimId");
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
				cotGivenDetail.setSignCount(cotGivenDetail.getGivenCount());
				
				records.add(cotGivenDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object eleId = jsonObject.get("eleId");
				if(eleId!=null && !"".equals(eleId.toString())){
					CotGivenDetail cotGivenDetail = givenMap.get(eleId.toString().toLowerCase());
					cotGivenDetail.setSignCount(cotGivenDetail.getGivenCount());
					
					records.add(cotGivenDetail);
				}
			}
		}

		// 保存
		this.getCotGivenService().modifyGivenDetail(records);
		
		Integer res = this.getCotGivenService().getChangeNum(Integer.parseInt(gId));
		if (res != 0) {
			this.getCotGivenService().modifyGivenStatus(Integer.parseInt(gId), "new");
		}

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
//			queryString.append(" where obj.custId = c.id and obj.empId=e.id");
//		} else {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotprice.do", "ALL");
//			if (all == true) {
//				queryString
//						.append(" where obj.custId = c.id and obj.empId=e.id");
//			} else {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotprice.do",
//						"DEPT");
//				if (dept == true) {
//					queryString
//							.append(" where obj.custId = c.id and obj.empId = e.id and e.deptId="
//									+ emp.getDeptId());
//				} else {
//					queryString
//							.append(" where obj.custId = c.id and obj.empId = e.id and obj.empId ="
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
//			totalRows = this.getCotGivenService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getCotGivenService().getPriceList(queryInfo);
//		request.setAttribute("cotprice", list);
//		request.setAttribute("allCusName", this.getCotGivenService()
//				.getCustMap());
		return mapping.findForward("queryPrice");
	}
	// 查询所有样品
	public ActionForward queryElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String eleId = request.getParameter("eleIdFind");// 样品编号
//		String eleName = request.getParameter("eleNameFind");// 中文名
//		String factoryId = request.getParameter("factoryIdFind");// 厂家
//		String eleCol = request.getParameter("eleColFind");// 颜色
//		String startTime = request.getParameter("startTime");// 起始时间
//		String endTime = request.getParameter("endTime");// 结束时间
//		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
//		String eleGrade = request.getParameter("eleGradeFind");// 等级
//		String eleForPerson = request.getParameter("eleForPersonFind");// 开发对象
//		
//		String boxLS = request.getParameter("boxLS");// 产品起始长
//		String boxLE = request.getParameter("boxLE");// 产品结束长
//		String boxWS = request.getParameter("boxWS");// 产品起始宽
//		String boxWE = request.getParameter("boxWE");// 产品结束宽
//		String boxHS = request.getParameter("boxHS");// 产品起始高
//		String boxHE = request.getParameter("boxHE");// 产品结束高
//
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where 1=1");
//		if (eleId != null && !eleId.toString().trim().equals("")) {
//			queryString.append(" and ele.eleId like '%"
//					+ eleId.toString().trim() + "%'");
//		}
//		if (eleName != null && !eleName.toString().trim().equals("")) {
//			queryString.append(" and ele.eleName like '%"
//					+ eleName.toString().trim() + "%'");
//		}
//		if (factoryId != null && !factoryId.toString().equals("")) {
//			queryString.append(" and ele.factoryId=" + factoryId.toString());
//		}
//		if (eleCol != null && !eleCol.toString().trim().equals("")) {
//			queryString.append(" and ele.eleCol like '%"
//					+ eleCol.toString().trim() + "%'");
//		}
//		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
//			queryString.append(" and ele.eleTypeidLv1="
//					+ eleTypeidLv1.toString());
//		}
//		if (eleGrade != null && !eleGrade.toString().equals("")) {
//			queryString.append(" and ele.eleGrade like '%"
//					+ eleGrade.toString().trim() + "%'");
//		}
//		
//		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
//			queryString.append(" and ele.eleForPerson like '%"
//					+ eleForPerson.toString().trim() + "%'");
//		}
//
//		if (startTime != null && endTime != null) {
//			if (!"".equals(startTime.trim()) && "".equals(endTime.trim())) {
//				queryString
//						.append(" and ele.eleProTime >'" + startTime + "'");
//			}
//			if (!"".equals(endTime.trim()) && "".equals(startTime.trim())) {
//				queryString.append(" and ele.eleProTime <'" + endTime
//						+ " 23:59:59'");
//			}
//			if (!"".equals(startTime.trim()) && !"".equals(endTime.trim())) {
//				queryString.append(" and ele.eleProTime between '"
//						+ startTime + "' and '" + endTime + " 23:59:59'");
//			}
//		}
//		
//		if (boxLS != null && boxLE != null) {
//			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
//				queryString.append(" and ele.boxL between "
//						+ boxLS + " and " + boxLE);
//			}
//		}
//		
//		if (boxWS != null && boxWE != null) {
//			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
//				queryString.append(" and ele.boxW between "
//						+ boxWS + " and " + boxWE);
//			}
//		}
//		
//		if (boxHS != null && boxHE != null) {
//			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
//				queryString.append(" and ele.boxH between "
//						+ boxHS + " and " + boxHE);
//			}
//		}
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("elementTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//				.setCountQuery("select count(*) from CotElementsNew ele"
//						+ queryString);
//		String sql = "from CotElementsNew ele";
//
//		queryInfo.setSelectString(sql);
//		// 设置查询参数
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by ele.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "elementTable");
//		// 取得排序信息
//		Sort sort = limit.getSort();
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		queryInfo.setOrderString(SystemUtil.getDefaultSortSQL(
//				"ele", sortValueMap));
//		
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getCotGivenService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getCotGivenService().getList(queryInfo);
//		request.setAttribute("elementList", list);
//		request.setAttribute("allFactoryName", this.getCotGivenService().getFacMap());
		return mapping.findForward("queryEle");
	}
	// 查询该送样单的送样明细产品的信息
	public ActionForward queryEleFrame(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		return mapping.findForward("queryEleFrame");
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
//		Limit limit = RequestUtils.getLimit(request, "givenPriceDetail");
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("givenPriceDetail_crd");
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
//			totalRows = this.getCotGivenService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//
//		List<?> list = this.getCotGivenService().getList(queryInfo);
//		request.setAttribute("detail", list);
//		request.setAttribute("allCurrencyName", this.getCotGivenService()
//				.getCurrencyMap());
		return mapping.findForward("queryPrice");
	}
	// 查询报价单数据
	public ActionForward queryOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String orderNoFind = request.getParameter("orderNoFind");// 订单单号
//		String custId = request.getParameter("custId");// 客户
//		String bussinessPerson = request.getParameter("businessPerson");//业务员
//		String startTime = request.getParameter("startTime");// 下单起始日期
//		String endTime = request.getParameter("endTime");// 下单结束日期
//		StringBuffer queryString = new StringBuffer();
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if ("admin".equals(emp.getEmpsId())) {
//			queryString.append(" where obj.bussinessPerson=e.id");
//		} else {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
//			if (all == true) {
//				queryString
//						.append(" where obj.bussinessPerson=e.id");
//			} else {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotorder.do",
//						"DEPT");
//				if (dept == true) {
//					queryString
//							.append(" where obj.bussinessPerson = e.id and e.deptId="
//									+ emp.getDeptId());
//				} else {
//					queryString
//							.append(" where obj.bussinessPerson = e.id and obj.bussinessPerson ="
//									+ emp.getId());
//				}
//			}
//		}
//
//		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
//			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim()
//					+ "%'");
//		}
//		if (custId != null && !custId.trim().equals("")) {
//			queryString.append(" and obj.custId=" + custId.trim());
//		}
//		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
//			queryString.append(" and obj.bussinessPerson=" + bussinessPerson.trim());
//		}
//		if (startTime!=null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.orderTime >='" + startTime + "'");
//		}
//		if (endTime!=null && !"".equals(endTime.trim())) {
//			queryString.append(" and obj.orderTime <='" + endTime
//					+ " 23:59:59'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("orderTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//		.setCountQuery("select count(*) from CotOrder obj,CotEmps e"
//				+ queryString);
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj from CotOrder obj,CotEmps e");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "orderTable");
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
//			totalRows = this.getCotGivenService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getCotGivenService().getList(queryInfo);
//		request.setAttribute("cotorder", list);
//		request.setAttribute("allCusShortName", this.getCotGivenService().getCustMap());
//		request.setAttribute("allEmpsName", this.getCotGivenService().getEmpsMap());
//		request.setAttribute("allCurrencyName", this.getCotGivenService().getCurrencyMap());
		return mapping.findForward("queryOrder");
	}
	// 查询该订单单的订单明细产品的信息
	public ActionForward queryOrderDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

//		String eleId = request.getParameter("eleIdFind");
//		String eleName = request.getParameter("eleNameFind");
//		String flag = request.getParameter("flag");// 用于区分是详细页面,添加页面还是编辑页面调用
//		String orderId = request.getParameter("orderId");// 主订单单号
//
//		StringBuffer queryString = new StringBuffer();
//		
//		if (orderId == null || "".equals(orderId)) {
//			return null;
//		}
//		if (eleId != null && !eleId.toString().trim().equals("")) {
//			queryString.append(" and obj.eleId like '%"
//					+ eleId.toString().trim() + "%'");
//		}
//		if (eleName != null && !eleName.toString().trim().equals("")) {
//			queryString.append(" and obj.eleName like '%"
//					+ eleName.toString().trim() + "%'");
//		}
//		queryString.append(" where obj.orderId=" + orderId);
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, flag);
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter(flag + "_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotOrderDetail obj"
//				+ queryString);
//		// 设置查询记录语句
//		queryInfo.setSelectString("from CotOrderDetail obj");
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
//			totalRows = this.getCotGivenService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//
//		List<?> list = this.getCotGivenService().getList(queryInfo);
//		request.setAttribute("detail", list);
//		request.setAttribute("allTypeName", this.getCotGivenService()
//				.getTypeMap());
//		request.setAttribute("allFactoryName", this.getCotGivenService()
//				.getFacMap());

		// 查询该单的所有订单明细的产品货号
//		String eleIds = this.getOrderService().findEleByOrderId(
//				Integer.parseInt(orderId));
//		request.setAttribute("eleIds", eleIds);

		return mapping.findForward("queryOrder");
	}
	
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	//从送样单添加提醒消息
	public ActionForward addToMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("addToMessage");
	}
	// 查询应收帐主单数据
	public ActionForward queryRecv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryRecv");
		String orderNo = request.getParameter("orderNo");// 单号
		String fkId = request.getParameter("fkId");// 外键
		String source = request.getParameter("source");// 金额源来
		String custId = request.getParameter("custId");// 客户

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

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
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
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
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		try{
			String json = this.getCotGivenService().getJsonData(queryInfo);
	
			response.getWriter().write(json);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询收款记录数据
	public ActionForward queryRecvDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryRecv");
		
		String recvId = request.getParameter("recvId");// 应收款ID

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where detail.finaceRecvid=p.id");

		if (recvId != null && !recvId.trim().equals("")) {
			queryString.append(" and detail.recvId=" + recvId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		/// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设定每页显示记录数
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
		
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotGivenService().getList(queryInfo);
		List<?> listVo = this.getCotGivenService().getRecvDetailList(list);
		int totalCount = this.getCotGivenService().getRecordCount(queryInfo);
		GridServerHandler gd = new GridServerHandler();
		gd.setData(listVo);
		gd.setTotalCount(totalCount);
		String json = gd.getLoadResponseText();
		try {
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
