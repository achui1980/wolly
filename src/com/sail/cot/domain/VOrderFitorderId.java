package com.sail.cot.domain;

import java.util.Date;

/**
 * VOrderFitorderId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VOrderFitorderId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String fitNo;
	private String fitName;
	private String buyUnit;
	private String useUnit;
	private Double fitTrans;
	private Double fitPrice;
	private String fitQualityStander;
	private String fitDesc;
	private String fitRemark;
	private Integer facId;
	private Integer typeLv3Id;
	private Date addTime;
	private Double requeirCount;
	private Double orderCount;
	private Double totalAmmount;
	private Integer fitOrderId;
	private String eleId;
	private Integer fitId;
	private Integer orderId;
	private String fittingOrderNo;
	
	private Double outRemain;
	private Double outCurrent;
	private Double outHasOut;  //已出货数量
	private String factoryShortName;
	private Integer allocateFlag;//是否已指定数量 0：未指定 1：已指定
	
	public Double getOutHasOut() {
		if(outRemain==null)
			outRemain=0.0;
		outHasOut = orderCount - outRemain;
		return outHasOut;
	}
	// Constructors
	
	/** default constructor */
	public VOrderFitorderId() {
	}

	/** minimal constructor */
	public VOrderFitorderId(Integer id, Integer fitId, Integer orderId) {
		this.id = id;
		this.fitId = fitId;
		this.orderId = orderId;
	}

	/** full constructor */
	public VOrderFitorderId(Integer id, String fitNo, String fitName,
			String buyUnit, String useUnit, Double fitTrans, Double fitPrice,
			String fitQualityStander, String fitDesc, String fitRemark,
			Integer facId, Integer typeLv3Id, Date addTime,
			Double requeirCount, Double orderCount, Double totalAmmount,
			Integer fitOrderId, String eleId, Integer fitId, Integer orderId) {
		this.id = id;
		this.fitNo = fitNo;
		this.fitName = fitName;
		this.buyUnit = buyUnit;
		this.useUnit = useUnit;
		this.fitTrans = fitTrans;
		this.fitPrice = fitPrice;
		this.fitQualityStander = fitQualityStander;
		this.fitDesc = fitDesc;
		this.fitRemark = fitRemark;
		this.facId = facId;
		this.typeLv3Id = typeLv3Id;
		this.addTime = addTime;
		this.requeirCount = requeirCount;
		this.orderCount = orderCount;
		this.totalAmmount = totalAmmount;
		this.fitOrderId = fitOrderId;
		this.eleId = eleId;
		this.fitId = fitId;
		this.orderId = orderId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getBuyUnit() {
		return this.buyUnit;
	}

	public void setBuyUnit(String buyUnit) {
		this.buyUnit = buyUnit;
	}

	public String getUseUnit() {
		return this.useUnit;
	}

	public void setUseUnit(String useUnit) {
		this.useUnit = useUnit;
	}

	public Double getFitTrans() {
		return this.fitTrans;
	}

	public void setFitTrans(Double fitTrans) {
		this.fitTrans = fitTrans;
	}

	public Double getFitPrice() {
		return this.fitPrice;
	}

	public void setFitPrice(Double fitPrice) {
		this.fitPrice = fitPrice;
	}

	public String getFitQualityStander() {
		return this.fitQualityStander;
	}

	public void setFitQualityStander(String fitQualityStander) {
		this.fitQualityStander = fitQualityStander;
	}

	public String getFitDesc() {
		return this.fitDesc;
	}

	public void setFitDesc(String fitDesc) {
		this.fitDesc = fitDesc;
	}

	public String getFitRemark() {
		return this.fitRemark;
	}

	public void setFitRemark(String fitRemark) {
		this.fitRemark = fitRemark;
	}

	public Integer getFacId() {
		return this.facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public Integer getTypeLv3Id() {
		return this.typeLv3Id;
	}

	public void setTypeLv3Id(Integer typeLv3Id) {
		this.typeLv3Id = typeLv3Id;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Double getRequeirCount() {
		return this.requeirCount;
	}

	public void setRequeirCount(Double requeirCount) {
		this.requeirCount = requeirCount;
	}

	public Double getOrderCount() {
		return this.orderCount;
	}

	public void setOrderCount(Double orderCount) {
		this.orderCount = orderCount;
	}

	public Double getTotalAmmount() {
		return this.totalAmmount;
	}

	public void setTotalAmmount(Double totalAmmount) {
		this.totalAmmount = totalAmmount;
	}

	public Integer getFitOrderId() {
		return this.fitOrderId;
	}

	public void setFitOrderId(Integer fitOrderId) {
		this.fitOrderId = fitOrderId;
	}

	public String getEleId() {
		return this.eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public Integer getFitId() {
		return this.fitId;
	}

	public void setFitId(Integer fitId) {
		this.fitId = fitId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VOrderFitorderId))
			return false;
		VOrderFitorderId castOther = (VOrderFitorderId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getFitNo() == castOther.getFitNo()) || (this
						.getFitNo() != null
						&& castOther.getFitNo() != null && this.getFitNo()
						.equals(castOther.getFitNo())))
				&& ((this.getFitName() == castOther.getFitName()) || (this
						.getFitName() != null
						&& castOther.getFitName() != null && this.getFitName()
						.equals(castOther.getFitName())))
				&& ((this.getBuyUnit() == castOther.getBuyUnit()) || (this
						.getBuyUnit() != null
						&& castOther.getBuyUnit() != null && this.getBuyUnit()
						.equals(castOther.getBuyUnit())))
				&& ((this.getUseUnit() == castOther.getUseUnit()) || (this
						.getUseUnit() != null
						&& castOther.getUseUnit() != null && this.getUseUnit()
						.equals(castOther.getUseUnit())))
				&& ((this.getFitTrans() == castOther.getFitTrans()) || (this
						.getFitTrans() != null
						&& castOther.getFitTrans() != null && this
						.getFitTrans().equals(castOther.getFitTrans())))
				&& ((this.getFitPrice() == castOther.getFitPrice()) || (this
						.getFitPrice() != null
						&& castOther.getFitPrice() != null && this
						.getFitPrice().equals(castOther.getFitPrice())))
				&& ((this.getFitQualityStander() == castOther
						.getFitQualityStander()) || (this
						.getFitQualityStander() != null
						&& castOther.getFitQualityStander() != null && this
						.getFitQualityStander().equals(
								castOther.getFitQualityStander())))
				&& ((this.getFitDesc() == castOther.getFitDesc()) || (this
						.getFitDesc() != null
						&& castOther.getFitDesc() != null && this.getFitDesc()
						.equals(castOther.getFitDesc())))
				&& ((this.getFitRemark() == castOther.getFitRemark()) || (this
						.getFitRemark() != null
						&& castOther.getFitRemark() != null && this
						.getFitRemark().equals(castOther.getFitRemark())))
				&& ((this.getFacId() == castOther.getFacId()) || (this
						.getFacId() != null
						&& castOther.getFacId() != null && this.getFacId()
						.equals(castOther.getFacId())))
				&& ((this.getTypeLv3Id() == castOther.getTypeLv3Id()) || (this
						.getTypeLv3Id() != null
						&& castOther.getTypeLv3Id() != null && this
						.getTypeLv3Id().equals(castOther.getTypeLv3Id())))
				&& ((this.getAddTime() == castOther.getAddTime()) || (this
						.getAddTime() != null
						&& castOther.getAddTime() != null && this.getAddTime()
						.equals(castOther.getAddTime())))
				&& ((this.getRequeirCount() == castOther.getRequeirCount()) || (this
						.getRequeirCount() != null
						&& castOther.getRequeirCount() != null && this
						.getRequeirCount().equals(castOther.getRequeirCount())))
				&& ((this.getOrderCount() == castOther.getOrderCount()) || (this
						.getOrderCount() != null
						&& castOther.getOrderCount() != null && this
						.getOrderCount().equals(castOther.getOrderCount())))
				&& ((this.getTotalAmmount() == castOther.getTotalAmmount()) || (this
						.getTotalAmmount() != null
						&& castOther.getTotalAmmount() != null && this
						.getTotalAmmount().equals(castOther.getTotalAmmount())))
				&& ((this.getFitOrderId() == castOther.getFitOrderId()) || (this
						.getFitOrderId() != null
						&& castOther.getFitOrderId() != null && this
						.getFitOrderId().equals(castOther.getFitOrderId())))
				&& ((this.getEleId() == castOther.getEleId()) || (this
						.getEleId() != null
						&& castOther.getEleId() != null && this.getEleId()
						.equals(castOther.getEleId())))
				&& ((this.getFitId() == castOther.getFitId()) || (this
						.getFitId() != null
						&& castOther.getFitId() != null && this.getFitId()
						.equals(castOther.getFitId())))
				&& ((this.getOrderId() == castOther.getOrderId()) || (this
						.getOrderId() != null
						&& castOther.getOrderId() != null && this.getOrderId()
						.equals(castOther.getOrderId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getFitNo() == null ? 0 : this.getFitNo().hashCode());
		result = 37 * result
				+ (getFitName() == null ? 0 : this.getFitName().hashCode());
		result = 37 * result
				+ (getBuyUnit() == null ? 0 : this.getBuyUnit().hashCode());
		result = 37 * result
				+ (getUseUnit() == null ? 0 : this.getUseUnit().hashCode());
		result = 37 * result
				+ (getFitTrans() == null ? 0 : this.getFitTrans().hashCode());
		result = 37 * result
				+ (getFitPrice() == null ? 0 : this.getFitPrice().hashCode());
		result = 37
				* result
				+ (getFitQualityStander() == null ? 0 : this
						.getFitQualityStander().hashCode());
		result = 37 * result
				+ (getFitDesc() == null ? 0 : this.getFitDesc().hashCode());
		result = 37 * result
				+ (getFitRemark() == null ? 0 : this.getFitRemark().hashCode());
		result = 37 * result
				+ (getFacId() == null ? 0 : this.getFacId().hashCode());
		result = 37 * result
				+ (getTypeLv3Id() == null ? 0 : this.getTypeLv3Id().hashCode());
		result = 37 * result
				+ (getAddTime() == null ? 0 : this.getAddTime().hashCode());
		result = 37
				* result
				+ (getRequeirCount() == null ? 0 : this.getRequeirCount()
						.hashCode());
		result = 37
				* result
				+ (getOrderCount() == null ? 0 : this.getOrderCount()
						.hashCode());
		result = 37
				* result
				+ (getTotalAmmount() == null ? 0 : this.getTotalAmmount()
						.hashCode());
		result = 37
				* result
				+ (getFitOrderId() == null ? 0 : this.getFitOrderId()
						.hashCode());
		result = 37 * result
				+ (getEleId() == null ? 0 : this.getEleId().hashCode());
		result = 37 * result
				+ (getFitId() == null ? 0 : this.getFitId().hashCode());
		result = 37 * result
				+ (getOrderId() == null ? 0 : this.getOrderId().hashCode());
		return result;
	}

	public String getFittingOrderNo() {
		return fittingOrderNo;
	}

	public void setFittingOrderNo(String fittingOrderNo) {
		this.fittingOrderNo = fittingOrderNo;
	}

	public Double getOutRemain() {
		return outRemain;
	}

	public void setOutRemain(Double outRemain) {
		this.outRemain = outRemain;
	}

	public Double getOutCurrent() {
		return outCurrent;
	}

	public void setOutCurrent(Double outCurrent) {
		this.outCurrent = outCurrent;
	}

	public Integer getAllocateFlag() {
		return allocateFlag;
	}

	public void setAllocateFlag(Integer allocateFlag) {
		this.allocateFlag = allocateFlag;
	}

	public String getFactoryShortName() {
		return factoryShortName;
	}

	public void setFactoryShortName(String factoryShortName) {
		this.factoryShortName = factoryShortName;
	}

}