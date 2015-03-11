package com.sail.cot.service.sample;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotPriceFac;
import com.sail.cot.query.QueryInfo;

public interface CotPriceFacService {

	public CotPriceFac getPriceFacById(Integer Id);
	 
	public Integer addOrUpdatePriceFac(CotPriceFac priceFac,String addTime);
	
	//删除报价
	public Boolean delPriceFac(List<CotPriceFac> priceFacList);
	 
	List<?> getCotCurrencyList();
	
	Map<?, ?> getMap();
	
	Map<?, ?> getFacMap();
	
	public boolean findRecord();
	
	//根据查询语句获取记录
	public List<?> getList(QueryInfo queryInfo);
	
	//根据查询语句获取总记录数	
	public int getRecordCount(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
}
