package com.sail.cot.domain;

/**
 * CotMailRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailRule implements java.io.Serializable {

	// Fields

	private Integer id;
	private String leftTerm;
	private String op;
	private String rightTerm;
	private Integer ruleId;
	private Integer ruleCfgId;
	private Integer custId;
	private String type;
	private String relate;

	// Constructors

	public Integer getRuleCfgId() {
		return ruleCfgId;
	}

	public void setRuleCfgId(Integer ruleCfgId) {
		this.ruleCfgId = ruleCfgId;
	}

	/** default constructor */
	public CotMailRule() {
	}

	/** full constructor */
	public CotMailRule(String leftTerm, String op, String rightTerm,
			Integer ruleId) {
		this.leftTerm = leftTerm;
		this.op = op;
		this.rightTerm = rightTerm;
		this.ruleId = ruleId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLeftTerm() {
		return this.leftTerm;
	}

	public void setLeftTerm(String leftTerm) {
		this.leftTerm = leftTerm;
	}

	public String getOp() {
		return this.op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getRightTerm() {
		return this.rightTerm;
	}

	public void setRightTerm(String rightTerm) {
		this.rightTerm = rightTerm;
	}

	public Integer getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getRelate() {
		return relate;
	}

	public void setRelate(String relate) {
		this.relate = relate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}