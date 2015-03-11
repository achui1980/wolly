package com.sail.cot.domain.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class CotPriceVO {
	
	private Integer id;
	private Integer detailId;//明细ID
	private String priceNo;
	private String custNo;
	private String eleId;
	private String eleName;
	private String eleNameEn;
	private String customerNo;
	private String fullNameCn;
	private String customerShortName;
	private Double commisionScale;
	private Double priceProfit;
	private Timestamp priceTime;
	private String addPerson;
	private Integer clauseId;
	private Integer currencyId;
	private Integer boxTypeId;
	private Integer situationId;
	private Integer empId;
	private Float priceOut;
	private Float pricePrice;
	private String customerEmail;
	private String empsName;
	private Integer validMonths;
	private Integer csId;
	private String op;
	private Integer priceFacUint;
	private Float priceFac;
	private Date eleAddTime;
	private Integer businessPerson;
	private Long priceStatus;
	private String priceCompose;
	private Integer contactId;
	private Float priceRate;
	private Integer custId;
	private String eleSizeDesc;
	
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public Integer getValidMonths() {
		return validMonths;
	}
	public void setValidMonths(Integer validMonths) {
		this.validMonths = validMonths;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
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
	public String getPriceNo() {
		return priceNo;
	}
	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getFullNameCn() {
		return fullNameCn;
	}
	public void setFullNameCn(String fullNameCn) {
		this.fullNameCn = fullNameCn;
	}
	public Double getCommisionScale() {
		return commisionScale;
	}
	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}
	public Double getPriceProfit() {
		return priceProfit;
	}
	public void setPriceProfit(Double priceProfit) {
		this.priceProfit = priceProfit;
	}
	public Timestamp getPriceTime() {
		return priceTime;
	}
	public void setPriceTime(Timestamp priceTime) {
		this.priceTime = priceTime;
	}
	public String getAddPerson() {
		return addPerson;
	}
	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}
	public Integer getClauseId() {
		return clauseId;
	}
	public void setClauseId(Integer clauseId) {
		this.clauseId = clauseId;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public Integer getCsId() {
		return csId;
	}
	public void setCsId(Integer csId) {
		this.csId = csId;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public Float getPriceOut() {
		return priceOut;
	}
	public void setPriceOut(Float priceOut) {
		this.priceOut = priceOut;
	}
	public String getEmpsName() {
		return empsName;
	}
	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}
	public Float getPricePrice() {
		return pricePrice;
	}
	public void setPricePrice(Float pricePrice) {
		this.pricePrice = pricePrice;
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
	public Integer getPriceFacUint() {
		return priceFacUint;
	}
	public void setPriceFacUint(Integer priceFacUint) {
		this.priceFacUint = priceFacUint;
	}
	public Float getPriceFac() {
		return priceFac;
	}
	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}
	public Date getEleAddTime() {
		return eleAddTime;
	}
	public void setEleAddTime(Date eleAddTime) {
		this.eleAddTime = eleAddTime;
	}
	public String getEleNameEn() {
		return eleNameEn;
	}
	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}
	public Integer getSituationId() {
		return situationId;
	}
	public void setSituationId(Integer situationId) {
		this.situationId = situationId;
	}
	public Integer getBusinessPerson() {
		return businessPerson;
	}
	public void setBusinessPerson(Integer businessPerson) {
		this.businessPerson = businessPerson;
	}
	public Long getPriceStatus() {
		return priceStatus;
	}
	public void setPriceStatus(Long priceStatus) {
		this.priceStatus = priceStatus;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public Integer getBoxTypeId() {
		return boxTypeId;
	}
	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
	}
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	public String getPriceCompose() {
		return priceCompose;
	}
	public void setPriceCompose(String priceCompose) {
		this.priceCompose = priceCompose;
	}
	public Float getPriceRate() {
		return priceRate;
	}
	public void setPriceRate(Float priceRate) {
		this.priceRate = priceRate;
	}
	public Integer getContactId() {
		return contactId;
	}
	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
	public String getEleSizeDesc() {
		return eleSizeDesc;
	}
	public void setEleSizeDesc(String eleSizeDesc) {
		this.eleSizeDesc = eleSizeDesc;
	}
}
