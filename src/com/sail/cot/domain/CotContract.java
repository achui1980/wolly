package com.sail.cot.domain;


/**
 * CotContract entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotContract implements java.io.Serializable {

	// Fields

	private Integer id;
	private String contractContent;
	private String contractRemark;
	private java.sql.Timestamp addTime;
	private String addPerson;
	private Integer contractType;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotContract() {
	}

	/** full constructor */
	public CotContract(String contractContent, String contractRemark,
			java.sql.Timestamp addTime, String addPerson) {
		this.contractContent = contractContent;
		this.contractRemark = contractRemark;
		this.addTime = addTime;
		this.addPerson = addPerson;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContractContent() {
		return this.contractContent;
	}

	public void setContractContent(String contractContent) {
		this.contractContent = contractContent;
	}

	public String getContractRemark() {
		return this.contractRemark;
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

	public void setContractRemark(String contractRemark) {
		this.contractRemark = contractRemark;
	}

	public java.sql.Timestamp getAddTime() {
		return this.addTime;
	}

	public void setAddTime(java.sql.Timestamp addTime) {
		this.addTime = addTime;
	}

	public String getAddPerson() {
		return this.addPerson;
	}

	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}

	public Integer getContractType() {
		return contractType;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

}