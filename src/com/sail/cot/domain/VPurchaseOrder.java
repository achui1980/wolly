package com.sail.cot.domain;

import java.util.Date;

/**
 * VPurchaseOrderId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VPurchaseOrder implements java.io.Serializable {

	// Fields
	private Integer id;
	private byte[] companyLogo;
	private String orderNo;
	private String businessPerson;
	private String poNo;
	private String jgtk;
	private String moneyType;
	private String pay;
	private String seller;
	private String agent;
	private String buyer;
	private String quality;
	private String colours;
	private String saleUnit;
	private String handleUnit;
	private String assortment;
	private String commnets;
	private String hd;
	private String booking;
	private Date colorApprovalDeadline;
	private Date prepruductionSample;
	private Date artworkApproval;
	private Date photoSample;
	private Date shipmentSample;
	private String shippingMark;
	private String consignee;
	private Double totalMoney;
	private Date revisedTime;
	private Date addTime;
	private Date shipmentDate;
	private Date deliveryDate;
	private String ywy;
	private String shr;
	private String orderRemark;
	private String depapture;
	private String destination;
	
	private String checkperson;
	private Integer isCheckAgent;
	private Double commisionScale;
	
	private String typeEnName;
	
	//写入PDF用
	private String approvalCommentsample;
	private String sampleApprove;
	private String approvalCommentsampleOut;
	private String sampleOutApprove;
	private String sampleOutPassed;
	private String sampleOutWash;
	private String confirmBy;

	// Constructors

	public Double getCommisionScale() {
		return commisionScale;
	}

	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}

	/** default constructor */
	public VPurchaseOrder() {
	}

	/** minimal constructor */
	public VPurchaseOrder(Integer id, String pay, String seller,
			String agent, String buyer, String quality, String colours,
			String saleUnit, String handleUnit, String assortment,
			String commnets, String hd, String booking,
			Date colorApprovalDeadline, Date prepruductionSample,
			Date artworkApproval, Date photoSample, Date shipmentSample,
			String shippingMark, String consignee, String shr,
			String orderRemark, String depapture) {
		this.id = id;
		this.pay = pay;
		this.seller = seller;
		this.agent = agent;
		this.buyer = buyer;
		this.quality = quality;
		this.colours = colours;
		this.saleUnit = saleUnit;
		this.handleUnit = handleUnit;
		this.assortment = assortment;
		this.commnets = commnets;
		this.hd = hd;
		this.booking = booking;
		this.colorApprovalDeadline = colorApprovalDeadline;
		this.prepruductionSample = prepruductionSample;
		this.artworkApproval = artworkApproval;
		this.photoSample = photoSample;
		this.shipmentSample = shipmentSample;
		this.shippingMark = shippingMark;
		this.consignee = consignee;
		this.shr = shr;
		this.orderRemark = orderRemark;
		this.depapture = depapture;
	}

	/** full constructor */
	public VPurchaseOrder(Integer id, byte[] companyLogo, String orderNo,
			String poNo, String jgtk, String moneyType, String pay,
			String seller, String agent, String buyer, String quality,
			String colours, String saleUnit, String handleUnit,
			String assortment, String commnets, String hd, String booking,
			Date colorApprovalDeadline, Date prepruductionSample,
			Date artworkApproval, Date photoSample, Date shipmentSample,
			String shippingMark, String consignee, Double totalMoney,
			Date revisedTime, Date addTime, Date shipmentDate,
			Date deliveryDate, String ywy, String shr, String orderRemark,
			String depapture, String destination) {
		this.id = id;
		this.companyLogo = companyLogo;
		this.orderNo = orderNo;
		this.poNo = poNo;
		this.jgtk = jgtk;
		this.moneyType = moneyType;
		this.pay = pay;
		this.seller = seller;
		this.agent = agent;
		this.buyer = buyer;
		this.quality = quality;
		this.colours = colours;
		this.saleUnit = saleUnit;
		this.handleUnit = handleUnit;
		this.assortment = assortment;
		this.commnets = commnets;
		this.hd = hd;
		this.booking = booking;
		this.colorApprovalDeadline = colorApprovalDeadline;
		this.prepruductionSample = prepruductionSample;
		this.artworkApproval = artworkApproval;
		this.photoSample = photoSample;
		this.shipmentSample = shipmentSample;
		this.shippingMark = shippingMark;
		this.consignee = consignee;
		this.totalMoney = totalMoney;
		this.revisedTime = revisedTime;
		this.addTime = addTime;
		this.shipmentDate = shipmentDate;
		this.deliveryDate = deliveryDate;
		this.ywy = ywy;
		this.shr = shr;
		this.orderRemark = orderRemark;
		this.depapture = depapture;
		this.destination = destination;
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

	public String getJgtk() {
		return this.jgtk;
	}

	public void setJgtk(String jgtk) {
		this.jgtk = jgtk;
	}

	public String getMoneyType() {
		return this.moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	public String getPay() {
		return this.pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
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

	public String getHd() {
		return this.hd;
	}

	public void setHd(String hd) {
		this.hd = hd;
	}

	public String getBooking() {
		return this.booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}

	public Date getColorApprovalDeadline() {
		return this.colorApprovalDeadline;
	}

	public void setColorApprovalDeadline(Date colorApprovalDeadline) {
		this.colorApprovalDeadline = colorApprovalDeadline;
	}

	public Date getPrepruductionSample() {
		return this.prepruductionSample;
	}

	public void setPrepruductionSample(Date prepruductionSample) {
		this.prepruductionSample = prepruductionSample;
	}

	public Date getArtworkApproval() {
		return this.artworkApproval;
	}

	public void setArtworkApproval(Date artworkApproval) {
		this.artworkApproval = artworkApproval;
	}

	public Date getPhotoSample() {
		return this.photoSample;
	}

	public void setPhotoSample(Date photoSample) {
		this.photoSample = photoSample;
	}

	public Date getShipmentSample() {
		return this.shipmentSample;
	}

	public void setShipmentSample(Date shipmentSample) {
		this.shipmentSample = shipmentSample;
	}

	public String getShippingMark() {
		return this.shippingMark;
	}

	public void setShippingMark(String shippingMark) {
		this.shippingMark = shippingMark;
	}

	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Double getTotalMoney() {
		return this.totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Date getRevisedTime() {
		return this.revisedTime;
	}

	public void setRevisedTime(Date revisedTime) {
		this.revisedTime = revisedTime;
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

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getYwy() {
		return this.ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}

	public String getShr() {
		return this.shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getOrderRemark() {
		return this.orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VPurchaseOrder))
			return false;
		VPurchaseOrder castOther = (VPurchaseOrder) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getCompanyLogo() == castOther.getCompanyLogo()) || (this
						.getCompanyLogo() != null
						&& castOther.getCompanyLogo() != null && this
						.getCompanyLogo().equals(castOther.getCompanyLogo())))
				&& ((this.getOrderNo() == castOther.getOrderNo()) || (this
						.getOrderNo() != null
						&& castOther.getOrderNo() != null && this.getOrderNo()
						.equals(castOther.getOrderNo())))
				&& ((this.getPoNo() == castOther.getPoNo()) || (this.getPoNo() != null
						&& castOther.getPoNo() != null && this.getPoNo()
						.equals(castOther.getPoNo())))
				&& ((this.getJgtk() == castOther.getJgtk()) || (this.getJgtk() != null
						&& castOther.getJgtk() != null && this.getJgtk()
						.equals(castOther.getJgtk())))
				&& ((this.getMoneyType() == castOther.getMoneyType()) || (this
						.getMoneyType() != null
						&& castOther.getMoneyType() != null && this
						.getMoneyType().equals(castOther.getMoneyType())))
				&& ((this.getPay() == castOther.getPay()) || (this.getPay() != null
						&& castOther.getPay() != null && this.getPay().equals(
						castOther.getPay())))
				&& ((this.getSeller() == castOther.getSeller()) || (this
						.getSeller() != null
						&& castOther.getSeller() != null && this.getSeller()
						.equals(castOther.getSeller())))
				&& ((this.getAgent() == castOther.getAgent()) || (this
						.getAgent() != null
						&& castOther.getAgent() != null && this.getAgent()
						.equals(castOther.getAgent())))
				&& ((this.getBuyer() == castOther.getBuyer()) || (this
						.getBuyer() != null
						&& castOther.getBuyer() != null && this.getBuyer()
						.equals(castOther.getBuyer())))
				&& ((this.getQuality() == castOther.getQuality()) || (this
						.getQuality() != null
						&& castOther.getQuality() != null && this.getQuality()
						.equals(castOther.getQuality())))
				&& ((this.getColours() == castOther.getColours()) || (this
						.getColours() != null
						&& castOther.getColours() != null && this.getColours()
						.equals(castOther.getColours())))
				&& ((this.getSaleUnit() == castOther.getSaleUnit()) || (this
						.getSaleUnit() != null
						&& castOther.getSaleUnit() != null && this
						.getSaleUnit().equals(castOther.getSaleUnit())))
				&& ((this.getHandleUnit() == castOther.getHandleUnit()) || (this
						.getHandleUnit() != null
						&& castOther.getHandleUnit() != null && this
						.getHandleUnit().equals(castOther.getHandleUnit())))
				&& ((this.getAssortment() == castOther.getAssortment()) || (this
						.getAssortment() != null
						&& castOther.getAssortment() != null && this
						.getAssortment().equals(castOther.getAssortment())))
				&& ((this.getCommnets() == castOther.getCommnets()) || (this
						.getCommnets() != null
						&& castOther.getCommnets() != null && this
						.getCommnets().equals(castOther.getCommnets())))
				&& ((this.getHd() == castOther.getHd()) || (this.getHd() != null
						&& castOther.getHd() != null && this.getHd().equals(
						castOther.getHd())))
				&& ((this.getBooking() == castOther.getBooking()) || (this
						.getBooking() != null
						&& castOther.getBooking() != null && this.getBooking()
						.equals(castOther.getBooking())))
				&& ((this.getColorApprovalDeadline() == castOther
						.getColorApprovalDeadline()) || (this
						.getColorApprovalDeadline() != null
						&& castOther.getColorApprovalDeadline() != null && this
						.getColorApprovalDeadline().equals(
								castOther.getColorApprovalDeadline())))
				&& ((this.getPrepruductionSample() == castOther
						.getPrepruductionSample()) || (this
						.getPrepruductionSample() != null
						&& castOther.getPrepruductionSample() != null && this
						.getPrepruductionSample().equals(
								castOther.getPrepruductionSample())))
				&& ((this.getArtworkApproval() == castOther
						.getArtworkApproval()) || (this.getArtworkApproval() != null
						&& castOther.getArtworkApproval() != null && this
						.getArtworkApproval().equals(
								castOther.getArtworkApproval())))
				&& ((this.getPhotoSample() == castOther.getPhotoSample()) || (this
						.getPhotoSample() != null
						&& castOther.getPhotoSample() != null && this
						.getPhotoSample().equals(castOther.getPhotoSample())))
				&& ((this.getShipmentSample() == castOther.getShipmentSample()) || (this
						.getShipmentSample() != null
						&& castOther.getShipmentSample() != null && this
						.getShipmentSample().equals(
								castOther.getShipmentSample())))
				&& ((this.getShippingMark() == castOther.getShippingMark()) || (this
						.getShippingMark() != null
						&& castOther.getShippingMark() != null && this
						.getShippingMark().equals(castOther.getShippingMark())))
				&& ((this.getConsignee() == castOther.getConsignee()) || (this
						.getConsignee() != null
						&& castOther.getConsignee() != null && this
						.getConsignee().equals(castOther.getConsignee())))
				&& ((this.getTotalMoney() == castOther.getTotalMoney()) || (this
						.getTotalMoney() != null
						&& castOther.getTotalMoney() != null && this
						.getTotalMoney().equals(castOther.getTotalMoney())))
				&& ((this.getRevisedTime() == castOther.getRevisedTime()) || (this
						.getRevisedTime() != null
						&& castOther.getRevisedTime() != null && this
						.getRevisedTime().equals(castOther.getRevisedTime())))
				&& ((this.getAddTime() == castOther.getAddTime()) || (this
						.getAddTime() != null
						&& castOther.getAddTime() != null && this.getAddTime()
						.equals(castOther.getAddTime())))
				&& ((this.getShipmentDate() == castOther.getShipmentDate()) || (this
						.getShipmentDate() != null
						&& castOther.getShipmentDate() != null && this
						.getShipmentDate().equals(castOther.getShipmentDate())))
				&& ((this.getDeliveryDate() == castOther.getDeliveryDate()) || (this
						.getDeliveryDate() != null
						&& castOther.getDeliveryDate() != null && this
						.getDeliveryDate().equals(castOther.getDeliveryDate())))
				&& ((this.getYwy() == castOther.getYwy()) || (this.getYwy() != null
						&& castOther.getYwy() != null && this.getYwy().equals(
						castOther.getYwy())))
				&& ((this.getShr() == castOther.getShr()) || (this.getShr() != null
						&& castOther.getShr() != null && this.getShr().equals(
						castOther.getShr())))
				&& ((this.getOrderRemark() == castOther.getOrderRemark()) || (this
						.getOrderRemark() != null
						&& castOther.getOrderRemark() != null && this
						.getOrderRemark().equals(castOther.getOrderRemark())))
				&& ((this.getDepapture() == castOther.getDepapture()) || (this
						.getDepapture() != null
						&& castOther.getDepapture() != null && this
						.getDepapture().equals(castOther.getDepapture())))
				&& ((this.getDestination() == castOther.getDestination()) || (this
						.getDestination() != null
						&& castOther.getDestination() != null && this
						.getDestination().equals(castOther.getDestination())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getCompanyLogo() == null ? 0 : this.getCompanyLogo()
						.hashCode());
		result = 37 * result
				+ (getOrderNo() == null ? 0 : this.getOrderNo().hashCode());
		result = 37 * result
				+ (getPoNo() == null ? 0 : this.getPoNo().hashCode());
		result = 37 * result
				+ (getJgtk() == null ? 0 : this.getJgtk().hashCode());
		result = 37 * result
				+ (getMoneyType() == null ? 0 : this.getMoneyType().hashCode());
		result = 37 * result
				+ (getPay() == null ? 0 : this.getPay().hashCode());
		result = 37 * result
				+ (getSeller() == null ? 0 : this.getSeller().hashCode());
		result = 37 * result
				+ (getAgent() == null ? 0 : this.getAgent().hashCode());
		result = 37 * result
				+ (getBuyer() == null ? 0 : this.getBuyer().hashCode());
		result = 37 * result
				+ (getQuality() == null ? 0 : this.getQuality().hashCode());
		result = 37 * result
				+ (getColours() == null ? 0 : this.getColours().hashCode());
		result = 37 * result
				+ (getSaleUnit() == null ? 0 : this.getSaleUnit().hashCode());
		result = 37
				* result
				+ (getHandleUnit() == null ? 0 : this.getHandleUnit()
						.hashCode());
		result = 37
				* result
				+ (getAssortment() == null ? 0 : this.getAssortment()
						.hashCode());
		result = 37 * result
				+ (getCommnets() == null ? 0 : this.getCommnets().hashCode());
		result = 37 * result + (getHd() == null ? 0 : this.getHd().hashCode());
		result = 37 * result
				+ (getBooking() == null ? 0 : this.getBooking().hashCode());
		result = 37
				* result
				+ (getColorApprovalDeadline() == null ? 0 : this
						.getColorApprovalDeadline().hashCode());
		result = 37
				* result
				+ (getPrepruductionSample() == null ? 0 : this
						.getPrepruductionSample().hashCode());
		result = 37
				* result
				+ (getArtworkApproval() == null ? 0 : this.getArtworkApproval()
						.hashCode());
		result = 37
				* result
				+ (getPhotoSample() == null ? 0 : this.getPhotoSample()
						.hashCode());
		result = 37
				* result
				+ (getShipmentSample() == null ? 0 : this.getShipmentSample()
						.hashCode());
		result = 37
				* result
				+ (getShippingMark() == null ? 0 : this.getShippingMark()
						.hashCode());
		result = 37 * result
				+ (getConsignee() == null ? 0 : this.getConsignee().hashCode());
		result = 37
				* result
				+ (getTotalMoney() == null ? 0 : this.getTotalMoney()
						.hashCode());
		result = 37
				* result
				+ (getRevisedTime() == null ? 0 : this.getRevisedTime()
						.hashCode());
		result = 37 * result
				+ (getAddTime() == null ? 0 : this.getAddTime().hashCode());
		result = 37
				* result
				+ (getShipmentDate() == null ? 0 : this.getShipmentDate()
						.hashCode());
		result = 37
				* result
				+ (getDeliveryDate() == null ? 0 : this.getDeliveryDate()
						.hashCode());
		result = 37 * result
				+ (getYwy() == null ? 0 : this.getYwy().hashCode());
		result = 37 * result
				+ (getShr() == null ? 0 : this.getShr().hashCode());
		result = 37
				* result
				+ (getOrderRemark() == null ? 0 : this.getOrderRemark()
						.hashCode());
		result = 37 * result
				+ (getDepapture() == null ? 0 : this.getDepapture().hashCode());
		result = 37
				* result
				+ (getDestination() == null ? 0 : this.getDestination()
						.hashCode());
		return result;
	}

	public String getCheckperson() {
		return checkperson;
	}

	public void setCheckperson(String checkperson) {
		this.checkperson = checkperson;
	}

	public Integer getIsCheckAgent() {
		return isCheckAgent;
	}

	public void setIsCheckAgent(Integer isCheckAgent) {
		this.isCheckAgent = isCheckAgent;
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