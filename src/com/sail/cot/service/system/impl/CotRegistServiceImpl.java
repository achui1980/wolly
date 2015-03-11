/**
 * 
 */
package com.sail.cot.service.system.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;
import org.eclipse.jdt.internal.compiler.impl.CharConstant;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotServerInfo;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotRegistService;
import com.sail.cot.util.KiCrypt;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemInfoUtil;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Sep 28, 2008 5:55:18 PM </p>
 * <p>Class Name: CotRegistServiceImpl.java </p>
 * @author achui
 *
 */
public class CotRegistServiceImpl implements CotRegistService {

	private CotBaseDao registDao;
	
	public CotBaseDao getRegistDao() {
		return registDao;
	}

	public void setRegistDao(CotBaseDao registDao) {
		this.registDao = registDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotRegistServiceImpl.class);
	 
	public void addRegistInfo(List<CotServerInfo> registList) {
		for(int i=0; i<registList.size(); i++)
		{
			CotServerInfo serverInfo = (CotServerInfo)registList.get(i);
			String rand = RandomStringUtils.randomNumeric(7) + serverInfo.getIsStangAlone() + RandomStringUtils.randomNumeric(8);
			CotServerInfo serverInfo1 = this.getCotServerInfo(serverInfo.getMechineKey());
			if(serverInfo1 != null)
				serverInfo.setId(serverInfo1.getId());
			serverInfo.setIsStangAlone(rand);
			registList.set(i, serverInfo);
		}
		this.getRegistDao().saveOrUpdateRecords(registList);
	}

	public int deleteRegistInfo(List<CotServerInfo> registList) {
		 
		return 0;
	}

	public void modifyRegistInfo(List<CotServerInfo> registList) {
		 
		try {
			this.getRegistDao().updateRecords(registList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改注册失败", e);
		}
		
	}
	 
	public List<?> getRegistList() {
		 
		return this.getRegistDao().getRecords("CotServerInfo");
	}

	 
	 
	//是否存在该主机MAC地址（注册序列号）相关注册记录
	public boolean isExistMesg() {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		String mechineKey = "001CBF5F8A69";
		try
		{
			mechineKey = this.getMechineKey();
		}
		catch(Exception ex){}
		 
		queryInfo.setCountQuery("select count(*) from CotServerInfo obj where obj.mechineKey='"+mechineKey+"'");
		try {
			int count = this.getRegistDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 
			logger.error("查找记录失败", e);
		}
		return isExist;
	}
	
	//输入注册码是否与数据库中注册码相同
	public boolean isSameRegeditKey() {
		boolean isSame = false;
		QueryInfo queryInfo = new QueryInfo();
		String mechineKey = this.getMechineKey();
		CotServerInfo cotServerInfo = this.getCotServerInfo(mechineKey);
		String softVer = cotServerInfo.getSoftVer();
		boolean isExistMecKey = this.isExistMechineKey(mechineKey);
		if(isExistMecKey){
			String serverNo = null;
			String regeditKey = null;
			if("0".equals(cotServerInfo.getIsAlone()))
			{
				serverNo = this.getServerNo(mechineKey);
				serverNo = serverNo.substring(0, 6);
				regeditKey = this.getRegeditKey(mechineKey, softVer ,serverNo);
			}
			else if("1".equals(cotServerInfo.getIsAlone())){
				regeditKey = this.getDemoRegeditKey(cotServerInfo, null, null, null);
			}
			
			queryInfo.setCountQuery("select count(*) from CotServerInfo obj where obj.regeditKey='"+regeditKey+"'");
			try {
				int count = this.getRegistDao().getRecordsCount(queryInfo);
				if(count > 0)
					isSame =  true;
			} catch (DAOException e) {
				 
				logger.error("查找重复方法失败", e);
			}
		}
		return isSame;
	}
	
	//数据库中是否存在该主机MAC地址记录（注册序列号）
	public boolean isExistMechineKey(String mechineKey){
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false; 
		queryInfo.setCountQuery("select count(*) from CotServerInfo obj where obj.mechineKey='"+mechineKey+"' ");
		try {
			int count = this.getRegistDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 
			logger.error("查找记录失败", e);
		}
		return isExist;
	}
    
	//获取本机MAC地址（注册序列号）
	public String getMechineKey() {
		SystemInfoUtil systemInfoUtil = new SystemInfoUtil();
		String mKey = "";
		//获取磁盘物理序列号
		mKey = systemInfoUtil.getVolumSeri();
		String mechineKey = mKey.substring(4, mKey.length());
		return mechineKey;
	}
	
	//获取数据库中加密的serverNo并进行解密
	public String getServerNo(String mechineKey) {
		List<?> list = this.getRegistDao().find("select obj.serverNo from CotServerInfo obj where obj.mechineKey='"+mechineKey+"' order by obj.isStangAlone desc");
		String serverNo = (String) list.get(0);
		//解密
		String strSource = serverNo;
		String strKey = KiCrypt.KEY_STRING;;
		KiCrypt crypt = new KiCrypt();
		try
		{
			serverNo = crypt.KiDecrypt(strKey, strSource);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return serverNo;
	}
	
	//十六进制的serverNo转化为十进制（Decimal）
	public String getDecimalServerNo(String HexServerNo) {
		String HexServerNo8 = HexServerNo.substring(0,6);
		String decimalServerNo = Integer.valueOf(HexServerNo8,16).toString();
		 
		return decimalServerNo;
	}
	
	//获取加密的serverNo
	public String getKiEncryptServerNo(String serverNo) {
		int aa = 6 - serverNo.length();
		String sNo = "";
		for (int i = 0; i < aa; i++) {
			sNo = sNo+"0";
		}
		sNo = sNo+serverNo;
		//加密
		String ski = sNo;
		String skiKey = KiCrypt.KEY_STRING;;
		KiCrypt crypt = new KiCrypt();
		try
		{
			serverNo = crypt.KiEncrypt(skiKey, ski);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return serverNo;
	}
	
	//获取组装并且加密的注册码
	public String getRegeditKey(String mechineKey, String softVer,String serverNo) {
		String softVerNo = null;
		if (softVer == null) 
			softVer = "sample";
		if(softVer.equals("sample")){
			softVerNo = "1A";
		}
		if(softVer.equals("price")){
			softVerNo = "2B";
		}
		if(softVer.equals("trade")){
			softVerNo = "3C";
		}
		if(softVer.equals("trade_f")){
			softVerNo = "4D";
		}
		if(softVer.equals("email")){
			softVerNo = "5E";
		}
		String regeditKey = null;
		//加密
		String ski = mechineKey+softVerNo+serverNo;
		String skiKey = KiCrypt.KEY_STRING;;
		KiCrypt crypt = new KiCrypt();
		try
		{
			regeditKey = crypt.KiEncrypt(skiKey, ski);
			//System.out.println("getRegeditKey(注册码)======================="+regeditKey);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return regeditKey;
	}
	
	//将未加密的注册码进行加密
	public String getKiEncryptRegeditKey(String regeditKey) {
		//加密
		String ski = regeditKey;
		String skiKey = KiCrypt.KEY_STRING;;
		KiCrypt crypt = new KiCrypt();
		try
		{
			regeditKey = crypt.KiEncrypt(skiKey, ski);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return regeditKey;
	}

	//将注册码进行解密，并获取软件版本
	public String getSoftVerByRegeditKey(String regeditKey) {
		String softVer = null;
		//System.out.println("------------------------------------------------------------------------1="+regeditKey);
		//解密
		String strSource = regeditKey;
		String strKey = KiCrypt.KEY_STRING;;
		KiCrypt crypt = new KiCrypt();
		try
		{
			regeditKey = crypt.KiDecrypt(strKey, strSource);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		//System.out.println("------------------------------------------------------------------------2="+regeditKey);
		String softVerNo = regeditKey.substring(8, 10);
		System.out.println("softVerNo:"+softVerNo);
		if(softVerNo.equals("1A")){
			softVer = "sample";
		}
		else if(softVerNo.equals("2B")){
			softVer = "price";
		}
		else if(softVerNo.equals("3C")){
			softVer = "trade";
		}
		else if(softVerNo.equals("4D")){
			softVer = "trade_f";
		}
		//邮件
		else if(softVerNo.equals("5E")){
			softVer = "email";
		}
		return softVer;
	}

	
	//获取十六进制的serverNo（6位数）
	public String getHexServerNo(String serverNo) {
		String hexNo = Long.toHexString(Long.parseLong(serverNo));
		int aa = 6 - hexNo.length();
		String sNo = "";
		for (int i = 0; i < aa; i++) {
			sNo = sNo+"0";
		}
		sNo = sNo+hexNo;
		return sNo;
	}

	//获取当时间
	public Timestamp getTime() {
		Timestamp currDate = new Timestamp(System.currentTimeMillis());
		return currDate;
	}

	public CotServerInfo getIdByMechineKey(String mechineKey) {
		List<?> list = this.getRegistDao().find("select obj from CotServerInfo obj where obj.mechineKey='"+mechineKey+"' order by obj.isStangAlone desc");
		if(list.size() > 0)
		{
			CotServerInfo cotServerInfo = (CotServerInfo) list.get(0);
			return cotServerInfo;
		}
		else
			return null;
	}

	 
	public CotServerInfo getCotServerInfoFromSession() {
		HttpSession session = WebContextFactory.get().getSession();
		CotServerInfo cotServerInfo = (CotServerInfo) session.getAttribute("CotServerInfo");
		return cotServerInfo;
	}
	
	//通过mechineKey获取CotServerInfo对象
	public CotServerInfo getCotServerInfo(String mechineKey) {
		List<?> list = this.getRegistDao().find("from CotServerInfo obj where obj.mechineKey='"+mechineKey+"' order by obj.isStangAlone desc");
		if(list.size()>0){
			return (CotServerInfo) list.get(0);
		}
		return null;
	}

	

	//加密
	public String getKiEncrypt(String str){
		//加密
		String ski = str;
		String skiKey = KiCrypt.KEY_STRING;;
		KiCrypt crypt = new KiCrypt();
		try
		{
			String res = crypt.KiEncrypt(skiKey, ski);
			return res;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//解密
	public String getKiDecrypt(String str){
		//解密
		String strSource = str;
		String strKey = KiCrypt.KEY_STRING;;
		KiCrypt crypt = new KiCrypt();
		try
		{
			String res = crypt.KiDecrypt(strKey, strSource);
			return res;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//注册试用版本
	public void saveDemoRegedit(CotServerInfo serverInfo,int serverno ,int month,int useTime){
		//加密serverNo:月份(1位)+使用次数（4位）+端数（1位）
		//格式化月份
		String monthString = Integer.toHexString(month);
		//格式化使用次数
		String usedTime = String.format("%1$04X", useTime);
		//格式化端数
		String serverNo = String.format("%1$01X",serverno);
		//是否是临时版本1：临时版本，0：正式版本
		Integer isStangAlone = 1;
		//加密版本类型 7为随机+类型+8位随机
		String strIsStangAlone = RandomStringUtils.randomAlphanumeric(7)+isStangAlone+RandomStringUtils.randomAlphanumeric(8);
		String softVer = serverInfo.getSoftVer();
		String softVerNo = null;
		String mechineKey = serverInfo.getMechineKey();
		if(softVer.equals("sample")){
			softVerNo = "1A";
		}
		if(softVer.equals("price")){
			softVerNo = "2B";
		}
		if(softVer.equals("trade")){
			softVerNo = "3C";
		}
		if(softVer.equals("trade_f")){
			softVerNo = "4D";
		}
		if(softVer.equals("email")){
			softVerNo = "5E";
		}
		String regeditKey = serverInfo.getMechineKey()+softVerNo+monthString+usedTime+serverNo;
		//加密注册码
		regeditKey = this.getKiEncrypt(regeditKey);
		String filepath = "C:/WINDOWS/system32/jni.dll";
		File file = new File(filepath);
		//获取13位随机数
		String random = "";
		random = RandomStringUtils.randomNumeric(12);
		//获取系统当前时间
		java.util.Calendar cal = java.util.Calendar.getInstance();
		Date addTime = cal.getTime();
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String addTimeString = "00"+sdf.format(addTime);
		//加密addTimeString
		addTimeString = this.getKiEncrypt(addTimeString);
		
		//获取试用结束时间
		cal.add(java.util.Calendar.MONTH, month);
		Date endTime = cal.getTime();
		java.text.SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String endTimeString = "00"+sdf2.format(endTime);
		//加密endTimeString
		endTimeString = this.getKiEncrypt(endTimeString);
		 
		
		//对已使用次数加密，写入文件
        String usedTotalCount = random + usedTime;
        //加密usedTotalCount
        usedTotalCount = this.getKiEncrypt(usedTotalCount);
		//文件不存在
		if (!file.exists()){
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					bw.write("addTime=" + "\r\n" 
							+ "lastTime=" + "\r\n" 
							+ "endTime=" + "\r\n" 
							+ "month=" + "\r\n" 
							+ "usedTime=" + "\r\n" 
							+ "userTime=");
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				serverInfo.setRegeditKey(regeditKey);
				serverInfo.setServerNo(this.getKiEncrypt(String.format("%1$06X",serverno)+String.format("%1$010X",0)));
				serverInfo.setIsStangAlone(strIsStangAlone);
				serverInfo.setAddTime(addTime);
				serverInfo.setUserTime(usedTotalCount);
				this.getRegistDao().create(serverInfo);
				
			
				//加密已使用次数，默认0
				Map<String, String> demoMap = new HashMap<String, String>();
				demoMap.put("addTime", addTimeString);
				demoMap.put("lastTime", addTimeString);
				demoMap.put("month", this.getKiEncrypt(random+String.format("%1$04X",month)));
				demoMap.put("endTime", endTimeString);
				demoMap.put("usedTime", this.getKiEncrypt(random+"0000"));//已使用次数
				demoMap.put("userTime", usedTotalCount);
				SystemUtil.setPropertyFile(demoMap, filepath);
			
		}else{
			//判断mechineKey是否有记录
			serverInfo = this.getCotServerInfo(serverInfo.getMechineKey());
			if(serverInfo==null){
				serverInfo = new CotServerInfo();
				serverInfo.setMechineKey(mechineKey);
				serverInfo.setAddTime(new Date(System.currentTimeMillis()));
			}
			
			//从jni获取试用次数
				
			serverInfo.setUserTime(usedTotalCount);
			serverInfo.setMechineKey(serverInfo.getMechineKey());
			serverInfo.setRegeditKey(regeditKey);
			serverInfo.setServerNo(this.getKiEncrypt(String.format("%1$06X",serverno)+String.format("%1$010X",0)));
			serverInfo.setIsStangAlone(strIsStangAlone);
			serverInfo.setSoftVer(softVer);
			List<CotServerInfo> records = new ArrayList<CotServerInfo>();
			records.add(serverInfo);
			this.getRegistDao().saveOrUpdateRecords(records);
			
			Map<String, String> demoMap = new HashMap<String, String>();
	

			demoMap.put("month", this.getKiEncrypt(random+String.format("%1$04X",month)));
			demoMap.put("endTime", endTimeString);
			demoMap.put("usedTime", this.getKiEncrypt(random+"0000"));//已使用次数
			demoMap.put("userTime", usedTotalCount);
			SystemUtil.setPropertyFile(demoMap, filepath);
		}
	}
	
	//更新试用版本注册信息
	public boolean updateDemoRegedit(){
		boolean res = true;
		String filepath = "C:/WINDOWS/system32/jni.dll";
		String key1 = "userTime";//总次数
		String key2 = "usedTime";//已使用次数
			Map<String, String> demoMap = new HashMap<String, String>();
			//从数据库获取试用次数
			String str1 = SystemUtil.getProperty(filepath, key1);
			//从jni获取试用次数
			String str2 = SystemUtil.getProperty(filepath, key2);
			//解密
			String userTime = this.getKiDecrypt(str1).substring(12, 16); 
			String usedTime = this.getKiDecrypt(str2).substring(12, 16);
			//转化为Integer类型
			Integer uTime1 = Integer.parseInt(userTime,16);//使用总次数
			Integer uTime2 = Integer.parseInt(usedTime,16);//已使用次数
			String random = RandomStringUtils.randomNumeric(12);
			if(uTime1 > uTime2){
				uTime2 = uTime2 + 1; //使用次数+1
				String uTime = random + String.format("%1$04X", uTime2);
				//加密
				uTime = this.getKiEncrypt(uTime);
				demoMap.put("usedTime", uTime);
			}
			else {
				res = false;
			}
			SystemUtil.setPropertyFile(demoMap, filepath);
		
		return res;
	}
	
	//试用权限检查
	public int loginCheck(){
		
		String filepath = "C:/WINDOWS/system32/jni.dll";
		File file = new File(filepath);
		if(!file.exists())
		{
			return 0;
		}	
		//从jni获取最后登陆时间
		String lastTime = SystemUtil.getProperty(filepath, "lastTime");
		String lastTimeString = this.getKiDecrypt(lastTime).substring(2, 16); 
		Long lastTimeLong = Long.parseLong(lastTimeString);
		
		//从jni获取试用到期时间
		String endTime = SystemUtil.getProperty(filepath, "endTime");
		String endTimeString = this.getKiDecrypt(endTime).substring(2, 16); 
		Long endTimeLong = Long.parseLong(endTimeString);
		
		//获取系统当前时间
		java.util.Calendar cal = java.util.Calendar.getInstance();
		Date nowTime = cal.getTime();
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTimeString = sdf.format(nowTime);
		Long nowTimeLong = Long.parseLong(nowTimeString);
		
		if(nowTimeLong > endTimeLong){
			return 1;   //试用到期
		}else if(lastTimeLong > nowTimeLong){
			return 2;   //系统时间被修改
		}else{
			//修改最后登陆时间
			Map<String, String> demoMap = new HashMap<String, String>();
			nowTimeString = "00" + nowTimeString;
			String newLastTime = this.getKiEncrypt(nowTimeString);
			demoMap.put("lastTime", newLastTime);
			SystemUtil.setPropertyFile(demoMap, filepath);
			
			//更新试用版本注册信息
			boolean res = this.updateDemoRegedit();
			if(res){
				return 4;   //正常登陆
			}else{
				return 3;   //试用次数已用完
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRegistService#getDemoRegeditKey()
	 */
	public String getDemoRegeditKey(CotServerInfo serverInfo,Integer month,Integer userCount,Integer serverNo) {
		String softVerNo = null;
		String softVer = serverInfo.getSoftVer();
		if (softVer == null) 
			softVer = "sample";
		if(softVer.equals("sample")){
			softVerNo = "1A";
		}
		if(softVer.equals("price")){
			softVerNo = "2B";
		}
		if(softVer.equals("trade")){
			softVerNo = "3C";
		}
		if(softVer.equals("trade_f")){
			softVerNo = "4D";
		}
		if(softVer.equals("email")){
			softVerNo = "5E";
		}
		String filepath = "C:/WINDOWS/system32/jni.dll";
		String userTime = null;
		String monthString = null;
		String serverno = null;
		if(month != null)
			monthString = String.format("%1$016X", month);
		else 
			 monthString = this.getKiDecrypt(SystemUtil.getProperty(filepath, "month"));
		if(userCount != null)
			userTime = String.format("%1$016X", userCount);
		else {
			userTime = this.getKiDecrypt(SystemUtil.getProperty(filepath, "userTime"));
		}
		if(serverNo != null)
		{
			serverno = Integer.toHexString(serverNo.intValue());
		}
		else
		{
			serverno = this.getKiDecrypt(serverInfo.getServerNo());
			serverno = serverno.substring(5, 6);
		}
		userTime = userTime.substring(12,16);
		monthString = monthString.substring(15,16);
		
		//加密serverNo:月份(1位)+使用次数（4位）+端数（1位）
		//格式化月份
		String keyString = serverInfo.getMechineKey()+softVerNo+monthString+userTime+serverno;
		keyString = this.getKiEncrypt(keyString);	
		return keyString;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRegistService#getMachineKey(java.lang.String)
	 */
	public Map getMachineKey(String mechineKey) {
		CotServerInfo serverInfo = this.getCotServerInfo(mechineKey);
		String regKey = serverInfo.getRegeditKey();
		regKey = this.getKiDecrypt(regKey);
		int month = 0,totalCount=0,serverNo = 0;
		if("1".equals(serverInfo.getIsAlone()) ) //临时注册
		{
			 month = Integer.parseInt(regKey.substring(10,11),16);//转10进制
			 totalCount = Integer.parseInt(regKey.substring(11,15),16);//转10进制
			 serverNo = Integer.parseInt(regKey.substring(15,16),16);//转10进制
		}
		else if("0".equals(serverInfo.getIsAlone())){
			serverNo = Integer.parseInt(regKey.substring(10,16),16);//转10进制
		}
		Map res = new HashMap();
		res.put("serverNo", serverNo);
		res.put("month", month);
		res.put("userTime", totalCount);
		res.put("isStangAlone", serverInfo.getIsAlone());
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotRegistService#getAttachModuleByRegeditedKey(java.lang.String)
	 */
	public String getAttachModuleByRegeditedKey(String regeditedKey) {
		//解密，附加模块注册形式 8位机器码+8为的模块编码，模块编码冲1开始，1：邮件，最多支持8个模块，同时拥有模块这并列显示
		//如：1:邮件，2:库存，那么“12”表示同时拥有邮件和库存模块
		//如果位都为0，则表示没有附加模块
		if(regeditedKey == null || regeditedKey.equals(""))
			return null;
		if(regeditedKey.length() != 16) return null;
		String key = this.getKiDecrypt(regeditedKey);
		String module = key.substring(8);
		//过滤掉所有的0,
		module = StringUtils.remove(module, "0");
		if(StringUtils.isEmpty(module)){
			return null;
		}
		char[] chars = module.toCharArray();
		List res = new ArrayList();
		for(char c : chars){
			res.add(String.valueOf(c));
		}
		module = StringUtils.join(res,"|");
		return module;
	}
}
