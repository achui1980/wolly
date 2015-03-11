package com.sail.cot.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotOrderFac entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderFac implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private String orderNo;
	private Date orderTime;
	private Date sendTime;
	private String orderAddress;
	private String givenAddress;
	private Timestamp addTime;
	private Integer businessPerson;
	private Long orderStatus;
	private String checkReason;
	private Long orderIscheck;
	private String orderRemark;
	private String orderZm;
	private String orderCm;
	private String orderNm;
	private String orderZhm;
	private String orderMb;
	private byte[]  orderMBImg;
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
	private Set cotOrderFacdetails = new HashSet(0);
	private String orderClause ;
	private String recvPerson;
	private Float prePrice;// 预收货款金额
	private Float priceScal;// 预收货款比例
	private String op;
	private Integer facContactId;
	
	private Integer checkPerson;//审核人--关联员工表
	private Double realMoney;//实际金额
	private String  productM;//产品标
	
	private Integer sampleStatus;//产前样确认状态
	private Integer sampleOutStatus;//出货样确认状态
	private Integer packetStatus;//包装确认状态
	private Integer qcStatus;//QC确认状态
	private Integer outStatus;//出货确认状态
	private Integer orderId;//订单ID
	private String poNo;
	private Integer payTypeId;
	private String allPinName;
	private Integer clauseTypeId;
	private Integer containerTypeId;
	private Integer zheType;
	private Float zheNum;
	private Date shipmentDate;
	private Date simpleSampleDeadline;
	private Date simpleSampleApproval;
	private Date completeSampleDeadline;
	private Date completeSampleApproval;
	private Date facDeadline;
	private Date facApproval;
	private Date pcaketDeadline;
	private Date pcaketApproval;
	private Date samplePicDeadline;
	private Date samplePicApproval;
	private Date sampleOutDeadline;
	private Date sampleOutApproval;
	private Date qcDeadline;
	private Date qcApproval;
	private Date shippingDeadline;
	private Date shippingApproval;
	private Date loadingDeadline;
	private Date loadingApproval;
	
	private Timestamp modTime;
	private Integer modPerson;
	private Integer addPerson;
	private Integer isCheckAgent;
   
	private String consignee;
	private Integer forwardingAgent;//货代(公司英名)
	private String booking;//货代(公司备注)
	private Double commisionScale;
	
	private Date orderLcDelay;
	private Double orderEarnest;//---手动输入总cbm
	private Integer typeLv1Id;//department
	
	private Date oderFacText;
	
	private Integer preNum;
	private String preText;
	private Integer artNum;
	private String artText;
	private Integer samNum;
	private String samText;
	private Integer qcNum;
	private String qcText;
	private Integer shipNum;
	private String shipText;
	
	private Integer preMan;
	private Integer artMan;
	private Integer samMan;
	private Integer qcMan;
	private Integer shipMan;
	
	private String themes;
	private String themeStr;
	private Date designTime;
	
	private Double estConsumedTime;
	private Double estTime;
	private Double travelTime;
	private Double travelConsumedTime;
	private Double totalExpenseUsd;
	private String qcPerson;
	private String newRemark;
	private Integer nationId;
	private Date checkDate;
	private Date etdDate;
	//写入PDF用
	private String approvalCommentsample;
	private String sampleApprove;
	private String approvalCommentsampleOut;
	private String sampleOutApprove;
	private String sampleOutPassed;
	private String sampleOutWash;
	private String confirmBy;

	public Integer getPreNum() {
		return preNum;
	}

	public void setPreNum(Integer preNum) {
		this.preNum = preNum;
	}

	public String getPreText() {
		return preText;
	}

	public Date getDesignTime() {
		return designTime;
	}

	public void setDesignTime(Date designTime) {
		this.designTime = designTime;
	}

	public void setPreText(String preText) {
		this.preText = preText;
	}

	public Integer getArtNum() {
		return artNum;
	}

	public void setArtNum(Integer artNum) {
		this.artNum = artNum;
	}

	public String getArtText() {
		return artText;
	}

	public void setArtText(String artText) {
		this.artText = artText;
	}

	public Integer getSamNum() {
		return samNum;
	}

	public void setSamNum(Integer samNum) {
		this.samNum = samNum;
	}

	public String getSamText() {
		return samText;
	}

	public void setSamText(String samText) {
		this.samText = samText;
	}

	public Integer getQcNum() {
		return qcNum;
	}

	public void setQcNum(Integer qcNum) {
		this.qcNum = qcNum;
	}

	public String getQcText() {
		return qcText;
	}

	public void setQcText(String qcText) {
		this.qcText = qcText;
	}

	public Integer getShipNum() {
		return shipNum;
	}

	public void setShipNum(Integer shipNum) {
		this.shipNum = shipNum;
	}

	public String getShipText() {
		return shipText;
	}

	public void setShipText(String shipText) {
		this.shipText = shipText;
	}

	public Date getOderFacText() {
		return oderFacText;
	}

	public void setOderFacText(Date oderFacText) {
		this.oderFacText = oderFacText;
	}

	public Date getOrderLcDelay() {
		return orderLcDelay;
	}

	public void setOrderLcDelay(Date orderLcDelay) {
		this.orderLcDelay = orderLcDelay;
	}

	public Integer getForwardingAgent() {
		return forwardingAgent;
	}

	public void setForwardingAgent(Integer forwardingAgent) {
		this.forwardingAgent = forwardingAgent;
	}

	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}



	public Integer getIsCheckAgent() {
		return isCheckAgent;
	}

	public void setIsCheckAgent(Integer isCheckAgent) {
		this.isCheckAgent = isCheckAgent;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Integer getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getAllPinName() {
		return allPinName;
	}

	public void setAllPinName(String allPinName) {
		this.allPinName = allPinName;
	}

	public Integer getClauseTypeId() {
		return clauseTypeId;
	}

	public void setClauseTypeId(Integer clauseTypeId) {
		this.clauseTypeId = clauseTypeId;
	}

	public Integer getContainerTypeId() {
		return containerTypeId;
	}

	public void setContainerTypeId(Integer containerTypeId) {
		this.containerTypeId = containerTypeId;
	}

	public Integer getZheType() {
		return zheType;
	}

	public void setZheType(Integer zheType) {
		this.zheType = zheType;
	}

	public Float getZheNum() {
		return zheNum;
	}

	public void setZheNum(Float zheNum) {
		this.zheNum = zheNum;
	}

	public Date getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}
	
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
	private String qcLocation;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getProductM() {
		return productM;
	}

	public void setProductM(String productM) {
		this.productM = productM;
	}

	public Double getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(Double realMoney) {
		this.realMoney = realMoney;
	}

	public Integer getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(Integer checkPerson) {
		this.checkPerson = checkPerson;
	}

	// Constructors
	/** default constructor */
	public CotOrderFac() {
	}

	/** minimal constructor */
	public CotOrderFac(String orderNo, Long orderStatus, Long orderIscheck) {
		this.orderNo = orderNo;
		this.orderStatus = orderStatus;
		this.orderIscheck = orderIscheck;
	}

	/** full constructor */
	public CotOrderFac(Integer empId, String orderNo, Date orderTime,
			Date sendTime, String orderAddress, String givenAddress,
			Timestamp addTime, Integer businessPerson, Long orderStatus,
			String checkReason, Long orderIscheck, String orderRemark,
			String orderZm, String orderCm, String orderNm, String orderZhm,
			String orderMb, Double totalMoney, Integer totalCount,
			Integer totalContainer, Double totalCbm, Integer currencyId,
			Integer shipportId, Integer targetportId, Integer trafficId,
			Integer companyId, Integer factoryId, String orderNumber,
			Set cotOrderFacdetails) {
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
		this.orderRemark = orderRemark;
		this.orderZm = orderZm;
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
		this.cotOrderFacdetails = cotOrderFacdetails;
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

	public Timestamp getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Timestamp addTime) {
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
	
	public byte[] getOrderMBImg() {
		return orderMBImg;
	}

	public void setOrderMBImg(byte[] orderMBImg) {
		this.orderMBImg = orderMBImg;
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

	public Set getCotOrderFacdetails() {
		return this.cotOrderFacdetails;
	}

	public void setCotOrderFacdetails(Set cotOrderFacdetails) {
		this.cotOrderFacdetails = cotOrderFacdetails;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getOrderClause() {
		return orderClause;
	}

	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}

	public String getRecvPerson() {
		return recvPerson;
	}

	public void setRecvPerson(String recvPerson) {
		this.recvPerson = recvPerson;
	}

	public Float getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(Float prePrice) {
		this.prePrice = prePrice;
	}

	public Float getPriceScal() {
		return priceScal;
	}

	public void setPriceScal(Float priceScal) {
		this.priceScal = priceScal;
	}

	public Integer getFacContactId() {
		return facContactId;
	}

	public void setFacContactId(Integer facContactId) {
		this.facContactId = facContactId;
	}

	public Integer getSampleStatus() {
		return sampleStatus;
	}

	public void setSampleStatus(Integer sampleStatus) {
		this.sampleStatus = sampleStatus;
	}

	public Integer getSampleOutStatus() {
		return sampleOutStatus;
	}

	public void setSampleOutStatus(Integer sampleOutStatus) {
		this.sampleOutStatus = sampleOutStatus;
	}

	public Integer getPacketStatus() {
		return packetStatus;
	}

	public void setPacketStatus(Integer packetStatus) {
		this.packetStatus = packetStatus;
	}

	public Integer getQcStatus() {
		return qcStatus;
	}

	public void setQcStatus(Integer qcStatus) {
		this.qcStatus = qcStatus;
	}

	public Integer getOutStatus() {
		return outStatus;
	}

	public void setOutStatus(Integer outStatus) {
		this.outStatus = outStatus;
	}

	public Date getSimpleSampleDeadline() {
		return simpleSampleDeadline;
	}

	public void setSimpleSampleDeadline(Date simpleSampleDeadline) {
		this.simpleSampleDeadline = simpleSampleDeadline;
	}

	public Date getSimpleSampleApproval() {
		return simpleSampleApproval;
	}

	public void setSimpleSampleApproval(Date simpleSampleApproval) {
		this.simpleSampleApproval = simpleSampleApproval;
	}

	public Date getCompleteSampleDeadline() {
		return completeSampleDeadline;
	}

	public void setCompleteSampleDeadline(Date completeSampleDeadline) {
		this.completeSampleDeadline = completeSampleDeadline;
	}

	public Date getCompleteSampleApproval() {
		return completeSampleApproval;
	}

	public void setCompleteSampleApproval(Date completeSampleApproval) {
		this.completeSampleApproval = completeSampleApproval;
	}

	public Date getFacDeadline() {
		return facDeadline;
	}

	public void setFacDeadline(Date facDeadline) {
		this.facDeadline = facDeadline;
	}

	public Date getFacApproval() {
		return facApproval;
	}

	public void setFacApproval(Date facApproval) {
		this.facApproval = facApproval;
	}

	public Date getPcaketDeadline() {
		return pcaketDeadline;
	}

	public void setPcaketDeadline(Date pcaketDeadline) {
		this.pcaketDeadline = pcaketDeadline;
	}

	public Date getPcaketApproval() {
		return pcaketApproval;
	}

	public void setPcaketApproval(Date pcaketApproval) {
		this.pcaketApproval = pcaketApproval;
	}

	public Date getSamplePicDeadline() {
		return samplePicDeadline;
	}

	public void setSamplePicDeadline(Date samplePicDeadline) {
		this.samplePicDeadline = samplePicDeadline;
	}

	public Date getSamplePicApproval() {
		return samplePicApproval;
	}

	public void setSamplePicApproval(Date samplePicApproval) {
		this.samplePicApproval = samplePicApproval;
	}

	public Date getSampleOutDeadline() {
		return sampleOutDeadline;
	}

	public void setSampleOutDeadline(Date sampleOutDeadline) {
		this.sampleOutDeadline = sampleOutDeadline;
	}

	public Date getSampleOutApproval() {
		return sampleOutApproval;
	}

	public void setSampleOutApproval(Date sampleOutApproval) {
		this.sampleOutApproval = sampleOutApproval;
	}

	public Date getQcDeadline() {
		return qcDeadline;
	}

	public void setQcDeadline(Date qcDeadline) {
		this.qcDeadline = qcDeadline;
	}

	public Date getQcApproval() {
		return qcApproval;
	}

	public void setQcApproval(Date qcApproval) {
		this.qcApproval = qcApproval;
	}

	public Date getShippingDeadline() {
		return shippingDeadline;
	}

	public void setShippingDeadline(Date shippingDeadline) {
		this.shippingDeadline = shippingDeadline;
	}

	public Date getShippingApproval() {
		return shippingApproval;
	}

	public void setShippingApproval(Date shippingApproval) {
		this.shippingApproval = shippingApproval;
	}

	public Date getLoadingDeadline() {
		return loadingDeadline;
	}

	public void setLoadingDeadline(Date loadingDeadline) {
		this.loadingDeadline = loadingDeadline;
	}

	public Date getLoadingApproval() {
		return loadingApproval;
	}

	public void setLoadingApproval(Date loadingApproval) {
		this.loadingApproval = loadingApproval;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public Integer getModPerson() {
		return modPerson;
	}

	public void setModPerson(Integer modPerson) {
		this.modPerson = modPerson;
	}

	public Integer getAddPerson() {
		return addPerson;
	}

	public void setAddPerson(Integer addPerson) {
		this.addPerson = addPerson;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getQcLocation() {
		return qcLocation;
	}

	public void setQcLocation(String qcLocation) {
		this.qcLocation = qcLocation;
	}

	public Double getCommisionScale() {
		return commisionScale;
	}

	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}

	public Double getOrderEarnest() {
		return orderEarnest;
	}

	public void setOrderEarnest(Double orderEarnest) {
		this.orderEarnest = orderEarnest;
	}

	public Integer getTypeLv1Id() {
		return typeLv1Id;
	}

	public void setTypeLv1Id(Integer typeLv1Id) {
		this.typeLv1Id = typeLv1Id;
	}

	public Integer getPreMan() {
		return preMan;
	}

	public void setPreMan(Integer preMan) {
		this.preMan = preMan;
	}

	public Integer getArtMan() {
		return artMan;
	}

	public void setArtMan(Integer artMan) {
		this.artMan = artMan;
	}

	public Integer getSamMan() {
		return samMan;
	}

	public void setSamMan(Integer samMan) {
		this.samMan = samMan;
	}

	public Integer getQcMan() {
		return qcMan;
	}

	public void setQcMan(Integer qcMan) {
		this.qcMan = qcMan;
	}

	public Integer getShipMan() {
		return shipMan;
	}

	public void setShipMan(Integer shipMan) {
		this.shipMan = shipMan;
	}

	public String getThemes() {
		return themes;
	}

	public void setThemes(String themes) {
		this.themes = themes;
	}

	public String getThemeStr() {
		return themeStr;
	}

	public void setThemeStr(String themeStr) {
		this.themeStr = themeStr;
	}

	public Double getEstConsumedTime() {
		return estConsumedTime;
	}

	public void setEstConsumedTime(Double estConsumedTime) {
		this.estConsumedTime = estConsumedTime;
	}

	public Double getEstTime() {
		return estTime;
	}

	public void setEstTime(Double estTime) {
		this.estTime = estTime;
	}

	public Double getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(Double travelTime) {
		this.travelTime = travelTime;
	}

	public Double getTravelConsumedTime() {
		return travelConsumedTime;
	}

	public void setTravelConsumedTime(Double travelConsumedTime) {
		this.travelConsumedTime = travelConsumedTime;
	}

	public Double getTotalExpenseUsd() {
		return totalExpenseUsd;
	}

	public void setTotalExpenseUsd(Double totalExpenseUsd) {
		this.totalExpenseUsd = totalExpenseUsd;
	}

	public String getNewRemark() {
		return newRemark;
	}

	public void setNewRemark(String newRemark) {
		this.newRemark = newRemark;
	}

	public String getQcPerson() {
		return qcPerson;
	}

	public void setQcPerson(String qcPerson) {
		this.qcPerson = qcPerson;
	}

	public Integer getNationId() {
		return nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getEtdDate() {
		return etdDate;
	}

	public void setEtdDate(Date etdDate) {
		this.etdDate = etdDate;
	}

	public String getApprovalCommentsample() {
		return approvalCommentsample;
	}

	public void setApprovalCommentsample(String approvalCommentsample) {
		this.approvalCommentsample = approvalCommentsample;
	}

	public String getApprovalCommentsampleOut() {
		return approvalCommentsampleOut;
	}

	public void setApprovalCommentsampleOut(String approvalCommentsampleOut) {
		this.approvalCommentsampleOut = approvalCommentsampleOut;
	}

	public String getSampleApprove() {
		return sampleApprove;
	}

	public void setSampleApprove(String sampleApprove) {
		this.sampleApprove = sampleApprove;
	}

	public String getSampleOutApprove() {
		return sampleOutApprove;
	}

	public void setSampleOutApprove(String sampleOutApprove) {
		this.sampleOutApprove = sampleOutApprove;
	}

	public String getConfirmBy() {
		return confirmBy;
	}

	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}

	public String getSampleOutPassed() {
		return sampleOutPassed;
	}

	public void setSampleOutPassed(String sampleOutPassed) {
		this.sampleOutPassed = sampleOutPassed;
	}

	public String getSampleOutWash() {
		return sampleOutWash;
	}

	public void setSampleOutWash(String sampleOutWash) {
		this.sampleOutWash = sampleOutWash;
	}
}