package com.sail.cot.service.sample.impl;

import java.sql.Date; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.service.sample.CotPriceOutService;
import com.sail.cot.util.Log4WebUtil;

public class CotPriceOutServiceImpl implements CotPriceOutService {

    private Logger logger = Log4WebUtil.getLogger(CotPriceOutServiceImpl.class);
	
	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	public CotElementsNew getElementsNewById(Integer Id) {
		 
		return (CotElementsNew) this.getCotBaseDao().getById(CotElementsNew.class, Id);
	}
	
	//添加或修改报价
	public void addOrUpdatePriceOut(CotElementsNew cotElementsNew) {
		 
		Date currDate = new java.sql.Date(System.currentTimeMillis());
		cotElementsNew.setEleAddTime(currDate);
		this.getCotBaseDao().update(cotElementsNew);
	}
	
	//删除报价
	public void delPriceOut(CotElementsNew cotElementsNew) {
		cotElementsNew.setPriceOut(null);
		cotElementsNew.setPriceOutUint(null);
		this.getCotBaseDao().update(cotElementsNew);
	}
   
	//获取币种表对象
	public List<?> getCotCurrencyList() {
		 
		return this.getCotBaseDao().getRecords("CotCurrency");
	}

	
	//获取币种映射
	public Map<?, ?> getMap() {
		 
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotCurrency");
		for(int i=0; i<res.size(); i++)
		{
			CotCurrency cotCurrency = (CotCurrency)res.get(i);
			retur.put(cotCurrency.getId().toString(),cotCurrency.getCurName());
		}
		return retur;
	}
	
	//是否有报价记录
	public boolean findRecord(){
		boolean isHaveRecord = false;
		List<?> record = this.getCotBaseDao().find("select obj.priceOutUint,obj.priceOut from CotElementsNew obj");
		if(record.size()>0){
			isHaveRecord = true;
		}
		return isHaveRecord;
	}
}
