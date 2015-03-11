package com.sail.cot.domain;

import java.sql.Blob; 

/**
 * CotPicture entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPicture implements java.io.Serializable {

	// Fields

	private Integer id;
	private String picName;
	private String picPath;
	private Long picCap;
	private Long picL;
	private Long picH;
	private String picRealpath;
	private Integer eleId;
	private Integer picFlag;
	private byte[] picImg;
	private String op;
	private String chk;

	// Constructors
	/** default constructor */
	public CotPicture() {
	}

	/** minimal constructor */
	public CotPicture(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotPicture(Integer id, String picName, String picPath, Long picCap,
			Long picL, Long picH, Integer eleId) {
		this.id = id;
		this.picName = picName;
		this.picPath = picPath;
		this.picCap = picCap;
		this.picL = picL;
		this.picH = picH;
		this.eleId = eleId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPicName() {
		return this.picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicPath() {
		return this.picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	public Integer getPicFlag() {
		return picFlag;
	}

	public void setPicFlag(Integer picFlag) {
		this.picFlag = picFlag;
	}

	public Long getPicCap() {
		return this.picCap;
	}

	public void setPicCap(Long picCap) {
		this.picCap = picCap;
	}

	public Long getPicL() {
		return this.picL;
	}

	public void setPicL(Long picL) {
		this.picL = picL;
	}

	public Long getPicH() {
		return this.picH;
	}

	public void setPicH(Long picH) {
		this.picH = picH;
	}

	public Integer getEleId() {
		return this.eleId;
	}

	public void setEleId(Integer eleId) {
		this.eleId = eleId;
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

	public String getPicRealpath() {
		return picRealpath;
	}

	public void setPicRealpath(String picRealpath) {
		this.picRealpath = picRealpath;
	}

	public byte[] getPicImg() {
		return picImg;
	}

	public void setPicImg(byte[] picImg) {
		this.picImg = picImg;
	}

	

}