
package com.sail.cot.service.orderfac;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
  
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderStatus;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.VDetailStatusId;
import com.sail.cot.query.QueryInfo;




public interface CotOrderFacService {
	
	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);
	
	//得到总记录数(JDBC查询)
	public Integer getRecordCountJDBC(QueryInfo queryInfo);
	
	//查询记录
	public List<?> getList(QueryInfo queryInfo);
	
	//根据条件查询主单记录
	public List<?> getOrderFacList(QueryInfo queryInfo);
	
	//得到objName的集合
	public List<?> getObjList(String objName);
	
	//查找所有订单
	public List<CotOrder> getCotOrderList();
	
	//根据id查找采购单信息
	public CotOrderFac getOrderFacById(Integer id);
	
	//根据采购明细id查找采购明细单信息
	public CotOrderFacdetail getOrderFacDetailById(Integer id);
	
	//根据采购明细id查找采购图片对象
	public CotOrderfacPic getOrderFacPicByDetailId(Integer detailId) ;
	
	// 判断该主单的明细是否已存在该产品货号
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer detailId);
	
	//查询采购单单号是否存在
	public Integer findIsExistOrderNo(String orderNo,String id);
	
	// 根据订单编号字符串组装采购明细
	public List<CotOrderFacdetail> findOrderFacDetailByIds(String ids); 
	
	// 根据订单明细id字符串组装采购明细，并修改订单未采购数量
	public List<CotOrderFacdetail> updateOrderDetailForFac(String ids);
	
	// 根据采购主单id查找采购明细
	public List<CotOrderFacdetail> getFacDetailByOrderId(Integer orderId); 
	
	//保存或者更新主采购单
	public Integer saveOrUpdateOrderFac(CotOrderFac cotOrderFac,String orderTime,String sendTime, String shipmentDate,String addTime,boolean flag, String oderFacText);
	
	//更新主采购单
	public void updateOrderFac(CotOrderFac cotOrderFac);
	
	//修改主单的总数量,总箱数,总体积,总金额
	public void modifyFacTotalByMap(HashMap<Integer, Integer> map);
	
	// 修改主单的总数量,总箱数,总体积,总金额
	public Double modifyCotOrderFacTotal(Integer orderId);
	
	// 查询采购单的总金额
	public Float findTotalMoney(Integer orderId);
	
	// 删除主采购单
	public Boolean deleteOrderFacs(List<CotOrderFac> orderFacList);
	
	//删除主单关联的其他费用
	public void delOtherFee(Integer orderId);
	
	// 保存采购单的产品明细
	public Boolean addOrderFacDetails(List<CotOrderFacdetail> details);
	
	// 更新采购单明细
	public Boolean modifyOrderFacDetail(CotOrderFacdetail e, String eleProTime);
	
	// 批量更新采购单的产品明细
	public Boolean modifyOrderFacDetails(List<CotOrderFacdetail> details);
	
	// 根据采购明细产品的id删除
	public boolean deleteDetailByIds(List<Integer> ids);
	
	//通过id获取订单明细对象
	public CotOrderDetail getOrderDetailById(Integer orderDetailId);
	
	//更新订单明细对象
	public boolean updateCotOrderDetail(CotOrderDetail cotOrderDetail);
	
	//通过id获取订单中的未采购数量
	public Long getUnBoxCount4OrderFacById(Integer orderDetailId);
	
	//查询所有厂家名称
	public Map<?, ?> getFactoryNameMap();
	
	// 查询所有厂家
	public Map<?, ?> getFactoryNameMapAction(HttpServletRequest request);
	
	// 查询所有公司
	public Map<?, ?> getCompanyNameMap();
	
	//查询所有订单号
	public Map<?, ?> getOrderNoMap();
	
	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap();
	
	// 查询所有币种
	public Map<?, ?> getCurrencyMap();
	
	//获取默认公司ID
	public Integer getDefaultCompanyId();
	
	//从数据字典中取生产合同合同条款
    public String getContract();
	
	//判断货号字符串是否已经添加
	public List<String> checkExistList(String detailIds);
	
	//判断货号是否已经添加
	public boolean checkExist(String detailId);
	
	//清空Map
	public void clearMap();
	
	//清空(addMap)
	public void clearAddMap() ;
	
	//通过key获取Map的value 
	public CotOrderFacdetail getOrderFacMapValue(String detailId);
	
	// 通过key获取(addMap)的value
	public CotOrderFacdetail getAddMapValue(String orderDetailId);
	
	//储存Map
	public  void setOrderFacMap(String detailId,CotOrderFacdetail cotOrderFacdetail);
	
	// 储存(addMap)
	public void setAddMap(String orderDetailId, CotOrderFacdetail cotOrderFacdetail);
	
	// 通过detailId修改Map中对应的采购明细
	public boolean updateMapValueByDetailId(String detailId ,String property,String value);
	
	// 通过orderDetailId修改(addMap)中对应的采购明细
	public boolean updateAddMap(String orderDetailId, String property, String value);
	
	// 通过货号修改Map中对应的征样明细
	public boolean updateMapValueByEleId(String eleId, String property,
			String value);
	
	// 通过addBoxCount修改addMap中相应的值
	public boolean updateAddMapByAddBoxCount(String orderDetailId,Integer addBoxCount);
	
	//清除Map中detailId对应的映射
	public void delMapByKey(String detailId);
	
	//清除(addMap)中orderDetailId对应的映射;
	public void delAddMapByKey(String orderDetailId);
	
	//Action获取Map
	public  HashMap<String, CotOrderFacdetail> getMapAction(HttpSession session);
	
	//Action获取(addMap)
	public  HashMap<String, CotOrderFacdetail> getAddMapAction(HttpSession session);
	
	//Action储存Map
	public  void setMapAction(HttpSession session,String detailId,CotOrderFacdetail cotOrderFacdetail);
	
	//Action储存(addMap)
	public  void setAddMapAction(HttpSession session,String detailId,CotOrderFacdetail cotOrderFacdetail);
	
	//在Action中清除Map中detailId对应的映射
	public void delMapByKeyAction(String detailId,HttpSession session);
	
	//在Action中清除(addMap)中orderDetailId对应的映射
	public void delAddMapByKeyAction(String orderDetailId,HttpSession session);
	
	//判断该采购明细是否已存在，并将添加的对象储存到后台addMap中
	public Integer saveToMap(CotOrderFacdetail detail);
	
	//根据住单编号查找唛标图片（orderMBImg）
	public byte[] getFacMbByOrderFacId(Integer orderfacId);
	
	//更新采购单唛标图片
	public void updateMBImg(String filePath,Integer orderfacId,HttpServletRequest request);
	
	//获得暂无图片的图片字节
	public byte[] getZwtpPic();
	
	//根据明细货号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId);
	
	//删除明细图片picImg
	public boolean deletePicImg(Integer detailId);
	
	//更新图片picImg字段
	public void updatePicImg(String filePath,Integer detailId);
	
	//同步到订单
	public void updateToOrder(String facdetailIds);
	
	//生成应付帐款单号
	public String createDealNo(Integer facId);
	
	//保存应付帐款单号
	public void savaSeq();
	
	//保存应付帐款
	public void saveAccountdeal(CotFinaceAccountdeal dealDetail,String amountDate,String priceScal,String prePrice);
	
	//根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id);
	
	// 保存其他费用
	public Boolean addOtherList(List<?> details);
	
	//更新其他费用
	public Boolean updateOtherList(List<?> details);
	
	//删除其他费用
	public Boolean deleteOtherList(List<?> details);
	
	//删除应付帐款
	public Boolean deleteDealList(List<?> details);
	
	//获取当前登陆员工信息
	public Boolean checkCurrEmpsIsSuperAd();
	
	//批量删除时对审核通过单据的处理
	public String isOrderStatus(List<CotOrderFac> orderfacList);
	
	//更新送样审核状态
	public int saveOrderStatus(Integer id,Long status);
	
	// 查询采购单其他费用的总金额
	public Double findTotalOtherFee(Integer orderId);
	
	//修改其他费用状态 0：未生成 1: 已生成
	public void modifyFinOtherStatus(Integer fkId,String finaceName,String flag);
	
	//根据编号获得对象
	public CotFinaceAccountdeal getGivenDealById(Integer id);
	
	//判断应付帐款是否有冲帐明细
	public String checkIsHaveDetail(Integer[] ids);
	
	//根据id判断该生产合同是否已存在应付帐款
	public Integer getDealNumById(Integer orderfacid);
	
	//判断生产合同是否有应付帐款记录
	public String checkIsHaveDeal(List<CotOrderFac> orderfacList);
	
	//将采购合同id及单号存入订单明细中
	public void saveIdAndNoToOrderDetail(Integer orderfacId);
	
	//判断应付帐款是否有流转记录
	public String checkIsHaveTrans(Integer[] ids);
	
	//判断其他费用是否已导入订单
	public String checkIsInputOrder(Integer[] ids);
	
	//根据id判断该生产合同的其他费用是否导入订单
	public Integer getOtherNumById(Integer orderfacid);
	
	// 根据id判断该生产合同的其他费用是否导入出货
	public Integer getOtherOutNumById(Integer orderfacid);
	
	//判断生产合同的其他费用是否导入订单
	public String checkIsOtherToOrder(List<CotOrderFac> orderfacList);
	
	// 判断生产合同的其他费用是否导入出货
	public String checkIsOtherToOrderOut(List<CotOrderFac> orderfacList);
	
	//获取订单分解后的采购单id
	public List<Integer> getOrderFacId(HashMap<Integer, Integer> map);
	
	//根据订单明细id获取采购单id
	public String getOrderFacIdsByDetailId(Integer detailId);
	
	//根据订单id获取采购单ids
	public String getOrderFacIds(Integer orderId);
	
	//判断是否已存在该厂家的采购单
	public boolean checkIsFactory(Integer factoryId,Integer orderId);
	
	// 计算价格
	public Float[] calPriceAll(CotOrderFacdetail elements);
	
	//重新计算序列号
	public boolean updateSortNo(Integer id, Integer type, String field,
			String fieldType);
	
	/********************************************************************************
	 * 修改于：2010.01.22
	 * 描述：用随机数作为key操作Map
	 * 添加人：chi_ch
	*/
	
	// Action储存(orderfacMap)
	public void setMapByRdmAction(HttpSession session, String rdm,CotOrderFacdetail cotOrderFacdetail);
	
	// Action获取(orderfacMap)
	public HashMap<String, CotOrderFacdetail> getMapByRdmAction(HttpSession session);
	
	// 在Action中清除orderfacMap中rdm对应的映射
	public void delMapByRdmAction(String rdm, HttpSession session);
	
	//储存orderfacMap
	public  void setOrderFacMapValueByRdm(String rdm,CotOrderFacdetail cotOrderFacdetail);
	
	// 通过key获取(orderfacMap)的value
	public CotOrderFacdetail getOrderFacMapValueByRdm(String rdm);
	
	// 通过rdm修改orderfacMap中对应的采购明细
	public boolean updateOrderFacMapValueByRdm(String rdm, String property,String value);
	
	//清除(orderfacMap)中orderDetailId对应的映射;
	public void delOrderFacMapByRdm(String rdm);
	
	//清空(orderfacMap)
	public void clearOrderFacMap() ;
	
	// 通过addBoxCount修改orderfacMap中相应的值
	public boolean updateOrderFacMapByAddBoxCount(String rdm,Integer addBoxCount);
	
	/********************************************************************************/
	
	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String rdm, String packingPrice);
	
	//通过订单明细获取主订单id
	public Integer getOrderIdByDetailId(Integer detailId);
	
	//获取定单ids
	public List<Integer> getCotOrderIds(Integer orderfacId);
	
	//从订单中导入唛头信息
	public boolean updateCotOrderFacMb(Integer orderfacId,Integer orderId);
	
	//获取采购主单厂家id,用于比较是否订单明细的厂家
	public Integer getOrderfacFactoryId(Integer orderid);
	
	//查询采购明细中是否有该订单明细的采购记录
	public CotOrderFacdetail isExistOrderFacDetail(CotOrderDetail detail);
	
	//更新中文品名
	public boolean updateOrderFacDetail(Integer orderfacDetailId,String eleName);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	// 查找该产品是否有订单
	public Object findIsExistDetail(String id);
	
	// 判断要更新到订单的明细货号哪些重复
	public Map<String, List<String>> findIsExistInOrder(String[] key);
	
	// 同步相同的订单的图片
	public Object[] getOrderByEle(String rdm, CotOrderPic cotOrderPic);
	
	// 根据采购的图片转换成订单图片
	public CotOrderPic changeOrderPic(Integer id, CotOrderPic pic);
	
	// 根据同步选择项同步更新订单
	public CotOrderDetail setOrderByTong(CotOrderDetail old,
			CotOrderFacdetail newEle, String eleStr, String boxStr, String otherStr);
	
	// 同步不同的订单明细的图片
	public Map getOrderByDisEles(Map<String, String> disMap);
	
	// 根据订单明细货号字符串查询明细
	public void updateToOrderDetail(String[] same, String[] sameRdm, String[] dis,
			String[] disRdm, String eleStr, String boxStr, String otherStr,
			boolean isPic);
	//更加单号分解订单,批量
	public void savaOrderFacByEleIdList(List<String> eleIdList, Integer orderId);
	//更加单号分解订单个
	public void savaOrderFacByEleId(Integer eleId,Integer orderId);
	
	//删除麦标
	public boolean deleteMBPicImg(Integer orderfacId);
	
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
	
	// 保存应付帐款
	public void saveAccountDeal(CotFinaceAccountdeal recvDetail,
			String amountDate, String priceScal, String prePrice);
	
	// 更新产品标图片
	public void updateProMBImg(String filePath, Integer mainId);
	
	//查找产品标PicImg
	public byte[] getProPicImgByOrderId(Integer custId);
	
	// 根据编号获得对象
	public CotOrderStatus getOrderStatusById(Integer id);
	
	//生产合同分解
	public Map saveOrderFacByDecomposeOrder(Integer orderId) throws Exception;
	
	//更新单据状态
	public Integer[] updateOrderStatusCon(Integer orderfacId,Integer orderId) throws Exception;

	// 修改审核状态
	public Timestamp updateOrderStatus(Integer orderId, Integer orderStatus,Integer checkPerson) throws DAOException;
	//通过订单ID获得客户对象
	public CotCustomer getCustByOrderId(Integer orderId);
	//返回审核数
	public Integer getCountPO();
	
	// 根据id查找采购单信息
	public Integer getCanOutByOrderId(Integer orderId);
	
	//删除已经存在的采购单明细
	public void deleteFacDetail(Integer orderId);
	
	public List<?> getOrderFacVO(QueryInfo queryInfo);
	
	//给5大标签的厂家评分
	public void updateOrderFacFen(Integer orderFacId,String status,Integer fen,String remark,Integer man) throws DAOException;
	
	// 修改
	public void updateList(List<?> list);
	
	public void createArtWork(Integer orderId) throws Exception;
	
	public void updateAddPerson() throws DAOException;
	
	public boolean getApprove(Integer orderfacId);
	
	public void doComment(Integer orderFacid,String comment,String approve,String type) throws Exception;

	public void confirmByPassword(Integer orderfacId,String type) throws Exception;
	
	public void saveCopy(String orderfacJson) throws Exception;
	
	public void saveDetailCopy(Integer orderfacId) ;
}
