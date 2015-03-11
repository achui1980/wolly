package com.sail.cot.domain.vo;

import com.sail.cot.domain.CotFinacegivenDetail;

public class CotDealDetailVO {
	private CotFinacegivenDetail detail;
	private String finaceNo;
	
	public CotFinacegivenDetail getDetail() {
		return detail;
	}
	public void setDetail(CotFinacegivenDetail detail) {
		this.detail = detail;
	}
	public String getFinaceNo() {
		return finaceNo;
	}
	public void setFinaceNo(String finaceNo) {
		this.finaceNo = finaceNo;
	}
}
