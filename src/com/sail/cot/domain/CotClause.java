package com.sail.cot.domain;

/**
 * CotClause entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotClause implements java.io.Serializable {

	// Fields

	private Integer id;
	private String clauseName;
	private String clauseRemark;
	private String op;
	private String chk;
	private Integer calId;
	// Constructors

	public Integer getCalId() {
		return calId;
	}

	public void setCalId(Integer calId) {
		this.calId = calId;
	}

	/** default constructor */
	public CotClause() {
	}

	/** full constructor */
	public CotClause(String clauseName, String clauseRemark) {
		this.clauseName = clauseName;
		this.clauseRemark = clauseRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClauseName() {
		return this.clauseName;
	}

	public void setClauseName(String clauseName) {
		this.clauseName = clauseName;
	}

	public String getClauseRemark() {
		return this.clauseRemark;
	}

	public void setClauseRemark(String clauseRemark) {
		this.clauseRemark = clauseRemark;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

}