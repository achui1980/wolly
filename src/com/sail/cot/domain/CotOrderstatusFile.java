package com.sail.cot.domain;

/**
 * CotOrderstatusFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderstatusFile implements java.io.Serializable {

	// Fields

	private Integer id;
	private String fileName;
	private String filePath;
	private Integer fileSize;
	private Integer orderId;
	private Integer empsId;
	private String orderStatus;

	// Constructors

	/** default constructor */
	public CotOrderstatusFile() {
	}

	/** minimal constructor */
	public CotOrderstatusFile(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotOrderstatusFile(Integer id, String fileName, String filePath,
			Integer fileSize, Integer orderId, Integer empsId) {
		this.id = id;
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.orderId = orderId;
		this.empsId = empsId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getEmpsId() {
		return this.empsId;
	}

	public void setEmpsId(Integer empsId) {
		this.empsId = empsId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}