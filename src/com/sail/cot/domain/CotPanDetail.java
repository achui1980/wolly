package com.sail.cot.domain;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;


/**
 * PanPriceDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPanDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer factoryId;//供应商id
	private String weight;//面料克重
	private String filling;//充棉克数
	private String construction;//面料成分
	private String packing;//包装描述
	private Integer boxObcount;//外箱装率
	private Double price;//供应商报价
	private Integer ccyId;//供应商报价币种
	private Date valDate;//有效期
	private Integer panId;//询盘明细id
	private Date modDate;//最后修改时间
	private Integer modPerson;//最后修改人
	private Integer state;//状态([0, '未发邮件'], [1, '已发邮件'],[3, '供应商确认'])
	private String productTime;//生产周期
	private Integer hideStatus;//状态(0:Running;1:hide)
	
	private String eleId;//临时字段
	private String remark;
	private String carton;//CBM PER OUTER CARTON
	
	private String willSupplier;//意向供应商
	private String fileUrl;
	private Integer uploadEmp;

	private String eleNameEn;//临时字段
	private String priceNo;//临时字段
	private String manufactorer;//临时字段
	// Constructors

	/** default constructor */
	public CotPanDetail() {
	}
	
	public CotPanDetail(CotPanDetail obj,String eleId) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		try {
			BeanUtils.copyProperties(this, obj);
			BeanUtils.copyProperty(this, "eleId", eleId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getEleNameEn() {
		return eleNameEn;
	}

	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}

	public CotPanDetail(CotPanDetail obj,String eleNameEn,Integer state,String priceNo,String manufactorer) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		try {
			BeanUtils.copyProperties(this, obj);
			BeanUtils.copyProperty(this, "eleNameEn", eleNameEn);
			BeanUtils.copyProperty(this, "state", state);
			BeanUtils.copyProperty(this, "priceNo", priceNo);
			BeanUtils.copyProperty(this, "manufactorer", manufactorer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public Integer getHideStatus() {
		return hideStatus;
	}

	public String getManufactorer() {
		return manufactorer;
	}

	public void setManufactorer(String manufactorer) {
		this.manufactorer = manufactorer;
	}

	public void setHideStatus(Integer hideStatus) {
		this.hideStatus = hideStatus;
	}

	public Integer getId() {
		return id;
	}

	public String getRemark() {
		return remark;
	}

	public String getPriceNo() {
		return priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Integer getModPerson() {
		return modPerson;
	}

	public void setModPerson(Integer modPerson) {
		this.modPerson = modPerson;
	}


	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getFilling() {
		return filling;
	}

	public void setFilling(String filling) {
		this.filling = filling;
	}

	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public Integer getBoxObcount() {
		return boxObcount;
	}

	public void setBoxObcount(Integer boxObcount) {
		this.boxObcount = boxObcount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getCcyId() {
		return ccyId;
	}

	public void setCcyId(Integer ccyId) {
		this.ccyId = ccyId;
	}

	public Integer getPanId() {
		return panId;
	}

	public void setPanId(Integer panId) {
		this.panId = panId;
	}

	public Date getValDate() {
		return valDate;
	}

	public void setValDate(Date valDate) {
		this.valDate = valDate;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getProductTime() {
		return productTime;
	}

	public void setProductTime(String productTime) {
		this.productTime = productTime;
	}

	public String getWillSupplier() {
		return willSupplier;
	}

	public void setWillSupplier(String willSupplier) {
		this.willSupplier = willSupplier;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getUploadEmp() {
		return uploadEmp;
	}

	public void setUploadEmp(Integer uploadEmp) {
		this.uploadEmp = uploadEmp;
	}
}