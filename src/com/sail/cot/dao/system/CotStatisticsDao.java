/**
 * 
 */
package com.sail.cot.dao.system;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotStatPopedom;
import com.sail.cot.domain.CotStatistics;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Dec 18, 2008 8:36:16 PM </p>
 * <p>Class Name: CotStatisticsDao.java </p>
 * @author achui
 *
 */
public interface CotStatisticsDao extends CotBaseDao {
	//根据员工获取相应的查询统计列表
	List<CotStatistics> getStatisticsListByEmpId(String empId);
	
	//获取查询统计列表
	List<CotStatistics> getStatisticsList();
	
	//删除员工报表权限
	public void deleteStatPopedom(List empIds) throws DAOException;
	
	//添加员工报表权限
	public void addStatPopedom(List<CotStatPopedom> popedomList) throws DAOException;
	
	//根据员工ID删除权限
	public int deletePopedomByEmpId(String empId) throws  DAOException;
	
	//获取某个员工权限
	public List getPopedom(Integer empId) throws  DAOException;
}
