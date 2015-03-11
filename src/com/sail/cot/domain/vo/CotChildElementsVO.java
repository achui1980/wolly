package com.sail.cot.domain.vo;


/**
 * CotElements entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotChildElementsVO implements java.io.Serializable{
	
	private Integer parentId;//父类编号
	private Integer childElementId;//子类编号
	private String childEleId;//子货号
	private String childEleName;//中文品名
	private String childEleNameEn;//英文品名
	private Integer childFactoryId;//厂家
	private Integer childEleHsid;//海关编码
	private Integer childPriceOutId;//对外报价编号
	private Float childPriceOut;//对外报价
	private Integer childPriceFacId;//厂家报价编号
	private Float childPriceFac;//厂家报价
	private String childEleSizeDesc;//尺寸描述
	private String childEleInchDesc;//英寸描述
	private String picName;//图片名称
	private String picPath;//图片路径
	private Integer childBoxId;//包装信息编号
	private Float childBoxObL;//外箱长度
	private Float childBoxObW;//外箱宽度
	private Float childBoxObH;//外箱高度
	private String childBoxRemark;//包装备注
	
	public String getChildEleNameEn() {
		return childEleNameEn;
	}
	public void setChildEleNameEn(String childEleNameEn) {
		this.childEleNameEn = childEleNameEn;
	}
	public Integer getChildFactoryId() {
		return childFactoryId;
	}
	public void setChildFactoryId(Integer childFactoryId) {
		this.childFactoryId = childFactoryId;
	}
	
	public Float getChildPriceOut() {
		return childPriceOut;
	}
	public void setChildPriceOut(Float childPriceOut) {
		this.childPriceOut = childPriceOut;
	}
	public Float getChildPriceFac() {
		return childPriceFac;
	}
	public void setChildPriceFac(Float childPriceFac) {
		this.childPriceFac = childPriceFac;
	}
	public Float getChildBoxObL() {
		return childBoxObL;
	}
	public void setChildBoxObL(Float childBoxObL) {
		this.childBoxObL = childBoxObL;
	}
	public Float getChildBoxObW() {
		return childBoxObW;
	}
	public void setChildBoxObW(Float childBoxObW) {
		this.childBoxObW = childBoxObW;
	}
	public Float getChildBoxObH() {
		return childBoxObH;
	}
	public void setChildBoxObH(Float childBoxObH) {
		this.childBoxObH = childBoxObH;
	}
	public String getChildBoxRemark() {
		return childBoxRemark;
	}
	public void setChildBoxRemark(String childBoxRemark) {
		this.childBoxRemark = childBoxRemark;
	}
	public Integer getChildBoxId() {
		return childBoxId;
	}
	public void setChildBoxId(Integer childBoxId) {
		this.childBoxId = childBoxId;
	}
	public String getChildEleId() {
		return childEleId;
	}
	public void setChildEleId(String childEleId) {
		this.childEleId = childEleId;
	}
	public String getChildEleName() {
		return childEleName;
	}
	public void setChildEleName(String childEleName) {
		this.childEleName = childEleName;
	}
	public String getChildEleSizeDesc() {
		return childEleSizeDesc;
	}
	public void setChildEleSizeDesc(String childEleSizeDesc) {
		this.childEleSizeDesc = childEleSizeDesc;
	}
	
	public String getChildEleInchDesc() {
		return childEleInchDesc;
	}
	public void setChildEleInchDesc(String childEleInchDesc) {
		this.childEleInchDesc = childEleInchDesc;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Integer getChildElementId() {
		return childElementId;
	}
	public void setChildElementId(Integer childElementId) {
		this.childElementId = childElementId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getChildPriceOutId() {
		return childPriceOutId;
	}
	public void setChildPriceOutId(Integer childPriceOutId) {
		this.childPriceOutId = childPriceOutId;
	}
	public Integer getChildPriceFacId() {
		return childPriceFacId;
	}
	public void setChildPriceFacId(Integer childPriceFacId) {
		this.childPriceFacId = childPriceFacId;
	}
	public Integer getChildEleHsid() {
		return childEleHsid;
	}
	public void setChildEleHsid(Integer childEleHsid) {
		this.childEleHsid = childEleHsid;
	}
}