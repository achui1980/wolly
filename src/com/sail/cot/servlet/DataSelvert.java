/**
 * 
 */
package com.sail.cot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sail.cot.service.BaseData;

/**
 * <p>
 * Title: 旗行办公自动化系统（OA）
 * </p>
 * <p>
 * Description:页面下拉列表框数据绑定
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Sep 18, 2009 9:44:20 AM
 * </p>
 * <p>
 * Class Name: DataSelvert.java
 * </p>
 * 
 * @author achui
 * 
 */
public class DataSelvert extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null)
			start = "0";
		if (limit == null)
			limit = "100000";

		String tbname = request.getParameter("tbname");
		String type = request.getParameter("type");
		String flag = request.getParameter("flag");
		String jsonString = "";
		if (type != null) {
			// 用于包装材料绑定下拉框
			jsonString = this.getNameByType(tbname, type, start, limit);
		} else if ("pic".equals(flag)) {
			jsonString = this.getSysDicStringHavePic(tbname, start, limit);
		} else if ("filter".equals(flag)) {
			String[] filterClone = null;
			if ("CotCustomer".equals(tbname)) {
				String[] filter = { "cotCustContacts", "customerVisitedLogs",
						"customerClaim", "picImg", "custImg" };
				filterClone = filter;
			}
			if ("CotEmps".equals(tbname)) {
				String[] filter = { "customers" };
				filterClone = filter;
			}
			if ("CotOrderOut".equals(tbname)) {
				String[] filter = { "cotOrderOutdetails",
						"cotOrderOuthsdetails", "cotSplitInfos", "cotHsInfos",
						"cotShipments", "cotSymbols", "cotOrderouthsRpt",
						"orderMBImg" };
				filterClone = filter;
			}
			jsonString = this.getSysDicStringByFilter(tbname, start, limit,
					filterClone);
		} else {
			jsonString = this.getSysDicString(tbname, start, limit);
		}
		response.getWriter().write(jsonString);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String tbname = request.getParameter("tbname");
		String key = request.getParameter("key");
		String value = request.getParameter("id");
		String queryCol = request.getParameter("queryCol");// 下拉框需要查询的列的属性值
		// 分页传递参数
		String query = request.getParameter("query");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		// 过滤字段及值
		String typeName = request.getParameter("typeName");
		String type = request.getParameter("type");
		//当前登录人
		String logId = request.getParameter("logId");

		if (start == null)
			start = "0";
		if (limit == null)
			limit = "100000";
		// 加载分页下拉框值时传递的参数
		if (queryCol != null) {
			key = queryCol;
		}

		String jsonString = "";
		if (key != null && value != null)
			jsonString = this.getSysDicStringById(tbname, key, value, start,
					limit);// 下拉框级联操作
		else {
			if (query != null && !"".equals(query))
				if (type != null && typeName != null) {
					jsonString = this.getSysDicStringByFac(tbname, key, query,
							type, typeName, start, limit,logId);
				} else {
					jsonString = this.getSysDicStringByTxt(tbname, key, query,
							start, limit);
				}
			else if (type != null && typeName != null) {
				jsonString = this.getSysDicByType(tbname, type, typeName,
						start, limit,logId);
			} else {
				jsonString = this.getSysDicString(tbname, start, limit);
			}
		}
		response.getWriter().write(jsonString);
	}

	private String getSysDicString(String tbName, String start, String limit) {
		BaseData baseData = new BaseData();
		String res = baseData.getBaseDicData(tbName, start, limit);
		return res;
	}

	private String getSysDicByType(String tbName, String type, String typeName,
			String start, String limit,String logId) {
		BaseData baseData = new BaseData();
		String res = null;
		if(typeName.equals("orderout")){
			res = baseData.findSpecialFac(tbName, null, null, type,
					typeName, start, limit);
		}else if(tbName.equals("CotOrderDetail")){
			res = baseData.getBarcodesByType(tbName, type, typeName,
					start, limit,logId);
		}else{
			res = baseData.getBaseDicDataByType(tbName, type, typeName,
					start, limit,logId);
		}
		return res;
	}

	private String getSysDicStringById(String tbname, String key, String value,
			String start, String limit) {
		BaseData baseData = new BaseData();
		String res = baseData.getBaseDicDataById(tbname, key, value, start,
				limit);
		return res;
	}

	private String getSysDicStringByTxt(String tbname, String key,
			String value, String start, String limit) {
		BaseData baseData = new BaseData();
		String res = baseData.getBaseDicDataByTxt(tbname, key, value, start,
				limit);
		return res;
	}

	private String getNameByType(String tbname, String type, String start,
			String limit) {
		BaseData baseData = new BaseData();
		String res = baseData.getNameByBoxType(tbname, type, start, limit);
		return res;
	}

	// 过滤图片
	private String getSysDicStringHavePic(String tbname, String start,
			String limit) {
		BaseData baseData = new BaseData();
		String res = baseData.getBaseDicDataHavePic(tbname, start, limit);
		return res;
	}

	// 用于分类过滤：如厂家
	private String getSysDicStringByFac(String tbname, String key,
			String value, String type, String typeName, String start,
			String limit,String logId) {
		BaseData baseData = new BaseData();
		String res =null;
		if(typeName.equals("orderout")){
			res = baseData.findSpecialFac(tbname, key, value, type,
					typeName, start, limit);
		}else if(tbname.equals("CotOrderDetail")){
			res = baseData.getBarcodes(tbname, key, value, type,
					typeName, start, limit,logId);
		}else{
			res = baseData.getBaseDicDataByFac(tbname, key, value, type,
					typeName, start, limit,logId);
		}
		return res;
	}

	// 过滤掉不需要查询的字段
	private String getSysDicStringByFilter(String tbName, String start,
			String limit, String[] filter) {
		BaseData baseData = new BaseData();
		String res = baseData.getBaseDicDataByFilter(tbName, start, limit,
				filter);
		return res;
	}
}
