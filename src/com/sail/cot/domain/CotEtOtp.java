package com.sail.cot.domain;

/**
 * CotEtOtp entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotEtOtp implements java.io.Serializable {

	// Fields

	private Integer id;
	private String barCode;//令牌号（背面条形码)
	private String authKey;//密钥
	private Long currSucc;//成功值
	private Integer currDft;//漂移值

	// Constructors

	/** default constructor */
	public CotEtOtp() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public Long getCurrSucc() {
		return currSucc;
	}

	public void setCurrSucc(Long currSucc) {
		this.currSucc = currSucc;
	}

	public Integer getCurrDft() {
		return currDft;
	}

	public void setCurrDft(Integer currDft) {
		this.currDft = currDft;
	}

	
}