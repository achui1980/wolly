/**
 * 
 */
package com.sail.cot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotPackingOrder;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.seq.Sequece;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;

public class BaseData {

	private CotBaseDao baseDao;
	String excludes[] = { "customers", "cotCustContacts",
			"customerVisitedLogs", "customerClaim", "custImg", "picImg",
			"companyLogo", "cotPictures", "childs", "cotFile", "cotPriceFacs",
			"cotEleFittings", "cotElePrice","cotElePacking","cotOrderOutdetails","cotOrderOuthsdetails","cotSplitInfos",
			"cotHsInfos","cotShipments","cotSymbols","cotOrderouthsRpt","orderMBImg"};

	public CotBaseDao getBaseDao() {
		if (baseDao == null)
			baseDao = (CotBaseDao) SystemUtil.getService("CotBaseDao");
		return baseDao;
	}

	/**
	 * 描述： 获取所有基础数据
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @return 返回值：String json形式的字符串
	 */
	public String getBaseDicData(String tbName, String start, String limit) {
		// Session session =this.getBaseDao().getBaseSession();
		QueryInfo queryInfo = new QueryInfo();
		// 如果是查询样品.排除子货号
		String str = "";
		if(tbName.equals("CotElementsNew")){
//			str=" where obj.eleFlag!=2";
		}
		if(tbName.equals("CotNation")){
			queryInfo.setOrderString(" order by obj.nationName ");
		}
		String hql = " from " + tbName + " obj";
		String countSqlString = "select count(*) from " + tbName + " obj";
		
		queryInfo.setExcludes(excludes);
		if (start != null && !"".equals(start)) {
			queryInfo.setStartIndex(Integer.parseInt(start));
		}
		queryInfo.setCountQuery(countSqlString+str);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString(str);
		if (limit != null && !"".equals(limit)) {
			queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		}
		// Query q=session.createQuery(hql);
		// if (start != null && limit != null){
		// q.setFirstResult(Integer.parseInt(start));
		// q.setMaxResults(Integer.parseInt(limit));
		// }
		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// GridServerHandler gd = new GridServerHandler();
		// gd.setData(resList);
		// gd.setTotalCount(resList.size());
		//		
		// session.flush();
		// session.close();
		return json;
	}

	/**
	 * 描述： 获取所有基础数据
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @return 返回值：String json形式的字符串
	 */
	public String getBaseDicDataByType(String tbName, String type,
			String typeName, String start, String limit,String logId) {
		//Session session =this.getBaseDao().getBaseSession();
		
		
		String hql = " from " + tbName + " obj where obj." + typeName + "='"
				+ type+"'";
		String countSqlString = "select count(*) from " + tbName
				+ " obj where obj." + typeName + "='" + type+"'";
		
		//如果是查询审核人,不显示当前登录人
//		if(tbName.equals("CotEmps")){
//			hql = " from CotEmps obj where obj.shenFlag=1 and id!="+logId;
//			countSqlString = "select count(*) from CotEmps obj where obj.shenFlag=1 and id!="+logId;
//		}
		
		
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		if (start != null && !"".equals(start)) {
			queryInfo.setStartIndex(Integer.parseInt(start));
		}
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		if (limit != null && !"".equals(limit)) {
			queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		}

		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 描述： 获取所有基础数据
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @return 返回值：List
	 */
	public List getBaseDicList(String tbName) {
		List res = this.getBaseDao().getRecords(tbName);
		return res;
	}

	/**
	 * 描述：
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @param key
	 *            该表的外键Id
	 * @param value
	 *            需要关联的值
	 * @return 返回值：String
	 */
	public String getBaseDicDataById(String tbName, String key, String value,
			String start, String limit) {
		// Session session =this.getBaseDao().getBaseSession();
		String hql = "select obj from " + tbName + " obj where obj." + key
				+ "=" + value;
		String countSqlString = "select count(*) from " + tbName
				+ " obj where obj." + key + "=" + value;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	//
	/**
	 * 描述：查询带有子属性的列表，如厂家，分为产品，配件，包材
	 * 
	 * @param tbName
	 *            表名
	 * @param key
	 *            需要获取的key值，对应tbName对象中是属性
	 * @param value
	 *            需要获取的value值，对应tbName对象中是属性
	 * @param queryKey
	 *            需要查询的字段
	 * @param queryVal
	 *            需要关联的值
	 * @return 返回值：Map
	 */
	public Map getBaseDicMapById(String tbName, String key, String value,
			String queryKey, String queryVal) {
		// Session session =this.getBaseDao().getBaseSession();
		String hql = "select obj from " + tbName + " obj where obj." + queryKey
				+ "='" + queryVal+"'";
		Map map = new HashMap();
		try {
			List res = this.getBaseDao().find(hql);
			Class clzz = Class.forName("com.sail.cot.domain." + tbName);
			for (int i = 0; i < res.size(); i++) {
				Object obj = clzz.newInstance();
				obj = res.get(i);
				String keyAttr = BeanUtils.getProperty(obj, key);
				String valueAttr = BeanUtils.getProperty(obj, value);
				map.put(keyAttr, valueAttr);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/**
	 * 描述：
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @param key
	 *            需要查询的字段
	 * @param value
	 *            需要关联的值
	 * @return 返回值：String
	 */
	public String getBaseDicDataByTxt(String tbName, String key, String value,
			String start, String limit) {
		
		// 如果是查询样品.排除子货号
		String str = " where obj." + key + " like '%" + value + "%'";
		if(tbName.equals("CotElementsNew")){
//			str+=" and obj.eleFlag!=2";
		}
		
		String hql = " from " + tbName + " obj";
		String countSqlString = "select count(*) from " + tbName+ " obj";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString+str);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString(str);
		queryInfo.setOrderString(" order by LOCATE('"+value+"',obj."+key+"),length(obj."+key+")");
		// queryInfo.setOrderString(" order by obj.id");
		queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 描述：
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotFactory等
	 * @param key
	 *            需要查询的字段
	 * @param value
	 *            需要关联的值
	 * @return 返回值：String
	 */
	public String getBaseDicDataByFac(String tbName, String key, String value,
			String type, String typeName, String start, String limit,String logId) {
		// Session session =this.getBaseDao().getBaseSession();
		String hql = "select obj from " + tbName + " obj  where obj." + key
				+ " like '%" + value + "%' and obj." + typeName + "=" + type;
		String countSqlString = "select count(*) from " + tbName
				+ " obj where obj." + key + " like '%" + value + "%' and obj."
				+ typeName + "=" + type;
		
		//如果是查询审核人,不显示当前登录人
//		if(tbName.equals("CotEmps")){
//			hql = " from CotEmps obj where obj.shenFlag=1 and id!="+logId+" and obj." + key
//				+ " like '%" + value + "%'";
//			countSqlString = "select count(*) from CotEmps obj where obj.shenFlag=1 and id!="+logId +" and obj." + key
//				+ " like '%" + value + "%'";
//		}
		
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		queryInfo.setOrderString(" order by LOCATE('"+value+"',obj."+key+"),length(obj."+key+")");
		queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 描述：获取所有基础数据
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @param key
	 *            需要获取的key值，对应tbName对象中是属性
	 * @param value
	 *            需要获取的value值，对应tbName对象中是属性
	 * @return 返回值：Map 按ID，value 形式组合的Map 调用：以CotDept为例
	 *         getBaseDicDataMap("CotDept","id" ,"deptName")
	 */
	public Map getBaseDicDataMap(String tbName, String key, String value) {
		List res = this.getBaseDao().getRecords(tbName);
		Map map = new HashMap();
		try {

			Class clzz = Class.forName("com.sail.cot.domain." + tbName);
			for (int i = 0; i < res.size(); i++) {
				Object obj = clzz.newInstance();
				obj = res.get(i);
				String keyAttr = BeanUtils.getProperty(obj, key);
				String valueAttr = BeanUtils.getProperty(obj, value);
				// String keyAttr =
				// key.substring(0,1).toUpperCase()+key.substring(1);
				// String valueAttr =
				// value.substring(0,1).toUpperCase()+value.substring(1);
				// Method method = clzz.getMethod("get"+keyAttr, null);
				// Object[] args = {};
				// keyAttr = String.valueOf( method.invoke(obj, args));
				// method = clzz.getMethod("get"+valueAttr, null);
				// valueAttr = String.valueOf(method.invoke(obj, args));
				map.put(keyAttr, valueAttr);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	// 获得json
	public String getJsonByObjId(String tabName, Integer id) {
		String hql = "from " + tabName + " obj where obj.id=" + id;
		List list = this.getBaseDao().find(hql);
		if (list.size() == 1) {

			JSONObject obj = JSONObject.fromObject(list.get(0));
			return obj.toString();
		}
		return "";
	}

	// 查询对象集合
	public List getObjs(String tbName, String key, String value) {
		List resList = this.getBaseDao().find(
				"select obj from " + tbName + " obj where obj." + key + "="
						+ value);
		return resList;
	}

	/**
	 * 描述：
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @param type
	 *            需要关联的值
	 * @return 返回值：String
	 */
	public String getNameByBoxType(String tbName, String type, String start,
			String limit) {
		String hql = "select obj from " + tbName + " obj where obj.type='"
				+ type + "'";
		String countSqlString = "select count(*) from " + tbName
				+ " obj where obj.type='" + type + "'";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 描述： 获取所有基础数据(包含图片)
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @return 返回值：String json形式的字符串
	 */
	public String getBaseDicDataHavePic(String tbName, String start,
			String limit) {
		String hql = " from " + tbName + " obj";
		String countSqlString = "select count(*) from " + tbName + " obj";
		QueryInfo queryInfo = new QueryInfo();
		if (start != null && !"".equals(start)) {
			queryInfo.setStartIndex(Integer.parseInt(start));
		}
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		if (limit != null && !"".equals(limit)) {
			queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		}
		// String [] filter ={"id","companyLogo"};
		// queryInfo.setExcludes(filter);
		String json = null;
		try {
			List list = this.getBaseDao().findRecords(queryInfo);
			List resList = new ArrayList();
			if ("CotCompany".equals(tbName)) {
				for (int i = 0; i < list.size(); i++) {
					CotCompany company = (CotCompany) list.get(i);
					company.setCompanyLogo(null);
					resList.add(company);
				}
			}
			GridServerHandler gd = new GridServerHandler();
			gd.setData(resList);
			gd.setTotalCount(resList.size());
			json = gd.getLoadResponseText();
		} catch (DAOException e1) {
			e1.printStackTrace();
		}

		return json;
	}

	public String getBaseDicDataByFilter(String tbName, String start,
			String limit, String[] filter) {
		String hql = " from " + tbName + " obj";
		String countSqlString = "select count(*) from " + tbName + " obj";
		QueryInfo queryInfo = new QueryInfo();
		if (start != null && !"".equals(start)) {
			queryInfo.setStartIndex(Integer.parseInt(start));
		}
		queryInfo.setExcludes(filter);
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		if (limit != null && !"".equals(limit)) {
			queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		}

		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	// 查找出货的应付款 点击新增按钮时,过滤出厂家(明细涉及的厂家和协作类型的厂家)
	public String findSpecialFac(String tbname, String key,
			String value, String type, String typeName, String start,
			String limit) {
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		// 生产合同的条数
		String hql = "select distinct obj.factoryId from CotOrderFacdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ type;
		List<?> orderAry = this.getBaseDao().find(hql);
		if(orderAry!=null){
			Iterator<?> it = orderAry.iterator();
			while(it.hasNext()){
				Integer facId = (Integer) it.next();
				if(facId!=null){
					map.put(facId, facId);
				}
			}
		}
		
		// 配件合同的条数
		String hql2 = "select distinct obj.facId from CotFittingsOrderdetail obj,CotOrderOutdetail b"
				+ " where obj.orderDetailId=b.orderDetailId and b.orderId ="
				+ type;
		List<?> orderAry2 = this.getBaseDao().find(hql2);
		if(orderAry2!=null){
			Iterator<?> it = orderAry2.iterator();
			while(it.hasNext()){
				Integer facId = (Integer) it.next();
				if(facId!=null){
					map.put(facId, facId);
				}
			}
		}
		// 包材合同的条数
		String hql3 = "select distinct p.factoryId from CotPackingOrderdetail obj,CotOrderOutdetail b,CotPackingOrder p"
				+ " where obj.orderDetailId=b.orderDetailId and obj.packingOrderId=p.id and b.orderId ="
				+ type;
		List<?> orderAry3 = this.getBaseDao().find(hql3);
		if(orderAry3!=null){
			Iterator<?> it = orderAry3.iterator();
			while(it.hasNext()){
				Integer facId = (Integer) it.next();
				if(facId!=null){
					map.put(facId, facId);
				}
			}
		}
		String tempHql = "select f from CotFactory f";
		String countSqlString = "select count(*) from CotFactory f";
		QueryInfo queryInfo = new QueryInfo();
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(key!=null){
			queryString.append(" and f." + key+ " like '%" + value + "%'");
			queryInfo.setOrderString(" order by LOCATE('"+value+"',f."+key+"),length(f."+key+")");
		}else{
			queryInfo.setOrderString(" order by f.id desc");
		}
		
		String facIds="";
		for (Integer facId : map.keySet()) {
			facIds+=facId+",";
		}
		if(!facIds.equals("")){
			queryString.append(" and (f.factroyTypeidLv1=4 or f.id in("+facIds.substring(0,facIds.length()-1)+"))");
		}else{
			queryString.append(" and f.factroyTypeidLv1=4");
		}
		
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString+queryString);
		queryInfo.setSelectString(tempHql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	//获取条形码单号
	public String getBarcodeNo(){
		Sequece seq =new Sequece(false);
		String barcode=seq.getBarcodeNo();
		seq.saveSeq("barcode");
		return barcode;
	}
	
	public String getBarcodes(String tbName, String key, String value,
			String type, String typeName, String start, String limit,String logId) {
		// Session session =this.getBaseDao().getBaseSession();
		String hql = "select obj from " + tbName + " obj  where obj.barcode is not null and obj.barcode!='' and obj." + key
				+ " like '%" + value + "%' and obj." + typeName + "=" + type;
		String countSqlString = "select count(*) from " + tbName
				+ " obj where obj.barcode is not null and obj.barcode!='' and obj." + key + " like '%" + value + "%' and obj."
				+ typeName + "=" + type;
		
		//如果是查询审核人,不显示当前登录人
//		if(tbName.equals("CotEmps")){
//			hql = " from CotEmps obj where obj.shenFlag=1 and id!="+logId+" and obj." + key
//				+ " like '%" + value + "%'";
//			countSqlString = "select count(*) from CotEmps obj where obj.shenFlag=1 and id!="+logId +" and obj." + key
//				+ " like '%" + value + "%'";
//		}
		
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		queryInfo.setOrderString(" order by LOCATE('"+value+"',obj."+key+"),length(obj."+key+")");
		queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}
	
	public String getBarcodesByType(String tbName, String type,
			String typeName, String start, String limit,String logId) {
		//Session session =this.getBaseDao().getBaseSession();
		
		
		String hql = " from " + tbName + " obj where obj." + typeName + "='"
				+ type+"'";
		String countSqlString = "select count(*) from  " + tbName
				+ " obj where obj." + typeName + "='" + type+"'";
		
		//如果是查询审核人,不显示当前登录人
//		if(tbName.equals("CotEmps")){
//			hql = " from CotEmps obj where obj.shenFlag=1 and id!="+logId;
//			countSqlString = "select count(*) from CotEmps obj where obj.shenFlag=1 and id!="+logId;
//		}
		
		
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		if (start != null && !"".equals(start)) {
			queryInfo.setStartIndex(Integer.parseInt(start));
		}
		queryInfo.setCountQuery(countSqlString);
		queryInfo.setSelectString(hql);
		queryInfo.setQueryString("");
		if (limit != null && !"".equals(limit)) {
			queryInfo.setCountOnEachPage(Integer.parseInt(limit));
		}

		String json = null;
		try {
			json = this.getBaseDao().getJsonData(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return json;
	}


}
