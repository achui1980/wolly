package com.sail.cot.domain;

/**
 * CotFactory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFactory implements java.io.Serializable {

	// Fields

	private Integer id;
	private String factoryNo;
	private String factoryName;
	private String shortName;
	private String contactPerson;
	private String factoryNbr;
	private String factoryFax;
	private String factoryAddr;
	private String post;
	private String cooperateLv;
	private String cooperateDesc;
	private String factoryCorporation;
	private String factoryBank;
	private String factoryScale;
	private String bankAccount;
	private String taxNo;
	private String purchaseType;
	private String remark;
	private Integer cityId;
	private Integer areaId;
	//factroyTypeidLv1 1:产品，2：配件,3:包材,4:协作
	private Integer factroyTypeidLv1;
	private String op;
	private String chk;
	private String factoryEmail;
	private Integer provinceId;
	private Integer nationId;
	
	private String beneficiaryName;
	private String beneficiaryAddress;
	private String bankAddress;
	private String swiftCode;
	private Integer shipportId;//起运港
	
	private Integer payTypeId;//付款方式
	// Constructors

	public Integer getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public Integer getShipportId() {
		return shipportId;
	}

	public void setShipportId(Integer shipportId) {
		this.shipportId = shipportId;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}

	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	/** default constructor */
	public CotFactory() {
	}

	/** minimal constructor */
	public CotFactory(Integer id, Integer cityId) {
		this.id = id;
		this.cityId = cityId;
	}

	/** full constructor */
	public CotFactory(Integer id, String factoryNo, String factoryName,
			String shortName,String contactPerson, String factoryNbr, String factoryFax,
			String factoryAddr, String post, String cooperateLv,
			String cooperateDesc, String factoryCorporation,
			String factoryBank, String factoryScale, String bankAccount,
			String taxNo, String purchaseType, String remark, Integer cityId,
			Integer areaId, Integer factroyTypeidLv1) {
		this.id = id;
		this.factoryNo = factoryNo;
		this.factoryName = factoryName;
		this.shortName = shortName;
		this.contactPerson = contactPerson;
		this.factoryNbr = factoryNbr;
		this.factoryFax = factoryFax;
		this.factoryAddr = factoryAddr;
		this.post = post;
		this.cooperateLv = cooperateLv;
		this.cooperateDesc = cooperateDesc;
		this.factoryCorporation = factoryCorporation;
		this.factoryBank = factoryBank;
		this.factoryScale = factoryScale;
		this.bankAccount = bankAccount;
		this.taxNo = taxNo;
		this.purchaseType = purchaseType;
		this.remark = remark;
		this.cityId = cityId;
		this.areaId = areaId;
		this.factroyTypeidLv1 = factroyTypeidLv1;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFactoryNo() {
		return this.factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public String getFactoryName() {
		return this.factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getFactoryNbr() {
		return factoryNbr;
	}

	public void setFactoryNbr(String factoryNbr) {
		this.factoryNbr = factoryNbr;
	}

	public String getFactoryFax() {
		return this.factoryFax;
	}

	public void setFactoryFax(String factoryFax) {
		this.factoryFax = factoryFax;
	}

	public String getFactoryAddr() {
		return this.factoryAddr;
	}

	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}

	public String getPost() {
		return this.post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getCooperateLv() {
		return this.cooperateLv;
	}

	public void setCooperateLv(String cooperateLv) {
		this.cooperateLv = cooperateLv;
	}

	public String getCooperateDesc() {
		return this.cooperateDesc;
	}

	public void setCooperateDesc(String cooperateDesc) {
		this.cooperateDesc = cooperateDesc;
	}

	public String getFactoryCorporation() {
		return this.factoryCorporation;
	}

	public void setFactoryCorporation(String factoryCorporation) {
		this.factoryCorporation = factoryCorporation;
	}

	public String getFactoryBank() {
		return this.factoryBank;
	}

	public void setFactoryBank(String factoryBank) {
		this.factoryBank = factoryBank;
	}

	public String getFactoryScale() {
		return this.factoryScale;
	}

	public void setFactoryScale(String factoryScale) {
		this.factoryScale = factoryScale;
	}

	public String getBankAccount() {
		return this.bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getTaxNo() {
		return this.taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getPurchaseType() {
		return this.purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getFactroyTypeidLv1() {
		return this.factroyTypeidLv1;
	}

	public void setFactroyTypeidLv1(Integer factroyTypeidLv1) {
		this.factroyTypeidLv1 = factroyTypeidLv1;
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

	public String getFactoryEmail() {
		return factoryEmail;
	}

	public void setFactoryEmail(String factoryEmail) {
		this.factoryEmail = factoryEmail;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getNationId() {
		return nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	
}