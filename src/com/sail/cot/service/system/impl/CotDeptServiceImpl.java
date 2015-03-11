package com.sail.cot.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

import com.jason.core.exception.DAOException;
import com.jason.core.exception.ServiceException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotDept;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotDeptService;

public class CotDeptServiceImpl implements CotDeptService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
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
	
	 
	public void deleteCotDept(CotDept cotDept) throws ServiceException {
		 
		this.getCotBaseDao().delete(cotDept);
	 }

	public CotDept getDeptById(Integer id) {
		 
		return (CotDept) this.getCotBaseDao().getById(CotDept.class, id);
	}

	 
	public boolean saveCotDept(CotDept cotDept) throws ServiceException {
		 
		//判断部门名是否存在
		boolean  dn= this.isNotExistDeptName(cotDept.getDeptName());
		if(dn==true)
		{
			List<CotDept> list=new ArrayList<CotDept>();
			list.add(cotDept);
			try {
				this.getCotBaseDao().saveRecords(list);
			} catch (DAOException e) {
				 e.printStackTrace();
			}
			return true;
		}else{
			return false;
		}
	}

	 
	public void saveCotDeptByList(List<CotDept> cotDeptList)
			throws ServiceException {
		 try {
			this.getCotBaseDao().saveRecords(cotDeptList);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	 
	public boolean updateCotDept(CotDept cotDept) throws ServiceException {
		 
		boolean  dn= this.isNotExistDeptName(cotDept.getDeptName());
		if(dn==true)
		{
			 this.getCotBaseDao().update(cotDept);
			 return true;
		}else{
			return false;
		}
	}

	 
	public void updateCotDeptByList(List<CotDept> cotDeptList)
			throws ServiceException {
		 
		 this.getCotBaseDao().updateRecords(cotDeptList);
	}
	
	 
	@SuppressWarnings("unchecked")
	public boolean isNotExistDeptName(String deptName) {
		 
		List<CotDept> res = new ArrayList<CotDept>();
		res = this.getCotBaseDao().find("from CotDept d where d.deptName='"+deptName+"'");
		if(res.size()!=1){
			return false;
		}else{
			return true;
		}
	}

	public Map<?, ?> getMap() {
		 
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotCompany");
		for(int i=0; i<res.size(); i++)
		{
			CotCompany cotCompany = (CotCompany)res.get(i);
			retur.put(cotCompany.getId().toString(),cotCompany.getCompanyShortName());
			
		}
		return retur;
	}

	public int deleteCotDeptByList(List<CotDept> cotDeptList)
			throws ServiceException {
	    List<Integer> ids=new ArrayList<Integer>();
        for(int i=0; i<cotDeptList.size(); i++)
		{
        	CotDept cotDept = (CotDept)cotDeptList.get(i);
			ids.add(cotDept.getId());
		}
         try{
        	this.getCotBaseDao().deleteRecordByIds(ids, "CotDept");
        	return 0;
        }
        catch(DAOException e)
        {
        	 e.printStackTrace();
        	 return -1;
        }
	}

	public boolean findEmpsRecordsCount(String deptId) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotEmps obj where obj.deptId='"+deptId+"'");
		queryInfo.setCountQuery("select count(*) from CotEmps obj where obj.deptId='"+deptId+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 e.printStackTrace();
		}
		return isExist;
	
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}

	 
}
