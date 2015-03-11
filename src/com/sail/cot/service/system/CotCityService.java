
package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCity;
import com.sail.cot.query.QueryInfo;

 
public interface CotCityService {
	
	//获取省份信息列表
	
	List<?> getCityList();
	//获取省份信息
	CotCity getCityById(Integer Id);
	//添加省份信息
	void addCity(List<?> cityList);
	//修改省份信息
	void modifyCity(List<?> cityList);
	//删除省份信息
	int deleteCity(List<?> cityList);
	 
	//根据名称判断是否存在
	boolean findExistByName(String name);
	
	Map<?, ?> getMap();
	
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
