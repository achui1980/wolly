/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotBank;
import com.sail.cot.domain.CotTrafficType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.impl.BaseTypeServiceImpl;
import com.sail.cot.service.systemdic.CotBankService;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 23, 2009 2:42:53 PM </p>
 * <p>Class Name: CotBankServiceImpl.java </p>
 * @author achui
 *
 */
public class CotBankServiceImpl extends BaseTypeServiceImpl implements CotBankService {

	/* (non-Javadoc)
	 * @see com.sail.cot.service.impl.BaseTypeServiceImpl#deleteList(java.util.List)
	 */
	@Override
	public int deleteList(List objList){
		// TODO Auto-generated method stub
		int res = -1;
		List ids = new ArrayList();
		for(int i=0; i<objList.size(); i++)
		{
			CotBank bank = (CotBank)objList.get(i);
			ids.add(bank.getId());
			res = 0;
		}
		try {
			this.getBaseDao().deleteRecordByIds(ids, "CotBank");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = -1;
			
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotBankService#getBankById(java.lang.Integer)
	 */
	public CotBank getBankById(Integer id) {
		return (CotBank)this.getObjById(id, CotBank.class);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotBankService#findExistByName(java.lang.String)
	 */
	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotBank obj where obj.bankShortName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotBank obj where obj.bankShortName='"+name+"'");
		try {
			int count = this.getBaseDao().getRecordsCount(queryInfo);
			if(count>0)
			{
				isExist = true;
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isExist;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getBaseDao().getJsonData(queryInfo);
	}

	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name) {
		String strSql = " from CotBank obj where obj.bankShortName = '"+name+"'";
		List<?> res = null;
		res = this.getBaseDao().find(strSql);
		if(res == null || res.size() == 0){  
			return false;
		}else{
			CotBank cotBank = (CotBank) res.get(0);
			Integer expid = cotBank.getId();
			if(expid.toString().equals(id)){
				return false;
			}
		}	
		return true;
	}
}
