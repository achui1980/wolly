/**
 * 
 */
package com.sail.cot.dao.sample;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotPriceFac;
import com.sail.cot.domain.CotPriceOut;
import com.sail.cot.domain.vo.CotChildElementsVO;
import com.sail.cot.domain.vo.CotEleIdCustNo;
import com.sail.cot.query.QueryInfo;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 31, 2008 5:55:16 PM </p>
 * <p>Class Name: CotEmpsDao.java </p>
 * @author achui
 *
 */
public interface CotElementsDao extends CotBaseDao {

	/**
	 * 判断样品编号是否重复
	 * @param eleId
	 * @return
	 */
	public Integer isExistEleId(String eleId);

	/**
	 * jdbc样品表左联图片表查询
	 * @return
	 */
	public List<?> findCotElementsVOList(String sql);

	/**
	 * 判断类别名称是否存在
	 * @param typeName
	 * @return
	 */
	public Boolean isExistTypeName(String typeName);

	/**
	 * 判断厂家编号是否存在
	 * @param factoryNo
	 * @return
	 */
	public Integer isExistFactoryNo(String factoryNo);

	//修改图片当前的默认图标的picFlag为0
	public void updateDefaultPic(Integer cotElementsId);

	//按类别生成查询树,"factory"按厂家生成,"type"按样品类别生成
	List getQueryCondition(String queryType);

	/**
	 * 批量修改样品
	 * @param ids
	 * @param map
	 */
	public void modifyBatch(String ids, Map<?, ?> map,Integer flag,boolean fit,boolean price,boolean pack);
	
	//根据查询条件批量修改
	public void modifyBatchAll(Map<?, ?> queryMap, Map<?, ?> map,Integer flag,boolean fit,boolean price,boolean pack)throws Exception ;

	/**
	 * 判断图片是否在这个样品中存在
	 * @param eleId
	 * @param picName
	 */
	public Boolean isExistPicture(Integer eleId, String picName);

	/**
	 * 查询要导出的样品数据
	 * @param ids
	 * @param page
	 * @return String[][]
	 */
	public String[][] findExportData(String ids, String page,
			String queryString, String queryStringHQL);

	//保存盘点的样品数据到文件upload/BaseInfo.txt
	public void saveCheckMachineData(String ids, String page,
			String queryStringHQL)throws IOException;

	/**
	 * 查找未定义的厂家
	 * @return
	 */
	public CotFactory findFactoryDefault();

	//绑定货号客号关系
	public void saveEleCustNo(String EleId, String CustNo, String type,
			Integer typePrimId, Integer typeDetailId, Integer custId,
			String eleNameEn);

	//根据类型和主单号删除货号客号对应关系
	public void deleteEleCustNoByTypeAndPrimId(String type, Integer typePrimId);

	//根据类型和详细单号删除货号客号对应关系
	public void deleteEleCustNoByTypeAndDetailId(String type, Integer typeDetail);

	//根据主单号，货号，类型删除某个货号客号对应关系
	public void deleteEleCustByCodition(String EleId, String type,
			Integer typePrimId);

	//根据货号获取最近的客号
	//public  void updateEleCustNoByTypeDetailId(Integer typeDetail);

	//根据货号获取最近的客号
	public List<CotEleIdCustNo> getCustNoListByEleId(String EleId);

	//根据客号查询货号，查询最近一个
	public Map getCustNoListMapByEleId(String EleId, Integer custId);

	//判断客号是否在货号客号表中存在,如果不存在则添加到对应关系表
	public void saveEleCustNoByCustList(Map elecustMap, Map elenameenMap,
			Integer custId, String type);

	//根据货号返回客号
	public String getCustNoByEleId(String EleId, Integer custId);

	//获得暂无图片的图片字节
	public byte[] getZwtpPic();

	public int getRecordsCountJDBC(QueryInfo queryInfo);
	
	public void deleteEleCustNoById(Integer id);
	
	//取得默认的样品配置
	public CotEleCfg getDefaultEleCfg();
	
	//根据查询条件查询样品编号字符串
	public String findEles(String queryStringHQL) throws IOException;
	//MC550文件导出
	public void saveCheckMachineData4Mc550(String ids, String page,
			String queryStringHQL) throws IOException;

}
