package com.sail.cot.service.systemdic;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotNationCity;
import com.sail.cot.query.QueryInfo;

public interface CotNationCityService {

	List<?> getNationCityList();
	 
	CotNationCity getNationCityById(Integer Id);
	 
	void addNationCity(List<CotNationCity> nationcityList);
 
	void modifyNationCity(List<CotNationCity> nationcityList);
 
	int deleteNationCity(List<CotNationCity> nationcityList);

	Map<?, ?> getNationMap();
	
	Map<?, ?> getProvinceMap();
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//查询城市名称是否重复
	public Integer findExistByNo(String name, String id);
	
	//保存
	public boolean saveOrUpdate(CotNationCity eleOther);
}
