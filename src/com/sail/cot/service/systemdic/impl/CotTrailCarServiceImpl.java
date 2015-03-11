package com.sail.cot.service.systemdic.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotTrailCar;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTrailCarService;
import com.sail.cot.util.Log4WebUtil;
public class CotTrailCarServiceImpl implements CotTrailCarService {

	private CotBaseDao cotBaseDao;
	private Logger logger = Log4WebUtil.getLogger(CotTrailCarServiceImpl.class);
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	//根据id获取拖车行信息
	public CotTrailCar getTrailCarById(Integer id){
		CotTrailCar cotTrailCar = new CotTrailCar();
		cotTrailCar = (CotTrailCar) this.getCotBaseDao().getById(CotTrailCar.class, id);
		return cotTrailCar;
	}
	
	//添加或修改拖车行信息
	public void saveOrUpdateTrailCar(List<CotTrailCar> trailcarList){
		this.getCotBaseDao().saveOrUpdateRecords(trailcarList);
	}
	
	//批量删除拖车行信息
	public void deleteTrailCarByList(List<Integer> ids){
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotTrailCar");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("批量删除拖车行信息异常",e);
		}
	}
	
	//获取拖车行信息总数
	public int getTrailCarCount(QueryInfo queryInfo){
		int res = 0;
		try {
			res = this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			logger.error("获取拖车行记录数异常", e);
			e.printStackTrace();
		}
		return res;
	}
	
	//获取拖车行信息类表（分页）
	public List<?> getTrailCarList(QueryInfo queryInfo){
		List<?> res = null;
		try {
			res = this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			logger.error("获取拖车行信息列表异常", e);
			e.printStackTrace();
		}
		return res;
	}
	
	//判断拖车行是否存在 true:存在 false：不存在
	public boolean findExistTrailCar(String id,String name){
		String strSql = " from CotTrailCar obj where obj.name = '"+name+"'";
		List<?> res = null;
		res = this.getCotBaseDao().find(strSql);
		if(res == null || res.size() == 0){  
			return false;
		}else{
			CotTrailCar cotTrailCar = (CotTrailCar) res.get(0);
			Integer trailcarId = cotTrailCar.getId();
			if(trailcarId.toString().equals(id)){
				return false;
			}
		}	
		return true;
	}
	
	public List getTrailCarList() {
		return this.getCotBaseDao().getRecords("CotTrailCar");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotTrailCarService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

}
