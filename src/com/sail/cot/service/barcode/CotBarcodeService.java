package com.sail.cot.service.barcode;

import java.util.List;

import com.sail.cot.domain.CotBarcode;

public interface CotBarcodeService {
	
	//得到objName的集合
	public List<?> getObjList(String objName);
	
	//判断样品表中是否存在货号eleId，存在返回真实货号
	public String isExistEleId(String eleId) ;
	
	//批量添加条形码
	public void addBarcode(List<CotBarcode> barcodeList);
	
	//通过登陆人Id删除所有此登陆人添加的条形码记录
	public boolean delBarcodeByAddEmp(Integer empId);
	
	//通过货号获取套件数和中文名
	public Object[] getEleUnitNum(String eleId);
	
	//根据条件查找样品信息并保存至条形码
	public boolean saveAllBarcode(String eleId, String eleTypeidLv1,String factoryId, String eleCol,Integer defaultCount,Integer empId);
	
	//验证条码是否合法
	public boolean checkBarcode(String msg);
	
}
