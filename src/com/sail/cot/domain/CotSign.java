package com.sail.cot.domain;


import java.util.HashSet;
import java.util.Set;

/**
 * CotSign entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSign implements java.io.Serializable {

	// Fields

	private Integer id;
	private String givenNo;
	private Integer givenId;
	private String signNo;
	private java.sql.Date signTime;
	private java.sql.Date custTime;
	private java.sql.Date requireTime;
	private String signRemark;
	private java.sql.Date addTime;
	private String addPerson;
	private String followPerson;
	private Long signStatus;
	private Long signIscheck;
	private String checkReason;
	private Integer factoryId;
	private Integer signTypeId;
	private Integer custId;
	private Integer  bussinessPerson;
	private Integer empId;
	private java.sql.Date arriveTime;
	private String contactPerson;
	private String givenAddr;
	private Integer checkSign;
	private String op;
	private String chk;
	private Integer contactId;
	

	// Constructors
	/** default constructor */
	public CotSign() {
	}

	/** minimal constructor */
	public CotSign(String signNo, Long signStatus, Long signIscheck) {
		this.signNo = signNo;
		this.signStatus = signStatus;
		this.signIscheck = signIscheck;
	}

	/** full constructor */
	public CotSign(String signNo, java.sql.Date signTime, java.sql.Date custTime,
			java.sql.Date requireTime,Integer  bussinessPerson,
			String signRemark, java.sql.Date addTime, String addPerson,
			String followPerson, Long signStatus, Long signIscheck,
			String checkReason, Integer factoryId, Integer signTypeId,
			Integer custId, Set cotSignDetails) {
		this.signNo = signNo;
		this.signTime = signTime;
		this.custTime = custTime;
		this.requireTime = requireTime;
		this.signRemark = signRemark;
		this.addTime = addTime;
		this.addPerson = addPerson;
		this.followPerson = followPerson;
		this.signStatus = signStatus;
		this.signIscheck = signIscheck;
		this.checkReason = checkReason;
		this.factoryId = factoryId;
		this.signTypeId = signTypeId;
		this.custId = custId;
		this.bussinessPerson = bussinessPerson;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSignNo() {
		return this.signNo;
	}

	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}

	public java.sql.Date getSignTime() {
		return this.signTime;
	}

	public void setSignTime(java.sql.Date signTime) {
		this.signTime = signTime;
	}
	
	public java.sql.Date getCustTime() {
		return custTime;
	}

	public void setCustTime(java.sql.Date custTime) {
		this.custTime = custTime;
	}

	public java.sql.Date getRequireTime() {
		return this.requireTime;
	}

	public void setRequireTime(java.sql.Date requireTime) {
		this.requireTime = requireTime;
	}

	public String getSignRemark() {
		return this.signRemark;
	}

	public void setSignRemark(String signRemark) {
		this.signRemark = signRemark;
	}

	public java.sql.Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(java.sql.Date addTime) {
		this.addTime = addTime;
	}

	public String getAddPerson() {
		return this.addPerson;
	}

	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}

	public String getFollowPerson() {
		return this.followPerson;
	}

	public void setFollowPerson(String followPerson) {
		this.followPerson = followPerson;
	}

	public Long getSignStatus() {
		return this.signStatus;
	}

	public void setSignStatus(Long signStatus) {
		this.signStatus = signStatus;
	}

	public Long getSignIscheck() {
		return this.signIscheck;
	}

	public void setSignIscheck(Long signIscheck) {
		this.signIscheck = signIscheck;
	}

	public String getCheckReason() {
		return this.checkReason;
	}

	public void setCheckReason(String checkReason) {
		this.checkReason = checkReason;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getSignTypeId() {
		return this.signTypeId;
	}

	public void setSignTypeId(Integer signTypeId) {
		this.signTypeId = signTypeId;
	}

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
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

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getBussinessPerson() {
		return bussinessPerson;
	}

	public void setBussinessPerson(Integer bussinessPerson) {
		this.bussinessPerson = bussinessPerson;
	}

	public String getGivenNo() {
		return givenNo;
	}

	public void setGivenNo(String givenNo) {
		this.givenNo = givenNo;
	}

	public Integer getGivenId() {
		return givenId;
	}

	public void setGivenId(Integer givenId) {
		this.givenId = givenId;
	}

	public java.sql.Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(java.sql.Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getGivenAddr() {
		return givenAddr;
	}

	public void setGivenAddr(String givenAddr) {
		this.givenAddr = givenAddr;
	}

	public Integer getCheckSign() {
		return checkSign;
	}

	public void setCheckSign(Integer checkSign) {
		this.checkSign = checkSign;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	
}