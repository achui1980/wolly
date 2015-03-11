package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotTaxType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTaxType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String taxName;
	private String taxCode;
	private String taxRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotTaxType() {
	}

	/** full constructor */
	public CotTaxType(String taxName, String taxCode, String taxRemark,
			Set cotHsInfos) {
		this.taxName = taxName;
		this.taxCode = taxCode;
		this.taxRemark = taxRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTaxName() {
		return this.taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getTaxRemark() {
		return this.taxRemark;
	}

	public void setTaxRemark(String taxRemark) {
		this.taxRemark = taxRemark;
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