package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotHsCompany entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotHsCompany implements java.io.Serializable {

	// Fields

	private Integer id;
	private String hsCompanyName;
	private String hsCompanyNameEn;
	private String hsContactNbr;
	private String hsFax;
	private String hsAdd;
	private String hsContantPerson;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotHsCompany() {
	}

	/** full constructor */
	public CotHsCompany(String hsCompanyName, String hsContantPerson,
			String hsContactNbr, Set cotHsInfos) {
		this.hsCompanyName = hsCompanyName;
		this.hsContantPerson = hsContantPerson;
		this.hsContactNbr = hsContactNbr;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHsCompanyName() {
		return this.hsCompanyName;
	}

	public void setHsCompanyName(String hsCompanyName) {
		this.hsCompanyName = hsCompanyName;
	}

	public String getHsContantPerson() {
		return this.hsContantPerson;
	}

	public void setHsContantPerson(String hsContantPerson) {
		this.hsContantPerson = hsContantPerson;
	}

	public String getHsContactNbr() {
		return this.hsContactNbr;
	}

	public void setHsContactNbr(String hsContactNbr) {
		this.hsContactNbr = hsContactNbr;
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

	public String getHsCompanyNameEn() {
		return hsCompanyNameEn;
	}

	public void setHsCompanyNameEn(String hsCompanyNameEn) {
		this.hsCompanyNameEn = hsCompanyNameEn;
	}

	public String getHsFax() {
		return hsFax;
	}

	public void setHsFax(String hsFax) {
		this.hsFax = hsFax;
	}

	public String getHsAdd() {
		return hsAdd;
	}

	public void setHsAdd(String hsAdd) {
		this.hsAdd = hsAdd;
	}
	
	
}