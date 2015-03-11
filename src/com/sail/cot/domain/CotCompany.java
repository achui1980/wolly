package com.sail.cot.domain;

/**
 * CotCompany entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCompany implements java.io.Serializable {

	// Fields

	private Integer id;
	private String companyName;
	private String companyEnName;
	private String companyShortName;
	private String companyCorporation;
	private String companyAddr;
	private String companyEnAddr;
	private String companyNbr;
	private String companyFax;
	private String companyPost;
	private String companyEmail;
	private String companyWebsite;
	private String companyRemark;
	private Integer companyIsdefault;
	private byte[] companyLogo;
	private String companyBank;
	private String bankAccount;
	private String bankAddress;
	private String bankContact;
	private String bankTel;
	private String companybeneficiary;
	private String advisingBank;
	private String swiftAddress;
	private String cableAddress;
	private String telexNo;
	private String op;
	private String chk;
	
	private String account;//邮件帐号
	private String mailPwd;//邮箱密码
	private String smtpHost;//邮箱Smtp服务器地址
	private Long smtpIss;
	private Long smtpPort;
	private String companyNo;

	// Constructors

	/** default constructor */
	public CotCompany() {
	}

	/** minimal constructor */
	public CotCompany(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotCompany(Integer id, String companyName, String companyEnName, String companyShortName,
			String companyCorporation,String companyAddr, String companyEnAddr,String companyNbr, 
			String companyFax,String companyPost, String companyEmail, String companyWebsite, 
			String companyRemark,Integer companyIsdefault) {
		this.id = id;
		this.companyName = companyName;
		this.companyEnName = companyEnName;
		this.companyShortName = companyShortName;
		this.companyCorporation = companyCorporation;
		this.companyAddr = companyAddr;
		this.companyEnAddr = companyEnAddr;
		this.companyNbr = companyNbr;
		this.companyFax = companyFax;
		this.companyPost = companyPost;
		this.companyEmail = companyEmail;
		this.companyWebsite = companyWebsite;
		this.companyRemark = companyRemark;
		this.companyIsdefault = companyIsdefault;
	}

	// Property accessors

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddr() {
		return this.companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getCompanyNbr() {
		return this.companyNbr;
	}

	public void setCompanyNbr(String companyNbr) {
		this.companyNbr = companyNbr;
	}

	public String getCompanyFax() {
		return this.companyFax;
	}

	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}

	public String getCompanyEmail() {
		return this.companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyWebsite() {
		return this.companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public String getCompanyRemark() {
		return this.companyRemark;
	}

	public void setCompanyRemark(String companyRemark) {
		this.companyRemark = companyRemark;
	}

	public Integer getCompanyIsdefault() {
		return companyIsdefault;
	}

	public void setCompanyIsdefault(Integer companyIsdefault) {
		this.companyIsdefault = companyIsdefault;
	}
    
	public String getCompanyEnName() {
		return companyEnName;
	}

	public void setCompanyEnName(String companyEnName) {
		this.companyEnName = companyEnName;
	}

	public String getCompanyShortName() {
		return companyShortName;
	}

	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}

	public String getCompanyCorporation() {
		return companyCorporation;
	}

	public void setCompanyCorporation(String companyCorporation) {
		this.companyCorporation = companyCorporation;
	}

	public String getCompanyEnAddr() {
		return companyEnAddr;
	}

	public void setCompanyEnAddr(String companyEnAddr) {
		this.companyEnAddr = companyEnAddr;
	}

	public String getCompanyPost() {
		return companyPost;
	}

	public void setCompanyPost(String companyPost) {
		this.companyPost = companyPost;
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

	public byte[] getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(byte[] companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getCompanyBank() {
		return companyBank;
	}

	public void setCompanyBank(String companyBank) {
		this.companyBank = companyBank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankContact() {
		return bankContact;
	}

	public void setBankContact(String bankContact) {
		this.bankContact = bankContact;
	}

	public String getBankTel() {
		return bankTel;
	}

	public void setBankTel(String bankTel) {
		this.bankTel = bankTel;
	}

	public String getCompanybeneficiary() {
		return companybeneficiary;
	}

	public void setCompanybeneficiary(String companybeneficiary) {
		this.companybeneficiary = companybeneficiary;
	}

	public String getAdvisingBank() {
		return advisingBank;
	}

	public void setAdvisingBank(String advisingBank) {
		this.advisingBank = advisingBank;
	}

	public String getSwiftAddress() {
		return swiftAddress;
	}

	public void setSwiftAddress(String swiftAddress) {
		this.swiftAddress = swiftAddress;
	}

	public String getCableAddress() {
		return cableAddress;
	}

	public void setCableAddress(String cableAddress) {
		this.cableAddress = cableAddress;
	}

	public String getTelexNo() {
		return telexNo;
	}

	public void setTelexNo(String telexNo) {
		this.telexNo = telexNo;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMailPwd() {
		return mailPwd;
	}

	public void setMailPwd(String mailPwd) {
		this.mailPwd = mailPwd;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public Long getSmtpIss() {
		return smtpIss;
	}

	public void setSmtpIss(Long smtpIss) {
		this.smtpIss = smtpIss;
	}

	public Long getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Long smtpPort) {
		this.smtpPort = smtpPort;
	}

	
}