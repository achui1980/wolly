package com.sail.cot.service.sample.impl;

import java.io.File;

import java.util.ArrayList; 
import java.util.List; 

import org.apache.log4j.Logger;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.sample.CotFileDao; 
import com.sail.cot.domain.CotFile;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotFileService; 
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotFileServiceImpl   implements CotFileService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private CotFileDao cotFileDao;
	public CotFileDao getCotFileDao() {
		return cotFileDao;
	}

	public void setCotFileDao(CotFileDao cotFileDao) {
		this.cotFileDao = cotFileDao;
	}

	 
	private Logger logger = Log4WebUtil.getLogger(CotFileServiceImpl.class);
	
	
	public void addFile(List<CotFile> fileList) {
		try {
			this.getCotBaseDao().saveRecords(fileList);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public boolean deleteFile(List<CotFile> fileList) {
		List<Integer> ids=new ArrayList<Integer>();
        for(int i=0; i<fileList.size(); i++)
		{
        	CotFile cotFile = (CotFile)fileList.get(i);
			ids.add(cotFile.getId());
		}
         try{
        	this.getCotBaseDao().deleteRecordByIds(ids, "CotFile");
        	return true;
        }
        catch(DAOException e)
        {
        	logger.error("删除文件信息异常",e);
        	return false;
        }
	}

	public void modifyFile(List<CotFile> fileList) {
		try{
     	   this.getCotBaseDao().updateRecords(fileList);
        }catch(Exception ex)
        {
     	   logger.error("更新文件信息异常", ex);
        }
	}
	
	public boolean findExistName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotFile obj where obj.fileName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotFile obj where obj.fileName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	 

	public CotFile getFileById(Integer Id) {
		CotFile cotFile=(CotFile)this.getCotBaseDao().getById(CotFile.class, Id);
		cotFile = (CotFile)SystemUtil.deepClone(cotFile);
		if(cotFile!=null){
			cotFile.setFileContent(null);
			return cotFile;
		}
		return null;
	}
	
	public byte[] getFileContentById(Integer Id) {
		CotFile cotFile=(CotFile)this.getCotBaseDao().getById(CotFile.class, Id);
		if(cotFile!=null){
			byte[] fileContent = cotFile.getFileContent();
			if(fileContent!=null){
				return fileContent;
			}
		}
		return null;
	}

	public List<?> getFileList() {
		return this.getCotBaseDao().getRecords("CotFile");
	}

	 

	public void deleteUploadFileById(Integer Id) {
		CotFile cotFile=(CotFile)this.getFileById(Id);
		String filePath=cotFile.getFilePath();
		File file=new File(filePath);
		file.delete();
		 
	}

	public void deleteByFileName(String fileName) {
		this.getCotFileDao().deleteByName(fileName);
	}
	
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getCotBaseDao().getJsonData(queryInfo);
	} 
	
	
}
