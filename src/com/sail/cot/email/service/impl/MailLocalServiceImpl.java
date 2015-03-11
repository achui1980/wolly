package com.sail.cot.email.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.jason.core.Application;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.domain.CotMailCache;
import com.sail.cot.domain.CotMailCfg;
import com.sail.cot.domain.CotMailEmpsRule;
import com.sail.cot.domain.CotMailExecute;
import com.sail.cot.domain.CotMailTree;
import com.sail.cot.domain.vo.CotAutoCompleteVO;
import com.sail.cot.domain.vo.HistoryMailAttachVO;
import com.sail.cot.domain.vo.HistoryMailVO;
import com.sail.cot.email.service.MailCfgService;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.email.service.MailTreeService;
import com.sail.cot.email.util.Constants;
import com.sail.cot.email.util.MailCacheUtil;
import com.sail.cot.email.util.MailEntityConverUtil;
import com.sail.cot.email.util.MailLocalUtil;
import com.sail.cot.mail.MailExecuteAction;
import com.sail.cot.mail.ruleservice.MailCheckService;
import com.sail.cot.mail.ruleservice.MailRuleService;
import com.sail.cot.mail.sysservice.MailReciveJob;
import com.sail.cot.mail.sysservice.MailRecvPublicJob;
import com.sail.cot.mail.sysservice.MailRecvPublicSchedule;
import com.sail.cot.mail.sysservice.MailRecvSchedule;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;
import com.sun.mail.pop3.POP3Folder;
import com.zhao.mail.MailService;
import com.zhao.mail.ReciveMailService;
import com.zhao.mail.entity.MailCfg;
import com.zhao.mail.impl.MailServiceDefault;
import com.zhao.mail.impl.ReciveMailDefault;
import com.zhao.mail.util.CodeUtil;
/**
 * 对本地邮件的所有操作
 * @author zhao
 *
 */
public class MailLocalServiceImpl implements MailLocalService {
	private CotBaseDao cotBaseDao;
	private Logger logger = Log4WebUtil.getLogger(MailLocalServiceImpl.class);
	private MailLocalUtil mailLocalUtil = new MailLocalUtil();
	private MailTreeService mailTreeService;
	private MailCfgService mailCfgService;
	public MailCfgService getMailCfgService(){
		if(mailCfgService == null){
			mailCfgService = (MailCfgService) Application.getInstance().getContainer().getComponent("MailCfgService");
		}
		return mailCfgService;
	}
	MailCheckService mailCheckService;
	public MailCheckService getMailCheckService() {
		if(mailCheckService == null){
			mailCheckService = (MailCheckService)SystemUtil.getService("MailCheckService");
		}
		return mailCheckService;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailReadOneService#readMailAllInfo(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public CotMail readMailAllInfo(String id) {
		// TODO:
		logger.debug("执行读取邮件详情方法");
		if (id == null || id.trim().equals(""))
			return null;
		Object[] objs = new Object[1];
		objs[0] = id;
		CotMail cotMail= (CotMail) this.getCotBaseDao().getById(CotMail.class, id);
		if(cotMail==null){
			logger.debug("邮件为空，不执行");
			return null;
		}
		String hql = "select obj.name,obj.url,obj.size from CotMailAttach as obj where obj.mailId = ?";
		List<Object[]> attachList = this.getCotBaseDao().queryForLists(hql, objs);
		for (Object[] attachObj : attachList) {
			CotMailAttach attach = new CotMailAttach();
			attach.setName((String)attachObj[0]);
			attach.setUrl((String)attachObj[1]);
			attach.setSize((Integer)attachObj[2]);
			cotMail.getAttachs().add(attach);
		}
		Integer status = cotMail.getMailStatus();
		if(status==null){
			cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_READ);
			List<CotMail> cotMailList = new ArrayList<CotMail>();
			cotMailList.add(cotMail);
			this.getCotBaseDao().saveOrUpdateRecords(cotMailList);
		}
		else if (status == Constants.MAIL_LOCAL_STATUS_NOREAD || status == Constants.MAIL_LOCAL_STATUS_NOTIFICATION || status == Constants.MAIL_LOCAL_STATUS_ASSIGNNOREAD || status == Constants.MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION) {
			status = status == Constants.MAIL_LOCAL_STATUS_ASSIGNNOREAD||status ==Constants.MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION ? Constants.MAIL_LOCAL_STATUS_ASSIGNREAD : Constants.MAIL_LOCAL_STATUS_READ;
			cotMail.setMailStatus(status);
			cotMail.setIsNotification(false);
			List<CotMail> cotMailList = new ArrayList<CotMail>();
			cotMailList.add(cotMail);
			this.getCotBaseDao().saveOrUpdateRecords(cotMailList);
		}
		if(cotMail.getIsNotification()&&cotMail.getMailType()==Constants.MAIL_LOCAL_TYPE_INBOX){
			cotMail.setIsNotification(false);
			List<CotMail> cotMailList = new ArrayList<CotMail>();
			cotMailList.add(cotMail);
			this.getCotBaseDao().saveOrUpdateRecords(cotMailList);
		}
		String body = cotMail.getBody();
		if(body!=null&&body.toLowerCase().indexOf("<html")==-1&&body.toLowerCase().indexOf("</html>")==-1){
			body = body.replaceAll("\r\n", "<br />");
			cotMail.setBody(body);
		}
		if(body!=null){
			body = body.replaceAll("src=\"mail/attach", "src=\"downLoadMailFile.down?path=mail/attach");
			body = body.replaceAll("src='mail/attach", "src='downLoadMailFile.down?path=mail/attach");
			cotMail.setBody(body);
		}
		return cotMail;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.email.service.MailLocalService#deleteMails(java.lang.String[])
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteMails(List<String> ids) {
		// TODO:有使用
		logger.debug("执行彻底删除本地邮件方法");
		if (ids == null || ids.size() == 0){
			logger.debug("执行删除本地邮件方法");
			return false;
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotMail");
			return true;
		} catch (DAOException e) {
			logger.error("彻底删除本地邮件失败", e);
			return false;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailReadOneService#getExcel(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public CotMail getExcel(String key, String random) {
		// TODO:有使用
		logger.debug("执行获得带excel附件的邮件方法");
		try {
			HttpSession session = WebContextFactory.get().getSession();
			CotMail cotMail = (CotMail) session.getAttribute(key);
			session.removeAttribute(key);
			// 将附件临时保存在session的map+random中，以继续附件上传删除操作
			List<CotMailAttach> attachList = cotMail.getAttachs();
			if (attachList.size() > 0) {
				Map map = new HashMap<String, CotMailAttach>();
				for (int i = 0; i < attachList.size(); i++) {
					CotMailAttach mailAttach = attachList.get(i);
					map.put(mailAttach.getName() + "*" + (i + 1), mailAttach);
				}
				session.setAttribute("map" + random, map);
				session.setAttribute("random" + random, random);
				session.setAttribute("nameIndex" + random, attachList.size());
			}
			return cotMail;
		} catch (Exception e) {
			logger.error("获得带excel附件的邮件错误", e);
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailReadOneService#getSendTypeInfo(java.lang.String)
	 */
	public CotMail getSendTypeInfo(String id, String random) {
		logger.debug("执行获得发件类型邮件不带附件方法");
		CotMail cotMail = (CotMail) this.getCotBaseDao().getById(CotMail.class,id);
		return cotMail;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailReadOneService#getSendTypeInfo(java.lang.String, java.lang.String)
	 */
	public CotMail getSendTypeInfoAndAttach(String id, String random) {
		logger.debug("执行获得发件类型邮件带附件方法");
		try {
			HttpSession session = WebContextFactory.get().getSession();
			CotMail cotMail = this.readMailAllInfo(id);
			if(cotMail==null){
				logger.debug("邮件为空，不执行");
				return null;
			}
			// 将附件临时保存在session的map+random中，以继续附件上传删除操作
			List<CotMailAttach> attachList = cotMail.getAttachs();
			if (attachList.size() > 0) {
				Map<String,CotMailAttach> map = new HashMap<String, CotMailAttach>();
				for (int i = 0; i < attachList.size(); i++) {
					CotMailAttach mailAttach = attachList.get(i);
					map.put(mailAttach.getName().replaceAll(Constants.MAIL_SEND_ATTACH_UPLOAD_RG, "") + "*" + (i + 1), mailAttach);
					String path = Constants.MAIL_ATTACH_UPLOAD_PATH + "/" + RandomStringUtils.randomAlphanumeric(20) + "/" + mailAttach.getName();
					SystemUtil.copyRealFile(MailLocalUtil.getProPath()+CodeUtil.deCode(mailAttach.getUrl()), MailLocalUtil.getProPath()+path);
					mailAttach.setUrl(path);
				}
				session.setAttribute("map" + random, map);
				session.setAttribute("random" + random, random);
				session.setAttribute("nameIndex" + random, attachList.size() + 1);
			}
			return cotMail;
		}catch (Exception e) {
			logger.debug("获得收件箱邮件错误：" + e.getMessage(), e);
		}
		return null;
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailUpdateService#saveSend(com.sail.cot.domain.CotMail, java.lang.String)
	 */
	public void saveSend(CotMail cotMail, String random) throws DAOException {
		logger.debug("执行保存邮箱发送的邮件到数据库方法");
		// TODO:有使用
		mailLocalUtil.saveSendMail(cotMail, random, Constants.MAIL_LOCAL_TYPE_SEND);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailUpdateService#saveCheck(com.sail.cot.domain.CotMail, java.lang.String)
	 */
	public void saveCheck(CotMail cotMail, String random) throws DAOException {
		logger.debug("执行保存邮箱发送的邮件到数据库方法");
		// TODO:有使用
		mailLocalUtil.saveSendMail(cotMail, random, Constants.MAIL_LOCAL_TYPE_CHECK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.email.service.MailSendService#saveDraft(com.achui.mail.entity.MailObject)
	 */
	public void saveDraft(CotMail cotMail, String random) throws DAOException {
		// TODO:
		logger.debug("保存邮件到草稿箱");
		mailLocalUtil.saveSendMail(cotMail, random, Constants.MAIL_LOCAL_TYPE_DRAFT);
	}

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	@SuppressWarnings("unchecked")
	public boolean moveAssignMail(Integer empId, List<String> dbIDs) {
		// TODO:有使用
		logger.debug("执行更新邮件员工ID方法");
		if(dbIDs==null||dbIDs.size()==0){
			logger.debug("邮件ID为空，不执行更新");
			return false;
		}
		StringBuffer select = new StringBuffer("from CotMail obj where obj.id in(");
		select.append("?");
		for (int i = 1; i < dbIDs.size(); i++) {
			select.append(",?");
		}
		select.append(")");
		List<CotMail> cotMailList = this.getCotBaseDao().queryForLists(select.toString(), dbIDs.toArray());
		if(cotMailList==null||cotMailList.size()==0)
			return false;
		for (CotMail cotMail : cotMailList) {
			logger.debug("邮件更改前状态："+cotMail.getMailStatus());
			cotMail.setEmpId(empId);
			cotMail.setNodeId(this.getMailTreeService().findMailInboxId(empId));
			Integer status = cotMail.getMailStatus();
			if(status == Constants.MAIL_LOCAL_STATUS_ERROR||status==Constants.MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION){
			}else if(status==Constants.MAIL_LOCAL_STATUS_NOTIFICATION){
				cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION);
			}else{
				cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_ASSIGNNOREAD);
			}
			logger.debug("邮件更改后状态："+cotMail.getMailStatus());
		}
		this.getCotBaseDao().saveOrUpdateRecords(cotMailList);
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailHistoryService#getHistoryAttach(com.sail.cot.query.QueryInfo)
	 */
	@SuppressWarnings("unchecked")
	public List<HistoryMailAttachVO> getHistoryAttach(String hql,Object[] params,int start,int limit) {
		List<HistoryMailAttachVO> listVo = new ArrayList<HistoryMailAttachVO>();
		try {
			List<Object[]> list = this.getCotBaseDao().queryForLists(hql, start, limit, params);
			if(list!=null)
				for (Object[] objs : list) {
					HistoryMailAttachVO hMailAttachVO = new HistoryMailAttachVO();
					hMailAttachVO.setId((String) objs[0]);
					hMailAttachVO.setMailId((String) objs[1]);
					hMailAttachVO.setMailType((Integer) objs[2]);
					hMailAttachVO.setName((String) objs[3]);
					hMailAttachVO.setSendTime((Date) objs[4]);
					hMailAttachVO.setSize((Integer) objs[5]);
					hMailAttachVO.setUrl((String) objs[6]);
					listVo.add(hMailAttachVO);
				}
		} catch (Exception e) {
			logger.error("获得历史附件失败",e);
		}
		return listVo;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailHistoryService#getHistoryMail(com.sail.cot.query.QueryInfo)
	 */
	@SuppressWarnings("unchecked")
	public List<HistoryMailVO> getHistoryMail(String hql,Object[] params,int start,int limit) {
		logger.debug("执行获得历史邮件方法");
		List<HistoryMailVO> listVo = new ArrayList<HistoryMailVO>();
		try {
			List<Object[]> list = this.getCotBaseDao().queryForLists(hql, start, limit, params);
			if(list!=null)
				for (Object[] objs : list) {
					HistoryMailVO historyMailVO = new HistoryMailVO();
					historyMailVO.setId((String) objs[0]);
					historyMailVO.setMailType((Integer) objs[1]);
					historyMailVO.setSubject((String) objs[2]);
					historyMailVO.setIsContainAttach((Boolean) objs[3]);
					historyMailVO.setSendTime((Date) objs[4]);
					listVo.add(historyMailVO);
				}
		} catch (Exception e) {
			logger.error("获得历史邮件失败",e);
		}
		return listVo;
	}
	public MailTreeService getMailTreeService() {
		return mailTreeService;
	}

	public void setMailTreeService(MailTreeService mailTreeService) {
		this.mailTreeService = mailTreeService;
	}
	@SuppressWarnings("unchecked")
	public boolean saveMailToEmpNode(Integer empId, Integer nodeId) throws NumberFormatException, DAOException {
		logger.debug("执行接收新邮件方法");
		// TODO:调用邮件规则，占不用指定接收到指定节点
		nodeId = this.getMailTreeService().findMailInboxId(empId);
		if(empId==null){ // 公共邮箱
			logger.debug("公共邮箱接收");
			MailRecvPublicSchedule recvPublicSchedule = new MailRecvPublicSchedule();
			int state = recvPublicSchedule.getPublicJobState();
			if(state!=1&&state!=-1){
				recvPublicSchedule.pauseOrresumAllJobs("P");
			}
			MailRecvPublicJob mailRecvPublicJob = new MailRecvPublicJob();
			mailRecvPublicJob.recvPublicAsign();
			if(state!=1&&state!=-1){
				recvPublicSchedule.restartSchedule();
			}
		}else { // 员工邮箱
			logger.debug("员工邮箱接收");
			MailRecvSchedule recvSchedule = new MailRecvSchedule();
			int state = recvSchedule.getJobStateByEmpId(empId);
			if(state!=1&&state!=-1){
				recvSchedule.pauseOrresumeJobByName(empId.toString(), "P");
			}
			MailReciveJob mailReciveJob = new MailReciveJob();
			mailReciveJob.recvAsign(empId);
			if(state!=1&&state!=-1){
				recvSchedule.restartJobByName(empId.toString(), empId);
			}
		}
		return true;
	}	
	@SuppressWarnings("unchecked")
	public List<CotMail> saveMailAll() {
		// TODO: 有使用
		logger.debug("公共邮箱接收邮件");
		// 获得公共邮箱配置，并初始化邮箱
		Integer nodeId = this.mailTreeService.findMailGInboxId();
		return this.saveMailByEmpAndNode(null,nodeId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailUpdateService#saveMailByEmp(java.lang.Integer)
	 */
	public List<CotMail> saveMailByEmp(Integer empId) {
		// TODO:有使用
		logger.debug("员工接收邮件,EmpId:"+empId);
		Integer nodeId = this.getMailTreeService().findMailInboxId(empId);
		return this.saveMailByEmpAndNode(empId, nodeId);
	}
	
	private List<CotMail> saveMailByEmpAndNode(Integer empId,Integer nodeId) {
		// TODO:有使用
		logger.debug("执行接收邮件方法");
		try {
			// 获得配置信息
			MailCfgService cfgService = this.getMailCfgService();
			logger.debug("获得邮箱配置");
			CotMailCfg cotMailCfg;
			if(empId==null)	// empId为空则获得公共邮箱
				cotMailCfg = cfgService.getCfg();
			else// 否则获得私人邮箱
				cotMailCfg = cfgService.getEmpCfg(empId);
			return readMessageToLocal(empId, nodeId, cotMailCfg,null);
		} catch (Exception e) {
			logger.error("接收新邮件错误",e);
			return null;
		}
	}
	/**
	 * 读取邮件并保存邮件
	 * @param empId
	 * @param nodeId
	 * @param cotMailCfg
	 * @param existsMails 可选，当不为空时，则表示从服务器重新接收
	 * @return
	 * @throws Exception 
	 */
	private List<CotMail> readMessageToLocal(Integer empId, Integer nodeId,CotMailCfg cotMailCfg,CotMail existsMail) throws Exception {
		logger.debug("创建邮箱服务");
		String account = cotMailCfg.getDefaultAccount();
		// 如果账号不存在则不执行接收
		if(account==null||account.trim().equals("")){
			logger.debug("不存在邮箱账号");
			return null;
		}
		MailService mailService = null;
		try {
			MailCfg cfg = MailEntityConverUtil.toMailCfg(cotMailCfg);
			mailService = new MailServiceDefault();
			mailService.setMailCfg(cfg);
			mailService.initConfig();
			ReciveMailService<CotMail> reciveMailService = new ReciveMailDefault<CotMail, CotMailAttach>(
					CotMail.class, CotMailAttach.class, false);
			reciveMailService.setMailService(mailService);
			String path = MailLocalUtil.getProPath();
			logger.info("开始接收文件");
			List<CotMail> saveMailList = new ArrayList<CotMail>();
			POP3Folder folder = (POP3Folder) mailService
					.openFolder(Folder.READ_ONLY);
			
			logger.info("获得已存在邮件UID");
			List<String> uids = MailCacheUtil.getCacheListByMailAddr(account);
			List<Message> msgList = null;
			if(existsMail!=null){ // 更新邮件
				List<String> existsIds = new ArrayList<String>();
				String existsId = existsMail.getErrMessage();
				if(existsId!=null&&existsId.indexOf("，")!=-1){
					existsId.substring(0,existsId.indexOf("，"));
					existsIds.add(existsId);
				}
				msgList = reciveMailService.readMessages(existsIds);
			}else { // 接收新邮件
				msgList = reciveMailService.readNewMessages(uids);
//				msgList = reciveMailService.readNewMessages(new ArrayList<String>());// TODO:测试用
			}
			
			if (msgList.size() == 0) {
				logger.info("服务器没有新邮件");
				return null;
			} else {
				logger.info("存在" + msgList.size() + "封新邮件");
				//附件大小限制
				String attachSizeLimit = ContextUtil.getProperty(
						"remoteaddr.properties", "attach_size_limit");
				Integer sizeLimit = 1;
				if (attachSizeLimit != null) {
					try {
						sizeLimit = new Integer(attachSizeLimit);
					} catch (Exception e) {
						logger.error("附件大小限制转换错误", e);
						sizeLimit = 5;
						logger.debug("附件大小限制为默认：" + sizeLimit + "M");
					}
				}
				//				sizeLimit = 2; 测试用
				Date addTime = null; // 邮件插入数据库时间
				CotMail cotMail = null; // 读取的邮件
				if(existsMail!=null)
					cotMail = existsMail;
				List<CotMail> cotMailList = null; // 要保存的邮件集合
				boolean isSaveMail = false; // 是否已将邮件执久化
				String uid = null;
				int newMsgCount = 0;
				logger.info("开始读取所有新邮件");
				for (Message msg : msgList) {
					try {
						isSaveMail = false; // 初始化为false;
						uid = folder.getUID(msg);//1tbiAQASC0amO2kdDgANs0
						String pastUID = ""; // 测试用
						if (existsMail!=null||pastUID.indexOf(uid) != -1) {
							System.out.println("pastUID:" + uid);
						} else if (MailCacheUtil.isExistMsgId(uid, account))
							continue;
						logger.info("开始读取一封新邮件");
						cotMail = reciveMailService.readOneMailBaseInfo(msg);
						logger.info("邮件大小:" + cotMail.getSize());
						if (cotMail == null) {
							logger.debug("邮件UID为：" + uid + "未读到");
							continue;
						}
						String uidFlag = uid+"，";
						if (cotMail.getSize() >= sizeLimit * 1024 * 1024) {
							logger.info("邮件大小超过指定大小");
							cotMail.setMailStatus(Constants.MAIL_LOCAL_STATUS_ERROR);
							cotMail.setBody("  "); // 空时，页面会出错
							cotMail.setErrMessage(uidFlag + "邮件大小超过指定大小");
						} else {
							String attachDir = Constants.MAIL_ATTACH_SAVE_INBOX_PATH
									+ (empId == null ? "/system" : "/emps");
							CotMail detailMail = reciveMailService
									.readOneMailDetailInfo(msg, path
											+ attachDir, null);
							if (detailMail == null) {
								logger.info("读取邮件主体内容失败");
								cotMail
										.setMailStatus(Constants.MAIL_LOCAL_STATUS_ERROR);
								cotMail.setBody("  ");
								cotMail.setErrMessage(uidFlag + "读取邮件主体内容失败");
							} else {
								logger.info("读取邮件主体内容成功");
//								cotMail.setBody(new String(detailMail.getBody().getBytes(),"utf-8"));
								cotMail.setBody(detailMail.getBody());
								cotMail.setAttachs(detailMail.getAttachs());
								if (cotMail.getIsNotification())
									cotMail
											.setMailStatus(Constants.MAIL_LOCAL_STATUS_NOTIFICATION); // 新邮件要回执
								else
									cotMail
											.setMailStatus(Constants.MAIL_LOCAL_STATUS_NOREAD); // 新邮件不回执
								cotMail.setErrMessage(uidFlag);
							}
						}
						logger.info("正在给邮件" + cotMail.getSubject()
								+ "加入数据库相关信息");
						// 重新判断是否包含附件
						cotMail.setIsContainAttach(cotMail.getAttachs().size() > 0);
						addTime = new Date();
						cotMail.setAddTime(addTime); // 设置加入数据库时间
						cotMail.setEmpId(empId); // 所属员工ID
						cotMail.setMailType(Constants.MAIL_LOCAL_TYPE_INBOX); // 邮件类型
						cotMail.setNodeId(nodeId); // 节点ID
						logger.debug("邮件数据开始执久化");
						cotMailList = new ArrayList<CotMail>(); // 要保存的邮件集合
						cotMailList.add(cotMail);
						
						this.getCotBaseDao().saveOrUpdateRecords(cotMailList);
						isSaveMail = true; // 执行过执久化
						saveMailList.add(cotMail);
						logger.debug("邮件数据执久化成功");
						this.parseSaveAttach(cotMail);
						this.saveMailCache(account, uid);
						newMsgCount++; // 新邮件加1
						logger.debug("邮件状态：" + cotMail.getMailStatus());
						logger.info("读取一封新邮件结束");
					} catch (Exception e) {
						logger.error("读取一封新邮件失败", e);
						if(existsMail==null){
							if (isSaveMail && cotMail != null) {
								List<String> ids = new ArrayList<String>();
								ids.add(cotMail.getId());
								this.deleteMails(ids);
							}
							MailCacheUtil.removeMsgIdFromCache(uid, account);
						}
					}
				}
				logger.info("读取所有新邮件结束,读取到 " + newMsgCount + " 封新邮件");
				return saveMailList;
			}
		} catch (Exception e) {
			throw e;
		}finally{
			if(mailService!=null)
				mailService.closeAll();
		}
	}
	/**
	 * 解释附件并保存附件
	 * @param cotMail
	 * @return
	 */
	private void parseSaveAttach(CotMail cotMail) {
		boolean existsCid = false;
		List<CotMailAttach> mailAttachList = cotMail.getAttachs();
		if(mailAttachList.size()>0){
			logger.info("正在给邮件"+cotMail.getSubject()+"的附件加入数据库相关信息");
			for (int i=0;i<mailAttachList.size();i++) {
				CotMailAttach cotMailAttach = mailAttachList.get(i);
				cotMailAttach.setAddTime(cotMail.getAddTime());
				cotMailAttach.setCustUrl(cotMail.getSendUrl());
				cotMailAttach.setEmpId(cotMail.getEmpId());
				cotMailAttach.setMailId(cotMail.getId());
				cotMailAttach.setMailType(cotMail.getMailType());
				cotMailAttach.setSendTime(cotMail.getSendTime());
				cotMailAttach.setUrl(CodeUtil.enCodeMU(cotMailAttach.getUrl().replaceAll(MailLocalUtil.getProPath(), "")));
				String body = cotMail.getBody();
				String cid = cotMailAttach.getCid();
				
				if (cid != null && cid.length() > 2) {
					logger.info("cid:"+cid);
					System.out.println("src=\"cid:" + cid.substring(1, cid.length() - 1)+"\"");
					cotMail.setBody(cotMail.getBody().replaceAll("src=\"cid:" + cid.substring(1, cid.length() - 1)+"\"", " src=\"downLoadMailFile.down?path="+cotMailAttach.getUrl()+"\""));
					System.out.println("cid:" + cid.substring(1, cid.length() - 1));
					cotMail.setBody(cotMail.getBody().replaceAll("cid:" + cid.substring(1, cid.length() - 1), "downLoadMailFile.down?path="+cotMailAttach.getUrl()));
					existsCid = true;
					mailAttachList.remove(i);
					i--;
				}
				String attachName = cotMailAttach.getName();
				if(body!=null&&(body.indexOf("src=\""+attachName+"\"")!=-1||body.indexOf("src='"+attachName+"'")!=-1)){
					logger.info("logo_attach:"+attachName);
					cotMail.setBody(cotMail.getBody().replaceAll("src=\""+attachName+"\"", "src=\"downLoadMailFile.down?path="+cotMailAttach.getUrl()+"\""));
					cotMail.setBody(cotMail.getBody().replaceAll("src='"+attachName+"'", "src='downLoadMailFile.down?path="+cotMailAttach.getUrl()+"'"));
					existsCid = true;
					mailAttachList.remove(i);
					i--;
				}
			}
			logger.debug("邮件附件数据开始执久化");
			this.getCotBaseDao().saveOrUpdateRecords(mailAttachList);
			logger.debug("邮件附件数据执久化成功");
		}
		if (existsCid){
			logger.debug("存在CID，邮件数据开始更新");
			List<CotMail> cotMailList = new ArrayList<CotMail>(); // 要保存的邮件集合
			cotMail.setIsContainAttach(cotMail.getAttachs().size()>0);
			cotMailList.add(cotMail);
			this.getCotBaseDao().saveOrUpdateRecords(cotMailList);
			logger.debug("邮件数据更新结束");
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailUpdateService#saveMail(com.sail.cot.domain.CotMail, java.lang.Integer)
	 */
	public boolean saveMail(CotMail cotMail, Integer nodeId) {
		// TODO: 有使用
		logger.debug("执行保存邮件到指定的节点方法");
		if(cotMail!= null){
			try {
				CotMailTree mailTree = (CotMailTree) this.getCotBaseDao().getById(CotMailTree.class, nodeId);  // 获得树节点
				if (mailTree == null)
					return false;
				if (mailTree.getFlag() == null)
					return false;
				Date addTime = new Date();	 // 添加时间
				String flag = mailTree.getFlag();
				List<CotMail> cotMailList = new ArrayList<CotMail>();
				cotMail.setAddTime(addTime);
				cotMail.setIsContainAttach(cotMail.getAttachs().size() > 0);
				cotMail.setEmpId(mailTree.getEmpId());	// 所属员工
				cotMail.setNodeId(nodeId); // 所属节点
				int mailStatus = Constants.MAIL_LOCAL_STATUS_NOREAD; // TODO: 默认保存为未读
				if(cotMail.getMailStatus()==null)
					cotMail.setMailStatus(mailStatus);
				int mailType = Constants.MAIL_LOCAL_TYPE_INBOX;
				if(flag.equals(Constants.MAIL_TREE_FLAG_SEND))
					mailType = Constants.MAIL_LOCAL_TYPE_SEND;
				else if(flag.equals(Constants.MAIL_TREE_FLAG_DRATF))
					mailType = Constants.MAIL_LOCAL_TYPE_DRAFT;
				cotMail.setMailType(mailType);
				//cotMail.setCustId(custId); // TODO:导入时可能得归档客户
				cotMailList.add(cotMail);
				this.getCotBaseDao().saveRecords(cotMailList);	// 保存邮件
				this.parseSaveAttach(cotMail);
				return true;
			} catch (Exception e) {
				logger.error("保存邮件错误",e);
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<CotAutoCompleteVO> getAutoCompletList(QueryInfo queryInfo) {
		try {
			List<String> list = this.getCotBaseDao().findRecords(queryInfo);
			List<CotAutoCompleteVO> aList = new ArrayList<CotAutoCompleteVO>();
			for (String name : list) {
				aList.add(new CotAutoCompleteVO(name));
			}
			return aList;
		} catch (DAOException e) {
			logger.error("获得自动补全主题错误："+e.getMessage(),e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean saveArchivesMail(Integer custId,List<String> dbIDs) throws DAOException {
		// TODO：有使用
		logger.debug("执行更新邮件客户方法");
		if(dbIDs==null||dbIDs.size()==0){
			logger.debug("邮件数为空，不执行");
			return false;
		}
		logger.debug("客户ID："+custId);
		String hql = "update CotMail obj set obj.custId = :custId where obj.id in (:ids)";
		Map map = new HashMap();
		map.put("custId", custId);
		map.put("ids", dbIDs);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		int result = this.getCotBaseDao().executeUpdate(queryInfo,map);
		if(result>0){
			logger.debug("更新成功");
			return true;
		}else {
			logger.debug("更新失败");
			return false;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailUpdateService#updateRecvMailStatus(java.util.List, int)
	 */
	@SuppressWarnings("unchecked")
	public void updateMailStatus(List<String> ids, boolean isNew) {
		// TODO：
		logger.debug("更新本地邮件状态");
		if (ids == null || ids.size() == 0)
			return ;
		StringBuffer select = new StringBuffer("from CotMail obj where obj.id in(?");
		for (int i = 1; i < ids.size(); i++) {
			select.append(",?");
		}
		select.append(")");
		List<CotMail> mailList = this.getCotBaseDao().queryForLists(select.toString(), ids.toArray());
		int status = isNew? Constants.MAIL_LOCAL_STATUS_NOREAD:Constants.MAIL_LOCAL_STATUS_READ;
		int assignStauts = isNew ? Constants.MAIL_LOCAL_STATUS_ASSIGNNOREAD : Constants.MAIL_LOCAL_STATUS_ASSIGNREAD;
		if(mailList!=null&&!mailList.isEmpty()){
			for (CotMail cotMail : mailList) {
				if(cotMail.getMailStatus()<=2)
					cotMail.setMailStatus(status);
				else if(cotMail.getMailStatus()<=4)
					cotMail.setMailStatus(assignStauts);
			}
			this.getCotBaseDao().updateRecords(mailList);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCacheService#getMailListCache()
	 */
	@SuppressWarnings("unchecked")
	public List<CotMailCache> getMailListCache() {
		String strSql = " from CotMailCache obj";
		List<CotMailCache> list = this.getCotBaseDao().find(strSql);
		return list;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCacheService#saveMailCache(com.sail.cot.domain.CotMailCache)
	 */
	@SuppressWarnings("deprecation")
	public void saveMailCache(String mailAddr,String msgId) {
		Date date = new Date();
		CotMailCache mailCache = new CotMailCache(mailAddr,msgId,date,date.getYear());
		List<CotMailCache> list = new ArrayList<CotMailCache>();
		list.add(mailCache);
		this.getCotBaseDao().saveOrUpdateRecords(list);
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.email.service.MailUpdateService#saveArchivesMail4Fac(java.lang.Integer, java.util.List)
	 */

	@SuppressWarnings("unchecked")
	public boolean saveArchivesMail4Fac(Integer facId, List<String> dbIDs) throws DAOException {
		// TODO：有使用
		logger.debug("执行更新邮件归档到厂家方法");
		if(dbIDs==null||dbIDs.size()==0){
			logger.debug("邮件数为空，不执行");
			return false;
		}
		logger.debug("客户ID："+facId);
		String hql = "update CotMail obj set obj.facId = :facId where obj.id in (:ids)";
		Map map = new HashMap();
		map.put("facId", facId);
		map.put("ids", dbIDs);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		int result = this.getCotBaseDao().executeUpdate(queryInfo,map);
		if(result>0){
			logger.debug("更新成功");
			return true;
		}else {
			logger.debug("更新失败");
			return false;
		}
	}
	
	//查找审核人ID
	
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCacheService#getIsMailCheck(com.sail.cot.domain.CotMail, java.lang.Integer)
	 */
	public boolean getIsMailCheck(CotMail cotMail,Integer empId){
		String empsRuleFile = MailReciveJob.class.getResource("/").getPath()+"mailrules/Rule_EMPID_CHECK_"+empId+".xml";
		File ruleFile = new File(empsRuleFile);
		if(!ruleFile.exists()) return false;//不存在邮件规则，返回
		List<MailExecuteAction> actionList = this.getMailRuleService().getRuleResult(cotMail, empsRuleFile);
		if(actionList.size()==0){
			return false;
		}else{
			CotMailEmpsRule cotMailEmpsRule =this.getMailCheckService().getDefaultCotMailEmpsRule(empId);
			if(cotMailEmpsRule==null) return false;
			List<CotMailExecute> list= this.getMailCheckService().getMailExecuteById(cotMailEmpsRule.getId());
			if(list.size()==0) return false;
			boolean flag=false;
			for(CotMailExecute cotMailExecute : list){
				int cfgId=cotMailExecute.getExecuteCfgId();
				String check=cotMailExecute.getArgs();
				if(cfgId==5 && check.equals("false")){
					flag=true;
				}
			}
			if(flag==true){
				return false;
			}else{
				for(CotMailExecute cotMailExecute : list){
					if(cotMailExecute.getExecuteCfgId()==4){
						cotMail.setCheckEmpId(Integer.parseInt(cotMailExecute.getArgs()));
						return true;
					}
				}
			}
		}
		return false;
	}
	
	MailRuleService mailRuleService;
	public MailRuleService getMailRuleService() {
		if(mailRuleService == null){
			mailRuleService = (MailRuleService)SystemUtil.getService("MailRuleService");
		}
		return mailRuleService;
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailUpdateService#updateMailByServer(java.util.List)
	 */
	public void updateMailByServer(String id){
		logger.debug("执行重新从邮件服务器上更新邮件");
//		if(id==null){
//			logger.debug("邮件ID为空，不执行");
//			return;
//		}
//		try {
//			CotMail cotMail = (CotMail) this.getCotBaseDao().getById(
//					CotMail.class, id);
//			if (cotMail != null) {
//				String uidFlag = cotMail.getErrMessage();
//				if (uidFlag != null && uidFlag.indexOf("，") != -1) {
//					MailCfgService cfgService = this.getMailCfgService();
//					logger.debug("获得邮箱配置");
//					String empStr = uidFlag.substring(0,uidFlag.indexOf("，"));
//					Integer empId = null;
//					try {
//						empId = Integer.parseInt(empStr);
//					} catch (Exception e) {
//						empId = cotMail.getEmpId();
//					}
//					CotMailCfg cotMailCfg;
//					if (empId == null) // empId为空则获得公共邮箱
//						cotMailCfg = cfgService.getCfg();
//					else
//						// 否则获得私人邮箱
//						cotMailCfg = cfgService.getEmpCfg(empId);
//					if(cotMailCfg!=null)
//						this.readMessageToLocal(cotMail.getEmpId(), cotMail.getNodeId(), cotMailCfg, cotMail);
//				}
//			}
//		} catch (Exception e) {
//		}
	}
}
