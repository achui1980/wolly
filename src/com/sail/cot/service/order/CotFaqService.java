/**
 * 
 */
package com.sail.cot.service.order;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotAnwser;
import com.sail.cot.domain.CotQuestion;
import com.sail.cot.query.QueryInfo;


public interface CotFaqService {
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public void saveOrUpdateQuestion(CotQuestion question) throws DAOException;
	
	public void saveOrUpdateAnswer(CotAnwser anwser) throws DAOException;
	
	public void deleteQuestion(List  ids) throws DAOException;
	
	public void deleteAnswer(List  ids) throws DAOException;
	
	//过滤掉orderFac中5个状态为1的记录
	public List getNoApproveData(QueryInfo queryInfo) throws DAOException;
	
	public String getListData(List list, String flag)
			throws DAOException;
}
