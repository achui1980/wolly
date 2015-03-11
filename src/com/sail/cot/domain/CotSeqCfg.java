package com.sail.cot.domain;

/**
 * CotSeqCfg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSeqCfg implements java.io.Serializable {

	// Fields

	private Integer id;
	private String key;
	private String obj;
	private String attribute;
	private String method;
	private String args;
	private String argsType;
	private String express;
	private String type;
	private String typeName;

	// Constructors

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** default constructor */
	public CotSeqCfg() {
	}

	/** full constructor */
	public CotSeqCfg(String key, String obj, String attribute, String method,
			String args, String express) {
		this.key = key;
		this.obj = obj;
		this.attribute = attribute;
		this.method = method;
		this.args = args;
		this.express = express;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getObj() {
		return this.obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}

	public String getAttribute() {
		return this.attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
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

	public String getExpress() {
		return this.express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArgsType() {
		return argsType;
	}

	public void setArgsType(String argsType) {
		this.argsType = argsType;
	}

}