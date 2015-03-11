package com.sail.cot.domain;

import java.sql.Date;

/**
 * CotClaim entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotClaim implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer custId;
	private Date claimTime;
	private Float claimMoney;
	private String claimReason;
	private String claimDeal;
	private String claimRemark;
	private Date addTime;
	private Integer addPerson;
	private String op;

	// Constructors

	/** default constructor */
	public CotClaim() {
	}

	/** full constructor */
	public CotClaim(CotCustomer cotCustomer, Date claimTime, Float claimMoney,
			String claimReason, String claimDeal, String claimRemark,
			Date addTime, Integer addPerson) {
		
		this.claimTime = claimTime;
		this.claimMoney = claimMoney;
		this.claimReason = claimReason;
		this.claimDeal = claimDeal;
		this.claimRemark = claimRemark;
		this.addTime = addTime;
		this.addPerson = addPerson;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Date getClaimTime() {
		return this.claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public Float getClaimMoney() {
		return this.claimMoney;
	}

	public void setClaimMoney(Float claimMoney) {
		this.claimMoney = claimMoney;
	}

	public String getClaimReason() {
		return this.claimReason;
	}

	public void setClaimReason(String claimReason) {
		this.claimReason = claimReason;
	}

	public String getClaimDeal() {
		return this.claimDeal;
	}

	public void setClaimDeal(String claimDeal) {
		this.claimDeal = claimDeal;
	}

	public String getClaimRemark() {
		return this.claimRemark;
	}

	public void setClaimRemark(String claimRemark) {
		this.claimRemark = claimRemark;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getAddPerson() {
		return this.addPerson;
	}

	public void setAddPerson(Integer addPerson) {
		this.addPerson = addPerson;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}