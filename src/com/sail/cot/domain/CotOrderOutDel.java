package com.sail.cot.domain;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotOrderOut entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderOutDel implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private String orderNo;
	private Date orderTime;
	private Date sendTime;
	private Date addTime;
	private Integer businessPerson;
	private Long orderStatus;
	private String checkReason;
	private Long orderIscheck;
	private String orderRemark;
	private Double cutScale;
	private Double commisionScale;//---利润率
	private String orderZm;
	private String orderCm;
	private String orderNm;
	private String orderZhm;
	private String orderMb;
	private byte[]  orderMBImg;
	private String totalName;
	private Float totalMoney;
	private Integer totalCount;
	private Integer totalContainer;
	private Float totalCbm;
	private String orderNumber;
	private Integer currencyId;
	private Integer shipportId;
	private Integer targetportId;
	private Integer trafficId;
	private Integer companyId;
	private Integer factoryId;
	private Integer nateId;
	private Integer clauseTypeId;
	private Integer containerTypeId;
	private Integer commisionTypeId;
	private Integer cutTypeId;
	private String contractContent;
	private Integer splitFlag;
	private String op;
	private Integer custId;
	private String creditNo;
	private Float totalHsMoney;
	private Integer totalHsCount;
	private Integer totalHsContainer;
	private Float totalHsCbm;
	private Float totalGross;
	private Float totalNet;
	private Float totalHsGross;
	private Float totalHsNet;
	private String auditNo;
	private String orderCompose;
	private Integer bankId;
	
	private String checkPerson;//审核人姓名
	
	private String hsTotalName;//报关总品名
	
	private Integer totalGrossChk;//是否自定义总毛重总净重
	private Integer totalNetChk;
	private Integer totalHsGrossChk;
	private Integer totalHsNetChk;
	private Integer totalMoneyChk;//是否自定义总金额总CBM
	private Integer totalCbmChk;
	private Integer totalHsMoneyChk;
	private Integer totalHsCbmChk;
	
	private Float totalRecvOther;//应收帐的其他费用总和
	private Integer contactId;
	
	private Integer paTypeId;//付款方式
	private Double orderRate;//币种汇率
	
	//Additional
	private String  quality;
	private String  colours;
	private String  saleUnit;
	private String  handleUnit;
	private String  assortment;
	private String  comments;
	private String  shippingMark;
	private String  buyer;
	private String  seller;
	private String  agent;
	
	private Integer orderId;//订单编号
	private Integer taxLv;//税率
	private Float taxTotalMoney;//税后金额
	private Integer isCheckAgent;
	private Integer typeLv1Id;//department
	private Integer nationId;
	
	private Date  orderLcDate;
	
	public Date getOrderLcDate() {
		return orderLcDate;
	}

	public void setOrderLcDate(Date orderLcDate) {
		this.orderLcDate = orderLcDate;
	}

	public Integer getTaxLv() {
		return taxLv;
	}

	public void setTaxLv(Integer taxLv) {
		this.taxLv = taxLv;
	}

	public Float getTaxTotalMoney() {
		return taxTotalMoney;
	}

	public Integer getTypeLv1Id() {
		return typeLv1Id;
	}

	public void setTypeLv1Id(Integer typeLv1Id) {
		this.typeLv1Id = typeLv1Id;
	}

	public void setTaxTotalMoney(Float taxTotalMoney) {
		this.taxTotalMoney = taxTotalMoney;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getColours() {
		return colours;
	}

	public void setColours(String colours) {
		this.colours = colours;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public String getHandleUnit() {
		return handleUnit;
	}

	public void setHandleUnit(String handleUnit) {
		this.handleUnit = handleUnit;
	}

	public String getAssortment() {
		return assortment;
	}

	public void setAssortment(String assortment) {
		this.assortment = assortment;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getShippingMark() {
		return shippingMark;
	}

	public void setShippingMark(String shippingMark) {
		this.shippingMark = shippingMark;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Double getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(Double orderRate) {
		this.orderRate = orderRate;
	}

	public String getHsTotalName() {
		return hsTotalName;
	}

	public void setHsTotalName(String hsTotalName) {
		this.hsTotalName = hsTotalName;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	// Constructors
	public String getCreditNo() {
		return creditNo;
	}

	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
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

	/** default constructor */
	public CotOrderOutDel() {
	}

	/** minimal constructor */
	public CotOrderOutDel(String orderNo, Long orderStatus, Long orderIscheck) {
		this.orderNo = orderNo;
		this.orderStatus = orderStatus;
		this.orderIscheck = orderIscheck;
	}

	/** full constructor */
	public CotOrderOutDel(Integer empId, String orderNo, Date orderTime,
			Date sendTime, Date addTime, Integer businessPerson,
			Long orderStatus, String checkReason, Long orderIscheck,
			String orderRemark, Double cutScale, Double commisionScale,
			String orderZm, String orderCm, String orderNm, String orderZhm,
			String orderMb, String totalName, Float totalMoney,
			Integer totalCount, Integer totalContainer, Float totalCbm,
			String orderNumber, Integer currencyId, Integer shipportId,
			Integer targetportId, Integer trafficId, Integer companyId,
			Integer factoryId, Integer nateId, Integer clauseTypeId,
			Integer containerTypeId, Integer commisionTypeId,
			Integer cutTypeId, Set cotOrderOutdetails, Set cotSplitInfos,
			Set cotHsInfos, Set cotShipments, Set cotSymbols) {
		this.empId = empId;
		this.orderNo = orderNo;
		this.orderTime = orderTime;
		this.sendTime = sendTime;
		this.addTime = addTime;
		this.businessPerson = businessPerson;
		this.orderStatus = orderStatus;
		this.checkReason = checkReason;
		this.orderIscheck = orderIscheck;
		this.orderRemark = orderRemark;
		this.cutScale = cutScale;
		this.commisionScale = commisionScale;
		this.orderZm = orderZm;
		this.orderCm = orderCm;
		this.orderNm = orderNm;
		this.orderZhm = orderZhm;
		this.orderMb = orderMb;
		this.totalName = totalName;
		this.totalMoney = totalMoney;
		this.totalCount = totalCount;
		this.totalContainer = totalContainer;
		this.totalCbm = totalCbm;
		this.orderNumber = orderNumber;
		this.currencyId = currencyId;
		this.shipportId = shipportId;
		this.targetportId = targetportId;
		this.trafficId = trafficId;
		this.companyId = companyId;
		this.factoryId = factoryId;
		this.nateId = nateId;
		this.clauseTypeId = clauseTypeId;
		this.containerTypeId = containerTypeId;
		this.commisionTypeId = commisionTypeId;
		this.cutTypeId = cutTypeId;
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

	public String getOrderRemark() {
		return this.orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Double getCutScale() {
		return this.cutScale;
	}

	public void setCutScale(Double cutScale) {
		this.cutScale = cutScale;
	}

	public Double getCommisionScale() {
		return this.commisionScale;
	}

	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}

	public String getOrderZm() {
		return this.orderZm;
	}

	public void setOrderZm(String orderZm) {
		this.orderZm = orderZm;
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

	public String getTotalName() {
		return this.totalName;
	}

	public void setTotalName(String totalName) {
		this.totalName = totalName;
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

	public String getOrderNumber() {
		return this.orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public Integer getNateId() {
		return this.nateId;
	}

	public void setNateId(Integer nateId) {
		this.nateId = nateId;
	}

	public Integer getClauseTypeId() {
		return this.clauseTypeId;
	}

	public void setClauseTypeId(Integer clauseTypeId) {
		this.clauseTypeId = clauseTypeId;
	}

	public Integer getContainerTypeId() {
		return this.containerTypeId;
	}

	public void setContainerTypeId(Integer containerTypeId) {
		this.containerTypeId = containerTypeId;
	}

	public Integer getCommisionTypeId() {
		return this.commisionTypeId;
	}

	public void setCommisionTypeId(Integer commisionTypeId) {
		this.commisionTypeId = commisionTypeId;
	}

	public Integer getCutTypeId() {
		return this.cutTypeId;
	}

	public void setCutTypeId(Integer cutTypeId) {
		this.cutTypeId = cutTypeId;
	}


	public byte[] getOrderMBImg() {
		return orderMBImg;
	}

	public void setOrderMBImg(byte[] orderMBImg) {
		this.orderMBImg = orderMBImg;
	}

	public String getContractContent() {
		return contractContent;
	}

	public void setContractContent(String contractContent) {
		this.contractContent = contractContent;
	}

	public Integer getSplitFlag() {
		return splitFlag;
	}

	public void setSplitFlag(Integer splitFlag) {
		this.splitFlag = splitFlag;
	}

	public Integer getTotalHsCount() {
		return totalHsCount;
	}

	public void setTotalHsCount(Integer totalHsCount) {
		this.totalHsCount = totalHsCount;
	}

	public Integer getTotalHsContainer() {
		return totalHsContainer;
	}

	public void setTotalHsContainer(Integer totalHsContainer) {
		this.totalHsContainer = totalHsContainer;
	}

	public String getAuditNo() {
		return auditNo;
	}

	public void setAuditNo(String auditNo) {
		this.auditNo = auditNo;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public void setTotalCbm(Float totalCbm) {
		this.totalCbm = totalCbm;
	}

	public void setTotalHsMoney(Float totalHsMoney) {
		this.totalHsMoney = totalHsMoney;
	}

	public void setTotalHsCbm(Float totalHsCbm) {
		this.totalHsCbm = totalHsCbm;
	}

	public void setTotalGross(Float totalGross) {
		this.totalGross = totalGross;
	}

	public void setTotalNet(Float totalNet) {
		this.totalNet = totalNet;
	}

	public void setTotalHsGross(Float totalHsGross) {
		this.totalHsGross = totalHsGross;
	}

	public void setTotalHsNet(Float totalHsNet) {
		this.totalHsNet = totalHsNet;
	}

	public Float getTotalMoney() {
		return totalMoney;
	}

	public Float getTotalCbm() {
		return totalCbm;
	}

	public Float getTotalHsMoney() {
		return totalHsMoney;
	}

	public Float getTotalHsCbm() {
		return totalHsCbm;
	}

	public Float getTotalGross() {
		return totalGross;
	}

	public Float getTotalNet() {
		return totalNet;
	}

	public Float getTotalHsGross() {
		return totalHsGross;
	}

	public Float getTotalHsNet() {
		return totalHsNet;
	}

	public String getOrderCompose() {
		return orderCompose;
	}

	public void setOrderCompose(String orderCompose) {
		this.orderCompose = orderCompose;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getTotalGrossChk() {
		return totalGrossChk;
	}

	public void setTotalGrossChk(Integer totalGrossChk) {
		this.totalGrossChk = totalGrossChk;
	}

	public Integer getTotalNetChk() {
		return totalNetChk;
	}

	public void setTotalNetChk(Integer totalNetChk) {
		this.totalNetChk = totalNetChk;
	}

	public Integer getTotalHsGrossChk() {
		return totalHsGrossChk;
	}

	public void setTotalHsGrossChk(Integer totalHsGrossChk) {
		this.totalHsGrossChk = totalHsGrossChk;
	}

	public Integer getTotalHsNetChk() {
		return totalHsNetChk;
	}

	public void setTotalHsNetChk(Integer totalHsNetChk) {
		this.totalHsNetChk = totalHsNetChk;
	}

	public Float getTotalRecvOther() {
		return totalRecvOther;
	}

	public void setTotalRecvOther(Float totalRecvOther) {
		this.totalRecvOther = totalRecvOther;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public Integer getTotalMoneyChk() {
		return totalMoneyChk;
	}

	public void setTotalMoneyChk(Integer totalMoneyChk) {
		this.totalMoneyChk = totalMoneyChk;
	}

	public Integer getTotalCbmChk() {
		return totalCbmChk;
	}

	public void setTotalCbmChk(Integer totalCbmChk) {
		this.totalCbmChk = totalCbmChk;
	}

	public Integer getTotalHsMoneyChk() {
		return totalHsMoneyChk;
	}

	public void setTotalHsMoneyChk(Integer totalHsMoneyChk) {
		this.totalHsMoneyChk = totalHsMoneyChk;
	}

	public Integer getTotalHsCbmChk() {
		return totalHsCbmChk;
	}

	public void setTotalHsCbmChk(Integer totalHsCbmChk) {
		this.totalHsCbmChk = totalHsCbmChk;
	}

	public Integer getPaTypeId() {
		return paTypeId;
	}

	public void setPaTypeId(Integer paTypeId) {
		this.paTypeId = paTypeId;
	}

	public Integer getIsCheckAgent() {
		return isCheckAgent;
	}

	public Integer getNationId() {
		return nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	public void setIsCheckAgent(Integer isCheckAgent) {
		this.isCheckAgent = isCheckAgent;
	}

}