package com.sail.cot.domain;

/**
 * CotCustomerType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustomerType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String typeName;
	private String typeRemark;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotCustomerType() {
	}

	/** full constructor */
	public CotCustomerType(String typeName, String typeRemark) {
		this.typeName = typeName;
		this.typeRemark = typeRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeRemark() {
		return this.typeRemark;
	}

	public void setTypeRemark(String typeRemark) {
		this.typeRemark = typeRemark;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	
}