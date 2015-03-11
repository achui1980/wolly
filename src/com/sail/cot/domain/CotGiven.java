package com.sail.cot.domain;

import java.util.Set;

/**
 * CotGiven entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotGiven implements java.io.Serializable {

	// Fields

	private Integer id;
	private String givenNo;
	private java.sql.Timestamp givenTime;
	private java.sql.Date custRequiretime;
	private java.sql.Date planGiventime;
	private java.sql.Date realGiventime;
	private String givenAddr;
	private String followPerson;
	private String givenRemark;
	private Long givenIscheck;//审核状态
	private Long givenStatus;//征样完成否
	private String givenCheckreason;
	private java.sql.Date addTime;
	private String addPerson;
	private Integer givenId;
	private Integer balanceTypeId;
	private Integer custId;
	private Set cotGivenDetails = null;
	private Set cotSign = null;
	private Integer empId;
	private String op;
	private String chk;
	private Integer  bussinessPerson;
	
	private Integer checkComplete;
	private String containerDesc;
	private String grossWeightDesc;
	private Double signTotalPrice;
	private Integer currencyId;//快递费用币种
	private Integer payType;
	private Integer checkFee;
	private Integer bankId;
	private String emsNo;
	private Integer expressCompany;
	private Integer companyId;
	private Double sampleFee;
	private Integer sampleFeeCheck;
	
	private String checkPerson;//审核人姓名
	
	private Integer curId;//主单币种

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	// Constructors
	public Integer getBussinessPerson() {
		return bussinessPerson;
	}

	public void setBussinessPerson(Integer bussinessPerson) {
		this.bussinessPerson = bussinessPerson;
	}

	/** default constructor */
	public CotGiven() {
	}

	/** minimal constructor */
	public CotGiven(Integer id, Long givenIscheck, Long givenStatus) {
		this.id = id;
		this.givenIscheck = givenIscheck;
		this.givenStatus = givenStatus;
	}

	/** full constructor */
	public CotGiven(Integer id, String givenNo, java.sql.Timestamp givenTime,
			java.sql.Date custRequiretime, java.sql.Date planGiventime, java.sql.Date realGiventime,
			String givenAddr, String followPerson, String givenRemark,
			Long givenIscheck, Long givenStatus, String givenCheckreason,
			java.sql.Date addTime, String addPerson, Integer givenId,
			Integer balanceTypeId,Integer custId, Set cotGivenDetails) {
		this.id = id;
		this.givenNo = givenNo;
		this.givenTime = givenTime;
		this.custRequiretime = custRequiretime;
		this.planGiventime = planGiventime;
		this.realGiventime = realGiventime;
		this.givenAddr = givenAddr;
		this.followPerson = followPerson;
		this.givenRemark = givenRemark;
		this.givenIscheck = givenIscheck;
		this.givenStatus = givenStatus;
		this.givenCheckreason = givenCheckreason;
		this.addTime = addTime;
		this.addPerson = addPerson;
		this.givenId = givenId;
		this.balanceTypeId = balanceTypeId;
		this.custId = custId;
		this.cotGivenDetails = cotGivenDetails;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGivenNo() {
		return this.givenNo;
	}

	public void setGivenNo(String givenNo) {
		this.givenNo = givenNo;
	}

	public java.sql.Timestamp getGivenTime() {
		return this.givenTime;
	}

	public void setGivenTime(java.sql.Timestamp givenTime) {
		this.givenTime = givenTime;
	}

	public java.sql.Date getCustRequiretime() {
		return this.custRequiretime;
	}

	public void setCustRequiretime(java.sql.Date custRequiretime) {
		this.custRequiretime = custRequiretime;
	}

	public java.sql.Date getPlanGiventime() {
		return this.planGiventime;
	}

	public void setPlanGiventime(java.sql.Date planGiventime) {
		this.planGiventime = planGiventime;
	}

	public java.sql.Date getRealGiventime() {
		return this.realGiventime;
	}

	public void setRealGiventime(java.sql.Date realGiventime) {
		this.realGiventime = realGiventime;
	}

	public String getGivenAddr() {
		return this.givenAddr;
	}

	public void setGivenAddr(String givenAddr) {
		this.givenAddr = givenAddr;
	}

	public String getFollowPerson() {
		return this.followPerson;
	}

	public void setFollowPerson(String followPerson) {
		this.followPerson = followPerson;
	}

	public String getGivenRemark() {
		return this.givenRemark;
	}

	public void setGivenRemark(String givenRemark) {
		this.givenRemark = givenRemark;
	}

	public Long getGivenIscheck() {
		return this.givenIscheck;
	}

	public void setGivenIscheck(Long givenIscheck) {
		this.givenIscheck = givenIscheck;
	}

	public Long getGivenStatus() {
		return this.givenStatus;
	}

	public void setGivenStatus(Long givenStatus) {
		this.givenStatus = givenStatus;
	}

	public String getGivenCheckreason() {
		return this.givenCheckreason;
	}

	public void setGivenCheckreason(String givenCheckreason) {
		this.givenCheckreason = givenCheckreason;
	}

	public java.sql.Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(java.sql.Date addTime) {
		this.addTime = addTime;
	}

	public String getAddPerson() {
		return this.addPerson;
	}

	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}

	public Integer getGivenId() {
		return this.givenId;
	}

	public void setGivenId(Integer givenId) {
		this.givenId = givenId;
	}

	public Integer getBalanceTypeId() {
		return this.balanceTypeId;
	}

	public void setBalanceTypeId(Integer balanceTypeId) {
		this.balanceTypeId = balanceTypeId;
	}

	public Set getCotGivenDetails() {
		return this.cotGivenDetails;
	}

	public void setCotGivenDetails(Set cotGivenDetails) {
		this.cotGivenDetails = cotGivenDetails;
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


	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Set getCotSign() {
		return cotSign;
	}

	public void setCotSign(Set cotSign) {
		this.cotSign = cotSign;
	}

	public Integer getCheckComplete() {
		return checkComplete;
	}

	public void setCheckComplete(Integer checkComplete) {
		this.checkComplete = checkComplete;
	}

	public String getContainerDesc() {
		return containerDesc;
	}

	public void setContainerDesc(String containerDesc) {
		this.containerDesc = containerDesc;
	}

	public String getGrossWeightDesc() {
		return grossWeightDesc;
	}

	public void setGrossWeightDesc(String grossWeightDesc) {
		this.grossWeightDesc = grossWeightDesc;
	}

	public Double getSignTotalPrice() {
		return signTotalPrice;
	}

	public void setSignTotalPrice(Double signTotalPrice) {
		this.signTotalPrice = signTotalPrice;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getCheckFee() {
		return checkFee;
	}

	public void setCheckFee(Integer checkFee) {
		this.checkFee = checkFee;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getEmsNo() {
		return emsNo;
	}

	public void setEmsNo(String emsNo) {
		this.emsNo = emsNo;
	}

	public Integer getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(Integer expressCompany) {
		this.expressCompany = expressCompany;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Double getSampleFee() {
		return sampleFee;
	}

	public void setSampleFee(Double sampleFee) {
		this.sampleFee = sampleFee;
	}

	public Integer getSampleFeeCheck() {
		return sampleFeeCheck;
	}

	public void setSampleFeeCheck(Integer sampleFeeCheck) {
		this.sampleFeeCheck = sampleFeeCheck;
	}

	public Integer getCurId() {
		return curId;
	}

	public void setCurId(Integer curId) {
		this.curId = curId;
	}


}