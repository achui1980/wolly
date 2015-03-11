package com.sail.cot.action.customer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.mail.service.MailService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

public class CotCustomerAction extends AbstractAction {

	private CotCustomerService cotCustomerService;
	private CotOrderService cotOrderService;
	private MailService mailService;

	public CotCustomerService getCotCustomerService() {
		if (cotCustomerService == null) {
			cotCustomerService = (CotCustomerService) super
					.getBean("CotCustomerService");
		}
		return cotCustomerService;
	}

	public CotOrderService getCotOrderService() {
		if (cotOrderService == null) {
			cotOrderService = (CotOrderService) super
					.getBean("CotOrderService");
		}
		return cotOrderService;
	}

	public void setCotCustomerService(CotCustomerService cotCustomerService) {
		this.cotCustomerService = cotCustomerService;
	}

	public MailService getMailService() {
		if (mailService == null) {
			mailService = (MailService) super.getBean("mailService");
		}
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer queryString = new StringBuffer();
		queryString
				.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotcustomer.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotcustomer.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nation_Id in(").append(nationStr).append(")");
				}else {
					queryString.append(" and obj.nation_Id = 0");
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
			queryString.append(" or obj.FULL_NAME_EN like '%"+customerShortName +"%'");
			
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
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
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
						"obj.CUSTOMER_ADDR_EN as customerAddrEn," +
						"obj.NATION_ID as nationId," +
						"obj.add_time as addTime" +
						" from cot_customer obj left join cot_emps e on obj.EMP_ID=e.id");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		
		queryInfo.setQueryObjType("CotCustomerVO");
		int count = this.getCotCustomerService().getRecordCountJDBC(queryInfo);
		
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		
		GridServerHandler gd = new GridServerHandler();
		List res = this.getCotCustomerService().getCustVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	public ActionForward uploadPhoto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("uploadPhoto");
	}

	public ActionForward uploadMb(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("uploadMb");
	}

	public ActionForward loadBaseInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("loadBaseInfo");
	}

	public ActionForward loadCustMb(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("loadCustMb");
	}

	public ActionForward loadBusinessInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		return mapping.findForward("loadBusinessInfo");
	}

	public ActionForward loadMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

//		Limit limit = RequestUtils.getLimit(request, "mailTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		Map<?, ?> filterPropertyMap = limit.getFilterSet()
//				.getPropertyValueMap();
//
//		StringBuffer sql = new StringBuffer();
//		String custId = request.getParameter("custId");
//		boolean temp1 = false;
//		temp1 = org.apache.commons.lang.StringUtils.isBlank(custId);
//		if (null != custId && !"".equals(custId) && temp1 == false) {
//			custId = custId.trim();
//			sql.append(" and obj.custId =" + custId);
//		}
//
//		System.out.println(sql.toString());
//
//		QueryInfo queryInfo = new QueryInfo();
//		queryInfo.setCountOnEachPage(15);
//		queryInfo.setCountQuery("select count(*) from CotMail obj where 1=1"
//				+ sql.toString());
//		queryInfo.setSelectString("from CotMail obj where 1=1");
//		queryInfo.setQueryString(sql.toString());
//		queryInfo.setOrderString("");
//		int totalCount = this.getMailService().getRecordCount(queryInfo, 3);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"
//					+ ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",
//					sortValueMap));
//		}
//		List<?> res = this.getMailService().getList(queryInfo, 3);
//		request.setAttribute("cotMail", res);
		return mapping.findForward("loadMail");
	}

	// 查看订单图片
	public ActionForward queryOrderPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			String startDate = request.getParameter("startTime");
			String endDate = request.getParameter("endTime");
			String cId = request.getParameter("cid");
			
			if(start == null || limit == null)
				return mapping.findForward("queryOrderPic");
			int istart = Integer.parseInt(start);
			int ilimit = Integer.parseInt(limit);
			List res = this.getCotOrderService().getOrderImg(cId,startDate,endDate,istart,ilimit);
			int count = this.getCotOrderService().findPicCount(cId,startDate,endDate);
			GridServerHandler gd = new GridServerHandler();
			
			gd.setData(res);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

	// 查看客户索赔信息
	public ActionForward queryClaim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryClaim");
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1 = 1");
		
		String custId = request.getParameter("custId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		if (custId != null && !"".equals(custId.trim())) {
			queryString.append(" and obj.custId =" + custId.trim());
		}else {
			return null;
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.claimTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.claimTime <='" + endTime
					+ " 23:59:59'");
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		queryInfo.setCountQuery("select count(*) from CotClaim obj "+ queryString.toString());
		queryInfo.setSelectString("from CotClaim obj");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotCustomerService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	// 进入客户索赔编辑页面
	public ActionForward claimEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("claimEdit");
	}
	
	// 查看客户图片信息
	public ActionForward queryCustPc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryCustPc");
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1 = 1");
		
		String custId = request.getParameter("custId");

		if (custId != null && !"".equals(custId.trim())) {
			queryString.append(" and obj.custId =" + custId.trim());
		}else {
			return null;
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		queryInfo.setCountQuery("select count(*) from CotCustPc obj "+ queryString.toString());
		queryInfo.setSelectString("from CotCustPc obj");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String[] excludes={"phone"};
			queryInfo.setExcludes(excludes);
			String json = this.getCotCustomerService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	// 进入客户图片编辑页面
	public ActionForward custPcEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("custPcEdit");
	}
	
}
