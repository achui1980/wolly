package com.sail.cot.domain;

import java.util.Date;

/**
 * CotLoginInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotLoginInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginEmpid;
	private String loginName;
	private String loginIpaddr;
	private Date loginTime;
	private String sessionId;

	// Constructors

	/** default constructor */
	public CotLoginInfo() {
	}

	/** minimal constructor */
	public CotLoginInfo(String loginEmpid) {
		this.loginEmpid = loginEmpid;
	}

	/** full constructor */
	public CotLoginInfo(String loginEmpid, String loginName,
			String loginIpaddr, Date loginTime) {
		this.loginEmpid = loginEmpid;
		this.loginName = loginName;
		this.loginIpaddr = loginIpaddr;
		this.loginTime = loginTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginEmpid() {
		return this.loginEmpid;
	}

	public void setLoginEmpid(String loginEmpid) {
		this.loginEmpid = loginEmpid;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginIpaddr() {
		return this.loginIpaddr;
	}

	public void setLoginIpaddr(String loginIpaddr) {
		this.loginIpaddr = loginIpaddr;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}