/**
 * 
 */
package com.sail.cot.service.finace;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.query.QueryInfo;


public interface CotRecvService {

	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);

	//得到集合
	public List<?> getList(QueryInfo queryInfo);
	
	//查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);
	
	//查询所有员工
	public Map<?, ?> getEmpsMap();
	
	// 查询所有客户简称
	public Map<?, ?> getCustNameMap();
	
	//查询所有厂家
	public Map<?, ?> getFactoryNameMap();
	
	//查询所有公司
	public Map<?, ?> getCompanyNameMap();
	
	//根据id获取付款记录信息
	public CotFinaceAccountdeal getCotDealById(Integer id);
	
	//获取系统当前登陆员工
	public CotEmps getCurEmp();
	
	// 根据条件查询冲帐明细记录
	public List<?> getRecvDetailList(List<?> list);
	
	// 根据条件查询流转明细记录
	public List<?> getLiuDetailList(List<?> list);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
