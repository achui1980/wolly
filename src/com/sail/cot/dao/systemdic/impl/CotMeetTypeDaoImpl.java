/**
 * 
 */
package com.sail.cot.dao.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.systemdic.CotMeetTypeDao;

public class CotMeetTypeDaoImpl extends CotBaseDaoImpl implements
		CotMeetTypeDao {

	/**
	 * 判断送样方式名称是否重复
	 * 
	 * @param meetName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistMeetName(String meetName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select obj.id from CotMeetType obj where obj.meetName='"
						+ meetName + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}
}
