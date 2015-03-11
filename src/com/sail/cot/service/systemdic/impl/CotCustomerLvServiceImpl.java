package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotClause;
import com.sail.cot.domain.CotCustomerLv;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotCustomerLvService;
import com.sail.cot.util.Log4WebUtil;

public class CotCustomerLvServiceImpl implements CotCustomerLvService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotCustomerLvServiceImpl.class);
	
	public void addCustomerLv(List CustomerLvList) {
		// TODO Auto-generated method stub
		try {
			this.getCotBaseDao().saveRecords(CustomerLvList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加客户等级信息异常",e);
		}
	}

	public int deleteCustomerLv(List CustomerLvList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < CustomerLvList.size(); i++) {
			CotCustomerLv cotCustomerLv = (CotCustomerLv) CustomerLvList.get(i);
			ids.add(cotCustomerLv.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCustomerLv");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除客户等级信息异常",e);
			res = -1;
		}
		return res;
	}

	public boolean findExistByName(String name) {
		// TODO Auto-generated method stub
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCustomerLv obj where obj.lvName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotCustomerLv obj where obj.lvName='"+name+"'");
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
	public CotCustomerLv getCustomerLvById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotCustomerLv) this.getCotBaseDao().getById(CotCustomerLv.class, Id);
	}

	public List getCustomerLvList() {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords("CotCustomerLv");
	}

	public boolean modifyCustomerLv(List CustomerLvList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < CustomerLvList.size(); i++) {
			CotCustomerLv cotCustomerLv = (CotCustomerLv)CustomerLvList.get(i);
			Integer id = this.isExistCustomLvId(cotCustomerLv.getLvName());
			if (id!=null && !id.toString().equals(cotCustomerLv.getId().toString())) {
				return false;
			}
		}
		try {
			this.getCotBaseDao().updateRecords(CustomerLvList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新客户等级信息异常", e);
		}
		return true;
	}

	public Integer isExistCustomLvId(String customerLvName) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotCustomerLv c where c.lvName='"+customerLvName+"'");
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

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotCustomerLvService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

}
