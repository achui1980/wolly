package com.sail.cot.domain;

/**
 * CotProvince entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotProvince implements java.io.Serializable {

	// Fields

	private Integer id;
	private String provinceName;
	private String provinceRemark;
	private Integer nationId;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotProvince() {
	}

	/** full constructor */
	public CotProvince(String provinceName, String provinceRemark,
			Integer nationId) {
		this.provinceName = provinceName;
		this.provinceRemark = provinceRemark;
		this.nationId = nationId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceRemark() {
		return this.provinceRemark;
	}

	public void setProvinceRemark(String provinceRemark) {
		this.provinceRemark = provinceRemark;
	}

	public Integer getNationId() {
		return this.nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
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