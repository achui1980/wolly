package com.sail.cot.domain;

/**
 * CotPriceEleprice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPriceEleprice implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer priceDetailId;
	private String priceName;
	private Integer priceUnit;
	private Double priceAmount;
	private String remark;
	private Integer priceId;

	// Constructors

	/** default constructor */
	public CotPriceEleprice() {
	}

	

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPriceDetailId() {
		return priceDetailId;
	}



	public void setPriceDetailId(Integer priceDetailId) {
		this.priceDetailId = priceDetailId;
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

	public Integer getPriceId() {
		return this.priceId;
	}

	public void setPriceId(Integer priceId) {
		this.priceId = priceId;
	}

}