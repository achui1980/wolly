package com.sail.cot.domain;

import com.sail.cot.email.util.PasswordEncrypt;

/**
 * CotMailCfg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMailCfg implements java.io.Serializable {

	// Fields

	private Integer id;
	private String defaultHost;
	private String defaultHostSmtp;
	private String defaultAccount;
	private String defaultPwd;
	private Long defaultDebug;
	private Long defaultCount;
	private Long defaultPriv;
	private Long defaultMintues;
	private Long defaultAuth;
	private Long defaultIsSslpop3;
	private Long defaultPop3port;
	private Long defaultSmtpPort;
	private Long defaultIsSslsmtp;

	// Constructors

	/** default constructor */
	public CotMailCfg() {
	}

	/** full constructor */
	public CotMailCfg(String defaultHost, String defaultHostSmtp,
			String defaultAccount, String defaultPwd, Long defaultDebug,
			Long defaultCount, Long defaultPriv, Long defaultMintues,
			Long defaultAuth, Long defaultIsSslpop3, Long defaultPop3port,
			Long defaultSmtpPort, Long defaultIsSslsmtp) {
		this.defaultHost = defaultHost;
		this.defaultHostSmtp = defaultHostSmtp;
		this.defaultAccount = defaultAccount;
		this.defaultPwd = defaultPwd;
		this.defaultDebug = defaultDebug;
		this.defaultCount = defaultCount;
		this.defaultPriv = defaultPriv;
		this.defaultMintues = defaultMintues;
		this.defaultAuth = defaultAuth;
		this.defaultIsSslpop3 = defaultIsSslpop3;
		this.defaultPop3port = defaultPop3port;
		this.defaultSmtpPort = defaultSmtpPort;
		this.defaultIsSslsmtp = defaultIsSslsmtp;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDefaultHost() {
		return this.defaultHost;
	}

	public void setDefaultHost(String defaultHost) {
		this.defaultHost = defaultHost;
	}

	public String getDefaultHostSmtp() {
		return this.defaultHostSmtp;
	}

	public void setDefaultHostSmtp(String defaultHostSmtp) {
		this.defaultHostSmtp = defaultHostSmtp;
	}

	public String getDefaultAccount() {
		return this.defaultAccount;
	}

	public void setDefaultAccount(String defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	public String getDefaultPwd() {
		return this.defaultPwd;
	}

	public void setDefaultPwd(String defaultPwd) {
		this.defaultPwd = defaultPwd;
	}

	public Long getDefaultDebug() {
		return this.defaultDebug;
	}

	public void setDefaultDebug(Long defaultDebug) {
		this.defaultDebug = defaultDebug;
	}

	public Long getDefaultCount() {
		return this.defaultCount;
	}

	public void setDefaultCount(Long defaultCount) {
		this.defaultCount = defaultCount;
	}

	public Long getDefaultPriv() {
		return this.defaultPriv;
	}

	public void setDefaultPriv(Long defaultPriv) {
		this.defaultPriv = defaultPriv;
	}

	public Long getDefaultMintues() {
		return this.defaultMintues;
	}

	public void setDefaultMintues(Long defaultMintues) {
		this.defaultMintues = defaultMintues;
	}

	public Long getDefaultAuth() {
		return this.defaultAuth;
	}

	public void setDefaultAuth(Long defaultAuth) {
		this.defaultAuth = defaultAuth;
	}

	public Long getDefaultIsSslpop3() {
		return this.defaultIsSslpop3;
	}

	public void setDefaultIsSslpop3(Long defaultIsSslpop3) {
		this.defaultIsSslpop3 = defaultIsSslpop3;
	}

	public Long getDefaultPop3port() {
		return this.defaultPop3port;
	}

	public void setDefaultPop3port(Long defaultPop3port) {
		this.defaultPop3port = defaultPop3port;
	}

	public Long getDefaultSmtpPort() {
		return this.defaultSmtpPort;
	}

	public void setDefaultSmtpPort(Long defaultSmtpPort) {
		this.defaultSmtpPort = defaultSmtpPort;
	}

	public Long getDefaultIsSslsmtp() {
		return this.defaultIsSslsmtp;
	}

	public void setDefaultIsSslsmtp(Long defaultIsSslsmtp) {
		this.defaultIsSslsmtp = defaultIsSslsmtp;
	}

}