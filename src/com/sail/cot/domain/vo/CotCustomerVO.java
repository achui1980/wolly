package com.sail.cot.domain.vo;

import java.util.Date;


/**
 * CotContract entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustomerVO implements java.io.Serializable {

	// Fields

	private Integer id;
	private String customerNo;
	private String customerShortName;
	private Integer custTypeId;
	private String fullNameEn;
	private String fullNameCn;
	private String contactNbr;
	private String customerFax;
	private String customerEmail;
	private String contactPerson;
	private Integer empId;
	private String type;
	private Integer custId;
	private String priContact;
	private String customerAddrEn;
	private Integer nationId;
	private Date addTime;
	

	// Constructors

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getNationId() {
		return nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public Integer getCustTypeId() {
		return custTypeId;
	}

	public void setCustTypeId(Integer custTypeId) {
		this.custTypeId = custTypeId;
	}

	public String getFullNameEn() {
		return fullNameEn;
	}

	public void setFullNameEn(String fullNameEn) {
		this.fullNameEn = fullNameEn;
	}

	public String getFullNameCn() {
		return fullNameCn;
	}

	public void setFullNameCn(String fullNameCn) {
		this.fullNameCn = fullNameCn;
	}

	public String getContactNbr() {
		return contactNbr;
	}

	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}

	public String getCustomerFax() {
		return customerFax;
	}

	public void setCustomerFax(String customerFax) {
		this.customerFax = customerFax;
	}

	/** default constructor */
	public CotCustomerVO() {
	}

	/** full constructor */
	public CotCustomerVO(String customerShortName, String customerEmail,String type,
			 String contactPerson,Integer empId,Integer custId) {
		this.customerEmail =customerShortName;
		this.customerEmail =customerEmail;
		this.contactPerson = contactPerson;
		this.empId =empId;
		this.type =type;
		this.custId =custId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerShortName() {
		return customerShortName;
	}

	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getPriContact() {
		return priContact;
	}

	public void setPriContact(String priContact) {
		this.priContact = priContact;
	}

	public String getCustomerAddrEn() {
		return customerAddrEn;
	}

	public void setCustomerAddrEn(String customerAddrEn) {
		this.customerAddrEn = customerAddrEn;
	}
}