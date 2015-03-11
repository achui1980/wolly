package com.sail.cot.domain.vo;

/**
 * CotTypeLv1 entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTypeLvVO implements java.io.Serializable {

	// Fields

	private Integer id;
	private String cnEnName;

	// Constructors

	/** default constructor */
	public CotTypeLvVO() {
	}

	/** minimal constructor */
	public CotTypeLvVO(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCnEnName() {
		return cnEnName;
	}

	public void setCnEnName(String cnEnName) {
		this.cnEnName = cnEnName;
	}


}