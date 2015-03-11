/**
 * 
 */
package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotShipPort;
import com.sail.cot.query.QueryInfo;


public interface CotShipPortService {
	
	List getShipPortList();
	
	//根据起运港编号取得对象
	public CotShipPort getShipPortById(Integer id);
	
	//保存起运港
	public Boolean addShipPorts(List<?> shipPortsList);
	
	//更新起运港
	public Boolean modifyShipPorts(List<?> shipPortsList);
	
	//删除起运港
	public Boolean deleteShipPorts(List<?> shipPortsList);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public boolean isExistShipPortName(String shipPortName);
}
