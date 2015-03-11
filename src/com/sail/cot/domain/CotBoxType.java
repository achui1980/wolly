package com.sail.cot.domain;

/**
 * CotEmps entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotBoxType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String typeName;
	private String typeNameEn;
	private Integer boxIName;
	private Integer boxMName;
	private Integer boxOName;
	private Integer boxPName;
	private Integer inputGridType;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotBoxType() {
	}

	/** minimal constructor */
	public CotBoxType(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotBoxType(Integer id, String typeName, Integer boxIName, Integer boxMName, Integer boxOName) {
		this.id = id;
		this.typeName = typeName;
		this.boxIName = boxIName;
		this.boxMName = boxMName;
		this.boxOName = boxOName;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getBoxIName() {
		return boxIName;
	}

	public void setBoxIName(Integer boxIName) {
		this.boxIName = boxIName;
	}

	public Integer getBoxMName() {
		return boxMName;
	}

	public void setBoxMName(Integer boxMName) {
		this.boxMName = boxMName;
	}

	public Integer getBoxOName() {
		return boxOName;
	}

	public void setBoxOName(Integer boxOName) {
		this.boxOName = boxOName;
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

	public String getTypeNameEn() {
		return typeNameEn;
	}

	public void setTypeNameEn(String typeNameEn) {
		this.typeNameEn = typeNameEn;
	}

	public Integer getBoxPName() {
		return boxPName;
	}

	public void setBoxPName(Integer boxPName) {
		this.boxPName = boxPName;
	}

	public Integer getInputGridType() {
		return inputGridType;
	}

	public void setInputGridType(Integer inputGridType) {
		this.inputGridType = inputGridType;
	}

	

}