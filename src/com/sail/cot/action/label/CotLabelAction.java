package com.sail.cot.action.label;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotLabeldetail;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.label.CotLabelService;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotLabelAction extends AbstractAction {

	private CotLabelService cotLabelService;

	public CotLabelService getCotLabelService() {
		if (cotLabelService == null) {
			cotLabelService = (CotLabelService) super
					.getBean("CotLabelService");
		}
		return cotLabelService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String labelId = request.getParameter("labelId");// 主单标号
//		// 1.使用反射获取对象的所有属性名称
//		String[] properties = ReflectionUtils
//				.getDeclaredFields(CotLabeldetail.class);
//		// 2.获取页面传入的所有属性的值
//		String[] recordKeys = request
//				.getParameterValues(TableConstants.RECORDKEY_NAME);
//		List<CotLabeldetail> records = new ArrayList<CotLabeldetail>();
//		// 声明需要多少个类
//		String[] eleId = request.getParameterValues("eleId");
//		if (eleId != null) {
//			for (int j = 0; j < eleId.length; j++) {
//				CotLabeldetail labeldetail = new CotLabeldetail();
//				labeldetail.setLabelId(Integer.parseInt(labelId));
//				records.add(labeldetail);
//			}
//		}
//		for (int i = 0; i < properties.length; i++) {
//			// 获取页面传过来的属性值
//			String[] values = request.getParameterValues(properties[i]);
//			if (values != null) {
//				for (int j = 0; j < values.length; j++) {
//					try {
//						// 设置对象个属性
//						BeanUtils.setProperty(records.get(j), properties[i],
//								values[j]);
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//					} catch (InvocationTargetException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//
//		// 返回的结果数组，数组长度一般为增加对象的个数,1为成功，0为失败
//		// 暂时都设为成功
//		int[] resultCodes = new int[records.size()];
//		for (int i = 0; i < resultCodes.length; i++) {
//			resultCodes[i] = 1;
//		}
//		// 返回的信息数组，暂时设为空
//		String[] messages = null;
//		// 调用service更新记录
//		this.getCotLabelService().addList(records);
//		try {
//			super.defaultAjaxResopnse("labelDetail", recordKeys, resultCodes,
//					messages, request, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String labelId = request.getParameter("labelId");// 主单标号
//		String[] properties = ReflectionUtils
//				.getDeclaredFields(CotLabeldetail.class);
//		String[] recordKeys = request
//				.getParameterValues(TableConstants.RECORDKEY_NAME);
//		List<CotLabeldetail> records = new ArrayList<CotLabeldetail>();
//		for (int i = 0; i < recordKeys.length; i++) {
//			CotLabeldetail labeldetail = new CotLabeldetail();
//			labeldetail.setId(new Integer(recordKeys[i]));
//			labeldetail.setLabelId(Integer.parseInt(labelId));
//			records.add(labeldetail);
//
//		}
//		// 设置对象属性信息
//		for (int i = 0; i < properties.length; i++) {
//			String[] values = request.getParameterValues(properties[i]);
//			if (values != null) {
//				for (int j = 0; j < values.length; j++) {
//					try {
//						BeanUtils.setProperty(records.get(j), properties[i],
//								values[j]);
//					} catch (Exception e) {
//						e.printStackTrace();
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
//		this.getCotLabelService().updateList(records);
//		try {
//			super.defaultAjaxResopnse("labelDetail", recordKeys, resultCodes,
//					messages, request, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String labelNoFind = request.getParameter("labelNoFind");// 单号
//		String facIdFind = request.getParameter("facIdFind");// 厂家
//		String startTime = request.getParameter("startTime");
//		String endTime = request.getParameter("endTime");
//
//		StringBuffer queryString = new StringBuffer();
//
//		// 最高权限
//		boolean all = SystemUtil.isAction(request, "cotlabel.do", "ALL");
//		// 部门权限
//		boolean dept = SystemUtil.isAction(request, "cotlabel.do", "DEPT");
//
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if ("admin".equals(emp.getEmpsId())) {
//			queryString.append(" where obj.businessPerson=e.id");
//		} else {
//			// 判断是否有最高权限
//			if (all == true) {
//				queryString.append(" where obj.businessPerson=e.id");
//			} else {
//				// 判断是否有查看该部门权限
//				if (dept == true) {
//					queryString
//							.append(" where obj.businessPerson = e.id and e.deptId="
//									+ emp.getDeptId());
//				} else {
//					queryString
//							.append(" where obj.businessPerson = e.id and obj.businessPerson ="
//									+ emp.getId());
//				}
//			}
//		}
//		if (labelNoFind != null && !labelNoFind.trim().equals("")) {
//			queryString.append(" and obj.labelNo like '%" + labelNoFind.trim()
//					+ "%'");
//		}
//		if (facIdFind != null && !facIdFind.trim().equals("")) {
//			queryString.append(" and obj.factoryId=" + facIdFind.trim());
//		}
//		
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.sendDate >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
//			queryString.append(" and obj.sendDate <='" + endTime
//					+ " 23:59:59'");
//		}
//
//		QueryInfo queryInfo = new QueryInfo();
//
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("labelTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotLabel obj,CotEmps e"
//				+ queryString.toString());
//		// 设置查询记录语句
//		queryInfo.setSelectString("select obj from CotLabel obj,CotEmps e");
//
//		// 设置查询条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id asc");
//
//		Limit limit = RequestUtils.getLimit(request, "labelTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//
//		int totalCount = this.getCotLabelService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, pageCount);
//		int startIndex = limit.getRowStart();
//
//		queryInfo.setStartIndex(startIndex);
//
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//
//		List<?> list = this.getCotLabelService().getList(queryInfo);
//		request.setAttribute("cotLabel", list);
//		request.setAttribute("empMapping", this.getCotLabelService()
//				.getEmpsMap());
//		request.setAttribute("facMapping", this.getCotLabelService()
//				.getFactoryNameMap(request));
//		request.setAttribute("comMapping", this.getCotLabelService()
//				.getCompanyNameMap(request));

		return mapping.findForward("querySuccess");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取页面传入的所有recordKey的属性值
//		String[] recordKeys = request
//				.getParameterValues(TableConstants.RECORDKEY_NAME);
//		int[] resultCodes = new int[0];
//		String[] messages = null;
//		if (recordKeys != null) {
//			List<CotLabeldetail> list = new ArrayList<CotLabeldetail>();
//			for (int i = 0; i < recordKeys.length; i++) {
//				CotLabeldetail labeldetail = new CotLabeldetail();
//				labeldetail.setId(new Integer(recordKeys[i]));
//				list.add(labeldetail);
//
//			}
//			this.getCotLabelService().deleteList(list);
//			resultCodes = new int[recordKeys.length];
//			for (int i = 0; i < resultCodes.length; i++) {
//				resultCodes[i] = 1;
//			}
//		}
//		try {
//			super.defaultAjaxResopnse("labelDetail", recordKeys, resultCodes,
//					messages, request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	// 跳转到标签添加页面
	public ActionForward addLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addLabel");
	}

	// 查询明细信息
	public ActionForward queryLabelDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//		String labelId = request.getParameter("labelId");// 主单单号
//		// if (labelId == null || "".equals(labelId)) {
//		// return null;
//		// }
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where obj.labelId=" + labelId);
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "labelDetail");
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("labelDetail_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotLabeldetail obj"
//				+ queryString);
//		// 设置查询记录语句
//		queryInfo.setSelectString("from CotLabeldetail obj");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id asc");
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
//		List<?> list = this.getCotLabelService().getList(queryInfo);
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = list.size();
//		}
//		// 将明细存到内存中的map
//		Map<?, ?> facMap = this.getCotLabelService().getFactoryNameMap(request);
//		for (int i = 0; i < list.size(); i++) {
//			CotLabeldetail cotLabeldetail = (CotLabeldetail) list.get(i);
//			if (cotLabeldetail.getFactoryId() != null) {
//				cotLabeldetail.setFactoryShortName(facMap.get(
//						cotLabeldetail.getFactoryId().toString()).toString());
//			}
//		}
//
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//
//		request.setAttribute("detail", list);
//		request.setAttribute("allFactoryName", this.getCotLabelService()
//				.getFactoryNameMap(request));
		return mapping.findForward("queryLabelDetail");
	}

	// 浏览样品其他图片
	public ActionForward showPicture(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("showPicture");
	}

	// 显示上传其他图片的层
	public ActionForward showOtherPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showOtherPic");
	}

}
