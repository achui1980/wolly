package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotTradeType;
import com.sail.cot.query.QueryInfo;

public interface CotTradeTypeService {
	
	public void addTradeType(CotTradeType cotTradeType);  
	
	public void modifyTradeType(CotTradeType cotTradeType);   
	
	public void deleteTradeType(List<CotTradeType> TradeTypeList);  
	
	public CotTradeType getTradeTypeById(Integer id);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List getTradeTypeList();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
