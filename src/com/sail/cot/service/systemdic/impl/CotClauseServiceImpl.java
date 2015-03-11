package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCalculation;
import com.sail.cot.domain.CotClause;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotClauseService;
import com.sail.cot.util.Log4WebUtil;

public class CotClauseServiceImpl implements CotClauseService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotClauseServiceImpl.class);
	
	public void addClause(List ClauseList) {
		// TODO Auto-generated method stub
		try {
			this.getCotBaseDao().saveRecords(ClauseList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加价格条款信息异常",e);
		}
	}

	public int deleteClause(List ClauseList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < ClauseList.size(); i++) {
			CotClause cotClause = (CotClause) ClauseList.get(i);
			ids.add(cotClause.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotClause");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除价格条款信息异常",e);
			res = -1;
		}
		return res;
	}

	public boolean findExistByName(String name) {
		// TODO Auto-generated method stub
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotClause obj where obj.clauseName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotClause obj where obj.clauseName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count>0)
			{
				isExist = true;
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("查找重复方法失败", e);
		}
		
		return isExist;
	}

	public CotClause getClauseById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotClause) this.getCotBaseDao().getById(CotClause.class, Id);
	}

	public List getClauseList() {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords("CotClause");
	}

	public boolean modifyClause(List ClauseList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < ClauseList.size(); i++) {
			CotClause cotClause = (CotClause)ClauseList.get(i);
			Integer id = this.isExistClauseId(cotClause.getClauseName());
			//System.out.println("____________________________"+id);
			if(id!=null && !id.toString().equals(cotClause.getId().toString())){
				return false;
			}
		}
		try {
			this.getCotBaseDao().updateRecords(ClauseList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新价格条款信息异常", e);
		}
		return true;
	}

	public List getList(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public Map getMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotCalculation");
		for(int i=0; i<res.size(); i++)
		{
			CotCalculation cotCalculation = (CotCalculation)res.get(i);
			retur.put(cotCalculation.getId().toString(),cotCalculation.getCalName());
			
		}
		return retur;
	}

	public Integer isExistClauseId(String claName) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotClause c where c.clauseName='"+claName+"'");
		if(res.size()!=1){
			return null;
		}else{
			return res.get(0);
		}
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}

}
