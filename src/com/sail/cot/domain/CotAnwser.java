package com.sail.cot.domain;

import java.sql.Timestamp;

/**
 * CotCustSeq entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotAnwser implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer anwserPerson;
	private String anwserText;
	private Timestamp anwserTime;
	private Integer questionId;

	// Constructors

	/** default constructor */
	public CotAnwser() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnwserPerson() {
		return anwserPerson;
	}

	public void setAnwserPerson(Integer anwserPerson) {
		this.anwserPerson = anwserPerson;
	}

	public String getAnwserText() {
		return anwserText;
	}

	public void setAnwserText(String anwserText) {
		this.anwserText = anwserText;
	}

	public Timestamp getAnwserTime() {
		return anwserTime;
	}

	public void setAnwserTime(Timestamp anwserTime) {
		this.anwserTime = anwserTime;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

}