package com.sail.cot.domain;

/**
 * CotArea entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotModPrice implements java.io.Serializable {

	// Fields

	private Integer id;
	private String eleId;
	private Integer custId;
	private Float price;
	private Integer currencyId;
	private Integer modNum;
	private String remark;
	// Constructors

	/** default constructor */
	public CotModPrice() {
	}

	/** full constructor */
	public CotModPrice(Integer id, String eleId, Integer custId,Float price,
			Integer mod) {
		this.id = id;
		this.eleId = eleId;
		this.custId = custId;
		this.price = price;
		this.modNum = mod;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getModNum() {
		return modNum;
	}

	public void setModNum(Integer modNum) {
		this.modNum = modNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	
}