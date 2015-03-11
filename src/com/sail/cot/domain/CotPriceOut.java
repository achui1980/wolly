package com.sail.cot.domain;

import java.sql.Date;

/**
 * CotPriceOut entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPriceOut implements java.io.Serializable {

	// Fields----配件报价记录

	private Integer id;
	private Integer fitId;//配件
	private Float priceOut;//报价价格
	private Integer priceUnit;//报价币种
	private Integer facId;//供应商
	private Date addTime;//报价日期
	private String remark;//备注
	private String op;
	// Constructors

	/** default constructor */
	public CotPriceOut() {
	}



	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getPriceOut() {
		return this.priceOut;
	}

	public void setPriceOut(Float priceOut) {
		this.priceOut = priceOut;
	}

	public Integer getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(Integer priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Integer getFitId() {
		return fitId;
	}

	public void setFitId(Integer fitId) {
		this.fitId = fitId;
	}

	public Integer getFacId() {
		return facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}