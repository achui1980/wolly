package com.sail.cot.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * CotStatistics entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotStatistics implements java.io.Serializable {

	// Fields

	private Integer id;
	private String statName;
	private String statType;
	private Integer statParent;
	private String statFile;
	private String statUrl;
	private String statOrderBy;
	private Long statOrder;
	private Long statLv;

	// Constructors

	/** default constructor */
	public CotStatistics() {
	}

	/** full constructor */
	public CotStatistics(String statName, String statType, Integer statParent,
			String statFile, String statUrl, String statOrderBy,
			Long statOrder, Long statLv, Set cotStatPopedoms) {
		this.statName = statName;
		this.statType = statType;
		this.statParent = statParent;
		this.statFile = statFile;
		this.statUrl = statUrl;
		this.statOrderBy = statOrderBy;
		this.statOrder = statOrder;
		this.statLv = statLv;
		
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatName() {
		return this.statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public String getStatType() {
		return this.statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	public Integer getStatParent() {
		return this.statParent;
	}

	public void setStatParent(Integer statParent) {
		this.statParent = statParent;
	}

	public String getStatFile() {
		return this.statFile;
	}

	public void setStatFile(String statFile) {
		this.statFile = statFile;
	}

	public String getStatUrl() {
		return this.statUrl;
	}

	public void setStatUrl(String statUrl) {
		this.statUrl = statUrl;
	}

	public String getStatOrderBy() {
		return this.statOrderBy;
	}

	public void setStatOrderBy(String statOrderBy) {
		this.statOrderBy = statOrderBy;
	}

	public Long getStatOrder() {
		return this.statOrder;
	}

	public void setStatOrder(Long statOrder) {
		this.statOrder = statOrder;
	}

	public Long getStatLv() {
		return this.statLv;
	}

	public void setStatLv(Long statLv) {
		this.statLv = statLv;
	}
}