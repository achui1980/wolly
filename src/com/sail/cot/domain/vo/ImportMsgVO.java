package com.sail.cot.domain.vo;

public class ImportMsgVO {

	private Integer coverSum;//覆盖条数
	private Integer addSum;// 新增条数
	private Integer crossSum;// 跳过条数
	private String crossMsg;//跳过的信息
	
	public String getCrossMsg() {
		return crossMsg;
	}
	public void setCrossMsg(String crossMsg) {
		this.crossMsg = crossMsg;
	}
	public ImportMsgVO(){
	}
	public ImportMsgVO(Integer coverSum,Integer addSum, Integer crossSum){
		this.coverSum=coverSum;
		this.addSum=addSum;
		this.crossSum=crossSum;
	}
	public Integer getCoverSum() {
		return coverSum;
	}
	public void setCoverSum(Integer coverSum) {
		this.coverSum = coverSum;
	}
	public Integer getAddSum() {
		return addSum;
	}
	public void setAddSum(Integer addSum) {
		this.addSum = addSum;
	}
	public Integer getCrossSum() {
		return crossSum;
	}
	public void setCrossSum(Integer crossSum) {
		this.crossSum = crossSum;
	}
}
