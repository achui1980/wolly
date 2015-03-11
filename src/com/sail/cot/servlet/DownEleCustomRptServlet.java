package com.sail.cot.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableWorkbook;

public class DownEleCustomRptServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	
	public void downFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//设置下载头
		setDownloadResponseHeader(response,"Custom_template.xls");
		request.setCharacterEncoding("UTF-8");
		javax.servlet.ServletOutputStream sos = response.getOutputStream();
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(sos);
		//样品字段
		String eleStr = request.getParameter("eleStr"); 
		//包装字段
		String boxStr = request.getParameter("boxStr");
		//报价字段
		String priceStr = request.getParameter("priceStr");
		try {
			WritableWorkbook book = Workbook.createWorkbook(bos);
			jxl.write.WritableSheet sheet = book.createSheet("Sample Import Data", 0);
			
			//设置字段名的字体样式
			WritableFont font = new WritableFont(WritableFont
					.createFont("宋体"), 12, WritableFont.NO_BOLD);
			font.setColour(Colour.BLACK);
			WritableCellFormat format = new WritableCellFormat(font);
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBackground(Colour.PALE_BLUE);
			
			//设置excel数据的字体样式
			WritableFont rowfont = new WritableFont(WritableFont
					.createFont("宋体"), 10);
			rowfont.setColour(Colour.BLUE);
			WritableCellFormat rowformat = new WritableCellFormat(rowfont);
			rowformat.setAlignment(jxl.format.Alignment.CENTRE);
			rowformat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			//中文字段名
			List<String> head = new ArrayList<String>();
			//对应数据库字段名
			List<String> headValue = new ArrayList<String>();
			//储存字段长度
			Map<String,Integer> map = new HashMap<String,Integer>();
			String[] eleAry = null;
			if(eleStr!=null && !"".equals(eleStr)){
				eleAry = eleStr.split(",");
				//合并单元格，添加“样品基本信息”标题
				sheet.mergeCells(0, 0,eleAry.length, 0);
				//设置表头的字体样式
				WritableFont eleFont = new WritableFont(WritableFont
						.createFont("宋体"), 14, WritableFont.BOLD);
				eleFont.setColour(Colour.WHITE);
				WritableCellFormat formatHead = new WritableCellFormat(eleFont);
				formatHead.setAlignment(jxl.format.Alignment.CENTRE);
				formatHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				formatHead.setBackground(Colour.GRAY_25);
				Label eleHead = new Label(0, 0, "Basic Information", formatHead);
				sheet.addCell(eleHead);
				for (int i = 0; i < eleAry.length; i++) {
					headValue.add( eleAry[i]);
					if(eleAry[i].equals("ELE_NAME")){
						head.add("中文品名");
						map.put(eleAry[i], 200);
					}else if(eleAry[i].equals("ELE_NAME_EN")){
						head.add("Description");
						map.put(eleAry[i], 500);
					}else if(eleAry[i].equals("FACTORY_NO")){
						head.add("Supplier Article No.");
						map.put(eleAry[i], 200);
					}else if(eleAry[i].equals("SORT_NO")){
						head.add("Sort");
						map.put(eleAry[i], 11);
					}else if(eleAry[i].equals("CUST_NO")){
						head.add("Cust.No.");
						map.put(eleAry[i], 100);
					}else if(eleAry[i].equals("ELE_FLAG")){
						head.add("单件/套件/组件");
						map.put(eleAry[i], 2);
					}else if(eleAry[i].equals("ELE_UNITNUM")){
						head.add("Pieces");
						map.put(eleAry[i], 11);
					}else if(eleAry[i].equals("TAO_UNIT")){
						head.add("报价套(组)件单位");
						map.put(eleAry[i], 100);
					}else if(eleAry[i].equals("ELE_PARENT")){
						head.add("Parent Article No.");
						map.put(eleAry[i], 100);
					}else if(eleAry[i].equals("ELE_FROM")){
						head.add("Source");
						map.put(eleAry[i], 100);
					}else if(eleAry[i].equals("ELE_GRADE")){
						head.add("Level");
						map.put(eleAry[i], 30);
					}else if(eleAry[i].equals("ELE_TYPENAME_LV1")){
						head.add("材质");
						map.put(eleAry[i], 100);
					}else if(eleAry[i].equals("ELE_TYPEID_LV2")){
						head.add("产品分类");
						map.put(eleAry[i], 20);
					}else if(eleAry[i].equals("ELE_COL")){
						head.add("Color");
						map.put(eleAry[i], 50);
					}else if(eleAry[i].equals("BARCODE")){
						head.add("Barcode");
						map.put(eleAry[i], 30);
					}else if(eleAry[i].equals("ELE_UNIT")){
						head.add("Unit");
						map.put(eleAry[i], 20);
					}else if(eleAry[i].equals("HS_ID")){
						head.add("海关编码");
						map.put(eleAry[i], 20);
					}else if(eleAry[i].equals("TUI_LV")){
						head.add("退税率");
						map.put(eleAry[i], 10);
					}else if(eleAry[i].equals("SHORT_NAME")){
						head.add("Supplier");
						map.put(eleAry[i], 100);
					}else if(eleAry[i].equals("ELE_SIZE_DESC")){
						head.add("中文规格");
						map.put(eleAry[i], 500);
					}else if(eleAry[i].equals("ELE_INCH_DESC")){
						head.add("Size Description");
						map.put(eleAry[i], 500);
					}else if(eleAry[i].equals("ELE_PRO_TIME")){
						head.add("Date");
						map.put(eleAry[i], 30);
					}else if(eleAry[i].equals("ELE_FOR_PERSON")){
						head.add("Designer");
						map.put(eleAry[i], 50);
					}else if(eleAry[i].equals("ELE_TYPENAME_LV2")){
						head.add("所属年份");
						map.put(eleAry[i], 20);
					}else if(eleAry[i].equals("ELE_MOD")){
						head.add("MOQ");
						map.put(eleAry[i], 11);
					}else if(eleAry[i].equals("LI_RUN")){
						head.add("Profit(%)");
						map.put(eleAry[i], 20);
					}else if(eleAry[i].equals("ELE_REMARK")){
						head.add("Remarks");
						map.put(eleAry[i], 500);
					}
				}
			}
			String[] boxAry = null;
			if(boxStr!=null && !"".equals(boxStr)){
				boxAry = boxStr.split(",");
				//设置表头的字体样式
				WritableFont boxFont = new WritableFont(WritableFont
						.createFont("宋体"), 14, WritableFont.BOLD);
				boxFont.setColour(Colour.WHITE);
				WritableCellFormat formatHead = new WritableCellFormat(boxFont);
				formatHead.setAlignment(jxl.format.Alignment.CENTRE);
				formatHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				formatHead.setBackground(Colour.GREEN);
				//合并单元格，添加“包装信息”标题
				if(eleAry!=null){
					sheet.mergeCells(eleAry.length+1,0,eleAry.length+boxAry.length, 0);
					Label eleHead = new Label(eleAry.length+1, 0, "Size Information", formatHead);
					sheet.addCell(eleHead);
				}else{
					sheet.mergeCells(0, 0,boxAry.length, 0);
					Label eleHead = new Label(0, 0, "Size Information", formatHead);
					sheet.addCell(eleHead);
				}
				
				for (int i = 0; i < boxAry.length; i++) {
					headValue.add( boxAry[i]);
					if(boxAry[i].equals("BOX_L_INCH")){
						head.add("样品英寸长");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_W_INCH")){
						head.add("样品英寸宽");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_H_INCH")){
						head.add("样品英寸高");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("cube")){
						head.add("容积");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_L")){
						head.add("Product_L");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_W")){
						head.add("Product_W");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_H")){
						head.add("Product_H");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_HANDLEH")){
						head.add("样品把高");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("ELE_DESC")){
						head.add("产品描述");
						map.put(boxAry[i], 500);
					}else if(boxAry[i].equals("BOX_TDESC")){
						head.add("口径描述");
						map.put(boxAry[i], 200);
					}else if(boxAry[i].equals("BOX_BDESC")){
						head.add("底径描述");
						map.put(boxAry[i], 200);
					}else if(boxAry[i].equals("BOX_PB_L")){
						head.add("Packing_L");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_PB_W")){
						head.add("Packing_W");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_PB_H")){
						head.add("Packing_H");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_IB_L")){
						head.add("Inner_L");
						map.put(boxAry[i], 200);
					}else if(boxAry[i].equals("BOX_IB_W")){
						head.add("Inner_W");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_IB_H")){
						head.add("Inner_H");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_MB_L")){
						head.add("中盒长");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_MB_W")){
						head.add("中盒宽");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_MB_H")){
						head.add("中盒高");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_CBM")){
						head.add("CBM");
						map.put(boxAry[i], 10);
					}else if(boxAry[i].equals("BOX_OB_L")){
						head.add("Outer_L");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_OB_W")){
						head.add("Outer_W");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_OB_H")){
						head.add("Outer_H");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_WEIGTH")){
						head.add("Weight");
						map.put(boxAry[i], 10);
					}else if(boxAry[i].equals("BOX_GROSS_WEIGTH")){
						head.add("G.W");
						map.put(boxAry[i], 10);
					}else if(boxAry[i].equals("BOX_NET_WEIGTH")){
						head.add("N.W");
						map.put(boxAry[i], 10);
					}else if(boxAry[i].equals("BOX_PB_COUNT")){
						head.add("Product Box");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_IB_COUNT")){
						head.add("Inner Box");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_MB_COUNT")){
						head.add("中盒装数");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_OB_COUNT")){
						head.add("Outer Box");
						map.put(boxAry[i], 8);
					}else if(boxAry[i].equals("BOX_TYPE_ID")){
						head.add("Packing Way");
						map.put(boxAry[i], 50);
					}else if(boxAry[i].equals("BOX_REMARK")){
						head.add("英文包装");
						map.put(boxAry[i], 500);
					}else if(boxAry[i].equals("BOX_REMARK_CN")){
						head.add("Packing Description");
						map.put(boxAry[i], 500);
					}
				}
			}
			if(priceStr!=null && !"".equals(priceStr)){
				String[] priceAry = priceStr.split(",");
				//设置表头的字体样式
				WritableFont priceFont = new WritableFont(WritableFont
						.createFont("宋体"), 14, WritableFont.BOLD);
				priceFont.setColour(Colour.WHITE);
				WritableCellFormat formatHead = new WritableCellFormat(priceFont);
				formatHead.setAlignment(jxl.format.Alignment.CENTRE);
				formatHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				formatHead.setBackground(Colour.OCEAN_BLUE);
				if(eleAry==null && boxAry == null){
					sheet.mergeCells(0, 0,priceAry.length, 0);
					Label eleHead = new Label(0, 0, "Price Information", formatHead);
					sheet.addCell(eleHead);
				}
				if(eleAry!=null && boxAry == null){
					sheet.mergeCells(eleAry.length+1, 0,eleAry.length+priceAry.length, 0);
					Label eleHead = new Label(eleAry.length+1, 0, "Price Information", formatHead);
					sheet.addCell(eleHead);
				}
				if(eleAry==null && boxAry != null){
					sheet.mergeCells(boxAry.length+1, 0,priceAry.length, 0);
					Label eleHead = new Label(boxAry.length+1, 0, "Price Information", formatHead);
					sheet.addCell(eleHead);
				}
				if(eleAry!=null && boxAry != null){
					sheet.mergeCells(eleAry.length+boxAry.length+1, 0,eleAry.length+boxAry.length+priceAry.length, 0);
					Label eleHead = new Label(eleAry.length+boxAry.length+1, 0, "Price Information", formatHead);
					sheet.addCell(eleHead);
				}
				
				
				for (int i = 0; i < priceAry.length; i++) {
					headValue.add( priceAry[i]);
					if(priceAry[i].equals("PRICE_UINT")){
						head.add("Purchage Currency");
						map.put(priceAry[i], 50);
					}else if(priceAry[i].equals("PRICE_FAC")){
						head.add("Purchage Price");
						map.put(priceAry[i], 11);
					}else if(priceAry[i].equals("PRICE_UNIT")){
						head.add("Sale Currency");
						map.put(priceAry[i], 50);
					}else if(priceAry[i].equals("PRICE_OUT")){
						head.add("Sale Prices");
						map.put(priceAry[i], 11);
					}else if(priceAry[i].equals("BOX_COUNT")){
						head.add("Quantity");
						map.put(priceAry[i], 11);
					}else if(priceAry[i].equals("CONTAINER_COUNT")){
						head.add("Cartons");
						map.put(priceAry[i], 11);
					}
				}
			}
			
			//添加货号
			Label labelId = new Label(0, 1, "ELE_ID", rowformat);
			sheet.addCell(labelId);
			//添加货号名称
			Label labelIdValue = new Label(0, 2, "Article No.", format);
			//添加货号长度
			Label labelLong = new Label(0, 3, "100", rowformat);
			sheet.addCell(labelLong);
			sheet.addCell(labelIdValue);
			sheet.setColumnView(0, 20);
			// 写好标题
			for (int i = 0; i < head.size(); i++) {
				Label label = new Label(i+1, 2, head.get(i), format);
				sheet.setColumnView(i+1, 20);
				sheet.addCell(label);
			}
			// 写好标题
			for (int i = 0; i < headValue.size(); i++) {
				Label label = new Label(i+1, 1, headValue.get(i), rowformat);
				sheet.addCell(label);
			}
			// 写好限制的字段长度,供excel宏调用
			for (int i = 0; i < headValue.size(); i++) {
				Label label = new Label(i+1, 3, map.get(headValue.get(i)).toString(), rowformat);
				sheet.addCell(label);
			}
			sheet.setRowView(1, 0);
			sheet.setRowView(3, 0);
			book.write();
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			sos.close();
			bos.close();
		}
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
