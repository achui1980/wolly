package com.sail.cot.domain;

/**
 * CotTypeLv1 entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTypeLv1 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String typeName;
	private String typeEnName;
	private String typeRemark;
	private String op;
	private String chk;
	
	private String faName;
	private String baoName;
	private String typeNo;
	// Constructors

	/** default constructor */
	public CotTypeLv1() {
	}

	/** minimal constructor */
	public CotTypeLv1(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotTypeLv1(Integer id, String typeName, String typeRemark) {
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

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	public String getTypeEnName() {
		return typeEnName;
	}

	public void setTypeEnName(String typeEnName) {
		this.typeEnName = typeEnName;
	}

	public String getFaName() {
		return faName;
	}

	public void setFaName(String faName) {
		this.faName = faName;
	}

	public String getBaoName() {
		return baoName;
	}

	public void setBaoName(String baoName) {
		this.baoName = baoName;
	}

	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

}