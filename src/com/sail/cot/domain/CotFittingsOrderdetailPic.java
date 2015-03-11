package com.sail.cot.domain;

/**
 * CotFittingsOrderdetailPic entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFittingsOrderdetailPic implements java.io.Serializable {

	// Fields

	private Integer id;
	private byte[] picImg;
	private Integer picSize;
	private String eleId;
	private Integer fkId;
	private String picName;

	// Constructors

	/** default constructor */
	public CotFittingsOrderdetailPic() {
	}

	
	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getPicImg() {
		return this.picImg;
	}

	public void setPicImg(byte[] picImg) {
		this.picImg = picImg;
	}

	public Integer getPicSize() {
		return this.picSize;
	}

	public void setPicSize(Integer picSize) {
		this.picSize = picSize;
	}

	public String getEleId() {
		return this.eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public Integer getFkId() {
		return this.fkId;
	}

	public void setFkId(Integer fkId) {
		this.fkId = fkId;
	}

	public String getPicName() {
		return this.picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

}