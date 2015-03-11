package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotInsureContract;
import com.sail.cot.query.QueryInfo;

public interface CotInsureContractService {

    public void addInsureContract(CotInsureContract cotInsureContract);  
	
	public void modifyInsureContract(CotInsureContract cotInsureContract);   
	
	public boolean deleteInsureContract(List<CotInsureContract> InsureContractList);  
	
	public CotInsureContract getInsureContractById(Integer id);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List getInsureContractList();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name);
	
	public Integer isExistName(String name);
}
