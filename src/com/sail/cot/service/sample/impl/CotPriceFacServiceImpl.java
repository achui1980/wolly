package com.sail.cot.service.sample.impl;

import java.sql.Date; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotPriceFac;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotPriceFacService;
import com.sail.cot.util.Log4WebUtil;

public class CotPriceFacServiceImpl implements CotPriceFacService {

	private Logger logger = Log4WebUtil.getLogger(CotPriceFacServiceImpl.class);
	
	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	public CotPriceFac getPriceFacById(Integer Id) {
		 
		return (CotPriceFac) this.getCotBaseDao().getById(CotPriceFac.class, Id);
	}
    
	//添加或修改报价
	public Integer addOrUpdatePriceFac(CotPriceFac priceFac,String addTime) {
		 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		List<CotPriceFac> records = new ArrayList<CotPriceFac>();
		
		// 保存厂家报价
		try {
			if (addTime != null && !"".equals(addTime)) {
				priceFac.setAddTime(new Date(sdf.parse(addTime).getTime()));
			}
			records.add(priceFac);
			this.getCotBaseDao().saveOrUpdateRecords(records);
		} catch (Exception e) {
			logger.error("保存厂家报价出错！");
			return null;
		}
		return priceFac.getId();
	}
	
	//删除报价
	public Boolean delPriceFac(List<CotPriceFac> priceFacList) {

		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < priceFacList.size(); i++) {
			CotPriceFac priceFac = (CotPriceFac)priceFacList.get(i);
			Integer priceFacId = priceFac.getId();
			ids.add(priceFacId);
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotPriceFac");
			return true;
		} catch (DAOException e) {
			logger.error("删除厂家报价出错");
			return false;
		}
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
			retur.put(cotCurrency.getId().toString(),cotCurrency.getCurNameEn());
		}
		return retur;
	}
	
	//获取厂家映射
	public Map<?, ?> getFacMap() {
		 
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotFactory");
		for(int i=0; i<res.size(); i++)
		{
			CotFactory cotFactory = (CotFactory)res.get(i);
			retur.put(cotFactory.getId().toString(),cotFactory.getShortName());
		}
		return retur;
	}
	
	//是否有报价记录
	public boolean findRecord(){
		boolean isHaveRecord = false;
		List<?> record = this.getCotBaseDao().find("select obj.priceFacUint,obj.priceFac from CotElementsNew obj");
		if(record.size()>0){
			isHaveRecord = true;
		}
		return isHaveRecord;
	} 
	
	//查询记录
	public List<?> getList(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  null;
	}

	//得到总记录数
	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.sample.CotPriceFacService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}
	
}
