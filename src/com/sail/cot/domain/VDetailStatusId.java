package com.sail.cot.domain;

import java.util.Date;

/**
 * VDetailStatusId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VDetailStatusId implements java.io.Serializable {

	// Fields
	private Integer id;
	private String orderNo;
	private Integer orderId;
	private String orderfacNo;
	private Integer orderfacId;
	private Integer empId;
	private Date loadingApproval;
	private Date loadingDeadline;
	private Date shippingApproval;
	private Date shippingDeadline;
	private Date qcApproval;
	private Date qcDeadline;
	private Date sampleOutApproval;
	private Date sampleOutDeadline;
	private Date samplePicApproval;
	private Date samplePicDeadline;
	private Date pcaketApproval;
	private Date pcaketDeadline;
	private Date facApproval;
	private Date facDeadline;
	private Date completeSampleApproval;
	private Date completeSampleDeadline;
	private Date simpleSampleApproval;
	private Date simpleSampleDeadline;
	private Integer canOut;
	private Date sendTime;
	private String allPinName;//-----Product(产品名称)
	private Date  orderLcDate;//LC有效期   ----Shipment(船期)
	private Integer factoryId;
	private Integer custId;
	private Float povalue;
	private String qcLocation;
	private Date orderLcDelay;
	// Constructors
	private Integer typeLv1Id;//department
	
	private Integer smapleStatus;
	private Integer sampleOutStatus;
	private Integer outStatus;
	private Integer qcStatus;
	private Integer packetStatus;
	private String clientNo;
	
	private Integer flag;//标识是否全部答复
	private Date orderFacText;
	
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
	
	private Integer clauseTypeId;
	
	private Integer shipportId;
	private Integer targetportId;
	
	private Integer chk;//sendTime字段是否显示成红色 (不存在视图)
	
	private Date designTime;
	private Double estConsumedTime;
	private Double estTime;
	private Double travelTime;
	private Double travelConsumedTime;
	private Double totalExpenseUsd;
	private Integer preStaId;//状态标识
	private Integer artStaId;//状态标识
	private Integer shiStaId;//状态标识
	private Integer qcStaId;//状态标识
	private Integer spStaId;//状态标识
	private String qcPerson;
	private Integer nationId;
	private Date etdDate;
	private Integer zheType;
	private Double zheNum;
	private String themeStr;

	public Integer getPreMan() {
		return preMan;
	}

	public void setPreMan(Integer preMan) {
		this.preMan = preMan;
	}

	public Integer getArtMan() {
		return artMan;
	}

	public Integer getPreStaId() {
		return preStaId;
	}

	public void setPreStaId(Integer preStaId) {
		this.preStaId = preStaId;
	}

	public Integer getArtStaId() {
		return artStaId;
	}

	public void setArtStaId(Integer artStaId) {
		this.artStaId = artStaId;
	}

	public Integer getShiStaId() {
		return shiStaId;
	}

	public void setShiStaId(Integer shiStaId) {
		this.shiStaId = shiStaId;
	}

	public Integer getQcStaId() {
		return qcStaId;
	}

	public void setQcStaId(Integer qcStaId) {
		this.qcStaId = qcStaId;
	}

	public Integer getSpStaId() {
		return spStaId;
	}

	public void setSpStaId(Integer spStaId) {
		this.spStaId = spStaId;
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

	public Date getDesignTime() {
		return designTime;
	}

	public void setDesignTime(Date designTime) {
		this.designTime = designTime;
	}

	public void setTargetportId(Integer targetportId) {
		this.targetportId = targetportId;
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

	public Integer getPreNum() {
		return preNum;
	}

	public void setPreNum(Integer preNum) {
		this.preNum = preNum;
	}

	public String getPreText() {
		return preText;
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

	public Date getOrderFacText() {
		return orderFacText;
	}

	public void setOrderFacText(Date orderFacText) {
		this.orderFacText = orderFacText;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getSmapleStatus() {
		return smapleStatus;
	}

	public void setSmapleStatus(Integer smapleStatus) {
		this.smapleStatus = smapleStatus;
	}

	public Integer getSampleOutStatus() {
		return sampleOutStatus;
	}

	public void setSampleOutStatus(Integer sampleOutStatus) {
		this.sampleOutStatus = sampleOutStatus;
	}

	public Integer getOutStatus() {
		return outStatus;
	}

	public void setOutStatus(Integer outStatus) {
		this.outStatus = outStatus;
	}

	public Integer getQcStatus() {
		return qcStatus;
	}

	public void setQcStatus(Integer qcStatus) {
		this.qcStatus = qcStatus;
	}

	public Integer getPacketStatus() {
		return packetStatus;
	}

	public void setPacketStatus(Integer packetStatus) {
		this.packetStatus = packetStatus;
	}

	public Integer getTypeLv1Id() {
		return typeLv1Id;
	}

	public void setTypeLv1Id(Integer typeLv1Id) {
		this.typeLv1Id = typeLv1Id;
	}

	public String getQcLocation() {
		return qcLocation;
	}

	public void setQcLocation(String qcLocation) {
		this.qcLocation = qcLocation;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getAllPinName() {
		return allPinName;
	}

	public void setAllPinName(String allPinName) {
		this.allPinName = allPinName;
	}

	public Date getOrderLcDate() {
		return orderLcDate;
	}

	public void setOrderLcDate(Date orderLcDate) {
		this.orderLcDate = orderLcDate;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	/** default constructor */
	public VDetailStatusId() {
	}

	/** minimal constructor */
	public VDetailStatusId(Integer orderId, Integer orderfacId) {
		this.orderId = orderId;
		this.orderfacId = orderfacId;
	}

	/** full constructor */
	public VDetailStatusId(String orderNo, Integer orderId, String orderfacNo,
			Integer orderfacId, Integer empId, Date loadingApproval,
			Date loadingDeadline, Date shippingApproval, Date shippingDeadline,
			Date qcApproval, Date qcDeadline, Date sampleOutApproval,
			Date sampleOutDeadline, Date samplePicApproval,
			Date samplePicDeadline, Date pcaketApproval, Date pcaketDeadline,
			Date facApproval, Date facDeadline, Date completeSampleApproval,
			Date completeSampleDeadline, Date simpleSampleApproval,
			Date simpleSampleDeadline) {
		this.orderNo = orderNo;
		this.orderId = orderId;
		this.orderfacNo = orderfacNo;
		this.orderfacId = orderfacId;
		this.empId = empId;
		this.loadingApproval = loadingApproval;
		this.loadingDeadline = loadingDeadline;
		this.shippingApproval = shippingApproval;
		this.shippingDeadline = shippingDeadline;
		this.qcApproval = qcApproval;
		this.qcDeadline = qcDeadline;
		this.sampleOutApproval = sampleOutApproval;
		this.sampleOutDeadline = sampleOutDeadline;
		this.samplePicApproval = samplePicApproval;
		this.samplePicDeadline = samplePicDeadline;
		this.pcaketApproval = pcaketApproval;
		this.pcaketDeadline = pcaketDeadline;
		this.facApproval = facApproval;
		this.facDeadline = facDeadline;
		this.completeSampleApproval = completeSampleApproval;
		this.completeSampleDeadline = completeSampleDeadline;
		this.simpleSampleApproval = simpleSampleApproval;
		this.simpleSampleDeadline = simpleSampleDeadline;
		
	}

	// Property accessors

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

	public String getOrderfacNo() {
		return this.orderfacNo;
	}

	public void setOrderfacNo(String orderfacNo) {
		this.orderfacNo = orderfacNo;
	}

	public Integer getOrderfacId() {
		return this.orderfacId;
	}

	public void setOrderfacId(Integer orderfacId) {
		this.orderfacId = orderfacId;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Date getLoadingApproval() {
		return this.loadingApproval;
	}

	public void setLoadingApproval(Date loadingApproval) {
		this.loadingApproval = loadingApproval;
	}

	public Date getLoadingDeadline() {
		return this.loadingDeadline;
	}

	public void setLoadingDeadline(Date loadingDeadline) {
		this.loadingDeadline = loadingDeadline;
	}

	public Date getShippingApproval() {
		return this.shippingApproval;
	}

	public void setShippingApproval(Date shippingApproval) {
		this.shippingApproval = shippingApproval;
	}

	public Date getShippingDeadline() {
		return this.shippingDeadline;
	}

	public void setShippingDeadline(Date shippingDeadline) {
		this.shippingDeadline = shippingDeadline;
	}

	public Date getQcApproval() {
		return this.qcApproval;
	}

	public void setQcApproval(Date qcApproval) {
		this.qcApproval = qcApproval;
	}

	public Date getQcDeadline() {
		return this.qcDeadline;
	}

	public void setQcDeadline(Date qcDeadline) {
		this.qcDeadline = qcDeadline;
	}

	public Date getSampleOutApproval() {
		return this.sampleOutApproval;
	}

	public void setSampleOutApproval(Date sampleOutApproval) {
		this.sampleOutApproval = sampleOutApproval;
	}

	public Date getSampleOutDeadline() {
		return this.sampleOutDeadline;
	}

	public void setSampleOutDeadline(Date sampleOutDeadline) {
		this.sampleOutDeadline = sampleOutDeadline;
	}

	public Date getSamplePicApproval() {
		return this.samplePicApproval;
	}

	public void setSamplePicApproval(Date samplePicApproval) {
		this.samplePicApproval = samplePicApproval;
	}

	public Date getSamplePicDeadline() {
		return this.samplePicDeadline;
	}

	public void setSamplePicDeadline(Date samplePicDeadline) {
		this.samplePicDeadline = samplePicDeadline;
	}

	public Date getPcaketApproval() {
		return this.pcaketApproval;
	}

	public void setPcaketApproval(Date pcaketApproval) {
		this.pcaketApproval = pcaketApproval;
	}

	public Date getPcaketDeadline() {
		return this.pcaketDeadline;
	}

	public void setPcaketDeadline(Date pcaketDeadline) {
		this.pcaketDeadline = pcaketDeadline;
	}

	public Date getFacApproval() {
		return this.facApproval;
	}

	public void setFacApproval(Date facApproval) {
		this.facApproval = facApproval;
	}

	public Date getFacDeadline() {
		return this.facDeadline;
	}

	public void setFacDeadline(Date facDeadline) {
		this.facDeadline = facDeadline;
	}

	public Date getCompleteSampleApproval() {
		return this.completeSampleApproval;
	}

	public void setCompleteSampleApproval(Date completeSampleApproval) {
		this.completeSampleApproval = completeSampleApproval;
	}

	public Date getCompleteSampleDeadline() {
		return this.completeSampleDeadline;
	}

	public void setCompleteSampleDeadline(Date completeSampleDeadline) {
		this.completeSampleDeadline = completeSampleDeadline;
	}

	public Date getSimpleSampleApproval() {
		return this.simpleSampleApproval;
	}

	public void setSimpleSampleApproval(Date simpleSampleApproval) {
		this.simpleSampleApproval = simpleSampleApproval;
	}

	public Date getSimpleSampleDeadline() {
		return this.simpleSampleDeadline;
	}

	public void setSimpleSampleDeadline(Date simpleSampleDeadline) {
		this.simpleSampleDeadline = simpleSampleDeadline;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VDetailStatusId))
			return false;
		VDetailStatusId castOther = (VDetailStatusId) other;

		return ((this.getOrderNo() == castOther.getOrderNo()) || (this
				.getOrderNo() != null
				&& castOther.getOrderNo() != null && this.getOrderNo().equals(
				castOther.getOrderNo())))
				&& ((this.getOrderId() == castOther.getOrderId()) || (this
						.getOrderId() != null
						&& castOther.getOrderId() != null && this.getOrderId()
						.equals(castOther.getOrderId())))
				&& ((this.getOrderfacNo() == castOther.getOrderfacNo()) || (this
						.getOrderfacNo() != null
						&& castOther.getOrderfacNo() != null && this
						.getOrderfacNo().equals(castOther.getOrderfacNo())))
				&& ((this.getOrderfacId() == castOther.getOrderfacId()) || (this
						.getOrderfacId() != null
						&& castOther.getOrderfacId() != null && this
						.getOrderfacId().equals(castOther.getOrderfacId())))
				&& ((this.getEmpId() == castOther.getEmpId()) || (this
						.getEmpId() != null
						&& castOther.getEmpId() != null && this.getEmpId()
						.equals(castOther.getEmpId())))
				&& ((this.getLoadingApproval() == castOther
						.getLoadingApproval()) || (this.getLoadingApproval() != null
						&& castOther.getLoadingApproval() != null && this
						.getLoadingApproval().equals(
								castOther.getLoadingApproval())))
				&& ((this.getLoadingDeadline() == castOther
						.getLoadingDeadline()) || (this.getLoadingDeadline() != null
						&& castOther.getLoadingDeadline() != null && this
						.getLoadingDeadline().equals(
								castOther.getLoadingDeadline())))
				&& ((this.getShippingApproval() == castOther
						.getShippingApproval()) || (this.getShippingApproval() != null
						&& castOther.getShippingApproval() != null && this
						.getShippingApproval().equals(
								castOther.getShippingApproval())))
				&& ((this.getShippingDeadline() == castOther
						.getShippingDeadline()) || (this.getShippingDeadline() != null
						&& castOther.getShippingDeadline() != null && this
						.getShippingDeadline().equals(
								castOther.getShippingDeadline())))
				&& ((this.getQcApproval() == castOther.getQcApproval()) || (this
						.getQcApproval() != null
						&& castOther.getQcApproval() != null && this
						.getQcApproval().equals(castOther.getQcApproval())))
				&& ((this.getQcDeadline() == castOther.getQcDeadline()) || (this
						.getQcDeadline() != null
						&& castOther.getQcDeadline() != null && this
						.getQcDeadline().equals(castOther.getQcDeadline())))
				&& ((this.getSampleOutApproval() == castOther
						.getSampleOutApproval()) || (this
						.getSampleOutApproval() != null
						&& castOther.getSampleOutApproval() != null && this
						.getSampleOutApproval().equals(
								castOther.getSampleOutApproval())))
				&& ((this.getSampleOutDeadline() == castOther
						.getSampleOutDeadline()) || (this
						.getSampleOutDeadline() != null
						&& castOther.getSampleOutDeadline() != null && this
						.getSampleOutDeadline().equals(
								castOther.getSampleOutDeadline())))
				&& ((this.getSamplePicApproval() == castOther
						.getSamplePicApproval()) || (this
						.getSamplePicApproval() != null
						&& castOther.getSamplePicApproval() != null && this
						.getSamplePicApproval().equals(
								castOther.getSamplePicApproval())))
				&& ((this.getSamplePicDeadline() == castOther
						.getSamplePicDeadline()) || (this
						.getSamplePicDeadline() != null
						&& castOther.getSamplePicDeadline() != null && this
						.getSamplePicDeadline().equals(
								castOther.getSamplePicDeadline())))
				&& ((this.getPcaketApproval() == castOther.getPcaketApproval()) || (this
						.getPcaketApproval() != null
						&& castOther.getPcaketApproval() != null && this
						.getPcaketApproval().equals(
								castOther.getPcaketApproval())))
				&& ((this.getPcaketDeadline() == castOther.getPcaketDeadline()) || (this
						.getPcaketDeadline() != null
						&& castOther.getPcaketDeadline() != null && this
						.getPcaketDeadline().equals(
								castOther.getPcaketDeadline())))
				&& ((this.getFacApproval() == castOther.getFacApproval()) || (this
						.getFacApproval() != null
						&& castOther.getFacApproval() != null && this
						.getFacApproval().equals(castOther.getFacApproval())))
				&& ((this.getFacDeadline() == castOther.getFacDeadline()) || (this
						.getFacDeadline() != null
						&& castOther.getFacDeadline() != null && this
						.getFacDeadline().equals(castOther.getFacDeadline())))
				&& ((this.getCompleteSampleApproval() == castOther
						.getCompleteSampleApproval()) || (this
						.getCompleteSampleApproval() != null
						&& castOther.getCompleteSampleApproval() != null && this
						.getCompleteSampleApproval().equals(
								castOther.getCompleteSampleApproval())))
				&& ((this.getCompleteSampleDeadline() == castOther
						.getCompleteSampleDeadline()) || (this
						.getCompleteSampleDeadline() != null
						&& castOther.getCompleteSampleDeadline() != null && this
						.getCompleteSampleDeadline().equals(
								castOther.getCompleteSampleDeadline())))
				&& ((this.getSimpleSampleApproval() == castOther
						.getSimpleSampleApproval()) || (this
						.getSimpleSampleApproval() != null
						&& castOther.getSimpleSampleApproval() != null && this
						.getSimpleSampleApproval().equals(
								castOther.getSimpleSampleApproval())))
				&& ((this.getSimpleSampleDeadline() == castOther
						.getSimpleSampleDeadline()) || (this
						.getSimpleSampleDeadline() != null
						&& castOther.getSimpleSampleDeadline() != null && this
						.getSimpleSampleDeadline().equals(
								castOther.getSimpleSampleDeadline())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrderNo() == null ? 0 : this.getOrderNo().hashCode());
		result = 37 * result
				+ (getOrderId() == null ? 0 : this.getOrderId().hashCode());
		result = 37
				* result
				+ (getOrderfacNo() == null ? 0 : this.getOrderfacNo()
						.hashCode());
		result = 37
				* result
				+ (getOrderfacId() == null ? 0 : this.getOrderfacId()
						.hashCode());
		result = 37 * result
				+ (getEmpId() == null ? 0 : this.getEmpId().hashCode());
		result = 37
				* result
				+ (getLoadingApproval() == null ? 0 : this.getLoadingApproval()
						.hashCode());
		result = 37
				* result
				+ (getLoadingDeadline() == null ? 0 : this.getLoadingDeadline()
						.hashCode());
		result = 37
				* result
				+ (getShippingApproval() == null ? 0 : this
						.getShippingApproval().hashCode());
		result = 37
				* result
				+ (getShippingDeadline() == null ? 0 : this
						.getShippingDeadline().hashCode());
		result = 37
				* result
				+ (getQcApproval() == null ? 0 : this.getQcApproval()
						.hashCode());
		result = 37
				* result
				+ (getQcDeadline() == null ? 0 : this.getQcDeadline()
						.hashCode());
		result = 37
				* result
				+ (getSampleOutApproval() == null ? 0 : this
						.getSampleOutApproval().hashCode());
		result = 37
				* result
				+ (getSampleOutDeadline() == null ? 0 : this
						.getSampleOutDeadline().hashCode());
		result = 37
				* result
				+ (getSamplePicApproval() == null ? 0 : this
						.getSamplePicApproval().hashCode());
		result = 37
				* result
				+ (getSamplePicDeadline() == null ? 0 : this
						.getSamplePicDeadline().hashCode());
		result = 37
				* result
				+ (getPcaketApproval() == null ? 0 : this.getPcaketApproval()
						.hashCode());
		result = 37
				* result
				+ (getPcaketDeadline() == null ? 0 : this.getPcaketDeadline()
						.hashCode());
		result = 37
				* result
				+ (getFacApproval() == null ? 0 : this.getFacApproval()
						.hashCode());
		result = 37
				* result
				+ (getFacDeadline() == null ? 0 : this.getFacDeadline()
						.hashCode());
		result = 37
				* result
				+ (getCompleteSampleApproval() == null ? 0 : this
						.getCompleteSampleApproval().hashCode());
		result = 37
				* result
				+ (getCompleteSampleDeadline() == null ? 0 : this
						.getCompleteSampleDeadline().hashCode());
		result = 37
				* result
				+ (getSimpleSampleApproval() == null ? 0 : this
						.getSimpleSampleApproval().hashCode());
		result = 37
				* result
				+ (getSimpleSampleDeadline() == null ? 0 : this
						.getSimpleSampleDeadline().hashCode());
		return result;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCanOut() {
		return canOut;
	}

	public void setCanOut(Integer canOut) {
		this.canOut = canOut;
	}

	public Float getPovalue() {
		return povalue;
	}

	public void setPovalue(Float povalue) {
		this.povalue = povalue;
	}


	public Date getOrderLcDelay() {
		return orderLcDelay;
	}

	public void setOrderLcDelay(Date orderLcDelay) {
		this.orderLcDelay = orderLcDelay;
	}

	public String getClientNo() {
		return clientNo;
	}

	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}

	public Integer getClauseTypeId() {
		return clauseTypeId;
	}

	public void setClauseTypeId(Integer clauseTypeId) {
		this.clauseTypeId = clauseTypeId;
	}

	public Integer getChk() {
		return chk;
	}

	public void setChk(Integer chk) {
		this.chk = chk;
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

	public Date getEtdDate() {
		return etdDate;
	}

	public void setEtdDate(Date etdDate) {
		this.etdDate = etdDate;
	}

	public Integer getZheType() {
		return zheType;
	}

	public void setZheType(Integer zheType) {
		this.zheType = zheType;
	}

	public Double getZheNum() {
		return zheNum;
	}

	public void setZheNum(Double zheNum) {
		this.zheNum = zheNum;
	}

	public String getThemeStr() {
		return themeStr;
	}

	public void setThemeStr(String themeStr) {
		this.themeStr = themeStr;
	}
	
}