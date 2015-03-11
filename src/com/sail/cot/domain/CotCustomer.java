package com.sail.cot.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * CotCustomer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustomer implements java.io.Serializable {

	// Fields

	private Integer id;
	private String customerShortName;
	private String customerNo;
	private String customPhoto;
	private String priContact;
	private String fullNameCn;
	private String fullNameEn;
	private String contactNbr;
	private String customerAddress;
	private String customerEmail;
	private String customerPost;
	private String customerAddrEn;
	private String customerBank;
	private String customerAccount;
	private String customerFax;
	private Double commisionScale;
	private Double cutScale;
	private String cooperateLv;
	private String customerZm;
	private String customerRemark;
	private String customerMb;
	private byte[] picImg;
	private byte[] custImg;
	private String customerCm;
	private String customerNm;
	private String customerZhm;
	private java.sql.Timestamp addTime;
	private String addPersorn;
	private Integer empId;
	private Integer deptId;
	private Integer shipportId;
	private Integer trgportId;
	private Integer nationId;
	private Integer provinceId;
	private Integer cityId;
	private Integer custTypeId;
	private Integer custLvId;
	private Integer clauseId;
	private Integer commisionTypeId;
	private Integer payTypeId;
	private Integer cutTypeId;
	private String cultureBackground;
	private String shipInfo;
	private Set cotCustContacts = new HashSet(0);
	private Set customerVisitedLogs = new HashSet(0);
	private Set customerClaim = new HashSet(0);
	private String custSeq;
	private String op;
	private String chk;
	private String vatNo;
	private String invoiceCustomerName;
	private String invoiceShortName;
	private String invoiceCustomerPost;
	private String invoiceCustomerAddress;
	private Integer invoiceCountryId;
	private Integer invoiceCityId;
	private String supplierNo;
	private Integer forwardingAgent;//货代
	private String productIn;
	// Constructors

	public Integer getForwardingAgent() {
		return forwardingAgent;
	}

	public void setForwardingAgent(Integer forwardingAgent) {
		this.forwardingAgent = forwardingAgent;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	/** default constructor */
	public CotCustomer() {
	}

	/** full constructor */
	public CotCustomer(String customerShortName, String customerNo,
			String customPhoto, String priContact, String fullNameCn,
			String fullNameEn, String contactNbr, String customerAddress,
			String customerEmail, String customerPost, String customerAddrEn,
			String customerBank, String customerAccount, String customerFax,
			Double commisionScale, Double cutScale, String cooperateLv,
			String customerZm, String customerRemark, String customerMb,
			String customerCm, String customerNm, String customerZhm,
			java.sql.Timestamp addTime, String addPersorn, Integer empId, Integer deptId,
			Integer shipportId, Integer trgportId, Integer nationId,
			Integer provinceId, Integer cityId, Integer custTypeId,
			Integer custLvId, Integer clauseId, Integer commisionTypeId,
			Integer payTypeId, Integer cutTypeId, Set cotCustContacts,
			Set customerVisitedLogs) {
		this.customerShortName = customerShortName;
		this.customerNo = customerNo;
		this.customPhoto = customPhoto;
		this.priContact = priContact;
		this.fullNameCn = fullNameCn;
		this.fullNameEn = fullNameEn;
		this.contactNbr = contactNbr;
		this.customerAddress = customerAddress;
		this.customerEmail = customerEmail;
		this.customerPost = customerPost;
		this.customerAddrEn = customerAddrEn;
		this.customerBank = customerBank;
		this.customerAccount = customerAccount;
		this.customerFax = customerFax;
		this.commisionScale = commisionScale;
		this.cutScale = cutScale;
		this.cooperateLv = cooperateLv;
		this.customerZm = customerZm;
		this.customerRemark = customerRemark;
		this.customerMb = customerMb;
		this.customerCm = customerCm;
		this.customerNm = customerNm;
		this.customerZhm = customerZhm;
		this.addTime = addTime;
		this.addPersorn = addPersorn;
		this.empId = empId;
		this.deptId = deptId;
		this.shipportId = shipportId;
		this.trgportId = trgportId;
		this.nationId = nationId;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.custTypeId = custTypeId;
		this.custLvId = custLvId;
		this.clauseId = clauseId;
		this.commisionTypeId = commisionTypeId;
		this.payTypeId = payTypeId;
		this.cutTypeId = cutTypeId;
		this.cotCustContacts = cotCustContacts;
		this.customerVisitedLogs = customerVisitedLogs;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerShortName() {
		return this.customerShortName;
	}

	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}

	public String getCustomerNo() {
		return this.customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomPhoto() {
		return this.customPhoto;
	}

	public void setCustomPhoto(String customPhoto) {
		this.customPhoto = customPhoto;
	}

	public String getPriContact() {
		return this.priContact;
	}

	public void setPriContact(String priContact) {
		this.priContact = priContact;
	}

	public String getFullNameCn() {
		return this.fullNameCn;
	}

	public void setFullNameCn(String fullNameCn) {
		this.fullNameCn = fullNameCn;
	}

	public String getFullNameEn() {
		return this.fullNameEn;
	}

	public void setFullNameEn(String fullNameEn) {
		this.fullNameEn = fullNameEn;
	}

	public String getContactNbr() {
		return this.contactNbr;
	}

	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}

	public String getCustomerAddress() {
		return this.customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerEmail() {
		return this.customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPost() {
		return this.customerPost;
	}

	public void setCustomerPost(String customerPost) {
		this.customerPost = customerPost;
	}

	public String getCustomerAddrEn() {
		return this.customerAddrEn;
	}

	public void setCustomerAddrEn(String customerAddrEn) {
		this.customerAddrEn = customerAddrEn;
	}

	public String getCustomerBank() {
		return this.customerBank;
	}

	public void setCustomerBank(String customerBank) {
		this.customerBank = customerBank;
	}

	public String getCustomerAccount() {
		return this.customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getCustomerFax() {
		return this.customerFax;
	}

	public void setCustomerFax(String customerFax) {
		this.customerFax = customerFax;
	}

	public Double getCommisionScale() {
		return this.commisionScale;
	}

	public void setCommisionScale(Double commisionScale) {
		this.commisionScale = commisionScale;
	}

	public Double getCutScale() {
		return this.cutScale;
	}

	public void setCutScale(Double cutScale) {
		this.cutScale = cutScale;
	}

	public String getCooperateLv() {
		return this.cooperateLv;
	}

	public void setCooperateLv(String cooperateLv) {
		this.cooperateLv = cooperateLv;
	}

	public String getCustomerZm() {
		return this.customerZm;
	}

	public void setCustomerZm(String customerZm) {
		this.customerZm = customerZm;
	}

	public String getCustomerRemark() {
		return this.customerRemark;
	}

	public void setCustomerRemark(String customerRemark) {
		this.customerRemark = customerRemark;
	}

	public String getCustomerMb() {
		return this.customerMb;
	}

	public void setCustomerMb(String customerMb) {
		this.customerMb = customerMb;
	}

	public String getCustomerCm() {
		return this.customerCm;
	}

	public void setCustomerCm(String customerCm) {
		this.customerCm = customerCm;
	}

	public String getCustomerNm() {
		return this.customerNm;
	}

	public void setCustomerNm(String customerNm) {
		this.customerNm = customerNm;
	}

	public String getCustomerZhm() {
		return this.customerZhm;
	}

	public void setCustomerZhm(String customerZhm) {
		this.customerZhm = customerZhm;
	}

	public java.sql.Timestamp getAddTime() {
		return this.addTime;
	}

	public void setAddTime(java.sql.Timestamp addTime) {
		this.addTime = addTime;
	}

	public String getAddPersorn() {
		return this.addPersorn;
	}

	public void setAddPersorn(String addPersorn) {
		this.addPersorn = addPersorn;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getShipportId() {
		return this.shipportId;
	}

	public void setShipportId(Integer shipportId) {
		this.shipportId = shipportId;
	}

	public Integer getTrgportId() {
		return this.trgportId;
	}

	public void setTrgportId(Integer trgportId) {
		this.trgportId = trgportId;
	}

	public Integer getNationId() {
		return this.nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCustTypeId() {
		return this.custTypeId;
	}

	public void setCustTypeId(Integer custTypeId) {
		this.custTypeId = custTypeId;
	}

	public Integer getCustLvId() {
		return this.custLvId;
	}

	public void setCustLvId(Integer custLvId) {
		this.custLvId = custLvId;
	}

	public Integer getClauseId() {
		return this.clauseId;
	}

	public void setClauseId(Integer clauseId) {
		this.clauseId = clauseId;
	}

	public Integer getCommisionTypeId() {
		return this.commisionTypeId;
	}

	public void setCommisionTypeId(Integer commisionTypeId) {
		this.commisionTypeId = commisionTypeId;
	}

	public Integer getPayTypeId() {
		return this.payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public Integer getCutTypeId() {
		return this.cutTypeId;
	}

	public void setCutTypeId(Integer cutTypeId) {
		this.cutTypeId = cutTypeId;
	}

	public Set getCotCustContacts() {
		return this.cotCustContacts;
	}

	public void setCotCustContacts(Set cotCustContacts) {
		this.cotCustContacts = cotCustContacts;
	}

	public Set getCustomerVisitedLogs() {
		return this.customerVisitedLogs;
	}

	public void setCustomerVisitedLogs(Set customerVisitedLogs) {
		this.customerVisitedLogs = customerVisitedLogs;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	public byte[] getPicImg() {
		return picImg;
	}

	public void setPicImg(byte[] picImg) {
		this.picImg = picImg;
	}

	public byte[] getCustImg() {
		return custImg;
	}

	public void setCustImg(byte[] custImg) {
		this.custImg = custImg;
	}

	public Set getCustomerClaim() {
		return customerClaim;
	}

	public void setCustomerClaim(Set customerClaim) {
		this.customerClaim = customerClaim;
	}

	public String getCultureBackground() {
		return cultureBackground;
	}

	public void setCultureBackground(String cultureBackground) {
		this.cultureBackground = cultureBackground;
	}

	public String getShipInfo() {
		return shipInfo;
	}

	public void setShipInfo(String shipInfo) {
		this.shipInfo = shipInfo;
	}

	public String getCustSeq() {
		return custSeq;
	}

	public void setCustSeq(String custSeq) {
		this.custSeq = custSeq;
	}

	public String getVatNo() {
		return vatNo;
	}

	public void setVatNo(String vatNo) {
		this.vatNo = vatNo;
	}

	public String getInvoiceCustomerName() {
		return invoiceCustomerName;
	}

	public void setInvoiceCustomerName(String invoiceCustomerName) {
		this.invoiceCustomerName = invoiceCustomerName;
	}

	public String getInvoiceShortName() {
		return invoiceShortName;
	}

	public void setInvoiceShortName(String invoiceShortName) {
		this.invoiceShortName = invoiceShortName;
	}

	public String getInvoiceCustomerPost() {
		return invoiceCustomerPost;
	}

	public void setInvoiceCustomerPost(String invoiceCustomerPost) {
		this.invoiceCustomerPost = invoiceCustomerPost;
	}

	public String getInvoiceCustomerAddress() {
		return invoiceCustomerAddress;
	}

	public void setInvoiceCustomerAddress(String invoiceCustomerAddress) {
		this.invoiceCustomerAddress = invoiceCustomerAddress;
	}

	public Integer getInvoiceCountryId() {
		return invoiceCountryId;
	}

	public void setInvoiceCountryId(Integer invoiceCountryId) {
		this.invoiceCountryId = invoiceCountryId;
	}

	public Integer getInvoiceCityId() {
		return invoiceCityId;
	}

	public void setInvoiceCityId(Integer invoiceCityId) {
		this.invoiceCityId = invoiceCityId;
	}

	public String getProductIn() {
		return productIn;
	}

	public void setProductIn(String productIn) {
		this.productIn = productIn;
	}



	
}