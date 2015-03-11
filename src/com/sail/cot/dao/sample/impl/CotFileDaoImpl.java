package com.sail.cot.dao.sample.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.jason.core.dao.PaginationSupport;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.sample.CotFileDao;
import com.sail.cot.query.QueryInfo;

public class CotFileDaoImpl extends CotBaseDaoImpl implements CotFileDao {

	public void deleteByName(String name) {
		 
	      
	     
	     String hql = "select obj.id from CotFile obj where obj.fileName=?";

	     List<?> ids=this.getHibernateTemplate().find(hql, name);
	     
	     try {
			this.deleteRecordByIds(ids, "CotFile");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}

}
