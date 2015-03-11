package com.sail.cot.service.given;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotGiven;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPriceDetail; 
import com.sail.cot.domain.CotSign;
import com.sail.cot.query.QueryInfo;

public interface CotGivenService {

	// 查询送样单单号是否存在
	public Integer findIsExistGivenNo(String givenNo,String id);
	
	//根据查询语句获取记录
	public List<?> getList(QueryInfo queryInfo);
	
	//根据查询语句获取总记录数	
	public int getRecordCount(QueryInfo queryInfo);
	
	//得到objName的集合	
	public List<?> getObjList(String objName);
	
	// 删除主送样单	
	public Boolean deleteGivenList(List<CotGiven> givenList);
	
	// 删除送样单的产品明细	
	public Boolean deleteGivenDetailList(List<CotGivenDetail> givenDetailList);
	
	// 根据送样编号查找送样单信息
	public CotGiven getGivenById(Integer id);
	
	//查询该单的所有送样明细的产品货号
	public String findEleByGivenId(Integer givenId);
	
	// 根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id);
	
	// 根据厂家编号查找厂家信息
	public CotFactory getFactoryById(Integer id);
	
	public List<?> getDetailListByGivenId(Integer givenId);
	
	//获得客户简称的映射
	public Map<?, ?> getCustMap();
	
	//获得厂家简称的映射
	public Map<?, ?> getFacMap();
	
	//获得样品材质的映射
	public Map<?, ?> getTypeMap();
	
	//获得包装方式的映射
	public Map<?, ?>  getBoxTypeMap();
	
	//查询所有用户姓名
	public Map<?, ?> getEmpsMap();
	
	//根据送样明细编号查找送样明细单信息
	public CotGivenDetail getGivenDetailById(Integer detailId);
	
	//根据送样明细编号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId);
	
	//更新送样明细的产品
	public Boolean modifyGivenDetail(List<CotGivenDetail> detailList);
	
	// 更新征样单的产品明细
	public Boolean updateGivenDetail(CotGivenDetail e, String eleProTime);
	
	//删除送样明细单产品的原来图片
	public void deleteGivenDetailImg(Integer id);
	
	public List<?> getGivenTypeList();
	
	public List<?> getBalanceTypeList();
	
	//保存主送样单
	public Integer saveOrUpdateGiven(CotGiven cotGiven , String givenTime, String custRequiretime, String realGiventime);
	
	// 保存主单
	public Integer saveByExcel(CotGiven cotGiven, String givenTime, String custRequiretime,String realGiventime);
	
	//修改主送样单
	public Boolean modifyGivenList(List<?> givenList);
	
	// 根据样品货号组装送样明细产品对象 
	public CotGivenDetail findDetailByEleId(String eleId);
	
	//判断该产品货号是否存在
	public Boolean findIsExistEleByEleId(String eleId);

	//保存送样单的产品明细
	public Boolean addGivenDetails(List<?> details);
	
	//根据送样明细产品的ids删除
	public void deleteDetailByIds(HttpServletRequest request,List<Integer> ids);
	
	//通过部门id获取所在部门员工列表
	public List<CotEmps> getDeptEmpListByDeptId(Integer deptId);
	
	//通过员工id获取所在员工信息
	public List<CotEmps> getEmpListByEmpId(Integer empId);
	
	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo);
	
	//查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId);
	
	// 根据报价编号字符串查找报价明细
	public List<CotPriceDetail> findPriceDetailByIds(String ids);
 
	// 根据订单编号字符串查找订单明细
	public List<CotOrderDetail> findOrderDetailByIds(String ids);
	
	//得到objName的集合
	public List<?> getList(String objName);	
	
	//查询所有币种
	public Map<?, ?> getCurrencyMap();
	
	//判断要更新到样品表的明细货号哪些重复
	public Map<String, List<String>> findIsExistInEle(String[] eleIdAry);

	//通过单号删除送样单的所有产品图片
	public void deleteGivenImgByGivenNo(String givenNo);
	
	//获取givenMap
	public  TreeMap<String, CotGivenDetail> getGivenMap();
		
	//储存givenMap
	public  void setGivenMap(String eleId,CotGivenDetail cotGivenDetail);
	
	//Action获取givenMap
	public  TreeMap<String, CotGivenDetail> getGivenMapAction(HttpSession session);
	
	//Action储存givenMap
	public  void setGivenMapAction(HttpSession session,String eleId,CotGivenDetail cotGivenDetail);
	
	//清空givenMap
	public void clearGivenMap();
	
	//清除givenMap中eleId对应的映射
	public void delGivenMapByKey(String eleId);
	
	//在Action中清除givenMap中eleId对应的映射
	public void delGivenMapByKeyAction(String eleId,HttpSession session);
	
	//判断货号是否已经添加
	public boolean checkExist(String eleId);
	
	//判断货号字符串是否已经添加
	public List<String> checkExistEles(String[] eleIds);
	
	//通过key获取givenMap的value 
	public CotGivenDetail getGivenMapValue(String eleId);
	
	//通过货号修改Map中对应的送样明细
	public  boolean updateMapValueByEleId(String eleId ,String property,String value);
 
	
	//根据样品货号字符串获得样品集合并转化为订单明细
	public List<CotGivenDetail> saveDetailByEleIds(String eleIds, Integer mainId);
	
	//判断该主单的明细是否已存在该产品货号
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId);
	
	//根据货号获取样品图片对象(CotPicture)
	public CotPicture getCotPictureByEleId(String eleId);
	
	//根据货号获取样品对象
	public CotElementsNew getElementsByEleId(String eleId);
	
	//更新征样图片picImg字段
	public void updatePicImg(HttpServletRequest request,String filePath,Integer detailId);
	
	//删除征样图片picImg
	public boolean deletePicImg(Integer detailId);
	
	//得到objName的集合
	public List<?> getDicList(String objName);
	
	//查询新增寄样明细的工厂是否已存在
	public boolean checkIsFactory(Integer factoryId,Integer givenId);
	
	//重新分解判断
	public boolean checkIsNew(Integer factoryId,Integer givenId);	
	
	//分解生成主征样单
	public boolean saveSign(String[] factoryIdAry, Integer givenId);
	
	//修改寄样分解状态
	public void modifyGivenStatus(Integer givenId,String flag);
	
	//修改寄样明细分解状态
	public void modifySignFlag(Integer factoryId,Integer givenId);
	
	//删除征样主单时修改寄样明细分解状态
	public void modifySignFlagForDel(Integer factoryId,Integer givenId);
	
	//根据送样编号查找征样单
	public List<?> getSignByGivenId(Integer givenId);
	
	public CotSign getSignById(Integer id);
	
	//删除征样单
	public Boolean deleteSignList(List<CotSign> signList);
	
	//计算寄样单样品总费用
	public Float calSampleFee(Integer givenId);
	
	//删除征样明细
	public boolean deleteSignDetail(Integer id);
	
	//根据主寄样单号检索所有明细记录的征样状态
	public Integer getChangeNum(Integer gid);
	
	//根据文件路径和行号导入excel
	public List<?> updateOneReport(String filename, Integer rowNum,Integer givenId, boolean isCover);
	
	//根据文件路径导入
	public List<?> saveReport(String filename,boolean isCover,Integer givenId);
	
	//判断货号字符串是否已经添加
	public boolean checkExistByExcel();
	
	// 清空excel的缓存
	public void removeExcelSession();
	
	// 清空Pan的缓存
	public void removePanSession();
	
	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价(excel导入)
	public CotGivenDetail findDetailByEleIdExcel(String eleId,Map<?, ?> map, String liRunCau);
	
	// 保存明细
	public void saveDetail(Integer givenId, Integer currencyId, Integer custId);
	
	// 获得暂无图片的图片字节
	public byte[] getZwtpPic();
	
	// 将该货号对应的样品转成寄样明细
	public CotGivenDetail changeEleToGivenDetail(String eleId);
	
	//获取当前登陆员工信息
	public Boolean checkCurrEmpsIsSuperAd();
	
	//返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate);
	
	//批量删除时对审核通过单据的处理
	public String givenIsChecks(List<CotGiven> givenList);
 
	//更新送样审核状态
	public int saveGivenStatus(Integer id,Long status);
	
	//保存报价、订单导入明细
	public void saveGivenDetail(String  gid,String eleId);
	
	public Float[] calPriceAll(String rdm);
	
	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String eleId, String packingPrice);
	
	// 根据客号查找报价明细表中的货号(取最近报价时间)
	public Object[] findEleByCustNo(String custNo, Integer custId,String gTime);
	
	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);
	
	//判断样品单费用和快递费用是否添加
	public int findIsExist(Integer gId);
	
	//生成应收款
	public boolean saveRecv(Integer gId,Integer flag,Integer curId,Integer currencyId,String sampleFeeStr,String signTotalPriceStr,Integer sampleFeeCheck,Integer checkFee);

	
	// 判断应收帐是否有收款记录
	public boolean checkIsShou(List<Integer> ids);
	
	// 删除应收帐
	public Boolean deleteByAccount(List<?> ids);
	
	// 根据条件查询冲帐明细记录
	public List<?> getRecvDetailList(List<?> list);
	
	// 删除收款记录
	public Boolean deleteByRecvDetail(List<Integer> ids);
	
	// 获取默认公司ID
	public Integer getDefaultCompanyId();
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	// 查找该产品是否有送样、报价、订单
	public Object findIsExistDetail(String eleId, String id,String type);
	
	// 根据上传的盘点机数据存入内存
	public void saveCheckMachine(List<String> list, HttpServletRequest request);
	
	// 得到盘点机的内存map的key集合
	public void savePanList(String no,Integer givenId);
	
	// 从session中取得上传的盘点机流水号
	public List<String> getMachineNum();
	
	// 根据盘点机流水号获得明细字符串集合
	public List<String> getMachineDetails(String checkNo);
	
	// 根据盘点机流水号获得明细对象集合
	public List<CotGivenDetail> getMachineDetailList(String checkNo);
	
	// 重新排序
	public boolean updateSortNo(Integer id,Integer type,String field,String fieldType);
	
	// 根据报价的图片转换成样品图片
	public CotElePic changeElePic(String type, Integer srcId, CotElePic pic);
	
	// 判断该产品是否对该用户报价过
	public Object[] findDetail(String eleId, String cusId,String pTime);
	
	// 同步不同的样品的图片
	public Map getGivenByDisEles(String eleId);
	
	// 根据样品货号字符串查询明细
	public void updateToEle(String[] same, String[] dis, String eleStr, String boxStr, String otherStr,
			boolean isPic);
	
	// 根据同步选择项同步更新样品
	public CotElementsNew setEleByTong(CotElementsNew old,
			CotGivenDetail newEle, String eleStr, String boxStr, String otherStr);
	
	// 同步相同的样品的图片
	public Object[] getGivenByEle(String eleId, CotElePic cotElePic);
	
	// 查询VO记录
	public List<?> getGivenVOList(QueryInfo queryInfo);
	
	// 储存Map
	public void setExcelMap(String rdm, CotGivenDetail cotGivenDetail);
	//查询是否有应付账款
	public Integer getDealNumById(Integer givenid);
	
	// 计算价格
	public Float[] calPriceAllByEleId(String eleId);
	
	
}
