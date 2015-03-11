package com.sail.cot.domain;

import java.util.Date;


/**
 * PanPrice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPanContact implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer panId;//询盘主单id
	private Integer contactId;//联系人id
	private Integer state;//状态(0:未查看;1:已查看)
	private String mailUrl;//邮件地址
	
	private String priceNo;//询盘单号---临时字段
	private String contactPerson;//联系人名称---临时字段
	private String contactNbr;//电话---临时字段
	private String contactEmail;//联系人名称---临时字段
	private Integer factoryId;//联系人名称---临时字段
	private String loginName;
	
	private Date sendTime;//发送时间

	// Constructors

	/** default constructor */
	public CotPanContact() {
	}
	
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public CotPanContact(CotPanContact obj,String priceNo,String contactPerson) {
		this.id=obj.getId();
		this.panId=obj.getPanId();
		this.contactId=obj.getContactId();
		this.state=obj.getState();
		this.mailUrl=obj.getMailUrl();
		this.priceNo=priceNo;
		this.sendTime=obj.getSendTime();
		this.contactPerson=contactPerson;
		
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public CotPanContact(CotPanContact obj,String contactNbr,String contactPerson,Integer factoryId,String loginName) {
		this.id=obj.getId();
		this.state=obj.getState();
		this.contactPerson=contactPerson;
		this.contactNbr=contactNbr;
		this.contactEmail=obj.getMailUrl();
		this.factoryId=factoryId;
		this.sendTime=obj.getSendTime();
		this.loginName=loginName;
		
	}

	public String getPriceNo() {
		return priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}


	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPanId() {
		return panId;
	}

	public void setPanId(Integer panId) {
		this.panId = panId;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMailUrl() {
		return mailUrl;
	}

	public void setMailUrl(String mailUrl) {
		this.mailUrl = mailUrl;
	}

	public String getContactNbr() {
		return contactNbr;
	}

	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}


}