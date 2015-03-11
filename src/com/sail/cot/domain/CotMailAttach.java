package com.sail.cot.domain;

import java.util.Date;

import com.zhao.mail.entity.MailAttach;

@SuppressWarnings("serial")
public class CotMailAttach extends MailAttach implements
		java.io.Serializable {
	
	private String id;
	private Date sendTime;
	private Date addTime;
	private Integer mailType;
	private String mailId;
	private String custUrl;
	private Integer empId;
	
	private String cid;

	/** default constructor */
	public CotMailAttach() {
	}


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getMailType() {
		return this.mailType;
	}

	public void setMailType(Integer mailType) {
		this.mailType = mailType;
	}

	public String getMailId() {
		return this.mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getCustUrl() {
		return this.custUrl;
	}

	public void setCustUrl(String custUrl) {
		this.custUrl = custUrl;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}


	public String getCid() {
		return this.cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

}