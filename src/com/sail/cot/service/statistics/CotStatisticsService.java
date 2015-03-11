/**
 * 
 */
package com.sail.cot.service.statistics;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotStatPopedom;
import com.sail.cot.domain.CotStatistics;
import com.sail.cot.query.QueryInfo;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Dec 18, 2008 8:23:08 PM </p>
 * <p>Class Name: CotStatisticsService.java </p>
 * @author achui
 *
 */
public interface CotStatisticsService {
	
	//根据员工获取相应的查询统计列表
	List<CotStatistics> getStatisticsListByEmpId(String empId);
	
	//获取查询统计列表
	List<CotStatistics> getStatisticsList();
	
	//删除员工报表权限
	public void deleteStatPopedom(List empIds) ;
	
	//添加员工报表权限
	public int addPopedom(List<CotStatPopedom> popedomList) ;
	
	//根据员工ID删除权限
	public int deletePopedomByEmpId(String empId) ;
	
	//获取某个员工权限
	public List getPopedom(Integer empId) ;
	
	public CotEmps getEmpsById(Integer id);
	//保存统计报表
	public Integer saveOrUpdateRptFile(CotStatistics cotStatistics);
	//通过节点名称获得相应对象
	public CotStatistics getCotStatisticsByName(String statName,String filePath);
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	//获得对象
	public CotStatistics getCotStatisticsById(Integer Id);
	//删除
	public Integer delStatsById(List<Integer> list);
	//验证报表名称是否存在
	public Integer isExitStatName(String statName,Integer statParent,String id);
}
