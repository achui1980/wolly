package com.sail.cot.action.fittingorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotFittingsAnys;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.fittingorder.CotFitOrderService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotFitAnysAction extends AbstractAction {

	private CotFitOrderService cotFitOrderService;

	public CotFitOrderService getCotFitOrderService() {
		if (cotFitOrderService == null) {
			cotFitOrderService = (CotFitOrderService) super
					.getBean("CotFitOrderService");
		}
		return cotFitOrderService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List<CotFittingsAnys> records = new ArrayList<CotFittingsAnys>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFittingsAnys.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFittingsAnys fittingsAnys = new CotFittingsAnys();
				for (int i = 0; i < properties.length; i++) {
					BeanUtils.setProperty(fittingsAnys, properties[i],
							jsonObject.get(properties[i]));
				}
				records.add(fittingsAnys);

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					CotFittingsAnys fittingsAnys = new CotFittingsAnys();
					for (int j = 0; j < properties.length; j++) {
						BeanUtils.setProperty(fittingsAnys, properties[j],
								jsonObject.get(properties[j]));
					}
					records.add(fittingsAnys);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getCotFitOrderService().addList(records);
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFittingsAnys.class);
		List<CotFittingsAnys> records = new ArrayList<CotFittingsAnys>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFittingsAnys fittingsAnys = this.getCotFitOrderService()
						.getFitAnysById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if(jsonObject.get(properties[i])!=null){
						BeanUtils.setProperty(fittingsAnys, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				records.add(fittingsAnys);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotFittingsAnys fittingsAnys = this.getCotFitOrderService()
							.getFitAnysById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if(jsonObject.get(properties[j])!=null){
							BeanUtils.setProperty(fittingsAnys, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					records.add(fittingsAnys);
				}
			}
			this.getCotFitOrderService().updateList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");

		String eleIdFind = request.getParameter("eleIdFind");
		String orderId = request.getParameter("orderId");
		String factoryIdFind = request.getParameter("factoryIdFind");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (eleIdFind != null && !eleIdFind.trim().equals("")) {
			queryString.append(" and obj.eleId like '%" + eleIdFind.trim()
					+ "%'");
		}
		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.orderId=" + orderId.trim());
		}
		if (factoryIdFind != null && !factoryIdFind.trim().equals("")) {
			queryString.append(" and obj.facId=" + factoryIdFind.trim());
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFittingsAnys obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotFittingsAnys obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		List<?> list = this.getCotFitOrderService().getList(queryInfo);
		int count = this.getCotFitOrderService().getRecordCount(queryInfo);
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
		this.getCotFitOrderService().deleteList(ids);

		return null;
	}

	public ActionForward queryFitFrame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("queryFitFrame");
	}

	public ActionForward queryFitOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");

		String orderNo = request.getParameter("orderNo");
		String orderId = request.getParameter("orderId");
		String facIdFind = request.getParameter("facIdFind");
		String startTime = request.getParameter("startTime");// 起始日期
		String endTime = request.getParameter("endTime");// 结束日期

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.orderId=" + orderId.trim());
		}
		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.fittingOrderNo like '%"
					+ orderNo.trim() + "%'");
		}
		if (facIdFind != null && !facIdFind.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + facIdFind.trim());
		}
		
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderDate >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderDate <='" + endTime
					+ " 23:59:59'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFittingOrder obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotFittingOrder obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		List<?> list = this.getCotFitOrderService().getList(queryInfo);
		int count = this.getCotFitOrderService().getRecordCount(queryInfo);
		try {
			String excludes[] = {"cotFittingsOrderdetails"};
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

	// 查询样品配件
	public ActionForward queryFitting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String fitNo = request.getParameter("fitNo");// 配件编号
//
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where 1=1");
//		if (fitNo != null && !fitNo.toString().trim().equals("")) {
//			queryString.append(" and obj.fitNo like '%"
//					+ fitNo.toString().trim() + "%'");
//		}
//
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("fittingTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotFittings obj"
//				+ queryString);
//		// String sql = "from CotFittings obj";
//
//		queryInfo.setSelectString("select obj from CotFittings obj");
//		// 设置查询参数
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.fitNo asc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "fittingTable");
//		// 取得排序信息
//		Sort sort = limit.getSort();
//
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getCotFitOrderService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getCotFitOrderService().getList(queryInfo);
//		// request.setAttribute("allFactoryName", this.getQueryService()
//		// .getFactoryNameMap());
//		request.setAttribute("fittingList", list);
		return mapping.findForward("queryFitting");
	}
}
