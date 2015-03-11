package com.sail.cot.domain;

import java.util.Date;

/**
 * VOrderOrderfacId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VOrderOrderfacId implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private String orderNo; //采购合同单号
	private Date orderTime;
	private Date sendTime;
	private String orderAddress;
	private String givenAddress;
	private Date addTime;
	private Integer businessPerson;
	private Long orderStatus;
	private String checkReason;
	private Long orderIscheck;
	private String orderCm;
	private String orderNm;
	private String orderZhm;
	private String orderMb;
	private Double totalMoney;
	private Integer totalCount;
	private Integer totalContainer;
	private Double totalCbm;
	private Integer currencyId;
	private Integer shipportId;
	private Integer targetportId;
	private Integer trafficId;
	private Integer companyId;
	private Integer factoryId;
	private String orderNumber;
	private String orderRemark;
	private String orderZm;
	private String orderClause;
	private Integer orderDetailid;
	private String recvPerson;
	private Integer orderId;
	private String eleId;
	private String factoryNo;
	private Long boxCount;
	private Long outRemain;
	private Long outCurrent;
	private Long outHasOut;  //已出货数量
	private String factoryName;
	private Integer orderfacDetailId;//采购明细id
	private Integer allocateFlag;//是否已指定数量 0：未指定 1：已指定
	private Integer orderFacId;
	private Integer custId;
	private String poNo;
	private String orderNoOrd;
	private Float priceFac;
	
	// Constructors

	public Float getPriceFac() {
		return priceFac;
	}

	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Long getOutHasOut() {
		if(outRemain==null)
			outRemain=0l;
		outHasOut = boxCount - outRemain;
		return outHasOut;
	}
	
	public String getFactoryName(){
		return this.factoryName;
	}
	public void setFactoryName(String factoryName){
		this.factoryName = factoryName;
	}
	
	/** default constructor */
	public VOrderOrderfacId() {
	}

	/** minimal constructor */
	public VOrderOrderfacId(Integer id, Integer orderId) {
		this.id = id;
		this.orderId = orderId;
	}

	/** full constructor */
	public VOrderOrderfacId(Integer id, Integer empId, String orderNo,
			Date orderTime, Date sendTime, String orderAddress,
			String givenAddress, Date addTime, Integer businessPerson,
			Long orderStatus, String checkReason, Long orderIscheck,
			String orderCm, String orderNm, String orderZhm, String orderMb,
			Double totalMoney, Integer totalCount, Integer totalContainer,
			Double totalCbm, Integer currencyId, Integer shipportId,
			Integer targetportId, Integer trafficId, Integer companyId,
			Integer factoryId, String orderNumber, String orderRemark,
			String orderZm, String orderClause, String orderMbImg,
			String recvPerson, Integer orderId) {
		this.id = id;
		this.empId = empId;
		this.orderNo = orderNo;
		this.orderTime = orderTime;
		this.sendTime = sendTime;
		this.orderAddress = orderAddress;
		this.givenAddress = givenAddress;
		this.addTime = addTime;
		this.businessPerson = businessPerson;
		this.orderStatus = orderStatus;
		this.checkReason = checkReason;
		this.orderIscheck = orderIscheck;
		this.orderCm = orderCm;
		this.orderNm = orderNm;
		this.orderZhm = orderZhm;
		this.orderMb = orderMb;
		this.totalMoney = totalMoney;
		this.totalCount = totalCount;
		this.totalContainer = totalContainer;
		this.totalCbm = totalCbm;
		this.currencyId = currencyId;
		this.shipportId = shipportId;
		this.targetportId = targetportId;
		this.trafficId = trafficId;
		this.companyId = companyId;
		this.factoryId = factoryId;
		this.orderNumber = orderNumber;
		this.orderRemark = orderRemark;
		this.orderZm = orderZm;
		this.orderClause = orderClause;
		this.recvPerson = recvPerson;
		this.orderId = orderId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderTime() {
		return this.orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getOrderAddress() {
		return this.orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getGivenAddress() {
		return this.givenAddress;
	}

	public void setGivenAddress(String givenAddress) {
		this.givenAddress = givenAddress;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getBusinessPerson() {
		return this.businessPerson;
	}

	public void setBusinessPerson(Integer businessPerson) {
		this.businessPerson = businessPerson;
	}

	public Long getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(Long orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCheckReason() {
		return this.checkReason;
	}

	public void setCheckReason(String checkReason) {
		this.checkReason = checkReason;
	}

	public Long getOrderIscheck() {
		return this.orderIscheck;
	}

	public void setOrderIscheck(Long orderIscheck) {
		this.orderIscheck = orderIscheck;
	}

	public String getOrderCm() {
		return this.orderCm;
	}

	public void setOrderCm(String orderCm) {
		this.orderCm = orderCm;
	}

	public String getOrderNm() {
		return this.orderNm;
	}

	public void setOrderNm(String orderNm) {
		this.orderNm = orderNm;
	}

	public String getOrderZhm() {
		return this.orderZhm;
	}

	public void setOrderZhm(String orderZhm) {
		this.orderZhm = orderZhm;
	}

	public String getOrderMb() {
		return this.orderMb;
	}

	public void setOrderMb(String orderMb) {
		this.orderMb = orderMb;
	}

	public Double getTotalMoney() {
		return this.totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalContainer() {
		return this.totalContainer;
	}

	public void setTotalContainer(Integer totalContainer) {
		this.totalContainer = totalContainer;
	}

	public Double getTotalCbm() {
		return this.totalCbm;
	}

	public void setTotalCbm(Double totalCbm) {
		this.totalCbm = totalCbm;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getShipportId() {
		return this.shipportId;
	}

	public void setShipportId(Integer shipportId) {
		this.shipportId = shipportId;
	}

	public Integer getTargetportId() {
		return this.targetportId;
	}

	public void setTargetportId(Integer targetportId) {
		this.targetportId = targetportId;
	}

	public Integer getTrafficId() {
		return this.trafficId;
	}

	public void setTrafficId(Integer trafficId) {
		this.trafficId = trafficId;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public String getOrderNumber() {
		return this.orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderRemark() {
		return this.orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public String getOrderZm() {
		return this.orderZm;
	}

	public void setOrderZm(String orderZm) {
		this.orderZm = orderZm;
	}

	public String getOrderClause() {
		return this.orderClause;
	}

	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}

	

	public Integer getOrderDetailid() {
		return orderDetailid;
	}

	public void setOrderDetailid(Integer orderDetailid) {
		this.orderDetailid = orderDetailid;
	}

	public String getRecvPerson() {
		return this.recvPerson;
	}

	public void setRecvPerson(String recvPerson) {
		this.recvPerson = recvPerson;
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
		if (!(other instanceof VOrderOrderfacId))
			return false;
		VOrderOrderfacId castOther = (VOrderOrderfacId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getEmpId() == castOther.getEmpId()) || (this
						.getEmpId() != null
						&& castOther.getEmpId() != null && this.getEmpId()
						.equals(castOther.getEmpId())))
				&& ((this.getOrderNo() == castOther.getOrderNo()) || (this
						.getOrderNo() != null
						&& castOther.getOrderNo() != null && this.getOrderNo()
						.equals(castOther.getOrderNo())))
				&& ((this.getOrderTime() == castOther.getOrderTime()) || (this
						.getOrderTime() != null
						&& castOther.getOrderTime() != null && this
						.getOrderTime().equals(castOther.getOrderTime())))
				&& ((this.getSendTime() == castOther.getSendTime()) || (this
						.getSendTime() != null
						&& castOther.getSendTime() != null && this
						.getSendTime().equals(castOther.getSendTime())))
				&& ((this.getOrderAddress() == castOther.getOrderAddress()) || (this
						.getOrderAddress() != null
						&& castOther.getOrderAddress() != null && this
						.getOrderAddress().equals(castOther.getOrderAddress())))
				&& ((this.getGivenAddress() == castOther.getGivenAddress()) || (this
						.getGivenAddress() != null
						&& castOther.getGivenAddress() != null && this
						.getGivenAddress().equals(castOther.getGivenAddress())))
				&& ((this.getAddTime() == castOther.getAddTime()) || (this
						.getAddTime() != null
						&& castOther.getAddTime() != null && this.getAddTime()
						.equals(castOther.getAddTime())))
				&& ((this.getBusinessPerson() == castOther.getBusinessPerson()) || (this
						.getBusinessPerson() != null
						&& castOther.getBusinessPerson() != null && this
						.getBusinessPerson().equals(
								castOther.getBusinessPerson())))
				&& ((this.getOrderStatus() == castOther.getOrderStatus()) || (this
						.getOrderStatus() != null
						&& castOther.getOrderStatus() != null && this
						.getOrderStatus().equals(castOther.getOrderStatus())))
				&& ((this.getCheckReason() == castOther.getCheckReason()) || (this
						.getCheckReason() != null
						&& castOther.getCheckReason() != null && this
						.getCheckReason().equals(castOther.getCheckReason())))
				&& ((this.getOrderIscheck() == castOther.getOrderIscheck()) || (this
						.getOrderIscheck() != null
						&& castOther.getOrderIscheck() != null && this
						.getOrderIscheck().equals(castOther.getOrderIscheck())))
				&& ((this.getOrderCm() == castOther.getOrderCm()) || (this
						.getOrderCm() != null
						&& castOther.getOrderCm() != null && this.getOrderCm()
						.equals(castOther.getOrderCm())))
				&& ((this.getOrderNm() == castOther.getOrderNm()) || (this
						.getOrderNm() != null
						&& castOther.getOrderNm() != null && this.getOrderNm()
						.equals(castOther.getOrderNm())))
				&& ((this.getOrderZhm() == castOther.getOrderZhm()) || (this
						.getOrderZhm() != null
						&& castOther.getOrderZhm() != null && this
						.getOrderZhm().equals(castOther.getOrderZhm())))
				&& ((this.getOrderMb() == castOther.getOrderMb()) || (this
						.getOrderMb() != null
						&& castOther.getOrderMb() != null && this.getOrderMb()
						.equals(castOther.getOrderMb())))
				&& ((this.getTotalMoney() == castOther.getTotalMoney()) || (this
						.getTotalMoney() != null
						&& castOther.getTotalMoney() != null && this
						.getTotalMoney().equals(castOther.getTotalMoney())))
				&& ((this.getTotalCount() == castOther.getTotalCount()) || (this
						.getTotalCount() != null
						&& castOther.getTotalCount() != null && this
						.getTotalCount().equals(castOther.getTotalCount())))
				&& ((this.getTotalContainer() == castOther.getTotalContainer()) || (this
						.getTotalContainer() != null
						&& castOther.getTotalContainer() != null && this
						.getTotalContainer().equals(
								castOther.getTotalContainer())))
				&& ((this.getTotalCbm() == castOther.getTotalCbm()) || (this
						.getTotalCbm() != null
						&& castOther.getTotalCbm() != null && this
						.getTotalCbm().equals(castOther.getTotalCbm())))
				&& ((this.getCurrencyId() == castOther.getCurrencyId()) || (this
						.getCurrencyId() != null
						&& castOther.getCurrencyId() != null && this
						.getCurrencyId().equals(castOther.getCurrencyId())))
				&& ((this.getShipportId() == castOther.getShipportId()) || (this
						.getShipportId() != null
						&& castOther.getShipportId() != null && this
						.getShipportId().equals(castOther.getShipportId())))
				&& ((this.getTargetportId() == castOther.getTargetportId()) || (this
						.getTargetportId() != null
						&& castOther.getTargetportId() != null && this
						.getTargetportId().equals(castOther.getTargetportId())))
				&& ((this.getTrafficId() == castOther.getTrafficId()) || (this
						.getTrafficId() != null
						&& castOther.getTrafficId() != null && this
						.getTrafficId().equals(castOther.getTrafficId())))
				&& ((this.getCompanyId() == castOther.getCompanyId()) || (this
						.getCompanyId() != null
						&& castOther.getCompanyId() != null && this
						.getCompanyId().equals(castOther.getCompanyId())))
				&& ((this.getFactoryId() == castOther.getFactoryId()) || (this
						.getFactoryId() != null
						&& castOther.getFactoryId() != null && this
						.getFactoryId().equals(castOther.getFactoryId())))
				&& ((this.getOrderNumber() == castOther.getOrderNumber()) || (this
						.getOrderNumber() != null
						&& castOther.getOrderNumber() != null && this
						.getOrderNumber().equals(castOther.getOrderNumber())))
				&& ((this.getOrderRemark() == castOther.getOrderRemark()) || (this
						.getOrderRemark() != null
						&& castOther.getOrderRemark() != null && this
						.getOrderRemark().equals(castOther.getOrderRemark())))
				&& ((this.getOrderZm() == castOther.getOrderZm()) || (this
						.getOrderZm() != null
						&& castOther.getOrderZm() != null && this.getOrderZm()
						.equals(castOther.getOrderZm())))
				&& ((this.getOrderClause() == castOther.getOrderClause()) || (this
						.getOrderClause() != null
						&& castOther.getOrderClause() != null && this
						.getOrderClause().equals(castOther.getOrderClause())))
				&& ((this.getOrderDetailid() == castOther.getOrderDetailid()) || (this
						.getOrderDetailid() != null
						&& castOther.getOrderDetailid() != null && this
						.getOrderDetailid().equals(castOther.getOrderDetailid())))
				&& ((this.getRecvPerson() == castOther.getRecvPerson()) || (this
						.getRecvPerson() != null
						&& castOther.getRecvPerson() != null && this
						.getRecvPerson().equals(castOther.getRecvPerson())))
				&& ((this.getOrderId() == castOther.getOrderId()) || (this
						.getOrderId() != null
						&& castOther.getOrderId() != null && this.getOrderId()
						.equals(castOther.getOrderId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getEmpId() == null ? 0 : this.getEmpId().hashCode());
		result = 37 * result
				+ (getOrderNo() == null ? 0 : this.getOrderNo().hashCode());
		result = 37 * result
				+ (getOrderTime() == null ? 0 : this.getOrderTime().hashCode());
		result = 37 * result
				+ (getSendTime() == null ? 0 : this.getSendTime().hashCode());
		result = 37
				* result
				+ (getOrderAddress() == null ? 0 : this.getOrderAddress()
						.hashCode());
		result = 37
				* result
				+ (getGivenAddress() == null ? 0 : this.getGivenAddress()
						.hashCode());
		result = 37 * result
				+ (getAddTime() == null ? 0 : this.getAddTime().hashCode());
		result = 37
				* result
				+ (getBusinessPerson() == null ? 0 : this.getBusinessPerson()
						.hashCode());
		result = 37
				* result
				+ (getOrderStatus() == null ? 0 : this.getOrderStatus()
						.hashCode());
		result = 37
				* result
				+ (getCheckReason() == null ? 0 : this.getCheckReason()
						.hashCode());
		result = 37
				* result
				+ (getOrderIscheck() == null ? 0 : this.getOrderIscheck()
						.hashCode());
		result = 37 * result
				+ (getOrderCm() == null ? 0 : this.getOrderCm().hashCode());
		result = 37 * result
				+ (getOrderNm() == null ? 0 : this.getOrderNm().hashCode());
		result = 37 * result
				+ (getOrderZhm() == null ? 0 : this.getOrderZhm().hashCode());
		result = 37 * result
				+ (getOrderMb() == null ? 0 : this.getOrderMb().hashCode());
		result = 37
				* result
				+ (getTotalMoney() == null ? 0 : this.getTotalMoney()
						.hashCode());
		result = 37
				* result
				+ (getTotalCount() == null ? 0 : this.getTotalCount()
						.hashCode());
		result = 37
				* result
				+ (getTotalContainer() == null ? 0 : this.getTotalContainer()
						.hashCode());
		result = 37 * result
				+ (getTotalCbm() == null ? 0 : this.getTotalCbm().hashCode());
		result = 37
				* result
				+ (getCurrencyId() == null ? 0 : this.getCurrencyId()
						.hashCode());
		result = 37
				* result
				+ (getShipportId() == null ? 0 : this.getShipportId()
						.hashCode());
		result = 37
				* result
				+ (getTargetportId() == null ? 0 : this.getTargetportId()
						.hashCode());
		result = 37 * result
				+ (getTrafficId() == null ? 0 : this.getTrafficId().hashCode());
		result = 37 * result
				+ (getCompanyId() == null ? 0 : this.getCompanyId().hashCode());
		result = 37 * result
				+ (getFactoryId() == null ? 0 : this.getFactoryId().hashCode());
		result = 37
				* result
				+ (getOrderNumber() == null ? 0 : this.getOrderNumber()
						.hashCode());
		result = 37
				* result
				+ (getOrderRemark() == null ? 0 : this.getOrderRemark()
						.hashCode());
		result = 37 * result
				+ (getOrderZm() == null ? 0 : this.getOrderZm().hashCode());
		result = 37
				* result
				+ (getOrderClause() == null ? 0 : this.getOrderClause()
						.hashCode());
		result = 37
				* result
				+ (getOrderDetailid() == null ? 0 : this.getOrderDetailid()
						.hashCode());
		result = 37
				* result
				+ (getRecvPerson() == null ? 0 : this.getRecvPerson()
						.hashCode());
		result = 37 * result
				+ (getOrderId() == null ? 0 : this.getOrderId().hashCode());
		return result;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getFactoryNo() {
		return factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public Long getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Long boxCount) {
		this.boxCount = boxCount;
	}

	public Long getOutRemain() {
		return outRemain;
	}

	public void setOutRemain(Long outRemain) {
		this.outRemain = outRemain;
	}

	public Long getOutCurrent() {
		return outCurrent;
	}

	public void setOutCurrent(Long outCurrent) {
		this.outCurrent = outCurrent;
	}

	public Integer getOrderfacDetailId() {
		return orderfacDetailId;
	}

	public void setOrderfacDetailId(Integer orderfacDetailId) {
		this.orderfacDetailId = orderfacDetailId;
	}

	public Integer getAllocateFlag() {
		return allocateFlag;
	}

	public void setAllocateFlag(Integer allocateFlag) {
		this.allocateFlag = allocateFlag;
	}

	public Integer getOrderFacId() {
		return orderFacId;
	}

	public void setOrderFacId(Integer orderFacId) {
		this.orderFacId = orderFacId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getOrderNoOrd() {
		return orderNoOrd;
	}

	public void setOrderNoOrd(String orderNoOrd) {
		this.orderNoOrd = orderNoOrd;
	}

}