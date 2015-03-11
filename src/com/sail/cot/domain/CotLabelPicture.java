package com.sail.cot.domain;


/**
 * CotLabel entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotLabelPicture implements java.io.Serializable {

	// Fields

	private Integer id;
	private String picName;
	private Long picCap;
	private Long picL;
	private Long picH;
	private Integer labelId;
	private byte[] picImg;
	

	// Constructors

	/** default constructor */
	public CotLabelPicture() {
	}


	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getPicName() {
		return picName;
	}


	public void setPicName(String picName) {
		this.picName = picName;
	}

	public Long getPicCap() {
		return picCap;
	}


	public void setPicCap(Long picCap) {
		this.picCap = picCap;
	}


	public Long getPicL() {
		return picL;
	}


	public void setPicL(Long picL) {
		this.picL = picL;
	}


	public Long getPicH() {
		return picH;
	}


	public void setPicH(Long picH) {
		this.picH = picH;
	}


	public Integer getLabelId() {
		return labelId;
	}


	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}


	public byte[] getPicImg() {
		return picImg;
	}


	public void setPicImg(byte[] picImg) {
		this.picImg = picImg;
	}

	

}