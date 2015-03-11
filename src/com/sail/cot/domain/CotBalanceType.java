package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotBalanceType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotBalanceType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String balanceName;
	private String balanceRemark;
	private String op;
	private String chk;

	// Constructors

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

	/** default constructor */
	public CotBalanceType() {
	}

	/** full constructor */
	public CotBalanceType(String balanceName, String balanceRemark,
			Set cotGivens) {
		this.balanceName = balanceName;
		this.balanceRemark = balanceRemark;

	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBalanceName() {
		return this.balanceName;
	}

	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}

	public String getBalanceRemark() {
		return this.balanceRemark;
	}

	public void setBalanceRemark(String balanceRemark) {
		this.balanceRemark = balanceRemark;
	}

	

}