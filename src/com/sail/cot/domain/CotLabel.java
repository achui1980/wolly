package com.sail.cot.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotLabel entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotLabel implements java.io.Serializable {

	// Fields

	private Integer id;
	private String labelName;
	private Date sendDate;
	private Integer factoryId;
	private Integer companyId;
	private String labelRemark;
	private Date addTime;
	private Integer businessPerson;
	private String labelNo;
	private Integer orderId;
	private Double otherMoney;
	private Double totalMoney;
	private Set cotLabeldetails = new HashSet(0);
	private Set cotPictures = null;
	private String op;

	// Constructors

	/** default constructor */
	public CotLabel() {
	}

	/** full constructor */
	public CotLabel(String labelName, Date sendDate, Integer factoryId,
			Integer companyId, String labelRemark, Date addTime,
			Integer businessPerson, String labelNo, Integer orderId,
			Double totalMoney, Set cotLabeldetails) {
		this.labelName = labelName;
		this.sendDate = sendDate;
		this.factoryId = factoryId;
		this.companyId = companyId;
		this.labelRemark = labelRemark;
		this.addTime = addTime;
		this.businessPerson = businessPerson;
		this.labelNo = labelNo;
		this.orderId = orderId;
		this.totalMoney = totalMoney;
		this.cotLabeldetails = cotLabeldetails;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabelName() {
		return this.labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getLabelRemark() {
		return this.labelRemark;
	}

	public void setLabelRemark(String labelRemark) {
		this.labelRemark = labelRemark;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getBusinessPerson() {
		return this.businessPerson;
	}

	public void setBusinessPerson(Integer businessPerson) {
		this.businessPerson = businessPerson;
	}

	public String getLabelNo() {
		return this.labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Double getTotalMoney() {
		return this.totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Set getCotLabeldetails() {
		return this.cotLabeldetails;
	}

	public void setCotLabeldetails(Set cotLabeldetails) {
		this.cotLabeldetails = cotLabeldetails;
	}

	public Double getOtherMoney() {
		return otherMoney;
	}

	public void setOtherMoney(Double otherMoney) {
		this.otherMoney = otherMoney;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Set getCotPictures() {
		return cotPictures;
	}

	public void setCotPictures(Set cotPictures) {
		this.cotPictures = cotPictures;
	}

}