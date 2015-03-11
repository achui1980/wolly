/**
 * 
 */
package com.sail.cot.service.sample;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sail.cot.domain.CotModPrice;
import com.sail.cot.query.QueryInfo;


public interface CotModPriceService {
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);
	
	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo);
	
	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);
	
	//根据编号获得对象
	public CotModPrice getCotModPriceById(Integer id);
	
	//保存
	public void addList(List<?> list);
	
	//修改
	public void updateList(List<?> list) ;
	
	//  删除
	public void deleteList(List<Integer> ids);
	
	//根据货号查找样品编号
	public Integer findEleIdByEle(String eleId);
	
}
