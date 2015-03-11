package com.sail.cot.service.system.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotLoginInfo;
import com.sail.cot.service.system.CotLoginInfoService;
import com.sail.cot.util.Log4WebUtil;

public class CotLoginInfoServiceImpl implements CotLoginInfoService {

	private CotBaseDao loginInfoDao;
	private Logger logger = Log4WebUtil.getLogger(CotLoginInfoServiceImpl.class);

	//添加
	public void saveLoginInfo(CotLoginInfo cotLoginInfo){
		try {
			if(cotLoginInfo.getId()!=null){
				this.getLoginInfoDao().update(cotLoginInfo);
			}else{
				this.getLoginInfoDao().create(cotLoginInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新在线登录信息表异常", e);
		}
	}
	
	//根据IP地址删除
	public void deleteLoginInfos(String ip){
//		String hql = "from CotLoginInfo c where c.loginIpaddr='"+ip+"'";
		String hql = "from CotLoginInfo c where c.sessionId='"+ip+"'";
		List<?> list = this.getLoginInfoDao().find(hql);
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			CotLoginInfo cotLoginInfo = (CotLoginInfo) list.get(i);
			ids.add(cotLoginInfo.getId());
		}
		if(ids.size()!=0){
			try {
				this.getLoginInfoDao().deleteRecordByIds(ids, "CotLoginInfo");
			} catch (DAOException e) {
				e.printStackTrace();
				logger.error("删除在线登录信息表异常", e);
			}
		}
	}
	
	//查询是否存在登录记录
	public CotLoginInfo findIsExistLoginInfo(String ip){
		String hql = "from CotLoginInfo c where c.sessionId='"+ip+"'";
		List<?> list = this.getLoginInfoDao().find(hql);
		if(list!=null && list.size()!=0){
			return (CotLoginInfo) list.get(0);
		}else{
			return null;
		}
	}
	
	//查询所有员工姓名
	public Map<?, ?> getEmpsNameMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getLoginInfoDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps)list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	public CotBaseDao getLoginInfoDao() {
		return loginInfoDao;
	}

	public void setLoginInfoDao(CotBaseDao loginInfoDao) {
		this.loginInfoDao = loginInfoDao;
	}
	
	// 查询在线人数树
	public String getLoginTree(Integer flag) throws DAOException {
		StringBuffer returnStr = new StringBuffer();
		List list = this.getLoginInfoDao().find("from CotLoginInfo obj");
		Map<String,List> map = new HashMap<String,List>();
		for (int i = 0; i < list.size(); i++) {
			CotLoginInfo login = (CotLoginInfo) list.get(i);
			//按照用户名--ip显示
			if(flag==0){
				List listChild=null;
				if(map.get(login.getLoginName())==null){
					listChild = new ArrayList();
				}else{
					listChild =map.get(login.getLoginName());
				}
				listChild.add(login);
				map.put(login.getLoginName(), listChild);
			}
			//按照ip--用户名显示
			if(flag==1){
				List listChild=null;
				if(map.get(login.getLoginIpaddr())==null){
					listChild = new ArrayList();
				}else{
					listChild =map.get(login.getLoginIpaddr());
				}
				listChild.add(login);
				map.put(login.getLoginIpaddr(), listChild);
			}
		}
		return this.modifyStr(this.recursionFn(map,flag)); 
	}
	//递归运算
	@SuppressWarnings("unchecked")
	public String recursionFn(Map<String,List> map,Integer flag) {
		StringBuffer returnStr = new StringBuffer();
		int num=0;
		Iterator<?> it = map.keySet().iterator();
		while(it.hasNext()){
			num++;
			String key = (String) it.next();
			List listChild =map.get(key);
			for (int i = 0; i < listChild.size(); i++) {
				CotLoginInfo login = (CotLoginInfo) listChild.get(i);
				if(i==0){
					returnStr.append("{id:'");
					returnStr.append("aaa"+num);
					returnStr.append("',empId:");
					if(flag==0){
						returnStr.append(login.getLoginEmpid());
					}else{
						returnStr.append(-1);
					}
					returnStr.append(",text:'");
					returnStr.append(key);
					returnStr.append("',parentId:'root_0'");
					returnStr.append(",sessionId:'");
					returnStr.append("");
					returnStr.append("',leaf:false");
					returnStr.append(",children:[");
				}
				returnStr.append("{id:");
				returnStr.append(login.getId());
				returnStr.append(",empId:'");
				returnStr.append(login.getLoginEmpid());
				returnStr.append("',text:'");
				if(flag==0){
					returnStr.append(login.getLoginIpaddr());
				}else{
					returnStr.append(login.getLoginName());
				}
				returnStr.append("',parentId:'aaa"+num);
				returnStr.append("',sessionId:'");
				returnStr.append(login.getSessionId());
				returnStr.append("',leaf:true},");
			}
			returnStr.append("]},");
		}
		return returnStr.toString();
	}

	//修饰一下才能满足Extjs的Json格式 
	public String modifyStr(String returnStr) {
		return ("[" + returnStr + "]").replaceAll(",]", "]");

	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotLoginInfoService#deleteLoginInfo()
	 */
	public void deleteLoginInfo() throws DAOException {
		List<CotLoginInfo> res = this.getLoginInfoDao().getRecords("CotLoginInfo");
		List ids = new ArrayList();
		for(CotLoginInfo loginInfo : res)
		{
			ids.add(loginInfo.getId());
		}
		if(ids.size() > 0)
		{
			this.getLoginInfoDao().deleteRecordByIds(ids, "CotLoginInfo");
		}
	}
	
}
