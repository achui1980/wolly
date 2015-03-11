/**
 * 
 */
package com.sail.cot.service.sample;

import java.util.List;



/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:23:39 PM </p>
 * <p>Class Name: CotSystemMenu.java </p>
 * @author achui
 *
 */
public interface CotReportService {
	
	//根据文件路径导入
	public List<?> saveReport(String filePath,Boolean isCover);
	
	//根据文件路径和行号导入excel
	public List<?> updateOneReport(String filePath,Integer rowNum, Boolean isCover);
}
