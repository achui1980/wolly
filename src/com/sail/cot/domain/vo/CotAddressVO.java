package com.sail.cot.domain.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CotAddressVO implements Serializable{
	private Integer id;	 
	private String name; // 厂家或客户名
	private String addressName; // 联系人名
	private String email; // 联系人
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
