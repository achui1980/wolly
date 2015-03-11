package com.sail.cot.domain;

import java.sql.Timestamp;

import java.util.Date;

/**
 * CotImportresult entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotImportresult implements java.io.Serializable {

	// Fields

	private Integer id;
	private String impType;
	private Timestamp impTime;
	private String impFilename;
	private String impFilepath;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotImportresult() {
	}

	/** minimal constructor */
	public CotImportresult(Timestamp impTime) {
		this.impTime = impTime;
	}

	/** full constructor */
	public CotImportresult(String impType, Timestamp impTime, String impFilename,
			String impFilepath) {
		this.impType = impType;
		this.impTime = impTime;
		this.impFilename = impFilename;
		this.impFilepath = impFilepath;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImpType() {
		return this.impType;
	}

	public void setImpType(String impType) {
		this.impType = impType;
	}

	public Date getImpTime() {
		return this.impTime;
	}

	public void setImpTime(Timestamp impTime) {
		this.impTime = impTime;
	}

	public String getImpFilename() {
		return this.impFilename;
	}

	public void setImpFilename(String impFilename) {
		this.impFilename = impFilename;
	}

	public String getImpFilepath() {
		return this.impFilepath;
	}

	public void setImpFilepath(String impFilepath) {
		this.impFilepath = impFilepath;
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