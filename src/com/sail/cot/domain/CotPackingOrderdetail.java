package com.sail.cot.domain;

import java.util.Date;

/**
 * CotPackingOrderdetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPackingOrderdetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer packingOrderId;//采购主单编号
	private String eleId;//货号
	private String custNo;//客号
	private String eleName;//中文名
	private Double sizeL;//长
	private Double sizeW;//宽
	private Double sizeH;//高
	private Integer boxTypeId;//包材类型
	private Integer boxPackingId;//包材名称
	private Integer packCount;//数量
	private Double packPrice;//单价
	private String remark;//备注
	private Date addTime;
	private Integer orderId;//订单主单编号
	private Integer orderDetailId;//订单明细编号
	private Long outCurrent;
	private Long outRemain;
	private Integer allocateFlag;
	private Double totalAmmount;
	private Long boxObCount;
	private String boxRemark;//包装备注
	private Long outHasOut;  //已出货数量
	private String factoryShortName;
	private String rdm;
	
	private Integer sortNo;//排序编号
	
	private String packAnysIds;//存储临时的分析表编号,用","组成字符串
	
	public Long getOutHasOut() {
		if (outRemain == null) {
			outRemain = 0L;
		}
		if (packCount == null) {
			packCount = 0;
		}
		outHasOut = packCount - outRemain;
		return outHasOut;
	}
	
	// Constructors
	
	/** default constructor */
	public CotPackingOrderdetail() {
	}

	/** minimal constructor */
	public CotPackingOrderdetail(Integer id) {
		this.id = id;
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

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailId() {
		return this.orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public Integer getPackingOrderId() {
		return packingOrderId;
	}

	public void setPackingOrderId(Integer packingOrderId) {
		this.packingOrderId = packingOrderId;
	}

	public Double getTotalAmmount() {
		return totalAmmount;
	}

	public void setTotalAmmount(Double totalAmmount) {
		this.totalAmmount = totalAmmount;
	}

	public String getFactoryShortName() {
		return factoryShortName;
	}

	public void setFactoryShortName(String factoryShortName) {
		this.factoryShortName = factoryShortName;
	}

	public String getRdm() {
		return rdm;
	}

	public void setRdm(String rdm) {
		this.rdm = rdm;
	}

	public Long getBoxObCount() {
		return boxObCount;
	}

	public void setBoxObCount(Long boxObCount) {
		this.boxObCount = boxObCount;
	}

	public String getBoxRemark() {
		return boxRemark;
	}

	public void setBoxRemark(String boxRemark) {
		this.boxRemark = boxRemark;
	}

	public String getPackAnysIds() {
		return packAnysIds;
	}

	public void setPackAnysIds(String packAnysIds) {
		this.packAnysIds = packAnysIds;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

}