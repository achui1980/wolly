package com.sail.cot.action.fittingorder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFittingsOrderdetail;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotPackingOrderdetail;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.fittingorder.CotFitOrderService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotFitOrderAction extends AbstractAction {

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
		// TODO Auto-generated method stub
		return mapping.findForward("addFittingOrder");
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

		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		String fittingOrderNo = request.getParameter("fittingOrderNo");// 单号
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String factoryId = request.getParameter("factoryId");// 厂家
		String empId = request.getParameter("empId");

		StringBuffer queryString = new StringBuffer();

		// 最高权限
		boolean all = SystemUtil.isAction(request, "cotfittingorder.do", "ALL");
		// 部门权限
		boolean dept = SystemUtil.isAction(request, "cotfittingorder.do",
				"DEPT");

		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.empId=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				queryString.append(" where obj.empId=e.id");
			} else {
				// 判断是否有查看该部门征样的权限
				if (dept == true) {
					queryString.append(" where obj.empId = e.id and e.deptId="
							+ emp.getDeptId());
				} else {
					queryString
							.append(" where obj.empId = e.id and obj.empId ="
									+ emp.getId());
				}
			}
		}

		if (fittingOrderNo != null && !fittingOrderNo.trim().equals("")) {
			fittingOrderNo = fittingOrderNo.trim();
			queryString.append(" and obj.fittingOrderNo like '%"
					+ fittingOrderNo + "%'");
		}

		if (empId != null && !empId.trim().equals("")) {
			queryString.append(" and obj.empId=" + empId.trim());
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		if (startTime != null && !startTime.trim().equals("")) {
			queryString.append(" and obj.orderDate >='" + startTime + "'");
		}
		if (endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.orderDate <='" + endTime + "'");
		}

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFittingOrder obj,CotEmps e"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotFittingOrder obj,CotEmps e");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");
		int startIndex = Integer.parseInt(start);

		queryInfo.setStartIndex(startIndex);
		String excludes[] = { "cotFittingsOrderdetails" };
		queryInfo.setExcludes(excludes);
		try {
			String json = this.getCotFitOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	public ActionForward queryFitFrame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("queryFitFrame");
	}

	public ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String flag = request.getParameter("flag");

		if (start == null || limit == null)
			return mapping.findForward("queryDetail");

		String orderfitId = request.getParameter("orderfitId");// 主排载单号
		if (orderfitId == null || "".equals(orderfitId)) {
			return null;
		}

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderfitId);

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo
				.setCountQuery("select count(*) from CotFittingsOrderdetail obj"
						+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotFittingsOrderdetail obj");
		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.fitNo asc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getCotFitOrderService().getList(queryInfo);
		int totalCount = this.getCotFitOrderService().getRecordCount(queryInfo);
		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		if (flag.equals("fitOrderDetail")) {
			for (int i = 0; i < list.size(); i++) {
				CotFittingsOrderdetail cotFittingsOrderdetail = (CotFittingsOrderdetail) list
						.get(i);
				String rdm = "1" + RandomStringUtils.randomNumeric(8);
				cotFittingsOrderdetail.setRdm(rdm);
				this.getCotFitOrderService().setFitOrderMapAction(session, rdm,
						cotFittingsOrderdetail);
			}
		}
		try {
			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(totalCount);
			response.getWriter().write(gd.getLoadResponseText());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public ActionForward modifyDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// 内存数据
		HttpSession session = request.getSession();
		TreeMap<String, CotFittingsOrderdetail> fitorderMap = this
				.getCotFitOrderService().getFitNoMapAction(session);

		List<CotFittingsOrderdetail> records = new ArrayList<CotFittingsOrderdetail>();

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object rdm = jsonObject.get("rdm");
			if (rdm != null && !"".equals(rdm.toString())) {
				CotFittingsOrderdetail detail = fitorderMap.get(rdm);

				// 克隆对象,避免造成指针混用
				CotFittingsOrderdetail cloneObj = (CotFittingsOrderdetail) SystemUtil
						.deepClone(detail);
				if (detail != null) {
					records.add(cloneObj);
				}
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object rdm = jsonObject.get("rdm");
				if (rdm != null && !"".equals(rdm.toString())) {
					CotFittingsOrderdetail detail = fitorderMap.get(rdm);

					// 克隆对象,避免造成指针混用
					CotFittingsOrderdetail cloneObj = (CotFittingsOrderdetail) SystemUtil
							.deepClone(detail);
					if (detail != null) {
						records.add(cloneObj);
					}
				}
			}
		}
		// 保存
		this.getCotFitOrderService().modifyFitOrderDetail(records);
		return null;
	}

	public ActionForward removeDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// 主配件采购id
		String fitorderId = request.getParameter("fitorderId");

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

		Double total = 0.0;
		HttpSession session = request.getSession();
		for (int j = 0; j < ids.size(); j++) {
			Integer id = (Integer) ids.get(j);
			CotFittingsOrderdetail detail = this.getCotFitOrderService()
					.getFittingDetailById(id);

			// 修改配件分析表中的id及分析状态
			this.getCotFitOrderService().modifyFitIdAndFlag(detail.getId());

			// 统计所删除配件明细的总金额
			total += detail.getTotalAmmount();

			// 在Action中清除fitNoMap中fitNo对应的映射
			// String rdm = detail.getRdm();
			// this.getCotFitOrderService().delFitOrderMapByKeyAction(rdm,
			// session);

		}
		this.getCotFitOrderService().deleteDetailByIds(ids);

		// 修改配件采购主单的总金额
		this.getCotFitOrderService().modifyFitOrderAmount(
				Integer.parseInt(fitorderId), total);
		return null;
	}

	public ActionForward queryFinanceOther(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryFinanceOther");

		String fkId = request.getParameter("fkId");// 外键
		String type = request.getParameter("type");// 类型 0:应收 1：应付

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1 and obj.source ='fitorder'");

		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		} 
		if (type != null && !type.trim().equals("")) {
			queryString.append(" and obj.type=" + type.trim());
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
		queryInfo.setCountQuery("select count(*) from CotFinaceOther as obj"
				+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceOther AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotFitOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存
	public ActionForward addOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String orderNo = request.getParameter("mainNo");// 订单单号
		String fkId = request.getParameter("mainId");// 订单主单编号
		String facId = request.getParameter("facId");// 厂家id

		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();

		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinaceOther.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				String name = jsonObject.getString("finaceName");
				if (!"".equals(name)) {
					CotFinaceOther finaceOther = new CotFinaceOther();
					for (int i = 0; i < properties.length; i++) {
						BeanUtils.setProperty(finaceOther, properties[i],
								jsonObject.get(properties[i]));
					}
					finaceOther.setType(1);// 类型 0:应收 1：应付
					finaceOther.setSource("fitorder");
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					finaceOther.setStatus(0);
					finaceOther.setFactoryId(Integer.parseInt(facId));
					finaceOther.setFkId(Integer.parseInt(fkId));
					records.add(finaceOther);
				}

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					String name = jsonObject.getString("finaceName");
					if (!"".equals(name)) {
						CotFinaceOther finaceOther = new CotFinaceOther();
						for (int j = 0; j < properties.length; j++) {
							BeanUtils.setProperty(finaceOther, properties[j],
									jsonObject.get(properties[j]));
						}
						finaceOther.setType(1);// 类型 0:应收 1：应付
						finaceOther.setSource("fitorder");
						finaceOther.setOrderNo(orderNo);
						finaceOther.setBusinessPerson(empId);
						finaceOther.setStatus(0);
						finaceOther.setFkId(Integer.parseInt(fkId));
						finaceOther.setFactoryId(Integer.parseInt(facId));
						records.add(finaceOther);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getCotFitOrderService().addOtherList(records);
		return null;
	}

	// 修改
	public ActionForward modifyOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String orderNo = request.getParameter("orderNo");// 单号

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinaceOther.class);
		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFinaceOther finaceOther = this.getCotFitOrderService()
						.getFinaceOtherById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if (jsonObject.get(properties[i]) != null) {
						BeanUtils.setProperty(finaceOther, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				finaceOther.setOrderNo(orderNo);
				finaceOther.setBusinessPerson(empId);
				records.add(finaceOther);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotFinaceOther finaceOther = this.getCotFitOrderService()
							.getFinaceOtherById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if (jsonObject.get(properties[j]) != null) {
							BeanUtils.setProperty(finaceOther, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					records.add(finaceOther);
				}
			}
			this.getCotFitOrderService().updateOtherList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除
	public ActionForward removeOther(ActionMapping mapping, ActionForm form,
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

		this.getCotFitOrderService().deleteOtherList(ids);
		// JSONObject suc = new JSONObject();
		// suc.put("success", true);
		// suc.put("msg", "成功");
		// try {
		// response.getWriter().write(suc.toString());
		// } catch (IOException e) {
		// e.printStackTrace();
		// JSONObject error = new JSONObject();
		// error.put("success", false);
		// error.put("msg", "失败");
		// }
		return null;
	}

	// 查询应付帐款
	public ActionForward queryDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryDeal");

		String orderNo = request.getParameter("orderNo");// 单号
		String fkId = request.getParameter("fkId");// 外键
		String source = request.getParameter("source");// 金额源来
		String factoryId = request.getParameter("factoryId");// 客户

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo='" + orderNo.trim() + "'");
		}
		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}
		if (source != null && !source.trim().equals("")) {
			queryString.append(" and obj.source='" + source.trim() + "'");
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
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
		queryInfo
				.setCountQuery("select count(*) from CotFinaceAccountdeal obj"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("from CotFinaceAccountdeal obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getCotFitOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除
	public ActionForward removeDeal(ActionMapping mapping, ActionForm form,
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

		for (int j = 0; j < ids.size(); j++) {
			Integer id = (Integer) ids.get(j);
			CotFinaceAccountdeal finaceAccountdeal = this
					.getCotFitOrderService().getGivenDealById(id);
			// 修改其他费用生成状态 0：未生成 1：已生成
			this.getCotFitOrderService().modifyFinOtherStatus(
					finaceAccountdeal.getFkId(),
					finaceAccountdeal.getFinaceName(), "remove");
		}

		this.getCotFitOrderService().deleteDealList(ids);
		// JSONObject suc = new JSONObject();
		// suc.put("success", true);
		// suc.put("msg", "成功");
		// try {
		// response.getWriter().write(suc.toString());
		// } catch (IOException e) {
		// e.printStackTrace();
		// JSONObject error = new JSONObject();
		// error.put("success", false);
		// error.put("msg", "失败");
		// }
		return null;
	}

	// 查询应付帐款
	public ActionForward queryDealDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryDealDetail");

		String dealId = request.getParameter("dealId");// 客户

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
		// queryString.append(" and obj.status = 0");

		if (dealId != null && !dealId.trim().equals("")) {
			queryString.append(" and obj.dealId=" + dealId.trim());
		} else {
			return null;
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
		queryInfo
				.setCountQuery("select count(*) from CotFinacegivenDetail obj,CotEmps e"
						+ queryString.toString());
		// 设置查询记录语句
		queryInfo
				.setSelectString("select obj from CotFinacegivenDetail obj,CotEmps e");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotFitOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
