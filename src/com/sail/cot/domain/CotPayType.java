package com.sail.cot.domain;

/**
 * CotPayType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPayType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String payName;
	private String payRemark;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotPayType() {
	}

	/** full constructor */
	public CotPayType(String payName, String payRemark) {
		this.payName = payName;
		this.payRemark = payRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayName() {
		return this.payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayRemark() {
		return this.payRemark;
	}

	public void setPayRemark(String payRemark) {
		this.payRemark = payRemark;
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