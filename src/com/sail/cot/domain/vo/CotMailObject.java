/**
 * 
 */
package com.sail.cot.domain.vo;

import com.sail.cot.domain.CotMail;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 5, 2008 11:54:05 AM </p>
 * <p>Class Name: CotMailObject.java </p>
 * @author achui
 *
 */
public class CotMailObject extends CotMail{
	
	private String custName;
	private String empName;
	private String empEmail;
	private String custEmail;
	
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpEmail() {
		return empEmail;
	}
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

}
