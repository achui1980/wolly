package com.sail.cot.domain;

/**
 * CotInsureContract entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotInsureContract implements java.io.Serializable {

	// Fields

	private Integer id;
	private String insureName;
	private String insureNameEn;
	private String remark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotInsureContract() {
	}

	/** full constructor */
	public CotInsureContract(String insureName, String insureNameEn,
			String remark) {
		this.insureName = insureName;
		this.insureNameEn = insureNameEn;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInsureName() {
		return this.insureName;
	}

	public void setInsureName(String insureName) {
		this.insureName = insureName;
	}

	public String getInsureNameEn() {
		return this.insureNameEn;
	}

	public void setInsureNameEn(String insureNameEn) {
		this.insureNameEn = insureNameEn;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	
}