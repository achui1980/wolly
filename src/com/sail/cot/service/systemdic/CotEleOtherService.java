package com.sail.cot.service.systemdic;

import java.util.List;

import com.sail.cot.domain.CotEleOther;

public interface CotEleOtherService {
	
	//保存
	public boolean saveOrUpdate(CotEleOther typeLv2);
	
	//删除
	public boolean deleteByIds(List<?> TypeLvList);
	
	//查询报关中文名是否重复
	public Integer findExistByNo(String typeName, String id);
	
	//根据id获得对象
	public CotEleOther getObjById(Integer id);
	
	//获得所有产品分类
	public List<?> getList();
	
	// 更改样品档案的海关编码的退税率
	public int updateEleTax(Float tax,Integer eleHsid);
}
