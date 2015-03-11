package com.sail.cot.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotFinacegiven entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFinacegiven implements java.io.Serializable {

	// Fields

	private Integer id;
	private String finaceNo;
	private String finaceName;
	private Date finaceRecvDate;
	private Double amount;
	private Double bankFee;
	private Double realAmount;
	private Double remainAmount;
	private String finaceRemark;
	private Integer payTypeid;
	private Integer factoryId;
	private Integer bankId;
	private Integer currencyId;
	private Date addTime;
	private Integer finaceStatus;
	private String orderNo;
	private Integer fkId;
	private String source;
	private Integer companyId;
	private Integer givenPerson;
	private Integer addPerson;
	private Set cotFinacegivenDetails = new HashSet(0);
	private String op;

	// Constructors

	/** default constructor */
	public CotFinacegiven() {
	}

	/** full constructor */
	public CotFinacegiven(String finaceNo, Date finaceRecvDate, Double amount,
			Double bankFee, Double realAmount, Double remainAmount,
			String finaceRemark, Integer payTypeid, Integer factoryId,
			Integer bankId, Integer currencyId, Date addTime,
			Integer finaceStatus, String orderNo, Integer fkId, String source,
			Integer companyId, Set cotFinacegivenDetails) {
		this.finaceNo = finaceNo;
		this.finaceRecvDate = finaceRecvDate;
		this.amount = amount;
		this.bankFee = bankFee;
		this.realAmount = realAmount;
		this.remainAmount = remainAmount;
		this.finaceRemark = finaceRemark;
		this.payTypeid = payTypeid;
		this.factoryId = factoryId;
		this.bankId = bankId;
		this.currencyId = currencyId;
		this.addTime = addTime;
		
		this.finaceStatus = finaceStatus;
		this.orderNo = orderNo;
		this.fkId = fkId;
		this.source = source;
		this.companyId = companyId;
		this.cotFinacegivenDetails = cotFinacegivenDetails;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFinaceNo() {
		return this.finaceNo;
	}

	public void setFinaceNo(String finaceNo) {
		this.finaceNo = finaceNo;
	}

	public Date getFinaceRecvDate() {
		return this.finaceRecvDate;
	}

	public void setFinaceRecvDate(Date finaceRecvDate) {
		this.finaceRecvDate = finaceRecvDate;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBankFee() {
		return this.bankFee;
	}

	public void setBankFee(Double bankFee) {
		this.bankFee = bankFee;
	}

	public Double getRealAmount() {
		return this.realAmount;
	}

	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
	}

	public Double getRemainAmount() {
		return this.remainAmount;
	}

	public void setRemainAmount(Double remainAmount) {
		this.remainAmount = remainAmount;
	}

	public String getFinaceRemark() {
		return this.finaceRemark;
	}

	public void setFinaceRemark(String finaceRemark) {
		this.finaceRemark = finaceRemark;
	}

	public Integer getPayTypeid() {
		return this.payTypeid;
	}

	public void setPayTypeid(Integer payTypeid) {
		this.payTypeid = payTypeid;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getBankId() {
		return this.bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getFinaceStatus() {
		return this.finaceStatus;
	}

	public void setFinaceStatus(Integer finaceStatus) {
		this.finaceStatus = finaceStatus;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getFkId() {
		return this.fkId;
	}

	public void setFkId(Integer fkId) {
		this.fkId = fkId;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Set getCotFinacegivenDetails() {
		return this.cotFinacegivenDetails;
	}

	public void setCotFinacegivenDetails(Set cotFinacegivenDetails) {
		this.cotFinacegivenDetails = cotFinacegivenDetails;
	}

	public String getFinaceName() {
		return finaceName;
	}

	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}

	public Integer getGivenPerson() {
		return givenPerson;
	}

	public void setGivenPerson(Integer givenPerson) {
		this.givenPerson = givenPerson;
	}

	public Integer getAddPerson() {
		return addPerson;
	}

	public void setAddPerson(Integer addPerson) {
		this.addPerson = addPerson;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}