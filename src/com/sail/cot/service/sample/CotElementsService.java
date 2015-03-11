/**
 * 
 */
package com.sail.cot.service.sample;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.vo.CotEleIdCustNo;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.query.QueryInfo;

/**
 * <p>
 * Title: 工艺品管理系统
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Jul 31, 2008 5:23:39 PM
 * </p>
 * <p>
 * Class Name: CotSystemMenu.java
 * </p>
 * 
 * @author achui
 * 
 */
public interface CotElementsService {

	// 根据编号取得对象
	public CotElementsNew getElementsById(Integer id);

	// 根据编号查询样品
	public CotElementsNew getEleById(Integer id);

	// 保存样品
	public Integer saveElement(CotElementsNew e, String eleProTime);

	// 保存样品和包装信息和图片信息
	public Boolean saveByCopy(CotElementsNew e, String newEleId,
			String eleProTime);

	// 更新elements
	public Boolean modifyElements(CotElementsNew e, String eleAddTime);

	// 保存或更新子样品
	public Integer saveOrUpdateChild(CotElementsNew e, String eleProTime);

	// 删除
	public Boolean deleteElements(List<Integer> ids);

	// 查询所有厂家名称
	public Map<?, ?> getFactoryNameMap();

	// 查询配件厂家
	public Map<?, ?> getFittingFacMap();

	// 查询包材厂家
	public Map<?, ?> getPackingFacMap();

	// 查询所有业务员名称
	public Map<?, ?> getEmpsNameMap();

	// 查询所有币种名称
	public Map<?, ?> getCurrencyNameMap();

	// 查询所有客户简称
	public Map<?, ?> getCusShortNameMap();

	// 查询所有条款名称
	public Map<?, ?> getClauseNameMap();

	// 获取报价单号映射
	public Map<?, ?> getPriceNoMap();

	// 获取报价日期映射
	public Map<?, ?> getPriceTimeMap();

	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo);

	// 根据条件查询主送样单记录
	public List<?> getGivenList(QueryInfo queryInfo);

	// 根据条件查询主订单记录
	public List<?> getOrderList(QueryInfo queryInfo);

	// 将String类型的时间转化为Timestamp类型
	public Timestamp getStampTime(String time);

	// 将Timestamp类型的时间转化为String类型
	public String getStringTime(Timestamp time);

	// 获取订单单号映射
	public Map<?, ?> getOrderNoMap();

	// 获取送样单号映射
	public Map<?, ?> getGivenNoMap();

	// 获取征样单号映射
	public Map<?, ?> getSignNoMap();

	// 获得样品材质的映射
	public Map<?, ?> getTypeMap();

	// 获得客户编号的映射
	public Map<?, ?> getCustomerNoMap();

	// 获得客户简称的映射
	public Map<?, ?> getCustomerShortNameMap();

	// 查询所有厂家
	public List<?> getFactoryList();

	// 查询所有材质
	public List<?> loadTypeLv1List();

	// 查询所有产品分类
	public List<?> loadTypeLv2List();

	// 查询图片名称是否存在
	public boolean findExistName(String name);

	// 通过图片Id删除图片
	public Boolean deletePictureByPicId(Integer picId);

	// 根据查询条件查询
	List getQueryCondition(String queryType);

	// 查找所有海关信息
	public List<?> getEleOtherList();

	// 查找所有币种信息
	public List<?> getCurrencyList();

	// 查找所有包装类型
	public List<?> getBoxTypeList();

	// 查找所有报表类型
	public List<?> getReportTypeList();

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId);

	// 通过编号查找包装类型
	public CotBoxType getBoxTypeById(Integer id);

	// 通过编号查找包装类型名称
	public String getBoxNameById(Integer id);

	// 报表导出
	public int getExoprtRpt(String type, String strSql, String rptXMLPath,
			String exportPath);

	// 删除数据库中的主样品图片
	public boolean deletePicImg(Integer Id);

	// 批量修改
	public Boolean modifyBatch(String ids, Map<?, ?> map, Integer flag,
			boolean fit, boolean price,boolean pack);

	// 根据表格查询条件批量修改
	public Boolean modifyBatchAll(Map<String, String> queryMap, Map<?, ?> map,
			Integer flag, boolean fit, boolean price,boolean pack);

	// 查询要导出报表的样品数据
	public String[][] findExportData(String ids, String page,
			String queryString, String queryStringHQL);

	// 查询要导出报表的样品数据
	public void saveCheckMachineData(String ids, String page,
			String queryStringHQL);

	// 获取样品图片的HTML显示，用图片演示用
	public String getEleImgHtml(int CurrentIndex, int countOnEachPage,
			CotElementsVO queryCondition, int imgHeigth, int imgWidth);

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo);

	// 得到objName的集合
	public List<?> getList(String objName);

	// 得到默认objName的集合
	public List<?> getDefaultList();

	// 得到样品总记录数
	public Integer getCount();

	// 根据条件查询主报价单记录
	public List<?> getList(QueryInfo queryInfo);

	// 判断产品货号是否存在
	public boolean findIsExistEleId(String eleId, Integer eId);

	// 获取查询的记录数,图片演示用
	public int findEleCount(CotElementsVO queryCondition);

	// 根据货号取得样品对象
	public CotElementsNew getElementsByEleId(String eleId);

	// 根据货号取得样品对象
	public CotElementsNew getEleByEleId(String eleId);

	// 绑定货号客号关系
	public void saveEleCustNo(String EleId, String CustNo, String type,
			Integer typePrimId, Integer typeDetailId, Integer custId,
			String eleNameEn);

	// 根据类型和主单号删除货号客号对应关系
	public void deleteEleCustNoByTypeAndPrimId(String type, Integer typePrimId);

	// 根据类型和详细单号删除货号客号对应关系
	public void deleteEleCustNoByTypeAndDetailId(String type, Integer typeDetail);

	// 根据主单号，货号，类型删除某个货号客号对应关系
	public void deleteEleCustByCodition(String EleId, String type,
			Integer typePrimId);

	// 根据货号获取最近的客号
	public List<CotEleIdCustNo> getCustNoListByEleId(String EleId);

	// 根据客号查询货号，查询最近一个
	public Map getCustNoListMapByEleId(String EleIds, Integer custId);

	// 根据货号返回客号
	public String getCustNoByEleId(String EleId, Integer custId);

	// 判断客号是否在货号客号表中存在,如果不存在则添加到对应关系表
	public void saveEleCustNoByCustList(Map elecustMap, Map elenameenMap,
			Integer custId, String type);

	// 根据图片编号获得图片
	public CotPicture getPicById(Integer pictureId);

	// 更改默认图片
	public void updateDefaultPic(String filePath, Integer eId) throws Exception;

	// 上传其他图片
	public void saveOtherPic(String filePath, Integer eId) throws Exception;
	
	//更新其他图片的名称
	public void updateOtherPic(List list);

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic();

	// 获得没有权限图片的图片字节
	public byte[] getNoPicSel();

	// 获取注册软件版本
	public String getSoftVer();

	// 获取样品其他图片的HTML显示，用图片演示用
	public String getOtherPicHtml(int currentPage, int countOnEachPage,
			CotPicture queryCondition, int imgHeigth, int imgWidth);

	// 获取样品其他图片查询的记录数,图片演示用
	public int findOtherPicCount(CotPicture queryCondition);

	// 获取查询客号的记录
	public List<?> getCotEleIdCustNoList(int currentPage, int countOnEachPage,
			CotEleIdCustNo queryCondition);

	// 获取查询客号的记录数
	public int findCotEleIdCustNoCount(CotEleIdCustNo queryCondition);

	// 根据条件查询出样品的货号和编号的map
	public List<?> findByParms(Map<String, String> map);

	// 根据查询条件删除样品
	public void deleteEleByCondition(Map<String, String> map);

	// 查询条件删除样品得到样品总记录数
	public Integer getCountByCondition(Map<String, String> map);

	// public int getRecountCountByTable(String tableName);

	// 设置外销价值
	public Double getPriceOut(Float priceFac, Float tuiLv,
			Integer priceFacUint, Integer priceOutUint, Float liRun, Float cbm,
			Integer boxObCount);

	// 通过币种id取得汇率
	public Float getCurRate(Integer id);

	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit();

	// 根据厂家编号获取简称
	public String getFacShortName(Integer id) throws Exception;

	// 根据海关编码id获取退税率
	public Float getTaxRate(Integer id);

	// 获取子货号的所有中英文规格
	public List getEleSizeAndInchDesc(Integer parentId);

	// 删除综合查询中样品档案记录
	public void deleteCustNo(Integer id);

	// 判断配件号是否已经添加
	public boolean checkExistFitNo(String fitNo, Integer eleid, String eleNo,
			String flag);

	// 根据配件号组装产品对象
	public CotEleFittings findDetailByFitNo(String fitNo);

	// 获取fitMap
	public TreeMap<String, CotEleFittings> getFitMap();

	// 储存fitMap
	public void setFitMap(String fitNo, CotEleFittings eleFitting);

	// Action获取fitMap
	public TreeMap<String, CotEleFittings> getFitMapAction(HttpSession session);

	// Action储存fitMap
	public void setFitMapAction(HttpSession session, String fitNo,
			CotEleFittings eleFitting);

	// 清空fitMap
	public void clearFitMap();

	// 清除fitMap中fitNo对应的映射
	public void delFitMapByKey(String fitNo);

	// 在Action中清除fitMap中fitNo对应的映射
	public void delFitMapByKeyAction(String fitNo, HttpSession session);

	// 根据fitid查询配件信息
	public CotFittings getFittingById(Integer fitId);

	// 通过key获取Map的value
	public CotEleFittings getFitMapValue(String fitNo);

	// 通过单号修改Map中对应的配件明细
	public boolean updateMapValueByFitNo(String fitNo, String property,
			String value);

	// 保存配件明细
	public Boolean addEleFitting(List<?> details);

	// 更新配件明细
	public Boolean modifyEleFitting(List<CotEleFittings> detailList);

	// 根据配件明细的ids删除
	public boolean deleteEleFittingByIds(List<Integer> ids);

	// 根据编号查找配件明细单信息
	public CotEleFittings getEleFittingById(Integer detailId);

	// 根据配件号查询配件信息
	public CotFittings getFittingByFitNO(String fitNo);

	// 根据id获取厂家信息
	public CotFactory getFactoryById(Integer id);

	// 根据父货号id及子货号获取配件总成本
	public Float getTotalFitPrice(Integer parentId, String eleNo);

	// 修改子货号配件成本
	public void modifyChildFitPrice(Integer parentId, String eleNo);

	// 根据父货号id获取配件总成本
	public Float getParentTotalFitPrice(Integer parentId);

	// 修改父货号配件成本
	public void modifyParentFitPrice(Integer parentId);

	// 计算生产价
	public Double calPriceFac(Integer id);

	// 更改货号生产价
	public void modifyPriceFac(Integer id, Float priceFac);

	// 批量修改样品价格
	public void modifyPriceBatch(Integer flag);

	// 根据样品编号计算生产价 先根据新的成本总和加上样品中的配件总和
	public Float[] modifyPriceFacByEleId(Integer id);

	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId);

	// 计算价格
	public String calPrice(CotElementsNew elements, Integer boxPackingId);
	
	//计算价格
	public Float[] calPriceAll(CotElementsNew elements);

	// 保存包装方案｛参数中数组的顺序对应外、中、内、产品｝
	public void saveOrUpdateElePacking(Integer eleId, CotElementsNew element,
			String[] priceAry, String[] oldIdAry);

	// 保存每种包装成本
	public void saveOrUpdatePacking(Integer eleId, Integer boxPackingId,
			String price, Long count, String oldId);

	// 根据包材价格调整生产价
	public Double calPriceFacByPackPrice(Integer id, String packingPrice);
	
	//批量修改包材成本
	public void updatePackPrice(String ids);
	
	//修改全部样品包材成本
	public void updatePackPriceAll();
	
	//
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	// 根据条件查询主采购记录
	public List<?> getOrderFacList(QueryInfo queryInfo);
	
	//根据查询条件查询样品编号字符串
	public String findEles(String queryStringHQL);
	
	//MC550文件导出
	public void saveCheckMachineData4Mc550(String ids, String page,
			String queryStringHQL) throws IOException;
	
	// 根据货号取得样品对象
	public void createPic(String eleId);
	
	// 查找所有海关海关编码的退税率
	public Float getEleTax(Integer eId);
	
}
