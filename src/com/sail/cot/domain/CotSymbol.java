package com.sail.cot.domain;

/**
 * CotSymbol entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSymbol implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderOutId;//出货单编号
	private String sendPerson;
	private String invoiceRecvPerson;
	private String consignSendPerson;//发货人
	private String consignNotePerson;//通知人
	private String consignRecvPerson;//收货人
	private String boxRemark;
	private String inoviceRemark;
	private String shipmentRemark;
	private String recReMark;//托运单备注
	private String consignNotePersons;//同时通知人

	// Constructors

	/** default constructor */
	public CotSymbol() {
	}

	/** full constructor */
	public CotSymbol(Integer orderOutId, String sendPerson,
			String invoiceRecvPerson, String consignSendPerson,
			String consignNotePerson, String consignRecvPerson,
			String boxRemark, String inoviceRemark, String shipmentRemark) {
		this.orderOutId = orderOutId;
		this.sendPerson = sendPerson;
		this.invoiceRecvPerson = invoiceRecvPerson;
		this.consignSendPerson = consignSendPerson;
		this.consignNotePerson = consignNotePerson;
		this.consignRecvPerson = consignRecvPerson;
		this.boxRemark = boxRemark;
		this.inoviceRemark = inoviceRemark;
		this.shipmentRemark = shipmentRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderOutId() {
		return orderOutId;
	}

	public void setOrderOutId(Integer orderOutId) {
		this.orderOutId = orderOutId;
	}

	public String getSendPerson() {
		return this.sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public String getInvoiceRecvPerson() {
		return this.invoiceRecvPerson;
	}

	public void setInvoiceRecvPerson(String invoiceRecvPerson) {
		this.invoiceRecvPerson = invoiceRecvPerson;
	}

	public String getConsignSendPerson() {
		return this.consignSendPerson;
	}

	public void setConsignSendPerson(String consignSendPerson) {
		this.consignSendPerson = consignSendPerson;
	}

	public String getConsignNotePerson() {
		return this.consignNotePerson;
	}

	public void setConsignNotePerson(String consignNotePerson) {
		this.consignNotePerson = consignNotePerson;
	}

	public String getConsignRecvPerson() {
		return this.consignRecvPerson;
	}

	public void setConsignRecvPerson(String consignRecvPerson) {
		this.consignRecvPerson = consignRecvPerson;
	}

	public String getBoxRemark() {
		return this.boxRemark;
	}

	public void setBoxRemark(String boxRemark) {
		this.boxRemark = boxRemark;
	}

	public String getInoviceRemark() {
		return this.inoviceRemark;
	}

	public void setInoviceRemark(String inoviceRemark) {
		this.inoviceRemark = inoviceRemark;
	}

	public String getShipmentRemark() {
		return this.shipmentRemark;
	}

	public void setShipmentRemark(String shipmentRemark) {
		this.shipmentRemark = shipmentRemark;
	}

	public String getRecReMark() {
		return recReMark;
	}

	public void setRecReMark(String recReMark) {
		this.recReMark = recReMark;
	}

	public String getConsignNotePersons() {
		return consignNotePersons;
	}

	public void setConsignNotePersons(String consignNotePersons) {
		this.consignNotePersons = consignNotePersons;
	}

}