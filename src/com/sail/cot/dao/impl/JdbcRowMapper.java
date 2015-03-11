/**
 * 
 */
package com.sail.cot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sail.cot.domain.vo.CotCustVO;
import com.sail.cot.domain.vo.CotCustomerVO;
import com.sail.cot.domain.vo.CotEleIdCustNo;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.domain.vo.CotNewOrderVO;
import com.sail.cot.domain.vo.CotOrderFacVO;
import com.sail.cot.domain.vo.CotOrderOutVO;
import com.sail.cot.domain.vo.CotOrderVO;
import com.sail.cot.domain.vo.CotSampleVO;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Oct 23, 2008 8:50:59 PM </p>
 * <p>Class Name: JdbcRowMapper.java </p>
 * @author achui
 *
 */
public class JdbcRowMapper implements RowMapper{

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	private String type = "CotElementsVo";
	public JdbcRowMapper()
	{
		
	}
	public Object mapRow(ResultSet rs, int index) throws SQLException {
		// TODO Auto-generated method stub
		if(this.type.equals("CotElementsVo"))
		{
			CotElementsVO vo = new CotElementsVO();
			vo.setId(rs.getInt("id"));
			vo.setEleId(rs.getString("eleId"));
			vo.setEleName(rs.getString("eleName"));
			vo.setEleNameEn(rs.getString("eleNameEn"));
			vo.setEleSizeDesc(rs.getString("eleSizeDesc"));
			vo.setPicPath(rs.getString("picPath"));
			vo.setPrice(rs.getString("price"));
			vo.setCurrenty(rs.getString("currency"));
			
			return vo;
		}
		else if(this.type.equals("CotNewOrderVO"))
		{
			CotNewOrderVO newOrderVO = new CotNewOrderVO();
			newOrderVO.setId(rs.getInt("id"));
			newOrderVO.setEleId(rs.getString("eleId")); 
			newOrderVO.setEleName(rs.getString("eleName"));
			newOrderVO.setEleNameEn(rs.getString("eleNameEn"));
			newOrderVO.setEleSizeDesc(rs.getString("eleSizeDesc"));
			newOrderVO.setOrderTime(rs.getTimestamp("orderTime"));
			return newOrderVO;
		}
		else if(this.type.equals("CotEleIdCustNo"))
		{
			CotEleIdCustNo eleIdCustNo = new CotEleIdCustNo();
			 
			eleIdCustNo.setCustNo(rs.getString("custNo"));
			eleIdCustNo.setEleNameEn(rs.getString("eleNameEn"));
			eleIdCustNo.setCustId(rs.getString("custId"));
			eleIdCustNo.setEleId(rs.getString("eleId"));
			eleIdCustNo.setCustName(rs.getString("custName"));
			
			return eleIdCustNo;
		}else if(this.type.equals("CotSampleVO"))
		{
			CotSampleVO sampleVO = new CotSampleVO();
			sampleVO.setId(rs.getInt("id"));
			sampleVO.setEleId(rs.getString("eleId"));
			sampleVO.setEleName(rs.getString("eleName"));
			sampleVO.setEleNameEn(rs.getString("eleNameEn"));
			sampleVO.setFactoryId(rs.getInt("factoryId"));
			sampleVO.setPriceFac(rs.getFloat("priceFac"));
			sampleVO.setPriceOut(rs.getFloat("priceOut"));
			sampleVO.setPriceFacUint(rs.getInt("priceFacUint"));
			sampleVO.setPriceOutUint(rs.getInt("priceOutUint"));
			sampleVO.setCustNo(rs.getString("custNo"));
			sampleVO.setCustId(rs.getInt("custId"));
			sampleVO.setCid(rs.getInt("cid"));
			
			return sampleVO;
		}
		else if(this.type.equals("CotCustVO"))
		{
			CotCustVO custVO = new CotCustVO();
			custVO.setId(rs.getInt("id"));
			custVO.setCustomerNo(rs.getString("customerNo"));
			custVO.setCustomerShortName(rs.getString("customerShortName"));
			return custVO;
		}
		else if(this.type.equals("CotOrderOutVO"))
		{
			CotOrderOutVO orderVO = new CotOrderOutVO();
			orderVO.setId(rs.getInt("id"));
			orderVO.setOrderNo(rs.getString("orderNo"));
			orderVO.setOrderTime(rs.getDate("orderTime"));
			//orderVO.setReclaimDate(rs.getDate("reclaimDate"));
			orderVO.setOrderStatus(rs.getLong("orderStatus"));
			orderVO.setCustomerShortName(rs.getString("customerShortName"));
			orderVO.setEmpsName(rs.getString("empsName"));
			orderVO.setTotalCount(rs.getInt("totalCount"));
			orderVO.setTotalContainer(rs.getInt("totalContainer"));
			orderVO.setTotalCbm(rs.getFloat("totalCbm"));
			//orderVO.setShipportId(rs.getInt("shipportId"));
			//orderVO.setTargetportId(rs.getInt("targetportId"));
			orderVO.setTrafficId(rs.getInt("trafficId"));
			orderVO.setCustId(rs.getInt("cId"));
			orderVO.setTotalMoney(rs.getFloat("totalMoney"));
			orderVO.setTotalHsMoney(rs.getFloat("totalHsMoney"));
			orderVO.setPaTypeId(rs.getInt("paTypeId"));
			orderVO.setClauseTypeId(rs.getInt("clauseTypeId"));
			orderVO.setOdNo(rs.getString("odNo"));
			orderVO.setTypeLv1Id(rs.getInt("typeLv1Id"));
			orderVO.setPoNo(rs.getString("poNo"));
			orderVO.setAllPinName(rs.getString("allPinName"));
			orderVO.setShortName(rs.getString("shortName"));
			orderVO.setOrderLcDate(rs.getDate("orderLcDate"));
			orderVO.setOrderLcDelay(rs.getDate("orderLcDelay"));
			orderVO.setSendTime(rs.getDate("sendTime"));
			orderVO.setShipportId(rs.getInt("shipportId"));
			orderVO.setTargetportId(rs.getInt("targetportId"));
			orderVO.setTaxTotalMoney(rs.getFloat("taxTotalMoney"));
			orderVO.setCurrencyId(rs.getInt("currencyId"));
			return orderVO;
		}
		else if(this.type.equals("CotCustomerVO"))
		{
			CotCustomerVO custVO = new CotCustomerVO();
			custVO.setId(rs.getInt("id"));
			custVO.setCustomerNo(rs.getString("customerNo"));
			custVO.setCustomerShortName(rs.getString("customerShortName"));
			custVO.setCustTypeId(rs.getInt("custTypeId"));
			custVO.setFullNameEn(rs.getString("fullNameEn"));
			custVO.setFullNameCn(rs.getString("fullNameCn"));
			custVO.setContactNbr(rs.getString("contactNbr"));
			custVO.setCustomerFax(rs.getString("customerFax"));
			custVO.setCustomerEmail(rs.getString("customerEmail"));
			custVO.setPriContact(rs.getString("priContact"));
			custVO.setCustomerAddrEn(rs.getString("customerAddrEn"));
			custVO.setEmpId(rs.getInt("empId"));
			custVO.setNationId(rs.getInt("nationId"));
			custVO.setAddTime(rs.getDate("addTime"));
			return custVO;
		}
		else if(this.type.equals("CotOrderVO"))
		{
			CotOrderVO orderVO = new CotOrderVO();
			orderVO.setId(rs.getInt("id"));
			orderVO.setOrderTime(rs.getTimestamp("orderTime"));
			orderVO.setSendTime(rs.getDate("sendTime"));
			orderVO.setCustomerShortName(rs.getString("customerShortName"));
			orderVO.setOrderNo(rs.getString("orderNo"));
			orderVO.setEmpsName(rs.getString("empsName"));
			orderVO.setClauseTypeId(rs.getInt("clauseTypeId"));
			orderVO.setCurrencyId(rs.getInt("currencyId"));
			orderVO.setPayTypeId(rs.getInt("payTypeId"));
			orderVO.setTotalCount(rs.getInt("totalCount"));
			orderVO.setTotalContainer(rs.getInt("totalContainer"));
			orderVO.setTotalCBM(rs.getDouble("totalCbm"));
			orderVO.setTotalMoney(rs.getFloat("totalMoney"));
			orderVO.setOrderStatus(rs.getLong("orderStatus"));
			orderVO.setPoNo(rs.getString("poNo"));
			orderVO.setOrderRate(rs.getDouble("orderRate"));
			orderVO.setOrderCompose(rs.getString("orderCompose"));
			orderVO.setTotalGross(rs.getDouble("totalGross"));
			orderVO.setCustId(rs.getInt("cId"));
			orderVO.setAllPinName(rs.getString("allPinName"));
			orderVO.setOrderLcDate(rs.getDate("orderLcDate"));
			orderVO.setOrderLcDelay(rs.getDate("orderLcDelay"));
			orderVO.setOrderEarnest(rs.getDouble("orderEarnest"));
			orderVO.setFactoryId(rs.getInt("factoryId"));
			orderVO.setCanOut(rs.getInt("canOut"));
			orderVO.setFtm(rs.getFloat("ftm"));
			orderVO.setTypeLv1Id(rs.getInt("typeLv1Id"));
			orderVO.setShipportId(rs.getInt("shipportId"));
			orderVO.setTargetportId(rs.getInt("targetportId"));
			orderVO.setNewRemark(rs.getString("newRemark"));
			
			return orderVO;
		}
		else if(this.type.equals("CotOrderFacVO"))
		{
			CotOrderFacVO orderVO = new CotOrderFacVO();
			orderVO.setId(rs.getInt("id"));
			orderVO.setFactoryId(rs.getInt("factoryId"));
			orderVO.setOrderNo(rs.getString("orderNo"));
			orderVO.setCurrencyId(rs.getInt("currencyId"));
			orderVO.setOrderTime(rs.getDate("orderTime"));
			orderVO.setSendTime(rs.getDate("sendTime"));
			orderVO.setBusinessPerson(rs.getInt("businessPerson"));
			orderVO.setTotalMoney(rs.getDouble("totalMoney"));
			orderVO.setOrderStatus(rs.getInt("orderStatus"));
			orderVO.setOrderId(rs.getInt("orderId"));
			orderVO.setPoNo(rs.getString("poNo"));
			orderVO.setAllPinName(rs.getString("allPinName"));
			orderVO.setShipmentDate(rs.getDate("shipmentDate"));
			orderVO.setPayTypeId(rs.getInt("payTypeId"));
			orderVO.setClauseTypeId(rs.getInt("clauseTypeId"));
			orderVO.setOrderLcDelay(rs.getDate("orderLcDelay"));
			orderVO.setTypeLv1Id(rs.getInt("typeLv1Id"));
			orderVO.setNewRemark(rs.getString("newRemark"));
			orderVO.setPiStatus(rs.getInt("piStatus"));
			orderVO.setCheckPerson(rs.getInt("checkPerson"));
			return orderVO;
		}
		return null;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
