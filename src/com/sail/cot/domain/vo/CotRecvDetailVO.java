package com.sail.cot.domain.vo;

import java.util.Date;


public class CotRecvDetailVO {
	
	private Integer id;
	private String finaceNo;//收款单号
	private String finaceName;//费用名称
	private String flag;//'A'/'M'
	private String orderNo;//发票编号
	private Date addTime;//冲帐日期
	private Integer currencyId;//收款币种
	private Double amount;//总金额
	private Double currentAmount;//本次冲帐金额
	
	public String getFinaceNo() {
		return finaceNo;
	}
	public void setFinaceNo(String finaceNo) {
		this.finaceNo = finaceNo;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Double currentAmount) {
		this.currentAmount = currentAmount;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getFinaceName() {
		return finaceName;
	}
	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
