package com.sail.cot.domain.vo;

import java.sql.Date;

public class CotSampleVO {
	
	private Integer id;
	private String eleId;
	private String eleName;
	private String eleNameEn;
	private String custNo;
	private Integer custId;
	private Integer factoryId;
	private Float priceFac;
	private Float priceOut;
	private Date eleProTime;
	private Integer priceFacUint;
	private Integer priceOutUint;
	private Integer cid;
	private String op;
	
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
	public String getEleName() {
		return eleName;
	}
	public void setEleName(String eleName) {
		this.eleName = eleName;
	}
	public String getEleNameEn() {
		return eleNameEn;
	}
	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}
	public Float getPriceFac() {
		return priceFac;
	}
	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}
	public Float getPriceOut() {
		return priceOut;
	}
	public void setPriceOut(Float priceOut) {
		this.priceOut = priceOut;
	}
	public Date getEleProTime() {
		return eleProTime;
	}
	public void setEleProTime(Date eleProTime) {
		this.eleProTime = eleProTime;
	}
	public Integer getPriceFacUint() {
		return priceFacUint;
	}
	public void setPriceFacUint(Integer priceFacUint) {
		this.priceFacUint = priceFacUint;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Integer getPriceOutUint() {
		return priceOutUint;
	}
	public void setPriceOutUint(Integer priceOutUint) {
		this.priceOutUint = priceOutUint;
	}
	
}
