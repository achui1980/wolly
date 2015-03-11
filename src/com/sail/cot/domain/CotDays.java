package com.sail.cot.domain;

/**
 * CotDays entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotDays implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer shipId;
	private Integer tarId;
	private Integer days;
	private String remark;
	// Constructors

	/** default constructor */
	public CotDays() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShipId() {
		return shipId;
	}

	public void setShipId(Integer shipId) {
		this.shipId = shipId;
	}

	public Integer getTarId() {
		return tarId;
	}

	public void setTarId(Integer tarId) {
		this.tarId = tarId;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}