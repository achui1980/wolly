package com.sail.cot.domain;

/**
 * CotRptFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotRptFile implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer rptType;
	private String rptfilePath;
	private String rptName;
	private String rptImgPath;
	private Integer flag;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotRptFile() {
	}

	/** full constructor */
	public CotRptFile(Integer rptType, String rptfilePath,
			String rptName, String rptImgPath) {
		this.rptType = rptType;
		this.rptfilePath = rptfilePath;
		this.rptName = rptName;
		this.rptImgPath = rptImgPath;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public String getRptfilePath() {
		return this.rptfilePath;
	}

	public void setRptfilePath(String rptfilePath) {
		this.rptfilePath = rptfilePath;
	}

	public String getRptName() {
		return this.rptName;
	}

	public void setRptName(String rptName) {
		this.rptName = rptName;
	}

	public Integer getRptType() {
		return rptType;
	}

	public void setRptType(Integer rptType) {
		this.rptType = rptType;
	}

	public String getRptImgPath() {
		return rptImgPath;
	}

	public void setRptImgPath(String rptImgPath) {
		this.rptImgPath = rptImgPath;
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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	

}