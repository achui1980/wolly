 
package com.sail.cot.dao.barcode;
 
 

import com.sail.cot.dao.CotBaseDao; 

 
public interface CotBarcodeDao extends CotBaseDao {
	
	//通过登陆人Id删除所有此登陆人添加的条形码记录
	public boolean delBarcodeByAddEmp(Integer empId);

	 //根据条件查找样品信息并保存至条形码
	public int saveAllBarcode(String eleId, String eleTypeidLv1,String factoryId, String eleCol,Integer defaultCount,Integer empId);

}
