package com.sail.cot.domain;

/**
 * CotExpCompany entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotExpCompany implements java.io.Serializable {

	// Fields

	private Integer id;
	private String expCompanyName;
	private String expCompanyNameEn;
	private String expCompanyTel;
	private String expCompanyRemark;
	private String expCompanyContact;
	private String expCompanyAccount;
	private String op;

	// Constructors

	/** default constructor */
	public CotExpCompany() {
	}

	/** full constructor */
	public CotExpCompany(String expCompanyName, String expCompanyNameEn,
			String expCompanyTel, String expCompanyRemark) {
		this.expCompanyName = expCompanyName;
		this.expCompanyNameEn = expCompanyNameEn;
		this.expCompanyTel = expCompanyTel;
		this.expCompanyRemark = expCompanyRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExpCompanyName() {
		return this.expCompanyName;
	}

	public void setExpCompanyName(String expCompanyName) {
		this.expCompanyName = expCompanyName;
	}

	public String getExpCompanyNameEn() {
		return this.expCompanyNameEn;
	}

	public void setExpCompanyNameEn(String expCompanyNameEn) {
		this.expCompanyNameEn = expCompanyNameEn;
	}

	public String getExpCompanyTel() {
		return this.expCompanyTel;
	}

	public void setExpCompanyTel(String expCompanyTel) {
		this.expCompanyTel = expCompanyTel;
	}

	public String getExpCompanyRemark() {
		return this.expCompanyRemark;
	}

	public void setExpCompanyRemark(String expCompanyRemark) {
		this.expCompanyRemark = expCompanyRemark;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getExpCompanyContact() {
		return expCompanyContact;
	}

	public void setExpCompanyContact(String expCompanyContact) {
		this.expCompanyContact = expCompanyContact;
	}

	public String getExpCompanyAccount() {
		return expCompanyAccount;
	}

	public void setExpCompanyAccount(String expCompanyAccount) {
		this.expCompanyAccount = expCompanyAccount;
	}

}