package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotTrailCar;
import com.sail.cot.query.QueryInfo;


public interface CotTrailCarService {

	//根据id获取拖车行信息
	public CotTrailCar getTrailCarById(Integer id);
	
	//添加或修改拖车行信息
	public void saveOrUpdateTrailCar(List<CotTrailCar> trailcarList);
	
	//批量删除拖车行信息
	public void deleteTrailCarByList(List<Integer> ids);
	
	//获取拖车行信息总数
	public int getTrailCarCount(QueryInfo queryInfo);
	
	//获取拖车行信息类表（分页）
	public List<?> getTrailCarList(QueryInfo queryInfo);
	
	//判断拖车行是否存在 true:存在 false：不存在
	public boolean findExistTrailCar(String id,String name);
	
	public List getTrailCarList();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
