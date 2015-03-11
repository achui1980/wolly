package com.sail.cot.domain;

/**
 * CotCalculation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCalculation implements java.io.Serializable {

	// Fields

	private Integer id;
	private String expessionIn;
	private String expressionOut;
	private String checkCalculation;
	private String remark;
	private String calName;
	private String op;
	private String chk;

	// Constructors

	public String getCalName() {
		return calName;
	}

	public void setCalName(String calName) {
		this.calName = calName;
	}

	/** default constructor */
	public CotCalculation() {
	}

	/** full constructor */
	public CotCalculation(String expessionIn, String expressionOut,
			String remark,String checkCalculation) {
		this.expessionIn = expessionIn;
		this.expressionOut = expressionOut;
		this.checkCalculation = checkCalculation;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExpessionIn() {
		return this.expessionIn;
	}

	public void setExpessionIn(String expessionIn) {
		this.expessionIn = expessionIn;
	}

	public String getExpressionOut() {
		return this.expressionOut;
	}

	public void setExpressionOut(String expressionOut) {
		this.expressionOut = expressionOut;
	}
	
	public String getCheckCalculation() {
		return checkCalculation;
	}

	public void setCheckCalculation(String checkCalculation) {
		this.checkCalculation = checkCalculation;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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