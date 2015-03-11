package com.sail.cot.domain;

/**
 * CotArea entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotArea implements java.io.Serializable {

	// Fields

	private Integer id;
	private String areaName;
	private String areaCode;
	private String remark;
	private Integer cityId;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotArea() {
	}

	/** minimal constructor */
	public CotArea(Integer id, Integer cityId) {
		this.id = id;
		this.cityId = cityId;
	}

	/** full constructor */
	public CotArea(Integer id, String areaName, String areaCode, String remark,
			Integer cityId) {
		this.id = id;
		this.areaName = areaName;
		this.areaCode = areaCode;
		this.remark = remark;
		this.cityId = cityId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
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