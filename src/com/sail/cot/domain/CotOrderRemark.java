package com.sail.cot.domain;

import java.util.Date;

/**
 * CotOrderRemark entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderRemark implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private String remark;
	private Date addTime;
	private String addPerson;

	// Constructors

	/** default constructor */
	public CotOrderRemark() {
	}

	/** minimal constructor */
	public CotOrderRemark(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotOrderRemark(Integer id, Integer orderId, String remark,
			Date addTime, Integer addPerson) {
		this.id = id;
		this.orderId = orderId;
		this.remark = remark;
		this.addTime = addTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}

	public String getAddPerson() {
		return addPerson;
	}
}