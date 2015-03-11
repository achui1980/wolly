package com.sail.cot.domain;

import java.util.Date;

/**
 * VProformaInvoiceId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VProformaInvoice implements java.io.Serializable {
	// Fields
	private Integer id;
	private byte[] companyLogo;
	private String orderNo;
	private String businessPerson;
	private String poNo;
	private Double totalMoney;
	private String curNameen;
	private Date sendTime;
	private String orderRemark;
	private String seller;
	private String buyer;
	private String quality;
	private String colours;
	private String saleUnit;
	private String assortment;
	private String commnets;
	private Date addTime;
	private Date orderLcDate;
	private Date modtime;
	private Integer isCheckAgent;
	private String handleUnit;
	private String osaleunit;
	private String checkperson;
	private String addperosn;
	private String payName;
	private String  agent; 
	private Double commisionScale;
	private String clauseName;
	private String depapture;
	private String destination;
	private String typeEnName;
	private String contact;

	// Constructors

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	/** default constructor */
	public VProformaInvoice() {
	}

	/** minimal constructor */
	public VProformaInvoice(Integer id, String seller, String buyer,
			String quality, String colours, String saleUnit, String assortment,
			String commnets, Integer isCheckAgent) {
		this.id = id;
		this.seller = seller;
		this.buyer = buyer;
		this.quality = quality;
		this.colours = colours;
		this.saleUnit = saleUnit;
		this.assortment = assortment;
		this.commnets = commnets;
		this.isCheckAgent = isCheckAgent;
	}

	/** full constructor */
	public VProformaInvoice(Integer id, byte[] companyLogo, String orderNo,
			String poNo, Double totalMoney, String curNameen, Date sendTime,
			String orderRemark, String seller, String buyer, String quality,
			String colours, String saleUnit, String assortment,
			String commnets, Date addTime, Date orderLcDate, Date modtime,
			Integer isCheckAgent, String handleUnit, String osaleunit,
			String checkperson, String addperosn, String payName) {
		this.id = id;
		this.companyLogo = companyLogo;
		this.orderNo = orderNo;
		this.poNo = poNo;
		this.totalMoney = totalMoney;
		this.curNameen = curNameen;
		this.sendTime = sendTime;
		this.orderRemark = orderRemark;
		this.seller = seller;
		this.buyer = buyer;
		this.quality = quality;
		this.colours = colours;
		this.saleUnit = saleUnit;
		this.assortment = assortment;
		this.commnets = commnets;
		this.addTime = addTime;
		this.orderLcDate = orderLcDate;
		this.modtime = modtime;
		this.isCheckAgent = isCheckAgent;
		this.handleUnit = handleUnit;
		this.osaleunit = osaleunit;
		this.checkperson = checkperson;
		this.addperosn = addperosn;
		this.payName = payName;
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

	public Double getTotalMoney() {
		return this.totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getCurNameen() {
		return this.curNameen;
	}

	public void setCurNameen(String curNameen) {
		this.curNameen = curNameen;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getOrderLcDate() {
		return this.orderLcDate;
	}

	public void setOrderLcDate(Date orderLcDate) {
		this.orderLcDate = orderLcDate;
	}

	public Date getModtime() {
		return this.modtime;
	}

	public void setModtime(Date modtime) {
		this.modtime = modtime;
	}

	public Integer getIsCheckAgent() {
		return this.isCheckAgent;
	}

	public void setIsCheckAgent(Integer isCheckAgent) {
		this.isCheckAgent = isCheckAgent;
	}

	public String getHandleUnit() {
		return this.handleUnit;
	}

	public void setHandleUnit(String handleUnit) {
		this.handleUnit = handleUnit;
	}

	public String getOsaleunit() {
		return this.osaleunit;
	}

	public void setOsaleunit(String osaleunit) {
		this.osaleunit = osaleunit;
	}

	public String getCheckperson() {
		return this.checkperson;
	}

	public void setCheckperson(String checkperson) {
		this.checkperson = checkperson;
	}

	public String getAddperosn() {
		return this.addperosn;
	}

	public void setAddperosn(String addperosn) {
		this.addperosn = addperosn;
	}

	public String getPayName() {
		return this.payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Double getCommisionScale() {
		return commisionScale;
	}

	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}

	public String getClauseName() {
		return clauseName;
	}

	public void setClauseName(String clauseName) {
		this.clauseName = clauseName;
	}

	public String getDepapture() {
		return depapture;
	}

	public void setDepapture(String depapture) {
		this.depapture = depapture;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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