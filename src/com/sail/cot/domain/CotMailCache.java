package com.sail.cot.domain;

import java.util.Date;

/**
 * CotMailCache entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailCache implements java.io.Serializable {

	// Fields

	private Integer id;
	private String mailAddr;
	private String msgId;
	private Date addTime;
	private Integer addYear;

	// Constructors

	/** default constructor */
	public CotMailCache() {
	}

	/** full constructor */
	public CotMailCache(String mailAddr,String msgId, Date addTime, Integer addYear) {
		this.mailAddr = mailAddr;
		this.msgId = msgId;
		this.addTime = addTime;
		this.addYear = addYear;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getAddYear() {
		return this.addYear;
	}

	public void setAddYear(Integer addYear) {
		this.addYear = addYear;
	}

	public String getMailAddr() {
		return mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

}