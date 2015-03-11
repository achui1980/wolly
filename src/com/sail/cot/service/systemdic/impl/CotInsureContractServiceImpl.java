package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotExpCompany;
import com.sail.cot.domain.CotInsureContract;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotInsureContractService;

public class CotInsureContractServiceImpl implements CotInsureContractService {

private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void addInsureContract(CotInsureContract cotInsureContract) {
		// TODO Auto-generated method stub
		this.getCotBaseDao().create(cotInsureContract);
	}
	
	public void modifyInsureContract(CotInsureContract cotInsureContract) {
		// TODO Auto-generated method stub
		this.getCotBaseDao().update(cotInsureContract);
	}

	public boolean deleteInsureContract(List<CotInsureContract> InsureContractList) {
		// TODO Auto-generated method stub
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < InsureContractList.size(); i++) {
			CotInsureContract cotInsureContract = (CotInsureContract) InsureContractList.get(i);
			ids.add(cotInsureContract.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotInsureContract");
			return true;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public List getList(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  null;
	}

	public CotInsureContract getInsureContractById(Integer id) {
		// TODO Auto-generated method stub
		return (CotInsureContract) this.getCotBaseDao().getById(CotInsureContract.class, id);
	}

	public int getRecordCount(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  0;
	}
	
	public List getInsureContractList(){
		return this.getCotBaseDao().getRecords("CotInsureContract");
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}
	
	public Integer isExistName(String name) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotInsureContract c where c.insureName='"+name+"'");
		if(res.size()!=1){
			return null;
		}else{
			return res.get(0);
		}
	}
	
	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name) {
		String strSql = " from CotInsureContract obj where obj.insureName = '"+name+"'";
		List<?> res = null;
		res = this.getCotBaseDao().find(strSql);
		if(res == null || res.size() == 0){  
			return false;
		}else{
			CotInsureContract insureContract = (CotInsureContract) res.get(0);
			Integer expid = insureContract.getId();
			if(expid.toString().equals(id)){
				return false;
			}
		}	
		return true;
	}

}
