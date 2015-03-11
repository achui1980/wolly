package com.sail.cot.servlet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import jxl.Workbook;

import com.jason.core.exception.ServiceException;

import com.sail.cot.domain.CotRptFile;

import com.sail.cot.service.sample.CotRptFileService;
import com.sail.cot.util.Log4WebUtil;

public class UploadReportFileService extends UploadService {

	Logger logger = Log4WebUtil.getLogger(UploadReportImgService.class);
	@Override
	public void doPorcess(HashMap map) throws ServiceException {
		// TODO Auto-generated method stub
		 logger.info("获取页面参数:"+map.get("rptfilePath"));
		 logger.info("获取页面参数:"+map.get("rptName"));
		 
		 String rptfilePath=(String) map.get("rptfilePath");
		 String rptName=(String) map.get("rptName");
		 Integer rptId= new Integer( map.get("rptId").toString());
		     System.out.println("rptName="+rptName);
		     System.out.println("rptfilePath="+rptfilePath);
		     System.out.println("rptId="+rptId);
			  
		 CotRptFileService cotRptFileService= (CotRptFileService) super.getService("CotRptFileService");
		 CotRptFile cotRptFile = cotRptFileService.getRptFileById(rptId);
		 cotRptFile.setRptName(rptName);
		 cotRptFile.setRptfilePath(rptfilePath); 
		 
		 
		 List rptfileList=new ArrayList();
		 rptfileList.add(cotRptFile);
		 //cotRptFileService.modifyRptFile(rptfileList);
		 System.out.println("上传成功！");
	}

	@Override
	public void doPorcess(Workbook wb, HashMap map) throws ServiceException {
		// TODO Auto-generated method stub

	}
	
	public CotRptFile getRptFile(HashMap map)throws ServiceException
	{
		CotRptFile cotRptFile = new CotRptFile();
		String rptfilePath=(String) map.get("rptfilePath");
		String rptName=(String) map.get("fileName");
		cotRptFile.setRptName(rptName);
		cotRptFile.setRptfilePath(rptfilePath); 
		return cotRptFile;
	}

}
