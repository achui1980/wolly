/**
 * 
 */
package com.sail.cot.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;

import com.jason.core.dao.AbstractQueryInfo;
import com.sail.cot.query.QueryInfo;

/**
 * <p>
 * Title: 工艺品管理系统
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
 * Create Time: Oct 23, 2008 5:34:47 PM
 * </p>
 * <p>
 * Class Name: SpringJdbcPagenation.java
 * </p>
 * 
 * @author achui
 * 
 */
public class SpringJdbcPagenation extends JdbcTemplate {

	private Page page;

	public List queryForPage(final String sql, final RowMapper rowMapper,
			Connection conn) throws DataAccessException {
		List res = new ArrayList();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int currentPage = this.getPage().getCurrentPage();
			int startNo = (currentPage - 1) * this.getPage().getPageSize();
			pstm.setMaxRows(startNo + this.getPage().getPageSize());// 最大查询到第几条记录
			rs = pstm.executeQuery();
			res = ResultSetForList(rs, rowMapper);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
				if(conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * 分页之后，填充到以对象形式存储的List 中
	 * 
	 * @param rs
	 *            记录集
	 * @param rm
	 *            对象转换填充器
	 * @return List
	 * @throws SQLException
	 */
	private List ResultSetForList(ResultSet rs, RowMapper rm) {
		List listArray = new ArrayList();

		try {
			rs.first();
			// 游标移动到要输出的第一条记录
			int startNo = (this.getPage().getCurrentPage() - 1) * this.getPage().getPageSize();
			rs.relative(startNo - 1);
			while (rs.next()) {
				listArray.add(rm.mapRow(rs, rs.getRow()));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("SQLException: " + e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("SQLException: " + e.getMessage());
			}
		}
		return listArray;


	}

	public Page getPage() {
		return this.page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
