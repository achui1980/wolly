package com.sail.cot.domain;

/**
 * CotEleCfg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotEleCfg implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer eleTypeId;
	private Integer eleFacId;
	private Integer elePriceFacUnit;
	private Integer elePriceOutUnit;
	private Long boxIbCount;
	private Long boxMbCount;
	private Long boxObCount;
	private String boxIbType;
	private String boxMbType;
	private String boxObType;
	private Integer boxTypeId;
	private String eleUnit;
	private Long eleFlag;
	private Integer eleUnitnum;
	private Long cfgFlag;
	private Float priceProfit;
	private String expessionIn;
	private String expressionOut;
	private String checkCalculation;
	private Float grossNum;
	private String op;
	private String chk;
	private Float tuiLv;
	private String expessionFacIn;
	private String expressionFacOut;
	private String checkFacCalculation;
	private Long boxPbCount;
	private String boxPbType;

	private Integer boxPbTypeId;
	private Integer boxIbTypeId;
	private Integer boxMbTypeId;
	private Integer boxObTypeId;
	private Integer inputGridTypeId;
	
	private Integer sizeFollow;
	private String costName;
	
	// Constructors

	public String getCostName() {
		return costName;
	}

	public void setCostName(String costName) {
		this.costName = costName;
	}

	public Float getTuiLv() {
		return tuiLv;
	}

	public void setTuiLv(Float tuiLv) {
		this.tuiLv = tuiLv;
	}

	/** default constructor */
	public CotEleCfg() {
	}

	/** full constructor */
	public CotEleCfg(Integer eleTypeId, Integer eleFacId,
			Integer elePriceFacUnit, Integer elePriceOutUnit, Long boxIbCount,
			Long boxMbCount, Long boxObCount, String boxIbType,
			String boxMbType, String boxObType, Integer boxTypeId,
			String eleUnit, Long eleFlag, Integer eleUnitnum, Long cfgFlag) {
		this.eleTypeId = eleTypeId;
		this.eleFacId = eleFacId;
		this.elePriceFacUnit = elePriceFacUnit;
		this.elePriceOutUnit = elePriceOutUnit;
		this.boxIbCount = boxIbCount;
		this.boxMbCount = boxMbCount;
		this.boxObCount = boxObCount;
		this.boxIbType = boxIbType;
		this.boxMbType = boxMbType;
		this.boxObType = boxObType;
		this.boxTypeId = boxTypeId;
		this.eleUnit = eleUnit;
		this.eleFlag = eleFlag;
		this.eleUnitnum = eleUnitnum;
		this.cfgFlag = cfgFlag;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEleTypeId() {
		return this.eleTypeId;
	}

	public void setEleTypeId(Integer eleTypeId) {
		this.eleTypeId = eleTypeId;
	}

	public Integer getEleFacId() {
		return this.eleFacId;
	}

	public void setEleFacId(Integer eleFacId) {
		this.eleFacId = eleFacId;
	}

	public Integer getElePriceFacUnit() {
		return this.elePriceFacUnit;
	}

	public void setElePriceFacUnit(Integer elePriceFacUnit) {
		this.elePriceFacUnit = elePriceFacUnit;
	}

	public Integer getElePriceOutUnit() {
		return this.elePriceOutUnit;
	}

	public void setElePriceOutUnit(Integer elePriceOutUnit) {
		this.elePriceOutUnit = elePriceOutUnit;
	}

	public Long getBoxIbCount() {
		return this.boxIbCount;
	}

	public void setBoxIbCount(Long boxIbCount) {
		this.boxIbCount = boxIbCount;
	}

	public Long getBoxMbCount() {
		return this.boxMbCount;
	}

	public void setBoxMbCount(Long boxMbCount) {
		this.boxMbCount = boxMbCount;
	}

	public Long getBoxObCount() {
		return this.boxObCount;
	}

	public void setBoxObCount(Long boxObCount) {
		this.boxObCount = boxObCount;
	}

	public Integer getBoxTypeId() {
		return this.boxTypeId;
	}

	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
	}

	public String getEleUnit() {
		return this.eleUnit;
	}

	public void setEleUnit(String eleUnit) {
		this.eleUnit = eleUnit;
	}

	public Long getEleFlag() {
		return this.eleFlag;
	}

	public void setEleFlag(Long eleFlag) {
		this.eleFlag = eleFlag;
	}

	public Integer getEleUnitnum() {
		return this.eleUnitnum;
	}

	public void setEleUnitnum(Integer eleUnitnum) {
		this.eleUnitnum = eleUnitnum;
	}

	public Long getCfgFlag() {
		return this.cfgFlag;
	}

	public void setCfgFlag(Long cfgFlag) {
		this.cfgFlag = cfgFlag;
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

	public Float getPriceProfit() {
		return priceProfit;
	}

	public void setPriceProfit(Float priceProfit) {
		this.priceProfit = priceProfit;
	}

	public String getExpessionIn() {
		return expessionIn;
	}

	public void setExpessionIn(String expessionIn) {
		this.expessionIn = expessionIn;
	}

	public String getExpressionOut() {
		return expressionOut;
	}

	public void setExpressionOut(String expressionOut) {
		this.expressionOut = expressionOut;
	}

	public String getCheckCalculation() {
		return checkCalculation;
	}

	public void setCheckCalculation(String checkCalculation) {
		this.checkCalculation = checkCalculation;
	}

	public Float getGrossNum() {
		return grossNum;
	}

	public void setGrossNum(Float grossNum) {
		this.grossNum = grossNum;
	}

	public String getExpessionFacIn() {
		return expessionFacIn;
	}

	public void setExpessionFacIn(String expessionFacIn) {
		this.expessionFacIn = expessionFacIn;
	}

	public String getExpressionFacOut() {
		return expressionFacOut;
	}

	public void setExpressionFacOut(String expressionFacOut) {
		this.expressionFacOut = expressionFacOut;
	}

	public String getCheckFacCalculation() {
		return checkFacCalculation;
	}

	public void setCheckFacCalculation(String checkFacCalculation) {
		this.checkFacCalculation = checkFacCalculation;
	}

	public String getBoxIbType() {
		return boxIbType;
	}

	public void setBoxIbType(String boxIbType) {
		this.boxIbType = boxIbType;
	}

	public String getBoxMbType() {
		return boxMbType;
	}

	public void setBoxMbType(String boxMbType) {
		this.boxMbType = boxMbType;
	}

	public String getBoxObType() {
		return boxObType;
	}

	public void setBoxObType(String boxObType) {
		this.boxObType = boxObType;
	}

	public Long getBoxPbCount() {
		return boxPbCount;
	}

	public void setBoxPbCount(Long boxPbCount) {
		this.boxPbCount = boxPbCount;
	}

	public String getBoxPbType() {
		return boxPbType;
	}

	public void setBoxPbType(String boxPbType) {
		this.boxPbType = boxPbType;
	}

	public Integer getBoxPbTypeId() {
		return boxPbTypeId;
	}

	public void setBoxPbTypeId(Integer boxPbTypeId) {
		if(boxPbTypeId!=null && boxPbTypeId==0){
			boxPbTypeId=null;
		}
		this.boxPbTypeId = boxPbTypeId;
	}

	public Integer getBoxIbTypeId() {
		return boxIbTypeId;
	}

	public void setBoxIbTypeId(Integer boxIbTypeId) {
		if(boxIbTypeId!=null && boxIbTypeId==0){
			boxIbTypeId=null;
		}
		this.boxIbTypeId = boxIbTypeId;
	}

	public Integer getBoxMbTypeId() {
		return boxMbTypeId;
	}

	public void setBoxMbTypeId(Integer boxMbTypeId) {
		if(boxMbTypeId!=null && boxMbTypeId==0){
			boxMbTypeId=null;
		}
		this.boxMbTypeId = boxMbTypeId;
	}

	public Integer getBoxObTypeId() {
		return boxObTypeId;
	}

	public void setBoxObTypeId(Integer boxObTypeId) {
		if(boxObTypeId!=null && boxObTypeId==0){
			boxObTypeId=null;
		}
		this.boxObTypeId = boxObTypeId;
	}

	public Integer getSizeFollow() {
		return sizeFollow;
	}

	public void setSizeFollow(Integer sizeFollow) {
		this.sizeFollow = sizeFollow;
	}

	public Integer getInputGridTypeId() {
		return inputGridTypeId;
	}

	public void setInputGridTypeId(Integer inputGridTypeId) {
		this.inputGridTypeId = inputGridTypeId;
	}
}