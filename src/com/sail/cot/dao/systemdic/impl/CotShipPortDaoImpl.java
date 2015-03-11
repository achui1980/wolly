/**
 * 
 */
package com.sail.cot.dao.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.systemdic.CotShipPortDao;

public class CotShipPortDaoImpl extends CotBaseDaoImpl implements
		CotShipPortDao {

	/**
	 * 判断起运港名称是否重复
	 * 
	 * @param shipPortName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistShipPortName(String shipPortName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select obj.id from CotShipPort obj where obj.shipPortNameEn='"
						+ shipPortName + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}
}
