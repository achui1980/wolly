package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotTradeType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTradeType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String tradeCode;
	private String tradeName;
	private String tradeRemark;
	private String op;
	private String chk;

	// Constructors

	/** default constructor */
	public CotTradeType() {
	}

	/** full constructor */
	public CotTradeType(String tradeCode, String tradeName, String tradeRemark,
			Set cotHsInfos) {
		this.tradeCode = tradeCode;
		this.tradeName = tradeName;
		this.tradeRemark = tradeRemark;
		
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTradeCode() {
		return this.tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getTradeRemark() {
		return this.tradeRemark;
	}

	public void setTradeRemark(String tradeRemark) {
		this.tradeRemark = tradeRemark;
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
	
	
}