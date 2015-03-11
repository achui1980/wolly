package com.sail.cot.domain;

/**
 * CotTargetPort entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTargetPort implements java.io.Serializable {

	// Fields

	private Integer id;
	private String targetPortName;
	private String targetPortEnName;
	private String targetPortNation;
	private String shipingLine;
	private String targetPortRemark;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotTargetPort() {
	}

	/** full constructor */
	public CotTargetPort(String targetPortName, String targetPortEnName, String targetPortRemark) {
		this.targetPortName = targetPortName;
		this.targetPortEnName = targetPortEnName;
		this.targetPortRemark = targetPortRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTargetPortName() {
		return this.targetPortName;
	}

	public void setTargetPortName(String targetPortName) {
		this.targetPortName = targetPortName;
	}

	public String getTargetPortEnName() {
		return targetPortEnName;
	}

	public void setTargetPortEnName(String targetPortEnName) {
		this.targetPortEnName = targetPortEnName;
	}

	public String getTargetPortRemark() {
		return this.targetPortRemark;
	}

	public void setTargetPortRemark(String targetPortRemark) {
		this.targetPortRemark = targetPortRemark;
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

	public String getTargetPortNation() {
		return targetPortNation;
	}

	public void setTargetPortNation(String targetPortNation) {
		this.targetPortNation = targetPortNation;
	}

	public String getShipingLine() {
		return shipingLine;
	}

	public void setShipingLine(String shipingLine) {
		this.shipingLine = shipingLine;
	}

	
}