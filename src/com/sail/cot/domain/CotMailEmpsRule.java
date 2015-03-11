package com.sail.cot.domain;

/**
 * CotMailEmpsRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailEmpsRule implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer empId;
	private String empName;
	private String relate;
	private String ruleName;
	private String xmlPath;
	private Integer ruleDefault;
	private String ruleDesc;
	private String type;

	// Constructors

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	/** default constructor */
	public CotMailEmpsRule() {
	}

	/** full constructor */
	public CotMailEmpsRule(Integer empId, String empName, String relate,
			String ruleName, String xmlPath, Integer ruleDefault) {
		this.empId = empId;
		this.empName = empName;
		this.relate = relate;
		this.ruleName = ruleName;
		this.xmlPath = xmlPath;
		this.ruleDefault = ruleDefault;
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

	public String getEmpName() {
		return this.empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getRelate() {
		return this.relate;
	}

	public void setRelate(String relate) {
		this.relate = relate;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getXmlPath() {
		return this.xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public Integer getRuleDefault() {
		return this.ruleDefault;
	}

	public void setRuleDefault(Integer ruleDefault) {
		this.ruleDefault = ruleDefault;
	}

}