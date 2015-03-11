/**
 * 
 */
package com.sail.cot.service.backtax;

import java.util.List;
import java.util.Map;
 
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotAudit;
import com.sail.cot.domain.CotBacktax;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
 
public interface CotBackTaxService {
	
	//获取退税单列表
	List<?> getCotBackTaxList();
	
	//获取单据列表
	public List<?> getAuditList(Integer txtId);
	
	//获取退税单信息
	CotBacktax getCotBackTaxById(Integer Id);
	
	//添加修改退税单信息
	void addOrSaveCotBackTax(List<CotBacktax> backTaxList);
	 
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	//获得当前登陆员工
	public CotEmps getEmps();
	
	//处理退税单
	public int savaOrUpdateBackTax(CotBacktax cotBackTax,String taxDate,String[] auditNos);
	
	//查询所有用户姓名
	public Map<?, ?> getEmpsMap();
	
	public CotAudit getCotAuditById(Integer Id);
	
	//删除退税单
	public int deleteBackTax(List<CotBacktax> backtaxList);
	
	//判断退税单是否存在
	public Integer findExistByNo(String taxNo);
	
	//判断退税单是否已包含
	public List<?> isExistAuditNo(Integer id);
	
	//退税单中删除核销单时修改核销表中的taxId为空
	public boolean modifyTaxId(Integer taxId,List ids);
	
	/**
	 * 描述：
	 * @param queryInfo
	 * @return
	 * @throws DAOException
	 * 返回值：String
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
