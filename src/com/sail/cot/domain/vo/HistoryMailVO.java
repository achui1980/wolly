package com.sail.cot.domain.vo;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class HistoryMailVO implements Serializable{
	
	private String id;
	private String subject;
	private Date sendTime;
	private Boolean isContainAttach;
	private Integer mailType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Boolean getIsContainAttach() {
		return isContainAttach;
	}
	public void setIsContainAttach(Boolean isContainAttach) {
		this.isContainAttach = isContainAttach;
	}
	public Integer getMailType() {
		return mailType;
	}
	public void setMailType(Integer mailType) {
		this.mailType = mailType;
	}
}
