/**
 * 
 */
package com.sail.cot.service.price;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotPrice;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotPriceEleprice;
import com.sail.cot.domain.CotPriceFittings;
import com.sail.cot.domain.vo.CotNewPriceVO;
import com.sail.cot.query.QueryInfo;

public interface CotPriceService {

	//根据报价编号查找报价单信息
	public CotPrice getPriceById(Integer id);

	//根据报价明细编号查找报价明细单信息
	public CotPriceDetail getPriceDetailById(Integer id);

	//根据报价明细货号查找报价明细单信息
	public CotPriceDetail getPriceDetailByEleId(String eleId);

	//保存报价单的产品明细
	public Boolean addPriceDetails(List<?> details);

	//更新报价单的产品明细
	public Boolean modifyPriceDetails(List<?> details);

	//更新报价单的产品明细
	public Boolean modifyPriceDetail(CotPriceDetail e, String eleProTime);

	//删除主报价单
	public Boolean deletePrices(List<?> prices);

	//保存或者更新主报价单
	public Integer saveOrUpdatePrice(CotPrice cotPrice, String priceTime,
			boolean flag, Map<String, String> map, String liRunCau);

	//删除报价单的产品明细
	public Boolean deletePriceDetails(HttpServletRequest request,List<?> details);

	//根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id);

	//得到总记录数
	public Integer getRecordCount(String objName, String whereStr);

	//得到objName的集合
	public List<?> getList(String objName);

	//根据主报价单号找报价明细
	public List<?> getDetailByPriceId(Integer priceId);

	//查询所有厂家名称
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request);
	
	// 查询配件厂家
	public Map<?, ?> getFitFactoryNameMap(HttpServletRequest request);

	//查询所有客户名称
	public Map<?, ?> getCusNameMap();

	// 查询所有客户简称
	public Map<?, ?> getCusShortNameMap();

	// 查询所有客户编号
	public Map<?, ?> getCustNoMap();

	// 查询所有客户邮箱
	public Map<?, ?> getCustMailMap();

	//查询所有价格条款
	public Map<?, ?> getClauseMap(HttpServletRequest request);
	
	// 查询所有报价场合
	public Map<?, ?> getSituationMap(HttpServletRequest request);

	//查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);

	//查询所有材质
	public Map<?, ?> getTypeMap();

	//查询所有用户姓名
	public Map<?, ?> getEmpsMap();

	//查询报价单单号是否存在
	public Integer findIsExistPriceNo(String priceNo,String id);

	//根据条件查询样品记录
	public List<?> getElementList(QueryInfo queryInfo);

	//根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo);

	//查询记录
	public List<?> getList(QueryInfo queryInfo);
	
	// 如果是报价或订单的配件.需要判断该配件厂家或者采购价格是否和配件库一致,如果不同,用配件库的最新数据代替
	public List<?> getNewList(String tableName, List list);

	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo);


	//查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId);

	//根据样品货号组装报价明细产品对象,并根据报价参数算出单价
	public CotPriceDetail findDetailByEleId(CotPriceDetail detail,Map<?, ?> map, String liRunCau);
	
	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价(excel导入)
	public CotPriceDetail findDetailByEleIdExcel(String eleId,
			boolean isUsePriceOut, Map<?, ?> map, String liRunCau);

	//根据报价明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids);

	//查询该单的所有报价明细的产品货号
	public String findEleByPriceId(Integer priceId);

	//根据要更改的产品货号字符串 和 价格条件更改单价,再返回这组单价
	public Map<String, Float[]> getNewPrice(String rdmIds, Map<?, ?> map,
			String liRunCau);
	
	// 根据币种改变返回这组单价
	public Map<String, Float[]> getNewPriceByCurreny(String eleIds, Map<?, ?> map,
			String liRunCau,Integer oldCur);

	// 根据明细中的利润率代入主单利润率算出最新价格
	public Float getNewPriceByLiRun(Map<?, ?> map, String rdm);
	
	// 根据明细中的退税率算出最新价格
	public Float getNewPriceByTuiLv(Map<?, ?> map, String rdm);
	
	// 根据明细中的生产价算出最新价格
	public Float getNewPriceByPriceFac(Map<?, ?> map, String rdm);
	
	// 根据明细中的生产价币种算出最新价格
	public Float getNewPriceByPriceFacUint(Map<?, ?> map, String rdm);

	//根据产品货号和客户编号查找该产品对该客户的历史报价
	public CotNewPriceVO findNewPriceVO(String eleId, Integer cusId);

	//判断该产品是否对该用户报价过
	public Object[] findIsExistDetail(String eleId, String cusId,String clauseId,String priceTime);

	//判断该产品货号是否存在
	public Integer findIsExistEleByEleId(String eleId);

	//发邮件
	public String saveMail(Map<?, ?> map);

	//获取邮件对象Id
	public String getCotMailId(Map<?, ?> map);

	//根据产品货号查找对外报价
	public Float findPriceOutByEleId(String eleId);

	// 判断要更新到样品表的明细货号哪些重复
	public Map<String,List<String>> findIsExistInEle(String[] key);
	
	// 判断要更新到样品表的明细货号哪些重复
	public List<?> updateEleToDetail(String[] eleAry,String[] rdmAry, String eleStr,String boxStr,String otherStr,boolean isPic);

	//根据样品货号字符串查询明细
	public void updateToEle(String[] same,String[] sameRdm, String[] dis,String[] disRdm,String eleStr,String boxStr,String otherStr,boolean isPic);

	//判断该主单的明细是否已存在该产品货号
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId);

	//清空Map
	public void clearMap(String typeName);

	// 清空盘点机Map
	public void clearCheckMap();

	//通过key获取Map的value 
	public CotPriceDetail getPriceMapValue(String rdm);

	//储存Map
	public void setPriceMap(String eleId, CotPriceDetail cotPriceDetail);

	//通过货号修改Map中对应的征样明细
	public boolean updateMapValueByEleId(String rdm, String property,
			String value);

	//清除Map中eleId对应的映射
	public void delMapByKey(String eleId);

	//在Action中清除Map中eleId对应的映射
	public void delMapByKeyAction(String rdm, HttpSession session);

	//Action获取signMap
	public Map<String, CotPriceDetail> getMapAction(HttpSession session);
	
	//Action储存signMap
	public void setMapAction(HttpSession session, String rdm,
			CotPriceDetail cotPriceDetail);

	//根据明细货号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId);

	//删除明细图片picImg
	public boolean deletePicImg(Integer detailId);

	//更新征样图片picImg字段
	public void updatePicImg(String filePath, Integer detailId);

	//得到公司的集合
	public List<?> getCompanyList();

	//得到客户的集合
	public List<?> getCustomerList();

	//得到员工的集合
	public List<?> getEmpsList();

	//根据上传的盘点机数据插入到报价单
	public void saveCheckMachine(List<String> list, HttpServletRequest request);

	//从session中取得上传的盘点机流水号
	public List<String> getMachineNum();

	//从session中取得上传的盘点机流水号
	public List<String> getMachineDetails(String checkNo);

	//根据盘点机流水号获得明细对象集合
	public List<CotPriceDetail> getMachineDetailList(String checkNo);

	//根据单号查询该单的客户
	public CotCustomer getCusByPriceId(Integer orderId);

	//根据员工编号获得员工
	public CotEmps getEmpsById(Integer empId);

	//得到单样品利润公式
	public String getLiRunCau();

	//获得新利润率
	public Float getNewLiRun(Map<?, ?> map, String eleId);

	//返回该货号的子货号的id集合
	public List<?> getChildrens(String eleId);

	// 根据文件路径导入
	public List<?> saveReport(Integer orderId,String filename,boolean cover,Integer currencyId,boolean excelFlag);
	
	//根据文件路径和行号导入excel
	public List<?> updateOneReport(String filePath, Integer rowNum, Integer orderId,boolean cover,Integer currencyId,boolean excelFlag);
	
	//清空excel的缓存
	public void removeExcelSession();
	
	// 清空Pan的缓存
	public void removePanSession();
	
	// 得到objName的集合
	public List<?> getDicList(String objName);
	
	//判断明细中是否存在该货号
	public boolean checkIsExistEle(Integer mainId,String eleId);
	
	// 根据客户编号查找客户编号，简称，和联系人
	public String[] getTeCustById(Integer id);
	
	// 根据员工编号获得员工编号和名称
	public String[] getTeEmpsById(Integer empId);
	
	//查询VO记录
	public List<?> getPriceVOList(QueryInfo queryInfo);
	
	//根据客号查找报价明细表中的货号(取最近报价时间)
	public  Object[] findEleByCustNo(String custNo,Integer custId,String clauseId,String pTime);
	
	//将该货号对应的样品转成报价明细
	public CotPriceDetail changeEleToPriceDetail(String eleId);
	
	// 保存主报价单
	public Integer saveByExcel(CotPrice cotPrice, String priceTime)throws Exception;
	
	// 保存明细
	public void saveDetail(Integer priceId, Integer currencyId, Integer custId);
	
	// 根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id);
	
	// 根据起运港港编号获得起运港名称
	public String[] getShipPortNameById(Integer id);
	
	// 通过货号修改Map中对应的明细并返回对象
	public CotPriceDetail updateObjByEle(String eleId, String property,
			String value);
	
	// 得到excel的内存map的key集合
	public List<?> getRdmList();
	
	//返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate);
	
	// 查询配件信息
	public List findFittingsByIds(String ids, Integer priceDetailId);
	
	//增加
	public void addList(List list);
	
	//增加成本
	public void addElePriceList(List list);

	//删除
	public void deleteList(List list,String tabName);

	//修改
	public void modifyList(List list);
	
	// 获得报价配件对象
	public CotPriceFittings getPriceFittingById(Integer id);
	
	// 获得报价明细成本对象
	public CotPriceEleprice getElePriceById(Integer id);
	
	//判断输入的配件号是否存在.完全存在的话直接加到表格中,有模糊数据弹出层.
	public List findIsExistFitNo(String fitNo,Integer priceDetailId);
	
	//计算明细的生产价
	public CotPriceDetail updatePriceFac(Integer priceDetailId,Integer rdm,Map<Object, Object> map);
	
	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit();
	
	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String rdm, String packingPrice);
	
	//修改报价审核状态
	public void updatePriceStatus(Integer priceId,Integer priceStatus);
	
	// 计算价格
	public Float[] calPriceAll(String rdm);
	
	//重新计算序列号
	public boolean updateSortNo(Integer id,Integer type,String field,String fieldType);
	
	// 储存Map
	public void setPriceMapAndDel(String rdm, CotPriceDetail cotPriceDetail);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	//查询含有rdm的报价记录
	public String getListData(HttpServletRequest request,QueryInfo queryInfo) throws DAOException;
	
	// 得到盘点机的内存map的key集合
	public void savePanList(String no,Integer priceId);
	
	// 报价同步样品配件信息
	public boolean deleteAndTongEleFitting(Integer priceDetailId, String eleId);
	
	// 报价同步到样品配件
	public boolean deleteAndTongFitting(Integer priceDetailId, String eleId)throws DAOException ;
	
	//修改样品生产价
	public boolean modifyPriceFacByEleId(String eleId);
	
	// 储存Map
	public void setExcelMap(String rdm, CotPriceDetail cotPriceDetail);
	
	//计算勾选的报价明细的InverseMargin
	public Map calLiRun(Map<?, ?> map, List rdmAry);
	
	//用过货号查找样品id
	public Integer getEleIdByEleName(String eleId);
	
	//另存报价单
	public boolean saveAs(String newOrderNo, Integer mainId) throws Exception;
	
	//得到海关编码退税率的值
	public Float getTuiLv(Integer hsId);
	
	//发邮件
	public String sendMail(String priceId,String priceNo,String reportTemple);
}
