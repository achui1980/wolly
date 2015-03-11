package com.sail.cot.domain.vo;

import java.sql.Date;

public class CotOrderOutVO {
	private Integer id;
	private String orderNo;//发票编号
	private Date orderTime;//签单日期
	private Date sendTime;//装运期限
	private String customerShortName;
	private String empsName;
	private Integer totalCount;//总数量
	private Integer totalContainer;//总箱数
	private Float totalCbm;//总体积
	private Long orderStatus;//审核状态
	private Integer shipportId;//起运港
	private Integer targetportId;//目的港
	private Integer trafficId;//运输方式
	private String eleId;
	private String eleName;
	private Float orderPrice;
	private Integer currencyId;
	private Integer custId;
	private String op;
	private Float totalMoney;
	private Float totalHsMoney;
	private Date reclaimDate;//开船时间
	private Integer clauseTypeId;//价格条款
	private Integer paTypeId;//付款方式
	private Integer typeLv1Id;//department
	
	private String odNo;//pi编号
	private String poNo;
	private String allPinName;
	private String shortName;
	private Date  orderLcDate;
	private Date  orderLcDelay;
	private Float taxTotalMoney;//税后金额
	
	private Integer chk;
	
	public Date getOrderLcDelay() {
		return orderLcDelay;
	}
	public void setOrderLcDelay(Date orderLcDelay) {
		this.orderLcDelay = orderLcDelay;
	}
	public Float getTaxTotalMoney() {
		return taxTotalMoney;
	}
	public void setTaxTotalMoney(Float taxTotalMoney) {
		this.taxTotalMoney = taxTotalMoney;
	}
	public Date getOrderLcDate() {
		return orderLcDate;
	}
	public void setOrderLcDate(Date orderLcDate) {
		this.orderLcDate = orderLcDate;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public String getEmpsName() {
		return empsName;
	}
	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalContainer() {
		return totalContainer;
	}
	public void setTotalContainer(Integer totalContainer) {
		this.totalContainer = totalContainer;
	}
	public Float getTotalCbm() {
		return totalCbm;
	}
	public void setTotalCbm(Float totalCbm) {
		this.totalCbm = totalCbm;
	}
	public Long getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Long orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getShipportId() {
		return shipportId;
	}
	public void setShipportId(Integer shipportId) {
		this.shipportId = shipportId;
	}
	public Integer getTargetportId() {
		return targetportId;
	}
	public void setTargetportId(Integer targetportId) {
		this.targetportId = targetportId;
	}
	public Integer getTrafficId() {
		return trafficId;
	}
	public void setTrafficId(Integer trafficId) {
		this.trafficId = trafficId;
	}
	public String getEleId() {
		return eleId;
	}
	public void setEleId(String eleId) {
		this.eleId = eleId;
	}
	public String getEleName() {
		return eleName;
	}
	public void setEleName(String eleName) {
		this.eleName = eleName;
	}
	public Float getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Float orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Float getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Float getTotalHsMoney() {
		return totalHsMoney;
	}
	public void setTotalHsMoney(Float totalHsMoney) {
		this.totalHsMoney = totalHsMoney;
	}
	public Date getReclaimDate() {
		return reclaimDate;
	}
	public void setReclaimDate(Date reclaimDate) {
		this.reclaimDate = reclaimDate;
	}
	public Integer getClauseTypeId() {
		return clauseTypeId;
	}
	public void setClauseTypeId(Integer clauseTypeId) {
		this.clauseTypeId = clauseTypeId;
	}
	public Integer getPaTypeId() {
		return paTypeId;
	}
	public void setPaTypeId(Integer paTypeId) {
		this.paTypeId = paTypeId;
	}
	public String getOdNo() {
		return odNo;
	}
	public void setOdNo(String odNo) {
		this.odNo = odNo;
	}
	public Integer getTypeLv1Id() {
		return typeLv1Id;
	}
	public void setTypeLv1Id(Integer typeLv1Id) {
		this.typeLv1Id = typeLv1Id;
	}
	public String getAllPinName() {
		return allPinName;
	}
	public void setAllPinName(String allPinName) {
		this.allPinName = allPinName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public Integer getChk() {
		return chk;
	}
	public void setChk(Integer chk) {
		this.chk = chk;
	}
	
	
}
