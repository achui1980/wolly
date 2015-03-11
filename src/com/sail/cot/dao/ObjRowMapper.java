/**
 * 
 */
package com.sail.cot.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 14, 2008 9:03:55 AM </p>
 * <p>Class Name: ObjRowMapper.java </p>
 * @author achui
 *
 */
public class ObjRowMapper implements RowMapper{
	
	private List resultList;
	/* (non-Javadoc)
	 * @see net.jcreate.e3.table.creator.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public ObjRowMapper(List<?> resultList)
	{
		this.resultList = resultList;
	}
	public Object mapRow(ResultSet rs, int rownum) throws SQLException {
		return resultList.get(rownum -1 );
	}
}
