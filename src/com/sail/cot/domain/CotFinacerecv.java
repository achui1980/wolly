package com.sail.cot.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotFinacerecv entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFinacerecv implements java.io.Serializable {

	// Fields

	private Integer id;
	private String finaceNo;//收款单号
	private String finaceName;//费用名称
	private Date finaceRecvDate;//收款日期
	private Double amount;//金额
	private Double bankFee;//银行费用
	private Double realAmount;//已冲金额
	private Double remainAmount;//未冲帐金额
	private String finaceRemark;//收款备注
	private Integer payTypeid;//收款类型
	private Integer custId;//客户
	private Integer bankId;//银行
	private Integer currencyId;//币种
	private Date addTime;//操作时间
	private Integer finaceStatus;//货款状态
	private String orderNo;
	private Integer fkId;
	private String source;
	private Integer companyId;
	private Integer recvPerson;//收款人
	private Integer addPerson;//制单人
	private Set cotFinacerecvDetails = new HashSet(0);

	// Constructors

	/** default constructor */
	public CotFinacerecv() {
	}

	/** full constructor */
	public CotFinacerecv(String finaceNo, Date finaceRecvDate, Double amount,
			Double bankFee, Double realAmount, Double remainAmount,
			String finaceRemark, Integer payTypeid, Integer custId,
			Integer bankId, Integer currencyId, Date addTime,
			Integer finaceStatus, String orderNo, Integer fkId, String source,
			Integer companyId, Set cotFinacerecvDetails) {
		this.finaceNo = finaceNo;
		this.finaceRecvDate = finaceRecvDate;
		this.amount = amount;
		this.bankFee = bankFee;
		this.realAmount = realAmount;
		this.remainAmount = remainAmount;
		this.finaceRemark = finaceRemark;
		this.payTypeid = payTypeid;
		this.custId = custId;
		this.bankId = bankId;
		this.currencyId = currencyId;
		this.addTime = addTime;
		this.finaceStatus = finaceStatus;
		this.orderNo = orderNo;
		this.fkId = fkId;
		this.source = source;
		this.companyId = companyId;
		this.cotFinacerecvDetails = cotFinacerecvDetails;
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

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
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

	public Set getCotFinacerecvDetails() {
		return this.cotFinacerecvDetails;
	}

	public void setCotFinacerecvDetails(Set cotFinacerecvDetails) {
		this.cotFinacerecvDetails = cotFinacerecvDetails;
	}

	public String getFinaceName() {
		return finaceName;
	}

	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}

	public Integer getRecvPerson() {
		return recvPerson;
	}

	public void setRecvPerson(Integer recvPerson) {
		this.recvPerson = recvPerson;
	}

	public Integer getAddPerson() {
		return addPerson;
	}

	public void setAddPerson(Integer addPerson) {
		this.addPerson = addPerson;
	}

}