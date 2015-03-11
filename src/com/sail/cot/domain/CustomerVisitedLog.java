package com.sail.cot.domain;

import java.sql.Date;

/**
 * CustomerVisitedLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CustomerVisitedLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer cotCustomerId;
	private String visitPeople;
	private java.sql.Date visitTime;
	private String visitDuty;
	private String visitReason;
	private Long visitNo;
	private String visitRemark;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CustomerVisitedLog() {
	}

	/** full constructor */
	public CustomerVisitedLog(CotCustomer cotCustomer, String visitPeople,
			java.sql.Date visitTime, String visitDuty, String visitReason, Long visitNo,
			String visitRemark) {
		
		this.visitPeople = visitPeople;
		this.visitTime = visitTime;
		this.visitDuty = visitDuty;
		this.visitReason = visitReason;
		this.visitNo = visitNo;
		this.visitRemark = visitRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getCotCustomerId() {
		return cotCustomerId;
	}

	public void setCotCustomerId(Integer cotCustomerId) {
		this.cotCustomerId = cotCustomerId;
	}

	public String getVisitPeople() {
		return this.visitPeople;
	}

	public void setVisitPeople(String visitPeople) {
		this.visitPeople = visitPeople;
	}

	public java.sql.Date getVisitTime() {
		return this.visitTime;
	}

	public void setVisitTime(java.sql.Date visitTime) {
		this.visitTime = visitTime;
	}

	public String getVisitDuty() {
		return this.visitDuty;
	}

	public void setVisitDuty(String visitDuty) {
		this.visitDuty = visitDuty;
	}

	public String getVisitReason() {
		return this.visitReason;
	}

	public void setVisitReason(String visitReason) {
		this.visitReason = visitReason;
	}

	public Long getVisitNo() {
		return this.visitNo;
	}

	public void setVisitNo(Long visitNo) {
		this.visitNo = visitNo;
	}

	public String getVisitRemark() {
		return this.visitRemark;
	}

	public void setVisitRemark(String visitRemark) {
		this.visitRemark = visitRemark;
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