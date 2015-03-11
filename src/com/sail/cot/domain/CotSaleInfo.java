package com.sail.cot.domain;

import java.util.Date;

/**
 * CotSaleInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSaleInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer eleId;
	private Date recordDate;
	private Integer inNum;
	private Integer outNum;
	private Integer currNum;
	private Integer custId;
	private Double money;
	private Double totalMoney;
	private Date addTime;
	private String eleName;
	private Integer saleType;

	// Constructors

	/** default constructor */
	public CotSaleInfo() {
	}

	/** full constructor */
	public CotSaleInfo(CotElementsNew cotElementsNew, Date recordDate,
			Integer inNum, Integer outNum, Integer currNum, Integer custId,
			Double money, Double totalMoney, Date addTime, String eleName,
			Integer saleType) {
		
		this.recordDate = recordDate;
		this.inNum = inNum;
		this.outNum = outNum;
		this.currNum = currNum;
		this.custId = custId;
		this.money = money;
		this.totalMoney = totalMoney;
		this.addTime = addTime;
		this.eleName = eleName;
		this.saleType = saleType;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Integer getEleId() {
		return eleId;
	}

	public void setEleId(Integer eleId) {
		this.eleId = eleId;
	}

	public Date getRecordDate() {
		return this.recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Integer getInNum() {
		return this.inNum;
	}

	public void setInNum(Integer inNum) {
		this.inNum = inNum;
	}

	public Integer getOutNum() {
		return this.outNum;
	}

	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
	}

	public Integer getCurrNum() {
		return this.currNum;
	}

	public void setCurrNum(Integer currNum) {
		this.currNum = currNum;
	}

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getTotalMoney() {
		return this.totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getEleName() {
		return this.eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public Integer getSaleType() {
		return this.saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}

}