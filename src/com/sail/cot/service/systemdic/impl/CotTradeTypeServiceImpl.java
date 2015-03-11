package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotTradeType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTradeTypeService;

public class CotTradeTypeServiceImpl implements CotTradeTypeService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void addTradeType(CotTradeType cotTradeType) {
		 
		this.getCotBaseDao().create(cotTradeType);
	}
	
	public void modifyTradeType(CotTradeType cotTradeType) {
		 
		this.getCotBaseDao().update(cotTradeType);
	}

	public void deleteTradeType(List<CotTradeType> TradeTypeList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < TradeTypeList.size(); i++) {
			CotTradeType cotTradeType = (CotTradeType) TradeTypeList.get(i);
			ids.add(cotTradeType.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotTradeType");
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

	public CotTradeType getTradeTypeById(Integer id) {
		 
		return (CotTradeType) this.getCotBaseDao().getById(CotTradeType.class, id);
	}

	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  0;
	}
	
	public List getTradeTypeList() {
		return this.getCotBaseDao().getRecords("CotTradeType");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotTradeTypeService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

}
