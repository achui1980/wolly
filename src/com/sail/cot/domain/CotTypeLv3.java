package com.sail.cot.domain;

/**
 * CotTypeLv3 entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTypeLv3 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String typeName;
	private String typeRemark;
	private Integer typeIdLv2;
	private String typeNo;

	// Constructors

	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	/** default constructor */
	public CotTypeLv3() {
	}

	/** minimal constructor */
	public CotTypeLv3(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotTypeLv3(Integer id, String typeName, String typeRemark,
			Integer typeIdLv2) {
		this.id = id;
		this.typeName = typeName;
		this.typeRemark = typeRemark;
		this.typeIdLv2 = typeIdLv2;
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

	public Integer getTypeIdLv2() {
		return this.typeIdLv2;
	}

	public void setTypeIdLv2(Integer typeIdLv2) {
		this.typeIdLv2 = typeIdLv2;
	}

}