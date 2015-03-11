/**
 * 
 */
package com.sail.cot.action.price;

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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotPanPic;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotPriceEleprice;
import com.sail.cot.domain.CotPriceFittings;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.price.CotPriceService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotPriceAction extends AbstractAction {

	private CotPriceService priceService;
	private CotElementsService cotElementsService;

	@SuppressWarnings( { "deprecation", "unchecked" })
	@Override
	// 添加报价单明细
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 内存数据
		HttpSession session = request.getSession();
		Map<String, CotPriceDetail> priceMap = this.getPriceService()
				.getMapAction(session);

		List<CotPriceDetail> records = new ArrayList<CotPriceDetail>();
		List<CotPricePic> imgList = new ArrayList<CotPricePic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		byte[] zwtpByte = this.getCotElementsService().getZwtpPic();
		String priceId = request.getParameter("pricePrimId");
		String currencyId = request.getParameter("currencyId");
		String customerId = request.getParameter("custId");
		Integer custId = Integer.parseInt(customerId);

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object rdm = jsonObject.get("rdm");
			if (rdm != null && !"".equals(rdm.toString())) {
				CotPriceDetail cotPriceDetail = priceMap.get(rdm.toString());
				// 设置序列号
				Integer sortNo = (Integer) jsonObject.get("sortNo");
				cotPriceDetail.setSortNo(sortNo);
				// 设置是否锁定价格
				Object checkFlag = jsonObject.get("checkFlag");
				if (checkFlag == null || checkFlag.toString().equals("null")) {
					cotPriceDetail.setCheckFlag(0);
				} else {
					if (checkFlag instanceof Integer) {
						cotPriceDetail.setCheckFlag((Integer) checkFlag);
					}
					if (checkFlag instanceof Boolean) {
						if ((Boolean) checkFlag == false) {
							cotPriceDetail.setCheckFlag(0);
						} else {
							cotPriceDetail.setCheckFlag(1);
						}
					}
				}
				records.add(cotPriceDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object rdm = jsonObject.get("rdm");
				if (rdm != null && !"".equals(rdm.toString())) {
					CotPriceDetail cotPriceDetail = priceMap
							.get(rdm.toString());
					// 设置序列号
					Integer sortNo = (Integer) jsonObject.get("sortNo");
					cotPriceDetail.setSortNo(sortNo);
					// 设置是否锁定价格
					Object checkFlag = jsonObject.get("checkFlag");
					if (checkFlag == null
							|| checkFlag.toString().equals("null")) {
						cotPriceDetail.setCheckFlag(0);
					} else {
						if (checkFlag instanceof Integer) {
							cotPriceDetail.setCheckFlag((Integer) checkFlag);
						}
						if (checkFlag instanceof Boolean) {
							if ((Boolean) checkFlag == false) {
								cotPriceDetail.setCheckFlag(0);
							} else {
								cotPriceDetail.setCheckFlag(1);
							}
						}
					}
					records.add(cotPriceDetail);
				}
			}
		}

		for (int i = 0; i < records.size(); i++) {
			CotPriceDetail detail = records.get(i);
			if (detail == null) {
				continue;
			}
			detail.setCurrencyId(Integer.parseInt(currencyId));
			detail.setPriceId(Integer.parseInt(priceId));
			detail.setEleAddTime(new Date(System.currentTimeMillis()));// 添加时间
			// 新建图片
			CotPricePic pricePic = new CotPricePic();
			if (detail.getType().equals("ele")) {
				CotElePic cotElePic = impOpService.getElePicImgByEleId(detail
						.getSrcId());
				detail.setPicName(cotElePic.getEleId());
				pricePic.setEleId(cotElePic.getEleId());
				pricePic.setPicImg(cotElePic.getPicImg());
				pricePic.setPicSize(cotElePic.getPicImg().length);
				pricePic.setPicName(cotElePic.getEleId());
			}
			if (detail.getType().equals("price")) {
				CotPricePic cotPricePic = impOpService.getPricePic(detail
						.getSrcId());
				// 新增的这行 引用的图片刚好是同个表格被删掉的行,先执行删除,已经删掉图片
				// 避免图片消失的方法,先保存后再单独删除要删除的行
				if (cotPricePic != null) {
					detail.setPicName(cotPricePic.getEleId());
					pricePic.setEleId(cotPricePic.getEleId());
					pricePic.setPicImg(cotPricePic.getPicImg());
					pricePic.setPicSize(cotPricePic.getPicImg().length);
					pricePic.setPicName(cotPricePic.getEleId());
				} else {
					detail.setPicName(detail.getEleId());
					pricePic.setEleId(detail.getEleId());
					pricePic.setPicImg(zwtpByte);
					pricePic.setPicSize(zwtpByte.length);
					pricePic.setPicName(detail.getEleId());
				}

			}
			if (detail.getType().equals("order")) {
				CotOrderPic cotOrderPic = impOpService.getOrderPic(detail
						.getSrcId());
				detail.setPicName(cotOrderPic.getEleId());
				pricePic.setEleId(cotOrderPic.getEleId());
				pricePic.setPicImg(cotOrderPic.getPicImg());
				pricePic.setPicSize(cotOrderPic.getPicImg().length);
				pricePic.setPicName(cotOrderPic.getEleId());
			}
			if (detail.getType().equals("given")) {
				CotGivenPic cotGivenPic = impOpService.getGivenPic(detail
						.getSrcId());
				detail.setPicName(cotGivenPic.getEleId());
				pricePic.setEleId(cotGivenPic.getEleId());
				pricePic.setPicImg(cotGivenPic.getPicImg());
				pricePic.setPicSize(cotGivenPic.getPicImg().length);
				pricePic.setPicName(cotGivenPic.getEleId());
			}
			if (detail.getType().equals("panEle")) {
				CotPanPic cotPanPic = impOpService.getPanPic(detail.getSrcId());
				detail.setPicName(cotPanPic.getEleId());
				pricePic.setEleId(cotPanPic.getEleId());
				pricePic.setPicImg(cotPanPic.getPicImg());
				pricePic.setPicSize(cotPanPic.getPicImg().length);
				pricePic.setPicName(cotPanPic.getEleId());
			}
			if (detail.getType().equals("none")) {
				detail.setPicName(detail.getEleId());
				pricePic.setEleId(detail.getEleId());
				pricePic.setPicImg(zwtpByte);
				pricePic.setPicSize(zwtpByte.length);
				pricePic.setPicName(detail.getEleId());
			}
			// 添加到图片数组
			imgList.add(pricePic);
		}

		// 保存
		this.getPriceService().addPriceDetails(records);
		// 保存报价图片
		for (int i = 0; i < records.size(); i++) {
			CotPriceDetail detail = (CotPriceDetail) records.get(i);
			CotPricePic cotPricePic = (CotPricePic) imgList.get(i);
			cotPricePic.setFkId(detail.getId());
			List picList = new ArrayList();
			picList.add(cotPricePic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(picList);
		}
		
		//b货号调用历史数据时,如果刚好是同个主单的明细a,如果刚好这条明细需要被删除,
		//则后台action会先调用删除方法,把a删掉,数据库的外键级联删除明细图片,那么b明细保存时,无法找到引用的a图片
		//解决方法是,先判断要删除的订单明细是否被其他订单引用,如果有,则暂时不删除该明细,把该明细的type字段设置为'delete',在保存的action中的最后再删除
		List delIds = new ArrayList();
		Iterator<?> itAt = priceMap.keySet().iterator();
		while (itAt.hasNext()) {
			String key = (String) itAt.next();
			CotPriceDetail detail = priceMap.get(key);
			if(detail.getType()!=null && detail.getType().equals("delete")){
				delIds.add(detail.getId());
			}
		}
		this.getPriceService().deleteDetailByIds(delIds);

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "price";
		while (it.hasNext()) {
			CotPriceDetail detail = (CotPriceDetail) it.next();
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
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// 内存数据
		HttpSession session = request.getSession();
		Map<String, CotPriceDetail> priceMap = this.getPriceService()
				.getMapAction(session);

		List<CotPriceDetail> records = new ArrayList<CotPriceDetail>();
		String customerId = request.getParameter("custId");
		String currencyId = request.getParameter("currencyId");
		Integer custId = Integer.parseInt(customerId);

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object rdm = jsonObject.get("rdm");
			if (rdm != null && !"".equals(rdm.toString())) {
				CotPriceDetail cotPriceDetail = priceMap.get(rdm.toString());
				cotPriceDetail.setCurrencyId(Integer.parseInt(currencyId));
				// 设置是否锁定价格
				Object checkFlag = jsonObject.get("checkFlag");
				if (checkFlag instanceof Integer) {
					cotPriceDetail.setCheckFlag((Integer) checkFlag);
				}
				if (checkFlag instanceof Boolean) {
					if ((Boolean) checkFlag == false) {
						cotPriceDetail.setCheckFlag(0);
					} else {
						cotPriceDetail.setCheckFlag(1);
					}
				}
				records.add(cotPriceDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object rdm = jsonObject.get("rdm");
				if (rdm != null && !"".equals(rdm.toString())) {
					CotPriceDetail cotPriceDetail = priceMap
							.get(rdm.toString());
					cotPriceDetail.setCurrencyId(Integer.parseInt(currencyId));
					// 设置是否锁定价格
					Object checkFlag = jsonObject.get("checkFlag");
					if (checkFlag instanceof Integer) {
						cotPriceDetail.setCheckFlag((Integer) checkFlag);
					}
					if (checkFlag instanceof Boolean) {
						if ((Boolean) checkFlag == false) {
							cotPriceDetail.setCheckFlag(0);
						} else {
							cotPriceDetail.setCheckFlag(1);
						}
					}
					records.add(cotPriceDetail);
				}
			}
		}

		// 保存
		this.getPriceService().modifyPriceDetails(records);

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "price";
		while (it.hasNext()) {
			CotPriceDetail detail = (CotPriceDetail) it.next();
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
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
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
		this.getPriceService().deletePriceDetails(request, ids);
		return null;
	}

	// 跳转到报价添加页面
	public ActionForward addPrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("allCurrencyName", this.getPriceService()
				.getCurrencyMap(request));
		request.setAttribute("allFactoryName", this.getPriceService()
				.getFactoryNameMap(request));
		return mapping.findForward("addDetail");
	}

	// 跳转到报价明细编辑页面
	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("detail");
	}

	// 查询报价单数据
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String flag = request.getParameter("flag");// 用于客户页面调用

		String custId = request.getParameter("custId");// 客户ID
		String companyId = request.getParameter("companyId");// 客户ID
		String priceNoFind = request.getParameter("priceNoFind");// 报价单号
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 报价起始日期
		String endTime = request.getParameter("endTime");// 报价结束日期
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		String query = request.getParameter("query");
		if(query!=null&&!query.trim().equals(""))
			try {
				JSONObject jObject = JSONObject.fromObject(query);
				custId = jObject.getString("custId");
				priceNoFind = jObject.getString("priceNoFind");
				businessPerson = jObject.getString("businessPerson");
				startTime = jObject.getString("startTime");
				endTime = jObject.getString("endTime");
			} catch (Exception e) {}
		
		if (start == null || limit == null) {
			if ("customerPage".equals(flag)) {
				return mapping.findForward("queryPrice");
			} else {
				return mapping.findForward("querySuccess");
			}
		}

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
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId =" + custId.trim());
		}
		if (companyId != null && !companyId.trim().equals("")) {
			queryString.append(" and obj.companyId =" + companyId.trim());
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
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPrice as obj,"
				+ "CotEmps AS e,CotCustomer c" + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," + "obj.priceTime,"
				+ "c.customerShortName," + "obj.priceNo," + "e.empsName,"
				+ "obj.clauseId," + "obj.currencyId," + "obj.situationId,"
				+ "obj.validMonths," + "obj.priceStatus," + "obj.priceRate,"
				+ "obj.priceCompose,c.id as cId " + "from CotPrice AS obj,"
				+ "CotEmps AS e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.priceNo asc");
		//queryInfo.setOrderString(" order by obj.id asc");
		int count = this.getPriceService().getRecordCount(queryInfo);

		// 根据起始
		List<?> list = this.getPriceService().getPriceVOList(queryInfo);

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

	// 查询该报价单的报价明细产品的信息
	public ActionForward queryPriceDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("priceDetail");

		StringBuffer queryString = new StringBuffer();
		String priceId = request.getParameter("priceId");// 主报价单号
		if (priceId == null || "".equals(priceId)) {
			return null;
		}
		queryString.append(" where obj.priceId=" + priceId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPriceDetail obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotPriceDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.eleId asc");

		try {
			String json = this.getPriceService()
					.getListData(request, queryInfo);
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

	// 跳转到上传盘点机文件页面
	public ActionForward showMachineUpload(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("showMachineUpload");
	}

	// 跳转到excel导入界面
	public ActionForward showExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showExcel");
	}

	// 跳转到图片导入页面
	public ActionForward importPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("importPic");
	}

	// 添加提醒消息
	public ActionForward addToMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addToMessage");
	}

	// 查询报价明细的配件信息
	public ActionForward queryPriceFittings(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryPriceFittings");

		StringBuffer queryString = new StringBuffer();
		String rdm = request.getParameter("rdm");// 报价明细
		// 获得内存map
		Map<String, CotPriceDetail> priceMap = this.getPriceService()
				.getMapAction(request.getSession());
		CotPriceDetail detail = priceMap.get(rdm);
		String tableName = "";
		// 判断是否是新增,隐藏页面工具栏不让操作
		if (detail.getId() == null) {
			request.setAttribute("isHId", 0);
			// 判断货号来源(报价,样品,订单)
			String type = detail.getType();
			Integer srcId = detail.getSrcId();
			if ("ele".equals(type)) {
				tableName = "CotEleFittings";
				queryString.append(" where obj.eleId=" + srcId);
			}
			if("given".equals(type)){
				tableName = "CotEleFittings";
				//用过货号查找样品id
				Integer eId = this.getPriceService().getEleIdByEleName(detail.getEleId());
				if(eId!=0){
					queryString.append(" where obj.eleId=" + eId);
				}else{
					queryString.append(" where 1=0");
				}
			}
			if("none".equals(type)){
				tableName = "CotEleFittings";
				queryString.append(" where 1=0");
			}
			if ("price".equals(type)) {
				tableName = "CotPriceFittings";
				queryString.append(" where obj.priceDetailId=" + srcId);
			}
			if ("order".equals(type)) {
				tableName = "CotOrderFittings";
				queryString.append(" where obj.orderDetailId=" + srcId);
			}
		} else {
			tableName = "CotPriceFittings";
			request.setAttribute("isHId", detail.getId());
			queryString.append(" where obj.priceDetailId=" + detail.getId());
		}

		String facId = request.getParameter("facId");
		String fitNo = request.getParameter("fitNo");
		String fitName = request.getParameter("fitName");
		String fitDesc = request.getParameter("fitDesc");

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();

		if (facId != null && !"".equals(facId)) {
			queryString.append(" and obj.facId=" + facId);
		}
		if (fitNo != null && !"".equals(fitNo)) {
			queryString.append(" and obj.fitNo like '%" + fitNo.trim() + "%'");
		}
		if (fitName != null && !"".equals(fitName)) {
			queryString.append(" and obj.fitName like '%" + fitName.trim()
					+ "%'");
		}
		if (fitDesc != null && !"".equals(fitDesc)) {
			queryString.append(" and obj.fitDesc like '%" + fitDesc.trim()
					+ "%'");
		}

		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from " + tableName + " obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from " + tableName + " obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int count = this.getPriceService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getPriceService().getList(queryInfo);
		//新增时
		if (detail.getId() == null) {
			//如果是报价或订单的配件.需要判断该配件厂家或者采购价格是否和配件库一致,如果不同,用配件库的最新数据代替
			list = this.getPriceService().getNewList(tableName,list);
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

	// 查询报价明细的成本信息
	public ActionForward queryPriceElePrice(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryPriceElePrice");
		StringBuffer queryString = new StringBuffer();
		String rdm = request.getParameter("rdm");// 报价明细
		// 获得内存map
		Map<String, CotPriceDetail> priceMap = this.getPriceService()
				.getMapAction(request.getSession());
		CotPriceDetail detail = priceMap.get(rdm);
		String tableName = "";
		// 判断是否是新增,隐藏页面工具栏不让操作
		if (detail.getId() == null) {
			request.setAttribute("isHId", false);
			// 判断货号来源(报价,样品,订单)
			String type = detail.getType();
			Integer srcId = detail.getSrcId();
			if ("ele".equals(type)) {
				tableName = "CotElePrice";
				queryString.append(" where obj.eleId=" + srcId);
			}
			if("given".equals(type)){
				tableName = "CotElePrice";
				//用过货号查找样品id
				Integer eId = this.getPriceService().getEleIdByEleName(detail.getEleId());
				if(eId!=0){
					queryString.append(" where obj.eleId=" + eId);
				}else{
					queryString.append(" where 1=0");
				}
			}
			if("none".equals(type)){
				tableName = "CotElePrice";
				queryString.append(" where 1=0");
			}
			if ("price".equals(type)) {
				tableName = "CotPriceEleprice";
				queryString.append(" where obj.priceDetailId=" + srcId);
			}
			if ("order".equals(type)) {
				tableName = "CotOrderEleprice";
				queryString.append(" where obj.orderDetailId=" + srcId);
			}
		} else {
			tableName = "CotPriceEleprice";
			request.setAttribute("isHId", true);
			queryString.append(" where obj.priceDetailId=" + detail.getId());
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
		queryInfo.setCountQuery("select count(*) from " + tableName + " obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from " + tableName + " obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {

			String json = this.getPriceService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 保存报价明细的配件
	public ActionForward addFitting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotPriceFittings.class);
		List<CotPriceFittings> records = new ArrayList<CotPriceFittings>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotPriceFittings priceFittings = new CotPriceFittings();
				for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(priceFittings, properties[i],
								null);
					} else {
						BeanUtils.setProperty(priceFittings, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				priceFittings.setId(null);
				priceFittings.setPriceDetailId(Integer.parseInt(detailId));
				priceFittings.setPriceId(Integer.parseInt(priceId));
				records.add(priceFittings);

			} else {

				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotPriceFittings priceFittings = new CotPriceFittings();
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(priceFittings, properties[j],
									null);
						} else {
							BeanUtils.setProperty(priceFittings, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					priceFittings.setId(null);
					priceFittings.setPriceDetailId(Integer.parseInt(detailId));
					priceFittings.setPriceId(Integer.parseInt(priceId));
					records.add(priceFittings);
				}
			}
			// 调用service更新记录
			this.getPriceService().addList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 修改报价明细的配件
	public ActionForward modifyFitting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotPriceFittings.class);
		List<CotPriceFittings> records = new ArrayList<CotPriceFittings>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotPriceFittings priceFittings = this.getPriceService()
						.getPriceFittingById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(priceFittings, properties[i],
								null);
					} else {
						BeanUtils.setProperty(priceFittings, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				priceFittings.setPriceDetailId(Integer.parseInt(detailId));
				priceFittings.setPriceId(Integer.parseInt(priceId));
				records.add(priceFittings);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotPriceFittings priceFittings = this.getPriceService()
							.getPriceFittingById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(priceFittings, properties[j],
									null);
						} else {
							BeanUtils.setProperty(priceFittings, properties[j],
									jsonObject.get(properties[j]));
						}


					}
					priceFittings.setPriceDetailId(Integer.parseInt(detailId));
					priceFittings.setPriceId(Integer.parseInt(priceId));
					records.add(priceFittings);
				}
			}
			this.getPriceService().modifyList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 删除报价明细的配件
	public ActionForward removeFitting(ActionMapping mapping, ActionForm form,
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
		this.getPriceService().deleteList(ids, "CotPriceFittings");
		return null;
	}

	// 保存报价明细的配件
	public ActionForward addElePrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotPriceEleprice.class);
		List<CotPriceEleprice> records = new ArrayList<CotPriceEleprice>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotPriceEleprice priceEleprice = new CotPriceEleprice();
				for (int i = 0; i < properties.length; i++) {
					BeanUtils.setProperty(priceEleprice, properties[i],
							jsonObject.get(properties[i]));
				}
				priceEleprice.setId(null);
				priceEleprice.setPriceDetailId(Integer.parseInt(detailId));
				priceEleprice.setPriceId(Integer.parseInt(priceId));
				records.add(priceEleprice);

			} else {

				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotPriceEleprice priceEleprice = new CotPriceEleprice();
					for (int j = 0; j < properties.length; j++) {
						BeanUtils.setProperty(priceEleprice, properties[j],
								jsonObject.get(properties[j]));

					}
					priceEleprice.setId(null);
					priceEleprice.setPriceDetailId(Integer.parseInt(detailId));
					priceEleprice.setPriceId(Integer.parseInt(priceId));
					records.add(priceEleprice);
				}
			}
			this.getPriceService().addElePriceList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 修改报价明细的配件
	public ActionForward modifyElePrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotPriceEleprice.class);
		List<CotPriceEleprice> records = new ArrayList<CotPriceEleprice>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotPriceEleprice priceEleprice = this.getPriceService()
						.getElePriceById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					BeanUtils.setProperty(priceEleprice, properties[i],
							jsonObject.get(properties[i]));
				}
				priceEleprice.setPriceDetailId(Integer.parseInt(detailId));
				priceEleprice.setPriceId(Integer.parseInt(priceId));
				records.add(priceEleprice);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotPriceEleprice priceEleprice = this.getPriceService()
							.getElePriceById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						BeanUtils.setProperty(priceEleprice, properties[j],
								jsonObject.get(properties[j]));

					}
					priceEleprice.setPriceDetailId(Integer.parseInt(detailId));
					priceEleprice.setPriceId(Integer.parseInt(priceId));
					records.add(priceEleprice);
				}

			}
			this.getPriceService().modifyList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除报价明细的配件
	public ActionForward removeElePrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取页面传入的所有recordKey的属性值
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
		this.getPriceService().deleteList(ids, "CotPriceEleprice");

		return null;
	}

	public CotPriceService getPriceService() {
		if (priceService == null) {
			priceService = (CotPriceService) super.getBean("CotPriceService");
		}
		return priceService;
	}

	public void setPriceService(CotPriceService priceService) {
		this.priceService = priceService;
	}

	public CotElementsService getCotElementsService() {
		if (cotElementsService == null) {
			cotElementsService = (CotElementsService) super
					.getBean("CotElementsService");
		}
		return cotElementsService;
	}

	public void setCotElementsService(CotElementsService cotElementsService) {
		this.cotElementsService = cotElementsService;
	}

}
