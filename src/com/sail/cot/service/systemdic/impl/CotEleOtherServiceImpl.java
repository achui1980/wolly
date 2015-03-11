package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotEleOtherService;

public class CotEleOtherServiceImpl implements CotEleOtherService {

	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	//保存
	public boolean saveOrUpdate(CotEleOther eleOther) {
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
			this.getCotBaseDao().deleteRecordByIds(ids, "CotEleOther");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//查询报关中文名是否重复
	public Integer findExistByNo(String cnName, String id) {
		String hql = "select obj.id from CotEleOther obj where obj.cnName=?";
		Object[] obj = new Object[1];
		obj[0]=cnName;
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
	public CotEleOther getObjById(Integer id){
		return (CotEleOther) this.getCotBaseDao().getById(CotEleOther.class, id);
	}
	
	//获得所有产品分类
	public List<?> getList() {
		List<?> list =  this.getCotBaseDao().getRecords("CotEleOther");
		return list;
	}
	
	// 更改样品档案的海关编码的退税率
	public int updateEleTax(Float tax,Integer eleHsid) {
		try {
			String faclMb = "update CotElementsNew obj set obj.tuiLv=:tuiLv where obj.eleHsid=:eleHsid";
			Map map = new HashMap();
			map.put("tuiLv", tax);
			map.put("eleHsid", eleHsid);
			QueryInfo queryInfo = new QueryInfo();
			queryInfo.setSelectString(faclMb);
			int result = this.getCotBaseDao().executeUpdate(queryInfo, map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
