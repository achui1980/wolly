
package com.sail.cot.service.systemdic;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotDays;
import com.sail.cot.query.QueryInfo;
/**
 * *********************************************
 * @Copyright :(C),2008-2010
 * @CompanyName :厦门市旗航软件有限公司(www.xmqh.net)
 * @Version :1.0
 * @Date :2011-6-8
 * @author : azan
 * @class :CotDaysService.java
 * @Description :交货日期延长期限
 */
 
public interface CotDaysService {
	
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public void saveOrUpdate(CotDays cotDays);
	
	public void deleteDays(List ids) throws DAOException;
	
	/**
	 * 描述:查询起运港和目的港是否已存在
	 * @param shipId
	 * @param tarId
	 * @return boolean
	 */
	public boolean checkIsExist(Integer shipId,Integer tarId,Integer id);
}
