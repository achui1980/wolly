/**
 * 
 */
package com.sail.cot.action.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.order.CotFaqService;

public class CotFaqAction extends AbstractAction {
	
	private CotFaqService faqService;
	
	public CotFaqService getFaqService() {
		if (faqService == null) {
			faqService = (CotFaqService) super.getBean("CotFaqService");
		}
		return faqService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String flag = request.getParameter("flag");
		String orderId = request.getParameter("orderId");

		if (start == null || limit == null)
			return null;
		StringBuffer queryString = new StringBuffer(" where obj.orderId=d.id");
		
		QueryInfo queryInfo = new QueryInfo();
		
		queryString.append(" and obj.flag="+flag);
		queryString.append(" and obj.orderId="+orderId);
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotQuestion obj,CotOrder d" + queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select new CotQuestion(obj,d.orderNo,d.allPinName,d.custId,d.factoryId,d.orderLcDate) from CotQuestion obj,CotOrder d");
		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by queryTime desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getFaqService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ActionForward queryAnsw(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String questionId = request.getParameter("questionId");

		if (start == null || limit == null)
			return null;
		StringBuffer queryString = new StringBuffer(" where 1=1");
		
		QueryInfo queryInfo = new QueryInfo();
		if(questionId!=null && !questionId.equals("")){
			queryString.append(" and obj.questionId="+questionId);
		}
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotAnwser obj" + queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("from CotAnwser obj");
		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by anwserTime desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getFaqService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward queryHome(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String flag = request.getParameter("flag");
		String queryPerson = request.getParameter("queryPerson");
		String cgk = request.getParameter("cgk");//0:全部提问 2:没有全部答复  3.全部答复
		String orderNoFind = request.getParameter("orderNoFind");
		String startTime = request.getParameter("startTimeFaq");// 报价起始日期
		String endTime = request.getParameter("endTimeFaq");// 报价结束日期
		String typeLv1Id =request.getParameter("typeLv1IdFind");

		if (start == null || limit == null)
			return null;
		StringBuffer queryString = new StringBuffer(" where obj.orderId=p.id");
		
		QueryInfo queryInfo = new QueryInfo();
		
		if(flag!=null && !"".equals(flag)){
			queryString.append(" and obj.flag="+flag);
		}
		if(queryPerson!=null && !"".equals(queryPerson)){
			queryString.append(" and obj.queryPerson="+queryPerson);
		}
		if(orderNoFind!=null && !"".equals(orderNoFind)){
			queryString.append(" and p.orderNo like '%"+orderNoFind+"%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.queryTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			endTime=endTime.substring(0,10);
			queryString.append(" and obj.queryTime <='" + endTime
					+ " 23:59:59'");
		}
		//department
		if (typeLv1Id!= null && !"".equals(typeLv1Id.trim())) {
			queryString.append(" and p.typeLv1Id =" +Integer.parseInt(typeLv1Id));
		}
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotQuestion obj,CotOrder p" + queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select new CotQuestion(obj,p.orderNo,p.allPinName,p.custId,p.factoryId,p.orderLcDate) from CotQuestion obj,CotOrder p");
		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by queryTime desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			//过滤掉orderFac中5个状态为1的记录
			List list = this.getFaqService().getNoApproveData(queryInfo);
			String json = this.getFaqService().getListData(list,cgk);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
