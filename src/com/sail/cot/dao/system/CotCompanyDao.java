package com.sail.cot.dao.system;
import com.sail.cot.dao.CotBaseDao;

public interface CotCompanyDao extends CotBaseDao {
	
	 
      boolean isNotExistCompanyName(String companyName);
}
