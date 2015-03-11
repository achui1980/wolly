package com.sail.cot.service.library.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotSaleInfo;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.library.CotLibraryService;
import com.sail.cot.util.Log4WebUtil;

public class CotLibraryServiceImpl implements CotLibraryService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotLibraryServiceImpl.class);
	
	public void addLibrary(List<CotSaleInfo> libraryList) {
		 
		try {
			this.getCotBaseDao().saveRecords(libraryList);
		} catch (DAOException e) {
			 e.printStackTrace();
			logger.error("添加包装类型信息异常",e);
		}
	}

	public void deleteLibrary(List<CotSaleInfo> libraryList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < libraryList.size(); i++) {
			CotSaleInfo library = (CotSaleInfo) libraryList.get(i);
			ids.add(library.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSaleInfo");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除包装类型信息异常",e);
		}
	}
	
	public void modifyLibrary(List<CotSaleInfo> libraryList) {
		 
		try {
			this.getCotBaseDao().updateRecords(libraryList);
		} catch (Exception e) {
			logger.error("更新包装类型信息异常", e);
		}
	}

	public boolean findExistByName(String name) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotSaleInfo obj where obj.eleName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotSaleInfo obj where obj.eleName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count>0)
			{
				isExist = true;
			}
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查找重复方法失败", e);
		}
		
		return isExist;
	}

	public CotSaleInfo getLibraryById(Integer Id) {
		 
		return (CotSaleInfo) this.getCotBaseDao().getById(CotSaleInfo.class, Id);
	}

	public List<?> getLibraryList() {
		 
		return this.getCotBaseDao().getRecords("CotSaleInfo");
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
	
	//批量删除库存信息
	public void deleteLibraryByList(List<Integer> ids){
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSaleInfo");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("批量删除库存信息异常",e);
		}
	}
	
	//添加或修改库存信息
	public Integer saveOrUpdateLibrary(CotSaleInfo cotSaleInfo,String recordDate){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (recordDate != null && !"".equals(recordDate)) {
				cotSaleInfo.setRecordDate(new Date(sdf.parse(recordDate).getTime()));
			}
			
			cotSaleInfo.setAddTime(new java.util.Date());

			List<CotSaleInfo> list = new ArrayList<CotSaleInfo>();
			list.add(cotSaleInfo);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			
			return cotSaleInfo.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存库存记录出错！");
			return null;
		}
	}

}
