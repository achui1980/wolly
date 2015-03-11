/**
 * 
 */
package com.sail.cot.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * <p>Title: 旗航ERP管理系统（QHERP）</p>
 * <p>Description:批量更是对象的某个属性</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * <p>Create Time: Mar 4, 2011 9:05:29 AM </p>
 * <p>Class Name: BatchUpdatePropertyCallBack.java </p>
 * @author achui
 * 
 */
public class BatchUpdatePropertyCallBack  implements HibernateCallback{
	
	/**
	 * 更新的语句
	 */
	private String updateSql;
	/**
	 * 更新的参数列表
	 */
	private Map paramsMap;
	/**
	 * @param updateSql
	 * @param paramsMap
	 */
	public BatchUpdatePropertyCallBack(String updateSql, Map paramsMap) {
		this.updateSql = updateSql;
		this.paramsMap = paramsMap;
	}
	/* (non-Javadoc)
	 * @see org.springframework.orm.hibernate3.HibernateCallback#doInHibernate(org.hibernate.Session)
	 */
	public Object doInHibernate(Session session) throws HibernateException,
			SQLException {
		Query query = session.createQuery(this.updateSql);
		if (this.paramsMap != null) {
			Iterator iterator = this.paramsMap.keySet().iterator();
			while (iterator.hasNext()) {
				String namedParms = (String) iterator.next();
				Object object = this.paramsMap.get(namedParms);
				if (object instanceof List) {
					List list = (List) object;
					query.setParameterList(namedParms, list);
				} else
					query.setParameter(namedParms, this.paramsMap.get(namedParms));
			}
		}
		Integer result = query.executeUpdate();
		session.flush();
		session.clear();
		return new Integer(result);
	}
	
	
}
