package com.sail.cot.email.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMailCfg;
import com.sail.cot.email.service.MailCfgService;
import com.sail.cot.email.util.Constants;
import com.sail.cot.email.util.MailEntityConverUtil;
import com.sail.cot.email.util.MailLocalUtil;
import com.sail.cot.util.Log4WebUtil;
import com.zhao.mail.MailService;
import com.zhao.mail.entity.MailCfg;
import com.zhao.mail.impl.MailServiceDefault;
/**
 * 邮件配置
 * @author zhao
 *
 */
public class MailCfgServiceImpl implements MailCfgService{
	private CotBaseDao cotBaseDao;
	private Logger logger = Log4WebUtil.getLogger(MailCfgServiceImpl.class);
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCfgService#updateCfg(com.sail.cot.domain.CotMailCfg)
	 */
	public int updateCfg(CotMailCfg cotMailCfg){
		logger.debug("执行更新公共邮件配置方法");
		if(cotMailCfg!= null){
			MailCfg mailCfg = MailEntityConverUtil.toMailCfg(cotMailCfg);
			int result = this.connTest(mailCfg);
			if(result!=Constants.MAIL_CONNECT_NO_ERROR_STATUS)
				return result;
//			cotMailCfg.setDefaultPwd(PasswordEncrypt.encrypt(cotMailCfg.getDefaultPwd()));
			List<CotMailCfg> records = new ArrayList<CotMailCfg>();
			records.add(cotMailCfg);
			this.getCotBaseDao().updateRecords(records);
			return Constants.MAIL_CONNECT_NO_ERROR_STATUS;
		}
		return Constants.MAIL_CONNECT_ERROR_STATUS;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCfgService#updateEmpCfg(com.sail.cot.domain.CotMailCfg)
	 */
	public int updateEmpCfg(CotMailCfg cotMailCfg){
		logger.debug("执行更新个人邮件配置方法");
		if(cotMailCfg!=null){
			MailCfg mailCfg = MailEntityConverUtil.toMailCfg(cotMailCfg);
			int result = this.connTest(mailCfg);
			if(result!=Constants.MAIL_CONNECT_NO_ERROR_STATUS)
				return result;
			HttpSession session = WebContextFactory.get().getSession();
			CotEmps cotEmps = (CotEmps) session.getAttribute("emp");
			cotEmps = MailEntityConverUtil.toCotEmps(cotMailCfg, cotEmps);
//			cotEmps.setEmpsMailPwd(PasswordEncrypt.encrypt(cotEmps.getEmpsMailPwd()));
			
			List<CotEmps> records = new ArrayList<CotEmps>();
			records.add(cotEmps);
			this.getCotBaseDao().saveOrUpdateRecords(records);
			return Constants.MAIL_CONNECT_NO_ERROR_STATUS;
		}
		return Constants.MAIL_CONNECT_ERROR_STATUS;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCfgService#getCfg()
	 */
	@SuppressWarnings("unchecked")
	public CotMailCfg getCfg(){
		logger.debug("执行获得公共邮件配置方法");
		String hql = " from CotMailCfg as m";
		List<CotMailCfg> res = new ArrayList<CotMailCfg>();
		res = this.getCotBaseDao().find(hql);
		if (res.size() !=0 ) {
			CotMailCfg cotMailCfg = res.get(0);
			MailCfg mailCfg = MailEntityConverUtil.toMailCfg(cotMailCfg);
			CotMailCfg cotMailCfg2 = MailEntityConverUtil.toCotMailCfg(mailCfg);
			cotMailCfg2.setId(cotMailCfg.getId());
			cotMailCfg2.setDefaultMintues(cotMailCfg.getDefaultMintues());
//			cotMailCfg2.setDefaultPwd(PasswordEncrypt.decrypt(cotMailCfg2.getDefaultPwd()));
			return cotMailCfg2;
		}else {
			return null;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCfgService#getEmpCfg()
	 */
	public CotMailCfg getEmpCfg(Integer id) {
		logger.debug("执行获得员工邮件配置方法");
		CotEmps cotEmps = (CotEmps) this.getCotBaseDao().getById(CotEmps.class, id);
		CotMailCfg cotMailCfg = MailEntityConverUtil.toCotMailCfg(cotEmps);
//		cotMailCfg.setDefaultPwd(PasswordEncrypt.decrypt(cotMailCfg.getDefaultPwd()));
		return cotMailCfg;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailCfgService#connTest(com.achui.mail.entity.MailCfg)
	 */
	public int connTest(MailCfg mailCfg){
		logger.debug("执行测试连接方法");
		MailService mailService = new MailServiceDefault();
		mailService.setMailCfg(mailCfg);
		mailService.initConfig();
		try {
//			mailService.connectStore();
			mailService.connectTransport();
			return Constants.MAIL_CONNECT_NO_ERROR_STATUS;
		} catch (MessagingException e) {
				return MailLocalUtil.connErrInfo(e);
		}finally{
			mailService.closeAll();
		}
	}
	
	
	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}
}
