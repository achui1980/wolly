package com.sail.cot.domain;

/**
 * CotContact entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotContact implements java.io.Serializable {

	// Fields

	private Integer id;
	private String contactPerson;
	private String contactNbr;
	private String contactEmail;
	private String contactDuty;
	private String contactFax;
	private String contactPhone;
	private String contactRemark;
	private Integer factoryId;
	private Integer mainFlag;//是否主要联系人
	private String loginName;
	private String loginPwd;
	private String op;
	private String chk;
	
	private Integer state;//临时字段 状态(0:未查看;1:已查看)
	// Constructors

	/** default constructor */
	public CotContact() {
	}

	/** minimal constructor */
	public CotContact(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotContact(Integer id, String contactPerson, String contactNbr,
			String contactEmail, String contactDuty, String contactFax,
			String contactPhone, String contactRemark,Integer factoryId) {
		this.id = id;
		this.contactPerson = contactPerson;
		this.contactNbr = contactNbr;
		this.contactEmail = contactEmail;
		this.contactDuty = contactDuty;
		this.contactFax = contactFax;
		this.contactPhone = contactPhone;
		this.contactRemark = contactRemark;
		this.factoryId = factoryId;
		
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactNbr() {
		return this.contactNbr;
	}

	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}

	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactDuty() {
		return this.contactDuty;
	}

	public void setContactDuty(String contactDuty) {
		this.contactDuty = contactDuty;
	}

	public String getContactFax() {
		return this.contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactRemark() {
		return contactRemark;
	}

	public void setContactRemark(String contactRemark) {
		this.contactRemark = contactRemark;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
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

	public Integer getMainFlag() {
		return mainFlag;
	}

	public void setMainFlag(Integer mainFlag) {
		this.mainFlag = mainFlag;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public Integer getState() {
		return 0;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	
}