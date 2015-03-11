/**
 * 
 */
package com.sail.cot.dao.system.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.system.CotEmpsDao;
import com.sail.cot.domain.CotDept;
import com.sail.cot.domain.CotPopedom;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:58:48 PM </p>
 * <p>Class Name: CotEmpsDaoImpl.java </p>
 * @author achui
 *
 */
public class CotEmpsDaoImpl extends CotBaseDaoImpl implements CotEmpsDao {

	/**
     *  根据公司编号查询所有部门信息
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<CotDept> queryDeptListByCompanyId(Integer companyId) {
		return super.find("from CotDept c where c.companyId='"+companyId+"'and c.deptStatus=1" );
	}
	
    /**
     * 判断员工编号是否重复
     * @param empsId
     * @return
     */
    @SuppressWarnings("unchecked")
	public Integer isExistEmpsId(String empsId){
    	List<Integer> res = new ArrayList<Integer>();
		res = super.find("select c.id from CotEmps c where c.empsId='"+empsId+"'");
		if(res.size()!=1){
			return null;
		}else{
			return res.get(0);
		}
    }
    
    /**
     * 根据员工编号删除权限表
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
	public void deletePopedomByEmpsId(Integer id){
    	List<Integer> res = new ArrayList<Integer>();
		res = super.find("from CotPopedom c where c.empsId="+id);
		List<Integer> ids = new ArrayList<Integer>();
		Iterator<?> it =res.iterator();
		while(it.hasNext()){
			CotPopedom cotPopedom = (CotPopedom)it.next();
			ids.add(cotPopedom.getId());
		}
		if(ids.size()!=0){
			try {
				super.deleteRecordByIds(ids, "CotPopedom");
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
    }

	/* (non-Javadoc)
	 * @see com.sail.cot.dao.system.CotEmpsDao#copyPopedom(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void copyPopedom(Integer empsId_from, Integer empsId_to) {
		// TODO Auto-generated method stub
		String sql ="{call sp_copypopedom(?,?)}";
		Integer[] values = new Integer[2];
		values[0] =empsId_from;
		values[1] =empsId_to;
		//this.getCotBaseDao().callProc(sql, values);
		try {
			callProc(sql, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
