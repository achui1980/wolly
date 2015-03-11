/**
 * 
 */
package com.sail.cot.service.system.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotSeq;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.seq.Sequece;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

import com.sun.org.apache.bcel.internal.generic.NEW;


 
public class CotSeqServiceImpl implements CotSeqService {

	 
	private CotBaseDao baseDao;
	private Logger logger = Log4WebUtil.getLogger(CotSeqServiceImpl.class);
	

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getBaseDao().getJsonData(queryInfo);
	}


	public CotBaseDao getBaseDao() {
		return baseDao;
	}


	public void setBaseDao(CotBaseDao baseDao) {
		this.baseDao = baseDao;
	}


	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotSeqService#saveOrUpdateList(java.util.List)
	 */
	public void saveOrUpdateList(List records) {
		// TODO Auto-generated method stub
		
		for(int i=0; i<records.size(); i++)
		{
			CotSeq seq = (CotSeq)records.get(i);
			if(seq.getId() == null)
				seq.setHisDay(new Date(System.currentTimeMillis()));
//			else {
//				CotSeq seqOld = this.getCotSeq(seq.getId());
//				seq.setCurrentDay(seqOld.getCurrentDay());
//				seq.setHisDay(seqOld.getHisDay());
//			}
			records.set(i, seq);
		}
		this.getBaseDao().saveOrUpdateRecords(records);
	}


	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotSeqService#getCotSeq(java.lang.Integer)
	 */
	public CotSeq getCotSeq(Integer id) {
		// TODO Auto-generated method stub
		return (CotSeq) this.getBaseDao().getById(CotSeq.class, id);
	}


	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotSeqService#delCotSeq(java.util.List)
	 */
	public void delCotSeq(List ids) throws DAOException {
		this.getBaseDao().deleteRecordByIds(ids, "CotSeq");
	}


	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotSeqService#zero()
	 */
	public int zero(String id) throws DAOException {
		// TODO Auto-generated method stub
		String hql = "update CotSeq set currentSeq = 0 where id=:id";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		Map paramMap = new HashMap();
		paramMap.put("id", new Integer(id));
		int res = this.getBaseDao().executeUpdate(queryInfo, paramMap);
		return res;
	}
	//获取客户单号
	public String getCustNo(){
		Sequece seq =new Sequece(false);
		return seq.getCustNo();
	}
	
	//获取厂家单号
	public String getFacNo(){
		Sequece seq =new Sequece(false);
		return seq.getFactoryNo();
	}
	
	//获取外销合同单号
	public String getOrderNo(Integer custId,Integer empId,String currdate,Integer companyId){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotCustomer",custId);
		map.put("CotEmps", empId);
		map.put("CotCompany", companyId);
		String orderNo=seq.getOrderNo(map, currdate);
		//直接保存
//		this.saveCustSeq(custId, "order", currdate);
//		this.saveSeq("order");
		
		return orderNo;
	}
	
	//获取样品货号
	public String getEleNo(Integer byId,Integer myId,Integer syId,Integer facId){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotTypeLv4",byId);
		map.put("CotTypeLv2", myId);
		map.put("CotTypeLv3", syId);
		map.put("CotFactory", facId);
		String eleId=seq.getEleNo(map);
		
		this.saveFacSeq(facId, "ele");
		//直接保存
		this.saveSeq("ele");
		return eleId;
	}
	
	//获取报价单单号
	public String getPriceNo(Integer custId,Integer empId,String currdate,Integer companyId){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotCustomer",custId);
		map.put("CotEmps", empId);
		map.put("CotCompany", companyId);
		
		String priceNo=seq.getPriceNo(map, currdate);
		
//		this.saveCustSeq(custId, "price",currdate);
//		this.saveSeq("price");
		
		return priceNo;
	}
	
	//获取送样单号
	public String getGivenNo(Integer custId,Integer empId,String currdate){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotCustomer",custId);
		map.put("CotEmps", empId);
		return seq.getGivenNo(map, currdate);
	}
	
	//获取征样单号
	public String getSignNo(Integer custId,Integer empId,Integer factoryId ,String currdate){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotCustomer",custId);
		map.put("CotFactory",factoryId);
		map.put("CotEmps", empId);
		
		return seq.getSignNo(map, currdate);
	}
	//获取生产合同单号
	public String getOrderFacNo(Integer factoryId,Integer empId,String currdate){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotFactory",factoryId);
		map.put("CotEmps", empId);
		return seq.getOrderFacNo(map, currdate);
	}
	
	//获取生产合同分解单号
	public String getAutoOrderFacNo(Integer factoryId,Integer orderId){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotFactory",factoryId);
		map.put("CotOrder", orderId);
		return seq.getAutoOrderFacNo(map);
	}
	
	
	//获取出货单号
	public String getOrderOutNo(Integer custId,Integer empId,String currdate){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotCustomer",custId);
		map.put("CotEmps", empId);
		
		String order=seq.getOrderOutNo(map, currdate);
		
		this.saveCustSeq(custId, "orderout",currdate);
		this.saveSeq("orderout");
		
		return order;
	}
	
	//获取排载单号
	public String getContainerNo(String currdate){
		Sequece seq =new Sequece(false);
		return seq.getContainerNo(currdate);
	}
	
	//获取收款单编号
	public String getFinaceRecNo(Integer custId,String currdate){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotCustomer",custId);
		return seq.getFinaceRecNo(map,currdate);
	}
	
	//获取付款单编号  
	public String getFinaceGivenNo(Integer factoryId){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotFactory",factoryId);
		return seq.getFinaceGivenNo(map);
	}
	
	//获取应付款单编号  
	public String getFinaceNeeGivenNo(Integer factoryId){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotFactory",factoryId);
		return seq.getFinaceNeeGivenNo(map);
	}
	
	//获取应收款单编号  
	public String getFinaceNeeRecGivenNo(Integer custId){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotCustomer",custId);
		return seq.getFinaceNeeRecGivenNo(map);
	}
	
	//获取配件编号
	public String getFitingorderNo(){
		Sequece seq =new Sequece(false);
		return seq.getFitingNo();
	}
	//获取包材编号
	public String getPackingorderNo(){
		Sequece seq =new Sequece(false);
		return seq.getPackingorderNo();
	}
	
	//保存完后，更新当前序列号的值
	public void saveSeq(String type){
		Sequece seq =new Sequece(false);
		seq.saveSeq(type);
	}
	//保存客户序列号
	public void saveCustSeq(Integer custId, String type,String currDate){
		Sequece seq =new Sequece(false);
		seq.saveCustSeq(custId, type,currDate);
	}
	
	//保存厂家序列号
	public void saveFacSeq(Integer facId, String type){
		Sequece seq =new Sequece(false);
		seq.saveFacSeq(facId, type);
	}
	
	//获取条形码单号
	public String getBarcodeNo(){
		Sequece seq =new Sequece(false);
		return seq.getBarcodeNo();
	}
	
	//获取询盘单号
	public String getPanNo(Integer empId,String currdate){
		Sequece seq =new Sequece(false);
		Map map =new HashMap();
		map.put("CotEmps", empId);
		String orderNo=seq.getPanNo(map, currdate);
		//直接保存
//		this.saveCustSeq(custId, "order", currdate);
//		this.saveSeq("order");
		
		return orderNo;
	}
	
}
