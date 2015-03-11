package com.sail.cot.domain;

import java.sql.Date;

/**
 * CotFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFile implements java.io.Serializable {

	// Fields

	private Integer id;
	private String fileName;
	private String filePath;
	private Date fileDate;
	private String fileDesc;
	private Integer eleId;
	private byte[] fileContent;
	private String op;
	private String chk;
	// Constructors

	/** default constructor */
	public CotFile() {
	}

	/** minimal constructor */
	public CotFile(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotFile(Integer id, String fileName, String filePath, Date fileDate,
			String fileDesc,Integer otherId) {
		this.id = id;
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileDate = fileDate;
		this.fileDesc = fileDesc;
		
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getFileDate() {
		return this.fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}
    
    public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public Integer getEleId() {
		return eleId;
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

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	
}