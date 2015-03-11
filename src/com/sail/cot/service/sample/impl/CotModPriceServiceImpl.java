/**
 * 
 */
package com.sail.cot.service.sample.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotFittingsAnys;
import com.sail.cot.domain.CotModPrice;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotModPriceService;


public class CotModPriceServiceImpl implements CotModPriceService {
	private CotBaseDao modPriceDao;

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getModPriceDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getModPriceDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}


	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId().toString(), cotCurrency.getCurNameEn());
		}
		return map;
	}

	// 根据编号获得对象
	public CotModPrice getCotModPriceById(Integer id) {
		return (CotModPrice) this.getModPriceDao().getById(CotModPrice.class,
				id);
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getModPriceDao().getRecords(objName);
	}

	// 保存
	public void addList(List<?> list) {
		try {
			this.getModPriceDao().saveRecords(list);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 修改
	public void updateList(List<?> list) {
		try {
			this.getModPriceDao().updateRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void deleteList(List<Integer> ids) {
		try {
			this.getModPriceDao().deleteRecordByIds(ids, "CotModPrice");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//根据货号查找样品编号
	public Integer findEleIdByEle(String eleId){
		String hql = "select obj.id from CotElementsNew obj where obj.eleId='"+eleId+"'";
		List list = this.getModPriceDao().find(hql);
		if(list!=null && list.size()>0){
			return (Integer) list.get(0);
		}else{
			return 0;
		}
	}
	
	public CotBaseDao getModPriceDao() {
		return modPriceDao;
	}

	public void setModPriceDao(CotBaseDao modPriceDao) {
		this.modPriceDao = modPriceDao;
	}

}
