package com.sail.cot.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotFittingsOrderdetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFittingsOrderdetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private String fitNo;
	private String fitName;
	private String buyUnit;
	private String useUnit;
	private Double fitTrans;
	private Double fitPrice;
	private Long fitMinCount;
	private String fitQualityStander;
	private String fitDesc;
	private String fitRemark;
	private Integer facId;
	private Integer typeLv3Id;
	private Date addTime;
	private Double requeirCount;
	private Double orderCount;
	private Double totalAmmount;
	private String eleId;
	private Integer orderDetailId;
	
	private Double outRemain;
	private Double outCurrent;
	private Double outHasOut;  //已出货数量
	private String factoryShortName;
	private Integer allocateFlag;//是否已指定数量 0：未指定 1：已指定
	private String rdm;
	
	private Integer sortNo;
	
	
	public Integer getSortNo() {
		return sortNo;
	}


	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}


	public Double getOutHasOut() {
		if (outRemain == null) {
			outRemain = 0.0;
		}
		if (orderCount == null) {
			orderCount = 0.0;
		}
		outHasOut = orderCount - outRemain;
		return outHasOut;
	}
	
	private String type;
	
	private String fitAnysIds;//存储临时的分析表编号,用","组成字符串

	// Constructors

	/** default constructor */
	public CotFittingsOrderdetail() {
	}

	
	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}


	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public Long getFitMinCount() {
		return this.fitMinCount;
	}

	public void setFitMinCount(Long fitMinCount) {
		this.fitMinCount = fitMinCount;
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

	public String getFitAnysIds() {
		return fitAnysIds;
	}


	public void setFitAnysIds(String fitAnysIds) {
		this.fitAnysIds = fitAnysIds;
	}


	public String getEleId() {
		return eleId;
	}


	public void setEleId(String eleId) {
		this.eleId = eleId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getOrderDetailId() {
		return orderDetailId;
	}


	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
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


	public String getRdm() {
		return rdm;
	}


	public void setRdm(String rdm) {
		this.rdm = rdm;
	}

}