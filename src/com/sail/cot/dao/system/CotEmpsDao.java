/**
 * 
 */
package com.sail.cot.dao.system;

import java.util.List;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotDept;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:55:16 PM </p>
 * <p>Class Name: CotEmpsDao.java </p>
 * @author achui
 *
 */
public interface CotEmpsDao extends CotBaseDao {
	
    /**
     * 查询所有部门信息
     * @return
     */
    public List<CotDept> queryDeptListByCompanyId(Integer companyId);
    
    /**
     * 判断员工编号是否重复
     * @param empsId
     * @return
     */
    public Integer isExistEmpsId(String empsId);
    
    /**
     * 根据员工编号删除权限表
     * @param id
     * @return
     */
    public void deletePopedomByEmpsId(Integer id);
    
    /**
     * 复制用户权限
     * @param empsId_from
     * @param empsId_to
     */
    public void copyPopedom(Integer empsId_from,Integer empsId_to);
}
