package com.sail.cot.domain;

/**
 * CotGivenType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotGivenType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String givenName;
	private String givenRemark;
	private String op;
	private String chk;

	// Constructors

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

	/** default constructor */
	public CotGivenType() {
	}

	/** full constructor */
	public CotGivenType(String givenName, String givenRemark) {
		this.givenName = givenName;
		this.givenRemark = givenRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGivenName() {
		return this.givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getGivenRemark() {
		return this.givenRemark;
	}

	public void setGivenRemark(String givenRemark) {
		this.givenRemark = givenRemark;
	}

}