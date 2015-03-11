package com.sail.cot.domain;

import java.util.Date;

/**
 * CotBacktax entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotBacktax implements java.io.Serializable {

	// Fields

	private Integer id;
	private String taxNo;
	private Date taxDate;
	private Integer empId;
	private Double taxAmount;
	private Long taxStatus;
	private Double taxRealAmount;
	private Date addTime;
	private String op;

	// Constructors

	/** default constructor */
	public CotBacktax() {
	}

	/** full constructor */
	public CotBacktax(String taxNo, Date taxDate, Integer empId,
			Double taxAmount, Long taxStatus, Double taxRealAmount, Date addTime) {
		this.taxNo = taxNo;
		this.taxDate = taxDate;
		this.empId = empId;
		this.taxAmount = taxAmount;
		this.taxStatus = taxStatus;
		this.taxRealAmount = taxRealAmount;
		this.addTime = addTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTaxNo() {
		return this.taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public Date getTaxDate() {
		return this.taxDate;
	}

	public void setTaxDate(Date taxDate) {
		this.taxDate = taxDate;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Double getTaxAmount() {
		return this.taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Long getTaxStatus() {
		return this.taxStatus;
	}

	public void setTaxStatus(Long taxStatus) {
		this.taxStatus = taxStatus;
	}

	public Double getTaxRealAmount() {
		return this.taxRealAmount;
	}

	public void setTaxRealAmount(Double taxRealAmount) {
		this.taxRealAmount = taxRealAmount;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}