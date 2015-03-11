package com.sail.cot.service.system;

import java.util.List;
import java.util.Map;
 

import com.jason.core.exception.DAOException;
import com.jason.core.exception.ServiceException;
import com.sail.cot.domain.CotDept;
import com.sail.cot.query.QueryInfo;

public interface CotDeptService {

	
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	CotDept getDeptById(Integer id);
	
	public boolean saveCotDept(CotDept cotDept)throws ServiceException;
	
	public void saveCotDeptByList(List<CotDept> cotDeptList)throws ServiceException;
	
	public boolean updateCotDept(CotDept cotDept)throws ServiceException;
	
	public void updateCotDeptByList(List<CotDept> cotDeptList)throws ServiceException;
	
	public void deleteCotDept(CotDept cotDept)throws ServiceException;
	
	public int deleteCotDeptByList(List<CotDept> cotDeptList)throws ServiceException;
	
	Map<?, ?> getMap();
	
	public boolean findEmpsRecordsCount(String deptId);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public boolean isNotExistDeptName(String deptName);
}
