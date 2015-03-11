package com.sail.cot.service.systemdic;

import java.util.List;
import java.util.Map;

import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.query.QueryInfo;

public interface CotPriceCfgService {
	
	public void addPriceCfg(CotPriceCfg cotPriceCfg);
	
	public Integer modifyPriceCfg(CotPriceCfg cotPriceCfg);
	
	public List<?> getObjList(String objName);
	
	public CotPriceCfg getPriceCfgById(Integer id);
	
	public List getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public Map getShipPortMap();
	
	public Map getCaluseMap();

	public Map getCurrencyMap();
	
	public boolean findRecord();
	
	public Map getSituationMap();
	
	public Map getContainerMap();
	
	public Map getTrafficMap();
	
	public Map getCompanyMap();
	
	//得到公司的集合
	public List<?> getCompanyList();
	
	// 得到objName的集合
	public List<?> getDicList(String objName);
	
	public CotPriceCfg getPriceCfg();
}
