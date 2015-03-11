package com.sail.cot.domain;

import java.util.Date;

/**
 * CotCustLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer custId;
	private Integer empId;
	private Date logDate; //日期
	private String logContent;//沟通内容
	private String logAdvise;//主管批注
	private String remark;//备注
	private Integer logCheck;//是否批注
	private Date addTime;//添加时间

	// Constructors

	/** default constructor */
	public CotCustLog() {
	}

	/** full constructor */
	public CotCustLog(Integer custId, Date logDate, String logContent,
			String logAdvise, String remark, Date addTime) {
		this.custId = custId;
		this.logDate = logDate;
		this.logContent = logContent;
		this.logAdvise = logAdvise;
		this.remark = remark;
		this.addTime = addTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getLogContent() {
		return this.logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getLogAdvise() {
		return this.logAdvise;
	}

	public void setLogAdvise(String logAdvise) {
		this.logAdvise = logAdvise;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getLogCheck() {
		return logCheck;
	}

	public void setLogCheck(Integer logCheck) {
		this.logCheck = logCheck;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

}