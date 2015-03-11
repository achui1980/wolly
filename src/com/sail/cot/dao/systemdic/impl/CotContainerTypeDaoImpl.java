/**
 * 
 */
package com.sail.cot.dao.systemdic.impl;

import java.util.ArrayList;
import java.util.List;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.systemdic.CotContainerTypeDao;

public class CotContainerTypeDaoImpl extends CotBaseDaoImpl implements
		CotContainerTypeDao {

	/**
	 * 判断集装箱名称是否重复
	 * 
	 * @param containerName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistContainerName(String containerName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select obj.id from CotContainerType obj where obj.containerName='"
						+ containerName + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}
}
