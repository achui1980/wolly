package com.sail.cot.domain;

import java.util.Date;
import java.util.Set;

/**
 * CotFittings entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFittings implements java.io.Serializable {

	// Fields

	private Integer id;
	private String fitNo;//材料编号
	private String fitName;//配件名称
	private String buyUnit;//采购单位
	private String useUnit;//领用单位
	private Double fitTrans;//换算率
	private Double fitPrice;//采购价格
	private Long fitMinCount;//最小采购量
	private String fitQualityStander;//质量标准
	private String fitDesc;//规格描述
	private String fitRemark;//备注
	private Integer facId;//供应商(厂家)
	private Integer typeLv3Id;//配件类别
	private Date addTime;//添加时间
	private String op;
	private String picPath;
	
	private String facShortName;//临时字段,厂家简称

	// Constructors

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	/** default constructor */
	public CotFittings() {
	}

	/** full constructor */
	public CotFittings(String fitNo, String fitName, String buyUnit,
			String useUnit, Double fitTrans, Double fitPrice, Long fitMinCount,
			String fitQualityStander, String fitDesc, String fitRemark,
			Integer facId, Integer typeLv3Id, Date addTime, Set cotFittingsPics) {
		this.fitNo = fitNo;
		this.fitName = fitName;
		this.buyUnit = buyUnit;
		this.useUnit = useUnit;
		this.fitTrans = fitTrans;
		this.fitPrice = fitPrice;
		this.fitMinCount = fitMinCount;
		this.fitQualityStander = fitQualityStander;
		this.fitDesc = fitDesc;
		this.fitRemark = fitRemark;
		this.facId = facId;
		this.typeLv3Id = typeLv3Id;
		this.addTime = addTime;

	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFitNo() {
		return this.fitNo;
	}

	public void setFitNo(String fitNo) {
		this.fitNo = fitNo;
	}

	public String getFitName() {
		return this.fitName;
	}

	public void setFitName(String fitName) {
		this.fitName = fitName;
	}

	public String getBuyUnit() {
		return this.buyUnit;
	}

	public void setBuyUnit(String buyUnit) {
		this.buyUnit = buyUnit;
	}

	public String getUseUnit() {
		return this.useUnit;
	}

	public void setUseUnit(String useUnit) {
		this.useUnit = useUnit;
	}

	public Double getFitTrans() {
		return this.fitTrans;
	}

	public void setFitTrans(Double fitTrans) {
		this.fitTrans = fitTrans;
	}

	public Double getFitPrice() {
		return this.fitPrice;
	}

	public void setFitPrice(Double fitPrice) {
		this.fitPrice = fitPrice;
	}

	public Long getFitMinCount() {
		return this.fitMinCount;
	}

	public void setFitMinCount(Long fitMinCount) {
		this.fitMinCount = fitMinCount;
	}

	public String getFitQualityStander() {
		return this.fitQualityStander;
	}

	public void setFitQualityStander(String fitQualityStander) {
		this.fitQualityStander = fitQualityStander;
	}

	public String getFitDesc() {
		return this.fitDesc;
	}

	public void setFitDesc(String fitDesc) {
		this.fitDesc = fitDesc;
	}

	public String getFitRemark() {
		return this.fitRemark;
	}

	public void setFitRemark(String fitRemark) {
		this.fitRemark = fitRemark;
	}

	public Integer getFacId() {
		return this.facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public Integer getTypeLv3Id() {
		return this.typeLv3Id;
	}

	public void setTypeLv3Id(Integer typeLv3Id) {
		this.typeLv3Id = typeLv3Id;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getFacShortName() {
		return facShortName;
	}

	public void setFacShortName(String facShortName) {
		this.facShortName = facShortName;
	}
}