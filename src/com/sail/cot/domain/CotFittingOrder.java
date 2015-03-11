package com.sail.cot.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotFittingOrder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFittingOrder implements java.io.Serializable {

	// Fields

	private Integer id;
	private Date orderDate;//采购日期
	private String fittingOrderNo;//采购单号
	private Date sendDate;//交货日期
	private String signAddr;//签约地点
	private String sendAddr;//送货地点
	private Double totalAmmount;//总金额
	private Double realMoney;//实际金额
	private String remark;//备注
	private Integer empId;//采购人员
	private Integer factoryId;//供应商
	private String orderNo;//订单号
	private Integer orderId;//订单编号
	private Integer companyId;//采购公司
	private Float prePrice;// 预收货款金额
	private Float priceScal;// 预收货款比例
	private Set cotFittingsOrderdetails = new HashSet(0);
	private String op;
	private Integer orderStatus;//审核状态
	private String checkReason;//审核原因
	private String checkPerson;//审核人姓名
	private Integer facContactId;

	// Constructors

	public Integer getFacContactId() {
		return facContactId;
	}

	public void setFacContactId(Integer facContactId) {
		this.facContactId = facContactId;
	}

	/** default constructor */
	public CotFittingOrder() {
	}

	/** full constructor */
	public CotFittingOrder(Date orderDate, String fittingOrderNo,
			Date sendDate, String signAddr, String sendAddr, Double totalAmmount,
			String remark, Integer empId, Integer factoryId, String orderNo,
			Integer orderId, Set cotFittingsOrderdetails) {
		this.orderDate = orderDate;
		this.fittingOrderNo = fittingOrderNo;
		this.sendDate = sendDate;
		this.signAddr = signAddr;
		this.sendAddr = sendAddr;
		this.totalAmmount = totalAmmount;
		this.remark = remark;
		this.empId = empId;
		this.factoryId = factoryId;
		this.orderNo = orderNo;
		this.orderId = orderId;
		this.cotFittingsOrderdetails = cotFittingsOrderdetails;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getFittingOrderNo() {
		return this.fittingOrderNo;
	}

	public void setFittingOrderNo(String fittingOrderNo) {
		this.fittingOrderNo = fittingOrderNo;
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

	public String getSendAddr() {
		return this.sendAddr;
	}

	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}

	public Double getTotalAmmount() {
		return this.totalAmmount;
	}

	public void setTotalAmmount(Double totalAmmount) {
		this.totalAmmount = totalAmmount;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Set getCotFittingsOrderdetails() {
		return this.cotFittingsOrderdetails;
	}

	public void setCotFittingsOrderdetails(Set cotFittingsOrderdetails) {
		this.cotFittingsOrderdetails = cotFittingsOrderdetails;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Float getPriceScal() {
		return priceScal;
	}

	public void setPriceScal(Float priceScal) {
		this.priceScal = priceScal;
	}

	public Float getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(Float prePrice) {
		this.prePrice = prePrice;
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

	public Double getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(Double realMoney) {
		this.realMoney = realMoney;
	}

}