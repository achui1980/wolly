package com.sail.cot.domain.vo;


public class CotOrdeNameRptVO {
	
	private String orderName;
	private Long totalContainerCount;
	private Long totalCount;
	private Float totalCbm;
	private Float totalNet;
	private Float totalGross;
	private Float avgPrice;
	private Float totalMoney;
	
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public Long getTotalContainerCount() {
		return totalContainerCount;
	}
	public void setTotalContainerCount(Long totalContainerCount) {
		this.totalContainerCount = totalContainerCount;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public Float getTotalCbm() {
		return totalCbm;
	}
	public void setTotalCbm(Float totalCbm) {
		this.totalCbm = totalCbm;
	}
	public Float getTotalNet() {
		return totalNet;
	}
	public void setTotalNet(Float totalNet) {
		this.totalNet = totalNet;
	}
	public Float getTotalGross() {
		return totalGross;
	}
	public void setTotalGross(Float totalGross) {
		this.totalGross = totalGross;
	}
	public Float getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(Float avgPrice) {
		this.avgPrice = avgPrice;
	}
	public Float getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}
}
