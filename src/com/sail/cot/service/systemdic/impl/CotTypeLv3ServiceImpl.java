package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotTypeLv3;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotTypeLv3Service;
import com.sail.cot.util.Log4WebUtil;

public class CotTypeLv3ServiceImpl implements CotTypeLv3Service {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotTypeLv3ServiceImpl.class);
	
	public void addTypeLv(List TypeLvList) {
		// TODO Auto-generated method stub
		try {
			this.getCotBaseDao().saveRecords(TypeLvList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加客户等级信息异常",e);
		}
	}

	public int deleteTypeLv(List TypeLvList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < TypeLvList.size(); i++) {
			CotTypeLv3 cotTypeLv = (CotTypeLv3) TypeLvList.get(i);
			ids.add(cotTypeLv.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotTypeLv3");
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
		queryInfo.setSelectString("from CotTypeLv3 obj where obj.typeName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotTypeLv3 obj where obj.typeName='"+name+"'");
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
	public CotTypeLv3 getTypeLvById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotTypeLv3) this.getCotBaseDao().getById(CotTypeLv3.class, Id);
	}

	public List getTypeLvList() {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords("CotTypeLv3");
	}

	public boolean modifyTypeLv(List TypeLvList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < TypeLvList.size(); i++) {
			CotTypeLv3 cotTypeLv = (CotTypeLv3)TypeLvList.get(i);
			Integer id = this.isExistTypeLvId(cotTypeLv.getTypeName());
			if (id!=null && !id.toString().equals(cotTypeLv.getId().toString())) {
				return false;
			}
		}
		try {
			this.getCotBaseDao().updateRecords(TypeLvList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新客户等级信息异常", e);
		}
		return true;
	}

	public Integer isExistTypeLvId(String typeLvName) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotTypeLv3 c where c.typeName='"+typeLvName+"'");
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
	 * @see com.sail.cot.service.systemdic.CotTypeLv3Service#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

}
