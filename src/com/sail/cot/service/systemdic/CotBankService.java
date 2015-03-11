/**
 * 
 */
package com.sail.cot.service.systemdic;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotBank;
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
public interface CotBankService extends BaseTypeSerivce{
	
	public CotBank getBankById(Integer id);
	
	public boolean findExistByName(String name);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//判断是否存在 true:存在 false：不存在
	public boolean findExistNameById(String id,String name);
}
