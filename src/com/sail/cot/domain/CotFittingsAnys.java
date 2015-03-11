package com.sail.cot.domain;

/**
 * CotFittingsAnys entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFittingsAnys implements java.io.Serializable {

	// Fields

	private Integer id;
	private String eleId;//货号
	private String eleName;//产品名称
	private Double boxCount;//订单数量
	private String fitNo;//配件号
	private String fitName;//配件名称
	private String fitDesc;//规格型号
	private Integer facId;//供应商
	private String fitBuyUnit;//采购单位
	private Double fitUsedCount;//用量
	private Double fitCount;//数量
	private Double fitPrice;//单价
	private Double orderFitCount;//订单需求数量
	private Double totalAmount;//总金额
	private String remark;//备注
	private Integer orderId;//订单编号
	private Integer orderdetailId;//订单明细编号
	private String anyFlag;//是否已生成采购单
	private Integer fittingsOrderid;//采购主单编号
	private Integer fittingsDetailid;//采购明细编号
	
	private Integer fittingId;//配件库编号
	
	private String factoryShortName;

	// Constructors

	public String getFactoryShortName() {
		return factoryShortName;
	}

	public void setFactoryShortName(String factoryShortName) {
		this.factoryShortName = factoryShortName;
	}

	/** default constructor */
	public CotFittingsAnys() {
	}

	/** minimal constructor */
	public CotFittingsAnys(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotFittingsAnys(Integer id, String eleId, String eleName,
			Double boxCount, String fitNo, String fitName, String fitDesc,
			Integer facId, String fitBuyUnit, Double fitUsedCount,Double fitCount,
			Double fitPrice, Double orderFitCount, Double totalAmount,
			String remark, Integer orderId) {
		this.id = id;
		this.eleId = eleId;
		this.eleName = eleName;
		this.boxCount = boxCount;
		this.fitNo = fitNo;
		this.fitName = fitName;
		this.fitDesc = fitDesc;
		this.facId = facId;
		this.fitBuyUnit = fitBuyUnit;
		this.fitUsedCount = fitUsedCount;
		this.fitCount = fitCount;
		this.fitPrice = fitPrice;
		this.orderFitCount = orderFitCount;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.orderId = orderId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		if(this.facId!=null && this.facId==0){
			return null;
		}
		return this.facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public String getFitBuyUnit() {
		return this.fitBuyUnit;
	}

	public void setFitBuyUnit(String fitBuyUnit) {
		this.fitBuyUnit = fitBuyUnit;
	}

	public Double getFitPrice() {
		return this.fitPrice;
	}

	public void setFitPrice(Double fitPrice) {
		this.fitPrice = fitPrice;
	}

	public Double getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Double boxCount) {
		this.boxCount = boxCount;
	}

	public Double getOrderFitCount() {
		return orderFitCount;
	}

	public void setOrderFitCount(Double orderFitCount) {
		this.orderFitCount = orderFitCount;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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

	public Integer getOrderdetailId() {
		return orderdetailId;
	}

	public void setOrderdetailId(Integer orderdetailId) {
		this.orderdetailId = orderdetailId;
	}

	public String getAnyFlag() {
		return anyFlag;
	}

	public void setAnyFlag(String anyFlag) {
		this.anyFlag = anyFlag;
	}

	public Integer getFittingsOrderid() {
		return fittingsOrderid;
	}

	public void setFittingsOrderid(Integer fittingsOrderid) {
		this.fittingsOrderid = fittingsOrderid;
	}

	public Integer getFittingId() {
		return fittingId;
	}

	public void setFittingId(Integer fittingId) {
		this.fittingId = fittingId;
	}

	public Integer getFittingsDetailid() {
		return fittingsDetailid;
	}

	public void setFittingsDetailid(Integer fittingsDetailid) {
		this.fittingsDetailid = fittingsDetailid;
	}

	public Double getFitUsedCount() {
		return fitUsedCount;
	}

	public void setFitUsedCount(Double fitUsedCount) {
		this.fitUsedCount = fitUsedCount;
	}

	public Double getFitCount() {
		return fitCount;
	}

	public void setFitCount(Double fitCount) {
		this.fitCount = fitCount;
	}

}