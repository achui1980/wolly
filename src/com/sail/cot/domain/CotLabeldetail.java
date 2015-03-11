package com.sail.cot.domain;

import java.util.Date;

/**
 * CotLabeldetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotLabeldetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer labelId;
	private Integer orderdetailId;
	private Long labelIb;
	private Long labelProd;
	private Long labelMb;
	private Long labelOb;
	private Double amountIb;
	private Double amountProd;
	private Double amountOb;
	private Double amountMb;
	private Date addTime;
	
	private String eleId;
	private String custNo;
	private String barcode;
	private Integer factoryId;
	private String factoryShortName;

	// Constructors

	/** default constructor */
	public CotLabeldetail() {
	}

	/** full constructor */
	public CotLabeldetail(CotLabel cotLabel, Integer orderdetailId,
			Long labelIb, Long labelProd, Long labelMb, Long labelOb,
			Double amountIb, Double amountProd, Double amountOb,
			Double amountMb, Date addTime) {

		this.orderdetailId = orderdetailId;
		this.labelIb = labelIb;
		this.labelProd = labelProd;
		this.labelMb = labelMb;
		this.labelOb = labelOb;
		this.amountIb = amountIb;
		this.amountProd = amountProd;
		this.amountOb = amountOb;
		this.amountMb = amountMb;
		this.addTime = addTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLabelId() {
		return labelId;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public Integer getOrderdetailId() {
		return orderdetailId;
	}

	public void setOrderdetailId(Integer orderdetailId) {
		this.orderdetailId = orderdetailId;
	}

	public Long getLabelIb() {
		return this.labelIb;
	}

	public void setLabelIb(Long labelIb) {
		this.labelIb = labelIb;
	}

	public Long getLabelProd() {
		return this.labelProd;
	}

	public void setLabelProd(Long labelProd) {
		this.labelProd = labelProd;
	}

	public Long getLabelMb() {
		return this.labelMb;
	}

	public void setLabelMb(Long labelMb) {
		this.labelMb = labelMb;
	}

	public Long getLabelOb() {
		return this.labelOb;
	}

	public void setLabelOb(Long labelOb) {
		this.labelOb = labelOb;
	}

	public Double getAmountIb() {
		return this.amountIb;
	}

	public void setAmountIb(Double amountIb) {
		this.amountIb = amountIb;
	}

	public Double getAmountProd() {
		return this.amountProd;
	}

	public void setAmountProd(Double amountProd) {
		this.amountProd = amountProd;
	}

	public Double getAmountOb() {
		return this.amountOb;
	}

	public void setAmountOb(Double amountOb) {
		this.amountOb = amountOb;
	}

	public Double getAmountMb() {
		return this.amountMb;
	}

	public void setAmountMb(Double amountMb) {
		this.amountMb = amountMb;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryShortName() {
		return factoryShortName;
	}

	public void setFactoryShortName(String factoryShortName) {
		this.factoryShortName = factoryShortName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}