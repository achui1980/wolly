package com.sail.cot.domain;

import java.util.Date;

/**
 * CotServerInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotServerInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String mechineKey;
	private String regeditKey;
	private String serverNo;
	private String isStangAlone;
	private String isAlone;
	private Date addTime;
	private String softVer;
	private String userTime;
	private String regeditModule;

	// Constructors

	/** default constructor */
	public CotServerInfo() {
	}



	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMechineKey() {
		return this.mechineKey;
	}

	public void setMechineKey(String mechineKey) {
		this.mechineKey = mechineKey;
	}

	public String getRegeditKey() {
		return this.regeditKey;
	}

	public void setRegeditKey(String regeditKey) {
		this.regeditKey = regeditKey;
	}

	public String getServerNo() {
		return this.serverNo;
	}

	public void setServerNo(String serverNo) {
		this.serverNo = serverNo;
	}



	public String getIsStangAlone() {
//		if(isStangAlone.length() > 8)
//			isStangAlone = isStangAlone.substring(7,8);
		return isStangAlone;
	}



	public void setIsStangAlone(String isStangAlone) {
		this.isStangAlone = isStangAlone;
	}



	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getSoftVer() {
		return this.softVer;
	}

	public void setSoftVer(String softVer) {
		this.softVer = softVer;
	}

	public String getUserTime() {
		return this.userTime;
	}

	public void setUserTime(String userTime) {
		this.userTime = userTime;
	}



	public String getIsAlone() {
		
		if(this.isStangAlone.length()>= 8)
			isAlone = isStangAlone.substring(7,8);
		return isAlone;
	}



	public void setIsAlone(String isAlone) {
		this.isAlone = isAlone;
	}



	public String getRegeditModule() {
		return regeditModule;
	}



	public void setRegeditModule(String regeditModule) {
		this.regeditModule = regeditModule;
	}

}