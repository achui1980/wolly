/**
 * 
 */
package com.sail.cot.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.VOrderOrderfacId;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.QueryService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

public class QueryAction extends AbstractAction {

	private QueryService queryService;

	private CotBaseDao baseDao;

	public CotBaseDao getBaseDao() {
		if (baseDao == null)
			baseDao = (CotBaseDao) SystemUtil.getService("CotBaseDao");
		return baseDao;
	}

	public QueryService getQueryService() {
		if (queryService == null) {
			queryService = (QueryService) super.getBean("QueryService");
		}
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	// 查询集装箱类型
	public ActionForward queryContainerType(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//		Limit limit = RequestUtils.getLimit(request, "containertypeTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		// Map<?, ?> filterPropertyMap =
//		// limit.getFilterSet().getPropertyValueMap();
//
//		String containerNameFind = request.getParameter("containerNameFind");
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where 1=1");
//		if (containerNameFind != null && !containerNameFind.trim().equals("")) {
//			queryString.append(" and obj.containerName like '%"
//					+ containerNameFind.trim() + "%'");
//		}
//
//		QueryInfo queryInfo = new QueryInfo();
//
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("containertypeTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotContainerType obj"
//				+ queryString);
//		// 设置查询记录语句
//		queryInfo.setSelectString("from CotContainerType obj");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString("");
//
//		int totalCount = this.getQueryService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//
//		queryInfo.setStartIndex(startIndex);
//
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"
//					+ ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("cotcontainertype", list);
		return mapping.findForward("queryContainerType");
	}

	// 查询所有员工
	public ActionForward queryEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String empsName = request.getParameter("empNameFind");// 客户中文名
//
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where 1=1");
//		if (empsName != null && !empsName.toString().trim().equals("")) {
//			queryString.append(" and obj.empsName like '%"
//					+ empsName.toString().trim() + "%'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("empTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotEmps obj"
//				+ queryString);
//		// 查询语句
//		queryInfo.setSelectString("from CotEmps obj");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "empTable");
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
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("empList", list);
//		request
//				.setAttribute("allDeptName", this.getQueryService()
//						.getDeptMap());
//		request.setAttribute("allCompanyName", this.getQueryService()
//				.getCompanyMap());
		return mapping.findForward("queryEmp");
	}

	// 查询登录人拥有的客户
	public ActionForward queryCustomer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String customerNo = request.getParameter("customerNoFind");// 客户编号
//		String customerShortName = request
//				.getParameter("customerShortNameFind");// 客户中文名
//		// 获得登录人的empId
//		Integer empId = (Integer) request.getSession().getAttribute("empId");
//		String empNo = (String) request.getSession().getAttribute("empNo");
//
//		StringBuffer queryString = new StringBuffer();
//		if (!"admin".equals(empNo)) {
//			queryString.append(" where obj.empId is null or obj.empId ="
//					+ empId);
//		} else {
//			queryString.append(" where 1=1");
//		}
//		if (customerNo != null && !customerNo.toString().trim().equals("")) {
//			queryString.append(" and obj.customerNo like '%"
//					+ customerNo.toString().trim() + "%'");
//		}
//		if (customerShortName != null
//				&& !customerShortName.toString().trim().equals("")) {
//			queryString.append(" and obj.customerShortName like '%"
//					+ customerShortName.toString().trim() + "%'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
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
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("customerList", list);
		return mapping.findForward("queryCus");
	}

	// 查询登录人拥有的客户
	public ActionForward queryCustomerToContract(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return null;
		
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotcustomer.do", "ALL");
			if (all == false) {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotcustomer.do",
						"DEPT");
				if (dept == true) {
					queryString.append(" and (obj.EMP_ID is null or e.DEPT_ID=" + emp.getDeptId()+")");
				} else {
					queryString.append(" and (obj.EMP_ID is null or obj.EMP_ID ="
							+ emp.getId()+")");
				}
			}
		}
		String customerNo = request.getParameter("custNo");
		String customerShortName = request.getParameter("custShortName");
		String custTypeId = request.getParameter("custTypeId");
		String custLvId = request.getParameter("custLvId");
		String nationId = request.getParameter("nationId");
		String provinceId = request.getParameter("provinceId");
		String cityId = request.getParameter("cityId");
		String eId = request.getParameter("eId");
		if (null != customerNo && !"".equals(customerNo)) {
			customerNo = customerNo.trim();
			queryString.append(" and obj.CUSTOMER_NO like '%" + customerNo + "%'");
		}
		if (null != customerShortName && !"".equals(customerShortName)) {
			customerShortName = customerShortName.trim();
			queryString.append(" and obj.CUSTOMER_SHORT_NAME like '%" + customerShortName
					+ "%'");
		}
		if (null != custTypeId && !"".equals(custTypeId)) {
			custTypeId = custTypeId.trim();
			queryString.append(" and obj.CUST_TYPE_ID =" + custTypeId);
		}
		if (null != custLvId && !"".equals(custLvId)) {
			custLvId = custLvId.trim();
			queryString.append(" and obj.CUST_LV_ID =" + custLvId);
		}
		if (null != nationId && !"".equals(nationId)) {
			nationId = nationId.trim();
			queryString.append(" and obj.NATION_ID =" + nationId);
		}
		if (null != provinceId && !"".equals(provinceId)) {
			provinceId = provinceId.trim();
			queryString.append(" and obj.PROVINCE_ID =" + provinceId);
		}
		if (null != cityId && !"".equals(cityId)) {
			cityId = cityId.trim();
			queryString.append(" and obj.CITY_ID =" + cityId);
		}
		if (null != eId && !"".equals(eId)) {
			eId = eId.trim();
			queryString.append(" and obj.EMP_ID =" + eId);
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		queryInfo.setCountQuery("select count(*) from cot_customer obj left join cot_emps e on obj.EMP_ID=e.id"+ queryString.toString());
		queryInfo
				.setSelectString("select obj.id," +
						"obj.CUSTOMER_NO as customerNo," +
						"obj.CUSTOMER_SHORT_NAME as customerShortName," +
						"obj.CUST_TYPE_ID as custTypeId," +
						"obj.FULL_NAME_EN as fullNameEn," +
						"obj.FULL_NAME_CN as fullNameCn," +
						"obj.CONTACT_NBR as contactNbr," +
						"obj.CUSTOMER_FAX as customerFax," +
						"obj.CUSTOMER_EMAIL as customerEmail," +
						"obj.EMP_ID as empId," +
						"obj.PRI_CONTACT as priContact," +
						"obj.CUSTOMER_ADDR_EN as customerAddrEn" +
						" from cot_customer obj left join cot_emps e on obj.EMP_ID=e.id");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		
		queryInfo.setQueryObjType("CotCustomerVO");
		int count = this.getQueryService().getRecordCountJDBC(queryInfo);
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		
		GridServerHandler gd = new GridServerHandler();
		List res = this.getQueryService().getCustVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询公司
	public ActionForward queryCompanyToContract(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward(null);

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		queryInfo.setStartIndex(Integer.parseInt(start));
		
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotCompany obj ");
		// 设置查询记录语句
		queryInfo.setSelectString("from CotCompany obj ");
		// 设置条件语句
		queryInfo.setQueryString("");
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.companyIsdefault desc");

		// 根据起始
		List<?> list = this.getQueryService().getList(queryInfo);
		int totalCount = this.getQueryService().getRecordCount(queryInfo);
		String[] excludes={"companyLogo"};
		GridServerHandler gd = new GridServerHandler(excludes);
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

	// 查询所有样品
	public ActionForward queryEle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String eleId = request.getParameter("eleIdFind");// 样品编号
		String eleName = request.getParameter("eleNameFind");// 中文名称
		String eleNameEn = request.getParameter("eleNameEnFind");// 英文名称
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String eleCol = request.getParameter("eleColFind");// 颜色
		String child = request.getParameter("childFind");// 子货号标识
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String eleTypenameLv2 = request.getParameter("eleTypenameLv2");// 所属年份
		String eleTypeidLv2 = request.getParameter("eleTypeidLv2");// 产品分类

		String fitId = request.getParameter("fitId");// 配件编号

		if (start == null || limit == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and ele.eleFlag=2");
		} else {
			queryString.append(" and (ele.eleFlag is null or ele.eleFlag!=2)");
		}

		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and ele.eleName like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and ele.eleNameEn like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1="
					+ eleTypeidLv1.toString());
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and ele.eleCol like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (startTime != null && !startTime.trim().equals("")
				&& (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and ele.eleAddTime >='" + startTime + "'");
		}
		if ((startTime == null || startTime.trim().equals(""))
				&& endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and ele.eleAddTime <='" + endTime + "'");
		}
		if (startTime != null && !startTime.trim().equals("")
				&& endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and ele.eleAddTime between '" + startTime
					+ "' and '" + endTime + "'");
		}
		if (eleTypenameLv2 != null
				&& !eleTypenameLv2.toString().trim().equals("")) {
			queryString.append(" and ele.eleTypenameLv2 ="
					+ eleTypenameLv2.toString().trim());
		}
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv2="
					+ eleTypeidLv2.toString());
		}

		if (fitId != null && !fitId.toString().equals("")) {
			// 查找含有该配件的样品编号
			String hql = "select obj.eleId from CotEleFittings obj where obj.fittingId="
					+ fitId;
			List list = this.getBaseDao().find(hql);
			String ids = "";
			for (int i = 0; i < list.size(); i++) {
				Integer id = (Integer) list.get(i);
				ids += id + ",";
			}

			if (!"".equals(ids)) {
				queryString.append(" and ele.id not in ("
						+ ids.substring(0, ids.length() - 1) + ")");
			}
		}

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotElementsNew ele"
				+ queryString);
		String sql = "from CotElementsNew ele";

		queryInfo.setSelectString(sql);
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by ele.id desc");

		// 排除样品的一些属性不加载到json
		String[] excludes = new String[8];
		excludes[0] = "picImg";
		excludes[1] = "cotPictures";
		excludes[2] = "childs";
		excludes[3] = "cotFile";
		excludes[4] = "cotPriceFacs";
		excludes[5] = "cotEleFittings";
		excludes[6] = "cotElePrice";
		excludes[7] = "cotElePacking";
		queryInfo.setExcludes(excludes);

		try {
			String json = this.getBaseDao().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询所有样品
	public ActionForward queryElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;

		String eleId = request.getParameter("eleIdFind");// 样品编号
		String eleName = request.getParameter("eleNameFind");// 中文名称
		String eleNameEn = request.getParameter("eleNameEnFind");// 英文名称
		String eleTypeidLv1 = request.getParameter("query_eleTypeidLv1");// 大类
		String eleTypeidLv2 = request.getParameter("query_eleTypeidLv2");// 中类
		String eleTypeidLv3 = request.getParameter("query_eleTypeidLv3");// 小类
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String eleCol = request.getParameter("eleColFind");// 颜色
		String child = request.getParameter("childFind");// 子货号标识
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		
		String eleSizeDesc = request.getParameter("eleSizeDescFind");// 中文规格
		String eleInchDesc = request.getParameter("eleInchDescFind");// 英文规格

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and ele.eleFlag=2");
		} else {
			queryString.append(" and (ele.eleFlag is null or ele.eleFlag!=2)");
		}

		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and ele.eleName like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and ele.eleNameEn like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1="
					+ eleTypeidLv1.toString());
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and ele.eleCol like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and ele.eleProTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and ele.eleProTime <='" + endTime
					+ " 23:59:59'");
		}
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv2="
					+ eleTypeidLv2.toString());
		}
		if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv3="
					+ eleTypeidLv3.toString());
		}
		if (eleSizeDesc != null && !eleSizeDesc.toString().trim().equals("")) {
			queryString.append(" and ele.eleSizeDesc like '%"
					+ eleSizeDesc.toString().trim() + "%'");
		}
		if (eleInchDesc != null && !eleInchDesc.toString().trim().equals("")) {
			queryString.append(" and ele.eleInchDesc like '%"
					+ eleInchDesc.toString().trim() + "%'");
		}

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotElementsNew ele"
				+ queryString);
		String sql = "from CotElementsNew ele";

		queryInfo.setSelectString(sql);
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by ele.id desc");

		// 排除样品的一些属性不加载到json
		String[] excludes = new String[8];
		excludes[0] = "picImg";
		excludes[1] = "cotPictures";
		excludes[2] = "childs";
		excludes[3] = "cotFile";
		excludes[4] = "cotPriceFacs";
		excludes[5] = "cotEleFittings";
		excludes[6] = "cotElePrice";
		excludes[7] = "cotElePacking";
		queryInfo.setExcludes(excludes);

		try {
			String json = this.getBaseDao().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询所有子样品
	public ActionForward queryChildElements(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//
//		String ids = request.getParameter("childIds");// 子样品编号字符串
//
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where 1=1");
//		if (ids != null && !ids.toString().trim().equals("")) {
//			queryString.append(" and ele.id in ("
//					+ ids.substring(0, ids.length() - 1) + ")");
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
//		queryInfo.setCountQuery("select count(*) from CotElementsNew ele"
//				+ queryString);
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
//		queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("ele",
//				sortValueMap));
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("elementList", list);
//		request.setAttribute("allCurrency", this.getQueryService()
//				.getCurrencyMap(request));
		return mapping.findForward("queryChildElements");
	}

	// 查询厂家信息
	public ActionForward queryFactory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		Limit limit = RequestUtils.getLimit(request, "facTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		// Map<?, ?> filterPropertyMap =
//		// limit.getFilterSet().getPropertyValueMap();
//
//		StringBuffer sql = new StringBuffer();
//		String factoryNo = request.getParameter("facNo");
//		String shortName = request.getParameter("facShortName");
//
//		boolean temp1 = false;
//		temp1 = org.apache.commons.lang.StringUtils.isBlank(factoryNo);
//		if (factoryNo != null && !factoryNo.trim().equals("") && temp1 == false) {
//			sql.append(" and obj.factoryNo like '%" + factoryNo.trim() + "%'");
//		}
//		boolean temp2 = false;
//		temp2 = org.apache.commons.lang.StringUtils.isBlank(shortName);
//		if (shortName != null && !shortName.trim().equals("") && temp2 == false) {
//			sql.append(" and obj.shortName like '%" + shortName.trim() + "%'");
//		}
//		System.out.println(sql.toString());
//
//		QueryInfo queryInfo = new QueryInfo();
//
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("facTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotFactory obj where 1=1"
//				+ sql.toString());
//		queryInfo.setSelectString("from CotFactory obj where 1=1");
//		queryInfo.setQueryString(sql.toString());
//		queryInfo.setOrderString("");
//
//		int totalCount = this.getQueryService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//
//		queryInfo.setStartIndex(startIndex);
//
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"
//					+ ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("cotFactory", list);
		return mapping.findForward("queryFac");

	}

	// 查询订单数据
	public ActionForward queryOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String orderNoFind = request.getParameter("noFind");// 订单单号
//		String custId = request.getParameter("custId");// 客户
//		String bussinessPerson = request.getParameter("person");// 业务员
//		String startTime = request.getParameter("startTime");// 下单起始日期
//		String endTime = request.getParameter("endTime");// 下单结束日期
//		String flag = request.getParameter("flag");// 区分该订单查询用于哪个模块
//		StringBuffer queryString = new StringBuffer();
//		queryString
//				.append(" where obj.bussinessPerson = e.id and obj.custId=c.id");
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if (!"admin".equals(emp.getEmpsId())) {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
//			if (all == false) {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotorder.do",
//						"DEPT");
//				if (dept == true) {
//					queryString.append(" and e.deptId=" + emp.getDeptId());
//				} else {
//					queryString.append(" and obj.bussinessPerson ="
//							+ emp.getId());
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
//			queryString.append(" and obj.bussinessPerson="
//					+ bussinessPerson.trim());
//		}
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.orderTime >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
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
//		queryInfo.setCountQuery("select count(*) from CotOrder as obj,"
//				+ "CotEmps AS e,CotCustomer c" + queryString);
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj.id,obj.orderNo,obj.orderTime,c.customerShortName,e.empsName from CotOrder AS obj,"
//						+ "CotEmps AS e,CotCustomer c");
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
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getOrderVOList(queryInfo);
//		request.setAttribute("cotorder", list);
//		request.setAttribute("allClauseName", this.getQueryService()
//				.getClauseMap(request));
//		return mapping.findForward(flag);
		return null;
	}

	// 配件分析时查询订单明细,用于新增配件时
	public ActionForward queryOrderDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String eleId = request.getParameter("eleIdFindPack");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		String orderId = request.getParameter("orderId");// 主报价单号
		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		
		queryString.append(" where obj.orderId=" + orderId);
		
		if (eleId != null && !eleId.trim().equals("")) {
			queryString.append(" and obj.eleId like '%" + eleId.trim()
					+ "%'");
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
		queryInfo.setCountQuery("select count(*) from CotOrderDetail obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.eleId asc");

		try {
			String json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询报价单数据
	public ActionForward queryPrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String custId = request.getParameter("custId");// 客户ID
//		String priceNoFind = request.getParameter("noFind");// 报价单号
//		String businessPerson = request.getParameter("person");// 业务员
//		String startTime = request.getParameter("startTime");// 报价起始日期
//		String endTime = request.getParameter("endTime");// 报价结束日期
//		String flag = request.getParameter("flag");// 区分该查询用于哪个模块
//
//		StringBuffer queryString = new StringBuffer();
//		queryString
//				.append(" where obj.businessPerson = e.id and obj.custId=c.id");
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if (!"admin".equals(emp.getEmpsId())) {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotprice.do", "ALL");
//			if (all == false) {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotprice.do",
//						"DEPT");
//				if (dept == true) {
//					queryString.append(" and e.deptId=" + emp.getDeptId());
//				} else {
//					queryString.append(" and obj.businessPerson ="
//							+ emp.getId());
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
//		if (businessPerson != null && !businessPerson.trim().equals("")) {
//			queryString.append(" and obj.businessPerson="
//					+ businessPerson.trim());
//		}
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.priceTime >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
//			queryString.append(" and obj.priceTime <='" + endTime
//					+ " 23:59:59'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("priceTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotPrice as obj,"
//				+ "CotEmps AS e,CotCustomer c" + queryString);
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj.id,obj.priceNo,obj.priceTime,obj.clauseId,c.customerShortName from CotPrice AS obj,"
//						+ "CotEmps AS e,CotCustomer c");
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
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getPriceVOList(request,queryInfo);
//		request.setAttribute("cotprice", list);
//		request.setAttribute("allClauseName", this.getQueryService()
//				.getClauseMap(request));
//		return mapping.findForward(flag);
		return null;
	}

	// 查询该报价单的报价明细产品的信息
	public ActionForward queryPriceDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//		String priceId = request.getParameter("priceId");// 主报价单号
//		String flag = request.getParameter("flag");// 区分该查询用于哪个模块
//		if (priceId == null || "".equals(priceId)) {
//			return null;
//		}
//
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where obj.priceId=" + priceId);
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
//
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "queryTable");
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("queryTable_crd");
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
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("detail", list);
//		request.setAttribute("allCurrencyName", this.getQueryService()
//				.getCurrencyMap(request));
//		return mapping.findForward(flag);
		return null;
	}

	public ActionForward queryGiven(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String flag = request.getParameter("flag");// 区分该查询用于哪个模块
//		String givenNo = request.getParameter("noFind");// 单号
//		String custId = request.getParameter("custId");
//		String startTime = request.getParameter("startTime");
//		String endTime = request.getParameter("endTime");
//		String bussinessPerson = request.getParameter("person");
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where obj.empId=e.id and obj.custId=c.id");
//
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if (!"admin".equals(emp.getEmpsId())) {
//			// 最高权限
//			boolean all = SystemUtil.isAction(request, "cotgiven.do", "ALL");
//			// 判断是否有最高权限
//			if (all == false) {
//				// 部门权限
//				boolean dept = SystemUtil.isAction(request, "cotgiven.do",
//						"DEPT");
//				// 判断是否有查看该部门征样的权限
//				if (dept == true) {
//					queryString.append(" and e.deptId=" + emp.getDeptId());
//				} else {
//					queryString.append(" and obj.empId =" + emp.getId());
//				}
//			}
//		}
//
//		if (givenNo != null && !givenNo.trim().equals("")) {
//			givenNo = givenNo.trim();
//			queryString.append(" and obj.givenNo like '%" + givenNo + "%'");
//		}
//		if (custId != null && !custId.trim().equals("")) {
//			queryString.append(" and obj.custId =" + custId.trim());
//		}
//		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
//			queryString.append(" and obj.bussinessPerson="
//					+ bussinessPerson.toString());
//		}
//
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.givenTime >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
//			queryString.append(" and obj.givenTime <='" + endTime
//					+ " 23:59:59'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("givenTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//				.setCountQuery("select count(*) from CotGiven obj,CotEmps e,CotCustomer c"
//						+ queryString);
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj.id,obj.givenNo,c.customerShortName,obj.givenTime,obj.custRequiretime,obj.givenAddr,e.empsName from CotGiven obj,CotEmps e,CotCustomer c");
//
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "givenTable");
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
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getGivenVOList(queryInfo);
//		request.setAttribute("cotGiven", list);
//		return mapping.findForward(flag);
		return null;
	}

	// 查询该送样单的送样明细产品的信息
	public ActionForward queryGivenDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//
//		String flag = request.getParameter("flag");// 区分是报价调用还是订单调用
//		String gId = request.getParameter("givenId");// 主送样单号
//
//		if (gId == null || "".equals(gId)) {
//			return null;
//		}
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where obj.givenId=" + gId);
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 10;
//
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "queryTable");
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("queryTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotGivenDetail obj"
//				+ queryString);
//		// 设置查询记录语句
//		queryInfo.setSelectString("from CotGivenDetail obj");
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
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("detail", list);
//		request.setAttribute("facMapping", this.getQueryService()
//				.getFactoryNameMap(request));
//		request.setAttribute("typeMapping", this.getQueryService().getTypeMap(
//				request));
//		if ("price".equals(flag)) {
//			return mapping.findForward("givenToPrice");
//		} else {
//			return mapping.findForward("givenToOrder");
//		}
		return null;
	}

	// 查询订单明细数据（采购管理调用）
	public ActionForward queryOrderToOrderFac(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String otherFac = request.getParameter("otherFac");// 厂家

		String factoryId = request.getParameter("factoryId");// 厂家
		String orderId = request.getParameter("orderIdFind");// 订单id
		String eleId = request.getParameter("eleIdFind");// 货号
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return null;

		if (factoryId == null || factoryId.trim().equals("")) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1 = 1");
		
		queryString.append(" and obj.unBoxSend4OrderFac = 0 and obj.unBoxCount4OrderFac > 0");// 获取未完全分解及未分解数量大于0的订单

		if (otherFac != null) {
			if (otherFac.equals("on")) {
				factoryId = null;
			}
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.orderId like '%" + orderId.trim()
					+ "%'");
		}
		if (eleId != null && !eleId.trim().equals("")) {
			queryString.append(" and obj.eleId like '%" + eleId.trim() + "%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"picImg"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderDetail obj "+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotOrderDetail obj "); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询订单主单数据
	public ActionForward showOrderByFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String bussinessPerson = request.getParameter("bPerson");// 业务员id
		String orderNo = request.getParameter("noFind");// 订单单号

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return null;
		
		StringBuffer queryString = new StringBuffer();
		//queryString.append(" where 1 = 1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.bussinessPerson = e.id");
		} else {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
			if (all == true) {
				queryString.append(" where obj.bussinessPerson = e.id");
			} else {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorder.do",
						"DEPT");
				if (dept == true) {
					queryString
							.append(" where obj.bussinessPerson = e.id and e.deptId="
									+ emp.getDeptId());
				} else {
					queryString
							.append(" where obj.bussinessPerson = e.id and obj.bussinessPerson ="
									+ emp.getId());
				}
			}
		}

		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
			queryString.append(" and obj.bussinessPerson ="
					+ bussinessPerson.trim());
		}
		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNo.trim()
					+ "%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"cotOrderDetails","orderMBImg"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrder obj,CotEmps e"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotOrder obj,CotEmps e"); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 进入打印全部条码页面
	public ActionForward queryPrintAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("emp", emp);
		return mapping.findForward("queryPrintAll");
	}

	// 查询该报价单的报价明细产品的信息
	public ActionForward addElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addElements");
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	// 查询订单编号数据
	public ActionForward queryOrderNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String orderNoFind = request.getParameter("orderNoFind");// 订单单号
//		String startTime = request.getParameter("startTime");// 下单起始日期
//		String endTime = request.getParameter("endTime");// 下单结束日期
//		StringBuffer queryString = new StringBuffer();
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if ("admin".equals(emp.getEmpsId())) {
//			queryString.append(" where obj.bussinessPerson = e.id");
//		} else {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
//			if (all == true) {
//				queryString.append(" where obj.bussinessPerson = e.id");
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
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.orderTime >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
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
//		queryInfo.setCountQuery("select count(*) from CotOrder as obj,"
//				+ "CotEmps AS e" + queryString);
//		// 查询语句
//		queryInfo.setSelectString("select obj from CotOrder AS obj,"
//				+ "CotEmps AS e");
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
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("cotorder", list);
//		// request.setAttribute("allCusShortName",
//		// this.getOrderService().getCusShortNameMap());

		return mapping.findForward("queryOrderNo");
	}

	// 查询报价单编号数据
	public ActionForward queryPriceNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		String priceNoFind = request.getParameter("priceNoFind");// 报价单号
//		String startTime = request.getParameter("startTime");// 报价起始日期
//		String endTime = request.getParameter("endTime");// 报价结束日期
//		StringBuffer queryString = new StringBuffer();
//
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if ("admin".equals(emp.getEmpsId())) {
//			queryString.append(" where obj.businessPerson = e.id");
//		} else {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotprice.do", "ALL");
//			if (all == true) {
//				queryString.append(" where obj.businessPerson = e.id");
//			} else {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotprice.do",
//						"DEPT");
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
//		if (priceNoFind != null && !priceNoFind.trim().equals("")) {
//			queryString.append(" and obj.priceNo like '%" + priceNoFind.trim()
//					+ "%'");
//		}
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.priceTime >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
//			queryString.append(" and obj.priceTime <='" + endTime
//					+ " 23:59:59'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("priceTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotPrice as obj,"
//				+ "CotEmps AS e" + queryString);
//		// 查询语句
//		queryInfo.setSelectString("select obj from CotPrice AS obj,"
//				+ "CotEmps AS e");
//
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
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("cotprice", list);
//		// request.setAttribute("allCusShortName",
//		// this.getPriceService().getCusShortNameMap());

		return mapping.findForward("queryPriceNo");
	}

	// 查询报价记录(报价管理)
	public ActionForward queryPriceInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// 设置查询参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryPrice");

		String startTime = request.getParameter("startTime");// 报价起始日期
		String endTime = request.getParameter("endTime");// 报价结束日期
		String custId = request.getParameter("custId");// 客户ID
		String eleId = request.getParameter("eleId");// 货号

		String clauseId = request.getParameter("clauseId");
		String currencyId = request.getParameter("currencyId");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.priceId = p.id and p.custId = c.id");
		if (eleId != null && !eleId.trim().equals("")) {
			queryString.append(" and obj.eleId = '" + eleId.trim() + "'");
		}
		if (clauseId != null && !clauseId.trim().equals("")) {
			queryString.append(" and p.clauseId = '" + clauseId.trim() + "'");
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and p.currencyId = '" + currencyId.trim()
					+ "'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.priceTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.priceTime <='" + endTime + " 23:59:59'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and p.custId =" + custId.trim());
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
		queryInfo
				.setCountQuery("select count(*) from CotPriceDetail as obj, CotPrice as p,CotCustomer c"
						+ queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," + "obj.eleId,"
				+ "p.priceNo," + "c.customerShortName," + "obj.pricePrice,"
				+ "p.priceTime" + " from CotPriceDetail as obj,"
				+ " CotPrice as p," + "CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.priceTime desc,p.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getQueryService().getHistoryPriceVOList(queryInfo);
		int totalCount = this.getQueryService().getRecordCount(queryInfo);
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		gd.setData(list);
		gd.setTotalCount(totalCount);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 查询报价记录(外销合同)
	public ActionForward queryOrderPriceInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrderPrice");
		String startTime = request.getParameter("startTime");// 报价起始日期
		String endTime = request.getParameter("endTime");// 报价结束日期
		String custId = request.getParameter("custId");// 客户ID
		System.out.println("cust=====" + custId);
		String clauseId = request.getParameter("clauseId");
		String currencyId = request.getParameter("currencyId");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId = o.id and o.custId = c.id ");
		String eleId = request.getParameter("eleId");// 货号
		if (eleId != null && !eleId.trim().equals("")) {
			queryString.append(" and obj.eleId = '" + eleId.trim() + "'");
		}

		if (clauseId != null && !clauseId.trim().equals("")) {
			queryString.append(" and o.clauseId = '" + clauseId.trim() + "'");
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and o.currencyId = '" + currencyId.trim()
					+ "'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and o.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and o.orderTime <='" + endTime + " 23:59:59'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and o.custId =" + custId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderDetail as obj, CotOrder as o,CotCustomer c"
						+ queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," + "obj.eleId,"
				+ "o.orderNo," + "c.customerShortName," + "obj.orderPrice,"
				+ "o.orderTime" + " from CotOrderDetail as obj, "
				+ "CotOrder as o," + "CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by o.orderTime desc,o.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getQueryService().getHistoryOrderVOList(queryInfo);
		int totalCount = this.getQueryService().getRecordCount(queryInfo);
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		gd.setData(list);
		gd.setTotalCount(totalCount);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 查询报价记录(生产合同)
	public ActionForward queryPriceFacInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;
			//return mapping.findForward("queryPriceFac");
		
		String startTime = request.getParameter("startTime");// 报价起始日期
		String endTime = request.getParameter("endTime");// 报价结束日期
		String factoryId = request.getParameter("factoryId");

		String currencyId = request.getParameter("currencyId");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId = o.id and o.factoryId = f.id ");
		String eleId = request.getParameter("eleId");// 货号
		if (eleId != null && !eleId.trim().equals("")) {
			queryString.append(" and obj.eleId = '" + eleId.trim() + "'");
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and o.currencyId = '" + currencyId.trim()
					+ "'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.addTime <='" + endTime + " 23:59:59'");
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and o.factoryId =" + factoryId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int pageCount = 15;
		// 得到limit对象
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderFacdetail as obj, CotOrderFac as o,CotFactory f"
						+ queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," + "obj.eleId,"
				+ "o.orderNo," + "f.shortName," + "obj.priceFac,"
				+ "obj.addTime" + " from CotOrderFacdetail as obj, "
				+ "CotOrderFac as o," + "CotFactory f");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.addTime desc,obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getQueryService().getHistoryOrderFacVOList(queryInfo);
		int totalCount = this.getQueryService().getRecordCount(queryInfo);
		GridServerHandler gd = null;
		gd = new GridServerHandler();
		gd.setData(list);
		gd.setTotalCount(totalCount);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "priceInfoTable");
//		// 取得排序信息
//		Sort sort = limit.getSort();
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			StringBuffer rs = new StringBuffer();
//			for (Iterator<?> itor = sortValueMap.keySet().iterator(); itor
//					.hasNext();) {
//				Object field = (String) itor.next();
//				String ord = (String) sortValueMap.get(field);
//				if (field.toString().equals("shortName")) {
//					rs.append(" ORDER BY ").append("f").append(".").append(
//							field).append(" ").append(ord);
//				} else if (field.toString().equals("orderNo")) {
//					rs.append(" ORDER BY ").append("o").append(".").append(
//							field).append(" ").append(ord);
//				} else {
//					rs.append(" ORDER BY ").append("obj").append(".").append(
//							field).append(" ").append(ord);
//				}
//				break;
//			}
//			// 设置排序语句
//			queryInfo.setOrderString(rs.toString());
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getHistoryOrderFacVOList(
//				queryInfo);
//		request.setAttribute("priceInfoList", list);
//		return mapping.findForward("queryPriceFac");
	}

	// 查找报关行信息
	public ActionForward queryHsCompany(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("hsCompanyTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotHsCompany as obj");
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj from CotHsCompany AS obj where 1=1");
//		// 设置条件语句
//		queryInfo.setQueryString("");
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "hsCompanyTable");
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("hsCompanylist", list);
		return mapping.findForward("queryHsCompany");
	}

	// 查找拖车行信息
	public ActionForward queryTrailCar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("trailcarTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotTrailCar as obj");
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj from CotTrailCar AS obj where 1=1");
//		// 设置条件语句
//		queryInfo.setQueryString("");
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "trailcarTable");
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("trailcarlist", list);
		return mapping.findForward("queryTrailCar");
	}

	// 查找船运公司信息
	public ActionForward queryConsignCompany(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("consignCompanyTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//				.setCountQuery("select count(*) from CotConsignCompany as obj");
//		// 查询语句
//		queryInfo
//				.setSelectString("select obj from CotConsignCompany AS obj where 1=1");
//		// 设置条件语句
//		queryInfo.setQueryString("");
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "consignCompanyTable");
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("consignCompanylist", list);
		return mapping.findForward("queryConsignCompany");
	}

	// 进入打印信息页面
	public ActionForward showPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("showPrint");
	}

	// 查询目的港
	public ActionForward queryTargetPort(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//		Limit limit = RequestUtils.getLimit(request, "targetportTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		Map<?, ?> filterPropertyMap = limit.getFilterSet()
//				.getPropertyValueMap();
//
//		String targetPortName = request.getParameter("Name");
//		String targetPortEnName = request.getParameter("EnName");
//		StringBuffer sql = new StringBuffer();
//		if (null != targetPortName && !"".equals(targetPortName)) {
//			targetPortName = targetPortName.trim();
//			sql.append(" and obj.targetPortName like '%" + targetPortName
//					+ "%'");
//		}
//		if (null != targetPortEnName && !"".equals(targetPortEnName)) {
//			targetPortEnName = targetPortEnName.trim();
//			sql.append(" and obj.targetPortEnName like '%" + targetPortEnName
//					+ "%'");
//		}
//
//		QueryInfo queryInfo = new QueryInfo();
//
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("targetportTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//				.setCountQuery("select count(*) from CotTargetPort obj where 1=1"
//						+ sql.toString());
//
//		queryInfo.setSelectString("from CotTargetPort obj where 1=1");
//
//		queryInfo.setQueryString(sql.toString());
//
//		queryInfo.setOrderString("");
//
//		int totalCount = this.getQueryService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//
//		queryInfo.setStartIndex(startIndex);
//
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"
//					+ ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("cotTargetPort", list);
		return mapping.findForward("queryTargetPort");
	}

	// 查询国别
	public ActionForward queryNation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		Limit limit = RequestUtils.getLimit(request, "nationTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		Map<?, ?> filterPropertyMap = limit.getFilterSet()
//				.getPropertyValueMap();
//
//		QueryInfo queryInfo = new QueryInfo();
//
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("nationTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotNation obj where 1=1");
//		// 设置查询记录语句
//		queryInfo.setSelectString("from CotNation obj");
//		// 设置查询条件语句
//		queryInfo.setQueryString("");
//		// 设置排序语句
//		queryInfo.setOrderString("");
//
//		int totalCount = this.getQueryService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//
//		queryInfo.setStartIndex(startIndex);
//
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"
//					+ ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("cotNation", list);
		return mapping.findForward("queryNation");
	}

	// 查询报价主单和明细数据
	public ActionForward queryPriceAndDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;
		String custId = request.getParameter("custIdFind");// 客户ID
		String priceNoFind = request.getParameter("noFind");// 报价单号
		String eleIdFind = request.getParameter("eleIdFind");// 货号
		String businessPerson = request.getParameter("person");// 业务员
		String startTime = request.getParameter("startTime");// 报价起始日期
		String endTime = request.getParameter("endTime");// 报价结束日期
		String eleSizeDesc = request.getParameter("eleSizeDesc");// 中文规格
		String typeLv1Id = request.getParameter("typeLv1Id");// 中文规格

		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.businessPerson = e.id and obj.custId=c.id and p.priceId=obj.id");
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
		if (priceNoFind != null && !priceNoFind.trim().equals("")) {
			queryString.append(" and obj.priceNo like '%" + priceNoFind.trim()
					+ "%'");
		}
		if (eleIdFind != null && !eleIdFind.trim().equals("")) {
			queryString
					.append(" and p.eleId like '%" + eleIdFind.trim() + "%'");
		}
		if (eleSizeDesc != null && !eleSizeDesc.trim().equals("")) {
			queryString
					.append(" and p.eleSizeDesc like '%" + eleSizeDesc.trim() + "%'");
		}
		if (typeLv1Id != null && !typeLv1Id.trim().equals("")) {
			queryString.append(" and p.eleTypeidLv1="
					+ typeLv1Id.trim());
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

		int pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotPrice as obj,CotPriceDetail AS p,"
						+ "CotEmps AS e,CotCustomer c" + queryString);
		// 查询语句
		queryInfo.setSelectString("select p.id," + "obj.priceNo,"
				+ "obj.priceTime," + "obj.clauseId," + "c.customerShortName,"
				+ "p.eleId," + "p.eleNameEn," + "p.priceFac,"
				+ "p.priceFacUint," + "p.pricePrice," + "p.currencyId," + "p.eleSizeDesc" 
				+ " from CotPrice AS obj,CotPriceDetail AS p,"
				+ "CotEmps AS e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.sortNo");

		int count = this.getQueryService().getRecordCount(queryInfo);
		List<?> list = this.getQueryService().getPriceVOList(request,queryInfo);
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

	// 查询订单和订单明细数据
	public ActionForward queryOrderAndDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String orderNoFind = request.getParameter("noFind");// 订单单号
		String eleIdFind = request.getParameter("eleIdFind");// 货号
		String custId = request.getParameter("custIdFind");// 客户
		String bussinessPerson = request.getParameter("person");// 业务员
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.bussinessPerson = e.id and obj.custId=c.id and p.orderId=obj.id");
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

		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim()
					+ "%'");
		}
		if (eleIdFind != null && !eleIdFind.trim().equals("")) {
			queryString
					.append(" and p.eleId like '%" + eleIdFind.trim() + "%'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId=" + custId.trim());
		}
		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
			queryString.append(" and obj.bussinessPerson="
					+ bussinessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderTime <='" + endTime
					+ " 23:59:59'");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();

		int pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrder as obj,CotOrderDetail AS p,"
						+ "CotEmps AS e,CotCustomer c" + queryString);
		// 查询语句
		queryInfo.setSelectString("select p.id," + "obj.orderNo,"
				+ "obj.orderTime," + "c.customerShortName," + "e.empsName,"
				+ "p.eleId," + "p.eleNameEn," + "p.boxCount," + "p.totalMoney,"
				+ "p.orderPrice," + "p.currencyId," + "p.custNo"
				+ ",p.boxTypeId "
				+ " from CotOrder AS obj,CotOrderDetail AS p,"
				+ "CotEmps AS e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.sortNo");

		int count = this.getQueryService().getRecordCount(queryInfo);
		List<?> list = this.getQueryService().getOrderVOList(queryInfo);
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

	public ActionForward queryGivenAndDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String givenNo = request.getParameter("noFind");// 单号
		String eleIdFind = request.getParameter("eleIdFind");// 货号
		String custId = request.getParameter("custIdFind");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String bussinessPerson = request.getParameter("person");
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.bussinessPerson = e.id and obj.custId=c.id and p.givenId=obj.id");

		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 最高权限
			boolean all = SystemUtil.isAction(request, "cotgiven.do", "ALL");
			// 判断是否有最高权限
			if (all == false) {
				// 部门权限
				boolean dept = SystemUtil.isAction(request, "cotgiven.do",
						"DEPT");
				// 判断是否有查看该部门征样的权限
				if (dept == true) {
					queryString.append(" and e.deptId=" + emp.getDeptId());
				} else {
					queryString.append(" and obj.bussinessPerson ="
							+ emp.getId());
				}
			}
		}

		if (givenNo != null && !givenNo.trim().equals("")) {
			givenNo = givenNo.trim();
			queryString.append(" and obj.givenNo like '%" + givenNo + "%'");
		}
		if (eleIdFind != null && !eleIdFind.trim().equals("")) {
			queryString
					.append(" and p.eleId like '%" + eleIdFind.trim() + "%'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId =" + custId.trim());
		}
		if (bussinessPerson != null && !bussinessPerson.trim().equals("")) {
			queryString.append(" and obj.bussinessPerson="
					+ bussinessPerson.toString());
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.givenTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.givenTime <='" + endTime
					+ " 23:59:59'");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();

		int pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotGiven as obj,CotGivenDetail AS p,"
						+ "CotEmps AS e,CotCustomer c" + queryString);
		// 查询语句
		queryInfo.setSelectString("select p.id," + "obj.givenNo,"
				+ "c.customerShortName," + "obj.givenTime,"
				+ "obj.custRequiretime," + "obj.givenAddr," + "e.empsName,"
				+ "p.eleId," + "p.eleNameEn"
				+ " from CotGiven AS obj,CotGivenDetail AS p,"
				+ "CotEmps AS e,CotCustomer c");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by p.sortNo");

		int count = this.getQueryService().getRecordCount(queryInfo);
		List<?> list = this.getQueryService().getGivenVOList(queryInfo);
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

	// 征样查询
	public ActionForward queryGivenToSign(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;
		String gId = request.getParameter("givenId");// 主送样单号
		StringBuffer queryString = new StringBuffer();
		if (gId == null || "".equals(gId)) {
			return null;
		} else {
			queryString.append(" where obj.givenId=" + gId
					+ " and obj.signFlag=0");
		}
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotGivenDetail obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotGivenDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;

	}

	// 查询主订单数据,标签管理调用
	public ActionForward queryOrderByLabel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//		String orderNoFind = request.getParameter("orderNoFind");// 订单单号
//		String custId = request.getParameter("custId");// 客户
//		String bussinessPerson = request.getParameter("businessPerson");// 业务员
//		String startTime = request.getParameter("startTime");// 下单起始日期
//		String endTime = request.getParameter("endTime");// 下单结束日期
//		StringBuffer queryString = new StringBuffer();
//		queryString
//				.append(" where obj.bussinessPerson = e.id and obj.custId=c.id and (obj.labelImpstatus is null or obj.labelImpstatus=0)");
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if (!"admin".equals(emp.getEmpsId())) {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
//			if (all == false) {
//				// 判断是否有查看该部门报价的权限
//				boolean dept = SystemUtil.isAction(request, "cotorder.do",
//						"DEPT");
//				if (dept == true) {
//					queryString.append(" and e.deptId=" + emp.getDeptId());
//				} else {
//					queryString.append(" and obj.bussinessPerson ="
//							+ emp.getId());
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
//			queryString.append(" and obj.bussinessPerson="
//					+ bussinessPerson.trim());
//		}
//		if (startTime != null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.orderTime >='" + startTime + "'");
//		}
//		if (endTime != null && !"".equals(endTime.trim())) {
//			queryString.append(" and obj.orderTime <='" + endTime
//					+ " 23:59:59'");
//		}
//
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 20;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("orderTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotOrder as obj,"
//				+ "CotEmps AS e,CotCustomer c" + queryString);
//		// 查询语句
//		queryInfo.setSelectString("select obj.id," + "obj.orderTime,"
//				+ "obj.sendTime," + "c.customerShortName," + "obj.orderNo,"
//				+ "e.empsName, " + "obj.clauseTypeId," + "obj.currencyId,"
//				+ "obj.payTypeId," + "obj.totalCount," + "obj.totalContainer,"
//				+ "obj.totalCBM," + "obj.totalMoney " + "from CotOrder AS obj,"
//				+ "CotEmps AS e,CotCustomer c");
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
//			StringBuffer rs = new StringBuffer();
//			for (Iterator<?> itor = sortValueMap.keySet().iterator(); itor
//					.hasNext();) {
//				Object field = (String) itor.next();
//				String ord = (String) sortValueMap.get(field);
//				if (field.toString().equals("customerShortName")) {
//					rs.append(" ORDER BY ").append("c").append(".").append(
//							field).append(" ").append(ord);
//				} else if (field.toString().equals("empsName")) {
//					rs.append(" ORDER BY ").append("e").append(".").append(
//							field).append(" ").append(ord);
//				} else {
//					rs.append(" ORDER BY ").append("obj").append(".").append(
//							field).append(" ").append(ord);
//				}
//				break;
//			}
//			// 设置排序语句
//			queryInfo.setOrderString(rs.toString());
//		}
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getOrderVOs(queryInfo);
//		request.setAttribute("cotorder", list);
//		request.setAttribute("allCurrencyName", this.getQueryService()
//				.getCurrencyMap(request));
//		request.setAttribute("allClauseName", this.getQueryService()
//				.getClauseMap(request));
//		request.setAttribute("allPayTypeName", this.getQueryService()
//				.getPayTypeMap(request));

		return mapping.findForward("queryOrderByLabel");
	}

	// 查询样品配件
	public ActionForward queryFitting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String fitNo = request.getParameter("fitNoFind");// 材料编号
		String fitName = request.getParameter("fitNameFind");// 配件名称
		String typeLv3Id = request.getParameter("typeLv3Id");// 配件类型

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		QueryInfo queryInfo = new QueryInfo();

		if (fitNo != null && !fitNo.trim().equals("")) {
			queryString.append(" and obj.fitNo like '%" + fitNo.trim() + "%'");
		}
		if (fitName != null && !fitName.trim().equals("")) {
			queryString.append(" and obj.fitName like '%" + fitName.trim() + "%'");
		}
		if (typeLv3Id != null && !typeLv3Id.trim().equals("")) {
			queryString.append(" and obj.typeLv3Id=" + typeLv3Id);
		}

		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFittings obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotFittings obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);

		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查找数据字典合同条款
	public ActionForward queryContract(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String keyWord = request.getParameter("keyWord");// 关键字
//
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where 1=1");
//		if (keyWord != null && !keyWord.trim().equals("")) {
//			queryString.append(" and obj.contractContent like '%"
//					+ keyWord.trim() + "%'");
//		}
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		Integer pageCount = 10;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("contractTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotContract as obj"
//				+ queryString);
//		// 查询语句
//		queryInfo.setSelectString("from CotContract AS obj ");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.id desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "contractTable");
//
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getQueryService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getQueryService().getList(queryInfo);
//		request.setAttribute("contractList", list);
		return mapping.findForward("queryContract");
	}

	// 跳转到选择同步字段的页面
	public ActionForward queryTong(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryTong");
	}

	// 根据父货号查询所有子样品
	public ActionForward queryChildEles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;

		String parentId = request.getParameter("parentId");// 父样品货号
		String childEleId = request.getParameter("childEleId");// 子货号样品货号
		String factoryId = request.getParameter("factoryId");// 厂家
		String eleName = request.getParameter("eleName");// 中文名

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where ele.eleFlag=2");
		if (parentId != null && !parentId.toString().equals("") && !parentId.toString().equals("-1")) {
			queryString.append(" and ele.eleParentId=" + parentId.toString());
		}
		if (childEleId != null && !childEleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%"
					+ childEleId.toString().trim() + "%'");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and ele.eleName like '%"
					+ eleName.toString().trim() + "%'");
		}

		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId.toString());
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotElementsNew ele"
				+ queryString);
		String sql = "from CotElementsNew ele";

		queryInfo.setSelectString(sql);
		// 设置查询参数
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by ele.id desc");
		int totalRows = this.getQueryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getQueryService().getList(queryInfo);
		// 判断是否有权限查看样品图片
		this.getQueryService().findIsSelPic(request, list);
		// 判断是否有权限查看报价信息
		try {
			List<?> listNew = this.getQueryService().findIsSelPrice(request,
					list);
			String excludes[] = { "cotPictures", "childs", "cotFile",
					"cotPriceFacs", "cotEleFittings", "cotElePrice",
					"cotElePacking" };
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
	
	public ActionForward queryOrderMb(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String orderIds = request.getParameter("orderIds"); //采购单号
		if (orderIds == null || orderIds.equals("")) {
			return null;
		}
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return null;
		
		StringBuffer queryString = new StringBuffer();
		//queryString.append(" where 1 = 1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.bussinessPerson = e.id");
		} else {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
			if (all == true) {
				queryString.append(" where obj.bussinessPerson = e.id");
			} else {
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorder.do",
						"DEPT");
				if (dept == true) {
					queryString
							.append(" where obj.bussinessPerson = e.id and e.deptId="
									+ emp.getDeptId());
				} else {
					queryString
							.append(" where obj.bussinessPerson = e.id and obj.bussinessPerson ="
									+ emp.getId());
				}
			}
		}
		
		if (orderIds.trim() != null && !"".equals(orderIds.trim()) ) {
			if ("admin".equals(emp.getEmpsId())) {
				queryString.append(" and obj.id in("+orderIds+")");
			}else{
				queryString.append(" and obj.id in("+orderIds+") and (obj.orderStatus = 2 or obj.orderStatus = 9)");
			}
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		String [] filter = {"cotOrderDetails","orderMBImg"};
		queryInfo.setExcludes(filter);
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrder obj,CotEmps e"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotOrder obj,CotEmps e"); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	// 查询该订单的剩余订单明细产品的信息
	public ActionForward queryRemainOrderDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryRemainOrderDetail");
		
		String orderId = request.getParameter("oId");// 主订单单号
		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.unBoxCount4OrderFac>0 and obj.orderId=" + orderId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderDetail obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getQueryService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	// 根据客户Id查询相应的单据
	public ActionForward queryOrderByCust(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		String custId = request.getParameter("id");// 主订单单号
		String table = request.getParameter("table");
		String query = request.getParameter("query");//模糊查询的值
//		if (custId == null || "".equals(custId)) {
//			return null;
//		}
		if("".equals(query))
			query = null;
		StringBuffer queryString = new StringBuffer();
		queryString.append("");
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		if("order".equals(table)){
			if (custId != null && !"".equals(custId)) 
				queryString.append(" and obj.custId="+custId);
			if(query != null)
				queryString.append(" and obj.orderNo like '%"+query+"%'");
			queryInfo.setSelectString("select obj.orderNo from CotOrder obj where 1=1 "+ queryString);
			queryInfo.setCountQuery("select count(*) from CotOrder obj where 1=1"+queryString );
		
		}else if("price".equals(table)){
			if (custId != null && !"".equals(custId)) 
				queryString.append(" and obj.custId="+custId);
			if(query != null)
				queryString.append(" and obj.priceNo like '%"+query+"%'");
			queryInfo.setSelectString("select obj.priceNo from CotPrice obj where 1=1 "+ queryString);
			queryInfo.setCountQuery("select count(*) from CotPrice obj where 1=1 "+queryString);
		
		}else if("given".equals(table)){
			if (custId != null && !"".equals(custId)) 
				queryString.append(" and obj.custId="+custId);
			if(query != null)
				queryString.append(" and obj.givenNo like '%"+query+"%'");
			queryInfo.setSelectString("select obj.givenNo from CotGiven obj where 1=1 " +queryString);
			queryInfo.setCountQuery("select count(*) from CotGiven obj where 1=1 " +queryString);

		}
		// 设置每页显示多少行
		if(limit==null){
			limit="10";
		}
		int pageCount = Integer.parseInt(limit);
		queryInfo.setQueryString("");
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.addTime desc");
		if(start==null){
			start="0";
		}
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			int totalCount = this.getQueryService().getRecordCount(queryInfo);
			List list = this.getQueryService().findRecords(queryInfo);
			List arr = new ArrayList();
			//创建一个动态对象
			DynaProperty properties[] = new DynaProperty[]{
					new DynaProperty("orderNo", String.class)
			};
			DynaClass myBeanClass = new BasicDynaClass("mybean", null, properties);
			for(int i=0; i<list.size(); i++)
			{
				//创建动态对象
				DynaBean myBean = myBeanClass.newInstance();
				myBean.set("orderNo", list.get(i));
				arr.add(myBean);
			}
			

			GridServerHandler gd = new GridServerHandler();
			gd.setTotalCount(totalCount);
			gd.setData(arr);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	// 根据客户Id查询相应的单据
	public ActionForward queryOrderFacByFac(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		String facId = request.getParameter("id");// 主订单单号
		String table = request.getParameter("table");
		String query = request.getParameter("query");//模糊查询的值
//		if (custId == null || "".equals(custId)) {
//			return null;
//		}
		if("".equals(query))
			query = null;
		StringBuffer queryString = new StringBuffer();
		queryString.append("");
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		if("orderfac".equals(table)){
			if (facId != null && !"".equals(facId)) 
				queryString.append(" and obj.factoryId="+facId);
			if(query != null)
				queryString.append(" and obj.orderNo like '%"+query+"%'");
			queryInfo.setSelectString("select obj.orderNo from CotOrderFac obj where 1=1 "+ queryString);
			queryInfo.setCountQuery("select count(*) from CotOrderFac obj where 1=1"+queryString );
		
		}
		// 设置每页显示多少行
		if(limit==null){
			limit="10";
		}
		int pageCount = Integer.parseInt(limit);
		queryInfo.setQueryString("");
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.addTime desc");
		if(start==null){
			start="0";
		}
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			int totalCount = this.getQueryService().getRecordCount(queryInfo);
			List list = this.getQueryService().findRecords(queryInfo);
			List arr = new ArrayList();
			//创建一个动态对象
			DynaProperty properties[] = new DynaProperty[]{
					new DynaProperty("orderNo", String.class)
			};
			DynaClass myBeanClass = new BasicDynaClass("mybean", null, properties);
			for(int i=0; i<list.size(); i++)
			{
				//创建动态对象
				DynaBean myBean = myBeanClass.newInstance();
				myBean.set("orderNo", list.get(i));
				arr.add(myBean);
			}
			

			GridServerHandler gd = new GridServerHandler();
			gd.setTotalCount(totalCount);
			gd.setData(arr);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	// 根据客户Id查询相应的单据
	public ActionForward showOrderCustByFac(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		 
		String orderfacId = request.getParameter("orderfacId");// 主订单单号

		if(orderfacId == null) return null;
		StringBuffer queryString = new StringBuffer();
		queryString.append(" from VOrderOrderfacId obj where obj.orderFacId="+orderfacId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt("1000");
		queryInfo.setSelectString(queryString.toString());
		queryInfo.setQueryString("");
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt("0");	
		queryInfo.setStartIndex(startIndex);
		try {
			//int totalCount = this.getQueryService().getRecordCount(queryInfo);
			List list = this.getQueryService().findRecords(queryInfo);
			List arr = new ArrayList();
			//创建一个动态对象
			DynaProperty properties[] = new DynaProperty[]{
					new DynaProperty("custId", Integer.class),
					new DynaProperty("orderNoOrd",String.class),
					new DynaProperty("poNo",String.class)
			};
			DynaClass myBeanClass = new BasicDynaClass("mybean", null, properties);
			Map custMap = new HashMap();
			for(int i=0; i<list.size(); i++)
			{
				VOrderOrderfacId orderfac = (VOrderOrderfacId)list.get(i);
				Map objMap = new HashMap();
				objMap.put("custId", orderfac.getCustId());
				objMap.put("orderNoOrd", orderfac.getOrderNoOrd());
				objMap.put("poNo", orderfac.getPoNo());
				custMap.put(orderfac.getCustId(),objMap);
			}
			Iterator iterator = custMap.keySet().iterator();
			while(iterator.hasNext()){
				Object key = iterator.next();
				//创建动态对象
				DynaBean myBean = myBeanClass.newInstance();
				Map objMap = (Map)custMap.get(key);
				myBean.set("custId",objMap.get("custId"));
				myBean.set("orderNoOrd", objMap.get("orderNoOrd"));
				myBean.set("poNo", objMap.get("poNo"));
				arr.add(myBean);
			}
			

			GridServerHandler gd = new GridServerHandler();
			gd.setTotalCount(arr.size());
			gd.setData(arr);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	// 查询询盘主单和询盘明细数据
	public ActionForward queryPanAndEle(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return null;
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.panId=p.id and p.addPerson=e.id and p.deptId=d.id and obj.state=1");
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String deptId = request.getParameter("deptId");
		String addPerson = request.getParameter("addPerson");
		String manufactorer = request.getParameter("manufactorer");
		String eleNameEn = request.getParameter("eleNameFind");
		String priceNo = request.getParameter("priceNoFind");
		String prsNo = request.getParameter("prsNo");
		String eleId = request.getParameter("eleIdFind");
		
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString
					.append(" and p.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.addTime <='" + endTime
					+ " 23:59:59'");
		}
		if (deptId != null && !deptId.toString().trim().equals("")) {
			queryString.append(" and p.deptId="
					+ deptId.toString().trim());
		}
		if (addPerson != null && !addPerson.toString().trim().equals("")) {
			queryString.append(" and p.addPerson="
					+ addPerson.toString().trim());
		}
		if (manufactorer != null && !manufactorer.toString().trim().equals("")) {
			queryString.append(" and obj.manufactorer like '%"
					+ manufactorer.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and obj.eleNameEn like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (priceNo != null && !priceNo.toString().trim().equals("")) {
			queryString.append(" and p.priceNo like '%"
					+ priceNo.toString().trim() + "%'");
		}
		if (prsNo != null && !prsNo.toString().trim().equals("")) {
			queryString.append(" and p.prsNo like '%"
					+ prsNo.toString().trim() + "%'");
		}
		if (eleId != null && !"".equals(eleId.trim())) {
			queryString.append(" and obj.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPanEle obj,CotPan p,CotEmps e,CotTypeLv1 d"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("select new CotPanEle(obj,p,e.empsName,d.typeEnName) from CotPanEle obj,CotPan p,CotEmps e,CotTypeLv1 d");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
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
	
	// 查询po和po明细数据
	public ActionForward queryOrderFacAndDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String factoryId = request.getParameter("factoryFacId");// 订单单号
		String orderNoFind = request.getParameter("noFind");// 订单单号
		String eleIdFind = request.getParameter("eleIdFind");// 货号
		String businessPerson = request.getParameter("person");// 业务员
		String startTime = request.getParameter("startTime");// 下单起始日期
		String endTime = request.getParameter("endTime");// 下单结束日期
		String typeLv1Id = request.getParameter("typeLv1Id");// 下单结束日期
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where obj.businessPerson = e.id and p.orderId=obj.id");
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
					queryString.append(" and obj.businessPerson ="
							+ emp.getId());
				}
			}
		}

		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim()
					+ "%'");
		}
		if (eleIdFind != null && !eleIdFind.trim().equals("")) {
			queryString
					.append(" and p.eleId like '%" + eleIdFind.trim() + "%'");
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId="
					+ factoryId.trim());
		}
		if (typeLv1Id != null && !typeLv1Id.trim().equals("")) {
			queryString.append(" and obj.typeLv1Id="
					+ typeLv1Id.trim());
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.businessPerson="
					+ businessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderTime <='" + endTime
					+ " 23:59:59'");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();

		int pageCount = Integer.parseInt(limit);
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotOrderFac as obj,CotOrderFacdetail AS p,"
						+ "CotEmps AS e" + queryString);
		// 查询语句
		queryInfo.setSelectString("select p.id," + "obj.orderNo,"
				+ "obj.orderTime," + "e.empsName,"
				+ "p.eleId," + "p.eleNameEn," + "p.boxCount," + "p.totalMoney,"
				+ "p.priceFac," + "obj.currencyId," + "p.custNo"
				+ ",p.boxTypeId "
				+ " from CotOrderFac AS obj,CotOrderFacdetail AS p,"
				+ "CotEmps AS e");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.orderNo desc,p.sortNo");

		int count = this.getQueryService().getRecordCount(queryInfo);
		List<?> list = this.getQueryService().getOrderFacVOList(queryInfo);
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
	
}
