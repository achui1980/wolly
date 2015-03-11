package com.sail.cot.domain.vo;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

public class CotReport {
	
	private String name;//文件名
	private String sheetName;//工作簿名
	private Integer sheetPosition;//工作簿位置
	private List<?> data;//数据
	private String[] header;//表头名称
	private String headerFontName;//表头字体
	private Integer headerFontSize;//表头大小
	private String rowFontName;//行字体
	private Integer rowFontSize;//行大小
	private String opTime;//操作时间
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public Integer getSheetPosition() {
		return sheetPosition;
	}
	public void setSheetPosition(Integer sheetPosition) {
		this.sheetPosition = sheetPosition;
	}
	
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
	public String[] getHeader() {
		return header;
	}
	public void setHeader(String[] header) {
		this.header = header;
	}
	public String getHeaderFontName() {
		return headerFontName;
	}
	public void setHeaderFontName(String headerFontName) {
		this.headerFontName = headerFontName;
	}
	public Integer getHeaderFontSize() {
		return headerFontSize;
	}
	public void setHeaderFontSize(Integer headerFontSize) {
		this.headerFontSize = headerFontSize;
	}
	public String getRowFontName() {
		return rowFontName;
	}
	public void setRowFontName(String rowFontName) {
		this.rowFontName = rowFontName;
	}
	public Integer getRowFontSize() {
		return rowFontSize;
	}
	public void setRowFontSize(Integer rowFontSize) {
		this.rowFontSize = rowFontSize;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	//初始化
	public void init(HttpServletResponse response,String fileName) {
		response.setContentType("application/octet-stream; CHARSET=utf8");
		try {
			response.setHeader("Content-Disposition","attachment; filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	
	//在表格右下角显示操作日期
	public void setOpTimeLabel(WritableSheet sheet,String opTime) throws WriteException{
		int rowCount = data.size();
		//建立行单元格字体样式
		WritableFont rowfont = new WritableFont(WritableFont
				.createFont("华文楷体"), 14);
		rowfont.setColour(Colour.BLUE);
		//建立行单元格字体格式
		WritableCellFormat rowformat = new WritableCellFormat(rowfont);
		//水平对齐
		rowformat.setAlignment(jxl.format.Alignment.CENTRE);
		//垂直对齐
		rowformat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		// 合并单元格.!arg1,第几列开始,arg2,第几行开始,arg3,第几列结束,arg4,第几行结束,
		sheet.mergeCells(header.length - 2, rowCount + 2,
				header.length - 1, data.size() + 2);
		Label opLabel = new Label(header.length - 2, rowCount + 2, "导出日期："+opTime);
		sheet.addCell(opLabel);
    }
}
