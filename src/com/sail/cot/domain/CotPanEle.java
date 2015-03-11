package com.sail.cot.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;




public class CotPanEle implements java.io.Serializable {

	// Fields
	private Integer id;
	//PRS2.0
	private Integer panId;//询盘主单id
	private String eleId;//货号--Article name
	private String eleNameEn;//货号--Article name
	private String size;//尺寸--Size
	private String boxCount;//预计数量--Quantity expected
	//颜色
	private Integer printed;//印花
	private Integer dyed;//染色
	private Integer yarnDyed;//色织
	private Integer others;//其他
	private String packaging;//包装--Packaging(吊牌/腰封/礼品盒/彩卡/其他)
	private String pack;//包装--Packing(混装/托盘/纸托盘)
	private String packOther;//包装--Packing(混装/托盘/纸托盘)
	private String productTime;//预计出货时间
	private String material;//材料
	private String construction;//面料成分
	private String fillingMaterial;//充棉材质
	private String fillingWeight;//充棉克数
	
	private String colorRemark;//颜色备注
	private String packagingRemark;//包装备注
	private String packRemark;//包材备注
	private String priceRemark;//报价备注
	
	private Integer canCurId;//参考币种
	private Double canPrice;//参考报价
	
	private Integer targetCurId;//目标币种
	private Double targetPrice;//目标报价
	
	private Double panPrice;//确认报价
	private Integer currencyId;//确认币种
	private String manufactorer;//确认的意向供应商名称
	
	private Date modDate;//wolly最后修改时间
	private Integer modPerson;//wolly最后修改人
	private Integer state;//状态([0, '未完成'], [1, '已完成'])
	
	private String prsNo;//临时字段
	private String pcs2040;//临时字段
	private String priceNo;//临时字段 title
	private String addTime;//临时字段
	private String empsName;//临时字段 
	private String deptName;//临时字段
	private String certificate;
	
	// Constructors
	/** default constructor */
	public CotPanEle() {
	}
	
	public CotPanEle(CotPanEle obj,CotPan pan,String empsName,String deptName) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			BeanUtils.copyProperties(this, obj);
			BeanUtils.copyProperty(this, "prsNo", pan.getPrsNo());
			BeanUtils.copyProperty(this, "priceNo", pan.getPriceNo());
			BeanUtils.copyProperty(this, "addTime",sdf.format(pan.getAddTime()));
			BeanUtils.copyProperty(this, "empsName",empsName);
			BeanUtils.copyProperty(this, "deptName",deptName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getId() {
		return id;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getEmpsName() {
		return empsName;
	}

	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public String getPrsNo() {
		return prsNo;
	}

	public void setPrsNo(String prsNo) {
		this.prsNo = prsNo;
	}

	public String getPriceNo() {
		return priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getColorRemark() {
		return colorRemark;
	}

	public void setColorRemark(String colorRemark) {
		this.colorRemark = colorRemark;
	}

	public String getPackagingRemark() {
		return packagingRemark;
	}

	public void setPackagingRemark(String packagingRemark) {
		this.packagingRemark = packagingRemark;
	}

	public String getPackRemark() {
		return packRemark;
	}

	public void setPackRemark(String packRemark) {
		this.packRemark = packRemark;
	}

	public String getPriceRemark() {
		return priceRemark;
	}

	public void setPriceRemark(String priceRemark) {
		this.priceRemark = priceRemark;
	}

	public Date getModDate() {
		return modDate;
	}

	public Integer getCanCurId() {
		return canCurId;
	}

	public void setCanCurId(Integer canCurId) {
		this.canCurId = canCurId;
	}

	public Integer getTargetCurId() {
		return targetCurId;
	}

	public void setTargetCurId(Integer targetCurId) {
		this.targetCurId = targetCurId;
	}

	public Double getTargetPrice() {
		return targetPrice;
	}

	public void setTargetPrice(Double targetPrice) {
		this.targetPrice = targetPrice;
	}

	public String getManufactorer() {
		return manufactorer;
	}

	public void setManufactorer(String manufactorer) {
		this.manufactorer = manufactorer;
	}
	public String getEleNameEn() {
		return eleNameEn;
	}

	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public String getPackOther() {
		return packOther;
	}

	public void setPackOther(String packOther) {
		this.packOther = packOther;
	}

	public Integer getModPerson() {
		return modPerson;
	}

	public void setModPerson(Integer modPerson) {
		this.modPerson = modPerson;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getPanId() {
		return panId;
	}

	public void setPanId(Integer panId) {
		this.panId = panId;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(String boxCount) {
		this.boxCount = boxCount;
	}

	public Integer getPrinted() {
		return printed;
	}

	public void setPrinted(Integer printed) {
		this.printed = printed;
	}

	public Integer getDyed() {
		return dyed;
	}

	public void setDyed(Integer dyed) {
		this.dyed = dyed;
	}

	public Integer getYarnDyed() {
		return yarnDyed;
	}

	public void setYarnDyed(Integer yarnDyed) {
		this.yarnDyed = yarnDyed;
	}

	public Integer getOthers() {
		return others;
	}

	public void setOthers(Integer others) {
		this.others = others;
	}


	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getProductTime() {
		return productTime;
	}

	public void setProductTime(String productTime) {
		this.productTime = productTime;
	}

	public Double getCanPrice() {
		return canPrice;
	}

	public void setCanPrice(Double canPrice) {
		this.canPrice = canPrice;
	}

	public Double getPanPrice() {
		return panPrice;
	}

	public void setPanPrice(Double panPrice) {
		this.panPrice = panPrice;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public String getFillingMaterial() {
		return fillingMaterial;
	}

	public void setFillingMaterial(String fillingMaterial) {
		this.fillingMaterial = fillingMaterial;
	}

	public String getFillingWeight() {
		return fillingWeight;
	}

	public void setFillingWeight(String fillingWeight) {
		this.fillingWeight = fillingWeight;
	}

	public String getPcs2040() {
		return pcs2040;
	}

	public void setPcs2040(String pcs2040) {
		this.pcs2040 = pcs2040;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

}