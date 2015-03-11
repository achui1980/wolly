package com.sail.cot.domain;

import java.util.Date;

/**
 * CotSyslog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSyslog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private String opMessage;
	private Date opTime;
	private Integer opType;
	private String opModule;
	private String itemNo;
	private String op;

	// Constructors

	/** default constructor */
	public CotSyslog() {
	}

	/** minimal constructor */
	public CotSyslog(Date opTime) {
		this.opTime = opTime;
	}

	/** full constructor */
	public CotSyslog(CotEmps cotEmps, String opMessage, Date opTime,
			Integer opType, String opModule, String itemNo) {
		
		this.opMessage = opMessage;
		this.opTime = opTime;
		this.opType = opType;
		this.opModule = opModule;
		this.itemNo = itemNo;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getOpMessage() {
		return this.opMessage;
	}

	public void setOpMessage(String opMessage) {
		this.opMessage = opMessage;
	}

	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public Integer getOpType() {
		return this.opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public String getOpModule() {
		return this.opModule;
	}

	public void setOpModule(String opModule) {
		this.opModule = opModule;
	}

	public String getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}