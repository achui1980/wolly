/**
 * 
 */
package com.sail.cot.dao.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Oct 23, 2008 6:00:28 PM </p>
 * <p>Class Name: Page.java </p>
 * @author achui
 *
 */
public class Page {

	private int pageCount=1;

	private int currentPage=1;

	private int pageSize=10;

	private int maxPage=1;

	private List resultSet=new ArrayList();

	public int getCurrentPage() {
	checkPage();
	return currentPage;
	}

	private void initMaxPage()
	{
	this.maxPage = (this.pageCount + this.pageSize - 1) / this.pageSize;
	}
	public void setCurrentPage(int currentPage) {

	this.currentPage = currentPage;
	}

	public int getMaxPage() {

	return this.maxPage;
	}

	public void setMaxPage(int maxPage) {
	this.maxPage = maxPage;
	}

	public int getPageCount() {
	return pageCount;
	}

	public void setPageCount(int pageCount) {

	this.pageCount = pageCount;
	initMaxPage();
	}

	public int getPageSize() {
	return pageSize;
	}

	public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
	}

	public List getResultSet() {
	return resultSet;
	}

	public void setResultSet(List resultSet) {
	this.resultSet = resultSet;
	}

	private void checkPage()
	{
	if(this.currentPage<1)
	this.currentPage=1;
//	if(this.currentPage>this.maxPage)
//	this.currentPage=this.maxPage;
	}

	}
