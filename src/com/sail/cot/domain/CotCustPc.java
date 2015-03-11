package com.sail.cot.domain;

/**
 * CotCustMb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustPc implements java.io.Serializable {

	// Fields

	private Integer id;
	private String filePath;//项目底下的artWorkPdf/client/
	private byte[] phone;
	private Integer categoryId;
	private String tempLate;
	private String pcRemark;
	private Integer custId;
	private String category;//材料名称,不存储入数据库

	/** default constructor */
	public CotCustPc() {
	}
	
	public CotCustPc(CotCustPc obj,String typeName) {
		this.id=obj.getId();
		this.categoryId=obj.getCategoryId();
		this.tempLate=obj.getTempLate();
		this.pcRemark=obj.getPcRemark();
		this.custId=obj.getCustId();
		this.category=typeName;
		this.filePath=obj.getFilePath();
	}


	// Property accessors

	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}


	public String getPcRemark() {
		return pcRemark;
	}


	public void setPcRemark(String pcRemark) {
		this.pcRemark = pcRemark;
	}


	public Integer getCustId() {
		return custId;
	}


	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getTempLate() {
		return tempLate;
	}

	public void setTempLate(String tempLate) {
		this.tempLate = tempLate;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte[] getPhone() {
		return phone;
	}

	public void setPhone(byte[] phone) {
		this.phone = phone;
	}


}