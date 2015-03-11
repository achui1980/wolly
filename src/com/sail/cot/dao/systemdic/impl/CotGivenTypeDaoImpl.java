/**
 * 
 */
package com.sail.cot.dao.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.systemdic.CotGivenTypeDao;

public class CotGivenTypeDaoImpl extends CotBaseDaoImpl implements
		CotGivenTypeDao {

	/**
	 * 判断送样方式名称是否重复
	 * 
	 * @param givenName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistGivenName(String givenName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select obj.id from CotGivenType obj where obj.givenName='"
						+ givenName + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}
}
