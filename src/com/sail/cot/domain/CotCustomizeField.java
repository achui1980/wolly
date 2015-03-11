package com.sail.cot.domain;

/**
 * CotCustomizeField entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustomizeField implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private String type;
	private String fieldName;

	// Constructors

	/** default constructor */
	public CotCustomizeField() {
	}

	/** full constructor */
	public CotCustomizeField(Integer empId, String type, String fieldName) {
		this.empId = empId;
		this.type = type;
		this.fieldName = fieldName;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}