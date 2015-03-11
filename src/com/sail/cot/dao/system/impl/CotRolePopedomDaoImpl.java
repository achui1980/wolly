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
import com.sail.cot.dao.system.CotRolePopedomDao;
import com.sail.cot.domain.CotRolepopedom;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 13, 2008 3:02:28 PM </p>
 * <p>Class Name: CotPopedomDaoImpl.java </p>
 * @author achui
 *
 */
public class CotRolePopedomDaoImpl extends CotBaseDaoImpl implements CotRolePopedomDao{

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#addPopedom()
	 */
	
	public int addRolePopedom(List popedomList)  throws  DAOException {
	
		super.saveRecords(popedomList);
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#deletePopedom(java.lang.Integer)
	 */
	public int deletePopedom(List roleIds) throws  DAOException {

		super.deleteRecordByIds(roleIds, "CotRolepopedom");
	
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#getPopedomByEmpId(java.lang.Integer)
	 */
	public List getPopedomByRoleId(Integer roleId) throws  DAOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("select * from cot_rolepopedom where 1=1 ");
		sb.append(" and role_id=");
		sb.append(roleId);
		sb.append(" order by id desc");
		List res = super.getSession().createSQLQuery(sb.toString()).addEntity(CotRolepopedom.class).list();
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#deletePopedomByEmpId(java.lang.String)
	 */
	public int deletePopedomByRoleId(int roleId) throws DAOException {
		String strSql = "delete from cot_rolepopedom  where role_id="+roleId;
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
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return res;
	}
}
