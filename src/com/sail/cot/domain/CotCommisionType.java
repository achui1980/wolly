package com.sail.cot.domain;

/**
 * CotCommisionType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCommisionType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String commisionName;
	private String commisionRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotCommisionType() {
	}

	/** full constructor */
	public CotCommisionType(String commisionName, String commisionRemark) {
		this.commisionName = commisionName;
		this.commisionRemark = commisionRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommisionName() {
		return this.commisionName;
	}

	public void setCommisionName(String commisionName) {
		this.commisionName = commisionName;
	}

	public String getCommisionRemark() {
		return this.commisionRemark;
	}

	public void setCommisionRemark(String commisionRemark) {
		this.commisionRemark = commisionRemark;
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