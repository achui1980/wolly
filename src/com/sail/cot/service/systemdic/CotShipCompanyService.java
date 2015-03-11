/**
 * 
 */
package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotShipCompany;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.BaseTypeSerivce;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 23, 2009 2:41:30 PM </p>
 * <p>Class Name: CotBank.java </p>
 * @author achui
 *
 */
public interface CotShipCompanyService extends BaseTypeSerivce{
	
	public CotShipCompany getShipCompanyById(Integer id);
	
	public boolean findExistByName(String name);
	
	public List getShipCompanyList() ;
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	
}
