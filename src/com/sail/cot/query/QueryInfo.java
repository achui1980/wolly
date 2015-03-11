/**
 * 
 */
package com.sail.cot.query;


import com.jason.core.dao.AbstractQueryInfo;

/**
 * <p>Title: Ext+DWR+Spring</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 9, 2008 11:13:37 PM </p>
 * <p>Class Name: QueryInfo.java </p>
 * @author achui
 *
 */
public class QueryInfo  extends AbstractQueryInfo {

	
	private String queryObjType ;
	
	//json-lib过滤属性用
	private String[] excludes ;
	
	private int count;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	

	/* (non-Javadoc)
	 * @see com.jason.core.dao.AbstractQueryInfo#populate()
	 */
	public void populate() {
		// TODO Auto-generated method stub
		
	}
	public String getQueryObjType() {
		return queryObjType;
	}
	public void setQueryObjType(String queryObjType) {
		this.queryObjType = queryObjType;
	}
	public String[] getExcludes() {
		return excludes;
	}
	public void setExcludes(String[] excludes) {
		this.excludes = excludes;
	}

}
