package com.sail.cot.service.sample.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotImportresult;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotImportResultService;
import com.sail.cot.servlet.DownImportResultServlet;

public class CotImportResultServiceImpl implements CotImportResultService {

	private CotBaseDao CotBaseDao;

	public CotBaseDao getCotBaseDao() {
		return CotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		CotBaseDao = cotBaseDao;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	public void deleteCotImportresult(List<CotImportresult> importresultList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < importresultList.size(); i++) {
			CotImportresult cotImportresult = (CotImportresult) importresultList
					.get(i);
			ids.add(cotImportresult.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotImportresult");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public void deleteImportresultById(Integer Id) {
		 
		CotImportresult cotImportresult = (CotImportresult) this
				.getImportresultById(Id);
		String impFilepath = cotImportresult.getImpFilepath();
		String classPath = DownImportResultServlet.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length()-16);

		System.out.println("impFilepath=" + impFilepath);
		File file = new File(systemPath+impFilepath);
		file.delete();
	}

	public CotImportresult getImportresultById(Integer Id) {
		 
		return (CotImportresult) this.getCotBaseDao().getById(
				CotImportresult.class, Id);
	}
	
	 
	 
}
