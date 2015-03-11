package com.sail.cot.domain;

/**
 * CotBarcode entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotBarcode implements java.io.Serializable {

	// Fields

	private Integer id;
	private String eleId;
	private Integer addEmp;

	// Constructors

	/** default constructor */
	public CotBarcode() {
	}

	/** full constructor */
	public CotBarcode(String eleId, Integer addEmp) {
		this.eleId = eleId;
		this.addEmp = addEmp;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEleId() {
		return this.eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public Integer getAddEmp() {
		return this.addEmp;
	}

	public void setAddEmp(Integer addEmp) {
		this.addEmp = addEmp;
	}

}