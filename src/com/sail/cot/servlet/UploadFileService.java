/**
 * 
 */
package com.sail.cot.servlet;
 
import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger; 

import jxl.Workbook;

import com.sail.cot.domain.CotFile;
import com.sail.cot.service.sample.CotFileService;
import com.jason.core.exception.ServiceException;
import com.sail.cot.util.Log4WebUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 13, 2008 10:31:23 AM </p>
 * <p>Class Name: UploadFileService.java </p>
 * @author achui
 *
 */
public class UploadFileService extends UploadService{

	/* (non-Javadoc)
	 * @see com.sail.cot.servlet.UploadService#doPorcess(java.util.HashMap)
	 */
	Logger logger = Log4WebUtil.getLogger(UploadFileService.class);
	@Override
	public void doPorcess(HashMap map) throws ServiceException {
		//获取系统路径
		String classPath = UploadFileService.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		
		//获取当时间
		Date currDate = new Date(System.currentTimeMillis());
		 
		String fileName=(String) map.get("fileName");
		String filePath=(String) map.get("filePath");
		Integer eleId= new Integer( map.get("eleId").toString());
		String fileDesc=(String) map.get("fileDesc");
		 
		CotFileService cotFileService= (CotFileService) super.getService("CotFileService");
		CotFile cotFile=new CotFile();
		cotFile.setFileName(fileName);
		cotFile.setFilePath(filePath);
		cotFile.setFileDate(currDate);
		cotFile.setEleId(eleId);
		cotFile.setFileDesc(fileDesc);
		
		String realPath = systemPath + filePath;
		File file = new File(realPath); 
		if(file.exists()){
			try {
				FileInputStream in = new FileInputStream(file);
				byte[] fileContent = new byte[(int)(file.length())];
				while (in.read(fileContent)!= -1) {}
				in.close();
				cotFile.setFileContent(fileContent);
				file.delete();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<CotFile> fileList = new ArrayList<CotFile>();
		fileList.add(cotFile);
		cotFileService.addFile(fileList);
		 
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.servlet.UploadService#doPorcess(jxl.Workbook, java.util.HashMap)
	 */
	@Override
	public void doPorcess(Workbook wb, HashMap map) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}
