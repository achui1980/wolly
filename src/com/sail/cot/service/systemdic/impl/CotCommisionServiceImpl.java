package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCommisionType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotCommisionService;
import com.sail.cot.util.Log4WebUtil;

public class CotCommisionServiceImpl implements CotCommisionService {


	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotCommisionServiceImpl.class);
	
	public void addCommision(List CommisionList) {
		// TODO Auto-generated method stub
		try {
			this.getCotBaseDao().saveRecords(CommisionList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加佣金类型信息异常",e);
		}
	}

	public int deleteCommision(List CommisionList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < CommisionList.size(); i++) {
			CotCommisionType cotCommision = (CotCommisionType) CommisionList.get(i);
			ids.add(cotCommision.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCommisionType");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除佣金类型信息异常",e);
			res = -1;
		}
		return res;
	}

	public boolean findExistByName(String name) {
		// TODO Auto-generated method stub
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCommisionType obj where obj.commisionName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotCommisionType obj where obj.commisionName='"+name+"'");
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

	public CotCommisionType getCommisionById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotCommisionType) this.getCotBaseDao().getById(CotCommisionType.class, Id);
	}

	public List getCommisionList() {
		return this.getCotBaseDao().getRecords("CotCommisionType");
	}

	public boolean modifyCommision(List CommisionList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < CommisionList.size(); i++) {
			CotCommisionType cotCommisionType = (CotCommisionType)CommisionList.get(i);
			Integer id = this.isExistCommisionId(cotCommisionType.getCommisionName());
			if (id!=null&& !id.toString().equals(cotCommisionType.getId().toString())) {
				return false;
			}
		}
		
		try {
			this.getCotBaseDao().updateRecords(CommisionList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新佣金类型信息异常", e);
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

	public Integer isExistCommisionId(String commisionName) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotCommisionType c where c.commisionName='"+commisionName+"'");
		if (res.size()!=1) {
			return null;
		}else {
			return res.get(0);
		}
					
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotCommisionService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

}
