/**
 * 
 */
package com.sail.cot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sail.cot.dao.impl.CotExportRptDaoImpl;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:根据实达公司的配置需求，生成打印模板，动态显示列</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: May 31, 2009 10:08:29 AM </p>
 * <p>Class Name: RptDesign4SD.java </p>
 * @author achui
 *
 */
public class RptDesign4SD {
	
	public static JasperDesign getJasperDesign(String file)
	{
		JasperDesign design = null;
		try {
//			design = JRXmlLoader.load(file);
//			//JRField[] fieldList = design.getFields();
//			JRElement[] elementList =  design.getColumnHeader().getElements();
//			List<JRElement> arr = new ArrayList<JRElement>();
//			//不显示密码列
//			//int index = 0;
//			for(JRElement element : elementList)
//			{
//				String key = element.getKey();
//				System.out.println(key);
//				if("static_pwd".equals(key) || "staticText-static".equals(key))
//				{
//					continue;
//				}
//				arr.add(element);
//			}
//			JRDesignBand band = new JRDesignBand();
//			band.setHeight(30);
//			int currentX = 0;
//			for(JRElement element : arr)
//			{
//				element.setX(currentX);
//				currentX += element.getWidth();
//				band.addElement((JRDesignStaticText)element);
//			}
//			design.setColumnHeader(band);
//			
//			band = new JRDesignBand();
//			band.setHeight(18);
//			JRElement[] elementDetailList = design.getDetail().getElements();
//			
//			arr = new ArrayList<JRElement>();
//			currentX = 0;
//			for(JRElement field : elementDetailList)
//			{
//				String key = field.getKey();
//				if("textField_pwd".equals(key) || "textField_status".equals(key))
//				{
//					continue;
//				}
//				arr.add(field);
//			}
//			for(JRElement element : arr)
//			{
//				element.setX(currentX);
//				currentX += element.getWidth();
//				band.addElement((JRDesignTextField)element);
//			}
//			design.setDetail(band);
			design = JRXmlLoader.load("E:\\price_List.jrxml");
			JRElement[] elements = design.getPageHeader().getElements();
			//获取pageHeader的高度
			int height = 0;
			int titleHeight = 0;
			if(design.getPageHeader() != null)
				height = design.getPageHeader().getHeight();
			if(design.getTitle() != null)
				titleHeight = design.getTitle().getHeight();
			JRDesignBand band = new JRDesignBand();
			band = (JRDesignBand)design.getTitle();
			//设置title的高度
			band.setHeight(band.getHeight() + height);
			//将pageHeader的元素添加的Title区域
			for(JRElement element : elements)
			{
				JRDesignElement tmpElement = (JRDesignElement)element;
				tmpElement.setY(titleHeight +tmpElement.getY());
				band.addElement(tmpElement);
			} 
			//设置Title
			design.setTitle(band);
			//清空PageHeader
			band = new JRDesignBand();
			band.setHeight(0);
			design.setPageHeader(band);
			//生成jxml文件
			//String filePath = String.valueOf(System.currentTimeMillis()) + ".jrxml";
			//JRXmlWriter.writeReport((JRReport)design, filePath, "UTF-8");
		} catch (JRException e) {
			
			e.printStackTrace();
		}
		return design;
	}
	/**
	 * 描述：
	 * @param filepath  报表文件路径
	 * @param displayMap 需要显示的项
	 * @param area		作用的区域（如：title：TitleBand，等）
	 * @param displayCol 需要动态显示选项 key:对应需要修改的列，value：对相应的表达式，直接用$F{XXX}/$F{XXX}表示
	 * @param count		每页显示的记录数
	 * @return
	 * @throws JRException
	 * 返回值：变更后报表的路径
	 */
	public static JasperDesign getDesign(String filepath,Map displayMap,String area) throws JRException
	{
		JasperDesign design = null;
		design = JRXmlLoader.load(filepath);
		JRElement[] elements = null;
		JRDesignBand band = new JRDesignBand();
		if(area.equals("Title"))
		{
			elements = design.getTitle().getElements();
			band = (JRDesignBand)design.getTitle();
		}
		if(area.equals("PageHeader"))
		{
			elements = design.getPageHeader().getElements();
			band = (JRDesignBand)design.getPageHeader();
		}
		if(area.equals("ColumnHeader"))
		{
			elements = design.getColumnHeader().getElements();
			band = (JRDesignBand)design.getColumnHeader();
		}
		if(area.equals("ColumnFooter"))
		{
			elements = design.getColumnFooter().getElements();
			band = (JRDesignBand)design.getColumnFooter();
		}
		if(area.equals("PageFooter"))
		{
			elements = design.getPageFooter().getElements();
			band = (JRDesignBand)design.getPageFooter();
		}
		if(area.equals("LastPageFooter"))
		{
			elements = design.getLastPageFooter().getElements();
			band = (JRDesignBand)design.getLastPageFooter();
		}
		if(area.equals("Summary"))
		{
			elements = design.getSummary().getElements();
			band = (JRDesignBand)design.getSummary();
		}
		Map rowCountMap = new HashMap();
		Map delMap = new HashMap();
		//获取所有元素所在的行的集合
		for(JRElement element : elements)
		{
			if(rowCountMap.get(element.getY())!= null)
			{
				Map keyMap = (Map)rowCountMap.get(element.getY());
				keyMap.put(element.getKey(), element.getHeight());
				rowCountMap.put(element.getY(), keyMap);
			}
			else
			{
				Map keyMap = new HashMap();
				keyMap.put(element.getKey(), element.getHeight());
				rowCountMap.put(element.getY(), keyMap);
			}
			//获取不需显示的列
			JRDesignElement textField = null;
			if(!(element instanceof JRDesignStaticText))
			{
				if(displayMap.get(element.getKey()) == null) 
				{
					delMap.put(element.getKey(), element);
				}
			}
		}
		int totalY = 0;
		
		for(JRElement element : elements)
		{
			JRDesignElement text = (JRDesignElement)element;
			//band = (JRDesignBand)design.getPageFooter();
			String key = element.getKey();
			if(delMap.get(key) != null) //需要删除的元素
			{
				Map keyMap = (Map)rowCountMap.get(element.getY());
				keyMap.remove(key);
				if(keyMap.isEmpty())
				{
					totalY += element.getHeight();
				}
				band.removeElement(text);
				continue;
			}
			text.setY(text.getY() - totalY);	
		}
		band.setHeight(band.getHeight() - totalY);
		if(area.equals("Title"))
			design.setTitle(band);
		if(area.equals("PageHeader"))
			design.setPageHeader(band);
		if(area.equals("ColumnHeader"))
			design.setColumnHeader(band);
		if(area.equals("ColumnFooter"))
			design.setColumnFooter(band);
		if(area.equals("PageFooter"))
			design.setPageFooter(band);
		if(area.equals("LastPageFooter"))
			design.setLastPageFooter(band);
		if(area.equals("Summary"))
			design.setSummary(band);
		
		
		return design;
	}
	/**
	 * 描述：
	 * @param filepath  报表文件路径
	 * @param displayMap 需要显示的项
	 * @param area		作用的区域（如：title：TitleBand，等）
	 * @param displayCol 需要动态显示选项 key:对应需要修改的列，value：对相应的表达式，直接用$F{XXX}/$F{XXX}表示
	 * @param pageSize		每页显示的记录数
	 * @return
	 * @throws JRException
	 * 返回值：变更后报表的路径
	 */
	public static JasperDesign getDesign(String filepath,Map displayMap,String area,Map displayCol,int pageSize) throws JRException
	{
		JasperDesign design = RptDesign4SD.getDesign(filepath, displayMap, area);
		
		
		//计算除了detail区外的文档高度
		int docHeightNodetail = 0;
		if(design.getTitle() != null)
			docHeightNodetail += design.getTitle().getHeight();
		if(design.getPageHeader() != null)
			docHeightNodetail += design.getPageHeader().getHeight();
		if(design.getColumnHeader() != null)
			docHeightNodetail += design.getColumnHeader().getHeight();
		if(design.getColumnFooter() != null)
			docHeightNodetail += design.getColumnFooter().getHeight();
		if(design.getPageFooter() != null)
			docHeightNodetail += design.getPageFooter().getHeight();
		if(design.getLastPageFooter() != null)
			docHeightNodetail += design.getLastPageFooter().getHeight();
		if(design.getSummary() != null)
			docHeightNodetail += design.getSummary().getHeight();
		if(design.getNoData() != null)
			docHeightNodetail += design.getNoData().getHeight();
		docHeightNodetail += design.getTopMargin() + design.getBottomMargin();
		//动态处理detail区域
		JRDesignBand band = (JRDesignBand)design.getDetail();

		if(pageSize > 0)
		{
			//设置每条记录的高度
			int detailHeight = design.getDetail().getHeight();
			int realDetailHeight = design.getPageHeight() - docHeightNodetail;
			int detailPerHeight = realDetailHeight / pageSize;
			if(detailHeight >= detailPerHeight)
				detailPerHeight = detailHeight;
			band = (JRDesignBand)design.getDetail();
			band.setHeight(detailPerHeight);
			design.setDetail(band);
		}
		//设置需要显示的列
		if(displayCol == null || displayCol.isEmpty())
			return design;
		Iterator iterator = displayCol.keySet().iterator();
		while(iterator.hasNext())
		{
			String key = (String)iterator.next();
			String value = (String)displayCol.get(key);
			JRDesignTextField element = (JRDesignTextField)design.getDetail().getElementByKey(key);
			JRDesignExpression exp = (JRDesignExpression)element.getExpression();
			exp.setText(value);
			element.setExpression(exp);
		}

		return design;
	}
	/**
	 * 描述：将area区的元素移动到title区
	 * @param filepath  报表文件路径
	 * @param displayMap 需要显示的项
	 * @param area		作用的区域（如：title：TitleBand，等）
	 * @return
	 * @throws JRException 
	 * @throws JRException
	 * 返回值：JasperDesign
	 */
	public static JasperDesign moveDesign(String filepath,Map displayMap,String area) throws JRException{
		
		JasperDesign design = RptDesign4SD.getDesign(filepath, displayMap, area);
		int titleHeight = design.getTitle().getHeight();
		JRDesignBand titleBand = (JRDesignBand)design.getTitle();
		JRElement[] elements = null;
		JRDesignBand band = new JRDesignBand();
		
		if(area.equals("PageHeader"))
		{
			elements = design.getPageHeader().getElements();
			band = (JRDesignBand)design.getPageHeader();
		}
		else if(area.equals("ColumnHeader"))
		{
			elements = design.getColumnHeader().getElements();
			band = (JRDesignBand)design.getColumnHeader();
		}
		else if(area.equals("ColumnFooter"))
		{
			elements = design.getColumnFooter().getElements();
			band = (JRDesignBand)design.getColumnFooter();
		}
		else if(area.equals("PageFooter"))
		{
			elements = design.getPageFooter().getElements();
			band = (JRDesignBand)design.getPageFooter();
		}
		else if(area.equals("LastPageFooter"))
		{
			elements = design.getLastPageFooter().getElements();
			band = (JRDesignBand)design.getLastPageFooter();
		}
		else if(area.equals("Summary"))
		{
			elements = design.getSummary().getElements();
			band = (JRDesignBand)design.getSummary();
		}
		titleBand.setHeight(titleHeight + 200 + band.getHeight());
		JRDesignStaticText titleEle = new JRDesignStaticText();
		titleEle.setText("For Details Please See Attachment.");
		titleEle.setY(titleHeight + 80);
		titleEle.setWidth(design.getPageWidth()-design.getLeftMargin()-design.getRightMargin());
		titleEle.setHeight(30);
		titleEle.setY(titleHeight + 50);
		titleEle.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		titleEle.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
		titleEle.setBold(true);
		titleEle.setFontSize(14);
		titleBand.addElement((JRDesignElement)titleEle);
		for(JRElement ele : elements)
		{
			JRDesignElement element = (JRDesignElement)ele;
			element.setY(element.getY() + titleHeight +200);
			titleBand.addElement(element);
			band.removeElement(element);
		}
		band.setHeight(0);
		design.setTitleNewPage(true);
		design.setTitle(titleBand);
		return design;
	}
	public static JasperDesign moveDesign(JasperDesign design,String area,boolean attachText)
	{
		int titleHeight = design.getTitle().getHeight();
		JRDesignBand titleBand = (JRDesignBand)design.getTitle();
		JRElement[] elements = null;
		JRDesignBand band = new JRDesignBand();
		
		if(area.equals("PageHeader"))
		{
			elements = design.getPageHeader().getElements();
			band = (JRDesignBand)design.getPageHeader();
		}
		else if(area.equals("ColumnHeader"))
		{
			elements = design.getColumnHeader().getElements();
			band = (JRDesignBand)design.getColumnHeader();
		}
		else if(area.equals("ColumnFooter"))
		{
			elements = design.getColumnFooter().getElements();
			band = (JRDesignBand)design.getColumnFooter();
		}
		else if(area.equals("PageFooter"))
		{
			elements = design.getPageFooter().getElements();
			band = (JRDesignBand)design.getPageFooter();
		}
		else if(area.equals("LastPageFooter"))
		{
			elements = design.getLastPageFooter().getElements();
			band = (JRDesignBand)design.getLastPageFooter();
		}
		else if(area.equals("Summary"))
		{
			elements = design.getSummary().getElements();
			band = (JRDesignBand)design.getSummary();
		}
		if(attachText)
		{
			titleBand.setHeight(titleHeight + 200 + band.getHeight());
			JRDesignStaticText titleEle = new JRDesignStaticText();
			titleEle.setText("For Details Please See Attachment.");
			titleEle.setY(titleHeight + 80);
			titleEle.setWidth(design.getPageWidth()-design.getLeftMargin()-design.getRightMargin());
			titleEle.setHeight(30);
			titleEle.setY(titleHeight + 50);
			titleEle.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
			titleEle.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
			titleEle.setBold(true);
			titleEle.setFontSize(14);
			titleBand.addElement((JRDesignElement)titleEle);
		}
		else
			titleBand.setHeight(titleHeight + band.getHeight());
		for(JRElement ele : elements)
		{
			JRDesignElement element = (JRDesignElement)ele;
			if(attachText)
				element.setY(element.getY() + titleHeight +200);
			else
				element.setY(element.getY() + titleHeight);
			titleBand.addElement(element);
			band.removeElement(element);
		}
		//添加订单号
		band.setHeight(20);
		JRDesignTextField field = new JRDesignTextField();
		JRDesignExpression exp = new JRDesignExpression();
		exp.setText("\"No：\"+$F{o_ORDER_NO}");
		field.setExpression(exp);
		field.setHeight(20);
		field.setWidth(255);
		field.setX(0);
		band.addElement((JRDesignElement)field);
		design.setTitleNewPage(true);
		design.setTitle(titleBand);
		return design;
	}
	public static String getRptPath(String filepath,Map displayMap,String area,Map displayCol,int pageSize) throws Exception
	{
		String path = "";
		if(displayMap == null || area == null)
			return filepath;
		try {
			JRReport design = RptDesign4SD.getDesign(filepath, displayMap, area, displayCol, pageSize);
		} catch (JRException e) {
			e.printStackTrace();
			throw new Exception("发生报表转换异常"+e.getMessage());
		}
		String classpath = CotExportRptDaoImpl.class.getResource("/").getPath();
		int index = classpath.indexOf("WEB-INF");
		classpath = classpath.substring(1,index) + "reportfile/tmp";
		path = classpath + File.separator + String.valueOf(System.currentTimeMillis()) + ".jrxml";
		return path;
	}
	public static void main(String[] args) throws JRException
	{
		JasperDesign design = RptDesign4SD.getDesign("E:\\orderRpt.jrxml", new HashMap(), "PageFooter");
		
		design = RptDesign4SD.moveDesign(design,"PageHeader",true);
		try {
			//JasperCompileManager.compileReportToFile(design, "E:\\achui.jrxml");
			net.sf.jasperreports.engine.xml.JRXmlWriter.writeReport((JRReport)design, "E:\\achui.jrxml", "UTF-8");
		} catch (JRException e) {

			e.printStackTrace();
		}
	}
	//E:\apache-tomcat-6.0.18\webapps\CotSystem\reportfile
}
