package com.sail.cot.domain;

/**
 * CotElePacking entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotElePacking implements java.io.Serializable {

	// Fields

	private Integer id;
	private String packingName;
	private String packingNameEn;
	private Double packingPrice;
	private String packingUnit;
	private Integer factoryId;
	private Double packingTypePrice;
	private Integer packingCount;
	private String packingType;
	private Integer eleId;
	private Integer packingId;
	private String op;

	// Constructors

	/** default constructor */
	public CotElePacking() {
	}

	/** minimal constructor */
	public CotElePacking(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotElePacking(Integer id, String packingName, String packingNameEn,
			Double packingPrice, String packingUnit, Integer factoryId,
			Double packingTypePrice, Integer packingCount, String packingType,
			Integer eleId, Integer packingId) {
		this.id = id;
		this.packingName = packingName;
		this.packingNameEn = packingNameEn;
		this.packingPrice = packingPrice;
		this.packingUnit = packingUnit;
		this.factoryId = factoryId;
		this.packingTypePrice = packingTypePrice;
		this.packingCount = packingCount;
		this.packingType = packingType;
		this.eleId = eleId;
		this.packingId = packingId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPackingName() {
		return this.packingName;
	}

	public void setPackingName(String packingName) {
		this.packingName = packingName;
	}

	public String getPackingNameEn() {
		return this.packingNameEn;
	}

	public void setPackingNameEn(String packingNameEn) {
		this.packingNameEn = packingNameEn;
	}

	public Double getPackingPrice() {
		return this.packingPrice;
	}

	public void setPackingPrice(Double packingPrice) {
		this.packingPrice = packingPrice;
	}

	public String getPackingUnit() {
		return this.packingUnit;
	}

	public void setPackingUnit(String packingUnit) {
		this.packingUnit = packingUnit;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Double getPackingTypePrice() {
		return this.packingTypePrice;
	}

	public void setPackingTypePrice(Double packingTypePrice) {
		this.packingTypePrice = packingTypePrice;
	}

	public Integer getPackingCount() {
		return this.packingCount;
	}

	public void setPackingCount(Integer packingCount) {
		this.packingCount = packingCount;
	}

	public String getPackingType() {
		return this.packingType;
	}

	public void setPackingType(String packingType) {
		this.packingType = packingType;
	}

	public Integer getEleId() {
		return this.eleId;
	}

	public void setEleId(Integer eleId) {
		this.eleId = eleId;
	}

	public Integer getPackingId() {
		return this.packingId;
	}

	public void setPackingId(Integer packingId) {
		this.packingId = packingId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}