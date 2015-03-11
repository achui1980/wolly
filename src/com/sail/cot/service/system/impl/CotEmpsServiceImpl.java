/**
 * 
 */
package com.sail.cot.service.system.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.system.CotEmpsDao;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotDept;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotEtOtp;
import com.sail.cot.domain.CotLoginInfo;
import com.sail.cot.domain.CotMailTree;
import com.sail.cot.domain.CotRole;
import com.sail.cot.domain.CotServerInfo;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotEmpsService;
import com.sail.cot.service.system.CotRegistService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.servlet.DownImportResultServlet;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.KiCrypt;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemInfoUtil;

import ft.otp.verify.OTPVerify;

/**
 * <p>
 * Title: 工艺品管理系统
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Jul 31, 2008 5:45:09 PM
 * </p>
 * <p>
 * Class Name: CotSystemMenuServiceImpl.java
 * </p>
 * 
 * @author achui
 * 
 */
public class CotEmpsServiceImpl implements CotEmpsService {

	private CotEmpsDao empsDao;
	private CotRegistService registService;
	private Logger logger = Log4WebUtil.getLogger(CotEmpsServiceImpl.class);

	// 操作日志
	CotSysLogService sysLogService;

	public void setSysLogService(CotSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}
	
	//判断该动态令牌是否被别的员工使用了
	public boolean checkIsUseOtp(Integer empId,Integer otpId){
		if(otpId==null){
			return false;
		}
		String hql="select obj.id from CotEmps obj where obj.etId="+otpId;
		List list = this.getEmpsDao().find(hql);
		if(list!=null && list.size()>0){
			Object id = list.get(0);
			if(id!=null){
				Integer tempId=(Integer) id;
				if(empId!=null){
					if(tempId.intValue()!=empId.intValue()){
						return true;
					}
				}else{
					return true;
				}
			}
		}
		return false;
	}

	public int addEmps(List<CotEmps> empsList) {
		CotEmps emps = (CotEmps) empsList.get(0);
		boolean flag=this.checkIsUseOtp(emps.getId(), emps.getEtId());
		if(flag==true){
			return 2;
		}
		
		for (int i = 0; i < empsList.size(); i++) {
			CotEmps cotEmps = (CotEmps) empsList.get(i);
			// 设置员工的默认密码为“123456”
			cotEmps.setEmpsPwd("123456");
			// cotEmps.setEmpsMailPwd(PasswordEncrypt.encrypt(cotEmps.getEmpsMailPwd()));
			// Integer id =
			// this.getEmpsDao().isExistEmpsId(cotEmps.getEmpsId());
			// if(id!=null){
			// return false;
			// }
			String classPath = DownImportResultServlet.class.getResource("/").toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			String path = systemPath + "mailfolder/" + cotEmps.getEmpsId();
			// 如果文件夹不存在 则建立新文件夹
			File a = new File(path);
			if (!a.exists())
				a.mkdirs();

		}
		
		try {
			this.getEmpsDao().saveRecords(empsList);
			// 生成邮件树节点
//			for (int i = 0; i < empsList.size(); i++) {
//				CotEmps cotEmps = (CotEmps) empsList.get(i);
//				// 生成父节点
//				CotMailTree mailTree = new CotMailTree();
//				mailTree.setEmpId(cotEmps.getId());
//				mailTree.setNodeName(cotEmps.getEmpsName());
//				mailTree.setParentId(1);
//				mailTree.setUpdateFlag("n");
//				List temp = new ArrayList();
//				temp.add(mailTree);
//				this.getEmpsDao().saveRecords(temp);
//				// 生成子节点
//				List tempChild = new ArrayList();
//				// 收件箱
//				CotMailTree child = new CotMailTree();
//				child.setEmpId(cotEmps.getId());
//				child.setNodeName("收件箱");
//				child.setParentId(mailTree.getId());
//				child.setCls("folder_database");
//				child.setFlag("R");
//				child.setUpdateFlag("n");
//				tempChild.add(child);
//				// 已发送
//				CotMailTree childS = new CotMailTree();
//				childS.setEmpId(cotEmps.getId());
//				childS.setNodeName("已发送");
//				childS.setParentId(mailTree.getId());
//				childS.setCls("folder_go");
//				childS.setFlag("S");
//				childS.setUpdateFlag("n");
//				tempChild.add(childS);
//				// 草稿箱
//				CotMailTree childC = new CotMailTree();
//				childC.setEmpId(cotEmps.getId());
//				childC.setNodeName("草稿箱");
//				childC.setParentId(mailTree.getId());
//				childC.setCls("folder_page");
//				childC.setFlag("C");
//				childC.setUpdateFlag("n");
//				tempChild.add(childC);
//				// 废件箱
//				CotMailTree childF = new CotMailTree();
//				childF.setEmpId(cotEmps.getId());
//				childF.setNodeName("废件箱");
//				childF.setParentId(mailTree.getId());
//				childF.setCls("folder_delete");
//				childF.setFlag("D");
//				childF.setUpdateFlag("n");
//				tempChild.add(childF);
//				// 待发送
//				CotMailTree childP = new CotMailTree();
//				childP.setEmpId(cotEmps.getId());
//				childP.setNodeName("待发送");
//				childP.setParentId(mailTree.getId());
//				childP.setCls("folder_pause");
//				childP.setFlag("P");
//				childP.setUpdateFlag("n");
//				tempChild.add(childP);
//				this.getEmpsDao().saveRecords(tempChild);
//
//			}
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加员工异常", e);
			return 1;
		}
		return 0;
	}

	public Boolean deleteEmps(Integer empsId) throws DAOException {
		try {
			CotEmps cotEmps = this.getEmpsById(empsId);
			String name=cotEmps.getEmpsId();
			this.getEmpsDao().deleteRecordById(empsId, "CotEmps");
			
			String classPath = DownImportResultServlet.class.getResource("/").toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			String path = systemPath + "mailfolder/" + name;
			File a = new File(path);
			a.delete();

		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除员工异常", e);
			throw e;
		}
		return true;
	}

	public CotEmps getEmpsById(Integer id) {
		Object object = this.getEmpsDao().getById(CotEmps.class, id);
		if (object != null) {
			CotEmps cotEmps = (CotEmps) object;
			cotEmps.setCustomers(null);
			// cotEmps.setEmpsMailPwd(PasswordEncrypt.decrypt(cotEmps.getEmpsMailPwd()));
			return cotEmps;
		} else {
			return null;
		}
	}

	public List<?> getDeptListByCompanyId(Integer companyId) {
		return this.getEmpsDao().queryDeptListByCompanyId(companyId);
	}

	public List<?> getRoleList() {
		return this.getEmpsDao().find("from CotRole c where c.roleStatus=1");
	}

	public int modifyEmps(List<?> empsList) {
		try {
			CotEmps emps = (CotEmps) empsList.get(0);
			boolean flag=this.checkIsUseOtp(emps.getId(), emps.getEtId());
			if(flag==true){
				return 2;
			}
			
			for (int i = 0; i < empsList.size(); i++) {
				CotEmps cotEmps = (CotEmps) empsList.get(i);
				// cotEmps.setEmpsMailPwd(PasswordEncrypt.encrypt(cotEmps.getEmpsMailPwd()));
//				// 更新该员工的邮件树节点
//				String hql = "update CotMailTree obj set obj.nodeName=:nodeName where obj.parentId=1 and obj.empId=:empId";
//				Map map = new HashMap();
//				map.put("nodeName", cotEmps.getEmpsName());
//				map.put("empId", cotEmps.getId());
//				QueryInfo queryInfo = new QueryInfo();
//				queryInfo.setSelectString(hql);
//				this.getEmpsDao().executeUpdate(queryInfo, map);
			}
			this.getEmpsDao().updateRecords(empsList);
		} catch (Exception e) {
			logger.error("更新员工信息异常", e);
			return 1;
		}
		return 0;
	}

	public Map<?, ?> getDeptNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getEmpsDao().getRecords("CotDept");
		for (int i = 0; i < list.size(); i++) {
			CotDept cotDept = (CotDept) list.get(i);
			map.put(cotDept.getId().toString(), cotDept.getDeptName());
		}
		return map;
	}

	public Map<?, ?> getRoleNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getEmpsDao().getRecords("CotRole");
		for (int i = 0; i < list.size(); i++) {
			CotRole cotRole = (CotRole) list.get(i);
			map.put(cotRole.getId().toString(), cotRole.getRoleName());
		}
		return map;
	}

	public Map<?, ?> getCompanyNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getEmpsDao().getRecords("CotCompany");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany.getCompanyShortName());
		}
		return map;
	}

	public Map<?, ?> getEmpsStatusMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "在职");
		map.put("1", "离职");
		return map;
	}

	// 登录验证(0代表工号不存在,1代表密码不正确,3代表已经达到最大登录人数,4，5代表还没注册.2代表成功,6：员工状态为离职,7:临时试用登录（走试用流程）,8:该员工需要命令牌,但是没输入,9:输入了错误的命令牌,10:一个命令牌重复登录)
	public int login(String empsId, String empsPwd, String otp) {
		List<?> list = this.getEmpsDao().find("from CotEmps e where e.empsId='" + empsId + "'");
		CotEmps cotEmps = new CotEmps();

		if (list.size() != 1) {
			return 0;
		} else {
			cotEmps = (CotEmps) list.get(0);
			if (!cotEmps.getEmpsPwd().equals(empsPwd)) {
				return 1;
			}
			if (cotEmps.getEmpsStatus() != null && cotEmps.getEmpsStatus() == 1) {
				return 6;// 员工离职
			}
		}
		WebContext wContext = WebContextFactory.get();
//		String ip = wContext.getHttpServletRequest().getRemoteAddr();
//		// 删除登录超时的记录,超过5分钟
//		String sql = "from CotLoginInfo c where c.loginTime<=TimeStamp(now()-500)";
//		// sql = " from CotLoginInfo c where DATE_ADD(c.loginTime, INTERVAL 5*60
//		// SECOND) <= now()";
//
//		List loginInfoList = this.getEmpsDao().find(sql);
//		List idsList = new ArrayList();
//		for (int i = 0; i < loginInfoList.size(); i++) {
//			CotLoginInfo loginInfo = (CotLoginInfo) (loginInfoList.get(i));
//			idsList.add(loginInfo.getId());
//		}
//		try {
//			if (idsList.size() > 0) {
//				this.getEmpsDao().deleteRecordByIds(idsList, "CotLoginInfo");
//			}
//		} catch (DAOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// if(listcount.size() >= 1)
		// return 2;
		// 判断目前登录总人数是否已经达到最大数---以前是查询ip,现在是登陆人
		String sql = "";
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql = "select count(distinct c.sessionId) from CotLoginInfo c where DATE(c.loginTime)='" + sdf.format(new Date()) + "'";
		List<?> listCount = this.getEmpsDao().find(sql);
		Integer count = ((Integer) listCount.get(0)).intValue();
//		if(idsList.size() > 0){
//			//由于事务控制问题，删除操作不能马上生效，当端数满后，又存在异常退出的倾向下，先模拟删除一个端
//			//空出一个端出来，使其可以登入，等方法执行完毕后，在做事务操作。
//			//count = count - 1;
//		}
		// 取得mac地址
		String mac = "";
		SystemInfoUtil systemInfoUtil = new SystemInfoUtil();
		mac = systemInfoUtil.getVolumSeri();
		// 查询注册表的信息
		// String mKey =
		List<?> slist = this.getEmpsDao().find("from CotServerInfo obj where obj.mechineKey='" + mac.substring(4, mac.length()).trim() + "' order by obj.isStangAlone desc");
		if (slist.size() == 1) {
			CotServerInfo cotServerInfo = (CotServerInfo) slist.get(0);
			String skiKey = KiCrypt.KEY_STRING;
			KiCrypt crypt = new KiCrypt();
			try {
				String resSki = crypt.KiDecrypt(skiKey, cotServerInfo.getServerNo());
				int max = Integer.parseInt(resSki.substring(0, 6), 16);
				// 如果查询值为99999999时不限制登录人数
				if (count >= max && max != 99999999) {
					return 3;
				}

				// 判断是否有正确的注册码(防止通过修改web.xml和数据库登录系统)
				String selectRegeditKey = cotServerInfo.getRegeditKey();
				String hexSn = this.getRegistService().getHexServerNo(max + "");
				String rightRegeditKey = null;
				if ("1".equals(cotServerInfo.getIsAlone())) {
					rightRegeditKey = this.getRegistService().getDemoRegeditKey(cotServerInfo, null, null, null);
				} else if ("0".equals(cotServerInfo.getIsAlone())) {
					rightRegeditKey = this.getRegistService().getRegeditKey(cotServerInfo.getMechineKey(), cotServerInfo.getSoftVer(), hexSn);
				}
				if (selectRegeditKey == null || "".equals(selectRegeditKey)) {
					return 4;
				} else {
					if (!selectRegeditKey.equals(rightRegeditKey)) {
						return 4;
					}
				}

				// 如果该员工需要输入动态命令牌则必须输入otp
				if (cotEmps.getEtId() != null) {
					if (otp.equals("")) {
						return 8;
					}
					// 判断该命令牌是否正确
					CotEtOtp cotEtOtp = (CotEtOtp) this.getEmpsDao().getById(CotEtOtp.class, cotEmps.getEtId());
					String seed = cotEtOtp.getAuthKey();
					int iDrift = cotEtOtp.getCurrDft();
					long lSucc = cotEtOtp.getCurrSucc();

					Map hashMap = OTPVerify.ET_CheckPwdz201(seed, // 令牌密钥
							System.currentTimeMillis() / 1000, // 调用本接口计算机的当前时间
							0, // 给0
							60, // 给60，因为每60秒变更新的动态口令
							iDrift, // 漂移值，用于调整硬件与服务器的时间偏差，见手册说明
							20, // 认证窗口，见手册说明
							lSucc, // 成功值，用于调整硬件与服务器的时间偏差，见手册说明
							otp); // 要认证的动态口令OTP

					Long nReturn = (Long) hashMap.get("returnCode");
					if (nReturn == OTPVerify.OTP_SUCCESS) {
						iDrift = ((Long) hashMap.get("currentDrift")).intValue();
						lSucc = ((Long) hashMap.get("currentUTCEpoch")).longValue();
						// 将漂移值和成功值存入cot_et_otp
						cotEtOtp.setCurrDft(iDrift);
						cotEtOtp.setCurrSucc(lSucc);
						List listEt = new ArrayList();
						listEt.add(cotEtOtp);
						this.getEmpsDao().updateRecords(listEt);
					}else if (nReturn == OTPVerify.OTP_ERR_REPLAY) {
						return 10;
					}  else {
						return 9;
					}
				}

				if ("1".equals(cotServerInfo.getIsAlone()))// 临时注册
				{
					return 7;// 临时用户登录
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return 5;
			}
		} else {
			return 4;
		}

		// 添加操作日志
		CotSyslog syslog = new CotSyslog();
		syslog.setEmpId(cotEmps.getId());
		syslog.setOpMessage("系统登陆成功");
		syslog.setOpModule("login");
		syslog.setOpTime(new Date(System.currentTimeMillis()));
		syslog.setOpType(0); // 1:添加 2：修改 3：删除 0：登陆
		List<CotSyslog> logList = new ArrayList<CotSyslog>();
		logList.add(syslog);
		sysLogService.addSysLog(logList);
		return 2;
	}

	// 登录验证(0代表工号不存在,1代表密码不正确,2代表成功)---(试用版)
	public int login_demo(String empsId, String empsPwd) {
		List<?> list = this.getEmpsDao().find("from CotEmps e where e.empsId='" + empsId + "'");
		if (list.size() != 1) {
			return 0;
		} else {
			CotEmps cotEmps = (CotEmps) list.get(0);
			if (!cotEmps.getEmpsPwd().equals(empsPwd)) {
				return 1;
			}
		}
		return 2;
	}

	public CotEmpsDao getEmpsDao() {
		return empsDao;
	}

	public void setEmpsDao(CotEmpsDao empsDao) {
		this.empsDao = empsDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.system.CotEmpsService#getEmpsByEmpId(java.lang.Integer)
	 */
	public CotEmps getEmpsByEmpId(String empId) {
		String sql = " from CotEmps t where t.empsId = '" + empId + "'";
		List list = this.getEmpsDao().find(sql);
		if (list != null && list.size() > 0)
			return (CotEmps) list.get(0);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.system.CotEmpsService#getLoginEmp()
	 */
	public CotEmps getLoginEmp() {
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps) session.getAttribute("emp");
		return emp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.system.CotEmpsService#modifyPwdByEmpId(java.lang.String,
	 *      java.lang.String)
	 * 
	 * 返回值:1:旧密码错误;0:修改密码成功
	 */
	public int modifyPwdByEmpId(String oldPwd, String newPwd) {
		CotEmps emp = getLoginEmp();
		if (!emp.getEmpsPwd().equals(oldPwd)) {
			return 1;
		}
		emp.setEmpsPwd(newPwd);
		this.getEmpsDao().update(emp);
		return 0;
	}

	// 判断目前登录总人数是否超过范围
	public boolean checkIsMaxLoginCount(String ip, Integer max) {
		String sql = "select count(distinct(c.loginIpaddr)) from CotLoginInfo c";
		List<?> list = this.getEmpsDao().find(sql);
		Integer count = (Integer) list.get(0);
		if (count >= max) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.system.CotEmpsService#getEmpsByRoleId(java.lang.String)
	 */
	public List getEmpsByRoleId(String roleId) {
		return this.getEmpsDao().find("from CotEmps obj where obj.roleId=" + roleId);
	}

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getEmpsDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}

	// 根据条件查询主报价单记录
	public List<?> getEmpsList(QueryInfo queryInfo) {
		try {
			return this.getEmpsDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	public CotRegistService getRegistService() {
		return registService;
	}

	public void setRegistService(CotRegistService registService) {
		this.registService = registService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.system.CotEmpsService#updateLoginInfo()
	 */
	public void updateLoginInfo(CotLoginInfo loginInfo) {
		// TODO Auto-generated method stub
		try {
			List list = new ArrayList();
			list.add(loginInfo);
			this.getEmpsDao().updateRecords(list);
			System.out.println("update login time success");
		} catch (Exception e) {
			System.out.println("更新系统时间异常,登录ID:" + loginInfo.getId());
		}

	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getEmpsDao().getJsonData(queryInfo);
	}

	// 判断登录号是否存在
	public boolean findIsExistEmpsId(String empsId, String eId) {
		List<Integer> res = this.getEmpsDao().find("select c.id from CotEmps c where c.empsId='" + empsId + "'");
		if (res.size() != 1) {
			return true;
		} else {
			if (res.get(0).toString().equals(eId)) {
				return true;
			} else {
				return false;
			}
		}
	}

	// 判断用户名是否存在
	public boolean findIsExistEmpsName(String empsName, String eId) {
		List<Integer> res = this.getEmpsDao().find("select c.id from CotEmps c where c.empsName='" + empsName + "'");
		if (res.size() != 1) {
			return true;
		} else {
			if (res.get(0).toString().equals(eId)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.system.CotEmpsService#getMailEmpsList()
	 */
	public List<CotEmps> getMailEmpsList() {
		String strSql = " from CotEmps obj where 1=1 and obj.empsStatus = 0";
		List<CotEmps> empList = this.getEmpsDao().find(strSql);
		for (int i = empList.size() - 1; i >= 0; i--) {
			CotEmps emps = (CotEmps) empList.get(i);
			if (emps.getEmpsMailHost() == null || "".equals(emps.getEmpsMailHost())) {
				empList.remove(i);
			} else if (emps.getEmpsMailPwd() == null || "".equals(emps.getEmpsMailPwd())) {
				empList.remove(i);
			}
		}
		return empList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.system.CotEmpsService#getMailAlarmByEmpId(java.lang.Integer)
	 */
	public String getMailAlarmByEmpId(Integer empId, boolean isPublic) {
		Cache cache4MailMsg = ContextUtil.getCache4MailMsg();
		if (isPublic) {
			// 获取公共邮箱的信息
			Element element = cache4MailMsg.get("PUBLIC_MAIL");
			if (element == null)
				return null;
			String msg = element.getValue().toString();
			// 用完后，删除缓存
			cache4MailMsg.remove("PUBLIC_MAIL");
			return msg;
		} else {
			Element element = cache4MailMsg.get(empId);
			if (element == null)
				return null;
			String msg = element.getValue().toString();
			// 用完后，删除缓存
			cache4MailMsg.remove(empId);
			return msg;
		}
	}

	// 同步动态密码 (0:该员工帐户不存在;1:该帐户不需要动态密码;2:同步动态密码失败;3:同步成功)
	public int tong(String empId, String oneOtp, String twoOtp) {
		List<?> list = this.getEmpsDao().find("from CotEmps e where e.empsId='" + empId + "'");
		CotEmps cotEmps = new CotEmps();
		if (list.size() != 1) {
			return 0;
		} else {
			cotEmps = (CotEmps) list.get(0);
		}
		if(cotEmps.getEtId()==null){
			return 1;
		}
		// 判断该命令牌是否正确
		CotEtOtp cotEtOtp = (CotEtOtp) this.getEmpsDao().getById(CotEtOtp.class, cotEmps.getEtId());
		String seed = cotEtOtp.getAuthKey();
		int iDrift = cotEtOtp.getCurrDft();
		long lSucc = cotEtOtp.getCurrSucc();

		Map hashMap = OTPVerify.ET_Syncz201(seed, System.currentTimeMillis() / 1000, 0, 60, iDrift, 120, lSucc, oneOtp, twoOtp);

		Long nReturn = (Long) hashMap.get("returnCode");

		if (nReturn == OTPVerify.OTP_SUCCESS) {
			iDrift = ((Long) hashMap.get("currentDrift")).intValue();
			lSucc = ((Long) hashMap.get("currentUTCEpoch")).longValue();
			// 将漂移值和成功值存入cot_et_otp
			cotEtOtp.setCurrDft(iDrift);
			cotEtOtp.setCurrSucc(lSucc);
			List listEt = new ArrayList();
			listEt.add(cotEtOtp);
			this.getEmpsDao().updateRecords(listEt);
		} else {
			return 2;
		}
		return 3;
	}

	public List getList(String tbName) {
		return this.getEmpsDao().getRecords(tbName);
	}
	
	//复制权限
	public Integer copyEmpRight(Integer empsId_from, Integer empsId_to)throws Exception {
		System.out.println("right here");
		System.out.println("empid_from is"+empsId_from);
		System.out.println("empid_to is"+empsId_to);
		try{
			this.getEmpsDao().copyPopedom(empsId_from, empsId_to);
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return 2;
		}
	}

}
