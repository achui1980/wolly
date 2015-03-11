package com.sail.cot.domain;

/**
 * CotPackingAnys entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPackingAnys implements java.io.Serializable {

	// Fields

	private Integer id;
	private String eleId;//货号
	private String eleName;//样品中文名称
	private String custNo;//客号
	private Long orderCount;//订单数量
	private Long containerCount;//订单箱数
	private Long boxPbCount;//产品装数
	private Long boxIbCount;//内装数
	private Long boxMbCount;//中装数
	private Long boxObCount;//外装数
	private String boxRemark;//包装说明
	private Integer boxTypeId;//产品0/内1/中2/外3 包材分析
	private Integer boxPackingId;//包装材料
	private Long packCount;//包材数量(订单数量/内装数)
	private Double packPrice;//单价(根据boxPackingId找CotBoxPacking中的materialPrice)
	private Double sizeL;//订单明细中对应的内中外包装尺寸
	private Double sizeW;
	private Double sizeH;
	private Integer factoryId;//厂家
	private Integer orderId;//订单编号
	private Integer orderdetailId;//订单明细编号
	private String anyFlag;//是否已生成采购单
	private Integer packingOrderid;//采购主单编号
	private Integer packingdetailId;//采购明细编号
	
	private String packRemark; //备注
	private Integer flag;//标识是否是订单生成后新添加的(0否,1是)
	
	private Double totalAmount;//总金额  (临时字段,数据库不保存)

	// Constructors

	/** default constructor */
	public CotPackingAnys() {
	}

	/** minimal constructor */
	public CotPackingAnys(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotPackingAnys(Integer id, String eleId, String eleName,
			String custNo, Long orderCount, Long boxIbCount, Long boxMbCount,
			Long boxObCount, String boxRemark, Integer boxTypeId,
			Integer boxPackingId, Long packCount, Double packPrice,
			Double sizeL, Double sizeW, Double sizeH, Integer factoryId,
			Integer orderId, Integer orderdetailId, String anyFlag,
			Integer packingOrderid, Integer packingdetailId) {
		this.id = id;
		this.eleId = eleId;
		this.eleName = eleName;
		this.custNo = custNo;
		this.orderCount = orderCount;
		this.boxIbCount = boxIbCount;
		this.boxMbCount = boxMbCount;
		this.boxObCount = boxObCount;
		this.boxRemark = boxRemark;
		this.boxTypeId = boxTypeId;
		this.boxPackingId = boxPackingId;
		this.packCount = packCount;
		this.packPrice = packPrice;
		this.sizeL = sizeL;
		this.sizeW = sizeW;
		this.sizeH = sizeH;
		this.factoryId = factoryId;
		this.orderId = orderId;
		this.orderdetailId = orderdetailId;
		this.anyFlag = anyFlag;
		this.packingOrderid = packingOrderid;
		this.packingdetailId = packingdetailId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCustNo() {
		return this.custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public Long getOrderCount() {
		return this.orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public Long getBoxIbCount() {
		return this.boxIbCount;
	}

	public void setBoxIbCount(Long boxIbCount) {
		this.boxIbCount = boxIbCount;
	}

	public Long getBoxMbCount() {
		return this.boxMbCount;
	}

	public void setBoxMbCount(Long boxMbCount) {
		this.boxMbCount = boxMbCount;
	}

	public Long getBoxObCount() {
		return this.boxObCount;
	}

	public void setBoxObCount(Long boxObCount) {
		this.boxObCount = boxObCount;
	}

	public String getBoxRemark() {
		return this.boxRemark;
	}

	public void setBoxRemark(String boxRemark) {
		this.boxRemark = boxRemark;
	}

	public Integer getBoxTypeId() {
		return this.boxTypeId;
	}

	public void setBoxTypeId(Integer boxTypeId) {
		this.boxTypeId = boxTypeId;
	}

	public Integer getBoxPackingId() {
		return this.boxPackingId;
	}

	public void setBoxPackingId(Integer boxPackingId) {
		this.boxPackingId = boxPackingId;
	}

	public Long getPackCount() {
		return this.packCount;
	}

	public void setPackCount(Long packCount) {
		this.packCount = packCount;
	}

	public Double getPackPrice() {
		return this.packPrice;
	}

	public void setPackPrice(Double packPrice) {
		this.packPrice = packPrice;
	}

	public Double getSizeL() {
		return this.sizeL;
	}

	public void setSizeL(Double sizeL) {
		this.sizeL = sizeL;
	}

	public Double getSizeW() {
		return this.sizeW;
	}

	public void setSizeW(Double sizeW) {
		this.sizeW = sizeW;
	}

	public Double getSizeH() {
		return this.sizeH;
	}

	public void setSizeH(Double sizeH) {
		this.sizeH = sizeH;
	}

	public Integer getFactoryId() {
		return this.factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderdetailId() {
		return this.orderdetailId;
	}

	public void setOrderdetailId(Integer orderdetailId) {
		this.orderdetailId = orderdetailId;
	}

	public String getAnyFlag() {
		return this.anyFlag;
	}

	public void setAnyFlag(String anyFlag) {
		this.anyFlag = anyFlag;
	}

	public Integer getPackingOrderid() {
		return this.packingOrderid;
	}

	public void setPackingOrderid(Integer packingOrderid) {
		this.packingOrderid = packingOrderid;
	}

	public Integer getPackingdetailId() {
		return this.packingdetailId;
	}

	public void setPackingdetailId(Integer packingdetailId) {
		this.packingdetailId = packingdetailId;
	}

	public String getPackRemark() {
		return packRemark;
	}

	public void setPackRemark(String packRemark) {
		this.packRemark = packRemark;
	}

	public Long getBoxPbCount() {
		return boxPbCount;
	}

	public void setBoxPbCount(Long boxPbCount) {
		this.boxPbCount = boxPbCount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Long getContainerCount() {
		return containerCount;
	}

	public void setContainerCount(Long containerCount) {
		this.containerCount = containerCount;
	}

}