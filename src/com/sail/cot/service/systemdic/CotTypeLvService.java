package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotTypeLv1;

public interface CotTypeLvService {
	
	//保存
	public boolean saveOrUpdate(CotTypeLv1 typeLv);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//查询英文名是否重复
	public Integer findExistByEnNo(String typeEnName, String id);
	
	//查询中文名是否重复
	public Integer findExistByNo(String typeName, String id);
	
	//根据id获得对象
	public CotTypeLv1 getObjById(Integer id);
	
	//获得所有产品分类
	public List<?> getList();
}
