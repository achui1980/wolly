package com.sail.cot.service.packingorder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotPackingAnys;
import com.sail.cot.domain.CotPackingOrder;
import com.sail.cot.domain.CotPackingOrderdetail;
import com.sail.cot.query.QueryInfo;




public interface CotPackOrderService {
	
	//=======================配件采购=====================================
	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);	
	
	//查询记录
	public List<?> getList(QueryInfo queryInfo);
	
	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request);
	
	// 查询包材厂家
	public Map<?, ?> getPackFactoryNameMap(HttpServletRequest request);
	
	// 查询所有包装方案名称
	public Map<?, ?> getBoxTypeNameMap(HttpServletRequest request);
	
	// 查询所有包材名称
	public Map<?, ?> getPackValueMap(HttpServletRequest request);
	
	// 查询所有包材类型名称
	public Map<?, ?> getPackTypeMap(HttpServletRequest request);
	
	// 查询所有公司
	public Map<?, ?> getCompanyNameMap(HttpServletRequest request);
	
	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap();
	
	//根据编号获得对象
	public CotPackingOrder getPackOrderById(Integer id);
	
	// Action储存PackOrderMap
	public void setPackOrderMapAction(HttpSession session, String rdm,CotPackingOrderdetail cotPackingOrderdetail);
	
	// Action获取GivenMap
	public TreeMap<String, CotPackingOrderdetail> getPackNoMapAction(HttpSession session);
	
	// 通过配件编号修改Map中对应的配件采购明细
	public boolean updateMapValueByPackNo(String rdm, String property,String value);
	
	// 储存PackOrderMap
	public void setPackNoMap(String rdm, CotPackingOrderdetail cotPackingOrderdetail);
	
	// 获取PackOrderMap
	public TreeMap<String, CotPackingOrderdetail> getPackOrderMap();
	
	// 通过key获取PackOrderMap的value
	public CotPackingOrderdetail getPackNoMapValue(String rdm);
	
	// 保存主配件采购单
	public Integer saveOrUpdate(CotPackingOrder cotPackingOrder, String orderDate,String sendDate);
	
	// 更新配件采购明细
	public Boolean modifyPackOrderDetail(List<CotPackingOrderdetail> detailList);
	
	// 清空PackOrderMap
	public void clearPackNoMap();
	
	// 清除PackOrderMap中PackNo对应的映射
	public void delPackNoMapByKey(String rdm);
	
	// 在Action中清除PackOrderMap中PackNo对应的映射
	public void delPackOrderMapByKeyAction(String rdm, HttpSession session);
	
	//根据编号获得对象
	public CotPackingOrderdetail getPackingDetailById(Integer id);
	
	// 根据编号获得对象
	public CotPackingAnys getPackingAnysById(Integer id);
	
	//根据送样明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids);
	
	// 删除包材采购主单
	public Integer deletePackOrderList(List<?> orderIds);
	
	//删除主单关联的其他费用
	public void delOtherFee(Integer packorderId);
	
	//修改配件分析表中的id及分析状态
	public void modifyPackIdAndFlag(Integer packDetailId);
	
	//根据主采购id修改配件分析表中的id及分析状态
	public void modifyIdAndFlagByOrderId(Integer packOrderId);
	
	//根据主采购id修改总金额
	public void modifyPackOrderAmount(Integer packOrderId,Double total);
	
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
	public Integer getDealNumById(Integer packorderId);
	
	//判断生产合同是否有应付帐款记录
	public String checkIsHaveDeal(List<CotPackingOrder> orderList);
	
	//计算单个价格
	public Double calOnePrice(Integer detailId,Float sizeL,Float sizeW,Float sizeH);
	
	//=======================配件采购(结束)=====================================
	
	// 根据订单主单编号获得明细
	public List getEleIdsFromOrderId(Integer orderId);
	
	// 根据包材资料
	public List getBoxPackingById(Integer detailId,Integer boxPackingId);
	
	// 根据包装类型获得包材资料
	public List getBoxPackingsByType(Integer type);
	
	// 保存
	public void addList(List<?> list);
	
	// 修改
	public void updateList(List<?> list);
	
	// 删除
	public void deleteList(List<Integer> ids);
	
	//根据分析对象编号和长宽高重新计算单价
	public Double getNewPrice(Integer anyId,String size,Float value);
	
	// 将包材采购分析生成包材采购单,(ids有值只处理勾选的分析数据)
	public Boolean savePackOrderByAnys(Integer orderId, String ids);
	
	
	//根据订单编号取得订单号
	public String getOrderNoByOrdeId(Integer orderId);
	
	// 修改订单审核状态
	public void updateOrderStatus(Integer orderId, Integer orderStatus);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	// 编辑包材时计算单价
	public Double getPackPriceByType(CotOrderDetail detail, Integer boxPackingId);
	
	// 查找包材麦标PicImg
	public byte[] getPicImgByOrderId(Integer fkId);
	
	// 删除唛标明细图片
	public boolean deleteMBPicImg(Integer orderId);
	
	//从订单拷贝唛头信息
	public String[] updatePackMb(Integer packId)throws DAOException;
	
	// 更新麦标图片
	public void updateMBImg(String filePath, Integer mainId);
	
	// 分析订单明细集合,生成包材采购分析数据
	public boolean savePackAnysAgain(Integer orderId);
	
	//判断其他费用是否存在
	public boolean findIsExistName(String name,Integer orderId,Integer recId);
	
	// 更新包材采购的实际金额
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
	public Integer findIsExistPackOrderNo(String packingOrderNo, String id);
}
