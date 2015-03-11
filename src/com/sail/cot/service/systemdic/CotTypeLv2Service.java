package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotTypeLv2;

public interface CotTypeLv2Service {
	
	//保存
	public boolean saveOrUpdate(CotTypeLv2 typeLv2);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//查询是否重复
	public Integer findExistByNo(String typeName, String id);
	
	//根据id获得对象
	public CotTypeLv2 getObjById(Integer id);
	
	//获得所有对象
	public List<?> getList();
}
