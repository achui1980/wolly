/**
 * 
 */
package com.sail.cot.dao;

import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:报表导出工具</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Sep 4, 2008 9:31:25 AM </p>
 * <p>Class Name: CotExportRpt.java </p>
 * @author achui
 *
 */
public interface CotExportRptDao {

	public int exportRptToXLS(String rptXMLPath,String exportPath,HashMap paramMap);
	
	public int exportRptToPDF(String rptXMLPath,String exportPath,HashMap paramMap) ;
	
	public int exportRptToHTML(String rptXMLPath,String exportPath,HashMap paramMap) ;
	
	public int exportRptToXLSBytes(String rptXMLPath,String exportPath,HashMap paramMap,byte[] printBytes);
	
	 JasperPrint getJasperPrint(String rptXMLPath,HashMap paramMap) throws JRException;
	
}