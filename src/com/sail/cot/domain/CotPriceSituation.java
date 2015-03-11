package com.sail.cot.domain;

/**
 * CotPriceSituation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPriceSituation implements java.io.Serializable {

	// Fields

	private Integer id;
	private String situationName;
	private String situationRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotPriceSituation() {
	}

	/** full constructor */
	public CotPriceSituation(String situationName, String situationRemark) {
		this.situationName = situationName;
		this.situationRemark = situationRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSituationName() {
		return this.situationName;
	}

	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}

	public String getSituationRemark() {
		return this.situationRemark;
	}

	public void setSituationRemark(String situationRemark) {
		this.situationRemark = situationRemark;
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