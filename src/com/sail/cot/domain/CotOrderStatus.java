package com.sail.cot.domain;

import java.util.Date;

import javax.persistence.Column;

/**
 * CotOrderStatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderStatus implements java.io.Serializable {

	// Fields

	private Integer id;
	private String stateFlag;//内部标识---SIMPLE_SAMPLE:小样确认 
//										COMPLETE_SAMPLE:完整样确认 
//										FAC:发至供应商 
//										PCAKET:包装交货 
//										SAMPLE_PIC:照片样 
//										SAMPLE_OUT:出货确认样 
//										QC:验货日期 
//										SHIPPING:订舱检查
//										LOADING:装柜通知
	private String flagName;//英文名称
	private Date planDate;//计划日期
	private Date comleteDate;//完成日期
	private Integer orderId;//订单编号
	private Integer orderFacId;//生产合同编号
	private String groupType;//组类型---SAMPLE:产前样 PACKAGE:包装确认 SAMPLEOUT:出货样 QC:QC OUT:出货
	private String orderType;//来源 ORDER-订单，ORDERFAC-生产合同
	private String remark;

	// Constructors

	/** default constructor */
	public CotOrderStatus() {
	}

	/** full constructor */
	public CotOrderStatus(String stateFlag, String flagName, Date planDate, Date comleteDate, Integer orderId, Integer orderFacId, String groupType) {
		this.stateFlag = stateFlag;
		this.flagName = flagName;
		this.planDate = planDate;
		this.comleteDate = comleteDate;
		this.orderId = orderId;
		this.orderFacId = orderFacId;
		this.groupType = groupType;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStateFlag() {
		return this.stateFlag;
	}

	public void setStateFlag(String stateFlag) {
		this.stateFlag = stateFlag;
	}

	public String getFlagName() {
		return this.flagName;
	}

	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}

	public Date getPlanDate() {
		return this.planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public Date getComleteDate() {
		return this.comleteDate;
	}

	public void setComleteDate(Date comleteDate) {
		this.comleteDate = comleteDate;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderFacId() {
		return this.orderFacId;
	}

	public void setOrderFacId(Integer orderFacId) {
		this.orderFacId = orderFacId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}