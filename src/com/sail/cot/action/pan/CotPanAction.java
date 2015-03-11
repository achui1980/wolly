/**
 * 
 */
package com.sail.cot.action.pan;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotPanDetail;
import com.sail.cot.domain.CotPanEle;
import com.sail.cot.domain.CotPanOtherPic;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.pan.CotPanService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;

public class CotPanAction extends AbstractAction {

	private CotPanService panService;

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("querySuccess");

		String priceNo = request.getParameter("orderNoFind");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String startDealLine = request.getParameter("startDeadLine");
		String endDealLine = request.getParameter("endDeadLine");
		String addPerson = request.getParameter("addPerson");
		String customer = request.getParameter("customerStr");
		String factory = request.getParameter("factoryStr");
		String status = request.getParameter("status");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		
		if (priceNo != null && !priceNo.trim().equals("")) {
			queryString.append(" and obj.priceNo like '%" + priceNo.trim()
					+ "%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.addTime <='" + endTime + " 23:59:59'");
		}
		if (startDealLine != null && !"".equals(startDealLine.trim())) {
			queryString.append(" and obj.valDate >='" + startDealLine + "'");
		}
		if (endDealLine != null && !"".equals(endDealLine.trim())) {
			queryString.append(" and obj.valDate <='" + endDealLine + " 23:59:59'");
		}
		if (addPerson != null && !"".equals(addPerson.trim())) {
			queryString.append(" and obj.addPerson=" + addPerson);
		}
		if (status != null && !"0".equals(status.trim()) && !"".equals(status.trim())) {
			if("1".equals(status.trim())){
				queryString.append(" and (obj.status is null or obj.status=1)");
			}else{
				queryString.append(" and obj.status=" + status);
			}
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
				.setCountQuery("select count(*) from CotPan obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotPan obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String panId = request.getParameter("panId");

		if (start == null || limit == null || panId == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (panId != null && !panId.trim().equals("")) {
			queryString.append(" and obj.panId=" + panId.trim());
		}else{
			queryString.append(" and 1=0");
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPanEle obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotPanEle obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward queryFacDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String panId = request.getParameter("panId");
		String factoryId = request.getParameter("factoryId");
		
		if(start == null || limit == null || panId==null)
			return null;
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(panId!=null && !"".equals(panId)){
			queryString.append(" and obj.panIdd="+panId);
		}
		if(factoryId!=null && !"".equals(factoryId)){
			queryString.append(" and obj.factoryId="+factoryId);
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from PanView obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from PanView obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
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
		String panId = request.getParameter("panId");

		if (start == null || limit == null)
			return null;
		
		if(panId == null){
			panId="0";
		}

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.panId=e.id");
		if (panId != null && !panId.trim().equals("")) {
			queryString.append(" and obj.panId=" + panId.trim());
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotPanDetail obj,CotPanEle e"
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select new CotPanDetail(obj,e.eleId) from CotPanDetail obj,CotPanEle e");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward queryContacts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String factoryId = request.getParameter("factoryId");
		String panId = request.getParameter("panId");

		if (start == null || limit == null || factoryId == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.mainFlag is not null and obj.mainFlag=1 and obj.contactEmail is not null and obj.contactEmail!=''");
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}

		// 查询该询盘单未确认过的厂家集合id
		List facIds = this.getPanService().findAlreadySendMails(
				Integer.parseInt(panId));
		if (facIds!=null && facIds.size()>0) {
			String str = "";
			for (int i = 0; i <facIds.size(); i++) {
				str+=facIds.get(i)+",";
			}
			queryString.append(" and obj.factoryId in (" + str.substring(0,str.length()-1) + ")");
		}else{
			queryString.append(" and 1=0");
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotContact obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotContact obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward querySendMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String panId = request.getParameter("panId");
		String factoryId = request.getParameter("factoryId");

		if (start == null || limit == null || panId == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.panId=p.id and obj.contactId=c.id and c.factoryId=f.id");
		if (panId != null && !panId.trim().equals("")) {
			queryString.append(" and obj.panId=" + panId.trim());
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and c.factoryId=" + factoryId.trim());
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotPanContact obj,CotPan p,CotContact c,CotFactory f"
						+ queryString);
		// 设置查询记录语句
		queryInfo
				.setSelectString("select new CotPanContact(obj,p.priceNo,c.contactPerson) from CotPanContact obj,CotPan p,CotContact c,CotFactory f");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 跳转到编辑页面
	public ActionForward showEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("edit");
	}
	
	// 跳转到编辑页面
	public ActionForward showEditHw(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("editHw");
	}
	
	// 跳转到厂家报价编辑页面
	public ActionForward showFacEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("editFac");
	}

	// 跳转到编辑页面
	public ActionForward queryOtherPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryOtherPic");
	}

	public CotPanService getPanService() {
		if (panService == null) {
			panService = (CotPanService) super.getBean("CotPanService");
		}
		return panService;
	}

	public void setPanService(CotPanService panService) {
		this.panService = panService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String pId = request.getParameter("pId");
		String json = request.getParameter("data");
		// Integer empId = (Integer) request.getSession().getAttribute("empId");
		List list = new ArrayList();
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			String eleId = jsonObject.getString("eleId");
			if (eleId != null && !eleId.equals("")) {
				CotPanEle obj = (CotPanEle) jsonObject.toBean(jsonObject,
						CotPanEle.class);
				obj.setId(null);
				obj.setPanId(Integer.parseInt(pId));
				list.add(obj);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			List listAry = jarray.toList(jarray, CotPanEle.class);
			for (int i = 0; i < listAry.size(); i++) {
				CotPanEle obj = (CotPanEle) listAry.get(i);
				String eleId = obj.getEleId();
				if (eleId != null && !eleId.equals("")) {
					obj.setId(null);
					obj.setPanId(Integer.parseInt(pId));
					list.add(obj);
				}
			}
		}
		if (list.size() > 0) {
			this.getPanService().addList(list);
		}
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String json = request.getParameter("data");
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		List list = null;
		ConvertUtils.register(new IntegerConverter(null), Integer.class);

		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			CotPanEle obj = (CotPanEle) jsonObject.toBean(jsonObject,
					CotPanEle.class);
			obj.setModDate(new java.util.Date());
			obj.setModPerson(empId);
			list = new ArrayList();
			list.add(obj);
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, CotPanEle.class);
			for (Object object : list) {
				CotPanEle obj = (CotPanEle) object;
				obj.setModDate(new java.util.Date());
				obj.setModPerson(empId);
			}
		}
		// 保存
		this.getPanService().saveOrUpdateList(list);
		return null;
	}

	public ActionForward modifyDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String json = request.getParameter("data");
		List list = null;
		ConvertUtils.register(new IntegerConverter(null), Integer.class);

		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			CotPanDetail obj = (CotPanDetail) jsonObject.toBean(jsonObject,
					CotPanDetail.class);
			list = new ArrayList();
			list.add(obj);
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, CotPanDetail.class);
		}
		// 保存
		this.getPanService().saveOrUpdateList(list);
		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		List list = null;
		List ids = new ArrayList();
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			list = new ArrayList();
			list.add(new Integer(json));

		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, ArrayList.class);
		}
		try {
			this.getPanService().deletePanDetails(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward removeDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		List list = null;
		List ids = new ArrayList();
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			list = new ArrayList();
			list.add(new Integer(json));

		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, ArrayList.class);
		}
		try {
			this.getPanService().deletePriceDetails(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 跳转厂家询盘界面
	public ActionForward showSupplierPan(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("showSupplierPan");
	}
	
	// 跳转供应商来信界面
	public ActionForward showSupplierPanFrom(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("showSupplierPanFrom");
	}

	public ActionForward querySupplierPanDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		String eleNameEn = request.getParameter("eleNameEn");
		String manufactorer = request.getParameter("manufactorer");
		String state = request.getParameter("state");
		String uploadEmp = request.getParameter("uploadEmp");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String priceNo = request.getParameter("priceNo");
		String addPerson = request.getParameter("addPerson");
		String deptId = request.getParameter("deptId");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.panId=e.id and e.panId=p.id and obj.price is not null and obj.price>0");
		
		if (eleNameEn != null && !eleNameEn.trim().equals("")) {
			queryString.append(" and e.eleNameEn like '%" + eleNameEn.trim()
					+ "%'");
		}
		if (priceNo != null && !priceNo.trim().equals("")) {
			queryString.append(" and p.priceNo like '%" + priceNo.trim()
					+ "%'");
		}
		if (manufactorer != null && !manufactorer.trim().equals("")) {
			queryString.append(" and e.willSupplier like '%" + manufactorer.trim()
					+ "%'");
		}
		if (state != null && !state.trim().equals("")) {
			if(state.trim().equals("2")){
//				queryString.append(" and (e.state is null or e.state=0)");
				queryString.append(" and (e.state is null or e.state=0 or e.manufactorer is null or e.manufactorer!=obj.willSupplier)");
			}else{
				queryString.append(" and e.state is not null and e.state=1");
				queryString.append(" and e.manufactorer=obj.willSupplier");
			}
		}
		if (uploadEmp != null && !uploadEmp.trim().equals("")) {
			queryString.append(" and obj.uploadEmp=" + uploadEmp);
		}
		if (addPerson != null && !addPerson.trim().equals("")) {
			queryString.append(" and p.addPerson=" + addPerson);
		}
		if (deptId != null && !deptId.trim().equals("")) {
			queryString.append(" and p.deptId=" + deptId);
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.modDate >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.modDate <='" + endTime + " 23:59:59'");
		}
		
		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotPanDetail obj,CotPanEle e,CotPan p" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select new CotPanDetail(obj,e.eleNameEn,e.state,p.priceNo,e.manufactorer) from CotPanDetail obj,CotPanEle e,CotPan p");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by e.modDate desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 浏览样品其他图片
	public ActionForward showPicture(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String eId = request.getParameter("eId");

		if (start == null || limit == null)
			return mapping.findForward("queryOtherPic");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (eId != null && !eId.equals("")) {
			queryString.append(" and obj.fkId = " + eId);
		}
		QueryInfo queryInfo = new QueryInfo();
		String sql = "from CotPanOtherPic obj";
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		queryInfo.setCountQuery("select count(*) from CotPanOtherPic obj "
				+ queryString.toString());

		int pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String excludes[] = { "picImg" };
			queryInfo.setExcludes(excludes);
			String json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 修改图片名称
	public ActionForward modifyPics(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 前台传递来的数据
		String json = request.getParameter("data");
		List<CotPanOtherPic> records = new ArrayList<CotPanOtherPic>();
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			String picName = jsonObject.getString("picName");
			Integer pId = jsonObject.getInt("id");
			CotPanOtherPic picture = this.getPanService().getPicById(pId);
			picture.setPicName(picName);
			records.add(picture);
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				String picName = jsonObject.getString("picName");
				Integer pId = jsonObject.getInt("id");
				CotPanOtherPic picture = this.getPanService().getPicById(pId);
				picture.setPicName(picName);
				records.add(picture);
			}
		}
		this.getPanService().saveOrUpdateList(records);
		return null;
	}
	
	// 询盘编辑页面点击Mail查询
	public ActionForward queryAllContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String panId = request.getParameter("panId");
		String factoryId = request.getParameter("factoryIdFindL");
		String contactPerson = request.getParameter("contactFindL");

		if (start == null || limit == null )
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
//		List supList = this.getPanService().findFactoryByPanId(
//				Integer.parseInt(panId));
//		String st = "";
//		for (int i = 0; i < supList.size(); i++) {
//			st += supList.get(i) + ",";
//		}
//		if (!st.equals("")) {
//			queryString.append(" and obj.factoryId in ("
//					+ st.substring(0, st.length() - 1) + ")");
//		}else{
//			queryString.append(" and 1=0");
//		}
		
		if (factoryId != null && !"".equals(factoryId.trim())) {
			queryString.append(" and obj.factoryId=" + factoryId.toString());
		}
		if (contactPerson != null && !contactPerson.toString().trim().equals("")) {
			queryString.append(" and obj.contactPerson like '%"
					+ contactPerson.toString().trim() + "%'");
		}
		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotContact obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotContact obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 询盘编辑页面点击Mail查询
	public ActionForward queryAllMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String panId = request.getParameter("panId");

		if (start == null || limit == null || panId == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.contactId=c.id");
		QueryInfo queryInfo = new QueryInfo();
		
		if (panId != null && !"".equals(panId.trim())) {
			queryString.append(" and obj.panId="+panId.trim());
		}
		String factoryId = request.getParameter("factoryIdFind");
		String contactPerson = request.getParameter("contactFind");
		if (factoryId != null && !"".equals(factoryId.trim())) {
			queryString.append(" and c.factoryId=" + factoryId.toString());
		}
		if (contactPerson != null && !contactPerson.toString().trim().equals("")) {
			queryString.append(" and c.contactPerson like '%"
					+ contactPerson.toString().trim() + "%'");
		}

		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotPanContact obj,CotContact c"  + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select new CotPanContact(obj,c.contactNbr,c.contactPerson,c.factoryId,c.loginName) from CotPanContact obj,CotContact c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getPanService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward addContacts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String json = request.getParameter("data");
		List list = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			CotContact obj = (CotContact) jsonObject.toBean(jsonObject,
					CotContact.class);
			list = new ArrayList();
			obj.setId(null);
			CotFactory factory = this.getPanService().getFacById(
					obj.getFactoryId());
			obj.setMainFlag(1);
			obj.setLoginName(factory.getShortName());
			obj.setLoginPwd("123456");
			list.add(obj);
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, CotContact.class);
			for (int i = 0; i < list.size(); i++) {
				CotContact obj = (CotContact) list.get(i);
				obj.setId(null);
				CotFactory factory = this.getPanService().getFacById(
						obj.getFactoryId());
				obj.setMainFlag(1);
				obj.setLoginName(factory.getShortName());
				obj.setLoginPwd("123456");
				list.set(i, obj);
			}
		}
		this.getPanService().saveOrUpdateList(list);
		return null;
	}

	public ActionForward modifyContacts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotContact.class);
		List<CotContact> records = new ArrayList<CotContact>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotContact contact = this.getPanService().getContactById(
						Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if (jsonObject.get(properties[i]) != null) {
						BeanUtils.setProperty(contact, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				records.add(contact);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotContact contact = this.getPanService().getContactById(
							Integer.parseInt(jsonObject.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if (jsonObject.get(properties[j]) != null) {
							BeanUtils.setProperty(contact, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					records.add(contact);
				}
			}
			this.getPanService().saveOrUpdateList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
