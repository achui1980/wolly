package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotEleOther entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotEleOther implements java.io.Serializable {

	// Fields

	private Integer id;  
	private String cnName;
	private String enName;
	private String hscode;
	private String op;
	private String chk;
	private Set cotFiles = null;
	private Float taxRate;
	private String remark;
	// Constructors

	public Float getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Float taxRate) {
		this.taxRate = taxRate;
	}

	public Set getCotFiles() {
		return cotFiles;
	}

	public void setCotFiles(Set cotFiles) {
		this.cotFiles = cotFiles;
	}

	/** default constructor */
	public CotEleOther() {
	}

	/** minimal constructor */
	public CotEleOther(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotEleOther(Integer id, String cnName, String enName, String hscode,
			Integer eleId) {
		this.id = id;
		this.cnName = cnName;
		this.enName = enName;
		this.hscode = hscode;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCnName() {
		return this.cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return this.enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getHscode() {
		return this.hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}