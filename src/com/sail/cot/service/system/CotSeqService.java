
package com.sail.cot.service.system;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotSeq;
import com.sail.cot.query.QueryInfo;

 
public interface CotSeqService {
	
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public void saveOrUpdateList(List records);
	
	public CotSeq getCotSeq(Integer id);
	
	public void delCotSeq (List ids) throws DAOException;
	
	//归零
	public int zero(String id)throws DAOException;
	
	//获取客户单号
	public String getCustNo();
	
	//获取工厂单号
	public String getFacNo();
	
	//获取外销合同单号
	public String getOrderNo(Integer custId,Integer empId,String currdate,Integer companyId);
	
	//获取样品货号
	public String getEleNo(Integer byId,Integer myId,Integer syId,Integer facId);
	
	//获取报价单单号
	public String getPriceNo(Integer custId,Integer empId,String currdate,Integer companyId);
	
	//获取送样单号
	public String getGivenNo(Integer custId,Integer empId,String currdate);
	
	//获取征样单号
	public String getSignNo(Integer custId,Integer empId,Integer factoryId ,String currdate);
	
	//获取生产合同单号
	public String getOrderFacNo(Integer factoryId,Integer empId,String currdate);
	
	//获取出货单号
	public String getOrderOutNo(Integer factoryId,Integer empId,String currdate);
	
	//获取排载单号
	public String getContainerNo(String currdate);
	
	//获取收款单编号
	public String getFinaceRecNo(Integer custId,String currdate);
	
	//获取付款单编号
	public String getFinaceGivenNo(Integer factoryId);
	
	//获取生产合同分解单号
	public String getAutoOrderFacNo(Integer factoryId,Integer orderId);
	
	//获取配件编号
	public String getFitingorderNo();
	
	//获取包材编号
	public String getPackingorderNo();
	
	//获取应付款单编号  
	public String getFinaceNeeGivenNo(Integer factoryId);
	
	//获取应收款单编号  
	public String getFinaceNeeRecGivenNo(Integer custId);
	
	//保存完后，更新当前序列号的值
	public void saveSeq(String type);
	
	//保存客户序列号
	public void saveCustSeq(Integer custId, String type,String currDate);
	
	//保存厂家序列号
	public void saveFacSeq(Integer facId, String type);
	
	//获取询盘单号
	public String getPanNo(Integer empId,String currdate);
}
