package com.sail.cot.domain;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;

/**
 * CotCity entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotArtWork implements java.io.Serializable {

	// Fields

	private Integer id;
	private String artWork;
	private String artText;
	private String remark;
	private Integer eleId;
	private Integer orderId;
	
	private String awStr;//art work
	private String sizeInch;//size
	private String comment;
	
	private Integer piDetailId; //pi明细的id
	
	private String eleNameEn;//临时货号
	private String barcode;//临时货号
	private String custNo;//临时货号

	// Constructors

	/** default constructor */
	public CotArtWork() {
	}
	
	public CotArtWork(CotArtWork obj,String eleNameEn,String barcode,String custNo) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		try {
			BeanUtils.copyProperties(this, obj);
			BeanUtils.copyProperty(this, "eleNameEn", eleNameEn);
			BeanUtils.copyProperty(this, "barcode", barcode);
			BeanUtils.copyProperty(this, "custNo", custNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getPiDetailId() {
		return piDetailId;
	}

	public void setPiDetailId(Integer piDetailId) {
		this.piDetailId = piDetailId;
	}

	public Integer getId() {
		return id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEleNameEn() {
		return eleNameEn;
	}

	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}

	public String getAwStr() {
		return awStr;
	}

	public void setAwStr(String awStr) {
		this.awStr = awStr;
	}

	public String getSizeInch() {
		return sizeInch;
	}

	public void setSizeInch(String sizeInch) {
		this.sizeInch = sizeInch;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArtWork() {
		return artWork;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public void setArtWork(String artWork) {
		this.artWork = artWork;
	}

	public Integer getEleId() {
		return eleId;
	}

	public void setEleId(Integer eleId) {
		this.eleId = eleId;
	}

	public String getArtText() {
		return artText;
	}

	public void setArtText(String artText) {
		this.artText = artText;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}