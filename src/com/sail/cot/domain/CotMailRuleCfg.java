package com.sail.cot.domain;

/**
 * CotMailRuleCfg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailRuleCfg implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String property;
	private String type;
	// Constructors

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/** default constructor */
	public CotMailRuleCfg() {
	}

	/** full constructor */
	public CotMailRuleCfg(String name, String property) {
		this.name = name;
		this.property = property;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}