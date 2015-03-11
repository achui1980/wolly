package com.sail.cot.service.system;

import java.util.List;
 

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.vo.CotAddressVO;
import com.sail.cot.query.QueryInfo;
public interface CotFactoryService {
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);

	List<?> getFactoryList();
	
	public List<?> getTypeLv1List();
	 
	CotFactory getFactoryById(Integer Id);
	 
	boolean addFactory(List<CotFactory> factoryList);
 
	boolean modifyFactory(List<CotFactory> factoryList);
	 
	boolean deleteFactory(List<CotFactory> factoryList);
	
	public int deleteFactoryById(Integer Id);
 
	boolean findExistByName(String name);
    
    //获取厂家编号
    String getFacNo();
    
	public boolean findElementsRecordsCount(String factoryId);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public Integer findExistByNo(String No,String id);
	
	//保存和修改
	public boolean saveOrUpdate(CotFactory factory,List<?> contact);
	
	//判断厂家简称重复性
	public boolean findIsExistShortName(int id,String shortName);
	/**
	 * 获得联系人ID，工厂名称，联系人名称，联系人邮件信息
	 * @param queryInfo
	 * @return
	 */
	public List<CotAddressVO> getFactoryMailList(QueryInfo queryInfo);
	
	// 查询厂家报价VO记录
	public List<?> getPriceFacVO(QueryInfo queryInfo);
	
}
