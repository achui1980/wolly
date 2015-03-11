/**
 * 
 */
package com.sail.cot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sail.cot.service.BasePropedomData;

/**
 * <p>Title: 旗航不锈钢管理系统（QHERP）</p>
 * <p>Description:带权限控制的下拉框数据绑定</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Apr 12, 2011 9:25:44 AM </p>
 * <p>Class Name: DataPopedomServlet.java </p>
 * @author achui
 *
 */
public class DataPropedomServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
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
		

		response.getWriter().write(jsonString);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
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

		if (start == null)
			start = "0";
		if (limit == null)
			limit = "100000";
		// 加载分页下拉框值时传递的参数
		if (queryCol != null) {
			key = queryCol;
		}
		String jsonString = "";
		if (query != null && !"".equals(query)){
			jsonString = this.getSysDicStringByTxt(tbname, key, query,
					start, limit,request);
		}else{
			jsonString = this.getSysDicString(tbname, start, limit,request);
		}
		response.getWriter().write(jsonString);
	}
	private String getSysDicString(String tbName, String start, String limit,HttpServletRequest request) {
		BasePropedomData baseData = new BasePropedomData();
		String res = baseData.getBaseDicData(tbName, start, limit, request, request.getParameter("validUrl"));
		return res;
	}
	private String getSysDicStringByTxt(String tbname, String key,
			String value, String start, String limit,HttpServletRequest request) {
		BasePropedomData baseData = new BasePropedomData();
		String res = baseData.getBaseDicDataByTxt(tbname, key, value, start,
				limit,request,request.getParameter("validUrl"));
		return res;
	}

}
