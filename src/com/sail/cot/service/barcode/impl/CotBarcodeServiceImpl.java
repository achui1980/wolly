package com.sail.cot.service.barcode.impl;
 
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.linear.code39.Code39Barcode;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.barcode.CotBarcodeDao;
import com.sail.cot.domain.CotBarcode;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.service.barcode.CotBarcodeService;

public class CotBarcodeServiceImpl implements CotBarcodeService {
	
	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private CotBarcodeDao barcodeDao;
	
	public CotBarcodeDao getBarcodeDao() {
		return barcodeDao;
	}

	public void setBarcodeDao(CotBarcodeDao barcodeDao) {
		this.barcodeDao = barcodeDao;
	}
	

	//得到objName的集合
	public List<?> getObjList(String objName){
		
		return this.getCotBaseDao().getRecords(objName);
	}
	
	//判断样品表中是否存在货号eleId，存在返回真实货号
	@SuppressWarnings("unchecked")
	public String isExistEleId(String eleId) {
		List<CotElementsNew> elementsNewList = new ArrayList<CotElementsNew>();
		elementsNewList = this.getCotBaseDao().find("from CotElementsNew c where c.eleId='" + eleId + "'");
		if (elementsNewList.size() > 0) {
			return elementsNewList.get(0).getEleId();
		} else {
			return null;
		}
	}
	
	//批量添加条形码
	public void addBarcode(List<CotBarcode> barcodeList) {
		try {
			this.getCotBaseDao().saveRecords(barcodeList);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
	}
	
	//通过登陆人Id删除所有此登陆人添加的条形码记录
	public boolean delBarcodeByAddEmp(Integer empId) {
		boolean res = this.getBarcodeDao().delBarcodeByAddEmp(empId);
		return res;
	}
	
	
	//通过货号获取套件数
	public Object[] getEleUnitNum(String eleId){
		String hql = "select ele.eleId, ele.eleName, ele.eleUnitNum,ele.id from CotElementsNew ele where ele.eleId=?";
		Object[] obj = new Object[1];
		obj[0]=eleId;
		List<?> list = this.getCotBaseDao().queryForLists(hql, obj);
		if(list.size()>0){
			Object[] object = (Object[]) list.get(0);
			return object;
		}
		return null;
	}
	
	//根据条件查找样品信息并保存至条形码
	public boolean saveAllBarcode(String eleId, String eleTypeidLv1,String factoryId, String eleCol,Integer defaultCount,Integer empId){
		int res = this.getBarcodeDao().saveAllBarcode(eleId, eleTypeidLv1, factoryId, eleCol, defaultCount, empId);
		if(res==0){
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.barcode.CotBarcodeService#checkBarcode(java.lang.String)
	 */
	public boolean checkBarcode(String msg) {
		try {
			Code39Barcode code39Barcode = new Code39Barcode(msg,true);
			//System.out.println("msg:"+msg+" checked success");
		} catch (BarcodeException e) {
			System.out.println("msg:"+msg+" barcode checked error");
			return false;
		}
		return true;
	}

}
