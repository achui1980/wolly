package com.sail.cot.service.sample;

import java.util.List;
import java.util.Map;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.query.QueryInfo;

public interface CotEleCfgService {

	//获取邮件列表
	List<?> getEleCfgList();	
	//删除邮件
	public void deleteEleCfg(List<?> eleCfgList);
	
	public boolean delEleCfg(Integer id);
	//根据id获取邮件信息
	public CotEleCfg getEleCfgById(Integer Id);
	
    public	int getRecordCount(QueryInfo queryInfo);
    
    public	List<?> getList(QueryInfo queryInfo);
    //添加样品默认数据
	public void addEleCfg(CotEleCfg cotEleCfg);
	//修改样品默认数据
	public void modifyEleCfg(CotEleCfg cotEleCfg);
	
	public boolean findRecord();
	
	public Map getEleTypeMap();
	
	public Map getEleFacMap();
	
	public Map getCurrencyMap();
	
	public List<?> getObjList(String objName);
	
	//查找所有包装类型
	public List<?> getBoxTypeList();
	
	//通过编号查找包装类型
	public CotBoxType getBoxTypeById(Integer id);
	
	public String getBoxNameById(Integer id);
 
	public Map getBoxTypeMap();

	public void updateDefaultFlag(CotEleCfg cotEleCfg);
	
	boolean IsDefault(Integer Id);
	
	public boolean checkCalculation(String str);
	
	//根据厂家编号获取简称
	public String getFacShortName(Integer id);
	
	public CotEleCfg getEleCfg();

}
