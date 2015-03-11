package com.sail.cot.domain.vo;


public class CotFinaceOtherVO {
	private Integer id;
	private String finaceName;
	private Double amount;// 金额
	private Integer currencyId;// 币种
	private String orderNo;
	private String customerShortName;
	private String finaceNo;
	public String getOrderNo() {
		return orderNo;
	}
	public String getFinaceName() {
		return finaceName;
	}
	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public String getFinaceNo() {
		return finaceNo;
	}
	public void setFinaceNo(String finaceNo) {
		this.finaceNo = finaceNo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
