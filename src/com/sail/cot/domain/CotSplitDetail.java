package com.sail.cot.domain;

/**
 * CotSplitDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSplitDetail implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer splitId;
	private Integer orderDetailId;
	private String eleId;
	private String custNo;
	private String eleNameEn;
	private Long totalBoxCount;
	private Long totalContainerCont;
	private Long containerCount;
	private Long boxCount;
	private String detailRemark;
	private String eleName;
	private String eleSizeDesc;
	private String op;

	// Constructors

	/** default constructor */
	public CotSplitDetail() {
	}

	/** full constructor */
	public CotSplitDetail(CotSplitInfo cotSplitInfo, String eleId,
			String custNo, Long totalBoxCount, Long totalContainerCont,
			Long containerCount, Long boxCount, String detailRemark) {
		this.eleId = eleId;
		this.custNo = custNo;
		this.totalBoxCount = totalBoxCount;
		this.totalContainerCont = totalContainerCont;
		this.containerCount = containerCount;
		this.boxCount = boxCount;
		this.detailRemark = detailRemark;
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

	public String getCustNo() {
		return this.custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public Long getTotalBoxCount() {
		return this.totalBoxCount;
	}

	public void setTotalBoxCount(Long totalBoxCount) {
		this.totalBoxCount = totalBoxCount;
	}

	public Long getTotalContainerCont() {
		return this.totalContainerCont;
	}

	public void setTotalContainerCont(Long totalContainerCont) {
		this.totalContainerCont = totalContainerCont;
	}

	public Long getContainerCount() {
		return this.containerCount;
	}

	public void setContainerCount(Long containerCount) {
		this.containerCount = containerCount;
	}

	public Long getBoxCount() {
		return this.boxCount;
	}

	public void setBoxCount(Long boxCount) {
		this.boxCount = boxCount;
	}

	public String getDetailRemark() {
		return this.detailRemark;
	}

	public void setDetailRemark(String detailRemark) {
		this.detailRemark = detailRemark;
	}

	public Integer getSplitId() {
		return splitId;
	}

	public void setSplitId(Integer splitId) {
		this.splitId = splitId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getEleNameEn() {
		return eleNameEn;
	}

	public void setEleNameEn(String eleNameEn) {
		this.eleNameEn = eleNameEn;
	}

	public String getEleName() {
		return eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public String getEleSizeDesc() {
		return eleSizeDesc;
	}

	public void setEleSizeDesc(String eleSizeDesc) {
		this.eleSizeDesc = eleSizeDesc;
	}

}