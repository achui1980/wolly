/**
 * 
 */
package com.sail.cot.service.sample;

import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Sep 5, 2008 6:12:48 PM </p>
 * <p>Class Name: CotExportRptService.java </p>
 * @author achui
 *
 */
public interface CotExportRptService {
	
	public int exportRptToXLS(String rptXMLPath,String exportPath,HashMap<?,?> paramMap);
	
	public int exportRptToPDF(String rptXMLPath,String exportPath,HashMap<?,?> paramMap) ;
	
	public int exportRptToHTML(String rptXMLPath,String exportPath,HashMap<?,?> paramMap) ;
	
	public JasperPrint getJasperPrint(String rptXMLPath,HashMap<?,?> paramMap);
}
