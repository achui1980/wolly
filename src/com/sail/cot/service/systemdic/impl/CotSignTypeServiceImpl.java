/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.systemdic.CotSignTypeDao;
import com.sail.cot.domain.CotSignType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotSignTypeService;
import com.sail.cot.util.Log4WebUtil;


public class CotSignTypeServiceImpl implements CotSignTypeService {

	private CotSignTypeDao signTypeDao;
	private Logger logger = Log4WebUtil.getLogger(CotSignTypeServiceImpl.class);

	//根据征样编号取得对象
	public CotSignType getSignTypeById(Integer id){
		Object object = this.getSignTypeDao().getById(CotSignType.class, id);
		if(object!=null){
			return (CotSignType)object;
		}else{
			return null;
		}
	}
	
	//保存征样方式
	public Boolean addSignTypes(List<?> signTypesList){
		for (int i = 0; i < signTypesList.size(); i++) {
			CotSignType cotSignType = (CotSignType)signTypesList.get(i);
			Integer id = this.getSignTypeDao().isExistSignName(cotSignType.getSignName());
			if(id!=null){
				return false;
			}
		}
		try {
			this.getSignTypeDao().saveRecords(signTypesList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加征样方式异常", e);
			return false;
		}
		return true;
	}
	
	//更新征样方式
	public Boolean modifySignTypes(List<?> signTypesList){
		for (int i = 0; i < signTypesList.size(); i++) {
			CotSignType cotSignType = (CotSignType)signTypesList.get(i);
			Integer id = this.getSignTypeDao().isExistSignName(cotSignType.getSignName());
			if(id!=null && !id.toString().equals(cotSignType.getId().toString())){
				return false;
			}
		}
		try {
			this.getSignTypeDao().updateRecords(signTypesList);
		} catch (Exception e) {
			logger.error("更新征样方式异常", e);
		}
		return true;
	}
	
	//删除征样方式
	public Boolean deleteSignTypes(List<?> signTypesList){
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < signTypesList.size(); i++) {
			CotSignType cotSignType = (CotSignType)signTypesList.get(i);
			ids.add(cotSignType.getId());
		}
		try {
			this.getSignTypeDao().deleteRecordByIds(ids, "CotSignType");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除征样方式异常", e);
			return false;
		}
		return true;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getSignTypeDao().getJsonData(queryInfo);
	}

	public CotSignTypeDao getSignTypeDao() {
		return signTypeDao;
	}

	public void setSignTypeDao(CotSignTypeDao signTypeDao) {
		this.signTypeDao = signTypeDao;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getSignTypeDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getSignTypeDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	
}
