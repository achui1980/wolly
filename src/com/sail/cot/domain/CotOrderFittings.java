package com.sail.cot.domain;

/**
 * CotOrderFittings entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderFittings implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderDetailId;
	private String eleId;
	private String eleName;
	private Double boxCount;
	private String fitNo;//材料编号
	private String fitName;//配件名称
	private String fitDesc;//规格描述
	private Integer facId;//供应商(厂家)
	private String fitUseUnit;//单位
	private Double fitUsedCount;//用量
	private Double fitCount;//数量
	private Double fitPrice;//单价
	private Double orderFitCount;
	private Double fitTotalPrice;
	private String fitRemark;//备注
	private Integer orderId;
	private Integer fittingId;

	// Constructors

	/** default constructor */
	public CotOrderFittings() {
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

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getFittingId() {
		return this.fittingId;
	}

	public void setFittingId(Integer fittingId) {
		this.fittingId = fittingId;
	}

}