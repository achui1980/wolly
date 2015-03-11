package com.sail.cot.domain;

/**
 * CotCustContact entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustContact implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer customerId;
	private String contactPerson;
	private String contactNbr;
	private String contactEmail;
	private String contactDuty;
	private String contactFax;
	private Integer contactFlag;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotCustContact() {
	}

	/** full constructor */
	public CotCustContact(CotCustomer cotCustomer, String contactPerson,
			String contactNbr, String contactEmail, String contactDuty,
			Integer contactFlag,String contactFax) {
		
		this.contactPerson = contactPerson;
		this.contactNbr = contactNbr;
		this.contactEmail = contactEmail;
		this.contactDuty = contactDuty;
		this.contactFax = contactFax;
		this.contactFlag = contactFlag;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getContactFlag() {
		return contactFlag;
	}

	public void setContactFlag(Integer contactFlag) {
		this.contactFlag = contactFlag;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	
}