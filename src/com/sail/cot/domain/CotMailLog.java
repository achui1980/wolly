package com.sail.cot.domain;

import java.util.Date;

/**
 * CotMailLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private String mailSubject;
	private String msgId;
	private Date addTime;
	private Integer empId;

	// Constructors

	/** default constructor */
	public CotMailLog() {
	}

	/** full constructor */
	public CotMailLog(String mailSubject, String msgId, Date addTime,
			Integer empId) {
		this.mailSubject = mailSubject;
		this.msgId = msgId;
		this.addTime = addTime;
		this.empId = empId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMailSubject() {
		return this.mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

}