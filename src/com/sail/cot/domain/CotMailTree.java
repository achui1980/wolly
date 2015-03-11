package com.sail.cot.domain;


/**
 * CotFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailTree implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nodeName;
	private Integer parentId;
	private Integer empId;
	private String cls;
	private String flag;//节点类型
	private String updateFlag;//是否可删除,可重命名
	
	// Constructors

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	/** default constructor */
	public CotMailTree() {
	}

	/** minimal constructor */
	public CotMailTree(Integer id) {
		this.id = id;
	}
	
	/** full constructor */
	public CotMailTree(Integer id, String nodeName, Integer parentId, Integer empId) {
		this.id = id;
		this.nodeName = nodeName;
		this.parentId = parentId;
		this.empId = empId;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
	
}