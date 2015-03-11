package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotContract;

public interface CotContractService {
	
	//保存
	public boolean saveOrUpdate(CotContract typeLv2);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//根据id获得对象
	public CotContract getObjById(Integer id);
	
	//获得所有对象
	public List<?> getList();
}
