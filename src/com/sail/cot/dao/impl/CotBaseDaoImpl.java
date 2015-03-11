/**
 * 
 */
package com.sail.cot.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

import com.jason.core.dao.AbstractDAO;
import com.jason.core.dao.AbstractQueryInfo;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.GridServerHandler;

/**
 * <p>
 * Title: Ext+DWR+Spring
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Jul 9, 2008 4:37:52 PM
 * </p>
 * <p>
 * Class Name: ExtBaseDao.java
 * </p>
 * 
 * @author achui
 * 
 */
public class CotBaseDaoImpl extends AbstractDAO implements CotBaseDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#deleteRecordById(java.lang.Integer,
	 *      java.lang.String)
	 */
	public void deleteRecordById(Integer id, String objName)
			throws DAOException {
		List ids = new ArrayList();
		ids.add(id);
		this.deleteRecordByIds(ids, objName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#deleteRecordByIds(java.util.List,
	 *      java.lang.String)
	 */
	public void deleteRecordByIds(List ids, String objName) throws DAOException {
		try {
			super.getHibernateTemplate().execute(
					new BatchDeleteCallback(ids, objName));
		} catch (Exception e) {
			System.out.println(">>>>>>>>>>" + e.getMessage());
			// e.printStackTrace();
			throw new DAOException("error: delete fail");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#getRecords(java.lang.String)
	 */
	public List getRecords(String objName) {
		return super.getHibernateTemplate().find("from " + objName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#saveOrUpdateRecords(java.util.List)
	 */
	public void saveOrUpdateRecords(List records) {
		super.getHibernateTemplate().execute(new BatchSoUCallback(records));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#saveRecords(java.util.List)
	 */
	public void saveRecords(List records) throws DAOException {
		try {
			super.getHibernateTemplate().execute(
					new BatchInsertCallback(records));
		} catch (Exception he) {
			he.printStackTrace();
			throw new DAOException("error:save fail");

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#updateRecords(java.util.List)
	 */
	public void updateRecords(List records) {
		super.getHibernateTemplate().execute(new BatchUpdateCallback(records));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#findRecords(com.jason.core.dao.AbstractQueryInfo)
	 */
	public List findRecords(AbstractQueryInfo queryInfo) throws DAOException {
		queryInfo.populate();
		Query query = null;
		List records = null;
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(queryInfo.getSelectString()).append(
				queryInfo.getQueryString());
		if (StringUtils.isNotBlank(queryInfo.getOrderString())) {
			queryBuffer.append(queryInfo.getOrderString());
		} else {
			// 默认按id排序
			queryBuffer.append(" order by id desc");
		}
		try {
			System.out.println("-------" + queryBuffer.toString());
			String sql = queryBuffer.toString();
			String like = StringUtils.substringBetween(sql, "'%", "%'");
			sql = sql.replace("'%" + like + "%'", "?");
			System.out.println("-------" + sql);
			Session session = getHibernateTemplate().getSessionFactory()
					.openSession();
			query = session.createQuery(sql);
			if (like != null && !"".equals(like))
				query.setString(0, "%" + like + "%");
			// session.createSQLQuery(queryBuffer.toString());
			query.setProperties(queryInfo);
			query.setFirstResult(queryInfo.getStartIndex());
			query.setMaxResults(queryInfo.getCountOnEachPage());
			// if not use level 2 cache it will invoke query.list()
			// fix me:it will be invoked at here? or use global
			// setCacheQuery(..)?
			query.setCacheable(super.isCacheQuery());
			records = query.list();
			session.flush();
			session.close();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new DAOException("find fail");
		}
		return records;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#getRecordsCount(com.jason.core.dao.AbstractQueryInfo)
	 */
	public int getRecordsCount(AbstractQueryInfo queryInfo) throws DAOException {
		int count = 0;
		String countQuery = queryInfo.getCountQuery();
		if (countQuery == null) {
			StringBuffer select = new StringBuffer();
			select.append(StringUtils.replace(queryInfo.getSelectString(),
					"select", "select count("));
			select.append(')');
			select.append(queryInfo.getQueryString());
			countQuery = select.toString();
		}
		System.out.println("getCountQuery" + queryInfo.getCountQuery());
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		Query query = session.createQuery(queryInfo.getCountQuery());
		query.setProperties(queryInfo);
		query.setCacheable(super.isCacheQuery());
		// count = query.list().size();
		count = ((Integer) query.list().get(0)).intValue();
		session.flush();
		session.close();
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptn.ext.dao.ExtBaseDao#queryForLists(java.lang.String,
	 *      java.lang.Object[])
	 */
	public List queryForLists(final String select, final Object[] values) {
		List res = null;
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.openSession();
			Query query = session.createQuery(select);
			if (values != null) {
				for (int i = 0; i < values.length; i++)
					query.setParameter(i, values[i]);
			}
			res = query.list();
			session.flush();
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	public List queryForLists(final String select, int start, int limit,
			final Object[] values) {
		List res = null;
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.openSession();
			Query query = session.createQuery(select);
			if (values != null) {
				for (int i = 0; i < values.length; i++)
					query.setParameter(i, values[i]);
			}
			query.setFirstResult(start);
			query.setMaxResults(limit);
			res = query.list();
			session.flush();
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	// getById可能会从缓存中找对象,而不去数据库查询,达不到从数据库获得数据的目的
	public Object getObjectById(String clzz, Integer id) {
		String hql = "from " + clzz + " obj where obj.id=?";
		Object[] parm = new Object[1];
		parm[0] = id;
		List oldList = this.queryForLists(hql, parm);
		if (oldList.size() == 1) {
			return oldList.get(0);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.CotBaseDao#findRecordsJDBC(com.jason.core.dao.AbstractQueryInfo)
	 */
	public List findRecordsJDBC(QueryInfo queryInfo) throws DAOException {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(queryInfo.getSelectString()).append(
				queryInfo.getQueryString()).append(queryInfo.getOrderString());
		// 设置每页翻页记录数
		Page page = new Page();
		SpringJdbcPagenation jdbcpage = new SpringJdbcPagenation();
		int currentPage = 0;
		currentPage = (queryInfo.getStartIndex() + queryInfo
				.getCountOnEachPage())
				/ queryInfo.getCountOnEachPage();

		page.setCurrentPage(currentPage);
		// page.setPageCount(queryInfo.getStartIndex());
		page.setPageSize(queryInfo.getCountOnEachPage());
		jdbcpage.setPage(page);
		JdbcRowMapper rowMapper = new JdbcRowMapper();
		rowMapper.setType(queryInfo.getQueryObjType());
		Connection conn = super.getSession().connection();
		return jdbcpage.queryForPage(queryBuffer.toString(), rowMapper, conn);

	}

	public int getRecordsCountJDBC(QueryInfo queryInfo) {
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(queryInfo.getCountQuery());
			rs = pstm.executeQuery();
			while (rs.next()) {
				res = rs.getInt(1);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.CotBaseDao#getRecountCountByTable(java.lang.String)
	 */
	public int getRecountCountByTable(String tableName) {
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = super.getSession().connection();
			String strSql = "select  SQL_CALC_FOUND_ROWS id from " + tableName
					+ " where 1=1 limit 1";
			String strSqlCount = "select found_rows() as rowcount";
			long ls = System.currentTimeMillis();
			pstm = conn.prepareStatement(strSql);
			pstm.executeQuery();
			pstm = conn.prepareStatement(strSqlCount);
			rs = pstm.executeQuery();
			long le = System.currentTimeMillis();
			System.out.println("-----------------执行时间：" + (le - ls) + " ms");
			while (rs.next()) {
				res = rs.getInt("rowcount");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.CotBaseDao#findRecords(com.sail.cot.query.QueryInfo,
	 *      java.lang.Class)
	 */
	public List findRecords(QueryInfo queryInfo, Class clzz)
			throws DAOException {
		queryInfo.populate();
		SQLQuery query = null;
		List records = null;
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(queryInfo.getSelectString()).append(
				queryInfo.getQueryString());
		if (StringUtils.isNotBlank(queryInfo.getOrderString())) {
			queryBuffer.append(queryInfo.getOrderString());
		} else {
			// 默认按id排序
			// queryBuffer.append(" order by id desc");
		}
		try {
			System.out.println("-------" + queryBuffer.toString());
			Session session = getHibernateTemplate().getSessionFactory()
					.openSession();
			query = session.createSQLQuery(queryBuffer.toString());
			query.setProperties(queryInfo);
			query.setFirstResult(queryInfo.getStartIndex());
			query.setMaxResults(queryInfo.getCountOnEachPage());
			// if not use level 2 cache it will invoke query.list()
			// fix me:it will be invoked at here? or use global
			// setCacheQuery(..)?
			query.setCacheable(super.isCacheQuery());
			records = query.addEntity(clzz).list();
			session.flush();
			session.close();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new DAOException("find fail");
		}
		return records;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.CotBaseDao#getConnection()
	 */
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return super.getSession().connection();
	}

	// 调用存储过程
	public boolean callProc(String call, Object[] values) throws Exception {
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		Connection conn = session.connection();
		CallableStatement proc = conn.prepareCall(call); // 调用存储过程
		if (values != null) {
			for (int i = 0; i < values.length; i++)
				proc.setObject(i + 1, values[i]); // 给输入参数传值
		}
		// proc.registerOutParameter(1, Types.VARCHAR); //声明输出参数是什么类型的
		proc.execute(); // 执行
		// String flag=proc.getString(1);
		conn.close();
		session.flush();
		session.close();
		return true;

	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		GridServerHandler gd = null;
		if (queryInfo.getExcludes() != null)
			gd = new GridServerHandler(queryInfo.getExcludes());
		else
			gd = new GridServerHandler();
		List res = this.findRecords(queryInfo);
		int count = this.getRecordsCount(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
	}

	public int executeUpdate(QueryInfo queryInfo, Map values)
			throws DAOException {
		try {
			String select = queryInfo.getSelectString()
					+ (queryInfo.getQueryString() == null ? "" : queryInfo
							.getQueryString());
			Integer result = (Integer) super.getHibernateTemplate().execute(
					new BatchUpdatePropertyCallBack(select, values));
			return result;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new DAOException("批量属性更新异常:" + e.getMessage());
		}
	}
}
