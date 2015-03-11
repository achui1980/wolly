package com.sail.cot.domain;

/**
 * CotModule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotModule implements java.io.Serializable {

	// Fields

	private Integer id;
	private String moduleName;
	private String moduleUrl;
	private String moduleImgurl;
	private Integer moudleParent;
	private Long moduleLv;
	private String moudleType;
	private Long moudleOrder;
	private String moduleValidUrl;
	private String moduleFlag;

	// Constructors

	public String getModuleFlag() {
		return moduleFlag;
	}

	public void setModuleFlag(String moduleFlag) {
		this.moduleFlag = moduleFlag;
	}

	public String getModuleValidUrl() {
		return moduleValidUrl;
	}

	public void setModuleValidUrl(String moduleValidUrl) {
		this.moduleValidUrl = moduleValidUrl;
	}

	/** default constructor */
	public CotModule() {
	}

	/** minimal constructor */
	public CotModule(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotModule(Integer id, String moduleName, String moduleUrl,
			String moduleImgurl, Integer moudleParent, Long moduleLv,
			String moudleType, Long moudleOrder) {
		this.id = id;
		this.moduleName = moduleName;
		this.moduleUrl = moduleUrl;
		this.moduleImgurl = moduleImgurl;
		this.moudleParent = moudleParent;
		this.moduleLv = moduleLv;
		this.moudleType = moudleType;
		this.moudleOrder = moudleOrder;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleUrl() {
		return this.moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public String getModuleImgurl() {
		return this.moduleImgurl;
	}

	public void setModuleImgurl(String moduleImgurl) {
		this.moduleImgurl = moduleImgurl;
	}

	public Integer getMoudleParent() {
		return this.moudleParent;
	}

	public void setMoudleParent(Integer moudleParent) {
		this.moudleParent = moudleParent;
	}

	public Long getModuleLv() {
		return this.moduleLv;
	}

	public void setModuleLv(Long moduleLv) {
		this.moduleLv = moduleLv;
	}

	public String getMoudleType() {
		return this.moudleType;
	}

	public void setMoudleType(String moudleType) {
		this.moudleType = moudleType;
	}

	public Long getMoudleOrder() {
		return this.moudleOrder;
	}

	public void setMoudleOrder(Long moudleOrder) {
		this.moudleOrder = moudleOrder;
	}

}