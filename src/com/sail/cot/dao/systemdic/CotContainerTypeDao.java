/**
 * 
 */
package com.sail.cot.dao.systemdic;

import com.sail.cot.dao.CotBaseDao;

public interface CotContainerTypeDao extends CotBaseDao {
	
	/**
     * 判断集装箱名称是否重复
     * @param containerName
     * @return
     */
    public Integer isExistContainerName(String containerName);

}
