package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotBoxPackingService;
import com.sail.cot.util.Log4WebUtil;

public class CotBoxPackingServiceImpl implements CotBoxPackingService {
	
	private CotBaseDao cotBaseDao ;
	private Logger logger = Log4WebUtil.getLogger(CotBoxPackingServiceImpl.class);
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public int deleteBoxPackingByList(List<Integer> ids) {
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotBoxPacking");
			return 0;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("批量删除箱盒包装信息异常",e);
			return -1;
		}
	}

	public boolean findExistBoxPacking(String id, String value) {
		String strSql = " from CotBoxPacking obj where obj.value = '"+value+"'";
		List<?> res = this.getCotBaseDao().find(strSql);;
		if(res.size() == 0){  
			return false;
		}else{
			CotBoxPacking cotBoxPacking = (CotBoxPacking) res.get(0);
			Integer boxpackingId = cotBoxPacking.getId();
			if(boxpackingId.toString().equals(id)){
				return false;
			}
		}	
		return true;
	}

	public CotBoxPacking getBoxPackingById(Integer id) {
		CotBoxPacking cotBoxPacking = new CotBoxPacking();
		cotBoxPacking = (CotBoxPacking) this.getCotBaseDao().getById(CotBoxPacking.class, id);
		return cotBoxPacking;
	}

	public int getBoxPackingCount(QueryInfo queryInfo) {
		int res = 0;
		try {
			res = this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			logger.error("获取箱盒包装记录数异常", e);
			e.printStackTrace();
		}
		return res;
	}

	public List<?> getBoxPackingList(QueryInfo queryInfo) {
		List<?> res = null;
		try {
			res = this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			logger.error("获取箱盒包装信息列表异常", e);
			e.printStackTrace();
		}
		return res;
	}

	public List<?> getBoxPackingSelList() {
		List<?> res = null;
		res = this.getCotBaseDao().getRecords("CotBoxPacking");
		return res;
	}

	public boolean saveOrUpdateBoxPacking(CotBoxPacking packing) {
		try {
			List list = new ArrayList();
			list.add(packing);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	// 查询所有厂家名称
	public Map<?, ?> getFacMap() {

		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotFactory");
		for (int i = 0; i < res.size(); i++) {
			CotFactory cotFactory = (CotFactory) res.get(i);
			retur.put(cotFactory.getId().toString(), cotFactory.getShortName());

		}
		return retur;
	}
	
	//批量添加公式
	public void updateCalculator(String ids,String formulaIn,String formulaOut,String checkCalculation){
		
		List<CotBoxPacking> resList = new ArrayList<CotBoxPacking>();
		
		String [] packingIds = ids.split(",");
		for (int i = 0; i < packingIds.length; i++) {
			String hql = " from CotBoxPacking obj where obj.id ="+packingIds[i];
			List<?> list = this.getCotBaseDao().find(hql);
			if (list.size() == 1) {
				CotBoxPacking boxPacking = (CotBoxPacking) list.get(0);
				boxPacking.setCheckCalculation(checkCalculation);
				boxPacking.setFormulaIn(formulaIn);
				boxPacking.setFormulaOut(formulaOut);
				resList.add(boxPacking);
			}
		}
		this.getCotBaseDao().updateRecords(resList);
	}
	
	public boolean checkCalculation(String str) {
		Evaluator evaluator = new Evaluator();
		try {
			evaluator.evaluate(str);
			return true;
		} catch (EvaluationException ee) {
			System.out.println(ee);
			return false;
		}
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}

}
