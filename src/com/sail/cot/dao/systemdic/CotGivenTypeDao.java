/**
 * 
 */
package com.sail.cot.dao.systemdic;

import com.sail.cot.dao.CotBaseDao;

public interface CotGivenTypeDao extends CotBaseDao {
	
	/**
     * 判断送样方式名称是否重复
     * @param givenName
     * @return
     */
    public Integer isExistGivenName(String givenName);

}
