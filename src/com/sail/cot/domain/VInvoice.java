package com.sail.cot.domain;

import java.util.Date;

/**
 * VInvoice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VInvoice implements java.io.Serializable {

	private Integer id;
	private byte[] companyLogo;
	private String invoiceNo;
	private String businessPerson;
	private Date invoiceDate;
	private String orderNo;
	private String poNo;
	private Date ddOrderTime;
	private Double omoney;
	private Date sendTime;
	private String jgtk;
	private String moneyUnit;
	private String moneyType;
	private Date deliveryDate;
	private String orderRemark;
	private String seller;
	private String agent;
	private String buyer;
	private String quality;
	private String colours;
	private String saleUnit;
	private String handleUnit;
	private String assortment;
	private String commnets;
	private String pay;
	private String shy;
	private Date addTime;
	private Date shipmentDate;
	private Date revisedTime;
	private Integer checkperson;
	private String vatNo;
	private String factoryNo;
	private String ywy;
	private String depapture;
	private String destination;
	private Integer isCheckAgent;
	private Double commisionScale;
	private Integer orderStatus;
	private Integer taxLv;//是否需要加税 1.需要
	
	private String typeEnName;

	// Constructors

	/** default constructor */
	public VInvoice() {
	}

	/** minimal constructor */
	public VInvoice(Integer id, String seller, String agent, String buyer,
			String quality, String colours, String saleUnit, String handleUnit,
			String assortment, String commnets, String pay, String shy,
			String vatNo, String depapture, Integer isCheckAgent) {
		this.id = id;
		this.seller = seller;
		this.agent = agent;
		this.buyer = buyer;
		this.quality = quality;
		this.colours = colours;
		this.saleUnit = saleUnit;
		this.handleUnit = handleUnit;
		this.assortment = assortment;
		this.commnets = commnets;
		this.pay = pay;
		this.shy = shy;
		this.vatNo = vatNo;
		this.depapture = depapture;
		this.isCheckAgent = isCheckAgent;
	}

	/** full constructor */
	public VInvoice(Integer id, byte[] companyLogo, String invoiceNo,
			Date invoiceDate, String orderNo, String poNo, Date ddOrderTime,
			Double omoney, Date sendTime, String jgtk, String moneyUnit,
			String moneyType, Date deliveryDate, String orderRemark,
			String seller, String agent, String buyer, String quality,
			String colours, String saleUnit, String handleUnit,
			String assortment, String commnets, String pay, String shy,
			Date addTime, Date shipmentDate, Date revisedTime,
			Integer checkperson, String vatNo, String factoryNo, String ywy,
			String depapture, String destination, Integer isCheckAgent,
			Double commisionScale) {
		this.id = id;
		this.companyLogo = companyLogo;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.orderNo = orderNo;
		this.poNo = poNo;
		this.ddOrderTime = ddOrderTime;
		this.omoney = omoney;
		this.sendTime = sendTime;
		this.jgtk = jgtk;
		this.moneyUnit = moneyUnit;
		this.moneyType = moneyType;
		this.deliveryDate = deliveryDate;
		this.orderRemark = orderRemark;
		this.seller = seller;
		this.agent = agent;
		this.buyer = buyer;
		this.quality = quality;
		this.colours = colours;
		this.saleUnit = saleUnit;
		this.handleUnit = handleUnit;
		this.assortment = assortment;
		this.commnets = commnets;
		this.pay = pay;
		this.shy = shy;
		this.addTime = addTime;
		this.shipmentDate = shipmentDate;
		this.revisedTime = revisedTime;
		this.checkperson = checkperson;
		this.vatNo = vatNo;
		this.factoryNo = factoryNo;
		this.ywy = ywy;
		this.depapture = depapture;
		this.destination = destination;
		this.isCheckAgent = isCheckAgent;
		this.commisionScale = commisionScale;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getCompanyLogo() {
		return this.companyLogo;
	}

	public void setCompanyLogo(byte[] companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPoNo() {
		return this.poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Date getDdOrderTime() {
		return this.ddOrderTime;
	}

	public void setDdOrderTime(Date ddOrderTime) {
		this.ddOrderTime = ddOrderTime;
	}

	public Double getOmoney() {
		return this.omoney;
	}

	public void setOmoney(Double omoney) {
		this.omoney = omoney;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getJgtk() {
		return this.jgtk;
	}

	public void setJgtk(String jgtk) {
		this.jgtk = jgtk;
	}

	public String getMoneyUnit() {
		return this.moneyUnit;
	}

	public void setMoneyUnit(String moneyUnit) {
		this.moneyUnit = moneyUnit;
	}

	public String getMoneyType() {
		return this.moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getOrderRemark() {
		return this.orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public String getSeller() {
		return this.seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getQuality() {
		return this.quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getColours() {
		return this.colours;
	}

	public void setColours(String colours) {
		this.colours = colours;
	}

	public String getSaleUnit() {
		return this.saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public String getHandleUnit() {
		return this.handleUnit;
	}

	public void setHandleUnit(String handleUnit) {
		this.handleUnit = handleUnit;
	}

	public String getAssortment() {
		return this.assortment;
	}

	public void setAssortment(String assortment) {
		this.assortment = assortment;
	}

	public String getCommnets() {
		return this.commnets;
	}

	public void setCommnets(String commnets) {
		this.commnets = commnets;
	}

	public String getPay() {
		return this.pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getShy() {
		return this.shy;
	}

	public void setShy(String shy) {
		this.shy = shy;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getShipmentDate() {
		return this.shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public Date getRevisedTime() {
		return this.revisedTime;
	}

	public void setRevisedTime(Date revisedTime) {
		this.revisedTime = revisedTime;
	}

	public Integer getCheckperson() {
		return this.checkperson;
	}

	public void setCheckperson(Integer checkperson) {
		this.checkperson = checkperson;
	}

	public String getVatNo() {
		return this.vatNo;
	}

	public void setVatNo(String vatNo) {
		this.vatNo = vatNo;
	}

	public String getFactoryNo() {
		return this.factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public String getYwy() {
		return this.ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}

	public String getDepapture() {
		return this.depapture;
	}

	public void setDepapture(String depapture) {
		this.depapture = depapture;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getIsCheckAgent() {
		return this.isCheckAgent;
	}

	public void setIsCheckAgent(Integer isCheckAgent) {
		this.isCheckAgent = isCheckAgent;
	}

	public Double getCommisionScale() {
		return this.commisionScale;
	}

	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getTaxLv() {
		return taxLv;
	}

	public void setTaxLv(Integer taxLv) {
		this.taxLv = taxLv;
	}

	public String getBusinessPerson() {
		return businessPerson;
	}

	public void setBusinessPerson(String businessPerson) {
		this.businessPerson = businessPerson;
	}

	public String getTypeEnName() {
		return typeEnName;
	}

	public void setTypeEnName(String typeEnName) {
		this.typeEnName = typeEnName;
	}

}