package com.sail.cot.domain;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.lang.RandomStringUtils;

/**
 * PanPrice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CotPan implements java.io.Serializable {

	// Fields

	private Date valDate;//有效期
	private Integer urgency;//紧急程度(1:普通;2:中等;3紧急)
	private Integer empId;//业务员
	private Integer status;//状态(丹麦刚刚做完 主单灰色 只要有一条被提交供应商邮件 主单变黄色(1)  有一条被confirm主单变绿色(2))
	private Integer typePan;//是那种格式 0或null:TEXTILE  1:HARDWARE
	private Integer currencyId;//目标价和供应商报价的币种一致
	private Integer facId;//临时字段 (用于quation主界面)
	private Integer panId;//临时字段 (用于quation主界面)
	private String customer;//客户
	
	//*制单人
	private Integer addPerson;//制单人
	private Integer id;
	//*主题
	private String priceNo;//title
	//*询盘日期
	private Date addTime;//询盘日期
	//*部门
	private Integer deptId;
	//*客户
	private String customerStr;
	//*意向供应商
	private String factoryStr;
	//*相关订单号
	private String orderNo;
	//*备注
	private String remark;//备注
	
	//*询盘单号
	private String prsNo;//询盘单号

	// Constructors

	/** default constructor */
	public CotPan() {
	}
	
	public CotPan(CotPan obj,Integer facId) {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		try {
			BeanUtils.copyProperties(this, obj);
			String rdm = "1" + RandomStringUtils.randomNumeric(8);
			BeanUtils.copyProperty(this, "id", rdm);
			BeanUtils.copyProperty(this, "facId", facId);
			BeanUtils.copyProperty(this, "panId", obj.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getUrgency() {
		return urgency;
	}

	public Integer getPanId() {
		return panId;
	}

	public String getPrsNo() {
		return prsNo;
	}

	public void setPrsNo(String prsNo) {
		this.prsNo = prsNo;
	}

	public String getCustomer() {
		return customer;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getCustomerStr() {
		return customerStr;
	}

	public void setCustomerStr(String customerStr) {
		this.customerStr = customerStr;
	}

	public String getFactoryStr() {
		return factoryStr;
	}

	public void setFactoryStr(String factoryStr) {
		this.factoryStr = factoryStr;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public void setPanId(Integer panId) {
		this.panId = panId;
	}

	public Integer getFacId() {
		return facId;
	}

	public void setFacId(Integer facId) {
		this.facId = facId;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getTypePan() {
		return typePan;
	}

	public void setTypePan(Integer typePan) {
		this.typePan = typePan;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getValDate() {
		return valDate;
	}

	public void setValDate(Date valDate) {
		this.valDate = valDate;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getAddPerson() {
		return this.addPerson;
	}

	public void setAddPerson(Integer addPerson) {
		this.addPerson = addPerson;
	}

	public String getPriceNo() {
		return this.priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}

	public Integer getEmpId() {
		return this.empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

//	public Short getShenState() {
//		return this.shenState;
//	}
//
//	public void setShenState(Short shenState) {
//		this.shenState = shenState;
//	}
//
//	public Date getShenTime() {
//		return this.shenTime;
//	}
//
//	public void setShenTime(Date shenTime) {
//		this.shenTime = shenTime;
//	}
//
//	public Integer getShenPerson() {
//		return this.shenPerson;
//	}
//
//	public void setShenPerson(Integer shenPerson) {
//		this.shenPerson = shenPerson;
//	}

}