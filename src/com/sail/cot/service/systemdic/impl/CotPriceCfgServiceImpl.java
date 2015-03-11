package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotClause;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotContainerType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.domain.CotPriceSituation;
import com.sail.cot.domain.CotShipPort;
import com.sail.cot.domain.CotTrafficType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotPriceCfgService;
import com.sail.cot.util.SystemDicUtil;

public class CotPriceCfgServiceImpl implements CotPriceCfgService {
	
	private CotBaseDao cotBaseDao;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void addPriceCfg(CotPriceCfg cotPriceCfg) {
		// TODO Auto-generated method stub
		this.getCotBaseDao().create(cotPriceCfg);
	}

	public Integer modifyPriceCfg(CotPriceCfg cotPriceCfg) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
		list.add(cotPriceCfg);
		this.getCotBaseDao().saveOrUpdateRecords(list);
		return cotPriceCfg.getId();
	}

	public List<?> getObjList(String objName) {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords(objName);
	}

	public CotPriceCfg getPriceCfgById(Integer id) {
		// TODO Auto-generated method stub
		return (CotPriceCfg) this.getCotBaseDao().getById(CotPriceCfg.class, id);
	}
	
	public List getList(QueryInfo queryInfo){
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  null;
	}	
	 
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	} 
	
	public Map getShipPortMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotShipPort");
		for(int i=0; i<res.size(); i++)
		{
			CotShipPort cotShipPort = (CotShipPort)res.get(i);
			retur.put(cotShipPort.getId().toString(),cotShipPort.getShipPortNameEn());
			
		}
		return retur;
	}
	// 得到公司的集合
	public List<?> getCompanyList() {
		List<?> list = this.getCotBaseDao().getRecords("CotCompany");
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotCompany cotCompany = (CotCompany) it.next();
			cotCompany.setCompanyLogo(null);
		}
		return list;
	}
	public Map getCaluseMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotClause");
		for(int i=0; i<res.size(); i++)
		{
			CotClause cotClause = (CotClause)res.get(i);
			retur.put(cotClause.getId().toString(),cotClause.getClauseName());
			
		}
		return retur;
	}
	
	public Map getCurrencyMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotCurrency");
		for(int i=0; i<res.size(); i++)
		{
			CotCurrency cotCurrency = (CotCurrency)res.get(i);
			retur.put(cotCurrency.getId().toString(),cotCurrency.getCurNameEn() );
			
		}
		return retur;
	}
	
	public boolean findRecord(){
		boolean isHaveRecord = false;
		List record = this.getCotBaseDao().find("from CotPriceCfg obj");
		if(record.size()>0){
			isHaveRecord = true;
		}
		return isHaveRecord;
	}

	public Map getContainerMap() {
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotContainerType");
		for(int i=0; i<res.size(); i++)
		{
			CotContainerType cotContainerType = (CotContainerType)res.get(i);
			retur.put(cotContainerType.getId().toString(),cotContainerType.getContainerName() );
			
		}
		return retur;
	}

	public Map getSituationMap() {
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotPriceSituation");
		for(int i=0; i<res.size(); i++)
		{
			CotPriceSituation cotPriceSituation = (CotPriceSituation)res.get(i);
			retur.put(cotPriceSituation.getId().toString(),cotPriceSituation.getSituationName() );
			
		}
		return retur;
	}
	public Map  getCompanyMap() {
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotCompany");
		for(int i=0; i<res.size(); i++)
		{
			CotCompany cotCompany = (CotCompany)res.get(i);
			retur.put(cotCompany.getId().toString(),cotCompany.getCompanyShortName() );
			
		}
		return retur;
	}
	public Map getTrafficMap() {
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotTrafficType");
		for(int i=0; i<res.size(); i++)
		{
			CotTrafficType cotTrafficType = (CotTrafficType)res.get(i);
			retur.put(cotTrafficType.getId().toString(),cotTrafficType.getTrafficNameEn());
			
		}
		return retur;
	}
	
	// 得到objName的集合
	public List<?> getDicList(String objName){
		return systemDicUtil.getDicListByName(objName);
	}
	
	public CotPriceCfg getPriceCfg(){
		String hql = " from CotPriceCfg as p";
		List<CotPriceCfg> res = new ArrayList<CotPriceCfg>();
		res = this.getCotBaseDao().find(hql);
		if (res.size() !=0 ) {
			CotPriceCfg cotPriceCfg = res.get(0);
			return cotPriceCfg;
		}else {
			return null;
		}
	}
}
