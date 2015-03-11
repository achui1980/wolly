package com.sail.cot.action.finace;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.finace.CotDealService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;
 
 

public class CotDealAction extends AbstractAction {

	private CotDealService dealService;
	public CotDealService getCotDealService()
	{
		if(dealService==null)
		{
			dealService=(CotDealService) super.getBean("CotDealService");
		}
		return dealService;
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
		String financeNo = request.getParameter("financeNo");//单号
		String startTime = request.getParameter("startTime");//付款日期
		String endTime = request.getParameter("endTime");
		String factoryId = request.getParameter("factoryId");//工厂
		
		StringBuffer queryString=new StringBuffer();
		
		//最高权限
		boolean all = SystemUtil.isAction(request, "cotdeal.do", "ALL");
		//部门权限
		boolean dept = SystemUtil.isAction(request, "cotdeal.do","DEPT");		
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.businessPerson=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				queryString.append(" where obj.businessPerson=e.id");
			} else {
				// 判断是否有查看该部门权限
				if (dept == true) {
					queryString .append(" where obj.businessPerson = e.id and e.deptId=" + emp.getDeptId());
				} else {
					queryString .append(" where obj.businessPerson = e.id and obj.businessPerson =" + emp.getId());
				}
			}
		}
		
		if (financeNo != null && !financeNo.trim().equals("")) {
			queryString.append(" and obj.finaceNo='" + financeNo.trim()+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.amountDate >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.amountDate <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.amountDate between '"+startTime+"' and '"+endTime+"'");
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		
		queryString.append(" and obj.amount > 0");
		
		//如果是预付货款不过滤,如果不是,判断已冲帐金额和未流转金额是不是都为0
		queryString.append(" and (obj.finaceName='预付货款' or (obj.realAmount>0 or obj.zhRemainAmount>0))");
		
		QueryInfo queryInfo = new QueryInfo();
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceAccountdeal obj,CotEmps e"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotFinaceAccountdeal obj,CotEmps e");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
	
		try {
			
			String json = this.getCotDealService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// 查询应付帐冲帐明细
	public ActionForward queryDealDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("queryDeal");
		String dealId = request.getParameter("dealId");// 应付款ID

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.finaceGivenid=p.id");
		
		if (dealId != null && !dealId.trim().equals("")) {
			queryString.append(" and obj.dealId=" + dealId.trim());
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
		queryInfo.setCountQuery("select count(*) from CotFinacegiven p,CotFinacegivenDetail obj"
				+ queryString);
		// 查询语句
		queryInfo.setSelectString("select p.finaceNo,obj.addTime,"
						+ "obj.currencyId,obj.currentAmount from CotFinacegiven p,CotFinacegivenDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int totalCount = this.getCotDealService().getRecordCount(queryInfo);
		
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotDealService().getList(queryInfo);
		List<?> listVo = this.getCotDealService().getDealDetailList(list);
		GridServerHandler gd = new GridServerHandler();
		gd.setData(listVo);
		gd.setTotalCount(totalCount);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询流转记录
	public ActionForward queryTransDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("queryTrans");
		String dealId = request.getParameter("dealId");// 应付款ID

		StringBuffer queryString = new StringBuffer();
		
		queryString.append(" where obj.fkId = f.id");
		
		if (dealId != null && !dealId.trim().equals("")) {
			queryString.append(" and obj.outFlag=" + dealId.trim());
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
		queryInfo.setCountQuery("select count(*) from CotFinaceOther obj,CotOrderOut f"
				+ queryString);
		// 查询语句
		queryInfo.setSelectString("select f.orderNo," +
						"obj.finaceName," +
						"obj.flag," +
						"obj.currencyId," +
						"obj.amount " +
						"from CotFinaceOther obj,CotOrderOut f");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int totalCount = this.getCotDealService().getRecordCount(queryInfo);
		
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		List<?> list = this.getCotDealService().getList(queryInfo);
		List<?> listVo = this.getCotDealService().getTransDetailList(list);
		GridServerHandler gd = new GridServerHandler();
		gd.setData(listVo);
		gd.setTotalCount(totalCount);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}
	 
}
