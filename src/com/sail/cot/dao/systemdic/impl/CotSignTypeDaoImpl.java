/**
 * 
 */
package com.sail.cot.dao.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.systemdic.CotSignTypeDao;

public class CotSignTypeDaoImpl extends CotBaseDaoImpl implements
		CotSignTypeDao {

	/**
	 * 判断征样方式名称是否重复
	 * 
	 * @param signName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistSignName(String signName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select obj.id from CotSignType obj where obj.signName='"
						+ signName + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}
}
