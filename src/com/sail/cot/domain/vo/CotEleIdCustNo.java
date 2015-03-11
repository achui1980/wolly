/**
 * 
 */
package com.sail.cot.domain.vo;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Dec 24, 2008 4:30:30 PM </p>
 * <p>Class Name: CotEleIdCustNo.java </p>
 * @author achui
 *
 */
public class CotEleIdCustNo {
	private String eleId;
	private String custNo;
	private String type;
	private String typePrimId;
	private String tyepDetailId;
	private String custId;
	private String eleNameEn;
	private String custName;
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getEleNameEn() {
		return eleNameEn;
	}
	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}
	public String getEleId() {
		return eleId;
	}
	public void setEleId(String eleId) {
		this.eleId = eleId;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypePrimId() {
		return typePrimId;
	}
	public void setTypePrimId(String typePrimId) {
		this.typePrimId = typePrimId;
	}
	public String getTyepDetailId() {
		return tyepDetailId;
	}
	public void setTyepDetailId(String tyepDetailId) {
		this.tyepDetailId = tyepDetailId;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}

}
