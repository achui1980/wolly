/**
 * 
 */
package com.sail.cot.action.sample;

import java.util.ArrayList;
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
import com.sail.cot.domain.CotModPrice;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotModPriceService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;

/**
 * 样品最小定量的价格
 * 
 * @author qh-chzy
 * 
 */
public class CotModPriceAction extends AbstractAction {

	private CotModPriceService modPriceService;

	public CotModPriceService getModPriceService() {
		if (modPriceService == null)
			modPriceService = (CotModPriceService) super
					.getBean("CotModPriceService");
		return modPriceService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String custId = request.getParameter("custId");// 客户编号
		String eleId = request.getParameter("eId");// 样品货号
		
		List<CotModPrice> records = new ArrayList<CotModPrice>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotModPrice.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotModPrice modPrice = new CotModPrice();
				for (int i = 0; i < properties.length; i++) {
					BeanUtils.setProperty(modPrice, properties[i],
							jsonObject.get(properties[i]));
				}
				if(custId!=null){
					modPrice.setCustId(Integer.parseInt(custId));
				}
				modPrice.setEleId(eleId);
				records.add(modPrice);

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					CotModPrice modPrice = new CotModPrice();
					for (int j = 0; j < properties.length; j++) {
						BeanUtils.setProperty(modPrice, properties[j],
								jsonObject.get(properties[j]));
					}
					if(custId!=null){
						modPrice.setCustId(Integer.parseInt(custId));
					}
					modPrice.setEleId(eleId);
					records.add(modPrice);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getModPriceService().addList(records);
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
				.getDeclaredFields(CotModPrice.class);
		List<CotModPrice> records = new ArrayList<CotModPrice>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotModPrice modPrice = this.getModPriceService()
						.getCotModPriceById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if(jsonObject.get(properties[i])!=null){
						BeanUtils.setProperty(modPrice, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				records.add(modPrice);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotModPrice modPrice = this.getModPriceService()
							.getCotModPriceById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if(jsonObject.get(properties[j])!=null){
							BeanUtils.setProperty(modPrice, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					records.add(modPrice);
				}
			}
			this.getModPriceService().updateList(records);
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
		this.getModPriceService().deleteList(ids);

		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String custId = request.getParameter("custId");
		String eleId = request.getParameter("eId");
		StringBuffer queryString = new StringBuffer();
		// 设置查询参数
		queryString.append(" where 1=1");
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId=" + custId);
		}
		if (eleId != null && !eleId.trim().equals("")) {
			queryString.append(" and obj.eleId='" + eleId+"'");
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotModPrice obj"
				+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotModPrice obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		List<?> list = this.getModPriceService().getList(queryInfo);
		int count = this.getModPriceService().getRecordCount(queryInfo);
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
