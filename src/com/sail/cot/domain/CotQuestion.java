package com.sail.cot.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * CotCustSeq entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotQuestion implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer queryPerson;
	private String queryText;
	private Timestamp queryTime;
	private Integer orderId;
	private Integer flag;
	
	private String orderNo; //不存入数据库
	private String allPinName; //不存入数据库
	private Integer custId; //不存入数据库
	private Integer factoryId; //不存入数据库
	private Date orderLcDate; //不存入数据库

	// Constructors
	public CotQuestion(CotQuestion question,String orderNo) {
		this.id = question.getId();
		this.queryPerson = question.getQueryPerson();
		this.queryText = question.getQueryText();
		this.queryTime = question.getQueryTime();
		this.orderId = question.getOrderId();
		this.flag = question.getFlag();
		this.orderNo = orderNo;
	}
	public CotQuestion(CotQuestion question,String orderNo,String allPinName,Integer custId,Integer factoryId,Date orderLcDate) {
		this.id = question.getId();
		this.queryPerson = question.getQueryPerson();
		this.queryText = question.getQueryText();
		this.queryTime = question.getQueryTime();
		this.orderId = question.getOrderId();
		this.flag = question.getFlag();
		this.orderNo = orderNo;
		this.allPinName = allPinName;
		this.custId = custId;
		this.factoryId = factoryId;
		this.orderLcDate = orderLcDate;
	}

	/** default constructor */
	public CotQuestion() {
	}

	public String getAllPinName() {
		return allPinName;
	}

	public void setAllPinName(String allPinName) {
		this.allPinName = allPinName;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Date getOrderLcDate() {
		return orderLcDate;
	}

	public void setOrderLcDate(Date orderLcDate) {
		this.orderLcDate = orderLcDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQueryPerson() {
		return queryPerson;
	}

	public void setQueryPerson(Integer queryPerson) {
		this.queryPerson = queryPerson;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public Timestamp getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Timestamp queryTime) {
		this.queryTime = queryTime;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	

}