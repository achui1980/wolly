package com.sail.cot.domain;

/**
 * CotFinaceOther entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotFinaceOther implements java.io.Serializable {

	// Fields

	private Integer id;
	private String finaceName;// 费用名称
	private Double amount;// 金额
	private Integer currencyId;// 币种
	private Integer type;// 其他费用类型 0:应收 1：应付
	private String flag;// 费用加减项 'M':减项 'A'：加项
	private String source;// 金额源来 如："price","order","orderfac","sign"等
	private Double prePrice;// 预收货款金额
	private Long priceScal;// 预收货款比例
	private String orderNo;// 单号，可以是订单号，生产合同号
	private Integer fkId;// 对应来源的 外键ID
	private Integer businessPerson;// 业务员
	private Integer status;// 否是已生成货款 0：未生成 1：已生成
	private Integer isImport;// 否是已导过 0：未导过 1：已导过
	private Integer outFlag;// 订单导到出货的标识(0,null:未导入 1:导入)
	private String op;
	private Integer factoryId;//厂家id/客户id (用于溢付和溢收)
	private Double remainAmount;//剩余金额

	// Constructors

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	/** default constructor */
	public CotFinaceOther() {
	}

	/** full constructor */
	public CotFinaceOther(String finaceName, Double amount, Integer currencyId,
			Integer type, String flag, String source, Double prePrice,
			Long priceScal, String orderNo, Integer fkId,
			Integer businessPerson, Integer status) {
		this.finaceName = finaceName;
		this.amount = amount;
		this.currencyId = currencyId;
		this.type = type;
		this.flag = flag;
		this.source = source;
		this.prePrice = prePrice;
		this.priceScal = priceScal;
		this.orderNo = orderNo;
		this.fkId = fkId;
		this.businessPerson = businessPerson;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFinaceName() {
		return this.finaceName;
	}

	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getPrePrice() {
		return this.prePrice;
	}

	public void setPrePrice(Double prePrice) {
		this.prePrice = prePrice;
	}

	public Long getPriceScal() {
		return this.priceScal;
	}

	public void setPriceScal(Long priceScal) {
		this.priceScal = priceScal;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getFkId() {
		return this.fkId;
	}

	public void setFkId(Integer fkId) {
		this.fkId = fkId;
	}

	public Integer getBusinessPerson() {
		return this.businessPerson;
	}

	public void setBusinessPerson(Integer businessPerson) {
		this.businessPerson = businessPerson;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsImport() {
		return isImport;
	}

	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Integer getOutFlag() {
		return outFlag;
	}

	public void setOutFlag(Integer outFlag) {
		this.outFlag = outFlag;
	}

	public Double getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(Double remainAmount) {
		this.remainAmount = remainAmount;
	}
}