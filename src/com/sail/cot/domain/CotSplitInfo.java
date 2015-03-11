package com.sail.cot.domain;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CotSplitInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSplitInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderOutId;
	private String containerNo;
	private Date splitDate;
	private String labelNo;
	private String containerType;
	private Double containerCube;
	private Double totalCbm;
	private String splitRemark;
	private Set cotSplitDetails = new HashSet(0);
	private String op;
	private String string;

	// Constructors

	/** default constructor */
	public CotSplitInfo() {
	}

	/** full constructor */
	public CotSplitInfo(CotOrderOut cotOrderOut, String containerNo,
			Date splitDate, String labelNo, String containerType,
			Double containerCube, String splitRemark, Set cotSplitDetails) {
		this.containerNo = containerNo;
		this.splitDate = splitDate;
		this.labelNo = labelNo;
		this.containerType = containerType;
		this.containerCube = containerCube;
		this.splitRemark = splitRemark;
		this.cotSplitDetails = cotSplitDetails;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContainerNo() {
		return this.containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public Date getSplitDate() {
		return this.splitDate;
	}

	public void setSplitDate(Date splitDate) {
		this.splitDate = splitDate;
	}

	public String getLabelNo() {
		return this.labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getContainerType() {
		return this.containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public Double getContainerCube() {
		return this.containerCube;
	}

	public void setContainerCube(Double containerCube) {
		this.containerCube = containerCube;
	}

	public String getSplitRemark() {
		return this.splitRemark;
	}

	public void setSplitRemark(String splitRemark) {
		this.splitRemark = splitRemark;
	}

	public Set getCotSplitDetails() {
		return this.cotSplitDetails;
	}

	public void setCotSplitDetails(Set cotSplitDetails) {
		this.cotSplitDetails = cotSplitDetails;
	}

	public Integer getOrderOutId() {
		return orderOutId;
	}

	public void setOrderOutId(Integer orderOutId) {
		this.orderOutId = orderOutId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public Double getTotalCbm() {
		return totalCbm;
	}

	public void setTotalCbm(Double totalCbm) {
		this.totalCbm = totalCbm;
	}

}