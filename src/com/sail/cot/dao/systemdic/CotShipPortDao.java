/**
 * 
 */
package com.sail.cot.dao.systemdic;

import com.sail.cot.dao.CotBaseDao;

public interface CotShipPortDao extends CotBaseDao {
	
	/**
     * 判断起运港名称是否重复
     * @param shipPortName
     * @return
     */
    public Integer isExistShipPortName(String shipPortName);

}
