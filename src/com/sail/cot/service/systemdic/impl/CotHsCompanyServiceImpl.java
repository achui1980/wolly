package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotHsCompany;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotHsCompanyService;

public class CotHsCompanyServiceImpl implements CotHsCompanyService {

    private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void addHsCompany(CotHsCompany cotHsCompany) {
		 
		this.getCotBaseDao().create(cotHsCompany);
	}
	
	public void modifyHsCompany(CotHsCompany cotHsCompany) {
		 
		this.getCotBaseDao().update(cotHsCompany);
	}

	public void deleteHsCompany(List<CotHsCompany> HsCompanyList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < HsCompanyList.size(); i++) {
			CotHsCompany cotHsCompany = (CotHsCompany) HsCompanyList.get(i);
			ids.add(cotHsCompany.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotHsCompany");
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

	public CotHsCompany getHsCompanyById(Integer id) {
		 
		return (CotHsCompany) this.getCotBaseDao().getById(CotHsCompany.class, id);
	}

	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  0;
	}
	
	public List getHsCompanyList() {
		return this.getCotBaseDao().getRecords("CotHsCompany");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotHsCompanyService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

}
