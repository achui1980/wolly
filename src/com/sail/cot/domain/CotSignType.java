package com.sail.cot.domain;

/**
 * CotSignType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSignType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String signName;
	private String signRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotSignType() {
	}

	/** full constructor */
	public CotSignType(String signName, String signRemark) {
		this.signName = signName;
		this.signRemark = signRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSignName() {
		return this.signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getSignRemark() {
		return this.signRemark;
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

	public void setSignRemark(String signRemark) {
		this.signRemark = signRemark;
	}

}