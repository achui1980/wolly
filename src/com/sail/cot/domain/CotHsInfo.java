package com.sail.cot.domain;

import java.util.Date;

/**
 * CotHsInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotHsInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer tradeTypeId;
	private Integer orderOutId;
	private Integer taxTypeId;
	private String hsNo;
	private String backupNo;//提单
	private Date applyDate;//进舱时间
	private String licenceNo;//船次
	private Double shipmentCharge;
	private Double insuranceCharge;
	private Double otherCharge;
	private String resourceArea;//停靠码头
	private String targetArea;//目的地
	private String containerNum;//装柜
	private String containerNo;//排载备注
	private String plumbumNo;//订舱编号
	private Integer hsBusinessPerson;
	private Date reclaimDate;//开船时间
	private Date returnDate;//到港时间
	private Integer hsTrafficId;
	private Integer hsNateId;
	private Integer payTypeId;
	private Integer transportPayId;//运费付款方式(0到付,1预付)
	
	private Integer hsCompanyId;//报关公司
	private Integer trailerId;//拖车公司
	private Integer shipId;//船公司
	private Integer hsConsignCompanyId;//操作货代
	private Integer consignCompanyId;//指定货代
	
	private String hsCompanyStr;//报关公司详细
	private String trailerStr;//拖车公司详细
	private String shipCompanyStr;//船公司文本
	private String hsConsignCompanyStr;//操作货代文本
	private String consignCompanyStr;//指定货代文本
	private String hsConsignRemark;//报关公司备注
	private String consignCompanyRemark;//拖车公司备注
	
	private String shipName;//船名
	private String dingCangNo;//订舱编号
	
	private Date giveTime;//截关时间
	private String giveRemark;//订舱备注
	
	private Integer shipportId;//起运港
	private Integer midportId;//中转港
	private Integer targetportId;//目的港

	// Constructors

	public String getDingCangNo() {
		return dingCangNo;
	}

	public void setDingCangNo(String dingCangNo) {
		this.dingCangNo = dingCangNo;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	/** default constructor */
	public CotHsInfo() {
	}

	 

	/** full constructor */
	public CotHsInfo(CotTradeType cotTradeType, CotHsCompany cotHsCompany,
			CotOrderOut cotOrderOut, CotConsignCompany cotConsignCompany,
			CotTaxType cotTaxType, String hsNo, String backupNo,
			Date applyDate, String licenceNo, Double shipmentCharge,
			Double insuranceCharge, Double otherCharge, String resourceArea,
			String containerNum, String containerNo, String plumbumNo,
			Integer hsBusinessPerson, Date reclaimDate, Date returnDate,
			Integer hsTrafficId, Integer hsNateId, Integer payTypeId) {

		this.hsNo = hsNo;
		this.backupNo = backupNo;
		this.applyDate = applyDate;
		this.licenceNo = licenceNo;
		this.shipmentCharge = shipmentCharge;
		this.insuranceCharge = insuranceCharge;
		this.otherCharge = otherCharge;
		this.resourceArea = resourceArea;
		this.containerNum = containerNum;
		this.containerNo = containerNo;
		this.plumbumNo = plumbumNo;
		this.hsBusinessPerson = hsBusinessPerson;
		this.reclaimDate = reclaimDate;
		this.returnDate = returnDate;
		this.hsTrafficId = hsTrafficId;
		this.hsNateId = hsNateId;
		this.payTypeId = payTypeId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHsNo() {
		return this.hsNo;
	}

	public void setHsNo(String hsNo) {
		this.hsNo = hsNo;
	}

	public String getBackupNo() {
		return this.backupNo;
	}

	public void setBackupNo(String backupNo) {
		this.backupNo = backupNo;
	}

	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getLicenceNo() {
		return this.licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	public Double getShipmentCharge() {
		return this.shipmentCharge;
	}

	public void setShipmentCharge(Double shipmentCharge) {
		this.shipmentCharge = shipmentCharge;
	}

	public Double getInsuranceCharge() {
		return this.insuranceCharge;
	}

	public void setInsuranceCharge(Double insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}

	public Double getOtherCharge() {
		return this.otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

	public String getResourceArea() {
		return this.resourceArea;
	}

	public void setResourceArea(String resourceArea) {
		this.resourceArea = resourceArea;
	}

	public String getContainerNum() {
		return this.containerNum;
	}

	public void setContainerNum(String containerNum) {
		this.containerNum = containerNum;
	}

	public String getContainerNo() {
		return this.containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getPlumbumNo() {
		return this.plumbumNo;
	}

	public void setPlumbumNo(String plumbumNo) {
		this.plumbumNo = plumbumNo;
	}

	public Integer getHsBusinessPerson() {
		return hsBusinessPerson;
	}

	public void setHsBusinessPerson(Integer hsBusinessPerson) {
		this.hsBusinessPerson = hsBusinessPerson;
	}

	public Date getReclaimDate() {
		return this.reclaimDate;
	}

	public void setReclaimDate(Date reclaimDate) {
		this.reclaimDate = reclaimDate;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Integer getHsTrafficId() {
		return hsTrafficId;
	}

	public void setHsTrafficId(Integer hsTrafficId) {
		this.hsTrafficId = hsTrafficId;
	}

	public Integer getHsNateId() {
		return hsNateId;
	}

	public void setHsNateId(Integer hsNateId) {
		this.hsNateId = hsNateId;
	}

	public Integer getPayTypeId() {
		return this.payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public Integer getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(Integer tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public Integer getHsCompanyId() {
		return hsCompanyId;
	}

	public void setHsCompanyId(Integer hsCompanyId) {
		this.hsCompanyId = hsCompanyId;
	}

	public Integer getOrderOutId() {
		return orderOutId;
	}

	public void setOrderOutId(Integer orderOutId) {
		this.orderOutId = orderOutId;
	}

	public Integer getHsConsignCompanyId() {
		return hsConsignCompanyId;
	}

	public void setHsConsignCompanyId(Integer hsConsignCompanyId) {
		this.hsConsignCompanyId = hsConsignCompanyId;
	}

	public Integer getTaxTypeId() {
		return taxTypeId;
	}

	public void setTaxTypeId(Integer taxTypeId) {
		this.taxTypeId = taxTypeId;
	}



	public Integer getTransportPayId() {
		return transportPayId;
	}



	public void setTransportPayId(Integer transportPayId) {
		this.transportPayId = transportPayId;
	}



	public Integer getTrailerId() {
		return trailerId;
	}



	public void setTrailerId(Integer trailerId) {
		this.trailerId = trailerId;
	}



	public Integer getConsignCompanyId() {
		return consignCompanyId;
	}



	public void setConsignCompanyId(Integer consignCompanyId) {
		this.consignCompanyId = consignCompanyId;
	}



	public String getHsCompanyStr() {
		return hsCompanyStr;
	}



	public void setHsCompanyStr(String hsCompanyStr) {
		this.hsCompanyStr = hsCompanyStr;
	}



	public String getTrailerStr() {
		return trailerStr;
	}



	public void setTrailerStr(String trailerStr) {
		this.trailerStr = trailerStr;
	}



	public String getHsConsignCompanyStr() {
		return hsConsignCompanyStr;
	}



	public void setHsConsignCompanyStr(String hsConsignCompanyStr) {
		this.hsConsignCompanyStr = hsConsignCompanyStr;
	}



	public String getConsignCompanyStr() {
		return consignCompanyStr;
	}



	public void setConsignCompanyStr(String consignCompanyStr) {
		this.consignCompanyStr = consignCompanyStr;
	}

	public Integer getShipId() {
		return shipId;
	}

	public void setShipId(Integer shipId) {
		this.shipId = shipId;
	}

	public String getShipCompanyStr() {
		return shipCompanyStr;
	}

	public void setShipCompanyStr(String shipCompanyStr) {
		this.shipCompanyStr = shipCompanyStr;
	}

	public String getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(String targetArea) {
		this.targetArea = targetArea;
	}

	public Date getGiveTime() {
		return giveTime;
	}

	public void setGiveTime(Date giveTime) {
		this.giveTime = giveTime;
	}

	public String getGiveRemark() {
		return giveRemark;
	}

	public void setGiveRemark(String giveRemark) {
		this.giveRemark = giveRemark;
	}

	public Integer getShipportId() {
		return shipportId;
	}

	public void setShipportId(Integer shipportId) {
		this.shipportId = shipportId;
	}

	public Integer getMidportId() {
		return midportId;
	}

	public void setMidportId(Integer midportId) {
		this.midportId = midportId;
	}

	public Integer getTargetportId() {
		return targetportId;
	}

	public void setTargetportId(Integer targetportId) {
		this.targetportId = targetportId;
	}

	public String getHsConsignRemark() {
		return hsConsignRemark;
	}

	public void setHsConsignRemark(String hsConsignRemark) {
		this.hsConsignRemark = hsConsignRemark;
	}

	public String getConsignCompanyRemark() {
		return consignCompanyRemark;
	}

	public void setConsignCompanyRemark(String consignCompanyRemark) {
		this.consignCompanyRemark = consignCompanyRemark;
	}
	
}