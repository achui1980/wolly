/**
 * 
 */
package com.sail.cot.dao.systemdic;

import com.sail.cot.dao.CotBaseDao;

public interface CotMeetTypeDao extends CotBaseDao {
	
	/**
     * 判断接单方式名称是否重复
     * @param meetName
     * @return
     */
    public Integer isExistMeetName(String meetName);

}
