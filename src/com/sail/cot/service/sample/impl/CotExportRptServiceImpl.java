/**
 * 
 */
package com.sail.cot.service.sample.impl;

import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import com.sail.cot.dao.CotExportRptDao;
import com.sail.cot.service.sample.CotExportRptService;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Sep 5, 2008 6:14:09 PM </p>
 * <p>Class Name: CotExportRptServiceImpl.java </p>
 * @author achui
 *
 */
public class CotExportRptServiceImpl implements CotExportRptService {

	CotExportRptDao rptDao;
	/* (non-Javadoc)
	 * @see com.sail.cot.service.sample.CotExportRptService#exportRptToHTML(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public int exportRptToHTML(String rptXMLPath, String exportPath,
			HashMap<?,?> paramMap) {
		 
		return this.getRptDao().exportRptToHTML(rptXMLPath, exportPath, paramMap);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.sample.CotExportRptService#exportRptToPDF(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public int exportRptToPDF(String rptXMLPath, String exportPath,
			HashMap<?,?> paramMap) {
		 
		return this.getRptDao().exportRptToPDF(rptXMLPath, exportPath, paramMap);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.sample.CotExportRptService#exportRptToXLS(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public int exportRptToXLS(String rptXMLPath, String exportPath,
			HashMap<?,?> paramMap) {
		 
		return this.getRptDao().exportRptToXLS(rptXMLPath, exportPath, paramMap);
	}

	public CotExportRptDao getRptDao() {
		return rptDao;
	}

	public void setRptDao(CotExportRptDao rptDao) {
		this.rptDao = rptDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.sample.CotExportRptService#getJasperPrint(java.lang.String, java.util.HashMap)
	 */
	public JasperPrint getJasperPrint(String rptXMLPath, HashMap<?,?> paramMap) {
		 
		JasperPrint jasperPrint = null;
		try {
			jasperPrint =  this.getRptDao().getJasperPrint(rptXMLPath, paramMap);
		} catch (JRException e) {
			 
			e.printStackTrace();
		}
		return jasperPrint;
	}

}
