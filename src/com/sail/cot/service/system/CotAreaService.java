/**
 * 
 */
package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;
 
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotArea;  
import com.sail.cot.query.QueryInfo;
 
public interface CotAreaService {
//获取城市信息列表
	
	List<?> getAreaList();
	//获取城市信息
	CotArea getAreaById(Integer Id);
	//添加城市信息
	void addArea(List<CotArea> areaList);
	//修改城市信息
	void modifyArea(List<CotArea> areaList);
	//删除城市信息
	int deleteArea(List<CotArea> areaList);
	 
	//根据名称判断是否存在
	boolean findExistByName(String name);
	
	Map<?, ?> getMap();
	
	public List<CotArea> getAreaListByCityId(Integer cityId);
	
	//生成城市地区属
	public List<?> getCityAreaTree();
	
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	 
}
