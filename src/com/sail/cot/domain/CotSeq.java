package com.sail.cot.domain;

import java.util.Date;

/**
 * CotSeq entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotSeq implements java.io.Serializable {

	// Fields

	private Integer id;
	private String seqCfg;//单号配置表达式
	private Integer currentSeq;//当前序列号
	private Integer zeroType;//归零方式 1:系统，2：按年，3：按月，4：按日
	private Date currentDay;
	private Date hisDay;
	private String type;//单号类型：order,price,given
	private String name;//单号名称：订单单号，报价单号等

	// Constructors

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/** default constructor */
	public CotSeq() {
	}

	/** minimal constructor */
	public CotSeq(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public CotSeq(Integer id, String seqCfg, Integer currentSeq,
			Integer zeroType, Date currentDay, Date hisDay) {
		this.id = id;
		this.seqCfg = seqCfg;
		this.currentSeq = currentSeq;
		this.zeroType = zeroType;
		this.currentDay = currentDay;
		this.hisDay = hisDay;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSeqCfg() {
		return this.seqCfg;
	}

	public void setSeqCfg(String seqCfg) {
		this.seqCfg = seqCfg;
	}

	public Integer getCurrentSeq() {
		return this.currentSeq;
	}

	public void setCurrentSeq(Integer currentSeq) {
		this.currentSeq = currentSeq;
	}

	public Integer getZeroType() {
		return this.zeroType;
	}

	public void setZeroType(Integer zeroType) {
		this.zeroType = zeroType;
	}

	public Date getCurrentDay() {
		return this.currentDay;
	}

	public void setCurrentDay(Date currentDay) {
		this.currentDay = currentDay;
	}

	public Date getHisDay() {
		return this.hisDay;
	}

	public void setHisDay(Date hisDay) {
		this.hisDay = hisDay;
	}

}