/**
 * 
 */
package com.sail.cot.action.sample;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jason.core.exception.DAOException;
import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotElePriceService;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * *********************************************
 * @Copyright :(C),2009-2010
 * @CompanyName :厦门旗航有限公司(Sailingsoftware.com)
 * @Version :旗航办公自动化系统1.0
 * @Date :Sep 10, 2009
 * @author : qh-chzy
 * @class :CotElePriceAction.java
 * @Description :
 */
public class CotElePriceAction extends AbstractAction{
	
	private CotElePriceService elePriceService;
	
	public CotElePriceService getElePriceService() {
		if(elePriceService == null)
			elePriceService = (CotElePriceService)super.getBean("CotElePriceService");
		return elePriceService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String eId = request.getParameter("eId");// 样品编号
		
		String json = request.getParameter("data");
		List list = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if(single){
			JSONObject jsonObject = JSONObject.fromObject(json); 
			CotElePrice obj  = (CotElePrice)jsonObject.toBean(jsonObject, CotElePrice.class);
			list = new ArrayList();
			obj.setId(null);
			list.add(obj);
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, CotElePrice.class);
			for(int i=0; i<list.size(); i++)
			{
				CotElePrice obj = (CotElePrice)list.get(i);
				obj.setId(null);
				list.set(i, obj);
			}
		}
		this.getElePriceService().addList(list);
		JSONObject json1 = new JSONObject();
		json1.put("success", true);
		json1.put("msg", "成功");
		try {
			response.getWriter().write(json1.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String json = request.getParameter("data");
		List list = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if(single){
			JSONObject jsonObject = JSONObject.fromObject(json); 
			CotElePrice obj  = (CotElePrice)jsonObject.toBean(jsonObject, CotElePrice.class);
			list = new ArrayList();
			list.add(obj);
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, CotElePrice.class);
		}
		//保存
		this.getElePriceService().updateList(list, null);
		return null;
	}
	
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String json = request.getParameter("data");
		System.out.println("json:"+json);
		List list = null;
		List ids = new ArrayList();
		//判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if(!single){
			
			 
			list = new ArrayList();
			list.add(new Integer(json));
			
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, ArrayList.class);
		}
		//保存
		this.getElePriceService().deleteList(list);
		return null;

	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String mainId = request.getParameter("mainId");
		String priceName = request.getParameter("priceName");
		StringBuffer queryString = new StringBuffer();
		//设置查询参数
		queryString
		.append(" where 1=1");
		if (mainId != null && !mainId.trim().equals("")) {
			queryString.append(" and obj.eleId=" + mainId.trim());
		}
		if (priceName != null && !priceName.trim().equals("")) {
			queryString.append(" and obj.priceName like '%" + priceName.trim()+"%'");
		}
		QueryInfo queryInfo = new QueryInfo();
		//设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotElePrice obj"+ queryString);
		//查询语句
		queryInfo.setSelectString("from CotElePrice obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		//得到limit对象
		
		int startIndex = Integer.parseInt(start);
		//设置总行数和每页显示多少条
		queryInfo.setStartIndex(startIndex);
		//根据起始
		List<?> list = this.getElePriceService().getList(queryInfo);
		try {
			String json = this.getElePriceService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
}
