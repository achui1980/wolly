package com.sail.cot.domain;

import java.util.Set;

/**
 * CotArea entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotAudit implements java.io.Serializable {

	// Fields
	private Integer id;
	private String auditNo;//核销单号
	private String invoiceNo;//出货发票编号
	private java.sql.Date receiveDate;//领单日期
	private java.sql.Date effectDate;//有效期
	private java.sql.Date usedDate;//领用日期
	private java.sql.Date submitDate;//交单日期
	private Integer currencyId;//币种
	private Integer targetPortId;//目的港
	private Float totalMoney;//收汇金额
	private Float totalTui;//退税金额
	private java.sql.Date auditDate;//核销日期
	private java.sql.Date balanceDate;//结算日期
	private Long auditStatus;//核销状态 1.空白 2.领取 3.核销 4.逾期 5.迟交
	private String auditReasonDelay;//迟交原因
	private String auditReason;//逾期原因
	private java.sql.Date addTime;//添加日期
	private Integer businessPerson;//业务员
	private Integer orderOutId;
	private String op;
	private String chk;
	private Integer taxId;
	private Integer calCount;
	private Long bankFee;
	private Long cashFee;
	private Long commisionScal;
	
	public Integer getCalCount() {
		return calCount;
	}

	public void setCalCount(Integer calCount) {
		this.calCount = calCount;
	}

	public Long getBankFee() {
		return bankFee;
	}

	public void setBankFee(Long bankFee) {
		this.bankFee = bankFee;
	}

	public Long getCashFee() {
		return cashFee;
	}

	public void setCashFee(Long cashFee) {
		this.cashFee = cashFee;
	}

	public Long getCommisionScal() {
		return commisionScal;
	}

	public void setCommisionScal(Long commisionScal) {
		this.commisionScal = commisionScal;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	// Constructors
	/** default constructor */
	public CotAudit() {
	}

	/** minimal constructor */
	public CotAudit(String auditNo, Long auditStatus) {
		this.auditNo = auditNo;
		this.auditStatus = auditStatus;
		
	}

	/** full constructor */
	public CotAudit(String auditNo, String invoiceNo,java.sql.Date receiveDate, java.sql.Date effectDate,
			java.sql.Date usedDate,java.sql.Date submitDate,Integer currencyId,Integer targetPortId,
			Float totalMoney,Float totalTui,java.sql.Date auditDate,java.sql.Date balanceDate,Long auditStatus,
			String auditReasonDelay,String auditReason,java.sql.Date addTime,Integer businessPerson,Integer orderOutId) {
		this.auditNo = auditNo;
		this.invoiceNo = invoiceNo;
		this.receiveDate = receiveDate;
		this.effectDate = effectDate;
		this.usedDate = usedDate;
		this.submitDate = submitDate;
		this.currencyId = currencyId;
		this.targetPortId = targetPortId;
		this.totalMoney = totalMoney;
		this.totalTui = totalTui;
		this.auditDate = auditDate;
		this.balanceDate = balanceDate;
		this.auditStatus = auditStatus;
		this.auditReasonDelay = auditReasonDelay;
		this.auditReason = auditReason;
		this.addTime = addTime;
		this.businessPerson = businessPerson;
		this.orderOutId = orderOutId;
	}
	
	// Property accessors
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAuditNo() {
		return auditNo;
	}
	public void setAuditNo(String auditNo) {
		this.auditNo = auditNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public java.sql.Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(java.sql.Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public java.sql.Date getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(java.sql.Date effectDate) {
		this.effectDate = effectDate;
	}
	public java.sql.Date getUsedDate() {
		return usedDate;
	}
	public void setUsedDate(java.sql.Date usedDate) {
		this.usedDate = usedDate;
	}
	public java.sql.Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(java.sql.Date submitDate) {
		this.submitDate = submitDate;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Integer getTargetPortId() {
		return targetPortId;
	}
	public void setTargetPortId(Integer targetPortId) {
		this.targetPortId = targetPortId;
	}
	public Float getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Float getTotalTui() {
		return totalTui;
	}
	public void setTotalTui(Float totalTui) {
		this.totalTui = totalTui;
	}
	public java.sql.Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(java.sql.Date auditDate) {
		this.auditDate = auditDate;
	}
	public java.sql.Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(java.sql.Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Long getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditReasonDelay() {
		return auditReasonDelay;
	}
	public void setAuditReasonDelay(String auditReasonDelay) {
		this.auditReasonDelay = auditReasonDelay;
	}
	public String getAuditReason() {
		return auditReason;
	}
	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}
	public java.sql.Date getAddTime() {
		return addTime;
	}
	public void setAddTime(java.sql.Date addTime) {
		this.addTime = addTime;
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

	public Integer getBusinessPerson() {
		return businessPerson;
	}

	public void setBusinessPerson(Integer businessPerson) {
		this.businessPerson = businessPerson;
	}

	public Integer getOrderOutId() {
		return orderOutId;
	}

	public void setOrderOutId(Integer orderOutId) {
		this.orderOutId = orderOutId;
	}
	
}
