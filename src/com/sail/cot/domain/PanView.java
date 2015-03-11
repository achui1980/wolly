package com.sail.cot.domain;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;

/**
 * PanViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PanView implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer groupId;
	private Integer category;
	private String eleId;
	private String eleNameEn;
	private String quality;
	private String size;
	private String weight;
	private String filling;
	private String construction;
	private String packing;
	private Integer boxObcount;
	private Double panPrice;
	private Integer currencyId;
	private Double price;
	private Integer ccyId;
	private String productTime;
	private Integer panIdd;//主单id
	private Integer panId;//cotpanele id
	private Integer modPerson;
	private Date modDate;
	private Integer factoryId;
	private Date valDate;
	private Short state;
	
	private String priceNo; //临时字段
	private String remark;

	// Constructors

	/** default constructor */
	public PanView() {
	}
	
	public PanView(PanView obj,String priceNo) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		try {
			BeanUtils.copyProperties(this, obj);
			BeanUtils.copyProperty(this, "priceNo", priceNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PanView(PanView obj,String priceNo,Integer currencyId) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		try {
			BeanUtils.copyProperties(this, obj);
			BeanUtils.copyProperty(this, "priceNo", priceNo);
			BeanUtils.copyProperty(this, "ccyId", currencyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public PanView(CotPanEle ele,CotPanDetail obj,String priceNo) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		try {
			BeanUtils.copyProperties(this, ele);
			BeanUtils.copyProperties(this, obj);
			BeanUtils.copyProperty(this, "priceNo", priceNo);
			BeanUtils.copyProperty(this, "eleId", ele.getEleId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** minimal constructor */
	public PanView(Integer id) {
		this.id = id;
	}

	public String getPriceNo() {
		return priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}

	/** full constructor */
	public PanView(Integer id, Integer groupId, Integer category,
			String eleId, String eleNameEn, String quality, String size,
			String weight, String filling, String construction, String packing,
			Integer boxObcount, Double panPrice, Integer currencyId,
			Double price, Integer ccyId, String productTime, Integer panIdd,
			Integer panId, Integer modPerson, Date modDate, Integer factoryId,
			Date valDate, Short state) {
		this.id = id;
		this.groupId = groupId;
		this.category = category;
		this.eleId = eleId;
		this.eleNameEn = eleNameEn;
		this.quality = quality;
		this.size = size;
		this.weight = weight;
		this.filling = filling;
		this.construction = construction;
		this.packing = packing;
		this.boxObcount = boxObcount;
		this.panPrice = panPrice;
		this.currencyId = currencyId;
		this.price = price;
		this.ccyId = ccyId;
		this.productTime = productTime;
		this.panIdd = panIdd;
		this.panId = panId;
		this.modPerson = modPerson;
		this.modDate = modDate;
		this.factoryId = factoryId;
		this.valDate = valDate;
		this.state = state;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getCategory() {
		return this.category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getEleId() {
		return this.eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getEleNameEn() {
		return this.eleNameEn;
	}

	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}

	public String getQuality() {
		return this.quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getFilling() {
		return this.filling;
	}

	public void setFilling(String filling) {
		this.filling = filling;
	}

	public String getConstruction() {
		return this.construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public String getPacking() {
		return this.packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public Integer getBoxObcount() {
		return this.boxObcount;
	}

	public void setBoxObcount(Integer boxObcount) {
		this.boxObcount = boxObcount;
	}

	public Double getPanPrice() {
		return this.panPrice;
	}

	public void setPanPrice(Double panPrice) {
		this.panPrice = panPrice;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getCcyId() {
		return this.ccyId;
	}

	public void setCcyId(Integer ccyId) {
		this.ccyId = ccyId;
	}

	public String getProductTime() {
		return this.productTime;
	}

	public void setProductTime(String productTime) {
		this.productTime = productTime;
	}

	public Integer getPanIdd() {
		return this.panIdd;
	}

	public void setPanIdd(Integer panIdd) {
		this.panIdd = panIdd;
	}

	public Integer getPanId() {
		return this.panId;
	}

	public void setPanId(Integer panId) {
		this.panId = panId;
	}

	public Integer getModPerson() {
		return this.modPerson;
	}

	public void setModPerson(Integer modPerson) {
		this.modPerson = modPerson;
	}

	public Date getModDate() {
		return this.modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Date getValDate() {
		return this.valDate;
	}

	public void setValDate(Date valDate) {
		this.valDate = valDate;
	}

	public Short getState() {
		return this.state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PanView))
			return false;
		PanView castOther = (PanView) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getGroupId() == castOther.getGroupId()) || (this
						.getGroupId() != null
						&& castOther.getGroupId() != null && this.getGroupId()
						.equals(castOther.getGroupId())))
				&& ((this.getCategory() == castOther.getCategory()) || (this
						.getCategory() != null
						&& castOther.getCategory() != null && this
						.getCategory().equals(castOther.getCategory())))
				&& ((this.getEleId() == castOther.getEleId()) || (this
						.getEleId() != null
						&& castOther.getEleId() != null && this.getEleId()
						.equals(castOther.getEleId())))
				&& ((this.getEleNameEn() == castOther.getEleNameEn()) || (this
						.getEleNameEn() != null
						&& castOther.getEleNameEn() != null && this
						.getEleNameEn().equals(castOther.getEleNameEn())))
				&& ((this.getQuality() == castOther.getQuality()) || (this
						.getQuality() != null
						&& castOther.getQuality() != null && this.getQuality()
						.equals(castOther.getQuality())))
				&& ((this.getSize() == castOther.getSize()) || (this.getSize() != null
						&& castOther.getSize() != null && this.getSize()
						.equals(castOther.getSize())))
				&& ((this.getWeight() == castOther.getWeight()) || (this
						.getWeight() != null
						&& castOther.getWeight() != null && this.getWeight()
						.equals(castOther.getWeight())))
				&& ((this.getFilling() == castOther.getFilling()) || (this
						.getFilling() != null
						&& castOther.getFilling() != null && this.getFilling()
						.equals(castOther.getFilling())))
				&& ((this.getConstruction() == castOther.getConstruction()) || (this
						.getConstruction() != null
						&& castOther.getConstruction() != null && this
						.getConstruction().equals(castOther.getConstruction())))
				&& ((this.getPacking() == castOther.getPacking()) || (this
						.getPacking() != null
						&& castOther.getPacking() != null && this.getPacking()
						.equals(castOther.getPacking())))
				&& ((this.getBoxObcount() == castOther.getBoxObcount()) || (this
						.getBoxObcount() != null
						&& castOther.getBoxObcount() != null && this
						.getBoxObcount().equals(castOther.getBoxObcount())))
				&& ((this.getPanPrice() == castOther.getPanPrice()) || (this
						.getPanPrice() != null
						&& castOther.getPanPrice() != null && this
						.getPanPrice().equals(castOther.getPanPrice())))
				&& ((this.getCurrencyId() == castOther.getCurrencyId()) || (this
						.getCurrencyId() != null
						&& castOther.getCurrencyId() != null && this
						.getCurrencyId().equals(castOther.getCurrencyId())))
				&& ((this.getPrice() == castOther.getPrice()) || (this
						.getPrice() != null
						&& castOther.getPrice() != null && this.getPrice()
						.equals(castOther.getPrice())))
				&& ((this.getCcyId() == castOther.getCcyId()) || (this
						.getCcyId() != null
						&& castOther.getCcyId() != null && this.getCcyId()
						.equals(castOther.getCcyId())))
				&& ((this.getProductTime() == castOther.getProductTime()) || (this
						.getProductTime() != null
						&& castOther.getProductTime() != null && this
						.getProductTime().equals(castOther.getProductTime())))
				&& ((this.getPanIdd() == castOther.getPanIdd()) || (this
						.getPanIdd() != null
						&& castOther.getPanIdd() != null && this.getPanIdd()
						.equals(castOther.getPanIdd())))
				&& ((this.getPanId() == castOther.getPanId()) || (this
						.getPanId() != null
						&& castOther.getPanId() != null && this.getPanId()
						.equals(castOther.getPanId())))
				&& ((this.getModPerson() == castOther.getModPerson()) || (this
						.getModPerson() != null
						&& castOther.getModPerson() != null && this
						.getModPerson().equals(castOther.getModPerson())))
				&& ((this.getModDate() == castOther.getModDate()) || (this
						.getModDate() != null
						&& castOther.getModDate() != null && this.getModDate()
						.equals(castOther.getModDate())))
				&& ((this.getFactoryId() == castOther.getFactoryId()) || (this
						.getFactoryId() != null
						&& castOther.getFactoryId() != null && this
						.getFactoryId().equals(castOther.getFactoryId())))
				&& ((this.getValDate() == castOther.getValDate()) || (this
						.getValDate() != null
						&& castOther.getValDate() != null && this.getValDate()
						.equals(castOther.getValDate())))
				&& ((this.getState() == castOther.getState()) || (this
						.getState() != null
						&& castOther.getState() != null && this.getState()
						.equals(castOther.getState())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getGroupId() == null ? 0 : this.getGroupId().hashCode());
		result = 37 * result
				+ (getCategory() == null ? 0 : this.getCategory().hashCode());
		result = 37 * result
				+ (getEleId() == null ? 0 : this.getEleId().hashCode());
		result = 37 * result
				+ (getEleNameEn() == null ? 0 : this.getEleNameEn().hashCode());
		result = 37 * result
				+ (getQuality() == null ? 0 : this.getQuality().hashCode());
		result = 37 * result
				+ (getSize() == null ? 0 : this.getSize().hashCode());
		result = 37 * result
				+ (getWeight() == null ? 0 : this.getWeight().hashCode());
		result = 37 * result
				+ (getFilling() == null ? 0 : this.getFilling().hashCode());
		result = 37
				* result
				+ (getConstruction() == null ? 0 : this.getConstruction()
						.hashCode());
		result = 37 * result
				+ (getPacking() == null ? 0 : this.getPacking().hashCode());
		result = 37
				* result
				+ (getBoxObcount() == null ? 0 : this.getBoxObcount()
						.hashCode());
		result = 37 * result
				+ (getPanPrice() == null ? 0 : this.getPanPrice().hashCode());
		result = 37
				* result
				+ (getCurrencyId() == null ? 0 : this.getCurrencyId()
						.hashCode());
		result = 37 * result
				+ (getPrice() == null ? 0 : this.getPrice().hashCode());
		result = 37 * result
				+ (getCcyId() == null ? 0 : this.getCcyId().hashCode());
		result = 37
				* result
				+ (getProductTime() == null ? 0 : this.getProductTime()
						.hashCode());
		result = 37 * result
				+ (getPanIdd() == null ? 0 : this.getPanIdd().hashCode());
		result = 37 * result
				+ (getPanId() == null ? 0 : this.getPanId().hashCode());
		result = 37 * result
				+ (getModPerson() == null ? 0 : this.getModPerson().hashCode());
		result = 37 * result
				+ (getModDate() == null ? 0 : this.getModDate().hashCode());
		result = 37 * result
				+ (getFactoryId() == null ? 0 : this.getFactoryId().hashCode());
		result = 37 * result
				+ (getValDate() == null ? 0 : this.getValDate().hashCode());
		result = 37 * result
				+ (getState() == null ? 0 : this.getState().hashCode());
		return result;
	}

}