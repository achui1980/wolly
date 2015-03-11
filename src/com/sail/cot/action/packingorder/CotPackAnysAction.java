package com.sail.cot.action.packingorder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.sail.cot.domain.CotPackingAnys;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.packingorder.CotPackOrderService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotPackAnysAction extends AbstractAction {

	private CotPackOrderService cotPackOrderService;

	public CotPackOrderService getCotPackOrderService() {
		if (cotPackOrderService == null) {
			cotPackOrderService = (CotPackOrderService) super
					.getBean("CotPackOrderService");
		}
		return cotPackOrderService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List<CotPackingAnys> records = new ArrayList<CotPackingAnys>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotPackingAnys.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotPackingAnys packingAnys = new CotPackingAnys();
				for (int i = 0; i < properties.length; i++) {
					BeanUtils.setProperty(packingAnys, properties[i],
							jsonObject.get(properties[i]));
				}
				records.add(packingAnys);

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					CotPackingAnys packingAnys = new CotPackingAnys();
					for (int j = 0; j < properties.length; j++) {
						BeanUtils.setProperty(packingAnys, properties[j],
								jsonObject.get(properties[j]));
					}
					records.add(packingAnys);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getCotPackOrderService().addList(records);
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
				.getDeclaredFields(CotPackingAnys.class);
		List<CotPackingAnys> records = new ArrayList<CotPackingAnys>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotPackingAnys packingAnys = this.getCotPackOrderService()
						.getPackingAnysById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if(jsonObject.get(properties[i])!=null){
						BeanUtils.setProperty(packingAnys, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				records.add(packingAnys);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotPackingAnys packingAnys = this.getCotPackOrderService()
							.getPackingAnysById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if(jsonObject.get(properties[j])!=null){
							BeanUtils.setProperty(packingAnys, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					records.add(packingAnys);
				}
			}
			this.getCotPackOrderService().updateList(records);
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
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String eleIdFind = request.getParameter("eleIdFind");
		String orderId = request.getParameter("orderId");
		String orderDetailId = request.getParameter("orderDetailId");
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
		if (orderDetailId != null && !orderDetailId.trim().equals("")) {
			queryString.append(" and obj.orderdetailId=" + orderDetailId.trim());
		}
		if (factoryIdFind != null && !factoryIdFind.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryIdFind.trim());
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPackingAnys obj"
				+ queryString.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("select obj from CotPackingAnys obj");

		// 设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		List<?> list = this.getCotPackOrderService().getList(queryInfo);
		
		// 格式化数字.保留两位小数
		DecimalFormat df = new DecimalFormat("#.00");
		//计算总金额
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotPackingAnys packingAnys = (CotPackingAnys)it.next();
			if(packingAnys.getPackPrice()!=null && packingAnys.getPackCount()!=null){
				Double temp = Double.parseDouble(df.format(packingAnys.getPackPrice()*packingAnys.getPackCount()));
				packingAnys.setTotalAmount(temp);
			}
		}
		
		int count = this.getCotPackOrderService().getRecordCount(queryInfo);
		
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
		this.getCotPackOrderService().deleteList(ids);

		return null;
	}
	
	//查询采购主单
	public ActionForward queryPackOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String packingOrderNo=request.getParameter("packingOrderNo");//单号
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String empId = request.getParameter("empId");
		
		String orderId = request.getParameter("orderId");
		
		StringBuffer queryString=new StringBuffer();
		
		//最高权限
		boolean all = SystemUtil.isAction(request, "cotpackingorder.do", "ALL");
		//部门权限
		boolean dept = SystemUtil.isAction(request, "cotpackingorder.do","DEPT");		
		
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
					queryString .append(" where obj.empId = e.id and e.deptId=" + emp.getDeptId());
				} else {
					queryString .append(" where obj.empId = e.id and obj.empId =" + emp.getId());
				}
			}
		}
		
		if(packingOrderNo != null && !packingOrderNo.trim().equals(""))
		{
			packingOrderNo = packingOrderNo.trim();
			queryString.append(" and obj.packingOrderNo like '%"+packingOrderNo+"%'");
		}
		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.orderId="+orderId.trim());
		}
		if (empId != null && !empId.trim().equals("")) {
			queryString.append(" and obj.empId="+empId.trim());
		}
		if(startTime != null && !startTime.trim().equals("") && (endTime == null || endTime.trim().equals(""))) {
			queryString.append(" and obj.orderDate >='"+startTime+"'");
		}
		if((startTime == null || startTime.trim().equals("")) && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.orderDate <='"+endTime+"'");
		}
		if(startTime != null && !startTime.trim().equals("") && endTime != null && !endTime.trim().equals("")) {
			queryString.append(" and obj.orderDate between '"+startTime+"' and '"+endTime+"'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPackingOrder obj,CotEmps e"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotPackingOrder obj,CotEmps e");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");
		
		int count = this.getCotPackOrderService().getRecordCount(queryInfo);
		
		List<?> list = this.getCotPackOrderService().getList(queryInfo);
		String excludes[] = {"cotPackingOrderdetails"};
		try {
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
}
