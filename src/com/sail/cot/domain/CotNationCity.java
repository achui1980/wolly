package com.sail.cot.domain;

/**
 * CotNationCity entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotNationCity implements java.io.Serializable {

	// Fields

	private Integer id;
	private String cityName;
	private String cityRemark;
	private Integer nationId;
	private Integer provinceId;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotNationCity() {
	}

	/** minimal constructor */
	public CotNationCity(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotNationCity(Integer id, String cityName, String cityRemark,
			Integer nationId, Integer provinceId) {
		this.id = id;
		this.cityName = cityName;
		this.cityRemark = cityRemark;
		this.nationId = nationId;
		this.provinceId = provinceId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityRemark() {
		return this.cityRemark;
	}

	public void setCityRemark(String cityRemark) {
		this.cityRemark = cityRemark;
	}

	public Integer getNationId() {
		return this.nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
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