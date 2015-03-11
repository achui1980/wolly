package com.sail.cot.domain;

import java.util.Date;

/**
 * CotFinacerecvDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFinacerecvDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer finaceRecvid;//收款主单编号
	private String finaceName;//费用名称
	private String finaceNo;//
	private Double amount;//总金额
	private Double realAmount;//已冲金额
	private Double currentAmount;//本次金额
	private Integer currencyId;//币种
	private Integer type;//其他费用类型 0:应收 1：应付
	private String flag;//费用加减项 'M':减项 'A'：加项
	private String source;//金额源来　如："price","order","orderfac","sign"等
	private String orderNo;//单号，可以是订单号，生产合同号 等
	private Integer fkId;//对应来源的 外键ID
	private Integer businessPerson;//业务员
	private Integer status;//否是已生成货款 0：冲账未完成 1：已冲账完成
	private Date finaceDate;//帐款日期
	private Date addTime;//添加日期
	private Integer recvId;//应收帐编号

	// Constructors

	public Integer getRecvId() {
		return recvId;
	}

	public void setRecvId(Integer recvId) {
		this.recvId = recvId;
	}

	/** default constructor */
	public CotFinacerecvDetail() {
	}

	/** full constructor */
	public CotFinacerecvDetail(CotFinacerecv cotFinacerecv, String finaceName,
			String finaceNo, Double amount, Double realAmount,
			Double currentAmount, Integer currencyId, Integer type,
			String flag, String source, String orderNo, Integer fkId,
			Integer businessPerson, Integer status, Date finaceDate,
			Date addTime) {
		
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

	public Integer getFinaceRecvid() {
		return finaceRecvid;
	}

	public void setFinaceRecvid(Integer finaceRecvid) {
		this.finaceRecvid = finaceRecvid;
	}

}