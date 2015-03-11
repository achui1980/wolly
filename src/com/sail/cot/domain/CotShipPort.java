package com.sail.cot.domain;

/**
 * CotShipPort entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotShipPort implements java.io.Serializable {

	// Fields

	private Integer id;
	private String shipPortName;
	private String shipPortNameEn;
	private String shipPortRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotShipPort() {
	}

	/** full constructor */
	public CotShipPort(String shipPortName,String shipPortNameEn, String shipPortRemark) {
		this.shipPortName = shipPortName;
		this.shipPortNameEn = shipPortNameEn;
		this.shipPortRemark = shipPortRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShipPortName() {
		return this.shipPortName;
	}

	public String getShipPortNameEn() {
		return shipPortNameEn;
	}

	public void setShipPortNameEn(String shipPortNameEn) {
		this.shipPortNameEn = shipPortNameEn;
	}

	public void setShipPortName(String shipPortName) {
		this.shipPortName = shipPortName;
	}

	public String getShipPortRemark() {
		return this.shipPortRemark;
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

	public void setShipPortRemark(String shipPortRemark) {
		this.shipPortRemark = shipPortRemark;
	}

}