package com.sail.cot.domain;

import java.util.Date;

/**
 * CotEmpLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotEmpLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private Date logDate;//日期
	private String logContent;//工作内容
	private String logAdvise;//主管批注
	private String remark;//备注
	private Integer logCheck;//是否批注
	private Date addTime;//添加时间

	// Constructors

	/** default constructor */
	public CotEmpLog() {
	}

	/** full constructor */
	public CotEmpLog(Integer empId, Date logDate, String logContent,
			String logAdvise, String remark, Date addTime) {
		this.empId = empId;
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

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
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

}