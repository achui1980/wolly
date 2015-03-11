/**
 * 
 */
package com.sail.cot.util;

/**
 * <p>Title: Ext+DWR+Spring</p>
 * <p>Description:Ext结合Dwr作数据分页需要用到的类</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 9, 2008 6:29:35 PM </p>
 * <p>Class Name: ListRange.java </p>
 * @author achui
 *
 */
public class ListRange {
	
	private Object[] data;
	private int totalSize;
	
	/**
	 * 
	 */
	public ListRange()
	{
		
	}
	public void setData(Object[] data) {
		this.data = data;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public Object[] getData() {
		return data;
	}
	public int getTotalSize() {
		return totalSize;
	}
	
	
}
