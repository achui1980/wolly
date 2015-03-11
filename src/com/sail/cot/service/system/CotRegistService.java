
package com.sail.cot.service.system;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.sail.cot.domain.CotServerInfo;

 
public interface CotRegistService {
	
	//获取注册信息列表
	List<?> getRegistList();
	
	//添加注册信息
	void addRegistInfo(List<CotServerInfo> registList);
	
	//修改注册信息
	void modifyRegistInfo(List<CotServerInfo> registList);
	
	//删除注册信息
	int deleteRegistInfo(List<CotServerInfo> registList);
	
	//是否存在注册信息
    boolean isExistMesg();
    
    //是否相同RegeditKey
	boolean isSameRegeditKey();
	
	String getRegeditKey(String mechineKey, String softVer, String serverNo);
	
	String getMechineKey();
	
	String getServerNo(String mechineKey);
	
	//十六进制的serverNo转化为十进制（Decimal）
	public String getDecimalServerNo(String HexServerNo) ;
	
	String getKiEncryptServerNo(String serverNo);
	
	String getKiEncryptRegeditKey(String regeditKey);
	
	//将注册码进行解密，并获取软件版本
	public String getSoftVerByRegeditKey(String regeditKey);
	
	String getHexServerNo(String serverNo);
	
	Timestamp getTime();
	
	CotServerInfo getIdByMechineKey(String mechineKey);
	
	public CotServerInfo getCotServerInfoFromSession();
	
	//通过mechineKey获取CotServerInfo对象
	public CotServerInfo getCotServerInfo(String mechineKey);
	
	//加密
	public String getKiEncrypt(String str);
	
	//解密
	public String getKiDecrypt(String str);
	
	//注册试用版本
	public void saveDemoRegedit(CotServerInfo serverInfo,int serverno,int month,int time);
	
	//更新试用版本注册信息
	public boolean updateDemoRegedit();
	
	//试用权限检查
	public int loginCheck();
	
	//生成临时模块的注册码
	public String getDemoRegeditKey(CotServerInfo serverInfo,Integer month,Integer userCount,Integer serverNo);
	
	//获取注册码
	public Map getMachineKey(String mechineKey);
	
	public String getAttachModuleByRegeditedKey(String regeditedKey);
	
}
