/**
 * 
 */
package com.sail.cot.service.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotShipCompany;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.impl.BaseTypeServiceImpl;
import com.sail.cot.service.systemdic.CotShipCompanyService;

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
public class CotShipCompanyServiceImpl extends BaseTypeServiceImpl implements CotShipCompanyService {

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
			CotShipCompany bank = (CotShipCompany)objList.get(i);
			ids.add(bank.getId());
			res = 0;
		}
		try {
			this.getBaseDao().deleteRecordByIds(ids, "CotShipCompany");
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
	public CotShipCompany getShipCompanyById(Integer id) {
		return (CotShipCompany)this.getObjById(id, CotShipCompany.class);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotBankService#findExistByName(java.lang.String)
	 */
	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotShipCompany obj where obj.companyName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotShipCompany obj where obj.companyName='"+name+"'");
		try {
			int count = this.getBaseDao().getRecordsCount(queryInfo);
			if(count>0)
			{
				isExist = true;
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		return isExist;
	}
	
	public List getShipCompanyList() {
		return this.getBaseDao().getRecords("CotShipCompany");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.systemdic.CotShipCompanyService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getBaseDao().getJsonData(queryInfo);
	}

}
