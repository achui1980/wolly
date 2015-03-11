/**
 * 
 */
package com.sail.cot.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗航不锈钢管理系统（QHERP）</p>
 * <p>Description:带权限控制的数据绑定</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Apr 12, 2011 9:55:44 AM </p>
 * <p>Class Name: BasePropedomData.java </p>
 * @author achui
 *
 */
public class BasePropedomData {
	private CotBaseDao baseDao;
	String excludes[] = { "customers", "cotCustContacts",
			"customerVisitedLogs", "customerClaim", "custImg", "picImg",
			"companyLogo","companyYin", "cotPictures", "childs", "cotFile", "cotPriceFacs",
			"cotEleFittings", "cotElePrice","cotElePacking","cotOrderOutdetails","cotOrderOuthsdetails","cotSplitInfos",
			"cotHsInfos","cotShipments","cotSymbols","cotOrderouthsRpt","orderMBImg","cotOrderFacdetails"};

	public CotBaseDao getBaseDao() {
		if (baseDao == null)
			baseDao = (CotBaseDao) SystemUtil.getService("CotBaseDao");
		return baseDao;
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
			String start, String limit,HttpServletRequest request,String validUrl) {
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, validUrl, "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, validUrl, "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nationId in(").append(nationStr).append(")");
				}else{
					queryString.append(" and obj.nationId = 0");
				}
			}
		}
		if("CotFactory".equalsIgnoreCase(tbName)){
			queryString.append(" and obj.factroyTypeidLv1 = 1");
		}
		queryString.append("and obj." + key + " like '%" + value + "%'");
		String sql = "select obj from "+tbName+" obj   ";
		
		//String hql = "select obj from " + tbName + " obj ,CotEmps e";
		//String countSqlString = "select count(*) from " + tbName+ " obj,CotEmps";
		String countSqlString = "select count(*) from "+tbName+" obj ";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString+queryString.toString());
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by LOCATE('"+value+"',obj."+key+"),char_length(obj."+key+")");
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
	 * 描述： 获取所有基础数据
	 * 
	 * @param tbName
	 *            需要进行查询的表的对象，如CotDept，CotCompany等
	 * @return 返回值：String json形式的字符串
	 */
	public String getBaseDicData(String tbName, String start, String limit,HttpServletRequest request,String validUrl) {
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, validUrl, "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, validUrl, "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nationId in(").append(nationStr).append(")");
				}else{
					queryString.append(" and obj.nationId = 0");
				}
			}
		}
		if("CotFactory".equalsIgnoreCase(tbName)){
			queryString.append(" and obj.factroyTypeidLv1 = 1");
		}
		String sql = "select obj from "+tbName+" obj   ";
		
		//String hql = "select obj from " + tbName + " obj ,CotEmps e";
		//String countSqlString = "select count(*) from " + tbName+ " obj,CotEmps";
		String countSqlString = "select count(*) from "+tbName+" obj ";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setExcludes(excludes);
		queryInfo.setStartIndex(Integer.parseInt(start));
		queryInfo.setCountQuery(countSqlString+queryString.toString());
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
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
}
