/**
 * 
 */
package com.sail.cot.dao.system.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.system.CotStatisticsDao;
import com.sail.cot.domain.CotModule;
import com.sail.cot.domain.CotPopedom;
import com.sail.cot.domain.CotStatPopedom;
import com.sail.cot.domain.CotStatistics;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Dec 18, 2008 8:44:10 PM </p>
 * <p>Class Name: CotStatisticsDaoImpl.java </p>
 * @author achui
 *
 */
public class CotStatisticsDaoImpl extends CotBaseDaoImpl implements CotStatisticsDao {

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotStatisticsDao#getStatisticsList()
	 */
	public List<CotStatistics> getStatisticsList() {
		// TODO Auto-generated method stub
		return super.find(" from CotStatistics c where 1=1");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotStatisticsDao#getStatisticsListByEmpId(java.lang.String)
	 */
	public List<CotStatistics> getStatisticsListByEmpId(String empId) {
		String strSql = "SELECT t.*  " 
			+"FROM cot_statistics t,cot_stat_popedom p where 1=1"
			+" and t.id = p.stat_id"
			+" and p.emp_id = "+empId
			+ " order by t.stat_order asc";
		List res = super.getSession().createSQLQuery(strSql).addEntity(CotStatistics.class).list();
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotStatisticsDao#addStatPopedom(java.util.List)
	 */
	public void addStatPopedom(List popedomList) throws DAOException {
		super.saveRecords(popedomList);
		
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotStatisticsDao#deleteStatPopedom(java.util.List)
	 */
	public void deleteStatPopedom(List empIds) throws DAOException {
		super.deleteRecordByIds(empIds, "CotStatPopedom");
		
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotStatisticsDao#deletePopedomByEmpId(java.lang.String)
	 */
	public int deletePopedomByEmpId(String empId) throws DAOException {
		String strSql = "delete from cot_stat_popedom  where emp_id="+empId;
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try
		{
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			res = pstm.executeUpdate();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if(pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return res;

	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotStatisticsDao#getPopedom(java.lang.Integer)
	 */
	public List getPopedom(Integer empId) throws DAOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("select * from cot_stat_popedom where 1=1 ");
		sb.append(" and emp_id=");
		sb.append(empId);
		sb.append(" order by id desc");
		List res = super.getSession().createSQLQuery(sb.toString()).addEntity(CotStatPopedom.class).list();
		return res;
	}
}
