package com.sail.cot.domain;
 
import java.sql.Date; 

import com.sail.cot.util.ContextUtil;

/**
 * CotSignDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSignDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private String eleParent;
	private Integer eleParentId;
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
	private String custNo;
	private String factoryNo;
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
	private byte[]  picImg;
	private Float priceFac;
	private Float priceOut;
	private String signRequire;
	private Date custTime;
	private Date requireTime;
	private Date addTime;
	private String addPerson;
	private Integer signId;
	private Integer factoryId;
	private Integer eleTypeidLv1;
	private Integer eleTypeidLv2;
	private Integer eleTypeidLv3;
	private Integer eleHsid;
	private Integer priceFacUint;
	private Integer priceOutUint;
	private Integer boxTypeId;
	private String op;
	private String chk; 
	private Float cube;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	// Constructors
	public Float getCube() {
		return cube;
	}

	public void setCube(Float cube) {
		this.cube = cube;
	}

	/** default constructor */
	public CotSignDetail() {
	}

	/** full constructor */
	public CotSignDetail(String eleParent, String eleId, String eleName,
			String eleNameEn, Long eleFlag, String eleDesc, String eleFrom,
			String eleComposeType, String eleGrade, String eleUnit,
			String eleFactory, String eleCol, String eleMeterial,
			String eleSizeDesc, String eleInchDesc, String eleRemark,
			String eleTypenameLv1, String eleTypenameLv2,
			String eleTypenameLv3, Date eleAddTime, Date eleProTime,
			String eleForPerson, Integer eleMod, Integer eleUnitnum,
			String boxUint, String custNo, String factoryNo, Float boxCbm,
			Float boxTL, Float boxTLInch, Float boxTW, Float boxTWInch,
			Float boxTD, Float boxTDInch, Float boxBL, Float boxBLInch,
			Float boxBW, Float boxBWInch, Float boxBD, Float boxBDInch,
			Float boxL, Float boxW, Float boxH, Float boxLInch,
			Float boxWInch, Float boxHInch, Float boxIbL, Float boxIbLInch,
			Float boxIbW, Float boxIbWInch, Float boxIbH, Float boxIbHInch,
			Float boxMbL, Float boxMbLInch, Float boxMbW, Float boxMbWInch,
			Float boxMbH, Float boxMbHInch, Float boxObL, Float boxObLInch,
			Float boxObW, Float boxObWInch, Float boxObH, Float boxObHInch,
			Float boxWeigth, Float boxGrossWeigth, Float boxNetWeigth,
			Long boxIbCount, String boxIbType, Long boxMbCount,
			String boxMbType, Long boxObCount, String boxObType,
			Float boxCuft, String boxRemark, Float box20Count,
			Float box40Count, Float box40hqCount, String boxBDesc,
			String boxTDesc, Float boxHandleH, String picName, String picPath,
			Float priceFac, Float priceOut, String signRequire,
			Date custTime, Date requireTime, Date addTime, String addPerson,
			Integer signId, Integer factoryId, Integer eleTypeidLv1,
			Integer eleHsid, Integer priceFacUint, Integer priceOutUint,
			Integer boxTypeId ) {
		this.eleParent = eleParent;
		this.eleId = eleId;
		this.eleName = eleName;
		this.eleNameEn = eleNameEn;
		this.eleFlag = eleFlag;
		this.eleDesc = eleDesc;
		this.eleFrom = eleFrom;
		this.eleComposeType = eleComposeType;
		this.eleGrade = eleGrade;
		this.eleUnit = eleUnit;
		this.eleFactory = eleFactory;
		this.eleCol = eleCol;
		this.eleMeterial = eleMeterial;
		this.eleSizeDesc = eleSizeDesc;
		this.eleInchDesc = eleInchDesc;
		this.eleRemark = eleRemark;
		this.eleTypenameLv1 = eleTypenameLv1;
		this.eleTypenameLv2 = eleTypenameLv2;
		this.eleTypenameLv3 = eleTypenameLv3;
		this.eleAddTime = eleAddTime;
		this.eleProTime = eleProTime;
		this.eleForPerson = eleForPerson;
		this.eleMod = eleMod;
		this.eleUnitNum = eleUnitNum;
		this.boxUint = boxUint;
		this.custNo = custNo;
		this.factoryNo = factoryNo;
		this.boxCbm = boxCbm;
		this.boxTL = boxTL;
		this.boxTLInch = boxTLInch;
		this.boxTW = boxTW;
		this.boxTWInch = boxTWInch;
		this.boxTD = boxTD;
		this.boxTDInch = boxTDInch;
		this.boxBL = boxBL;
		this.boxBLInch = boxBLInch;
		this.boxBW = boxBW;
		this.boxBWInch = boxBWInch;
		this.boxBD = boxBD;
		this.boxBDInch = boxBDInch;
		this.boxL = boxL;
		this.boxW = boxW;
		this.boxH = boxH;
		this.boxLInch = boxLInch;
		this.boxWInch = boxWInch;
		this.boxHInch = boxHInch;
		this.boxIbL = boxIbL;
		this.boxIbLInch = boxIbLInch;
		this.boxIbW = boxIbW;
		this.boxIbWInch = boxIbWInch;
		this.boxIbH = boxIbH;
		this.boxIbHInch = boxIbHInch;
		this.boxMbL = boxMbL;
		this.boxMbLInch = boxMbLInch;
		this.boxMbW = boxMbW;
		this.boxMbWInch = boxMbWInch;
		this.boxMbH = boxMbH;
		this.boxMbHInch = boxMbHInch;
		this.boxObL = boxObL;
		this.boxObLInch = boxObLInch;
		this.boxObW = boxObW;
		this.boxObWInch = boxObWInch;
		this.boxObH = boxObH;
		this.boxObHInch = boxObHInch;
		this.boxWeigth = boxWeigth;
		this.boxGrossWeigth = boxGrossWeigth;
		this.boxNetWeigth = boxNetWeigth;
		this.boxIbCount = boxIbCount;
		this.boxIbType = boxIbType;
		this.boxMbCount = boxMbCount;
		this.boxMbType = boxMbType;
		this.boxObCount = boxObCount;
		this.boxObType = boxObType;
		this.boxCuft = boxCuft;
		this.boxRemark = boxRemark;
		this.box20Count = box20Count;
		this.box40Count = box40Count;
		this.box40hqCount = box40hqCount;
		this.boxBDesc = boxBDesc;
		this.boxTDesc = boxTDesc;
		this.boxHandleH = boxHandleH;
		this.picName = picName;
		this.picPath = picPath;
		this.priceFac = priceFac;
		this.priceOut = priceOut;
		this.signRequire = signRequire;
		this.custTime = custTime;
		this.requireTime = requireTime;
		this.addTime = addTime;
		this.addPerson = addPerson;
		this.signId = signId;
		this.factoryId = factoryId;
		this.eleTypeidLv1 = eleTypeidLv1;
		this.eleHsid = eleHsid;
		this.priceFacUint = priceFacUint;
		this.priceOutUint = priceOutUint;
		this.boxTypeId = boxTypeId;
		 
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEleParent() {
		return eleParent;
	}

	public void setEleParent(String eleParent) {
		this.eleParent = eleParent;
	}

	public String getEleId() {
		return this.eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getEleName() {
		return this.eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public String getEleNameEn() {
		return this.eleNameEn;
	}

	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}

	public Long getEleFlag() {
		return this.eleFlag;
	}

	public void setEleFlag(Long eleFlag) {
		this.eleFlag = eleFlag;
	}

	public String getEleDesc() {
		return this.eleDesc;
	}

	public void setEleDesc(String eleDesc) {
		this.eleDesc = eleDesc;
	}

	public String getEleFrom() {
		return this.eleFrom;
	}

	public void setEleFrom(String eleFrom) {
		this.eleFrom = eleFrom;
	}

	public String getEleComposeType() {
		return this.eleComposeType;
	}

	public void setEleComposeType(String eleComposeType) {
		this.eleComposeType = eleComposeType;
	}

	public String getEleGrade() {
		return this.eleGrade;
	}

	public void setEleGrade(String eleGrade) {
		this.eleGrade = eleGrade;
	}

	public String getEleUnit() {
		return this.eleUnit;
	}

	public void setEleUnit(String eleUnit) {
		this.eleUnit = eleUnit;
	}

	public String getEleFactory() {
		return this.eleFactory;
	}

	public void setEleFactory(String eleFactory) {
		this.eleFactory = eleFactory;
	}

	public String getEleCol() {
		return this.eleCol;
	}

	public void setEleCol(String eleCol) {
		this.eleCol = eleCol;
	}

	public String getEleMeterial() {
		return this.eleMeterial;
	}

	public void setEleMeterial(String eleMeterial) {
		this.eleMeterial = eleMeterial;
	}

	public String getEleSizeDesc() {
		return this.eleSizeDesc;
	}

	public void setEleSizeDesc(String eleSizeDesc) {
		this.eleSizeDesc = eleSizeDesc;
	}

	public String getEleInchDesc() {
		return this.eleInchDesc;
	}

	public void setEleInchDesc(String eleInchDesc) {
		this.eleInchDesc = eleInchDesc;
	}

	public String getEleRemark() {
		return this.eleRemark;
	}

	public void setEleRemark(String eleRemark) {
		this.eleRemark = eleRemark;
	}

	public String getEleTypenameLv1() {
		return this.eleTypenameLv1;
	}

	public void setEleTypenameLv1(String eleTypenameLv1) {
		this.eleTypenameLv1 = eleTypenameLv1;
	}

	public String getEleTypenameLv2() {
		return this.eleTypenameLv2;
	}

	public void setEleTypenameLv2(String eleTypenameLv2) {
		this.eleTypenameLv2 = eleTypenameLv2;
	}

	public String getEleTypenameLv3() {
		return this.eleTypenameLv3;
	}

	public void setEleTypenameLv3(String eleTypenameLv3) {
		this.eleTypenameLv3 = eleTypenameLv3;
	}

	public Date getEleAddTime() {
		return this.eleAddTime;
	}

	public void setEleAddTime(Date eleAddTime) {
		this.eleAddTime = eleAddTime;
	}

	public Date getEleProTime() {
		return this.eleProTime;
	}

	public void setEleProTime(Date eleProTime) {
		this.eleProTime = eleProTime;
	}

	public String getEleForPerson() {
		return this.eleForPerson;
	}

	public void setEleForPerson(String eleForPerson) {
		this.eleForPerson = eleForPerson;
	}

	public Integer getEleMod() {
		return this.eleMod;
	}

	public void setEleMod(Integer eleMod) {
		this.eleMod = eleMod;
	}

	public String getBoxUint() {
		return this.boxUint;
	}

	public void setBoxUint(String boxUint) {
		this.boxUint = boxUint;
	}

	public String getCustNo() {
		return this.custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getFactoryNo() {
		return this.factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public Float getBoxCbm() {
		return this.boxCbm;
	}

	public void setBoxCbm(Float boxCbm) {
		this.boxCbm = boxCbm;
	}

	public Float getBoxTL() {
		return this.boxTL;
	}

	public void setBoxTL(Float boxTL) {
		this.boxTL = boxTL;
	}

	public Float getBoxTLInch() {
		return this.boxTLInch;
	}

	public void setBoxTLInch(Float boxTLInch) {
		this.boxTLInch = boxTLInch;
	}

	public Float getBoxTW() {
		return this.boxTW;
	}

	public void setBoxTW(Float boxTW) {
		this.boxTW = boxTW;
	}

	public Float getBoxTWInch() {
		return this.boxTWInch;
	}

	public void setBoxTWInch(Float boxTWInch) {
		this.boxTWInch = boxTWInch;
	}

	public Float getBoxTD() {
		return this.boxTD;
	}

	public void setBoxTD(Float boxTD) {
		this.boxTD = boxTD;
	}

	public Float getBoxTDInch() {
		return this.boxTDInch;
	}

	public void setBoxTDInch(Float boxTDInch) {
		this.boxTDInch = boxTDInch;
	}

	public Float getBoxBL() {
		return this.boxBL;
	}

	public void setBoxBL(Float boxBL) {
		this.boxBL = boxBL;
	}

	public Float getBoxBLInch() {
		return this.boxBLInch;
	}

	public void setBoxBLInch(Float boxBLInch) {
		this.boxBLInch = boxBLInch;
	}

	public Float getBoxBW() {
		return this.boxBW;
	}

	public void setBoxBW(Float boxBW) {
		this.boxBW = boxBW;
	}

	public Float getBoxBWInch() {
		return this.boxBWInch;
	}

	public void setBoxBWInch(Float boxBWInch) {
		this.boxBWInch = boxBWInch;
	}

	public Float getBoxBD() {
		return this.boxBD;
	}

	public void setBoxBD(Float boxBD) {
		this.boxBD = boxBD;
	}

	public Float getBoxBDInch() {
		return this.boxBDInch;
	}

	public void setBoxBDInch(Float boxBDInch) {
		this.boxBDInch = boxBDInch;
	}

	public Float getBoxL() {
		return this.boxL;
	}

	public void setBoxL(Float boxL) {
		this.boxL = boxL;
	}

	public Float getBoxW() {
		return this.boxW;
	}

	public void setBoxW(Float boxW) {
		this.boxW = boxW;
	}

	public Float getBoxH() {
		return this.boxH;
	}

	public void setBoxH(Float boxH) {
		this.boxH = boxH;
	}

	public Float getBoxLInch() {
		return this.boxLInch;
	}

	public void setBoxLInch(Float boxLInch) {
		this.boxLInch = boxLInch;
	}

	public Float getBoxWInch() {
		return this.boxWInch;
	}

	public void setBoxWInch(Float boxWInch) {
		this.boxWInch = boxWInch;
	}

	public Float getBoxHInch() {
		return this.boxHInch;
	}

	public void setBoxHInch(Float boxHInch) {
		this.boxHInch = boxHInch;
	}

	public Float getBoxIbL() {
		return this.boxIbL;
	}

	public void setBoxIbL(Float boxIbL) {
		this.boxIbL = boxIbL;
	}

	public Float getBoxIbLInch() {
		return this.boxIbLInch;
	}

	public void setBoxIbLInch(Float boxIbLInch) {
		this.boxIbLInch = boxIbLInch;
	}

	public Float getBoxIbW() {
		return this.boxIbW;
	}

	public void setBoxIbW(Float boxIbW) {
		this.boxIbW = boxIbW;
	}

	public Float getBoxIbWInch() {
		return this.boxIbWInch;
	}

	public void setBoxIbWInch(Float boxIbWInch) {
		this.boxIbWInch = boxIbWInch;
	}

	public Float getBoxIbH() {
		return this.boxIbH;
	}

	public void setBoxIbH(Float boxIbH) {
		this.boxIbH = boxIbH;
	}

	public Float getBoxIbHInch() {
		return this.boxIbHInch;
	}

	public void setBoxIbHInch(Float boxIbHInch) {
		this.boxIbHInch = boxIbHInch;
	}

	public Float getBoxMbL() {
		return this.boxMbL;
	}

	public void setBoxMbL(Float boxMbL) {
		this.boxMbL = boxMbL;
	}

	public Float getBoxMbLInch() {
		return this.boxMbLInch;
	}

	public void setBoxMbLInch(Float boxMbLInch) {
		this.boxMbLInch = boxMbLInch;
	}

	public Float getBoxMbW() {
		return this.boxMbW;
	}

	public void setBoxMbW(Float boxMbW) {
		this.boxMbW = boxMbW;
	}

	public Float getBoxMbWInch() {
		return this.boxMbWInch;
	}

	public void setBoxMbWInch(Float boxMbWInch) {
		this.boxMbWInch = boxMbWInch;
	}

	public Float getBoxMbH() {
		return this.boxMbH;
	}

	public void setBoxMbH(Float boxMbH) {
		this.boxMbH = boxMbH;
	}

	public Float getBoxMbHInch() {
		return this.boxMbHInch;
	}

	public void setBoxMbHInch(Float boxMbHInch) {
		this.boxMbHInch = boxMbHInch;
	}

	public Float getBoxObL() {
		return this.boxObL;
	}

	public void setBoxObL(Float boxObL) {
		this.boxObL = boxObL;
	}

	public Float getBoxObLInch() {
		return this.boxObLInch;
	}

	public void setBoxObLInch(Float boxObLInch) {
		this.boxObLInch = boxObLInch;
	}

	public Float getBoxObW() {
		return this.boxObW;
	}

	public void setBoxObW(Float boxObW) {
		this.boxObW = boxObW;
	}

	public Float getBoxObWInch() {
		return this.boxObWInch;
	}

	public void setBoxObWInch(Float boxObWInch) {
		this.boxObWInch = boxObWInch;
	}

	public Float getBoxObH() {
		return this.boxObH;
	}

	public void setBoxObH(Float boxObH) {
		this.boxObH = boxObH;
	}

	public Float getBoxObHInch() {
		return this.boxObHInch;
	}

	public void setBoxObHInch(Float boxObHInch) {
		this.boxObHInch = boxObHInch;
	}

	public Float getBoxWeigth() {
		return this.boxWeigth;
	}

	public void setBoxWeigth(Float boxWeigth) {
		this.boxWeigth = boxWeigth;
	}

	public Float getBoxGrossWeigth() {
		return this.boxGrossWeigth;
	}

	public void setBoxGrossWeigth(Float boxGrossWeigth) {
		this.boxGrossWeigth = boxGrossWeigth;
	}

	public Float getBoxNetWeigth() {
		return this.boxNetWeigth;
	}

	public void setBoxNetWeigth(Float boxNetWeigth) {
		this.boxNetWeigth = boxNetWeigth;
	}

	public Long getBoxIbCount() {
		return this.boxIbCount;
	}

	public void setBoxIbCount(Long boxIbCount) {
		this.boxIbCount = boxIbCount;
	}

	public String getBoxIbType() {
		return this.boxIbType;
	}

	public void setBoxIbType(String boxIbType) {
		this.boxIbType = boxIbType;
	}

	public Long getBoxMbCount() {
		return this.boxMbCount;
	}

	public void setBoxMbCount(Long boxMbCount) {
		this.boxMbCount = boxMbCount;
	}

	public String getBoxMbType() {
		return this.boxMbType;
	}

	public void setBoxMbType(String boxMbType) {
		this.boxMbType = boxMbType;
	}

	public Long getBoxObCount() {
		return this.boxObCount;
	}

	public void setBoxObCount(Long boxObCount) {
		this.boxObCount = boxObCount;
	}

	public String getBoxObType() {
		return this.boxObType;
	}

	public void setBoxObType(String boxObType) {
		this.boxObType = boxObType;
	}

	public Float getBoxCuft() {
		return this.boxCuft;
	}

	public void setBoxCuft(Float boxCuft) {
		this.boxCuft = boxCuft;
	}

	public String getBoxRemark() {
		return this.boxRemark;
	}

	public void setBoxRemark(String boxRemark) {
		this.boxRemark = boxRemark;
	}

	public Float getBox20Count() {
		return this.box20Count;
	}

	public void setBox20Count(Float box20Count) {
		this.box20Count = box20Count;
	}

	public Float getBox40Count() {
		return this.box40Count;
	}

	public void setBox40Count(Float box40Count) {
		this.box40Count = box40Count;
	}

	public Float getBox40hqCount() {
		return this.box40hqCount;
	}

	public void setBox40hqCount(Float box40hqCount) {
		this.box40hqCount = box40hqCount;
	}

	public String getBoxBDesc() {
		return this.boxBDesc;
	}

	public void setBoxBDesc(String boxBDesc) {
		this.boxBDesc = boxBDesc;
	}

	public String getBoxTDesc() {
		return this.boxTDesc;
	}

	public void setBoxTDesc(String boxTDesc) {
		this.boxTDesc = boxTDesc;
	}

	public Float getBoxHandleH() {
		return this.boxHandleH;
	}

	public void setBoxHandleH(Float boxHandleH) {
		this.boxHandleH = boxHandleH;
	}

	public String getPicName() {
		return this.picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicPath() {
		return this.picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Float getPriceFac() {
		priceFac = new Float(ContextUtil.getObjByPrecision(priceFac, "facPrecision"));
		return this.priceFac;
	}

	public void setPriceFac(Float priceFac) {
		this.priceFac = priceFac;
	}

	public Float getPriceOut() {
		priceOut = new Float(ContextUtil.getObjByPrecision(priceOut, "outPrecision"));
		return this.priceOut;
	}

	public void setPriceOut(Float priceOut) {
		this.priceOut = priceOut;
	}

	public String getSignRequire() {
		return this.signRequire;
	}

	public void setSignRequire(String signRequire) {
		this.signRequire = signRequire;
	}

	public Date getCustTime() {
		return this.custTime;
	}

	public void setCustTime(Date custTime) {
		this.custTime = custTime;
	}

	public Date getRequireTime() {
		return this.requireTime;
	}

	public void setRequireTime(Date requireTime) {
		this.requireTime = requireTime;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddPerson() {
		return this.addPerson;
	}

	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}

	public Integer getSignId() {
		return this.signId;
	}

	public void setSignId(Integer signId) {
		this.signId = signId;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getEleTypeidLv1() {
		return this.eleTypeidLv1;
	}

	public void setEleTypeidLv1(Integer eleTypeidLv1) {
		this.eleTypeidLv1 = eleTypeidLv1;
	}

	public Integer getEleHsid() {
		return this.eleHsid;
	}

	public void setEleHsid(Integer eleHsid) {
		this.eleHsid = eleHsid;
	}

	public Integer getPriceFacUint() {
		return this.priceFacUint;
	}

	public void setPriceFacUint(Integer priceFacUint) {
		this.priceFacUint = priceFacUint;
	}

	public Integer getPriceOutUint() {
		return this.priceOutUint;
	}

	public void setPriceOutUint(Integer priceOutUint) {
		this.priceOutUint = priceOutUint;
	}

	public Integer getBoxTypeId() {
		return this.boxTypeId;
	}

	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
	}

	public Integer getEleUnitNum() {
		return eleUnitNum;
	}

	public void setEleUnitNum(Integer eleUnitNum) {
		this.eleUnitNum = eleUnitNum;
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

	 
	
}