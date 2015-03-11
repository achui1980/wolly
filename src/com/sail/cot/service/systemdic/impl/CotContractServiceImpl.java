package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotContract;
import com.sail.cot.service.systemdic.CotContractService;

public class CotContractServiceImpl implements CotContractService {

	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	//保存
	public boolean saveOrUpdate(CotContract typeLv2) {
		try {
			List list = new ArrayList();
			list.add(typeLv2);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//删除
	public boolean deleteByIds(List<?> ids) {
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotContract");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//根据id获得对象
	public CotContract getObjById(Integer id){
		return (CotContract) this.getCotBaseDao().getById(CotContract.class, id);
	}
	
	//获得所有产品分类
	public List<?> getList() {
		List<?> list =  this.getCotBaseDao().getRecords("CotContract");
		return list;
	}

}
