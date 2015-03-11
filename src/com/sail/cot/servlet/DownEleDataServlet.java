package com.sail.cot.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.jason.core.Application;
import com.sail.cot.service.sample.CotElementsService;

public class DownEleDataServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	
	public void downFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//需要备份导出的选择的数据
		String ids = request.getParameter("ids");
		
		//需要备份导出的第几页的数据
		String page = request.getParameter("page");
		
		//查询条件
		StringBuffer queryString = new StringBuffer();
		StringBuffer queryStringHQL = new StringBuffer();
		queryString.append(" where 1=1");
		queryStringHQL.append(" where 1=1");
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
		
		String boxLS = request.getParameter("boxLS");// 产品起始长
		String boxLE = request.getParameter("boxLE");// 产品结束长
		String boxWS = request.getParameter("boxWS");// 产品起始宽
		String boxWE = request.getParameter("boxWE");// 产品结束宽
		String boxHS = request.getParameter("boxHS");// 产品起始高
		String boxHE = request.getParameter("boxHE");// 产品结束高
		
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and e.ELE_ID like '%"
					+ eleId.toString().trim() + "%'");
			queryStringHQL.append(" and obj.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and e.ELE_FLAG=2");
			queryStringHQL.append(" and obj.eleFlag=2");
		}else{
			queryString.append(" and e.ELE_FLAG!=2");
			queryStringHQL.append(" and (ele.eleFlag is null or ele.eleFlag!=2)");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and e.ELE_NAME like '%"
					+ eleName.toString().trim() + "%'");
			queryStringHQL.append(" and obj.eleName like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and e.ELE_NAME_EN like '%"
					+ eleNameEn.toString().trim() + "%'");
			queryStringHQL.append(" and obj.eleNameEn like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and e.FACTORY_ID=" + factoryId.toString());
			queryStringHQL.append(" and obj.factoryId=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and e.ELE_COL like '%"
					+ eleCol.toString().trim() + "%'");
			queryStringHQL.append(" and obj.eleCol like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and e.ELE_TYPEID_LV1="
					+ eleTypeidLv1.toString());
			queryStringHQL.append(" and obj.eleTypeidLv1="
					+ eleTypeidLv1.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and e.ELE_GRADE like '%"
					+ eleGrade.toString().trim() + "%'");
			queryStringHQL.append(" and obj.eleGrade like '%"
					+ eleGrade.toString().trim() + "%'");
		}
		
		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and e.ELE_FOR_PERSON like '%"
					+ eleForPerson.toString().trim() + "%'");
			queryStringHQL.append(" and obj.eleForPerson like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString
					.append(" and e.ELE_PRO_TIME >='" + startTime + "'");
			queryStringHQL
			.append(" and obj.eleProTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and e.ELE_PRO_TIME <='" + endTime
					+ " 23:59:59'");
			queryStringHQL.append(" and obj.eleProTime <='" + endTime
					+ " 23:59:59'");
		}
		
		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
				queryString.append(" and e.BOX_L between "
						+ boxLS + " and " + boxLE);
				queryStringHQL.append(" and obj.boxL between "
						+ boxLS + " and " + boxLE);
			}
		}
		
		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
				queryString.append(" and e.BOX_W between "
						+ boxWS + " and " + boxWE);
				queryStringHQL.append(" and obj.boxW between "
						+ boxWS + " and " + boxWE);
			}
		}
		
		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
				queryString.append(" and e.BOX_H between "
						+ boxHS + " and " + boxHE);
				queryStringHQL.append(" and obj.boxH between "
						+ boxHS + " and " + boxHE);
			}
		}
		
		//设置下载头
		setDownloadResponseHeader(response,"样品导出数据.xls");
		// 获得tomcat路径
		String classPath = DownEleDataServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		//目标文件
		String targetFile = systemPath + "reportfile/elements.xls";
		
		File file = new File(targetFile);
		try {
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setEncoding("ISO-8859-1");
			setting.setLocale(locale);
			Workbook oldbook = Workbook.getWorkbook(file,setting);
			//打开一个文件的副本，并且指定数据写回到原文件 
			WritableWorkbook book=Workbook.createWorkbook(file,oldbook); 
			WritableSheet sheet = book.getSheet(0);
			int rows = sheet.getRows();
			// 清空原来的数据
			while (rows>=3) {
				sheet.removeRow(3);
				rows--;
			}
			
			//获取所有样品数据
			CotElementsService cotElementsService = (CotElementsService)this.getService("CotElementsService");
			String[][] eleData = cotElementsService.findExportData(ids,page,queryString.toString(),queryStringHQL.toString());
			Label label = null;
			for(int i=0;i<eleData.length;i++){ 
				for(int j=0;j<eleData[i].length;j++){
					label = new Label(j, i+3, eleData[i][j]);
					sheet.addCell(label);
				}
			}
			book.write();
			oldbook.close();
			book.close();
			
			//下载更新好的文件
			File fileOut = new File(targetFile);
			DataInputStream is=new DataInputStream(new FileInputStream(fileOut));
			DataOutputStream os=new DataOutputStream(response.getOutputStream());
			byte[] readBytes=new byte[128];
			while(is.read(readBytes)!=-1){
				os.write(readBytes);
			}
			os.close();  
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
		}
	}
	private Object getService(String strSerivce)
	{
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
	/**
	 * 设置文件下载的header
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException 
	 */
	private void setDownloadResponseHeader(HttpServletResponse response,String fileName) throws UnsupportedEncodingException{
		//response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition","attachment; filename="+
					java.net.URLEncoder.encode(fileName,"UTF-8"));
		//response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));   
	}
}
