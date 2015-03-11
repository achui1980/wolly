/**
 * 
 */
package com.sail.cot.dao.systemdic;

import com.sail.cot.dao.CotBaseDao;

public interface CotSignTypeDao extends CotBaseDao {
	
	/**
     * 判断征样方式名称是否重复
     * @param signName
     * @return
     */
    public Integer isExistSignName(String signName);

}
