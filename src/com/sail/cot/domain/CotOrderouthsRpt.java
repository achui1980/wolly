package com.sail.cot.domain;

/**
 * CotOrderouthsRpt entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderouthsRpt implements java.io.Serializable {

	// Fields

	private Integer id;
	private String hsName;
	private String rptContainerCount;
	private String rptBoxCount;
	private String rptCbm;
	private String rptGrossW;
	private String rptNetW;
	private String rptAvgPrice;
	private String rptTotalMoney;
	private String hsOutName;
	private String rptOutContainerCount;
	private String rptOutBoxCount;
	private String rptOutCbm;
	private String rptOutGrossW;
	private String rptOutNetW;
	private String rptOutAvgPrice;
	private String rptOutTotalMoney;
	private Integer orderOutId;

	// Constructors

	/** default constructor */
	public CotOrderouthsRpt() {
	}

	/** full constructor */
	public CotOrderouthsRpt(String hsName, String rptContainerCount,
			String rptBoxCount, String rptCbm, String rptGrossW,
			String rptNetW, String rptAvgPrice, String rptTotalMoney,
			Integer orderOutId) {
		this.hsName = hsName;
		this.rptContainerCount = rptContainerCount;
		this.rptBoxCount = rptBoxCount;
		this.rptCbm = rptCbm;
		this.rptGrossW = rptGrossW;
		this.rptNetW = rptNetW;
		this.rptAvgPrice = rptAvgPrice;
		this.rptTotalMoney = rptTotalMoney;
		this.orderOutId = orderOutId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHsName() {
		return this.hsName;
	}

	public void setHsName(String hsName) {
		this.hsName = hsName;
	}

	public String getRptContainerCount() {
		return this.rptContainerCount;
	}

	public void setRptContainerCount(String rptContainerCount) {
		this.rptContainerCount = rptContainerCount;
	}

	public String getRptBoxCount() {
		return this.rptBoxCount;
	}

	public void setRptBoxCount(String rptBoxCount) {
		this.rptBoxCount = rptBoxCount;
	}

	public String getRptCbm() {
		return this.rptCbm;
	}

	public void setRptCbm(String rptCbm) {
		this.rptCbm = rptCbm;
	}

	public String getRptGrossW() {
		return this.rptGrossW;
	}

	public void setRptGrossW(String rptGrossW) {
		this.rptGrossW = rptGrossW;
	}

	public String getRptNetW() {
		return this.rptNetW;
	}

	public void setRptNetW(String rptNetW) {
		this.rptNetW = rptNetW;
	}

	public String getRptAvgPrice() {
		return this.rptAvgPrice;
	}

	public void setRptAvgPrice(String rptAvgPrice) {
		this.rptAvgPrice = rptAvgPrice;
	}

	public String getRptTotalMoney() {
		return this.rptTotalMoney;
	}

	public void setRptTotalMoney(String rptTotalMoney) {
		this.rptTotalMoney = rptTotalMoney;
	}

	public Integer getOrderOutId() {
		return this.orderOutId;
	}

	public void setOrderOutId(Integer orderOutId) {
		this.orderOutId = orderOutId;
	}

	public String getHsOutName() {
		return hsOutName;
	}

	public void setHsOutName(String hsOutName) {
		this.hsOutName = hsOutName;
	}

	public String getRptOutContainerCount() {
		return rptOutContainerCount;
	}

	public void setRptOutContainerCount(String rptOutContainerCount) {
		this.rptOutContainerCount = rptOutContainerCount;
	}

	public String getRptOutBoxCount() {
		return rptOutBoxCount;
	}

	public void setRptOutBoxCount(String rptOutBoxCount) {
		this.rptOutBoxCount = rptOutBoxCount;
	}

	public String getRptOutCbm() {
		return rptOutCbm;
	}

	public void setRptOutCbm(String rptOutCbm) {
		this.rptOutCbm = rptOutCbm;
	}

	public String getRptOutGrossW() {
		return rptOutGrossW;
	}

	public void setRptOutGrossW(String rptOutGrossW) {
		this.rptOutGrossW = rptOutGrossW;
	}

	public String getRptOutNetW() {
		return rptOutNetW;
	}

	public void setRptOutNetW(String rptOutNetW) {
		this.rptOutNetW = rptOutNetW;
	}

	public String getRptOutAvgPrice() {
		return rptOutAvgPrice;
	}

	public void setRptOutAvgPrice(String rptOutAvgPrice) {
		this.rptOutAvgPrice = rptOutAvgPrice;
	}

	public String getRptOutTotalMoney() {
		return rptOutTotalMoney;
	}

	public void setRptOutTotalMoney(String rptOutTotalMoney) {
		this.rptOutTotalMoney = rptOutTotalMoney;
	}

}