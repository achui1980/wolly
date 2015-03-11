package com.sail.cot.domain.vo;


public class CotPriceFacVO {
	
	private Integer id;
	private String eleId;
	private Integer priceUint;
	private Float priceFac;
	private java.sql.Date addTime;
	private String eleName;
	private String priceRemark;
	private String shortName;
	private String eleSizeDesc;
	private Integer elId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEleId() {
		return eleId;
	}
	public void setEleId(String eleId) {
		this.eleId = eleId;
	}
	public Integer getPriceUint() {
		return priceUint;
	}
	public void setPriceUint(Integer priceUint) {
		this.priceUint = priceUint;
	}
	public Float getPriceFac() {
		return priceFac;
	}
	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}
	public java.sql.Date getAddTime() {
		return addTime;
	}
	public void setAddTime(java.sql.Date addTime) {
		this.addTime = addTime;
	}
	public String getEleName() {
		return eleName;
	}
	public void setEleName(String eleName) {
		this.eleName = eleName;
	}
	public String getPriceRemark() {
		return priceRemark;
	}
	public void setPriceRemark(String priceRemark) {
		this.priceRemark = priceRemark;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public Integer getElId() {
		return elId;
	}
	public void setElId(Integer elId) {
		this.elId = elId;
	}
	public String getEleSizeDesc() {
		return eleSizeDesc;
	}
	public void setEleSizeDesc(String eleSizeDesc) {
		this.eleSizeDesc = eleSizeDesc;
	}
	
	
}
