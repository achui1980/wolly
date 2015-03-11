/**
 * 
 */
package com.sail.cot.service.order;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderEleprice;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderFittings;
import com.sail.cot.domain.CotOrderPc;
import com.sail.cot.domain.CotOrderStatus;
import com.sail.cot.domain.vo.CotNewPriceVO;
import com.sail.cot.query.QueryInfo;

public interface CotOrderService {

	//根据报价编号查找报价单信息
	public CotOrder getOrderById(Integer id);

	//根据报价明细编号查找报价明细单信息
	public CotOrderDetail getOrderDetailById(Integer id);

	// 判断该主单的明细是否已存在该产品货号
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId);

	//保存或者更新主报价单
	public Integer saveOrUpdateOrder(CotOrder cotOrder, String orderTime,
			String sendTime,String orderLcDate,String orderLcDelay,String addTime,boolean oldFlag,String design);

	/**
	 * @Description 保存主单的产品明细
	 * @param detail 订单明细集合
	 */
	public Boolean addOrderDetails(List<?> detail);

	/**
	 * @Method: modifyOrderDetails
	 * @Description: 更新主单的产品明细
	 * @param details
	 * @return : Boolean
	 */
	public Boolean modifyOrderDetails(List<?> details);

	/**
	 * @Description 批量删除主订单
	 * @param orders 订单编号集合
	 * @return Integer
	 */
	public int[] deleteOrders(List<?> orders);

	//删除订单的产品明细
	public Boolean deleteOrderDetails(List<?> details);

	// 删除订单的产品明细
	public Boolean deleteOrderDetailsAction(HttpServletRequest request,
			List<?> ids);

	//根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id);

	// 根据客户编号查找客户编号，简称，和联系人,邮箱
	public String[] getTeCustById(Integer id);

	// 更新报价单的产品明细
	public Boolean modifyOrderDetail(CotOrderDetail e, String eleProTime);

	//得到总记录数
	public Integer getRecordCount(String objName, String whereStr);

	//得到objName的集合
	public List<?> getList(String objName);

	//根据主报价单号找报价明细
	public List<?> getDetailByOrderId(Integer orderId);

	//查询所有厂家名称
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request);
	
	// 查询配件厂家
	public Map<?, ?> getFitFactoryNameMap(HttpServletRequest request);

	//查询所有客户名称
	public Map<?, ?> getCusNameMap();

	//查询所有客户简称
	public Map<?, ?> getCusShortNameMap();

	//查询所有价格条款
	public Map<?, ?> getClauseMap(HttpServletRequest request);
	
	// 查询所有付款方式
	public Map<?, ?> getPayTypeMap(HttpServletRequest request);

	//查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);

	//查询所有材质
	public Map<?, ?> getTypeMap();

	//查询所有用户姓名
	public Map<?, ?> getEmpsMap();

	//查询报价单单号是否存在
	public Integer findIsExistOrderNo(String orderNo, String id);

	//删除报价明细单产品的原来图片
	public void deleteOrderDetailImg(Integer detailId);

	//根据条件查询样品记录
	public List<?> getElementList(QueryInfo queryInfo);

	//查询记录
	public List<?> getList(QueryInfo queryInfo);
	
	//如果是报价或订单的配件.需要判断该配件厂家或者采购价格是否和配件库一致,如果不同,用配件库的最新数据代替
	public List<?> getNewList(String tableName,List list);

	// 根据条件查询主单记录
	public List<?> getOrderList(QueryInfo queryInfo);
	
	// 根据条件查询冲帐明细记录
	public List<?> getRecvDetailList(List<?> list);

	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo);

	//根据用户查找他拥有的客户编号
	public String getCustomerIds(Integer empId);

	//查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId);

	//根据样品货号组装报价明细产品对象,并根据报价参数算出单价
	public CotOrderDetail findDetailByEleId(CotOrderDetail cotOrderDetail,
			Map<?, ?> map, String liRunCau[]);

	//根据报价明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids);

	//查询该单的所有报价明细的产品货号
	public String findEleByOrderId(Integer orderId);
	
	//计算平均利润率
	public Double sumAverageProfit(Double orderRate,Integer clauseTypeId);
	
	//计算总利润
	public Double sumTotalProfit(Double rate);

	//根据要更改的产品货号字符串 和 价格条件更改单价,再返回这组单价
	public Map<String, Float[]> getNewOrder(String rdmIds, Map<?, ?> map,
			String liRunCau[]);

	// 根据币种改变再返回这组单价
	@SuppressWarnings("unchecked")
	public Map<String, Float[]> getNewOrderCurreny(String eleIds,
			Map<?, ?> map, String liRunCau, Integer oldCur);

	//判断该产品是否对该用户报价过
	public Object[] findIsExistDetail(String eleId, String cusId,String clauseId,String pTime);

	//判断该产品货号是否存在
	public Integer findIsExistEleByEleId(String eleId);

	//根据产品货号查找对外报价
	public Float findPriceOutByEleId(String eleId);

	//判断同一张单中是否存在相同图片名称
	public Boolean findIsExistPicInOrder(Integer orderId, String picName);

	//根据更改图片路径
	public void modifyPicPathByPriceId(Integer detailId, String priceNo,
			String picName);

	//查询订单的总金额
	public Float findTotalMoney(Integer orderId);

	//修改主单的总数量,总箱数,总体积,总金额
	public void modifyCotOrderTotal(Integer orderId);

	// 修改主单的总数量,总箱数,总体积,总金额
	public Float[] modifyCotOrderTotalAction(Integer orderId);
	
	// 得到加减费用总和
	public String getRealMoney(Integer orderId, Integer currencyId);

	// 根据产品货号和客户编号查找该产品对该客户的历史报价
	public CotNewPriceVO findNewPriceVO(String eleId, Integer cusId);

	// 根据样品货号字符串查询明细
	public void updateToEle(String[] same,String[] sameRdm, String[] dis,String[] disRdm,String eleStr,String boxStr,String otherStr,boolean isPic);

	// 判断要更新到样品表的明细货号哪些重复
	public Map<String,List<String>> findIsExistInEle(String[] key);

	//清空Map
	public void clearMap(String typeName);

	//通过key获取Map的value 
	public CotOrderDetail getOrderMapValue(String rdm);

	//储存Map
	public void setOrderMap(String eleId, CotOrderDetail cotOrderDetail);
	
	// 储存Map,移除id一样的对象
	public void setOrderMapAndDel(String rdm, CotOrderDetail cotOrderDetail);

	//通过货号修改Map中对应的征样明细
	public boolean updateMapValueByEleId(String rdm, String property,
			String value);

	//清除Map中eleId对应的映射
	public void delMapByKey(String eleId);

	//Action获取signMap
	public Map<String, CotOrderDetail> getMapAction(HttpSession session);

	//Action储存signMap
	public void setMapAction(HttpSession session, String eleId,
			CotOrderDetail cotOrderDetail);

	//在Action中清除Map中eleId对应的映射
	public void delMapByKeyAction(String rdm, HttpSession session);

	//查询某张单下的订单明细,将所有货号组成一个map
	public Map getEleMap(Integer mainId);

	//获取订单图片的HTML显示，用图片演示用
	public List getOrderImg(String custId,String startDate,String endDate, int start, int limit);

	//获取查询的记录数,图片演示用
	public int findPicCount(String custId,String startDate,String endDate);

	//根据明细货号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId);

	//删除明细图片picImg
	public boolean deletePicImg(Integer detailId);
	
	// 删除唛标明细图片
	public boolean deleteMBPicImg(Integer orderId);
	
	// 删除产品标图片
	public boolean deleteProductMBPicImg(Integer orderId);

	//更新征样图片picImg字段
	public void updatePicImg(String filePath, Integer detailId);

	//更新订单的麦标图片
	public void updateMBImg(String filePath, Integer mainId);
	
	// 更新订单的产品标图片
	public void updateProMBImg(String filePath, Integer mainId);

	//根据客户编号查找麦标PicImg
	public byte[] getPicImgByCustId(Integer custId);

	//根据订单编号查找麦标PicImg
	public byte[] getPicImgByOrderId(Integer custId);
	
	// 根据订单编号查找产品标PicImg
	public byte[] getProPicImgByOrderId(Integer custId);

	//得到公司的集合
	public List<?> getCompanyList();

	//得到客户的集合
	public List<?> getCustomerList();

	//得到员工的集合
	public List<?> getEmpsList();

	//根据订单号查询该单的客户
	public CotCustomer getCusByOrderId(Integer orderId);

	//通过主单id获取订单明细id的集合
	public List<?> getDetailIdsByOrderId(Integer orderId);

	//保存主采购单并返回map
	public HashMap<?, ?> saveOrderFac(String[] factoryIdAry, Integer orderId);

	//获得暂无图片的图片字节
	public byte[] getZwtpPic();

	//根据factoryId获取map中的orderfacId
	public CotOrderFac getCotOrderFacByFacId(Integer factoryId, HashMap<?, ?> map);
	
	//保存生产合同单号及id到订单明细中
	public void saveOrderFacIdAndNo(Integer factoryId, HashMap<?, ?> map,Integer orderId);
	
	//保存采购单明细
	public Boolean saveOrderFacDetail(Integer factoryId, HashMap<?, ?> map,
			CotOrderFacdetail cotOrderFacdetail);

	//根据员工编号获得员工
	public CotEmps getEmpsById(Integer empId);

	// 根据员工编号获得员工编号和名称
	public String[] getTeEmpsById(Integer empId);

	// 得到单样品利润公式
	public String[] getLiRunCau();

	public List<?> getChildrens(String eleId);

	// 获得新利润率
	public Float getNewLiRun(Map<?, ?> map, String rdm);

	// 根据明细中的利润率代入主单利润率算出最新价格
	public Float getNewPriceByLiRun(Map<?, ?> map, String rdm);

	// 根据明细中的退税率算出最新价格
	public Float getNewPriceByTuiLv(Map<?, ?> map, String eleId);

	// 根据明细中的生产价算出最新价格
	public Float getNewPriceByPriceFac(Map<?, ?> map, String rdm);

	// 根据明细中的生产价币种算出最新价格
	public Float getNewPriceByPriceFacUint(Map<?, ?> map, String eleId);

	// 根据文件路径导入
	public List<?> saveReport(Integer orderId,String filename,boolean cover,Integer currencyId,boolean excelFlag);

	//根据文件路径和行号导入excel
	public List<?> updateOneReport(String filePath, Integer rowNum, Integer orderId,boolean cover,Integer currencyId,boolean excelFlag);

	//清空excel的缓存
	public void removeExcelSession();
	
	// 清空Pan的缓存
	public void removePanSession();

	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价(excel导入)
	public CotOrderDetail findDetailByEleIdExcel(String rdm,
			boolean isUsePriceOut, Map<?, ?> map, String liRunCau[]);

	// 得到objName的集合
	public List<?> getDicList(String objName);

	//判断明细中是否存在该货号
	public boolean checkIsExistEle(Integer mainId, String eleId);

	//根据类型获得合同条款
	public List<?> getCotContractList(int type);

	//查询VO记录
	public List<?> getOrderVOList(QueryInfo queryInfo);

	// 根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id);

	//根据客号查找报价明细表中的货号(取最近报价时间)
	public Object[] findEleByCustNo(String custNo, Integer custId,String clauseId,String pTime);

	//将该货号对应的样品转成明细
	public CotOrderDetail changeEleToOrderDetail(String eleId);

	//获取默认公司ID
	public Integer getDefaultCompanyId();

	// 保存或者更新主订单
	public Integer saveByExcel(CotOrder cotOrder, String orderTime,
			String sendTime, boolean oldFlag) throws Exception;

	//保存明细
	public void saveDetail(Integer orderId, Integer currencyId, Integer custId);
	
	// 根据起运港港编号获得起运港名称
	public String[] getShipPortNameById(Integer id);
	
	// 通过货号修改Map中对应的明细并返回对象
	public CotOrderDetail updateObjByEle(String eleId, String property,
			String value);
	
	//根据新单号另存为新的订单
	public boolean saveAs(String newOrderNo,Integer mainId) throws Exception;
	
	// 判断要更新到样品表的明细货号哪些重复
	public List<?> updateEleToDetail(String[] eleAry,String[] rdmAry, String eleStr,String boxStr,String otherStr,boolean isPic) ;
	
	// 得到excel的内存map的key集合
	public List<?> getRdmList();
	
	// 保存其他费用
	public Boolean addOtherList(List<?> details);
	
	// 保存计划日期
	public void saveTime(Integer orderId);
	
	// 保存应收帐款
	public Boolean addRecvList(List<?> details);
	
	//删除其他费用
	public Boolean deleteOtherList(List<?> details);
	
	//删除应收帐
	public Boolean deleteRecvList(List<?> details);
	
	//更改生产合同其他费用的导入标识为1
	public void updateOrderFacIsImport(String ids);
	
	//根据编号获得对象
	public CotFinaceOther getFinaceOtherById(Integer id);
	
	// 根据编号获得对象
	public CotOrderStatus getOrderStatusById(Integer id);
	
	//更新其他费用
	public Boolean updateOtherList(List<?> details);
	
	//更新其他费用的导入标识为1
	public Boolean updateStatus(String ids);
	
	//删除其他费用, 返回所有费用的rmb总金额
	public Float deleteByIds(List<?> ids,List<?> isImport,Float curRate);
	
	//删除应收帐
	public Boolean deleteByAccount(List<?> ids);
	
	//删除收款记录
	public Boolean deleteByRecvDetail(List<Integer> ids);
	
	//通过订单主单编号查找明细,在将明细中的生产合同主单编号组成字符串
	public String checkIsHasFac(Integer orderId);
	
	//生成应收帐款单号
	public String createRecvNo(Integer custId);
	
	//保存应收帐款
	public void saveAccountRecv(CotFinaceAccountrecv recvDetail,String amountDate,String priceScal,String prePrice);
	
	//根据币种英文名称查找编号
	public Integer getCurrenyId(String curNameEn);
	
	//返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate);
	
	//判断该订单费用是否已被导入出货单
	public boolean checkIsOut(Integer otherId);
	
	//判断应收帐是否导到出货
	public List<Integer> checkIsImport(List<Integer> ids);
	
	//判断要删除的订单明细是否已经生成配件采购单
	public List<Integer> checkIsHasFitOrder(String details);
	
	// 判断要删除的订单明细是否已经生成包材采购单
	public List<Integer> checkIsHasPackOrder(String details);
	
	// 查询配件信息
	public List findFittingsByIds(String ids, Integer rdm);
	
	//增加
	public void addList(List list);
	
	//增加成本
	public void addElePriceList(List list);

	//删除
	public void deleteList(List list,String tabName);

	//修改
	public void modifyList(List list);
	
	// 获得订单配件对象
	public CotOrderFittings getOrderFittingById(Integer id);
	
	// 获得订单明细成本对象
	public CotOrderEleprice getElePriceById(Integer id);
	
	//判断输入的配件号是否存在.完全存在的话直接加到表格中,有模糊数据弹出层.
	public List findIsExistFitNo(String fitNo,Integer orderDetailId);
	
	//计算明细的生产价
	public CotOrderDetail updatePriceFac(Integer orderDetailId, Integer rdm,Map map);
	
	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit();
	
	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String rdm, String packingPrice);
	
	//根据订单id获取采购单ids
	public String getOrderFacIds(Integer orderId);
	
	//根据订单明细id获取采购单id
	public String getOrderFacIdsByDetailId(Integer detailId);
	
	//修改订单审核状态
	public Timestamp updateOrderStatus(Integer orderId,Integer orderStatus,Integer checkPerson)throws Exception;
	
	// 根据上传的盘点机数据存入内存
	public void saveCheckMachine(List<String> list, HttpServletRequest request);
	
	// 清空盘点机Map
	public void clearCheckMap();
	
	// 从session中取得上传的盘点机流水号
	public List<String> getMachineNum();
	
	// 根据盘点机流水号获得明细字符串集合
	public List<String> getMachineDetails(String checkNo);
	
	// 根据盘点机流水号获得明细对象集合
	public List<CotOrderDetail> getMachineDetailList(String checkNo);
	
	// 计算价格
	public Float[] calPriceAll(String rdm);
	
	//如果是预收货款的收款记录,如果有转移过,不能再删除收款记录
	public boolean checkIsLiu(Integer id);
	
	// 重新排序
	public boolean updateSortNo(Integer id, Integer type, String field,
			String fieldType);
	
	//判断该订单是否已分解完成
	public boolean checkUnCountIsZero(Integer orderId);
	
	//判断该订单是否未分解
	public boolean checkUnCountIsEqual(Integer orderId);
	
	//判断订单是否已采购
	public boolean checkIsHaveOderFac(Integer orderId);
	
	//同步唛头信息至采购单
	public boolean updateOrderFacMb(Integer orderId);
	
	//获取采购单ids
	public List<Integer> getCotOrderFacIds(Integer orderId);
	
	public boolean updateCotOrderFacMb(Integer orderId,String [] orderfacId);
	
	//订单分解时判断是否已有改厂家采购单
	public boolean isExistFactory(Integer facId,Integer orderId);
	
	// 查询含有rdm的报价记录
	public String getListData(HttpServletRequest request, QueryInfo queryInfo,String ctPc)
			throws DAOException;
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	// 得到盘点机的内存map的key集合
	public void savePanList(String no,Integer orderId);
	
	//订单同步样品配件信息
	public boolean deleteAndTongEleFitting(Integer orderDetailId,String eleId);
	
	//订单单号
	public String getOrderNo(Integer custId,String orderTime);
	
	//更新至产品采购和包材采购唛头信息
	public void updateMb(Integer orderId,String orderZM,String orderCM,String orderZHM,String orderNM, String productM)throws DAOException;
	
	// 储存Map
	public void setExcelMap(String rdm, CotOrderDetail cotOrderDetail);
	
	// 判断订单是否有其他产品费用
	public boolean checkIsHaveFinace(Integer orderId);

	
	//判断订单其他费用是否存在
	public boolean findIsExistName(String name,Integer orderId,Integer recId);
	
	//需要判断该明细是否已生成出货和采购,如果是.不能删除
	public Integer findIsCanDel(Integer detailId);
	
	//计算勾选的订单明细的InverseMargin
	public Map calLiRun(Map<?, ?> map, List rdmAry);
	
	//用过货号查找样品id
	public Integer getEleIdByEleName(String eleId);
	
	// 订单同步到样品配件
	public boolean deleteAndTongFitting(Integer orderDetailId, String eleId)throws DAOException;
	
	//修改样品生产价
	public boolean modifyPriceFacByEleId(String eleId);
	
	//得到海关编码退税率的值
	public Float getTuiLv(Integer hsId);
	
	//根据订单主单id查找对应的生产合同主单id
	public Integer getOrderFacByOrderId(Integer orderId);
	
	//将Pi生成Invoice
	public Integer saveInvoice(Integer orderId,boolean flag);
	
	public List<?> getOrderVO(QueryInfo queryInfo);
	//返回审核数
	public Integer getCountPI();
	public List getPIPO();
	
public Integer saveOrderPc(CotOrderPc e, String picPath);
	
	// 删除图片
	public Boolean deleteOrderPcs(List<Integer> ids);
	
	// 根据编号获得对象
	public CotOrderPc getOrderPcById(Integer id);
	
	// 更改包装图片
	public void updateOrderPc(String filePath, Integer fkId)
			throws Exception;
	
	// 删除图片
	public boolean deleteOrderPcImg(Integer Id)throws Exception;
	
	//根据客户编号获得客户的图片
	public List getCustPcs(Integer custId);
	
	// 用过货号查找样品小类
	public Integer getEleTypeidLv3ByEleName(String eleId);
	
	//根据客户小类图片ids生成orderPc
	public void saveOrderPcs(String ids,Integer orderId,Integer detailId);
	
	// 判断po是否审核通过
	public boolean checkIsGuoShen(Integer orderId);
	
	//存放home页面里面的sc文件名
	public void saveScFile(Integer orderId,String fileName) throws DAOException;
	
	//存放home页面里面的client order文件名
	public void saveClientFile(Integer orderId,String fileName) throws DAOException;
	
	//存放home页面里面的client order文件名
	public String[] getFileNames(Integer orderId) throws DAOException;
	
	public String getPIComment(Integer orderId) throws DAOException;
	
	// 判断是否已经生成art work报表
	public boolean checkIsHasArtWork(String orderNo);
	
	public void saveOrderStaMark(Integer orderId,Integer staId,String staMark,Integer type) throws DAOException;
	
	public int updateClientPo(Integer orderId, String clientPo);
	
	public int updateOrderStatusAndAddDayRpt(Integer orderId);
	
	public CotOrderFac getOrderFacByPiId(Integer PIid);
	
	public List getExistBarcode(String barcodes,String orderId);
}
