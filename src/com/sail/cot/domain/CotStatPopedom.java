package com.sail.cot.domain;

/**
 * CotStatPopedom entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotStatPopedom implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer statId;
	private Integer empId;

	// Constructors

	/** default constructor */
	public CotStatPopedom() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatId() {
		return statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	
}