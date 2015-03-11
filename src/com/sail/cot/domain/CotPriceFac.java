package com.sail.cot.domain;

import java.util.Date;

import com.sail.cot.util.ContextUtil;

/**
 * CotPriceFac entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPriceFac implements java.io.Serializable {

	// Fields--样品厂家报价表

	private Integer id;
	private Integer eleId;
	private Integer priceUint;
	private Float priceFac;
	private Integer priceFlag;
	private java.sql.Date addTime;
	private Integer facId;
	private String priceRemark;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotPriceFac() {
	}


	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getPriceUint() {
		return this.priceUint;
	}

	public void setPriceUint(Integer priceUint) {
		this.priceUint = priceUint;
	}

	public Float getPriceFac() {
		priceFac = new Float(ContextUtil.getObjByPrecision(priceFac, "facPrecision"));
		return this.priceFac;
	}

	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}

	public java.sql.Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(java.sql.Date addTime) {
		this.addTime = addTime;
	}

	public Integer getEleId() {
		return eleId;
	}

	public void setEleId(Integer eleId) {
		this.eleId = eleId;
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

	public Integer getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(Integer priceFlag) {
		this.priceFlag = priceFlag;
	}

	public Integer getFacId() {
		return facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public String getPriceRemark() {
		return priceRemark;
	}

	public void setPriceRemark(String priceRemark) {
		this.priceRemark = priceRemark;
	}

	
}