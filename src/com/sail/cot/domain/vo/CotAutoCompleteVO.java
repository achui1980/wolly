package com.sail.cot.domain.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CotAutoCompleteVO implements Serializable{
	private String name;
	public CotAutoCompleteVO(){};
	public CotAutoCompleteVO(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
