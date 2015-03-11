package com.sail.cot.domain;

/**
 * CotDept entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotDept implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deptName;
	private Long deptStatus;
	private String deptRemark;
	private Integer companyId;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotDept() {
	}

	/** minimal constructor */
	public CotDept(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotDept(Integer id, String deptName, Long deptStatus,
			String deptRemark, Integer companyId) {
		this.id = id;
		this.deptName = deptName;
		this.deptStatus = deptStatus;
		this.deptRemark = deptRemark;
		this.companyId = companyId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getDeptStatus() {
		return this.deptStatus;
	}

	public void setDeptStatus(Long deptStatus) {
		this.deptStatus = deptStatus;
	}

	public String getDeptRemark() {
		return this.deptRemark;
	}

	public void setDeptRemark(String deptRemark) {
		this.deptRemark = deptRemark;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	
}