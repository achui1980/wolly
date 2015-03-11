/**
 * 
 */
package com.sail.cot.mail;

import java.util.Set;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 31, 2010 2:56:04 PM </p>
 * <p>Class Name: MailCheckExecuteAction.java </p>
 * @author achui
 *
 */
public class MailCheckExecuteAction {
	private Integer empId; //审核人
	private boolean checked = false; //是否需要审核
	
	public void setMailCheck(Integer empId,boolean checked){
		this.empId = empId;
		this.checked = checked;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
