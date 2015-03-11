package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotTaxType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTaxTypeService;

public class CotTaxTypeServiceImpl implements CotTaxTypeService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void addTaxType(CotTaxType cotTaxType) {
		 
		this.getCotBaseDao().create(cotTaxType);
	}
	
	public void modifyTaxType(CotTaxType cotTaxType) {
		 
		this.getCotBaseDao().update(cotTaxType);
	}

	public void deleteTaxType(List<CotTaxType> TaxTypeList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < TaxTypeList.size(); i++) {
			CotTaxType cotTaxType = (CotTaxType) TaxTypeList.get(i);
			ids.add(cotTaxType.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotTaxType");
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
	}

	public List<?> getList(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  null;
	}

	public CotTaxType getTaxTypeById(Integer id) {
		 
		return (CotTaxType) this.getCotBaseDao().getById(CotTaxType.class, id);
	}

	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  0;
	}
	
	public List getTaxTypeList() {
		return this.getCotBaseDao().getRecords("CotTaxType");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotTaxTypeService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}
}
