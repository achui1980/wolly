package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotConsignCompany;
import com.sail.cot.domain.CotConsignCompany;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotConsignCompanyService;

public class CotConsignCompanyServiceImpl implements CotConsignCompanyService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void addConsignCompany(CotConsignCompany cotConsignCompany) {
		 
		this.getCotBaseDao().create(cotConsignCompany);
	}
	
	public void modifyConsignCompany(CotConsignCompany cotConsignCompany) {
		 
		this.getCotBaseDao().update(cotConsignCompany);
	}

	public void deleteConsignCompany(List<CotConsignCompany> ConsignCompanyList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < ConsignCompanyList.size(); i++) {
			CotConsignCompany cotConsignCompany = (CotConsignCompany) ConsignCompanyList.get(i);
			ids.add(cotConsignCompany.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotConsignCompany");
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

	public CotConsignCompany getConsignCompanyById(Integer id) {
		 
		return (CotConsignCompany) this.getCotBaseDao().getById(CotConsignCompany.class, id);
	}

	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  0;
	}
	
	public List<?> getConsignCompanyList() {
		return this.getCotBaseDao().getRecords("CotConsignCompany");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotConsignCompanyService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

}
