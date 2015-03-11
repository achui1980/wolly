package com.sail.cot.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.core.Application;
import com.sail.cot.service.sample.CotElementsService;

public class DownCheckMachineServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	public void downFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// 需要备份导出的选择的数据
		String ids = request.getParameter("ids");

		// 需要备份导出的第几页的数据
		String page = request.getParameter("page");
		
		//查询条件
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		String eleId = request.getParameter("eleIdFind");// 样品编号
		String child = request.getParameter("childFind");// 子货号标识
		String eleName = request.getParameter("eleNameFind");// 中文名
		String eleNameEn = request.getParameter("eleNameEnFind");// 英文名
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String eleCol = request.getParameter("eleColFind");// 颜色
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
		String eleGrade = request.getParameter("eleGradeFind");// 等级
		String eleForPerson = request.getParameter("eleForPersonFind");// 开发对象
		String eleTypeidLv2 = request.getParameter("eleTypeidLv2");// 中类
		String eleTypeidLv3 = request.getParameter("eleTypeidLv3");// 小类
		String eleHsid = request.getParameter("eleHsid");// 海关编码
		String eleDesc = request.getParameter("eleDescFind");// 产品描述
		String eleTypenameLv2 = request.getParameter("eleTypenameLv2");// 所属年份
		String eleSizeDesc = request.getParameter("eleSizeDesc");// 中文规格
		String eleInchDesc = request.getParameter("eleInchDesc");// 英文规格
		
		String boxLS = request.getParameter("boxLS");// 产品起始长
		String boxLE = request.getParameter("boxLE");// 产品结束长
		String boxWS = request.getParameter("boxWS");// 产品起始宽
		String boxWE = request.getParameter("boxWE");// 产品结束宽
		String boxHS = request.getParameter("boxHS");// 产品起始高
		String boxHE = request.getParameter("boxHE");// 产品结束高
		
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and e.ELE_TYPEID_LV2="
					+ eleTypeidLv2.toString());
		}
		if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
			queryString.append(" and e.ELE_TYPEID_LV3="
					+ eleTypeidLv3.toString());
		}
		if (eleHsid != null && !eleHsid.toString().equals("")) {
			queryString.append(" and e.HS_ID="
					+ eleHsid.toString());
		}
		if (eleDesc != null && !eleDesc.toString().equals("")) {
			queryString.append(" and e.ELE_DESC like '%"
					+ eleDesc.toString().trim() + "%'");
		}
		if (eleTypenameLv2 != null && !eleTypenameLv2.toString().equals("")) {
			queryString.append(" and e.ELE_TYPENAME_LV2=" + eleTypenameLv2.toString());
		}
		
		if (eleSizeDesc != null && !eleSizeDesc.toString().equals("")) {
			queryString.append(" and e.ELE_SIZE_DESC like '%"
					+ eleSizeDesc.toString().trim() + "%'");
		}
		if (eleInchDesc != null && !eleInchDesc.toString().equals("")) {
			queryString.append(" and e.ELE_INCH_DESC like '%"
					+ eleInchDesc.toString().trim() + "%'");
		}
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and e.ELE_ID like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and e.ELE_FLAG=2");
		}else{
			queryString.append(" and e.ELE_FLAG!=2");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and e.ELE_NAME like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and e.ELE_NAME_EN like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and e.FACTORY_ID=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and e.ELE_COL like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and e.ELE_TYPEID_LV1="
					+ eleTypeidLv1.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and e.ELE_GRADE like '%"
					+ eleGrade.toString().trim() + "%'");
		}
		
		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and e.ELE_FOR_PERSON like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString
					.append(" and e.ELE_ADD_TIME >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and e.ELE_ADD_TIME <='" + endTime
					+ " 23:59:59'");
		}
		
		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
				queryString.append(" and e.BOX_L between "
						+ boxLS + " and " + boxLE);
			}
		}
		
		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
				queryString.append(" and e.BOX_W between "
						+ boxWS + " and " + boxWE);
			}
		}
		
		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
				queryString.append(" and e.BOX_H between "
						+ boxHS + " and " + boxHE);
			}
		}
		// 设置下载头
		setDownloadResponseHeader(response, "BaseInfo.txt");
		String flag = request.getParameter("flag");
		

		try {
			// 获取所有样品数据
			CotElementsService cotElementsService = (CotElementsService) this
					.getService("CotElementsService");
			if("0".equals(flag))//DT930
				cotElementsService.saveCheckMachineData(ids,
					page,queryString.toString());
			else if("1".equals(flag))//MC550
				cotElementsService.saveCheckMachineData4Mc550(ids,
						page,queryString.toString());
			
			// 获得tomcat路径
			String classPath = DownCheckMachineServlet.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			File file = new File(systemPath + "reportfile/BaseInfo.txt");
			if(!file.isFile()){
				file.createNewFile();
			}
			//下载更新好的文件
			InputStream is = new FileInputStream(file);
			byte[] b = new byte[100];
			int len;
			while ((len = is.read(b)) > 0){
				response.getOutputStream().write(b, 0, len);
			}
			is.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					"<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
		}
	}

	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer()
				.getComponent(strSerivce);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}

	/**
	 * 设置文件下载的header
	 * 
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 */
	private void setDownloadResponseHeader(HttpServletResponse response,
			String fileName) throws UnsupportedEncodingException {
		// response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ java.net.URLEncoder.encode(fileName, "UTF-8"));
		//response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));   
	}
}
