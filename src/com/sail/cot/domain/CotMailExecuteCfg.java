package com.sail.cot.domain;

/**
 * CotMailExecuteCfg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailExecuteCfg implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String cmpType;
	private String method;
	private String package_;
	private String class_;
	private String type;

	// Constructors

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/** default constructor */
	public CotMailExecuteCfg() {
	}

	/** minimal constructor */
	public CotMailExecuteCfg(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotMailExecuteCfg(Integer id, String cmpType, String method,
			String package_, String class_) {
		this.id = id;
		this.cmpType = cmpType;
		this.method = method;
		this.package_ = package_;
		this.class_ = class_;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCmpType() {
		return this.cmpType;
	}

	public void setCmpType(String cmpType) {
		this.cmpType = cmpType;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPackage_() {
		return this.package_;
	}

	public void setPackage_(String package_) {
		this.package_ = package_;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}