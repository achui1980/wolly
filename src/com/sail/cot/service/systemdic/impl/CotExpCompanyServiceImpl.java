package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotExpCompany;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotExpCompanyService;
import com.sail.cot.util.Log4WebUtil;

public class CotExpCompanyServiceImpl implements CotExpCompanyService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotExpCompanyServiceImpl.class);
	
	public void addExpCompany(List ExpCompanyList) {
		// TODO Auto-generated method stub
		try {
			this.getCotBaseDao().saveRecords(ExpCompanyList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加快递公司信息异常",e);
		}
	}

	public int deleteExpCompany(List ExpCompanyList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < ExpCompanyList.size(); i++) {
			CotExpCompany cotExpCompany = (CotExpCompany) ExpCompanyList.get(i);
			ids.add(cotExpCompany.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotExpCompany");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除快递公司信息异常",e);
			res = -1;
		}
		return res;
	}

	public boolean findExistByName(String name) {
		// TODO Auto-generated method stub
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotExpCompany obj where obj.expCompanyName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotExpCompany obj where obj.expCompanyName='"+name+"'");
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

	public CotExpCompany getExpCompanyById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotExpCompany) this.getCotBaseDao().getById(CotExpCompany.class, Id);
	}

	public List getExpCompanyList() {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords("CotExpCompany");
	}

	public boolean modifyExpCompany(List ExpCompanyList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < ExpCompanyList.size(); i++) {
			CotExpCompany cotExpCompany = (CotExpCompany)ExpCompanyList.get(i);
			Integer id = this.isExistExpCompany(cotExpCompany.getExpCompanyName());
			if (id!=null && !id.toString().equals(cotExpCompany.getId().toString())) {
				return false;
			}
		}
		try {
			this.getCotBaseDao().updateRecords(ExpCompanyList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新快递公司信息异常", e);
		}
		return true;
	}

	public Integer isExistExpCompany(String expCompanyName) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotExpCompany c where c.expCompanyName='"+expCompanyName+"'");
		if(res.size()!=1){
			return null;
		}else{
			return res.get(0);
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
	
	//判断快递公司是否存在 true:存在 false：不存在
	public boolean findExistExpCompany(String id,String name) {
		String strSql = " from CotExpCompany obj where obj.expCompanyName = '"+name+"'";
		List<?> res = null;
		res = this.getCotBaseDao().find(strSql);
		if(res == null || res.size() == 0){  
			return false;
		}else{
			CotExpCompany expCompany = (CotExpCompany) res.get(0);
			Integer expid = expCompany.getId();
			if(expid.toString().equals(id)){
				return false;
			}
		}	
		return true;
	}
	
	//添加或修改
	public void saveOrUpdateExpCompany(List<CotExpCompany> expCompanyList) {
		this.getCotBaseDao().saveOrUpdateRecords(expCompanyList);
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}

}
