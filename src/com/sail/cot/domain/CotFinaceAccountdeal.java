package com.sail.cot.domain;

import java.util.Date;

/**
 * CotFinaceAccountdeal entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFinaceAccountdeal implements java.io.Serializable {

	// Fields

	private Integer id;
	private String finaceNo;
	private String finaceName;
	private Double amount;
	private Double realAmount;
	private Double remainAmount;
	private Integer currencyId;
	private Integer factoryId;
	private String source;
	private String orderNo;
	private Integer fkId;
	private Integer businessPerson;//采购人员
	private Integer status;
	private Integer isImport;
	private Integer empId;
	private Date amountDate;
	private Date addDate;
	private Integer companyId;
	private Double zhRemainAmount;//流转后剩余金额
	private Integer finaceOtherId;//其他费用编号
	private Double zhAmount;//流转金额

	// Constructors

	public Double getZhRemainAmount() {
		return zhRemainAmount;
	}

	public void setZhRemainAmount(Double zhRemainAmount) {
		this.zhRemainAmount = zhRemainAmount;
	}

	public Double getZhAmount(){
		if (remainAmount != null && zhRemainAmount != null) {
			zhAmount = remainAmount - zhRemainAmount;
		}
		return zhAmount;
	}
	
	/** default constructor */
	public CotFinaceAccountdeal() {
	}

	/** minimal constructor */
	public CotFinaceAccountdeal(String orderNo) {
		this.orderNo = orderNo;
	}

	/** full constructor */
	public CotFinaceAccountdeal(String finaceName, Double amount,
			Double realAmount, Double remainAmount, Integer currencyId,
			Integer factoryId, String source, String orderNo, Integer fkId,
			Integer businessPerson, Integer status, Integer isImport,
			Integer empId, Date amountDate, Date addDate, Integer companyId) {
		this.finaceName = finaceName;
		this.amount = amount;
		this.realAmount = realAmount;
		this.remainAmount = remainAmount;
		this.currencyId = currencyId;
		this.factoryId = factoryId;
		this.source = source;
		this.orderNo = orderNo;
		this.fkId = fkId;
		this.businessPerson = businessPerson;
		this.status = status;
		this.isImport = isImport;
		this.empId = empId;
		this.amountDate = amountDate;
		this.addDate = addDate;
		this.companyId = companyId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFinaceName() {
		return this.finaceName;
	}

	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
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

	public Double getRemainAmount() {
		return this.remainAmount;
	}

	public void setRemainAmount(Double remainAmount) {
		this.remainAmount = remainAmount;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
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

	public Integer getIsImport() {
		return this.isImport;
	}

	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Date getAmountDate() {
		return this.amountDate;
	}

	public void setAmountDate(Date amountDate) {
		this.amountDate = amountDate;
	}

	public Date getAddDate() {
		return this.addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getFinaceNo() {
		return finaceNo;
	}

	public void setFinaceNo(String finaceNo) {
		this.finaceNo = finaceNo;
	}

	public Integer getFinaceOtherId() {
		return finaceOtherId;
	}

	public void setFinaceOtherId(Integer finaceOtherId) {
		this.finaceOtherId = finaceOtherId;
	}

	public void setZhAmount(Double zhAmount) {
		this.zhAmount = zhAmount;
	}

}