package com.sail.cot.domain.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class CotOrderVO {
	private Integer id;
	private Integer detailId;
	private String eleId;
	private String eleName;
	private String orderNo;
	private String custNo;
	private String customerNo;
	private String fullNameCn;
	private Timestamp orderTime;
	private Date sendTime;
	private String customerShortName;
	private Integer custId;
	private Integer empId;
	private Float priceFac;
	private Float priceOut;
	private String addPerson;
	private String bussinessPerson;
	private String empsName;
	private Double orderEarnest;
	private Integer priceFacUint;
	private Integer priceOutUint;
	private Integer currencyId;
	private Integer clauseTypeId;
	private Integer boxTypeId;
	private Integer payTypeId;
	private Float totalMoney;
	private Integer totalCount;//总数量
	private Integer totalContainer;//总箱数
	private Double totalCBM;//总体积
	private Double totalGross;//总毛重
	private Long boxCount;
	private Long containerCount;
	private Float unBoxCount;
	private String op;
	private Float orderPrice;
	private String shortName;
	private String eleNameEn;
	private Date addTime;
	private Long orderStatus;//审核状态
	private String poNo;
	private Double orderRate;
	private String orderCompose;
	private Integer contactId;
	private Float boxCbm;
	private Long boxObCount;
	private Integer unSendNum;
	private String allPinName;
	private Date orderLcDate;
	private Date orderLcDelay;
	private Integer factoryId;
	private Integer canOut;
	private Float ftm;
	private Integer typeLv1Id;//department
	
	private Integer shipportId;
	private Integer targetportId;
	private Integer chk;
	private String newRemark;
	private Date orderTimeFac;
	
	public Integer getTypeLv1Id() {
		return typeLv1Id;
	}
	public Integer getChk() {
		return chk;
	}
	public Date getOrderTimeFac() {
		return orderTimeFac;
	}
	public void setOrderTimeFac(Date orderTimeFac) {
		this.orderTimeFac = orderTimeFac;
	}
	public void setChk(Integer chk) {
		this.chk = chk;
	}
	public void setTypeLv1Id(Integer typeLv1Id) {
		this.typeLv1Id = typeLv1Id;
	}
	public Integer getCanOut() {
		return canOut;
	}
	public void setCanOut(Integer canOut) {
		this.canOut = canOut;
	}
	public Integer getUnSendNum() {
		return unSendNum;
	}
	public void setUnSendNum(Integer unSendNum) {
		this.unSendNum = unSendNum;
	}
	public Long getBoxObCount() {
		return boxObCount;
	}
	public void setBoxObCount(Long boxObCount) {
		this.boxObCount = boxObCount;
	}
	public Float getBoxCbm() {
		return boxCbm;
	}
	public void setBoxCbm(Float boxCbm) {
		this.boxCbm = boxCbm;
	}
	public String getOrderCompose() {
		return orderCompose;
	}
	public void setOrderCompose(String orderCompose) {
		this.orderCompose = orderCompose;
	}
	public Long getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Long orderStatus) {
		this.orderStatus = orderStatus;
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
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getFullNameCn() {
		return fullNameCn;
	}
	public void setFullNameCn(String fullNameCn) {
		this.fullNameCn = fullNameCn;
	}
	
	public String getAddPerson() {
		return addPerson;
	}
	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}
	public Timestamp getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public String getBussinessPerson() {
		return bussinessPerson;
	}
	public void setBussinessPerson(String bussinessPerson) {
		this.bussinessPerson = bussinessPerson;
	}
	public Double getOrderEarnest() {
		return orderEarnest;
	}
	public void setOrderEarnest(Double orderEarnest) {
		this.orderEarnest = orderEarnest;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public Float getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Float getPriceFac() {
		return priceFac;
	}
	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}
	public Float getPriceOut() {
		return priceOut;
	}
	public void setPriceOut(Float priceOut) {
		this.priceOut = priceOut;
	}
	public Integer getPriceFacUint() {
		return priceFacUint;
	}
	public void setPriceFacUint(Integer priceFacUint) {
		this.priceFacUint = priceFacUint;
	}
	public Integer getPriceOutUint() {
		return priceOutUint;
	}
	public void setPriceOutUint(Integer priceOutUint) {
		this.priceOutUint = priceOutUint;
	}
	public String getEmpsName() {
		return empsName;
	}
	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}
	public Integer getClauseTypeId() {
		return clauseTypeId;
	}
	public void setClauseTypeId(Integer clauseTypeId) {
		this.clauseTypeId = clauseTypeId;
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
	public Double getTotalCBM() {
		return totalCBM;
	}
	public void setTotalCBM(Double totalCBM) {
		this.totalCBM = totalCBM;
	}
	public Long getBoxCount() {
		return boxCount;
	}
	public void setBoxCount(Long boxCount) {
		this.boxCount = boxCount;
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
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getEleNameEn() {
		return eleNameEn;
	}
	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date e) {
		this.addTime = e;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getPayTypeId() {
		return payTypeId;
	}
	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public Integer getBoxTypeId() {
		return boxTypeId;
	}
	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
	}
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	public Double getOrderRate() {
		return orderRate;
	}
	public void setOrderRate(Double orderRate) {
		this.orderRate = orderRate;
	}
	public Double getTotalGross() {
		return totalGross;
	}
	public void setTotalGross(Double totalGross) {
		this.totalGross = totalGross;
	}
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public Long getContainerCount() {
		return containerCount;
	}
	public void setContainerCount(Long containerCount) {
		this.containerCount = containerCount;
	}
	public Float getUnBoxCount() {
		return unBoxCount;
	}
	public void setUnBoxCount(Float unBoxCount) {
		this.unBoxCount = unBoxCount;
	}
	public Integer getContactId() {
		return contactId;
	}
	public void setContactId(Integer contactId) {
		this.contactId = contactId;
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
	public Date getOrderLcDelay() {
		return orderLcDelay;
	}
	public void setOrderLcDelay(Date orderLcDelay) {
		this.orderLcDelay = orderLcDelay;
	}
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}
	public Float getFtm() {
		return ftm;
	}
	public void setFtm(Float ftm) {
		this.ftm = ftm;
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
	public String getNewRemark() {
		return newRemark;
	}
	public void setNewRemark(String newRemark) {
		this.newRemark = newRemark;
	}
}
