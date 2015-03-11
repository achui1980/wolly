package com.sail.cot.email.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailCfg;
import com.zhao.mail.entity.MailCfg;
import com.zhao.mail.entity.MailPerson;

public class MailEntityConverUtil {
	/**
	 * StringBuffer selHql = new StringBuffer("select obj.id,obj.custId,obj.empId,obj.nodeId,");
		selHql.append("obj.sendName,obj.sendUrl,obj.obj.toName,obj.toUrl,obj.ccName,obj.ccUrl,");
		selHql.append("obj.bccName,obj.bccUrl,obj.addTime,obj.mailStatus,obj.mailType,obj.errMessage,");
		selHql.append("obj.subject,obj.isContainAttach,obj.sendTime,obj.size,obj.mailTag");
		selHql.append(" from CotMail obj");
	 * @param obj
	 * @return
	 */
	public static CotMail toCotMail(Object[] objs){
		CotMail cotMail = new CotMail();
		cotMail.setId((String) objs[0]);
		cotMail.setCustId((Integer) objs[1]);
		cotMail.setEmpId((Integer) objs[2]);
		cotMail.setNodeId((Integer) objs[3]);
		cotMail.setSendName((String) objs[4]);
		cotMail.setSendUrl((String) objs[5]);
		cotMail.setToName((String) objs[6]);
		cotMail.setToUrl((String) objs[7]);
		cotMail.setCcName((String) objs[8]);
		cotMail.setCcUrl((String) objs[9]);
		cotMail.setBccName((String) objs[10]);
		cotMail.setBccUrl((String) objs[11]);
		cotMail.setAddTime((Date) objs[12]);
		cotMail.setMailStatus((Integer) objs[13]);
		cotMail.setMailType((Integer) objs[14]);
		cotMail.setErrMessage((String) objs[15]);
		cotMail.setSubject((String) objs[16]);
		cotMail.setIsContainAttach((Boolean) objs[17]);
		cotMail.setSendTime((Date) objs[18]);
		cotMail.setSize((Integer) objs[19]);
		cotMail.setMailTag((String) objs[20]);
		cotMail.setFacId((Integer) objs[21]);
		cotMail.setIsNotification((Boolean) objs[22]);
		return cotMail;
	}
	/**
	 * 将CotMailCfg对象转成MailCfg对象
	 * @param cotMailCfg
	 * @return
	 */
	public static MailCfg toMailCfg(CotMailCfg cotMailCfg){
		// TODO: 有使用
		if(cotMailCfg==null)
			return null;
		MailCfg mailCfg = new MailCfg();
		if(cotMailCfg.getDefaultCount()!=null) // 重发次数
			mailCfg.setAttemptLimit(cotMailCfg.getDefaultCount().intValue());
		mailCfg.setEmailAccount(cotMailCfg.getDefaultAccount()); // 邮件账号
		mailCfg.setEmailPassword(cotMailCfg.getDefaultPwd());  // 密码
		if(cotMailCfg.getDefaultPop3port()!=null)	// pop3端口
			mailCfg.setPopPort(cotMailCfg.getDefaultPop3port().intValue());
		mailCfg.setPopServerUrl(cotMailCfg.getDefaultHost());	// pop地址
		if(cotMailCfg.getDefaultIsSslpop3()==null)	// POP是否SSL
			mailCfg.setPopSSL(false);
		else
			mailCfg.setPopSSL(cotMailCfg.getDefaultIsSslpop3()!=0);
		if(cotMailCfg.getDefaultAuth()==null)// 是否验证
			mailCfg.setSmtpAuthentication(true);
		else 
			mailCfg.setSmtpAuthentication(cotMailCfg.getDefaultAuth()!=0); 
		if(cotMailCfg.getDefaultSmtpPort()!=null)	// SMTP端口
			mailCfg.setSmtpPort(cotMailCfg.getDefaultSmtpPort().intValue());
		mailCfg.setSmtpServerUrl(cotMailCfg.getDefaultHostSmtp()); // SMTP地址
		if(cotMailCfg.getDefaultIsSslsmtp()==null)	// SMTP SSL
			mailCfg.setSmtpSSL(false);
		else
			mailCfg.setSmtpSSL(cotMailCfg.getDefaultIsSslsmtp()!=0);
		if(cotMailCfg.getDefaultDebug()!=null)
			mailCfg.setDebug(cotMailCfg.getDefaultDebug().intValue()==0?false:true);
		else
			mailCfg.setDebug(false);
		return mailCfg;
	}
	public static MailCfg toMailCfg(CotEmps cotEmps){
		if(cotEmps==null)
			return null;
		MailCfg mailCfg = new MailCfg();
		mailCfg.setEmailAccount(cotEmps.getEmpsAccount());// 邮件账号
		mailCfg.setEmailPassword(cotEmps.getEmpsMailPwd());// 密码
		if(cotEmps.getEmpsPop3Port()!=null)	// pop3端口
			mailCfg.setPopPort(cotEmps.getEmpsPop3Port().intValue());
		mailCfg.setPopServerUrl(cotEmps.getEmpsMailHost());// pop地址
		if(cotEmps.getEmpsIsSSLPop3()==null)	// POP是否SSL
			mailCfg.setPopSSL(false);
		else
			mailCfg.setPopSSL(cotEmps.getEmpsIsSSLPop3()!=0);
		// TODO:
		mailCfg.setSmtpAuthentication(true);// 是否验证
		// TODO:
		if (cotEmps.getEmpsSmtpPort()!=null)
			mailCfg.setSmtpPort(cotEmps.getEmpsSmtpPort().intValue());// SMTP端口
		mailCfg.setSmtpServerUrl(cotEmps.getEmpsSmtpHost());// SMTP地址
		if(cotEmps.getEmpsIsSSLSmtp()!=null)
			mailCfg.setSmtpSSL(cotEmps.getEmpsIsSSLSmtp().intValue()==0?false:true);
		else {
			mailCfg.setSmtpSSL(false);
		}
		if(cotEmps.getEmpsIsSSLSmtp()==null)	// SMTP SSL
			mailCfg.setSmtpSSL(false);
		else
			mailCfg.setSmtpSSL(cotEmps.getEmpsIsSSLSmtp()!=0);
		
		return mailCfg;
	}
	/**
	 * 将MailCfg对象转成CotMailCfg对象
	 * @param mailCfg
	 * @return
	 */
	public static CotMailCfg toCotMailCfg(MailCfg mailCfg){
		if(mailCfg==null)
			return null;
		CotMailCfg cotMailCfg = new CotMailCfg();
		cotMailCfg.setDefaultAccount(mailCfg.getEmailAccount());
		if(mailCfg.getAttemptLimit()!=null)
			cotMailCfg.setDefaultCount((long)mailCfg.getAttemptLimit());
		int auth= mailCfg.getSmtpAuthentication()==true?1:0;
		cotMailCfg.setDefaultAuth((long)auth);
		cotMailCfg.setDefaultHost(mailCfg.getPopServerUrl());
		cotMailCfg.setDefaultHostSmtp(mailCfg.getSmtpServerUrl());
		cotMailCfg.setDefaultIsSslpop3((long)(mailCfg.getPopSSL()==true?1:0));
		cotMailCfg.setDefaultIsSslsmtp((long)(mailCfg.getSmtpSSL()==true?1:0));
		cotMailCfg.setDefaultPop3port((long)mailCfg.getPopPort());
		cotMailCfg.setDefaultPwd(mailCfg.getEmailPassword());
		cotMailCfg.setDefaultSmtpPort((long)mailCfg.getSmtpPort());
		return cotMailCfg;
	}
	/**
	 * 将cotEmps中的配置信息转成CotMailCfg对象
	 * @param cotEmps
	 * @return
	 */
	public static CotMailCfg toCotMailCfg(CotEmps cotEmps){
		// TODO: 有使用
		if(cotEmps==null)
			return null;
		CotMailCfg cotMailCfg = new CotMailCfg();
		cotMailCfg.setDefaultAccount(cotEmps.getEmpsAccount());
		cotMailCfg.setDefaultHost(cotEmps.getEmpsMailHost());
		cotMailCfg.setDefaultHostSmtp(cotEmps.getEmpsSmtpHost());
		cotMailCfg.setDefaultIsSslpop3(cotEmps.getEmpsIsSSLPop3());
		cotMailCfg.setDefaultIsSslsmtp(cotEmps.getEmpsIsSSLSmtp());
		cotMailCfg.setDefaultMintues(cotEmps.getEmpsMintues());
		cotMailCfg.setDefaultPop3port(cotEmps.getEmpsPop3Port());
		cotMailCfg.setDefaultPwd(cotEmps.getEmpsMailPwd());
		cotMailCfg.setDefaultSmtpPort(cotEmps.getEmpsSmtpPort());
		cotMailCfg.setDefaultDebug(0l);
		return cotMailCfg;
	}
	public static CotEmps toCotEmps(CotMailCfg cotMailCfg,CotEmps cotEmps){
		if(cotMailCfg==null)
			return null;
		if(cotEmps==null)
			cotEmps = new CotEmps();
		cotEmps.setEmpsAccount(cotMailCfg.getDefaultAccount());
		cotEmps.setEmpsIsSSLPop3(cotMailCfg.getDefaultIsSslpop3());
		cotEmps.setEmpsIsSSLSmtp(cotMailCfg.getDefaultIsSslsmtp());
		String mailUrl = cotMailCfg.getDefaultAccount();
		if(mailUrl.indexOf("@")==-1){
			String lastMailUrl = cotMailCfg.getDefaultHost();
			lastMailUrl = lastMailUrl.substring(lastMailUrl.indexOf(".")+1);
			cotEmps.setEmpsMail(mailUrl+"@"+lastMailUrl);
		}else
			cotEmps.setEmpsMail(mailUrl);
		cotEmps.setEmpsMailHost(cotMailCfg.getDefaultHost());
		cotEmps.setEmpsMailPwd(cotMailCfg.getDefaultPwd());
		cotEmps.setEmpsPop3Port(cotMailCfg.getDefaultPop3port());
		cotEmps.setEmpsSmtpHost(cotMailCfg.getDefaultHostSmtp());
		cotEmps.setEmpsSmtpPort(cotMailCfg.getDefaultSmtpPort());
		cotEmps.setEmpsMintues(cotMailCfg.getDefaultMintues());
		return cotEmps;
	}
	/**
	 * 根据邮件人集合对象，转成Key值为name,url的map值
	 * @param pList
	 * @return
	 */
	public static Map<String, String> toNameUrlMap(List<MailPerson> pList){
		// TODO: 有使用
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer nameBuffer = new StringBuffer();
		StringBuffer urlBuffer = new StringBuffer();
		for (MailPerson mailPerson : pList) {
			nameBuffer.append((mailPerson.getName()==null?"":mailPerson.getName())+Constants.MAIL_FIELD_DELIMITER);
			urlBuffer.append(mailPerson.getEmailUrl()+Constants.MAIL_FIELD_DELIMITER);
		}
		map.put("name", nameBuffer.toString());
		map.put("url", urlBuffer.toString());
		return map;
	}
	/**
	 * 根据邮件地址集合和名字集合转换成邮件人集合对象
	 * @param names 以Constants.MAIL_FIELD_DELIMITER 分割的邮件人姓名
	 * @param urls 以 Constants.MAIL_FIELD_DELIMITER分割的邮件地址
	 * @return
	 */
	public static List<MailPerson> toPersons(String names,String urls){
		// TODO: 有使用
		names = names==null?"":names;
		List<MailPerson> pList = new ArrayList<MailPerson>();
		if(urls!=null&&!urls.trim().equals("")){
			String[] mailNames = names.split(Constants.MAIL_FIELD_DELIMITER_REGEX);
			String[] mailUrls = urls.split(Constants.MAIL_FIELD_DELIMITER_REGEX);
			for (int i = 0; i < mailUrls.length; i++) {
				if(mailNames.length!=mailUrls.length)
					pList.add(new MailPerson("",mailUrls[i]));
				else 
					pList.add(new MailPerson(mailNames[i],mailUrls[i]));
			}
		}
		return pList;
	}
}
