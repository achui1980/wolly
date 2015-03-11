package com.sail.cot.domain;

/**
 * CotCustomerLv entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustomerLv implements java.io.Serializable {

	// Fields

	private Integer id;
	private String lvName;
	private String lvRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotCustomerLv() {
	}

	/** full constructor */
	public CotCustomerLv(String lvName, String lvRemark) {
		this.lvName = lvName;
		this.lvRemark = lvRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLvName() {
		return this.lvName;
	}

	public void setLvName(String lvName) {
		this.lvName = lvName;
	}

	public String getLvRemark() {
		return this.lvRemark;
	}

	public void setLvRemark(String lvRemark) {
		this.lvRemark = lvRemark;
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