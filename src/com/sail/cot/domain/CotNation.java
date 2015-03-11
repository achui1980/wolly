package com.sail.cot.domain;

/**
 * CotNation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotNation implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nationName;
	private String nationCode;
	private String nationRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotNation() {
	}

	/** full constructor */
	public CotNation(String nationName, String nationCode, String nationRemark) {
		this.nationName = nationName;
		this.nationCode = nationCode;
		this.nationRemark = nationRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNationName() {
		return this.nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public String getNationCode() {
		return nationCode;
	}

	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}

	public String getNationRemark() {
		return this.nationRemark;
	}

	public void setNationRemark(String nationRemark) {
		this.nationRemark = nationRemark;
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