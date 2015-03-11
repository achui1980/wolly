package com.sail.cot.domain;

/**
 * CotCustMb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderPc implements java.io.Serializable {

	// Fields

	private Integer id;
	private byte[] phone;
	private Integer categoryId;
	private String tempLate;
	private String pcRemark;
	private Integer orderId;
	private Integer orderDetailId;
	private String eleId;// 不存入数据库
	private String filePath;

	// Constructors

	/** default constructor */
	public CotOrderPc() {
	}

	public CotOrderPc(CotOrderPc obj, String eleId) {
		this.id = obj.getId();
		this.filePath = obj.getFilePath();
		this.categoryId = obj.getCategoryId();
		this.tempLate = obj.getTempLate();
		this.pcRemark = obj.getPcRemark();
		this.orderId = obj.getOrderId();
		this.orderDetailId = obj.getOrderDetailId();
		this.eleId = eleId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getPcRemark() {
		return pcRemark;
	}

	public void setPcRemark(String pcRemark) {
		this.pcRemark = pcRemark;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getTempLate() {
		return tempLate;
	}

	public void setTempLate(String tempLate) {
		this.tempLate = tempLate;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte[] getPhone() {
		return phone;
	}

	public void setPhone(byte[] phone) {
		this.phone = phone;
	}

}