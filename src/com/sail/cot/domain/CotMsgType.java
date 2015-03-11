package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotMsgType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotMsgType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String msgTypeName;
	private String msgTypeRemark;
	private String op;

	// Constructors

	/** default constructor */
	public CotMsgType() {
	}

	/** full constructor */
	public CotMsgType(String msgTypeName, String msgTypeRemark, Set cotMessages) {
		this.msgTypeName = msgTypeName;
		this.msgTypeRemark = msgTypeRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsgTypeName() {
		return this.msgTypeName;
	}

	public void setMsgTypeName(String msgTypeName) {
		this.msgTypeName = msgTypeName;
	}

	public String getMsgTypeRemark() {
		return this.msgTypeRemark;
	}

	public void setMsgTypeRemark(String msgTypeRemark) {
		this.msgTypeRemark = msgTypeRemark;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
}