package com.sail.cot.domain.vo;

import java.sql.Date;

public class CotSignVO {
	
	private Integer id;
	private String eleId;
	private String eleName;
	private String signNo;
	private Integer factoryId;
	private String customerShortName;
	private String empsName;
	private Date signTime;
	private Date requireTime;
	private Date arriveTime;
	private String op;
	private Integer signId;
	private Float priceOut;
	private Integer priceOutUint;
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
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
	public String getSignNo() {
		return signNo;
	}
	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public String getEmpsName() {
		return empsName;
	}
	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}
	public Date getSignTime() {
		return signTime;
	}
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	public Integer getSignId() {
		return signId;
	}
	public void setSignId(Integer signId) {
		this.signId = signId;
	}
	public Float getPriceOut() {
		return priceOut;
	}
	public void setPriceOut(Float priceOut) {
		this.priceOut = priceOut;
	}
	public Integer getPriceOutUint() {
		return priceOutUint;
	}
	public void setPriceOutUint(Integer priceOutUint) {
		this.priceOutUint = priceOutUint;
	}
	public Date getRequireTime() {
		return requireTime;
	}
	public void setRequireTime(Date requireTime) {
		this.requireTime = requireTime;
	}
	public Date getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}
	
	
}
