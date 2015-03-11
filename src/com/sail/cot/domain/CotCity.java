package com.sail.cot.domain;

/**
 * CotCity entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCity implements java.io.Serializable {

	// Fields

	private Integer id;
	private String cityName;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotCity() {
	}

	/** minimal constructor */
	public CotCity(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotCity(Integer id, String cityName) {
		this.id = id;
		this.cityName = cityName;
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