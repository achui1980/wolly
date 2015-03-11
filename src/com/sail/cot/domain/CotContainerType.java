package com.sail.cot.domain;

/**
 * CotContainerType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotContainerType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String containerName;
	private Float containerCube;
	private Float containerWeigh;
	private String containerRemark;
	private String op;
	private String chk;

	// Constructors

	public Float getContainerWeigh() {
		return containerWeigh;
	}

	public void setContainerWeigh(Float containerWeigh) {
		this.containerWeigh = containerWeigh;
	}

	/** default constructor */
	public CotContainerType() {
	}

	/** full constructor */
	public CotContainerType(String containerName,Float containerCube, String containerRemark) {
		this.containerName = containerName;
		this.containerRemark = containerRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public Float getContainerCube() {
		return containerCube;
	}

	public void setContainerCube(Float containerCube) {
		this.containerCube = containerCube;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContainerName() {
		return this.containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
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

	public String getContainerRemark() {
		return this.containerRemark;
	}

	public void setContainerRemark(String containerRemark) {
		this.containerRemark = containerRemark;
	}

}