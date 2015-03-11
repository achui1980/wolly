package com.sail.cot.domain;

/**
 * CotPopedom entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPopedom implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empsId;
	private Integer moduleId;

	// Constructors

	/** default constructor */
	public CotPopedom() {
	}

	/** full constructor */
	public CotPopedom(Integer id, Integer empsId, Integer moduleId) {
		this.id = id;
		this.empsId = empsId;
		this.moduleId = moduleId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmpsId() {
		return this.empsId;
	}

	public void setEmpsId(Integer empsId) {
		this.empsId = empsId;
	}

	public Integer getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

}