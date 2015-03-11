package com.sail.cot.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sail.cot.email.util.MailEntityConverUtil;
import com.zhao.mail.entity.MailObject;
import com.zhao.mail.entity.MailPerson;

@SuppressWarnings("serial")
public class CotMail extends MailObject<CotMailAttach> implements
		java.io.Serializable {

	// Fields

	private String id;
	private Integer custId;
	private Integer checkEmpId;
	private Integer facId;
	private Integer empId;
	private Integer nodeId;
	private String sendName;
	private String sendUrl;
	private String toName;
	private String toUrl;
	private String ccName;
	private String ccUrl;
	private String bccName;
	private String bccUrl;
	private Date addTime;
	private Integer mailStatus;
	private Integer mailType;
	private String mailTag;
	private String errMessage;
	private Integer sendPriv;
	private Integer sendCount;
	private Integer mailCount;
	private Byte mailDebug;
	
	private List<CotMailAttach> attachs = new ArrayList<CotMailAttach>();  // 附件
	// Constructors

	/** default constructor */
	public CotMail() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public String getSendName() {
		return this.sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendUrl() {
		return this.sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public String getToName() {
		return this.toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToUrl() {
		return this.toUrl;
	}

	public void setToUrl(String toUrl) {
		this.toUrl = toUrl;
	}

	public String getCcName() {
		return this.ccName;
	}

	public void setCcName(String ccName) {
		this.ccName = ccName;
	}

	public String getCcUrl() {
		return this.ccUrl;
	}

	public void setCcUrl(String ccUrl) {
		this.ccUrl = ccUrl;
	}

	public String getBccName() {
		return this.bccName;
	}

	public void setBccName(String bccName) {
		this.bccName = bccName;
	}

	public String getBccUrl() {
		return this.bccUrl;
	}

	public void setBccUrl(String bccUrl) {
		this.bccUrl = bccUrl;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getMailStatus() {
		return this.mailStatus;
	}

	public void setMailStatus(Integer mailStatus) {
		this.mailStatus = mailStatus;
	}

	public Integer getMailType() {
		return this.mailType;
	}

	public void setMailType(Integer mailType) {
		this.mailType = mailType;
	}

	public String getErrMessage() {
		return this.errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public Integer getSendPriv() {
		return this.sendPriv;
	}

	public void setSendPriv(Integer sendPriv) {
		this.sendPriv = sendPriv;
	}

	public Integer getSendCount() {
		return this.sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public Integer getMailCount() {
		return this.mailCount;
	}

	public void setMailCount(Integer mailCount) {
		this.mailCount = mailCount;
	}

	public Byte getMailDebug() {
		return this.mailDebug;
	}

	public void setMailDebug(Byte mailDebug) {
		this.mailDebug = mailDebug;
	}

	public List<CotMailAttach> getAttachs() {
		return this.attachs;
	}

	public List<MailPerson> getBcc() {
		return MailEntityConverUtil.toPersons(this.bccName, this.bccUrl);
	}

	public List<MailPerson> getCc() {
		return MailEntityConverUtil.toPersons(this.ccName, this.ccUrl);
	}

	public MailPerson getSender() {
		if(this.sendName==null&&this.sendUrl==null)
			return new MailPerson("","");
		return new MailPerson(this.sendName,this.sendUrl);
	}

	public List<MailPerson> getTo() {
		return MailEntityConverUtil.toPersons(this.toName, this.toUrl);
	}

	public void setAttachs(List<CotMailAttach> attachs) {
		this.attachs = attachs;
	}

	public void setBcc(List<MailPerson> bcc) {
		Map<String, String> map = MailEntityConverUtil.toNameUrlMap(bcc);
		this.bccName = map.get("name");
		this.bccUrl = map.get("url");
	}

	public void setCc(List<MailPerson> cc) {
		Map<String, String> map = MailEntityConverUtil.toNameUrlMap(cc);
		this.ccName = map.get("name");
		this.ccUrl = map.get("url");
	}

	public void setSender(MailPerson sender) {
		if(sender!=null){
			this.sendName = sender.getName();
			this.sendUrl = sender.getEmailUrl();
		}else {
			this.sendName = "";
			this.sendUrl = "";
		}
	}

	public void setTo(List<MailPerson> to) {
		Map<String, String> map = MailEntityConverUtil.toNameUrlMap(to);
		this.toName = map.get("name");
		this.toUrl = map.get("url");
	}

	public Integer getFacId() {
		return facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}
	
	public String getSendUrlLowerCase(){
		if(this.sendUrl == null)
			return null;
		else 
			return this.sendUrl.toLowerCase();
	}
	public String getCcUrlLowerCase(){
		if(this.ccUrl == null)
			return null;
		else
			return this.ccUrl.toLowerCase();
	}
	public String getToUrlLowerCase(){
		if(this.toUrl == null)
			return null;
		else
			return this.toUrl.toLowerCase();
	}

	public Integer getCheckEmpId() {
		return checkEmpId;
	}

	public void setCheckEmpId(Integer checkEmpId) {
		this.checkEmpId = checkEmpId;
	}

	public String getMailTag() {
		return mailTag;
	}

	public void setMailTag(String mailTag) {
		this.mailTag = mailTag;
	}
}