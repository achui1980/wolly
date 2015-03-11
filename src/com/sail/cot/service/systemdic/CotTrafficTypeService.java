package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotTrafficType;
import com.sail.cot.query.QueryInfo;

public interface CotTrafficTypeService {

    public boolean addTrafficType(CotTrafficType cotTrafficType);  
	
	public void modifyTrafficType(CotTrafficType cotTrafficType);   
	
	public boolean deleteTrafficType(List<CotTrafficType> TrafficTypeList);  
	
	public CotTrafficType getTrafficTypeById(Integer id);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public List getTrafficList();
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name);
	
	public Integer isExistName(String name);
}
