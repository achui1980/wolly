package com.sail.cot.domain;

/**
 * CotCutType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCutType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String cutName;
	private String cutRemark;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotCutType() {
	}

	/** full constructor */
	public CotCutType(String cutName, String cutRemark) {
		this.cutName = cutName;
		this.cutRemark = cutRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCutName() {
		return this.cutName;
	}

	public void setCutName(String cutName) {
		this.cutName = cutName;
	}

	public String getCutRemark() {
		return this.cutRemark;
	}

	public void setCutRemark(String cutRemark) {
		this.cutRemark = cutRemark;
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