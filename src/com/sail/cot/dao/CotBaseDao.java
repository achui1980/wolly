/**
 * 
 */
package com.sail.cot.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.jason.core.dao.AbstractQueryInfo;
import com.jason.core.dao.BaseDao;
import com.jason.core.exception.DAOException;
import com.sail.cot.query.QueryInfo;

/**
 * <p>Title: </p>
 * <p>Description: 操作数据库的通用类</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 9, 2008 4:27:30 PM </p>
 * <p>Class Name: ExtBaseDao.java </p>
 * @author achui
 *
 */
public interface CotBaseDao extends BaseDao{
	
	List getRecords(String objName);
	
	void deleteRecordByIds(List ids, String objName) throws DAOException;
	
	void deleteRecordById(Integer id, String objName) throws DAOException;
	
	void saveRecords(List records) throws DAOException;
	
	void updateRecords(List records);
	
	void saveOrUpdateRecords(List records);
	
	List findRecords(final AbstractQueryInfo queryInfo) throws DAOException;
	
	List findRecordsJDBC(final QueryInfo queryInfo) throws DAOException;
	
	int getRecordsCount(final AbstractQueryInfo queryInfo) throws DAOException;
	
	List queryForLists(String select,Object[] values);
	
	List queryForLists(String select,int start,int limit, Object[] values);
	
	public Object getObjectById(String clzz,Integer id);
	
	int getRecordsCountJDBC(final QueryInfo queryInfo);
	
	int getRecountCountByTable(String tableName);
	
	List findRecords(QueryInfo queryInfo,Class clzz) throws DAOException;
	
	public Connection getConnection();
	
	//调用存储过程
	public boolean callProc(String call,Object[] values) throws Exception;
	
	
	public String getJsonData(final QueryInfo queryInfo) throws DAOException;

	/**
	 * 描述：
	 * @param queryInfo 查询条件
	 * @param values 对应的映射关系，映射关系的关键字，需要和查询条件对应的参数对应
	 * 如：update CotEmps set empName=:empName 这是 map的可以为empName
	 * 如果 需要更新语句需要 in 关键字，如 update CotEmps set empName = :empName where id in(:ids);
	 * map的key为empName，ids，其中ids对应的数据类型必须是List
	 * @return 影响的行数
	 * 返回值：int
	 */
	int executeUpdate(QueryInfo queryInfo,Map values)throws DAOException;
	
}
