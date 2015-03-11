package com.sail.cot.dao.system.impl;

import java.util.ArrayList;
import java.util.List;


import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.system.CotCompanyDao;
import com.sail.cot.domain.CotCompany; 

public class CotCompanyDaoImpl extends CotBaseDaoImpl implements CotCompanyDao {


	@SuppressWarnings("unchecked")
	public boolean isNotExistCompanyName(String companyName) {
		 
		List<CotCompany> res = new ArrayList<CotCompany>();
		res = super.find("from CotCompany c where c.companyName='"+companyName+"'");
		if(res.size()!=1){
			return true;
		}else{
			return false;
		}
	}
  
	
}
