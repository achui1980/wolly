/**
 * 
 */
package com.sail.cot.dao.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.systemdic.CotBalanceTypeDao;

public class CotBalanceTypeDaoImpl extends CotBaseDaoImpl implements
		CotBalanceTypeDao {

	/**
	 * 判断费用结算方式名称是否重复
	 * 
	 * @param givenName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistBalanceName(String balanceName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select obj.id from CotBalanceType obj where obj.balanceName='"
						+ balanceName + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}
}
