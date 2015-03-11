/**
 * 
 */
package com.sail.cot.service;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotOrg;
import com.sail.cot.query.QueryInfo;


public interface CotOrgService {
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public Integer saveOrg(CotOrg e) throws DAOException;
	
	public Boolean deleteOrgs(List<Integer> ids) throws DAOException;
	
	public CotOrg getOrgById(Integer id);
}
