/**
 * 
 */
package com.sail.cot.action.finace;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinacegivenDetail;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.finace.CotFinanceGivenService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotFinanceGivenAction extends AbstractAction {

	private CotFinanceGivenService financeGivenService;

	// 查询收款记录主单数据
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		String financeNo = request.getParameter("financeNo");// 单号
		String startTime = request.getParameter("startTime");// 收款日期
		String endTime = request.getParameter("endTime");
		String factoryId = request.getParameter("factoryId");// 供应商
		String currencyId = request.getParameter("currencyId");//币种
		String givenPerson = request.getParameter("givenPerson");// 付款人

		StringBuffer queryString = new StringBuffer();
		// 最高权限
		boolean all = SystemUtil.isAction(request, "cotfinancegiven.do", "ALL");
		// 部门权限
		boolean dept = SystemUtil.isAction(request, "cotfinancegiven.do",
				"DEPT");

		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.addPerson=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				queryString.append(" where obj.addPerson=e.id");
			} else {
				// 判断是否有查看该部门权限
				if (dept == true) {
					queryString
							.append(" where obj.addPerson = e.id and e.deptId="
									+ emp.getDeptId());
				} else {
					queryString
							.append(" where obj.addPerson = e.id and obj.addPerson ="
									+ emp.getId());
				}
			}
		}

		if (financeNo != null && !financeNo.trim().equals("")) {
			queryString.append(" and obj.finaceNo like '%" + financeNo.trim()
					+ "%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.finaceRecvDate >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.finaceRecvDate <='" + endTime
					+ " 23:59:59'");
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and obj.currencyId=" + currencyId.trim());
		}
		if (givenPerson != null && !givenPerson.trim().equals("")) {
			queryString.append(" and obj.givenPerson =" + givenPerson.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinacegiven obj,CotEmps e"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotFinacegiven obj,CotEmps e");

		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String excludes[] = { "cotFinacegivenDetails" };
		queryInfo.setExcludes(excludes);
		// 根据起始
		try {
			String json = this.getFinanceGivenService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存冲帐明细
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String mainId = request.getParameter("mainId");// 外键
		String curId = request.getParameter("curId");// 币种
		// 操作时间
		Date addTime = new Date();

		List<CotFinacegivenDetail> records = new ArrayList<CotFinacegivenDetail>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinacegivenDetail.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFinacegivenDetail finacerecvDetail = new CotFinacegivenDetail();
				for (int i = 0; i < properties.length; i++) {
					if ("finaceDate".equals(properties[i])) {
						JSONObject date = JSONObject.fromObject(jsonObject
								.get(properties[i]));
						finacerecvDetail.setFinaceDate(new java.sql.Date(
								(Long) date.get("time")));
					} else {
						BeanUtils.setProperty(finacerecvDetail, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				finacerecvDetail.setBusinessPerson(empId);
				finacerecvDetail.setFinaceGivenid(Integer.parseInt(mainId));
				finacerecvDetail.setCurrencyId(Integer.parseInt(curId));
				finacerecvDetail.setAddTime(addTime);
				records.add(finacerecvDetail);

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					CotFinacegivenDetail finacerecvDetail = new CotFinacegivenDetail();
					for (int j = 0; j < properties.length; j++) {
						if ("finaceDate".equals(properties[j])) {
							JSONObject date = JSONObject.fromObject(jsonObject
									.get(properties[j]));
							finacerecvDetail.setFinaceDate(new java.sql.Date(
									(Long) date.get("time")));
						} else {
							BeanUtils.setProperty(finacerecvDetail,
									properties[j], jsonObject
											.get(properties[j]));
						}
					}
					finacerecvDetail.setBusinessPerson(empId);
					finacerecvDetail.setFinaceGivenid(Integer.parseInt(mainId));
					finacerecvDetail.setCurrencyId(Integer.parseInt(curId));
					finacerecvDetail.setAddTime(addTime);
					records.add(finacerecvDetail);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getFinanceGivenService().addRecvList(request, records);
		return null;
	}

	// 修改
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinacegivenDetail.class);
		List<CotFinacegivenDetail> records = new ArrayList<CotFinacegivenDetail>();
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFinacegivenDetail finacerecvDetail = this
						.getFinanceGivenService().getFinacegivenDetailById(
								Integer.parseInt(jsonObject.getString("id")));
				map.put(finacerecvDetail.getId(), finacerecvDetail
						.getCurrentAmount());
				for (int i = 0; i < properties.length; i++) {
					if (jsonObject.get(properties[i]) != null) {
						if(!properties[i].equals("finaceDate")){
							BeanUtils.setProperty(finacerecvDetail, properties[i],
									jsonObject.get(properties[i]));
						}
					}
				}
				records.add(finacerecvDetail);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotFinacegivenDetail finacerecvDetail = this
							.getFinanceGivenService().getFinacegivenDetailById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					map.put(finacerecvDetail.getId(), finacerecvDetail
							.getCurrentAmount());
					for (int j = 0; j < properties.length; j++) {
						if (jsonObject.get(properties[j]) != null) {
							BeanUtils.setProperty(finacerecvDetail,
									properties[j], jsonObject
											.get(properties[j]));
						}
					}
					records.add(finacerecvDetail);
				}
			}
			this.getFinanceGivenService().updateList(request, records, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除
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
		this.getFinanceGivenService().deleteList(request, ids);
		return null;
	}

	public ActionForward addFinacegiven(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		request.setAttribute("recvPersonMapping", this.getFinanceGivenService()
//				.getEmpsMap());
//		request.setAttribute("addPersonMapping", this.getFinanceGivenService()
//				.getEmpsMap());
//		request.setAttribute("custMapping", this.getFinanceGivenService()
//				.getCustNameMap());
//		request.setAttribute("payMapping", this.getFinanceGivenService()
//				.getPayTypeNameMap());
//		request.setAttribute("curMapping", this.getFinanceGivenService()
//				.getCurrencyMap(request));
		return mapping.findForward("add");
	}

	// 查询冲帐明细
	public ActionForward queryGivenDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null) {
			return null;
		}

		String mainId = request.getParameter("mainId");// 主单编号
		StringBuffer queryString = new StringBuffer();
		queryString.append("where 1=1");
		QueryInfo queryInfo = new QueryInfo();
		if (mainId != null && !mainId.trim().equals("")) {
			queryString.append(" and obj.finaceGivenid=" + mainId.trim());
		}
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinacegivenDetail obj "
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotFinacegivenDetail obj ");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		try {
			String json = this.getFinanceGivenService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询应付帐款
	public ActionForward queryDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null) {
			return null;
		}

		String factoryId = request.getParameter("factoryId");// 供应商
		String currencyId = request.getParameter("currencyId");// 付款单币种
		String financeNo = request.getParameter("financeNo");// 单号
		String startTime = request.getParameter("startTime");// 应付款日期
		String endTime = request.getParameter("endTime");

		StringBuffer queryString = new StringBuffer();

		// 最高权限
		boolean all = SystemUtil.isAction(request, "cotdeal.do", "ALL");
		// 部门权限
		boolean dept = SystemUtil.isAction(request, "cotdeal.do", "DEPT");

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
					queryString
							.append(" where obj.businessPerson = e.id and e.deptId="
									+ emp.getDeptId());
				} else {
					queryString
							.append(" where obj.businessPerson = e.id and obj.businessPerson ="
									+ emp.getId());
				}
			}
		}
		// 已冲完帐和已流转完的不显示
		queryString.append(" and obj.status = 0 and obj.realAmount!=obj.amount and (obj.finaceName='预付货款' or (obj.realAmount>0 or obj.zhRemainAmount>0))");

		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		if (financeNo != null && !financeNo.trim().equals("")) {
			queryString.append(" and obj.finaceNo like '%" + financeNo.trim()
					+ "%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.amountDate >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.amountDate <='" + endTime
					+ " 23:59:59'");
		}

		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFinaceAccountdeal obj,CotEmps e"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotFinaceAccountdeal obj,CotEmps e");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int count = this.getFinanceGivenService().getRecordCount(queryInfo);

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		// 转换成收款单币种
		List<?> list = this.getFinanceGivenService().getList(queryInfo);
		List<CotFinaceAccountdeal> listVo = new ArrayList<CotFinaceAccountdeal>();
		for (int i = 0; i < list.size(); i++) {
			CotFinaceAccountdeal old = (CotFinaceAccountdeal) list.get(i);
			CotFinaceAccountdeal acClone = (CotFinaceAccountdeal) SystemUtil
					.deepClone(old);
			Double newAmount = this.getFinanceGivenService().updatePrice(request,
					acClone.getAmount(), acClone.getCurrencyId(),
					Integer.parseInt(currencyId));
			Double zhRemainAmount = this.getFinanceGivenService().updatePrice(
					request, acClone.getZhRemainAmount(),
					acClone.getCurrencyId(), Integer.parseInt(currencyId));
			Double realAmount = this.getFinanceGivenService().updatePrice(
					request, acClone.getRealAmount(), acClone.getCurrencyId(),
					Integer.parseInt(currencyId));
			acClone.setAmount(newAmount);
			acClone.setZhRemainAmount(zhRemainAmount);
			acClone.setRealAmount(realAmount);
			acClone.setCurrencyId(Integer.parseInt(currencyId));
			listVo.add(acClone);
		}

		try {
			GridServerHandler gd = new GridServerHandler();
			gd.setData(listVo);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CotFinanceGivenService getFinanceGivenService() {
		if (financeGivenService == null) {
			financeGivenService = (CotFinanceGivenService) super
					.getBean("CotFinanceGivenService");
		}
		return financeGivenService;
	}

	public void setFinanceGivenService(CotFinanceGivenService financeGivenService) {
		this.financeGivenService = financeGivenService;
	}

}
