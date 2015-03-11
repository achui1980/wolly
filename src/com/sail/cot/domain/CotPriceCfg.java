package com.sail.cot.domain;

/**
 * CotPriceCfg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPriceCfg implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer shipPortId;
	private Integer caluseId;
	private Integer currencyId;
	private Integer noPrice;
	private Integer havePrice;
	private Integer situationId;
	private Double priceFob;
	private Double insureRate;
	private Double priceProfit;
	private Double insureAddRate;
	private Integer containerTypeId;
	private Integer trafficTypeId;
	private Integer companyId;
	private Integer insertType;
	private String op;
	private String chk;
	private Float tuiLv;
	private String orderAddress;
	private Integer appendDay;
	private Integer facPriceUnit;
	private String givenAddress;
	private Integer checkPerson;
	private Long isCheck;
	private Integer orderTip;

	// Constructors

	public Float getTuiLv() {
		return tuiLv;
	}

	public void setTuiLv(Float tuiLv) {
		this.tuiLv = tuiLv;
	}

	/** default constructor */
	public CotPriceCfg() {
	}

	/** full constructor */
	public CotPriceCfg(Integer shipPortId, Integer caluseId,
			Integer currencyId, Integer noPrice, Integer havePrice,
			Integer situationId, Double priceFob, Double insureRate,
			Double priceProfit, Double insureAddRate, Integer containerTypeId) {
		this.shipPortId = shipPortId;
		this.caluseId = caluseId;
		this.currencyId = currencyId;
		this.noPrice = noPrice;
		this.havePrice = havePrice;
		this.situationId = situationId;
		this.priceFob = priceFob;
		this.insureRate = insureRate;
		this.priceProfit = priceProfit;
		this.insureAddRate = insureAddRate;
		this.containerTypeId = containerTypeId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShipPortId() {
		return this.shipPortId;
	}

	public void setShipPortId(Integer shipPortId) {
		this.shipPortId = shipPortId;
	}

	public Integer getCaluseId() {
		return this.caluseId;
	}

	public void setCaluseId(Integer caluseId) {
		this.caluseId = caluseId;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getNoPrice() {
		return this.noPrice;
	}

	public void setNoPrice(Integer noPrice) {
		this.noPrice = noPrice;
	}

	public Integer getHavePrice() {
		return this.havePrice;
	}

	public void setHavePrice(Integer havePrice) {
		this.havePrice = havePrice;
	}

	public Integer getSituationId() {
		return this.situationId;
	}

	public void setSituationId(Integer situationId) {
		this.situationId = situationId;
	}

	public Double getPriceFob() {
		return this.priceFob;
	}

	public void setPriceFob(Double priceFob) {
		this.priceFob = priceFob;
	}

	public Double getInsureRate() {
		return this.insureRate;
	}

	public void setInsureRate(Double insureRate) {
		this.insureRate = insureRate;
	}

	public Double getPriceProfit() {
		return this.priceProfit;
	}

	public void setPriceProfit(Double priceProfit) {
		this.priceProfit = priceProfit;
	}

	public Double getInsureAddRate() {
		return this.insureAddRate;
	}

	public void setInsureAddRate(Double insureAddRate) {
		this.insureAddRate = insureAddRate;
	}

	public Integer getContainerTypeId() {
		return this.containerTypeId;
	}

	public void setContainerTypeId(Integer containerTypeId) {
		this.containerTypeId = containerTypeId;
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

	public Integer getTrafficTypeId() {
		return trafficTypeId;
	}

	public void setTrafficTypeId(Integer trafficTypeId) {
		this.trafficTypeId = trafficTypeId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getInsertType() {
		return insertType;
	}

	public void setInsertType(Integer insertType) {
		this.insertType = insertType;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public Integer getAppendDay() {
		return appendDay;
	}

	public void setAppendDay(Integer appendDay) {
		this.appendDay = appendDay;
	}

	public Integer getFacPriceUnit() {
		return facPriceUnit;
	}

	public void setFacPriceUnit(Integer facPriceUnit) {
		this.facPriceUnit = facPriceUnit;
	}

	public String getGivenAddress() {
		return givenAddress;
	}

	public void setGivenAddress(String givenAddress) {
		this.givenAddress = givenAddress;
	}

	public Integer getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(Integer checkPerson) {
		this.checkPerson = checkPerson;
	}

	public Long getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Long isCheck) {
		this.isCheck = isCheck;
	}

	public Integer getOrderTip() {
		return orderTip;
	}

	public void setOrderTip(Integer orderTip) {
		this.orderTip = orderTip;
	}

}