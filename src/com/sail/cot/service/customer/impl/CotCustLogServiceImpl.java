package com.sail.cot.service.customer.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustLog;
import com.sail.cot.domain.vo.CotCustLogVO;
import com.sail.cot.domain.vo.CotOrderVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustLogService;
import com.sail.cot.util.Log4WebUtil;

public class CotCustLogServiceImpl implements CotCustLogService {
	
	private CotBaseDao cotBaseDao ;
	private Logger logger = Log4WebUtil.getLogger(CotCustLogServiceImpl.class);
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}
	
	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	//添加或修改沟通记录
	public boolean saveOrUpdateCustLog(CotCustLog custLog,String logDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (logDate != null && !"".equals(logDate)) {
				custLog.setLogDate(new Timestamp(sdf.parse(logDate)
						.getTime()));
			}
			custLog.setAddTime(new java.util.Date());
			List list = new ArrayList();
			list.add(custLog);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//批量删除沟通记录
	public int deleteCustLogByList(List<Integer> ids) {
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCustLog");
			return 0;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("批量删除异常",e);
			return -1;
		}
	}
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}
	
	// 查询VO记录
	public List<?> getCustVOList(QueryInfo queryInfo) {
		List<CotCustLogVO> listVo = new ArrayList<CotCustLogVO>();
		try {
			List<?> list = this.getCotBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotCustLogVO custLogVO = new CotCustLogVO();
				Object[] obj = (Object[]) list.get(i);
				custLogVO.setId((Integer) obj[0]);
				custLogVO.setLogDate((java.util.Date) obj[1]);
				custLogVO.setLogContent((String) obj[2]);
				custLogVO.setLogCheck((Integer) obj[3]);
				custLogVO.setLogAdvise((String) obj[4]);
				custLogVO.setRemark((String) obj[5]);
				custLogVO.setEmpsName((String) obj[6]);
				custLogVO.setCustomerShortName((String) obj[7]);
				listVo.add(custLogVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	public CotCustLog geCustLogById(Integer id) {
		return (CotCustLog) this.getCotBaseDao().getById(CotCustLog.class, id);
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return getCotBaseDao().getJsonData(queryInfo);
	}

}
