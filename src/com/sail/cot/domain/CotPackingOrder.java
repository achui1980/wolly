package com.sail.cot.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotPackingOrder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPackingOrder implements java.io.Serializable {

	// Fields

	private Integer id;
	private String packingOrderNo;
	private Integer factoryId;
	private Date orderDate;
	private Date sendDate;
	private String signAddr;
	private String orderNo;
	private Integer companyId;
	private Integer empId;
	private String sendAddr;
	private Double totalAmount;
	private Double prePrice;
	private Double priceScal;
	private Integer orderId;
	private String remark;
	private Set cotPackingOrderdetails = new HashSet(0);
	private String op;
	private Integer orderStatus;//审核状态
	private String checkReason;//审核原因
	private String checkPerson;//审核人姓名
	private Integer facContactId;
	private String  orderZM;
	private String  orderCM;
	private String  orderZHM;
	private String  orderNM;
	private Double realMoney;//实际金额
	// Constructors

	public Double getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(Double realMoney) {
		this.realMoney = realMoney;
	}

	public String getOrderZM() {
		return orderZM;
	}

	public void setOrderZM(String orderZM) {
		this.orderZM = orderZM;
	}

	public String getOrderCM() {
		return orderCM;
	}

	public void setOrderCM(String orderCM) {
		this.orderCM = orderCM;
	}

	public String getOrderZHM() {
		return orderZHM;
	}

	public void setOrderZHM(String orderZHM) {
		this.orderZHM = orderZHM;
	}

	public String getOrderNM() {
		return orderNM;
	}

	public void setOrderNM(String orderNM) {
		this.orderNM = orderNM;
	}

	public Integer getFacContactId() {
		return facContactId;
	}

	public void setFacContactId(Integer facContactId) {
		this.facContactId = facContactId;
	}

	/** default constructor */
	public CotPackingOrder() {
	}

	/** full constructor */
	public CotPackingOrder(String packingOrderNo, Integer factoryId,
			Date orderDate, Date sendDate, String signAddr, String orderNo,
			Integer companyId, Integer empId, String sendAddr,
			Double totalAmount, Double prePrice, Double priceScal,
			Integer orderId, String remark, Set cotPackingOrderdetails) {
		this.packingOrderNo = packingOrderNo;
		this.factoryId = factoryId;
		this.orderDate = orderDate;
		this.sendDate = sendDate;
		this.signAddr = signAddr;
		this.orderNo = orderNo;
		this.companyId = companyId;
		this.empId = empId;
		this.sendAddr = sendAddr;
		this.totalAmount = totalAmount;
		this.prePrice = prePrice;
		this.priceScal = priceScal;
		this.orderId = orderId;
		this.remark = remark;
		this.cotPackingOrderdetails = cotPackingOrderdetails;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPackingOrderNo() {
		return this.packingOrderNo;
	}

	public void setPackingOrderNo(String packingOrderNo) {
		this.packingOrderNo = packingOrderNo;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSignAddr() {
		return this.signAddr;
	}

	public void setSignAddr(String signAddr) {
		this.signAddr = signAddr;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getSendAddr() {
		return this.sendAddr;
	}

	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getPrePrice() {
		return this.prePrice;
	}

	public void setPrePrice(Double prePrice) {
		this.prePrice = prePrice;
	}

	public Double getPriceScal() {
		return this.priceScal;
	}

	public void setPriceScal(Double priceScal) {
		this.priceScal = priceScal;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set getCotPackingOrderdetails() {
		return this.cotPackingOrderdetails;
	}

	public void setCotPackingOrderdetails(Set cotPackingOrderdetails) {
		this.cotPackingOrderdetails = cotPackingOrderdetails;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(String checkReason) {
		this.checkReason = checkReason;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

}