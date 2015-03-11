package com.sail.cot.domain;

import java.util.Date;

/**
 * VOrderPackorderId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VOrderPackorderId implements java.io.Serializable {

	// Fields

	private Integer id;
	private String eleId;
	private Integer packingOrderId;
	private String custNo;
	private String eleName;
	private Double sizeL;
	private Double sizeW;
	private Double sizeH;
	private Integer boxTypeId;
	private Integer boxPackingId;
	private Integer packCount;
	private Double packPrice;
	private String remark;
	private Date addTime;
	private Integer packOrderId;
	private Long outCurrent;
	private Long outRemain;
	private Integer allocateFlag;
	private Double totalAmmount;
	private Long box0bRemark;
	private String boxRemark;
	private Integer packId;
	private String packingOrderNo;
	private Integer orderId;
	private Integer factoryId;
	
	private Long outHasOut;  //已出货数量
	private String factoryShortName;

	public Long getOutHasOut() {
		if(outRemain==null)
			outRemain=0l;
		outHasOut = packCount - outRemain;
		return outHasOut;
	}
	// Constructors

	/** default constructor */
	public VOrderPackorderId() {
	}

	/** minimal constructor */
	public VOrderPackorderId(Integer id, Integer packId, Integer orderId) {
		this.id = id;
		this.packId = packId;
		this.orderId = orderId;
	}

	/** full constructor */
	public VOrderPackorderId(Integer id, String eleId, Integer packingOrderId,
			String custNo, String eleName, Double sizeL, Double sizeW,
			Double sizeH, Integer boxTypeId, Integer boxPackingId,
			Integer packCount, Double packPrice, String remark, Date addTime,
			Integer packOrderId, Long outCurrent, Long outRemain,
			Integer allocateFlag, Double totalAmmount, Long box0bRemark,
			String boxRemark, Integer packId, String packingOrderNo,
			Integer orderId) {
		this.id = id;
		this.eleId = eleId;
		this.packingOrderId = packingOrderId;
		this.custNo = custNo;
		this.eleName = eleName;
		this.sizeL = sizeL;
		this.sizeW = sizeW;
		this.sizeH = sizeH;
		this.boxTypeId = boxTypeId;
		this.boxPackingId = boxPackingId;
		this.packCount = packCount;
		this.packPrice = packPrice;
		this.remark = remark;
		this.addTime = addTime;
		this.packOrderId = packOrderId;
		this.outCurrent = outCurrent;
		this.outRemain = outRemain;
		this.allocateFlag = allocateFlag;
		this.totalAmmount = totalAmmount;
		this.box0bRemark = box0bRemark;
		this.boxRemark = boxRemark;
		this.packId = packId;
		this.packingOrderNo = packingOrderNo;
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

	public Integer getPackingOrderId() {
		return this.packingOrderId;
	}

	public void setPackingOrderId(Integer packingOrderId) {
		this.packingOrderId = packingOrderId;
	}

	public String getCustNo() {
		return this.custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getEleName() {
		return this.eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public Double getSizeL() {
		return this.sizeL;
	}

	public void setSizeL(Double sizeL) {
		this.sizeL = sizeL;
	}

	public Double getSizeW() {
		return this.sizeW;
	}

	public void setSizeW(Double sizeW) {
		this.sizeW = sizeW;
	}

	public Double getSizeH() {
		return this.sizeH;
	}

	public void setSizeH(Double sizeH) {
		this.sizeH = sizeH;
	}

	public Integer getBoxTypeId() {
		return this.boxTypeId;
	}

	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
	}

	public Integer getBoxPackingId() {
		return this.boxPackingId;
	}

	public void setBoxPackingId(Integer boxPackingId) {
		this.boxPackingId = boxPackingId;
	}

	public Integer getPackCount() {
		return this.packCount;
	}

	public void setPackCount(Integer packCount) {
		this.packCount = packCount;
	}

	public Double getPackPrice() {
		return this.packPrice;
	}

	public void setPackPrice(Double packPrice) {
		this.packPrice = packPrice;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getPackOrderId() {
		return this.packOrderId;
	}

	public void setPackOrderId(Integer packOrderId) {
		this.packOrderId = packOrderId;
	}

	public Long getOutCurrent() {
		return this.outCurrent;
	}

	public void setOutCurrent(Long outCurrent) {
		this.outCurrent = outCurrent;
	}

	public Long getOutRemain() {
		return this.outRemain;
	}

	public void setOutRemain(Long outRemain) {
		this.outRemain = outRemain;
	}

	public Integer getAllocateFlag() {
		return this.allocateFlag;
	}

	public void setAllocateFlag(Integer allocateFlag) {
		this.allocateFlag = allocateFlag;
	}

	public Double getTotalAmmount() {
		return this.totalAmmount;
	}

	public void setTotalAmmount(Double totalAmmount) {
		this.totalAmmount = totalAmmount;
	}

	public Long getBox0bRemark() {
		return this.box0bRemark;
	}

	public void setBox0bRemark(Long box0bRemark) {
		this.box0bRemark = box0bRemark;
	}

	public String getBoxRemark() {
		return this.boxRemark;
	}

	public void setBoxRemark(String boxRemark) {
		this.boxRemark = boxRemark;
	}

	public Integer getPackId() {
		return this.packId;
	}

	public void setPackId(Integer packId) {
		this.packId = packId;
	}

	public String getPackingOrderNo() {
		return this.packingOrderNo;
	}

	public void setPackingOrderNo(String packingOrderNo) {
		this.packingOrderNo = packingOrderNo;
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
		if (!(other instanceof VOrderPackorderId))
			return false;
		VOrderPackorderId castOther = (VOrderPackorderId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getEleId() == castOther.getEleId()) || (this
						.getEleId() != null
						&& castOther.getEleId() != null && this.getEleId()
						.equals(castOther.getEleId())))
				&& ((this.getPackingOrderId() == castOther.getPackingOrderId()) || (this
						.getPackingOrderId() != null
						&& castOther.getPackingOrderId() != null && this
						.getPackingOrderId().equals(
								castOther.getPackingOrderId())))
				&& ((this.getCustNo() == castOther.getCustNo()) || (this
						.getCustNo() != null
						&& castOther.getCustNo() != null && this.getCustNo()
						.equals(castOther.getCustNo())))
				&& ((this.getEleName() == castOther.getEleName()) || (this
						.getEleName() != null
						&& castOther.getEleName() != null && this.getEleName()
						.equals(castOther.getEleName())))
				&& ((this.getSizeL() == castOther.getSizeL()) || (this
						.getSizeL() != null
						&& castOther.getSizeL() != null && this.getSizeL()
						.equals(castOther.getSizeL())))
				&& ((this.getSizeW() == castOther.getSizeW()) || (this
						.getSizeW() != null
						&& castOther.getSizeW() != null && this.getSizeW()
						.equals(castOther.getSizeW())))
				&& ((this.getSizeH() == castOther.getSizeH()) || (this
						.getSizeH() != null
						&& castOther.getSizeH() != null && this.getSizeH()
						.equals(castOther.getSizeH())))
				&& ((this.getBoxTypeId() == castOther.getBoxTypeId()) || (this
						.getBoxTypeId() != null
						&& castOther.getBoxTypeId() != null && this
						.getBoxTypeId().equals(castOther.getBoxTypeId())))
				&& ((this.getBoxPackingId() == castOther.getBoxPackingId()) || (this
						.getBoxPackingId() != null
						&& castOther.getBoxPackingId() != null && this
						.getBoxPackingId().equals(castOther.getBoxPackingId())))
				&& ((this.getPackCount() == castOther.getPackCount()) || (this
						.getPackCount() != null
						&& castOther.getPackCount() != null && this
						.getPackCount().equals(castOther.getPackCount())))
				&& ((this.getPackPrice() == castOther.getPackPrice()) || (this
						.getPackPrice() != null
						&& castOther.getPackPrice() != null && this
						.getPackPrice().equals(castOther.getPackPrice())))
				&& ((this.getRemark() == castOther.getRemark()) || (this
						.getRemark() != null
						&& castOther.getRemark() != null && this.getRemark()
						.equals(castOther.getRemark())))
				&& ((this.getAddTime() == castOther.getAddTime()) || (this
						.getAddTime() != null
						&& castOther.getAddTime() != null && this.getAddTime()
						.equals(castOther.getAddTime())))
				&& ((this.getPackOrderId() == castOther.getPackOrderId()) || (this
						.getPackOrderId() != null
						&& castOther.getPackOrderId() != null && this
						.getPackOrderId().equals(castOther.getPackOrderId())))
				&& ((this.getOutCurrent() == castOther.getOutCurrent()) || (this
						.getOutCurrent() != null
						&& castOther.getOutCurrent() != null && this
						.getOutCurrent().equals(castOther.getOutCurrent())))
				&& ((this.getOutRemain() == castOther.getOutRemain()) || (this
						.getOutRemain() != null
						&& castOther.getOutRemain() != null && this
						.getOutRemain().equals(castOther.getOutRemain())))
				&& ((this.getAllocateFlag() == castOther.getAllocateFlag()) || (this
						.getAllocateFlag() != null
						&& castOther.getAllocateFlag() != null && this
						.getAllocateFlag().equals(castOther.getAllocateFlag())))
				&& ((this.getTotalAmmount() == castOther.getTotalAmmount()) || (this
						.getTotalAmmount() != null
						&& castOther.getTotalAmmount() != null && this
						.getTotalAmmount().equals(castOther.getTotalAmmount())))
				&& ((this.getBox0bRemark() == castOther.getBox0bRemark()) || (this
						.getBox0bRemark() != null
						&& castOther.getBox0bRemark() != null && this
						.getBox0bRemark().equals(castOther.getBox0bRemark())))
				&& ((this.getBoxRemark() == castOther.getBoxRemark()) || (this
						.getBoxRemark() != null
						&& castOther.getBoxRemark() != null && this
						.getBoxRemark().equals(castOther.getBoxRemark())))
				&& ((this.getPackId() == castOther.getPackId()) || (this
						.getPackId() != null
						&& castOther.getPackId() != null && this.getPackId()
						.equals(castOther.getPackId())))
				&& ((this.getPackingOrderNo() == castOther.getPackingOrderNo()) || (this
						.getPackingOrderNo() != null
						&& castOther.getPackingOrderNo() != null && this
						.getPackingOrderNo().equals(
								castOther.getPackingOrderNo())))
				&& ((this.getOrderId() == castOther.getOrderId()) || (this
						.getOrderId() != null
						&& castOther.getOrderId() != null && this.getOrderId()
						.equals(castOther.getOrderId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getEleId() == null ? 0 : this.getEleId().hashCode());
		result = 37
				* result
				+ (getPackingOrderId() == null ? 0 : this.getPackingOrderId()
						.hashCode());
		result = 37 * result
				+ (getCustNo() == null ? 0 : this.getCustNo().hashCode());
		result = 37 * result
				+ (getEleName() == null ? 0 : this.getEleName().hashCode());
		result = 37 * result
				+ (getSizeL() == null ? 0 : this.getSizeL().hashCode());
		result = 37 * result
				+ (getSizeW() == null ? 0 : this.getSizeW().hashCode());
		result = 37 * result
				+ (getSizeH() == null ? 0 : this.getSizeH().hashCode());
		result = 37 * result
				+ (getBoxTypeId() == null ? 0 : this.getBoxTypeId().hashCode());
		result = 37
				* result
				+ (getBoxPackingId() == null ? 0 : this.getBoxPackingId()
						.hashCode());
		result = 37 * result
				+ (getPackCount() == null ? 0 : this.getPackCount().hashCode());
		result = 37 * result
				+ (getPackPrice() == null ? 0 : this.getPackPrice().hashCode());
		result = 37 * result
				+ (getRemark() == null ? 0 : this.getRemark().hashCode());
		result = 37 * result
				+ (getAddTime() == null ? 0 : this.getAddTime().hashCode());
		result = 37
				* result
				+ (getPackOrderId() == null ? 0 : this.getPackOrderId()
						.hashCode());
		result = 37
				* result
				+ (getOutCurrent() == null ? 0 : this.getOutCurrent()
						.hashCode());
		result = 37 * result
				+ (getOutRemain() == null ? 0 : this.getOutRemain().hashCode());
		result = 37
				* result
				+ (getAllocateFlag() == null ? 0 : this.getAllocateFlag()
						.hashCode());
		result = 37
				* result
				+ (getTotalAmmount() == null ? 0 : this.getTotalAmmount()
						.hashCode());
		result = 37
				* result
				+ (getBox0bRemark() == null ? 0 : this.getBox0bRemark()
						.hashCode());
		result = 37 * result
				+ (getBoxRemark() == null ? 0 : this.getBoxRemark().hashCode());
		result = 37 * result
				+ (getPackId() == null ? 0 : this.getPackId().hashCode());
		result = 37
				* result
				+ (getPackingOrderNo() == null ? 0 : this.getPackingOrderNo()
						.hashCode());
		result = 37 * result
				+ (getOrderId() == null ? 0 : this.getOrderId().hashCode());
		return result;
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

}