package com.sail.cot.domain;

import java.util.Date;

/**
 * VOrderOrderfacStatusId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VOrderOrderfacStatusId implements java.io.Serializable {

	// Fields

	private String customerShortName;
	private String orderNo;
	private String orderFacNo;
	private Integer orderfacId;
	private Date orderTime;
	private Integer id;
	private Integer orderId;
	private Integer smapleStatus;
	private Integer sampleOutStatus;
	private Integer outStatus;
	private Integer qcStatus;
	private Integer packetStatus;
	private Integer empId;
	private Integer companyId;
	private Integer deptId;
	private String empsName;
	private Integer custId;
	private Integer canOut;
	private String allPinName;
	private String poNo;
	private Date orderLcDate;
	private Date orderLcDelay;
	private Integer clauseTypeId;
	private Integer payTypeId;
	private Integer facId;
	private Integer currencyId;
	private Integer currencyIdPO;

	private Float orderTotal;
	private Float orderFacTotal;
	private Date sendTime;
	private Date qcDeadline;
	private Date qcApproval;
	// Constructors
	private Integer typeLv1Id;//department
	
	private Integer shipportId;
	private Integer targetportId;
	private Integer nationId;
	private String themeStr;
	
	//2013-1-11
	private Integer staId;//状态标识
	private String  staMark;//状态备注
	
	private String clientFile;
	private String scFileName;
	
	private Integer chk;//sendTime字段是否显示成红色 (不存在视图)
	public Integer getShipportId() {
		return shipportId;
	}

	public void setShipportId(Integer shipportId) {
		this.shipportId = shipportId;
	}

	public Integer getTargetportId() {
		return targetportId;
	}

	public Integer getStaId() {
		return staId;
	}

	public void setStaId(Integer staId) {
		this.staId = staId;
	}

	public String getStaMark() {
		return staMark;
	}

	public void setStaMark(String staMark) {
		this.staMark = staMark;
	}

	public void setTargetportId(Integer targetportId) {
		this.targetportId = targetportId;
	}

	public Integer getChk() {
		return chk;
	}

	public void setChk(Integer chk) {
		this.chk = chk;
	}

	public Integer getTypeLv1Id() {
		return typeLv1Id;
	}

	public void setTypeLv1Id(Integer typeLv1Id) {
		this.typeLv1Id = typeLv1Id;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Float getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Float orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Float getOrderFacTotal() {
		return orderFacTotal;
	}

	public void setOrderFacTotal(Float orderFacTotal) {
		this.orderFacTotal = orderFacTotal;
	}

	public String getAllPinName() {
		return allPinName;
	}

	public void setAllPinName(String allPinName) {
		this.allPinName = allPinName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Integer getCanOut() {
		return canOut;
	}

	public void setCanOut(Integer canOut) {
		this.canOut = canOut;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	/** default constructor */
	public VOrderOrderfacStatusId() {
	}

	/** minimal constructor */
	public VOrderOrderfacStatusId(Integer id, Integer orderId) {
		this.id = id;
		this.orderId = orderId;
	}

	/** full constructor */
	public VOrderOrderfacStatusId(String customerShortName, String orderNo,
			String orderFacNo, Date orderTime, Integer id, Integer orderId,
			Integer smapleStatus, Integer sampleOutStatus, Integer outStatus,
			Integer qcStatus, Integer packetStatus, Integer empId,
			Integer companyId, Integer deptId, String empsName) {
		this.customerShortName = customerShortName;
		this.orderNo = orderNo;
		this.orderFacNo = orderFacNo;
		this.orderTime = orderTime;
		this.id = id;
		this.orderId = orderId;
		this.smapleStatus = smapleStatus;
		this.sampleOutStatus = sampleOutStatus;
		this.outStatus = outStatus;
		this.qcStatus = qcStatus;
		this.packetStatus = packetStatus;
		this.empId = empId;
		this.companyId = companyId;
		this.deptId = deptId;
		this.empsName = empsName;
	}

	// Property accessors

	public String getCustomerShortName() {
		return this.customerShortName;
	}

	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderFacNo() {
		return this.orderFacNo;
	}

	public void setOrderFacNo(String orderFacNo) {
		this.orderFacNo = orderFacNo;
	}

	public Date getOrderTime() {
		return this.orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getSmapleStatus() {
		return this.smapleStatus;
	}

	public void setSmapleStatus(Integer smapleStatus) {
		this.smapleStatus = smapleStatus;
	}

	public Integer getSampleOutStatus() {
		return this.sampleOutStatus;
	}

	public void setSampleOutStatus(Integer sampleOutStatus) {
		this.sampleOutStatus = sampleOutStatus;
	}

	public Integer getOutStatus() {
		return this.outStatus;
	}

	public void setOutStatus(Integer outStatus) {
		this.outStatus = outStatus;
	}

	public Integer getQcStatus() {
		return this.qcStatus;
	}

	public void setQcStatus(Integer qcStatus) {
		this.qcStatus = qcStatus;
	}

	public Integer getPacketStatus() {
		return this.packetStatus;
	}

	public void setPacketStatus(Integer packetStatus) {
		this.packetStatus = packetStatus;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getEmpsName() {
		return this.empsName;
	}

	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VOrderOrderfacStatusId))
			return false;
		VOrderOrderfacStatusId castOther = (VOrderOrderfacStatusId) other;

		return ((this.getCustomerShortName() == castOther
				.getCustomerShortName()) || (this.getCustomerShortName() != null
				&& castOther.getCustomerShortName() != null && this
				.getCustomerShortName()
				.equals(castOther.getCustomerShortName())))
				&& ((this.getOrderNo() == castOther.getOrderNo()) || (this
						.getOrderNo() != null
						&& castOther.getOrderNo() != null && this.getOrderNo()
						.equals(castOther.getOrderNo())))
				&& ((this.getOrderFacNo() == castOther.getOrderFacNo()) || (this
						.getOrderFacNo() != null
						&& castOther.getOrderFacNo() != null && this
						.getOrderFacNo().equals(castOther.getOrderFacNo())))
				&& ((this.getOrderTime() == castOther.getOrderTime()) || (this
						.getOrderTime() != null
						&& castOther.getOrderTime() != null && this
						.getOrderTime().equals(castOther.getOrderTime())))
				&& ((this.getId() == castOther.getId()) || (this.getId() != null
						&& castOther.getId() != null && this.getId().equals(
						castOther.getId())))
				&& ((this.getOrderId() == castOther.getOrderId()) || (this
						.getOrderId() != null
						&& castOther.getOrderId() != null && this.getOrderId()
						.equals(castOther.getOrderId())))
				&& ((this.getSmapleStatus() == castOther.getSmapleStatus()) || (this
						.getSmapleStatus() != null
						&& castOther.getSmapleStatus() != null && this
						.getSmapleStatus().equals(castOther.getSmapleStatus())))
				&& ((this.getSampleOutStatus() == castOther
						.getSampleOutStatus()) || (this.getSampleOutStatus() != null
						&& castOther.getSampleOutStatus() != null && this
						.getSampleOutStatus().equals(
								castOther.getSampleOutStatus())))
				&& ((this.getOutStatus() == castOther.getOutStatus()) || (this
						.getOutStatus() != null
						&& castOther.getOutStatus() != null && this
						.getOutStatus().equals(castOther.getOutStatus())))
				&& ((this.getQcStatus() == castOther.getQcStatus()) || (this
						.getQcStatus() != null
						&& castOther.getQcStatus() != null && this
						.getQcStatus().equals(castOther.getQcStatus())))
				&& ((this.getPacketStatus() == castOther.getPacketStatus()) || (this
						.getPacketStatus() != null
						&& castOther.getPacketStatus() != null && this
						.getPacketStatus().equals(castOther.getPacketStatus())))
				&& ((this.getEmpId() == castOther.getEmpId()) || (this
						.getEmpId() != null
						&& castOther.getEmpId() != null && this.getEmpId()
						.equals(castOther.getEmpId())))
				&& ((this.getCompanyId() == castOther.getCompanyId()) || (this
						.getCompanyId() != null
						&& castOther.getCompanyId() != null && this
						.getCompanyId().equals(castOther.getCompanyId())))
				&& ((this.getDeptId() == castOther.getDeptId()) || (this
						.getDeptId() != null
						&& castOther.getDeptId() != null && this.getDeptId()
						.equals(castOther.getDeptId())))
				&& ((this.getEmpsName() == castOther.getEmpsName()) || (this
						.getEmpsName() != null
						&& castOther.getEmpsName() != null && this
						.getEmpsName().equals(castOther.getEmpsName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCustomerShortName() == null ? 0 : this
						.getCustomerShortName().hashCode());
		result = 37 * result
				+ (getOrderNo() == null ? 0 : this.getOrderNo().hashCode());
		result = 37
				* result
				+ (getOrderFacNo() == null ? 0 : this.getOrderFacNo()
						.hashCode());
		result = 37 * result
				+ (getOrderTime() == null ? 0 : this.getOrderTime().hashCode());
		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getOrderId() == null ? 0 : this.getOrderId().hashCode());
		result = 37
				* result
				+ (getSmapleStatus() == null ? 0 : this.getSmapleStatus()
						.hashCode());
		result = 37
				* result
				+ (getSampleOutStatus() == null ? 0 : this.getSampleOutStatus()
						.hashCode());
		result = 37 * result
				+ (getOutStatus() == null ? 0 : this.getOutStatus().hashCode());
		result = 37 * result
				+ (getQcStatus() == null ? 0 : this.getQcStatus().hashCode());
		result = 37
				* result
				+ (getPacketStatus() == null ? 0 : this.getPacketStatus()
						.hashCode());
		result = 37 * result
				+ (getEmpId() == null ? 0 : this.getEmpId().hashCode());
		result = 37 * result
				+ (getCompanyId() == null ? 0 : this.getCompanyId().hashCode());
		result = 37 * result
				+ (getDeptId() == null ? 0 : this.getDeptId().hashCode());
		result = 37 * result
				+ (getEmpsName() == null ? 0 : this.getEmpsName().hashCode());
		return result;
	}

	public Date getOrderLcDate() {
		return orderLcDate;
	}

	public void setOrderLcDate(Date orderLcDate) {
		this.orderLcDate = orderLcDate;
	}

	public Date getOrderLcDelay() {
		return orderLcDelay;
	}

	public void setOrderLcDelay(Date orderLcDelay) {
		this.orderLcDelay = orderLcDelay;
	}

	public Integer getClauseTypeId() {
		return clauseTypeId;
	}

	public void setClauseTypeId(Integer clauseTypeId) {
		this.clauseTypeId = clauseTypeId;
	}

	public Integer getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public Integer getFacId() {
		return facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public Integer getCurrencyIdPO() {
		return currencyIdPO;
	}

	public void setCurrencyIdPO(Integer currencyIdPO) {
		this.currencyIdPO = currencyIdPO;
	}

	public Integer getOrderfacId() {
		return orderfacId;
	}

	public void setOrderfacId(Integer orderfacId) {
		this.orderfacId = orderfacId;
	}

	public Integer getNationId() {
		return nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	public String getThemeStr() {
		return themeStr;
	}

	public void setThemeStr(String themeStr) {
		this.themeStr = themeStr;
	}

	public String getClientFile() {
		return clientFile;
	}

	public void setClientFile(String clientFile) {
		this.clientFile = clientFile;
	}

	public String getScFileName() {
		return scFileName;
	}

	public void setScFileName(String scFileName) {
		this.scFileName = scFileName;
	}
	
}