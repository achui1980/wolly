package com.sail.cot.service.systemdic;

import java.util.List;
import java.util.Map;
 
 
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotProvince;
import com.sail.cot.query.QueryInfo;

public interface CotProvinceService {

	List<?> getProvinceList();
 
	CotProvince getProvinceById(Integer Id);
	 
	void addProvince(List<CotProvince> provinceList);
 
	void modifyProvince(List<CotProvince> provinceList);
 
	int deleteProvince(List<CotProvince> provinceList);
 
	boolean findExistByName(String name);
	
	Map<?, ?> getMap();

	public List<?> getProvinceListByNationId(Integer nationId);
	
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
