package com.sail.cot.domain;

import java.util.Date;

/**
 * CotFinacegivenDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFinacegivenDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer finaceGivenid;
	private String finaceName;
	private String finaceNo;
	private Double amount;
	private Double realAmount;
	private Double currentAmount;
	private Integer currencyId;
	private Integer type;
	private String flag;
	private String source;
	private String orderNo;
	private Integer fkId;
	private Integer businessPerson;
	private Integer status;
	private Date finaceDate;
	private Date addTime;
	private Integer factoryId;
	private String op;
	private Integer dealId;
	private String detailNo;
	
	//deal Fields
	private Double remainAmount;
	private Integer empId;
	private Date amountDate;
	private Integer companyId;
	private Date addDate;

	// Constructors

	public Integer getDealId() {
		return dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	/** default constructor */
	public CotFinacegivenDetail() {
	}

	/** full constructor */
	public CotFinacegivenDetail(CotFinacegiven cotFinacegiven,
			String finaceName, String finaceNo, Double amount,
			Double realAmount, Double currentAmount, Integer currencyId,
			Integer type, String flag, String source, String orderNo,
			Integer fkId, Integer businessPerson, Integer status,
			Date finaceDate, Date addTime, Integer factoryId) {
		
		this.finaceName = finaceName;
		this.finaceNo = finaceNo;
		this.amount = amount;
		this.realAmount = realAmount;
		this.currentAmount = currentAmount;
		this.currencyId = currencyId;
		this.type = type;
		this.flag = flag;
		this.source = source;
		this.orderNo = orderNo;
		this.fkId = fkId;
		this.businessPerson = businessPerson;
		this.status = status;
		this.finaceDate = finaceDate;
		this.addTime = addTime;
		this.factoryId = factoryId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Integer getFinaceGivenid() {
		return finaceGivenid;
	}

	public void setFinaceGivenid(Integer finaceGivenid) {
		this.finaceGivenid = finaceGivenid;
	}

	public String getFinaceName() {
		return this.finaceName;
	}

	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}

	public String getFinaceNo() {
		return this.finaceNo;
	}

	public void setFinaceNo(String finaceNo) {
		this.finaceNo = finaceNo;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getRealAmount() {
		return this.realAmount;
	}

	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
	}

	public Double getCurrentAmount() {
		return this.currentAmount;
	}

	public void setCurrentAmount(Double currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public Integer getBusinessPerson() {
		return this.businessPerson;
	}

	public void setBusinessPerson(Integer businessPerson) {
		this.businessPerson = businessPerson;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getFinaceDate() {
		return this.finaceDate;
	}

	public void setFinaceDate(Date finaceDate) {
		this.finaceDate = finaceDate;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
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

	public String getDetailNo() {
		return detailNo;
	}

	public void setDetailNo(String detailNo) {
		this.detailNo = detailNo;
	}

	public Double getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(Double remainAmount) {
		this.remainAmount = remainAmount;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Date getAmountDate() {
		return amountDate;
	}

	public void setAmountDate(Date amountDate) {
		this.amountDate = amountDate;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

}