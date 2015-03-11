/**
 * 
 */
package com.sail.cot.service.statistics.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.system.CotStatisticsDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotStatPopedom;
import com.sail.cot.domain.CotStatistics;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.statistics.CotStatisticsService;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Dec 18, 2008 8:27:13 PM </p>
 * <p>Class Name: CotStatisticsServiceImpl.java </p>
 * @author achui
 *
 */
public class CotStatisticsServiceImpl implements CotStatisticsService{

	private CotStatisticsDao baseDao;
	/* (non-Javadoc)
	 * @see com.sail.cot.service.statistics.CotStatisticsService#getStatisticsListByEmpId(java.lang.String)
	 */
	public List<CotStatistics> getStatisticsListByEmpId(String empId) {
		//empId传入null则代表管理员（admin），直接获取所有列表
		List res = null;
		if(empId == null)
		{
			res = this.getBaseDao().getStatisticsList();
		}
		else
		{
			res = this.getBaseDao().getStatisticsListByEmpId(empId);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.statistics.CotStatisticsService#getStatisticsList()
	 */
	public List<CotStatistics> getStatisticsList() {
		// TODO Auto-generated method stub
		return this.getBaseDao().getStatisticsList();
	}

	public CotStatisticsDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(CotStatisticsDao baseDao) {
		this.baseDao = baseDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.statistics.CotStatisticsService#addStatPopedom(java.util.List)
	 */
	public int addPopedom(List<CotStatPopedom> popedomList)  {
		try {
			this.getBaseDao().addStatPopedom(popedomList);
			return 0;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.statistics.CotStatisticsService#deletePopedomByEmpId(java.lang.String)
	 */
	public int deletePopedomByEmpId(String empId)  {
		int res = 0;
		try {
			res =  this.getBaseDao().deletePopedomByEmpId(empId);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.statistics.CotStatisticsService#deleteStatPopedom(java.util.List)
	 */
	public void deleteStatPopedom(List empIds)  {
		try {
			this.getBaseDao().deleteStatPopedom(empIds);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.statistics.CotStatisticsService#getPopedom(java.lang.Integer)
	 */
	public List getPopedom(Integer empId)  {
		List res = null;
		try {
			res = this.getBaseDao().getPopedom(empId);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public CotEmps getEmpsById(Integer id) {
		Object object = this.getBaseDao().getById(CotEmps.class, id);
		if(object!=null){
			CotEmps cotEmps = (CotEmps)object;
			cotEmps.setCustomers(null);
			return cotEmps;
		}else{
			return null;
		}
	}

	//保存统计报表
	public Integer saveOrUpdateRptFile(CotStatistics cotStatistics){
		List<CotStatistics> records =new ArrayList<CotStatistics>();
		records.add(cotStatistics);
		this.getBaseDao().saveOrUpdateRecords(records);
		return cotStatistics.getId();
	}
	//通过节点名称获得相应对象
	public CotStatistics getCotStatisticsByName(String statName,String filePath){
		String strSql = " from CotStatistics obj where obj.statName='"+statName+"'"+" and obj.statFile='"+filePath+"'";
		List<CotStatistics> res = this.getBaseDao().find(strSql);
		CotStatistics cotStatistics;
		if(res.size()>0){
			cotStatistics =res.get(0);
			return cotStatistics;
		}
		return null;
	}
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getBaseDao().getJsonData(queryInfo);
	}
	//获得对象
	public CotStatistics getCotStatisticsById(Integer Id){		 
		return (CotStatistics)this.getBaseDao().getById(CotStatistics.class, Id);
	}
	//删除
	public Integer delStatsById(List<Integer> list){
		try {
			int oldIds =list.size();
			List<Integer> ids=new ArrayList<Integer>();
			for(int i=0;i<list.size();i++){
				int id =list.get(i);
				String sql =" from CotStatPopedom obj where obj.statId="+id;
				List<?> res = this.getBaseDao().find(sql);
				if(res.size()>0) continue;
				ids.add(id);
			}
			int delIds =ids.size();
			if(ids.size()>0){
				this.getBaseDao().deleteRecordByIds(ids,"CotStatistics");
			}
			int reIds =oldIds -delIds;
			if(reIds >0){
				return reIds;
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	//验证报表名称是否存在
	public Integer isExitStatName(String statName,Integer statParent,String id){
		String hql = "select obj.id from CotStatistics obj where obj.statName='"+statName+"' and obj.statParent="+statParent;	
		List<?> res = this.getBaseDao().find(hql);
		if (res.size() == 0) {
			return null;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				return null;
			} else {
				return 1;
			}
		}
		return 2;
	}

}
