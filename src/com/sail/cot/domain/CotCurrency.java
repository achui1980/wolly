package com.sail.cot.domain;

/**
 * CotEmps entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCurrency implements java.io.Serializable {

	// Fields

	private Integer id;
	private String curName;
	private String curUnit;
	private Float curRate;
	private String curNameEn;
	private String op;
	private String chk;

	// Constructors

	public String getCurNameEn() {
		return curNameEn;
	}

	public void setCurNameEn(String curNameEn) {
		this.curNameEn = curNameEn;
	}

	/** default constructor */
	public CotCurrency() {
	}

	/** minimal constructor */
	public CotCurrency(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotCurrency(Integer id, String curName, String curUnit, Float curRate,
			String empsRemark, Integer dept, Integer roleId) {
		this.id = id;
		this.curName = curName;
		this.curUnit = curUnit;
		this.curRate = curRate;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
	}

	public String getCurUnit() {
		return curUnit;
	}

	public void setCurUnit(String curUnit) {
		this.curUnit = curUnit;
	}

	public Float getCurRate() {
		return curRate;
	}

	public void setCurRate(Float curRate) {
		this.curRate = curRate;
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