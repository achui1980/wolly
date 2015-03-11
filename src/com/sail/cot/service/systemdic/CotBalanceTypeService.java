/**
 * 
 */
package com.sail.cot.service.systemdic;

import java.util.List;
 

import com.sail.cot.domain.CotBalanceType;
import com.sail.cot.query.QueryInfo;


public interface CotBalanceTypeService {
	
    //根据费用结算编号取得对象
	public CotBalanceType getBalanceTypeById(Integer id);
	
	//保存费用结算方式
	public Boolean addBalanceTypes(List<?> balanceTypesList);
	
	//更新费用结算方式
	public Boolean modifyBalanceTypes(List<?> balanceTypesList);
	
	//删除费用结算方式
	public Boolean deleteBalanceTypes(List<?> balanceTypesList);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
}
