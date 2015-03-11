/**
 * @(#) BatchDeleteCallback.java 2007-6-25
 * 
 * Copyright 2006 ptnetwork
 */
package com.sail.cot.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * <p>Title: 预算支撑系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 厦门纵横集团科技股份有限公司</p>
 * @author jasonzhang
 *
 */
public class BatchDeleteCallback implements HibernateCallback {
	private List ids;
	private String objectName;
	
	public BatchDeleteCallback(List ids, String objectName) {
		this.ids = ids;
		this.objectName = objectName;
	}

	public Object doInHibernate(Session session) throws HibernateException, SQLException {
//		StringBuffer sb = new StringBuffer("delete from ");
//		sb.append(objectName).append(" as obj where id in(:ids)");
//		Query query = session.createQuery(sb.toString());
//		query.setParameterList("ids", ids);
//		int result = query.executeUpdate();
		//因为目前使用的weblogic与hibernate3中的新的hql语法有冲突，所以不能使用hibernate3的批量删除功能
//		StringBuffer sb = new StringBuffer("DELETE from ");
//		sb.append(objectName).append(" as obj where ");
//		for (int i=0; i<ids.size(); i++) {
//			sb.append("obj.id = ").append((Integer) ids.get(i));
//			if (i != ids.size() - 1) {
//				sb.append(" or ");
//			}
//		}
//		session.createQuery(sb.toString()).executeUpdate();
		if(ids.size() ==  0) return 0;
		StringBuffer sb = new StringBuffer("from ");
		sb.append(objectName).append(" as obj where ");
		for (int i=0; i<ids.size(); i++) {
			// TODO:
			Object id = ids.get(i);
			if (id instanceof Integer) {
				// TODO:
				sb.append("obj.id = ").append((Integer) ids.get(i));				
				// TODO:
			}else {
				sb.append("obj.id = '").append(id).append("'");
			}
			if (i != ids.size() - 1) {
				sb.append(" or ");
			}
			// TODO:
		}
		sb.append(" order by obj.id");
		int[] resultCodes = new int[ids.size()];
		List records = session.createQuery(sb.toString()).list();
//		for (int i=0; i<records.size(); i++) {
//			try {
//				session.delete(records.get(i));
//				session.flush();
//			} catch (HibernateException e) {
//				resultCodes[i] = -1;
//				continue;
//			}
//			resultCodes[i] = 1;
//			
//		}
		for (int i=0; i<records.size(); i++) {
			session.delete(records.get(i));
			resultCodes[i] = 1;
		}
//		try {
//			session.flush();
//		} catch (Exception e) {
//			resultCodes = new int[1];
//			resultCodes[0] = -1;
//		}
		session.flush();
		return resultCodes;
	}

}
