package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;
  
 
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotBoxType; 
import com.sail.cot.query.QueryInfo;

public interface CotBoxTypeService {

	List<?> getBoxTypeList();
	 
	CotBoxType getBoxTypeById(Integer Id);
	 
	void addBoxType(List<CotBoxType> BoxTypeList);
 
	boolean modifyBoxType(List<CotBoxType> BoxTypeList);
	 
	int deleteBoxType(List<CotBoxType> BoxTypeList);
 
	boolean findExistByName(String name);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	//得到内盒包装名称的集合
	public List<?> getBoxINameList();
	
	//得到中盒包装名称的集合
	public List<?> getBoxMNameList();
	
	//得到外盒包装名称的集合
	public List<?> getBoxONameList();
	
	//得到产品包装名称的集合
	public List<?> getBoxPNameList();
	
	Map<?, ?> getBoxPackingMap();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
 }
