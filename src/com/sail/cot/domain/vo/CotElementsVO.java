package com.sail.cot.domain.vo;



/**
 * CotElements entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotElementsVO implements java.io.Serializable {
	
	private Integer id;
	private String picPath;
	private String eleId;
	private String eleName;
	private String eleNameEn;
	private String eleSizeDesc;
	private Integer factoryId;
	private String facShortName;
	private Float boxCbm;
	private Long boxIbCount;
	private Long boxMbCount;
	private Long boxObCount;
	private Integer eleParentid;
	private String startTime;
	private String endTime;
	private String eleCol;
	private String eleTypeidLv1;
	private String eleGrade;
	private String eleDesc;
	private String price;
	private String currenty;
	private String child;
	private String op;
	private String chk;
	
	private String PIC_PATH;
	private String ELE_ID;
	private String ELE_NAME;
	private String ELE_NAME_EN;
	private String ELE_SIZE_DESC;
	private Integer FACTORY_ID;
	private Float BOX_CBM;
	private Long BOX_IB_COUNT;
	private Long BOX_MB_COUNT;
	private Long BOX_OB_COUNT;
	
	private String eleForPersonFind;
	private String boxLS;
	private String boxLE;
	private String boxWS;
	private String boxWE;
	private String boxHS;
	private String boxHE;
	
	private String priceFac;
	private String priceOut;
	private String priceFacUint;
	private String priceFacUintName;
	private String priceOutUint;
	private String priceOutUintName;
	
	private String eleUnit;
	private Float boxObL;
	private Float boxObW;
	private Float boxObH;
	private Integer eleMod;
	private String eleRemark;
	private String eleTypenameLv2;

	public String getPicPath() {
		return picPath;
	}

	public String getEleTypenameLv2() {
		return eleTypenameLv2;
	}

	public void setEleTypenameLv2(String eleTypenameLv2) {
		this.eleTypenameLv2 = eleTypenameLv2;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getEleName() {
		return eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public String getEleNameEn() {
		return eleNameEn;
	}

	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}

	public String getEleSizeDesc() {
		return eleSizeDesc;
	}

	public Float getBoxCbm() {
		return boxCbm;
	}

	public void setBoxCbm(Float boxCbm) {
		this.boxCbm = boxCbm;
	}

	public Long getBoxIbCount() {
		return boxIbCount;
	}

	public void setBoxIbCount(Long boxIbCount) {
		this.boxIbCount = boxIbCount;
	}

	public Long getBoxMbCount() {
		return boxMbCount;
	}

	public void setBoxMbCount(Long boxMbCount) {
		this.boxMbCount = boxMbCount;
	}

	public Long getBoxObCount() {
		return boxObCount;
	}

	public void setBoxObCount(Long boxObCount) {
		this.boxObCount = boxObCount;
	}

	public void setEleSizeDesc(String eleSizeDesc) {
		this.eleSizeDesc = eleSizeDesc;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getEleParentid() {
		return eleParentid;
	}

	public void setEleParentid(Integer eleParentid) {
		this.eleParentid = eleParentid;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEleCol() {
		return eleCol;
	}

	public void setEleCol(String eleCol) {
		this.eleCol = eleCol;
	}

	public String getEleTypeidLv1() {
		return eleTypeidLv1;
	}

	public void setEleTypeidLv1(String eleTypeidLv1) {
		this.eleTypeidLv1 = eleTypeidLv1;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCurrenty() {
		return currenty;
	}

	public void setCurrenty(String currenty) {
		this.currenty = currenty;
	}

	public String getEleGrade() {
		return eleGrade;
	}

	public void setEleGrade(String eleGrade) {
		this.eleGrade = eleGrade;
	}

	public String getPIC_PATH() {
		return PIC_PATH;
	}

	public void setPIC_PATH(String pic_path) {
		PIC_PATH = pic_path;
	}

	public String getELE_ID() {
		return ELE_ID;
	}

	public void setELE_ID(String ele_id) {
		ELE_ID = ele_id;
	}

	public String getELE_NAME() {
		return ELE_NAME;
	}

	public void setELE_NAME(String ele_name) {
		ELE_NAME = ele_name;
	}

	public String getELE_NAME_EN() {
		return ELE_NAME_EN;
	}

	public void setELE_NAME_EN(String ele_name_en) {
		ELE_NAME_EN = ele_name_en;
	}

	public String getELE_SIZE_DESC() {
		return ELE_SIZE_DESC;
	}

	public void setELE_SIZE_DESC(String ele_size_desc) {
		ELE_SIZE_DESC = ele_size_desc;
	}

	public Integer getFACTORY_ID() {
		return FACTORY_ID;
	}

	public void setFACTORY_ID(Integer factory_id) {
		FACTORY_ID = factory_id;
	}

	public Float getBOX_CBM() {
		return BOX_CBM;
	}

	public void setBOX_CBM(Float box_cbm) {
		BOX_CBM = box_cbm;
	}

	public Long getBOX_IB_COUNT() {
		return BOX_IB_COUNT;
	}

	public void setBOX_IB_COUNT(Long box_ib_count) {
		BOX_IB_COUNT = box_ib_count;
	}

	public Long getBOX_MB_COUNT() {
		return BOX_MB_COUNT;
	}

	public void setBOX_MB_COUNT(Long box_mb_count) {
		BOX_MB_COUNT = box_mb_count;
	}

	public Long getBOX_OB_COUNT() {
		return BOX_OB_COUNT;
	}

	public void setBOX_OB_COUNT(Long box_ob_count) {
		BOX_OB_COUNT = box_ob_count;
	}

	public String getEleForPersonFind() {
		return eleForPersonFind;
	}

	public void setEleForPersonFind(String eleForPersonFind) {
		this.eleForPersonFind = eleForPersonFind;
	}

	public String getBoxLS() {
		return boxLS;
	}

	public void setBoxLS(String boxLS) {
		this.boxLS = boxLS;
	}

	public String getBoxLE() {
		return boxLE;
	}

	public void setBoxLE(String boxLE) {
		this.boxLE = boxLE;
	}

	public String getBoxWS() {
		return boxWS;
	}

	public void setBoxWS(String boxWS) {
		this.boxWS = boxWS;
	}

	public String getBoxWE() {
		return boxWE;
	}

	public void setBoxWE(String boxWE) {
		this.boxWE = boxWE;
	}

	public String getBoxHS() {
		return boxHS;
	}

	public void setBoxHS(String boxHS) {
		this.boxHS = boxHS;
	}

	public String getBoxHE() {
		return boxHE;
	}

	public void setBoxHE(String boxHE) {
		this.boxHE = boxHE;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public String getEleDesc() {
		return eleDesc;
	}

	public void setEleDesc(String eleDesc) {
		this.eleDesc = eleDesc;
	}

	public String getPriceFac() {
		return priceFac;
	}

	public void setPriceFac(String priceFac) {
		this.priceFac = priceFac;
	}

	public String getPriceOut() {
		return priceOut;
	}

	public void setPriceOut(String priceOut) {
		this.priceOut = priceOut;
	}

	public String getPriceFacUint() {
		return priceFacUint;
	}

	public void setPriceFacUint(String priceFacUint) {
		this.priceFacUint = priceFacUint;
	}

	public String getPriceOutUint() {
		return priceOutUint;
	}

	public void setPriceOutUint(String priceOutUint) {
		this.priceOutUint = priceOutUint;
	}

	public String getEleUnit() {
		return eleUnit;
	}

	public void setEleUnit(String eleUnit) {
		this.eleUnit = eleUnit;
	}

	public Float getBoxObL() {
		return boxObL;
	}

	public void setBoxObL(Float boxObL) {
		this.boxObL = boxObL;
	}

	public Float getBoxObW() {
		return boxObW;
	}

	public void setBoxObW(Float boxObW) {
		this.boxObW = boxObW;
	}

	public Float getBoxObH() {
		return boxObH;
	}

	public void setBoxObH(Float boxObH) {
		this.boxObH = boxObH;
	}

	public Integer getEleMod() {
		return eleMod;
	}

	public void setEleMod(Integer eleMod) {
		this.eleMod = eleMod;
	}

	public String getEleRemark() {
		return eleRemark;
	}

	public void setEleRemark(String eleRemark) {
		this.eleRemark = eleRemark;
	}

	public String getFacShortName() {
		return facShortName;
	}

	public void setFacShortName(String facShortName) {
		this.facShortName = facShortName;
	}

	public String getPriceFacUintName() {
		return priceFacUintName;
	}

	public void setPriceFacUintName(String priceFacUintName) {
		this.priceFacUintName = priceFacUintName;
	}

	public String getPriceOutUintName() {
		return priceOutUintName;
	}

	public void setPriceOutUintName(String priceOutUintName) {
		this.priceOutUintName = priceOutUintName;
	}

	
	
	
}