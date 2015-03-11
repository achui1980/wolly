package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;

import com.sail.cot.domain.CotCustomizeField;

public interface CotCustomizeService {
	public void saveCotCustomize(List list,String type,Integer empsId);
	public List<String> getCotCustomizeFields(Integer empId,String type);
	//返回map
	public Map getCotCustomizeFieldMap(String type);
	//读取XML
	public Map readXML();
	//读取XML的数据
	public Map initPanel();
}
