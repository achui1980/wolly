package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotCurrency;

public interface CotCurrencyService {
	
	//保存
	public boolean saveOrUpdate(CotCurrency typeLv2);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//查询是否重复
	public Integer findExistByNo(String typeName, String id);
	
	//根据id获得对象
	public CotCurrency getObjById(Integer id);
	
	//获得所有产品分类
	public List<?> getList();
}
