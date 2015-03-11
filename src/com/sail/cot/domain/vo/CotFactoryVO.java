package com.sail.cot.domain.vo;


/**
 * CotContract entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFactoryVO implements java.io.Serializable {

	// Fields

	private Integer id;
	private String contactEmail;
	private String contactPerson;
	private String factoryName;
	private String type;
	private Integer factoryId;

	// Constructors

	/** default constructor */
	public CotFactoryVO() {
	}

	/** full constructor */
	public CotFactoryVO(String contactEmail ,String contactPerson,String type,
			String factoryName,Integer factoryId) {
		this.contactEmail =contactEmail;
		this.contactPerson = contactPerson;
		this.factoryName =factoryName;
		this.type =type;
		this.factoryId=factoryId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}
    

}