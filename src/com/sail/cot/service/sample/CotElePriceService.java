/**
 * 
 */
package com.sail.cot.service.sample;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.query.QueryInfo;
 


/**
 * *********************************************
 * @Copyright :(C),2009-2010
 * @CompanyName :厦门旗航有限公司(Sailingsoftware.com)
 * @Version :旗航办公自动化系统1.0
 * @Date :Sep 10, 2009
 * @author : qh-chzy
 * @class :CotElePriceService.java
 * @Description :
 */
public interface CotElePriceService {
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);
	
	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo);
	
	//保存,返回最新生产价
	public Float saveOrUpdateElePrice(CotElePrice elePrice,Double oldPrice,Integer oldCur);
	
	//删除,返回最新生产价
	public Float deleteElePrices(List<Integer> ids);
	
	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);
	
	//判断成本名称是否重复
	public boolean checkIsExist(String priceName,Integer id,Integer mainId);
	
	//根据编号获得对象
	public CotElePrice getCotElePriceById(Integer id);
	
	// 得到objName的集合
	public List<?> getList(String objName);
	//根据文件路径导入
	public List<?> saveReport(String filePath,Boolean isCover);
	
	//根据文件路径和行号导入excel
	public List<?> updateOneReport(String filePath,Integer rowNum, Boolean isCover);
	
	//保存
	public void addList(List<?> list);
	
	//修改
	public void updateList(List<?> list,Map<Integer,String[]> map) ;
	
	//  删除
	public void deleteList(List<Integer> ids);
	
	//根据样品编号查询生产价
	public Float[] modifyPriceFacByEleId(Integer id);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
