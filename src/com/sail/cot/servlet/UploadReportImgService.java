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

public class UploadReportImgService extends UploadService {

	Logger logger = Log4WebUtil.getLogger(UploadReportImgService.class);
	@Override
	public void doPorcess(HashMap map) throws ServiceException {
		// TODO Auto-generated method stub
		 logger.info("获取页面参数rptImgPath:"+map.get("rptImgPath"));
		 logger.info("获取页面参数rptId:"+map.get("rptId"));
		 
		 String rptImgPath=(String) map.get("rptImgPath");
		 Integer rptId= new Integer( map.get("rptId").toString());
		      
			  
		 CotRptFileService cotRptFileService= (CotRptFileService) super.getService("CotRptFileService");
		 CotRptFile cotRptFile = cotRptFileService.getRptFileById(rptId);
		 cotRptFile.setRptImgPath(rptImgPath);
		 System.out.println("cotRptFile.rptImgPath="+cotRptFile.getRptImgPath());
		 
		 List rptfileList=new ArrayList();
		 rptfileList.add(cotRptFile);
		 //cotRptFileService.modifyRptFile(rptfileList);
		 System.out.println("上传成功！");
	}

	@Override
	public void doPorcess(Workbook wb, HashMap map) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public CotRptFile getRptImgPath(HashMap map)throws ServiceException
	{
		CotRptFile cotRptFile = new CotRptFile();
		String rptImgPath=(String) map.get("rptImgPath");
		cotRptFile.setRptImgPath(rptImgPath);
		return cotRptFile;
	}
}
