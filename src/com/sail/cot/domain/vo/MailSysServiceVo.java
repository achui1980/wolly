/**
 * 
 */
package com.sail.cot.domain.vo;

import java.sql.Timestamp;
import java.util.Date;


/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description: 邮件后台服务列表对象</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 6, 2010 4:03:54 PM </p>
 * <p>Class Name: MailSysServiceVo.java </p>
 * @author achui
 *
 */
public class MailSysServiceVo {	
	private String jobname;//任务名称
	//private String status;//任务状态，启动或停止或暂停
	private Timestamp fireTime;//触发时间
	private Timestamp previousFireTime;//上次执行时间
	private Timestamp nextFireTime;//下次执行时间
	private int state;//执行状态
	private Integer empId;
	private String mail;//接收邮箱地址
	private String mailAccount;//邮箱账号
	private String msg;//异常信息
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMailAccount() {
		return mailAccount;
	}
	public void setMailAccount(String mailAccount) {
		this.mailAccount = mailAccount;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public Timestamp getFireTime() {
		return fireTime;
	}
	public void setFireTime(Timestamp fireTime) {
		this.fireTime = fireTime;
	}
	public Timestamp getPreviousFireTime() {
		return previousFireTime;
	}
	public void setPreviousFireTime(Timestamp previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public Timestamp getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Timestamp nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("jobName:").append(this.jobname).append("     ");
		sb.append("fireTime:").append(this.fireTime).append("     ");
		sb.append("previousFireTime:").append(this.previousFireTime).append("     ");
		sb.append("nextFireTime:").append(this.nextFireTime).append("     ");
		sb.append("state:").append(this.state).append("     ");
		
		return sb.toString();
	}
}
