package com.sail.cot.domain;

/**
 * CotRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotRole implements java.io.Serializable {

	// Fields

	private Integer id;
	private String roleName;
	private Long roleStatus;
	private String roleRemark;
 
	private String op;
	private String oa;
	private String chk;
 
	// Constructors

   

	/** default constructor */
	public CotRole() {
	}

	/** minimal constructor */
	public CotRole(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotRole(Integer id, String roleName, Long roleStatus,
			String roleRemark) {
		this.id = id;
		this.roleName = roleName;
		this.roleStatus = roleStatus;
		this.roleRemark = roleRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getRoleStatus() {
		return this.roleStatus;
	}

	public void setRoleStatus(Long roleStatus) {
		this.roleStatus = roleStatus;
	}

	public String getRoleRemark() {
		return this.roleRemark;
	}

	public void setRoleRemark(String roleRemark) {
		this.roleRemark = roleRemark;
	}

	 

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getOa() {
		return oa;
	}

	public void setOa(String oa) {
		this.oa = oa;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	
}