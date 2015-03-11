package com.sail.cot.domain;

/**
 * CotOrderEleprice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderEleprice implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderDetailId;
	private String priceName;
	private Integer priceUnit;
	private Double priceAmount;
	private String remark;
	private Integer orderId;

	// Constructors

	/** default constructor */
	public CotOrderEleprice() {
	}

	

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Integer getOrderDetailId() {
		return orderDetailId;
	}



	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}



	public String getPriceName() {
		return this.priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public Integer getPriceUnit() {
		return this.priceUnit;
	}

	public void setPriceUnit(Integer priceUnit) {
		this.priceUnit = priceUnit;
	}

	public Double getPriceAmount() {
		return this.priceAmount;
	}

	public void setPriceAmount(Double priceAmount) {
		this.priceAmount = priceAmount;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}