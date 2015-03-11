/**
 * 
 */
package com.sail.cot.dao.system.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.system.CotPopedomDao;
import com.sail.cot.domain.CotPopedom;
import com.sail.cot.domain.vo.CotPopedomMenu;
import com.sail.cot.domain.vo.CotPopedomOP;


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
public class CotPopedomDaoImpl extends CotBaseDaoImpl implements CotPopedomDao{

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#addPopedom()
	 */
	
	public int addPopedom(List popedomList)  throws  DAOException {
	
		super.saveRecords(popedomList);
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#deletePopedom(java.lang.Integer)
	 */
	public int deletePopedom(List empIds) throws  DAOException {

		super.deleteRecordByIds(empIds, "CotPopedom");
	
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#getPopedomByEmpId(java.lang.Integer)
	 */
	public List getPopedomByEmpId(Integer empId) throws  DAOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("select * from cot_popedom where 1=1 ");
		sb.append(" and emps_id=");
		sb.append(empId);
		sb.append(" order by id desc");
		List res = super.getSession().createSQLQuery(sb.toString()).addEntity(CotPopedom.class).list();
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#deletePopedomByEmpId(java.lang.String)
	 */
	public int deletePopedomByEmpId(int empId) throws DAOException {
		String strSql = "delete from cot_popedom  where emps_id="+empId;
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
	 * @see com.sail.cot.dao.system.CotPopedomDao#getPopedomByUrl(java.lang.String, java.lang.Integer)
	 */
	public Map getPopedomByUrl(String Url, Integer empId) throws DAOException {
		String strSql = "select t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MOUDLE_TYPE,p.* "
						+"from cot_module t, cot_popedom p where 1=1 "
						+"and   p.module_id = t.id "
						+"and p.emps_Id = "+empId
						+" and t.MODULE_VALIDURL = '"+Url+"' "
						+"order by p.module_id desc ";
		logger.info(strSql);
		Map map = new HashMap();
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		conn = super.getSession().connection();
		try {
			pstm = conn.prepareStatement(strSql);
			rs = pstm.executeQuery();
			while(rs.next())
			{
				map.put(rs.getString("MOUDLE_TYPE"), true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs != null )
					rs.close();
				if(pstm != null)
					pstm.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#getPopedomByEmp(java.lang.Integer)
	 */
	public Map getPopedomByEmp(Integer empId) throws DAOException {
		String strSql_OP = "select t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MOUDLE_TYPE,p.* "
			+" from cot_module t, cot_popedom p where 1=1 "
			+" and   p.module_id = t.id "
			+" and p.emps_Id = "+empId
			+" and t.MODULE_LV =3"
			+" order by p.module_id desc ";
		
		String strSql_Menu = "select t.MODULE_NAME,t.MODULE_URL,t.MODULE_VALIDURL,t.MOUDLE_TYPE,p.* "
			+" from cot_module t, cot_popedom p where 1=1 "
			+" and   p.module_id = t.id "
			+" and p.emps_Id = "+empId
			+" and t.MOUDLE_TYPE='MENU'"
			+" order by p.module_id desc ";
		
		Connection conn_op = null;
		Connection conn_menu = null;
		PreparedStatement pstm_op = null;
		PreparedStatement pstm_menu = null;
		ResultSet rs_op = null;
		ResultSet rs_menu = null;
		
		conn_op = super.getSession().connection();
		conn_menu = super.getSession().connection();
		List menuList = new ArrayList();
		List opList = new ArrayList();
		Map popedomMap = new HashMap();
		
		try {
			pstm_op = conn_op.prepareStatement(strSql_OP);
			pstm_menu = conn_menu.prepareStatement(strSql_Menu);
			rs_op = pstm_op.executeQuery();
			rs_menu = pstm_menu.executeQuery();
			while(rs_menu.next())
			{
				CotPopedomMenu menu = new CotPopedomMenu();
				menu.setMenuname(rs_menu.getString("MODULE_VALIDURL"));	
				menuList.add(menu);
			}
			while(rs_op.next())
			{
				CotPopedomOP op = new CotPopedomOP();
				op.setOp(rs_op.getString("MOUDLE_TYPE"));
				op.setMenuname(rs_op.getString("MODULE_VALIDURL"));
				opList.add(op);
			}
			for(int i=0; i<menuList.size(); i++)
			{
				CotPopedomMenu menu = (CotPopedomMenu)menuList.get(i);
				String menuName = menu.getMenuname();
				System.out.println("---"+menuName);
				Map opMap = new HashMap();
				for(int j=0; j<opList.size(); j++)
				{
					CotPopedomOP op = (CotPopedomOP)opList.get(j);
					String opName = op.getMenuname();
					
					if(menuName.equals(opName))
					{
						opMap.put(op.getOp(),true);
					}
				}
				if(opMap.size() != 0)
					popedomMap.put(menu.getMenuname(), opMap);
				System.out.println("-----------------------"+popedomMap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs_op != null )
					rs_op.close();
				if(pstm_op != null)
					pstm_op.close();
				if(rs_menu != null )
					rs_menu.close();
				if(pstm_menu != null)
					pstm_menu.close();
				if(conn_op != null)
					conn_op.close();
				if(conn_menu != null)
					conn_menu.close();
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return popedomMap;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#getPopedom(java.lang.Integer)
	 */
	public List getPopedom(Integer empId) throws DAOException {
		// TODO Auto-generated method stub
		return super.getRecords("cotpopedom");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotPopedomDao#deletePopedomByEmpsList(java.util.List)
	 */
	public void deletePopedomByEmpsList(List empIdList) {
		String strSql = "delete from cot_popedom  where emps_id= ?";
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try
		{
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			for(int i=0; i<empIdList.size(); i++)
			{
				Integer empId = new Integer(empIdList.get(i).toString());
				pstm.setInt(1, empId.intValue());
				pstm.addBatch();
			}
			pstm.executeBatch();
			
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
		
		
	}
}
