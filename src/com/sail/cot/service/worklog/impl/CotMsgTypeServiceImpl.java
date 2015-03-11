package com.sail.cot.service.worklog.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotMsgType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.worklog.CotMsgTypeService;
import com.sail.cot.util.Log4WebUtil;

public class CotMsgTypeServiceImpl implements CotMsgTypeService {


	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotMsgTypeServiceImpl.class);
	
	public void addMsgType(List MsgTypeList) {
		// TODO Auto-generated method stub
		try {
			this.getCotBaseDao().saveRecords(MsgTypeList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加佣金类型信息异常",e);
		}
	}

	public int deleteMsgType(List MsgTypeList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < MsgTypeList.size(); i++) {
			CotMsgType cotMsgType = (CotMsgType) MsgTypeList.get(i);
			ids.add(cotMsgType.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotMsgType");
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
		queryInfo.setSelectString("from CotMsgType obj where obj.msgTypeName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotMsgType obj where obj.msgTypeName='"+name+"'");
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

	public CotMsgType getMsgTypeById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotMsgType) this.getCotBaseDao().getById(CotMsgType.class, Id);
	}

	public List getMsgTypeList() {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords("CotMsgType");
	}

	public boolean modifyMsgType(List MsgTypeList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < MsgTypeList.size(); i++) {
			CotMsgType cotMsgType = (CotMsgType)MsgTypeList.get(i);
			Integer id = this.isExistMsgTypeId(cotMsgType.getMsgTypeName());
			if (id!=null&& !id.toString().equals(cotMsgType.getId().toString())) {
				return false;
			}
		}
		
		try {
			this.getCotBaseDao().updateRecords(MsgTypeList);
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

	public Integer isExistMsgTypeId(String msgTypeName) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotMsgType c where c.msgTypeName='"+msgTypeName+"'");
		if (res.size()!=1) {
			return null;
		}else {
			return res.get(0);
		}
					
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.worklog.CotMsgTypeService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}
}
