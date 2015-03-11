package com.sail.cot.domain;

/**
 * CotTrafficType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTrafficType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String trafficName;
	private String trafficNameEn;
	private String remark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotTrafficType() {
	}

	/** full constructor */
	public CotTrafficType(String trafficName, String trafficNameEn,
			String remark) {
		this.trafficName = trafficName;
		this.trafficNameEn = trafficNameEn;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTrafficName() {
		return this.trafficName;
	}

	public void setTrafficName(String trafficName) {
		this.trafficName = trafficName;
	}

	public String getTrafficNameEn() {
		return this.trafficNameEn;
	}

	public void setTrafficNameEn(String trafficNameEn) {
		this.trafficNameEn = trafficNameEn;
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