package com.sail.cot.service.fittingorder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFittingOrder;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotFittingsAnys;
import com.sail.cot.domain.CotFittingsOrderdetail;
import com.sail.cot.query.QueryInfo;




public interface CotFitOrderService {
	
	//=======================配件采购=====================================
	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);	
	
	//查询记录
	public List<?> getList(QueryInfo queryInfo);
	
	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request);
	
	// 查询配件厂家
	public Map<?, ?> getFitFactoryNameMap(HttpServletRequest request);
	
	// 查询所有公司
	public Map<?, ?> getCompanyNameMap(HttpServletRequest request);
	
	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap();
	
	//根据编号获得对象
	public CotFittingOrder getFitOrderById(Integer id);
	
	// Action储存FitOrderMap
	public void setFitOrderMapAction(HttpSession session, String rdm,CotFittingsOrderdetail cotFittingsOrderdetail);
	
	// Action获取GivenMap
	public TreeMap<String, CotFittingsOrderdetail> getFitNoMapAction(HttpSession session);
	
	// 通过配件编号修改Map中对应的配件采购明细
	public boolean updateMapValueByFitNo(String rdm, String property,String value);
	
	// 储存FitOrderMap
	public void setFitNoMap(String rdm, CotFittingsOrderdetail cotFittingsOrderdetail);
	
	// 获取FitOrderMap
	public TreeMap<String, CotFittingsOrderdetail> getFitOrderMap();
	
	// 通过key获取FitOrderMap的value
	public CotFittingsOrderdetail getFitNoMapValue(String rdm);
	
	// 保存主配件采购单
	public Integer saveOrUpdate(CotFittingOrder cotFittingOrder, String orderDate,String sendDate);
	
	// 更新配件采购明细
	public Boolean modifyFitOrderDetail(List<CotFittingsOrderdetail> detailList);
	
	// 清空FitOrderMap
	public void clearFitNoMap();
	
	// 清除FitOrderMap中fitNo对应的映射
	public void delFitNoMapByKey(String rdm);
	
	// 在Action中清除FitOrderMap中fitNo对应的映射
	public void delFitOrderMapByKeyAction(String rdm, HttpSession session);
	
	//根据编号获得对象
	public CotFittingsOrderdetail getFittingDetailById(Integer id);
	
	//根据送样明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids);
	
	// 删除配件采购主单
	public Integer deleteFitOrderList(List<?> ids);
	
	//删除主单关联的其他费用
	public void delOtherFee(Integer fitorderId);
	
	//更改默认图片
	public void updateDefaultPic(String filePath,Integer eId) throws Exception;
	
	// 删除配件图片
	public boolean deletePicImg(Integer Id);
	
	//修改配件分析表中的id及分析状态
	public void modifyFitIdAndFlag(Integer fitDetailId);
	
	//根据主采购id修改配件分析表中的id及分析状态
	public void modifyIdAndFlagByOrderId(Integer fitOrderId);
	
	//根据主采购id修改总金额
	public void modifyFitOrderAmount(Integer fitOrderId,Double total);
	
	// 保存其他费用
	public Boolean addOtherList(List<?> details);
	
	//根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id);
	
	//更新其他费用
	public Boolean updateOtherList(List<?> details);
	
	//删除其他费用
	public Boolean deleteOtherList(List<?> details);
	
	// 查询所有币种
	public Map<?, ?> getCurrencyMap();
	
	//根据编号获得对象
	public CotFinaceAccountdeal getGivenDealById(Integer id);
	
	//删除应付帐款
	public Boolean deleteDealList(List<?> details);
	
	//修改其他费用状态 0：未生成 1: 已生成
	public void modifyFinOtherStatus(Integer fkId,String finaceName,String flag);
	
	// 得到objName的集合
	public List<?> getList(String objName);
	
	//生成应付帐款单号
	public String createDealNo(Integer facId);
	
	//保存应付帐款单号
	public void savaDealNoSeq();
	
	//保存应付帐款
	public void saveAccountdeal(CotFinaceAccountdeal dealDetail,String amountDate,String priceScal,String prePrice);
	
	//判断应付帐款是否有冲帐明细
	public String checkIsHaveDetail(Integer[] ids);
	
	//判断应付帐款是否有流转记录
	public String checkIsHaveTrans(Integer[] ids);
	
	//根据id判断该生产合同是否已存在应付帐款
	public Integer getDealNumById(Integer fitorderId);
	
	//判断生产合同是否有应付帐款记录
	public String checkIsHaveDeal(List<CotFittingOrder> orderList);
	
	//=======================配件采购(结束)=====================================
	
	// 保存
	public void addList(List<?> list);

	// 修改
	public void updateList(List<?> list);

	// 删除
	public void deleteList(List<Integer> ids);
	
//	public String createRecvNo(Integer facId, String orderNo, Integer custId);
	
	// 获得配件采购分析
	public CotFittingsAnys getFitAnysById(Integer id);
	
	//将配件采购分析生成配件采购单
	public Boolean saveFitOrderByAnys(Integer order,String ids,boolean flag);
	
	//根据订单主单编号获得明细
	public List getEleIdsFromOrderId(Integer orderId);
	
	//根据样品配件编号获得样品配件信息
	public CotFittings getFittingById(Integer id);
	
	// 分析订单明细集合,生成配件采购分析数据
	public boolean saveFitAnysAgain(Integer orderId);
	
	//根据订单编号取得订单号
	public String getOrderNoByOrdeId(Integer orderId);
	
	// 修改订单审核状态
	public void updateOrderStatus(Integer orderId, Integer orderStatus);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//查询配件
	public CotFittings findFittingByFitNo(String fitNo);
	
	//判断其他费用是否存在
	public boolean findIsExistName(String name,Integer orderId,Integer recId);
	
	// 更新配件采购的实际金额
	public Double modifyRealMoney(Integer orderId);
	
	// 删除其他费用
	public Double deleteByIds(List<?> ids);
	
	// 更新其他费用的导入标识为1,剩余金额为0
	public Boolean updateStatus(String ids);
	
	// 判断应付帐是否导到出货,是否有付款记录
	public List<Integer> checkIsImport(List<Integer> ids);
	
	// 删除应付帐,还原其他其他费用
	public Boolean deleteByAccount(List<?> ids);
	
	// 重新排序
	public boolean updateSortNo(Integer type, String field,
			String fieldType);
	// 查询配件单号是否存在
	public Integer findIsExistFitOrderNo(String fittingOrderNo, String id);
}
