package com.sail.cot.domain;

import java.util.Date;

/**
 * CotCustSeq entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotCustSeq implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer custId;
	private Integer priceSeq;
	private Integer orderSeq;
	private Integer givenSeq;
	private Integer signSeq;
	private Integer orderfacSeq;
	private Integer orderoutSeq;
	private Integer splitSeq;
	private Integer finacerecvSeq;
	private Date currDate;
	private Integer finacegivenSeq;
	private Integer fincaeaccountdealSeq;
	private Integer fincaeaccountrecvSeq;
	private Integer backtaxSeq;
	private Integer auditSeq;
	private Integer packingSeq;
	private Integer accessSeq;

	// Constructors

	/** default constructor */
	public CotCustSeq() {
	}

	/** minimal constructor */
	public CotCustSeq(Integer custId) {
		this.custId = custId;
	}

	/** full constructor */
	public CotCustSeq(Integer custId, Integer priceSeq, Integer orderSeq,
			Integer givenSeq, Integer signSeq, Integer orderfacSeq,
			Integer orderoutSeq, Integer splitSeq, Integer finacerecvSeq,
			Date currDate, Integer finacegivenSeq,
			Integer fincaeaccountdealSeq, Integer fincaeaccountrecvSeq,
			Integer backtaxSeq, Integer auditSeq, Integer packingSeq,
			Integer accessSeq) {
		this.custId = custId;
		this.priceSeq = priceSeq;
		this.orderSeq = orderSeq;
		this.givenSeq = givenSeq;
		this.signSeq = signSeq;
		this.orderfacSeq = orderfacSeq;
		this.orderoutSeq = orderoutSeq;
		this.splitSeq = splitSeq;
		this.finacerecvSeq = finacerecvSeq;
		this.currDate = currDate;
		this.finacegivenSeq = finacegivenSeq;
		this.fincaeaccountdealSeq = fincaeaccountdealSeq;
		this.fincaeaccountrecvSeq = fincaeaccountrecvSeq;
		this.backtaxSeq = backtaxSeq;
		this.auditSeq = auditSeq;
		this.packingSeq = packingSeq;
		this.accessSeq = accessSeq;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustId() {
		return this.custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getPriceSeq() {
		return this.priceSeq;
	}

	public void setPriceSeq(Integer priceSeq) {
		this.priceSeq = priceSeq;
	}

	public Integer getOrderSeq() {
		return this.orderSeq;
	}

	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}

	public Integer getGivenSeq() {
		return this.givenSeq;
	}

	public void setGivenSeq(Integer givenSeq) {
		this.givenSeq = givenSeq;
	}

	public Integer getSignSeq() {
		return this.signSeq;
	}

	public void setSignSeq(Integer signSeq) {
		this.signSeq = signSeq;
	}

	public Integer getOrderfacSeq() {
		return this.orderfacSeq;
	}

	public void setOrderfacSeq(Integer orderfacSeq) {
		this.orderfacSeq = orderfacSeq;
	}

	public Integer getOrderoutSeq() {
		return this.orderoutSeq;
	}

	public void setOrderoutSeq(Integer orderoutSeq) {
		this.orderoutSeq = orderoutSeq;
	}

	public Integer getSplitSeq() {
		return this.splitSeq;
	}

	public void setSplitSeq(Integer splitSeq) {
		this.splitSeq = splitSeq;
	}

	public Integer getFinacerecvSeq() {
		return this.finacerecvSeq;
	}

	public void setFinacerecvSeq(Integer finacerecvSeq) {
		this.finacerecvSeq = finacerecvSeq;
	}

	public Date getCurrDate() {
		return this.currDate;
	}

	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}

	public Integer getFinacegivenSeq() {
		return this.finacegivenSeq;
	}

	public void setFinacegivenSeq(Integer finacegivenSeq) {
		this.finacegivenSeq = finacegivenSeq;
	}

	public Integer getFincaeaccountdealSeq() {
		return this.fincaeaccountdealSeq;
	}

	public void setFincaeaccountdealSeq(Integer fincaeaccountdealSeq) {
		this.fincaeaccountdealSeq = fincaeaccountdealSeq;
	}

	public Integer getFincaeaccountrecvSeq() {
		return this.fincaeaccountrecvSeq;
	}

	public void setFincaeaccountrecvSeq(Integer fincaeaccountrecvSeq) {
		this.fincaeaccountrecvSeq = fincaeaccountrecvSeq;
	}

	public Integer getBacktaxSeq() {
		return this.backtaxSeq;
	}

	public void setBacktaxSeq(Integer backtaxSeq) {
		this.backtaxSeq = backtaxSeq;
	}

	public Integer getAuditSeq() {
		return this.auditSeq;
	}

	public void setAuditSeq(Integer auditSeq) {
		this.auditSeq = auditSeq;
	}

	public Integer getPackingSeq() {
		return this.packingSeq;
	}

	public void setPackingSeq(Integer packingSeq) {
		this.packingSeq = packingSeq;
	}

	public Integer getAccessSeq() {
		return this.accessSeq;
	}

	public void setAccessSeq(Integer accessSeq) {
		this.accessSeq = accessSeq;
	}

}