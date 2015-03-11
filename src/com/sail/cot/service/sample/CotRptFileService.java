package com.sail.cot.service.sample;

import java.util.List;
import java.util.Map;
 
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotRptFile; 
import com.sail.cot.query.QueryInfo;

public interface CotRptFileService {

	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	List<?> getRptFileList();
	 
	CotRptFile getRptFileById(Integer Id);
	 
	void saveOrUpdateRptFile(CotRptFile rptfile);
	 
	void deleteRptFile(List<CotRptFile> rptfileList);
	
	void delRptFileById(Integer Id);
 
	boolean findExistByName(String name);
	
	public  void delImg(String filePathAndName);
	
	Map<?, ?> getMap();
	
    void delUploadFileById(Integer Id);
	
	void delUploadImgById(Integer Id);
	
	//设置默认模板
	public boolean setRptDefault(Integer rptId);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
