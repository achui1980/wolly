/**
 * 
 */
package com.sail.cot.service.orderout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFittingOrder;
import com.sail.cot.domain.CotFittingsOrderdetail;
import com.sail.cot.domain.CotHsInfo;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutDel;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotOrderOutdetailDel;
import com.sail.cot.domain.CotOrderOuthsdetail;
import com.sail.cot.domain.CotOrderouthsRpt;
import com.sail.cot.domain.CotPackingOrder;
import com.sail.cot.domain.CotPackingOrderdetail;
import com.sail.cot.domain.CotShipment;
import com.sail.cot.domain.CotSymbol;
import com.sail.cot.domain.VOrderFitorderId;
import com.sail.cot.domain.VOrderOrderfacId;
import com.sail.cot.domain.VOrderPackorderId;
import com.sail.cot.query.QueryInfo;

public interface CotOrderOutService {

	//保存或者更新主出货单
	public Integer[] saveOrUpdateOrderOut(CotOrderOut cotOrderOut,
			CotSymbol cotSymbol, CotHsInfo cotHsInfo,
			CotOrderouthsRpt cotOrderouthsRpt, String orderTime,String orderLcDate, boolean oldFlag);

	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo);

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request);

	//查询记录
	public List<?> getList(QueryInfo queryInfo);
	
	// 查询VO记录
	public List<?> getOrderVOList(QueryInfo queryInfo);

	// 根据条件查询主单记录
	public List<?> getOrderList(QueryInfo queryInfo);

	// 根据条件查询主单记录
	public List<?> getOrderMainList(QueryInfo queryInfo);

	// 根据条件查询明细记录
	public List<?> getOrderDetailList(QueryInfo queryInfo);

	//得到objName的集合
	public List<?> getList(String objName);

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId);

	// 清空明细Map
	public void clearMap(String typeName);

	//得到公司的集合
	public List<?> getCompanyList();

	//得到员工的集合
	public List<?> getEmpsList();

	//查询出货单号是否存在
	public Integer findIsExistOrderNo(String orderNo, String id);

	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap();

	// 查询所有客户编号
	public Map<?, ?> getCustMap();

	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);

	// 查询所有起运港
	public Map<?, ?> getShipPortMap();

	// 查询所有目的港
	public Map<?, ?> getTargetPortMap();

	// 查询所有运输方式
	public Map<?, ?> getTrafficTypeMap();

	// 查询所有出口商
	public Map<?, ?> getCompanyMap();

	// 查询所有厂家
	public Map<?, ?> getFactoryMap();

	// 查询所有国家
	public Map<?, ?> getNationMap();

	// 查询所有价格条款
	public Map<?, ?> getClauseMap();

	// 查询所有集装箱
	public Map<?, ?> getContainerTypeMap();

	// 查询所有佣金类型
	public Map<?, ?> getCommisionTypeMap();

	// 查询所有主订单号
	public Map<?, ?> getAllOrderNoMap();

	// Action储存出货明细Map
	public void setMapAction(HttpSession session, Integer detailId,
			CotOrderOutdetail cotOrderOutdetail);
	
	// Action储存出货明细Map
	@SuppressWarnings("unchecked")
	public void setMapDelAction(HttpSession session, Integer detailId,
			CotOrderOutdetailDel cotOrderOutdetail);

	// Action储存差额数据Map
	public void setChaMapAction(HttpSession session, Integer detailId,
			CotOrderOuthsdetail cotOrderOutdetail);

	// 根据出货编号查找出货单信息
	public CotOrderOut getOrderOutById(Integer id);
	
	// 根据出货编号查找作废单信息
	public CotOrderOutDel getOrderOutDelById(Integer id);

	// 查询出货单的总金额
	public Float findTotalMoney(Integer orderId);

	// 查询差额单的总金额
	public Float findChaTotalMoney(Integer orderId);

	// 删除主出货单
	public Boolean deleteOrders(List<?> orders,String invalidNo) throws Exception;

	//判断货号是否已经添加
	public boolean checkExist(String eleId);

	// 判断该产品货号是否存在,并且返回该产品的默认图片
	public Integer findIsExistEleByEleId(String eleId);

	// 通过key获取Map的value
	public CotOrderOutdetail getOrderMapValue(Integer orderDetailId);
	
	// 通过key获取Map的value
	@SuppressWarnings("unchecked")
	public CotOrderOutdetailDel getOrderMapDelValue(Integer orderDetailId);

	// 通过key获取Map的value
	public CotOrderOuthsdetail getChaOrderMapValue(Integer orderDetailId);
	
	// 通过key获取Map的value
//	public CotOrderOuthsdetailDel getChaOrderDelMapValue(Integer orderDetailId);

	// 储存出货明细Map
	public void setOrderMap(Integer orderDetailId,
			CotOrderOutdetail cotOrderDetail);

	// 储存差额Map
	public void setChaOrderMap(Integer orderDetailId,
			CotOrderOuthsdetail cotOrderDetail);

	// 通过货号修改Map中对应的明细
	public boolean updateMapValueByEleId(Integer orderDetailId,
			String property, String value);

	// 通过货号修改Map中对应的明细
	public boolean updateChaMapValueByEleId(Integer orderDetailId,
			String property, String value);

	// 修改Map中的差额数量
	public boolean updateChaMap(Integer orderDetailId, Integer chaValue);

	// 修改Map中的出货数量
	public boolean updateMap(Integer orderDetailId, Integer chaValue);

	// 根据明细编号查找明细单信息
	public CotOrderOutdetail getOrderDetailById(Integer id);

	//删除明细图片picImg
	public boolean deletePicImg(Integer detailId);

	// Action获取出货明细Map
	public HashMap<Integer, CotOrderOutdetail> getMapAction(HttpSession session);

	// Action获取差额数据Map
	public HashMap<Integer, CotOrderOuthsdetail> getChaMapAction(
			HttpSession session);

	// 更新出货单的产品明细
	public Boolean modifyOrderDetails(List<?> details);

	// 更新出货单的差额明细
	public Boolean modifyChaOrderDetails(List<?> details);

	// 保存报价单的产品明细
	public Boolean addOrderDetails(List<?> details);

	// 保存出货单的差额明细
	public Boolean addChaOrderDetails(List<?> details, Integer id);

	// 保存出货单的差额明细
	public Boolean addChaOrderDetailsAction(HttpServletRequest request,
			List<?> details, Integer id);

	// 清除Map中eleId对应的映射
	public void delMapByKey(Integer detailId);

	// 清除差额Map中detailId对应的映射
	public void delChaMapByKey(Integer detailId);

	//在Action中清除Map中detailId对应的映射
	public void delMapByKeyAction(Integer detailId, HttpSession session);

	//在Action中清除Map中detailId对应的映射
	public void delChaMapByKeyAction(Integer detailId, HttpSession session);

	// 判断是否有些订单明细已添加过
	public List<?> findDetailByIds(String orderDetailIds);

	// 修改主单的总数量,总箱数,总体积,总金额
	public void modifyOrderOutTotalAction(List<?> details,
			HttpServletRequest request) throws Exception;

	// 修改主单的总数量,总箱数,总体积,总金额
	public void modifyChaTotalAction(List<?> details, HttpServletRequest request)
			throws Exception;

	//存储最新的出货明细统计信息
	public void getTotalOutDetail(HttpServletRequest request, Integer orderId);

	//存储最新的报关明细统计信息
	public void getTotalChaDetail(HttpServletRequest request, Integer orderId);

	// 判断该主单的明细是否已存在该产品货号
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId);

	// 更新报价单的产品明细
	public Boolean modifyOrderDetail(CotOrderOutdetail e, String eleProTime);

	// 更新出货明细图片picImg字段
	public void updatePicImg(String filePath, Integer detailId);

	//得到抬头信息
	public CotSymbol getCotSymbolByOrderOutId(Integer orderOutId);

	//得到船务信息
	public CotShipment getCotShipmentByOrderOutId(Integer orderOutId);

	//得到报关信息
	public CotHsInfo getCotHsInfoByOrderOutId(Integer orderOutId);

	//得到合计信息
	public CotOrderouthsRpt getOrderouthsRptByOrderOutId(Integer orderOutId);
	
	// 得到作废单的合计信息
//	public CotOrderouthsRptDel getOrderouthsRptByOrderOutIdDel(Integer orderOutId);

	// 删除出货单的产品明细
	public Boolean deleteOrderDetails(HttpServletRequest request,
			List<?> details);

	// 删除出货单的差额数据
	public Boolean deleteChaDetails(HttpServletRequest request, List<?> ids,Integer flag);

	// 根据出货单编号删除出货单的差额数据
	public Boolean deleteChaDetailsByMainId(Integer mainId);

	//修改订单明细未出货数量
	public void updateOrderDetail(String detailIds);

	//判断该订单明细是否已添加
	public void findIsExistDetail(CotOrderOutdetail detail);

	//判断该订单明细是否已添加
	public void findIsExistCha(CotOrderOuthsdetail detail);

	//根据编号查找麦标PicImg
	public byte[] getPicImgByOrderId(Integer custId);
	
	// 根据编号查找麦标PicImg
//	public byte[] getPicImgByOrderIdDel(Integer custId);

	//更新出货的麦标图片
	public void updateMBImg(String filePath, Integer mainId);

	//根据编号查询公司
	public CotCompany getCompanyById(Integer companyId);

	// 保存并获取邮件对象Id
	public String saveMail(Map<?, ?> map);

	// 根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id);

	//根据员工编号获得员工
	public CotEmps getEmpsById(Integer empId);

	//查询订单明细的未发货量
	public Float findUnBoxCount(Integer detailId);

	//根据订单号查询该单的客户
	public CotCustomer getCusByOrderId(Integer orderId);

	//查询某张出货单下的明细含有几个厂家
	public Map getFactorysByMainId(Integer mainId);

	// 得到objName的集合
	public List<?> getDicList(String objName);

	// 根据客户编号查找客户编号，简称，和联系人,邮箱
	public String[] getTeCustById(Integer id);

	// 根据员工编号获得员工编号和名称
	public String[] getTeEmpsById(Integer empId);

	//获得所有出货明细,并且编号不等于ids
	public List<?> getOrderOutDetailById(Integer mainId, String ids, String ods);

	//获得更改的出货明细
	public List<?> getOrderOutDetailFromMap(String ods);

	// 根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id);

	// 根据国别编号获得国别名称
	public String[] getNationNameById(Integer id);

	//合计所有订单品名信息
	public List findAllPin(Integer orderOutId);

	//合计所有报关品名信息
	public List findAllOutPin(Integer orderOutId);
	
	//查找相同报关品名的中文规格
	public String findAllSizeByName(String orderName,Integer orderId);

	//更改主单的总箱数,总金额...
	public String[] updateOrderOutTotal(Integer orderId) throws Exception;
	
	// 获得暂无图片的图片字节
	public byte[] getZwtpPic();
	
	//返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate);
	
	// Action储存采购明细Map
	public void setFacMapAction(HttpSession session, Integer detailId,
			VOrderOrderfacId cotOrderFacdetail);
	
	// Action储存配件采购明细Map
	public void setFitFacMapAction(HttpSession session, Integer detailId,
			VOrderFitorderId fittingsOrderdetail);
	
	// Action储存包材采购明细Map
	public void setPackFacMapAction(HttpSession session, Integer detailId,
			VOrderPackorderId packingsOrderdetail);
	
	//通过采购明细id找厂家id
	public Integer findFactoryIdById(Integer orderfacDetailId);
	
	// 通过配件采购明细id找厂家id
	public Integer findFitFactoryIdById(Integer fitDetailId);
	
	// 通过配件采购明细id找厂家id
	public Integer findPackFactoryIdById(Integer packOrderId);
	
	//通过采购明细id找主单id
	public Integer findOrderFacId(Integer orderfacDetailId);
	
	//通过出货主单编号查找明细,在将明细中的订单主单编号及货号组成List
	public List<CotOrderOutdetail> checkIsHasOrder(Integer orderId);
	
	// 通过出货主单编号查找明细,在将明细中的生产合同主单编号组成字符串
	public List<CotOrderFac> checkIsHasFac(Integer orderoutId);
	
	// 通过出货主单编号查找明细,在将明细中的产品采购主单编号组成字符串
	public String checkIsHasFacOrders(Integer orderoutId);
	
	// 通过出货主单编号查找明细,在将明细中的配件采购主单编号组成字符串
	public String checkIsHasFitOrders(Integer orderoutId);
	
	// 通过出货主单编号查找明细,在将明细中的包材采购主单编号组成字符串
	public String checkIsHasPackOrders(Integer orderoutId);
	
	// 通过订单明细id查询采购合同id
	public String getFitOrderId(Integer orderDetailId);
	
	//通过订单明细id查询采购合同id
	public String getOrderFacId(Integer orderDetailId);
	
	// 通过出货主单编号查找生产合同主单编号并组成字符串
	public String checkIsHasFacs(Integer orderoutId);
	
	// 通过出货主单编号查找配件采购主单编号并组成字符串
	public String checkIsHasFits(Integer orderoutId);
	
	//通过出货主单编号查找包材采购主单编号并组成字符串
	public String checkIsHasPacks(Integer orderoutId);
	
	//根据id获取生产合同其他费用信息
	public CotFinaceOther getCotFinaceOtherById(Integer id);
	
	// 保存其他费用
	public Boolean addOrderOutOther(HttpServletRequest request,List<?> details);
	
	//更新其他费用
	public Boolean updateOrderFacOther(List<?> details);
	
	//删除其他费用
	public Boolean deleteOrderFacOther(List<?> details);
	
	//根据id获取生产合同未冲完的应付帐款记录
	public CotFinaceAccountdeal getOrderFacDealById(Integer id);
	
	//生成应付帐款单号
//	public String createDealNo(Integer otherId);
	
	//保存应付帐款单号
	public void savaSeq();
	
	//保存应付帐款
	public void saveAccountdeal(Integer otherId,CotFinaceAccountdeal dealDetail,String amountDate);
	
	//删除应付帐款
	public Boolean deleteDealList(List<?> details,Integer mainId);
	
	//更新已出货数量
	public void updateFacDetail(Integer orderfacDetailId,Long outCurrent);
	
	// 更新配件已出货数量
	public void updateFitDetail(Integer fitorderDetailId, Double outCurrent);
	
	// 更新包材已出货数量
	public void updatePackDetail(Integer packorderDetailId, Long outCurrent);
	
	//更新已出货数量-删除时
	public void updateFacDetailForDel(Integer orderfacDetailId);
	
	// 更新配件已出货数量-删除时
	public void updateFitDetailForDel(Integer detailId);
	
	// 更新包材已出货数量-删除时
	public void updatePackDetailForDel(Integer detailId);
	
	//插入一条出货其他费用--产品
	public void saveOrderFacOther(Integer orderfacDetailId,Integer orderoutId);
	
	// 插入一条出货其他费用--配件
	public void saveFitOrderOther(Integer detailId, Integer orderoutId);
	
	//	插入一条出货其他费用--包材
	public void savePackOrderOther(Integer detailId, Integer orderoutId);
	
	//插入一条出货其他费用--配件--未指定数量直接生成时
	public void saveFitOthersDeal(Integer orderoutId);
	
	//插入一条出货其他费用--包材--未指定数量直接生成时
	public void savePackOthersDeal(Integer orderoutId);
	
	//自动分配
	public void autoOrderOut(Integer orderoutId);
	
	//判断出货数量是否等于采购数量
	public boolean checkIsEqualCount(Integer orderId,String eleId,Long boxCount);
	
	//更改采购剩余数量
	public void updateOrderFacOutRemain(Integer orderId,String eleId,Integer orderoutId);
	
	//查询采购单是否有应付帐款
	public Integer checkIsHasDeal(Integer orderfacId,String source);
	
	//查询采购单是否有其他费用
	public Integer checkIsHasOther(Integer orderfacId,String source);
	
	//其他费用余额
	public void modifyOtherRemain(Integer otherId,Double amount,String flag);
	
	//其他费用导入标志
	public void modifyGivenOther(Integer outOtherId,Integer facOtherId,Double amount,String flag);
	
	//修改付款记录的剩余金额为0
	public void modifyGivenRemainAmount(Integer fingivenId,Double remainAmount);
	
	//其他应付款
	public void modifyDealRemain(Integer otherId,Double amount,String flag);
	
	//剩余金额-删除时
	public void modifyOtherRemainDel(Integer otherId);
	
	//根据编号获得对象
	public CotFinaceAccountdeal getGivenDealById(Integer id);
	
	//判断应付帐款是否有冲帐明细
	public List checkIsHaveDetail(List ids);
	
	//判断其对应的生产合同其他费用剩余金额是否已生成应付帐款
	public String checkIsHasDealed(Integer[] ids);
	
	// 保存其他费用
	public Boolean addOtherList(HttpServletRequest request,List<?> details);
	
	//根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id);
	
	// 更改配件采购剩余数量
	public void updateFitOrderOutRemain(Integer orderId);
	
	// 更改包材采购剩余数量
	public void updatePackOrderOutRemain(Integer orderId);
	
	//获取所有出货明细
	public List<CotOrderOutdetail> getOrderOutdetail(Integer orderoutId);
	
	//获取所有采购明细
	public List<CotOrderFacdetail> getOrderFacDetail(Integer orderDetailId);
	
	// 获取所有配件采购明细
	public List<VOrderFitorderId> getFitOrderDetail(Integer orderId);
	
	// 获取所有包材采购明细
	public List<VOrderPackorderId> getPackOrderDetail(Integer orderId);
	
	//导入生产合同货款金额
	public void saveOthers(Integer orderDetailId, Integer orderoutId);
	
	// 插入一条出货其他费用--配件--未指定数量直接生成时
	public void saveFitOthers(Integer detailId, Integer orderoutId);
	
	// 插入一条出货其他费用--包材--未指定数量直接生成时
	public void savePackOthers(Integer detailId, Integer orderoutId);
	
	//查询港口名
	public String[] findPortNameById(Integer sportId,Integer tportId);
	
	//导入生产合同货款金额
	public void saveOthersDeal(Integer orderoutId);
	
	// 插入出货其他费用
	public void saveOrderFacOtherAuto(Integer orderfacDetailId, Integer orderoutId);
	
	//更新其他费用
	public Boolean updateOtherList(HttpServletRequest request,List<?> details);
	
	// 更新应付款其他费用
	public Boolean updateDealOtherList(List<?> details);
	
	//删除其他费用
	public Boolean deleteOtherList(List<?> details);
	
	//根据出货主单编号查找出货明细中的订单orderNoid
	public String findOrderId(Integer orderOutId);
	
	// 根据条件查询冲帐明细记录
	public List<?> getOrderNoList(List<?> list);
	
	// 根据条件查询冲帐明细记录
	public List<?> getOrderFacNoList(List<?> list);
	
	// 根据生产合同编号查找采购信息
	public CotOrderFac getOrderFacById(Integer id);
	
	// 根据配件编号查找采购信息
	public CotFittingOrder getFitOrderById(Integer id);
	
	// 根据编号查找采购信息
	public CotPackingOrder getPackOrderById(Integer id);
	
	// 根据配件编号查找采购明细信息
	public CotFittingsOrderdetail getFitOrderDetailById(Integer id);
	
	// 根据包材编号查找采购明细信息
	public CotPackingOrderdetail getPackOrderDetailById(Integer id);
	
	//删除未导入出货的、付款金额为0的应付款
	public void delFinaceGivenDeal(String orderfacIds);
	
	// 修改其他费用状态 0：未生成 1: 已生成
	public void modifyFinOtherStatus(Integer fkId, String finaceName,String flag);
	
	//=================应付款结束==============================================
	
	// 更改订单其他费用的的剩余金额.如果剩余金额等于0,则outFlag为1
	public void updateOrderIsImport(HttpServletRequest request,List amountReal);
	
	// 更改订单其他费用的的剩余金额
	public void updateOtherRemain(HttpServletRequest request,List amountReal);
	
	// 更改订单应收帐的剩余金额.
	public void updateRecvMod(HttpServletRequest request,List accReal);
	
	// 更改应付帐的剩余金额.
	public void updateDealMod(HttpServletRequest request,List accReal);
	
	// 更改溢收款的剩余金额.
	public void updateYiRemain(HttpServletRequest request,List accReal);
	
	// 更改溢付款的剩余金额.
	public void updateDealYiRemain(HttpServletRequest request,List accReal);
	
	// 更改订单应收帐的导入标识为1
	public void updateOrderRecvIsImport(HttpServletRequest request,List accReal);
	
	// 更改收款记录的剩余金额
	public void updateRecvRemain(HttpServletRequest request, List amountYi);
	
	// 更改付款记录的剩余金额
	public void updateDealRemain(HttpServletRequest request, List amountYi);
	
	// 删除其他费用
	public Float deleteByIds(List<Integer> ids) ;
	
	// 删除应付款其他费用
	public Float deleteDealByIds(List<Integer> ids);
	
	//查询出货单的应收帐,如果存在应收帐则删除旧单
	public Boolean deleteAccountrecvByFkId(Integer fkId);
	
	// 保存应收帐款
	public void saveAccountRecv(CotFinaceAccountrecv recvDetail,String otherIds,
			String amountDate);
	
	// 生成应收帐款单号
	public String createRecvNo(Integer custId);
	
	// 根据条件查询冲帐明细记录
	public List<?> getRecvDetailList(List<?> list);
	
	// 根据条件查询溢收款记录
	public List<?> getYiList(List<?> list);
	
	// 删除收款记录
	public Boolean deleteByRecvDetail(List<Integer> ids);
	
	// 删除付款记录
	public Boolean deleteByDealDetail(List<Integer> ids);
	
	// 删除应收帐
	public Boolean deleteByAccount(Integer accountId,Integer mainId);
	
	//查询出货的应收帐
	public CotFinaceAccountrecv findAccountrecvByFkId(Integer fkId);
	
	// 判断出货单是否有应收帐,应付帐,是否被排载(0可删除,1有应收帐,2有应付帐,3应收应付都有,4有排载)
	public int checkCanDelete(Integer orderOutId);
	
	// 判断出货单是否有应收帐,应付帐,是否被排载
	public List checkCanDeleteBatch(List<Integer> ids);
	
	//修改出货审核状态
	public void updateOrderStatus(Integer orderId,Integer orderStatus);
	
	// 计算价格
	public Float[] calPriceAll(String detailId);
	
	// 计算价格
	public Float[] calPriceAllByOrderDetail(String detailId);
	
	// 根据文件路径导入
	public List<?> saveReport(String filename,Integer orderId);
	
	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String rdm, String packingPrice);
	
	//查询出货主单
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//查询出货明细
	public String getListData(HttpServletRequest request, QueryInfo queryInfo)
			throws DAOException;
	
	//查询出货报关明细
	public String getListChaData(HttpServletRequest request, QueryInfo queryInfo)
			throws DAOException;
	
	// 查询含有rdm的报价记录
//	public String getListChaDelData(HttpServletRequest request, QueryInfo queryInfo)
//			throws DAOException ;
	
	// 删除唛标明细图片
	public boolean deleteMBPicImg(Integer orderId);
	
	//判断订单的其他费用是否已导入过出货
	public boolean findIsExistOther(Integer orderOutId,Integer otherId);
	
	//判断订单的应收帐是否已导入过出货
	public boolean findIsExistRecv(Integer orderOutId,Integer recvId);
	
	//判断客户的溢收款是否已导入过出货
	public boolean findIsExistYi(Integer orderOutId,Integer yiId);
	
	// 重新排序出货明细
	public boolean updateSortNo(Integer type, String field,
			String fieldType);
	
	// 重新排序报关明细
	public boolean updateSortNoBao(Integer type, String field,
			String fieldType);
	
	// 查询出货明细时,关联订单明细查询目前的剩余出货数
	public List<?> getOrderDetailVOList(QueryInfo queryInfo);
	
	//判断出货其他费用是否存在
	public boolean findIsExistName(String name,Integer orderId,Integer recId);
	
	//判断出货应付款其他费用是否存在
	public boolean findIsExistNameDeal(String name,Integer orderId,Integer recId);
	
	// 更改产品采购应付帐的剩余金额
	public void updateFacDeal(HttpServletRequest request, List accReal);
	
	//查询订单到出货的可导入费用余额
	public Double findMaxMoney(Integer curId,String source,Integer outFlag);
	
	//查询订单到出货的可导入费用余额
	public Double findMaxMoneyDeal(Integer curId,String source,Integer outFlag);
	
	//查找原始费用值
	public Double findOldVal(Integer curId,Integer finaceOtherId);
	
	// 保存应付帐款
	public boolean saveDeal(Integer mainId);
	
	//查询未导入的应收款的条数
	public int findNoImportNum(Integer outId,Integer custId);
	
	// 查询未导入的应收款的编号
	public String[] findNoImportAll(Integer orderOutId, Integer custId);
	
	//根据对象名和ids查找记录
	public List getListByTable(String tabName,String ids);
	
	// 查询未导入的应付款的条数
	public int findNoImportNumFac(Integer orderOutId);
	
	// 查询未导入的应付款的编号
	public String[] findNoImportAllFac(Integer orderOutId);
	//通过订单Id获得厂家对象
	public CotFactory getFactoryByOrderId(Integer orderId);
	
	public boolean isInvoiceExistByPIId(Integer piId);
}
