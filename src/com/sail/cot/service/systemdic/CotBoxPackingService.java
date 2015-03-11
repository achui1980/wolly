package com.sail.cot.service.systemdic;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.query.QueryInfo;


public interface CotBoxPackingService {

	//获取箱盒包装信息列表（加载下拉列表框用）
	public List<?> getBoxPackingSelList();
	
	//根据id获取箱盒包装信息
	public CotBoxPacking getBoxPackingById(Integer id);
	
	//添加或修改箱盒包装信息
	public boolean saveOrUpdateBoxPacking(CotBoxPacking packing);
	
	//批量删除箱盒包装信息
	public int deleteBoxPackingByList(List<Integer> ids);
	
	//获取箱盒包装信息总数
	public int getBoxPackingCount(QueryInfo queryInfo);
	
	//获取箱盒包装信息类表（分页）
	public List<?> getBoxPackingList(QueryInfo queryInfo);
	
	//判断箱盒包装是否存在 true:存在 false：不存在
	public boolean findExistBoxPacking(String id,String value);
	
	//获得厂家简称的映射
	public Map<?, ?> getFacMap();
	
	//批量添加公式
	public void updateCalculator(String ids,String formulaIn,String formulaOut,String checkCalculation);
	
	//验证公式是否正确
	public boolean checkCalculation(String str);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
