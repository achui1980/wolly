package com.sail.cot.domain.vo;

import java.util.Date;


public class CotCustLogVO {
	
	private Integer id;
	private Date logDate; //日期
	private String logContent;//沟通内容
	private String logAdvise;//主管批注
	private String remark;//备注
	private Integer logCheck;//是否批注
	private String customerShortName;//客户简称
	private String empsName;//员工姓名
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	public String getLogAdvise() {
		return logAdvise;
	}
	public void setLogAdvise(String logAdvise) {
		this.logAdvise = logAdvise;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getLogCheck() {
		return logCheck;
	}
	public void setLogCheck(Integer logCheck) {
		this.logCheck = logCheck;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public String getEmpsName() {
		return empsName;
	}
	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}
	
	
}
