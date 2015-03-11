package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotEmps entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotEmps implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String empsName;
	private String empNameCn;
	private String empsPwd;
	private String empsId;
	private Long empsStatus;
	private String empsRemark;
	private Integer deptId;
	private Integer roleId;
	private Integer companyId;
	private String empsPhone;   //员工分机号
	private String empsMail;   //员工邮件地址
	private String empsAccount;//员工邮件帐号
	private String empsMailPwd;//员工邮箱密码
	private String empsMailHost;//员工邮箱服务器地址
	private String empsSmtpHost;//员工邮箱Smtp服务器地址
	private Long empsIsSSLPop3;
	private Long empsIsSSLSmtp;
	private Long empsPop3Port;
	private Long empsSmtpPort;
	private Long empsMintues;//新邮件检查时间
	private Set customers = new HashSet(0);
	private String empsMobile;
	private String empsSign; //员工的邮件签名
	private String empsMailTemplate; //员工的默认模板
	private String empsMailTemplateTransmit; //员工的默认模板
	private String empsMailTemplateReply; //员工的默认模板
	private String op;
	private String chk;
	private Integer etId;
	private Integer shenFlag;//1.是审核人;0和null不是
	private String empRight;
	private String passwordApproval;

	// Constructors

	public String getEmpsMail() {
		return empsMail;
	}

	public void setEmpsMail(String empsMail) {
		this.empsMail = empsMail;
	}

	public String getEmpsAccount() {
		return empsAccount;
	}

	public void setEmpsAccount(String empsAccount) {
		this.empsAccount = empsAccount;
	}

	public String getEmpsMailPwd() {
		return empsMailPwd;
	}

	public void setEmpsMailPwd(String empsMailPwd) {
		this.empsMailPwd = empsMailPwd;
	}

	public String getEmpsMailHost() {
		return empsMailHost;
	}

	public void setEmpsMailHost(String empsMailHost) {
		this.empsMailHost = empsMailHost;
	}

	public String getEmpsSmtpHost() {
		return empsSmtpHost;
	}

	public void setEmpsSmtpHost(String empsSmtpHost) {
		this.empsSmtpHost = empsSmtpHost;
	}

	/** default constructor */
	public CotEmps() {
	}

	/** minimal constructor */
	public CotEmps(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotEmps(Integer id, String empsName, String empsId, Long empsStatus,
			String empsRemark, Integer dept, Integer roleId) {
		this.id = id;
		this.empsName = empsName;
		this.empsId = empsId;
		this.empsStatus = empsStatus;
		this.empsRemark = empsRemark;
		this.deptId = deptId;
		this.roleId = roleId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmpsName() {
		return this.empsName;
	}

	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}

	public String getEmpsId() {
		return this.empsId;
	}

	public void setEmpsId(String empsId) {
		this.empsId = empsId;
	}

	public Long getEmpsStatus() {
		return this.empsStatus;
	}

	public void setEmpsStatus(Long empsStatus) {
		this.empsStatus = empsStatus;
	}

	public String getEmpsRemark() {
		return this.empsRemark;
	}

	public void setEmpsRemark(String empsRemark) {
		this.empsRemark = empsRemark;
	}

	public Integer getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getEmpsPwd() {
		return empsPwd;
	}

	public void setEmpsPwd(String empsPwd) {
		this.empsPwd = empsPwd;
	}

	public Long getEmpsMintues() {
		return empsMintues;
	}

	public void setEmpsMintues(Long empsMintues) {
		this.empsMintues = empsMintues;
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

	public Set getCustomers() {
		return customers;
	}

	public void setCustomers(Set customers) {
		this.customers = customers;
	}

	public String getEmpsPhone() {
		return empsPhone;
	}

	public void setEmpsPhone(String empsPhone) {
		this.empsPhone = empsPhone;
	}

	public String getEmpNameCn() {
		return empNameCn;
	}

	public void setEmpNameCn(String empNameCn) {
		this.empNameCn = empNameCn;
	}

	public Long getEmpsIsSSLPop3() {
		return empsIsSSLPop3;
	}

	public void setEmpsIsSSLPop3(Long empsIsSSLPop3) {
		this.empsIsSSLPop3 = empsIsSSLPop3;
	}

	public Long getEmpsIsSSLSmtp() {
		return empsIsSSLSmtp;
	}

	public void setEmpsIsSSLSmtp(Long empsIsSSLSmtp) {
		this.empsIsSSLSmtp = empsIsSSLSmtp;
	}

	public Long getEmpsPop3Port() {
		return empsPop3Port;
	}

	public void setEmpsPop3Port(Long empsPop3Port) {
		this.empsPop3Port = empsPop3Port;
	}

	public Long getEmpsSmtpPort() {
		return empsSmtpPort;
	}

	public void setEmpsSmtpPort(Long empsSmtpPort) {
		this.empsSmtpPort = empsSmtpPort;
	}

	public String getEmpsMobile() {
		return empsMobile;
	}

	public void setEmpsMobile(String empsMobile) {
		this.empsMobile = empsMobile;
	}

	public String getEmpsSign() {
		return empsSign;
	}

	public void setEmpsSign(String empsSign) {
		this.empsSign = empsSign;
	}

	public String getEmpsMailTemplate() {
		return empsMailTemplate;
	}

	public void setEmpsMailTemplate(String empsMailTemplate) {
		this.empsMailTemplate = empsMailTemplate;
	}

	public String getEmpsMailTemplateTransmit() {
		return empsMailTemplateTransmit;
	}

	public void setEmpsMailTemplateTransmit(String empsMailTemplateTransmit) {
		this.empsMailTemplateTransmit = empsMailTemplateTransmit;
	}

	public String getEmpsMailTemplateReply() {
		return empsMailTemplateReply;
	}

	public void setEmpsMailTemplateReply(String empsMailTemplateReply) {
		this.empsMailTemplateReply = empsMailTemplateReply;
	}

	public Integer getEtId() {
		return etId;
	}

	public void setEtId(Integer etId) {
		this.etId = etId;
	}

	public Integer getShenFlag() {
		return shenFlag;
	}

	public void setShenFlag(Integer shenFlag) {
		this.shenFlag = shenFlag;
	}

	public String getEmpRight() {
		return empRight;
	}

	public void setEmpRight(String empRight) {
		this.empRight = empRight;
	}

	public String getPasswordApproval() {
		return passwordApproval;
	}

	public void setPasswordApproval(String passwordApproval) {
		this.passwordApproval = passwordApproval;
	}

}