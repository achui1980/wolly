package com.sail.cot.service.sample;

import java.util.List; 
 

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotFile;
import com.sail.cot.query.QueryInfo;

public interface CotFileService {

	List<?> getFileList();
	 
	CotFile getFileById(Integer Id);
	
	public byte[] getFileContentById(Integer Id);
	 
	void addFile(List<CotFile> fileList);
 
	void modifyFile(List<CotFile> fileList);
	 
	boolean deleteFile(List<CotFile> fileList);
 
	boolean findExistName(String name);
	
	void deleteUploadFileById(Integer Id);
	
	void deleteByFileName(String fileName);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	 
}
