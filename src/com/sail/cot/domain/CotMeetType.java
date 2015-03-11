package com.sail.cot.domain;

/**
 * CotMeetType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMeetType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String meetName;
	private String meetRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotMeetType() {
	}

	/** full constructor */
	public CotMeetType(String meetName, String meetRemark) {
		this.meetName = meetName;
		this.meetRemark = meetRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMeetName() {
		return this.meetName;
	}

	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}

	public String getMeetRemark() {
		return this.meetRemark;
	}

	public void setMeetRemark(String meetRemark) {
		this.meetRemark = meetRemark;
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