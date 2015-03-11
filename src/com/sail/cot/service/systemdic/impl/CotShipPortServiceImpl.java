/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.systemdic.CotShipPortDao;
import com.sail.cot.domain.CotShipPort;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotShipPortService;
import com.sail.cot.util.Log4WebUtil;


public class CotShipPortServiceImpl implements CotShipPortService {

	private CotShipPortDao givenTypeDao;
	private Logger logger = Log4WebUtil.getLogger(CotShipPortServiceImpl.class);

	//根据起运港编号取得对象
	public CotShipPort getShipPortById(Integer id){
		Object object = this.getShipPortDao().getById(CotShipPort.class, id);
		if(object!=null){
			return (CotShipPort)object;
		}else{
			return null;
		}
	}
	
	//保存起运港
	public Boolean addShipPorts(List<?> givenTypesList){
		for (int i = 0; i < givenTypesList.size(); i++) {
			CotShipPort cotShipPort = (CotShipPort)givenTypesList.get(i);
			Integer id = this.getShipPortDao().isExistShipPortName(cotShipPort.getShipPortNameEn());
			if(id!=null){
				return false;
			}
		}
		try {
			this.getShipPortDao().saveRecords(givenTypesList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加起运港异常", e);
			return false;
		}
		return true;
	}
	
	//更新起运港
	public Boolean modifyShipPorts(List<?> givenTypesList){
		for (int i = 0; i < givenTypesList.size(); i++) {
			CotShipPort cotShipPort = (CotShipPort)givenTypesList.get(i);
			Integer id = this.getShipPortDao().isExistShipPortName(cotShipPort.getShipPortName());
			if(id!=null && !id.toString().equals(cotShipPort.getId().toString())){
				return false;
			}
		}
		try {
			this.getShipPortDao().updateRecords(givenTypesList);
		} catch (Exception e) {
			logger.error("更新起运港异常", e);
		}
		return true;
	}
	
	//删除起运港
	public Boolean deleteShipPorts(List<?> givenTypesList){
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < givenTypesList.size(); i++) {
			CotShipPort cotShipPort = (CotShipPort)givenTypesList.get(i);
			ids.add(cotShipPort.getId());
		}
		try {
			this.getShipPortDao().deleteRecordByIds(ids, "CotShipPort");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除起运港异常", e);
			return false;
		}
		return true;
	}

	public CotShipPortDao getShipPortDao() {
		return givenTypeDao;
	}

	public void setShipPortDao(CotShipPortDao givenTypeDao) {
		this.givenTypeDao = givenTypeDao;
	}

	public List getShipPortList() {
		// TODO Auto-generated method stub
		return this.getShipPortDao().getRecords("CotShipPort");
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getShipPortDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getShipPortDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getShipPortDao().getJsonData(queryInfo);
	}
	
	public boolean isExistShipPortName(String shipPortName) {
		List<Integer> res = new ArrayList<Integer>();
		res = getShipPortDao()
				.find("select obj.id from CotShipPort obj where obj.shipPortName='"
						+ shipPortName + "'");
		if (res.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
}
