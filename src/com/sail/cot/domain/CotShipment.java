package com.sail.cot.domain;

import java.util.Date;

/**
 * CotShipment entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotShipment implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer orderId;
	private Integer consignCompanyId;
	private Integer consignCompanyIdReal;
	private String consignNo;
	private Date consignDate;
	private Date validDate;
	private Date shipDate;
	private String shipRemark;
	private String trailerRemark;
	private String touchRemark;
	private String touchNo;
	private String shipName;
	private String shipSchedule;
	private String shipLine;
	private Date startDate;
	private Date arriveDate;
	private String shipTotalName;
	private String shipTotalCount;

	// Constructors

	/** default constructor */
	public CotShipment() {
	}

	/** minimal constructor */
	public CotShipment(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotShipment(Integer id, CotOrderOut cotOrderOut,
			CotConsignCompany cotConsignCompany, String consignNo,
			Date consignDate, Date validDate, Date shipDate, String shipRemark,
			String trailerRemark, String touchRemark, String touchNo,
			String shipName, String shipSchedule, String shipLine,
			Date startDate, Date arriveDate, String shipTotalName, String shipTotalCount) {
		this.id = id;
		this.consignNo = consignNo;
		this.consignDate = consignDate;
		this.validDate = validDate;
		this.shipDate = shipDate;
		this.shipRemark = shipRemark;
		this.trailerRemark = trailerRemark;
		this.touchRemark = touchRemark;
		this.touchNo = touchNo;
		this.shipName = shipName;
		this.shipSchedule = shipSchedule;
		this.shipLine = shipLine;
		this.startDate = startDate;
		this.arriveDate = arriveDate;
		this.shipTotalName = shipTotalName;
		this.shipTotalCount = shipTotalCount;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConsignNo() {
		return this.consignNo;
	}

	public void setConsignNo(String consignNo) {
		this.consignNo = consignNo;
	}

	public Date getConsignDate() {
		return this.consignDate;
	}

	public void setConsignDate(Date consignDate) {
		this.consignDate = consignDate;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Date getShipDate() {
		return this.shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public String getShipRemark() {
		return this.shipRemark;
	}

	public void setShipRemark(String shipRemark) {
		this.shipRemark = shipRemark;
	}

	public String getTrailerRemark() {
		return this.trailerRemark;
	}

	public void setTrailerRemark(String trailerRemark) {
		this.trailerRemark = trailerRemark;
	}

	public String getTouchRemark() {
		return this.touchRemark;
	}

	public void setTouchRemark(String touchRemark) {
		this.touchRemark = touchRemark;
	}

	public String getTouchNo() {
		return this.touchNo;
	}

	public void setTouchNo(String touchNo) {
		this.touchNo = touchNo;
	}

	public String getShipName() {
		return this.shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShipSchedule() {
		return this.shipSchedule;
	}

	public void setShipSchedule(String shipSchedule) {
		this.shipSchedule = shipSchedule;
	}

	public String getShipLine() {
		return this.shipLine;
	}

	public void setShipLine(String shipLine) {
		this.shipLine = shipLine;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getArriveDate() {
		return this.arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getShipTotalName() {
		return shipTotalName;
	}

	public void setShipTotalName(String shipTotalName) {
		this.shipTotalName = shipTotalName;
	}

	public String getShipTotalCount() {
		return shipTotalCount;
	}

	public void setShipTotalCount(String shipTotalCount) {
		this.shipTotalCount = shipTotalCount;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getConsignCompanyId() {
		return consignCompanyId;
	}

	public void setConsignCompanyId(Integer consignCompanyId) {
		this.consignCompanyId = consignCompanyId;
	}

	public Integer getConsignCompanyIdReal() {
		return consignCompanyIdReal;
	}

	public void setConsignCompanyIdReal(Integer consignCompanyIdReal) {
		this.consignCompanyIdReal = consignCompanyIdReal;
	}

}