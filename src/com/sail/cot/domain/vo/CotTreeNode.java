/**
 * 
 */
package com.sail.cot.domain.vo;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:构造一个树节点类型</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 28, 2008 11:12:15 PM </p>
 * <p>Class Name: CotTreeNode.java </p>
 * @author achui
 *
 */

public class CotTreeNode {
	
	private String nodeid;
	private String parentnodeid;
	private String nodename;
	private String nodetype;
	private String nodeaction;
	public String getNodeaction() {
		return nodeaction;
	}
	public void setNodeaction(String nodeaction) {
		this.nodeaction = nodeaction;
	}
	public String getNodetype() {
		return nodetype;
	}
	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	public String getParentnodeid() {
		return parentnodeid;
	}
	public void setParentnodeid(String parentnodeid) {
		this.parentnodeid = parentnodeid;
	}
	public String getNodename() {
		return nodename;
	}
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
}
