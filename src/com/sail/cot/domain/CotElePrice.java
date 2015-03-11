package com.sail.cot.domain;

/**
 * CotElePrice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotElePrice implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer eleId;
	private String priceName;
	private Integer priceUnit;
	private Double priceAmount;
	private String remark;
	private String op;

	// Constructors

	/** default constructor */
	public CotElePrice() {
	}

	/** minimal constructor */
	public CotElePrice(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotElePrice(Integer id, CotElementsNew cotElementsNew,
			String priceName, Integer priceUnit, Double priceAmount) {
		this.id = id;
		
		this.priceName = priceName;
		this.priceUnit = priceUnit;
		this.priceAmount = priceAmount;
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

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}