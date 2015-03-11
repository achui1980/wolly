package com.sail.cot.domain;

/**
 * CotMailExecute entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailExecute implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String method;
	private String argsName;
	private String args;
	private Integer ruleId;
	private String package_;
	private String class_;
	private Integer custId;
	private String type;

	private Integer executeCfgId;


	// Constructors

	public Integer getExecuteCfgId() {
		return executeCfgId;
	}

	public void setExecuteCfgId(Integer executeCfgId) {
		this.executeCfgId = executeCfgId;
	}

	/** default constructor */
	public CotMailExecute() {
	}

	/** full constructor */
	public CotMailExecute(String method, String args, Integer ruleId,
			String package_, String class_,String name) {
		this.method = method;
		this.args = args;
		this.ruleId = ruleId;
		this.package_ = package_;
		this.class_ = class_;
		this.name = name;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getArgs() {
		return this.args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public Integer getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
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

	public String getArgsName() {
		return argsName;
	}

	public void setArgsName(String argsName) {
		this.argsName = argsName;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}