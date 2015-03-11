package com.sail.cot.domain;

/**
 * CotPriceFittings entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPriceFittings implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer priceDetailId;
	private String eleId;
	private String eleName;
	private String fitNo;
	private String fitName;
	private String fitDesc;
	private Integer facId;
	private String fitUseUnit;
	private Double fitUsedCount;
	private Double fitCount;
	private Double fitPrice;
	private String fitRemark;
	private Integer priceId;
	private Integer fittingId;
	private Double fitTotalPrice;
	
	private Double orderFitCount;
	private Double boxCount;

	// Constructors

	/** default constructor */
	public CotPriceFittings() {
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

	public String getEleId() {
		return this.eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getEleName() {
		return this.eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public Double getBoxCount() {
		return this.boxCount;
	}

	public void setBoxCount(Double boxCount) {
		this.boxCount = boxCount;
	}

	public String getFitNo() {
		return this.fitNo;
	}

	public void setFitNo(String fitNo) {
		this.fitNo = fitNo;
	}

	public String getFitName() {
		return this.fitName;
	}

	public void setFitName(String fitName) {
		this.fitName = fitName;
	}

	public String getFitDesc() {
		return this.fitDesc;
	}

	public void setFitDesc(String fitDesc) {
		this.fitDesc = fitDesc;
	}

	public Integer getFacId() {
		return this.facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}


	public String getFitUseUnit() {
		return fitUseUnit;
	}

	public void setFitUseUnit(String fitUseUnit) {
		this.fitUseUnit = fitUseUnit;
	}

	public Double getFitUsedCount() {
		return this.fitUsedCount;
	}

	public void setFitUsedCount(Double fitUsedCount) {
		this.fitUsedCount = fitUsedCount;
	}

	public Double getFitCount() {
		return this.fitCount;
	}

	public void setFitCount(Double fitCount) {
		this.fitCount = fitCount;
	}

	public Double getFitPrice() {
		return this.fitPrice;
	}

	public void setFitPrice(Double fitPrice) {
		this.fitPrice = fitPrice;
	}

	public Double getOrderFitCount() {
		return this.orderFitCount;
	}

	public void setOrderFitCount(Double orderFitCount) {
		this.orderFitCount = orderFitCount;
	}

	public Double getFitTotalPrice() {
		return fitTotalPrice;
	}

	public void setFitTotalPrice(Double fitTotalPrice) {
		this.fitTotalPrice = fitTotalPrice;
	}

	public String getFitRemark() {
		return fitRemark;
	}

	public void setFitRemark(String fitRemark) {
		this.fitRemark = fitRemark;
	}

	public Integer getPriceId() {
		return this.priceId;
	}

	public void setPriceId(Integer priceId) {
		this.priceId = priceId;
	}

	public Integer getFittingId() {
		return this.fittingId;
	}

	public void setFittingId(Integer fittingId) {
		this.fittingId = fittingId;
	}

}