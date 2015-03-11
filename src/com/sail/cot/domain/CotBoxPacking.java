package com.sail.cot.domain;

/**
 * CotBoxPacking entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotBoxPacking implements java.io.Serializable {

	// Fields

	private Integer id;
	private String value;
	private String valueEn;
	private String type;
	private String remark;
	private Double materialPrice;
	private String formulaIn;
	private String formulaOut;
	private Integer facId;
	private String unit;
	private String checkCalculation;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotBoxPacking() {
	}

	/** full constructor */
	public CotBoxPacking(String value, String valueEn, String type,
			String remark, String formulaIn, String formulaOut, String unit,
			Integer factoryId, Double materialPrice) {
		this.value = value;
		this.valueEn = valueEn;
		this.type = type;
		this.remark = remark;
		this.formulaIn = formulaIn;
		this.formulaOut = formulaOut;
		this.unit = unit;
		this.facId = factoryId;
		this.materialPrice = materialPrice;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueEn() {
		return this.valueEn;
	}

	public void setValueEn(String valueEn) {
		this.valueEn = valueEn;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Double getMaterialPrice() {
		return materialPrice;
	}

	public void setMaterialPrice(Double materialPrice) {
		this.materialPrice = materialPrice;
	}

	public String getFormulaIn() {
		return formulaIn;
	}

	public void setFormulaIn(String formulaIn) {
		this.formulaIn = formulaIn;
	}

	public String getFormulaOut() {
		return formulaOut;
	}

	public void setFormulaOut(String formulaOut) {
		this.formulaOut = formulaOut;
	}

	public Integer getFacId() {
		return facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCheckCalculation() {
		return checkCalculation;
	}

	public void setCheckCalculation(String checkCalculation) {
		this.checkCalculation = checkCalculation;
	}

}