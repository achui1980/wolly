package com.sail.cot.domain.vo;


public class CotArtWordVO {
	private Integer id;
	private String eleNameEn;
	private String eleUnit;
	private String artWork;
	private String barcode;
	private String custNo;
	private String eleInchDesc;
	private String artText;
	private String remark;
	private String awStr;
	private String comment;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEleNameEn() {
		return eleNameEn;
	}
	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}
	public String getEleUnit() {
		return eleUnit;
	}
	public void setEleUnit(String eleUnit) {
		this.eleUnit = eleUnit;
	}
	public String getArtWork() {
		return artWork;
	}
	public void setArtWork(String artWork) {
		this.artWork = artWork;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getBarcode() {
		return barcode;
	}
	public String getAwStr() {
		return awStr;
	}
	public void setAwStr(String awStr) {
		this.awStr = awStr;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getEleInchDesc() {
		return eleInchDesc;
	}
	public void setEleInchDesc(String eleInchDesc) {
		this.eleInchDesc = eleInchDesc;
	}
	public String getArtText() {
		return artText;
	}
	public void setArtText(String artText) {
		this.artText = artText;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
