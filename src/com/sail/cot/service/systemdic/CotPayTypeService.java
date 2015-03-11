package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotPayType;

public interface CotPayTypeService {
	
	//保存
	public boolean saveOrUpdate(CotPayType typeLv2);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//查询是否重复
	public Integer findExistByNo(String payName, String id);
	
	//根据id获得对象
	public CotPayType getObjById(Integer id);
	
	//获得所有对象
	public List<?> getList();
}
