package com.sail.cot.domain;

/**
 * CotTrailCar entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotTrailCar implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String nameEn;
	private String addr;
	private String addrEn;
	private String contactPerson;
	private String contactPhone;
	private String trailFax;
	private String op;

	// Constructors

	/** default constructor */
	public CotTrailCar() {
	}

	/** full constructor */
	public CotTrailCar(String name, String nameEn, String addr, String addrEn,
			String contactPerson, String contactPhone) {
		this.name = name;
		this.nameEn = nameEn;
		this.addr = addr;
		this.addrEn = addrEn;
		this.contactPerson = contactPerson;
		this.contactPhone = contactPhone;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddrEn() {
		return this.addrEn;
	}

	public void setAddrEn(String addrEn) {
		this.addrEn = addrEn;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getTrailFax() {
		return trailFax;
	}

	public void setTrailFax(String trailFax) {
		this.trailFax = trailFax;
	}

}