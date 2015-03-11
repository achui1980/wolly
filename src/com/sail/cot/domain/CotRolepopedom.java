package com.sail.cot.domain;

/**
 * CotRolepopedom entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotRolepopedom implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer roleId;
	private Integer moduleId;

	// Constructors

	/** default constructor */
	public CotRolepopedom() {
	}

	/** minimal constructor */
	public CotRolepopedom(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotRolepopedom(Integer id, Integer roleId, Integer moduleId) {
		this.id = id;
		this.roleId = roleId;
		this.moduleId = moduleId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

}