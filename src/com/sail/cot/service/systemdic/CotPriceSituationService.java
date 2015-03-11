package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotPriceSituation;

public interface CotPriceSituationService {
	
	//保存
	public boolean saveOrUpdate(CotPriceSituation typeLv2);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//查询是否重复
	public Integer findExistByNo(String situationName, String id);
	
	//根据id获得对象
	public CotPriceSituation getObjById(Integer id);
	
	//获得所有对象
	public List<?> getList();
}
