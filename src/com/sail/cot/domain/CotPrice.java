package com.sail.cot.domain;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * CotPrice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPrice implements java.io.Serializable {

	// Fields

	private Integer id;
	private String priceNo;
	private Timestamp priceTime;
	private Double priceCharge;
	private Double priceFob;
	private Date addTime;
	private String addPerson;
	private Integer businessPerson;
	private Long priceStatus;
	private String checkReason;
	private Long priceIscheck;
	private String priceRemark;
	private Float priceRate;
	private Double commisionScale;
	private Double cutScale;
	private Double priceProfit;
	private Integer custId;
	private Integer currencyId;
	private Integer clauseId;
	private Integer shipportId;
	private Integer targetportId;
	private Integer containerTypeId;
	private Integer commisionTypeId;
	private Integer situationId;
	private Integer payTypeId;
	private Double insureRate;
	private Double insureAddRate;
	private Integer empId;
	private Integer trafficId;
	private Integer companyId;
	private Integer validMonths;
	private Set cotPriceDetails = new HashSet(0);
	private String op;
	private String chk;
	private String priceCompose;
	private String creditNo;
	private Integer insureContractId;
	private String prodPeriod;
	private Integer zheType;
	private Float zheNum;
	
	private String checkPerson;//审核人姓名
	private Integer contactId;

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	public Integer getZheType() {
		return zheType;
	}

	public void setZheType(Integer zheType) {
		this.zheType = zheType;
	}

	public Float getZheNum() {
		return zheNum;
	}

	public void setZheNum(Float zheNum) {
		this.zheNum = zheNum;
	}

	// Constructors
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

	/** default constructor */
	public CotPrice() {
	}

	/** minimal constructor */
	public CotPrice(String priceNo, Long priceStatus, Long priceIscheck) {
		this.priceNo = priceNo;
		this.priceStatus = priceStatus;
		this.priceIscheck = priceIscheck;
	}

	/** full constructor */
	public CotPrice(String priceNo, java.sql.Timestamp priceTime, Double priceCharge,
			Double priceFob, java.sql.Date addTime, String addPerson, Long priceStatus,
			String checkReason, Long priceIscheck, String priceRemark,
			Float priceRate, Double commisionScale, Double cutScale,
			Double priceProfit, Integer custId, Integer currencyId,
			Integer clauseId, Integer shipportId, Integer targetportId,
			Integer containerTypeId, Integer commisionTypeId,Double insureRate,Double insureAddRate,
			Integer situationId, Integer payTypeId, Set cotPriceDetails,Integer businessPerson) {
		this.priceNo = priceNo;
		this.priceTime = priceTime;
		this.priceCharge = priceCharge;
		this.priceFob = priceFob;
		this.addTime = addTime;
		this.addPerson = addPerson;
		this.priceStatus = priceStatus;
		this.checkReason = checkReason;
		this.priceIscheck = priceIscheck;
		this.priceRemark = priceRemark;
		this.priceRate = priceRate;
		this.commisionScale = commisionScale;
		this.cutScale = cutScale;
		this.priceProfit = priceProfit;
		this.custId = custId;
		this.currencyId = currencyId;
		this.clauseId = clauseId;
		this.shipportId = shipportId;
		this.targetportId = targetportId;
		this.containerTypeId = containerTypeId;
		this.commisionTypeId = commisionTypeId;
		this.situationId = situationId;
		this.insureRate = insureRate;
		this.insureAddRate = insureAddRate;
		this.payTypeId = payTypeId;
		this.cotPriceDetails = cotPriceDetails;
		this.businessPerson = businessPerson;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBusinessPerson() {
		return businessPerson;
	}

	public void setBusinessPerson(Integer businessPerson) {
		this.businessPerson = businessPerson;
	}

	public String getPriceNo() {
		return this.priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}

	public Double getPriceCharge() {
		return this.priceCharge;
	}

	public void setPriceCharge(Double priceCharge) {
		this.priceCharge = priceCharge;
	}

	public Double getPriceFob() {
		return this.priceFob;
	}

	public void setPriceFob(Double priceFob) {
		this.priceFob = priceFob;
	}

	public Timestamp getPriceTime() {
		return priceTime;
	}

	public void setPriceTime(Timestamp priceTime) {
		this.priceTime = priceTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddPerson() {
		return this.addPerson;
	}

	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}

	public Long getPriceStatus() {
		return this.priceStatus;
	}

	public void setPriceStatus(Long priceStatus) {
		this.priceStatus = priceStatus;
	}

	public String getCheckReason() {
		return this.checkReason;
	}

	public void setCheckReason(String checkReason) {
		this.checkReason = checkReason;
	}

	public Long getPriceIscheck() {
		return this.priceIscheck;
	}

	public void setPriceIscheck(Long priceIscheck) {
		this.priceIscheck = priceIscheck;
	}

	public String getPriceRemark() {
		return this.priceRemark;
	}

	public void setPriceRemark(String priceRemark) {
		this.priceRemark = priceRemark;
	}

	public Float getPriceRate() {
		return this.priceRate;
	}

	public void setPriceRate(Float priceRate) {
		this.priceRate = priceRate;
	}

	public Double getCommisionScale() {
		return this.commisionScale;
	}

	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}

	public Double getCutScale() {
		return this.cutScale;
	}

	public void setCutScale(Double cutScale) {
		this.cutScale = cutScale;
	}

	public Double getPriceProfit() {
		return this.priceProfit;
	}

	public void setPriceProfit(Double priceProfit) {
		this.priceProfit = priceProfit;
	}

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getClauseId() {
		return this.clauseId;
	}

	public void setClauseId(Integer clauseId) {
		this.clauseId = clauseId;
	}

	public Integer getShipportId() {
		return this.shipportId;
	}

	public void setShipportId(Integer shipportId) {
		this.shipportId = shipportId;
	}

	public Integer getTargetportId() {
		return this.targetportId;
	}

	public void setTargetportId(Integer targetportId) {
		this.targetportId = targetportId;
	}

	public Integer getContainerTypeId() {
		return this.containerTypeId;
	}

	public void setContainerTypeId(Integer containerTypeId) {
		this.containerTypeId = containerTypeId;
	}

	public Integer getCommisionTypeId() {
		return this.commisionTypeId;
	}

	public void setCommisionTypeId(Integer commisionTypeId) {
		this.commisionTypeId = commisionTypeId;
	}

	public Integer getSituationId() {
		return this.situationId;
	}

	public void setSituationId(Integer situationId) {
		this.situationId = situationId;
	}

	public Integer getPayTypeId() {
		return this.payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public Set getCotPriceDetails() {
		return this.cotPriceDetails;
	}

	public void setCotPriceDetails(Set cotPriceDetails) {
		this.cotPriceDetails = cotPriceDetails;
	}

	public Double getInsureRate() {
		return insureRate;
	}

	public void setInsureRate(Double insureRate) {
		this.insureRate = insureRate;
	}

	public Double getInsureAddRate() {
		return insureAddRate;
	}

	public void setInsureAddRate(Double insureAddRate) {
		this.insureAddRate = insureAddRate;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getTrafficId() {
		return trafficId;
	}

	public void setTrafficId(Integer trafficId) {
		this.trafficId = trafficId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getPriceCompose() {
		return priceCompose;
	}

	public void setPriceCompose(String priceCompose) {
		this.priceCompose = priceCompose;
	}

	public Integer getValidMonths() {
		return validMonths;
	}

	public void setValidMonths(Integer validMonths) {
		this.validMonths = validMonths;
	}

	public String getCreditNo() {
		return creditNo;
	}

	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
	}

	public Integer getInsureContractId() {
		return insureContractId;
	}

	public void setInsureContractId(Integer insureContractId) {
		this.insureContractId = insureContractId;
	}

	public String getProdPeriod() {
		return prodPeriod;
	}

	public void setProdPeriod(String prodPeriod) {
		this.prodPeriod = prodPeriod;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

}