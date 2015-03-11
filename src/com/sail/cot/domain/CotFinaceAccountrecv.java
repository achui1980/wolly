package com.sail.cot.domain;

import java.util.Date;

/**
 * CotFinaceAccountrecv entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFinaceAccountrecv implements java.io.Serializable {

	// Fields

	private Integer id;
	private String finaceNo;//帐款单号
	private String finaceName;//费用名称
	private Double amount;//帐款总金额
	private Double realAmount;//已收金额
	private Double remainAmount;//剩余金额
	private Integer currencyId;//币种
	private Integer custId;//客户ID
	private String source;//金额源来　如："price","order","orderfac","sign"等
	private String orderNo;//订单号
	private Integer fkId;//对应来源的 外键ID
	private Integer businessPerson;//业务员
	private Integer status;//否是已冲账 0：未冲账 1：已冲账(只有当amount = real_amount)
	private Integer isImport;//（预留）是否已导入出货 0:未导入，1:已导入
	private Integer empId;//操作员
	private Date amountDate;//帐款时间
	private Date addDate;//添加时间
	private Integer companyId;//出口公司
	private Integer finaceOtherId;//其他费用编号
	private Double zhRemainAmount;//流转后剩余金额
	private String op;

	// Constructors

	public Integer getFinaceOtherId() {
		return finaceOtherId;
	}

	public void setFinaceOtherId(Integer finaceOtherId) {
		this.finaceOtherId = finaceOtherId;
	}

	/** default constructor */
	public CotFinaceAccountrecv() {
	}

	/** minimal constructor */
	public CotFinaceAccountrecv(String orderNo) {
		this.orderNo = orderNo;
	}

	/** full constructor */
	public CotFinaceAccountrecv(String finaceName, Double amount,
			Double realAmount, Double remainAmount, Integer currencyId,
			Integer custId, String source, String orderNo, Integer fkId,
			Integer businessPerson, Integer status, Integer isImport,
			Integer empId, Date amountDate, Date addDate, Integer companyId) {
		this.finaceName = finaceName;
		this.amount = amount;
		this.realAmount = realAmount;
		this.remainAmount = remainAmount;
		this.currencyId = currencyId;
		this.custId = custId;
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

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
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

	public Double getZhRemainAmount() {
		return zhRemainAmount;
	}

	public void setZhRemainAmount(Double zhRemainAmount) {
		this.zhRemainAmount = zhRemainAmount;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}