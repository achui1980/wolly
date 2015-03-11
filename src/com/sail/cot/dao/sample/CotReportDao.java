/**
 * 
 */
package com.sail.cot.dao.sample;

import java.util.List;
import java.util.Map;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.query.QueryInfo;

/**
 * 样品报表导入
 * @author qh-chzy
 *
 */
public interface CotReportDao extends CotBaseDao {
	
	/**
     * 判断样品编号是否重复
     * @param eleId
     * @return
     */
    public Integer isExistEleId(String eleId);
	
	/**
     * 判断类别名称是否存在
     * @param typeName
     * @return
     */
    public Integer isExistTypeName(String typeName);
    
    /**
	 * 判断产品分类是否存在
	 * @param typeName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistEleTypeName(String typeName);
    
    /**
     * 判断海关编吗是否存在
     * @param hsId
     * @return
     */
    public Integer isExistHsId(String hsId);
    
    /**
     * 判断厂家简称是否存在
     * @param shortName
     * @return
     */
    public Integer isExistFactoryShortName(String shortName);
    
    /**
     * 判断币种是否存在
     * @param curUnit
     * @return
     */
    public Integer isExistCurUnit(String curUnit);
    
  //得到所有配件编号,返回map(key为配件编号,value为配件Id)
	public Map<String, Integer> getAllFitNo();
    
	//得到所有样品编号,返回map(key为样品编号,value为类别Id)
	public Map<String, Integer> getAllEleId();
	
	//得到所有样品父货号的编号,返回map(key为样品编号,value为类别Id)
	public Map<String, Integer> getAllParentEleId();
	
	//得到所有样品材质返回map(key为材质中文名称,value为类别Id)
	public Map<String, Integer> getAllTypeCn();
	
	//得到所有订单明细,返回map(key为明显编号,value为CotOrderDetail)
	public  List<?> getOrderDetails(Integer orderId);
	
	//得到所有寄样明细,返回map(key为明显编号,value为CotGivenDetail)
	public  List<?> getGivenDetails(Integer givenId);
	
	//得到所有报价明细
	public List<?> getPriceDetails(Integer orderId);
	
	//得到所有样品材质,返回map(key为材质英文名称,value为类别Id)
	public Map<String, Integer> getAllTypeEn();
	
	//得到所有产品类别返回map(key为类别名称,value为类别Id)
	public Map<String, Integer> getAllEleType();
	
	//得到所有币种,返回map(key为币种名称,value为Id)
	public Map<String, Integer> getAllCurrency();
	
	//得到所有厂家简称,返回map(key为厂家简称,value为Id)
	public Map<String, Integer> getAllFactoryShortName();
	
	//得到所有配件类别,返回map(key为配件类别名称,value为类别Id)
	public Map<String, Integer> getTypeLv3Name();
	
	//得到所有海关编码,返回map(key为海关编码,value为Id)
	public Map<String, Integer> getAllHsId();
	
	//得到所有包装类型返回map(key为类别中文名称,value为类别Id)
	public Map<String, String[]> getAllBoxTypeByCn();
	
	//得到所有包装类型返回map(key为类别英文名称,value为类别Id)
	public Map<String, String[]> getAllBoxTypeByEn();
	
	//设置导入时样品的默认值
	public CotElementsNew setDefault(CotEleCfg cfg,CotElementsNew e,Boolean isExist);
	
	//设置导入报价明细的默认值
	public CotPriceDetail setPriceDefault(CotEleCfg cfg,CotPriceDetail e);
	
	//设置导入时订单的默认值
	public CotOrderDetail setOrderDefault(CotEleCfg cfg,CotOrderDetail e);
	
	//设置导入时寄样的默认值
	public CotGivenDetail setGivenDefault(CotEleCfg cfg,CotGivenDetail e);
	
	//获得暂无图片的图片字节
	public byte[] getZwtpPic();
	
	//取得默认的样品配置
	public CotEleCfg getDefaultEleCfg();
	
	//取得业务配置
	public CotPriceCfg getPriceCfg();
	
	// 获得20/40/40HQ/45的柜体积(默认24/54/68/86)
	public Float[] getContainerCube();
	
	//根据样品默认配置的公式和厂价求外销价
	public Float getPriceOut(Map map);
	
	//通过币种id取得汇率
	public Float getCurRate(Integer id);
	
	//判断该名称在该货号是否重复
	public CotElePrice checkElePriceName(String name,Integer eId);
	
	//查询所有包装材料
	public Map getAllBoxPacking();
	
	//计算样品包材价格
	public CotElementsNew calPrice(Map map,CotElementsNew elements);
	//计算报价包材价格
	public CotPriceDetail calPrice(Map map,CotPriceDetail obj);
	
	//计算订单包材价格
	public CotOrderDetail calPrice(Map map,CotOrderDetail obj);
	
	//计算送样包材价格
	public CotGivenDetail calPrice(Map map,CotGivenDetail obj);
	
	//计算出货明细包材价格
	public CotOrderOutdetail calPrice(Map map,CotOrderOutdetail obj);
	
}
