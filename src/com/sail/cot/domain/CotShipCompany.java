package com.sail.cot.domain;

import java.util.Set;

/**
 * CotConsignCompany entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotShipCompany implements java.io.Serializable {

	// Fields

	private Integer id;
	private String companyName;
	private String companyNameEn;
	private String companyAddr;
	private String companyCode;
	private String companyContact;
	private String companyNbr;
	private String companyFax;
	private String companyEmail;
	private String companyWebsite;
	private String companyRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotShipCompany() {
	}

	/** full constructor */
	public CotShipCompany(String companyName, String companyAddr,
			String companyCode, String companyContact, String companyNbr,
			String companyFax, String companyEmail, String companyWebsite,
			String companyRemark, Set cotShipments, Set cotHsInfos) {
		this.companyName = companyName;
		this.companyAddr = companyAddr;
		this.companyCode = companyCode;
		this.companyContact = companyContact;
		this.companyNbr = companyNbr;
		this.companyFax = companyFax;
		this.companyEmail = companyEmail;
		this.companyWebsite = companyWebsite;
		this.companyRemark = companyRemark;

	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddr() {
		return this.companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyContact() {
		return this.companyContact;
	}

	public void setCompanyContact(String companyContact) {
		this.companyContact = companyContact;
	}

	public String getCompanyNbr() {
		return this.companyNbr;
	}

	public void setCompanyNbr(String companyNbr) {
		this.companyNbr = companyNbr;
	}

	public String getCompanyFax() {
		return this.companyFax;
	}

	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}

	public String getCompanyEmail() {
		return this.companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyWebsite() {
		return this.companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public String getCompanyRemark() {
		return this.companyRemark;
	}

	public void setCompanyRemark(String companyRemark) {
		this.companyRemark = companyRemark;
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

	public String getCompanyNameEn() {
		return companyNameEn;
	}

	public void setCompanyNameEn(String companyNameEn) {
		this.companyNameEn = companyNameEn;
	}
	
	
	
}