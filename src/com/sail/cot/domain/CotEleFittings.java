package com.sail.cot.domain;

/**
 * CotEleFittings entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotEleFittings implements java.io.Serializable {

	// Fields

	private Integer id;
	private String fitNo;
	private String fitName;
	private Integer facId;
	private String fitDesc;
	private Double fitUsedCount;
	private Double fitCount;
	private String fitUseUnit;
	private Double fitPrice;
	private Double fitTotalPrice;
	private Integer eleId;
	private Integer fittingId;
	private String fitRemark;
	private String eleChild;
	private Integer eleChildId;
	private String op;
	
	// Constructors

	public Integer getEleId() {
		return eleId;
	}

	public void setEleId(Integer eleId) {
		this.eleId = eleId;
	}

	public Integer getFittingId() {
		return fittingId;
	}

	public void setFittingId(Integer fittingId) {
		this.fittingId = fittingId;
	}

	/** default constructor */
	public CotEleFittings() {
	}

	/** full constructor */
	public CotEleFittings(String fitNo, String fitName, Integer facId,
			String fitDesc, Double fitUsedCount, Double fitCount,
			String fitUseUnit, Double fitPrice, Double fitTotalPrice) {
		this.fitNo = fitNo;
		this.fitName = fitName;
		this.facId = facId;
		this.fitDesc = fitDesc;
		this.fitUsedCount = fitUsedCount;
		this.fitCount = fitCount;
		this.fitUseUnit = fitUseUnit;
		this.fitPrice = fitPrice;
		this.fitTotalPrice = fitTotalPrice;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFitNo() {
		return this.fitNo;
	}

	public void setFitNo(String fitNo) {
		this.fitNo = fitNo;
	}

	public String getFitName() {
		return this.fitName;
	}

	public void setFitName(String fitName) {
		this.fitName = fitName;
	}

	public Integer getFacId() {
		return this.facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public String getFitDesc() {
		return this.fitDesc;
	}

	public void setFitDesc(String fitDesc) {
		this.fitDesc = fitDesc;
	}

	public Double getFitUsedCount() {
		return fitUsedCount;
	}

	public void setFitUsedCount(Double fitUsedCount) {
		this.fitUsedCount = fitUsedCount;
	}

	public Double getFitCount() {
		return fitCount;
	}

	public void setFitCount(Double fitCount) {
		this.fitCount = fitCount;
	}

	public String getFitUseUnit() {
		return this.fitUseUnit;
	}

	public void setFitUseUnit(String fitUseUnit) {
		this.fitUseUnit = fitUseUnit;
	}

	public Double getFitPrice() {
		return this.fitPrice;
	}

	public void setFitPrice(Double fitPrice) {
		this.fitPrice = fitPrice;
	}

	public Double getFitTotalPrice() {
		//fitTotalPrice = fitUsedCount*fitCount*fitPrice;
		return this.fitTotalPrice;
	}

	public void setFitTotalPrice(Double fitTotalPrice) {
		this.fitTotalPrice = fitTotalPrice;
	}

	public String getFitRemark() {
		return fitRemark;
	}

	public void setFitRemark(String fitRemark) {
		this.fitRemark = fitRemark;
	}

	public String getEleChild() {
		return eleChild;
	}

	public void setEleChild(String eleChild) {
		this.eleChild = eleChild;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Integer getEleChildId() {
		return eleChildId;
	}

	public void setEleChildId(Integer eleChildId) {
		this.eleChildId = eleChildId;
	}
}