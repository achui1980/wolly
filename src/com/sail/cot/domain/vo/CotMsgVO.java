package com.sail.cot.domain.vo;

public class CotMsgVO {
	
	private Integer rowNum;//行号
	private Integer colNum;//列号
	private Integer successNum;//成功条数
	private Integer failNum;// 失败条数
	private Integer coverNum;// 覆盖条数
	private Integer flag;// 错误级别
	private String msg;//操作信息
	
	public CotMsgVO(){
	}
	public CotMsgVO(Integer r,Integer c,String m){
		this.rowNum=r;
		this.colNum=c;
		this.msg=m;
	}
	
	public Integer getRowNum() {
		return rowNum;
	}
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	public Integer getColNum() {
		return colNum;
	}
	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(Integer successNum) {
		this.successNum = successNum;
	}

	public Integer getFailNum() {
		return failNum;
	}

	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getCoverNum() {
		return coverNum;
	}
	public void setCoverNum(Integer coverNum) {
		this.coverNum = coverNum;
	}
}
