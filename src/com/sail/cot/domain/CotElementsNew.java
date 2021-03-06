package com.sail.cot.domain;
 
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.sail.cot.util.ContextUtil;

/**
 * CotElementsNew entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotElementsNew implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer factoryId;
	private Integer eleParentId;
	private String eleParent;
	private Integer priceFacUint;
	private Integer eleHsid;
	private Integer eleTypeidLv1;
	private Integer eleTypeidLv2;
	private Integer eleTypeidLv3;
	private Integer priceOutUint;
	private String eleId;
	private String eleName;
	private String eleNameEn;
	private Long eleFlag;
	private String eleDesc;
	private String eleFrom;
	private String eleComposeType;
	private String eleGrade;
	private String eleUnit;
	private String eleFactory;
	private String eleCol;
	private String eleMeterial;
	private String eleSizeDesc;
	private String eleInchDesc;
	private String eleRemark;
	private String eleTypenameLv1;
	private String eleTypenameLv2;
	private String eleTypenameLv3;
	private Date eleAddTime;
	private Date eleProTime;
	private String eleForPerson;
	private Integer eleMod;
	private Integer eleUnitNum;
	private String boxUint;
	private Float boxCbm;
	private Float boxTL;
	private Float boxTLInch;
	private Float boxTW;
	private Float boxTWInch;
	private Float boxTD;
	private Float boxTDInch;
	private Float boxBL;
	private Float boxBLInch;
	private Float boxBW;
	private Float boxBWInch;
	private Float boxBD;
	private Float boxBDInch;
	private Float boxL;
	private Float boxW;
	private Float boxH;
	private Float boxLInch;
	private Float boxWInch;
	private Float boxHInch;
	private Float boxIbL;
	private Float boxIbLInch;
	private Float boxIbW;
	private Float boxIbWInch;
	private Float boxIbH;
	private Float boxIbHInch;
	private Float boxMbL;
	private Float boxMbLInch;
	private Float boxMbW;
	private Float boxMbWInch;
	private Float boxMbH;
	private Float boxMbHInch;
	private Float boxObL;
	private Float boxObLInch;
	private Float boxObW;
	private Float boxObWInch;
	private Float boxObH;
	private Float boxObHInch;
	private Float boxWeigth;
	private Float boxGrossWeigth;
	private Float boxNetWeigth;
	private Long boxIbCount;
	private String boxIbType;
	private Long boxMbCount;
	private String boxMbType;
	private Long boxObCount;
	private String boxObType;
	private Float boxCuft;
	private String boxRemark;
	private String boxRemarkCn;
	private Float box20Count;
	private Float box40Count;
	private Float box40hqCount;
	private Float box45Count;
	private String boxBDesc;
	private String boxTDesc;
	private Float boxHandleH;
	private String picName;
	private String picPath;
	private byte[]   picImg;
	private Float priceFac;
	private Float priceOut;
	private Set cotPictures = null;
	private Set childs = null;
	private Set cotFile = null;
	private String op;
	private Integer boxTypeId;
	private String factoryNo;
	private Float cube;
	private String taoUnit;
	private Float tuiLv;
	private Float liRun;
	private String barcode;
	private Float boxPbL;
	private Float boxPbLInch;
	private Float boxPbW;
	private Float boxPbWInch;
	private Float boxPbH;
	private Float boxPbHInch;
	private Long boxPbCount;
	private String boxPbType;
	private Float boxPbPrice;
	private Float boxIbPrice;
	private Float boxMbPrice;
	private Float boxObPrice;
	private Float packingPrice;
	
	private Integer boxPbTypeId;
	private Integer boxIbTypeId;
	private Integer boxMbTypeId;
	private Integer boxObTypeId;
	
	
	private Set cotPriceFacs = null;
	private Set cotEleFittings = null;
	private Set cotElePrice = null;
	private Set cotElePacking = null;
	private Float elePrice;
	private Float eleFittingPrice;
	
	private String priceFacUintName;
	private String priceOutUintName;
	private String facShortName;
	
	private Float inputGridPrice;
	private Integer inputGridTypeId;
	private Float putL;
	private Float putW;
	private Float putH;
	
	private String importTax;
	
	private Timestamp updateTime;
	
	private Integer lockBar;//是否锁定barcode,pi;1为锁定可以导入,0和null是不锁定不能导入


	// Constructors


	public Integer getLockBar() {
		return lockBar;
	}

	public void setLockBar(Integer lockBar) {
		this.lockBar = lockBar;
	}

	public String getImportTax() {
		return importTax;
	}

	public void setImportTax(String importTax) {
		this.importTax = importTax;
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

	public String getFacShortName() {
		return facShortName;
	}

	public void setFacShortName(String facShortName) {
		this.facShortName = facShortName;
	}

	public Float getElePrice() {
		return elePrice;
	}

	public void setElePrice(Float elePrice) {
		this.elePrice = elePrice;
	}

	public Float getEleFittingPrice() {
		return eleFittingPrice;
	}

	public void setEleFittingPrice(Float eleFittingPrice) {
		this.eleFittingPrice = eleFittingPrice;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Float getCube() {
		return cube;
	}

	public void setCube(Float cube) {
		this.cube = cube;
	}

	public String getFactoryNo() {
		return factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public Integer getBoxTypeId() {
		return boxTypeId;
	}

	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
	}

	/** default constructor */
	public CotElementsNew() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public String getEleParent() {
		return eleParent;
	}

	public void setEleParent(String eleParent) {
		this.eleParent = eleParent;
	}

	public Integer getPriceFacUint() {
		return priceFacUint;
	}

	public void setPriceFacUint(Integer priceFacUint) {
		this.priceFacUint = priceFacUint;
	}

	public Integer getEleHsid() {
		return eleHsid;
	}

	public void setEleHsid(Integer eleHsid) {
		this.eleHsid = eleHsid;
	}

	public Integer getEleTypeidLv1() {
		return eleTypeidLv1;
	}

	public void setEleTypeidLv1(Integer eleTypeidLv1) {
		this.eleTypeidLv1 = eleTypeidLv1;
	}

	public Integer getPriceOutUint() {
		return priceOutUint;
	}

	public void setPriceOutUint(Integer priceOutUint) {
		this.priceOutUint = priceOutUint;
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

	public Long getEleFlag() {
		return eleFlag;
	}
	

	public void setEleFlag(Long eleFlag) {
		this.eleFlag = eleFlag;
	}

	public String getEleDesc() {
		return eleDesc;
	}

	public void setEleDesc(String eleDesc) {
		this.eleDesc = eleDesc;
	}

	public String getEleFrom() {
		return eleFrom;
	}

	public void setEleFrom(String eleFrom) {
		this.eleFrom = eleFrom;
	}

	public String getEleGrade() {
		return eleGrade;
	}

	public void setEleGrade(String eleGrade) {
		this.eleGrade = eleGrade;
	}

	public String getEleUnit() {
		return eleUnit;
	}

	public void setEleUnit(String eleUnit) {
		this.eleUnit = eleUnit;
	}

	public String getEleFactory() {
		return eleFactory;
	}

	public void setEleFactory(String eleFactory) {
		this.eleFactory = eleFactory;
	}

	public String getEleCol() {
		return eleCol;
	}

	public void setEleCol(String eleCol) {
		this.eleCol = eleCol;
	}

	public String getEleMeterial() {
		return eleMeterial;
	}

	public void setEleMeterial(String eleMeterial) {
		this.eleMeterial = eleMeterial;
	}

	public String getEleSizeDesc() {
		return eleSizeDesc;
	}

	public void setEleSizeDesc(String eleSizeDesc) {
		this.eleSizeDesc = eleSizeDesc;
	}

	public String getEleInchDesc() {
		return eleInchDesc;
	}

	public void setEleInchDesc(String eleInchDesc) {
		this.eleInchDesc = eleInchDesc;
	}

	public String getEleRemark() {
		return eleRemark;
	}

	public void setEleRemark(String eleRemark) {
		this.eleRemark = eleRemark;
	}

	public String getEleTypenameLv1() {
		return eleTypenameLv1;
	}

	public void setEleTypenameLv1(String eleTypenameLv1) {
		this.eleTypenameLv1 = eleTypenameLv1;
	}

	public String getEleTypenameLv2() {
		return eleTypenameLv2;
	}

	public void setEleTypenameLv2(String eleTypenameLv2) {
		this.eleTypenameLv2 = eleTypenameLv2;
	}

	public String getEleTypenameLv3() {
		return eleTypenameLv3;
	}

	public void setEleTypenameLv3(String eleTypenameLv3) {
		this.eleTypenameLv3 = eleTypenameLv3;
	}

	public Date getEleAddTime() {
		return eleAddTime;
	}

	public void setEleAddTime(Date eleAddTime) {
		this.eleAddTime = eleAddTime;
	}
	
	public Date getEleProTime() {
		return eleProTime;
	}

	public void setEleProTime(Date eleProTime) {
		this.eleProTime = eleProTime;
	}

	public String getEleForPerson() {
		return eleForPerson;
	}

	public void setEleForPerson(String eleForPerson) {
		this.eleForPerson = eleForPerson;
	}

	public Integer getEleMod() {
		return eleMod;
	}

	public void setEleMod(Integer eleMod) {
		this.eleMod = eleMod;
	}

	public Integer getEleUnitNum() {
		return eleUnitNum;
	}

	public void setEleUnitNum(Integer eleUnitNum) {
		this.eleUnitNum = eleUnitNum;
	}

	public String getBoxUint() {
		return boxUint;
	}

	public void setBoxUint(String boxUint) {
		this.boxUint = boxUint;
	}

	public Float getBoxCbm() {
		return boxCbm;
	}

	public void setBoxCbm(Float boxCbm) {
		this.boxCbm = boxCbm;
	}

	public Float getBoxTL() {
		return boxTL;
	}

	public void setBoxTL(Float boxTL) {
		this.boxTL = boxTL;
	}

	public Float getBoxTLInch() {
		return boxTLInch;
	}

	public void setBoxTLInch(Float boxTLInch) {
		this.boxTLInch = boxTLInch;
	}

	public Float getBoxTW() {
		return boxTW;
	}

	public void setBoxTW(Float boxTW) {
		this.boxTW = boxTW;
	}

	public Float getBoxTWInch() {
		return boxTWInch;
	}

	public void setBoxTWInch(Float boxTWInch) {
		this.boxTWInch = boxTWInch;
	}

	public Float getBoxTD() {
		return boxTD;
	}

	public void setBoxTD(Float boxTD) {
		this.boxTD = boxTD;
	}

	public Float getBoxTDInch() {
		return boxTDInch;
	}

	public void setBoxTDInch(Float boxTDInch) {
		this.boxTDInch = boxTDInch;
	}

	public Float getBoxBL() {
		return boxBL;
	}

	public void setBoxBL(Float boxBL) {
		this.boxBL = boxBL;
	}

	public Float getBoxBLInch() {
		return boxBLInch;
	}

	public void setBoxBLInch(Float boxBLInch) {
		this.boxBLInch = boxBLInch;
	}

	public Float getBoxBW() {
		return boxBW;
	}

	public void setBoxBW(Float boxBW) {
		this.boxBW = boxBW;
	}

	public Float getBoxBWInch() {
		return boxBWInch;
	}

	public void setBoxBWInch(Float boxBWInch) {
		this.boxBWInch = boxBWInch;
	}

	public Float getBoxBD() {
		return boxBD;
	}

	public void setBoxBD(Float boxBD) {
		this.boxBD = boxBD;
	}

	public Float getBoxBDInch() {
		return boxBDInch;
	}

	public void setBoxBDInch(Float boxBDInch) {
		this.boxBDInch = boxBDInch;
	}

	public Float getBoxL() {
		return boxL;
	}

	public void setBoxL(Float boxL) {
		this.boxL = boxL;
	}

	public Float getBoxW() {
		return boxW;
	}

	public void setBoxW(Float boxW) {
		this.boxW = boxW;
	}

	public Float getBoxH() {
		return boxH;
	}

	public void setBoxH(Float boxH) {
		this.boxH = boxH;
	}

	public Float getBoxLInch() {
		return boxLInch;
	}

	public void setBoxLInch(Float boxLInch) {
		this.boxLInch = boxLInch;
	}

	public Float getBoxWInch() {
		return boxWInch;
	}

	public void setBoxWInch(Float boxWInch) {
		this.boxWInch = boxWInch;
	}

	public Float getBoxHInch() {
		return boxHInch;
	}

	public void setBoxHInch(Float boxHInch) {
		this.boxHInch = boxHInch;
	}

	public Float getBoxIbL() {
		return boxIbL;
	}

	public void setBoxIbL(Float boxIbL) {
		this.boxIbL = boxIbL;
	}

	public Float getBoxIbLInch() {
		return boxIbLInch;
	}

	public void setBoxIbLInch(Float boxIbLInch) {
		this.boxIbLInch = boxIbLInch;
	}

	public Float getBoxIbW() {
		return boxIbW;
	}

	public void setBoxIbW(Float boxIbW) {
		this.boxIbW = boxIbW;
	}

	public Float getBoxIbWInch() {
		return boxIbWInch;
	}

	public void setBoxIbWInch(Float boxIbWInch) {
		this.boxIbWInch = boxIbWInch;
	}

	public Float getBoxIbH() {
		return boxIbH;
	}

	public void setBoxIbH(Float boxIbH) {
		this.boxIbH = boxIbH;
	}

	public Float getBoxIbHInch() {
		return boxIbHInch;
	}

	public void setBoxIbHInch(Float boxIbHInch) {
		this.boxIbHInch = boxIbHInch;
	}

	public Float getBoxMbL() {
		return boxMbL;
	}

	public void setBoxMbL(Float boxMbL) {
		this.boxMbL = boxMbL;
	}

	public Float getBoxMbLInch() {
		return boxMbLInch;
	}

	public void setBoxMbLInch(Float boxMbLInch) {
		this.boxMbLInch = boxMbLInch;
	}

	public Float getBoxMbW() {
		return boxMbW;
	}

	public void setBoxMbW(Float boxMbW) {
		this.boxMbW = boxMbW;
	}

	public Float getBoxMbWInch() {
		return boxMbWInch;
	}

	public void setBoxMbWInch(Float boxMbWInch) {
		this.boxMbWInch = boxMbWInch;
	}

	public Float getBoxMbH() {
		return boxMbH;
	}

	public void setBoxMbH(Float boxMbH) {
		this.boxMbH = boxMbH;
	}

	public Float getBoxMbHInch() {
		return boxMbHInch;
	}

	public void setBoxMbHInch(Float boxMbHInch) {
		this.boxMbHInch = boxMbHInch;
	}

	public Float getBoxObL() {
		return boxObL;
	}

	public void setBoxObL(Float boxObL) {
		this.boxObL = boxObL;
	}

	public Float getBoxObLInch() {
		return boxObLInch;
	}

	public void setBoxObLInch(Float boxObLInch) {
		this.boxObLInch = boxObLInch;
	}

	public Float getBoxObW() {
		return boxObW;
	}

	public void setBoxObW(Float boxObW) {
		this.boxObW = boxObW;
	}

	public Float getBoxObWInch() {
		return boxObWInch;
	}

	public void setBoxObWInch(Float boxObWInch) {
		this.boxObWInch = boxObWInch;
	}

	public Float getBoxObH() {
		return boxObH;
	}

	public void setBoxObH(Float boxObH) {
		this.boxObH = boxObH;
	}

	public Float getBoxObHInch() {
		return boxObHInch;
	}

	public void setBoxObHInch(Float boxObHInch) {
		this.boxObHInch = boxObHInch;
	}

	public Float getBoxGrossWeigth() {
		return boxGrossWeigth;
	}

	public void setBoxGrossWeigth(Float boxGrossWeigth) {
		this.boxGrossWeigth = boxGrossWeigth;
	}

	public Float getBoxNetWeigth() {
		return boxNetWeigth;
	}

	public void setBoxNetWeigth(Float boxNetWeigth) {
		this.boxNetWeigth = boxNetWeigth;
	}

	public Long getBoxIbCount() {
		return boxIbCount;
	}

	public void setBoxIbCount(Long boxIbCount) {
		this.boxIbCount = boxIbCount;
	}

	public String getBoxIbType() {
		return boxIbType;
	}

	public void setBoxIbType(String boxIbType) {
		this.boxIbType = boxIbType;
	}

	public Long getBoxMbCount() {
		return boxMbCount;
	}

	public void setBoxMbCount(Long boxMbCount) {
		this.boxMbCount = boxMbCount;
	}

	public String getBoxMbType() {
		return boxMbType;
	}

	public void setBoxMbType(String boxMbType) {
		this.boxMbType = boxMbType;
	}

	public Long getBoxObCount() {
		return boxObCount;
	}

	public void setBoxObCount(Long boxObCount) {
		this.boxObCount = boxObCount;
	}

	public String getBoxObType() {
		return boxObType;
	}

	public void setBoxObType(String boxObType) {
		this.boxObType = boxObType;
	}

	public Float getBoxCuft() {
		return boxCuft;
	}

	public void setBoxCuft(Float boxCuft) {
		this.boxCuft = boxCuft;
	}

	public String getBoxRemark() {
		return boxRemark;
	}

	public void setBoxRemark(String boxRemark) {
		this.boxRemark = boxRemark;
	}

	public Float getBox20Count() {
		return box20Count;
	}

	public void setBox20Count(Float box20Count) {
		this.box20Count = box20Count;
	}

	public Float getBox40Count() {
		return box40Count;
	}

	public void setBox40Count(Float box40Count) {
		this.box40Count = box40Count;
	}

	public Float getBox40hqCount() {
		return box40hqCount;
	}

	public void setBox40hqCount(Float box40hqCount) {
		this.box40hqCount = box40hqCount;
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

	public Float getPriceFac() {
		priceFac = new Float(ContextUtil.getObjByPrecision(priceFac, "facPrecision"));
		return priceFac;
	}

	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}

	public Float getPriceOut() {
		priceOut = new Float(ContextUtil.getObjByPrecision(priceOut, "outPrecision"));
		return priceOut;
	}

	public void setPriceOut(Float priceOut) {
		this.priceOut = priceOut;
	}

	public Set getCotPictures() {
		return cotPictures;
	}

	public void setCotPictures(Set cotPictures) {
		this.cotPictures = cotPictures;
	}

	public Set getCotFile() {
		return cotFile;
	}

	public void setCotFile(Set cotFile) {
		this.cotFile = cotFile;
	}

	public Float getBoxWeigth() {
		return boxWeigth;
	}

	public void setBoxWeigth(Float boxWeigth) {
		this.boxWeigth = boxWeigth;
	}

	public String getEleComposeType() {
		return eleComposeType;
	}

	public void setEleComposeType(String eleComposeType) {
		this.eleComposeType = eleComposeType;
	}

	public String getBoxBDesc() {
		return boxBDesc;
	}

	public void setBoxBDesc(String boxBDesc) {
		this.boxBDesc = boxBDesc;
	}

	public String getBoxTDesc() {
		return boxTDesc;
	}

	public void setBoxTDesc(String boxTDesc) {
		this.boxTDesc = boxTDesc;
	}

	public Float getBoxHandleH() {
		return boxHandleH;
	}

	public void setBoxHandleH(Float boxHandleH) {
		this.boxHandleH = boxHandleH;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public byte[] getPicImg() {
		return picImg;
	}

	public void setPicImg(byte[] picImg) {
		this.picImg = picImg;
	}

	public Float getBox45Count() {
		return box45Count;
	}

	public void setBox45Count(Float box45Count) {
		this.box45Count = box45Count;
	}

	public Integer getEleTypeidLv2() {
		return eleTypeidLv2;
	}

	public void setEleTypeidLv2(Integer eleTypeidLv2) {
		this.eleTypeidLv2 = eleTypeidLv2;
	}

	public Integer getEleParentId() {
		return eleParentId;
	}

	public void setEleParentId(Integer eleParentId) {
		this.eleParentId = eleParentId;
	}

	public Set getChilds() {
		return childs;
	}

	public void setChilds(Set childs) {
		this.childs = childs;
	}

	public String getTaoUnit() {
		return taoUnit;
	}

	public void setTaoUnit(String taoUnit) {
		this.taoUnit = taoUnit;
	}

	public Float getTuiLv() {
		return tuiLv;
	}

	public void setTuiLv(Float tuiLv) {
		this.tuiLv = tuiLv;
	}

	public Float getLiRun() {
		return liRun;
	}

	public void setLiRun(Float liRun) {
		this.liRun = liRun;
	}

	public Set getCotPriceFacs() {
		return cotPriceFacs;
	}

	public void setCotPriceFacs(Set cotPriceFacs) {
		this.cotPriceFacs = cotPriceFacs;
	}

	public Set getCotEleFittings() {
		return cotEleFittings;
	}

	public void setCotEleFittings(Set cotEleFittings) {
		this.cotEleFittings = cotEleFittings;
	}

	public Set getCotElePrice() {
		return cotElePrice;
	}

	public void setCotElePrice(Set cotElePrice) {
		this.cotElePrice = cotElePrice;
	}

	public Float getBoxPbL() {
		return boxPbL;
	}

	public void setBoxPbL(Float boxPbL) {
		this.boxPbL = boxPbL;
	}

	public Float getBoxPbLInch() {
		return boxPbLInch;
	}

	public void setBoxPbLInch(Float boxPbLInch) {
		this.boxPbLInch = boxPbLInch;
	}

	public Float getBoxPbW() {
		return boxPbW;
	}

	public void setBoxPbW(Float boxPbW) {
		this.boxPbW = boxPbW;
	}

	public Float getBoxPbWInch() {
		return boxPbWInch;
	}

	public void setBoxPbWInch(Float boxPbWInch) {
		this.boxPbWInch = boxPbWInch;
	}

	public Float getBoxPbH() {
		return boxPbH;
	}

	public void setBoxPbH(Float boxPbH) {
		this.boxPbH = boxPbH;
	}

	public Float getBoxPbHInch() {
		return boxPbHInch;
	}

	public void setBoxPbHInch(Float boxPbHInch) {
		this.boxPbHInch = boxPbHInch;
	}

	public Long getBoxPbCount() {
		return boxPbCount;
	}

	public void setBoxPbCount(Long boxPbCount) {
		this.boxPbCount = boxPbCount;
	}

	public String getBoxPbType() {
		return boxPbType;
	}

	public void setBoxPbType(String boxPbType) {
		this.boxPbType = boxPbType;
	}

	public Float getBoxPbPrice() {
		return boxPbPrice;
	}

	public void setBoxPbPrice(Float boxPbPrice) {
		this.boxPbPrice = boxPbPrice;
	}

	public Float getBoxIbPrice() {
		return boxIbPrice;
	}

	public void setBoxIbPrice(Float boxIbPrice) {
		this.boxIbPrice = boxIbPrice;
	}

	public Float getBoxMbPrice() {
		return boxMbPrice;
	}

	public void setBoxMbPrice(Float boxMbPrice) {
		this.boxMbPrice = boxMbPrice;
	}

	public Float getBoxObPrice() {
		return boxObPrice;
	}

	public void setBoxObPrice(Float boxObPrice) {
		this.boxObPrice = boxObPrice;
	}

	public Float getPackingPrice() {
		return packingPrice;
	}

	public void setPackingPrice(Float packingPrice) {
		this.packingPrice = packingPrice;
	}

	public Integer getBoxPbTypeId() {
		return boxPbTypeId;
	}

	public void setBoxPbTypeId(Integer boxPbTypeId) {
		if(boxPbTypeId!=null && boxPbTypeId==0){
			boxPbTypeId=null;
		}
		this.boxPbTypeId = boxPbTypeId;
	}

	public Integer getBoxIbTypeId() {
		return boxIbTypeId;
	}

	public void setBoxIbTypeId(Integer boxIbTypeId) {
		if(boxIbTypeId!=null && boxIbTypeId==0){
			boxIbTypeId=null;
		}
		this.boxIbTypeId = boxIbTypeId;
	}

	public Integer getBoxMbTypeId() {
		return boxMbTypeId;
	}

	public void setBoxMbTypeId(Integer boxMbTypeId) {
		if(boxMbTypeId!=null && boxMbTypeId==0){
			boxMbTypeId=null;
		}
		this.boxMbTypeId = boxMbTypeId;
	}

	public Integer getBoxObTypeId() {
		return boxObTypeId;
	}

	public void setBoxObTypeId(Integer boxObTypeId) {
		if(boxObTypeId!=null && boxObTypeId==0){
			boxObTypeId=null;
		}
		this.boxObTypeId = boxObTypeId;
	}

	public Set getCotElePacking() {
		return cotElePacking;
	}

	public void setCotElePacking(Set cotElePacking) {
		this.cotElePacking = cotElePacking;
	}

	public Float getInputGridPrice() {
		return inputGridPrice;
	}

	public void setInputGridPrice(Float inputGridPrice) {
		this.inputGridPrice = inputGridPrice;
	}

	public Integer getInputGridTypeId() {
		return inputGridTypeId;
	}

	public void setInputGridTypeId(Integer inputGridTypeId) {
		if(inputGridTypeId!=null && inputGridTypeId==0){
			inputGridTypeId=null;
		}
		this.inputGridTypeId = inputGridTypeId;
	}

	public Float getPutL() {
		return putL;
	}

	public void setPutL(Float putL) {
		this.putL = putL;
	}

	public Float getPutW() {
		return putW;
	}

	public void setPutW(Float putW) {
		this.putW = putW;
	}

	public Float getPutH() {
		return putH;
	}

	public void setPutH(Float putH) {
		this.putH = putH;
	}

	public String getBoxRemarkCn() {
		return boxRemarkCn;
	}

	public void setBoxRemarkCn(String boxRemarkCn) {
		this.boxRemarkCn = boxRemarkCn;
	}

	public Integer getEleTypeidLv3() {
		return eleTypeidLv3;
	}

	public void setEleTypeidLv3(Integer eleTypeidLv3) {
		this.eleTypeidLv3 = eleTypeidLv3;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	

}