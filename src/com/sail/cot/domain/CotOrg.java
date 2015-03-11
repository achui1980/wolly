package com.sail.cot.domain;

import java.util.Date;

/**
 * CotCustMb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrg implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private Date uploadTime;
	private String filePath;
	private String remark;

	/** default constructor */
	public CotOrg() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}