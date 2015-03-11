/**
 * 
 */
package com.sail.cot.service.system;

import java.util.Map;
import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotLoginInfo;
import com.sail.cot.query.QueryInfo;

public interface CotLoginInfoService {
	
	//添加
	public void saveLoginInfo(CotLoginInfo cotLoginInfo);
	
	//根据IP地址删除
	public void deleteLoginInfos(String ip);
	
	//查询是否存在登录记录
	public CotLoginInfo findIsExistLoginInfo(String ip);
	
	//查询所有员工姓名
	public Map<?, ?> getEmpsNameMap();
	
	//删除所有登陆记录
	public void deleteLoginInfo() throws DAOException;
	
	// 查询在线人数树
	public String getLoginTree(Integer flag) throws DAOException;
}
