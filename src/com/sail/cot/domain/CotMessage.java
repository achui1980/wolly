package com.sail.cot.domain;

import java.sql.Date;

/**
 * CotMessage entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMessage implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer msgTypeId;
	private String msgContent;
	private Date msgBeginDate;
	private Date msgEndDate;
	private String msgOrderNo;
	private String msgTable;
	private String msgType;
	private Long msgStatus;
	private Long msgFlag;
	private Integer msgFromPerson;
	private Integer msgToPerson;
	private Date addTime;
	private String msgAction;
	private String op;
	private String msgEditAction;

	// Constructors

	/** default constructor */
	public CotMessage() {
	}

	/** full constructor */
	public CotMessage(Integer msgTypeId, String msgContent,
			Date msgBeginDate, Date msgEndDate, String msgOrderNo,
			String msgTable, String msgType, Long msgStatus, Long msgFlag,
			Integer msgFromPerson, Integer msgToPerson, Date addTime) {
		this.msgTypeId = msgTypeId;
		this.msgContent = msgContent;
		this.msgBeginDate = msgBeginDate;
		this.msgEndDate = msgEndDate;
		this.msgOrderNo = msgOrderNo;
		this.msgTable = msgTable;
		this.msgType = msgType;
		this.msgStatus = msgStatus;
		this.msgFlag = msgFlag;
		this.msgFromPerson = msgFromPerson;
		this.msgToPerson = msgToPerson;
		this.addTime = addTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getMsgTypeId() {
		return msgTypeId;
	}

	public void setMsgTypeId(Integer msgTypeId) {
		this.msgTypeId = msgTypeId;
	}

	public String getMsgContent() {
		return this.msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Date getMsgBeginDate() {
		return this.msgBeginDate;
	}

	public void setMsgBeginDate(Date msgBeginDate) {
		this.msgBeginDate = msgBeginDate;
	}

	public Date getMsgEndDate() {
		return this.msgEndDate;
	}

	public void setMsgEndDate(Date msgEndDate) {
		this.msgEndDate = msgEndDate;
	}

	public String getMsgOrderNo() {
		return this.msgOrderNo;
	}

	public void setMsgOrderNo(String msgOrderNo) {
		this.msgOrderNo = msgOrderNo;
	}

	public String getMsgTable() {
		return this.msgTable;
	}

	public void setMsgTable(String msgTable) {
		this.msgTable = msgTable;
	}

	public String getMsgType() {
		return this.msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Long getMsgStatus() {
		return this.msgStatus;
	}

	public void setMsgStatus(Long msgStatus) {
		this.msgStatus = msgStatus;
	}

	public Long getMsgFlag() {
		return this.msgFlag;
	}

	public void setMsgFlag(Long msgFlag) {
		this.msgFlag = msgFlag;
	}

	public Integer getMsgFromPerson() {
		return this.msgFromPerson;
	}

	public void setMsgFromPerson(Integer msgFromPerson) {
		this.msgFromPerson = msgFromPerson;
	}

	public Integer getMsgToPerson() {
		return this.msgToPerson;
	}

	public void setMsgToPerson(Integer msgToPerson) {
		this.msgToPerson = msgToPerson;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getMsgAction() {
		return msgAction;
	}

	public void setMsgAction(String msgAction) {
		this.msgAction = msgAction;
	}

	public String getMsgEditAction() {
		return msgEditAction;
	}

	public void setMsgEditAction(String msgEditAction) {
		this.msgEditAction = msgEditAction;
	}

}