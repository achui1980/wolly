/**
 * 
 */
package com.sail.cot.service.finace;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinacerecv;
import com.sail.cot.domain.CotFinacerecvDetail;
import com.sail.cot.query.QueryInfo;


public interface CotFinacerecvService {

	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);

	//得到集合
	public List<?> getList(QueryInfo queryInfo);
	
	// 保存其他费用
	public Boolean addList(List<?> details);
	
	// 根据币种换算价格
	public Double updatePrice(HttpServletRequest request, Double price,
			Integer oldCurId, Integer newCurId);
	
	// 保存
	public Boolean addRecvList(HttpServletRequest request,List<?> details);
	
	//更新其他费用
	public Boolean updateList(HttpServletRequest request,List<?> details,Map<Integer,Double> map);
	
	//删除其他费用
	public Boolean deleteList(HttpServletRequest request,List<?> details);
	
	//删除其他费用
	public Boolean deleteByIds(List<?> ids);
	
	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);
	
	//查询所有员工
	public Map<?, ?> getEmpsMap();
	
	// 查询所有客户
	public Map<?, ?> getCustNameMap();
	
	// 查询付款方式
	public Map<?, ?> getPayTypeNameMap();
	
	//删除记录
	public int deleteFinacerecvs(List<Integer> list);
	
	//根据id获取收款记录信息
	public CotFinacerecv getFinacerecvById(Integer id);
	
	// 根据id获取冲帐记录信息
	public CotFinacerecvDetail getFinacerecvDetailById(Integer id);
	
	// 根据id获取应收帐记录
	public CotFinaceAccountrecv getRecvById(Integer id,Integer newCur);
	
	// 保存或者更新收款记录
	public Integer saveOrUpdateRecv(CotFinacerecv cotFinacerecv, String finaceRecvDate);
	
	//保存主单金额
	public boolean saveFinacerecvByMoney(String remainAmount,Integer id);
	
	//生成单号
//	public String createFinaceNo(Integer custId);
	
	// 查询收款单单号是否存在
	public Integer findIsExistFinaceNo(String finaceNo, String id);
	
	//生成溢收账
	public Double setYiMonry(Integer mainId);
	
	//flag=0为可以删除,1为溢收款转到出货,2为预收货款有金额流到出货
	public Integer findIsYiOut(Integer id,Integer custId);
	
	//查询应收帐的未流转金额
	public Float getZhRemainAmountByRecv(Integer recvId);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
}
