package com.sail.cot.domain.vo;

import com.sail.cot.domain.CotFinaceOther;

public class CotTransDetailVO {
	private CotFinaceOther other;
	private String orderNo;
	
	public CotFinaceOther getOther() {
		return other;
	}
	public void setOther(CotFinaceOther other) {
		this.other = other;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
