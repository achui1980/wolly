package com.sail.cot.domain;

import java.sql.Date;

import com.sail.cot.util.ContextUtil;

/**
 * CotOrderFacdetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotOrderFacdetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private String eleId;
	private String eleParent;
	private Integer eleParentId;
	private String eleName;
	private String eleNameEn;
	private Long eleFlag;
	private String eleDesc;
	private String eleFrom;
	private String eleComposetype;
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
	private Integer eleUnitnum;
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
	private Float box20Count;
	private Float box40Count;
	private Float box40hqCount;
	private Float box45Count;
	private String boxBdesc;
	private String boxTdesc;
	private Float boxHandleh;
	private String picName;
	private String picPath;
	private byte[] picImg;
	private Float priceFac;
	private Float priceOut;
	private Integer factoryId;
	private Integer eleTypeidLv1;
	private Integer eleTypeidLv2;
	private Integer eleTypeidLv3;
	private Integer hsId;
	private Integer priceFacUint;
	private Integer priceOutUint;

	private Long boxCount;
	private Long containerCount;
	private Date addTime;
	private Float totalCbm;
	private Float totalGrossWeigth;
	private Float totalNetWeigth;
	private Float totalFac;
	private Float totalMoney;
	private String custNo;
	private String factoryNo;
	private Float orderPrice;
	private Integer currencyId;
	private Float boxWeigth;
	private Integer boxTypeId;
	private String taoUnit;
	private Integer orderDetailId;
	private String op;
	private Float cube;
	private String barcode;
	private String factoryShortName;
	private Long outRemain;
	private Long outCurrent;
	private Long outHasOut;  //已出货数量
	private Float moldCharge;
	private Integer allocateFlag;//是否已指定数量 0：未指定 1：已指定
	
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
	private Float elePrice;
	private Float eleFittingPrice;
	
	private Integer boxPbTypeId;
	private Integer boxIbTypeId;
	private Integer boxMbTypeId;
	private Integer boxObTypeId;
	
	private String type;
	private Long unContainerCount;
	private Integer sortNo;
	private String rdm;
	
	private Float inputGridPrice;
	private Integer inputGridTypeId;
	private Float putL;
	private Float putW;
	private Float putH;
	
	private String boxRemarkCn;
	private String importTax;

	// Constructors

	public String getImportTax() {
		return importTax;
	}

	public void setImportTax(String importTax) {
		this.importTax = importTax;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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

	public void setOutHasOut(Long outHasOut) {
		this.outHasOut = outHasOut;
	}

	public Long getOutHasOut() {
		if (outRemain == null) {
			outRemain = 0L;
		}
		outHasOut = boxCount - outRemain;
		return outHasOut;
	}

	public Long getOutRemain() {
		return outRemain;
	}

	public void setOutRemain(Long outRemain) {
		this.outRemain = outRemain;
	}

	public Long getOutCurrent() {
		return outCurrent;
	}

	public void setOutCurrent(Long outCurrent) {
		this.outCurrent = outCurrent;
	}

	public String getFactoryShortName() {
		return factoryShortName;
	}

	public void setFactoryShortName(String factoryShortName) {
		this.factoryShortName = factoryShortName;
	}

	public Float getCube() {
		return cube;
	}

	public void setCube(Float cube) {
		this.cube = cube;
	}

	public Float getBox45Count() {
		return box45Count;
	}

	public void setBox45Count(Float box45Count) {
		this.box45Count = box45Count;
	}

	public Long getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Long boxCount) {
		this.boxCount = boxCount;
	}

	public Long getContainerCount() {
		return containerCount;
	}

	public void setContainerCount(Long containerCount) {
		this.containerCount = containerCount;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/** default constructor */
	public CotOrderFacdetail() {
	}

	/** full constructor */
	public CotOrderFacdetail(CotOrderFac cotOrderFac, String eleId,
			String eleParent, String eleName, String eleNameEn, Long eleFlag,
			String eleDesc, String eleFrom, String eleComposetype,
			String eleGrade, String eleUnit, String eleFactory, String eleCol,
			String eleMeterial, String eleSizeDesc, String eleInchDesc,
			String eleRemark, String eleTypenameLv1, String eleTypenameLv2,
			String eleTypenameLv3, Date eleAddTime, Date eleProTime,
			String eleForPerson, Integer eleMod, Integer eleUnitnum,
			String boxUint, Float boxCbm, Float boxTL, Float boxTLInch,
			Float boxTW, Float boxTWInch, Float boxTD, Float boxTDInch,
			Float boxBL, Float boxBLInch, Float boxBW, Float boxBWInch,
			Float boxBD, Float boxBDInch, Float boxL, Float boxW, Float boxH,
			Float boxLInch, Float boxWInch, Float boxHInch, Float boxIbL,
			Float boxIbLInch, Float boxIbW, Float boxIbWInch, Float boxIbH,
			Float boxIbHInch, Float boxMbL, Float boxMbLInch, Float boxMbW,
			Float boxMbWInch, Float boxMbH, Float boxMbHInch, Float boxObL,
			Float boxObLInch, Float boxObW, Float boxObWInch, Float boxObH,
			Float boxObHInch, Float boxGrossWeigth, Float boxNetWeigth,
			Long boxIbCount, String boxIbType, Long boxMbCount,
			String boxMbType, Long boxObCount,

			String boxObType, Float boxCuft, String boxRemark,
			Float box20Count, Float box40Count, Float box40hqCount,
			String boxBdesc, String boxTdesc, Float boxHandleh, String picName,
			String picPath, String picImg, Float priceFac, Float priceOut,
			Integer factoryId, Integer eleTypeidLv1, Integer hsId,
			Integer priceFacUint, Integer priceOutUint, Integer orderDetailId) {

		this.eleId = eleId;
		this.eleParent = eleParent;
		this.eleName = eleName;
		this.eleNameEn = eleNameEn;
		this.eleFlag = eleFlag;
		this.eleDesc = eleDesc;
		this.eleFrom = eleFrom;
		this.eleComposetype = eleComposetype;
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
		this.eleUnitnum = eleUnitnum;
		this.boxUint = boxUint;
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
		this.boxBdesc = boxBdesc;
		this.boxTdesc = boxTdesc;
		this.boxHandleh = boxHandleh;
		this.picName = picName;
		this.picPath = picPath;
		this.priceFac = priceFac;
		this.priceOut = priceOut;
		this.factoryId = factoryId;
		this.eleTypeidLv1 = eleTypeidLv1;
		this.hsId = hsId;
		this.priceFacUint = priceFacUint;
		this.priceOutUint = priceOutUint;
		this.orderDetailId = orderDetailId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getEleId() {
		return this.eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getEleParent() {
		return eleParent;
	}

	public void setEleParent(String eleParent) {
		this.eleParent = eleParent;
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

	public String getEleComposetype() {
		return this.eleComposetype;
	}

	public void setEleComposetype(String eleComposetype) {
		this.eleComposetype = eleComposetype;
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

	public Integer getEleUnitnum() {
		return this.eleUnitnum;
	}

	public void setEleUnitnum(Integer eleUnitnum) {
		this.eleUnitnum = eleUnitnum;
	}

	public String getBoxUint() {
		return this.boxUint;
	}

	public void setBoxUint(String boxUint) {
		this.boxUint = boxUint;
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

	public String getBoxBdesc() {
		return this.boxBdesc;
	}

	public void setBoxBdesc(String boxBdesc) {
		this.boxBdesc = boxBdesc;
	}

	public String getBoxTdesc() {
		return this.boxTdesc;
	}

	public void setBoxTdesc(String boxTdesc) {
		this.boxTdesc = boxTdesc;
	}

	public Float getBoxHandleh() {
		return this.boxHandleh;
	}

	public void setBoxHandleh(Float boxHandleh) {
		this.boxHandleh = boxHandleh;
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

	public byte[] getPicImg() {
		return this.picImg;
	}

	public void setPicImg(byte[] picImg) {
		this.picImg = picImg;
	}

	public Float getPriceFac() {
		priceFac = new Float(ContextUtil.getObjByPrecision(priceFac, "facPrecision"));
		return this.priceFac;
	}

	public void setPriceFac(Float priceFac) {
		priceFac = new Float(ContextUtil.getObjByPrecision(priceFac, "facPrecision"));
		this.priceFac = priceFac;
	}

	public Float getPriceOut() {
		priceOut = new Float(ContextUtil.getObjByPrecision(priceOut, "outPrecision"));
		return this.priceOut;
	}

	public void setPriceOut(Float priceOut) {
		priceOut = new Float(ContextUtil.getObjByPrecision(priceOut, "outPrecision"));
		this.priceOut = priceOut;
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

	public Integer getHsId() {
		return this.hsId;
	}

	public void setHsId(Integer hsId) {
		this.hsId = hsId;
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

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Float getTotalCbm() {
		return totalCbm;
	}

	public void setTotalCbm(Float totalCbm) {
		this.totalCbm = totalCbm;
	}

	public Float getTotalGrossWeigth() {
		return totalGrossWeigth;
	}

	public void setTotalGrossWeigth(Float totalGrossWeigth) {
		this.totalGrossWeigth = totalGrossWeigth;
	}

	public Float getTotalNetWeigth() {
		return totalNetWeigth;
	}

	public void setTotalNetWeigth(Float totalNetWeigth) {
		this.totalNetWeigth = totalNetWeigth;
	}

	public Float getTotalFac() {
		return totalFac;
	}

	public void setTotalFac(Float totalFac) {
		this.totalFac = totalFac;
	}

	public Float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getFactoryNo() {
		return factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public Float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Float getBoxWeigth() {
		return boxWeigth;
	}

	public void setBoxWeigth(Float boxWeigth) {
		this.boxWeigth = boxWeigth;
	}

	public Integer getBoxTypeId() {
		return boxTypeId;
	}

	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
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


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}


	public String getTaoUnit() {
		return taoUnit;
	}

	public void setTaoUnit(String taoUnit) {
		this.taoUnit = taoUnit;
	}

	public Float getMoldCharge() {
		return moldCharge;
	}

	public void setMoldCharge(Float moldCharge) {
		this.moldCharge = moldCharge;
	}

	public Integer getAllocateFlag() {
		return allocateFlag;
	}

	public void setAllocateFlag(Integer allocateFlag) {
		this.allocateFlag = allocateFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUnContainerCount() {
		return unContainerCount;
	}

	public void setUnContainerCount(Long unContainerCount) {
		this.unContainerCount = unContainerCount;
	}

	public String getRdm() {
		return rdm;
	}

	public void setRdm(String rdm) {
		this.rdm = rdm;
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
}