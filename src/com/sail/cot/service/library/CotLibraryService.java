package com.sail.cot.service.library;

import java.util.List;
 
import com.sail.cot.domain.CotSaleInfo; 
import com.sail.cot.query.QueryInfo;

public interface CotLibraryService {

	List<?> getLibraryList();
	 
	CotSaleInfo getLibraryById(Integer Id);
	 
	void addLibrary(List<CotSaleInfo> libraryList);
 
	void modifyLibrary(List<CotSaleInfo> libraryList);
	 
	void deleteLibrary(List<CotSaleInfo> libraryList);
 
	boolean findExistByName(String name);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	//批量删除库存信息
	public void deleteLibraryByList(List<Integer> ids);
	
	//添加或修改库存信息
	public Integer saveOrUpdateLibrary(CotSaleInfo cotSaleInfo,String recordDate);
	
 }
