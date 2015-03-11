/**
 * 
 */
package com.sail.cot.service.audit;

import java.util.List;
 
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotAudit;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.query.QueryInfo;
 
public interface CotAuditService {
	
	//获取单据列表
	List<?> getCotAuditList();
	
	//获取单据信息
	CotAudit getCotAuditById(Integer Id);
	
	//添加修改单据信息
	void addOrSaveCotAudit(List<CotAudit> cotAuditList);
	 
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	public List<?> getAuditList(QueryInfo queryInfo);
	
	//生成核销单
	public boolean saveAudit(String auditNo,Integer auditNum, String receiveDate, String effectDate);
	 
	//获得当前登陆员工
	public CotEmps getEmps();
	
	//处理核销单
	public int savaOrUpdateAudit(CotAudit cotAudit);
	
	//根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id);
	
	//根据币种编号获得币种名称
	public String[] getCurrencyNameById(Integer id);
	
	//根据出货编号查找出货信息
	public CotOrderOut getOrderOutById(Integer id);
	
	//检查是否超过核销日期
	public void updateStatus(String curDate);
	
	//计算退税金额
	public Float calTotalTuiLv(String orderNo);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
