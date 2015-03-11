package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.service.systemdic.CotCurrencyService;

public class CotCurrencyServiceImpl implements CotCurrencyService {

	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	//保存
	public boolean saveOrUpdate(CotCurrency eleOther) {
		try {
			List list = new ArrayList();
			list.add(eleOther);
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
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCurrency");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//查询是否重复
	public Integer findExistByNo(String hscode, String id) {
		String hql = "select obj.id from CotCurrency obj where obj.curNameEn=?";
		Object[] obj = new Object[1];
		obj[0]=hscode;
		List<?> res = this.getCotBaseDao().queryForLists(hql, obj);
		if (res==null || res.size() == 0) {
			return null;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				return null;
			} else {
				return 1;
			}
		}
		return 2;
	}
	
	//根据id获得对象
	public CotCurrency getObjById(Integer id){
		return (CotCurrency) this.getCotBaseDao().getById(CotCurrency.class, id);
	}
	
	//获得所有产品分类
	public List<?> getList() {
		List<?> list =  this.getCotBaseDao().getRecords("CotCurrency");
		return list;
	}

}
