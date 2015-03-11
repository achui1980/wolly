package com.sail.cot.action.split;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotSplitDetail;
import com.sail.cot.domain.CotSplitInfo;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.orderout.CotOrderOutService;
import com.sail.cot.service.split.CotSplitService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;
public class CotSplitAction extends AbstractAction {

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

	@Override
	
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");	
		
		String orderId = request.getParameter("orderId");// 主发票号		
		
		String containerNo = request.getParameter("splitNoFind");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		StringBuffer queryString = new StringBuffer();
		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		queryString.append(" where obj.orderOutId=" + orderId);
		//不显示admin用户
		if(containerNo!=null && !containerNo.trim().equals("")){
			queryString.append(" and obj.containerNo  like '%"+containerNo.trim()+"%'");
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.splitDate >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.splitDate <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.splitDate between '"+startTime+"' and '"+endTime+"'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"cotSplitDetails"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotSplitInfo obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotSplitInfo obj"); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.splitDate asc");
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

	public ActionForward querySplitDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String flag = request.getParameter("flag");// 用于区分是详细页面,添加页面还是编辑页面调用
		
		if(start == null || limit == null)
			return mapping.findForward(flag);	
		
		String splitId = request.getParameter("splitId");// 主排载单号
		if (splitId == null || "".equals(splitId)) {
			return null;
		}
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.splitId=" + splitId);

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotSplitDetail obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotSplitDetail obj"); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		List<?> list = this.getCotSplitService().getList(queryInfo);		
		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		if (flag.equals("splitDetail")) {
			for (int i = 0; i < list.size(); i++) {
				CotSplitDetail cotSplitdetail = (CotSplitDetail) list.get(i);
				Integer orderDetailId = cotSplitdetail.getOrderDetailId();
				this.getCotSplitService().setMapAction(session,orderDetailId, cotSplitdetail);
			}
		}		
		try {
			String json = this.getOrderOutService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	// 查询出货单数据
	public ActionForward queryOrderOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
	
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		String custNo=request.getParameter("custId");//单号
		
		if(start == null || limit == null)
			return mapping.findForward("queryOrderOut");		 

		StringBuffer sql=new StringBuffer();
		
		//最高权限
		boolean all = SystemUtil.isAction(request, "cotgiven.do", "ALL");
		//部门权限
		boolean dept = SystemUtil.isAction(request, "cotgiven.do","DEPT");		
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			sql.append(" where obj.businessPerson=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				sql.append(" where obj.businessPerson=e.id");
			} else {
				// 判断是否有查看该部门征样的权限
				if (dept == true) {
					sql .append(" where obj.businessPerson = e.id and e.deptId=" + emp.getDeptId());
				} else {
					sql .append(" where obj.businessPerson = e.id and obj.businessPerson =" + emp.getId());
				}
			}
		}		
		
		if(custNo != null && !custNo.trim().equals(""))
		{
			custNo = custNo.trim();
			sql.append(" and obj.custId like '%"+custNo+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"cotHsInfos","cotOrderOutdetails","cotOrderOuthsdetails",
				"cotOrderouthsRpt","cotShipments","cotSplitInfos","cotSymbols","orderMBImg"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderOut obj,CotEmps e"+sql);
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotOrderOut obj,CotEmps e"); 
		//设置查询条件语句
		queryInfo.setQueryString(sql.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.orderTime desc");
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
	// 查询出货单数据
	public ActionForward queryOrderForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String orderNoFind = request.getParameter("orderNoFind");// 订单单号
//		String startTime = request.getParameter("startTime");
//		String endTime = request.getParameter("endTime");
//		
//		StringBuffer queryString = new StringBuffer();
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if ("admin".equals(emp.getEmpsId())) {
//			queryString.append(" where obj.businessPerson=e.id and obj.splitFlag <> 1");
//		} else {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotorderout.do", "ALL");
//			if (all == true) {
//				queryString
//						.append(" where obj.businessPerson=e.id and obj.splitFlag <> 1");
//			} else {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotorderout.do",
//						"DEPT");
//				if (dept == true) {
//					queryString
//							.append(" where obj.businessPerson = e.id and e.deptId="
//									+ emp.getDeptId()+" and obj.splitFlag <> 1");
//				} else {
//					queryString
//							.append(" where obj.businessPerson = e.id and obj.businessPerson ="
//									+ emp.getId()+" and obj.splitFlag <> 1");
//				}
//			}
//		}
//
//		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
//			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim()
//					+ "%'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
//			queryString.append(" and obj.orderTime >='"+startTime+"'");
//		}
//		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.orderTime <='"+endTime+"'");
//		}
//		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
//			queryString.append(" and obj.orderTime between '"+startTime+"' and '"+endTime+"'");
//		}
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("orderOutTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//		.setCountQuery("select count(*) from CotOrderOut obj,CotEmps e"
//				+ queryString);
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj from CotOrderOut obj,CotEmps e");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "orderOutTable");
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
//			totalRows = this.getOrderOutService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getOrderOutService().getList(queryInfo);
//		request.setAttribute("cotorderout", list);
		
		return mapping.findForward("queryOrderForm");
	}
	// 查询该出货单的明细产品的信息
	public ActionForward queryOrderDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return null;
	
		String orderId = request.getParameter("orderId");// 主送样单号

		StringBuffer queryString = new StringBuffer();
		
		if (orderId == null || "".equals(orderId)) {
			return null;
		}else {
			queryString.append(" where obj.orderId=" + orderId + " and obj.remainBoxCount > 0");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		
		int pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderOutdetail obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderOutdetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		
		int count = this.getOrderOutService().getRecordCount(queryInfo);
		List<?> list = this.getOrderOutService().getList(queryInfo);
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
	//添加排载主单
	public ActionForward addSplit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List<CotSplitInfo> records = new ArrayList<CotSplitInfo>();
		
		String orderOutId = request.getParameter("orderOutId");

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			CotSplitInfo splitInfo = new CotSplitInfo();
			splitInfo.setContainerCube(Double.parseDouble(jsonObject.get("containerCube").toString()));
			splitInfo.setContainerNo(String.valueOf(jsonObject.get("containerNo")));
			splitInfo.setContainerType(String.valueOf(jsonObject.get("containerType")));
			splitInfo.setLabelNo(String.valueOf(jsonObject.get("labelNo")));
			splitInfo.setOrderOutId(Integer.parseInt(jsonObject.get("orderOutId").toString()));
			//splitInfo.setSplitDate((Date)(jsonObject.get("containerNo")));
			splitInfo.setSplitRemark(String.valueOf(jsonObject.get("splitRemark")));
			records.add(splitInfo);
			this.getCotSplitService().saveOrUpdateSplit(splitInfo, String.valueOf(jsonObject.get("splitDate")));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				CotSplitInfo splitInfo = new CotSplitInfo();
				splitInfo.setContainerCube(Double.parseDouble(jsonObject.get("containerCube").toString()));
				splitInfo.setContainerType(String.valueOf(jsonObject.get("containerType")));
				splitInfo.setLabelNo(String.valueOf(jsonObject.get("labelNo")));
				splitInfo.setOrderOutId(Integer.parseInt(jsonObject.get("orderOutId").toString()));
				splitInfo.setContainerNo(String.valueOf(jsonObject.get("containerNo")));
				splitInfo.setSplitRemark(String.valueOf(jsonObject.get("splitRemark")));
				this.getCotSplitService().saveOrUpdateSplit(splitInfo, String.valueOf(jsonObject.get("splitDate")));
			}
		}
		return null;
	}
	public ActionForward modifySplit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List<CotSplitInfo> records = new ArrayList<CotSplitInfo>();
		
		String orderOutId = request.getParameter("orderOutId");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(java.util.Date.class,  new JsDateJsonBeanProcessor());

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json,jsonConfig);
			String date = jsonObject.get("splitDate").toString();
			if(jsonObject.get("splitDate").toString().startsWith("{")){
				JSONObject json1 = JSONObject.fromObject(jsonObject.get("splitDate"));
				int year = Integer.parseInt(json1.getString("year"));
				int month = Integer.parseInt(json1.getString("month"));
				int day = Integer.parseInt(json1.getString("day"));
				Date date1 = new Date(year-1900,month,day);
				date = date1.toLocaleString();
			}
			
			CotSplitInfo splitInfo = new CotSplitInfo();
			splitInfo.setContainerCube(Double.parseDouble(jsonObject.get("containerCube").toString()));
			splitInfo.setContainerNo(String.valueOf(jsonObject.get("containerNo")));
			splitInfo.setContainerType(String.valueOf(jsonObject.get("containerType")));
			splitInfo.setLabelNo(String.valueOf(jsonObject.get("labelNo")));
			splitInfo.setOrderOutId(Integer.parseInt(jsonObject.get("orderOutId").toString()));
			splitInfo.setId(Integer.parseInt(jsonObject.get("id").toString()));
			splitInfo.setSplitRemark(String.valueOf(jsonObject.get("splitRemark")));
			records.add(splitInfo);
			this.getCotSplitService().saveOrUpdateSplit(splitInfo, date);
		} else {
			JSONArray jarray = JSONArray.fromObject(json,jsonConfig);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				String date = jsonObject.get("splitDate").toString();
				if(jsonObject.get("splitDate").toString().startsWith("{")){
					JSONObject json1 = JSONObject.fromObject(jsonObject.get("splitDate"));
					int year = Integer.parseInt(json1.getString("year"));
					int month = Integer.parseInt(json1.getString("month"));
					int day = Integer.parseInt(json1.getString("day"));
					Date date1 = new Date(year-1900,month,day);
					date = date1.toLocaleString();
				}
				CotSplitInfo splitInfo = new CotSplitInfo();
				splitInfo.setContainerCube(Double.parseDouble(jsonObject.get("containerCube").toString()));
				splitInfo.setContainerType(String.valueOf(jsonObject.get("containerType")));
				splitInfo.setLabelNo(String.valueOf(jsonObject.get("labelNo")));
				splitInfo.setOrderOutId(Integer.parseInt(jsonObject.get("orderOutId").toString()));
				splitInfo.setContainerNo(String.valueOf(jsonObject.get("containerNo")));
				splitInfo.setId(Integer.parseInt(jsonObject.get("id").toString()));
				splitInfo.setSplitRemark(String.valueOf(jsonObject.get("splitRemark")));
				this.getCotSplitService().saveOrUpdateSplit(splitInfo, String.valueOf(jsonObject.get("splitDate")));
			}
		}	
		return null;
	}
	//添加排载明细
	@SuppressWarnings("deprecation")
	public ActionForward addSplitDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		//内存数据
		HttpSession session = request.getSession();
		HashMap<Integer, CotSplitDetail> splitMap = this.getCotSplitService().getSplitMapAction(session);

		if (splitMap == null) {
			return null;
		}

		List<CotSplitDetail> records = new ArrayList<CotSplitDetail>();
		
		String splitId = request.getParameter("splitPrimId");
		String orderOutId = request.getParameter("orderOutId");

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object orderDetailId = jsonObject.get("orderDetailId");
			if(orderDetailId!=null && !"".equals(orderDetailId.toString())){
				CotSplitDetail cotSplitDetail = splitMap.get(orderDetailId); 
				cotSplitDetail.setSplitId(Integer.parseInt(splitId));				
				
				records.add(cotSplitDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object orderDetailId = jsonObject.get("orderDetailId");
				if(orderDetailId!=null && !"".equals(orderDetailId.toString())){
					CotSplitDetail cotSplitDetail = splitMap.get(orderDetailId); 
					cotSplitDetail.setSplitId(Integer.parseInt(splitId));
					
					records.add(cotSplitDetail);
				}
			}
		}

		// 保存
		this.getCotSplitService().addSplitDetails(records);

		//修改主单的总cbm
		this.getCotSplitService().modifyCotSplitTotalCbm(Integer.parseInt(splitId));
		//修改出货明细中的剩余数量及cbm
		this.getCotSplitService().modifyRemainCountAndCbm(Integer.parseInt(splitId));
		//设置出货单中的排载标志
		this.getCotSplitService().modifySplitFlag(Integer.parseInt(orderOutId));		

		return null;
	}
	
	//删除排载明细
	@SuppressWarnings("deprecation")
	public ActionForward removeSplitDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String splitId = request.getParameter("splitPrimId");
		String orderOutId = request.getParameter("orderOutId");
		
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
		
		HttpSession session = request.getSession();
		for (int j = 0; j < ids.size(); j++) {
			Integer id = (Integer) ids.get(j);
			this.getCotSplitService().addCountAndCbm(Integer.parseInt(splitId),Integer.parseInt(orderOutId),id);
			
			//在Action中清除splitMap中eleId对应的映射
			CotSplitDetail detail = this.getCotSplitService().getSplitDetailById(id);
			this.getCotSplitService().modifyCotSplitCbm(Integer.parseInt(splitId),detail.getOrderDetailId());
			this.getCotSplitService().delSplitMapByKeyAction(detail.getOrderDetailId(), session);
		}
		
		this.getCotSplitService().deleteDetailByIds(ids);
		return null;
		
	}
	
	//修改送样明细
	public ActionForward modifySplitDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String splitId = request.getParameter("splitPrimId");
		String orderOutId = request.getParameter("orderOutId");
		
		//内存数据
		HttpSession session = request.getSession();
		HashMap<Integer, CotSplitDetail> splitMap = this.getCotSplitService().getSplitMapAction(session);
		if (splitMap == null) {
			return null;
		}

		List<CotSplitDetail> records = new ArrayList<CotSplitDetail>();

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object orderDetailId = jsonObject.get("orderDetailId");
			if(orderDetailId!=null && !"".equals(orderDetailId.toString())){
				CotSplitDetail detail = splitMap.get(orderDetailId);		
				//修改出货明细中的剩余数量及cbm
				this.getCotSplitService().updateRemainCountAndCbm(Integer.parseInt(splitId),detail.getOrderDetailId(),detail.getBoxCount());
				//克隆对象,避免造成指针混用
				CotSplitDetail cloneObj = (CotSplitDetail)SystemUtil.deepClone(detail);
			    if(detail!=null){
					records.add(cloneObj);
				}
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object orderDetailId = jsonObject.get("orderDetailId");
				if(orderDetailId!=null && !"".equals(orderDetailId.toString())){
					CotSplitDetail detail = splitMap.get(orderDetailId);		
					//修改出货明细中的剩余数量及cbm
					this.getCotSplitService().updateRemainCountAndCbm(Integer.parseInt(splitId),detail.getOrderDetailId(),detail.getBoxCount());
					//克隆对象,避免造成指针混用
					CotSplitDetail cloneObj = (CotSplitDetail)SystemUtil.deepClone(detail);
				    if(detail!=null){
						records.add(cloneObj);
					}
				}
			}
		}

		// 保存
		this.getCotSplitService().modifySplitDetail(records);
		
		//修改主单的总cbm
		this.getCotSplitService().modifyCotSplitTotalCbm(Integer.parseInt(splitId)); 
		//设置出货单中的排载标志
		this.getCotSplitService().modifySplitFlag(Integer.parseInt(orderOutId));
		
		return null;				
		
	}
	
	private CotOrderOutService orderOutService;
	public CotOrderOutService getOrderOutService() {
		if (orderOutService == null) {
			orderOutService = (CotOrderOutService) super.getBean("CotOrderOutService");
		}
		return orderOutService;
	}

	public void setOrderOutService(CotOrderOutService orderOutService) {
		this.orderOutService = orderOutService;
	}
	private CotSplitService cotSplitService;
	private CotSplitService getCotSplitService() {
		// TODO Auto-generated method stub
		if(cotSplitService==null)
		{
			cotSplitService = (CotSplitService) super.getBean("CotSplitService");
		}
		return cotSplitService;
	}
 
	public void setCotSplitService(CotSplitService cotSplitService) {
		this.cotSplitService = cotSplitService;
	}
	
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
