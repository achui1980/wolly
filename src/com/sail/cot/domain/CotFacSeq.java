package com.sail.cot.domain;

import java.util.Date;

/**
 * CotCustSeq entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFacSeq implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer facId;
	private Integer eleSeq;
	private Date currDate;

	// Constructors

	/** default constructor */
	public CotFacSeq() {
	}

	/** minimal constructor */
	public CotFacSeq(Integer facId) {
		this.facId = facId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFacId() {
		return facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public Integer getEleSeq() {
		return eleSeq;
	}

	public void setEleSeq(Integer eleSeq) {
		this.eleSeq = eleSeq;
	}

	public Date getCurrDate() {
		return currDate;
	}

	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}


}