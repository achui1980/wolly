package com.sail.cot.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotCity;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotBoxTypeService;
import com.sail.cot.util.Log4WebUtil;

public class CotBoxTypeServiceImpl implements CotBoxTypeService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotBoxTypeServiceImpl.class);
	
	public void addBoxType(List<CotBoxType> BoxTypeList) {
		 
		try {
			this.getCotBaseDao().saveRecords(BoxTypeList);
		} catch (DAOException e) {
			 e.printStackTrace();
			logger.error("添加包装类型信息异常",e);
		}
	}

	public int deleteBoxType(List<CotBoxType> BoxTypeList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < BoxTypeList.size(); i++) {
			CotBoxType cotBoxType = (CotBoxType) BoxTypeList.get(i);
			ids.add(cotBoxType.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotBoxType");
			return 0;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除包装类型信息异常",e);
			return -1;
		}
	}
	
	public boolean modifyBoxType(List<CotBoxType> BoxTypeList) {
		 
		try {
			this.getCotBaseDao().updateRecords(BoxTypeList);
			return true;
		} catch (Exception e) {
			logger.error("更新包装类型信息异常", e);
			return false;
		}
	}

	public boolean findExistByName(String name) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotBoxType obj where obj.typeName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotBoxType obj where obj.typeName='"+name+"'");
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

	public CotBoxType getBoxTypeById(Integer Id) {
		 
		return (CotBoxType) this.getCotBaseDao().getById(CotBoxType.class, Id);
	}

	public List<?> getBoxTypeList() {
		 
		return this.getCotBaseDao().getRecords("CotBoxType");
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
	
	// 得到objName的集合
	public List<?> getBoxINameList(String objName) {
		return this.getCotBaseDao().getRecords(objName);
	}
	
	//得到内盒包装名称的集合
	public List<?> getBoxINameList(){
		List<?> list = this.getCotBaseDao().find("from CotBoxPacking obj where obj.type = 'IB'");
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	//得到中盒包装名称的集合
	public List<?> getBoxMNameList(){
		List<?> list = this.getCotBaseDao().find("from CotBoxPacking obj where obj.type = 'MB'");
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	//得到外盒包装名称的集合
	public List<?> getBoxONameList(){
		List<?> list = this.getCotBaseDao().find("from CotBoxPacking obj where obj.type = 'OB'");
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	//得到产品包装名称的集合
	public List<?> getBoxPNameList(){
		List<?> list = this.getCotBaseDao().find("from CotBoxPacking obj where obj.type = 'PB'");
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	
	public List<?> getBoxPackingList() {
		 
		return this.getCotBaseDao().getRecords("CotBoxPacking");
	}
	
	public Map<?, ?> getBoxPackingMap() {
		 
		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getBoxPackingList();
		for(int i=0; i<res.size(); i++)
		{
			CotBoxPacking cotBoxPacking = (CotBoxPacking)res.get(i);
			retur.put(cotBoxPacking.getId().toString(), cotBoxPacking.getValue());
			
		}
		return retur;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}

}
