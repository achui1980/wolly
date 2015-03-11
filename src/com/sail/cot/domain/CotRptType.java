package com.sail.cot.domain;

/**
 * CotRptType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotRptType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String rptName;
	private String rptRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotRptType() {
	}

	/** full constructor */
	public CotRptType(String rptName, String rptRemark) {
		this.rptName = rptName;
		this.rptRemark = rptRemark;

	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRptName() {
		return this.rptName;
	}

	public void setRptName(String rptName) {
		this.rptName = rptName;
	}

	public String getRptRemark() {
		return this.rptRemark;
	}

	public void setRptRemark(String rptRemark) {
		this.rptRemark = rptRemark;
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