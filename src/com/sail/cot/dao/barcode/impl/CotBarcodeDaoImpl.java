package com.sail.cot.dao.barcode.impl;
 
import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.Statement;  

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.sail.cot.dao.barcode.CotBarcodeDao;
import com.sail.cot.dao.impl.CotBaseDaoImpl; 
import com.sail.cot.domain.CotBarcode;

public class CotBarcodeDaoImpl extends CotBaseDaoImpl implements CotBarcodeDao {
	
	//通过登陆人Id删除所有此登陆人添加的条形码记录
	public boolean delBarcodeByAddEmp(Integer empId) {
		String sql = "delete obj from cot_barcode obj where obj.ADD_EMP="+empId;
		try {
			Connection conn = super.getSession().connection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false; 
		}
		return true; 
	}
	
    //根据条件查找样品信息并保存至条形码
	public int saveAllBarcode(String eleId, String eleTypeidLv1, String factoryId, String eleCol,Integer defaultCount,Integer empId){
		
		String sql = "select ele.ELE_ID, ele.ELE_UNITNUM from cot_elements_new ele";
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.ELE_ID like '%" + eleId.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and ele.ELE_TYPEID_LV1=" + eleTypeidLv1.toString());
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.FACTORY_ID=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and ele.ELE_COL like '%" + eleCol.toString().trim() + "%'");
		}
		sql += queryString;
		
		String eleIdRes = null;
		Integer eleUnitNum = null;
		
		Connection conn = null;
		ResultSet rs = null;
		int i = 0;
		try {
			conn = super.getSession().connection();
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				i+=1;
				eleIdRes = rs.getString(1);
				eleUnitNum = rs.getInt(2);
				//打印数量
				Integer printCount = eleUnitNum * defaultCount;
				for (int j = 0; j < printCount; j++) {
					CotBarcode cotBarcode = new CotBarcode();
					cotBarcode.setEleId(eleIdRes);
					cotBarcode.setAddEmp(empId);
					this.create(cotBarcode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	 

	 
}
