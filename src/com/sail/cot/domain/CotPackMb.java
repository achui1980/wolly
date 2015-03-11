package com.sail.cot.domain;

/**
 * CotOrderMb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPackMb implements java.io.Serializable {

	// Fields

	private Integer id;
	private byte [] picImg;
	private Integer picSize;
	private Integer fkId;

	// Constructors

	/** default constructor */
	public CotPackMb() {
	}

	/** full constructor */
	public CotPackMb(byte [] picImg, Integer picSize, Integer fkId) {
		this.picImg = picImg;
		this.picSize = picSize;
		this.fkId = fkId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte [] getPicImg() {
		return this.picImg;
	}

	public void setPicImg(byte [] picImg) {
		this.picImg = picImg;
	}

	public Integer getPicSize() {
		return this.picSize;
	}

	public void setPicSize(Integer picSize) {
		this.picSize = picSize;
	}

	public Integer getFkId() {
		return this.fkId;
	}

	public void setFkId(Integer fkId) {
		this.fkId = fkId;
	}

}