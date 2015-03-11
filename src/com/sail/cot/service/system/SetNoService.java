package com.sail.cot.service.system;

import java.util.Map;

import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.vo.CotCfgNo;

public interface SetNoService {
	
	public void saveNo(CotCfgNo cotCfgNo);
	
	public CotCfgNo getNoMap();
	
	//归零
	public void toZero(String seqName);
	
	//获取报价单号
	public String getPriceNo(Integer custId,String currdate);
	
	//获取订单号
	public String getOrderNo(Integer custId,String currdate);
	
	//获取送样单号
	public String getGivenNo(Integer custId,String currdate);
	
	//获取征样单号
	public String getSignNo(String givenNo,Integer factoryId,String currdate);
	
	//获取生产合同单号
	public String getOrderFacNo(Integer factoryId,String currdate);
	
	//获取出货单号
	public String getOrderOutNo(Integer custId,String currdate);
	
	//获取排载单号
	public String getContainerNo(String currdate);
	
	//获取自动分解单号
	public String getAutoOrderFacNo(String orderNo,Integer factoryId,String currdate);
	
	//public Integer updateCustSeq(CotCustomer customer,String abstractNo);

	//获取自动分解的采购单号
	public String getCotOrderfacNO(Integer orderId,Integer factoryId);
}
