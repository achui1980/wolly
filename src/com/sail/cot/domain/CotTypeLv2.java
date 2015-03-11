package com.sail.cot.domain;

/**
 * CotTypeLv2 entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTypeLv2 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String typeName;
	private String typeRemark;
	private Integer typeIdLv1;
	private String op;
	private String typeNo;

	// Constructors

	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	/** default constructor */
	public CotTypeLv2() {
	}

	/** minimal constructor */
	public CotTypeLv2(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotTypeLv2(Integer id, String typeName, String typeRemark) {
		this.id = id;
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

	public Integer getTypeIdLv1() {
		return typeIdLv1;
	}

	public void setTypeIdLv1(Integer typeIdLv1) {
		this.typeIdLv1 = typeIdLv1;
	}

}