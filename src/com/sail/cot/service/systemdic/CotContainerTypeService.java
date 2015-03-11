package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotContainerType;

public interface CotContainerTypeService {
	
	//保存
	public boolean saveOrUpdate(CotContainerType typeLv2);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//查询是否重复
	public Integer findExistByNo(String typeName, String id);
	
	//根据id获得对象
	public CotContainerType getObjById(Integer id);
	
	//获得所有对象
	public List<?> getList();
}
